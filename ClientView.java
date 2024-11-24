class ClientView extends JFrame {
    private JTable clientTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton refreshButton;


    public ClientView() {
        setTitle("Client Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        clientTable = new JTable();
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
    }

    public int getSelectedClientId() {
        int selectedRow = clientTable.getSelectedRow();
        if (selectedRow != -1) {
            return (int) clientTable.getValueAt(selectedRow, 0);
        }
        return -1; // Возвращает -1, если строка не выбрана
    }

    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void setDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void setUpdateButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void setRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }

    public void update(List<Client> clients) {
        String[] columnNames = {"ID", "Фамилия", "Имя", "Отчество","Количество стрижек", "Телефон", "Email", "Пол"};
        Object[][] data = new Object[clients.size()][columnNames.length];

        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            data[i][0] = client.getId();
            data[i][1] = client.getSurname();
            data[i][2] = client.getName();
            data[i][3] = client.getPatronymic();
            data[i][4] = client.getServices();
            data[i][5] = client.getPhone();
            data[i][6] = client.getEmail();
            data[i][7] = client.getGender();
        }

        clientTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public void display() {
        setVisible(true);
    }
}
