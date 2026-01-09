package exospec.yumlspec.metamodels.association;

import exospec.visitor.Visitor;

public class Implementation extends RelationType{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
