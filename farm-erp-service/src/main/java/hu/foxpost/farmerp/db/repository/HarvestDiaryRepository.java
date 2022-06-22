package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.HarvestDiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface HarvestDiaryRepository extends JpaRepository<HarvestDiaryEntity, Integer > {

    Optional<List<HarvestDiaryEntity>> getAllBy();
    HarvestDiaryEntity getById(Integer id);
}
