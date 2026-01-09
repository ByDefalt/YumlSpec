package exospec.yumlspec;

import exospec.visitor.Visitor;
import exospec.yumlspec.association.RelationType;

public class Relation extends SpecElement{
    private Class source;
    private Class target;
    private RelationType type;
    private Multiplicity sourceMultiplicity;
    private Multiplicity targetMultiplicity;
    private String role;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

