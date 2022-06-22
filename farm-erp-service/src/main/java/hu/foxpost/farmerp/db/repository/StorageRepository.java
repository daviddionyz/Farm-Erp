package hu.foxpost.farmerp.db.repository;


import hu.foxpost.farmerp.db.entity.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StorageRepository extends JpaRepository<StorageEntity,Integer> {

    @Query(value = "select * from farm_erp.storage s where " +
            "(?1 = 'none' or LOWER(s.name) like CONCAT('%',LOWER(?1),'%') ) " +
            "limit ?3 offset ?2" , nativeQuery = true
    )
    List<StorageEntity> getAllStorageWithPageData(
            String name,
            Integer page,
            Integer pageSize
    );

    @Query(value = "select count(*) from farm_erp.storage s where " +
            "(?1 = 'none' or LOWER(s.name) like CONCAT('%',LOWER(?1),'%') )"
            , nativeQuery = true
    )
    Integer getAllStorageWithoutPageData(
            String name
    );

    Optional<List<StorageEntity>> findAllBy();


    Optional<StorageEntity> getStorageById(Integer id);
}
