package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.HarvestDiaryDTO;
import hu.foxpost.farmerp.db.entity.HarvestDiaryEntity;
import hu.foxpost.farmerp.db.repository.HarvestDiaryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class HarvestService {

    private final HarvestDiaryRepository harvestDiaryRepository;
    private final DeliveriesService deliveriesService;

    public BaseResponseDTO createNewDiary(HarvestDiaryDTO harvestDiaryDTO) {

        try {
            HarvestDiaryEntity harvestDiaryEntity = HarvestDiaryEntity.builder()
                    .name(harvestDiaryDTO.getName())
                    .year(harvestDiaryDTO.getYear())
                    .build();

            harvestDiaryRepository.saveAndFlush(harvestDiaryEntity);

        } catch (Exception e) {
            log.error("Making new diary failed, {}", e.getMessage());
            return new BaseResponseDTO("Falied to create new diary.", 501);
        }

        return new BaseResponseDTO("Success");
    }

    public BaseResponseDTO deleteDiary(Integer diaryId) {

        try {
            BaseResponseDTO response = deliveriesService.deleteDiaryIdInDeliveries(diaryId);
            harvestDiaryRepository.deleteById(diaryId);
            return  response;
        } catch (Exception e) {
            log.error("Deleting diary failed, {}", e.getMessage());
            return new BaseResponseDTO("Falied to delete diary.", 502);
        }
    }


    public BaseResponseDTO getAllDiary() {

        try {
            List<HarvestDiaryEntity> harvestDiaryEntityList = harvestDiaryRepository.getAllBy().orElse(null);

            if (Objects.nonNull(harvestDiaryEntityList)) {

                Collections.sort(harvestDiaryEntityList);

                return new BaseResponseDTO(harvestDiaryEntityList);
            } else {
                return new BaseResponseDTO("No page in db.", 503);
            }

        } catch (Exception e) {
            log.error("Making new diary page failed, {}", e.getMessage());
            return new BaseResponseDTO("Falied to crreate new diary .", 504);
        }

    }

    public HarvestDiaryEntity getOneDiaryById(Integer id) throws NullPointerException {

        HarvestDiaryEntity harvestDiaryEntity = harvestDiaryRepository.getById(id);

        if (Objects.nonNull(harvestDiaryEntity)){
            return harvestDiaryEntity;
        }else{
            throw new NullPointerException();
        }
    }

}
