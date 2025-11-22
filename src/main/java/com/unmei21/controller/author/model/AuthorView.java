package com.unmei21.controller.author.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorView {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
}
