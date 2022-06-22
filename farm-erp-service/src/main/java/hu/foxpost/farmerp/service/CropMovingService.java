package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.DeliveriesEntity;
import hu.foxpost.farmerp.db.repository.DeliveriesRepository;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.ArrayList;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class CropMovingService {

    private final DeliveriesRepository deliveriesRepository;

    private final DeliveriesService deliveriesService;
    private final StorageService storageService;
    private final VehiclesService vehiclesService;
    private final WorkerService workerService;
    private final FieldService fieldService;

    public BaseResponseDTO getAllDeliveriesForCorpMoving(String search,
                                                         Integer page,
                                                         Integer pageSize
    ) {
        try {
            List<DeliveriesEntity> deliveriesEntityList = deliveriesRepository.findAllByIsCorpMoving(true);
            List<DeliveriesEntity> response = new ArrayList<>();

            if (Objects.nonNull(deliveriesEntityList) && !search.equals("none")) {
                List<DeliveriesEntity> finalResponse = response;
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
            return new BaseResponseDTO("Getting data failed", 502);
        }
    }
}
