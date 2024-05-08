## 通过@Autowired

1. 匹配优先级: type-match > @Primary > @(Priority)Order > name-match

2. 处理器:

   - AbstractAutowireCapableBeanFactory#populateBean
   - AutowiredAnnotationBeanPostProcessor#postProcessProperties
   - InjectionMetadata.inject
   - AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement(/AutowiredMethodElement)#inject
       - DefaultListableBeanFactory#doResolveDependency (type-match只有一个结果时在此步完成依赖选择)
       - DefaultListableBeanFactory#determineAutowireCandidate (type-match有多个结果时，通过该方法选择注入对象)

3. 相比@Resource

    - 可以设置required属性，设为false时，未找到匹配的bean时不会报错
    - 注解可以修饰方法参数、另一个注解上
    - 修饰static方法或属性时，不会报错；@Resource会报错
    - 修饰方法时，不限制方法参数个数；@Resource修饰方法时只允许有一个方法参数

## 通过@Resource

1. 匹配优先级: name-match > type-match > @Primary > @(Priority)Order

2. 处理器

   - AbstractAutowireCapableBeanFactory#populateBean
   - CommonAnnotationBeanPostProcessor#postProcessProperties
   - InjectionMetadata#inject
   - CommonAnnotationBeanPostProcessor$ResourceElement#inject
       - CommonAnnotationBeanPostProcessor#getResource
       - CommonAnnotationBeanPostProcessor#autowireResource (name-match匹配到结果时，直接注入，短路后续匹配流程）
         - DefaultListableBeanFactory#doResolveDependency (type-match只有一个结果时在此步完成依赖选择)
         - DefaultListableBeanFactory#determineAutowireCandidate (type-match有多个结果时，通过该方法选择注入对象)

## 通过构造函数装配

1. 处理环节
    - AbstractAutowireCapableBeanFactory#createBean
    - AbstractAutowireCapableBeanFactory#doCreateBean
    - AbstractAutowireCapableBeanFactory#createBeanInstance
    - AbstractAutowireCapableBeanFactory#determineConstructorsFromBeanPostProcessors
        - 有符合autowired的带参构造函数时，或preferred指定的构造函数时，有走autowireConstructor
        - 上述两点未满足，走默认无参构造函数