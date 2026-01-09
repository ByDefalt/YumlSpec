package exospec.yumlspec.metamodels.style;

import exospec.visitor.Visitor;

public class Italic extends Style {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
