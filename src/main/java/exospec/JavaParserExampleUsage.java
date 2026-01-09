package exospec;

import exospec.reflect.JavaParser;
import exospec.visitor.YumlVisitor;
import exospec.yumlspec.metamodels.Diagram;

import java.util.ArrayList;
import java.util.List;

/**
 * Exemple d'utilisation du JavaParser
 */
public class JavaParserExampleUsage {

    public static void main(String[] args) {
        // Créer le parser
        JavaParser parser = new JavaParser();

        // Liste des classes à parser
        List<Class<?>> classesToParse = new ArrayList<>();
        classesToParse.add(Customer.class);
        classesToParse.add(Order.class);
        classesToParse.add(Product.class);

        // Parser et créer le diagramme
        Diagram diagram = parser.parseListClass(classesToParse);

        // Générer yUML
        YumlVisitor visitor = new YumlVisitor();
        diagram.accept(visitor);

        // Afficher le résultat
        System.out.println("=== yUML Syntax ===");
        System.out.println(visitor.getYumlOutput());
        System.out.println();

        System.out.println("=== yUML URL ===");
        System.out.println(visitor.getYumlUrl("scruffy"));
    }
}

// ==================== Classes de test ====================

/**
 * Classe Customer - hérite de Person (externe, ne sera pas parsée)
 */
class Customer extends Person {
    private String email;
    private List<Order> orders;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Order createOrder() {
        return new Order();
    }
}

/**
 * Classe Order - utilise Customer et Product
 */
class Order {
    private int orderId;
    private Customer customer;
    private List<Product> products;

    public void addProduct(Product product) {
        products.add(product);
    }

    public Customer getCustomer() {
        return customer;
    }
}

/**
 * Classe Product
 */
class Product {
    private String name;
    private double price;

    public double getPrice() {
        return price;
    }
}

/**
 * Classe externe (ne sera pas parsée)
 * Sera créée comme classe simple pour la relation d'héritage
 */
class Person {
    private String name;

    public String getName() {
        return name;
    }
}