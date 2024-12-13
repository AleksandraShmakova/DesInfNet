import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

class ClientView extends JFrame implements IClientView {
    private JTable clientTable;
    private JTextField surnameField;
    private JTextField nameField;
    private JTextField patrField;
    private JTextField servicesField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField genderField;
    private JButton addButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton moreInfoButton;
    private JButton nextPageButton;
    private JButton prevPageButton;
    private ClientPresenter presenter;


    public ClientView() {
        // Настройка окна
        setTitle("Client Management");
        setSize(600, 330);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Создание таблицы для отображения клиентов
        clientTable = new JTable(new ClientTableModel());
        JScrollPane tableScrollPane = new JScrollPane(clientTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Создание панели управления
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));

        // Создание кнопок управления
        JPanel buttonPanel = new JPanel(new FlowLayout());
        prevPageButton = new JButton("Назад");
        addButton = new JButton("Добавить");
        moreInfoButton = new JButton("Подробнее");
        nextPageButton = new JButton("Вперед");

        buttonPanel.add(prevPageButton);
        buttonPanel.add(addButton);
        buttonPanel.add(moreInfoButton);
        buttonPanel.add(nextPageButton);


        controlPanel.add(buttonPanel);

        add(controlPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> generateAddClientPage());
        moreInfoButton.addActionListener(e -> onMoreInfoButtonClick());
        display();
    }

    @Override
    public String update(List<Client> clients, int currentPage, int recordsPerPage, int totalPages) {
        //int totalPages = (int) Math.ceil((double) clients.size() / recordsPerPage);
        int offset = (currentPage - 1) * recordsPerPage;
        String[] columnNames = {"№", "Фамилия", "Имя", "Телефон"};
        ClientTableModel model = (ClientTableModel) clientTable.getModel();
        model.setColumnIdentifiers(columnNames);
        model.setRowCount(0);

        for(int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            Object[] row = {
                    offset + i + 1,
                    client.getSurname(),
                    client.getName(),
                    client.getPhone()
            };
            model.addRow(row);
        }

        nextPageButton.addActionListener(e -> {
            try {
                onNextPageClick(currentPage, totalPages);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        prevPageButton.addActionListener(e -> {
            try {
                onPrevPageClick(currentPage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < totalPages);
        return null;
    }

    public void onNextPageClick(int currentPage, int totalPages) throws IOException {
        if (currentPage < totalPages) {
            currentPage++;
            presenter.setCurrentPage(currentPage);
            presenter.update();
        }
    }

    public void onPrevPageClick(int currentPage) throws IOException {
        if (currentPage > 1) {
            currentPage--;
            presenter.setCurrentPage(currentPage);
            presenter.update();
        }
    }

    private static class ClientTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0 && column != 1 && column != 2 && column != 3;
        }
    }

    @Override
    public void setPresenter(ClientPresenter presenter) {
        this.presenter = presenter;
        try {
            presenter.update();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onMoreInfoButtonClick() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                String phone = (String) clientTable.getValueAt(selectedRow, 3);
                int clientId = presenter.getClientId(phone);
                presenter.getClientDetailsPage(clientId);
            } catch (Exception e) {
                showErrorMessage("Ошибка при нахождении клиента: " + e.getMessage());
            }
        } else {
            showErrorMessage("Выберите клиента для просмотра информации.");
        }
    }

    public void onAddButtonClick(JFrame infoWindow) throws Exception {
        Client newClient = getInfo(infoWindow);
        presenter.onAddButtonClick(newClient);
        presenter.update();
        infoWindow.dispose();
    }

    @Override
    public String generateAddClientPage() {
        JFrame infoWindow = new JFrame("Добавление клиента");
        infoWindow.setSize(400, 300);
        infoWindow.setLayout(new GridLayout(8, 2));

        // Добавляем метки с информацией
        infoWindow.add(new JLabel("Фамилия: "));
        surnameField = new JTextField();
        infoWindow.add(surnameField);
        infoWindow.add(new JLabel("Имя: "));
        nameField = new JTextField();
        infoWindow.add(nameField);
        infoWindow.add(new JLabel("Отчество: "));
        patrField = new JTextField();
        infoWindow.add(patrField);
        infoWindow.add(new JLabel("Количество стрижек: "));
        servicesField = new JTextField();
        infoWindow.add(servicesField);
        infoWindow.add(new JLabel("Телефон: "));
        phoneField = new JTextField();
        infoWindow.add(phoneField);
        infoWindow.add(new JLabel("Email: "));
        emailField = new JTextField();
        infoWindow.add(emailField);
        infoWindow.add(new JLabel("Пол: "));
        genderField = new JTextField();
        infoWindow.add(genderField);

        addButton = new JButton("Добавить");
        cancelButton = new JButton("Отмена");
        infoWindow.add(addButton);
        infoWindow.add(cancelButton);

        addButton.addActionListener(e -> {
            try {
                onAddButtonClick(infoWindow);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        cancelButton.addActionListener(e -> infoWindow.dispose());

        // Настраиваем и показываем окно
        infoWindow.setLocationRelativeTo(this);
        infoWindow.setVisible(true);
        return null;
    }

    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    @Override
    public String displayClientDetails(Client client) {
        JFrame infoWindow = new JFrame("Информация о клиенте");
        infoWindow.setSize(400, 300);
        infoWindow.setLayout(new GridLayout(8, 2));

        // Добавляем метки с информацией
        infoWindow.add(new JLabel("Фамилия: "));
        surnameField = new JTextField(client.getSurname());
        infoWindow.add(surnameField);
        infoWindow.add(new JLabel("Имя: "));
        nameField = new JTextField(client.getName());
        infoWindow.add(nameField);
        infoWindow.add(new JLabel("Отчество: "));
        patrField = new JTextField(client.getPatronymic());
        infoWindow.add(patrField);
        infoWindow.add(new JLabel("Количество стрижек: "));
        servicesField = new JTextField(String.valueOf(client.getTotal_services()));
        infoWindow.add(servicesField);
        infoWindow.add(new JLabel("Телефон: "));
        phoneField = new JTextField(client.getPhone());
        infoWindow.add(phoneField);
        phoneField.setEditable(false);
        infoWindow.add(new JLabel("Email: "));
        emailField = new JTextField(client.getEmail());
        infoWindow.add(emailField);
        infoWindow.add(new JLabel("Пол: "));
        genderField = new JTextField(client.getGender());
        infoWindow.add(genderField);

        deleteButton = new JButton("Удалить");
        updateButton = new JButton("Обновить");
        infoWindow.add(deleteButton);
        infoWindow.add(updateButton);

        String phone = phoneField.getText();
        deleteButton.addActionListener(e -> onDeleteButtonClick(phone, infoWindow));
        updateButton.addActionListener(e -> onUpdateButtonClick(infoWindow));

        // Настраиваем и показываем окно
        infoWindow.setLocationRelativeTo(this);
        infoWindow.setVisible(true);
        return null;
    }

    public void onDeleteButtonClick(String phone, JFrame infoWindow) {
        int clientId = presenter.getClientId(phone);
        presenter.onDeleteButtonClick(clientId);
        infoWindow.dispose();
    }

    public void onUpdateButtonClick(JFrame infoWindow) {
        Client updatedClient = getInfo(infoWindow);
        int clientId = presenter.getClientId(updatedClient.getPhone());
        updatedClient.setId(clientId);
        presenter.onUpdateButtonClick(updatedClient);
        infoWindow.dispose();
    }

    public Client getInfo(JFrame infoWindow) {
        if(!presenter.isValid(surnameField.getText(), nameField.getText(), patrField.getText(), Integer.parseInt(servicesField.getText()), phoneField.getText(), emailField.getText(), genderField.getText())) {
            showErrorMessage("Некорректно заполнены данные!");
            return null;
        }
        else {
            Client client = new Client(surnameField.getText(), nameField.getText(), patrField.getText(), Integer.parseInt(servicesField.getText()), phoneField.getText(), emailField.getText(), genderField.getText());
            return client;
        }
    }

    public void display() {
        setVisible(true);
    }
}
