package tech.manumouton.payments.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import tech.manumouton.payments.rest.errors.ApiError;
import tech.manumouton.payments.rest.errors.ApiValidationError;

@Service
@Slf4j
public class ErrorService {

    public ApiError buildBodyResponse(Errors errors) {
        ApiError apiError = new ApiError();
        errors.getFieldErrors().forEach(fieldError -> {
            ApiValidationError apiValidationError = ApiValidationError.builder()
                    .field(fieldError.getField())
                    .errorMessage(fieldError.getDefaultMessage())
                    .jsonPath(fieldError.getObjectName() + "." + fieldError.getField())
                    .rejectedValue(fieldError.getRejectedValue()).build();
            apiError.addValidationError(apiValidationError);
        });
        return apiError;
    }

}
