package com.unmei21.repository;

import com.unmei21.repository.entity.BookEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IBookRepository extends JpaRepository<BookEntity, String> {
    @Query("SELECT b.id FROM BookEntity b ORDER BY b.createdAt DESC")
    Slice<String> findPageBookIds(Pageable pageable);

    @Query("SELECT b FROM BookEntity b JOIN FETCH b.author WHERE b.id IN :ids ORDER BY b.createdAt DESC")
    List<BookEntity> findBooksByIds(
            List<String> ids
    );

    boolean existsByIsbnCode(String isbnCode);

    Optional<BookEntity> findByIsbnCode(String isbnCode);
}
