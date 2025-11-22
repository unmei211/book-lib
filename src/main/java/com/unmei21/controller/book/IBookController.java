package com.unmei21.controller.book;

import com.unmei21.controller.book.model.AddBookView;
import com.unmei21.controller.book.model.BookView;
import com.unmei21.service.book.model.CreateBookCommandModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

public interface IBookController {
    String getPageOfBooks(Integer page, Integer pageSize, Model model);

    String getBookAddPage(Model model);

    String addBook(
            AddBookView view,
            BindingResult bindingResult
    );

    String getBookEditPage(String bookId, Model model);

    String updateBook(BookView view, BindingResult bindingResult, String bookId);
}
