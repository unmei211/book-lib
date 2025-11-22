package com.unmei21.service.booking;

import com.unmei21.core.exception.NotFoundException;
import com.unmei21.core.uuid.GenID;
import com.unmei21.repository.IBookingRepository;
import com.unmei21.repository.entity.AuthorEntity;
import com.unmei21.repository.entity.BookEntity;
import com.unmei21.repository.entity.BookingEntity;
import com.unmei21.repository.entity.ClientEntity;
import com.unmei21.service.author.model.AuthorModel;
import com.unmei21.service.book.IBookService;
import com.unmei21.service.book.model.BookModel;
import com.unmei21.service.booking.model.BookingDetailsModel;
import com.unmei21.service.booking.model.BookingModel;
import com.unmei21.service.client.IClientService;
import com.unmei21.service.client.model.ClientModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService implements IBookingService {
    private final IBookService bookService;
    private final IClientService clientService;

    private final IBookingRepository bookingRepository;
    private final EntityManager em;

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    public BookingService(IBookService bookService, IClientService clientService, IBookingRepository bookingRepository, EntityManager em) {
        this.bookService = bookService;
        this.clientService = clientService;
        this.bookingRepository = bookingRepository;
        this.em = em;
    }

    @Override
    public void BookABook(String bookId, String clientId) {
        BookModel bookModel = bookService.findBookById(bookId).orElseThrow(() -> new NotFoundException("Can't find book with id:" + bookId));
        ClientModel clientModel = clientService.findClientById(clientId).orElseThrow(() -> new NotFoundException("Can't find client"));

        BookEntity bookRef = em.getReference(BookEntity.class, bookModel.getId());
        ClientEntity clientRef = em.getReference(ClientEntity.class, clientModel.getId());

        bookingRepository.save(new BookingEntity(
                GenID.uuid(),
                bookRef,
                clientRef,
                Instant.now(),
                Instant.now()
        ));
    }

    @Override
    public Slice<BookingDetailsModel> getPageOfBookingDetails(Pageable page) {
        Slice<String> idsSlice = bookingRepository.findPageBookingIds(page);
        log.debug("idsSlice:{} for pagination:{}", idsSlice, page);

        List<BookingEntity> entityList = bookingRepository.findBookingsByIds(idsSlice.toList());
        log.debug("entityList:{} for pagination:{}", entityList, page);

        List<BookingDetailsModel> bookingModels = entityList
                .stream()
                .map(bookingEntity -> {
                            ClientEntity clientEntity = bookingEntity.getClient();
                            BookEntity bookEntity = bookingEntity.getBook();
                            AuthorEntity authorEntity = bookEntity.getAuthor();

                            ClientModel clientModel = ClientModel.builder()
                                    .id(clientEntity.getId())
                                    .middleName(clientEntity.getMiddleName())
                                    .firstName(clientEntity.getFirstName())
                                    .lastName(clientEntity.getLastName())
                                    .dateOfBirth(clientEntity.getDateOfBirth())
                                    .build();

                            AuthorModel authorModel = AuthorModel.builder()
                                    .id(authorEntity.getId())
                                    .middleName(authorEntity.getMiddleName())
                                    .firstName(authorEntity.getFirstName())
                                    .lastName(authorEntity.getLastName())
                                    .build();

                            BookModel bookModel = BookModel.builder()
                                    .id(bookEntity.getId())
                                    .author(authorModel)
                                    .createdAt(bookEntity.getCreatedAt())
                                    .updatedAt(bookEntity.getUpdatedAt())
                                    .isbnCode(bookEntity.getIsbnCode())
                                    .name(bookEntity.getName())
                                    .build();

                            return BookingDetailsModel.builder()
                                    .book(bookModel)
                                    .client(clientModel)
                                    .booking(
                                            BookingModel.builder()
                                                    .bookId(bookModel.getId())
                                                    .clientId(clientModel.getId())
                                                    .id(bookingEntity.getId())
                                                    .createdAt(bookEntity.getCreatedAt())
                                                    .build()
                                    ).build();
                        }
                ).collect(Collectors.toList());
        return new SliceImpl<>(bookingModels, idsSlice.getPageable(), idsSlice.hasNext());
    }
}
