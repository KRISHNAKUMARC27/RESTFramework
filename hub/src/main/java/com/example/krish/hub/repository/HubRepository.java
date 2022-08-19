package com.example.krish.hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.krish.hub.entity.Hub;

@Repository
public interface HubRepository extends JpaRepository<Hub, Integer> {

}
