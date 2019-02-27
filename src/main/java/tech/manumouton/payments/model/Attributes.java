package tech.manumouton.payments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attributes {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "###.##")
    @ApiModelProperty(name = "amount", value = "Payment amount", example = "100.21")
    @NotNull(message = "Payment amount is required")
    private BigDecimal amount;

    @ApiModelProperty(name = "currency", value = "Payment currency", example = "GBP")
    @NotNull(message = "Payment currency is required")
    @Size(min = 3, max = 3, message = "Currency should be in ISO format over 3 characters")
    private String currency;

    @JsonProperty("beneficiary_party")
    @ApiModelProperty(name = "beneficiary_party", value = "Payment beneficiary")
    @NotNull(message = "Beneficiary party is required")
    @Valid
    private Party beneficiaryParty;
    @JsonProperty("debtor_party")
    @ApiModelProperty(name = "debtor_party", value = "Payment debtor")
    @NotNull(message = "Debtor party is required")
    @Valid
    private Party debtorParty;
    @JsonProperty("sponsor_party")
    @ApiModelProperty(name = "sponsor_party", value = "Payment sponsor")
    @Valid
    private Party sponsorParty;

    @JsonProperty("charges_information")
    @ApiModelProperty(name = "charges_information", value = "Payment charges information")
    @Valid
    private ChargesInformation chargesInformation;

    @ApiModelProperty(name = "fx", value = "Payment FX information")
    @Valid
    private Fx fx;

    @JsonProperty("end_to_end_reference")
    @ApiModelProperty(name = "end_to_end_reference", value = "Payment end to end reference", example = "Wil piano Jan")
    @NotNull(message = "End to end reference is required")
    private String endToEndReference;

    @JsonProperty("numeric_reference")
    @ApiModelProperty(name = "numeric_reference", value = "Payment numeric reference", example = "1002001")
    @NotNull(message = "Numeric reference is required")
    private long numericReference;

    @JsonProperty("payment_id")
    @ApiModelProperty(name = "payment_id", value = "Payment ID", example = "123456789012345678")
    @NotNull(message = "Payment ID is required")
    private long paymentId;

    @JsonProperty("payment_purpose")
    @ApiModelProperty(name = "payment_purpose", value = "Payment purpose", example = "Paying for goods/services")
    private String paymentPurpose;

    @JsonProperty("payment_scheme")
    @ApiModelProperty(name = "payment_scheme", value = "Payment scheme", example = "FPS")
    private String paymentScheme;

    @JsonProperty("payment_type")
    @ApiModelProperty(name = "payment_type", value = "Payment type", example = "Credit")
    private String paymentType;

    @JsonProperty("processing_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "processing_date", value = "Payment processing date", example = "2017-01-18")
    private Date processingDate;

    @ApiModelProperty(name = "reference", value = "Payment reference", example = "Payment for Em's piano lessons")
    private String reference;

    @JsonProperty("scheme_payment_sub_type")
    @ApiModelProperty(name = "scheme_payment_sub_type", value = "Payment sub-type scheme", example = "InternetBanking")
    private String schemePaymentSubType;

    @JsonProperty("scheme_payment_type")
    @ApiModelProperty(name = "scheme_payment_type", value = "Payment type scheme", example = "ImmediatePayment")
    private String schemePaymentType;


}
