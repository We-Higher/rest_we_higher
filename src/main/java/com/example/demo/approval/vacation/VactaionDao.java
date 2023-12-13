package com.example.demo.approval.vacation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VactaionDao extends JpaRepository<Vacation, Integer> {
}
