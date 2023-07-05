package com.lksnext.parkingmlonbide.DataClasses;

public class Parking {
    public static int PlazaCoches;
    public static int PlazasElectricos;
    public static int PlazasMinusvalidos;
    public static int Motos;

    static {
        PlazaCoches = 4;
        PlazasElectricos = 2;
        PlazasMinusvalidos = 2;
        Motos = 2;
    }

    public static void actualizarPlazas(int cocheNormal, int cocheElec, int minusv, int moto) {
        PlazaCoches = cocheNormal;
        PlazasElectricos = cocheElec;
        PlazasMinusvalidos = minusv;
        Motos = moto;
    }
}
