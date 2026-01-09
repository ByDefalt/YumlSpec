package exospec.yumlspec.metamodels.association;

import exospec.yumlspec.visitor.Visitor;

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

