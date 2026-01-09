package exospec.yumlspec.metamodels.association.style;

import exospec.yumlspec.visitor.Visitor;

public class Bold extends Style{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
