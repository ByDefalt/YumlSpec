package exospec.yumlspec.metamodels;

import exospec.visitor.Visitor;

public class Parameter extends SpecElement{
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

