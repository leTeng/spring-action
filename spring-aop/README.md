## 面向切面

- #### 定义

   > 面向切面是一种横向扩展方式，根据传统的方式是使用委托或者继承的方式进行调用重复使用的功能。会造成
     复杂的调用关系。面向切面对系统中重复使用到的功能抽象为一个切面，并声明式指定切面应用到确切的功能点
     无需在功能点进行实际调用，即可实现所要扩展(声明的)功能。
   ---   

  `好处`:

   1. 将重复使用的功能集中在一个地方，方便之后的修改和维护。
   2. 被切入的功能点更关注其主要的任务，其他次要任务由切面完成。

- #### AOP(切面)术语
   
  * 切点(pointcut)
  
      > 描述：指的是通过表达式去匹配被代理对象的多个方法，这些方法是切点。也是
             定义了通知切面在**何处**切入。
      ---
  
  * 通知(advice)

      > 描述：通知定义了切面的**具体**工作和**何时**工作。
      
      - 通知包含：
        
        1. `@BeforeAdevice`  前置通知：目标方法执行之前执行
        2. `@AfterAdevice`   后置通知：目标方法执行之后执行
        3. `@AfterReturnAdevice`  返回通知：目标方法返回之后执行    
        4. `@AfterThrowAdevice`  异常通知：目标方法抛出异常之后执行
        5. `@ARound`             环绕通知：使用自定义行为包裹目标方法
      ---
  
  * 连接点(join point)
    
      > 描述：指的是符合切点扫描条件,并且连接的每一个通知(之前、之后等),称之为
             一个连接点。也就是说通知织入到一个**切入点**后会存在多个连接点(之前、之后等).    
      ---
  
  * 切面
     
      > 指的是由切点和通知构成切面。定义了切面的所有内容，分别定义了在何地、
        何时、做了什么。
      ---  
  
  * 引入
     
      > 指的是将一个定义好的功能或者属性引入到一个对象中，是的对象具有该功能或属性
        但是不需要对对象有任何的修改就能实现。   
    ---
    
  * 织入
    
      > 将切面应用到目标对象是创建代理对象的过程。
     
      - 织入的时期
        1. `编译时：` 在类编译期就进行织入，这需要特殊的编译器。ApectJ以这种方式织入切面。
        2. `类加载时；`在类加载器加载类进入JVM时期进行织入
        3. `运行时：` 在运行的某一个时刻进行织入切面。IOC容器中为目标对象(IOC中的bean)创建代理对象
                     时织入切面,这也是Spring Aop织入切面的方式。

- #### spring对AOP的支持
        
    1.  Spring AOP支持的切入点
         
         `注意：` Spring AOP 是基于代理对象织入切面方式实现。代理是使用jdk的动态代理,只支持以方法应用通知。
                 不支持以字段或者构造器应用通知。如果需要扩展字段或者构造器的应用通知使用ApectJ实现切面。
                 其中Spring Aop 也是借鉴了ApectJ项目。
    
    2. Spring Aop支持切面的四种类型
    
        - 基于代理的经典的Spring Aop
            
            * 使用动态代理对方法进行拦截实现增强。使用ProxyBeanFatory方式。
        
        - 纯Pojo切面
           
            * 同样是基于动态代理对方法级别进行拦截,使用xml的配置方式指定一个Pojo为切面。
              
        - @ApectJ 注解驱动切面
            
            * 同样是基于动态代理对方法级别进行拦截。使用将Pojo使用注解驱动指定为一个切面。这种方式
              是最简单和简洁的。
              
        - 注入式的ApectJ切面
            
            * 一般需要字段或构造器级别的切面,才需要ApectJ。
                      
- #### 切点指示器
        
    > 描述：通过切点指示器(ApectJ指示器)选择连接节点,ApectJ是一种切点语言表达式。
    
    `注意：` Spring 只是支持AspcetJ中的一些(基于代理)指示器。
    
     #####  指示器列表：
     
     | 指示器  |  描述  |
     |:-------:|:-----------------------------:|
     | arg()        | **限制连接点为**匹配指定参数类型的方法 |
     | @arg()       | **限制连接点为**匹配指定注解标注的方法 |
     | execution()  | **执行限定规则匹配**的指示器(唯一一个可执行限定规则的指示器) |
     | this()       | **限制连接点匹配AOP代理bean引用**为指定类型的类 |
     | target       | **限制连接点匹配目标对象**为指定类型的类 |
     | @target      | **限制连接点匹配具有指定类型的注解**的类 |
     | within       | **限制连接点匹配指定类型的类(包)** |
     | @within      | **限制连接点匹配指定类型的注解**的包 |
     | @annotation  | **限制连接点为指定类型的注解**的连接点(方法) |
     | bean         | **限制连接点为指定beanID**的bean |
     
     `注意:` 只有execution指示器是用来匹配连接点,其他的指示器是用来限制连接点的。excetetion是
            最常用的指示器,它们也可以配合的使用。
     
