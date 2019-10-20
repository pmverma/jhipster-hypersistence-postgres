package com.mycompany.myapp.repository;
import com.mycompany.myapp.domain.ESeven;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ESeven entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ESevenRepository extends JpaRepository<ESeven, Long> {

}
