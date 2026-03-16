package com.senai.npsv_gestor_tintas_backend.interface_ui.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

public class ProblemDetailUtils {

    public static ProblemDetail criarProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            String instance,
            String codigoErro) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);

        problemDetail.setTitle(title);

        problemDetail.setType(URI.create("https://gestortintas.com/errors/regra-de-negocio"));
        problemDetail.setInstance(URI.create(instance));
        problemDetail.setProperty("timestamp", Instant.now());


        if (codigoErro != null && !codigoErro.isBlank()) {
            problemDetail.setProperty("codigoErro", codigoErro);
        }

        return problemDetail;
    }
}
