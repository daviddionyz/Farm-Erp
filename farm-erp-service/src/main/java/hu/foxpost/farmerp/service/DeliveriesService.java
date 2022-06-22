package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.DeliveriesEntity;
import hu.foxpost.farmerp.db.entity.FieldEntity;
import hu.foxpost.farmerp.db.entity.StorageEntity;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.DeliveriesDTO;
import hu.foxpost.farmerp.dto.StorageDTO;
import hu.foxpost.farmerp.db.repository.DeliveriesRepository;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import hu.foxpost.farmerp.utils.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class DeliveriesService {

    private final DeliveriesRepository deliveriesRepository;

    private StorageService storageService;
    private CropsService cropsService;

    public BaseResponseDTO getAllDeliveriesByDiary(Integer diaryId,
                                                   String search,
                                                   Integer page,
                                                   Integer pageSize
    ) {
        try {
            List<DeliveriesEntity> deliveriesEntityList = deliveriesRepository.getAllByDiaryId(diaryId).orElse(null);
            List<DeliveriesEntity> response = new ArrayList<>();

            if (Objects.nonNull(deliveriesEntityList) && !search.equals("none")) {
                List<DeliveriesEntity> finalResponse = response;
                deliveriesEntityList.stream().forEach(delivery -> {
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
            return new BaseResponseDTO("Getting data failed", 501);
        }
    }

    public BaseResponseDTO intakeDelivery(DeliveriesDTO deliveriesDTO) {
        log.info("{}", deliveriesDTO);
        BaseResponseDTO response = null;

        try {

            if (Objects.nonNull(deliveriesDTO.getFromStorage())){
                StorageEntity storageEntity = deliveriesDTO.getFromStorage();

                Integer newFullness = storageEntity.getFullness() - deliveriesDTO.getNet();

                cropsService.minusCorpsInStorage(deliveriesDTO.getFromStorage().getId(),
                        FieldEntity.builder().corpName(deliveriesDTO.getCrop().getCropsName())
                                .corpType(deliveriesDTO.getCrop().getCropsType()).build()
                        ,deliveriesDTO.getNet());

                storageService.updateStorage(StorageDTO.builder()
                        .id(storageEntity.getId())
                        .name(storageEntity.getName())
                        .capacity(storageEntity.getCapacity())
                        .fullness(newFullness <= 0 ? 0 : newFullness)
                        .build());
            }

            if (Objects.nonNull(deliveriesDTO.getWhere())) {
                // beirni a raktarba a fuvar mennyiseget, kapacitas ellenorzes ?
                StorageEntity storageEntity = storageService.getOneStorageById(deliveriesDTO.getWhere().getId());

                if (storageEntity.getCapacity() < storageEntity.getFullness() + deliveriesDTO.getNet()) {
                    response = new BaseResponseDTO("Mentés sikeres, de fuvar mértéke meghaladja a raktár kapacitását.", 502);
                }
                if(Objects.nonNull(deliveriesDTO.getFrom())){
                    cropsService.saveCrops(deliveriesDTO.getWhere().getId(),deliveriesDTO.getFrom(),deliveriesDTO.getNet());
                }else{
                    cropsService.saveCrops(deliveriesDTO.getWhere().getId(),
                            FieldEntity.builder().corpName(deliveriesDTO.getCrop().getCropsName())
                                    .corpType(deliveriesDTO.getCrop().getCropsType()).build()
                            ,deliveriesDTO.getNet());
                }

                storageService.updateStorage(StorageDTO.builder()
                        .id(storageEntity.getId())
                        .name(storageEntity.getName())
                        .capacity(storageEntity.getCapacity())
                        .fullness(storageEntity.getFullness() + deliveriesDTO.getNet())
                        .build());
            }

            deliveriesRepository.saveAndFlush(DeliveriesEntity.builder()
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
                    .build());

            log.info("Delivery save finished");
            return Objects.nonNull(response) ? response : new BaseResponseDTO("Success");
        } catch (Exception e) {
            log.error("Delivery save failed, {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 503);
        }

    }

    public BaseResponseDTO updateDelivery(DeliveriesDTO deliveriesDTO) {
        try {

            DeliveriesEntity oldDelivery = deliveriesRepository.getById(deliveriesDTO.getId());

            if (Objects.nonNull(deliveriesDTO.getFromStorage())){
                Integer diff        = oldDelivery.getNet() - deliveriesDTO.getNet();
                Integer newFullness = deliveriesDTO.getFromStorage().getFullness() + diff;

                cropsService.saveCrops(deliveriesDTO.getFromStorage().getId(),
                        FieldEntity.builder().corpName(deliveriesDTO.getCrop().getCropsName())
                                .corpType(deliveriesDTO.getCrop().getCropsType()).build()
                        ,diff);

                storageService.updateStorage(StorageDTO.builder()
                        .id(deliveriesDTO.getFromStorage().getId())
                        .name(deliveriesDTO.getFromStorage().getName())
                        .capacity(deliveriesDTO.getFromStorage().getCapacity())
                        .fullness(newFullness < 0 ? 0 : newFullness)
                        .build());
            }

            if (Objects.nonNull(deliveriesDTO.getWhere())){
                Integer diff        = oldDelivery.getNet() - deliveriesDTO.getNet();
                Integer newFullness = deliveriesDTO.getWhere().getFullness() - diff;

                cropsService.minusCorpsInStorage(deliveriesDTO.getWhere().getId(),
                        FieldEntity.builder().corpName(deliveriesDTO.getCrop().getCropsName())
                                .corpType(deliveriesDTO.getCrop().getCropsType()).build()
                        ,diff);

                storageService.updateStorage(StorageDTO.builder()
                        .id(deliveriesDTO.getWhere().getId())
                        .name(deliveriesDTO.getWhere().getName())
                        .capacity(deliveriesDTO.getWhere().getCapacity())
                        .fullness(newFullness < 0 ? 0 : newFullness)
                        .build());
            }

            DeliveriesEntity deliveriesEntity = DeliveriesEntity.builder()
                    .id(deliveriesDTO.getId())
                    .diaryId(deliveriesDTO.getDiaryId())
                    .worker(deliveriesDTO.getWorker())
                    .vehicle(deliveriesDTO.getVehicle())
                    .gross(deliveriesDTO.getGross())
                    .empty(deliveriesDTO.getEmpty())
                    .net(deliveriesDTO.getNet())
                    .intakeDate(CommonUtil.getSimpleDateFormat().parse(deliveriesDTO.getIntakeDate()))
                    .from(deliveriesDTO.getFrom())
                    .fromStorage(deliveriesDTO.getFromStorage())
                    .where(deliveriesDTO.getWhere())
                    .isCorpMoving(deliveriesDTO.getIsCorpMoving())
                    .build();

            deliveriesRepository.saveAndFlush(deliveriesEntity);

            return new BaseResponseDTO("success");
        } catch (Exception e) {
            log.error("Update delivery failed: {}", e.getMessage());
            return new BaseResponseDTO("update failed", 501);
        }
    }

    public BaseResponseDTO deleteDelivery(Integer deliveryId) {
        try {

            DeliveriesEntity delivery = deliveriesRepository.getById(deliveryId);

            if(Objects.nonNull(delivery.getFromStorage())){
                Integer newFullness = delivery.getFromStorage().getFullness() + delivery.getNet();

                try{
                    cropsService.saveCrops(delivery.getFromStorage().getId(),delivery.getFrom(),delivery.getNet());
                }catch (Exception e){

                }


                storageService.updateStorage(StorageDTO.builder()
                        .id(delivery.getFromStorage().getId())
                        .name(delivery.getFromStorage().getName())
                        .capacity(delivery.getFromStorage().getCapacity())
                        .fullness(newFullness)
                        .build());
            }

            if (Objects.nonNull(delivery.getWhere())) {

                Integer newFullness = delivery.getWhere().getFullness() - delivery.getNet();

                if(Objects.nonNull(delivery.getFrom()))
                    cropsService.minusCorpsInStorage(delivery.getWhere().getId(),delivery.getFrom(),delivery.getNet());

                storageService.updateStorage(StorageDTO.builder()
                        .id(delivery.getWhere().getId())
                        .name(delivery.getWhere().getName())
                        .capacity(delivery.getWhere().getCapacity())
                        .fullness(newFullness <= 0 ? 0 : newFullness).build());
            }

            deliveriesRepository.deleteById(deliveryId);
            return new BaseResponseDTO("success");
        } catch (Exception e) {
            log.error("Delete delivery failed");
            return new BaseResponseDTO("Delete failed", 501);
        }
    }

    @Transactional
    public BaseResponseDTO deleteDiaryIdInDeliveries(Integer diaryId) {

        try {
            List<DeliveriesEntity> deliveries = deliveriesRepository.getAllByDiaryId(diaryId).orElse(null);

            deliveries.forEach(delivery -> {
                delivery.setDiaryId(null);
                deliveriesRepository.saveAndFlush(delivery);
            });

            return new BaseResponseDTO("success");
        } catch (Exception e) {
            log.error("Soft delete failed diaryId : {}", diaryId);
            return new BaseResponseDTO("Soft delete failed", 501);
        }
    }
}
