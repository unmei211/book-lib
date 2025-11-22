package com.unmei21.service.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingModel {
    private String id;
    private String clientId;
    private String bookId;
    private Instant createdAt;
}
