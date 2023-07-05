package com.lksnext.parkingmlonbide;

import static org.junit.Assert.assertEquals;

import com.lksnext.parkingmlonbide.NavFragments.BookingFragment;

import org.junit.Test;

public class BookingFragmentTest {

    @Test
    public void testCalcularDuracionHoras() {
        BookingFragment fragment = new BookingFragment();

        // Caso de prueba 1: horaInicio = "08:00", horaFin = "08:30"
        int duracionHoras1 = fragment.calcularDuracionHoras("08:00", "010:30");
        assertEquals(3, duracionHoras1);

        // Caso de prueba 2: horaInicio = "09:00", horaFin = "10:30"
        int duracionHoras2 = fragment.calcularDuracionHoras("09:00", "10:30");
        assertEquals(2, duracionHoras2);

        // Caso de prueba 3: horaInicio = "14:00", horaFin = "15:00"
        int duracionHoras3 = fragment.calcularDuracionHoras("14:00", "15:00");
        assertEquals(1, duracionHoras3);
    }

    @Test
    public void testObtenerHoras() {
        BookingFragment fragment = new BookingFragment();

        // Caso de prueba 1: tiempo = "08:30"
        int horas1 = fragment.obtenerHoras("08:30");
        assertEquals(8, horas1);

        // Caso de prueba 2: tiempo = "12:45"
        int horas2 = fragment.obtenerHoras("12:45");
        assertEquals(12, horas2);

        // Caso de prueba 3: tiempo = "18:15"
        int horas3 = fragment.obtenerHoras("18:15");
        assertEquals(18, horas3);
    }

    @Test
    public void testObtenerMinutos() {
        BookingFragment fragment = new BookingFragment();

        // Caso de prueba 1: tiempo = "08:30"
        int minutos1 = fragment.obtenerMinutos("08:30");
        assertEquals(30, minutos1);

        // Caso de prueba 2: tiempo = "12:45"
        int minutos2 = fragment.obtenerMinutos("12:45");
        assertEquals(45, minutos2);

        // Caso de prueba 3: tiempo = "18:15"
        int minutos3 = fragment.obtenerMinutos("18:15");
        assertEquals(15, minutos3);
    }
}
