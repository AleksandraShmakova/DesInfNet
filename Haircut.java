import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class Haircut {
    private Integer id;
    private String title;
    private String gender;
    private Integer price;

    public Haircut(String title, String gender, Integer price) {
        this.setId(null);
        this.setTitle(title);
        this.setGender(gender);
        this.setPrice(price);
    }

    public Haircut(Integer id, String title, String gender, Integer price) {
        this(title, gender, price);
        this.setId(id);
    }


    // Перегруженный конструктор, принимающий строку
    public Haircut(String clientData) {
        String[] data = clientData.split(",");

        // Проверяем, что передано не менее 6 обязательных полей (surname, name, patronymic, phone, email, gender)
        if (data.length < 3) {
            throw new IllegalArgumentException("Неверный формат данных. Ожидается минимум 3 значения.");
        }

        if (data[0].trim().matches("\\d+")) {
            this.setId(Integer.parseInt(data[0].trim()));
            this.setTitle(data[1].trim());
            this.setGender(data[2].trim());
            this.setPrice(Integer.parseInt(data[3].trim()));
        } else {
            this.setTitle(data[0].trim());
            this.setGender(data[1].trim());
            this.setPrice(Integer.parseInt(data[2].trim()));
        }
    }


    // Геттеры
    public Integer getPrice() {
        return price;
    }
    public Integer getId() {
        return id;
    }
    public String getTitle() {return title;}
    public String getGender() {return gender;}

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

    public void setTitle(String title) {
        if (validateS(title)==true)
            this.title = title;
        else
            throw new IllegalArgumentException("Неверный формат названия.");
    }

    public void setGender(String gender) {
        if (validateS(gender)==true)
            this.gender = gender;
        else
            throw new IllegalArgumentException("Пол не может быть пустым.");
    }

    public static boolean validateS(String value) {
        if (value == null || value.trim().isEmpty() || !value.matches("^[A-Za-zА-Яа-яЁё\\s]+$"))
            return false;
        else
            return true;
    }

    public static boolean validateI(int val) {
        if (val < 0)
            return false;
        else
            return true;
    }

    @Override
    public String toString() {
        return "Haircut {" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Сравнение ссылок
        if (o == null || getClass() != o.getClass()) return false;  // Проверка на null и тип
        Haircut haircut = (Haircut) o;  // Приведение типов
        return getId() == haircut.getId();
    }

    public static Haircut fromJson(JSONObject jsonObject) throws Exception {
        return new Haircut(
                jsonObject.optInt("id"), // Или jsonObject.optInt, если может быть null
                jsonObject.getString("title"),
                jsonObject.getString("gender"),
                jsonObject.optInt("price")
        );
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getId());
        jsonObject.put("title", this.getTitle());
        jsonObject.put("gender", this.getGender());
        jsonObject.put("price", this.getPrice());
        return jsonObject;
    }

    public static Haircut fromYaml(Map<String, Object> map) {
        int id = (int) map.get("id");
        String title = (String) map.get("title");
        String gender = (String) map.get("gender");
        Integer price = (Integer) map.get("price");

        return new Haircut(id, title, gender, price);
    }

    public Map<String, Object> toYaml() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.getId());
        map.put("title", this.getTitle());
        map.put("gender", this.getGender());
        map.put("price", this.getPrice());
        return map;
    }
}
