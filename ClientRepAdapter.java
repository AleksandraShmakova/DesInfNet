class ClientRepositoryAdapter extends ClientRepository {
    private final ClientRepDB clientRepDB;

    public ClientRepositoryAdapter(String filename, ClientStrategy strategy, String dbName, String user, String password, String host, String port) {
        super(filename, strategy);
        this.clientRepDB = new ClientRepDB(dbName, user, password, host, port);
    }

    @Override
    public Client getById(int clientId) {
        Client client = super.getById(clientId);
        if (client == null) {
            client = clientRepDB.getById(clientId);
        }
        return client;
    }

    @Override
    public List<Client> getKNSortList(int k, int n) {
        return clientRepDB.getKNSortList(k, n);
    }

    @Override
    public void addClient(Client client) throws Exception {
        try {
            clientRepDB.addClient(client);
            super.addClient(client);
        } catch (SQLException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    @Override
    public boolean replaceById(int clientId, Client newClient) throws Exception {
        try {
            boolean updatedInDb = clientRepDB.replaceById(clientId, newClient);
            if (updatedInDb) {
                super.replaceById(clientId, newClient);
            }
            return updatedInDb;
        } catch (SQLException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(int clientId) {
        clientRepDB.deleteById(clientId);
        super.deleteById(clientId);
    }

    @Override
    public int getCount() {
        return clientRepDB.getCount();
    }
}
