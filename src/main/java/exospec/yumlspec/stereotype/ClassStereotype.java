package exospec.yumlspec.stereotype;

import exospec.visitor.Visitor;

public class ClassStereotype extends Stereotype{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
