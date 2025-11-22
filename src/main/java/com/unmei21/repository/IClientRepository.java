package com.unmei21.repository;

import com.unmei21.repository.entity.BookEntity;
import com.unmei21.repository.entity.ClientEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IClientRepository extends JpaRepository<ClientEntity, String> {
    @Query("SELECT b.id FROM ClientEntity b ORDER BY b.dateOfBirth DESC")
    Slice<String> findPageClientIds(Pageable pageable);

    @Query("SELECT b FROM ClientEntity b WHERE b.id IN :ids ORDER BY b.dateOfBirth DESC")
    List<ClientEntity> findClientsByIds(
            List<String> ids
    );

}
