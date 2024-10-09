package com.aluracursos.screenmatch.models;

import com.google.gson.annotations.SerializedName;

public record DatosSerie(
        @SerializedName("Title") String titulo,
        @SerializedName("totalSeasons") Integer totalTemporadas,
        @SerializedName("imdbRating") String calificacion) {
}
