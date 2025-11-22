package com.unmei21.repository;

import com.unmei21.repository.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IAuthorRepository extends JpaRepository<AuthorEntity, String> {

    @Query("SELECT a FROM AuthorEntity a WHERE a.firstName = :firstName AND a.lastName = :lastName AND a.middleName = :middleName")
    Optional<AuthorEntity> findByPersonalInfo(
            String firstName, String lastName, String middleName
    );
}
