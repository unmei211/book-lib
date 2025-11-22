package com.unmei21.controller.book.model;

import com.unmei21.controller.author.model.AuthorSimpleView;
import com.unmei21.controller.author.model.AuthorView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookView {
    private String id;

    private String isbnCode;

    private String name;

    private Instant updatedAt;
    private Instant createdAt;

    private AuthorView author;
}