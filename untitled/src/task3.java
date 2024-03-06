import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Task3 {

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

        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> todos = gson.fromJson(response, type);

        System.out.println("Open todos for user " + userId + ":");
        for (Map<String, Object> todo : todos) {
            boolean completed = (boolean) todo.get("completed");
            if (!completed) {
                String title = (String) todo.get("title");
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
            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                int data;
                while ((data = reader.read()) != -1) {
                    response.append((char) data);
                }
            }
            return response.toString();
        } else {
            throw new IOException("Error response code: " + responseCode);
        }
    }
}
