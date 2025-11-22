package com.unmei21.service.author;

import com.unmei21.service.author.model.AuthorModel;

public interface IAuthorService {
    AuthorModel createIfNotExists(
            AuthorModel model
    );
}
