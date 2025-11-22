package com.unmei21.service.client.model;

import com.unmei21.core.uuid.GenID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientModel {
    private String id = GenID.uuid();
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
}
