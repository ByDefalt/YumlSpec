package exospec.yumlspec.stereotype;

import exospec.visitor.Visitor;

public class AbstractStereotype extends Stereotype{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
