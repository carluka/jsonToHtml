package app;

public class HtmlRenderer {

    private final StringBuilder sb = new StringBuilder();
    private int indentLevel = 0;
    private final String indentUnit = "    ";
    private boolean newLine = true;

    private void writeIndentIfNeeded() {
        if (newLine) {
            for (int i = 0; i < indentLevel; i++) {
                sb.append(indentUnit);
            }
            newLine = false;
        }
    }

    public HtmlRenderer increaseIndent() {
        indentLevel++;
        return this;
    }

    public HtmlRenderer decreaseIndent() {
        if (indentLevel > 0) {
            indentLevel--;
        }
        return this;
    }

    public HtmlRenderer newLine() {
        sb.append("\n");
        newLine = true;
        return this;
    }

    public HtmlRenderer text(String text) {
        writeIndentIfNeeded();
        sb.append(text);
        return this;
    }

    public HtmlRenderer openTag(String name) {
        openTag(name, null);
        return this;
    }

    public HtmlRenderer openTag(String name, String attributes) {
        writeIndentIfNeeded();
        sb.append("<").append(name);
        if (attributes != null && !attributes.isEmpty()) {
            sb.append(" ").append(attributes);
        }
        sb.append(">");
        return this;
    }

    public HtmlRenderer closeTag(String name) {
        writeIndentIfNeeded();
        sb.append("</").append(name).append(">");
        return this;
    }

    public HtmlRenderer voidTag(String name, String attributes) {
        openTag(name, attributes);
        return this;
    }

    public String getContent() {
        return sb.toString();
    }
}
