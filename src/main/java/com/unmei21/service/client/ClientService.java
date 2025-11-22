package com.unmei21.service.client;

import com.unmei21.core.exception.NotFoundException;
import com.unmei21.core.uuid.GenID;
import com.unmei21.repository.IAuthorRepository;
import com.unmei21.repository.IClientRepository;
import com.unmei21.repository.entity.AuthorEntity;
import com.unmei21.repository.entity.BookEntity;
import com.unmei21.repository.entity.ClientEntity;
import com.unmei21.service.author.model.AuthorModel;
import com.unmei21.service.book.model.BookModel;
import com.unmei21.service.client.model.ClientModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService implements IClientService {
    private final IClientRepository clientRepository;
    private static Logger log = LoggerFactory.getLogger(ClientService.class);

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Slice<ClientModel> getClients(Pageable page) {
        Slice<String> idsSlice = clientRepository.findPageClientIds(page);
        log.debug("idsSlice:{} for pagination:{}", idsSlice, page);

        List<ClientEntity> entityList = clientRepository.findClientsByIds(idsSlice.toList());
        log.debug("entityList:{} for pagination:{}", entityList, page);
        List<ClientModel> clientModels = entityList
                .stream()
                .map(clientEntity -> {
                            return ClientModel
                                    .builder()
                                    .middleName(clientEntity.getMiddleName())
                                    .firstName(clientEntity.getFirstName())
                                    .lastName(clientEntity.getLastName())
                                    .id(clientEntity.getId()).build();
                        }
                ).collect(Collectors.toList());

        return new SliceImpl<>(clientModels, idsSlice.getPageable(), idsSlice.hasNext());
    }

    @Override
    public ClientModel addClient(ClientModel model) {
        model.setId(GenID.uuid());

        clientRepository.save(ClientEntity.builder()
                .dateOfBirth(model.getDateOfBirth())
                .firstName(model.getFirstName())
                .lastName(model.getLastName())
                .middleName(model.getMiddleName())
                .id(model.getId())
                .build());

        return model;
    }

    @Override
    public Optional<ClientModel> findClientById(String clientId) {
        return clientRepository.findById(clientId).map(clientEntity -> ClientModel.builder()
                .dateOfBirth(clientEntity.getDateOfBirth())
                .firstName(clientEntity.getFirstName())
                .middleName(clientEntity.getMiddleName())
                .lastName(clientEntity.getLastName())
                .id(clientEntity.getId())
                .build());
    }

    @Override
    public ClientModel updateClient(ClientModel model) {
        ClientEntity entity = clientRepository.findById(model.getId()).orElseThrow(() -> new NotFoundException(("Not found client with id: " + model.getId())));

        entity.setDateOfBirth(model.getDateOfBirth());
        entity.setFirstName(model.getFirstName());
        entity.setLastName(model.getLastName());
        entity.setMiddleName(model.getMiddleName());

        clientRepository.save(entity);

        return model;
    }
}
