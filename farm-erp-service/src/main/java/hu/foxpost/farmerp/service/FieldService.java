package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.Field;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.FieldDTO;
import hu.foxpost.farmerp.db.repository.FieldRepository;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import hu.foxpost.farmerp.interfaces.IFieldService;
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
public class FieldService implements IFieldService {

    private final FieldRepository fieldRepository;

    @Override
    public BaseResponseDTO getAllField(
            String name,
            String corpType,
            String corpName,
            Integer page,
            Integer pageSize
    ) {
        try {
            List<Field> fieldEntities = fieldRepository.getAllFieldWithPageData(name,  corpType,corpName, page * pageSize, pageSize);
            Integer searchNum = fieldRepository.getAllFieldWithoutPageData(name,  corpType,corpName);
            return new BaseResponseDTO(new PageResponseDTO(searchNum, Collections.singletonList(fieldEntities), page, pageSize));

        } catch (Exception e) {
            log.error("Get all field failed, {}", e.getMessage());
            return new BaseResponseDTO("No field found", 1100 );
        }

    }

    @Override
    public BaseResponseDTO getAllField() {
        try {
            List<Field> fieldEntities = fieldRepository.findAllByIsDeleted(false).orElse(null);
            return new BaseResponseDTO(fieldEntities);

        } catch (Exception e) {
            log.error("Get all field failed, {}", e.getMessage());
            return new BaseResponseDTO("No field found", 1100 );
        }

    }

    @Override
    public Field getOneFieldById(Integer id) throws NullPointerException {

        Field fieldEntity = fieldRepository.getFieldById(id).orElse(null);

        if (Objects.nonNull(fieldEntity)) {
            return fieldEntity;
        } else {
            throw new NullPointerException();
        }
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteField(Integer fieldId) {
        log.info("Field delete started for id: {}", fieldId);
        try {
            Field field = getOneFieldById(fieldId);

            field.setIsDeleted(true);

            fieldRepository.saveAndFlush(field);

        } catch (Exception e) {
            log.error("Delete failed : {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 1101);
        }

        log.info("Field delete finished successfully");
        return new BaseResponseDTO("Delete success");
    }

    @Override
    public BaseResponseDTO addNewField(FieldDTO field) {
        log.info("Filed creating started : {}", field);
        try {
            Field newFieldEntity = Field.builder()
                    .name(field.getName())
                    .size(field.getSize())
                    .corpType(field.getCorpType())
                    .corpName(field.getCorpName())
                    .isDeleted(false)
                    .build();

            fieldRepository.saveAndFlush(newFieldEntity);
        } catch (Exception e) {
            log.error("Save failed : {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 1102);
        }

        log.info("Filed creating finished successfully");
        return new BaseResponseDTO("Save success");
    }

    @Override
    @Transactional
    public BaseResponseDTO updateField(FieldDTO field) {
        log.info("Filed updated started : {}", field);
        try {
            Field oldFieldEntity = fieldRepository.getById(field.getId());

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
            return new BaseResponseDTO("Update failed", 1103);
        }

        log.info("Filed update finished successfully");
        return new BaseResponseDTO("Update success");
    }
}
