package com.fasten.wp4.iot.kafka.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.iot.kafka.model.Listner;

@Repository
public interface ListnerRepository extends JpaRepository<Listner, Long>{

}
