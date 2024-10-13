class ClientRepJson {
    private String filename;
    private List<Client> clients;

    public ClientRepJson(String filename) {
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
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            System.out.println("Содержимое файла: " + sb.toString());

            JSONArray data = new JSONArray(sb.toString());
            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                try {
                    clients.add(Client.fromJson(jsonObject));
                } catch (Exception e) {
                    e.printStackTrace(); 
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return clients;
    }

    private void saveAll() {
        JSONArray jsonArray = new JSONArray();
        for (Client client : clients) {
            jsonArray.put(client.toJson());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(jsonArray.toString(4)); // Отступ в 4 пробела
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
                if(newClient.getId() == null  && newClient.getServices() == null) {
                    client = new Client(clientId, newClient.getName(), newClient.getSurname(), newClient.getPatronymic(), clients.get(i).getServices(), newClient.getPhone(), newClient.getEmail(), newClient.getGender());
                }
                else if (newClient.getId() == null) {
                    client = new Client(clientId, newClient.getName(), newClient.getSurname(), newClient.getPatronymic(), newClient.getServices(), newClient.getPhone(), newClient.getEmail(), newClient.getGender());
                }
                else client = newClient;
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
