class Client extends ClientShort {
    private String patronymic;
    private Integer total_services;
    private String gender;
    private String email;


    public Client(String surname, String name, String patronymic, String phone, String email, String gender) {
        super(surname,name,phone);
        this.setPatronymic(patronymic);
        this.setServices(0);
        this.setEmail(email);
        this.setGender(gender);
    }

    public Client(String surname, String name, String patronymic, Integer total_services, String phone, String email, String gender) {
        this(surname,name, patronymic, phone, email, gender);
        this.setServices(total_services);
    }

    public Client(Integer id, String surname, String name, String patronymic, Integer total_services, String phone, String email, String gender) {
        this(surname,name, patronymic, total_services, phone, email, gender);
        this.setId(id);
    }

    // Перегруженный конструктор, принимающий строку
    public Client(String clientData) {
        // Ожидаемый формат строки: "id,surname,name,patronymic,total_services,phone,email,gender"
        String[] data = clientData.split(",");

        // Проверяем, что передано не менее 6 обязательных полей (surname, name, patronymic, phone, email, gender)
        if (data.length < 6) {
            throw new IllegalArgumentException("Неверный формат данных. Ожидается минимум 6 значений.");
        }

        if (data[0].trim().matches("\\d+")) {
            this.setId(Integer.parseInt(data[0].trim()));
            this.setSurname(data[1].trim());
            this.setName(data[2].trim());
            this.setPatronymic(data[3].trim());
            if (data[4].trim().matches("\\d+")) {
                this.setServices(Integer.parseInt(data[4].trim()));
                this.setPhone(data[5].trim());
                this.setEmail(data[6].trim());
                this.setGender(data[7].trim());
            } else {
                this.setPhone(data[4].trim());
                this.setEmail(data[5].trim());
                this.setGender(data[6].trim());
            }
        } else {
            this.setSurname(data[0].trim());
            this.setName(data[1].trim());
            this.setPatronymic(data[2].trim());
            if (data[3].trim().matches("\\d+")) {
                this.setServices(Integer.parseInt(data[3].trim()));
                this.setPhone(data[4].trim());
                this.setEmail(data[5].trim());
                this.setGender(data[6].trim());
            } else {
                this.setPhone(data[3].trim());
                this.setEmail(data[4].trim());
                this.setGender(data[5].trim());
            }
        }
    }


    // Геттеры
    public String getPatronymic() {
        return patronymic;
    }

    public Integer getServices() {
        return total_services;
    }

    public String getEmail() {return email;}
    public String getGender() {return gender;}

    // Сеттеры
    public void setPatronymic(String patronymic) {
        if (validateS(patronymic)==true)
            this.patronymic = patronymic;
        else
            throw new IllegalArgumentException("Неверный формат отчества.");
    }

    public void setServices(Integer total_services) {
        if (total_services==null || validateI(total_services)==true)
            this.total_services = total_services;
        else
            throw new IllegalArgumentException("Количество услуг не может быть отрицательным.");
    }

    public void setEmail(String email) {
        if (validateEmail(email)==true)
            this.email = email;
        else
            throw new IllegalArgumentException("Неверный формат почты.");
    }

    public void setGender(String gender) {
        if (validateS(gender)==true)
            this.gender = gender;
        else
            throw new IllegalArgumentException("Пол не может быть пустым.");
    }

    public static boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Email должен содержать один символ '@'
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@')) {
            return false;
        }

        // Проверка, что есть домен после '@'
        String domain = email.substring(atIndex + 1);
        if (domain.isEmpty() || !domain.contains(".")) {
            return false;
        }

        // Проверка, что домен содержит хотя бы одну точку и доменная часть после последней точки
        String topLevelDomain = domain.substring(domain.lastIndexOf('.') + 1);
        if (topLevelDomain.length() < 2) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Client {" +
                "id=" + getId() +
                ", surname='" + getSurname() + '\'' +
                ", name='" + getName() + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", total_services=" + total_services +
                ", phone='" + getPhone() + '\'' +
                ", email='" + email + '\'' +
                ", gender=" + gender +
                '}';
    }

    public String toShortString() {
        return "Client {" +
                "surname = '" + getSurname() + '\'' +
                ", name = '" + getName() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Сравнение ссылок
        if (o == null || getClass() != o.getClass()) return false;  // Проверка на null и тип
        Client client = (Client) o;  // Приведение типов
        return getPhone() == client.getPhone();
    }

    public static Client fromJson(JSONObject jsonObject) throws Exception {
        return new Client(
                jsonObject.optInt("id"), // Или jsonObject.optInt, если может быть null
                jsonObject.getString("name"),
                jsonObject.getString("surname"),
                jsonObject.getString("patronymic"),
                jsonObject.optInt("total_services", 0),    // Если total_services может быть null
                jsonObject.getString("phone"),
                jsonObject.getString("email"),
                jsonObject.getString("gender")
        );
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getId());
        jsonObject.put("name", this.getName());
        jsonObject.put("surname", this.getSurname());
        jsonObject.put("patronymic", this.getPatronymic());
        jsonObject.put("total_services", this.getServices());
        jsonObject.put("phone", this.getPhone());
        jsonObject.put("email", this.getEmail());
        jsonObject.put("gender", this.getGender());
        return jsonObject;
    }

    public static Client fromYaml(Map<String, Object> map) {
        int id = (int) map.get("id");
        String name = (String) map.get("name");
        String surname = (String) map.get("surname");
        String patronymic = (String) map.get("patronymic");
        Integer total_services = (Integer) map.get("total_services");
        String phone = (String) map.get("phone");
        String email = (String) map.get("email");
        String gender = (String) map.get("gender");

        return new Client(id, name, surname, patronymic, total_services, phone, email, gender);
    }

    public Map<String, Object> toYaml() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.getId());
        map.put("name", this.getName());
        map.put("surname", this.getSurname());
        map.put("patronymic", this.getPatronymic());
        map.put("total_services", this.getServices());
        map.put("phone", this.getPhone());
        map.put("email", this.getEmail());
        map.put("gender", this.getGender());
        return map;
    }
}
