package com.unmei21.controller.client;

import com.unmei21.controller.client.model.ClientView;
import com.unmei21.service.client.model.ClientModel;
import org.springframework.ui.Model;

public interface IClientController {
    String getPageOfClients(Integer page, Integer pageSize, Model model);

    String getClientAddPage(Model model);

    String addClient(
            ClientView view
    );

    String getClientEditPage(String clientId, Model model);

    String updateClient(ClientView clientModel, String clientId);
}
