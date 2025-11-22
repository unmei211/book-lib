package com.unmei21.service.author.model;

import com.unmei21.core.uuid.GenID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorModel {
    private String id = GenID.uuid();
    private String firstName;
    private String lastName;
    private String middleName;
}
