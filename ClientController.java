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
        refreshClientList();
    }

    public void onAddButtonClick() {
        new AddClientController(clientRep, this);
    }

    public void onDeleteButtonClick() {
        int selectedRow = view.getClientTable().getSelectedRow();
        int clientId = (Integer) view.getClientTable().getValueAt(selectedRow, 0);
        if (clientId != -1) {
            deleteClient(clientId);
            refreshClientList();
        } else {
            view.showError("Выберите клиента для удаления.");
        }
    }

    public void onUpdateButtonClick() {
        int selectedRow = view.getClientTable().getSelectedRow();
        if (selectedRow != -1) {
            try {
                // Получаем данные из ячеек и проверяем их типы
                Object idObj = view.getClientTable().getValueAt(selectedRow, 0);
                Object servicesObj = view.getClientTable().getValueAt(selectedRow, 4);

                // Проверяем и выводим информацию для диагностики
                if (idObj == null || servicesObj == null) {
                    view.showError("Ошибка: Пустое значение в обязательном поле (ID или количество стрижек).");
                    return;
                }

                // Преобразуем значения в нужный формат
                int clientId = Integer.parseInt(idObj.toString());
                int services = Integer.parseInt(servicesObj.toString());

                String surname = (String) view.getClientTable().getValueAt(selectedRow, 1);
                String name = (String) view.getClientTable().getValueAt(selectedRow, 2);
                String patronymic = (String) view.getClientTable().getValueAt(selectedRow, 3);
                String phone = (String) view.getClientTable().getValueAt(selectedRow, 5);
                String email = (String) view.getClientTable().getValueAt(selectedRow, 6);
                String gender = (String) view.getClientTable().getValueAt(selectedRow, 7);

                boolean success = clientRep.updateClient(clientId, updatedClient);
                if (!success) {
                    view.showError("Ошибка при обновлении клиента с ID: " + clientId);
                }

            } catch (NumberFormatException e) {
                view.showError("Ошибка при преобразовании данных. Пожалуйста, убедитесь, что все числовые поля заполнены корректно.");
                e.printStackTrace();
            } catch (Exception e) {
                view.showError("Ошибка при обновлении клиента: " + e.getMessage());
                e.printStackTrace();
            }

            refreshClientList();
        } else {
            view.showError("Выберите клиента для обновления.");
        }
    }


    public void onRefreshButtonClick() {
        refreshClientList();
    }

    @Override
    public void update(List<Client> clients) {
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
    public void refreshClientList() {
        List<Client> clients = clientRep.getKNSortList(1, clientRep.getCount());
        view.update(clients);
    }
}
