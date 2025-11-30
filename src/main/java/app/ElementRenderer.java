package app;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElementRenderer {

    private static final Set<String> VOID_TAGS = Set.of(
            "meta", "link", "br", "hr", "img", "input"
    );

    private final HtmlWriter writer;

    public ElementRenderer(HtmlWriter writer) {
        this.writer = writer;
    }

    public void renderElement(String tagName, JsonElement element) {
        if (element.isJsonArray()) {
            JsonArray arr = element.getAsJsonArray();
            for (JsonElement child : arr) {
                renderElement(tagName, child);
            }
            return;
        }

        if (element.isJsonPrimitive()) {
            writer.openTag(tagName);
            writer.text(escape(element.getAsJsonPrimitive().getAsString()));
            writer.closeTag(tagName);
            writer.newLine();
            return;
        }

        if (!element.isJsonObject()) {
            writer.openTag(tagName);
            writer.text(escape(element.toString()));
            writer.closeTag(tagName);
            writer.newLine();
            return;
        }

        JsonObject obj = element.getAsJsonObject();

        JsonObject attributesObj = null;
        if (obj.has("attributes") && obj.get("attributes").isJsonObject()) {
            attributesObj = obj.getAsJsonObject("attributes");
        }

        List<Map.Entry<String, JsonElement>> children = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            if ("attributes".equals(entry.getKey())) continue;
            children.add(entry);
        }

        int childCount = children.size();
        boolean hasChildren = childCount > 0;

        String singleChildKey = null;
        JsonElement singleChildElement = null;
        if (childCount == 1) {
            Map.Entry<String, JsonElement> e = children.get(0);
            singleChildKey = e.getKey();
            singleChildElement = e.getValue();
        }

        String attrs = attributesObj != null ? buildAttributes(attributesObj) : null;

        if (!hasChildren && VOID_TAGS.contains(tagName.toLowerCase())) {
            writer.voidTag(tagName, attrs);
            writer.newLine();
            return;
        }

        if (childCount == 1
                && "text".equals(singleChildKey)
                && singleChildElement != null
                && singleChildElement.isJsonPrimitive()) {

            writer.openTag(tagName, attrs);
            writer.text(escape(singleChildElement.getAsJsonPrimitive().getAsString()));
            writer.closeTag(tagName);
            writer.newLine();
            return;
        }

        writer.openTag(tagName, attrs);
        writer.newLine();
        writer.increaseIndent();

        for (Map.Entry<String, JsonElement> entry : children) {
            renderElement(entry.getKey(), entry.getValue());
        }

        writer.decreaseIndent();
        writer.closeTag(tagName);
        writer.newLine();
    }

    public String buildAttributes(JsonObject attributes) {
        StringBuilder sb = new StringBuilder();
        boolean firstAttr = true;

        for (Map.Entry<String, JsonElement> entry : attributes.entrySet()) {
            String name = entry.getKey();
            JsonElement value = entry.getValue();

            if ("style".equalsIgnoreCase(name) && value.isJsonObject()) {
                String css = buildStyle(value.getAsJsonObject());
                if (!css.isEmpty()) {
                    if (!firstAttr) sb.append(" ");
                    firstAttr = false;
                    sb.append("style=\"").append(escape(css)).append("\"");
                }
            } else if (value.isJsonPrimitive()) {
                JsonPrimitive prim = value.getAsJsonPrimitive();

                if (prim.isBoolean()) {
                    if (!prim.getAsBoolean()) {
                        continue;
                    }
                    if (!firstAttr) sb.append(" ");
                    firstAttr = false;
                    sb.append(name);
                    continue;
                }

                if (!firstAttr) sb.append(" ");
                firstAttr = false;
                sb.append(name)
                        .append("=\"")
                        .append(escape(prim.getAsString()))
                        .append("\"");
            } else {
                if (!firstAttr) sb.append(" ");
                firstAttr = false;
                sb.append(name)
                        .append("=\"")
                        .append(escape(value.toString()))
                        .append("\"");
            }
        }
        return sb.toString();
    }

    private String buildStyle(JsonObject styleObj) {
        StringBuilder css = new StringBuilder();
        for (Map.Entry<String, JsonElement> styleEntry : styleObj.entrySet()) {
            css.append(styleEntry.getKey())
                    .append(":")
                    .append(styleEntry.getValue().getAsString())
                    .append(";");
        }
        return css.toString();
    }

    public String escape(String s) {
        if (s == null) return "";
        return s
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
