package com.unmei21.service.booking.model;

import com.unmei21.service.book.model.BookModel;
import com.unmei21.service.client.model.ClientModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDetailsModel {
    private ClientModel client;
    private BookModel book;
    private BookingModel booking;
}
