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
        }
       ````
      - xml
       
       ````xml

       <bean class="com.eTeng.autowired.ambiguity.bean.impl.Bird" primary="true"/>

       ````
  
      - 使用限定符
        
         **`待补充`**

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
     }
   ````
- **xml**
> 使用一个<bean></bean>标签声明一个bean。指定其id和class(bean的全限定名),其还可以配置\<constructor>和\<property>等。
 
  ````xml
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
    ````xml
      <!--扫描组件-->
      <beans>
      <context:component-scan base-package="com.eTeng.mode.bean.impl"/>
      <!--xml 声明bean-->
      <bean id="barve" class="com.eTeng.mode.bean.impl.BarveKnight"/>
      <!--JavaConfig 混合使用-->
      <bean class="com.eTeng.mode.java.config.BarveKnightConfig"/>
      </beans>
    ````
  
- **xml配置命名空间的使用**
  > 命名空间只能使用在xml配置文件中,示例了在使用xml声明bean是,使用命名空间`c:`代替\<constructor>和使用`p:`代替\<property>。
  - **c:**
　　　　　　　　　　　![c命名空间使用图解](https://github.com/leTeng/spring-action/raw/master/image/cNameSpace.PNG)  </br>
    - 构造器参数名
    
      - 字面量
        ````xml
          <bean class="com.eTeng.mode.java.config.BarveKnightConfig"
              c:knightName="barve"
          />
        ````
      - 引用bean
        ````xml
          <bean class="com.eTeng.mode.java.config.BarveKnightConfig"
              c:quest-ref="rescueDemseQuest"
          />
        ````
    - 构造器参数索引
      
      **注意** 构造器参数索引的前面之所以加多了一个'_'，是因为xml属性不能以数字开头。
              并且当索引值为0时，可以隐藏索引。  
      - 字面量  
        ````xml
          <bean class="com.eTeng.mode.java.config.BarveKnightConfig"
              c:_1="barve"
          />
        ````
      - 引用bean  
        ````xml
          <bean class="com.eTeng.mode.java.config.BarveKnightConfig"
              c:_1-ref="barve"
          />
        ````
    - 索引值为零(第一个构造参数)
       
      - 字面量  
        ````xml
          <bean class="com.eTeng.mode.java.config.BarveKnightConfig"
              c:_-="barve"
          />
        ````
      - 引用bean  
        ````xml
          <bean class="com.eTeng.mode.java.config.BarveKnightConfig"
             c:_-ref="barve" 
          />
        ````
  - **p:**
     　　　　　　　　　　　　![p命名空间使用图解](https://github.com/leTeng/spring-action/raw/master/image/pNameSpace.PNG) </br>
    - 属性参数名
      
      - 字面量
        ````xml
          <bean class="com.eTeng.mode.java.config.BarveKnightConfig"
             p:knightName="barve" 
          />
        ````
      - 引用bean
        ````xml
          <bean class="com.eTeng.mode.java.config.BarveKnightConfig"
              p:quest-ref="rescueDemseQuest" 
          />
        ````     

### 高级装配

* profile
  
  > profile 提供了在不同的环境创建不同的bean(依据启动时激活的环境)，相对于maven在构建时选择环境
    spring与之区别在于一个是运行时(spring)一个是构建时(maven)。运行时相对于构建时，在每次的更
    改后不用每次都重新构建。
  
　　　　**注意:** 如果没有@profile声明的方法会默认自动创建bean，不受激活环境影
　　　　　　  响
　　　　
  - JavaConfig
    > @profile可以使用在类级别或者方法级别上
        
      ````java
        /**
         * 类级别
         */
        @Configuration
        @Profile("dev") //当激活profile为dev则创建该bean
        public class ProfileJavaConfig {
            
            @Bean
            public ProfileBean developmentProfileBean(){
                return new DevelopmentProfileBean();
            }
        }
        
        /**
         * 方法级别
         */
        @Configuration
        public class ProfileJavaConfig {
            
            @Bean
            @Profile("dev")
            public ProfileBean developmentProfileBean(){
                return new DevelopmentProfileBean();
            }
            
            @Bean
            @Profile("test")
            public ProfileBean testProfileBean(){
                return new TestProfileBean();
            }
            
            //会自动创建,不受环境影响
            @Bean
            public CommosBean commosBean(){
                return new CommosBean();
            }
        }  
      ````
    
  - xml
    > 在xml配置不同环境的bean，需要设置`<beans>`标签的profile属性。
      为了更好的给不同环境的bean分类，可以使用`<beans>`嵌套。
    ````xml
        <beans>
           
            <beans profile="dev">
                <bean id="devBean" class="com.eTeng.profile.bean.impl.DevelopmentProfileBean"/>
            </beans>
            
            <beans profile="produce">
                <bean id="prodBean" class="com.eTeng.profile.bean.impl.ProduceProfileBean"/>
            </beans>
            
            <beans profile="test">
                <bean id="testBean" class="com.eTeng.profile.bean.impl.TestProfileBean"/>
            </beans>
        
            <bean id="commonBean" class="com.eTeng.profile.bean.impl.CommosBean"/>
        </beans>
    ````
  - 激活profile 
    
    > 五种激活profile方式,都是配置两个参(spring.profiles.active 和 spring.profiles.default)
        数中的一个其中常用的是在DispatcherServlet配置两个参数中的一个。
            
    - 配置DispatcherServlet初始化参数
      ````xml
        <web-app>
             <servlet>
                <servlet-name>dispatcherServlet</servlet-name>
                <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
                <init-param>
                    <!--设置默认激活环境-->
                    <param-name>spring.profiles.default</param-name>
                    <param-value>dev</param-value>
                </init-param>
            </servlet>
            <servlet-mapping>
                <servlet-name>dispatcherServlet</servlet-name>
                <url-pattern>/</url-pattern>
            </servlet-mapping>
        </web-app>
      ````  
    - 配置web服务应用的上下文参数
        `待补充`
    - 配置为JNDI的条目
        `待补充`  
    - 配置为环境变量
        `待补充`
    - 配置为JVM系统属性
        `待补充`  
    - 在测试类中使用
      ````java
        @RunWith(SpringJUnit4ClassRunner.class)
        @ActiveProfiles("dev") //激活 Profile
        @ContextConfiguration(classes = ProfileJavaConfig.class)
        public class ProfileJavaConfigTest {
        
        }  
      ````   
    
* 条件化bean
* 自动注入歧义性
* 外部属性
* bean作用域
* spring EL

