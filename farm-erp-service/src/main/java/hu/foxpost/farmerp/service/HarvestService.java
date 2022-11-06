package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.HarvestDiaryDTO;
import hu.foxpost.farmerp.db.entity.HarvestDiary;
import hu.foxpost.farmerp.db.repository.HarvestDiaryRepository;
import hu.foxpost.farmerp.interfaces.IDeliveriesService;
import hu.foxpost.farmerp.interfaces.IHarvestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class HarvestService implements IHarvestService {

    private final HarvestDiaryRepository harvestDiaryRepository;
    private final IDeliveriesService deliveriesService;

    @Override
    public BaseResponseDTO createNewDiary(HarvestDiaryDTO harvestDiaryDTO) {
        log.info("Started to create diary: {}", harvestDiaryDTO);
        try {
            HarvestDiary harvestDiaryEntity = HarvestDiary.builder()
                    .name(harvestDiaryDTO.getName())
                    .year(harvestDiaryDTO.getYear())
                    .build();

            harvestDiaryRepository.saveAndFlush(harvestDiaryEntity);

        } catch (Exception e) {
            log.error("Making new diary failed, {}", e.getMessage());
            return new BaseResponseDTO("Falied to create new diary.", 1700);
        }

        log.info("Finished to create diary: {}", harvestDiaryDTO);
        return new BaseResponseDTO("Success");
    }

    @Override
    public BaseResponseDTO deleteDiary(Integer diaryId) {
        log.info("Started to delete diary with id: {}", diaryId);
        try {
            BaseResponseDTO response = deliveriesService.deleteDiaryIdInDeliveries(diaryId);
            harvestDiaryRepository.deleteById(diaryId);

            log.info("Finished to delete diary with id : {}", diaryId);
            return  response;
        } catch (Exception e) {
            log.error("Deleting diary failed, {}", e.getMessage());
            return new BaseResponseDTO("Falied to delete diary.", 1701);
        }
    }

    @Override
    public BaseResponseDTO getAllDiary() {

        try {
            List<HarvestDiary> harvestDiaryEntityList = harvestDiaryRepository.getAllBy().orElse(null);

            if (Objects.nonNull(harvestDiaryEntityList)) {

                Collections.sort(harvestDiaryEntityList);

                return new BaseResponseDTO(harvestDiaryEntityList);
            } else {
                return new BaseResponseDTO("No page in db.", 1702);
            }

        } catch (Exception e) {
            log.error("Making new diary page failed, {}", e.getMessage());
            return new BaseResponseDTO("Failed to create new diary .", 1702);
        }

    }

    @Override
    public HarvestDiary getOneDiaryById(Integer id) throws NullPointerException {

        HarvestDiary harvestDiaryEntity = harvestDiaryRepository.getById(id);

        if (Objects.nonNull(harvestDiaryEntity)){
            return harvestDiaryEntity;
        }else{
            throw new NullPointerException();
        }
    }

}
