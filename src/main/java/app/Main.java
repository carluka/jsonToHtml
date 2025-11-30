package app;

import com.google.gson.JsonElement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        String inputFileName = args.length > 0 ? args[0] : "test2.json";
        String outputFileName = args.length > 1 ? args[1] : "output.html";

        JsonDocumentLoader loader = selectLoader(inputFileName);
        HtmlGenerator htmlGenerator = new HtmlGenerator();

        try {
            JsonElement root = loader.load(inputFileName);

            String html = htmlGenerator.generate(root);

            Path outputPath = Path.of(outputFileName);
            Files.writeString(outputPath, html);

            System.out.println("Generirana datoteka: " + outputPath.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Napaka pri branju JSON datoteke: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Napaka pri generiranju HTML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static JsonDocumentLoader selectLoader(String inputFileName) {
        Path candidate = Path.of(inputFileName);
        if (Files.exists(candidate) && Files.isRegularFile(candidate)) {
            return new FileJsonDocumentLoader();
        }
        return new ResourceJsonDocumentLoader();
    }
}
