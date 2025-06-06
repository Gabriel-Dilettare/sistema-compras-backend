package com.dilettare.setorCompras.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dilettare.setorCompras.bridge.DatabaseContext;
import com.dilettare.setorCompras.dao.ItemDAO;
import com.dilettare.setorCompras.dao.ItemInventoryDAO;
import com.dilettare.setorCompras.dao.ManufacturerDAO;
import com.dilettare.setorCompras.dto.InventoryDTO;
import com.dilettare.setorCompras.dto.ItemInventoryDTO;
import com.dilettare.setorCompras.dto.ItemManufacturerDTO;
import com.dilettare.setorCompras.dto.ManufacturerDTO;
import com.dilettare.setorCompras.exception.AuthorizationException;
import com.dilettare.setorCompras.model.Inventory;
import com.dilettare.setorCompras.model.Item;

public class ItemService {
    protected DatabaseContext databaseContext;
    private final String username;

    public ItemService(DatabaseContext databaseContext,
            String username) {
        this.databaseContext = databaseContext;
        this.username = username;
    }

    public List<ItemManufacturerDTO> getAllItemForManufacturer(List<String> manufacturers)
            throws SQLException, AuthorizationException {
        try (Connection databaseConnection = this.databaseContext.getDatabaseConnectionFirebird(this.username)) {

            ItemDAO itemDAO = new ItemDAO(databaseConnection);
            ManufacturerDAO manufacturerDAO = new ManufacturerDAO(databaseConnection);
            ItemInventoryDAO itemInventoryDAO = new ItemInventoryDAO(databaseConnection);

            // Buscar estoques e itens
            List<Inventory> inventories = itemInventoryDAO.getAllItemStockForManufacturer(manufacturers);
            List<Item> items = itemDAO.getAllItemForManufacturer(manufacturers);

            // Mapear fabricantes
            Map<String, ManufacturerDTO> manufacturerMap = manufacturerDAO.getManufacturerInfo(manufacturers)
                    .stream()
                    .collect(Collectors.toMap(info -> info.name, info -> info));

            // Mapear inventários por código do item
            Map<Integer, List<InventoryDTO>> inventoryMap = inventories.stream()
                    .collect(Collectors.groupingBy(
                            Inventory::getCodeItem,
                            Collectors.mapping(inv -> {
                                InventoryDTO storeDTO = new InventoryDTO();
                                storeDTO.codeStore = inv.getCodeStore();
                                storeDTO.quantityItemStock = inv.getQuantityItemStock();
                                storeDTO.averageItemCost = inv.getAverageItemCost();
                                storeDTO.itemPriceTax = inv.getItemPriceTax();
                                storeDTO.quantityItemPending = inv.getQuantityItemPending();
                                return storeDTO;
                            }, Collectors.toList())));

            // Agrupar itens por fabricante e montar o retorno
            return items.stream()
                    .collect(Collectors.groupingBy(Item::getManufacturerItem))
                    .entrySet()
                    .stream()
                    .map(entry -> {
                        String manufacturer = entry.getKey();
                        ManufacturerDTO info = manufacturerMap.get(manufacturer);

                        List<ItemInventoryDTO> itemDTOList = entry.getValue().stream()
                                .map(item -> {
                                    List<InventoryDTO> storeList = inventoryMap.getOrDefault(item.getCodeItem(), List.of());
                                    return new ItemInventoryDTO(item, storeList);
                                })
                                .collect(Collectors.toList());

                        return new ItemManufacturerDTO(
                                manufacturer,
                                info != null ? info.deliveryTime : null,
                                info != null ? info.replacementDays : null,
                                itemDTOList);
                    })
                    .collect(Collectors.toList());
        }
    }
}
