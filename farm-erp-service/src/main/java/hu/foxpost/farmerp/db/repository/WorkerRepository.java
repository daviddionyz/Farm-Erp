package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.Vehicle;
import hu.foxpost.farmerp.db.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface WorkerRepository extends JpaRepository<Worker,Integer> {

    @Query(value = "select * from farm_erp.workers s where " +
            "(?1 = 'none' or LOWER(s.name) like CONCAT('%',LOWER(?1),'%') ) and" +
            "(?2 = '-1' or s.vehicle_id = ?2 ) and" +
            "(?3 = 'none' or LOWER(s.position) like CONCAT('%',LOWER(?3),'%') ) and " +
            "(s.is_deleted = false ) and" +
            "(s.join_date between ?4 and ?5 )" +
            "limit ?7 offset ?6" , nativeQuery = true
    )
    List<Worker> getAllWorkersWithPageData(
            String name,
            Integer vehicle,
            String position,
            @DateTimeFormat LocalDateTime resultFrom,
            @DateTimeFormat LocalDateTime resultTo,
            Integer page,
            Integer pageSize
    );

    @Query(value = "select count(*) from farm_erp.workers s where " +
            "(?1 = 'none' or LOWER(s.name) like CONCAT('%',LOWER(?1),'%') ) and" +
            "(?2 = '-1' or s.vehicle_id = ?2 ) and" +
            "(?3 = 'none' or LOWER(s.position) like CONCAT('%',LOWER(?3),'%') ) and " +
            "(s.is_deleted = false ) and" +
            "(s.join_date between ?4 and ?5 )"
            , nativeQuery = true
    )
    Integer getAllWorkersNumWithoutPageData(
            String name,
            Integer vehicle,
            String position,
            @DateTimeFormat LocalDateTime resultFrom,
            @DateTimeFormat LocalDateTime resultTo
    );

    Optional<List<Worker>> findAllBy();

    List<Worker> findAllByIsDeleted(Boolean isDeleted);

    Optional<Worker> getWorkerById(Integer id);

    Optional<List<Worker>> getWorkerByVehicle(Vehicle vehicle);
}
