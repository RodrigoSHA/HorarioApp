package com.example.horarioapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CursosFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemGenericoAdapter adapter;
    private List<Map<String, String>> listaCursos = new ArrayList<>();
    private FirebaseHelper firebaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cursos, container, false);

        recyclerView = view.findViewById(R.id.recycler_cursos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ItemGenericoAdapter(getContext(), listaCursos, "Curso");
        recyclerView.setAdapter(adapter);

        firebaseHelper = new FirebaseHelper();

        // Cargar cursos desde Firebase
        cargarCursosFirebase();

        // Botón agregar curso de ejemplo
        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> agregarCursoEjemplo());

        return view;
    }

    private void cargarCursosFirebase() {
        firebaseHelper.obtenerCursos(new FirebaseHelper.FirebaseCallback() {
            @Override
            public void onCallback(DataSnapshot snapshot) {
                listaCursos.clear();
                for (DataSnapshot s : snapshot.getChildren()) {
                    Map<String, String> cursoMap = new HashMap<>();
                    cursoMap.put("principal", s.child("nombre").getValue(String.class));
                    cursoMap.put("secundario", s.child("aula").getValue(String.class));
                    cursoMap.put("terciario", s.child("colorHex").getValue(String.class));
                    listaCursos.add(cursoMap);
                }
                adapter.notifyDataSetChanged();
            }

            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al cargar cursos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarCursoEjemplo() {
        String nombreCurso = "Curso Ejemplo";
        String aula = "Aula 101";
        String profesorId = "Profesor Ejemplo";
        String colorHex = "#FF5722"; // Naranja de ejemplo

        firebaseHelper.guardarCurso(nombreCurso, aula, profesorId, colorHex);
        Toast.makeText(getContext(), "Curso de ejemplo agregado", Toast.LENGTH_SHORT).show();

        // Recargar cursos
        cargarCursosFirebase();
    }
}
