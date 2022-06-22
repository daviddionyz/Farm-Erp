package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.VehiclesEntity;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.VehiclesDTO;
import hu.foxpost.farmerp.db.repository.VehiclesRepository;
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
public class VehiclesService {

    private final VehiclesRepository vehiclesRepository;

    public BaseResponseDTO getAllVehicles(
            String name,
            String type,
            Integer status,
            Integer page,
            Integer pageSize
    ) {
        try {
            List<VehiclesEntity> vehicles = vehiclesRepository.getAllVehiclesWithPageData(name, type, status, page * pageSize, pageSize);
            Integer searchNum = vehiclesRepository.getAllVehiclesWithoutPageData(name, type, status  );
            return new BaseResponseDTO(new PageResponseDTO(searchNum, Collections.singletonList(vehicles), page, pageSize));

        } catch (Exception e) {
            log.error("Get all field failed, {}", e.getMessage());
            return new BaseResponseDTO("No field found", 501);
        }
    }

    public BaseResponseDTO getAllVehicles() {
        try {
            List<VehiclesEntity> vehicles = vehiclesRepository.findAll();

            return new BaseResponseDTO(vehicles);

        } catch (Exception e) {
            log.error("Get all field failed, {}", e.getMessage());
            return new BaseResponseDTO("No field found", 501);
        }
    }

    public VehiclesEntity getOneVehiclesById(Integer id) throws NullPointerException {

        VehiclesEntity vehiclesEntity = vehiclesRepository.getVehiclesById(id).orElse(null);

        if (Objects.nonNull(vehiclesEntity)){
            return vehiclesEntity;
        }else{
            throw new NullPointerException();
        }
    }

    public BaseResponseDTO deleteVehicles(Integer vehiclesId) {
        try {
            VehiclesEntity vehiclesEntity = vehiclesRepository.getVehiclesById(vehiclesId).orElse(null);

            if (Objects.nonNull(vehiclesEntity)){
                vehiclesEntity.setIsDeleted(true);
                vehiclesRepository.saveAndFlush(vehiclesEntity);
            }
        } catch (Exception e) {
            log.error("Delete failed : {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 502);
        }

        return new BaseResponseDTO("Delete success");
    }

    public BaseResponseDTO addNewVehicles(VehiclesDTO vehicles) {
        try {
            VehiclesEntity newVehiclesEntity = VehiclesEntity.builder()
                    .name(vehicles.getName())
                    .type(vehicles.getType())
                    .status(vehicles.getStatus())
                    .isDeleted(false)
                    .build();

            vehiclesRepository.saveAndFlush(newVehiclesEntity);
        } catch (Exception e) {
            log.error("Save failed : {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 503);
        }

        return new BaseResponseDTO("Save success");
    }

    public BaseResponseDTO updateVehicles(VehiclesDTO vehicles) {
        try {
            VehiclesEntity oldVehicle = vehiclesRepository.getById(vehicles.getId());

            if (!oldVehicle.getName().equals(vehicles.getName())) {
                oldVehicle.setName(vehicles.getName());
            }

            if (!oldVehicle.getType().equals(vehicles.getType())) {
                oldVehicle.setType(vehicles.getType());
            }

            if (!oldVehicle.getStatus().equals(vehicles.getStatus())) {
                oldVehicle.setStatus(vehicles.getStatus());
            }

            vehiclesRepository.saveAndFlush(oldVehicle);
        } catch (Exception e) {
            log.error("Update failed : {}", e.getMessage());
            return new BaseResponseDTO("Update failed", 504);
        }

        return new BaseResponseDTO("Update success");
    }

    public void changeVehicleStatusToOccupied(VehiclesEntity vehiclesEntity){
        log.info("change vehicle status {}", vehiclesEntity);
        if (vehiclesEntity.getStatus().equals(3)){
            vehiclesEntity.setStatus(2);
            vehiclesRepository.saveAndFlush(vehiclesEntity);
        }
    }
}
