package com.shah.bankingapplicationcrud.model.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Date;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    @ApiModelProperty(notes = "Email address",
            name = "email", required = true,
            value = "abc@email.com",
            dataType = "String",
            example = "abcd@email.com")
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "First name cannot be empty")
    @Size(min = 3, message = "First name character must be more than 3!")
    @ApiModelProperty(example = "Christopher")
    private String firstName;

    @ApiModelProperty(example = "Columbus")
    @Size(min = 3, message = "Last name character must be more than 3!")
    private String lastName;

    @ApiModelProperty(example = "21")
    @Range(min = 21, max = 55, message = "Age must be between 21 and 55")
    private int age;

    @ApiModelProperty(example = "150000.33")
    @Digits(integer = 8, fraction = 2, message = "Whole number only up to 6 digits and in 2 decimal points!")
    private BigDecimal accBalance;

    @ApiModelProperty(example = "male")
    @NotNull(message = "Gender cannot be empty")
    @Pattern(regexp = "Male|Female", message = "Gender must be Male or Female")
    private String gender;

    @ApiModelProperty(example = "Japan")
    @NotNull(message = "Country cannot be empty")
    private String country;

    @ApiModelProperty(example = "Cleaner")
    private String designation;

    @ApiModelProperty(example = "1968-04-24")
    @DateTimeFormat
    @Past(message = "Date cannot be tomorrow onwards")
    private Date birthDate;
}
