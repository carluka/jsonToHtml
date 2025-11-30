package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ResourceJsonDocumentLoader implements JsonDocumentLoader {

    @Override
    public JsonElement load(String resourceName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();

        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = classLoader.getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IOException("Resource '" + resourceName + "' ni bil najden.");
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }
        }

        String jsonText = sb.toString();

        return JsonParser.parseString(jsonText);
    }
}
