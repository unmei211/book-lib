package com.unmei21.controller.client;

import com.unmei21.controller.client.model.ClientView;
import com.unmei21.core.exception.NotFoundException;
import com.unmei21.core.view.PaginationView;
import com.unmei21.service.client.IClientService;
import com.unmei21.service.client.model.ClientModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/clients")
public class ClientController implements IClientController {
    private final IClientService clientService;

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    ClientController(
            IClientService clientService
    ) {
        this.clientService = clientService;
    }

    @GetMapping
    public String getPageOfClients(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            Model model
    ) {
        Slice<ClientModel> modelSlice = clientService.getClients(PageRequest.of(page, pageSize));

        List<ClientView> clientViews = modelSlice
                .stream()
                .map(clientModel -> ClientView.builder()
                        .id(clientModel.getId())
                        .lastName(clientModel.getLastName())
                        .firstName(clientModel.getFirstName())
                        .middleName(clientModel.getMiddleName())
                        .build()).collect(Collectors.toList());

        model.addAttribute("clients", clientViews);
        model.addAttribute("pagination", PaginationView.builder().page(page).pageSize(pageSize).hasNext(modelSlice.hasNext()).build());

        return "client_list";
    }

    @GetMapping("/add")
    public String getClientAddPage(Model model) {
        model.addAttribute("clientView", new ClientView());
        return "client_add";
    }


    @PostMapping
    public String addClient(@ModelAttribute ClientView view) {
        clientService.addClient(ClientModel.builder()
                .dateOfBirth(view.getDateOfBirth())
                .lastName(view.getLastName())
                .firstName(view.getFirstName())
                .middleName(view.getMiddleName())
                .build());
        return "redirect:/clients";
    }

    @GetMapping("/{clientId}/edit")
    public String getClientEditPage(@PathVariable String clientId, Model model) {
        ClientModel clientModel = clientService.findClientById(clientId).orElseThrow(() -> new NotFoundException("Client not found"));

        ClientView clientView = ClientView.builder()
                .id(clientModel.getId())
                .lastName(clientModel.getLastName())
                .firstName(clientModel.getFirstName())
                .middleName(clientModel.getMiddleName())
                .dateOfBirth(clientModel.getDateOfBirth())
                .build();
        model.addAttribute("clientView", clientView);

        return "client_edit";
    }

    @PostMapping("/{clientId}/edit")
    public String updateClient(@ModelAttribute ClientView clientModel, @PathVariable String clientId) {
        clientService.updateClient(ClientModel.
                builder()
                .id(clientId)
                .lastName(clientModel.getLastName())
                .dateOfBirth(clientModel.getDateOfBirth())
                .firstName(clientModel.getFirstName())
                .middleName(clientModel.getMiddleName())
                .build()
        );

        return "client_list";
    }
}
