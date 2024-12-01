import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
class ClientView extends JFrame {
    private JTable clientTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton refreshButton;
    private ClientController controller;


    public ClientView(ClientController controller) {
        this.controller = controller;
        setTitle("Client Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        clientTable = new JTable(new ClientTableModel());
        JScrollPane tableScrollPane = new JScrollPane(clientTable);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Добавить");
        deleteButton = new JButton("Удалить");
        updateButton = new JButton("Обновить");
        refreshButton = new JButton("Обновить список");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(refreshButton);

        controlPanel.add(buttonPanel);

        add(controlPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> controller.onAddButtonClick());
        deleteButton.addActionListener(e -> controller.onDeleteButtonClick());
        updateButton.addActionListener(e -> controller.onUpdateButtonClick());
        refreshButton.addActionListener(e -> controller.onRefreshButtonClick());
    }
    
    // Кастомная модель для таблицы, с запретом редактирования определенных колонок
    private static class ClientTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column != 0 && column != 5;
        }
    }

    public JTable getClientTable() {
        return clientTable;
    }
    
    public void update(List<Client> clients) {
        String[] columnNames = {"ID", "Фамилия", "Имя", "Отчество","Количество стрижек", "Телефон", "Email", "Пол"};
        ClientTableModel model = (ClientTableModel) clientTable.getModel();
        model.setColumnIdentifiers(columnNames);
        model.setRowCount(0);

        for (Client client : clients) {
            Object[] row = {
                    client.getId(),
                    client.getSurname(),
                    client.getName(),
                    client.getPatronymic(),
                    client.getServices(),
                    client.getPhone(),
                    client.getEmail(),
                    client.getGender()
            };
            model.addRow(row);
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public void display() {
        setVisible(true);
    }
}
