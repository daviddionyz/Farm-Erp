package hu.foxpost.farmerp.controllers;

import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.VehicleDTO;
import hu.foxpost.farmerp.interfaces.IVehiclesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
@AllArgsConstructor
public class VehiclesController {

    private final IVehiclesService vehiclesService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getAllVehicles(
            @RequestParam(value = "name", required = false, defaultValue = "none") String name,
            @RequestParam(value = "type", required = false, defaultValue = "none") String type,
            @RequestParam(value = "status", required = false, defaultValue = "-1") Integer status,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ){
        return ResponseEntity.ok(vehiclesService.getAllVehicles(
                name,
                type,
                status,
                page,
                pageSize
        ));
    }

    @GetMapping("/all/every")
    public ResponseEntity<BaseResponseDTO> getAllVehiclesWithoutPageRequest(){
        return ResponseEntity.ok(vehiclesService.getAllVehicles());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteVehicles(@RequestParam Integer vehicleId){
        return ResponseEntity.ok(vehiclesService.deleteVehicles(vehicleId));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponseDTO> addNewVehicles(@RequestBody VehicleDTO vehicles){
        return ResponseEntity.ok(vehiclesService.addNewVehicles(vehicles));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO> updateVehicles(@RequestBody VehicleDTO vehicles){
        return ResponseEntity.ok(vehiclesService.updateVehicles(vehicles));
    }
}
