package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.Storage;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.StorageDTO;
import hu.foxpost.farmerp.db.repository.StorageRepository;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import hu.foxpost.farmerp.interfaces.IStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class StorageService implements IStorageService {

    private final CropsService cropsService;
    private final StorageRepository storageRepository;

    @Override
    public BaseResponseDTO getAllStorage(
            String name,
            Integer page,
            Integer pageSize
    ) {
        try {
            List<Storage> storageEntity = storageRepository.getAllStorageWithPageData(name, page * pageSize, pageSize);
            Integer searchNum = storageRepository.getAllStorageNumWithoutPageData(name);
            return new BaseResponseDTO(new PageResponseDTO(searchNum, Collections.singletonList(storageEntity), page, pageSize));

        } catch (Exception e) {
            log.error("Getting storages failed, {}", e.getMessage());
            return new BaseResponseDTO("Storage not found!", 1200);
        }
    }

    @Override
    public BaseResponseDTO getAllStorage() {
        try {
            List<Storage> storageEntity = storageRepository.findAllByIsDeleted(false);

            return new BaseResponseDTO(storageEntity);

        } catch (Exception e) {
            log.error("Getting storages failed, {}", e.getMessage());
            return new BaseResponseDTO("Storage not found!", 1200);
        }
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteStorage(Integer storageId) {
        log.info("Storage delete started for id : {}", storageId);
        try {
            Storage storage = getOneStorageById(storageId);

            storage.setIsDeleted(true);

            storageRepository.saveAndFlush(storage);

        } catch (Exception e) {
            log.error("Delete failed : {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 1201);
        }

        log.info("Storage delete finished successfully!");
        return new BaseResponseDTO("Delete success!");
    }

    @Override
    public BaseResponseDTO addNewStorage(StorageDTO storage) {
        log.info("Storage create started: {}", storage);

        try {
            Storage newStorageEntity = Storage.builder()
                    .name(storage.getName())
                    .capacity(storage.getCapacity())
                    .fullness(storage.getFullness())
                    .isDeleted(false)
                    .build();

            storageRepository.saveAndFlush(newStorageEntity);
        } catch (Exception e) {
            log.error("Save failed : {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 1202);
        }

        log.info("Storage create finished successfully!");
        return new BaseResponseDTO("Save success!");
    }

    @Override
    @Transactional
    public BaseResponseDTO updateStorage(StorageDTO storage) {
        log.info("Storage update started, input data: {}", storage);

        try  {
            Storage oldStorageEntity = storageRepository.getById(storage.getId());
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
            return new BaseResponseDTO("Update failed!", 1203);
        }

        log.info("Storage update finished: {}", storage.getId());
        return new BaseResponseDTO("Update success!");
    }

    @Override
    public BaseResponseDTO getCropsForStorage(Integer storageId){

        if (!storageRepository.existsStorageById(storageId)){
            return new BaseResponseDTO("Storage not found!", 1200);
        }

        return cropsService.getCropsByStorageId(storageId);
    }

    @Override
    public Storage getOneStorageById(Integer id) throws NullPointerException {

        Storage storageEntity = storageRepository.getStorageById(id).orElse(null);

        if (Objects.nonNull(storageEntity)){
            return storageEntity;
        }else{
            throw new NullPointerException();
        }
    }
}
