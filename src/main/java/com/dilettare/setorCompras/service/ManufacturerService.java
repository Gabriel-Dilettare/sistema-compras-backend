package com.dilettare.setorCompras.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.dilettare.setorCompras.bridge.DatabaseContext;
import com.dilettare.setorCompras.dao.ManufacturerDAO;
import com.dilettare.setorCompras.dto.ManufacturerDTO;
import com.dilettare.setorCompras.exception.AuthorizationException;
import com.dilettare.setorCompras.model.Manufacturer;

public class ManufacturerService {
    protected DatabaseContext databaseContext;
    private final String username;

    public ManufacturerService(DatabaseContext databaseContext,
            String username) {
        this.databaseContext = databaseContext;
        this.username = username;

    }

    public List<ManufacturerDTO> getManufacturersByBuyer(String buyer, String role)
            throws SQLException, AuthorizationException {
        try (Connection databaseConnection = this.databaseContext.getDatabaseConnectionFirebird(this.username)) {
            ManufacturerDAO manufacturerDAO = new ManufacturerDAO(databaseConnection);

            List<Manufacturer> manufacturers = manufacturerDAO.findAllByBuyer(buyer, role);
            return manufacturers.stream()
                    .map(m -> new ManufacturerDTO(m.getName()))
                    .collect(Collectors.toList());
        }
    }
}
