package com.example.WMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.WMS.model.SKU;

@Repository
public interface SKURepository extends JpaRepository<SKU, Long> {
    Optional<SKU> findByCode(String code);
}
