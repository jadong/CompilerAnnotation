package com.dong.lib_compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * 🌑🌒🌓🌔🌕🌖🌗🌘
 * Created by zengwendong on 2017/7/31.
 */
public class ProxyInfo {

    private String packageName;
    private String proxyClassName;
    private VariableElement variableElement;
    private Filer filer;
    private TypeElement classElement;

    public Map<Integer, VariableElement> injectVariables = new HashMap<>();

    public static final String PROXY_NAME = "ViewInject";
    public static final String PROXY_PACKAGE_NAME = "com.dong.library";

    public ProxyInfo(Filer filer, Elements elementUtils, VariableElement variableElement) {
        this.variableElement = variableElement;
        this.filer = filer;
        this.classElement = (TypeElement) variableElement.getEnclosingElement();
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        //classname
        String className = ClassValidator.getClassName(classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + PROXY_NAME;

    }

    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import com.dong.library.*;\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName)
                .append(" implements " + ProxyInfo.PROXY_NAME + "<")
                .append(classElement.getQualifiedName()).append(">");
        builder.append(" {\n");

        generateMethods(builder);
        builder.append('\n');

        builder.append("}\n");

        return builder.toString();

    }


    private void generateMethods(StringBuilder builder) {

        builder.append("@Override\n ");
        builder.append("public void inject(" + classElement.getQualifiedName() + " host, Object source ) {\n");


        for (int id : injectVariables.keySet()) {
            VariableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            builder.append(" if(source instanceof android.app.Activity){\n");
            builder.append("host." + name).append(" = ");
            builder.append("(" + type + ")(((android.app.Activity)source).findViewById( " + id + "));\n");
            builder.append("\n}else{\n");
            builder.append("host." + name).append(" = ");
            builder.append("(" + type + ")(((android.view.View)source).findViewById( " + id + "));\n");
            builder.append("\n}");
        }
        builder.append("  }\n");

    }

    public void generateJavaFile() throws IOException, ClassNotFoundException {

        ClassName parameterClass = ClassName.get(classElement);
        MethodSpec.Builder builder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addParameter(parameterClass, "host")
                .addParameter(Object.class, "source");

        for (int id : injectVariables.keySet()) {

            VariableElement element = injectVariables.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();

            builder.beginControlFlow("if(source instanceof android.app.Activity)")
                    .addStatement("$L.$L = ($L)(((android.app.Activity)source).findViewById($L))", "host", name, type, id)
                    .endControlFlow();
            builder.beginControlFlow("else")
                    .addStatement("$L.$L = ($L)(((android.view.View)source).findViewById($L))", "host", name, type, id)
                    .endControlFlow();
        }

        MethodSpec injectMethod = builder.build();

        //实现接口
        ClassName interfaceName = ClassName.get(PROXY_PACKAGE_NAME, PROXY_NAME);

        //泛型注入
        ClassName superinterface = ClassName.bestGuess(classElement.getQualifiedName().toString());

        TypeSpec proxyClass = TypeSpec.classBuilder(proxyClassName)
                .addSuperinterface(ParameterizedTypeName.get(interfaceName, superinterface))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(injectMethod)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, proxyClass)
                .build();

        javaFile.writeTo(filer);

    }

    public TypeElement getTypeElement() {
        return classElement;
    }
}
