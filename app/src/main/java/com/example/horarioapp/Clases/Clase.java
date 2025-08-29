package com.example.horarioapp.Clases;

import java.util.List;

public class Clase {
    public String id;          // Id generado por Firebase
    public String nombre;      // Nombre de la asignatura
    public String dia;         // Día de la clase (ej. "Lunes")
    public int horaInicio;     // Hora de inicio (24h)
    public int minutoInicio;   // Minuto de inicio
    public int horaFin;        // Hora de fin
    public int minutoFin;      // Minuto de fin
    public List<String> repeticion; // Ej: ["Lunes","Martes"]
    public String aula;        // Aula o lugar
    public String profesor;    // Nombre del profesor

    // Constructor vacío requerido por Firebase
    public Clase() {}

    // Constructor completo para pruebas
    public Clase(String nombre, String dia, int horaInicio, int minutoInicio,
                 int horaFin, int minutoFin, List<String> repeticion,
                 String aula, String profesor) {
        this.nombre = nombre;
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.minutoInicio = minutoInicio;
        this.horaFin = horaFin;
        this.minutoFin = minutoFin;
        this.repeticion = repeticion;
        this.aula = aula;
        this.profesor = profesor;
    }
}
