#### Spring Jdbc

- 数据源
  
  - jdbc驱动数据源
    > 基于jdbc驱动的数据源。这种数据源是没有连接池化,要么一直使用一个连接或者每次访问创建一个连接的。
    - `DriverManagerDataSource:` 每次访问数据库都创建一个新的连接,没有提供连接的池化管理。
       ```java
       class DataSourceConfig{
            @Bean
            public DataSource driverManagerDataSource(@Value("${jdbc.driver.class}") String driverClass,
                                                      @Value("${jdbc.url}") String url,
                                                      @Value("${jdbc.username}") String username,
                                                      @Value("${jdbc.password.key}") String password){
                DriverManagerDataSource dataSource = new DriverManagerDataSource();
                dataSource.setDriverClassName(driverClass);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                dataSource.setUrl(url);
                return dataSource;
            }      
        }
       ```
    - `SimpleDriverDataSource:` 和`DriverManagerDataSource`相似,用于解决特定环境的类加载。
        ```java
        class DataSourceConfig{
            @Bean
            public DataSource simpleDriverDataSource(@Value("${jdbc.driver.class}") String driverClass,
                                                     @Value("${jdbc.url}") String url,
                                                     @Value("${jdbc.username}") String username,
                                                     @Value("${jdbc.password.key}") String password) throws Exception{
                SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
                dataSource.setDriverClass((Class<Driver>)Class.forName(driverClass));
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                dataSource.setUrl(url);
                return dataSource;
            }
        }
        ```
    - `SingleConnectionDataSource:` 每次请求都是提供同一个连接,同样是没有提供连接池化的管理。
       缺点是不支持的并发的访问
        ```java
        class DataSourceConfig{
            @Bean
            public DataSource singleConnectionDataSource(@Value("${jdbc.driver.class}") String driverClass,
                                                              @Value("${jdbc.url}") String url,
                                                              @Value("${jdbc.username}") String username,
                                                              @Value("${jdbc.password.key}") String password){
                SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
                dataSource.setDriverClassName(driverClass);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                dataSource.setUrl(url);
                return dataSource;       
            }
        }
        ```
  - 外部数据源
    > 外部提供的池化数据源(DBCP,c3p0,druid等)，这种数据源是提供连接池化。
    
    - `DBCP` 
    - `C3P0`
    - `JDNI`
    - `DRUID`
    ````java
    class DataSourceConfig{     
        @Bean
        public DataSource druidDataSource(@Value("${jdbc.driver.class}") String driverClass,
                                          @Value("${jdbc.url}") String url,
                                          @Value("${jdbc.username}") String username,
                                          @Value("${jdbc.password}") String password,
                                          @Value("${jdbc.password.key}") String passwordKey,
                                          @Value("${jdbc.public.key}") String publicKey) throws Exception{
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setUrl(url);
            dataSource.setDriverClassName(driverClass);
    
            dataSource.setMaxActive(10);
            dataSource.setInitialSize(5);
            dataSource.setMaxWait(60000);
            dataSource.setMinIdle(3);
    
            dataSource.setTimeBetweenEvictionRunsMillis(60000);
            dataSource.setMinEvictableIdleTimeMillis(300000);
            dataSource.setTestWhileIdle(true);
            dataSource.setTestOnBorrow(false);
            dataSource.setTestOnReturn(true);
            dataSource.setPoolPreparedStatements(true);
            dataSource.setMaxOpenPreparedStatements(20);
            // 配置监控过滤器
    //        dataSource.setFilters("stat");
            // 配置合并SQL监控过滤器
            dataSource.setFilters("mergeStat");
            // 配置代理监控过滤器
    //        dataSource.setProxyFilters(proxyFilters());
    //        Properties properties = new Properties();
    //
    //        // 配置密码解密秘钥
    //        properties.setProperty("config.decrypt","true");
    //        properties.setProperty("config.decrypt.key",publicKey);
    //        dataSource.setConnectProperties(properties);
            return dataSource;
        }   
    }
    ````
    
  - 内嵌数据库: ``
    > 
    - H2
    
- 基于JDBC访问
   
  > Spring 对JDBC有基本的模板代码封装,使得用户只关心数据的访问,如开启事务、异常处理、回滚等
  模板化的代码由模板类实现。

  - JdbcTemplate
      ```java
      /**
       * 配置JDBC模板,基于参数索引进行参数的绑定
       * @param druidDataSource 数据源
       * @return
       */
      class JdbcConfig{   
        @Bean
        public JdbcTemplate jdbcTemplate(DataSource druidDataSource){
            return new JdbcTemplate(druidDataSource);
        }
      }
      ```
  
  - NamedParameterJdbcTemplate 
    ```java
    /**
     * 配置JDBC模板,基于参数名进行参数的绑定
     * @param druidDataSource
     * @return
     */
    class JdbcConfig{  
        @Bean
        public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource druidDataSource){
            return new NamedParameterJdbcTemplate(druidDataSource);
        }
    }
    ```
  `JdbcTemplate的使用：`
  ```java
    @RunWith(SpringJUnit4ClassRunner.class)
    @ContextConfiguration(classes = Application.class)
    public class SpringJdbcConfigTest{
    
        @Autowired
        private JdbcTemplate jdbcTemplate;
    
        @Test
        public void queryTest(){
            Object[] vals = {"243"};
            Users users = jdbcTemplate.queryForObject("SEELCT * FROM users WHERE users.id = ?",vals,Users.class);
            Assert.assertNotEquals(users,null);
        }
    }
  ```  
  
