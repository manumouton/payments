package tech.manumouton.payments.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargesInformation {

    @JsonProperty("bearer_code")
    @ApiModelProperty(name = "bearer_code", value = "Charge Bearer code", example = "SHAR")
    private String bearerCode;

    @JsonProperty("sender_charges")
    @ApiModelProperty(name = "sender_charges", value = "Sender charges")
    @Valid
    private List<Charge> senderCharges;

    @JsonProperty("receiver_charges")
    @ApiModelProperty(name = "receiver_charges", value = "Receiver charges")
    @Valid
    private List<Charge> receiverCharges;
}
