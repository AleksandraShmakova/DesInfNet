class ClientShort {
    private Integer id;
    private String surname;
    private String name;
    private String phone;

    public ClientShort() {

    }

    public ClientShort(String surname, String name, String phone) {
        this.setId(null);
        this.setSurname(surname);
        this.setName(name);
        this.setPhone(phone);
    }
    public ClientShort(Integer id, String surname, String name, String phone) {
        this(surname, name, phone);
        this.setId(id);
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
        if (val < 0)
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
            // Проверяем, если символ является цифрой, тогда увеличиваем счетчик цифр
            if (Character.isDigit(c)) {
                digitCount++;
            }
            // Разрешаем также символы пробела, скобок, дефиса и плюса
            else if (c != ' ' && c != '(' && c != ')' && c != '-' && c != '+') {
                return false;
            }
        }

        // Номер телефона должен содержать хотя бы 7 цифр
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
        if (this == o) return true;  // Сравнение ссылок
        if (o == null || getClass() != o.getClass()) return false;  // Проверка на null и тип
        ClientShort clientShort = (ClientShort) o;  // Приведение типов
        return phone == clientShort.phone;
    }

}
