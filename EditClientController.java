class EditClientController {
    private IClientRepository clientRepository;
    private ClientFormView formView;
    private ClientObserver mainController;
    private Client existingClient;

    public EditClientController(IClientRepository clientRepository, ClientObserver mainController, Client client) {
        this.clientRepository = clientRepository;
        this.formView = new ClientFormView("Редактировать клиента", "Сохранить");
        this.mainController = mainController;
        this.existingClient = client;

        formView.setClientData(client); // Заполняем поля данными клиента
        formView.setPhoneFieldEditable(false); // Поле телефона становится только для чтения
        setupActions();
        formView.setVisible(true);
    }

    private void setupActions() {
        formView.setConfirmButtonListener(e -> {
            Client clientInput = formView.getClientInput();

            if (isValidClient(clientInput)) {
                try {
                    clientInput.setId(existingClient.getId());
                    clientInput.setPhone(existingClient.getPhone()); // Устанавливаем неизменяемый телефон
                    boolean success = clientRepository.updateClient(existingClient.getId(), clientInput);
                    if (success) {
                        System.out.println(success);
                        mainController.update(clientRepository.getKNSortList(1, clientRepository.getCount()));
                        formView.dispose();
                    } else {
                        formView.showError("Клиент не найден!");
                    }
                } catch (Exception ex) {
                    formView.showError("Ошибка при сохранении клиента: " + ex.getMessage());
                }
            } else {
                formView.showError("Заполните все обязательные поля!");
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
