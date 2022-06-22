package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.FieldEntity;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.FieldDTO;
import hu.foxpost.farmerp.db.repository.FieldRepository;
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
public class FieldService {

    private final FieldRepository fieldRepository;

    public BaseResponseDTO getAllField(
            String name,
            String corpType,
            String corpName,
            Integer page,
            Integer pageSize
    ) {
        try {
            List<FieldEntity> fieldEntities = fieldRepository.getAllFieldWithPageData(name,  corpType,corpName, page * pageSize, pageSize);
            Integer searchNum = fieldRepository.getAllFieldWithoutPageData(name,  corpType,corpName);
            return new BaseResponseDTO(new PageResponseDTO(searchNum, Collections.singletonList(fieldEntities), page, pageSize));

        } catch (Exception e) {
            log.error("Get all field failed, {}", e.getMessage());
            return new BaseResponseDTO("No field found", 501);
        }

    }

    public BaseResponseDTO getAllField() {
        try {
            List<FieldEntity> fieldEntities = fieldRepository.findAllBy().orElse(null);
            return new BaseResponseDTO(fieldEntities);

        } catch (Exception e) {
            log.error("Get all field failed, {}", e.getMessage());
            return new BaseResponseDTO("No field found", 501);
        }

    }

    public FieldEntity getOneFieldById(Integer id) throws NullPointerException {

        FieldEntity fieldEntity = fieldRepository.getFieldById(id).orElse(null);

        if (Objects.nonNull(fieldEntity)) {
            return fieldEntity;
        } else {
            throw new NullPointerException();
        }
    }

    public BaseResponseDTO deleteField(Integer fieldId) {
        try {
            fieldRepository.deleteById(fieldId);
        } catch (Exception e) {
            log.error("Delete failed : {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 502);
        }

        return new BaseResponseDTO("Delete success");
    }

    public BaseResponseDTO addNewField(FieldDTO field) {
        try {
            FieldEntity newFieldEntity = FieldEntity.builder()
                    .name(field.getName())
                    .size(field.getSize())
                    .corpType(field.getCorpType())
                    .corpName(field.getCorpName())
                    .build();

            fieldRepository.saveAndFlush(newFieldEntity);
        } catch (Exception e) {
            log.error("Save failed : {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 503);
        }

        return new BaseResponseDTO("Save success");
    }

    public BaseResponseDTO updateField(FieldDTO field) {
        System.out.println(field);
        try {
            FieldEntity oldFieldEntity = fieldRepository.getById(field.getId());

            if (Objects.isNull(oldFieldEntity.getName()) || !oldFieldEntity.getName().equals(field.getName())) {
                oldFieldEntity.setName(field.getName());
            }

            if (Objects.isNull(oldFieldEntity.getSize()) || !oldFieldEntity.getSize().equals(field.getSize())) {
                oldFieldEntity.setSize(field.getSize());
            }

            if (Objects.isNull(oldFieldEntity.getCorpType()) || !oldFieldEntity.getCorpType().equals(field.getCorpType())) {
                oldFieldEntity.setCorpType(field.getCorpType());
            }

            if (Objects.isNull(oldFieldEntity.getCorpName()) || !oldFieldEntity.getCorpName().equals(field.getCorpName())) {
                oldFieldEntity.setCorpName(field.getCorpName());
            }

            fieldRepository.saveAndFlush(oldFieldEntity);
        } catch (Exception e) {
            log.error("Update failed : {}", e.getMessage());
            return new BaseResponseDTO("Update failed", 504);
        }

        return new BaseResponseDTO("Update success");
    }
}
