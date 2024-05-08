## Bean Lifecycle

### 定义

从一个普通的Java类，解析成Spring规范的BeanDefinition，再到实例化为Spring容器的管理对象

### BeanDefinition 结构

一个完整的bd，包含以下内容

- constructorArgumentValues 构造器参数
- beanClass 对应的Java类型
- lazyInit 是否懒加载
- scope 在Spring容器内是单例或多例
- primary 单接口多实现类模式下，是否为优先注入
- factoryBeanName 构造工厂名称
- factoryMethodName 使用构造工厂的指定方法进行实例化
- initMethodName 类class实例化成bean、放入Spring容器前初始化方法名称
- destroyMethodName 从Spring容器内销毁bean时调用的方法名称
- autowireMode 实例化该bean时，其property的注入模式
    - 枚举，分别支持：按名称、按类型、不注入
    - 默认：当java类使用了Spring注解(如@Service、@Controller)标记时，转化为beanDefinition时默认为“不注入”
    - 具体步骤
        - 获取class的所有property
        - 排除基础数据类型、String、Collection、Object
        - 对剩余的property进行依赖注入
    - 如果手动将beanDefinition的autowireMode改为按类型，其依赖属性可不添加@Autowired注解也可成功注入，前提是容器中存在属性对应类型的bean实例**（待验证）**

- autowireCandidates
- abstractFlag
- dependsOn
- qualifiers
- propertyValues

### BeanDefinition 生成

位于`AbstractApplicationContext.refresh()`中

1. 通过`obtainFreshBeanFactory()`获得工厂实例
2. ...beanFactory的准备和后处理
3. **invokeBeanFactoryPostProcessors**
    1. 执行接口**BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry**
        1. 根据Context指定的scanBasePackages进行扫描所有包含注解标识的Java类、@Configuration声明的bean配置
        2. 根据扫描结果，创建BeanDefinition，并记录到Spring的一个Map内
    2. 执行接口BeanFactoryPostProcessors.postProcessBeanFactory方法
4. ...



### Bean 生成

在`AbstractApplicationContext.refresh()`中，有两处位置与Bean生成有重要关联，其中

1. 通过`obtainFreshBeanFactory()`获得工厂实例

   通常Web服务使用的上下文是`ServletWebServerApplicationContext`，其内部多使用`DefaultListableBeanFactory`，

   其继承自`AbstractAutowireCapableBeanFactory`，也是集中了bean生命周期中的大部分操作的类

2. ...beanFactory的准备和后处理

3. invokeBeanFactoryPostProcessors

4. **registerBeanPostProcessors**

    1. 查找所有实现了BeanPostProcessor接口的后置处理器
    2. 检查是否有实现了PriorityOrdered、Ordered接口，按配置进行排序
    3. 注册到beanFactory内，在后续实例化bean时将匹配并使用这些后置处理器

