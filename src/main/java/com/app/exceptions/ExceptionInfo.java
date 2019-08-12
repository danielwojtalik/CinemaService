package com.app.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class ExceptionInfo {

    private String description;
    private ExceptionCode exceptionCode;
    private LocalDateTime exceptionDate;


    public ExceptionInfo(String description, ExceptionCode exceptionCode) {
        this.description = description;
        this.exceptionCode = exceptionCode;
        this.exceptionDate = LocalDateTime.now();
    }
}
