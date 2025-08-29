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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarioFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerTareas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerTareas = view.findViewById(R.id.recycler_tareas);

        // Configuramos RecyclerView
        recyclerTareas.setLayoutManager(new LinearLayoutManager(getContext()));

        // Lista de tareas de ejemplo
        List<Map<String, String>> tareas = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, String> tarea = new HashMap<>();
            tarea.put("principal", "Tarea " + i);
            tarea.put("secundario", "Descripción " + i);
            tarea.put("terciario", "Hora: " + (8 + i) + ":00");
            tareas.add(tarea);
        }

        // Conectamos adapter
        ItemGenericoAdapter adapter = new ItemGenericoAdapter(getContext(), tareas, "Tarea");
        recyclerTareas.setAdapter(adapter);

        // Listener de selección de fecha (por ahora no filtra tareas)
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Aquí luego filtraremos tareas según fecha
        });

        return view;
    }
}
