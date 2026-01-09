package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;
import exospec.yumlspec.metamodels.association.style.Style;
import exospec.yumlspec.metamodels.association.visibility.Visibility;

public class Attribute extends SpecElement{
    private Visibility visibility;
    private String name;
    private Style style;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
