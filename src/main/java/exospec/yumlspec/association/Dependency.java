package exospec.yumlspec.association;

import exospec.visitor.Visitor;

public class Dependency extends RelationType{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
