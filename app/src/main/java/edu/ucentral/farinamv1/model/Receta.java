package edu.ucentral.farinamv1.model;

import java.util.ArrayList;

public class Receta {
    private String recetaId;
    private String usuarioId; //usuario que creo la receta
    private String categoryaId; //Categoria a la que pertenece
    private double dificultad;
    private String descripcion;
    // 1 a muchos
    private ArrayList<Ingrediente> ingredient=new ArrayList<Ingrediente>(); //Lista de ingredientes
    private String pasos;
    private String utensil;
    private int tiempo;
    private double valoracion;
    private boolean enable;

}
