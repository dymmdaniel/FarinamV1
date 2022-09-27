package edu.ucentral.farinamv1.model;

public class Ingrediente {

    private String ingredienteId;
    private String nombre;
    private boolean enable;

    public String getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(String ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
