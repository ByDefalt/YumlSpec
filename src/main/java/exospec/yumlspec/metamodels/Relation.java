package exospec.yumlspec.metamodels;

import exospec.visitor.Visitor;
import exospec.yumlspec.metamodels.association.RelationType;

public class Relation extends SpecElement{
    private Class source;
    private Class target;
    private RelationType type;
    private Multiplicity sourceMultiplicity;
    private Multiplicity targetMultiplicity;
    private String role;


    public Class getSource() {
        return source;
    }

    public void setSource(Class source) {
        this.source = source;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    public Multiplicity getSourceMultiplicity() {
        return sourceMultiplicity;
    }

    public void setSourceMultiplicity(Multiplicity sourceMultiplicity) {
        this.sourceMultiplicity = sourceMultiplicity;
    }

    public Multiplicity getTargetMultiplicity() {
        return targetMultiplicity;
    }

    public void setTargetMultiplicity(Multiplicity targetMultiplicity) {
        this.targetMultiplicity = targetMultiplicity;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

