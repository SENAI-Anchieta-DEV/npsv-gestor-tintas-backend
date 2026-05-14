package com.senai.npsv_gestor_tintas_backend.interface_ui.exceptions;

import com.senai.npsv_gestor_tintas_backend.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CredenciaisInvalidasException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ProblemDetail handleEstoqueInsuficiente(EstoqueInsuficienteException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Estoque Insuficiente",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getCodigoRegraNegocio()
        );
    }

    @ExceptionHandler(MargemErroPesagemException.class)
    public ProblemDetail handleMargemErroPesagem(MargemErroPesagemException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Margem de Erro na Pesagem",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getCodigoRegraNegocio()
        );
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ProblemDetail handleRegraNegocioGenerica(RegraNegocioException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Violação de Regra de Negócio",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getCodigoRegraNegocio()
        );
    }

    @ExceptionHandler(ProducaoSemPesagemException.class)
    public ProblemDetail handleProducaoSemPesagem(ProducaoSemPesagemException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Produção sem Pesagem",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }


    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado.",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(EntidadeDuplicadaException.class)
    public ProblemDetail handleEntidadeDuplicada(EntidadeDuplicadaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.CONFLICT,
                "Conflito de dados.",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(TransicaoDeStatusInvalidaException.class)
    public ProblemDetail handleTransicaoInvalida(TransicaoDeStatusInvalidaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.CONFLICT,
                "Conflito de Status",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(CodigoJaExisteException.class)
    public ProblemDetail handleCodigoJaExiste(CodigoJaExisteException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.CONFLICT,
                "Código já existente",
                ex.getMessage(),
                request.getRequestURI(),
                "RN04 – Unicidade de Código"
        );
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ProblemDetail handleCpfJaCadastrado(CpfJaCadastradoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.CONFLICT,
                "CPF já cadastrado",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(VinculoExistenteException.class)
    public ProblemDetail handleVinculoExistente(VinculoExistenteException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.CONFLICT,
                "Exclusão Bloqueada",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(CnpjJaCadastradoException.class)
    public ProblemDetail handleCnpjJaCadastrado(CnpjJaCadastradoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.CONFLICT,
                "CNPJ já cadastrado",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(PedidoNaoEditavelException.class)
    public ProblemDetail handlePedidoNaoEditavel(PedidoNaoEditavelException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.CONFLICT,
                "Pedido Não Editável",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }


    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail handleCredenciaisInvalidas(CredenciaisInvalidasException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Credenciais Inválidas",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Não Autenticado",
                "Necessário enviar um token JWT válido para acessar este recurso.",
                request.getRequestURI(),
                null
        );
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.FORBIDDEN,
                "Acesso Negado",
                "Você não tem as permissões necessárias para executar esta ação.",
                request.getRequestURI(),
                null
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.criarProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Um ou mais campos no corpo da requisição são inválidos.",
                request.getRequestURI(),
                null
        );

        Map<String, List<String>> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String campo = error.getField();
            String mensagem = error.getDefaultMessage();
            errors.computeIfAbsent(campo, k -> new ArrayList<>()).add(mensagem);
        });

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.criarProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Erro de validação nos parâmetros",
                "Um ou mais parâmetros enviados são inválidos.",
                request.getRequestURI(),
                null
        );

        Map<String, List<String>> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String caminhoCompleto = violation.getPropertyPath().toString();
            String campo = caminhoCompleto.substring(caminhoCompleto.lastIndexOf('.') + 1);
            String mensagem = violation.getMessage();
            errors.computeIfAbsent(campo, k -> new ArrayList<>()).add(mensagem);
        });

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Tipo de parâmetro inválido",
                String.format("O parâmetro '%s' deve ser do tipo '%s'. Valor recebido: '%s'",
                        ex.getName(),
                        ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconhecido",
                        ex.getValue()),
                request.getRequestURI(),
                null
        );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Requisição Inválida",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }


}
