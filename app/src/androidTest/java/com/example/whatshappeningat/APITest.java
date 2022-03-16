package com.example.whatshappeningat;

        import android.content.Context;

        import androidx.test.platform.app.InstrumentationRegistry;
        import androidx.test.ext.junit.runners.AndroidJUnit4;

        import org.junit.Assert;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import static org.junit.Assert.*;

        import java.util.ArrayList;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class APITest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.whatshappeningat", appContext.getPackageName());
    }

    @Test
    public void getCoordsTest() {

        double[] expectedArray1 = {51.5085, -0.1257};
        double[] expectedArray2 = {39.9042, 116.4074};

        try {
            Assert.assertArrayEquals(expectedArray1, API.getCoords("London"), 1.0);
            Assert.assertArrayEquals(expectedArray1, API.getCoords("london"), 1.0);
            Assert.assertArrayEquals(expectedArray1, API.getCoords("london "), 1.0);

            Assert.assertArrayEquals(expectedArray2, API.getCoords("beijing"), 1.0);

        } catch (APIException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void weatherTest() {

        double[] coords = {51.5085, -0.1257};

        try {
            ArrayList<String[]> weather = API.getWeather(coords);

        } catch (APIException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}