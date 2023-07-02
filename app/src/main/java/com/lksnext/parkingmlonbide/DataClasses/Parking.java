package com.lksnext.parkingmlonbide.DataClasses;

public class Parking {
    public static int PlazaCoches;
    public static int PlazasElectricos;
    public static int PlazasMinusvalidos;
    public static int Motos;

    public static void actualizarPlazas(int cocheNormal, int cocheElec, int minusv, int moto) {
        PlazaCoches = cocheNormal;
        PlazasElectricos = cocheElec;
        PlazasMinusvalidos = minusv;
        Motos = moto;
    }
}
