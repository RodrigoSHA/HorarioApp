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

import com.example.horarioapp.Clases.Clase;
import com.example.horarioapp.Clases.Tarea;
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

public class PrincipalFragment extends Fragment {

    private RecyclerView recyclerClases, recyclerTareas;
    private ItemGenericoAdapter adapterClases, adapterTareas;
    private List<Map<String, String>> listaClases, listaTareas;

    private DatabaseReference clasesRef, tareasRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal, container, false);

        recyclerClases = view.findViewById(R.id.recycler_clases);
        recyclerTareas = view.findViewById(R.id.recycler_tareas);

        recyclerClases.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerTareas.setLayoutManager(new LinearLayoutManager(getContext()));

        listaClases = new ArrayList<>();
        listaTareas = new ArrayList<>();

        adapterClases = new ItemGenericoAdapter(getContext(), listaClases, "Clase");
        adapterTareas = new ItemGenericoAdapter(getContext(), listaTareas, "Tarea");

        recyclerClases.setAdapter(adapterClases);
        recyclerTareas.setAdapter(adapterTareas);

        // Referencias a Firebase
        clasesRef = FirebaseDatabase.getInstance().getReference("clases");
        tareasRef = FirebaseDatabase.getInstance().getReference("tareas");

        cargarClases();
        cargarTareas();

        return view;
    }

    private void cargarClases() {
        clasesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaClases.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Clase c = ds.getValue(Clase.class);
                    if (c != null) {
                        Map<String, String> map = new HashMap<>();
                        map.put("principal", c.nombre);
                        map.put("secundario", "Hora: " + c.horaInicio + ":" + c.minutoInicio + " - " +
                                c.horaFin + ":" + c.minutoFin);
                        map.put("terciario", "Aula: " + c.aula + " | Prof: " + c.profesor);
                        listaClases.add(map);
                    }
                }
                adapterClases.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error
            }
        });
    }

    private void cargarTareas() {
        tareasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaTareas.clear();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                try {
                    // Fecha de hoy a medianoche
                    String hoyStr = sdf.format(System.currentTimeMillis());
                    long hoyMillis = sdf.parse(hoyStr).getTime();

                    List<Map<String, String>> tempList = new ArrayList<>();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Tarea t = ds.getValue(Tarea.class);
                        if (t != null) {
                            long tareaMillis = sdf.parse(t.getFecha()).getTime();

                            // Solo agregamos tareas de hoy en adelante
                            if (tareaMillis >= hoyMillis) {
                                Map<String, String> map = new HashMap<>();
                                map.put("principal", t.getTitulo());
                                map.put("secundario", t.getDescripcion());
                                map.put("terciario", t.getFecha());
                                map.put("fechaMillis", String.valueOf(tareaMillis)); // Para ordenar fácilmente
                                tempList.add(map);
                            }
                        }
                    }

                    // Ordenamos por fecha ascendente
                    tempList.sort((m1, m2) -> {
                        long f1 = Long.parseLong(m1.get("fechaMillis"));
                        long f2 = Long.parseLong(m2.get("fechaMillis"));
                        return Long.compare(f1, f2);
                    });

                    // Quitamos la clave auxiliar antes de pasar al adapter
                    for (Map<String, String> map : tempList) {
                        map.remove("fechaMillis");
                        listaTareas.add(map);
                    }

                    adapterTareas.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar error
            }
        });
    }



}
