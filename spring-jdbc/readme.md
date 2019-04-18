#### Spring JDBC
 ##### 数据源
  ###### jdbc驱动数据源
  > 基于jdbc驱动的数据源。这种数据源是没有连接池化,要么一直使用一个连接或者每次访问创建一个连接的。
  - DriverManagerDataSource: 每次访问数据库都创建一个新的连接,没有提供连接的池化管理。
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
  - SimpleDriverDataSource: 和`DriverManagerDataSource`相似,用于解决特定环境的类加载。
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
  - SingleConnectionDataSource: 每次请求都是提供同一个连接,同样是没有提供连接池化的管理。
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
  ###### 外部数据源
   
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
            dataSource.setFilters("stat");
            // 配置合并SQL监控过滤器
            dataSource.setFilters("mergeStat");
            // 配置代理监控过滤器
            dataSource.setProxyFilters(proxyFilters());
            Properties properties = new Properties();
            
            // 配置密码解密秘钥
            properties.setProperty("config.decrypt","true");
            properties.setProperty("config.decrypt.key",publicKey);
            dataSource.setConnectProperties(properties);
            return dataSource;      
        }
    }   
    ````
    
  ###### 内嵌数据库
  > 
  - H2
    
  ##### 基于JDBC访问 
  > Spring 对JDBC有基本的模板代码封装,使得用户只关心数据的访问,如开启事务、异常处理、回滚等
  模板化的代码由模板类实现。

  - JdbcTemplate
      ```java
        class JdbcConfig{   
           /**
            * 配置JDBC模板,基于参数索引进行参数的绑定
            * @param druidDataSource 数据源
            * @return
            */
            @Bean
            public JdbcTemplate jdbcTemplate(DataSource druidDataSource){
                return new JdbcTemplate(druidDataSource);
            }
        }
      ```
  
  - NamedParameterJdbcTemplate 
    ```java
        class JdbcConfig{ 
            /**
             * 配置JDBC模板,基于参数名进行参数的绑定
             * @param druidDataSource
             * @return
             */
            @Bean
            public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource druidDataSource){
                return new NamedParameterJdbcTemplate(druidDataSource);
            }
        }
    ```
  - JdbcTemplate的使用
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
  ##### ORM访问 
   ##### Spring 对Hibernate的支持
   - `支持声明式事务:`Spring 为大多数的ORM框架提供统一的事务管理。
   - `统一的异常处理:`Spring 为众多ORM框架提供Spring统一定义的异常转换功能。
   - `线程安全、轻量的模板类:`Spring 集成了众多ORM框架,并提供操作的模板类，简化代码编写。
   - `DAO支持类:` Spring 为部分的ORM框架提供DAO类的实现，使用户不需要编写DAO实现来访问数据库。
    
   ###### Hibernate 事务管理器
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
   ###### 异常转换
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
   ###### Hibernate
   > Hibernate 是一个ORM框架,提供数据库和pojo的映射。简化了原始的JDBC访问数据库。
   并且扩展JDBC的功能,包括缓存、延迟加载、预先抓取、级联等功能。将构建sql抽象化,自动生
   成SQL,使得访问数据库更专注业务而非SQL的语法。
      
   ###### Spring 为Hibernate提供的SessionFactory
      
   - `LocalSessionFactoryBean:` 适用于Hibernatte3版本,只支持基于XML配置POJO构建SessionFactory(**已弃用**)。
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
   - `LocalSessionFactoryBean:` 适用于Hibernatte3版本,只支持基于注解配置POJO构建SessionFactory。(**已弃用**)。      
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
   ###### JPA
   > JPA(Java Persistence Api) java持久化接口的规范,其中Hibernate是JPA中的
     一个实现(还有其他的框架实现)。Spring给JPA提供了封装,包括模板类、由Spring的
     上下文管理实体管理器等。
   
   ###### 实体管理器
   - 应用实体管理器
     > 由应用程序创建实体管理器工厂，当需要获取实体管理器时。由应用程序创建，并且由
       应用程序管理实体管理器的开启的关闭。一般不适用于J2EE容器环境下运行。
   - 容器实体管理器
     >由J2EE容器创建实体管理器工厂，当需要获取实体管理器时。由J2EE创建，并且由J2EE
      容器管理实体管理器的开启和关闭。
      
   ###### Spring 对 JPA 的支持
   > 无论是应用实体管理器还是容器实体管理器，Spring都可以充当对应的角色对其进行管理。
   - `LocalEntityManagerFactoryBean:` Spring充当应用程序类型实体管理器给JPA提供支持
   ```java
    class JpaConfig{
        /**
         * 配置应用程序类型实体管理器工厂(保证在应用程序下有持久化单元的配置persistence.xml)
         * @return
         */
        @Bean
        public LocalEntityManagerFactoryBean localEntityManagerFactoryBean(){
          LocalEntityManagerFactoryBean entityManagerFactory = new LocalEntityManagerFactoryBean();
          // 配置实体管理器的持久化单元,每个持久化单元持有数据源和实体映射
          entityManagerFactory.setPersistenceUnitName("test_unit");
          return entityManagerFactory;
        }   
    }
   ```
   ```xml
    <persistence xmlns="http://java.sun.com/xml/ns/persistence">
        <persistence-unit name="test_unit">
            <class>com.eteng.pojo.Users</class>
            <class>com.eteng.pojo.OmOrder</class>
            <properties>
                <property name="hibernate.jdbc.driver" value="com.mysql.jdbc.Driver"/>
                <property name="hibernate.jdbc.url" value="jdbc:mysql:"/>
                <property name="hibernate.jdbc.username" value="root"/>
                <property name="hibernate.jdbc.passwrod" value="root"/>
            </properties>
        </persistence-unit>
    </persistence>
   ```
   - `LocalContainerEntityManagerFactoryBean:` Spring充当容器类型实体管理器给JPA提供支持  
   ```java
   class JpaConfig{
        /**
         * 配置容器类型实体管理器工厂
         * @return
         */
        @Bean(name = "entityManagerFactory")
        public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource,JpaVendorAdapter jpaVendorAdapter){
            LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
            // 配置数据源
            entityManagerFactory.setDataSource(dataSource);
            // 配置JPA实现厂商的适配器(适配器由Spring提供),这里Hibernate是JPA的实现
            entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
            // 扫描实体
            entityManagerFactory.setPackagesToScan("com.eteng.pojo");
            return entityManagerFactory;
        }
        
        /**
         * 配置JPA与Hibernate的适配器
         * @return
         */
        @Bean
        public JpaVendorAdapter jpaVendorAdapter(){
            HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
            // 设置数据库
            adapter.setDatabase(Database.MYSQL);
            // 是否执行DDL
            adapter.setGenerateDdl(true);
            // 是否打印SQL
            adapter.setShowSql(true);
            // 设置数据库方言
            adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
            return adapter;
        }
    }
   ``` 
   
   ###### 实体管理器的使用
   > 通过配置将的实体管理器交由Spring管理,那么Spring容器会创建一个EntityManagerFactory。并且通过
     EntityManagerFactory创建EntityManager。由于当前JPA的实现是使用HIbernate,所以通过配置一个
     Hibernate对应的事务管理器即可以通过注解声明式开启事务。
   ```java
     @Repository
     public class JpaUserRepository{
    
        /**
        * 通过@PersistenceUnit(由JPA规范定义,非Spring),注入一个实体管理器工厂
        */
         @PersistenceUnit
         private EntityManagerFactory entityFactory;
        /**
        * 通过@PersistenceContext(由JPA规范定义,非Spring),注入一个实体管理器(线程安全))
        */
         @PersistenceContext
         private EntityManager entity;
        
         /**
         * 通过id查询
         * @param uid
         * @return 
         */
         @Transactional
         public Users getUsersById(String uid){
             return entity.find(Users.class,uid);
         }
         
         /**
          * 通过用户名查询(使用JPQL)
          * @param userName
          * @return 
          */
         @Transactional
         public Users getUserByName(String userName){
             Query query = entity.createQuery("select u from Users u WHERE u.username = :userName");
             query.setParameter("userName",userName);
             query.setFirstResult(0);
             query.setMaxResults(1);
             Users users = (Users)query.getSingleResult();
             return users;
         }
     
         /**
          * 使用构造器方式包装返回值
          * @return
          */
         @Transactional
         public OmOrder getOrderByOrderNum(String orderNum){
             Query query = entity.createQuery("SELECT new com.eteng.pojo.OmOrder(omo.id,omo.orderNum) FROM OmOrder omo WHERE omo.orderNum = :orderNum");
             query.setParameter("orderNum",orderNum);
             query.setFirstResult(0);
             query.setMaxResults(1);
             OmOrder order = (OmOrder)query.getSingleResult();
             return order;
         }
     
         /**
          * 查询已定义列返回数组结构,使用SQL(这种方式只适用于Hibernate)
          * @return
          */
         @Transactional
         public OmOrder getOrderByOrderNumAndUsers(String orderNum,String uid){
             Query query = entity.createNativeQuery("SELECT omo.id,omo.order_num FROM om_order omo WHERE omo.order_num = :orderNum AND users = :users");
             query.setParameter("orderNum",orderNum);
             query.setParameter("users",uid);
             query.setFirstResult(0);
             query.setMaxResults(1);
             // 包装返回结果(通过别名映射)
             query.unwrap(SQLQuery.class)
                     .addScalar("id",StandardBasicTypes.STRING)
                     .addScalar("order_num",StandardBasicTypes.STRING)
                     .setResultTransformer(Transformers.aliasToBean(Order.class));
             OmOrder order = (OmOrder)query.getSingleResult();
             return order;
         }
     }    
   ```  
   `总结:` JPA提供两种的查询语句,一种JPQL,另一种原生SQL。相比提供实现厂商(如Hibernate)的功能相对少。
          因为实现总是可以扩展规范的,JPQL和SQL的查询和Hibernate的HQL和SQL很相似(内部也是使用Hibernate)。
          由于EntityManagerFactory是交由Spring容器管理，所以也支持事务管理。
          
   ###### Spring Data JPA
   >  Spring Data JPA 是 Spring Data项目对JPA开发进行简化。通过自动化实现JpaRepository
      (通过代理对象),用户不需要编写JpaRepository实现对数据库进行操作(CRUD)。实现自动化的虽然
      简化的开发的代码。但是也要遵循Spring Data Jpa的规范才可以实现。Spring Data Jpa 通过接
      口定义每种数据的访问。具体的访问分别由接口的方法签名、@Query、自定义实现方法定义。Spring 
      Data Jpa自动生成JpaRepository代理实现，并对方法签名、@Query、自定义实现方法解析来操作
      数据库。
      
   ###### 引入Spring Data Jpa 模块
   ```xml
   <dependencys>
        <!--spring data core-->
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-commons</artifactId>
            <version>${spring.data.vserion}</version>
        </dependency>
        <!--spring data jpa 模块-->
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
            <version>${spring.data.vserion}</version>
        </dependency>
    </dependencys>
   ```
   ###### 基于注解驱动开启Repository发现
   ```java
   // 开启JpaRepository自动代理
   @org.springframework.context.annotation.Configuration
   @EnableJpaRepositories(transactionManagerRef = "hibernateTransactionManager",basePackages = "com.eteng")
   class SpringDataJpaConfig{
    
   }
   ```
   ###### Spring Data 自动化 JPA
   - 使用方法签名
   > 用户也可以根据Spring Data定义的特定语言(DSL)自定义一个查询的方法名。该语言用
     于接口的方法签名上,Spring Data为该接口产生代理实现。
     
   ```java
       class SpringDataUsersRepositry{
            /**
             * 使用方法签名定义查询,根据继承接口的参数化类型推断返回值。所以方法名可以省略返回值,
             * 查询动词包括get、find、rend、count,get、find、rend是同义词,功能都是相似的。
             * count动词0是统计结果的个数。在By后面是断言(查询条件),多个条件使用AND或者OR相连，
             * 在字段后面可以使用操作符,默认操作符是“=”,例如username like操作的方法名断言为
             * getByUsernameLike(String username)。
             * @param username
             * @return
             */
            @Transactional
            List<Users> getByUsernameIsLike(String username);
       }
   ```  
   - 使用自定义SQL
   > 如果方法签名的方式无法满足查询，可以使用@Query注解自定义JPQL查询。@Query提供两种查询
     语言,分别是JPQL和原生SQL。
   ```java
        class SpringDataUsersRepositry{
            /**
             * 使用@Query注解自定义的SQL代替方法签名方式。该方式解决了使用方法签名无法满足查询和
             * 使用方法签名定义时方法名过长的情况。自定义的SQL可以是JPQL或者SQL，通过设置nativeQuery
             * 属性控制。如果使用参数名的方式填充参数，需要在方法签名的形参上通过@Param注解的value指定
             * 对应的参数。
             * @param enable
             * @param username
             * @return
             */
            @Transactional
            @Query("SELECT u from Users u where u.enable =:enable and u.username =:username")
            Users getUsers(@Param("enable")boolean enable,@Param("username") String username);
        }
   ```  
   - 使用自定义实现
   > 当使用方法签名和自定义SQL无法满足时,使用混合方式。即JpaRepository代理类和手动的实现类混合使用。
   ```java
    public interface JpaUsersExtRepository{
        @Transactional
        List<Users> al();
    }
    
    @Repository
    public class JpaUsersExtRepositoryImpl implements JpaUsersExtRepository{
        
        @PersistenceContext
        private EntityManager entityManager;
    
        public List<Users> al(){
            Query query = entityManager.createQuery("select u from Users u");
            return query.getResultList();
        }
    }
    
    /**
     * 混合使用自定义实现和自动代理实现 
     */
    public interface SpringDataJpaUserRepository extends JpaRepository<Users,Long>,JpaUsersExtRepository{
        // methods
    }
    
    class springDataJpaUserRepositoryTest{
        @Test
        public void testGetALlUsers(){
            List<Users> aLlUsers = repository.al();
        }
    }
   ```
   