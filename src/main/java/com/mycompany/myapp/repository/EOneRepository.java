package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.EOne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EOne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EOneRepository extends JpaRepository<EOne, Long> {

}
