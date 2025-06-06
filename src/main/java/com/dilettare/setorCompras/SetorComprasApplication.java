package com.dilettare.setorCompras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "API do Setor de Compras - Dilettare", version = "1.0", description = "API de uso exclusivo do setor de compras da Dilettare, empresa do segmento alimentício, desenvolvida para unificar os processos de cadastro, transferência, compra e gestão de itens, integrando os sistemas de atacado (Gestão) e varejo (ERP Info)."))
@SpringBootApplication
public class SetorComprasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SetorComprasApplication.class, args);
    }

}
