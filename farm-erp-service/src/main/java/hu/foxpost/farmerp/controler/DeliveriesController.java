package hu.foxpost.farmerp.controler;

import hu.foxpost.farmerp.dto.DeliveriesDTO;
import hu.foxpost.farmerp.dto.FieldDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.service.DeliveriesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/deliveries")
@AllArgsConstructor
public class DeliveriesController {

    private final DeliveriesService deliveriesService;

    @GetMapping("/get/{diaryId}")
    public ResponseEntity<BaseResponseDTO> getDeliveriesByDiaryId(@PathVariable Integer diaryId,
                                                                  @RequestParam(defaultValue = "none", required = false) String search,
                                                                  @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize){
        return ResponseEntity.ok(deliveriesService.getAllDeliveriesByDiary(diaryId,search,page,pageSize));
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponseDTO> createNewDelivery(@RequestBody DeliveriesDTO deliveriesDTO){
        return ResponseEntity.ok(deliveriesService.intakeDelivery(deliveriesDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteDelivery(@RequestParam Integer deliveryId){
        return ResponseEntity.ok(deliveriesService.deleteDelivery(deliveryId));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO> updateDelivery(@RequestBody DeliveriesDTO deliveriesDTO){
        return ResponseEntity.ok(deliveriesService.updateDelivery(deliveriesDTO));
    }
}
