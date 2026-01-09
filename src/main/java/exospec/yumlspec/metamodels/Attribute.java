package exospec.yumlspec.metamodels;

import exospec.visitor.Visitor;
import exospec.yumlspec.metamodels.style.Style;
import exospec.yumlspec.metamodels.visibility.Visibility;

public class Attribute extends SpecElement{
    private Visibility visibility;
    private String name;
    private Style style;

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
