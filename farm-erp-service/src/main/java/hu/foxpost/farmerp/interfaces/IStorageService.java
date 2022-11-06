package hu.foxpost.farmerp.interfaces;

import hu.foxpost.farmerp.db.entity.Storage;
import hu.foxpost.farmerp.dto.StorageDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

public interface IStorageService {

    BaseResponseDTO getAllStorage(
            String name,
            Integer page,
            Integer pageSize
    );

    BaseResponseDTO getAllStorage();

    BaseResponseDTO deleteStorage(Integer storageId);

    BaseResponseDTO addNewStorage(StorageDTO storage);

    BaseResponseDTO updateStorage(StorageDTO storage);

    BaseResponseDTO getCropsForStorage(Integer storageId);

    Storage getOneStorageById(Integer id) throws NullPointerException;
}
