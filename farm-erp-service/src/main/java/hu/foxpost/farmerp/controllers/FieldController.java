package hu.foxpost.farmerp.controllers;

import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.FieldDTO;
import hu.foxpost.farmerp.interfaces.IFieldService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/field")
@AllArgsConstructor
public class FieldController {

    private final IFieldService fieldService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getAllField(
        @RequestParam(value = "name", required = false, defaultValue = "none") String name,
        @RequestParam(value = "corpType", required = false, defaultValue = "none") String corpType,
        @RequestParam(value = "corpName", required = false, defaultValue = "none") String corpName,
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize
        ){
        return ResponseEntity.ok(fieldService.getAllField(
                name,
                corpType,
                corpName,
                page,
                pageSize
        ));
    }

    @GetMapping("/all/every")
    public ResponseEntity<BaseResponseDTO> getAllFieldWithoutPageRequest(){
        return ResponseEntity.ok(fieldService.getAllField());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteField(@RequestParam Integer fieldId){
        return ResponseEntity.ok(fieldService.deleteField(fieldId));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponseDTO> addNewField(@RequestBody FieldDTO field){
        return ResponseEntity.ok(fieldService.addNewField(field));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO> updateField(@RequestBody FieldDTO field){
        return ResponseEntity.ok(fieldService.updateField(field));
    }
}
