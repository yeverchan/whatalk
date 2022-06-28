package com.whatalk.gatewayservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(-1)
@RequiredArgsConstructor
@Component
public class GwExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpResponse response = exchange.getResponse();
        ErrorResponse errorResponse = new ErrorResponse(response.getRawStatusCode(), ex.getMessage());

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
            errorResponse.setCode(response.getRawStatusCode());
            errorResponse.setMessage(((ResponseStatusException) ex).getReason());
        } else {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
        }

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

        return
                response.writeWith(Mono.fromSupplier(() -> {

                            DataBufferFactory buffer = response.bufferFactory();

                            try {
                                byte[] error = objectMapper.writeValueAsBytes(errorResponse);

                                return buffer.wrap(error);
                            } catch (Exception e) {
                                return buffer.wrap(new byte[0]);
                            }
                        })
                );
    }
}
