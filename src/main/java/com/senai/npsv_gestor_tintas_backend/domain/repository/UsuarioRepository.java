package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}
