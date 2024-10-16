class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private String dbName;
    private String user;
    private String password;
    private String host;
    private String port;

    private DatabaseConnection(String dbName, String user, String password, String host, String port) {
        this.dbName = dbName;
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;

        try {
            String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка подключения к базе данных");
        }
    }

    public static DatabaseConnection getInstance(String dbName, String user, String password, String host, String port) {
        if (instance == null) {
            instance = new DatabaseConnection(dbName, user, password, host, port);
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
