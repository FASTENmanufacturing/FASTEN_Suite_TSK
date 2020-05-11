package com.fasten.wp4.iot.kafka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.iot.kafka.model.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long>{

	Optional<Topic> findByName(String name);
	
}
