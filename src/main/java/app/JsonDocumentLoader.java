package app;

import com.google.gson.JsonElement;

import java.io.IOException;

public interface JsonDocumentLoader {
    JsonElement load(String resourceName) throws IOException;
}
