package com.example.horarioapp.Clases;

public class Curso {
    public String nombre;
    public String aula;
    public String profesor;


    public Curso() { } // Constructor vacío requerido por Firebase

    public Curso(String nombre, String aula, String profesor) {
        this.nombre = nombre;
        this.aula = aula;
        this.profesor = profesor;
    }
}
