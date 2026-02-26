package com.senai.npsv_gestor_tintas_backend.domain.repository;

import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByEmailAndAtivoTrue(String email);
    Optional<Usuario> findByIdAndAtivoTrue(String id);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findAllByAtivoTrue();
}
