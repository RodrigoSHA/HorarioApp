package com.example.horarioapp.Clases;

public class Profesor {
    public String nombre;
    public String correo;
    public String telefono;

    public Profesor() { } // Constructor vacío requerido por Firebase

    public Profesor(String nombre, String correo, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }
}
