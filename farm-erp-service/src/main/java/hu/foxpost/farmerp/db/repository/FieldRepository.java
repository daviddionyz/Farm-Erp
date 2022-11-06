package hu.foxpost.farmerp.db.repository;

import hu.foxpost.farmerp.db.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FieldRepository extends JpaRepository<Field,Integer> {

    @Query(value = "select * from farm_erp.fields f where " +
            "(?1 = 'none' or LOWER(f.name) like CONCAT('%',LOWER(?1),'%') ) and" +
            "(?3 = 'none' or LOWER(f.corp_name) like CONCAT('%',LOWER(?3),'%') ) and" +
            "(?2 = 'none' or LOWER(f.corp_type) like CONCAT('%',LOWER(?2),'%') )  and " +
            "(f.is_deleted = false)" +
            "limit ?5 offset ?4" , nativeQuery = true
    )
    List<Field> getAllFieldWithPageData(
        String name,
        String corpType,
        String corpName,
        Integer page,
        Integer pageSize
    );

    @Query(value = "select count(*) from farm_erp.fields f where " +
            "(?1 = 'none' or LOWER(f.name) like CONCAT('%',LOWER(?1),'%') ) and" +
            "(?3 = 'none' or LOWER(f.corp_name) like CONCAT('%',LOWER(?3),'%') ) and" +
            "(?2 = 'none' or LOWER(f.corp_type) like CONCAT('%',LOWER(?2),'%') ) and " +
            "(f.is_deleted = false)"
            , nativeQuery = true
    )
    Integer getAllFieldWithoutPageData(
            String name,
            String corpType,
            String corpName
    );

    Optional<List<Field>> findAllByIsDeleted(Boolean isDeleted);

    Optional<Field> getFieldById(Integer id);
}
