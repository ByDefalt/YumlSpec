package exospec.yumlspec.metamodels;

import exospec.visitor.Visitor;
import exospec.yumlspec.metamodels.stereotype.Stereotype;

import java.util.ArrayList;
import java.util.List;

public class Class extends SpecElement{
    private String name;
    private Stereotype stereotype;
    private List<Attribute> attributes = new ArrayList<>();
    private List<Method> methods = new ArrayList<>();
    private String backgroundColor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Stereotype getStereotype() {
        return stereotype;
    }

    public void setStereotype(Stereotype stereotype) {
        this.stereotype = stereotype;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

