import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

class ClientPresenter {
    private final IClientView view;
    private final IClientRepository clientRep;
    private int currentPage = 1;
    private final int recordsPerPage = 10;

    public ClientPresenter(IClientView view, IClientRepository clientRep) {
        this.view = view;
        this.clientRep = clientRep;
    }

    public void setCurrentPage(int page) {
        this.currentPage = page;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public String update() throws IOException {
        List<Client> clients = clientRep.getKNSortList(currentPage, recordsPerPage);
        int totalPages = (int) Math.ceil((double) clientRep.getCount() / recordsPerPage);
        return view.update(clients, currentPage, recordsPerPage, totalPages);
    }

    public void onAddButtonClick(Client newClient) throws Exception {
        addClient(newClient);
    }

    public void onDeleteButtonClick(int clientId) {
        try {
            deleteClient(clientId);
            update();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный ID клиента: " + clientId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUpdateButtonClick(Client updatedClient) {
        try {
            int clientId = updatedClient.getId();
            updateClient(clientId, updatedClient);
            update();
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при обновлении клиента: " + e.getMessage());
        }
    }

    public String getClientDetailsPage(int clientId) {
        Client client = clientRep.getById(clientId);
        return view.displayClientDetails(client);
    }

    public void addClient(Client newClient) throws Exception {
        if (isValid(newClient.getSurname(), newClient.getName(), newClient.getPatronymic(), newClient.getTotal_services(), newClient.getPhone(), newClient.getEmail(), newClient.getGender())) {
            clientRep.addClient(newClient);
        } else {
            view.showErrorMessage("Некорректные данные клиента");
        }
    }

    public void deleteClient(int clientId) {
        clientRep.deleteById(clientId);
    }

    public void updateClient(int clientId, Client updatedClient) throws Exception {
        if (isValid(updatedClient.getSurname(), updatedClient.getName(), updatedClient.getPatronymic(), updatedClient.getTotal_services(), updatedClient.getPhone(), updatedClient.getEmail(), updatedClient.getGender())) {
            clientRep.updateClient(clientId, updatedClient);
        } else {
            view.showErrorMessage("Некорректные данные клиента");
        }
    }

    public boolean isValid(String surname, String name, String patronymic, int total_services, String phone, String email, String gender) {
        return Client.validateS(surname)
                && Client.validateS(name)
                && Client.validateS(patronymic)
                && Client.validateI(total_services)
                && Client.validatePhone(phone)
                && Client.validateEmail(email)
                && Client.validateS(gender);
    }

    public int getClientId(String phone) {
        try {
            List<Client> clients = clientRep.getKNSortList(1, clientRep.getCount());
            for (Client client : clients) {
                if (client.getPhone().equals(phone)) {
                    int clientId = client.getId();
                    return clientId;
                }
            }
        } catch (Exception e) {
            view.showErrorMessage("Ошибка при нахождении id: " + e.getMessage());
        }
        return -1;
    }

}
