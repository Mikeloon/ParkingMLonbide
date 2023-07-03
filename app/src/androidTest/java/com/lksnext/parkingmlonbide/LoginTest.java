package com.lksnext.parkingmlonbide;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.lksnext.parkingmlonbide.R;
import com.lksnext.parkingmlonbide.view.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testLogin() {
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

        Espresso.onView(ViewMatchers.withId(R.id.draweLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}

