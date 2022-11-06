package hu.foxpost.farmerp.interfaces;

import hu.foxpost.farmerp.db.entity.Field;
import hu.foxpost.farmerp.dto.FieldDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

public interface IFieldService {

    BaseResponseDTO getAllField(
            String name,
            String corpType,
            String corpName,
            Integer page,
            Integer pageSize
    );

    BaseResponseDTO getAllField();

    Field getOneFieldById(Integer id) throws NullPointerException;

    BaseResponseDTO deleteField(Integer fieldId);

    BaseResponseDTO addNewField(FieldDTO field);

    BaseResponseDTO updateField(FieldDTO field);
}
