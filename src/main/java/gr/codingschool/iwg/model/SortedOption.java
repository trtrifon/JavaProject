package gr.codingschool.iwg.model;

/**
 * 
 */
public class SortedOption {
    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected(String lastSelected) {
        return lastSelected != null && lastSelected.equals(this.value);
    }
}
