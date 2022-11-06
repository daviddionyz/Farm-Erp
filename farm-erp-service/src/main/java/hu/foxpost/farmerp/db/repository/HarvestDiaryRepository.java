package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.HarvestDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface HarvestDiaryRepository extends JpaRepository<HarvestDiary, Integer > {

    Optional<List<HarvestDiary>> getAllBy();
    HarvestDiary getById(Integer id);
}
