package com.shah.bankingapplicationcrud.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
    private UUID accountNumber;

    @Column(updatable = false, nullable = false, unique = true)
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
