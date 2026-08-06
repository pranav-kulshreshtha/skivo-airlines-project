package com.msp.payloads.requests;

import com.msp.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerRequest {

    @NotBlank(message = "First name is required!")
    private String firstName;

    @NotBlank(message = "Last name is required!")
    private String lastName;

    @NotBlank(message = "Email is required!")
    @Email(message = "Email should be valid!")
    private String email;

    @NotBlank(message = "Phone number is required!")
    @Pattern(regexp = "^\\+[0-9]{1,3}[0-9]{4,14}$",
            message = "Phone number must be in a valid format!")
    private String phone;

    @NotNull(message = "Date of birth is required!")
    @Past(message = "Date of birth must be in the past!")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required!")
    private Gender gender;

    @NotNull(message = "Seat instance ID is required!")
    private Long seatInstanceId;

    private String nationality;

    private String passportNumber;
    private String frequentFlyerNumber;

    private Boolean requiresWheelchairAssistance;
    private String dietaryPreferences;
    private String medicalConditions;
}
