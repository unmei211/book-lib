package com.unmei21.controller.booking.model;

import com.unmei21.controller.author.model.AuthorView;
import com.unmei21.controller.book.model.BookView;
import com.unmei21.controller.client.model.ClientView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailsView {
    private String firstName;
    private String lastName;
    private String middleName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    private String isbnCode;

    private String name;

    private AuthorView author;

    private LocalDate bookedAt;
}
