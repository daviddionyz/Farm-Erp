package hu.foxpost.farmerp.controllers;

import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.StorageDTO;
import hu.foxpost.farmerp.interfaces.IStorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/storage")
@AllArgsConstructor
public class StorageController {

    private final IStorageService storageService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllStorage(
            @RequestParam(value = "name", required = false, defaultValue = "none") String name,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ){
        return ResponseEntity.ok(storageService.getAllStorage(
                name,
                page,
                pageSize
        ));
    }

    @GetMapping("/all/every")
    public ResponseEntity<Object> getAllStorageWithoutPageRequest(){
        return ResponseEntity.ok(storageService.getAllStorage());
    }

    @GetMapping("/get/crops")
    public ResponseEntity<Object> getCropsWhatIsInStorage(@RequestParam Integer storageId){
        return ResponseEntity.ok(storageService.getCropsForStorage(storageId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteStorage(@RequestParam Integer storageId){
        return ResponseEntity.ok(storageService.deleteStorage(storageId));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponseDTO> addNewStorage(@RequestBody StorageDTO storage){
        return ResponseEntity.ok(storageService.addNewStorage(storage));

    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO> updateStorage(@RequestBody StorageDTO storage){
        return ResponseEntity.ok(storageService.updateStorage(storage));

    }
}
