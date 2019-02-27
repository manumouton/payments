package tech.manumouton.payments.rest.errors;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiValidationError {

    String jsonPath;
    String field;
    String errorMessage;
    Object rejectedValue;

}
