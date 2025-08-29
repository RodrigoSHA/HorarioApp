package com.example.horarioapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.horarioapp.Clases.Profesor;
import com.example.horarioapp.FirebaseHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
        fabAdd.setOnClickListener(v -> mostrarBottomSheetAgregarProfesor());


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

    private void mostrarBottomSheetAgregarProfesor() {
        View sheetView = getLayoutInflater().inflate(R.layout.bottomsheet_add_profesor, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(sheetView);

        EditText etNombre = sheetView.findViewById(R.id.et_nombre);
        EditText etCorreo = sheetView.findViewById(R.id.et_correo);
        EditText etTelefono = sheetView.findViewById(R.id.et_telefono);
        Button btnGuardar = sheetView.findViewById(R.id.btn_guardar);

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();

            if(nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar en Firebase usando tu helper
            firebaseHelper.guardarProfesor(nombre, correo, telefono);

            dialog.dismiss();
            Toast.makeText(getContext(), "Profesor agregado", Toast.LENGTH_SHORT).show();

            // Recargar la lista
            cargarProfesores();
        });

        dialog.show();
    }

}
