import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

class ClientPresenter {
    private final IClientRepository clientRep; 
    private int currentPage = 1;
    private final int recordsPerPage = 10;

    public ClientPresenter(IClientRepository clientRep) {
        this.clientRep = clientRep;
    }

    public String getClientListPage() throws IOException, IOException {
        List<Client> clients = clientRep.getKNSortList(currentPage, recordsPerPage);
        StringBuilder clientTableContent = new StringBuilder();
        int index = (currentPage - 1) * recordsPerPage + 1;

        for (Client client : clients) {
            clientTableContent.append("<tr>")
                    .append("<td>").append(index++).append("</td>")
                    .append("<td>").append(client.getSurname()).append("</td>")
                    .append("<td>").append(client.getName()).append("</td>")
                    .append("<td>").append(client.getPhone()).append("</td>")
                    .append("<td>")
                    .append("<a href='/client/details?id=").append(client.getId()).append("'>Подробнее</a> ")
                    .append("<form method='post' action='/client/delete' style='display:inline;'>")
                    .append("<input type='hidden' name='id' value='").append(client.getId()).append("'>")
                    .append("<button type='submit'>Удалить</button>")
                    .append("</form>")
                    .append("</td>")
                    .append("</tr>");
        }

        StringBuilder paginationControls = new StringBuilder();
        if (currentPage > 1) {
            paginationControls.append("<button onclick=\"window.location.href='/?page=").append(currentPage - 1).append("'\">Назад</button> ");
        }
        paginationControls.append("<span>Страница ").append(currentPage).append("</span>");
        if (clients.size() == recordsPerPage) {
            paginationControls.append(" <button onclick=\"window.location.href='/?page=").append(currentPage + 1).append("'\">Вперед</button>");
        }

        Path templatePath = Paths.get("src/web/index.html");
        String template = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

        String pageHtml = template.replace("{{clientTableContent}}", clientTableContent.toString())
                .replace("{{paginationControls}}", paginationControls.toString());

        return pageHtml;
    }


    public void setCurrentPage(int page) {
        this.currentPage = page;
    }

    public String generateAddClientPage() {
        try {
            Path templatePath = Paths.get("src/web/add_client.html");
            if (!Files.exists(templatePath)) {
                throw new IOException("Файл не найден: " + templatePath.toString());
            }
            String template = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);
            return template;
        } catch (IOException e) {
            e.printStackTrace();
            return "<html><body><h1>Ошибка сервера: " + e.getMessage() + "</h1></body></html>";
        }
    }

    public void onAddButtonClick(Client newClient) throws Exception {
        clientRep.addClient(newClient);
    }

    public String onDetailsButtonClick(int clientId) {
        return getClientDetailsPage(String.valueOf(clientId));
    }

    public void onDeleteButtonClick(int clientId) {
        try {
            deleteClient(clientId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный ID клиента: " + clientId);
        }
    }


    public void onUpdateButtonClick(Client updatedClient) {
        try {
            int clientId = updatedClient.getId();
            updateClient(clientId, updatedClient);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при обновлении клиента: " + e.getMessage());
        }
    }

    public String getClientDetailsPage(String query) {
        int clientId = parseIdFromQuery(query);
        Client client = clientRep.getById(clientId);

        if (client == null) {
            return "<html><body>Клиент не найден</body></html>";
        }

        try {
            Path templatePath = Paths.get("src/web/client_details.html");
            if (!Files.exists(templatePath)) {
                throw new IOException("Файл не найден: " + templatePath.toString());
            }

            String template = Files.readString(templatePath, StandardCharsets.UTF_8);

            template = template.replace("${id}", String.valueOf(client.getId()));
            template = template.replace("${surname}", client.getSurname());
            template = template.replace("${name}", client.getName());
            template = template.replace("${patronymic}", client.getPatronymic());
            template = template.replace("${services}", String.valueOf(client.getServices()));
            template = template.replace("${phone}", client.getPhone());
            template = template.replace("${email}", client.getEmail());
            template = template.replace("${gender}", client.getGender());
            return template;
        } catch (IOException e) {
            e.printStackTrace();
            return "<html><body><h1>Ошибка сервера: " + e.getMessage() + "</h1></body></html>";
        }
    }


    public void addClient(Client newClient) throws Exception {
        if (isValid(newClient)) {
            clientRep.addClient(newClient);
        } else {
            throw new IllegalArgumentException("Некорректные данные клиента");
        }
    }

    public void deleteClient(int clientId) {
        clientRep.deleteById(clientId);
    }

    public void updateClient(int clientId, Client updatedClient) throws Exception {
        if (isValid(updatedClient)) {
            clientRep.updateClient(clientId, updatedClient);
        } else {
            throw new IllegalArgumentException("Некорректные данные клиента");
        }
    }

    private boolean isValid(Client client) {
        return Client.validateS(client.getSurname())
                && Client.validateS(client.getName())
                && Client.validateS(client.getPatronymic())
                && Client.validateI(client.getServices())
                && Client.validatePhone(client.getPhone())
                && Client.validateEmail(client.getEmail())
                && Client.validateS(client.getGender());
    }

    private int parseIdFromQuery(String query) {
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals("id")) {
                return Integer.parseInt(keyValue[1]);
            }
        }
        throw new IllegalArgumentException("ID не найден в запросе");
    }
}
