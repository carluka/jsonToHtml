package app;

import java.nio.file.Files;
import java.nio.file.Path;

public class JsonDocumentLoaderFactory {
    
    public JsonDocumentLoader createLoader(String resourceName) {
        Path candidate = Path.of(resourceName);
        if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
            return new FileJsonDocumentLoader();
        }
        return new ResourceJsonDocumentLoader();
    }
}
