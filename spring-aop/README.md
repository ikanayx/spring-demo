## AOP

### AutoProxy自动代理

#### 使用@EnableAspectJAutoProxy

#### 使用&lt;aop:aspectj-autoproxy /&gt;

在`AbstractXmlApplicationContext`使用`XmlBeanDefinitionReader`解析XML文件阶段，使用`DefaultBeanDefinitionDocumentReader#parseBeanDefinitions`方法进行解析

对命名空间为aop的标签应用`parseCustomElement`，由spring.aop依赖包提供的`AopNamespaceHandler`，指定了`AspectJAutoProxyBeanDefinitionParser`解析&lt;aop:aspectj-autoproxy /&gt;配置

解析结果：向BeanRegistry注册`AspectJAwareAdvisorAutoProxyCreator`，该类实现了`InstantiationAwareBeanPostProcessor`和`BeanPostProcessor`接口，负责为目标Bean自动选择合适的代理方式、生成AOP代理对象

### Advisor构造

#### 使用@Aspect注解声明

1. 创建首个非advisedBean时，需要获取可应用的Advisors，此时spring通过BeanFactoryAspectJAdvisorsBuilder#buildAspectJAdvisors方法，解析所有带有@Aspect类的声明方法，生成对应的Advisors

2. 每个Advisor的生成，通过ReflectiveAspectJAdvisorFactory#getAdvisor获取，类型为`InstantiationModelAwarePointcutAdvisorImpl`

#### 使用<aop:aspect />声明

在`AbstractXmlApplicationContext`使用`XmlBeanDefinitionReader`解析XML文件阶段，使用`DefaultBeanDefinitionDocumentReader#parseBeanDefinitions`方法进行解析

对命名空间为aop的标签应用`parseCustomElement`，由spring.aop依赖包提供的`AopNamespaceHandler`，指定了`ConfigBeanDefinitionParser`解析<aop:aspect />配置

根据XML中的advice定义，生成AspectJPointCutAdvisor的BeanDefinition并注册到BeanFactory中

### AOP对象创建

#### 创建时机

由`AspectJAwareAdvisorAutoProxyCreator`的父类`AbstactAutoProxyCreator`所实现`BeanPostProcessor`接口的postProcessAfterInitialization方法，判断是否需要创建AOP代理对象

1. 先查找所有advisor，匹配是否有可应用到当前bean的advisor
2. 创建并初始化ProxyFactory实例
3. 如果proxyTargetClass=false，再通过evaluateProxyInterfaces检查当前bean是否有实现接口，如果没有实现接口，设置proxyTargetClass=true（使用Cglib代理）；否则使用JdkProxy
4. ProxyFactory的父类ProxyCreatorSupport持有的DefaultAopProxyFactory，进行代理对象创建（调用createAopProxy方法）

### Advice执行顺序

在创建Aop代理对象时，对匹配的advisors进行两两比较，获得最终排序

排序器为`AspectJPrecedenceComparator`

- 当两两比较的advisor，其声明的Aspect类有设置@Order注解，按注解配置的order进行排序

- 没有@Order注解时

    - 如果**是XML声明的Advice，且在同一个Aspect内**

        - 若两个advice有一个以上属于AfterAdvice，按照它们的XML标签声明顺序，后声明的优先级高
        - 若两个advice都不属于AfterAdvice，按照它们的XML标签声明顺序，先声明的优先级高

    - 如果是Annotation声明的Advice，没有DeclarationOrder，所有的CompareOrder比较结果都是0（Same）

      最终顺序 = 解析@Aspect类生成Advisors时的顺序，该顺序由`ReflectiveAspectJAdvisorFactory#getAdvisorMethods`获取类内声明方法时，进行单独排序，使用的Comparator为以下代码所呈现，优先级顺序为：Around > Before > After > AfterReturning > AfterThrowing

      > 如代码注释所言，虽然After优先级高，但After的方法会在AfterReturning和AfterThrowing之后执行，因为advice的方法是写在finally里面的

      ```java
      public class ReflectiveAspectJAdvisorFactory extends AbstractAspectJAdvisorFactory implements Serializable {
      
          private static final Comparator<Method> adviceMethodComparator;
      
          static {
              // Note: although @After is ordered before @AfterReturning and @AfterThrowing,
              // an @After advice method will actually be invoked after @AfterReturning and
              // @AfterThrowing methods due to the fact that AspectJAfterAdvice.invoke(MethodInvocation)
              // invokes proceed() in a `try` block and only invokes the @After advice method
              // in a corresponding `finally` block.
              Comparator<Method> adviceKindComparator = new ConvertingComparator<>(
                      new InstanceComparator<>(
                              Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class),
                      (Converter<Method, Annotation>) method -> {
                          AspectJAnnotation ann = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
                          return (ann != null ? ann.getAnnotation() : null);
                      });
              Comparator<Method> methodNameComparator = new ConvertingComparator<>(Method::getName);
              adviceMethodComparator = adviceKindComparator.thenComparing(methodNameComparator);
          }
      }
      ```



- 两两比较后，每个advisor都会持有两个集合：优先级比自己高的、比自己低的

- 遍历advisors，找出一个“优先级比自己高的集合为空”的advisor

    - 找到符合advisor，则接入到排序结果集合中，然后从剩余的advisors中移除改advisor引用，重复遍历逻辑
    - 找不到符合的advisor，说明按照上面的两两排序排序后，每个advisor都有比自己高的、也有比自己低。**放弃排序直接返回，仅对原列表进行普通的List.sort()，最终与原advisors声明顺序基本无异**

- 优先级高的advice，在invoke递归调用链中先进入；优先级低的后进入

  