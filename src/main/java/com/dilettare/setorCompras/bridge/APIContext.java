package com.dilettare.setorCompras.bridge;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class APIContext extends DatabaseContext {

    public APIContext(@Value("${firebird.jdbc-url}") String urlFirebird) {
        super(urlFirebird);
    }
}
