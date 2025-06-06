package com.dilettare.setorCompras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dilettare.setorCompras.model.Supplier;

public class SupplierDAO {

    protected Connection connection;

    public SupplierDAO(Connection databaseConnection) {
        this.connection = databaseConnection;
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = """
                SELECT
                    codigo,
                    nome,
                    razaoSocial,
                    endereco,
                    bairro,
                    cidade,
                    cep,
                    fone
                FROM
                    empresas
                WHERE
                    tipo = 'Fornecedor'
                    AND abreviatura = 'FN'
                    AND nome <> ''
                ORDER BY
                    nome
                """;

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier(rs.getInt("codigo"));
                    supplier.setNameSupplier(rs.getString("nome"));
                    supplier.setSocialNameSupplier(rs.getString("razaoSocial"));
                    supplier.setAddressSupplier(rs.getString("endereco"));
                    supplier.setNeighborhoodSupplier(rs.getString("bairro"));
                    supplier.setCitySupplier(rs.getString("cidade"));
                    supplier.setCEPSupplier(rs.getString("cep"));
                    supplier.setFoneSupplier(rs.getString("fone"));

                    suppliers.add(supplier);
                }
            }
        }

        return suppliers;
    }
}
