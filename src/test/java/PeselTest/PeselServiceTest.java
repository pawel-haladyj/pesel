package PeselTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mockito;
import pesel.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.when;

public class PeselServiceTest {

    private static final int PESEL_LENGHT = 11;

    private PeselService peselService;
    private PeselValidator peselValidator;
    private PeselDecoder peselDecoder;

    @BeforeEach
    void setUp() {
        peselDecoder = Mockito.mock(PeselDecoder.class);
        peselValidator = Mockito.mock(PeselValidator.class);
        peselService = new PeselService(peselDecoder, peselValidator);
    }

    @Test
    void shouldThrowExceptionWhenExtractingFromNullPesel() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> peselService.extract(null));
        assertEquals("Expected non-null PESEL", exception.getMessage());
    }

    @TestFactory
    Iterable<DynamicTest> dynamicTestsForExtractingPeselMetaDataForDifferentScenarios() {
        return Arrays.asList(
                dynamicTest(
                        "incorect pesel regex",
                        () -> {
                            String givenPesel = "123qwe!#";
                            when(peselValidator.checkIfPeselIsValid(givenPesel))
                                    .thenReturn(
                                            new ApiResponse(
                                                    false,
                                                    "Regex Exception, pesel should contain exectly 11 digits"
                                            )
                                    );
                            assertEquals(
                                    peselService.extract(givenPesel),
                                    new PeselMeta(
                                            "123qwe!#",
                                            null,
                                            null,
                                            new ApiResponse(false, "Regex Exception, pesel should contain exectly 11 digits")
                                    )
                            );
                        }

                ),
                dynamicTest("incorrect pesel control sum",
                        () -> {
                            String givenPesel = "01281106865";
                            when(peselValidator.checkIfPeselIsValid(givenPesel))
                                    .thenReturn(
                                            new ApiResponse(false, "Pesel control sum failed, invalid Pesel"));
                            assertEquals(
                                    peselService.extract(givenPesel),
                                    new PeselMeta(givenPesel, null, null,
                                            new ApiResponse(false, "Pesel control sum failed, invalid Pesel"))
                            );
                        }
                ),
                dynamicTest("incorrect birth date - not existing date",
                        () -> {
                            String givenPesel = "01263106863";
                            when(peselValidator.checkIfPeselIsValid(givenPesel))
                                    .thenReturn(new ApiResponse(true, "Pesel correct"));
                            when(peselDecoder.generateBirthDate(givenPesel))
                                    .thenReturn(Optional.empty());

                            assertEquals(peselService.extract(givenPesel),
                                    new PeselMeta(givenPesel, null, null,
                                            new ApiResponse(false, "Pesel compilation failed")));
                        }
                ),
                dynamicTest("incorect pesel month value",
                        () -> {
                            String givenPesel = "01190106866";
                            when(peselValidator.checkIfPeselIsValid(givenPesel))
                                    .thenReturn(new ApiResponse(true, "Pesel correct"));
                            when(peselDecoder.generateBirthDate(givenPesel))
                                    .thenReturn(Optional.empty());
                            assertEquals(peselService.extract(givenPesel),
                                    new PeselMeta(givenPesel, null, null,
                                            new ApiResponse(false, "Pesel compilation failed")));
                        }
                ),
                dynamicTest("correct pesel from 2200-2299 range",
                        () -> {
                            String givenPesel = "01610112718";
                            when(peselValidator.checkIfPeselIsValid(givenPesel))
                                    .thenReturn(new ApiResponse(true, "Pesel encoded successfully"));
                            when(peselDecoder.generateBirthDate(givenPesel))
                                    .thenReturn(Optional.of(LocalDate.of(2201, 1, 1)));
                            when(peselDecoder.getGender(givenPesel)).thenReturn(Gender.MALE);

                            assertEquals(
                                    peselService.extract(givenPesel),
                                    new PeselMeta(givenPesel,
                                            LocalDate.of(2201, 1, 1),
                                            Gender.MALE,
                                            new ApiResponse(true, "Pesel encoded successfully"))
                            );
                        }
                )
        );
    }
}
