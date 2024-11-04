class ClientRepository {
    protected String filename;
    protected List<Client> clients;
    private ClientStrategy strategy;

    public ClientRepository(String filename, ClientStrategy strategy) {
        this.filename = filename;
        this.setStrategy(strategy);
        this.clients = readAllClients();
    }

    public void setStrategy(ClientStrategy strategy) {
        this.strategy = strategy;
    }

    public void saveAllClients() {
        strategy.saveAll(clients);
    }

    public List<Client> readAllClients() {
        return strategy.readAll();
    }

    public Client getById(int clientId) {
        for (Client client : clients) {
            if (client.getId() == clientId) {
                return client;
            }
        }
        return null;
    }

    public List<Client> getKNSortList(int k, int n) {
        int start = (k - 1) * n;
        int end = Math.min(start + n, clients.size());
        return clients.subList(start, end);
    }

    public void sortByField() {
        clients.sort(Comparator.comparing(Client::getId));
    }

    protected boolean isUnique(String phone) {
        for (Client client : clients) {
            if (client.getPhone().equals(phone)) {
                return false;
            }
        }
        return true;
    }

    public void addClient(Client client) throws Exception {
        int newId = clients.stream().mapToInt(Client::getId).max().orElse(0) + 1;
        if (!isUnique(client.getPhone())) {
            throw new Exception("Клиент с таким телефоном уже существует!");
        }
        Client newClient = new Client(newId, client.getName(), client.getSurname(), client.getPatronymic(), client.getServices(), client.getPhone(), client.getEmail(), client.getGender());
        clients.add(newClient);
    }

    public boolean replaceById(int clientId, Client newClient) throws Exception {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() == clientId) {
                if (!isUnique(newClient.getPhone())) {
                    throw new Exception("Нельзя заменить клиента: клиент с таким телефоном уже существует!");
                }
                clients.set(i, newClient);
                return true;
            }
        }
        return false;
    }

    public void deleteById(int clientId) {
        clients = clients.stream()
                .filter(customer -> customer.getId() != clientId)
                .collect(Collectors.toList());
    }

    public int getCount() {
        return clients.size();
    }
}
