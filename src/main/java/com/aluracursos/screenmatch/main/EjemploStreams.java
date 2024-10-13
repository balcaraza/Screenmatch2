package com.aluracursos.screenmatch.main;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Brenda", "Luis", "Maria", "Fernanda", "Erick");

        nombres.stream()
                .sorted()
                .limit(3)
                .filter(n -> n.startsWith("F"))
                .map(n -> n.toUpperCase())
                .forEach(System.out::println);
    }

}
