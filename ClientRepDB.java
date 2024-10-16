class ClientRepDB {
    private DatabaseConnection db;

    public ClientRepDB(String dbName, String user, String password, String host, String port) {
        this.db = DatabaseConnection.getInstance(dbName, user, password, host, port);
    }

    public Client getById(int clientId) {
        Client client = null;
        String query = "SELECT * FROM clients WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("patronymic"),
                        rs.getInt("total_services"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("gender")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Client> getKNSortList(int k, int n) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM clients ORDER BY id OFFSET ? LIMIT ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, (k - 1) * n);
            stmt.setInt(2, n);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("patronymic"),
                        rs.getInt("total_services"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("gender")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public void addClient(Client client) throws SQLException {
        if (!isUnique(client.getPhone())) {
            throw new SQLException("Клиент с таким телефоном уже существует!");
        }

        String sql = "INSERT INTO clients (name, surname, patronymic, total_services, phone, email, gender) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getSurname());
            stmt.setString(3, client.getPatronymic());
            stmt.setObject(4, client.getServices(), Types.INTEGER); // Используем setObject для обработки null
            stmt.setString(5, client.getPhone());
            stmt.setString(6, client.getEmail());
            stmt.setString(7, client.getGender());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt("id");
                System.out.println("Клиент добавлен с ID: " + generatedId);
            }
        }
    }

    public boolean replaceById(int clientId, Client newClient) throws SQLException {
        if (!isUnique(newClient.getPhone())) {
            throw new SQLException("Нельзя заменить клиента: клиент с таким телефоном уже существует!");
        }

        String sql = "UPDATE clients SET name = ?, surname = ?, patronymic = ?, total_services = ?, phone = ?, email = ?, gender = ? WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newClient.getName());
            stmt.setString(2, newClient.getSurname());
            stmt.setString(3, newClient.getPatronymic());
            stmt.setObject(4, newClient.getServices(), Types.INTEGER); // Используем setObject для обработки null
            stmt.setString(5, newClient.getPhone());
            stmt.setString(6, newClient.getEmail());
            stmt.setString(7, newClient.getGender());
            stmt.setInt(8, clientId);
            return stmt.executeUpdate() > 0;
        }
    }

    public void deleteById(int clientId) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clientId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCount() {
        String sql = "SELECT COUNT(*) FROM clients";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean isUnique(String phone) {
        String sql = "SELECT COUNT(*) FROM clients WHERE phone = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Если 0, значит уникален
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
