package exospec.reflect;

import exospec.serializer.SerializedAttribute;
import exospec.serializer.SerializedEntity;
import exospec.yumlspec.metamodels.*;
import exospec.yumlspec.metamodels.Class;
import exospec.yumlspec.metamodels.Parameter;
import exospec.yumlspec.metamodels.association.*;
import exospec.yumlspec.metamodels.stereotype.AbstractStereotype;
import exospec.yumlspec.metamodels.stereotype.ClassStereotype;
import exospec.yumlspec.metamodels.stereotype.InterfaceStereotype;
import exospec.yumlspec.metamodels.visibility.Private;
import exospec.yumlspec.metamodels.visibility.Protected;
import exospec.yumlspec.metamodels.visibility.Public;

import java.lang.reflect.*;
import java.util.*;

public class JavaParser {

    // Cache pour stocker les classes déjà parsées (par nom complet)
    private Map<String, Class> classCache = new HashMap<>();

    // Liste des classes à parser (celles qui appartiennent au diagramme)
    private Set<String> classesToParse = new HashSet<>();

    public Diagram parseListClass(List<java.lang.Class<?>> clazzList) {
        Diagram diagram = new Diagram();

        // Stocker les noms des classes à parser
        for(java.lang.Class<?> clazz : clazzList) {
            classesToParse.add(clazz.getName());
        }

        // Parser toutes les classes
        for(java.lang.Class<?> clazz : clazzList) {
            parseClass(clazz, diagram);
        }

        return diagram;
    }

