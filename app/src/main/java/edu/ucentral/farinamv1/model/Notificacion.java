package edu.ucentral.farinamv1.model;

public class Notificacion {
    private String usuarioId;
    private String notificacion;

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

    @Override
    public String toString() {
        return notificacion;
    }
}
