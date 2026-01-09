package exospec;

import exospec.visitor.Visitor;
import exospec.yumlspec.*;
import exospec.yumlspec.Class;
import exospec.yumlspec.association.*;
import exospec.yumlspec.stereotype.AbstractStereotype;
import exospec.yumlspec.stereotype.ClassStereotype;
import exospec.yumlspec.stereotype.InterfaceStereotype;
import exospec.yumlspec.style.Bold;
import exospec.yumlspec.style.Italic;
import exospec.yumlspec.visibility.Private;
import exospec.yumlspec.visibility.Protected;
import exospec.yumlspec.visibility.Public;

import java.beans.VetoableChangeListener;

public class YumlVisitor implements Visitor {
    @Override
    public void visit(Relation relation) {

    }

    @Override
    public void visit(Parameter parameter) {

    }

    @Override
    public void visit(Note note) {

    }

    @Override
    public void visit(Multiplicity multiplicity) {

    }

    @Override
    public void visit(Method method) {

    }

    @Override
    public void visit(Diagram diagram) {

    }

    @Override
    public void visit(Class cls) {

    }

    @Override
    public void visit(Attribute attribute) {

    }

    @Override
    public void visit(Private aPrivate) {

    }

    @Override
    public void visit(Protected protectedVis) {

    }

    @Override
    public void visit(Public publicVis) {

    }

    @Override
    public void visit(Bold bold) {

    }

    @Override
    public void visit(Italic italic) {

    }

    @Override
    public void visit(AbstractStereotype abstractStereotype) {

    }

    @Override
    public void visit(InterfaceStereotype interfaceStereotype) {

    }

    @Override
    public void visit(ClassStereotype classStereotype) {

    }

    @Override
    public void visit(Association association) {

    }

    @Override
    public void visit(Aggregation aggregation) {

    }

    @Override
    public void visit(Composition composition) {

    }

    @Override
    public void visit(Dependency dependency) {

    }

    @Override
    public void visit(Implementation implementation) {

    }

    @Override
    public void visit(Inheritance inheritance) {

    }
}
