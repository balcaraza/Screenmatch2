package com.aluracursos.screenmatch.main;

import com.aluracursos.screenmatch.models.DatosEpisodio;
import com.aluracursos.screenmatch.models.DatosSerie;
import com.aluracursos.screenmatch.models.DatosTemporadas;
import com.aluracursos.screenmatch.models.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE ="https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6d1d6576";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraMenu(){
        System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);

        //busca los datos de todas las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();

        for (int i =1; i <=datos.totalTemporadas(); i++){
            json = consumoAPI.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        temporadas.forEach(System.out::println);

        //mostrar solo el titulo de los episodios para las temporadas
        /*
        for (int i = 0; i < datos.totalTemporadas(); i++) {
            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }*/
        //remplazando for con expresion lambda
       // temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //convertir toda la informacion a una lista tipo DatosEpisodio

        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        //top 5 episodios
        /*System.out.println("top 5 episodios");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("-------------Primer Filtro (N/A)" +e + "-------"))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("-------------Segundo Filtro (Ordenar de M>m)" +e + "-------"))
                .map(e-> e.titulo().toUpperCase())
                .peek(e -> System.out.println("-------------Tercer Filtro (Convirtiendo a Mayusculas)" +e + "-------"))
                .limit(5)
                .forEach(System.out::println);
        */
        System.out.println("++++++++++++++++++++++++++++++++++++++++++");
        //Convirtiendo los datos a una lista tipo episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
        //busqueda de episiodios a partir de año especifico
        /*
        System.out.println("indica el año a partir del cual deseas ver los episodios");
        var fecha = teclado.nextInt();
        teclado.nextLine();

        LocalDate fechaBusqueda = LocalDate.of(fecha,1,1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e-> e.getFechaDeLanzamiento() !=null && e.getFechaDeLanzamiento().isAfter(fechaBusqueda))
                .forEach(e-> System.out.println(
                        " Temporada " + e.getTemporada() +
                                " Episodio " + e.getTitulo() +
                                " Fecha de lanzamiento del episodio" + e.getFechaDeLanzamiento().format(dtf)
                ));

        // Busca episodio por pedazo del titulo
        System.out.println("Por favor escribe el titulo del episodio que desea ver");
        var pedazoTitulo = teclado.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
                .findFirst();
        if (episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado");
            System.out.println(" Los datos son: " + episodioBuscado.get());
        } else {
            System.out.println("Episodio no encontrado");
        }

         */

        Map<Integer , Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e -> e.getEvaluacion()>0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(evaluacionesPorTemporada);
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getEvaluacion()>0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media de evaluaciones " +est.getAverage());
        System.out.println("Episodio mejor evaluado " +est.getMax());
        System.out.println("Episodio peor evaluado " +est.getMin());

    }
}
