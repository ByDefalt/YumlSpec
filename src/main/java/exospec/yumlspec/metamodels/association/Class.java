package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;
import exospec.yumlspec.metamodels.association.stereotype.Stereotype;

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

