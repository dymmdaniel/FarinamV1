package edu.ucentral.farinamv1.model;

import java.util.ArrayList;

public class Receta {
    private String recetaId;
    private String usuarioId; //usuario que creo la receta
    private String categoriaId; //Categoria a la que pertenece
    private double dificultad;
    private String titulo;
    private String descripcion;
    // 1 a muchos
    private ArrayList<Ingrediente> ingredient=new ArrayList<Ingrediente>(); //Lista de ingredientes
    private String pasos;
    //private String utensil;
    private String image;
    private int tiempo;
    private double valoracion;
    private boolean enable;
    private int like;
    private int dislike;
    private String calorias;
    private String usuarioNombre;

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public String getRecetaId() {
        return recetaId;
    }

    public void setRecetaId(String recetaId) {
        this.recetaId = recetaId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public double getDificultad() {
        return dificultad;
    }

    public void setDificultad(double dificultad) {
        this.dificultad = dificultad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<Ingrediente> getIngredient() {
        return ingredient;
    }

    public void setIngredient(ArrayList<Ingrediente> ingredient) {
        this.ingredient = ingredient;
    }

    public String getPasos() {
        return pasos;
    }

    public void setPasos(String pasos) {
        this.pasos = pasos;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "recetaId='" + recetaId + '\'' +
                "Titulo='" + titulo + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", categoriaId='" + categoriaId + '\'' +
                ", dificultad=" + dificultad +
                ", descripcion='" + descripcion + '\'' +
                ", ingredient=" + ingredient +
                ", pasos='" + pasos + '\'' +
                ", tiempo=" + tiempo +
                ", valoracion=" + valoracion +
                ", enable=" + enable +
                ", like=" + like +
                ", dislike=" + dislike +
                '}';
    }
}
