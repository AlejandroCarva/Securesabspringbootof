package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Jornada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IJornadaRepository extends JpaRepository<Jornada, Integer> {
    // Métodos básicos ya vienen con JpaRepository
}