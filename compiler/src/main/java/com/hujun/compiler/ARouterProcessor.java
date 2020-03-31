package com.hujun.compiler;

import com.google.auto.service.AutoService;
import com.hujun.compiler.utils.Constants;
import com.hujun.compiler.utils.EmptyUtils;
import com.hujun.modulize.annotation.ARouter;
import com.hujun.modulize.annotation.RouterBean;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by junhu on 2020/3/27
 */
@AutoService(Processor.class)
//处理什么class
@SupportedAnnotationTypes({Constants.AROUTER_ANNOTATION_TYPES})
@SupportedSourceVersion(SourceVersion.RELEASE_7) //根据什么JDK的版本来生成class文件
@SupportedOptions({Constants.MODULE_NAME, Constants.PKG_NAME_FOR_APT})  //接收外面给的信息类型
public class ARouterProcessor extends AbstractProcessor {
    //操作Element工具类
    private Elements elementUtils;

    //type（类信息）工具类
    private Types typeUtils;

    //用来输出警告、错误等日志
    private Messager messager;

    //文件生成器
    private Filer filer;

    private String pkgNameForAPT;

    private String moduleName;

    private Map<String, List<RouterBean>> tempPathMap = new HashMap<>();

    private Map<String, String> tempGroupMap = new HashMap<>();

    //初始化工作，
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
        if (messager == null) {
            return;
        }
        if (processingEnvironment.getOptions() == null) {
            return;
        }

        Map<String, String> options = processingEnvironment.getOptions();

        if (!EmptyUtils.isEmpty(options)) {
            moduleName = options.get(Constants.MODULE_NAME);
            print("module name:" + moduleName);

            pkgNameForAPT = options.get(Constants.PKG_NAME_FOR_APT);
            print("pkg apt:" + pkgNameForAPT);
        }

        if (EmptyUtils.isEmpty(moduleName) || EmptyUtils.isEmpty(pkgNameForAPT)) {
            throw new RuntimeException("注解处理器需要的参数moduleName和pkgNameForAPT不能为空！");
        }
    }


    /**
     * 相当于main函数，开始处理注解
     * 注解处理器的核心方法，处理具体的注解，生成java文件
     *
     * @param set              使用了支持处理注解的节点集合
     * @param roundEnvironment 当前或之前的运行环境，可以通过该对象查找找到的注解
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
//            generateJavaClassFile(element);
            RouterBean routerBean = parseElement(element);
            checkRouter(routerBean);
            putInPathMap(routerBean);
        }
        writeToJava();

        return true;
    }

    /**
     * 将Path的map自动生成java代码
     */
    private void writeToJava() {
        if (EmptyUtils.isEmpty(tempPathMap)) {
            return;
        }
        TypeElement groupLoadFile = elementUtils.getTypeElement( Constants.AROUTER_GROUP);
        TypeElement pathLoadFile = elementUtils.getTypeElement( Constants.AROUTER_PATH);
        //1.生成路由的Path类文件，如ARouter$$Path$$app
        createPathFile(pathLoadFile);

        //2.生成路由组Group类文件，如ARouter$$Group$$app
        createGroupFile(groupLoadFile, pathLoadFile);
    }

