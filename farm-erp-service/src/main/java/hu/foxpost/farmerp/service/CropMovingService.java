package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.Delivery;
import hu.foxpost.farmerp.db.repository.DeliveryRepository;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import hu.foxpost.farmerp.interfaces.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.ArrayList;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class CropMovingService implements ICropMovingService {

    private final DeliveryRepository deliveriesRepository;

    private final IDeliveriesService deliveriesService;
    private final IStorageService storageService;
    private final IVehiclesService vehiclesService;
    private final IWorkerService workerService;
    private final IFieldService fieldService;

    @Override
    public BaseResponseDTO getAllDeliveriesForCorpMoving(String search,
                                                         Integer page,
                                                         Integer pageSize ) {
        try {
            List<Delivery> deliveriesEntityList = deliveriesRepository.findAllByIsCorpMoving(true);
            List<Delivery> response = new ArrayList<>();

            if (Objects.nonNull(deliveriesEntityList) && !search.equals("none")) {
                List<Delivery> finalResponse = response;
                deliveriesEntityList.forEach(delivery -> {
                    if (delivery.getWorker().getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)) ||
                            delivery.getVehicle().getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)) ||
                            (Objects.nonNull(delivery.getFrom()) &&
                                    delivery.getFrom().getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) ||
                            (Objects.nonNull(delivery.getWhere()) &&
                                    delivery.getWhere().getName().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT)))) {
                        finalResponse.add(delivery);
                    }
                });
                response = finalResponse;
            } else {
                response = deliveriesEntityList;
            }

            Collections.sort(response);
            Integer listSize = response.size();

            try {
                response = response.subList(page * pageSize, Math.min(page * pageSize + pageSize, response.size()));
            } catch (Exception e) {
                log.error("Making sub list failed: {}", e.getMessage());
            }

            return new BaseResponseDTO(new PageResponseDTO(listSize, Collections.singletonList(response), page, pageSize));

        } catch (Exception e) {
            log.error("Getting all deliveries for this corp moving: {}", e.getMessage());
            return new BaseResponseDTO("Getting data failed", 1500);
        }
    }
}
