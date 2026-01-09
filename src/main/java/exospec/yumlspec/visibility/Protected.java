package exospec.yumlspec.visibility;

import exospec.visitor.Visitor;

public class Protected extends Visibility{

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
