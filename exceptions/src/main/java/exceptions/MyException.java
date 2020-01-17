package exceptions;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class MyException extends RuntimeException {

    private ExceptionInfo exceptionInfo;

    public MyException(String exceptionDescription, ExceptionCode exceptionCode) {
        this.exceptionInfo = new ExceptionInfo(exceptionDescription, exceptionCode);
    }
}
