package exospec.yumlspec;

import exospec.visitor.Visitor;

public class Note extends SpecElement{
    private String text;
    private String backgroundColor;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

