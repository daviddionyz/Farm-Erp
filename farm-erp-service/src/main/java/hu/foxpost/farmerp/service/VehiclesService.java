package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.Vehicle;
import hu.foxpost.farmerp.db.entity.Worker;
import hu.foxpost.farmerp.db.repository.WorkerRepository;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.VehicleDTO;
import hu.foxpost.farmerp.db.repository.VehicleRepository;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import hu.foxpost.farmerp.interfaces.IVehiclesService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class VehiclesService implements IVehiclesService {

    private final VehicleRepository vehiclesRepository;
    private final WorkerRepository workerRepository;

    @Override
    public BaseResponseDTO getAllVehicles(
            String name,
            String type,
            Integer status,
            Integer page,
            Integer pageSize
    ) {
        try {
            List<Vehicle> vehicles = vehiclesRepository.getAllVehiclesWithPageData(name, type, status, page * pageSize, pageSize);
            Integer searchNum = vehiclesRepository.getAllVehiclesNumWithoutPageData(name, type, status);
            return new BaseResponseDTO(new PageResponseDTO(searchNum, Collections.singletonList(vehicles), page, pageSize));

        } catch (Exception e) {
            log.error("Getting vehicles failed, {}", e.getMessage());
            return new BaseResponseDTO("Vehicles not found", 1300);
        }
    }

    @Override
    public BaseResponseDTO getAllVehicles() {
        try {
            List<Vehicle> vehicles = vehiclesRepository.findAllByIsDeleted(false);

            return new BaseResponseDTO(vehicles);

        } catch (Exception e) {
            log.error("Getting vehicles failed, {}", e.getMessage());
            return new BaseResponseDTO("Vehicles not found", 1301);
        }
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteVehicles(Integer vehiclesId) {
        log.info("Vehicle delete started for id : {}", vehiclesId);
        try {
            Vehicle vehiclesEntity = getOneVehiclesById(vehiclesId);
            vehiclesEntity.setIsDeleted(true);
            deleteVehiclesIdFromWorker(vehiclesId);

            vehiclesRepository.saveAndFlush(vehiclesEntity);

        } catch (Exception e) {
            log.error("Delete failed : {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 1301);
        }

        log.info("Vehicle delete finished successfully!");
        return new BaseResponseDTO("Delete success");
    }

    @Override
    public BaseResponseDTO addNewVehicles(VehicleDTO vehicles) {
        log.info("Vehicle create started: {}", vehicles);
        try {
            Vehicle newVehiclesEntity = Vehicle.builder()
                    .name(vehicles.getName())
                    .type(vehicles.getType())
                    .status(vehicles.getStatus())
                    .isDeleted(false)
                    .build();

            vehiclesRepository.saveAndFlush(newVehiclesEntity);
        } catch (Exception e) {
            log.error("Save failed : {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 1302);
        }

        log.info("Vehicle create finished successfully!");
        return new BaseResponseDTO("Save success");
    }

    @Override
    @Transactional
    public BaseResponseDTO updateVehicles(VehicleDTO vehicles) {
        log.info("Vehicle update started, input data: {}", vehicles);

        try {
            Vehicle oldVehicle = vehiclesRepository.getById(vehicles.getId());

            if (!oldVehicle.getName().equals(vehicles.getName())) {
                oldVehicle.setName(vehicles.getName());
            }

            if (!oldVehicle.getType().equals(vehicles.getType())) {
                oldVehicle.setType(vehicles.getType());
            }

            if (!oldVehicle.getStatus().equals(vehicles.getStatus())) {
                if ( (oldVehicle.getStatus().equals(2) && vehicles.getStatus().equals(3)
                        || (oldVehicle.getStatus().equals(1) && vehicles.getStatus().equals(3))) ){
                    deleteVehiclesIdFromWorker(vehicles.getId());
                }

                oldVehicle.setStatus(vehicles.getStatus());
            }

            vehiclesRepository.saveAndFlush(oldVehicle);
        } catch (Exception e) {
            log.error("Update failed : {}", e.getMessage());
            return new BaseResponseDTO("Update failed", 1303);
        }

        log.info("Vehicle update finished: {}", vehicles.getId());
        return new BaseResponseDTO("Update success");
    }

    @Override
    public void changeVehicleStatusToOccupied(Vehicle vehiclesEntity) {
        log.info("change vehicle status to occupied {}", vehiclesEntity);
        if (vehiclesEntity.getStatus().equals(3)) {
            vehiclesEntity.setStatus(2);
            vehiclesRepository.saveAndFlush(vehiclesEntity);
        }else {
            log.info("Change failed vehicle under repair or already occupied!");
        }
    }

    @Override
    public void changeVehicleStatusToFree(Vehicle vehiclesEntity) {
        log.info("change vehicle status to free {}", vehiclesEntity);
        if (vehiclesEntity.getStatus().equals(2)) {
            vehiclesEntity.setStatus(3);
            vehiclesRepository.saveAndFlush(vehiclesEntity);
        }
    }

    @Override
    public Vehicle getOneVehiclesById(Integer id) throws NullPointerException {

        Vehicle vehiclesEntity = vehiclesRepository.getVehiclesById(id).orElse(null);

        if (Objects.nonNull(vehiclesEntity)) {
            return vehiclesEntity;
        } else {
            throw new NullPointerException();
        }
    }


    private void deleteVehiclesIdFromWorker(Integer vehicleId){
        Optional<List<Worker>> workers = workerRepository.getWorkerByVehicle(getOneVehiclesById(vehicleId));

        workers.ifPresent( wrs ->
            wrs.forEach( wr -> {
                wr.setVehicle(null);
                workerRepository.saveAndFlush(wr);
            }));
    }
}
