package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.exceptions.EntidadeDuplicadaException;
import com.senai.npsv_gestor_tintas_backend.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    @PreAuthorize("hasAnyRole('ADMIN')")
    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
        if (repository.findByEmailAndAtivoTrue(dto.email()).isPresent()) {
            throw new EntidadeDuplicadaException("Este e-mail já está em uso.");
        }

        Usuario novoUsuario = dto.toEntity();
        novoUsuario.setSenha(encoder.encode(dto.senha()));

        return new UsuarioResponseDTO(repository.save(novoUsuario));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UsuarioResponseDTO> listarUsuariosAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public UsuarioResponseDTO listarUsuarioPorEmail(String email) {
        var usuario = buscarUsuarioAtivoPorEmail(email);
        return new UsuarioResponseDTO(usuario);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public UsuarioResponseDTO listarUsuarioPorId(String id) {
        var usuario = BuscarUsuarioAtivoPorId(id);
        return new UsuarioResponseDTO(usuario);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public UsuarioResponseDTO atualizarUsuario(String email, UsuarioRequestDTO dto) {
        var usuario = buscarUsuarioAtivoPorEmail(email);

        usuario.setNome(dto.nome());
        usuario.setRole(dto.role());
        usuario.setSenha(encoder.encode(dto.senha()));
        return UsuarioResponseDTO.fromEntity(repository.save(usuario));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public UsuarioResponseDTO atualizarSenhaUsuario(String email, String novaSenha) {
        var usuario = buscarUsuarioAtivoPorEmail(email);
        usuario.setSenha(encoder.encode(novaSenha));
        return UsuarioResponseDTO.fromEntity(repository.save(usuario));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public void deletarUsuario(String email) {
        var usuario = buscarUsuarioAtivoPorEmail(email);
        usuario.setAtivo(false);
        repository.save(usuario);
    }

    private Usuario buscarUsuarioAtivoPorEmail(String email) {
        return repository.findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado ou inativo"));
    }

    private Usuario BuscarUsuarioAtivoPorId(String id) {
        return repository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado ou inativo"));
    }
}
