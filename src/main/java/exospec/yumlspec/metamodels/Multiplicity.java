package exospec.yumlspec.metamodels;

import exospec.visitor.Visitor;

public class Multiplicity extends SpecElement{
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

