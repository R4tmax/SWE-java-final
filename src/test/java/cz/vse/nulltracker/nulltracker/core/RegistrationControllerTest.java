package cz.vse.nulltracker.nulltracker.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationControllerTest {

    RegistrationController rgc = new RegistrationController();

    @Test
    public void pwCheck() {
        assertFalse(rgc.isPassSafe("hereispassword")); //does not contain number
        assertFalse(rgc.isPassSafe("123456789")); // does not contain letter
        assertFalse(rgc.isPassSafe("pa1")); //does not contain enough characters
        assertTrue(rgc.isPassSafe("hereisstrongpassword4204520"));

    }
}