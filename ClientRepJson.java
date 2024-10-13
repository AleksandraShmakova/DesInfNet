class ClientRepJson extends ClientRep{

    public ClientRepJson(String filename) {
        super(filename);
    }

    @Override
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

    @Override
    protected void saveAll() {
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
}
