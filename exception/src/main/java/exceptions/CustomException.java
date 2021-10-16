package exceptions;


import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter

public class CustomException extends RuntimeException {

    private ExceptionInfo exceptionInfo;

    public CustomException(String exceptionDescription, ExceptionCode exceptionCode) {
        this.exceptionInfo = new ExceptionInfo(exceptionDescription, exceptionCode);
    }
}
