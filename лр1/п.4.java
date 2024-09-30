class Client {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private int total_survices;

    public Client(int id, String name, String surname, String patronymic, int total_survices) {
        this.setId(id);
        this.setName(name);
        this.setSurname(surname);
        this.setPatronymic(patronymic);
        this.setSurvices(total_survices);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }
    
    public int getSurvices() {
        return total_survices;
    }
    public void setId(int id) {
        this.id = validateId(id);
    }
    
    public void setName(String name) {
        this.name = validateName(name);
    }

    public void setSurname(String surname) {
        this.surname = validateSurname(surname);
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = validatePatronymic(patronymic);
    }

    public void setSurvices(int total_survices) {
        this.total_survices = validateSurvices(total_survices);
    }

    public static int validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным числом.");
        }
        else
            return id;
    }

    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым.");
        }
        else
            return name;
    }

    public static String validateSurname(String surname) {
        if (surname == null || surname.trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия не может быть пустой.");
        }
        else
            return surname;
    }

    public static String validatePatronymic(String patronymic) {
        if (patronymic == null || patronymic.trim().isEmpty()) {
            throw new IllegalArgumentException("Отчество не может быть пустым.");
        }
        else
            return patronymic;
    }

    public static int validateSurvices(int total_survices) {
        if (total_survices < 0) {
            throw new IllegalArgumentException("Количество услуг не может быть отрицательным.");
        }
        else
            return total_survices;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", total_services='" + total_survices + '\'' +
                '}';
    }
}
