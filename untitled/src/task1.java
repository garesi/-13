import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class task1 {
    private static final String API_URL = "https://jsonplaceholder.typicode.com/users";

    public static void main(String[] args) throws IOException {
        System.out.println(getUserById(1));
        System.out.println(getUserByUsername("Bret"));
        System.out.println(createUser("{\"name\": \"Test User\", \"username\": \"testuser\", \"email\": \"testuser@example.com\"}"));
        System.out.println(updateUser(1, "{\"name\": \"Updated User\", \"username\": \"updateduser\", \"email\": \"updateduser@example.com\"}"));
        System.out.println(deleteUser(1));
    }

    private static String getUserById(int id) throws IOException {
        return sendHttpRequest(API_URL + "/" + id, "GET", null);
    }

    private static String getUserByUsername(String username) throws IOException {
        return sendHttpRequest(API_URL + "?username=" + username, "GET", null);
    }

    private static String createUser(String jsonInputString) throws IOException {
        return sendHttpRequest(API_URL, "POST", jsonInputString);
    }

    private static String updateUser(int id, String jsonInputString) throws IOException {
        return sendHttpRequest(API_URL + "/" + id, "PUT", jsonInputString);
    }

    private static String deleteUser(int id) throws IOException {
        return sendHttpRequest(API_URL + "/" + id, "DELETE", null);
    }

    private static String sendHttpRequest(String urlString, String method, String jsonInputString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        if (jsonInputString != null) {
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}
