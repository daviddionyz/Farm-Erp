package hu.foxpost.farmerp.db.repository;


import hu.foxpost.farmerp.db.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface StorageRepository extends JpaRepository<Storage,Integer> {

    @Query(value = "select * from farm_erp.storages s where " +
            "(?1 = 'none' or LOWER(s.name) like CONCAT('%',LOWER(?1),'%') ) and " +
            "(s.is_deleted = false)" +
            "limit ?3 offset ?2" , nativeQuery = true
    )
    List<Storage> getAllStorageWithPageData(
            String name,
            Integer page,
            Integer pageSize
    );

    @Query(value = "select count(*) from farm_erp.storages s where " +
            "(?1 = 'none' or LOWER(s.name) like CONCAT('%',LOWER(?1),'%') ) and " +
            "s.is_deleted = false"
            , nativeQuery = true
    )
    Integer getAllStorageWithoutPageData(
            String name
    );

    List<Storage> findAllByIsDeleted(Boolean isDeleted);

    Optional<Storage> getStorageById(Integer id);

    boolean existsStorageById(Integer id);
}
