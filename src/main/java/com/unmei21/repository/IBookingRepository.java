package com.unmei21.repository;

import com.unmei21.repository.entity.BookEntity;
import com.unmei21.repository.entity.BookingEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBookingRepository extends JpaRepository<BookingEntity, String> {
    @Query("SELECT b.id FROM BookingEntity b ORDER BY b.createdAt DESC")
    Slice<String> findPageBookingIds(Pageable pageable);

    @Query(" SELECT b FROM BookingEntity b JOIN FETCH b.book book JOIN FETCH book.author JOIN FETCH b.client WHERE b.id IN :ids ORDER BY b.createdAt DESC")
    List<BookingEntity> findBookingsByIds(
            List<String> ids
    );
}
