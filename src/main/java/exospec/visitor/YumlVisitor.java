package exospec.visitor;

import exospec.yumlspec.metamodels.Class;
import exospec.yumlspec.metamodels.association.*;
import exospec.yumlspec.metamodels.stereotype.*;
import exospec.yumlspec.metamodels.*;
import exospec.yumlspec.metamodels.visibility.*;
import exospec.yumlspec.metamodels.style.*;

import java.util.ArrayList;
import java.util.List;

/**
 * YumlVisitor implementation that respects the Open/Closed Principle.
 * No switch statements or if/else chains - only polymorphism through the visitor pattern.
 */
public class YumlVisitor implements Visitor {

    private StringBuilder yumlOutput = new StringBuilder();
    private List<String> elements = new ArrayList<>();

    private String lastVisibilitySymbol = "";
    private String lastStylePrefix = "";
    private String lastStyleSuffix = "";
    private String lastStereotype = "";
    private String lastMultiplicity = "";
    private String lastRelationSymbol = "";

    public String getYumlOutput() {
        return String.join(", ", elements);
    }

    public String getYumlUrl(String style) {
        if (style == null || style.isEmpty()) {
            style = "scruffy";
        }
        return "http://yuml.me/diagram/" + style + "/class/" + getYumlOutput();
    }

    @Override
    public void visit(Diagram diagram) {
        elements.clear();
        yumlOutput = new StringBuilder();

        // Visit all notes first
        if (diagram.getNotes() != null) {
            for (Note note : diagram.getNotes()) {
                note.accept(this);
            }
        }

        // Visit all classes
        if (diagram.getClasses() != null) {
            for (Class cls : diagram.getClasses()) {
                cls.accept(this);
            }
        }

        // Visit all relations
        if (diagram.getRelations() != null) {
            for (Relation relation : diagram.getRelations()) {
                relation.accept(this);
            }
        }
    }

    @Override
    public void visit(Class cls) {
        StringBuilder classBuilder = new StringBuilder();
        classBuilder.append("[");

        // Add stereotype if present
        if (cls.getStereotype() != null) {
            cls.getStereotype().accept(this);
            if (!lastStereotype.isEmpty()) {
                classBuilder.append(lastStereotype);
                if (cls.getName() != null && !cls.getName().isEmpty()) {
                    classBuilder.append(";");
                }
            }
            lastStereotype = "";
        }

        // Add class name
        if (cls.getName() != null && !cls.getName().isEmpty()) {
            classBuilder.append(cls.getName());
        }

        // Add attributes section
        if (cls.getAttributes() != null && !cls.getAttributes().isEmpty()) {
            classBuilder.append("|");
            List<String> attrs = new ArrayList<>();
            for (Attribute attr : cls.getAttributes()) {
                attr.accept(this);
                attrs.add(getLastAttributeOutput());
            }
            classBuilder.append(String.join(";", attrs));
        }

        // Add methods section
        if (cls.getMethods() != null && !cls.getMethods().isEmpty()) {
            classBuilder.append("|");
            List<String> methods = new ArrayList<>();
            for (Method method : cls.getMethods()) {
                method.accept(this);
                methods.add(getLastMethodOutput());
            }
            classBuilder.append(String.join(";", methods));
        }

        // Add background color if present
        if (cls.getBackgroundColor() != null && !cls.getBackgroundColor().isEmpty()) {
            classBuilder.append("{bg:").append(cls.getBackgroundColor()).append("}");
        }

        classBuilder.append("]");
        elements.add(classBuilder.toString());
    }

    private String getLastAttributeOutput() {
        StringBuilder result = new StringBuilder();
        result.append(lastVisibilitySymbol);
        result.append(lastStylePrefix);
        // The name is stored temporarily - we'll get it from context
        result.append(lastStyleSuffix);

        String output = result.toString();
        // Reset context
        lastVisibilitySymbol = "";
        lastStylePrefix = "";
        lastStyleSuffix = "";
        return output;
    }

    private String getLastMethodOutput() {
        String output = lastVisibilitySymbol;
        lastVisibilitySymbol = "";
        return output;
    }

    @Override
    public void visit(Attribute attribute) {
        // Visit visibility
        if (attribute.getVisibility() != null) {
            attribute.getVisibility().accept(this);
        }

        // Visit style and build styled name
        if (attribute.getStyle() != null) {
            attribute.getStyle().accept(this);
        }

        // Add the name with style wrappers
        if (attribute.getName() != null && !attribute.getName().isEmpty()) {
            lastStyleSuffix = lastStylePrefix + attribute.getName() + lastStyleSuffix;
            lastStylePrefix = "";
        }
    }

