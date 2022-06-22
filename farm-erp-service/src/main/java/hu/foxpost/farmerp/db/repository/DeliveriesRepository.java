package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.DeliveriesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DeliveriesRepository extends JpaRepository<DeliveriesEntity, Integer> {


    Optional<List<DeliveriesEntity>> getAllByDiaryId(Integer diaryId);
    Optional<List<DeliveriesEntity>> getAllByVehicleId(Integer vehicleId);
    Optional<List<DeliveriesEntity>> getAllByWorkerId(Integer workerId);

    List<DeliveriesEntity> findAllByIsCorpMoving(Boolean isCorpMoving);
}
