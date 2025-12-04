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

    public void increaseIndent() {
        indentLevel++;
    }

    public void decreaseIndent() {
        if (indentLevel > 0) {
            indentLevel--;
        }
    }

    public void newLine() {
        sb.append("\n");
        newLine = true;
    }

    public void text(String text) {
        writeIndentIfNeeded();
        sb.append(text);
    }

    public void openTag(String name) {
        openTag(name, null);
    }

    public void openTag(String name, String attributes) {
        writeIndentIfNeeded();
        sb.append("<").append(name);
        if (attributes != null && !attributes.isEmpty()) {
            sb.append(" ").append(attributes);
        }
        sb.append(">");
    }

    public void closeTag(String name) {
        writeIndentIfNeeded();
        sb.append("</").append(name).append(">");
    }

    public void voidTag(String name, String attributes) {
        openTag(name, attributes);
    }

    public String getContent() {
        return sb.toString();
    }
}
