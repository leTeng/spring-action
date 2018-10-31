 ### Spring IOC And DI
 
- 自动装配
  - @Component
  > 使用@component注解声明pojo为一个bean,其中参数的value的值为bean的Id,需要的显示的使用@componentScan进行扫描。
     ```java
      @Component("braveKnight1")
      public class BarveKnight implements Knight{
      
      }
     ````
- JavaConfig
- xml
      
### 高级装配

    - profile
    - 条件化bean
    - 自动注入歧义性
    - 外部属性
    - bean作用域
    - spring EL

