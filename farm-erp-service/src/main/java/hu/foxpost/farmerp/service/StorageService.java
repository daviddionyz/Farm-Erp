package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.StorageEntity;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.StorageDTO;
import hu.foxpost.farmerp.db.repository.StorageRepository;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;

    public BaseResponseDTO getAllStorage(
            String name,
            Integer page,
            Integer pageSize
    ) {
        try {
            List<StorageEntity> storageEntity = storageRepository.getAllStorageWithPageData(name, page * pageSize, pageSize);
            Integer searchNum = storageRepository.getAllStorageWithoutPageData(name);
            return new BaseResponseDTO(new PageResponseDTO(searchNum, Collections.singletonList(storageEntity), page, pageSize));

        } catch (Exception e) {
            log.error("Get all field failed, {}", e.getMessage());
            return new BaseResponseDTO("No field found", 501);
        }
    }

    public BaseResponseDTO getAllStorage() {
        try {
            List<StorageEntity> storageEntity = storageRepository.findAll();

            return new BaseResponseDTO(storageEntity);

        } catch (Exception e) {
            log.error("Get all field failed, {}", e.getMessage());
            return new BaseResponseDTO("No field found", 501);
        }
    }

    public StorageEntity getOneStorageById(Integer id) throws NullPointerException {

        StorageEntity storageEntity = storageRepository.getStorageById(id).orElse(null);

        if (Objects.nonNull(storageEntity)){
            return storageEntity;
        }else{
            throw new NullPointerException();
        }
    }

    public BaseResponseDTO deleteStorage(Integer storageId) {

        try {
            storageRepository.deleteById(storageId);
        } catch (Exception e) {
            log.error("Delete failed : {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 502);
        }

        return new BaseResponseDTO("Delete success");
    }

    public BaseResponseDTO addNewStorage(StorageDTO storage) {

        try {
            StorageEntity newStorageEntity = StorageEntity.builder()
                    .name(storage.getName())
                    .capacity(storage.getCapacity())
                    .fullness(storage.getFullness())
                    .build();

            storageRepository.saveAndFlush(newStorageEntity);
        } catch (Exception e) {
            log.error("Save failed : {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 503);
        }

        return new BaseResponseDTO("Save success");
    }

    public BaseResponseDTO updateStorage(StorageDTO storage) {
        log.info("Storage update started, input data: {}", storage);


        try  {
            StorageEntity oldStorageEntity = storageRepository.getById(storage.getId());
            log.info("Storage form db : {}", oldStorageEntity);
            if (Objects.isNull(oldStorageEntity.getName()) || !oldStorageEntity.getName().equals(storage.getName())) {
                oldStorageEntity.setName(storage.getName());
            }

            if (Objects.isNull(oldStorageEntity.getCapacity()) || !oldStorageEntity.getCapacity().equals(storage.getCapacity())) {
                oldStorageEntity.setCapacity(storage.getCapacity());
            }

            if (Objects.isNull(oldStorageEntity.getFullness()) || !oldStorageEntity.getFullness().equals(storage.getFullness())) {
                oldStorageEntity.setFullness(storage.getFullness());
            }

            storageRepository.saveAndFlush(oldStorageEntity);
        } catch (Exception e) {
            log.error("Update failed : {}", e.getMessage());
            return new BaseResponseDTO("Update failed", 504);
        }

        log.info("Storage update finished: {}", storage.getId());
        return new BaseResponseDTO("Update success");
    }
}
