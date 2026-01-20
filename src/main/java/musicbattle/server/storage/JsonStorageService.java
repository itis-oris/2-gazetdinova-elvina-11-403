package musicbattle.server.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class JsonStorageService {

    private static final String FILE_NAME = "scores.json";
    private final Gson gson = new Gson();

    public void save(Map<String, Integer> scores) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(scores, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> load() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type type = new TypeToken<Map<String, Integer>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return new HashMap<>(); // если файла нет
        }
    }
}
