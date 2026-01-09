package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;

public class Parameter extends SpecElement{
    private String name;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