    public void parseClass(java.lang.Class<?> clazz, Diagram diagram) {
        String className = clazz.getSimpleName();
        String fullClassName = clazz.getName();
        if(!clazz.isAnnotationPresent(SerializedEntity.class)) return;
        int modifiers = clazz.getModifiers();

        // Créer le modèle de classe
        Class classModel = new Class();
        List<Attribute> attributes = parseField(clazz);
        List<exospec.yumlspec.metamodels.Method> methods = parseMethod(clazz);

        classModel.setName(className);
        classModel.setAttributes(attributes);
        classModel.setMethods(methods);

        // Définir le stéréotype
        if(Modifier.isInterface(modifiers)) {
            classModel.setStereotype(new InterfaceStereotype());
        } else if(Modifier.isAbstract(modifiers)) {
            classModel.setStereotype(new AbstractStereotype());
        } else {
            classModel.setStereotype(new ClassStereotype());
        }

        // Ajouter au diagramme et au cache
        diagram.getClasses().add(classModel);
        classCache.put(fullClassName, classModel);

        // ==================== GÉRER L'HÉRITAGE ====================
        java.lang.Class<?> superClass = clazz.getSuperclass();
        if(superClass != null && superClass != Object.class) {
            Relation relation = new Relation();
            relation.setSource(classModel);

            // Vérifier si la superclasse fait partie des classes à parser
            if(classesToParse.contains(superClass.getName())) {
                Class targetClass = classCache.get(superClass.getName());
                if(targetClass == null) {
                    targetClass = new Class();
                    targetClass.setName(superClass.getSimpleName());
                    classCache.put(superClass.getName(), targetClass);
                }
                relation.setTarget(targetClass);
            } else {
                // Classe externe
                Class externalClass = new Class();
                externalClass.setName(superClass.getSimpleName());
                relation.setTarget(externalClass);
            }

            relation.setType(new Inheritance());
            diagram.getRelations().add(relation);
        }

        // ==================== GÉRER LES INTERFACES ====================
        java.lang.Class<?>[] interfaces = clazz.getInterfaces();
        for(java.lang.Class<?> interfaceClass : interfaces) {
            Relation relation = new Relation();
            relation.setSource(classModel);

            if(classesToParse.contains(interfaceClass.getName())) {
                Class targetClass = classCache.get(interfaceClass.getName());
                if(targetClass == null) {
                    targetClass = new Class();
                    targetClass.setName(interfaceClass.getSimpleName());
                    targetClass.setStereotype(new InterfaceStereotype());
                    classCache.put(interfaceClass.getName(), targetClass);
                }
                relation.setTarget(targetClass);
            } else {
                Class externalInterface = new Class();
                externalInterface.setName(interfaceClass.getSimpleName());
                externalInterface.setStereotype(new InterfaceStereotype());
                relation.setTarget(externalInterface);
            }

            relation.setType(new Implementation());
            diagram.getRelations().add(relation);
        }

        // ==================== GÉRER LES ASSOCIATIONS (CHAMPS) ====================
        for(Attribute attribute : attributes) {
            Field field = findFieldByName(clazz, attribute.getName());
            if(field != null) {
                // Extraire le type réel (gérer les collections)
                TypeInfo typeInfo = extractTypeInfo(field);

                // Vérifier si le type est une classe à parser
                if(typeInfo.actualType != null && classesToParse.contains(typeInfo.actualType.getName())) {
                    Relation relation = new Relation();
                    relation.setSource(classModel);

                    Class targetClass = classCache.get(typeInfo.actualType.getName());
                    if(targetClass == null) {
                        targetClass = new Class();
                        targetClass.setName(typeInfo.actualType.getSimpleName());
                        classCache.put(typeInfo.actualType.getName(), targetClass);
                    }
                    relation.setTarget(targetClass);
                    relation.setType(new Association());
                    relation.setRole(attribute.getName());

                    // Ajouter la multiplicité si c'est une collection
                    if(typeInfo.isCollection) {
                        Multiplicity targetMult = new Multiplicity();
                        targetMult.setValue("*");  // Collection = *
                        relation.setTargetMultiplicity(targetMult);

                        Multiplicity sourceMult = new Multiplicity();
                        sourceMult.setValue("0");
                        relation.setSourceMultiplicity(sourceMult);
                    }

                    diagram.getRelations().add(relation);
                }
            }
        }

        // ==================== GÉRER LES DÉPENDANCES (MÉTHODES) ====================
        for(exospec.yumlspec.metamodels.Method method : methods) {
            java.lang.reflect.Method javaMethod = findMethodByName(clazz, method.getName());
            if(javaMethod != null) {
                // Type de retour
                TypeInfo returnTypeInfo = extractTypeInfo(javaMethod.getReturnType());
                if(returnTypeInfo.actualType != null && classesToParse.contains(returnTypeInfo.actualType.getName())) {
                    createDependency(classModel, returnTypeInfo.actualType, diagram);
                }

                // Paramètres
                for(java.lang.reflect.Type paramType : javaMethod.getGenericParameterTypes()) {
                    TypeInfo paramTypeInfo = extractTypeInfo(paramType);
                    if(paramTypeInfo.actualType != null && classesToParse.contains(paramTypeInfo.actualType.getName())) {
                        createDependency(classModel, paramTypeInfo.actualType, diagram);
                    }
                }
            }
        }


    }

    /**
     * Extrait les informations de type, gérant les génériques (List<Product>, etc.)
     */
    private TypeInfo extractTypeInfo(Field field) {
        Type genericType = field.getGenericType();
        return extractTypeInfo(genericType);
    }

    /**
     * Extrait les informations de type d'un Type Java
     */
    private TypeInfo extractTypeInfo(Type type) {
        TypeInfo info = new TypeInfo();

        if (type instanceof ParameterizedType) {
            // C'est un type générique comme List<Product>
            ParameterizedType paramType = (ParameterizedType) type;
            Type rawType = paramType.getRawType();

            // Vérifier si c'est une collection
            if (rawType instanceof java.lang.Class) {
                java.lang.Class<?> rawClass = (java.lang.Class<?>) rawType;
                if (isCollectionType(rawClass)) {
                    info.isCollection = true;

                    // Extraire le type de l'élément
                    Type[] actualTypeArguments = paramType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof java.lang.Class) {
                        info.actualType = (java.lang.Class<?>) actualTypeArguments[0];
                    }
                }
            }
        } else if (type instanceof java.lang.Class) {
            // Type simple (non générique)
            java.lang.Class<?> clazz = (java.lang.Class<?>) type;
            if (isCollectionType(clazz)) {
                info.isCollection = true;
            } else {
                info.actualType = clazz;
            }
        }

