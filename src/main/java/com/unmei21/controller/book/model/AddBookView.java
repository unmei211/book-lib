package com.unmei21.controller.book.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBookView {
    private String bookName;
    private String authorFirstName;
    private String authorMiddleName;
    private String authorLastName;
    private String isbnCode;
}
