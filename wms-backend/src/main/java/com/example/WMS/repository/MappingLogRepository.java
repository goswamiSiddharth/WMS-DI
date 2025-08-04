package com.example.WMS.repository;

import com.example.WMS.model.MappingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MappingLogRepository extends JpaRepository<MappingLog, Long> { }
