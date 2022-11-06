package hu.foxpost.farmerp.interfaces;

import hu.foxpost.farmerp.dto.DeliveryDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

import java.time.LocalDate;

public interface IDeliveriesService {

    BaseResponseDTO getAllDeliveriesByDiary(Integer diaryId,
                                            String search,
                                            Integer page,
                                            Integer pageSize
    );

    BaseResponseDTO getAllDeliveriesByDate(LocalDate from,
                                           LocalDate to,
                                           Integer page,
                                           Integer pageSize
    );

    BaseResponseDTO intakeDelivery(DeliveryDTO deliveriesDTO);

    BaseResponseDTO updateDelivery(DeliveryDTO deliveriesDTO);

    BaseResponseDTO deleteDelivery(Integer deliveryId);

    BaseResponseDTO deleteDiaryIdInDeliveries(Integer diaryId);


}
