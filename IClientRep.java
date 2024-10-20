interface IClientRep {
    Client getById(int clientId);
    List<Client> getKNSortList(int k, int n);
    void addClient(Client client) throws Exception;
    boolean replaceById(int clientId, Client newClient) throws Exception;
    void deleteById(int clientId);
    int getCount();
}
