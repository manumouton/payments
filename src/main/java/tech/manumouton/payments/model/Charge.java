package tech.manumouton.payments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Charge {

    @ApiModelProperty(name = "currency", value = "Charge currency", example = "USD")
    @NotNull(message = "Currency is mandatory when defining a charge")
    @Size(min = 3, max = 3, message = "Currency should be in ISO format over 3 characters")
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "###.##")
    @ApiModelProperty(name = "amount", value = "Charge amount", example = "10.00")
    @NotNull(message = "Amount is mandatory when defining a charge")
    private BigDecimal amount;
}
