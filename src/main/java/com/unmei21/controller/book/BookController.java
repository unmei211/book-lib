package com.unmei21.controller.book;

import com.unmei21.controller.author.model.AuthorSimpleView;
import com.unmei21.controller.author.model.AuthorView;
import com.unmei21.controller.book.model.AddBookView;
import com.unmei21.controller.book.model.BookSimpleView;
import com.unmei21.controller.book.model.BookView;
import com.unmei21.core.exception.ConflictException;
import com.unmei21.core.view.PaginationView;
import com.unmei21.service.author.model.AuthorModel;
import com.unmei21.service.book.IBookService;
import com.unmei21.service.book.model.BookModel;
import com.unmei21.service.book.model.CreateBookCommandModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/books")
public class BookController implements IBookController {
    private final IBookService bookService;

    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    BookController(
            IBookService bookService
    ) {
        this.bookService = bookService;
    }

    @GetMapping
    public String getPageOfBooks(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        Slice<BookModel> modelSlice = bookService.getBooks(PageRequest.of(page, pageSize));

        List<BookSimpleView> bookViews = modelSlice
                .stream()
                .map(bookModel -> {
                    AuthorModel authorModel = bookModel.getAuthor();
                    return BookSimpleView.builder()
                            .id(bookModel.getId())
                            .author(
                                    AuthorSimpleView.builder()
                                            .id(authorModel.getId())
                                            .fullName(
                                                    Stream.of(
                                                                    authorModel.getLastName(),
                                                                    authorModel.getFirstName(),
                                                                    authorModel.getMiddleName()
                                                            )
                                                            .filter(s -> s != null && !s.trim().isEmpty())
                                                            .collect(Collectors.joining(" "))
                                            )
                                            .build()
                            )
                            .name(bookModel.getName())
                            .isbnCode(bookModel.getIsbnCode())
                            .build();
                }).collect(Collectors.toList());

        model.addAttribute("bookViews", bookViews);
        model.addAttribute("pagination", PaginationView.builder().page(page).pageSize(pageSize).hasNext(modelSlice.hasNext()).build());

        return "book_list";
    }

    @GetMapping("/add")
    public String getBookAddPage(Model model) {
        model.addAttribute("bookView", new AddBookView());
        return "book_add";
    }

    @PostMapping
    public String addBook(
            @ModelAttribute("bookView") AddBookView view,
            BindingResult bindingResult
    ) {
        try {
            bookService.createBook(CreateBookCommandModel.builder()
                    .bookName(view.getBookName())
                    .authorFirstName(view.getAuthorFirstName())
                    .authorMiddleName(view.getAuthorMiddleName())
                    .authorLastName(view.getAuthorLastName())
                    .isbnCode(view.getIsbnCode())
                    .build());
        } catch (ConflictException e) {
            logger.debug("Attempt to add an existing book");
            return "error";
        }
        return "redirect:/books";
    }


    @GetMapping("/{bookId}/edit")
    public String getBookEditPage(@PathVariable String bookId, Model model) {
        Optional<BookModel> bookModel = bookService.findBookById(bookId);
        if (!bookModel.isPresent()) {
            return "error";
        }

        AuthorModel authorModel = bookModel.get().getAuthor();
        model.addAttribute("bookView", BookView
                .builder()
                .updatedAt(bookModel.get().getUpdatedAt())
                .createdAt(bookModel.get().getCreatedAt())
                .name(bookModel.get().getName())
                .id(bookModel.get().getId())
                .isbnCode(bookModel.get().getIsbnCode())
                .author(AuthorView.builder()
                        .firstName(authorModel.getFirstName())
                        .lastName(authorModel.getLastName())
                        .middleName(authorModel.getMiddleName())
                        .id(authorModel.getId())
                        .build())
                .build());

        return "book_edit";
    }

    @PostMapping("/{bookId}/edit")
    public String updateBook(@ModelAttribute BookView view, BindingResult bindingResult, @PathVariable String bookId) {
        if (!view.getId().equals(bookId)) {
            return "error_bad_request";
        }
        AuthorView authorView = view.getAuthor();
        bookService.updateBook(
                BookModel.builder()
                        .id(bookId)
                        .isbnCode(view.getIsbnCode())
                        .name(view.getName())
                        .author(
                                AuthorModel.builder()
                                        .firstName(authorView.getFirstName())
                                        .lastName(authorView.getLastName())
                                        .middleName(authorView.getMiddleName())
                                        .build()
                        )
                        .build()
        );

        return "redirect:/books";
    }


}
