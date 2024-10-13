class ClientRepYaml {
    private String filename;
    private List<Client> clients;

    public ClientRepYaml(String filename) {
        this.filename = filename;
        this.clients = readAll();
    }

    public List<Client> readAll() {
        List<Client> clients = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists() || file.length() == 0) {
            System.out.println("Файл не существует или пуст: " + filename);
            return clients; // Возвращаем пустой список
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Yaml yaml = new Yaml();
            List<Map<String, Object>> data = yaml.load(reader);
            for (Map<String, Object> map : data) {
                try {
                    clients.add(Client.fromYaml(map));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Client client : clients) {
            System.out.println(client.toYaml());
        }
        return clients;
    }

    private void saveAll() {
        DumperOptions options = new DumperOptions();
        options.setIndent(4); // Установка отступа
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);

        Representer representer = new Representer(options);
        representer.addClassTag(Client.class, Tag.MAP);

        Yaml yaml = new Yaml(representer, options);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            yaml.dump(clients, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean isUnique(String phone) {
        for (Client client : clients) {
            if (client.getPhone().equals(phone)) {
                return false;
            }
        }
        return true;
    }

    public void addClient(String name, String surname, String patronymic, String phone, String email, String gender) throws Exception {
        addClient(name, surname, patronymic, 0, phone, email, gender); // Присваиваем 0 по умолчанию
    }

    // Добавление объекта (формируется новый ID)
    public void addClient(String name, String surname, String patronymic, Integer total_services, String phone, String email, String gender) throws Exception {
        int newId = clients.stream().mapToInt(Client::getId).max().orElse(0) + 1;
        if (!isUnique(phone)) {
            throw new Exception("Клиент с таким телефоном уже существует!");
        }
        Client newClient = new Client(newId, name, surname, patronymic, total_services, phone, email, gender);
        clients.add(newClient);
        saveAll();
    }

    public boolean replaceById(int clientId, Client newClient) throws Exception {
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getId() == clientId) {
                if (!isUnique(newClient.getPhone())) {
                    throw new Exception("Нельзя заменить клиента: клиент с таким телефоном уже существует!");
                }
                Client client;
                if (newClient.getId() == null && newClient.getServices() == null) {
                    client = new Client(clientId, newClient.getName(), newClient.getSurname(), newClient.getPatronymic(), clients.get(i).getServices(), newClient.getPhone(), newClient.getEmail(), newClient.getGender());
                } else if (newClient.getId() == null) {
                    client = new Client(clientId, newClient.getName(), newClient.getSurname(), newClient.getPatronymic(), newClient.getServices(), newClient.getPhone(), newClient.getEmail(), newClient.getGender());
                } else {
                    client = newClient;
                }
                clients.set(i, client);
                saveAll();
                return true;
            }
        }
        return false;
    }

    public void deleteById(int clientId) {
        clients = clients.stream()
                .filter(customer -> customer.getId() != clientId)
                .collect(Collectors.toList());
        saveAll();
    }

    public int getCount() {
        return clients.size();
    }
}
