package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CropRepository extends JpaRepository<Crop, Integer> {

    List<Crop> findAllByStorageId(Integer storageIdd);
}
