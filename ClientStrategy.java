interface ClientStrategy {
    List<Client> readAll();
    void saveAll(List<Client> clients);
}
