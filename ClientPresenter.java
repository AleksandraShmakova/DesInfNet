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

    public String getClientListPage() throws IOException {
        List<Client> clients = clientRep.getKNSortList(currentPage, recordsPerPage);
        return view.displayClientList(clients, currentPage, recordsPerPage);
    }

    public void onAddButtonClick(Client newClient) throws Exception {
        addClient(newClient);
    }

    public void onDeleteButtonClick(int clientId) {
        try {
            deleteClient(clientId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный ID клиента: " + clientId);
        }
    }

    public void onUpdateButtonClick(Client updatedClient) {
        try {
            int clientId = updatedClient.getId();
            updateClient(clientId, updatedClient);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при обновлении клиента: " + e.getMessage());
        }
    }

    public String getClientDetailsPage(String query) {
        int clientId = parseClient(query).getId();
        Client client = clientRep.getById(clientId);
        return view.displayClientDetails(client);
    }

    public void addClient(Client newClient) throws Exception {
        if (isValid(newClient)) {
            clientRep.addClient(newClient);
            view.showSuccessMessage("Клиент успешно добавлен!");
        } else {
            view.showErrorMessage("Некорректные данные клиента");
        }
    }

    public void deleteClient(int clientId) {
        clientRep.deleteById(clientId);
        view.showSuccessMessage("Клиент успешно удален!");
    }

    public void updateClient(int clientId, Client updatedClient) throws Exception {
        if (isValid(updatedClient)) {
            clientRep.updateClient(clientId, updatedClient);
            view.showSuccessMessage("Данные клиента успешно обновлены!");
        } else {
            view.showErrorMessage("Некорректные данные клиента");
        }
    }

    private boolean isValid(Client client) {
        return Client.validateS(client.getSurname())
                && Client.validateS(client.getName())
                && Client.validateS(client.getPatronymic())
                && Client.validateI(client.getServices())
                && Client.validatePhone(client.getPhone())
                && Client.validateEmail(client.getEmail())
                && Client.validateS(client.getGender());
    }

    public Client parseClient(String requestBody) {
        Client client = new Client();
        String[] params = requestBody.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                switch (keyValue[0]) {

                    case "id":
                        client.setId(Integer.parseInt(keyValue[1]));
                        break;
                    case "surname":
                        client.setSurname(decodeURIComponent(keyValue[1]));
                        break;
                    case "name":
                        client.setName(decodeURIComponent(keyValue[1]));
                        break;
                    case "patronymic":
                        client.setPatronymic(decodeURIComponent(keyValue[1]));
                        break;
                    case "services":
                        client.setServices(Integer.parseInt(keyValue[1]));
                        break;
                    case "phone":
                        client.setPhone(decodeURIComponent(keyValue[1]));
                        break;
                    case "email":
                        client.setEmail(decodeURIComponent(keyValue[1]));
                        break;
                    case "gender":
                        client.setGender(decodeURIComponent(keyValue[1]));
                        break;
                }
            }
        }
        return client;
    }

    private String decodeURIComponent(String value) {
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}
