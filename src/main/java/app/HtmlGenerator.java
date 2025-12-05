package app;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class HtmlGenerator {

    public String generate(JsonElement rootElement) {
        JsonObject root = requireObject(rootElement, "root");

        String doctype = getString(root, "doctype", "html");
        String language = getString(root, "language", "en");

        HtmlRenderer writer = new HtmlRenderer();
        ElementRenderer elementRenderer = new ElementRenderer(writer);
        HeadRenderer headRenderer = new HeadRenderer(writer, elementRenderer);
        BodyRenderer bodyRenderer = new BodyRenderer(writer, elementRenderer);

        String htmlAttrs = "lang=\"" + elementRenderer.escape(language) + "\"";
        writer.text("<!DOCTYPE " + elementRenderer.escape(doctype) + ">")
              .newLine()
              .openTag("html", htmlAttrs)
              .newLine()
              .increaseIndent();

        if (root.has("head")) {
            headRenderer.renderHead(requireObject(root.get("head"), "head"));
        }

        if (root.has("body")) {
            bodyRenderer.renderBody(requireObject(root.get("body"), "body"));
        } else {
            throw new IllegalArgumentException("Koren JSON dokumenta mora imeti objekt 'body'.");
        }

        writer.decreaseIndent()
              .closeTag("html")
              .newLine();

        return writer.getContent();
    }

    private JsonObject requireObject(JsonElement element, String name) {
        if (element == null || !element.isJsonObject()) {
            throw new IllegalArgumentException("'" + name + "' mora biti JSON objekt.");
        }
        return element.getAsJsonObject();
    }

    private String getString(JsonObject obj, String key, String defaultValue) {
        if (!obj.has(key)) return defaultValue;
        JsonElement element = obj.get(key);
        if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("Polje '" + key + "' mora biti niz.");
        }
        return element.getAsString();
    }
}
