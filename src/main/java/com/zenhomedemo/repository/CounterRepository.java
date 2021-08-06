package com.zenhomedemo.repository;

import com.zenhomedemo.model.Counter;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Long> {
}
