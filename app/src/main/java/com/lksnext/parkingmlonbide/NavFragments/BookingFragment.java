package com.lksnext.parkingmlonbide.NavFragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.DataClasses.Parking;
import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.DataClasses.TipoEstacionamiento;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.R;
import com.lksnext.parkingmlonbide.RegisterLogin.MainActivity;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BookingFragment extends Fragment {
    private DatePicker datePicker;
    private TimePicker startTime;
    private TimePicker endTime;
    private Spinner spinner;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private final TipoEstacionamiento[] tipoescogido = {null};
    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_booking, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Tratamiento de la fecha escogida
        datePicker = v.findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,7);
        long maxDate = calendar.getTimeInMillis();
        datePicker.setMinDate(System.currentTimeMillis());
        datePicker.setMaxDate(maxDate);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = calendar.getTime();
                    if (tieneReserva(date) == true){
                        Toast.makeText(getActivity(), "No puede realizar mas reservas para este día", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        //Tratamiento de la hora escogida
        startTime = v.findViewById(R.id.startTimePicker);
        endTime = v.findViewById(R.id.endTimePicker);
        startTime.setIs24HourView(true);
        endTime.setIs24HourView(true);


        //Añadir tipos de plaza
        spinner = v.findViewById(R.id.spinnerTipo);
        TipoEstacionamiento[] tipo = TipoEstacionamiento.values();
        ArrayAdapter<TipoEstacionamiento> adapter = new ArrayAdapter<TipoEstacionamiento>(getContext(), android.R.layout.simple_spinner_item,tipo);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoescogido[0] = (TipoEstacionamiento) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "Seleccione el tipo de plaza por favor", Toast.LENGTH_SHORT).show();
            }
        });

        Button button = v.findViewById(R.id.confirmbtton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarPeriodoMax(startTime,endTime) == true){
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int dayOfMonth = datePicker.getDayOfMonth();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth);
                    Date date = calendar.getTime();

                    int startHour = startTime.getHour();
                    int startMinute = startTime.getMinute();
                    int endHour = endTime.getHour();
                    int endMinute = endTime.getMinute();
                    long diffMillis = TimeUnit.HOURS.toMillis(endHour - startHour) + TimeUnit.MINUTES.toMillis(endMinute - startMinute);
                    int diffHours = (int) TimeUnit.MILLISECONDS.toHours(diffMillis);

                    Reserva r = new Reserva();
                    r.setFechaReserva(date);
                    r.setHorasReserva(diffHours);
                    r.setTipoPlaza(tipoescogido[0]);

                    String uid = mAuth.getCurrentUser().getUid();
                    switch (tipoescogido[0]){
                        case Normal:
                            Parking.PlazaCoches = Parking.PlazaCoches - 1;
                            break;
                        case Electrico:
                            Parking.PlazasElectricos = Parking.PlazasElectricos - 1;
                            break;
                        case Minusvalido:
                            Parking.PlazasMinusvalidos = Parking.PlazasMinusvalidos -1;
                            break;
                        case Moto:
                            Parking.Motos = Parking.Motos - 1;
                            break;
                        default:
                            break;
                    }

                    Map<String, Object> reservaData = new HashMap<>();
                    reservaData.put("id", r.getId());
                    reservaData.put("fechaReserva", r.getFechaReserva());
                    reservaData.put("horasReserva", r.getHorasReserva());
                    reservaData.put("tipoPlaza", r.getTipoPlaza().toString());
                    CollectionReference usersRef = db.collection("users");
                    DocumentReference userDocRef = usersRef.document(uid);

                    userDocRef.update("reservas", FieldValue.arrayUnion(reservaData))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Actualización exitosa
                                    Toast.makeText(getActivity(), "Reserva en el dia:" + date.toString() +"confirmada", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Ocurrió un error al guardar la reserva
                                    Toast.makeText(getActivity(), "Error al guardar la reserva", Toast.LENGTH_SHORT).show();
                                }
                            });

                }else {
                    Toast.makeText(getActivity(), "El periodo de tiempo máximo para la reserva es de 8 horas", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return v;
    }
    private boolean validarPeriodoMax(TimePicker tpInicio, TimePicker tpFin){
        // Obtener los valores de las horas y minutos seleccionados en el primer TimePicker
        int inicioHoras = tpInicio.getHour();
        int inicioMinutos = tpInicio.getMinute();

        // Obtener los valores de las horas y minutos seleccionados en el segundo TimePicker
        int finHoras = tpFin.getHour();
        int finMinutos = tpFin.getMinute();

        // Calcular la diferencia de horas y minutos entre los dos TimePicker y convertir a minutos totales
        int diferenciaMinutos = (finHoras * 60 + finMinutos) - (inicioHoras * 60 + inicioMinutos);

        // Verificar si la diferencia de minutos es mayor a 480 (8 horas)
        if (diferenciaMinutos > 480) {
            return false;
        } else return true;
    }

    private boolean tieneReserva(Date d){
        boolean tiene = false;
        for (Reserva r : User.misReservas){
            if (r.getFechaReserva() == d){
                tiene = true;
            }
        }
        return tiene;
    }
}
