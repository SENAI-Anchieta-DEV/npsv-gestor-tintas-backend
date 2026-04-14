package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeDuplicadaException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
        if (repository.findByEmailAndAtivoTrue(dto.email()).isPresent()) {
            throw new EntidadeDuplicadaException("Este e-mail já está em uso.");
        }

        Usuario novoUsuario = dto.toEntity();
        novoUsuario.setSenha(encoder.encode(dto.senha()));

        return new UsuarioResponseDTO(repository.save(novoUsuario));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponseDTO> listarUsuariosAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO listarUsuarioPorEmail(String email) {
        var usuario = buscarUsuarioAtivoPorEmail(email);
        return new UsuarioResponseDTO(usuario);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO listarUsuarioPorId(String id) {
        var usuario = buscarUsuarioAtivoPorId(id);
        return new UsuarioResponseDTO(usuario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO atualizarUsuario(String email, UsuarioRequestDTO dto) {
        var usuario = buscarUsuarioAtivoPorEmail(email);

        usuario.setNome(dto.nome());
        usuario.setRole(dto.role());
        usuario.setSenha(encoder.encode(dto.senha()));
        return UsuarioResponseDTO.fromEntity(repository.save(usuario));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO atualizarSenhaUsuario(String email, String novaSenha) {
        var usuario = buscarUsuarioAtivoPorEmail(email);
        usuario.setSenha(encoder.encode(novaSenha));
        return UsuarioResponseDTO.fromEntity(repository.save(usuario));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletarUsuario(String email) {
        var usuario = buscarUsuarioAtivoPorEmail(email);
        usuario.setAtivo(false);
        repository.save(usuario);
    }

    private Usuario buscarUsuarioAtivoPorEmail(String email) {
        return repository.findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado ou inativo"));
    }

    private Usuario buscarUsuarioAtivoPorId(String id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado ou inativo"));
    }
}
