package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.CropsEntity;
import hu.foxpost.farmerp.db.entity.FieldEntity;
import hu.foxpost.farmerp.db.repository.CropsRepository;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@AllArgsConstructor
public class CropsService {

    private final CropsRepository cropsRepository;

    public BaseResponseDTO getCropsByStorageId(Integer storageId){
        try{
            if (storageId == -1){
                return new BaseResponseDTO("This storage does not exist",501);
            }

            List<CropsEntity> crops = cropsRepository.findAllByStorageId(storageId);

            return new BaseResponseDTO(crops);
        }catch (Exception e){
            log.error("Getting crops data failed: {}",storageId);
            return new BaseResponseDTO("Getting crops data failed",501);
        }
    }

    public BaseResponseDTO saveCrops(Integer storageId, FieldEntity fieldEntity, Integer amount){
        try{
            List<CropsEntity> cropsEntityByStorage = (List<CropsEntity>) getCropsByStorageId(storageId).getData();

            AtomicBoolean isExist = new AtomicBoolean(false);

            cropsEntityByStorage.forEach(member -> {
                if (member.getCropsName().equals(fieldEntity.getCorpName()) && member.getCropsType().equals(fieldEntity.getCorpType())){
                    member.setAmount(member.getAmount()+amount);
                    cropsRepository.saveAndFlush(member);
                    isExist.set(true);
                }
            });

            if (!isExist.get()){
                CropsEntity crop = CropsEntity.builder()
                        .storageId(storageId)
                        .cropsName(fieldEntity.getCorpName())
                        .cropsType(fieldEntity.getCorpType())
                        .amount(amount)
                        .build();
                cropsRepository.saveAndFlush(crop);
            }

            return new BaseResponseDTO("success");
        }catch (Exception e){
            log.error("Getting crops save failed: {}",storageId);
            return new BaseResponseDTO("Getting crops save failed",502);
        }
    }

    public BaseResponseDTO minusCorpsInStorage(Integer storageId, FieldEntity fieldEntity, Integer amount){
        try{
            List<CropsEntity> cropsEntityByStorage = (List<CropsEntity>) getCropsByStorageId(storageId).getData();

            AtomicBoolean isExist = new AtomicBoolean(false);

            cropsEntityByStorage.forEach(member -> {
                if (member.getCropsName().equals(fieldEntity.getCorpName()) && member.getCropsType().equals(fieldEntity.getCorpType())){
                    if (member.getAmount() - amount <= 0){
                        cropsRepository.deleteById(member.getId());
                    }else{
                        member.setAmount(member.getAmount()-amount);
                        cropsRepository.saveAndFlush(member);
                    }
                    isExist.set(true);


                }
            });

            if (!isExist.get()){
                return new BaseResponseDTO("Minus the amount failed",503);
            }

            return new BaseResponseDTO("success");
        }catch (Exception e){
            log.error("Getting crops save failed: {}",storageId);
            return new BaseResponseDTO("Getting crops save failed",502);
        }
    }

}
