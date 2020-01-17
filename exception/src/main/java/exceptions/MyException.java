package exceptions;


import lombok.*;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter

public class MyException extends RuntimeException {

    private ExceptionInfo exceptionInfo;

    public MyException(String exceptionDescription, ExceptionCode exceptionCode) {
        this.exceptionInfo = new ExceptionInfo(exceptionDescription, exceptionCode);
    }
}
