package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;

public class Inheritance extends RelationType{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
