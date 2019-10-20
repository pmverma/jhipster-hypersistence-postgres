package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.ETwo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ETwo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ETwoRepository extends JpaRepository<ETwo, Long> {

}
