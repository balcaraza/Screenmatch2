package com.aluracursos.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosSerie(
        @JsonProperty("Title") String titulo,
        @JsonProperty("totalSeasons") Integer totalTemporadas,
        @JsonProperty("imdbRating") String calificacion) {
}
