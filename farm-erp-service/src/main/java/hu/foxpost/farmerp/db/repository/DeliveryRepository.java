package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

import java.util.*;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {


    List<Delivery> getAllByDiaryId(Integer diaryId);
    Optional<List<Delivery>> getAllByVehicleId(Integer vehicleId);
    Optional<List<Delivery>> getAllByWorkerId(Integer workerId);

    List<Delivery> findAllByIsCorpMoving(Boolean isCorpMoving);

    /* TODO vedd fel a kurva dátumot
    @Query("select * from Delivery d where d.id between ?1 and ?2")
    List<Delivery> getAllDiaryByDate(LocalDate from, LocalDate to);
     */
}
