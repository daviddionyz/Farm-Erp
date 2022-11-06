package hu.foxpost.farmerp.interfaces;

import hu.foxpost.farmerp.db.entity.Worker;
import hu.foxpost.farmerp.dto.WorkerDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

import java.time.LocalDateTime;

public interface IWorkerService {

    BaseResponseDTO getAllWorker(
            String name,
            Integer vehicle,
            String position,
            LocalDateTime resultFrom,
            LocalDateTime resultTo,
            Integer page,
            Integer pageSize
    );

    BaseResponseDTO getAllWorker();

    BaseResponseDTO deleteWorker(Integer workerId);

    BaseResponseDTO addNewWorker(WorkerDTO worker);

    BaseResponseDTO updateWorker(WorkerDTO worker);

    Worker getOneWorkerById(Integer id) throws NullPointerException;
}
