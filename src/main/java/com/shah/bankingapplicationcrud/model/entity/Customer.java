package com.shah.bankingapplicationcrud.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.UUID;

@JsonPropertyOrder({"firstName","lastName"})
@Builder
@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="uuid-char")
    @Column(updatable = false, nullable = false, unique = true)
    @Id
    private UUID id;

    @Column(updatable = true, nullable = false, unique = true)
    @Email(message = "Enter a valid email")
    private String email;

    @NotNull(message = "First name cannot be empty")
    @Size(min = 3, message = "First name character must be more than 3!")
    private String firstName;
    @Size(min = 3, message = "Last name character must be more than 3!")
    private String lastName;

    @Range(min = 21, max = 55, message = "Age must be between 21 and 55")
    private int age;

    @JsonIgnore
    private Double accBalance;

    @NotNull(message = "Gender cannot be empty")
    private String gender;

    @NotNull(message = "Country cannot be empty")
    private String country;

    @JsonProperty("Job Scope")
    private String designation;

    @CreationTimestamp
    private Date createdAt;
    @DateTimeFormat
    private Date birthDate;
}
