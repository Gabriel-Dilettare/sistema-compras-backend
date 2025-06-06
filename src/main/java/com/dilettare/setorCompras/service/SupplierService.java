package com.dilettare.setorCompras.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.dilettare.setorCompras.bridge.DatabaseContext;
import com.dilettare.setorCompras.dao.SupplierDAO;
import com.dilettare.setorCompras.dto.SupplierDTO;
import com.dilettare.setorCompras.exception.AuthorizationException;
import com.dilettare.setorCompras.model.Supplier;

public class SupplierService {

    protected DatabaseContext databaseContext;
    private final String username;

    public SupplierService(DatabaseContext databaseContext,
            String username) {
        this.databaseContext = databaseContext;
        this.username = username;

    }

    public List<SupplierDTO> getAllSuppliers() throws SQLException, AuthorizationException {
        try (Connection databaseConnection = this.databaseContext.getDatabaseConnectionFirebird(this.username)) {
            SupplierDAO supplierDAO = new SupplierDAO(databaseConnection);

            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            return suppliers.stream()
                    .map(SupplierDTO::new)
                    .collect(Collectors.toList());
        }
    }
}
