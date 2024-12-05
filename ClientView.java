import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
class ClientView extends JFrame {
    private JTable clientTable;
    private JTextField surnameField;
    private JTextField nameField;
    private JTextField patrField;
    private JTextField servicesField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField genderField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    //private JButton refreshButton;
    private JButton moreInfoButton;
    private JButton nextPageButton;
    private JButton prevPageButton;
    private ClientController controller;


    public ClientView(ClientController controller) {
        this.controller = controller;
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
        //refreshButton = new JButton("Обновить список");
        moreInfoButton = new JButton("Подробнее");
        nextPageButton = new JButton("Вперед");

        buttonPanel.add(prevPageButton);
        buttonPanel.add(addButton);
        //buttonPanel.add(refreshButton);
        buttonPanel.add(moreInfoButton);
        buttonPanel.add(nextPageButton);


        controlPanel.add(buttonPanel);

        add(controlPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> controller.onAddButtonClick());
        //refreshButton.addActionListener(e -> controller.onRefreshButtonClick());
        moreInfoButton.addActionListener(e -> controller.onMoreInfoButtonClick());
        nextPageButton.addActionListener(e -> controller.onNextPageClick());
        prevPageButton.addActionListener(e -> controller.onPrevPageClick());
        display();
    }
    // Кастомная модель для таблицы, с запретом редактирования определенных колонок
    private static class ClientTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0 && column != 1 && column != 2 && column != 3;
        }
    }

    public JTable getClientTable() {
        return clientTable;
    }

    // Обновление представления с данными клиентов
    public void update(List<Client> clients, int offset) {
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
    }

    public void updatePaginationControls(int currentPage, int totalPages) {
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < totalPages);
    }

    // Метод для отображения сообщений об ошибках
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public void openInfoWindow(Client client) {
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
        servicesField = new JTextField(String.valueOf(client.getServices()));
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
        deleteButton.addActionListener(e -> controller.onDeleteButtonClick(phone, infoWindow));
        updateButton.addActionListener(e -> getInfo(infoWindow));

        // Настраиваем и показываем окно
        infoWindow.setLocationRelativeTo(this);
        infoWindow.setVisible(true);
    }

    public void getInfo(JFrame infoWindow) {
        if(!controller.isValid(surnameField.getText(), nameField.getText(), patrField.getText(), Integer.parseInt(servicesField.getText()), phoneField.getText(), emailField.getText(), genderField.getText())) {
            showError("Некорректно заполнены данные!");
        }
        else {
            Client updatedClient = new Client(surnameField.getText(), nameField.getText(), patrField.getText(), Integer.parseInt(servicesField.getText()), phoneField.getText(), emailField.getText(), genderField.getText());
            controller.onUpdateButtonClick(updatedClient, infoWindow);
        }
    }

    public void display() {
        setVisible(true);
    }
}
