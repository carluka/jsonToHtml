package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class HeadRenderer {

    private final HtmlWriter writer;
    private final ElementRenderer elementRenderer;

    public HeadRenderer(HtmlWriter writer, ElementRenderer elementRenderer) {
        this.writer = writer;
        this.elementRenderer = elementRenderer;
    }

    public void renderHead(JsonObject head) {
        writer.openTag("head");
        writer.newLine();
        writer.increaseIndent();

        if (head.has("meta") && head.get("meta").isJsonObject()) {
            JsonObject meta = head.getAsJsonObject("meta");
            for (Map.Entry<String, JsonElement> entry : meta.entrySet()) {
                String name = entry.getKey();
                JsonElement value = entry.getValue();

                if ("charset".equalsIgnoreCase(name) && value.isJsonPrimitive()) {
                    String attrs = "charset=\"" + elementRenderer.escape(value.getAsString()) + "\"";
                    writer.voidTag("meta", attrs);
                    writer.newLine();
                } else {
                    String content;
                    if (value.isJsonPrimitive()) {
                        content = value.getAsString();
                    } else if (value.isJsonObject()) {
                        StringBuilder contentBuilder = new StringBuilder();
                        JsonObject obj = value.getAsJsonObject();
                        boolean first = true;
                        for (Map.Entry<String, JsonElement> e2 : obj.entrySet()) {
                            if (!first) {
                                contentBuilder.append(", ");
                            }
                            first = false;
                            contentBuilder
                                    .append(e2.getKey())
                                    .append("=")
                                    .append(e2.getValue().getAsString());
                        }
                        content = contentBuilder.toString();
                    } else {
                        content = value.toString();
                    }

                    String attrs =
                            "name=\"" + elementRenderer.escape(name) + "\" content=\"" +
                                    elementRenderer.escape(content) + "\"";
                    writer.voidTag("meta", attrs);
                    writer.newLine();
                }
            }
        }

        if (head.has("link")) {
            JsonElement linkElement = head.get("link");
            if (linkElement.isJsonArray()) {
                JsonArray array = linkElement.getAsJsonArray();
                for (JsonElement el : array) {
                    if (el.isJsonObject()) {
                        String attrs = elementRenderer.buildAttributes(el.getAsJsonObject());
                        writer.voidTag("link", attrs);
                        writer.newLine();
                    }
                }
            } else if (linkElement.isJsonObject()) {
                String attrs = elementRenderer.buildAttributes(linkElement.getAsJsonObject());
                writer.voidTag("link", attrs);
                writer.newLine();
            }
        }

        if (head.has("title")) {
            JsonElement title = head.get("title");
            writer.openTag("title");
            if (title.isJsonPrimitive()) {
                writer.text(elementRenderer.escape(title.getAsString()));
            } else {
                writer.text(elementRenderer.escape(title.toString()));
            }
            writer.closeTag("title");
            writer.newLine();
        }

        writer.decreaseIndent();
        writer.closeTag("head");
        writer.newLine();
    }
}
