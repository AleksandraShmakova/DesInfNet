class ClientShort {
    private Integer id;
    private String surname;
    private String name;
    private String phone;

    public ClientShort() { }

    public ClientShort(String surname, String name, String phone) {
        this.setId(null);
        this.setSurname(surname);
        this.setName(name);
        this.setPhone(phone);
    }
    public ClientShort(Integer id, String surname, String name, String phone) {
        this.setId(id);
        this.setSurname(surname);
        this.setName(name);
        this.setPhone(phone);
    }

    public Integer getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {return phone;}

    public void setId(Integer id) {
        if (id == null || validateI(id)==true)
            this.id = id;
        else
            throw new IllegalArgumentException("ID должен быть положительным числом.");
    }

    public void setSurname(String surname) {
        if (validateS(surname)==true)
            this.surname = surname;
        else
            throw new IllegalArgumentException("Неверный формат фамилии.");
    }

    public void setName(String name) {
        if (validateS(name)==true)
            this.name = name;
        else
            throw new IllegalArgumentException("Неверный формат имени.");
    }

    public void setPhone(String phone) {
        if (validatePhone(phone)==true)
            this.phone = phone;
        else
            throw new IllegalArgumentException("Неверный формат телефона.");
    }

    public static boolean validateS(String value) {
        if (value == null || value.trim().isEmpty() || !value.matches("^[A-Za-zА-Яа-яЁё\\s]+$"))
            return false;
        else
            return true;
    }

    public static boolean validateI(int val) {
        if (val <= 0)
            return false;
        else
            return true;
    }

    public static boolean validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }

        int digitCount = 0;
        for (char c : phone.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
            else if (c != ' ' && c != '(' && c != ')' && c != '-' && c != '+') {
                return false;
            }
        }
        return digitCount >= 7 && digitCount <= 15;
    }

    public String getInitial() {
        return getName().charAt(0) + ".";
    }

    @Override
    public String toString() {
        return "ClientShort{" +
                "surname='" + getSurname() + '\'' +
                ", initials='" + getInitial() + '\'' +
                ", phone=" + getPhone() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;  // Проверка на null и тип
        ClientShort clientShort = (ClientShort) o;  // Приведение типов
        return phone == clientShort.phone;
    }

}

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
        if (this == o) return true;  // Сравнение ссылок
        if (o == null || getClass() != o.getClass()) return false;  // Проверка на null и тип
        Client client = (Client) o;  // Приведение типов
        return getPhone() == client.getPhone();
    }
}
