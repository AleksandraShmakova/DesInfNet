class ClientFormView extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField patronymicField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField genderField;
    private JTextField totalServicesField;
    private JButton confirmButton;
    private JButton cancelButton;

    public ClientFormView(String title, String confirmButtonText) {
        setTitle(title);
        setSize(400, 300);
        setLayout(new GridLayout(9, 2));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new JLabel("Фамилия:"));
        surnameField = new JTextField();
        add(surnameField);

        add(new JLabel("Имя:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Отчество:"));
        patronymicField = new JTextField();
        add(patronymicField);

        add(new JLabel("Телефон:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Пол:"));
        genderField = new JTextField();
        add(genderField);

        add(new JLabel("Количество стрижек:"));
        totalServicesField = new JTextField();
        add(totalServicesField);

        confirmButton = new JButton(confirmButtonText);
        cancelButton = new JButton("Отмена");
        add(confirmButton);
        add(cancelButton);
    }

    public void setClientData(Client client) {
        if (client != null) {
            nameField.setText(client.getName());
            surnameField.setText(client.getSurname());
            patronymicField.setText(client.getPatronymic());
            phoneField.setText(client.getPhone());
            emailField.setText(client.getEmail());
            genderField.setText(client.getGender());
            totalServicesField.setText(String.valueOf(client.getServices()));
        }
    }

    public void setPhoneFieldEditable(boolean editable) {
        phoneField.setEditable(editable); // Делаем поле доступным или недоступным для редактирования
    }

    public Client getClientInput() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String patronymic = patronymicField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String gender = genderField.getText().trim();

        int totalServices = 0;
        try {
            totalServices = Integer.parseInt(totalServicesField.getText().trim());
        } catch (NumberFormatException ignored) {}

        return new Client(0, name, surname, patronymic, totalServices, phone, email, gender);
    }

    public void setConfirmButtonListener(ActionListener listener) {
        confirmButton.addActionListener(listener);
    }

    public void setCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
