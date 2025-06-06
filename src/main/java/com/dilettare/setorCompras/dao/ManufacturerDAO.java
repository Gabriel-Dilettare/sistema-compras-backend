package com.dilettare.setorCompras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dilettare.setorCompras.dto.ManufacturerDTO;
import com.dilettare.setorCompras.model.Manufacturer;

public class ManufacturerDAO {

    protected Connection connection;

    public ManufacturerDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Manufacturer> findAllByBuyer(String buyer, String role) throws SQLException {
        List<Manufacturer> manufacturers = new ArrayList<>();

        String sql = """
                SELECT
                    fabricante

                FROM
                    itens

                WHERE
                    datacancelamento IS NULL
                    AND fabricante NOT IN ('DESCONTINUAD')
                    """ + (role.equals("ADMIN") ? "" : "AND categoria3 = ? ") + """

                GROUP BY
                    fabricante

                ORDER BY
                    fabricante
                """;

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            if (!role.equals("ADMIN")) {
                stmt.setString(1, buyer);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("FABRICANTE");
                    manufacturers.add(new Manufacturer(name));
                }
            }
        }

        return manufacturers;
    }

    public List<ManufacturerDTO> getManufacturerInfo(List<String> manufacturers) throws SQLException {
        String inClause = manufacturers.stream()
                .map(m -> "?")
                .collect(Collectors.joining(", "));

        String sql = String.format("""
                    SELECT
                        i.fabricante AS manufacturer,
                        (SELECT ii.saldominimo
                        FROM itensinicial ii
                        WHERE ii.codigo = i.codigo AND ii.filial = 5) AS deliveryTime,
                        (SELECT ii.saldomaximo
                        FROM itensinicial ii
                        WHERE ii.codigo = i.codigo AND ii.filial = 5) AS replacementDays
                    FROM itens i
                    INNER JOIN (
                        SELECT fabricante, MIN(codigo) AS minCodigo
                        FROM itens
                        WHERE fabricante IN (%s)
                        GROUP BY fabricante
                    ) firstItem ON firstItem.fabricante = i.fabricante AND firstItem.minCodigo = i.codigo
                            """, inClause);

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            for (int i = 0; i < manufacturers.size(); i++) {
                stmt.setString(i + 1, manufacturers.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                List<ManufacturerDTO> result = new ArrayList<>();

                while (rs.next()) {
                    String manufacturer = rs.getString("manufacturer");
                    Integer deliveryTime = rs.getInt("deliveryTime");
                    if (rs.wasNull())
                        deliveryTime = 0;

                    Integer replacementDays = rs.getInt("replacementDays");
                    if (rs.wasNull())
                        replacementDays = 0;

                    result.add(new ManufacturerDTO(manufacturer, deliveryTime, replacementDays));
                }

                return result;
            }
        }
    }
}