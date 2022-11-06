package hu.foxpost.farmerp.controllers;

import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.interfaces.ICropMovingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cropmoving")
@AllArgsConstructor
public class CropMovingController {

    private final ICropMovingService cropMovingService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getDeliveries(@RequestParam(defaultValue = "none", required = false) String search,
                                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize){
        return ResponseEntity.ok(cropMovingService.getAllDeliveriesForCorpMoving(search,page,pageSize));
    }

}
