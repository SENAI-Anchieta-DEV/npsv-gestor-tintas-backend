package com.senai.npsv_gestor_tintas_backend.interface_ui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class ProblemDetailUtils {

    private static URI resolveType(HttpStatus status) {
        String baseUrl = "https://gestortintas.com/errors/";
        String path;
        if (status == HttpStatus.BAD_REQUEST) {
            path = "requisicao-invalida";
        } else if (status == HttpStatus.UNAUTHORIZED) {
            path = "nao-autenticado";
        } else if (status == HttpStatus.NOT_FOUND) {
            path = "nao-encontrado";
        } else if (status == HttpStatus.CONFLICT) {
            path = "conflito";
        } else {
            path = "regra-de-negocio";
        }
        return URI.create(baseUrl + path);
    }

    public static ProblemDetail criarProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            String instance,
            String codigoErro) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);

        problemDetail.setTitle(title);

        problemDetail.setType(resolveType(status));
        problemDetail.setInstance(URI.create(instance));
        problemDetail.setProperty("timestamp", Instant.now());


        if (codigoErro != null && !codigoErro.isBlank()) {
            problemDetail.setProperty("codigoErro", codigoErro);
        }

        return problemDetail;
    }
}
