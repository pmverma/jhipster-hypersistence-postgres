package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.EFour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the EFour entity.
 */
@Repository
public interface EFourRepository extends JpaRepository<EFour, Long> {

    @Query(value = "select distinct eFour from EFour eFour left join fetch eFour.eFives",
        countQuery = "select count(distinct eFour) from EFour eFour")
    Page<EFour> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct eFour from EFour eFour left join fetch eFour.eFives")
    List<EFour> findAllWithEagerRelationships();

    @Query("select eFour from EFour eFour left join fetch eFour.eFives where eFour.id =:id")
    Optional<EFour> findOneWithEagerRelationships(@Param("id") Long id);

}
