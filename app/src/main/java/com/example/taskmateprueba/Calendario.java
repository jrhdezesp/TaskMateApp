package com.example.taskmateprueba;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.Calendar;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Calendario extends AppCompatActivity {

    private Calendar calendarService;
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private CalendarView calendarView;
    private List<Event> todosLosEventos = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();
    private ActivityResultLauncher<Intent> authLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        Button btnCreateEvento = findViewById(R.id.btncrearevento); // Coincide con el ID del XML
        btnCreateEvento.setOnClickListener(v -> mostrarDialogoCrearEvento());



        // Inicializar vistas
        calendarView = findViewById(R.id.calendarView);
        eventsRecyclerView = findViewById(R.id.events_recycler_view);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList);
        eventsRecyclerView.setAdapter(eventAdapter);




        // Registrar launcher moderno para manejar permisos
        authLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Log.d("Calendario", "Permiso concedido");
                        fetchEvents();
                    } else {
                        Log.d("Calendario", "Permiso DENEGADO");
                        Toast.makeText(this, "Permisos de Google Calendar no autorizados", Toast.LENGTH_LONG).show();
                    }
                }
        );


        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            java.util.Calendar selectedDate = java.util.Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            actualizarEventosDelDia(selectedDate.getTime());
        });

        // Autenticación de cuenta
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            Toast.makeText(this, "Debes iniciar sesión con Google", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar servicio de Google Calendar y obtener eventos
        initializeGoogleCalendar(account);
        fetchEvents();

    }

    private void initializeGoogleCalendar(GoogleSignInAccount account) {
        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                this, Collections.singleton(CalendarScopes.CALENDAR));
        credential.setSelectedAccount(account.getAccount());

        calendarService = new Calendar.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("TaskMateApp")
                .build();
    }

    private void fetchEvents() {
        new Thread(() -> {
            try {
                Log.d("Calendario", "Iniciando fetchEvents");

                if (calendarService == null) {
                    Log.e("Calendario", "calendarService es null");
                    runOnUiThread(() ->
                            Toast.makeText(Calendario.this, "calendarService no está inicializado", Toast.LENGTH_LONG).show());
                    return;
                }

                Events events = calendarService.events()
                        .list("primary")
                        .setMaxResults(100)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();

                List<Event> items = events.getItems();

                runOnUiThread(() -> {
                    todosLosEventos.clear();
                    if (items != null) {
                        todosLosEventos.addAll(items);
                    }
                    actualizarEventosDelDia(new Date(calendarView.getDate()));
                });

            } catch (UserRecoverableAuthIOException e) {
                Log.e("Calendario", "Se requiere autorización del usuario", e);
                runOnUiThread(() -> authLauncher.launch(e.getIntent()));

            } catch (IOException e) {
                Log.e("Calendario", "IOException al obtener eventos", e);
                runOnUiThread(() ->
                        Toast.makeText(Calendario.this, "Error: " + (e.getMessage() != null ? e.getMessage() : "Error desconocido"), Toast.LENGTH_LONG).show()
                );

            } catch (Exception e) {
                Log.e("Calendario", "Error inesperado", e);
                runOnUiThread(() ->
                        Toast.makeText(Calendario.this, "Error inesperado: " + e.getClass().getSimpleName(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void actualizarEventosDelDia(Date fechaSeleccionada) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String fechaStr = sdf.format(fechaSeleccionada);

        List<Event> eventosFiltrados = new ArrayList<>();

        for (Event event : todosLosEventos) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) start = event.getStart().getDate();
            String eventDateStr = sdf.format(new Date(start.getValue()));
            if (fechaStr.equals(eventDateStr)) {
                eventosFiltrados.add(event);
            }
        }

        eventList.clear();
        eventList.addAll(eventosFiltrados);
        eventAdapter.notifyDataSetChanged();
    }

    private void mostrarDialogoCrearEvento() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_crear_evento, null);

        EditText tituloEditText = dialogView.findViewById(R.id.txtevtitulo);
        EditText descripcionEditText = dialogView.findViewById(R.id.txtevdesc);
        Button btnHoraInicio = dialogView.findViewById(R.id.btnhora);
        Button btnHoraFin = dialogView.findViewById(R.id.btnhorafin);

        final java.util.Calendar horaInicio = java.util.Calendar.getInstance();
        final java.util.Calendar horaFin = java.util.Calendar.getInstance();
        horaFin.add(java.util.Calendar.HOUR_OF_DAY, 1); // por defecto 1 hora después

        btnHoraInicio.setOnClickListener(v -> {
            TimePickerDialog timePicker = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        horaInicio.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
                        horaInicio.set(java.util.Calendar.MINUTE, minute);
                        btnHoraInicio.setText(String.format(Locale.getDefault(), "Inicio: %02d:%02d", hourOfDay, minute));
                    },
                    horaInicio.get(java.util.Calendar.HOUR_OF_DAY),
                    horaInicio.get(java.util.Calendar.MINUTE),
                    true
            );
            timePicker.show();
        });

        btnHoraFin.setOnClickListener(v -> {
            TimePickerDialog timePicker = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
                        horaFin.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
                        horaFin.set(java.util.Calendar.MINUTE, minute);
                        btnHoraFin.setText(String.format(Locale.getDefault(), "Fin: %02d:%02d", hourOfDay, minute));
                    },
                    horaFin.get(java.util.Calendar.HOUR_OF_DAY),
                    horaFin.get(java.util.Calendar.MINUTE),
                    true
            );
            timePicker.show();
        });

        new AlertDialog.Builder(this)
                .setTitle("Crear Evento")
                .setView(dialogView)
                .setPositiveButton("Crear", (dialog, which) -> {
                    String titulo = tituloEditText.getText().toString().trim();
                    String descripcion = descripcionEditText.getText().toString().trim();

                    if (titulo.isEmpty()) {
                        Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    crearEventoEnCalendario(titulo, descripcion, horaInicio, horaFin);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void crearEventoEnCalendario(String titulo, String descripcion,
                                         java.util.Calendar horaInicio, java.util.Calendar horaFin) {
        long fechaMillis = calendarView.getDate(); // fecha seleccionada
        java.util.Calendar fechaBase = java.util.Calendar.getInstance();
        fechaBase.setTimeInMillis(fechaMillis);

        // Establecer fecha seleccionada con hora de inicio y fin
        horaInicio.set(java.util.Calendar.YEAR, fechaBase.get(java.util.Calendar.YEAR));
        horaInicio.set(java.util.Calendar.MONTH, fechaBase.get(java.util.Calendar.MONTH));
        horaInicio.set(java.util.Calendar.DAY_OF_MONTH, fechaBase.get(java.util.Calendar.DAY_OF_MONTH));

        horaFin.set(java.util.Calendar.YEAR, fechaBase.get(java.util.Calendar.YEAR));
        horaFin.set(java.util.Calendar.MONTH, fechaBase.get(java.util.Calendar.MONTH));
        horaFin.set(java.util.Calendar.DAY_OF_MONTH, fechaBase.get(java.util.Calendar.DAY_OF_MONTH));

        DateTime startDateTime = new DateTime(horaInicio.getTime());
        DateTime endDateTime = new DateTime(horaFin.getTime());

        Event event = new Event()
                .setSummary(titulo)
                .setDescription(descripcion);

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Tegucigalpa");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Tegucigalpa");

        event.setStart(start);
        event.setEnd(end);

        new Thread(() -> {
            try {
                calendarService.events().insert("primary", event).execute();
                runOnUiThread(() -> {
                    Toast.makeText(this, "Evento creado correctamente", Toast.LENGTH_SHORT).show();
                    fetchEvents();
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Error al crear evento", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }




    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
        private final List<Event> events;

        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.event_item, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event event = events.get(position);
            holder.title.setText(event.getSummary());

            String time = "Hora: " + formatDateTime(event.getStart()) +
                    " - " + formatDateTime(event.getEnd());
            holder.time.setText(time);

            holder.description.setText(event.getDescription() != null ?
                    event.getDescription() : "Sin descripción");
        }

        @Override
        public int getItemCount() {
            return events.size();
        }

        class EventViewHolder extends RecyclerView.ViewHolder {
            TextView title, time, description;

            EventViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.event_title);
                time = itemView.findViewById(R.id.event_time);
                description = itemView.findViewById(R.id.event_description);
            }
        }
    }

    private String formatDateTime(EventDateTime eventDateTime) {
        if (eventDateTime.getDateTime() != null) {
            return eventDateTime.getDateTime().toStringRfc3339().substring(11, 16); // Solo hora:minuto
        } else if (eventDateTime.getDate() != null) {
            return eventDateTime.getDate().toStringRfc3339().substring(0, 10); // Solo fecha
        }
        return "";
    }
}
