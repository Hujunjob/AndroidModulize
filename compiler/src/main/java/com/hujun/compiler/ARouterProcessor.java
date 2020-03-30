package com.hujun.compiler;

import com.google.auto.service.AutoService;
import com.hujun.modulize.annotation.ARouter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by junhu on 2020/3/27
 */
@AutoService(Processor.class)
//处理什么class
@SupportedAnnotationTypes({"com.hujun.modulize.annotation.ARouter"})
@SupportedSourceVersion(SourceVersion.RELEASE_7) //根据什么JDK的版本来生成class文件
@SupportedOptions("content")  //接收外面给的信息类型
public class ARouterProcessor extends AbstractProcessor {
    //操作Element工具类
    private Elements elementUtils;

    //type（类信息）工具类
    private Types typeUtils;

    //用来输出警告、错误等日志
    private Messager messager;

    //文件生成器
    private Filer filer;

    //初始化工作，
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        if (messager==null){
            return;
        }
        if (processingEnvironment.getOptions()==null){
            return;
        }
        String content = processingEnvironment.getOptions().get("content");
        print(content);
    }



    /**
     * 相当于main函数，开始处理注解
     * 注解处理器的核心方法，处理具体的注解，生成java文件
     * @param set               使用了支持处理注解的节点集合
     * @param roundEnvironment  当前或之前的运行环境，可以通过该对象查找找到的注解
     * @return true 表示后续处理器不会再处理，已经处理完成
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set.isEmpty()) {
            return false;
        }

        //获取项目中所有使用了ARouter注解的节点
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);
        //遍历所有被ARouter注解的类节点
        for (Element element : elements) {
           generateJavaClassFile(element);
        }
        return true;
    }

    private void generateJavaClassFile(Element element){
        String pkgName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        String className = element.getSimpleName().toString();
        print("被注解的类有："+className);

        String finalClassName = className + "$$ARouter";
        try {
            JavaFileObject sourceFile = filer.createSourceFile(pkgName+"."+finalClassName);
            Writer writer = sourceFile.openWriter();
//            String javaCode = generateJavaByText(pkgName,className,finalClassName,element);
            String javaCode = generateJavaByJavaPoet(pkgName,className,finalClassName,element);
            print(javaCode);
            writer.write(javaCode);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//package com.hujun.modulize;
//
//    public class OrderActivity$$ARouter {
//        public static Class<?> findTargetClass(String path) {
//            if (path.equalsIgnoreCase("/app/OrderActivity")) {
//                return OrderActivity.class;
//            }
//            return null;
//        }
//    }
    private String generateJavaByJavaPoet(String pkgName,String className,String finalClassName,Element element){
        ARouter router = element.getAnnotation(ARouter.class);
        MethodSpec methodSpec = MethodSpec.methodBuilder("findTargetClass")
                .addModifiers(Modifier.PUBLIC,Modifier.STATIC)
                .returns(Class.class)
                .addParameter(String.class,"path")
                .beginControlFlow("if(path.equalsIgnoreCase($S))",router.path())
                .addStatement("return $T.class", ClassName.get((TypeElement) element))
                .endControlFlow()
                .addStatement("return null")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder(pkgName,typeSpec).build();
        return javaFile.toString();
    }


    private String generateJavaByText(String pkgName,String className,String finalClassName,Element element){
        StringBuilder builder = new StringBuilder();
        builder.append("package "+pkgName+";\n");
        builder.append("public class "+finalClassName+"{\n");
        builder.append("public static Class<?> findTargetClass(String path){\n");
        ARouter router = element.getAnnotation(ARouter.class);
        builder.append("if (path.equalsIgnoreCase(\""+router.path()+"\")){\n");
        builder.append("return "+className+".class;\n");
        builder.append("}\n");
        builder.append("return null;\n");
        builder.append("}\n}");
        return builder.toString();
    }

    private void print(String msg){
        if (messager==null){
            return;
        }
        messager.printMessage(Diagnostic.Kind.WARNING,msg);
    }
}
