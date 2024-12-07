import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

class ClientViewWeb implements IClientView {

    @Override
    public String displayClientList(List<Client> clients, int currentPage, int recordsPerPage) {
        StringBuilder clientTableContent = new StringBuilder();
        int index = (currentPage - 1) * recordsPerPage + 1;

        // Генерация строк таблицы
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

        // Генерация элементов навигации
        StringBuilder paginationControls = new StringBuilder();
        if (currentPage > 1) {
            paginationControls.append("<button onclick=\"window.location.href='/?page=").append(currentPage - 1).append("'\">Назад</button> ");
        }
        paginationControls.append("<span>Страница ").append(currentPage).append("</span>");
        if (clients.size() == recordsPerPage) {
            paginationControls.append(" <button onclick=\"window.location.href='/?page=").append(currentPage + 1).append("'\">Вперед</button>");
        }

        // Загружаем шаблон страницы и подставляем данные
        Path templatePath = Paths.get("src/web/index.html");
        String template = null;
        try {
            template = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Заменяем плейсхолдеры на данные
        String pageHtml = template.replace("{{clientTableContent}}", clientTableContent.toString())
                .replace("{{paginationControls}}", paginationControls.toString());

        return pageHtml;
    }

    @Override
    public String generateAddClientPage() {
        try {
            Path templatePath = Paths.get("src/web/add_client.html");
            if (!Files.exists(templatePath)) {
                throw new IOException("Файл не найден: " + templatePath.toString());
            }
            String template = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);
            return template;
        } catch (IOException e) {
            e.printStackTrace(); // Выводим стек трассировки в консоль или лог
            return "<html><body><h1>Ошибка сервера: " + e.getMessage() + "</h1></body></html>";
        }
    }

    @Override
    public String displayClientDetails(Client client) {
        if (client == null) {
            return "<html><body>Клиент не найден</body></html>";
        }
        try {
            Path templatePath = Paths.get("src/web/client_details.html");
            if (!Files.exists(templatePath)) {
                throw new IOException("Файл не найден: " + templatePath.toString());
            }

            String template = Files.readString(templatePath, StandardCharsets.UTF_8);

            // Заменяем плейсхолдеры в шаблоне на реальные значения
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
            e.printStackTrace(); // Выводим стек трассировки в консоль или лог
            return "<html><body><h1>Ошибка сервера: " + e.getMessage() + "</h1></body></html>";
        }
    }

    @Override
    public void showErrorMessage(String message) {
        System.err.println("Ошибка: " + message);
    }

    @Override
    public void showSuccessMessage(String message) {
        System.out.println("Успех: " + message);
    }
}
