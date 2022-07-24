package com.shah.bankingapplicationcrud.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePatch {

	@Column(updatable = true, unique = true)
	@Email(message = "Enter a valid email")
	private String email;

	@Size(min = 3, message = "First name character must be more than 3!")
	private String firstName;
	
	@Size(min = 3, message = "Last name character must be more than 3!")
	private String lastName;

	@Range(min = 21, max = 55, message = "Age must be between 21 and 55")
	private Integer age;
	private Double accBalance;
	private String gender;
	private String country;
	private String designation;

	@DateTimeFormat
	private Date birthDate;
}
