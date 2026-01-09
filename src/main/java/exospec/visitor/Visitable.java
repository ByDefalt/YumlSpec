package exospec.visitor;

public interface Visitable {
    void accept(Visitor visitor);
}
