package exospec.yumlspec.metamodels;

import exospec.visitor.Visitor;
import exospec.yumlspec.metamodels.visibility.Visibility;

import java.util.ArrayList;
import java.util.List;

public class Method extends SpecElement{
    private Visibility visibility;
    private String name;
    private List<Parameter> parameters = new ArrayList<>();

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

