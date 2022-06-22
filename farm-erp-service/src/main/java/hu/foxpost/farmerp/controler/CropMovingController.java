package hu.foxpost.farmerp.controler;

import hu.foxpost.farmerp.dto.DeliveriesDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.service.CropMovingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/corpmoving")
@AllArgsConstructor
public class CropMovingController {

    private final CropMovingService cropMovingService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getDeliveries(@RequestParam(defaultValue = "none", required = false) String search,
                                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize){
        return ResponseEntity.ok(cropMovingService.getAllDeliveriesForCorpMoving(search,page,pageSize));
    }

}
