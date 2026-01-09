package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Diagram extends SpecElement{
    private List<Class> classes = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

