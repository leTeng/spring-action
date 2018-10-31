 ### Spring IOC And DI
 
- 自动装配
  </br>
  - 声明bean
  > 使用@component注解声明pojo为一个bean,其中参数的value的值为bean的Id。
  >> 如下：分别声明了两个bean
     ```java
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
     </br>
     - 声明优先bean
     - 使用限定符
    
- JavaConfig
- xml
      
### 高级装配

* profile
* 条件化bean
* 自动注入歧义性
* 外部属性
* bean作用域
* spring EL

