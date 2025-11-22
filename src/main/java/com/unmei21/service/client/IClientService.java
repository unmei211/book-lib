package com.unmei21.service.client;

import com.unmei21.service.author.model.AuthorModel;
import com.unmei21.service.book.model.BookModel;
import com.unmei21.service.client.model.ClientModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface IClientService {
    Slice<ClientModel> getClients(
            Pageable page
    );

    ClientModel addClient(
            ClientModel model
    );

    Optional<ClientModel> findClientById(String clientId);

    ClientModel updateClient(ClientModel model);
}
