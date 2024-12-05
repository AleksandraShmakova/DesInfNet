import javax.swing.*;
import java.util.List;

class ClientController implements ClientObserver {
    private IClientRepository clientRep;
    private ClientView view;
    private int currentPage = 1;
    private final int recordsPerPage = 10;

    public ClientController(IClientRepository clientRep) {
        this.clientRep = clientRep;
        clientRep.addObserver(this);
    }

    public void setView(ClientView view) {
        this.view = view; // Устанавливаем представление после его создания
        update();
    }

    public void onAddButtonClick() {
        new AddClientController(clientRep);
    }

    public void onDeleteButtonClick(String phone, JFrame infoWindow) {
        try {
            List<Client> clients = clientRep.getKNSortList(1, clientRep.getCount());
            for (Client client : clients) {
                if (client.getPhone().equals(phone)) {
                    int clientId = client.getId();
                    deleteClient(clientId);
                    infoWindow.dispose();
                }
            }
        } catch (Exception e) {
            view.showError("Ошибка при удалении клиента: " + e.getMessage());
        }
    }

    public void onMoreInfoButtonClick() {
        JTable clientTable = view.getClientTable();
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                String phone = (String) clientTable.getValueAt(selectedRow, 3);
                List<Client> clients = clientRep.getKNSortList(1, clientRep.getCount());
                for (Client client : clients) {
                    if (client.getPhone().equals(phone)) {
                        view.openInfoWindow(client);
                    }
                }
            } catch (Exception e) {
                view.showError("Ошибка при нахождении клиента: " + e.getMessage());
            }
        } else {
            view.showError("Выберите клиента для просмотра информации.");
        }
    }

    public void onUpdateButtonClick(Client updatedClient, JFrame infoWindow) {
        try {
            List<Client> clients = clientRep.getKNSortList(1, clientRep.getCount());
            for (Client client : clients) {
                if (client.getPhone().equals(updatedClient.getPhone())) {
                    int clientId = client.getId();
                    updateClient(clientId, updatedClient);
                    infoWindow.dispose();
                }
            }
        } catch (Exception e) {
            view.showError("Ошибка при замене клиента: " + e.getMessage());
        }
    }

    public void onRefreshButtonClick() {
        update();
    }

    public void onNextPageClick() {
        int totalRecords = clientRep.getCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        if (currentPage < totalPages) {
            currentPage++;
            update();
        }
    }

    public void onPrevPageClick() {
        if (currentPage > 1) {
            currentPage--;
            update();
        }
    }

    // Реализация метода обновления для наблюдателя (контроллера)
    @Override
    public void update() {
        int totalRecords = clientRep.getCount();
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        int offset = (currentPage - 1) * recordsPerPage;
        List<Client> clientsToDisplay = clientRep.getKNSortList(currentPage, recordsPerPage);
        view.update(clientsToDisplay, offset);
        view.updatePaginationControls(currentPage, totalPages);
    }


    public void deleteClient(int clientId) {
        clientRep.deleteById(clientId);
    }

    public void updateClient(int clientId, Client newClient) {
        try {
            boolean success = clientRep.updateClient(clientId, newClient);
            if (!success) {
                view.showError("Ошибка при обновлении клиента: клиент не найден.");
            }
        } catch (Exception e) {
            view.showError("Ошибка при обновлении клиента: " + e.getMessage());
        }
    }

    public  boolean isValid(String surname, String name, String patr, int total_services, String phone, String email, String gender) {
        if(Client.validateS(surname) && Client.validateS(name) && Client.validateS(patr) && Client.validateI(total_services) && Client.validatePhone(phone) && Client.validateEmail(email) && Client.validateS(gender))
            return true;
        return false;
    }
}
