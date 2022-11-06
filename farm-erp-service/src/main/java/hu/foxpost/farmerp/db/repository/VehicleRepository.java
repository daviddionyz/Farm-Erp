package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {

    @Query(value = "select * from farm_erp.vehicles s where " +
            "(?1 = 'none' or LOWER(s.name) like CONCAT('%',LOWER(?1),'%') ) and" +
            "(?2 = 'none' or LOWER(s.type) like CONCAT('%',LOWER(?2),'%') ) and" +
            "(s.is_deleted = false ) and" +
            "(?3 = '-1' or s.status = ?3 ) " +
            "limit ?5 offset ?4" , nativeQuery = true
    )
    List<Vehicle> getAllVehiclesWithPageData(
            String name,
            String type,
            Integer status,
            Integer page,
            Integer pageSize
    );

    @Query(value = "select count(*) from farm_erp.vehicles s where " +
            "(?1 = 'none' or LOWER(s.name) like CONCAT('%',LOWER(?1),'%') ) and" +
            "(?2 = 'none' or LOWER(s.type) like CONCAT('%',LOWER(?2),'%') ) and" +
            "(s.is_deleted = false ) and" +
            "(?3 = '-1' or s.status = ?3 ) "
            , nativeQuery = true
    )
    Integer getAllVehiclesWithoutPageData(
            String name,
            String type,
            Integer status
    );

    List<Vehicle> findAllByIsDeleted(Boolean isDeleted);

//    Optional<List<VehiclesEntity>> findAllBy

    Optional<Vehicle> getVehiclesById(Integer id);

}
