package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
     boolean existsByCpf(String cpf);
     List<Cliente> findAllByAtivoTrue();
     Optional<Cliente> findByIdAndAtivoTrue(String id);
}
