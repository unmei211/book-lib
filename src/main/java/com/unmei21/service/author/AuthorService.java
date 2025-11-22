package com.unmei21.service.author;

import com.unmei21.core.uuid.GenID;
import com.unmei21.repository.IAuthorRepository;
import com.unmei21.repository.entity.AuthorEntity;
import com.unmei21.service.author.model.AuthorModel;
import org.springframework.stereotype.Service;

@Service
public class AuthorService implements IAuthorService {
    private final IAuthorRepository authorRepository;

    public AuthorService(IAuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorModel createIfNotExists(AuthorModel model) {
        AuthorEntity author = authorRepository
                .findByPersonalInfo(model.getFirstName(), model.getLastName(), model.getMiddleName())
                .orElseGet(() -> authorRepository.save(new AuthorEntity(
                        GenID.uuid(),
                        model.getFirstName(),
                        model.getLastName(),
                        model.getMiddleName()
                )));
        return AuthorModel.builder()
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .middleName(author.getMiddleName())
                .id(author.getId())
                .build();
    }
}
