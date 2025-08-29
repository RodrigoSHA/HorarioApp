package com.example.horarioapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.horarioapp.Clases.Profesor;
import com.example.horarioapp.Clases.Curso;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {

    private DatabaseReference mDatabase;

    public FirebaseHelper() {
        // Referencia raíz de Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /** -------------------- Profesores -------------------- **/

    // Guardar un profesor
    public void guardarProfesor(String nombre, String correo, String telefono) {
        Profesor profesor = new Profesor(nombre, correo, telefono);
        mDatabase.child("profesores").push().setValue(profesor)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseHelper", "Profesor guardado!"))
                .addOnFailureListener(e -> Log.e("FirebaseHelper", "Error: " + e.getMessage()));
    }

    // Obtener todos los profesores
    public void obtenerProfesores(final FirebaseCallback callback) {
        mDatabase.child("profesores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseHelper", "Error al leer profesores: " + error.getMessage());
            }
        });
    }

    /** -------------------- Cursos -------------------- **/

    // Guardar un curso
    public void guardarCurso(String nombre, String aula, String profesorId) {
        Curso curso = new Curso(nombre, aula, profesorId);
        mDatabase.child("cursos").push().setValue(curso)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseHelper", "Curso guardado!"))
                .addOnFailureListener(e -> Log.e("FirebaseHelper", "Error: " + e.getMessage()));
    }

    // Obtener todos los cursos
    public void obtenerCursos(final FirebaseCallback callback) {
        mDatabase.child("cursos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseHelper", "Error al leer cursos: " + error.getMessage());
            }
        });
    }

    /** -------------------- Getter base de datos -------------------- **/

    public DatabaseReference getDatabase() {
        return mDatabase;
    }

    /** -------------------- Interfaz callback -------------------- **/
    public interface FirebaseCallback {
        void onCallback(DataSnapshot dataSnapshot);
    }
}
