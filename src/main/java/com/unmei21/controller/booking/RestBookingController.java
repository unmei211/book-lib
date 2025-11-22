package com.unmei21.controller.booking;

import com.unmei21.controller.author.model.AuthorView;
import com.unmei21.controller.booking.model.BookABookRequestView;
import com.unmei21.controller.booking.model.BookingDetailsView;
import com.unmei21.service.book.IBookService;
import com.unmei21.service.booking.IBookingService;
import com.unmei21.service.client.IClientService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bookings")
public class RestBookingController {
    private final IBookingService bookingService;

    public RestBookingController(IBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/details")
    ResponseEntity<List<BookingDetailsView>> getPageOfBookingDetails(
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        return ResponseEntity
                .ok(bookingService
                        .getPageOfBookingDetails(PageRequest.of(page, pageSize))
                        .stream()
                        .map(model ->
                                BookingDetailsView.builder()
                                        .bookedAt(model.getBooking().getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate())
                                        .name(model.getBook().getName())
                                        .dateOfBirth(model.getClient().getDateOfBirth())
                                        .firstName(model.getClient().getFirstName())
                                        .middleName(model.getClient().getMiddleName())
                                        .lastName(model.getClient().getLastName())
                                        .isbnCode(model.getBook().getIsbnCode())
                                        .author(AuthorView.builder()
                                                .id(model.getBook().getAuthor().getId())
                                                .firstName(model.getBook().getAuthor().getFirstName())
                                                .lastName(model.getBook().getAuthor().getLastName())
                                                .middleName(model.getBook().getAuthor().getMiddleName())
                                                .build())
                                        .build()
                        )
                        .collect(Collectors.toList())
                );
    }
}
