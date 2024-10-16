class DatabaseConnection {
    private String dbName;
    private String user;
    private String password;
    private String host;
    private String port;

    public DatabaseConnection(String dbName, String user, String password, String host, String port) {
        this.dbName = dbName;
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public String getUrl() {
        return "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
