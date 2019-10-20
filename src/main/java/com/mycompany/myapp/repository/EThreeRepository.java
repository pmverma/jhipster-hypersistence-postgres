package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.EThree;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EThree entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EThreeRepository extends JpaRepository<EThree, Long> {

}
