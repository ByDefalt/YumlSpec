package exospec.yumlspec;

import exospec.visitor.Visitor;
import exospec.yumlspec.stereotype.Stereotype;

import java.util.ArrayList;
import java.util.List;

public class Class extends SpecElement{
    private String name;
    private Stereotype stereotype;
    private List<Attribute> attributes = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private String backgroundColor;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

