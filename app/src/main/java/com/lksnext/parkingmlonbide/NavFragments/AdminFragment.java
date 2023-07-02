package com.lksnext.parkingmlonbide.NavFragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lksnext.parkingmlonbide.DataClasses.Parking;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lksnext.parkingmlonbide.R;

public class AdminFragment extends Fragment {

    private static TextView numCocheNormalTextView;
    private static TextView numCocheElecTextView;
    private static TextView numMinusvTextView;
    private static TextView numMotoTextView;
    private static ImageView restaNormalImageView;
    private static ImageView sumaNormalImageView;
    private static ImageView restaCocheElecImageView;
    private static ImageView sumaCocheElecImageView;
    private static ImageView restaMinusvImageView;
    private static ImageView sumaMinusvImageView;
    private static ImageView restaMotoImageView;
    private static ImageView sumaMotoImageView;
    private Button guardarButton, restablecerButton;

    private static int numCocheNormal;
    private static int numCocheElec;
    private static int numMinusv;
    private static int numMoto;

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

        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios(getActivity());
            }
        });

        restablecerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restablecerValores(getActivity());
            }
        });

        return view;
    }

    private  static void obtenerNumeroPlazas() {
        numCocheNormal = Parking.PlazaCoches;
        numCocheElec = Parking.PlazasElectricos;
        numMinusv = Parking.PlazasMinusvalidos;
        numMoto = Parking.Motos;
        numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
        numCocheElecTextView.setText(String.valueOf(numCocheElec));
        numMinusvTextView.setText(String.valueOf(numMinusv));
        numMotoTextView.setText(String.valueOf(numMoto));

    }

    private static void configurarBotonesRestaSuma() {
        restaNormalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numCocheNormal > 0) {
                    numCocheNormal--;
                    numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
                }
            }
        });

        sumaNormalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numCocheNormal++;
                numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
            }
        });
        restaCocheElecImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numCocheElec > 0) {
                    numCocheElec--;
                    numCocheElecTextView.setText(String.valueOf(numCocheElec));
                }
            }
        });
        sumaCocheElecImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numCocheElec++;
                numCocheElecTextView.setText(String.valueOf(numCocheElec));
            }
        });
        restaMinusvImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numMinusv > 0) {
                    numMinusv--;
                    numMinusvTextView.setText(String.valueOf(numMinusv));
                }
            }
        });
        sumaMinusvImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numMinusv++;
                numMinusvTextView.setText(String.valueOf(numMinusv));
            }
        });
        restaMotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numMoto > 0) {
                    numMoto--;
                    numMotoTextView.setText(String.valueOf(numMoto));
                }
            }
        });
        sumaMotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numMoto++;
                numMotoTextView.setText(String.valueOf(numMoto));
            }
        });
    }
    private static void guardarCambios(Activity activity) {
        Parking.PlazaCoches = numCocheNormal;
        Parking.PlazasElectricos = numCocheElec;
        Parking.PlazasMinusvalidos = numMinusv;
        Parking.Motos = numMoto;

        Toast.makeText(activity, "Cambios guardados", Toast.LENGTH_SHORT).show();
    }

    private static void restablecerValores(Activity activity) {
        numCocheNormal = Parking.PlazaCoches;
        numCocheElec = Parking.PlazasElectricos;
        numMinusv = Parking.PlazasMinusvalidos;
        numMoto = Parking.Motos;

        numCocheNormalTextView.setText(String.valueOf(numCocheNormal));
        numCocheElecTextView.setText(String.valueOf(numCocheElec));
        numMinusvTextView.setText(String.valueOf(numMinusv));
        numMotoTextView.setText(String.valueOf(numMoto));

        guardarCambios(activity);
    }
}
