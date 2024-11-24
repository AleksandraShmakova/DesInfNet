class AddClientController {
    private IClientRepository clientRepository;
    private ClientFormView formView;
    private ClientObserver mainController;

    public AddClientController(IClientRepository clientRepository, ClientObserver mainController) {
        this.clientRepository = clientRepository;
        this.formView = new ClientFormView("Добавить клиента", "Добавить");
        this.mainController = mainController;

        setupActions();
        formView.setVisible(true);
    }

    private void setupActions() {
        formView.setConfirmButtonListener(e -> {
            Client clientInput = formView.getClientInput();

            if (isValidClient(clientInput)) {
                try {
                    clientRepository.addClient(clientInput);
                    mainController.update(clientRepository.getKNSortList(1, clientRepository.getCount()));
                    formView.dispose();
                } catch (Exception ex) {
                    formView.showError("Ошибка при добавлении клиента: " + ex.getMessage());
                }
            } else {
                formView.showError("Некорректные данные клиента!");
            }
        });

        formView.setCancelButtonListener(e -> formView.dispose());
    }

    private boolean isValidClient(Client client) {

        if (client.getSurname().trim().isEmpty() || client.getName().trim().isEmpty() || client.getPatronymic().trim().isEmpty() || client.getEmail().trim().isEmpty() || !client.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") || client.getGender().trim().isEmpty()) {
            return false;
        }
        return true;
    }

}
