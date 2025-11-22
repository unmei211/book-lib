package com.unmei21.service.book.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookCommandModel {
    private String bookName;
    private String authorFirstName;
    private String authorLastName;
    private String authorMiddleName;
    private String isbnCode;
}
