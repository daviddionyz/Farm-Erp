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

    List<Delivery> findAllByIsCorpMoving(Boolean isCorpMoving);

}
