package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.VehiclesEntity;
import hu.foxpost.farmerp.db.entity.WorkerEntity;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.WorkerDTO;
import hu.foxpost.farmerp.db.repository.WorkerRepository;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final VehiclesService vehiclesService;

    public BaseResponseDTO getAllWorker(
            String name,
            Integer vehicle,
            String position,
            LocalDateTime resultFrom,
            LocalDateTime resultTo,
            Integer page,
            Integer pageSize
    ) {

        try {
            if (Objects.isNull(resultFrom) ) {
                resultFrom = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
            }
            if (Objects.isNull(resultTo) ) {
                resultTo = LocalDateTime.of(2100, 12, 31, 0, 0, 0);
            }

            List<WorkerEntity> workerEntities = workerRepository.getAllWorkersWithPageData(name,  vehicle,position,resultFrom,resultTo, page * pageSize, pageSize);
            Integer searchNum = workerRepository.getAllWorkersWithoutPageData(name,  vehicle,position,resultFrom,resultTo);
            return new BaseResponseDTO(new PageResponseDTO(searchNum, Collections.singletonList(workerEntities), page, pageSize));

        } catch (Exception e) {
            log.error("Get all worker failed, {}", e.getMessage());
            return new BaseResponseDTO("Getting workers failed", 501);
        }
    }

    public BaseResponseDTO getAllWorker() {

        try {

            List<WorkerEntity> workerEntities = workerRepository.findAll();

            return new BaseResponseDTO(workerEntities);

        } catch (Exception e) {
            log.error("Get all worker failed, {}", e.getMessage());
            return new BaseResponseDTO("Getting workers failed", 501);
        }
    }

    public WorkerEntity getOneWorkerById(Integer id) throws NullPointerException {

        WorkerEntity workerEntity = workerRepository.getWorkerById(id).orElse(null);

        if (Objects.nonNull(workerEntity)){
            return workerEntity;
        }else{
            throw new NullPointerException();
        }
    }

    public BaseResponseDTO deleteWorker(Integer workerId) {
        try {
            WorkerEntity workerEntity = workerRepository.getWorkerById(workerId).orElse(null);

            if (Objects.nonNull(workerEntity)){
                workerEntity.setIsDeleted(true);
                workerRepository.saveAndFlush(workerEntity);
            }
        } catch (Exception e) {
            log.error("Delete failed : {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 502);
        }

        return new BaseResponseDTO("Delete success");
    }

    public BaseResponseDTO addNewWorker(WorkerDTO worker) {
        try {

            VehiclesEntity vehicle;
            try {
                vehicle = vehiclesService.getOneVehiclesById(worker.getVehicle());
            }catch (Exception e){
                vehicle = null;
            }

            if (Objects.nonNull(vehicle)){
                vehiclesService.changeVehicleStatusToOccupied(vehicle);
            }

            WorkerEntity newWorkerEntity = WorkerEntity.builder()
                    .name(worker.getName())
                    .joinDate(worker.getJoinDate())
                    .vehicle(vehicle)
                    .position(worker.getPosition())
                    .isDeleted(false)
                    .build();

            workerRepository.saveAndFlush(newWorkerEntity);
        } catch (Exception e) {
            log.error("Save failed : {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 503);
        }

        return new BaseResponseDTO("Save success");
    }

    public BaseResponseDTO updateWorker(WorkerDTO worker) {
        try {
            VehiclesEntity vehicle;
            try {
                vehicle = vehiclesService.getOneVehiclesById(worker.getVehicle());
            }catch (Exception e){
                vehicle = null;
            }

            if (Objects.nonNull(vehicle)){
                vehiclesService.changeVehicleStatusToOccupied(vehicle);
            }

            WorkerEntity workerEntitySave = WorkerEntity.builder()
                    .id(worker.getId())
                    .name(worker.getName())
                    .joinDate(worker.getJoinDate())
                    .vehicle(vehicle)
                    .position(worker.getPosition())
                    .build();

            workerRepository.saveAndFlush(workerEntitySave);
        } catch (Exception e) {
            log.error("Update failed : {}", e.getMessage());
            return new BaseResponseDTO("Update failed", 504);
        }

        return new BaseResponseDTO("Update success");
    }

}
