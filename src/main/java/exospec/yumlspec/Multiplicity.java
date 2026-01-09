package exospec.yumlspec;

import exospec.visitor.Visitor;

public class Multiplicity extends SpecElement{
    private String value;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

