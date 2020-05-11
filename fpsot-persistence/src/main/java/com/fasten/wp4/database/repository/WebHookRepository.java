package com.fasten.wp4.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.WebHook;

@Repository
public interface WebHookRepository extends JpaRepository<WebHook, Long>, JpaLazyDataModel<WebHook>{

	List<WebHook> findByEvent(String event);

	Optional<WebHook> findByConsumerServiceName(String consumerServiceName);
}
