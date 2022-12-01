package hu.foxpost.farmerp.service;

import hu.foxpost.farmerp.db.entity.Vehicle;
import hu.foxpost.farmerp.db.entity.Worker;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.WorkerDTO;
import hu.foxpost.farmerp.db.repository.WorkerRepository;
import hu.foxpost.farmerp.dto.response.PageResponseDTO;
import hu.foxpost.farmerp.interfaces.IWorkerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@AllArgsConstructor
public class WorkerService implements IWorkerService {

    private final WorkerRepository workerRepository;
    private final VehiclesService vehiclesService;

    @Override
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
            if (Objects.isNull(resultFrom)) {
                resultFrom = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
            }
            if (Objects.isNull(resultTo)) {
                resultTo = LocalDateTime.of(2100, 12, 31, 0, 0, 0);
            }

            List<Worker> workerEntities = workerRepository.getAllWorkersWithPageData(name, vehicle, position, resultFrom, resultTo, page * pageSize, pageSize);
            Integer searchNum = workerRepository.getAllWorkersNumWithoutPageData(name, vehicle, position, resultFrom, resultTo);
            return new BaseResponseDTO(new PageResponseDTO(searchNum, Collections.singletonList(workerEntities), page, pageSize));

        } catch (Exception e) {
            log.error("Getting workers failed, {}", e.getMessage());
            return new BaseResponseDTO("Getting workers failed", 1400);
        }
    }

    @Override
    public BaseResponseDTO getAllWorker() {

        try {

            List<Worker> workerEntities = workerRepository.findAllByIsDeleted(false);

            return new BaseResponseDTO(workerEntities);

        } catch (Exception e) {
            log.error("Getting workers failed, {}", e.getMessage());
            return new BaseResponseDTO("Getting workers failed", 1400);
        }
    }

    @Override
    @Transactional
    public BaseResponseDTO deleteWorker(Integer workerId) {
        log.info("Worker delete started for id : {}", workerId);
        try {
            Worker workerEntity = getOneWorkerById(workerId);

            workerEntity.setIsDeleted(true);

            workerRepository.saveAndFlush(workerEntity);
        } catch (Exception e) {
            log.error("Delete failed : {}", e.getMessage());
            return new BaseResponseDTO("Delete failed", 1401);
        }

        log.info("Worker delete finished successfully!");
        return new BaseResponseDTO("Delete success");
    }

    @Override
    public BaseResponseDTO addNewWorker(WorkerDTO worker) {
        log.info("Worker create started: {}", worker);
        try {
            Vehicle vehicle;
            try {
                vehicle = vehiclesService.getOneVehiclesById(worker.getVehicle());
            } catch (Exception e) {
                log.error("Vehicle not found!");
                vehicle = null;
            }

            if (Objects.nonNull(vehicle)) {
                vehiclesService.changeVehicleStatusToOccupied(vehicle);
            }

            Worker newWorkerEntity = Worker.builder()
                    .name(worker.getName())
                    .joinDate(worker.getJoinDate())
                    .vehicle(vehicle)
                    .position(worker.getPosition())
                    .isDeleted(false)
                    .build();

            workerRepository.saveAndFlush(newWorkerEntity);
        } catch (Exception e) {
            log.error("Save failed : {}", e.getMessage());
            return new BaseResponseDTO("Save failed", 1402);
        }

        log.info("Worker create finished successfully!");
        return new BaseResponseDTO("Save success");
    }

    @Override
    @Transactional
    public BaseResponseDTO updateWorker(WorkerDTO worker) {
        log.info("Worker update started, input data: {}", worker);
        try {
            Vehicle vehicle = null;
            Worker oldWorker = getOneWorkerById(worker.getId());

            if (Objects.isNull(worker.getVehicle()) || worker.getVehicle() == -1
                    || (Objects.nonNull(oldWorker.getVehicle()) && !oldWorker.getVehicle().getId().equals(worker.getVehicle()))) {
                if (Objects.nonNull(oldWorker.getVehicle()) && !isThereSomeoneElseUsingThisVehicle(worker.getId(), oldWorker.getVehicle())) {
                    vehiclesService.changeVehicleStatusToFree(oldWorker.getVehicle());
                }
            }
            if (Objects.nonNull(worker.getVehicle())) {
                try {
                    vehicle = vehiclesService.getOneVehiclesById(worker.getVehicle());
                    vehiclesService.changeVehicleStatusToOccupied(vehicle);
                } catch (Exception ignored) {
                    log.debug("Vehicle not found!");
                }
            }


            Worker workerEntitySave = Worker.builder()
                    .id(worker.getId())
                    .name(worker.getName())
                    .joinDate(worker.getJoinDate())
                    .vehicle(vehicle)
                    .position(worker.getPosition())
                    .isDeleted(false)
                    .build();

            workerRepository.saveAndFlush(workerEntitySave);
        } catch (Exception e) {
            log.error("Update failed : {}", e.getMessage());
            return new BaseResponseDTO("Update failed", 1403);
        }

        log.info("Worker update finished: {}", worker.getId());
        return new BaseResponseDTO("Update success");
    }

    @Override
    public Worker getOneWorkerById(Integer id) throws NullPointerException {

        Worker workerEntity = workerRepository.getWorkerById(id).orElse(null);

        if (Objects.nonNull(workerEntity)) {
            return workerEntity;
        } else {
            throw new NullPointerException();
        }
    }

    private Boolean isThereSomeoneElseUsingThisVehicle(Integer workerId, Vehicle vehicle) {
        Optional<List<Worker>> workers = workerRepository.getWorkerByVehicle(vehicle);
        AtomicBoolean result = new AtomicBoolean(false);

        workers.ifPresent(wrs -> wrs.forEach(wr -> {
            if (!wr.getId().equals(workerId)) {
                result.set(true);
            }
        }));

        return result.get();
    }
}
