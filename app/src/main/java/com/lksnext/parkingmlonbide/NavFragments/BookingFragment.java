package com.lksnext.parkingmlonbide.NavFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lksnext.parkingmlonbide.DataClasses.TipoEstacionamiento;
import com.lksnext.parkingmlonbide.R;
import com.lksnext.parkingmlonbide.RegisterLogin.MainActivity;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BookingFragment extends Fragment {

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_booking, container, false);

        //Tratamiento de la fecha escogida
        DatePicker datePicker = v.findViewById(R.id.datePicker);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,7);
        long maxDate = calendar.getTimeInMillis();
        datePicker.setMinDate(Calendar.DAY_OF_YEAR);
        datePicker.setMaxDate(maxDate);

        //Tratamiento de la hora escogida
        TimePicker startTime = v.findViewById(R.id.startTimePicker);
        TimePicker endTime = v.findViewById(R.id.endTimePicker);
        startTime.setIs24HourView(true);
        endTime.setIs24HourView(true);
        startTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                validarPeriodoMax(startTime,endTime);
            }
        });

        endTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                validarPeriodoMax(startTime,endTime);
            }
        });

        //Añadir tipos de plaza
        ScrollView scrollView = v.findViewById(R.id.scrollView2);
        LinearLayout linearLayout = v.findViewById(R.id.linearlayout);
        for (TipoEstacionamiento tipo: TipoEstacionamiento.values()){
            TextView textView = new TextView(getActivity());
            textView.setText(tipo.toString());
            linearLayout.addView(textView);
        }

        Button button = v.findViewById(R.id.confirmbtton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });
        return v;
    }
    private void validarPeriodoMax(TimePicker tpInicio, TimePicker tpFin){
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
            Toast.makeText(getActivity(), "El periodo de tiempo maximo es de 8 horas", Toast.LENGTH_SHORT).show();        }
    }
}
