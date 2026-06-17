package com.senai.npsv_gestor_tintas_backend.application.service;

import com.senai.npsv_gestor_tintas_backend.application.dto.cliente.ClienteRequestDTO;
import com.senai.npsv_gestor_tintas_backend.application.dto.cliente.ClienteResponseDTO;
import com.senai.npsv_gestor_tintas_backend.domain.entity.Cliente;
import com.senai.npsv_gestor_tintas_backend.domain.exception.VinculoExistenteException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CpfJaCadastradoException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.npsv_gestor_tintas_backend.domain.repository.ClienteRepository;
import com.senai.npsv_gestor_tintas_backend.domain.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VendaRepository vendaRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ClienteResponseDTO registrarCliente(ClienteRequestDTO dto) {
        if (clienteRepository.existsByCpf(dto.cpf())) {
            throw new CpfJaCadastradoException("CPF já cadastrado.");
        }

        Cliente cliente = dto.toEntity();
        return ClienteResponseDTO.fromEntity(clienteRepository.saveAndFlush(cliente));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public List<ClienteResponseDTO> listarClientesAtivos() {
        return clienteRepository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ClienteResponseDTO listarClientePorId(String id) {
        Cliente cliente = buscarClienteAtivoPorId(id);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ClienteResponseDTO atualizarCliente(String id, ClienteRequestDTO dto) {
        Cliente clienteExistente = buscarClienteAtivoPorId(id);

        if (!clienteExistente.getCpf().equals(dto.cpf()) && clienteRepository.existsByCpf(dto.cpf())) {
            throw new CpfJaCadastradoException("CPF já cadastrado.");
        }

        clienteExistente.setNome(dto.nome());
        clienteExistente.setCpf(dto.cpf());
        clienteExistente.setEmail(dto.email());
        clienteExistente.setTelefone(dto.telefone());
        clienteExistente.setEndereco(dto.endereco());

        return ClienteResponseDTO.fromEntity(clienteRepository.save(clienteExistente));
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deletarCliente(String id) {
        Cliente cliente = buscarClienteAtivoPorId(id);

        if (vendaRepository.existsByClienteId(id)) {
            throw new VinculoExistenteException("O cliente possui vendas vinculadas e não pode ser removido.");
        }

        clienteRepository.delete(cliente);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void desativarCliente(String id) {
        Cliente cliente = buscarClienteAtivoPorId(id);
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    private Cliente buscarClienteAtivoPorId(String id) {
        return clienteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado ou inativo com o ID: " + id));
    }
}
