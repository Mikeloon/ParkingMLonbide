package com.lksnext.parkingmlonbide.DataClasses;

import java.util.Date;
import java.security.SecureRandom;
import java.util.UUID;

public class Reserva {

    private int id;
    private Date fechaReserva;
    private int horasReserva;
    private TipoEstacionamiento tipoPlaza;

    public Reserva() {
    }

    public Reserva(Date fechaReserva, int horasReserva, TipoEstacionamiento tipoPlaza) {
        this.id = generateRandomId();
        this.fechaReserva = fechaReserva;
        this.horasReserva = horasReserva;
        this.tipoPlaza = tipoPlaza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(Date fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public int getHorasReserva() {
        return horasReserva;
    }

    public void setHorasReserva(int horasReserva) {
        this.horasReserva = horasReserva;
    }

    public TipoEstacionamiento getTipoPlaza() {
        return tipoPlaza;
    }

    public void setTipoPlaza(TipoEstacionamiento tipoPlaza) {
        this.tipoPlaza = tipoPlaza;
    }

    public static int generateRandomId() {
        SecureRandom secureRandom = new SecureRandom();
        UUID uuid = new UUID(secureRandom.nextLong(), secureRandom.nextLong());
        return uuid.hashCode();
    }
}
