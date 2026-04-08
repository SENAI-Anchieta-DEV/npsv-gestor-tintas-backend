package com.senai.npsv_gestor_tintas_backend.interface_ui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class ProblemDetailUtils {

    private static URI resolveType(HttpStatus status) {
        String baseUrl = "https://gestortintas.com/errors/";
        String path = switch (status) {
            case BAD_REQUEST -> "requisicao-invalida";
            case UNAUTHORIZED -> "nao-autenticado";
            case NOT_FOUND -> "nao-encontrado";
            case CONFLICT -> "conflito";
            case INTERNAL_SERVER_ERROR -> "erro-interno";
            case FORBIDDEN -> "proibido";
            case UNPROCESSABLE_ENTITY -> "entidade-nao-processavel";
            default -> "generico";
        };
        return URI.create(baseUrl + path);
    }

    public static ProblemDetail criarProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            String instance,
            String codigoRegraNegocio) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);

        problemDetail.setTitle(title);

        problemDetail.setType(resolveType(status));
        problemDetail.setInstance(URI.create(instance));
        problemDetail.setProperty("timestamp", Instant.now());


        if (codigoRegraNegocio != null && !codigoRegraNegocio.isBlank()) {
            problemDetail.setProperty("codigoRegraNegocio", codigoRegraNegocio);
        }

        return problemDetail;
    }
}
