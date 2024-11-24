interface IClientRepository {
    Client getById(int clientId);
    List<Client> getKNSortList(int k, int n);
    void addClient(Client client) throws Exception;
    boolean replaceById(int clientId, Client newClient) throws Exception;
    void deleteById(int clientId);
    int getCount();

    void addObserver(ClientObserver observer);
    void removeObserver(ClientObserver observer);
    void notifyObservers();
}
