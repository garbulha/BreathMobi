package com.example.admin.uber_tcc_sqlite.model;

import java.util.HashMap;

/**
 * Created by Admin on 29/02/2016.
 */
public class HMAux extends HashMap<String, String> {
    public static final String ID = "id";
    public static final String TEXTO01 = "texto01";
    public static final String TEXTO02 = "texto02";
    public static final String TEXTO03 = "texto03";
    public static final String TEXTO04 = "texto04";
    public static final String TEXTO05 = "texto05";

    @Override
    public String toString() {
        return get(TEXTO01);
    }
}
