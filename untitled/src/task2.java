import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class task2 {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) {
        try {
            int userId = 1; // ID користувача для завдання 2
            int postId = getLatestPostId(userId); // Отримуємо ID останнього поста користувача
            if (postId != -1) {
                String comments = getCommentsForPost(postId); // Отримуємо коментарі до останнього поста
                writeCommentsToFile(comments, userId, postId); // Записуємо коментарі в файл
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getLatestPostId(int userId) throws IOException {
        String endpoint = BASE_URL + "/users/" + userId + "/posts";
        String response = sendGetRequest(endpoint);
        String[] posts = response.split("\"id\":");
        if (posts.length > 1) {
            // Видаляємо пробіли та перетворюємо у ціле число
            return Integer.parseInt(posts[posts.length - 1].split(",")[0].trim());
        }
        return -1;
    }


    private static String getCommentsForPost(int postId) throws IOException {
        String endpoint = BASE_URL + "/posts/" + postId + "/comments";
        return sendGetRequest(endpoint);
    }

    private static void writeCommentsToFile(String comments, int userId, int postId) {
        String filename = "user-" + userId + "-post-" + postId + "-comments.json";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(comments);
            System.out.println("Comments written to file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
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