//package com.hujun.modulize.test;
//
//import com.hujun.modulize.api.core.ARouterLoadGroup;
//import com.hujun.modulize.api.core.ARouterLoadPath;
//
//import java.util.HashMap;
//import java.util.Map;
//
//    public class ARouter$$Group$$order implements ARouterLoadGroup {
//        @Override
//        public Map<String,Class<? extends ARouterLoadPath>> loadGroup(){
//            Map<String,Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
//            groupMap.put("order",ARouter$$Path$$order.class);
//            return groupMap;
//        }
//    }

    private void createGroupFile(TypeElement groupLoadFile, TypeElement pathLoadFile) {
        //返回值Map<String,Class<? extends ARouterLoadPath>>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                //下面是map泛型的两个值
                ClassName.get(String.class),
                //Class泛型
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadFile))
                )
        );

        for (Map.Entry<String, String> entry : tempGroupMap.entrySet()) {
            String group = entry.getKey();
            String pathName = entry.getValue();

            //方法体
            MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.GROUP_METHOD_NAME)
                    .returns(methodReturn)
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class);

            builder.addStatement("Map<String,Class<? extends ARouterLoadPath>> groupMap = new $T<>()", HashMap.class);
            builder.addStatement("groupMap.put($S,$T.class)", group, ClassName.get(pkgNameForAPT,pathName));
            builder.addStatement("return groupMap");

            MethodSpec methodSpec = builder.build();

            //类体
            String finalName = Constants.GROUP_FILE_NAME + group;
            TypeSpec typeSpec = TypeSpec.classBuilder(finalName)
                    .addMethod(methodSpec)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(groupLoadFile))
                    .build();

            JavaFile javaFile = JavaFile.builder(pkgNameForAPT, typeSpec).build();
//            print("group:\n" + javaFile.toString());
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void createPathFile(TypeElement pathLoadFile) {
        //首先实现方法
        //方法的返回值是Map<String, RouterBean>
        TypeName methodReturn = ParameterizedTypeName.get(Map.class, String.class, RouterBean.class);

        //遍历tempPathMap，将所有的group都创建一个path类文件，例如：ARouter$$Path$$app和ARouter$$Path$$order
        for (Map.Entry<String, List<RouterBean>> entry : tempPathMap.entrySet()) {
            String group = entry.getKey();
            List<RouterBean> routerBeans = entry.getValue();
            MethodSpec.Builder builder = MethodSpec.methodBuilder(Constants.PATH_METHOD_NAME)
                    .returns(methodReturn)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC);
            //方法体里面不循环的部分
            //Map<String, RouterBean> pathMap = new HashMap<>(10);
            builder.addStatement("Map<String, RouterBean> pathMap = new $T<>(10)", HashMap.class);
//            pathMap.put("/order/MainActivity",
//                    RouterBean.create(RouterBean.Type.ACTIVITY,
//                            OrderActivity.class, "/order/MainActivity", "order"));
            //方法体里面循环的部分
            for (RouterBean routerBean : routerBeans) {
                builder.addStatement("pathMap.put($S,RouterBean.create(RouterBean.Type.ACTIVITY,$T.class,$S,$S))", routerBean.getPath(), ClassName.get((TypeElement) routerBean.getElement()), routerBean.getPath(), routerBean.getGroup());
            }

            //方法体里面不循环的部分
            builder.addStatement("return pathMap");

            String finalClassName = Constants.PATH_FILE_NAME + group;
            MethodSpec methodSpec = builder.build();
            TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                    .addMethod(methodSpec)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(pathLoadFile))
                    .build();

            //生成path文件
            JavaFile javaFile = JavaFile.builder(pkgNameForAPT, typeSpec)
                    .build();
