package exospec;

import exospec.visitor.YumlVisitor;
import exospec.yumlspec.metamodels.Attribute;
import exospec.yumlspec.metamodels.Diagram;
import exospec.yumlspec.metamodels.Relation;
import exospec.yumlspec.metamodels.association.Dependency;
import exospec.yumlspec.metamodels.association.Inheritance;

public class Main {
    public static void main(String[] args) {
        YumlVisitor visitor = new YumlVisitor();

        Diagram diagram = new Diagram();
        exospec.yumlspec.metamodels.Class clazz = new exospec.yumlspec.metamodels.Class();

        clazz.setName("bonjour");
        Attribute attribute = new Attribute();
        attribute.setName("attribut1");
        clazz.getAttributes().add(attribute);

        exospec.yumlspec.metamodels.Class clazz2 = new exospec.yumlspec.metamodels.Class();

        clazz2.setName("bonjour2");

        Relation relation = new Relation();
        relation.setSource(clazz);
        relation.setTarget(clazz2);
        relation.setType(new Inheritance());
        diagram.getRelations().add(relation);


        diagram.getClasses().add(clazz);



        diagram.accept(visitor);
        System.out.println(visitor.getYumlOutput());
    }
}
