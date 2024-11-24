class ClientView extends JFrame {
    private JTable clientTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton refreshButton;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField patronymicField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField genderField;
    private JTextField totalServicesField;


    public ClientView() {
        setTitle("Client Management");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        clientTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(clientTable);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1));

        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        inputPanel.add(new JLabel("Фамилия:"));
        surnameField = new JTextField();
        inputPanel.add(surnameField);

        inputPanel.add(new JLabel("Имя:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Отчество:"));
        patronymicField = new JTextField();
        inputPanel.add(patronymicField);

        inputPanel.add(new JLabel("Количество стрижек:"));
        totalServicesField = new JTextField();
        inputPanel.add(totalServicesField);

        inputPanel.add(new JLabel("Телефон:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Пол:"));
        genderField = new JTextField();
        inputPanel.add(genderField);


        controlPanel.add(inputPanel);

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

    public Client getClientInput() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String patronymic = patronymicField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String gender = genderField.getText();

        int totalServices = 0;
        try {
            String totalServicesText = totalServicesField.getText().trim();
            if (!totalServicesText.isEmpty()) {
                totalServices = Integer.parseInt(totalServicesText);
            }
        } catch (NumberFormatException e) {
            // Если введено некорректное значение, оставляем 0
            totalServices = 0;
        }

        return new Client(0, name, surname, patronymic, totalServices, phone, email, gender);
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

    // Обновление представления с данными клиентов
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
