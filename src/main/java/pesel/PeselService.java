package pesel;

import java.time.LocalDate;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

public class PeselService {

    private final PeselDecoder peselDecoder;
    private final PeselValidator peselValidator;

    public PeselService(PeselDecoder peselDecoder, PeselValidator peselValidator) {
        this.peselDecoder = peselDecoder;
        this.peselValidator = peselValidator;
    }

    public PeselMeta extract(String pesel) {
        checkArgument(nonNull(pesel), "Expected non-null PESEL");

        ApiResponse response = peselValidator.checkIfPeselIsValid(pesel);
        if (!response.isSuccess()) {
            return new PeselMeta(pesel, null, null, response);
        }

        Optional<LocalDate> optionalBirthDate =peselDecoder.generateBirthDate(pesel);
        Gender gender =peselDecoder.getGender(pesel);

        return optionalBirthDate
                .map(birthDate -> new PeselMeta(pesel,birthDate,gender,
                        new ApiResponse(true, "Pesel encoded successfully")))
                .orElseGet(()-> new PeselMeta(pesel, null, null,
                        new ApiResponse(false, "Pesel compilation failed")));
    }
}
