package com.lksnext.parkingmlonbide;

import android.app.Instrumentation;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.lksnext.parkingmlonbide.R;
import com.lksnext.parkingmlonbide.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);
    private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    private UiDevice device = UiDevice.getInstance(instrumentation);
    @Test
    public void testLogin() throws InterruptedException {
        // Esperar a que se cargue la actividad de inicio de sesión
        ActivityScenario.launch(MainActivity.class);

        // Realizar la acción de ingresar el correo y la contraseña
        Espresso.onView(ViewMatchers.withId(R.id.userEmail))
                .perform(ViewActions.typeText("mikellonbide@gmail.com"));

        Espresso.onView(ViewMatchers.withId(R.id.password))
                .perform(ViewActions.typeText("mikelon"));

        // Hacer clic en el botón de inicio de sesión
        Espresso.onView(ViewMatchers.withId(R.id.loginbtn))
                .perform(ViewActions.click());

        device.wait(Until.hasObject(By.res("com.lksnext.parkingmlonbide:id/draweLayout")), 4000);
        Espresso.onView(ViewMatchers.withId(R.id.draweLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
    @Test
    public void testBooking() throws InterruptedException {
        ActivityScenario.launch(MainActivity.class);

        // Realizar la acción de ingresar el correo y la contraseña
        Espresso.onView(ViewMatchers.withId(R.id.userEmail))
                .perform(ViewActions.typeText("mikellonbide@gmail.com"));

        Espresso.onView(ViewMatchers.withId(R.id.password))
                .perform(ViewActions.typeText("mikelon"));

        // Hacer clic en el botón de inicio de sesión
        Espresso.onView(ViewMatchers.withId(R.id.loginbtn))
                .perform(ViewActions.click());

        device.wait(Until.hasObject(By.res("com.lksnext.parkingmlonbide:id/draweLayout")), 4000);
        Espresso.onView(ViewMatchers.withId(R.id.draweLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.imgMenu)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.menuReserva)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.BookingFragment))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.btnSiguiente)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.LinearLayoutRealizarReservaCoche)).perform(ViewActions.click());
        device.wait(Until.hasObject(By.res("com.lksnext.parkingmlonbide:id/popup")), 3000);
        Espresso.onView(ViewMatchers.withId(R.id.btnCerrar))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.btnAceptar)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.imgMenu)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.menuProfile)).perform(ViewActions.click());

        device.wait(Until.hasObject(By.res("com.lksnext.parkingmlonbide:id/profileFragment")), 3000);
        Espresso.onView(ViewMatchers.withId(R.id.buttonCancelar)).perform(ViewActions.click());
    }
}

