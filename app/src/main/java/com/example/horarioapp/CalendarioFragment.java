package com.example.horarioapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.horarioapp.Clases.Tarea;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarioFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerTareas;
    private ItemGenericoAdapter adapter;
    private List<Map<String, String>> listaMaps;
    private DatabaseReference tareasRef;

    // Guardar la fecha seleccionada
    private String fechaSeleccionada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerTareas = view.findViewById(R.id.recycler_tareas);
        recyclerTareas.setLayoutManager(new LinearLayoutManager(getContext()));

        listaMaps = new ArrayList<>();
        adapter = new ItemGenericoAdapter(getContext(), listaMaps, "Tarea");
        recyclerTareas.setAdapter(adapter);

        tareasRef = FirebaseDatabase.getInstance().getReference("tareas");

        // Fecha inicial -> hoy
        fechaSeleccionada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(System.currentTimeMillis());

        // Cargar las tareas de hoy al iniciar
        cargarTareas();

        // Cuando el usuario cambia la fecha en el calendario
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            fechaSeleccionada = String.format(Locale.getDefault(),
                    "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            cargarTareas();
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add);
        fab.setOnClickListener(v -> agregarTareaEjemplo());

        return view;
    }

    private void cargarTareas() {
        tareasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaMaps.clear();
                for (DataSnapshot tareaSnap : snapshot.getChildren()) {
                    Tarea tarea = tareaSnap.getValue(Tarea.class);
                    if (tarea != null && tarea.getFecha().equals(fechaSeleccionada)) {
                        Map<String, String> map = new HashMap<>();
                        map.put("principal", tarea.getTitulo());
                        map.put("secundario", tarea.getDescripcion());
                        map.put("terciario", tarea.getFecha());
                        listaMaps.add(map);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void agregarTareaEjemplo() {
        Tarea tarea = new Tarea("Tarea de Prueba", "Descripción de prueba", fechaSeleccionada);

        String id = tareasRef.push().getKey();
        if (id != null) {
            tareasRef.child(id).setValue(tarea);
            cargarTareas(); // recargar lista después de agregar
        }
    }
}
