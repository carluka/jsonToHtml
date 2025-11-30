package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileJsonDocumentLoader implements JsonDocumentLoader {

    @Override
    public JsonElement load(String resourceName) throws IOException {
        Path path = Path.of(resourceName);
        if (!Files.exists(path)) {
            throw new IOException("Datoteka '" + resourceName + "' ne obstaja.");
        }
        if (!Files.isRegularFile(path)) {
            throw new IOException("Pot '" + resourceName + "' ni datoteka.");
        }

        String jsonText = Files.readString(path, StandardCharsets.UTF_8);
        return JsonParser.parseString(jsonText);
    }
}
