package com.unmei21.service.book.model;

import com.unmei21.repository.entity.AuthorEntity;
import com.unmei21.service.author.model.AuthorModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookModel {
    private String id;

    private String isbnCode;

    private String name;

    private AuthorModel author;

    private Instant createdAt;

    private Instant updatedAt;
}
