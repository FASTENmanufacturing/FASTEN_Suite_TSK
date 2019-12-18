package com.fasten.wp4.iot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.iot.model.Listner;

@Repository
public interface ListnerRepository extends JpaRepository<Listner, Long>{

}
