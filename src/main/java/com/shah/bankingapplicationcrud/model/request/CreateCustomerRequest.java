package com.shah.bankingapplicationcrud.model.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author NORUL
 */
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    @Schema(name = "email",
            type = "String",
            example = "abcd@email.com")
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "First name cannot be empty")
    @Size(min = 3, message = "First name character must be more than 3!")
    @Schema(example = "Christopher")
    private String firstName;

    @Schema(example = "Columbus")
    @Size(min = 3, message = "Last name character must be more than 3!")
    private String lastName;

    @Schema(example = "21")
    @Range(min = 21, max = 55, message = "Age must be between 21 and 55")
    private int age;

    @Schema(example = "150000.33")
    @Digits(integer = 8, fraction = 2, message = "Whole number only up to 6 digits and in 2 decimal points!")
    private BigDecimal accBalance;

    @Schema(example = "male")
    @NotNull(message = "Gender cannot be empty")
    @Pattern(regexp = "(?i)Male|Female", message = "Gender must be Male or Female")
    private String gender;

    @Schema(example = "Japan")
    @NotNull(message = "Country cannot be empty")
    private String country;

    @Schema(example = "Cleaner")
    private String designation;

    @Schema(example = "1968-04-24")
    @DateTimeFormat
    @Past(message = "Date cannot be tomorrow onwards")
    private Date birthDate;
}