        return info;
    }

    /**
     * Vérifie si une classe est un type de collection
     */
    private boolean isCollectionType(java.lang.Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz) ||
                Map.class.isAssignableFrom(clazz) ||
                clazz.isArray();
    }

    /**
     * Classe interne pour stocker les informations de type
     */
    private static class TypeInfo {
        java.lang.Class<?> actualType;  // Le type réel (Product dans List<Product>)
        boolean isCollection;           // true si c'est List, Set, Map, etc.
    }

    /**
     * Crée une relation de dépendance
     */
    private void createDependency(Class source, java.lang.Class<?> targetJavaClass, Diagram diagram) {
        Relation relation = new Relation();
        relation.setSource(source);

        Class targetClass = classCache.get(targetJavaClass.getName());
        if(targetClass == null) {
            targetClass = new Class();
            targetClass.setName(targetJavaClass.getSimpleName());
            classCache.put(targetJavaClass.getName(), targetClass);
        }
        relation.setTarget(targetClass);
        relation.setType(new Dependency());

        // Éviter les doublons
        if(!relationExists(diagram, source, targetClass, Dependency.class)) {
            diagram.getRelations().add(relation);
        }
    }

    /**
     * Vérifie si une relation existe déjà
     */
    private boolean relationExists(Diagram diagram, Class source, Class target, java.lang.Class<?> relationType) {
        for(Relation rel : diagram.getRelations()) {
            if(rel.getSource() == source &&
                    rel.getTarget() == target &&
                    rel.getType().getClass() == relationType) {
                return true;
            }
        }
        return false;
    }

    /**
     * Trouve un champ par son nom
     */
    private Field findFieldByName(java.lang.Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Trouve une méthode par son nom (première occurrence)
     */
    private java.lang.reflect.Method findMethodByName(java.lang.Class<?> clazz, String name) {
        for(java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
            if(method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    public List<Attribute> parseField(java.lang.Class<?> clazz) {
        List<Field> fields = List.of(clazz.getDeclaredFields());
        List<Attribute> attributeList = new ArrayList<>();

        for(Field field : fields) {
            // Ignorer les champs synthétiques (générés par le compilateur)
            if(field.isSynthetic()) {
                continue;
            }
            if(!field.isAnnotationPresent(SerializedAttribute.class)) continue;

            Attribute attribute = new Attribute();
            attribute.setName(field.getName());

            int modifier = field.getModifiers();
            if(Modifier.isPublic(modifier)) {
                attribute.setVisibility(new Public());
            } else if(Modifier.isPrivate(modifier)) {
                attribute.setVisibility(new Private());
            } else if(Modifier.isProtected(modifier)) {
                attribute.setVisibility(new Protected());
            }

            attributeList.add(attribute);
        }

        return attributeList;
    }

    public List<exospec.yumlspec.metamodels.Method> parseMethod(java.lang.Class<?> clazz){
        List<java.lang.reflect.Method> methods = List.of(clazz.getDeclaredMethods());
        List<exospec.yumlspec.metamodels.Method> methodList = new ArrayList<>();

        for(java.lang.reflect.Method m : methods) {
            // Ignorer les méthodes synthétiques
            if(m.isSynthetic()) {
                continue;
            }

            exospec.yumlspec.metamodels.Method method = new exospec.yumlspec.metamodels.Method();
            method.setName(m.getName());

            int modifier = m.getModifiers();
            if(Modifier.isPublic(modifier)) {
                method.setVisibility(new Public());
            } else if(Modifier.isPrivate(modifier)) {
                method.setVisibility(new Private());
            } else if(Modifier.isProtected(modifier)) {
                method.setVisibility(new Protected());
            }

            method.setParameters(parseParameter(m));

            methodList.add(method);
        }

        return methodList;
    }

    public List<Parameter> parseParameter(java.lang.reflect.Method method){
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        List<Parameter> parameterList = new ArrayList<>();

        for(java.lang.reflect.Parameter p : parameters) {
            Parameter parameter = new Parameter();
            parameter.setName(p.getName());
            parameterList.add(parameter);
        }

        return parameterList;
    }
}