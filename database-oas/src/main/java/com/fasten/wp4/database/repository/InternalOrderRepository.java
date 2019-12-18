package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.InternalOrder;

@Repository
public interface InternalOrderRepository extends JpaRepository<InternalOrder, Long>,  JpaLazyDataModel<InternalOrder>{

}
