import javax.swing.*;
import java.util.List;

class ClientController implements ClientObserver {
    private IClientRepository clientRep;
    private ClientView view;

    public ClientController(IClientRepository clientRep) {
        this.clientRep = clientRep;
        clientRep.addObserver(this);
    }

    public void setView(ClientView view) {
        this.view = view; // Устанавливаем представление после его создания
        update(clientRep.getKNSortList(1, clientRep.getCount()));
    }

    public void onAddButtonClick() {
        new AddClientController(clientRep);
    }

    public void onDeleteButtonClick() {
        JTable clientTable = view.getClientTable();
        int selectedRow = clientTable.getSelectedRow();
        int clientId = (Integer) clientTable.getValueAt(selectedRow, 0);
        if (clientId != -1) {
            deleteClient(clientId);
        } else {
            view.showError("Выберите клиента для удаления.");
        }
    }

    public void onUpdateButtonClick() {
        JTable clientTable = view.getClientTable();
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                Object idObj = clientTable.getValueAt(selectedRow, 0);
                Object servicesObj = clientTable.getValueAt(selectedRow, 4);

                if (idObj == null || servicesObj == null) {
                    view.showError("Ошибка: Пустое значение в обязательном поле (ID или количество стрижек).");
                    return;
                }

                int clientId = Integer.parseInt(idObj.toString());
                int services = Integer.parseInt(servicesObj.toString());

                String surname = (String) clientTable.getValueAt(selectedRow, 1);
                String name = (String) clientTable.getValueAt(selectedRow, 2);
                String patronymic = (String) clientTable.getValueAt(selectedRow, 3);
                String phone = (String) clientTable.getValueAt(selectedRow, 5);
                String email = (String) clientTable.getValueAt(selectedRow, 6);
                String gender = (String) clientTable.getValueAt(selectedRow, 7);

                Client updatedClient = new Client(clientId, surname, name, patronymic, services, phone, email, gender);

                boolean success = clientRep.updateClient(clientId, updatedClient);
                if (!success) {
                    view.showError("Ошибка при обновлении клиента с ID: " + clientId);
                }

            } catch (NumberFormatException e) {
                view.showError("Ошибка при преобразовании данных. Пожалуйста, убедитесь, что все числовые поля заполнены корректно.");
                e.printStackTrace(); // Выводим стек вызовов для диагностики
            } catch (Exception e) {
                view.showError("Ошибка при обновлении клиента: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            view.showError("Выберите клиента для обновления.");
        }
    }


    public void onRefreshButtonClick() {
        update(clientRep.getKNSortList(1, clientRep.getCount()));
    }

    @Override
    public void update(List<Client> clients) {
        // Контроллер получает обновленные данные от модели и обновляет представление
        view.update(clients);
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
}
