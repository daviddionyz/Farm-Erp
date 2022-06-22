package hu.foxpost.farmerp.controler;

import hu.foxpost.farmerp.dto.HarvestDiaryDTO;
import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.service.HarvestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/harvest")
@AllArgsConstructor
public class HarvestController {

    private final HarvestService harvestService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getAllDiary(){
        return ResponseEntity.ok(harvestService.getAllDiary());
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponseDTO> createDiary(@RequestBody HarvestDiaryDTO diaryDTO){
        return ResponseEntity.ok(harvestService.createNewDiary(diaryDTO));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteDiary(@RequestParam Integer diaryId){
        return ResponseEntity.ok(harvestService.deleteDiary(diaryId));
    }

}
