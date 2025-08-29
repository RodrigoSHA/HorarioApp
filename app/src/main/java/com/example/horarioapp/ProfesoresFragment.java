package com.example.horarioapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.horarioapp.Clases.Profesor;
import com.example.horarioapp.FirebaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfesoresFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemGenericoAdapter adapter;
    private List<Map<String, String>> listaProfesores = new ArrayList<>();
    private FirebaseHelper firebaseHelper;
    private FloatingActionButton fabAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profesores, container, false);

        recyclerView = view.findViewById(R.id.recycler_profesores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ItemGenericoAdapter(getContext(), listaProfesores, "Profesor");
        recyclerView.setAdapter(adapter);

        fabAdd = view.findViewById(R.id.fab_add);

        firebaseHelper = new FirebaseHelper();

        // Cargar profesores desde Firebase
        cargarProfesores();

        // Acción del botón para agregar profesores de ejemplo
        fabAdd.setOnClickListener(v -> {
            // Crear dos profesores de ejemplo
            firebaseHelper.guardarProfesor("Juan Pérez", "juan.perez@email.com", "555-1234");
            firebaseHelper.guardarProfesor("María López", "maria.lopez@email.com", "555-5678");

            // Recargar lista después de agregar
            cargarProfesores();
        });

        return view;
    }

    private void cargarProfesores() {
        firebaseHelper.obtenerProfesores(dataSnapshot -> {
            listaProfesores.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Profesor p = snapshot.getValue(Profesor.class);
                Map<String, String> map = new HashMap<>();
                map.put("principal", p.nombre);
                map.put("secundario", p.correo);
                map.put("terciario", p.telefono);
                listaProfesores.add(map);
            }
            adapter.notifyDataSetChanged();
        });
    }
}
