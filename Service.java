import org.json.JSONObject;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class Service {
    private Integer id;
    private Integer client_id;
    private Integer haircut_id;
    private LocalDate date;
    private Integer price;

    public Service(Integer client_id, Integer haircut_id, String date, Integer price) {
        this.setId(null);
        this.setClientId(client_id);
        this.setHaircutId(haircut_id);
        this.setDate(date);
        this.setPrice(price);
    }

    public Service(Integer id, Integer client_id, Integer haircut_id, LocalDate date, Integer price) {
        this(client_id, haircut_id, String.valueOf(date), price);
        this.setId(id);
    }


    // Перегруженный конструктор, принимающий строку
    public Service(int id, int client_id, int haircut_id, String clientData, int price) {
        String[] data = clientData.split(",");

        // Проверяем, что передано не менее 6 обязательных полей (surname, name, patronymic, phone, email, gender)
        if (data.length < 4) {
            throw new IllegalArgumentException("Неверный формат данных. Ожидается минимум 3 значения.");
        }

        if (data[0].trim().matches("\\d+")) {
            this.setId(Integer.parseInt(data[0].trim()));
            this.setClientId(Integer.valueOf(data[1].trim()));
            this.setHaircutId(Integer.valueOf(data[2].trim()));
            this.setDate(data[3].trim());
            this.setPrice(Integer.valueOf(data[4].trim()));
        } else {
            this.setClientId(Integer.valueOf(data[0].trim()));
            this.setHaircutId(Integer.valueOf(data[1].trim()));
            this.setDate(data[2].trim());
            this.setPrice(Integer.valueOf(data[3].trim()));
        }
    }


    // Геттеры
    public Integer getPrice() {
        return price;
    }
    public Integer getId() {
        return id;
    }
    public Integer getClientId() {return client_id;}
    public Integer getHaircutId() {return haircut_id;}
    public LocalDate getDate() {return date;}

    // Сеттеры
    public void setPrice(Integer price) {
        if (validateI(price)==true)
            this.price = price;
        else
            throw new IllegalArgumentException("Цена не может быть отрицательной.");
    }

    public void setId(Integer id) {
        if (id==null || validateI(id)==true)
            this.id = id;
        else
            throw new IllegalArgumentException("id не может быть отрицательным.");
    }

    public void setClientId(Integer client_id) {
        if (client_id==null || validateI(client_id)==true)
            this.client_id = client_id;
        else
            throw new IllegalArgumentException("id не может быть отрицательным.");
    }

    public void setHaircutId(Integer haircut_id) {
        if (haircut_id==null || validateI(haircut_id)==true)
            this.haircut_id = haircut_id;
        else
            throw new IllegalArgumentException("id не может быть отрицательным.");
    }

    public void setDate(String date) {
        this.date = LocalDate.parse(date);
    }

    public static boolean validateI(int val) {
        if (val < 0)
            return false;
        else
            return true;
    }

    @Override
    public String toString() {
        return "Service {" +
                "id=" + getId() +
                ", client_id='" + getClientId() + '\'' +
                ", haircut_id='" + getHaircutId() + '\'' +
                ", date='" + getDate() + '\'' +
                ", price=" + getPrice() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Cравнение ссылок
        if (o == null || getClass() != o.getClass()) return false;  // Проверка на null и тип
        Service service = (Service) o;  // Приведение типов
        return getId() == service.getId();
    }

    public static Service fromJson(JSONObject jsonObject) throws Exception {
        return new Service(
                jsonObject.optInt("id"), // Или jsonObject.optInt, если может быть null
                jsonObject.optInt("client_id"),
                jsonObject.optInt("haircut_id"),
                jsonObject.getString("date"),
                jsonObject.optInt("price")
        );
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getId());
        jsonObject.put("client_id", this.getClientId());
        jsonObject.put("haircut_id", this.getHaircutId());
        jsonObject.put("date", this.getDate());
        jsonObject.put("price", this.getPrice());
        return jsonObject;
    }

    public static Service fromYaml(Map<String, Object> map) {
        int id = (int) map.get("id");
        int client_id = (int) map.get("client_id");
        int haircut_id = (int) map.get("haircut_id");
        String date = (String) map.get("date");
        Integer price = (Integer) map.get("price");

        return new Service(id, client_id, haircut_id, date, price);
    }

    public Map<String, Object> toYaml() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.getId());
        map.put("client_id", this.getClientId());
        map.put("haircut_id", this.getHaircutId());
        map.put("date", this.getDate());
        map.put("price", this.getPrice());
        return map;
    }
}
