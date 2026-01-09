package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;
import exospec.yumlspec.metamodels.association.visibility.Visibility;

import java.util.ArrayList;
import java.util.List;

public class Method extends SpecElement{
    private Visibility visibility;
    private String name;
    private List<Parameter> parameters = new ArrayList<>();

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

