package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.Prediction;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long>,  JpaLazyDataModel<Prediction>{

}
