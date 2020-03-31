package pesel;

import com.google.common.collect.ImmutableMap;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class PeselDecoder {

    private final int JANUARY = 1;
    private final int DECEMBER = 12;

    private static final ImmutableMap<Integer, Integer> PESEL_MONTH_YEAR_PATTERN = ImmutableMap.of(
            80, 1800,
            0, 1900,
            20, 2000,
            40, 2100,
            60, 2200

    );

    public Optional<LocalDate> generateBirthDate(String pesel) {
        int day = getIntValue(pesel, 4, 6);
        int month = getIntValue(pesel, 2, 4);
        int year = getIntValue(pesel, 0, 2);

        for (Map.Entry<Integer, Integer> entry : PESEL_MONTH_YEAR_PATTERN.entrySet()) {
            int monthPrefix = entry.getKey();
            int yearPrefix = entry.getValue();
            if (checkMonth(month, monthPrefix)) {
                month -= monthPrefix;
                year += yearPrefix;
                return checkDate(year, month, day);
            }
        }
        return Optional.empty();
    }

    private int getIntValue(String pesel, int startIndex, int endIndex) {
        return Integer.parseInt(pesel.substring(startIndex, endIndex));
    }

    private boolean checkMonth(int month, int monthPrefix) {
        return (month >= monthPrefix + JANUARY && month <= monthPrefix + DECEMBER);
    }

    private Optional<LocalDate> checkDate(int year, int month, int day) {
        LocalDate birthDate;
        try {
            birthDate = LocalDate.of(year, month, day);
            return Optional.of(birthDate);
        } catch (DateTimeException ex) {
            return Optional.empty();
        }
    }
    public Gender getGender(String pesel) {
        if (getIntValue(pesel, 9, 10) % 2 != 0) {
            return Gender.MALE;
        } else {
            return Gender.FEMALE;
        }
    }


}
