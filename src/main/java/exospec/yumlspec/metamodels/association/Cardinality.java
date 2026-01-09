package exospec.yumlspec.metamodels.association;

import exospec.visitor.Visitor;

public class Cardinality extends RelationType{
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
