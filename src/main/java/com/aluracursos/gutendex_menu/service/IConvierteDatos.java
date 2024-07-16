package com.aluracursos.gutendex_menu.service;

public interface IConvierteDatos {
    <T> T convertirDatos(String json, Class<T> clase);
}
