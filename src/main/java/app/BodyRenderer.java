package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class BodyRenderer {

    private final HtmlWriter writer;
    private final ElementRenderer elementRenderer;

    public BodyRenderer(HtmlWriter writer, ElementRenderer elementRenderer) {
        this.writer = writer;
        this.elementRenderer = elementRenderer;
    }

    public void renderBody(JsonObject body) {
        String bodyAttrs = null;
        if (body.has("attributes") && body.get("attributes").isJsonObject()) {
            bodyAttrs = elementRenderer.buildAttributes(body.getAsJsonObject("attributes"));
        }

        writer.openTag("body", bodyAttrs);
        writer.newLine();
        writer.increaseIndent();

        for (Map.Entry<String, JsonElement> entry : body.entrySet()) {
            String key = entry.getKey();
            if ("attributes".equals(key)) {
                continue;
            }
            elementRenderer.renderElement(key, entry.getValue());
        }

        writer.decreaseIndent();
        writer.closeTag("body");
        writer.newLine();
    }
}