5. **finishBeanFactoryInitialization** 完成所有非lazyInit、scope=单例的bean实例创建

    1. preInstantiateSingletons入口关联的后置处理器包括

        - InstantiationAwareBeanPostProcessor

            - ① postProcessBeforeInstantiation
                - 用途：在实例化bean之前，检查是否有符合的proxy对象用于返回
                - 触发位置（方法调用栈）：
                    - AbstractAutowireCapableBeanFactory#createBean
                    - AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation
                    - AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInstantiation
                - 接口默认实现：返回null
                - spring-boot-3中，通过org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator提供接口方法实现**（返回的自动代理对象，跟SpringAOP有无关联，待验证）**
                - 如果该方法返回了proxy对象，则跳过②、③、④、⑤、⑥、⑦步骤，直接通过AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization，调用步骤⑧的BeanPostProcessor#postProcessAfterInitialization方法

            - ⑤ postProcessAfterInstantiation

                - 实例化完毕后的一些处理
                - 根据返回值boolean决定是否执行步骤⑥，true则执行，false则不执行
                - 触发位置（方法调用栈）：
                    - AbstractAutowireCapableBeanFactory#createBean
                    - AbstractAutowireCapableBeanFactory#doCreateBean
                    - AbstractAutowireCapableBeanFactory#populateBean

            - ⑥ postProcessPropertyValue

                - 设置bean的属性字段的值，注入相关依赖
                - 触发位置（方法调用栈）：
                    - AbstractAutowireCapableBeanFactory#createBean
                    - AbstractAutowireCapableBeanFactory#doCreateBean
                    - AbstractAutowireCapableBeanFactory#populateBean
                - spring-boot-3中，通过以下提供接口方法实现
                    - org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
                        - 根据步骤③的InjectionMetadata，查找**@Resource**关联的依赖进行注入、如果依赖bean未初始化，则创建
                    - org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
                        - 根据步骤③的InjectionMetadata，查找**@Autowired**关联的依赖进行注入、如果依赖bean未初始化，则创建
        - SmartInstantiationAwareBeanPostProcessor

            - ② detemineCandidateConstructors
                - Java类存在多个构造方法时，帮助factory决定使用具体哪一个构造方法
                - 触发位置（方法调用栈）：
                    - AbstractAutowireCapableBeanFactory#createBean
                    - AbstractAutowireCapableBeanFactory#doCreateBean
                    - AbstractAutowireCapableBeanFactory#createBeanInstance
                    - AbstractAutowireCapableBeanFactory#determineConstructorsFromBeanPostProcessors
                - spring-boot-3中，通过org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor提供接口方法实现

            - ④ getEarlyBeanReference

                - 循环引用依赖检查，添加实例化中的bean对象引用
        - MergedBeanDefinitionPostProcessor

            - ③ postProcessMergedBeanDefinition
                - 作用
                - 触发位置（方法调用栈）：
                    - AbstractAutowireCapableBeanFactory#createBean
                    - AbstractAutowireCapableBeanFactory#doCreateBean
                    - AbstractAutowireCapableBeanFactory#applyMergedBeanDefinitionPostProcessors
                - spring-boot-3中，通过以下提供接口方法实现
                    - org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor
                        - 通过BeanDefinition，构建LifecycleMetadata数据，通过@PostConstruct标记init方法，通过@PreDestroy标记destroy的方法
                    - org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
                        - 通过BeanDefinition，构建InjectionMetadata数据，标记@Resource标记相关的字段属性和方法，为后续注入依赖服务
                    - org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
                        - 通过BeanDefinition，构建InjectionMetadata数据，标记@Autowired标记相关的字段属性和方法，为后续注入依赖服务
                    - org.springframework.context.support.ApplicationListenerDetector
        - BeanPostProcessor

            - ⑦ postProcessBeforeInitialization

                - **执行之前还会执行Aware相关接口，设置beanName、beanFactory、beanClassLoader**
                - bean实例化成功、属性设置成功、初始化方法执行前值处理器
                - 触发位置（方法调用栈）：
                    - AbstractAutowireCapableBeanFactory#createBean
                    - AbstractAutowireCapableBeanFactory#doCreateBean
                    - AbstractAutowireCapableBeanFactory#initializeBean
                    - AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization
                - spring-boot-3中，通过以下提供接口方法实现
                    - org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor
                        - 根据步骤③的LifecycleMetadata，查找@PostConstruct标记方法并执行
                    - org.springframework.context.support.ApplicationContextAwareProcessor
                        - 执行Aware相关接口，设置ApplicationContext、Environment、EventPublisher等
                - **执行之后会执行InitializeBean#afterPropertiesSet方法和声明的init-method**
            - ⑧ postProcessAfterInitialization

                - 设置bean的属性字段的值，注入相关依赖
                - 触发位置（方法调用栈）：
                    - AbstractAutowireCapableBeanFactory#createBean
                    - AbstractAutowireCapableBeanFactory#doCreateBean
                    - AbstractAutowireCapableBeanFactory#initializeBean
                    - AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization
    2. preInstantiateSingletons入口完成初始化所有单例bean后，会再次检查并执行所有实现了接口`SmartInitializingSingleton`的bean后置处理方法
