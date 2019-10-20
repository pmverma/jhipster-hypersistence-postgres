package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.ESix;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ESix entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ESixRepository extends JpaRepository<ESix, Long> {

}
