package com.senai.npsv_gestor_tintas_backend.interface_ui.exceptions;

import com.senai.npsv_gestor_tintas_backend.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.senai.npsv_gestor_tintas_backend.domain.exception.CredenciaisInvalidasException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =================================================================================
    // TRATAMENTO DE REGRAS DE NEGÓCIO ESPECÍFICAS (STATUS 422 - UNPROCESSABLE ENTITY)
    // =================================================================================

    @ExceptionHandler(EstoqueBaixoException.class)
    public ProblemDetail handleEstoqueBaixo(EstoqueBaixoException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Violação de Regra de Negócio: Estoque",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getCodigoErro() // Ex: RN02
        );
    }

    @ExceptionHandler(VendaBloqueadaException.class)
    public ProblemDetail handleVendaBloqueada(VendaBloqueadaException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Violação de Regra de Negócio: Venda",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getCodigoErro() // Ex: RN03
        );
    }

    @ExceptionHandler(MargemErroPesagemException.class)
    public ProblemDetail handleMargemErroPesagem(MargemErroPesagemException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Violação de Regra de Negócio: Pesagem",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getCodigoErro() // Ex: RN01
        );
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ProblemDetail handleRegraNegocioGenerica(RegraNegocioException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Violação de Regra de Negócio",
                ex.getMessage(),
                request.getRequestURI(),
                ex.getCodigoErro()
        );
    }

    // =================================================================================
    // TRATAMENTO DE ERROS DE RECURSO E ESTADO (STATUS 404 e 409)
    // =================================================================================

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

    // =================================================================================
    // TRATAMENTO DE ERROS DE SEGURANÇA E AUTENTICAÇÃO (STATUS 401 - UNAUTHORIZED)
    // =================================================================================

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail handleCredenciaisInvalidas(CredenciaisInvalidasException ex, HttpServletRequest request) {
        return ProblemDetailUtils.criarProblemDetail(
                HttpStatus.UNAUTHORIZED, // 401
                "Falha na Autenticação",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }
    // =================================================================================
    // TRATAMENTO DE ERROS DE VALIDAÇÃO DO SPRING (STATUS 400 - BAD REQUEST)
    // =================================================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetailUtils.criarProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Erro de validação",
                "Um ou mais campos no corpo da requisição são inválidos.",
                request.getRequestURI(),
                null
        );

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

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

        Map<String, String> errors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String campo = violation.getPropertyPath().toString();
            String mensagem = violation.getMessage();
            errors.put(campo, mensagem);
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
