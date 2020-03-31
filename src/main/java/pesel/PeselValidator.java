package pesel;

import java.util.regex.Pattern;

public class PeselValidator {

    private final int[] WEIGHTS = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
    private static int PESEL_LENGTH = 11;

    private static final Pattern PESEL_PATTERN = Pattern.compile("^\\d{" + PESEL_LENGTH + "}$");

    public ApiResponse checkIfPeselIsValid(String pesel) {
        if (!isRegexRooleValid(pesel)) {
            return new ApiResponse(false, "Regex Exception, pesel should contain exectly 11 digits");
        }
        if (!checkControlSumPesel(pesel)) {
            return new ApiResponse(false, "Pesel control sum failed, invalid Pesel");
        }
        return new ApiResponse(true, "Pesel correct");
    }

    private boolean checkControlSumPesel(String pesel) {
        int calculatedSum = 0;
        int digit;
        for (int i = 0; i < PESEL_LENGTH - 1; i++) {
            digit = Integer.parseInt(pesel.substring(i, i + 1));
            calculatedSum += digit * WEIGHTS[i];
        }
        calculatedSum = (10 - (calculatedSum % 10)) % 10;
        int validationNumber = Integer.parseInt(pesel.substring(PESEL_LENGTH - 1, PESEL_LENGTH));
        return calculatedSum == validationNumber;
    }

    private boolean isRegexRooleValid(String pesel) {
        return PESEL_PATTERN.matcher(pesel).matches();
    }
}
