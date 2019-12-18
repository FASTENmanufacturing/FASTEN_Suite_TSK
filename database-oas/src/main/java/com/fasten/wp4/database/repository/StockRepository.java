package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long>, JpaLazyDataModel<Stock>{

}
