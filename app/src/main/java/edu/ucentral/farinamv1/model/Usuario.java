package edu.ucentral.farinamv1.model;


public class Usuario {
    private String usuarioId;
    private String nombre;
    private Long numero;
    private String email;
    private String password;
    private String role;
    private String image;
    private boolean isTienda;

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isTienda() {
        return isTienda;
    }

    public void setTienda(boolean tienda) {
        isTienda = tienda;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuarioId='" + usuarioId + '\'' +
                ", nombre='" + nombre + '\'' +
                ", numero=" + numero +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
