package com.tallerjava.taller.repository;

import com.tallerjava.taller.model.Novedad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovedadRepository extends JpaRepository<Novedad, Integer> {
}

