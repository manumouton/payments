package tech.manumouton.payments.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Party {

    @JsonProperty("account_name")
    @ApiModelProperty(name = "account_name", value = "Account name", example = "W Owens")
    private String accountName;
    @JsonProperty("account_number")
    @ApiModelProperty(name = "account_number", value = "Account number", example = "31926819")
    @NotNull(message = "Account number is required when defining a party")
    private String accountNumber;
    @JsonProperty("account_number_code")
    @ApiModelProperty(name = "account_number_code", value = "Account number code", example = "BBAN")
    private String accountNumberCode;
    @JsonProperty("account_type")
    @ApiModelProperty(name = "account_type", value = "Account type", example = "0")
    private int accountType;

    @JsonProperty("bank_id")
    @ApiModelProperty(name = "bank_id", value = "Bank id", example = "403000")
    @NotNull(message = "Bank ID is required when defining a party")
    private int bankId;
    @JsonProperty("bank_id_code")
    @ApiModelProperty(name = "bank_id_code", value = "Bank id code", example = "GBDSC")
    @NotNull(message = "Bank ID code is required when defining a party")
    private String bankIdCode;

    @ApiModelProperty(name = "name", value = "Name", example = "Wilfred Jeremiah Owens")
    private String name;
    @ApiModelProperty(name = "address", value = "Address", example = "1 The Beneficiary Localtown SE2")
    private String address;
}
