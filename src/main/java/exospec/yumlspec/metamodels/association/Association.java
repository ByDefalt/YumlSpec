package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;

public class Association extends RelationType{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
