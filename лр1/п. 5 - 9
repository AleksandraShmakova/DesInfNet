class ClientShort {
    private int id;
    private String surname;
    private String name;

    public ClientShort() {

    }
    public ClientShort(int id, String surname, String name) {
        this.setId(id);
        this.setSurname(surname);
        this.setName(name);
    }

    public int getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = validateI(id, "ID должен быть положительным числом.");
    }

    public void setSurname(String surname) {
        this.surname = validateS(surname, "Фамилия не может быть пустой.");
    }

    public void setName(String name) {
        this.name = validateS(name, "Имя не может быть пустым.");
    }

    public static String validateS(String value, String ex) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(ex);
        }
        else
            return value;
    }

    public static int validateI(int val, String ex) {
        if (val <= 0) {
            throw new IllegalArgumentException(ex);
        }
        else
            return val;
    }

    public String getInitial() {
        return getName().charAt(0) + ".";
    }

    @Override
    public String toString() {
        return "ClientShort{" +
                "surname='" + getSurname() + '\'' +
                ", initials='" + getInitial() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;  // Сравнение ссылок
        if (o == null || getClass() != o.getClass()) return false;  // Проверка на null и тип
        ClientShort clientShort = (ClientShort) o;  // Приведение типов
        return id == clientShort.id &&
                name.equals(clientShort.name) &&
                surname.equals(clientShort.surname);
    }

}

class Client extends ClientShort {
    private String patronymic;
    private int total_services;

    // Основной конструктор
    public Client(int id, String surname, String name, String patronymic, int total_services) {
        super(id, surname, name);
        this.patronymic = validateS(patronymic, "Отчество не может быть пустым.");
        this.total_services = validateI(total_services, "Количество услуг не может быть отрицательным.");
    }

    // Перегруженный конструктор, принимающий строку
    public Client(String clientData) {
        // Ожидаемый формат строки: "id,surname,name,patronymic,total_services"
        String[] data = clientData.split(",");
        if (data.length != 5) {
            throw new IllegalArgumentException("Неверный формат данных. Ожидается 5 значений.");
        }

        // Вызов основного конструктора с разобранными значениями
        this.setId(Integer.parseInt(data[0].trim()));
        this.setSurname(data[1].trim());
        this.setName(data[2].trim());
        this.setPatronymic(data[3].trim());
        this.setServices(Integer.parseInt(data[4].trim()));
    }

    // Геттеры
    public String getPatronymic() {
        return patronymic;
    }

    public int getServices() {
        return total_services;
    }

    // Сеттеры
    public void setPatronymic(String patronymic) {
        this.patronymic = validateS(patronymic, "Отчество не может быть пустым.");
    }

    public void setServices(int total_services) {
        this.total_services = validateI(total_services, "Количество услуг не может быть отрицательным.");
    }

    @Override
    public String toString() {
        return "Client {" +
                "id=" + getId() +
                ", surname='" + getSurname() + '\'' +
                ", name='" + getName() + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", total_services=" + total_services +
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
        return getId() == client.getId() &&
                total_services == client.total_services &&
                getName().equals(client.getName()) &&
                getSurname().equals(client.getSurname()) &&
                patronymic.equals(client.getPatronymic());
    }
}
