package com.example.horarioapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;

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

        // Botón para agregar curso
        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> mostrarBottomSheetAgregarCurso());

        return view;
    }

    private void cargarCursosFirebase() {
        firebaseHelper.obtenerCursos(snapshot -> {
            listaCursos.clear();
            for (DataSnapshot s : snapshot.getChildren()) {
                Map<String, String> cursoMap = new HashMap<>();
                cursoMap.put("principal", s.child("nombre").getValue(String.class));
                cursoMap.put("secundario", s.child("aula").getValue(String.class));
                cursoMap.put("terciario", s.child("profesor").getValue(String.class));
                listaCursos.add(cursoMap);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void mostrarBottomSheetAgregarCurso() {
        View sheetView = getLayoutInflater().inflate(R.layout.bottomsheet_add_curso, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(sheetView);

        EditText etNombre = sheetView.findViewById(R.id.et_nombre);
        EditText etAula = sheetView.findViewById(R.id.et_aula);
        Spinner spinnerProfesor = sheetView.findViewById(R.id.spinner_profesor);
        Button btnGuardar = sheetView.findViewById(R.id.btn_guardar);

        // 1️⃣ Cargar profesores en el spinner
        firebaseHelper.obtenerProfesores(snapshot -> {
            List<String> nombres = new ArrayList<>();
            for (DataSnapshot s : snapshot.getChildren()) {
                String nombre = s.child("nombre").getValue(String.class);
                if (nombre != null) nombres.add(nombre);
            }
            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, nombres);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProfesor.setAdapter(adapterSpinner);
        });

        // 2️⃣ Guardar curso
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String aula = etAula.getText().toString().trim();
            String profesor = spinnerProfesor.getSelectedItem() != null ?
                    spinnerProfesor.getSelectedItem().toString() : "";

            if (nombre.isEmpty() || aula.isEmpty() || profesor.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseHelper.guardarCurso(nombre, aula, profesor);
            Toast.makeText(getContext(), "Curso agregado", Toast.LENGTH_SHORT).show();
            cargarCursosFirebase();
            dialog.dismiss();
        });

        dialog.show();
    }
}
