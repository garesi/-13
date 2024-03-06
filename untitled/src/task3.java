import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class task3 {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        try {
            int userId = 1; // ID користувача для завдання 3
            printOpenTodosForUser(userId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printOpenTodosForUser(int userId) throws IOException {
        String endpoint = BASE_URL + "/users/" + userId + "/todos";
        String response = sendGetRequest(endpoint);
        String[] todos = response.split("\"completed\":");
        System.out.println("Open todos for user " + userId + ":");
        for (String todo : todos) {
            if (todo.contains("false")) {
                String title = todo.split("\"title\":")[1].split(",")[0];
                System.out.println(title);
            }
        }
    }

    private static String sendGetRequest(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            return response.toString();
        } else {
            throw new IOException("Error response code: " + responseCode);
        }
    }
}
