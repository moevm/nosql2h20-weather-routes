package nosql2h20.weather.routes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainTest {
    @Test
    void appHasAGreeting() {
        Main classUnderTest = new Main();

        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