- #### 编写切点
    
    * 使用execution匹配连接点
     
        `如图所示：
        ` 
          
        [point1](https://github.com/leTeng/spring-action/raw/master/image/point1.PNG)   
     
       > 解析：使用了execution指示器执行匹配,‘*’ 代表匹配返回任意类型的方法,‘concert.Performmance.perform(..)’,
          其中concert.Performmance.perform指定了concert包下的Performmance类型的perform方法为连接点，
          并指定了参数为任意个数和类型。
     
    * 使用其他指示器限制连接点
        
      `如图所示:`
              
        [point2](https://github.com/leTeng/spring-action/raw/master/image/point2.PNG)      
    
      > 解释：使用了execution指示器执行匹配连接点为concert.Performmance.perform。并且使用了within限定了目标对象
           在concert包下。其中使用 && 表示指示器的并关系, || 表示或关系，！表示非关系。在使用xml配置时分别使用
           and、or、not表示。
           
- #### 编写切面
       
    * 定义切面
    
      - Aspect注解驱动    
    
        ```java
          @Aspect //定义为一个切面
          public class PerformAdvice implements Advice{
          
              private static final Logger LOGGER = LoggerFactory
                      .getLogger(Television.class);
          
              //定义一个公共切点
              @Pointcut("execution(* com.eTeng.point.interfaces.Perform.processShow())")
              public void performShow(){
          
              }
          
              //连接电源(调用之前)
              @Before("performShow()")
              public void noElectricity(){
                  LOGGER.info("turn on electricity success");
              }
          
              //连接电源(调用之前)
              @Before("performShow()")
              public void openTv(){
                  LOGGER.info("openTv success");
              }
          
              //关闭(返回之后)
              @AfterReturning("performShow()")
              public void closeTv(){
                  LOGGER.info("close success");
              }
          
              //修复(出异常)
              @AfterThrowing("performShow()")
              public void fix(){
                  LOGGER.info("proceed fix");
              }
          
              //断开电源(返回之后或出异常)
              @After("performShow()")
              public void offElectricity(){
                  LOGGER.info("deenergization success");
              }
          }
        ```
        > 解析：使用@Aspect注解声明一个Pojo为切面,同时还是一个Pojo。@before、@after等注解定义了通知时机
           做对应的工作。使用@pointcut标识一个方法相当于定义了一个切点。在定义通知时机时，可直接套用定
           义好的切点方法名。这样也可以重复使用切点。不需要每次重复写切点匹配。
            
      - xml定义
        
        ```xml
            <bean>
                <aop:config>
                    <!--定义一个切面,并引用通知-->
                    <aop:aspect ref="performAdvice" id="perform">
                        <!--定义一个切点,精确到方法级别-->
                        <aop:pointcut id="performPoint" expression="execution(* com.eTeng.point.interfaces.Perform.processShow())"/>
                        <!--定义两个前置通知-->
                        <aop:before method="noElectricity" pointcut-ref="performPoint"/>
                        <aop:before method="openTv" pointcut-ref="performPoint"/>
                        <!--定义一个后置通知-->
                        <aop:after method="offElectricity" pointcut-ref="performPoint"/>
                        <!--定义一个返回通知-->
                        <aop:after-returning method="closeTv" pointcut-ref="performPoint"/>
                        <!--定义一个异常通知-->
                        <aop:after-throwing method="fix" pointcut-ref="performPoint"/>
                    </aop:aspect>
                </aop:config>
            </bean>
        ```
        > 描述：多个切面的配置在<aop:config>标签下,可以在标签下定义多个公共切点应用于多个切面。
               使用一个<aop:aspect>定义一个切点,使用id属性标识每个切点,使用AspectJ表达式匹配
               连接点。使用<aop:before>、<aop:after>、<aop:returning>定义通知,每个标签代表
               不同通知时机。pointcut-ref属性表示通知哪一个目标方法。
                          
    * 开启自动代理
        
      1. 注解驱动
      
        ```java
          @Configuration
          @EnableAspectJAutoProxy //设置AspectJ自动代理
          @ComponentScan(basePackageClasses = MagicPerform.class)//扫描目标对象bean
          public class AspectJavaConfig{
          
              /**
               * 创建通知对象bean
               */
              @Bean
              public PerformAdvice performAdvice(){
                  return new PerformAdvice();
              }
          }
        ```
        
      2. xml配置
      
        ```xml
            <beans>
              <!--开启Spring Aop自动代理-->
              <aop:aspectj-autoproxy/>
              <!--目标对象-->
              <context:component-scan base-package="com.eTeng.point.impl"/>
              <!--通知-->
              <bean class="com.eTeng.advice.PerformAdvice"/>
            </beans>
        ```
        
    * 环绕通知
        
      > 描述：环绕通知可以将被通知的目标方法包裹起来,在环绕通知使用ProceedJoinPoint
                接口的proceed来通知目标调用。
                
      - 注解驱动
        
        ```java
          @Aspect //声明切面
          public class PerformAdvice2{
          
              @Autowired
              private Advice performAdvice;
          
              @Pointcut("execution(* com.eTeng.point.interfaces.Perform.processShow(..))")
              private void processShow(){}
          
              /**
               * 声明环绕通知
               * @param jp
               */
              @Around("processShow()")
              public void watchPerform(ProceedingJoinPoint jp){
                 try{
                     performAdvice.noElectricity();
                     performAdvice.openTv();
                     jp.proceed(); //调用被通知方法
         //            performAdvice.closeTv();
                     performAdvice.offElectricity();
                 }catch(Throwable ex){
                     performAdvice.fix();
                 }
              }
          }
        ```
        
      - xml配置
          
        ```xml
            <aop:aspect ref="performAdvice2" id="perform2">
                <!--定义一个切点,精确到方法级别-->
                <aop:pointcut id="performPoint" expression="execution(* com.eTeng.point.interfaces.Perform.processShow())"/>
                <!--定义环绕通知-->
                <aop:before method="watchPerform" pointcut-ref="performPoint"/>
            </aop:aspect>
        ```
        
        `注意：` 环绕通知可以在一个通知实现其他的四个通知逻辑(before.after等)。环绕通知方法需要一个
                ProceedJoinPoint接口,通过调用Proceed()函数调用目标方法。如果不调用proceed()函数
                那么通知阻塞了被通知的调用。也可以重复调用proceed()函数，一般用来失败重试。       
    
    * 访问被通知参数    
      
      > 描述：当调用被通知方法时,其中传递了参数。该参数可以通过通知方法访问。
        
        `如图所示：`
            
        [访问参数图示](https://github.com/leTeng/spring-action/raw/master/image/adviceParam.PNG)

        `解析：` 同样是使用一个@Pointcut注解定义了一个切点,其中限定了连接点方法参数类型为int。
                最后还使用了args()限定符,表示被通知方法的参数会传递到通知方法。但需要保证**args
                限定符的参数名称**和**被通知方法的参数名称**一样。**args限定符的参数**也要和**切面定义的通知
                方法参数**一致。
        
        **实现观看不同频道表演,记录频道播放次数**
        
        - 注解驱动
        
          ```java
              @Aspect
              public class WatchPerformCounterAdvice{
              
                  private static final Logger LOGGER = LoggerFactory
                          .getLogger(Television.class);
              
                  private Map<Integer,Integer> counter = new HashMap<Integer,Integer>();
              
                  //选择参数为int类型的连接点,并且将参数传递给通知方法
                  @Pointcut("execution(* com.eTeng.point.interfaces.Perform.processShow(int)) " +
                          "&& args(itemNum)")
                  public void showWithItem(int itemNum){}
              
              
                  @Before("showWithItem(itemNum)")
                  public void recordCounter(int itemNum){
                      LOGGER.info("record itemNum:" + itemNum);
                      Integer count = getCounter(itemNum);
                      counter.put(itemNum,count+1);
                  }
              
                  public Integer getCounter(int itemNum){
                      return counter.containsKey(itemNum) ? counter.get(itemNum) : 0;
                  }
              }
          ```        
        
        - xml配置
          
          ```xml
            <!--定义一个切面,通知可访问被通知方法参数-->
            <aop:aspect ref="wcctAdvice">
                <!--定义切点表达式,并指明将itemNum(被通知方法的)参数传递到通知方法-->
                <aop:pointcut id="watchPerformCounter" expression="execution(* com.eTeng.point.interfaces.Perform.processShow(int)) and args(itemNum)"/>
                <!--定义前置通知-->
                <aop:before method="recordCounter" pointcut-ref="watchPerformCounter"/>
            </aop:aspect>
          ```  
          
          **测试**      
          ```java
              public class AspectJavaConfigTest{
                    
              @Autowired
              Perform perform;
              
              @Test
              public void testSpecifyItem(){
                  perform.processShow(0); //每次选择频道,都会当前频道到通知方法里面。通知方法记录该频道的观看次数。
                  perform.processShow(1);
                  perform.processShow(1);
                  perform.processShow(2);
                  perform.processShow(2);
                  perform.processShow(3);       
              }     
            }
          ```
        
- #### 通过切面引入新功能
  
  > 使用切面为bean不修改代码引入新的接口(方法),其中新引入的接口是其他委托目标对象。实际
    一个bean的被代理委托了多个目标对象。
     
  如图所示：
    
    [AOP引入新功能](https://github.com/leTeng/spring-action/raw/master/image/referenceApi.PNG)   

    - 注解驱动
    
      ```java
        @Aspect
        public class PlayAdIntroduce{
       
         /*
          * 1.+ ：表示匹配Perform接口的所有子类引入新接口。
          * 2.defaultImpl：表示引入功能默认委托目标对象
          * 3.adAdvice 表示引入功能的接口
          */
         @DeclareParents(value = "com.eTeng.point.interfaces.Perform+",
                 defaultImpl = DeFaultPlayAd.class)
         public static PlayAd playAd;
        }  
      ```
      
    - xml配置
      
      ```xml
        <aop:aspect>
          <aop:declare-parents types-matching="com.eTeng.point.interfaces.Perform+"
                               implement-interface="com.eTeng.pojo.interfaces.PlayAd"
                               delegate-ref= "deFaultPlayAd"/>
        </aop:aspect>
      ```  
      
- #### 注入AspectJ切面