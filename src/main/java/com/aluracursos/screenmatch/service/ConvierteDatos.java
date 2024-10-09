package com.aluracursos.screenmatch.service;
import com.google.gson.Gson;

public class ConvierteDatos implements IConvierteDatos{
    Gson gson = new Gson();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        return gson.fromJson(json, clase);
    }
}
