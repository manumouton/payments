package tech.manumouton.payments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fx {

    @JsonProperty("contract_reference")
    @ApiModelProperty(name = "contract_reference", value = "FX Contract reference", example = "FX123")
    @NotNull
    private String contractReference;

    @JsonProperty("exchange_rate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "###.#####")
    @ApiModelProperty(name = "exchange_rate", value = "Exchange rate", example = "2.00000")
    @NotNull
    private BigDecimal exchangeRate;

    @JsonProperty("original_amount")
    @ApiModelProperty(name = "original_amount", value = "Original amount", example = "200.42")
    @NotNull
    private BigDecimal originalAmount;

    @JsonProperty("original_currency")
    @ApiModelProperty(name = "original_currency", value = "Original currency", example = "USD")
    @Size(min = 3, max = 3, message = "Currency should be in ISO format over 3 characters")
    @NotNull
    private String originalCurrency;

}
