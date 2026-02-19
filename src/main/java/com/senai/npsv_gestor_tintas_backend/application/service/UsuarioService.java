package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.UsuarioResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Usuario;
import com.senai.npsv_gestor_tintas_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }

        Usuario novoUsuario = dto.toEntity();

        novoUsuario.setSenha(encoder.encode(dto.senha()));

        return new UsuarioResponseDTO(repository.save(novoUsuario));
    }

    public List<UsuarioResponseDTO> listarUsuariosAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    public UsuarioResponseDTO listarUsuarioPorEmail(String email) {
        var usuario = getUsuarioByEmail(email);
        return new UsuarioResponseDTO(usuario);
    }

    public UsuarioResponseDTO atualizarUsuario(String email, UsuarioRequestDTO dto) {
        var usuario = getUsuarioByEmail(email);

        usuario.setNome(dto.nome());
        usuario.setRole(dto.role());
        usuario.setAtivo(dto.ativo());
        usuario.setSenha(encoder.encode(dto.senha()));
        return UsuarioResponseDTO.fromEntity(repository.save(usuario));
    }

    public void deletarUsuario(String email) {
        var usuario = getUsuarioByEmail(email);
        usuario.setAtivo(false);
        repository.save(usuario);
    }

    private Usuario getUsuarioByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}
