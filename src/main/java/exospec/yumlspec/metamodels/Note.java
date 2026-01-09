package exospec.yumlspec.metamodels;

import exospec.visitor.Visitor;

public class Note extends SpecElement{
    private String text;
    private String backgroundColor;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

