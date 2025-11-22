package com.unmei21.service.book;

import com.unmei21.controller.book.model.BookView;
import com.unmei21.core.exception.ConflictException;
import com.unmei21.service.book.model.BookModel;
import com.unmei21.service.book.model.CreateBookCommandModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface IBookService {
    Slice<BookModel> getBooks(
            Pageable page
    );

    BookModel createBook(CreateBookCommandModel command) throws ConflictException;

    Optional<BookModel> findBookById(String bookId);

    BookModel updateBook(BookModel model);
}
