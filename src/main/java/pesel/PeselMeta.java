package pesel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeselMeta {

    private String pesel;
    private LocalDate birthDate;
    private Gender gender;
    private ApiResponse response;
}
