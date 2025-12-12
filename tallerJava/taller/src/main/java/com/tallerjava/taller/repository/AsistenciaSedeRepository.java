package com.tallerjava.taller.repository;
import com.tallerjava.taller.model.AsistenciaSede;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenciaSedeRepository extends JpaRepository<AsistenciaSede, Integer> {

}
