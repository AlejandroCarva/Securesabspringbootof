package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Integer> {
    // Métodos básicos son suficientes
}