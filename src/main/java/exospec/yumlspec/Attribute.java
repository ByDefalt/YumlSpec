package exospec.yumlspec;

import exospec.visitor.Visitor;
import exospec.yumlspec.style.Style;
import exospec.yumlspec.visibility.Visibility;

public class Attribute extends SpecElement{
    private Visibility visibility;
    private String name;
    private Style style;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
