package exospec.visitor;

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

public interface Visitor {
    void visit(Relation relation);
    void visit(Parameter parameter);
    void visit(Note note);
    void visit(Multiplicity multiplicity);
    void visit(Method method);
    void visit(Diagram diagram);
    void visit(Class cls);
    void visit(Attribute attribute);
    void visit(Private aPrivate);
    void visit(Protected protectedVis);
    void visit(Public publicVis);
    void visit(Bold bold);
    void visit(Italic italic);
    void visit(AbstractStereotype abstractStereotype);
    void visit(InterfaceStereotype interfaceStereotype);
    void visit(ClassStereotype classStereotype);
    void visit(Association association);
    void visit(Aggregation aggregation);
    void visit(Composition composition);
    void visit(Dependency dependency);
    void visit(Implementation implementation);
    void visit(Inheritance inheritance);

}
