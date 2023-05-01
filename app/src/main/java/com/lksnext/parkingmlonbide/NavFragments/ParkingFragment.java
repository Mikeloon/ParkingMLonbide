package com.lksnext.parkingmlonbide.NavFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.lksnext.parkingmlonbide.DataClasses.Parking;
import com.lksnext.parkingmlonbide.R;


public class ParkingFragment extends Fragment {
    private ImageView carImg;
    private ImageView ElectImg;
    private ImageView disImg;
    private ImageView MotoImg;


    public ParkingFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_parking, container, false);

        carImg = v.findViewById(R.id.CarImg);
        ElectImg = v.findViewById(R.id.iElectImg);
        disImg = v.findViewById(R.id.DisImg);
        MotoImg = v.findViewById(R.id.MotoImg);

        carImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getContext(), "Plazas de coche disponibles: "+ Parking.PlazaCoches, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        ElectImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getContext(), "Plazas de coches electricos disponibles: "+ Parking.PlazasElectricos, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        disImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getContext(), "Plazas para minusvalidos disponibles: "+ Parking.PlazasMinusvalidos, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        MotoImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(getContext(), "Plazas de moto disponibles: "+ Parking.Motos, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        return v;
    }
}