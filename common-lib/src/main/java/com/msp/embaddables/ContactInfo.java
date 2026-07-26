package com.msp.embaddables;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ContactInfo {
    private String email;
    private String phone;
}
