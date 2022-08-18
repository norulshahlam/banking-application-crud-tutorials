package com.shah.bankingapplicationcrud.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetOneCustomerRequest {

    @NotBlank
    @ApiModelProperty(value = "Customer ID", required = true, example = "f9bd1139-c907-11ec-b11c-0242ac110002")
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Value must be in UUID format eg f9bd1139-c907-11ec-b11c-0242ac110002")
    private String id;
}
