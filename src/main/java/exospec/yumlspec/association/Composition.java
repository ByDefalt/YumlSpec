package exospec.yumlspec.association;

import exospec.visitor.Visitor;

public class Composition extends RelationType{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
