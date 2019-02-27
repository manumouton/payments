package tech.manumouton.payments.rest.errors;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiError {

    private List<ApiValidationError> errors;

    public ApiError(){
        this.errors = new ArrayList<>();
    }

    public void addValidationError(final ApiValidationError error){
        this.errors.add(error);
    }
}