- ORM访问  
    
    ##### Spring 对Hibernate的支持
     - `支持声明式事务:`Spring 为大多数的ORM框架提供统一的事务管理。
     - `统一的异常处理:`Spring 为众多ORM框架提供Spring统一定义的异常转换功能。
     - `线程安全、轻量的模板类:`Spring 集成了众多ORM框架,并提供操作的模板类，简化代码编写。
     - `DAO支持类:` Spring 为部分的ORM框架提供DAO类的实现，使用户不需要编写DAO实现来访问数据库。
    
    - Hibernate 事务管理器
      >  配置事务管理器,使用注解驱动的方式开启事务,HibernateTransactionManager是Spring
         对Hibernate独立提供的事务管理器。如果未开启事务对数据进行写操作是不允许的。
      ```java
        @org.springframework.context.annotation.Configuration
        // 开启基于注解启动事务
        @org.springframework.transaction.annotation.EnableTransactionManagement
        class TranscationManagerConfig{
            @Bean
            public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory){
                return new HibernateTransactionManager(sessionFactory);
            }
        }
      ```
    - 异常转换
    > 如果不是使用Spring提供的Template操作数据库,使用ORM框架原生的API操作。如果出现异常那么抛出的是ORM的一样,
      无法转换我Spring统一定义的异常。定义一个PersistenceExceptionTranslationPostProcessor的Bean,它是
      一个给声明了@Repository注解的类添加一个后置通知,用于转换SQL的异常为Spring统一的异常。这也是@Repository
      除了声明为一个Component之外的另一个作用。
    ```java
        class PersistenceExceptionConfig{
            @Bean
            public BeanPostProcessor persistenTranscation(){
                return new PersistenceExceptionTranslationPostProcessor();
            }      
        } 
    ```     
    - Hibernate
      
      > Hibernate 是一个ORM框架,提供数据库和pojo的映射。简化了原始的JDBC访问数据库。
      并且扩展JDBC的功能,包括缓存、延迟加载、预先抓取、级联等功能。将构建sql抽象化,自动生
      成SQL,使得访问数据库更专注业务而非SQL的语法。
      
      ###### Spring 为Hibernate提供的SessionFactory
      
      - `LocalSessionFactoryBean:` 适用于Hibernatte3版本,只支持基于XML配置POJO构建SessionFactory(*已弃用*)。
        ````java
            class HibernateConfig {
                @Bean
                public org.springframework.orm.hibernate3.LocalSessionFactoryBean localSessionFactoryBean3(DataSource druidDataSource){
                    org.springframework.orm.hibernate3.LocalSessionFactoryBean sessionFactory = new org.springframework.orm.hibernate3.LocalSessionFactoryBean();
                    sessionFactory.setDataSource(druidDataSource);
                    sessionFactory.setMappingResources("users.hbm.xml");
                    Properties prop = new Properties();
                    sessionFactory.setHibernateProperties(prop);
                    return sessionFactory;
                }
            }
        ````
      - `LocalSessionFactoryBean:` 适用于Hibernatte3版本,只支持基于注解配置POJO构建SessionFactory。(*已弃用*)。      
        ````java
            class HibernateConfig {
                @Bean
                public AnnotationSessionFactoryBean annotationSessionFactoryBean(DataSource druidDataSource){
                        AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
                        sessionFactory.setAnnotatedClasses(Users.class);
                        Properties prop = new Properties();
                        sessionFactory.setHibernateProperties(prop);
                        return sessionFactory;
                    }
              }
        ````  
      - `LocalSessionFactoryBean:` 适用于Hibernatte4以上版本,支持基于XML配置或者注解配置POJO构建SessionFactory。      
        ````java
             class HibernateConfig {
                 public LocalSessionFactoryBean localSessionFactoryBean(DataSource druidDataSource){
                         LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
                         // 配置数据源
                         sessionFactory.setDataSource(druidDataSource);
                         // 配置扫描实体
                         sessionFactory.setPackagesToScan("com.eteng.pojo");
                 //        配置实体映射文件
                 //        sessionFactory.setMappingResources();
                         // 配置hibernate
                         Properties prop = new Properties();
                         prop.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
                         prop.setProperty("hibernate.show_sql","true");
                         prop.setProperty("hibernate.hbm2ddl.auto","update");
                         prop.setProperty("hibernate.current_session_context_class","org.springframework.orm.hibernate4.SpringSessionContext");
                         prop.setProperty("hibernate.format_sql","true");
                         sessionFactory.setHibernateProperties(prop);
                         return sessionFactory;         
                }
             }
         ````
      ###### HibernateTemplate 和 SessionFactory的使用
      > 由于应用程序中Repositroy尽量和Spring解耦。所以在Repositroy实现类尽量少用Spring 提供
        的Template。
      ```java
        @Repository
        public class UserRepository{
        
            @Autowired
            private SessionFactory sessionFactory;
        
            @Autowired
            private HibernateTemplate hibernateTemplate;
        
            @Transactional()
            public String add(){
                Users users  = new Users("zhangsan","123",true);
                hibernateTemplate.save(users);
                return users.getId();
            }
            
            @Transactional
            public Users fetch(){
                Session currentSession = sessionFactory.getCurrentSession();
                Criteria criteria = currentSession.createCriteria(Users.class);
                criteria.setFirstResult(0);
                criteria.setMaxResults(1);
                Users users = (Users)criteria.uniqueResult();
                if(Objects.nonNull(users)){
                    List<OmOrder> orders = users.getOrders();
                    for(OmOrder order : orders){
                        System.out.println(order.getOrderNum());
                    }
                }
                return users;
            }
        }
      ```  
    - JPA
    
    - Spring Data JPA