    @Override
    public void visit(Method method) {
        StringBuilder methodBuilder = new StringBuilder();

        // Visit visibility
        if (method.getVisibility() != null) {
            method.getVisibility().accept(this);
            methodBuilder.append(lastVisibilitySymbol);
            lastVisibilitySymbol = "";
        }

        // Add method name
        if (method.getName() != null && !method.getName().isEmpty()) {
            methodBuilder.append(method.getName());
        }

        // Add parameters
        methodBuilder.append("(");
        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
            List<String> params = new ArrayList<>();
            for (Parameter param : method.getParameters()) {
                if (param.getName() != null) {
                    params.add(param.getName());
                }
            }
            methodBuilder.append(String.join(",", params));
        }
        methodBuilder.append(")");

        // Store the complete method string
        lastVisibilitySymbol = methodBuilder.toString();
    }

    @Override
    public void visit(Parameter parameter) {
        // Parameters are handled directly in Method visitor
    }

    @Override
    public void visit(Relation relation) {
        StringBuilder relationBuilder = new StringBuilder();

        // Build source
        if (relation.getSource() != null) {
            relationBuilder.append("[").append(relation.getSource().getName()).append("]");
        }

        // Add source multiplicity
        if (relation.getSourceMultiplicity() != null) {
            relation.getSourceMultiplicity().accept(this);
            relationBuilder.append(lastMultiplicity);
            lastMultiplicity = "";
        }

        // Visit relation type to get the symbol
        if (relation.getType() != null) {
            relation.getType().accept(this);
            relationBuilder.append(lastRelationSymbol);
            lastRelationSymbol = "";
        }

        // Add role if present
        if (relation.getRole() != null && !relation.getRole().isEmpty()) {
            relationBuilder.append(relation.getRole());
        }

        // Add target multiplicity
        if (relation.getTargetMultiplicity() != null) {
            relation.getTargetMultiplicity().accept(this);
            relationBuilder.append(lastMultiplicity);
            lastMultiplicity = "";
        }

        // Build target
        if (relation.getTarget() != null) {
            relationBuilder.append("[").append(relation.getTarget().getName()).append("]");
        }

        elements.add(relationBuilder.toString());
    }

    @Override
    public void visit(Multiplicity multiplicity) {
        if (multiplicity.getValue() != null && !multiplicity.getValue().isEmpty()) {
            lastMultiplicity = multiplicity.getValue();
        }
    }

    @Override
    public void visit(Note note) {
        StringBuilder noteBuilder = new StringBuilder();
        noteBuilder.append("[note: ");

        if (note.getText() != null && !note.getText().isEmpty()) {
            noteBuilder.append(note.getText());
        }

        if (note.getBackgroundColor() != null && !note.getBackgroundColor().isEmpty()) {
            noteBuilder.append("{bg:").append(note.getBackgroundColor()).append("}");
        }

        noteBuilder.append("]");
        elements.add(noteBuilder.toString());
    }

    // ==================== Visibility Visitors ====================
    // Each visibility knows its own yUML symbol

    @Override
    public void visit(Private aPrivate) {
        lastVisibilitySymbol = "-";
    }

    @Override
    public void visit(Protected protectedVis) {
        lastVisibilitySymbol = "#";
    }

    @Override
    public void visit(Public publicVis) {
        lastVisibilitySymbol = "+";
    }

    // ==================== Style Visitors ====================
    // Each style knows its own yUML formatting

    @Override
    public void visit(Bold bold) {
        lastStylePrefix = "*";
        lastStyleSuffix = "*";
    }

    @Override
    public void visit(Italic italic) {
        lastStylePrefix = "_";
        lastStyleSuffix = "_";
    }

    // ==================== Stereotype Visitors ====================
    // Each stereotype knows its own yUML representation

    @Override
    public void visit(AbstractStereotype abstractStereotype) {
        lastStereotype = "<<abstract>>";
    }

    @Override
    public void visit(InterfaceStereotype interfaceStereotype) {
        lastStereotype = "<<interface>>";
    }

    @Override
    public void visit(ClassStereotype classStereotype) {
        lastStereotype = "";
    }

    // ==================== Relation Type Visitors ====================
    // Each relation type knows its own yUML symbol

    @Override
    public void visit(Association association) {
        lastRelationSymbol = "->";
    }

    @Override
    public void visit(Aggregation aggregation) {
        lastRelationSymbol = "<>-";
    }

    @Override
    public void visit(Composition composition) {
        lastRelationSymbol = "++-";
    }

    @Override
    public void visit(Dependency dependency) {
        lastRelationSymbol = "-.-";
    }

    @Override
    public void visit(Implementation implementation) {
        lastRelationSymbol = "^-.-";
    }

    @Override
    public void visit(Inheritance inheritance) {
        lastRelationSymbol = "^-";
    }
}