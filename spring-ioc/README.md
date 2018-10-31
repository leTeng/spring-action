 ### Spring IOC And DI
 
- **自动装配**
  - 声明bean
  > 使用@component注解声明pojo为一个bean,其中参数的value的值为bean的Id。如下：分别声明了两个bean
     ```java
      package com.eTeng.mode.bean.impl;
      @Component("braveKnight1")
      public class BarveKnight implements Knight{
      
      }
      
      @Component("RescueDemse1")
      public class RescueDemseQuest implements Quest{

          public void embark(){
              System.out.print("embark success");
     }
     ```
  - 扫描bean
  > 使用@componentScan进行组件扫描,参数为包的路径或者标识的Class。没有参数就以当前配置类的包路径作为基础包。
   ````java
    //@ComponentScan
    @ComponentScan("com.eTeng")
    @Configuration
    public class BarveKnightConfig{
    }
   ````
   - 自动装配bean
   > 使用@autowird注解自动注入
    ````java
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = BarveKnightConfig.class) //加载spring的上下文。可以是java配置的上下文。也可以是xml配置上下文。
    public class BraveKnightTest{

        @Autowired  //自动装配
        BarveKnight barveKnight;
 
        @Autowired //自动装配
        Quest quest;

        @Test
        public void testEmbark(){
            barveKnight.embarkOnQuest();
        }
    }
    ````
  - 自动装配bean的歧义性　
  > 当bean容器中存在多个相同类型的bean时,使用自动装配或者参数装配(JavaConfig)会产生歧义。不知应注入那个bean到目标bean。
    
    　**其中提供两种解决方式：**
   
   > 1.声明优先bean:该方式简单,但是当多个相同类型bean声明为优先会产生新的歧义。</br>
   > 2.限定符: 这种方式解决了(1)带来的新问题,它可以在发生声明多个相同的限定符(beanID)时,可以再声明一个限定符来缩小范围。
      
    - 声明优先bean
        
      - JavaConfig

       ````java

        @Component
        @Primary //Primary也可以和@Compoent组合使用
        public class Cat implements Animal{

           public int getLegCount(){
               return 4;
           }
        }

        @Configuration
        public class PrimaryJavaConfig{

        @Bean
        @Primary //设置为自动装配首先bean标识。
        public Animal dog(){
            return new Dog();
        }

       ````
      - xml
       
       ````java

       <bean class="com.eTeng.autowired.ambiguity.bean.impl.Bird" primary="true"/>

       ````
  
      - 使用限定符
    

- **JavaConfig**

    > 使用java类作为spring上下文配置,在类中的方法上使用@Bean声明为创建一个bean。方法的返回值是bean的实例,方法名称默认为bean
    id。如果bean对其他bean有具体依赖,有两种装配依赖方式(接下会展示)。相比于xml和自动发现创建，该方式更灵活。
  
  - 声明bean
    ````java
     @Configuration
     public class BarveKnightConfig{

       @Bean
       public BarveKnight barveKnight(){
           return new BarveKnight();
       }
     }
    ````
    
  - 通过调用方法依赖
  
    > 通过调用的方法来依赖bean,不会每次都会真正**调用方法**,会对创建bean的方法拦截。如果该bean已经创建直接返回bean引用。
    其中缺点是bean必须是在JavaConfig中,否则无法依赖。提供一种解决方式是将依赖注入到方法参数中依赖。  
     ````java
     @Configuration
     public class BarveKnightConfig{

       @Bean
       public BarveKnight barveKnight(){
           Quest quest = rescueDemseQuest();
           return new BarveKnight(quest);
       }
       
       @Bean
       public Quest rescueDemseQuest(){
           return new RescueDemseQuest();
       }
       
    ````
  - 方法参数依赖
     > 方法参数依赖解决了当中方式及配置时,JavaConfig无法依赖其他方式配置的bean。也会带来新的问题就是**自动装配的歧义性**
     ````java
     @Configuration
     public class BarveKnightConfig{

       @Bean
       public BarveKnight barveKnight(Quest quest){
           return new BarveKnight(quest);
     }
   ````
- **xml**
> 使用一个<bean></bean>标签声明一个bean。指定其id和class(bean的全限定名),其还可以配置\<controltor>和\<property>等。
 
  ````java
   <bean id="barveKnightConfig" class="com.eTeng.mode.java.config.BarveKnightConfig"/>
  ````
 
- **多种配置方式混合使用**
> 一个以spring为bean容器的应用程序可能不同的bean使用不同的方式声明。如第三方生产商提供的组件我们只能用xml或者JavaConfig。
  如果是维护老项目使用xml,如今使用JavaConfig,怎么讲两种配置的方式整合？
  
　　**注意：** 因为自动扫描的方式必须显示xml或JavaConfig配置才生效,所以不能以自动扫描为主
  
  - JavaConfig为主
     > 将xml配置bean引入到JavaConfig类中,并开启扫描组件注解,这样所有方式声明的bean都可以创建。
     ````java
     //@ComponentScan
     @ComponentScan("com.eTeng") //开启组件扫描
     @Configuration
     @Import(BarveKnightConfig.class) //当一个配置类过于膨大,使用多个JavaConfig配置,然后使用@import注解整合成一个JavaConfig
     @ImportResource("classpath:/applicationContext.xml") //该注解将xml配置文件bean导入到JavaConfig中。达成混合配置
     public class BarveKnightConfig{
        
        @Bean
        public BarveKnight barveKnight(){
           return new BarveKnight();
        }
     }
     ````
  - xml 为主
   > 将JavaConfig类在xml到声明为一个bean,并开启扫描组件配置,这样所有方式声明的bea n都可以创建。
   ````java
     <!--扫描组件-->
     <context:component-scan base-package="com.eTeng.mode.bean.impl"/>
     <!--xml 声明bean-->
     <bean id="barve" class="com.eTeng.mode.bean.impl.BarveKnight"/>
     <!--JavaConfig 混合使用-->
     <bean class="com.eTeng.mode.java.config.BarveKnightConfig"/>
   ````
  
- **xml配置命名空间的使用**

### 高级装配

* profile
* 条件化bean
* 自动注入歧义性
* 外部属性
* bean作用域
* spring EL

