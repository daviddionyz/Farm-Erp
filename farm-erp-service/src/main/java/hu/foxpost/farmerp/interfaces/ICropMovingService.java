package hu.foxpost.farmerp.interfaces;

import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

public interface ICropMovingService {

    BaseResponseDTO getAllDeliveriesForCorpMoving(String search,
                                                  Integer page,
                                                  Integer pageSize
    );
}
