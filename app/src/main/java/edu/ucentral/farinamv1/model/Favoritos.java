package edu.ucentral.farinamv1.model;

import java.util.ArrayList;

public class Favoritos {

    private String usuarioId;
    private ArrayList<Receta> recetas;

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public ArrayList<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(ArrayList<Receta> recetas) {
        this.recetas = recetas;
    }

    @Override
    public String toString() {
        return "Favoritos{" +
                "usuarioId='" + usuarioId + '\'' +
                ", recetas=" + recetas +
                '}';
    }
}
