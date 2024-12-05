class AddClientController {
    private IClientRepository clientRepository;
    private ClientFormView formView;

    public AddClientController(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.formView = new ClientFormView("Добавить клиента", "Добавить", this);
        formView.setVisible(true);
    }

    public void onConfirmButtonClick() {
        Client clientInput = formView.getClientInput();
        try {
            clientRepository.addClient(clientInput);
            formView.dispose();
        } catch (Exception ex) {
            //formView.showError("Ошибка при добавлении клиента!");
        }
    }

    public void onCancelButtonClick() {
        formView.dispose();
    }

    public  boolean isValid(String surname, String name, String patr, int total_services, String phone, String email, String gender) {
        if(Client.validateS(surname) && Client.validateS(name) && Client.validateS(patr) && Client.validateI(total_services) && Client.validatePhone(phone) && Client.validateEmail(email) && Client.validateS(gender))
            return true;
        return false;
    }

}
