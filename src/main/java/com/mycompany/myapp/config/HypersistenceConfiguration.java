
package com.mycompany.myapp.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;

/**
 * HypersistenceConfiguration
 */
@Configuration
public class HypersistenceConfiguration implements CommandLineRunner{

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void run(String... args) throws Exception {
        new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory)
        ).init();
    }
}