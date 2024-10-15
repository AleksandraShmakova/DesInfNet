class JsonClientStrategy implements ClientStrategy {
    private String filename;

    public JsonClientStrategy(String filename) {
        this.filename = filename;
    }

    public List<Client> readAll() {
        List<Client> clients = new ArrayList<>();
        File file = new File(filename);

        if (!file.exists() || file.length() == 0) {
            System.out.println("Файл не существует или пуст: " + filename);
            return clients;
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

    public void saveAll(List<Client> clients) {
        JSONArray jsonArray = new JSONArray();
        for (Client client : clients) {
            jsonArray.put(client.toJson());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(jsonArray.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
