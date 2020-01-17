package exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class ExceptionInfo {

    private String description;
    private ExceptionCode exceptionCode;
    private LocalDate exceptionDate;


    public ExceptionInfo(String description, ExceptionCode exceptionCode) {
        this.description = description;
        this.exceptionCode = exceptionCode;
        this.exceptionDate = LocalDate.now();
    }
}
