package tech.manumouton.payments.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Payment", description="Describes a Payment")
public class Payment {

    @Id
    @ApiModelProperty(hidden = true)
    private ObjectId id;

    @NotNull
    @ApiModelProperty(name = "type", value = "The type of this resource", example = "Payment")
    private String type;

    @NotNull
    @ApiModelProperty(name = "version", value = "The version of this resource", example = "0")
    private int version;

    @JsonProperty("organisation_id")
    @NotBlank
    @ApiModelProperty(name= "organisation_id", value = "The organisation ID", example = "743d5b63-8e6f-432e-a8fa-c5d8d2ee5fcb")
    private String organisationId;

    @ApiModelProperty(name = "attributes", value = "The attributes of this resource")
    @Valid
    private Attributes attributes;

}
