class ClientAdapter implements IClientRep {
    private ClientRepDB clientRepDB;

    public ClientAdapter(String dbName, String user, String password, String host, String port) {
        this.clientRepDB = new ClientRepDB(dbName, user, password, host, port);
    }

    @Override
    public Client getById(int clientId) {
        return clientRepDB.getById(clientId);
    }

    @Override
    public List<Client> getKNSortList(int k, int n) {
        return clientRepDB.getKNSortList(k, n);
    }

    @Override
    public void addClient(Client client) throws SQLException {
        clientRepDB.addClient(client);
    }

    @Override
    public boolean replaceById(int clientId, Client newClient) throws SQLException {
        return clientRepDB.replaceById(clientId, newClient);
    }

    @Override
    public void deleteById(int clientId) {
        clientRepDB.deleteById(clientId);
    }

    @Override
    public int getCount() {
        return clientRepDB.getCount();
    }
}
