package exospec.yumlspec.metamodels.visibility;

import exospec.visitor.Visitor;

public class Private extends Visibility{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
