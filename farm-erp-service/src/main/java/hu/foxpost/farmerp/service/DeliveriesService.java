package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.Delivery;
import hu.foxpost.farmerp.db.entity.Field;
import hu.foxpost.farmerp.db.entity.Storage;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.DeliveryDTO;
import hu.foxpost.farmerp.dto.StorageDTO;
import hu.foxpost.farmerp.db.repository.DeliveryRepository;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import hu.foxpost.farmerp.interfaces.IDeliveriesService;
import hu.foxpost.farmerp.utils.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class DeliveriesService implements IDeliveriesService {

    private final DeliveryRepository deliveriesRepository;

    private StorageService storageService;
    private CropsService cropsService;

    @Override
    public BaseResponseDTO getAllDeliveriesByDiary(Integer diaryId,
                                                   String search,
                                                   Integer page,
                                                   Integer pageSize) {
        try {
            List<Delivery> deliveriesEntityList = deliveriesRepository.getAllByDiaryId(diaryId);

            List<Delivery> response;
            if (deliveriesEntityList.size() > 0 && !search.equals("none")) {
                List<Delivery> finalResponse = new ArrayList<>();
                deliveriesEntityList.forEach(delivery -> {
                    if (delivery.getWorker().getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)) ||
                            delivery.getVehicle().getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)) ||
                            delivery.getFrom().getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)) ||
                            delivery.getWhere().getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
                        finalResponse.add(delivery);
                    }
                });
                response = finalResponse;
            } else {
                response = deliveriesEntityList;
            }

            Collections.sort(response);
            Integer listSize = response.size();

            try {
                response = response.subList(page * pageSize, Math.min(page * pageSize + pageSize, response.size()));
            } catch (Exception e) {
                log.error("Making sub list failed: {}", e.getMessage());
            }

            return new BaseResponseDTO(new PageResponseDTO(listSize, Collections.singletonList(response), page, pageSize));

        } catch (Exception e) {
            log.error("Getting all deliveries for this diary {} failed , {}", diaryId, e.getMessage());
            return new BaseResponseDTO("Getting data failed", 1500);
        }
    }

    @Override
    public BaseResponseDTO getAllDeliveriesByDate(LocalDate from,
                                                  LocalDate to,
                                                  Integer page,
                                                  Integer pageSize) {
        List<Delivery> response = new ArrayList<>();

        Collections.sort(response);
        Integer listSize = response.size();

        try {
            response = response.subList(page * pageSize, Math.min(page * pageSize + pageSize, response.size()));
        } catch (Exception e) {
            log.error("Making sub list failed: {}", e.getMessage());
        }

        return new BaseResponseDTO(new PageResponseDTO(listSize, Collections.singletonList(response), page, pageSize));
    }

    @Override
    public BaseResponseDTO intakeDelivery(DeliveryDTO deliveriesDTO) {
        log.info("Started save delivery: {}", deliveriesDTO);
        BaseResponseDTO response = null;

        try {
            if (Objects.nonNull(deliveriesDTO.getFromStorage()) && Objects.nonNull(deliveriesDTO.getFromStorage().getId())) {
                Storage fromStorage = deliveriesDTO.getFromStorage();

                int newFullness = fromStorage.getFullness() - deliveriesDTO.getNet();

                cropsService.minusCorpsInStorage(deliveriesDTO.getFromStorage().getId(),
                        deliveriesDTO.getCrop().getCropName(),
                        deliveriesDTO.getCrop().getCropType(),
                        deliveriesDTO.getNet());

                storageService.updateStorage(StorageDTO.builder()
                        .id(fromStorage.getId())
                        .name(fromStorage.getName())
                        .capacity(fromStorage.getCapacity())
                        .fullness(Math.max(newFullness, 0))
                        .build());
            }

            if (Objects.nonNull(deliveriesDTO.getWhere()) && Objects.nonNull(deliveriesDTO.getWhere().getId() )) {
                // beirni a raktarba a fuvar mennyiseget, kapacitas ellenorzes ?
                Storage storageEntity = storageService.getOneStorageById(deliveriesDTO.getWhere().getId());

                if (storageEntity.getCapacity() < storageEntity.getFullness() + deliveriesDTO.getNet()) {
                    response = new BaseResponseDTO("Mentés sikeres, de fuvar mértéke meghaladja a raktár kapacitását.", 1501);
                }

                if (Objects.nonNull(deliveriesDTO.getFrom())) {
                    cropsService.saveCrops(deliveriesDTO.getWhere().getId(),
                            deliveriesDTO.getCrop().getCropName(),
                            deliveriesDTO.getCrop().getCropType(),
                            deliveriesDTO.getNet());
                } else {
                    cropsService.saveCrops(deliveriesDTO.getWhere().getId(),
                            deliveriesDTO.getCrop().getCropName(),
                            deliveriesDTO.getCrop().getCropType(),
                            deliveriesDTO.getNet());
                }

                storageService.updateStorage(StorageDTO.builder()
                        .id(storageEntity.getId())
                        .name(storageEntity.getName())
                        .capacity(storageEntity.getCapacity())
                        .fullness(storageEntity.getFullness() + deliveriesDTO.getNet())
                        .build());
            }

            deliveriesRepository.saveAndFlush(Delivery.builder()
                    .diaryId(deliveriesDTO.getDiaryId())
                    .gross(deliveriesDTO.getGross())
                    .empty(deliveriesDTO.getEmpty())
                    .net(deliveriesDTO.getNet())
                    .worker(deliveriesDTO.getWorker())
                    .vehicle(deliveriesDTO.getVehicle())
                    .intakeDate(CommonUtil.getSimpleDateFormat().parse(deliveriesDTO.getIntakeDate()))
                    .from(deliveriesDTO.getFrom())
                    .where(deliveriesDTO.getWhere())
                    .fromStorage(deliveriesDTO.getFromStorage())
                    .isCorpMoving(deliveriesDTO.getIsCorpMoving())
                    .cropName(deliveriesDTO.getCrop().getCropName())
                    .cropType(deliveriesDTO.getCrop().getCropType())
                    .build());

            log.info("Delivery save finished");
            return Objects.nonNull(response) ? response : new BaseResponseDTO("Success");
        } catch (Exception e) {
            log.error("Delivery save failed, {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 1502);
        }
    }

    @Override
    public BaseResponseDTO updateDelivery(DeliveryDTO newDelivery) {
        log.info("Started to update delivery: {}", newDelivery);
        try {
            Delivery oldDelivery = deliveriesRepository.getById(newDelivery.getId());

            // TODO: input valtas van kezelni kell az atirast
            //       vagy hogy hova kerul azt is lekezelni ha valtozik ki kell torolni a crop tablabol és raktarbol es atirni az ujba vagy ha kintre ment akkor csak eltorolni

            /** From          change for what       Where       change for what
             *  At Move
             *
             *  Storage   ->  Storage v out         Storage -> storage v out
             *
             *  out       -> Storage                out     -> storage
             *
             *  ----------------------------------------------------------------------
             *
             *  From            change for what     where       change for what
             *  At Harvest
             *  Field     -> Field           Storage -> Storage v out
             *
             *                                out    -> Storage
             */

            deleteDelivery(oldDelivery.getId());
            intakeDelivery(newDelivery);

            if (newDelivery.getIsCorpMoving()){
                // TODO : save if field or storage kulso helyszin

            }else {
            }


            log.info("Finished updating delivery: {}", newDelivery.getId());
            return new BaseResponseDTO("success");
        } catch (Exception e) {
            log.error("Update delivery failed: {}", e.getMessage());
            return new BaseResponseDTO("update failed", 1502);
        }
    }

    @Override
    public BaseResponseDTO deleteDelivery(Integer deliveryId) {
        log.info("Started to delete delivery with id : {}", deliveryId);
        try {
            Delivery delivery = deliveriesRepository.getById(deliveryId);

            if (Objects.nonNull(delivery.getFromStorage())) {
                int newFullness = Math.max(delivery.getFromStorage().getFullness() + delivery.getNet(), 0);

                // vissza hozza kell adni a mennyiseget ami a delivery altal el lett belole vive
                cropsService.saveCrops(delivery.getFromStorage().getId(),
                        delivery.getCropName(),
                        delivery.getCropType(),
                        delivery.getNet());

                storageService.updateStorage(StorageDTO.builder()
                        .id(delivery.getFromStorage().getId())
                        .name(delivery.getFromStorage().getName())
                        .capacity(delivery.getFromStorage().getCapacity())
                        .fullness(newFullness)
                        .build());

            } else if (Objects.nonNull(delivery.getWhere())) {

                int newFullness = Math.max(delivery.getWhere().getFullness() - delivery.getNet(), 0);

                if (Objects.nonNull(delivery.getFrom()))
                    cropsService.minusCorpsInStorage(delivery.getWhere().getId(),
                            delivery.getCropName(),
                            delivery.getCropType(),
                            delivery.getNet());

                storageService.updateStorage(StorageDTO.builder()
                        .id(delivery.getWhere().getId())
                        .name(delivery.getWhere().getName())
                        .capacity(delivery.getWhere().getCapacity())
                        .fullness(newFullness).build());
            }

            deliveriesRepository.deleteById(deliveryId);

            log.info("Finished deleting delivery with id: {}", deliveryId);
            return new BaseResponseDTO("success");
        } catch (Exception e) {
            log.error("Delete delivery failed, {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 1503);
        }
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteDiaryIdInDeliveries(Integer diaryId) {
        log.info("Started to delete diary id in deliveries diary id : {}", diaryId);
        try {
            List<Delivery> deliveries = deliveriesRepository.getAllByDiaryId(diaryId);

            deliveries.forEach(delivery -> {
                delivery.setDiaryId(null);
                deliveriesRepository.saveAndFlush(delivery);
            });

            log.info("Finished deleting diary ids in deliveries");
            return new BaseResponseDTO("success");
        } catch (Exception e) {
            log.error("Soft delete failed diaryId : {} , {}", diaryId, e.getMessage());
            return new BaseResponseDTO("Soft delete failed", 1503);
        }
    }
}
