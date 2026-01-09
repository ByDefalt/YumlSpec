package exospec.yumlspec.metamodels.association.style;

import exospec.yumlspec.visitor.Visitor;

public class Italic extends Style {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
