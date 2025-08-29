package com.example.horarioapp.Clases;

public class Curso {
    public String nombre;
    public String aula;
    public String profesor;
    public String color; // Almacena el color en hexadecimal

    public Curso() { } // Constructor vacío requerido por Firebase

    public Curso(String nombre, String aula, String profesor, String color) {
        this.nombre = nombre;
        this.aula = aula;
        this.profesor = profesor;
        this.color = color;
    }
}
