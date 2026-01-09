package exospec.yumlspec;

import exospec.visitor.Visitor;

public class Parameter extends SpecElement{
    private String name;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

