package com.unmei21.controller.booking;

import com.unmei21.controller.booking.model.BookABookRequestView;
import com.unmei21.service.book.IBookService;
import com.unmei21.service.booking.IBookingService;
import com.unmei21.service.client.IClientService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {
    private final IBookService bookService;
    private final IClientService clientService;
    private final IBookingService bookingService;

    public BookingController(IBookService bookService, IClientService clientService, IBookingService bookingService) {
        this.bookService = bookService;
        this.clientService = clientService;
        this.bookingService = bookingService;
    }

    @GetMapping
    String showBookingForm(@RequestParam(defaultValue = "0") int bookPage,
                           @RequestParam(defaultValue = "10") int bookSize,
                           @RequestParam(defaultValue = "0") int clientPage,
                           @RequestParam(defaultValue = "10") int clientSize,
                           Model model) {
        Pageable bookPageable = PageRequest.of(bookPage, bookSize);
        Pageable clientPageable = PageRequest.of(clientPage, clientSize);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("bookSize", bookSize);
        model.addAttribute("clientPage", clientPage);
        model.addAttribute("clientSize", clientSize);

        model.addAttribute("books", bookService.getBooks(bookPageable));
        model.addAttribute("clients", clientService.getClients(clientPageable));

        return "booking_book_a_book";
    }
    @PostMapping
    String bookABook(
            @ModelAttribute BookABookRequestView request
    ) {
        bookingService.BookABook(request.getBookId(), request.getClientId());

        return "redirect:/bookings";
    }
}
