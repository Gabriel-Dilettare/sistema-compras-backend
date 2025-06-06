package com.dilettare.setorCompras.bridge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.dilettare.setorCompras.exception.AuthorizationException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

public abstract class DatabaseContext {
    private final String urlFirebird;
    private final Map<String, HikariDataSource> firebirdDataSources = new HashMap<>();

    public Map<String, HikariDataSource> getFirebirdDataSources() {
        return firebirdDataSources;
    }

    public DatabaseContext(String urlFirebird) {
        this.urlFirebird = urlFirebird;
    }

    public boolean initializeUserConnectionPools(String username, String firebirdPassword) {

        if (!firebirdDataSources.containsKey(username)) {
            System.out.println("Inicializando pool de conexão Firebird para o usuário: " + username);
            try {
                firebirdDataSources.put(username, createDataSource("firebird", username, firebirdPassword));
                System.out.println("Pool de conexão Firebird criado para o usuário: " + username);
                return true;
            } catch (Exception e) {
                System.err.println("Erro ao criar conexão para " + username + ": " + e.getMessage());
                return false;
            }
        } else {
            if (checkFirebirdCredentials(this.urlFirebird, username, firebirdPassword)) {
                System.out.println("Pool de conexão Firebird já existe para o usuário: " + username);
                return true;
            } else {
                System.err.println("Erro ao criar conexão para " + username + ": Usuário ou senha inválidos");
                return false;
            }

        }
    }

    public HikariDataSource createDataSource(String database, String username, String password) {
        String url = "";

        if (database.equals("firebird")) {
            url = this.urlFirebird;
            if (!checkFirebirdCredentials(url, username, password)) {
                throw new IllegalArgumentException("Invalid credentials for Firebird database");
            }
        }

        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("jdbcUrl is required for " + database);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(urlFirebird);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(2);
        config.setIdleTimeout(900_000);
        config.setConnectionTimeout(20_000);
        config.setMaxLifetime(1_800_000);
        config.setAutoCommit(true);
        config.setLeakDetectionThreshold(60_000);
        return new HikariDataSource(config);
    }

    private boolean checkFirebirdCredentials(String url, String username, String password) {
        try (@SuppressWarnings("unused")
        Connection ignored = DriverManager.getConnection(url, username, password)) {
            return true;
        } catch (SQLException e) {
            System.err.println("Erro na conexão com Firebird: " + e.getMessage());
            return false;
        }
    }

    public Connection getDatabaseConnectionFirebird(String username) throws SQLException, AuthorizationException {
        HikariDataSource ds = firebirdDataSources.get(username);
        if (ds == null) {
            throw new AuthorizationException("Acesso negado: conexão não inicializada.");
        }
        return ds.getConnection();
    }

    public void printPoolStats(String username) {
        HikariDataSource ds = firebirdDataSources.get(username);
        if (ds != null) {
            HikariPoolMXBean pool = ds.getHikariPoolMXBean();
            System.out.printf("User: %s | Total: %d | Active: %d | Idle: %d | Waiting: %d%n",
                    username,
                    pool.getTotalConnections(),
                    pool.getActiveConnections(),
                    pool.getIdleConnections(),
                    pool.getThreadsAwaitingConnection());
        }
    }
}