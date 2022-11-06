package hu.foxpost.farmerp.interfaces;

import hu.foxpost.farmerp.db.entity.HarvestDiary;
import hu.foxpost.farmerp.dto.HarvestDiaryDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

public interface IHarvestService {

    BaseResponseDTO createNewDiary(HarvestDiaryDTO harvestDiaryDTO);
    BaseResponseDTO deleteDiary(Integer diaryId);
    BaseResponseDTO getAllDiary();
    HarvestDiary getOneDiaryById(Integer id);
}
