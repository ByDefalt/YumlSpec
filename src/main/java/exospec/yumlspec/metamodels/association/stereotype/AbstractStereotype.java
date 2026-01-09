package exospec.yumlspec.metamodels.association.stereotype;

import exospec.yumlspec.visitor.Visitor;

public class AbstractStereotype extends Stereotype{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
