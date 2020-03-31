package PeselTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import pesel.Gender;
import pesel.PeselDecoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class PeselDecoderTest {

    private PeselDecoder peselDecoder;

    @BeforeEach
    void setUp() {
        peselDecoder = new PeselDecoder();
    }

    @TestFactory
    Iterable<DynamicTest> dynamicTestForDecodingBirthDate() {
        return Arrays.asList(
                dynamicTest(
                        "incorect birthdate - error during parse decode birthdate value",
                        () -> assertEquals(peselDecoder.generateBirthDate("01263106863"), Optional.empty())),
                dynamicTest("incorrect month code",
                        () -> assertEquals(peselDecoder.generateBirthDate("01190106866"), Optional.empty())),
                dynamicTest(
                        "correct pesel from 2200-2299 range",
                        () ->
                                assertEquals(
                                        peselDecoder.generateBirthDate("50652013910"),
                                        Optional.of(LocalDate.of(2250, 5, 20)))),
                dynamicTest(
                        "correct pesel from 2100-2199 range",
                        () ->
                                assertEquals(
                                        peselDecoder.generateBirthDate("01470105589"),
                                        Optional.of(LocalDate.of(2101, 7, 1)))),
                dynamicTest(
                        "correct pesel from 2000-2099 range",
                        () ->
                                assertEquals(
                                        peselDecoder.generateBirthDate("01273104787"),
                                        Optional.of(LocalDate.of(2001, 7, 31)))),
                dynamicTest(
                        "correct pesel from 1900-1999 range",
                        () ->
                                assertEquals(
                                        peselDecoder.generateBirthDate("19100505029"),
                                        Optional.of(LocalDate.of(1919, 10, 5)))),
                dynamicTest(
                        "correct pesel from 1800-1899 range",
                        () ->
                                assertEquals(
                                        peselDecoder.generateBirthDate("98920502256"),
                                        Optional.of(LocalDate.of(1898, 12, 5))))
        );

    }
    @TestFactory
    Iterable<DynamicTest> dynamicTestsForDecodeGender() {
        return Arrays.asList(
                dynamicTest(
                        "decode female gender",
                        () -> assertEquals(peselDecoder.getGender("01263106863"), Gender.FEMALE)),
                dynamicTest(
                        "decode female gender",
                        () -> assertEquals(peselDecoder.getGender("01263106863"), Gender.FEMALE)));
    }

}
