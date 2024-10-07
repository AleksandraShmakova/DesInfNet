class Client extends ClientShort {
    private String patronymic;
    private Integer total_services;
    private String gender;
    private String email;


    public Client(String surname, String name, String patronymic, String phone, String email, String gender) {
        super(surname,name,phone);
        this.setPatronymic(patronymic);
        this.setEmail(email);
        this.setGender(gender);
    }
    public Client(Integer id, String surname, String name, String patronymic, Integer total_services, String phone, String email, String gender) {
        super(id, surname, name, phone);
        this.setPatronymic(patronymic);
        this.setServices(total_services);
        this.setEmail(email);
        this.setGender(gender);
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

    public int getServices() {
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

        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex != email.lastIndexOf('@')) {
            return false;
        }

        String domain = email.substring(atIndex + 1);
        if (domain.isEmpty() || !domain.contains(".")) {
            return false;
        }

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
        if (this == o) return true; 
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o; 
        return getPhone() == client.getPhone();
    }

    public static Client fromJson(JSONObject jsonObject) throws Exception {
        return new Client(
                jsonObject.optInt("id"),
                jsonObject.getString("name"),
                jsonObject.getString("surname"),
                jsonObject.getString("patronymic"),
                jsonObject.optInt("total_services"),
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
}
