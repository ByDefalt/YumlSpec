package exospec.yumlspec.metamodels;

import exospec.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Diagram extends SpecElement{
    private List<Class> classes = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();
    private List<Note> notes = new ArrayList<>();

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

