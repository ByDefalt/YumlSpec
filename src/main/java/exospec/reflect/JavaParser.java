package exospec.reflect;

import exospec.yumlspec.metamodels.Diagram;

import java.util.List;

public class JavaParser {

    public Diagram parseListClass(List<Class<?>> clazzList) {
        Diagram diagram = new Diagram();

        for(Class<?> clazz : clazzList) {
            parseClass(clazz, diagram);
        }

        return diagram;
    }

    public void parseClass(Class<?> clazz, Diagram diagram) {
        // Implementation to parse a single class and add it to the diagram

        String className = clazz.getSimpleName();


    }
}
