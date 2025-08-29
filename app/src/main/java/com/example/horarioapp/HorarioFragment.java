package com.example.horarioapp;

import android.graphics.Color;
import android.graphics.Typeface;
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

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.horarioapp.Clases.Clase;

public class HorarioFragment extends Fragment {

    private WeekView weekView;
    private RecyclerView recyclerClases;
    private FloatingActionButton fabAdd;

    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horario, container, false);

        weekView = view.findViewById(R.id.weekView);
        recyclerClases = view.findViewById(R.id.recycler_clases);
        fabAdd = view.findViewById(R.id.fab_add);

        // Referencia a Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference("clases");

        setupWeekView();
        setupRecyclerView();
        setupFab(); // Por ahora solo para probar toast

        loadClasesFromFirebase();

        return view;
    }

    private void setupWeekView() {
        weekView.setShowNowLine(true);
        weekView.setNowLineColor(Color.RED);
        weekView.setHourHeight(150);
        weekView.setMinHour(6);
        weekView.setMaxHour(22);
        weekView.setTypeface(Typeface.SANS_SERIF);

        weekView.setEventTextSize(16);
        weekView.setAllDayEventTextSize(14);
        weekView.setDefaultEventColor(Color.parseColor("#2196F3"));
        weekView.setDefaultEventTextColor(Color.WHITE);
        weekView.setEventCornerRadius(12);

        weekView.setShowHourSeparators(true);
        weekView.setHourSeparatorColor(Color.LTGRAY);
        weekView.setHourSeparatorStrokeWidth(2);

        weekView.setShowDaySeparators(true);
        weekView.setDaySeparatorColor(Color.GRAY);
        weekView.setDaySeparatorStrokeWidth(2);

        weekView.setAdapter(new WeekView.SimpleAdapter<Clase>() {
            @Override
            public WeekViewEntity onCreateEntity(Clase item) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();

                start.set(Calendar.HOUR_OF_DAY, item.horaInicio);
                start.set(Calendar.MINUTE, item.minutoInicio);

                end.set(Calendar.HOUR_OF_DAY, item.horaFin);
                end.set(Calendar.MINUTE, item.minutoFin);

                return new WeekViewEntity.Event.Builder<>(item)
                        .setId(item.nombre.hashCode()) // id único simple
                        .setTitle(item.nombre + " - " + item.aula)
                        .setStartTime(start)
                        .setEndTime(end)
                        .build();
            }
        });
    }

    private void setupRecyclerView() {
        recyclerClases.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setupFab() {
        fabAdd.setOnClickListener(v -> {
            // Crear dos clases de ejemplo
            Clase clase1 = new Clase(
                    "Matemáticas",
                    "Lunes",
                    8, 0,   // hora inicio
                    10, 0,  // hora fin
                    List.of("Lunes","Miércoles","Viernes"),
                    "Aula 101",
                    "Profesor A"
            );

            Clase clase2 = new Clase(
                    "Física",
                    "Martes",
                    9, 0,
                    11, 0,
                    List.of("Martes","Jueves"),
                    "Aula 202",
                    "Profesor B"
            );

            // Guardar en Firebase con push() para generar un id único
            dbRef.push().setValue(clase1);
            dbRef.push().setValue(clase2);

            Toast.makeText(getContext(), "Se agregaron dos clases de ejemplo", Toast.LENGTH_SHORT).show();
        });
    }


    private void loadClasesFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Clase> clases = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Clase clase = ds.getValue(Clase.class);
                    if (clase != null) clases.add(clase);
                }

                // Refrescar WeekView
                ((WeekView.SimpleAdapter) weekView.getAdapter()).submitList(clases);
                weekView.scrollToDateTime(Calendar.getInstance());

                // Refrescar RecyclerView
                List<Map<String, String>> listaClases = new ArrayList<>();
                for (Clase c : clases) {
                    Map<String, String> item = new HashMap<>();
                    item.put("principal", c.nombre);
                    item.put("secundario", "Hora: " + c.horaInicio + ":" + c.minutoInicio + " - " +
                            c.horaFin + ":" + c.minutoFin);
                    item.put("terciario", "Aula: " + c.aula + " | Prof: " + c.profesor);
                    listaClases.add(item);
                }
                recyclerClases.setAdapter(new ItemGenericoAdapter(getContext(), listaClases, "Clase"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al leer Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
