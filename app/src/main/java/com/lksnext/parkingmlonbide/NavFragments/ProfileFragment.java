package com.lksnext.parkingmlonbide.NavFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lksnext.parkingmlonbide.DataClasses.Reserva;
import com.lksnext.parkingmlonbide.DataClasses.User;
import com.lksnext.parkingmlonbide.R;


public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView userTxt = (TextView) v.findViewById(R.id.txtWelcome);
        TextView mailTxt = (TextView) v.findViewById(R.id.Correotxt);
        TextView dateTxt = (TextView) v.findViewById(R.id.fechaTxt);
        TextView reservastxt = (TextView) v.findViewById(R.id.reservastxt);

        userTxt.setText(User.username);
        mailTxt.setText(User.email);
        dateTxt.setText(User.birthdate);
        String reservas = "";
        for (Reserva r : User.misReservas){
            reservas = reservas;
        }
        reservastxt.setText(reservas);
        return v;
    }
}