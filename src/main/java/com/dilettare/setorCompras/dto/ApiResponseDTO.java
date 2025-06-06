package com.dilettare.setorCompras.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta genérica da API")
public class ApiResponseDTO<T> {

    @Schema(description = "Dados da resposta")
    private T result;

    @Schema(description = "Indica se a requisição foi bem-sucedida")
    private boolean success;

    @Schema(description = "Mensagem adicional")
    private String message;

    public ApiResponseDTO() {
    }

    public ApiResponseDTO(T result) {
        this.result = result;
        this.success = true;
        this.message = "Operação realizada com sucesso";
    }

    public ApiResponseDTO(T result, boolean success, String message) {
        this.result = result;
        this.success = success;
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
