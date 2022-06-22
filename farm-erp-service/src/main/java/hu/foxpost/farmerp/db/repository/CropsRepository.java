package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.CropsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CropsRepository extends JpaRepository<CropsEntity, Integer> {

    List<CropsEntity> findAllByStorageId(Integer storageId);
}
