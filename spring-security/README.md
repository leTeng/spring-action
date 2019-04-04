### Spring Security

  #### 简介
  
  - 技术图谱
    
    ![](https://github.com/leTeng/spring-action/raw/master/image/Security-技术图谱.png)  
    
  - Spring Security是使用声明式安全框架。能够在web请求(Filter)和方法调用(AOP)进行
       认证和授权。Spring Security充分利用了Aop 和 IOC。 
  
  - Spring Secutity 的模块 
     
    | 模块  |  描述  |
    |-------|-----------------------------|
    | ACL          | 通过访问控制列表(Access Control List)为域对象提供安全 |
    | Aspect(切面)  | 使用Spring Security 注解时。使用的是ApectJ切面,而非Spring AOP |
    | CAS          | 提供与Jasig的中心认证服务(central authentication service)进行集成服务 |
    | Config(配置)  | 对XML和Java方式配置的支持(作为最基础的模块) |
    | Core(核心)    | 提供基本的库(最基本的模块) |
    | Cryptography | 提供编码和解码功能,对数据加密和解密|
    | LDAP         | 支持对LDAP进行认证 |
    | OpenID       | 支持对OpenID集中认证 | 
    | Remoting     | 对Spring Remote 的支持 |
    | Taglib(标签库)|  Spring Security 的Jsp标签库 |
    | Web          |  Spring Security 基于Filter的安全性支持 |

      `注意` Configuration和Core是基本的模块。 
      
  - Security实现安全的一个方式是使用一系列的Filter。DelegatingFilterProxy是Filter
      代理,其实是一个Filter。其委托其他一系列的Filter实现安全控制。所以配置一个DelegatingFilterProxy
      到Servlet上下文即可。但需要给DelegatingFilterProxy配置一系列的Filter实现,任其委托。
      
      `如图：`
      　　![](https://github.com/leTeng/spring-action/raw/master/image/delegatingFilter.PNG)  
      
      ##### 在Servlet上下文配置DelegatingFilterProxy
        
        1. XML
          
            ```xml
               <filter>
                   <!--将委托id为springSecurityFilterChain的bean-->
                   <filter-name>springSecurityFilterChain</filter-name>
                   <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
               </filter>
            ```    
            
        2. 实现WebApplicationInitializer接口配置
            
            `注意` 这里配置类继承了AbstractSecurityWebApplicationInitializer,该类实现于WebApplicationInitializer。
                    
            ```java
               /**
                * @Description 该方式能代替web.xml配置方式配置Servlet上下文。
                *  原理：
                *      1.在Servlet3.0规范中,会在类路径下扫描实现了ServletContainerInitializer接口的类作为容器的初始化上下文。
                *      2.SpringServletContainerInitializer是Spring提供的ServletContainerInitializer实现类。SpringServletContainerInitializer
                *        反过来查找类路径的WebApplicationInitializer实现类。Spring为多个模块提供WebApplicationInitializer实现类进行自定义的初始化
                *        其中AbstractSecurityWebApplicationInitializer是Spring提供的WebApplicationInitializer实现。用于Spring Security模块
                *        在Servlet上下文启动时注册DelegatingFilterProxy过滤器。
                *
                */
               public class WebSecurityConfig extends AbstractSecurityWebApplicationInitializer{
               
                   /*
                    * 1.可以重写insertFilter()和addFilter()来注册其他的Filter。
                    * 2.除了在Servlet容器注册DelegatingFilterProxy之外。还需要一个委托过滤器(bean)。由DelegatingFilterProxy
                    *   委托过滤器,委托的过滤器进行具体的逻辑。该过滤器由Spring提供。
                    */
               }
            ```
            
      ##### 配置委托过滤器
          
      > 描述：DelegatingFilterProxy实现具体的安全配置需要委托一个bean。该bean必须实现WebSecurityConfigurer接口。
                 Spring提供其中一个实现为WebSecurityConfigurerAdapter。扩展该类,重载其中configure()方法实现安全配置。

      **WebSecurityConfigurerAdapter的configure()方法重载：**
          
      |   方法签名      |           描述                      |
      |---------------|-------------------------------------|
      | config(WebSecurity) | 配置Spring Security的Filter链(由DelegatingFilterPorxy委托)|  
      | config(HttpSecurity) | 配置拦截器如何保护web应用安全 |
      | config(AuthenticationManagerBuilder) | 配置user-detail服务(提供用户信息数据)服务 |
     
      ##### 配置Security的要素
              
        1. 配置Filter链。
        2. 配置需要拦截的请求。
        3. 提供基础的用户信息查询。  
          
  #### 用户信息查询
    
  > 所有的用户信息查询可重载config(AuthenticationManagerBuilder)方法配置查询用户信息。
     
  - 内存存储
      
    > 使用内存存储提供用户详细信息(由Security规范),Security提供众多API构建用户详细信息的配置。
    
    ##### 构建用户信息存储的API表
      
      |          方法                         |     描述           |
      |--------------------------------------|----------------------|
      | accountExpired(Boolean)              | 定义账号是否过期      |
      | accountExpired(Boolean)              | 定义账号是否锁定      |
      | and()                                | 连接多个用户信息配置   |
      | authorities(GrantedAuthority ...)    | 授予用户授予一个或者多个权限  |
      | authorities(List<GrantedAuthority>)  | 授予用户授予一个或者多个权限 |
      | authorities(String ...)              | 授予用户授予一个或者多个权限 |
      | credentialsExpired(boolean)          | 定义用户凭证是否过期    |
      | disable(Boolean)                     | 定义用户是否禁用 |
      | password(String)                     | 定义用户密码 |
      | roles(Boolean ...)                   | 授予用户一个或多个角色 |
      
    ##### Simple Example
        
      ```java
        @Configuration //声明为Spring 上下文
        @EnableWebSecurity //开启安全
        public class MemorySecurityConfig extends WebSecurityConfigurerAdapter{
        
            /**
             * 重写configure(AuthenticationManagerBuilder auth)方法构建用户的存储
             * @param auth
             * @throws Exception
             */
            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception{
                //内存的方式存储用户
                auth.inMemoryAuthentication()
                        .withUser("mary").password("123").roles("user")
                        .and()
                        .withUser("admin").password("admin").roles("user","admin");
            }
        }
      ```
        
     `备注`
        
        1.通过inMemoryAuthentication()方法选择内存方式构建用户信息,
        2.通过withUser()方法构建具体的用户信息
        3.通过内存构建用户信息一般使用于测试阶段,一般生产环境使用数据库构建用户信息。
          
  - JDBC储存
        
      > 使用关系型数据库构建用户信息,如果使用默认的查询语句,前提是数据库必须存在SQL语句存在的表和字段。
        也可以自定义SQL语句,不过所有的SQL都需要遵循一个协议时,所有的信息查询都通过username作为用户标识
        查询。
        
      - 使用默认SQL查询用户信息
          
        ##### Security 默认查询用户信息SQL
          
          |    SQL          |           描述              |
          |-----------------|----------------------------|
          |  select username,password,enable  from users where username = ?  |  查询用户信息     |           
          |  select username,authorities from authorities where username = ? | 查询用户权限     |
          |  select id,gm.group_name,ga.authorities from groups g,group_members gm,group_authorities ga where gm.username = ? and g.id = gm.group_id and g.id = ga.group.id | 查询用户权限(归属的群) |
           
          `备注`使用默认的SQL语句,数据库必须提供默认SQL中的表和字段
          
          ##### Simple Example
          
          ```java
            @Configuration
            @EnableWebSecurity //开启安全
            public class JdbcSecurityConfig extends WebSecurityConfigurerAdapter{
            
                @Autowired
                DataSource dataSource;
            
                /**
                 * 基于Jdbc方式构建用户存储
                 *
                 * 1.提供数据源,并且已存在必须(由userByUserNameQuery()、groupByUserNameQuey()、
                 * authoritiesByUserNameQuery()实现的sql定义)的表。
                 *
                 * 2.PasswordEncoder 在验证时,密码已经加密情况下。可以配置对明文进行加密与正确密码匹配。
                 *   Security 的 PasswordEncoder三个实现，分别提供三种不同的加密算法。如果需要自定义
                 *   加密算法,实现PasswordEncoder接口。其中matches()是密码对比的实现。
                 * @param auth
                 * @throws Exception
                 */
                @Override
                protected void configure(AuthenticationManagerBuilder auth) throws Exception{
                    //使用默认的查询sql,并使用密码编码
                    auth.jdbcAuthentication().dataSource(dataSource)
                            .passwordEncoder(new BCryptPasswordEncoder());
                }
            }
          ```  
          
      - 使用自定义SQL查询用户信息
          
        > 使用自定义SQL时，必须遵循以用户名查询。查询的结果需要满足Security 规范的用户信息所需数据
          
          ##### Simple Example
          
          ```java
            @Configuration
            @EnableWebSecurity //开启安全
            public class JdbcSecurityConfig extends WebSecurityConfigurerAdapter{
            
                @Autowired
                DataSource dataSource;
                          
                /**
                 * 使用自定义的sql查询时都要遵循一个原则,所有的查询都是通过username查询。
                 * @param auth
                 * @throws Exception
                 * @throws Exception
                 */
                private void buildCustomSql(AuthenticationManagerBuilder auth) throws Exception{
                    auth.jdbcAuthentication().dataSource(dataSource)
                            .usersByUsernameQuery("select username,password,enable" +
                                    "from users where username = ?;")
                            .groupAuthoritiesByUsername("select id,gm.group_name,ga.authorities" +
                                    "from groups g,group_members gm,group_authorities ga " +
                                    "where gm.username = ? and g.id = gm.group_id and g.id = ga.group.id")
                            .authoritiesByUsernameQuery("select username,authorities " +
                                    "from authorities where username = ?");
                }
            }
          ```
          
  - LDAP存储
      
      `待补充`
      
  - 自定义储存
      
    > 当用户数据不是存储在关系型数据库时,比如存储在NoSql数据库时。由于Security不知数据的具体来源
        Security提供一个UserDetailService接口。由通过loadUserByUsername()方法,用户自定义构建用户的详细信息。
        
    **Example**
       
      ```java
        @Configuration
        @EnableWebSecurity
        public class CustomSecurityConfig extends WebSecurityConfigurerAdapter{
        
            private UserRepository userRepository;
        
            @Autowired
            public void setUserRepository(UserRepository userRepository){
                this.userRepository = userRepository;
            }
        
            /**
             * 自定义构建用户的存储。
             * 1.这种方式不需要考虑数据的来源,只关注UserDetailsService提供一个UserDetails。使得
             *   用户的信息无论是持久化数据(关系型数据库)还是内存数据(NoSql数据库)
             * @param auth
             * @throws Exception
             */
            @Override
            protected void configure(final AuthenticationManagerBuilder auth) throws Exception{
                auth.userDetailsService(new UserDetailsService(){
                    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
                        //获取用户数据
                        Users users = userRepository.userByUsernameQuery(username);
                        //获取用户已授权的权限
                        List<String> authorities = userRepository.authoritiesByUsernameQuery(username);
                        //构建User(security规范的User)
                        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        
                        for(String authority : authorities){
                            grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                        }
                        User user = new User(users.getUsername(),users.getPassword(),grantedAuthorities);
                        return user;
                    }
                });
            }
        }    
      ``` 
         
         `备注`
           
           1.该方式可以不需考虑数据的来源,但是需要用户提供UserDetail(Security规范)所需数据。
           2.可以兼容其他的方式查询用户信息(jdbc、内存、LDAP)。
             
  #### 拦截请求
  
  > 通过自定义表达式拦截的匹配的路径配置(拦截表达式),并对请求进行用户认证和权限认证。可定于多个路径配置(拦截表达式),优先权由上到下,
      所以匹配范围广的表达式可能会覆盖匹配范围小的路径配置。
      
  ##### 自定义配置拦截请求规则和权限验证API表
     
  |   API   |         描述         |
  |---------|----------------------|
  |    authorizeRequest()         |        根据HttpServletRequest配置限制访问    |
  |    antMatchers(String ...)    |        **Ant风格**自定义配置拦截请求规则    |
  |    mvcMatchers(String ...)    |        **限定mvc模式**自定义配置拦截请求规则    |
  |    regexMatchers(String ...)  |        **正则风格**自定义配置拦截请求规则    |
  |    and()                      |        连接HttpSecurity的其他配置    |
  |    access(String)             |        使用Spring El配置权限认证    |
  |    anonymous()                |        允许匿名访问(不需要认证)    |
  |    authenticated()            |        允许认证用户访问    |
  |    denyAll()                  |        拒绝所有的访问    |
  |    fullyAuthenticated()       |        不是通过remember-me认证,允许访问  |
  |    hasAnyAuthority(String ...)|        拥有给定的随意一个权限,允许访问  |
  |    hasAnyRole(String ...)     |        拥有给定的随意一个角色,允许访问  |
  |    hsaAuthority(String)       |        拥有给定的权限,允许访问  |
  |    hasIpAddrees(String)       |        请求ip等于给定的请求ip,允许访问  |
  |    hasRole(String)            |        拥有给定的角色,允许访问  |
  |    not()                      |        对其他限定方法取反  |
  |    permitAll()                |        无条件访问  |
  |    remenberMe()               |        通过remenber-me认证，允许访问  |  
     
  ###### Simple Example
      
  ```java
    @Configuration
    @EnableWebSecurity
    public class PlanSecurityConfig extends WebSecurityConfigurerAdapter{
    
        /**
         * 通过重载configure(HttpSecurity http)方法,自定义配置请求拦截规则和权限认证。
         *
         * 使用普通的方式配置权限认证只能是一维的。例如antMatchers("/aut/**").authenticated(),
         * 这一个拦截。如果配置多个权限限定(antMatchers("/aut/**").authenticated().hasAuthority("admin"))
         * 是不允许的。
         * @param http
         * @throws Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception{
            http.authorizeRequests()
                    .antMatchers("/aut/**") //定义第一个拦截表达式
                    .authenticated() // 配置权限校验(认证过后允许访问)
                    .antMatchers("/aut/getRole") //定义第二个拦截表达式
                    .hasAuthority("admin") //配置权限校验(拥有admin权限,允许访问)
                    .anyRequest().permitAll() //配置其他请求都可以访问
                    .and() //连接其他配置
                    .formLogin(); //配置重定向登录页(使用Security提供默认的)
        }
    }
  ```
      
  `备注` 多个antMatchers()、mvcMatchers()、regexMatchers()可以串联使用,他们的优先权是
        由前得到后。串联时可能会发生覆盖。
      
  ###### 使用Spring El表达式配置权限
      
  > Security 提供对Spring Security的支持,当权限校验时需要多重校验。需要Spring El表达式。
      
     
  - 认证用户
  
  - 保护视图                                                                                