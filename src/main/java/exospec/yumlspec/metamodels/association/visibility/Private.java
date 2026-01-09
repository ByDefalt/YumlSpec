package exospec.yumlspec.metamodels.association.visibility;

import exospec.yumlspec.visitor.Visitor;

public class Private extends Visibility{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
