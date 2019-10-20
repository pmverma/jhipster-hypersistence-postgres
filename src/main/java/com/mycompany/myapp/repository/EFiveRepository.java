package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.EFive;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EFive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EFiveRepository extends JpaRepository<EFive, Long> {

}
