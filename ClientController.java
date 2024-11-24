class ClientController implements ClientObserver {
    private IClientRepository clientRep;
    private ClientView view;

    public ClientController(IClientRepository clientRep, ClientView view) {
        this.clientRep = clientRep;
        this.view = view;

        // Подписываем контроллер на изменения модели
        clientRep.addObserver(this);
        refreshClientList();
        setupActions();
    }

    public void setupActions() {
        view.setAddButtonListener(e -> {
            Client newClient = view.getClientInput();
            addClient(newClient);
        });

        view.setDeleteButtonListener(e -> {
            int clientId = view.getSelectedClientId();
            if (clientId != -1) {
                deleteClient(clientId);
            }
        });

        view.setUpdateButtonListener(e -> {
            int clientId = view.getSelectedClientId();
            if (clientId != -1) {
                Client updatedClient = view.getClientInput();
                replaceClient(clientId, updatedClient);
            }
        });

        view.setRefreshButtonListener(e -> {
            refreshClientList();
        });
    }

    @Override
    public void update(List<Client> clients) {
        // Контроллер получает обновленные данные от модели и обновляет представление
        view.update(clients);
    }

    public void addClient(Client client) {
        try {
            clientRep.addClient(client);
        } catch (Exception e) {
            view.showError("Ошибка при добавлении клиента: " + e.getMessage());
        }
    }

    public void deleteClient(int clientId) {
        clientRep.deleteById(clientId);
    }

    public void replaceClient(int clientId, Client newClient) {
        try {
            boolean success = clientRep.replaceById(clientId, newClient);
            if (!success) {
                view.showError("Ошибка при замене клиента: клиент не найден.");
            }
        } catch (Exception e) {
            view.showError("Ошибка при замене клиента: " + e.getMessage());
        }
    }
    public void refreshClientList() {
        List<Client> clients = clientRep.getKNSortList(1, clientRep.getCount());
        view.update(clients);
    }
}
