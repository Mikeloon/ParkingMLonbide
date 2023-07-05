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
public class AdminTest {

    private Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    private UiDevice device = UiDevice.getInstance(instrumentation);

    @Test
    public void checkGetAdminRole(){
        ActivityScenario.launch(MainActivity.class);

        // Realizar la acción de ingresar el correo y la contraseña
        Espresso.onView(ViewMatchers.withId(R.id.userEmail))
                .perform(ViewActions.typeText("mikel@maik.com"));

        Espresso.onView(ViewMatchers.withId(R.id.password))
                .perform(ViewActions.typeText("mikelon"));

        // Hacer clic en el botón de inicio de sesión
        Espresso.onView(ViewMatchers.withId(R.id.loginbtn))
                .perform(ViewActions.click());

        device.wait(Until.hasObject(By.res("com.lksnext.parkingmlonbide:id/draweLayout")), 4000);
        Espresso.onView(ViewMatchers.withId(R.id.draweLayout))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.imgMenu)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.menuAdmin)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.gestionlbl))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
