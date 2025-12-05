package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class BodyRenderer {

    private final HtmlRenderer writer;
    private final ElementRenderer elementRenderer;

    public BodyRenderer(HtmlRenderer writer, ElementRenderer elementRenderer) {
        this.writer = writer;
        this.elementRenderer = elementRenderer;
    }

    public void renderBody(JsonObject body) {
        String bodyAttrs = null;
        if (body.has("attributes") && body.get("attributes").isJsonObject()) {
            bodyAttrs = elementRenderer.buildAttributes(body.getAsJsonObject("attributes"));
        }

        writer.openTag("body", bodyAttrs)
              .newLine()
              .increaseIndent();

        for (Map.Entry<String, JsonElement> entry : body.entrySet()) {
            String key = entry.getKey();
            if ("attributes".equals(key)) {
                continue;
            }
            elementRenderer.renderElement(key, entry.getValue());
        }

        writer.decreaseIndent()
              .closeTag("body")
              .newLine();
    }
}
