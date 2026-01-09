package exospec.yumlspec.association;

import exospec.visitor.Visitor;

public class Association extends RelationType{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
