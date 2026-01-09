package exospec.yumlspec.metamodels.visibility;

import exospec.visitor.Visitor;

public class Public extends Visibility{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
