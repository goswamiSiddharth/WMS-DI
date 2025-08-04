package com.example.WMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.WMS.model.MSKU;

@Repository
public interface MSKURepository extends JpaRepository<MSKU, Long> {
    Optional<MSKU> findByName(String name);
}
