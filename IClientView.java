import java.util.List;

interface IClientView {
    String displayClientList(List<Client> clients, int currentPage, int recordsPerPage);
    String generateAddClientPage();
    String displayClientDetails(Client client);
    void showErrorMessage(String message);
    void showSuccessMessage(String message);
}
