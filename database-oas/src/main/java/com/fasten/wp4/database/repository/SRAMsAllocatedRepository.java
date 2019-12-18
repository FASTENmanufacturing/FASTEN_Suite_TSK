package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.SRAMsAllocated;

@Repository
public interface SRAMsAllocatedRepository extends JpaRepository<SRAMsAllocated, Long>,  JpaLazyDataModel<SRAMsAllocated>{

}
