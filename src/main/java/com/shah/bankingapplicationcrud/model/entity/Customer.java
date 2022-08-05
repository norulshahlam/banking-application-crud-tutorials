package com.shah.bankingapplicationcrud.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.shah.bankingapplicationcrud.model.enums.Gender;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(updatable = false, nullable = false, unique = true)
    @Id
    private UUID id;

    @Column(updatable = true, nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private int age;
    private BigDecimal accBalance;
    private String gender;
    private String country;
    private String designation;
    private Date birthDate;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime updatedAt;

}
