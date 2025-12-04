package app;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String inputFileName = args.length > 0 ? args[0] : "pageNotFound.json";
        String outputFileName = args.length > 1 ? args[1] : "output.html";

        JsonDocumentLoaderFactory loaderFactory = new JsonDocumentLoaderFactory();
        JsonDocumentLoader loader = loaderFactory.createLoader(inputFileName);
        HtmlGenerator htmlGenerator = new HtmlGenerator();

        try {
            JsonElement root = loader.load(inputFileName);

            String html = htmlGenerator.generate(root);

            Path outputPath = Path.of(outputFileName);
            Files.writeString(outputPath, html);

            logger.info("Generirana datoteka: {}", outputPath.toAbsolutePath());

        } catch (IOException e) {
            logger.error("Napaka pri branju JSON datoteke: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Napaka pri generiranju HTML: {}", e.getMessage(), e);
        }
    }
}
