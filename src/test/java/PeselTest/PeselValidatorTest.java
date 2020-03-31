package PeselTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mock;
import pesel.ApiResponse;
import pesel.PeselValidator;
import sun.security.krb5.internal.APOptions;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class PeselValidatorTest {

    @Mock
    PeselValidator peselValidator;

    @BeforeEach
    void setUp() {
        peselValidator = new PeselValidator();
    }

    @TestFactory
    Iterable<DynamicTest> DynamicTestCheckingIfPeselIsCorrect() {
        return Arrays.asList(
                dynamicTest(
                        "incorrect PESEL regex",
                        () -> assertEquals(
                                peselValidator.checkIfPeselIsValid("!@#$%^&*()"),
                                new ApiResponse(false, "Regex Exception, " +
                                        "pesel should contain exectly 11 digits"))),
                dynamicTest(
                        "incorrect pesel control sum",
                        ()->assertEquals(
                                peselValidator.checkIfPeselIsValid("01281106864"),
                                new ApiResponse(false, "Pesel control sum failed, invalid Pesel"))),
                dynamicTest(
                        "correct pesel from 2200-2299 range",
                        ()->assertEquals(
                                peselValidator.checkIfPeselIsValid("50652013910"),
                                new ApiResponse(true, "Pesel correct"))),
                dynamicTest(
                        "correct pesel from 2100-2199 range",
                        ()-> assertEquals(peselValidator.checkIfPeselIsValid("01470105589"),
                                new ApiResponse(true, "Pesel correct"))),
                dynamicTest(
                        "correct pesel from 2000-2099 range",
                        ()-> assertEquals(
                                peselValidator.checkIfPeselIsValid("01273104787"),
                                new ApiResponse(true, "Pesel correct"))),
                dynamicTest(
                        "correct pesel from 1900-1999 range",
                        ()->assertEquals(peselValidator.checkIfPeselIsValid("19100505029"),
                                new ApiResponse(true, "Pesel correct"))),
                dynamicTest(
                        "correct pesel from 1800-1899 range",
                        ()->assertEquals(peselValidator.checkIfPeselIsValid("98920502256"),
                                new ApiResponse(true, "Pesel correct")))
        );
    }
}