//            String javaCode = javaFile.toString();
//            print("createPathFile:\n" + javaCode);

            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //将生成的path文件名字，放入group map中
            tempGroupMap.put(group, finalClassName);
        }
    }

    private void putInPathMap(RouterBean routerBean) {
        print("putInPathMap : " + routerBean.toString());
        List<RouterBean> list = tempPathMap.get(routerBean.getGroup());
        if (EmptyUtils.isEmpty(list)) {
            list = new ArrayList<>();
            list.add(routerBean);
            tempPathMap.put(routerBean.getGroup(), list);
        } else {
            for (RouterBean bean : list) {
                if (bean.getPath().equalsIgnoreCase(routerBean.getPath())) {
                    return;
                }
            }
            list.add(routerBean);
        }
    }

    private void checkRouter(RouterBean bean) {
        if (EmptyUtils.isEmpty(bean.getPath())) {
            throw new RuntimeException("@ARouter注解错误 path必须填写");
        }

        //path需要以/开头
        if (!bean.getPath().startsWith("/")) {
            throw new RuntimeException("@ARouter注解错误 path需要以/开始");
        }

        if (bean.getPath().lastIndexOf("/") == 0) {
            throw new RuntimeException("@ARouter注解错误 path格式不正确");
        }

        //注解中没有赋值group
        if (!EmptyUtils.isEmpty(bean.getGroup()) && !bean.getGroup().equalsIgnoreCase(moduleName)) {
            throw new RuntimeException("@ARouter注解错误 group必须和当前子模块名相同");
        }

        //如果group为空，则为其赋予一个group
        if (EmptyUtils.isEmpty(bean.getGroup())) {
            String group = bean.getPath().substring(1, bean.getPath().indexOf("/", 1));
            if (EmptyUtils.isEmpty(group)) {
                throw new RuntimeException("@ARouter注解错误 path格式不正确");
            }
            if (group.contains("/")) {
                throw new RuntimeException("@ARouter注解错误 path格式不正确");
            }
            bean.setGroup(group);
        }
    }

    private RouterBean parseElement(Element element) {
        TypeMirror typeMirror = element.asType();
        print("parseElement：" + typeMirror.toString());

        ARouter aRouter = element.getAnnotation(ARouter.class);
        String path = aRouter.path();
        String group = aRouter.group();

        //通过Element工具类，获取Activity类
        TypeElement activityType = elementUtils.getTypeElement(Constants.ACTIVITY_PKG);

        //类信息
        TypeMirror activityMirror = activityType.asType();

        //对注解进行判断，@ARouter只能用在类上面，并且只能用在Activity上
        if (!typeUtils.isSubtype(typeMirror, activityMirror)) {
            throw new RuntimeException("@ARouter注解错误，需要注解到Activity上");
        }

        //路由详细信息封装到类对象
        RouterBean routerBean = new RouterBean.Builder()
                .setElement(element)
                .setGroup(group)
                .setPath(path)
                .build();
        routerBean.setType(RouterBean.Type.ACTIVITY);

        return routerBean;
    }

    private void generateJavaClassFile(Element element) {
        String pkgName = elementUtils.getPackageOf(element).getQualifiedName().toString();
        String className = element.getSimpleName().toString();
        print("被注解的类有：" + className);

        String finalClassName = className + "$$ARouter";
        try {
            JavaFileObject sourceFile = filer.createSourceFile(pkgName + "." + finalClassName);
            Writer writer = sourceFile.openWriter();
//            String javaCode = generateJavaByText(pkgName,className,finalClassName,element);
            String javaCode = generateJavaByJavaPoet(pkgName, className, finalClassName, element);
            print(javaCode);
            writer.write(javaCode);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateJavaByJavaPoet(String pkgName, String className, String finalClassName, Element element) {
        ARouter router = element.getAnnotation(ARouter.class);
        MethodSpec methodSpec = MethodSpec.methodBuilder("findTargetClass")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(Class.class)
                .addParameter(String.class, "path")
                .beginControlFlow("if(path.equalsIgnoreCase($S))", router.path())
                .addStatement("return $T.class", ClassName.get((TypeElement) element))
                .endControlFlow()
                .addStatement("return null")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder(finalClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder(pkgName, typeSpec).build();
        return javaFile.toString();
    }


    private String generateJavaByText(String pkgName, String className, String finalClassName, Element element) {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + pkgName + ";\n");
        builder.append("public class " + finalClassName + "{\n");
        builder.append("public static Class<?> findTargetClass(String path){\n");
        ARouter router = element.getAnnotation(ARouter.class);
        builder.append("if (path.equalsIgnoreCase(\"" + router.path() + "\")){\n");
        builder.append("return " + className + ".class;\n");
        builder.append("}\n");
        builder.append("return null;\n");
        builder.append("}\n}");
        return builder.toString();
    }

    private void print(String msg) {
        if (messager == null) {
            return;
        }
        messager.printMessage(Diagnostic.Kind.WARNING, msg);
    }
}
