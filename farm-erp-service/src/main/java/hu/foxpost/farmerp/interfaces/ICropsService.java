package hu.foxpost.farmerp.interfaces;

import hu.foxpost.farmerp.db.entity.Field;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

public interface ICropsService {

    BaseResponseDTO getCropsByStorageId(Integer storageId);
    BaseResponseDTO saveCrops(Integer storageId, String cropName, String cropType, Integer amount);
    BaseResponseDTO minusCorpsInStorage(Integer storageId, String cropName, String cropType, Integer amount);
}
