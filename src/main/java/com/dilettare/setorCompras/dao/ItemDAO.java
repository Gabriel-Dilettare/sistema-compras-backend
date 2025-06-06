package com.dilettare.setorCompras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dilettare.setorCompras.model.Item;

public class ItemDAO {
    protected Connection connection;

    public ItemDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Item> getAllItemForManufacturer(List<String> manufacturers) throws SQLException {
        List<Item> itens = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                    SELECT
                        I.CODIGO as codigo,
                        I.DESCRICAO as descricao,
                        I.CODIGOBARRAS as codigobarras,
                        I.FABRICANTE as fabricante,
                        I.EMBALAGEM as embalagem,
                        CASE
                            I.FABRICANTE
                        WHEN
                            'SEPAC'
                        THEN
                            I.MEDIDA
                        ELSE
                            I.PESO
                        END as pesoMedida
                    FROM
                        ITENS as I
                """);

        String placeholders = String.join(", ", manufacturers.stream().map(m -> "?").toArray(String[]::new));
        sql.append(" WHERE I.FABRICANTE IN (").append(placeholders).append(")");

        sql.append("""
                    ORDER BY
                        I.DESCRICAO
                """);

        try (PreparedStatement stmt = this.connection.prepareStatement(sql.toString())) {
            int index = 1;

            for (String manufacturer : manufacturers) {
                stmt.setString(index++, manufacturer);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item(rs.getInt("codigo"));
                    item.setDescriptionItem(rs.getString("descricao"));
                    item.setBarcodeItem(rs.getString("codigobarras"));
                    item.setManufacturerItem(rs.getString("fabricante"));
                    item.setPackagingItem(rs.getInt("embalagem"));
                    item.setWeightMeasureItem(rs.getString("pesoMedida"));

                    itens.add(item);
                }
            }
        }

        return itens;
    }
}
