package com.unmei21.service.book;

import com.unmei21.core.exception.ConflictException;
import com.unmei21.core.exception.NotFoundException;
import com.unmei21.core.uuid.GenID;
import com.unmei21.repository.IAuthorRepository;
import com.unmei21.repository.IBookRepository;
import com.unmei21.repository.entity.AuthorEntity;
import com.unmei21.repository.entity.BookEntity;
import com.unmei21.service.author.IAuthorService;
import com.unmei21.service.author.model.AuthorModel;
import com.unmei21.service.book.model.BookModel;
import com.unmei21.service.book.model.CreateBookCommandModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService implements IBookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);

    private final IBookRepository bookRepository;
    private final IAuthorService authorService;

    private final EntityManager entityManager;

    public BookService(IBookRepository bookRepository, IAuthorService authorService, EntityManager entityManager) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.entityManager = entityManager;
    }

    @Override
    public BookModel createBook(CreateBookCommandModel command) {
        if (bookRepository.existsByIsbnCode(command.getIsbnCode())) {
            throw new ConflictException("Book with isbn code already exists");
        }

        AuthorModel author = authorService.createIfNotExists(AuthorModel.builder()
                .firstName(command.getAuthorFirstName())
                .lastName(command.getAuthorLastName())
                .middleName(command.getAuthorMiddleName())
                .build());
        Instant now = Instant.now();

        AuthorEntity authorProxy = entityManager.getReference(AuthorEntity.class, author.getId());

        BookModel book = BookModel.builder()
                .createdAt(now)
                .updatedAt(now)
                .author(author)
                .isbnCode(command.getIsbnCode())
                .name(command.getBookName())
                .id(GenID.uuid())
                .build();

        bookRepository.save(
                BookEntity.builder()
                        .createdAt(book.getCreatedAt())
                        .updatedAt(book.getUpdatedAt())
                        .name(book.getName())
                        .id(book.getId())
                        .isbnCode(book.getIsbnCode())
                        .author(authorProxy)
                        .build()
        );

        return book;
    }

    @Override
    public Slice<BookModel> getBooks(Pageable page) {
        Slice<String> idsSlice = bookRepository.findPageBookIds(page);
        log.debug("idsSlice:{} for pagination:{}", idsSlice, page);

        List<BookEntity> entityList = bookRepository.findBooksByIds(idsSlice.toList());
        log.debug("entityList:{} for pagination:{}", entityList, page);
        List<BookModel> bookModels = entityList
                .stream()
                .map(bookEntity -> {
                            AuthorEntity authorEntity = bookEntity.getAuthor();
                            return BookModel
                                    .builder()
                                    .name(bookEntity.getName())
                                    .author(
                                            AuthorModel.builder()
                                                    .middleName(authorEntity.getMiddleName())
                                                    .firstName(authorEntity.getFirstName())
                                                    .lastName(authorEntity.getLastName())
                                                    .id(authorEntity.getId()).build()
                                    )
                                    .id(bookEntity.getId())
                                    .createdAt(bookEntity.getCreatedAt())
                                    .updatedAt(bookEntity.getUpdatedAt())
                                    .build();
                        }
                ).collect(Collectors.toList());

        return new SliceImpl<>(bookModels, idsSlice.getPageable(), idsSlice.hasNext());
    }

    @Override
    public Optional<BookModel> findBookById(String bookId) {
        Optional<BookEntity> entity = bookRepository.findById(bookId);

        Optional<BookModel> model = entity.map(nonNullEntity -> {
            AuthorEntity authorEntity = nonNullEntity.getAuthor();
            return BookModel.builder()
                    .id(nonNullEntity.getId())
                    .name(nonNullEntity.getName())
                    .isbnCode(nonNullEntity.getIsbnCode())
                    .updatedAt(nonNullEntity.getUpdatedAt())
                    .createdAt(nonNullEntity.getCreatedAt())
                    .author(
                            AuthorModel.builder()
                                    .id(authorEntity.getId())
                                    .middleName(authorEntity.getMiddleName())
                                    .firstName(authorEntity.getFirstName())
                                    .lastName(authorEntity.getLastName())
                                    .build()
                    )
                    .build();
        });
        return model;
    }

    @Override
    public BookModel updateBook(BookModel model) {
        Optional<BookEntity> sameCodeBook = bookRepository.findByIsbnCode(model.getIsbnCode());

        sameCodeBook.ifPresent(bookEntity -> {
            if (!bookEntity.getId().equals(model.getId())) {
                throw new ConflictException("Book with isbn code already exists in another book");
            }
        });

        BookEntity existsBook = bookRepository.findById(model.getId()).orElseThrow(() -> new NotFoundException("Book with id not found"));

        AuthorModel author = authorService.createIfNotExists(AuthorModel.builder()
                .firstName(model.getAuthor().getFirstName())
                .lastName(model.getAuthor().getLastName())
                .middleName(model.getAuthor().getMiddleName())
                .build());

        model.setAuthor(author);
        model.setIsbnCode(existsBook.getIsbnCode());
        model.setName(model.getName());
        model.setUpdatedAt(Instant.now());
        model.setCreatedAt(existsBook.getCreatedAt());

        existsBook.setName(model.getName());
        existsBook.setIsbnCode(model.getIsbnCode());
        existsBook.setUpdatedAt(model.getUpdatedAt());
        existsBook.setCreatedAt(existsBook.getCreatedAt());

        AuthorEntity authorProxy = entityManager.getReference(AuthorEntity.class, author.getId());

        existsBook.setAuthor(authorProxy);

        bookRepository.save(existsBook);

        return model;
    }
}
