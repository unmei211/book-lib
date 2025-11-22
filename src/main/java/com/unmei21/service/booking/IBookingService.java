package com.unmei21.service.booking;

import com.unmei21.service.booking.model.BookingDetailsModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface IBookingService {
    void BookABook(
            String bookId,
            String clientId
    );

    Slice<BookingDetailsModel> getPageOfBookingDetails(
            Pageable page
    );
}
