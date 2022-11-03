package edu.ucentral.farinamv1.model;

public class Notificacion {
    private String id;
    private String usuarioId;
    private String notificacion;
    private String usuarioReceta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNotificacion() {
        return notificacion;
    }

    public void setNotificacion(String notificacion) {
        this.notificacion = notificacion;
    }

    public String getUsuarioReceta() {
        return usuarioReceta;
    }

    public void setUsuarioReceta(String usuarioReceta) {
        this.usuarioReceta = usuarioReceta;
    }

    @Override
    public String toString() {
        return notificacion;
    }
}
