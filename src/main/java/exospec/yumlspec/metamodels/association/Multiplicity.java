package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;

public class Multiplicity extends SpecElement{
    private String value;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

