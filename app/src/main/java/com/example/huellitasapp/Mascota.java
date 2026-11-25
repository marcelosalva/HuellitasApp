package com.example.huellitasapp;

import com.google.firebase.firestore.Exclude;

public class Mascota {
    private String id;
    private String nombre;
    private String especie;
    private int edad;
    private boolean vacunado;
    private String estado;
    private String descripcion;

    // Firestore necesita el constructor vacío.
    public Mascota() { }

    public Mascota(String nombre, String especie, int edad, boolean vacunado,
                   String estado, String descripcion) {
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.vacunado = vacunado;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    // El ID es el nombre del documento, no un campo adicional.
    @Exclude
    public String getId() { return id; }
    @Exclude
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public boolean getVacunado() { return vacunado; }
    public void setVacunado(boolean vacunado) { this.vacunado = vacunado; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
