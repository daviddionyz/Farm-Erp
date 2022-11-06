package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.Crop;
import hu.foxpost.farmerp.db.entity.Field;
import hu.foxpost.farmerp.db.repository.CropRepository;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.interfaces.ICropsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@AllArgsConstructor
public class CropsService implements ICropsService {

    private final CropRepository cropsRepository;

    public BaseResponseDTO getCropsByStorageId(Integer storageId) {
        try {
            List<Crop> crops = cropsRepository.findAllByStorageId(storageId);

            return new BaseResponseDTO(crops);
        } catch (Exception e) {
            log.error("Getting crops data failed: {}", storageId);
            return new BaseResponseDTO("Getting crops data failed", 1800);
        }
    }

    public BaseResponseDTO saveCrops(Integer storageId, String cropName, String cropType, Integer amount) {
        log.info("Started to save crops data, storage: {}, crop: {} - {}, amount: {}",storageId,cropName,cropType,amount);
        try {
            List<Crop> cropsEntityByStorage = (List<Crop>) getCropsByStorageId(storageId).getData();

            AtomicBoolean isExist = new AtomicBoolean(false);

            cropsEntityByStorage.forEach(member -> {
                if (member.getCropName().equals(cropName) && member.getCropType().equals(cropType)) {
                    member.setAmount(member.getAmount() + amount);
                    cropsRepository.saveAndFlush(member);
                    isExist.set(true);
                }
            });

            if (!isExist.get()) {
                Crop crop = Crop.builder()
                        .storageId(storageId)
                        .cropName(cropName)
                        .cropType(cropType)
                        .amount(amount)
                        .build();
                cropsRepository.saveAndFlush(crop);
            }

            log.info("Finished saving crops data");
            return new BaseResponseDTO("success");
        } catch (Exception e) {
            log.error("Crops data save failed in save method: {}", storageId);
            return new BaseResponseDTO("Crops data save failed in save method!", 1801);
        }
    }

    public BaseResponseDTO minusCorpsInStorage(Integer storageId, String cropName, String cropType, Integer amount) {
        log.info("Started to extract crops data for storage: {}, crop: {} - {}, amount: {}", storageId, cropName, cropType, amount);
        try {
            List<Crop> cropsEntitiesByStorage = (List<Crop>) getCropsByStorageId(storageId).getData();

            cropsEntitiesByStorage.forEach(crop -> {
                if (crop.getCropName().equals(cropName) && crop.getCropType().equals(cropType)) {
                    if (crop.getAmount() - amount <= 0) {
                        cropsRepository.deleteById(crop.getId());
                    } else {
                        crop.setAmount(crop.getAmount() - amount);
                        cropsRepository.saveAndFlush(crop);
                    }
                }
            });

            log.info("Finished extracting crops data");
            return new BaseResponseDTO("success");
        } catch (Exception e) {
            log.error("Crops data save failed in minus method: {}", storageId);
            return new BaseResponseDTO("Crops data save failed in minus method!", 1801);
        }
    }

}
