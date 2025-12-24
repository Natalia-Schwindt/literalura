package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.service.ConsumoAPI;

public class Principal {
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";

    public void muestraElMenu() {
        var json = consumoApi.obtenerDatos(URL_BASE);
        System.out.println(json);
    }
}