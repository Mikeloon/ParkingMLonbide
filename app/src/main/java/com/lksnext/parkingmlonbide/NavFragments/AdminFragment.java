package com.lksnext.parkingmlonbide.NavFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.DataClasses.Parking;
import com.lksnext.parkingmlonbide.R;

public class AdminFragment extends Fragment {

    private TextView numCocheNormalTextView, numCocheElecTextView, numMinusvTextView, numMotoTextView;
    private ImageView restaNormalImageView, sumaNormalImageView, restaCocheElecImageView, sumaCocheElecImageView,
            restaMinusvImageView, sumaMinusvImageView, restaMotoImageView, sumaMotoImageView;
    private Button guardarButton, restablecerButton;

    private int numCocheNormal, numCocheElec, numMinusv, numMoto;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        numCocheNormalTextView = view.findViewById(R.id.NumCocheNormal);
        numCocheElecTextView = view.findViewById(R.id.NumCocheElec);
        numMinusvTextView = view.findViewById(R.id.NumMinusv);
        numMotoTextView = view.findViewById(R.id.NumMoto);

        restaNormalImageView = view.findViewById(R.id.btnRestaNormal);
        sumaNormalImageView = view.findViewById(R.id.btonSumaNormal);
        restaCocheElecImageView = view.findViewById(R.id.btnRestaCocheElec);
        sumaCocheElecImageView = view.findViewById(R.id.btonSumaElec);
        restaMinusvImageView = view.findViewById(R.id.btnRestaMinusv);
        sumaMinusvImageView = view.findViewById(R.id.btonSumaMinusv);
        restaMotoImageView = view.findViewById(R.id.btnRestaMoto);
        sumaMotoImageView = view.findViewById(R.id.btonSumaMoto);

        guardarButton = view.findViewById(R.id.guardarButton);
        restablecerButton = view.findViewById(R.id.restablecerButton);

        db = FirebaseFirestore.getInstance();

        obtenerNumeroPlazas();

        configurarBotonesRestaSuma();

        guardarButton.setOnClickListener(v -> guardarCambios());

        restablecerButton.setOnClickListener(v -> restablecerValores());

        return view;
    }

    private void obtenerNumeroPlazas() {
        numCocheNormal = Parking.PlazaCoches;
        numCocheElec = Parking.PlazasElectricos;
        numMinusv = Parking.PlazasMinusvalidos;
        numMoto = Parking.Motos;
        numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
        numCocheElecTextView.setText(String.valueOf(numCocheElec));
        numMinusvTextView.setText(String.valueOf(numMinusv));
        numMotoTextView.setText(String.valueOf(numMoto));
    }

    private void configurarBotonesRestaSuma() {
        restaNormalImageView.setOnClickListener(v -> {
            if (numCocheNormal > 0) {
                numCocheNormal--;
                numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
            }
        });

        sumaNormalImageView.setOnClickListener(v -> {
            numCocheNormal++;
            numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
        });

        restaCocheElecImageView.setOnClickListener(v -> {
            if (numCocheElec > 0) {
                numCocheElec--;
                numCocheElecTextView.setText(String.valueOf(numCocheElec));
            }
        });

        sumaCocheElecImageView.setOnClickListener(v -> {
            numCocheElec++;
            numCocheElecTextView.setText(String.valueOf(numCocheElec));
        });

        restaMinusvImageView.setOnClickListener(v -> {
            if (numMinusv > 0) {
                numMinusv--;
                numMinusvTextView.setText(String.valueOf(numMinusv));
            }
        });

        sumaMinusvImageView.setOnClickListener(v -> {
            numMinusv++;
            numMinusvTextView.setText(String.valueOf(numMinusv));
        });

        restaMotoImageView.setOnClickListener(v -> {
            if (numMoto > 0) {
                numMoto--;
                numMotoTextView.setText(String.valueOf(numMoto));
            }
        });

        sumaMotoImageView.setOnClickListener(v -> {
            numMoto++;
            numMotoTextView.setText(String.valueOf(numMoto));
        });
    }

    private void guardarCambios() {
        Parking.actualizarPlazas(numCocheNormal, numCocheElec, numMinusv, numMoto);
        Toast.makeText(getActivity(), "Cambios guardados", Toast.LENGTH_SHORT).show();
    }

    private void restablecerValores() {
        numCocheNormal = Parking.PlazaCoches;
        numCocheElec = Parking.PlazasElectricos;
        numMinusv = Parking.PlazasMinusvalidos;
        numMoto = Parking.Motos;

        numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
        numCocheElecTextView.setText(String.valueOf(numCocheElec));
        numMinusvTextView.setText(String.valueOf(numMinusv));
        numMotoTextView.setText(String.valueOf(numMoto));

        guardarCambios();
    }
}
