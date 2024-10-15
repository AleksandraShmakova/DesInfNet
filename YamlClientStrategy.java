class YamlClientStrategy implements ClientStrategy {
    private String filename;

    public YamlClientStrategy(String filename) {
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
    public void saveAll(List<Client> clients) {
        DumperOptions options = new DumperOptions();
        options.setIndent(4);
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
}
