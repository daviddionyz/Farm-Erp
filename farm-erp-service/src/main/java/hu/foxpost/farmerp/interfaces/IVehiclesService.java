package hu.foxpost.farmerp.interfaces;

import hu.foxpost.farmerp.db.entity.Vehicle;
import hu.foxpost.farmerp.dto.VehicleDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;

public interface IVehiclesService {

    BaseResponseDTO getAllVehicles(
            String name,
            String type,
            Integer status,
            Integer page,
            Integer pageSize
    );


    BaseResponseDTO getAllVehicles();

    BaseResponseDTO deleteVehicles(Integer vehiclesId);

    BaseResponseDTO addNewVehicles(VehicleDTO vehicles);

    BaseResponseDTO updateVehicles(VehicleDTO vehicles);

    void changeVehicleStatusToOccupied(Vehicle vehiclesEntity);

    void changeVehicleStatusToFree(Vehicle vehiclesEntity);

    Vehicle getOneVehiclesById(Integer id) throws NullPointerException;
}