import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

class ClientHTTP {
    private final ClientPresenter presenter;

    public ClientHTTP(ClientPresenter presenter) {
        this.presenter = presenter;
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", this::handleClientList);
        server.createContext("/add", exchange -> {
            try {
                handleAddClient(exchange);
            } catch (IOException e) {
                e.printStackTrace();
                sendResponse(exchange, "Внутренняя ошибка сервера", 500);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        server.createContext("/client/delete", this::handleDeleteClient);
        server.createContext("/client/details", this::handleClientDetails);
        server.createContext("/client/edit", this::handleEditClient);
        server.createContext("/style.css", this::handleStaticFiles);
      
        server.setExecutor(null);
        server.start();
        System.out.println("Сервер запущен на http://localhost:8080/");
    }


    private void handleStaticFiles(HttpExchange exchange) throws IOException {
        String uriPath = exchange.getRequestURI().getPath();
        String filePath = "src/web" + uriPath.replace("/static", "");

        Path path = Paths.get(filePath);

        if (Files.exists(path)) {
            String mimeType = Files.probeContentType(path);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            byte[] fileContent = Files.readAllBytes(path);

            exchange.getResponseHeaders().add("Content-Type", mimeType);
            exchange.sendResponseHeaders(200, fileContent.length);

            OutputStream os = exchange.getResponseBody();
            os.write(fileContent);
            os.close();
        } else {
            String response = "Файл не найден: " + filePath;
            exchange.sendResponseHeaders(404, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private void handleClientList(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        int page = 1;

        if (query != null && query.contains("page=")) {
            try {
                page = Integer.parseInt(query.split("=")[1]);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        presenter.setCurrentPage(page);
        String response = presenter.getClientListPage();

        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes("UTF-8"));
        os.close();
    }

    private void handleAddClient(HttpExchange exchange) throws Exception {
        if ("GET".equals(exchange.getRequestMethod())) {
            String response = presenter.generateAddClientPage();
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("UTF-8"));
            os.close();
        }
        else if ("POST".equals(exchange.getRequestMethod())) {
            InputStream bodyStream = exchange.getRequestBody();
            String body = new String(bodyStream.readAllBytes(), StandardCharsets.UTF_8);
            Client newClient = parseClient(body);
            presenter.onAddButtonClick(newClient);
            sendRedirect(exchange, "/");
        } else {
            sendResponse(exchange, "Метод не поддерживается", 405);
        }
    }
  
    private void handleDeleteClient(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String requestBody = getRequestBody(exchange);

            if (requestBody == null || requestBody.isEmpty()) {
                sendResponse(exchange, "Ошибка: Пустое тело запроса", 400);
                return;
            }

            Client client = parseClient(requestBody);

            if (client.getId() != null && client.getId() > 0) {
                presenter.onDeleteButtonClick(client.getId()); 
                sendRedirect(exchange, "/");
            } else {
                sendResponse(exchange, "Ошибка: ID клиента не найден или некорректен", 400);
            }
        } else {
            sendResponse(exchange, "Метод не поддерживается", 405);
        }
    }


    private void handleClientDetails(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String query = exchange.getRequestURI().getQuery();
            String response = presenter.getClientDetailsPage(query);
            sendResponse(exchange, response, 200);
        } else {
            sendResponse(exchange, "Метод не поддерживается", 405);
        }
    }

    private void handleEditClient(HttpExchange exchange) throws IOException {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                String requestBody = getRequestBody(exchange);
                Client updatedClient = parseClient(requestBody);
                presenter.onUpdateButtonClick(updatedClient);
                sendRedirect(exchange, "/");
            } else {
                sendResponse(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, "Ошибка сервера: " + e.getMessage(), 500);
        }
    }


    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes("UTF-8").length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes("UTF-8"));
        }
    }


    private void sendRedirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().add("Location", location);
        exchange.sendResponseHeaders(302, -1);
    }

    private String getRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes());
    }

    private Client parseClient(String requestBody) {
        Client client = new Client();
        String[] params = requestBody.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                switch (keyValue[0]) {

                    case "id":
                        client.setId(Integer.parseInt(keyValue[1]));
                        break;
                    case "surname":
                        client.setSurname(decodeURIComponent(keyValue[1]));
                        break;
                    case "name":
                        client.setName(decodeURIComponent(keyValue[1]));
                        break;
                    case "patronymic":
                        client.setPatronymic(decodeURIComponent(keyValue[1]));
                        break;
                    case "services":
                        client.setServices(Integer.parseInt(keyValue[1]));
                        break;
                    case "phone":
                        client.setPhone(decodeURIComponent(keyValue[1]));
                        break;
                    case "email":
                        client.setEmail(decodeURIComponent(keyValue[1]));
                        break;
                    case "gender":
                        client.setGender(decodeURIComponent(keyValue[1]));
                        break;
                }
            }
        }
        return client;
    }

    private String decodeURIComponent(String value) {
        try {
            return java.net.URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}
