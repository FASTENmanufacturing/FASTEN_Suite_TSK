package com.fasten.wp4.iot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.iot.model.Producer;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long>{

	Optional<Producer> findByName(String name);
	
}
