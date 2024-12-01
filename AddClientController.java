class AddClientController {
    private IClientRepository clientRepository;
    private ClientFormView formView;
    private ClientObserver mainController;

    public AddClientController(IClientRepository clientRepository, ClientController mainController) {
        this.clientRepository = clientRepository;
        this.formView = new ClientFormView("Добавить клиента", "Добавить", this);
        this.mainController = mainController;

        formView.setVisible(true);
    }

    public void onConfirmButtonClick() {
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
    }

    public void onCancelButtonClick() {
        formView.dispose();
    }

    private boolean isValidClient(Client client) {

        if (client.getSurname().trim().isEmpty() || client.getName().trim().isEmpty() || client.getPatronymic().trim().isEmpty() || client.getEmail().trim().isEmpty() || !client.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") || client.getGender().trim().isEmpty()) {
            return false;
        }
        return true;
    }

}
