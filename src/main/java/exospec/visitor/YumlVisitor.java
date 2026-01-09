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
 * Corrected version with proper yUML syntax for relations.
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
            String mult = relation.getSourceMultiplicity().getValue();
            if (mult != null && !mult.isEmpty()) {
                relationBuilder.append(mult);
            }
        }

        // Visit relation type to get the symbol
        if (relation.getType() != null) {
            relation.getType().accept(this);
            relationBuilder.append(lastRelationSymbol);
            lastRelationSymbol = "";
        }

        // Add role if present (with space before >)
        if (relation.getRole() != null && !relation.getRole().isEmpty()) {
            relationBuilder.append(relation.getRole()).append(" ");
        }

        // Add target multiplicity
        if (relation.getTargetMultiplicity() != null) {
            String mult = relation.getTargetMultiplicity().getValue();
            if (mult != null && !mult.isEmpty()) {
                relationBuilder.append(mult);
            }
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
        lastStereotype = "";  // Classes normales n'ont pas de stéréotype affiché
    }

    // ==================== Relation Type Visitors ====================
    // CORRIGÉ : Symboles yUML corrects d'après la documentation

    @Override
    public void visit(Association association) {
        lastRelationSymbol = "->";  // Association directionnelle
    }

    @Override
    public void visit(Aggregation aggregation) {
        lastRelationSymbol = "<>->";  // Agrégation
    }

    @Override
    public void visit(Composition composition) {
        lastRelationSymbol = "++->";  // Composition
    }

    @Override
    public void visit(Dependency dependency) {
        lastRelationSymbol = "-.->";  // Dépendance (pointillé)
    }

    @Override
    public void visit(Implementation implementation) {
        lastRelationSymbol = "^-.-";  // Implémentation
    }

    @Override
    public void visit(Inheritance inheritance) {
        lastRelationSymbol = "^-";  // Héritage
    }
}