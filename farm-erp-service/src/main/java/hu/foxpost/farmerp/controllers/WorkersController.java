package hu.foxpost.farmerp.controllers;


import hu.foxpost.farmerp.dto.response.BaseResponseDTO;
import hu.foxpost.farmerp.dto.WorkerDTO;
import hu.foxpost.farmerp.interfaces.IWorkerService;
import hu.foxpost.farmerp.utils.CommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping("/api/worker")
@AllArgsConstructor
public class WorkersController {

    private final IWorkerService workerService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponseDTO> getAllWorker(
            @RequestParam(value = "name", required = false, defaultValue = "none")   String name,
            @RequestParam(value = "vehicle", required = false, defaultValue = "-1")   Integer vehicle,
            @RequestParam(value = "position", required = false, defaultValue = "none")  String position,
            @RequestParam(value = "createdAtFrom", required = false)                 String resultFrom,
            @RequestParam(value = "createdAtTo", required = false)                   String resultTo,
            @RequestParam(value = "page", required = false, defaultValue = "0")      Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ){
        return ResponseEntity.ok(workerService.getAllWorker(
                name,
                vehicle,
                position,
                Objects.isNull(resultFrom) ? null : LocalDate.parse(resultFrom, CommonUtil.getTOFDateFormatter()).atStartOfDay(),
                Objects.isNull(resultTo) ? null : LocalDate.parse(resultTo, CommonUtil.getTOFDateFormatter()).atTime(23, 59),
                page,
                pageSize
        ));
    }

    @GetMapping("/all/every")
    public ResponseEntity<BaseResponseDTO> getAllWorkerWithoutPageRequest(){
        return ResponseEntity.ok(workerService.getAllWorker());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponseDTO> deleteWorker(@RequestParam Integer workerId){
        return ResponseEntity.ok(workerService.deleteWorker(workerId));
    }

    @PostMapping("/add")
    public ResponseEntity<BaseResponseDTO> addNewWorker(@RequestBody WorkerDTO worker){
        return ResponseEntity.ok(workerService.addNewWorker(worker));
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponseDTO> updateWorker(@RequestBody WorkerDTO worker){
        return ResponseEntity.ok(workerService.updateWorker(worker));
    }
}
