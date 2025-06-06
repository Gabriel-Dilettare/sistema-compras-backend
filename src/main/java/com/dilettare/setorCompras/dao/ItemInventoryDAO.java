package com.dilettare.setorCompras.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dilettare.setorCompras.model.Inventory;

public class ItemInventoryDAO {

    protected Connection connection;

    public ItemInventoryDAO(Connection databaseConnection) {
        this.connection = databaseConnection;
    }

    public List<Inventory> getAllItemStockForManufacturer(List<String> manufacturers) throws SQLException {
        List<Inventory> inventories = new ArrayList<>();

        String inClause = manufacturers.stream()
                .map(m -> "?")
                .collect(Collectors.joining(", "));

        String sql = String.format("""
                    SELECT
                        i.codigo,
                        COALESCE(ii.saldoAtual, 0) AS estoque,
                        COALESCE(ii.custoatual, 0) as custo,

                        COALESCE((
                            SELECT
                                ROUND(
                                    COALESCE(n.valorunitario, 0) +
                                    COALESCE(n.valorsubstituicao, 0) / NULLIF(n.quantidade, 0) +
                                    COALESCE(n.valoripi, 0) / NULLIF(n.quantidade, 0) +
                                    COALESCE(n.outroscustos, 0) / NULLIF(n.quantidade, 0),
                                2)
                            FROM
                                itensnotas n
                            INNER JOIN
                                itensinicial ini ON ini.sequltimaentrada = n.sequencial
                            WHERE
                                ini.codigo = i.codigo
                                AND ini.filial = f.filial
                            FETCH FIRST 1 ROWS ONLY
                        ), 0) AS valor_impostos,

                        f.filial,

                        COALESCE((
                            SELECT
                                SUM(p.quantidade - p.quantidadecancelada - p.quantidadeentregue)
                            FROM
                                pedidos p
                            WHERE
                                (p.quantidade - p.quantidadecancelada - p.quantidadeentregue) > 0.1
                                AND p.item = i.codigo
                                AND p.filial = f.filial
                        ), 0) AS pendente

                    FROM
                        (SELECT DISTINCT codigo FROM itens WHERE fabricante IN (%s)) i

                    CROSS JOIN
                        (SELECT 5 AS filial FROM RDB$DATABASE
                         UNION ALL SELECT 9 FROM RDB$DATABASE
                         UNION ALL SELECT 11 FROM RDB$DATABASE
                         UNION ALL SELECT 15 FROM RDB$DATABASE
                         UNION ALL SELECT 17 FROM RDB$DATABASE) f

                    LEFT JOIN
                        itensinicial ii ON ii.codigo = i.codigo AND ii.filial = f.filial;
                """, inClause);

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            for (int i = 0; i < manufacturers.size(); i++) {
                stmt.setString(i + 1, manufacturers.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Inventory itemInventory = new Inventory();
                    itemInventory.setCodeItem(rs.getInt("codigo"));
                    itemInventory.setQuantityItemStock(rs.getDouble("estoque"));
                    itemInventory.setAverageItemCost(rs.getDouble("custo"));
                    itemInventory.setItemPriceTax(rs.getDouble("valor_impostos"));
                    itemInventory.setCodeStore(rs.getInt("filial"));
                    itemInventory.setQuantityItemPending(rs.getDouble("pendente"));
                    inventories.add(itemInventory);

                }
            }
        }

        return inventories;
    }

    public Double getItemSalesForCodeItemStoreAndDate(Integer codeItem, Integer store, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = """
                SELECT
                    SUM(IO.QUANTIDADE) AS QUANTIDADE
                FROM
                    ITENS i
                JOIN ITENSORCAMENTOS IO ON IO.ITEM = i.CODIGO
                JOIN ORCAMENTOS O ON (
                    O.SEQUENCIAL = IO.SEQUENCIALORCAMENTO
                )
                WHERE
                    i.codigo = ?
                    AND i.DATACANCELAMENTO IS NULL
                    AND O.FILIAL = ?
                    AND O.DATALIBERACAO BETWEEN ? AND ?
                    AND O.LIBERADO = 'S'
                GROUP BY
                    O.filial,
                    i.CODIGO
                """;

        try (PreparedStatement stmt = this.connection.prepareStatement(sql)) {
            stmt.setInt(1, codeItem);
            stmt.setInt(2, store);
            stmt.setObject(3, startDate);
            stmt.setObject(4, endDate);

             // Assuming 5 is the store code, adjust as necessary
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("QUANTIDADE");
                }
            }
        }

        return 0.0;
    }
}