package com.pgimenez.test.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiExceptionResponse {
    
    private String code; // Código interno del error
    private String errorMessage; // Mensaje de error

}
