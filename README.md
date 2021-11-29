# SSMP整合案例
>  本案例根据[黑马程序员SpringBoot2全套视频教程，springboot零基础到项目实战](https://www.bilibili.com/video/BV15b4y1a7yG)实现
## 1.模块创建

1. 创建SpringBoot项目

2. 勾选Spring-MVC和MySQL坐标

3. 在pom.xml中加入Druid，MybatisPlus，Lombok的坐标

   ```xml
   <!--Lombok-->
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
   </dependency>
   <!--Druid-->
   <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>druid-spring-boot-starter</artifactId>
       <version>1.2.8</version>
   </dependency>
   <!--MybatisPlus-->
   <dependency>
       <groupId>com.baomidou</groupId>
       <artifactId>mybatis-plus-boot-starter</artifactId>
       <version>3.4.3.4</version>
   </dependency>
   ```

4. 修改配置文件application.properties为yml格式

5. 在配置文件中设置tomcat端口为80方便访问

   ```yaml
   server:
     port: 80
   ```

6. 模块初始结构如下

   ![image-20211128164530617](https://img.lccyj.ltd/img/image-20211128164530617.png)

## 2. 实体类开发

> **构建数据库环境**
>
> ```SQL
> CREATE DATABASE ssm;
> CREATE TABLE book(
>     id int PRIMARY KEY AUTO_INCREMENT,
>     type varchar(20),
>     name varchar(20) NOT NULL ,
>     description varchar(20)
> )ENGINE =InnoDB DEFAULT CHARSET =utf8;
> ```
>
> **创建实体类**
>
> ```java
> /**
>  * book表实体类
>  * @author 刘淳
>  */
> @Data
> public class Book implements Serializable {
>     @TableId("id")
>     private Integer id;
>     private String type;
>     private String name;
>     private String description;
> }
> ```

## 3. 持久层开发

* **技术实现方案**
  * **MybatisPlus**
  * **Druid**

> **配置数据源与MybatisPlus的id生成策略**
>
> ```yaml
> spring:
>   datasource:
>     druid:
>       driver-class-name: com.mysql.cj.jdbc.Driver
>       url: jdbc:mysql://127.0.0.1:3306/ssm?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false
>       username: root
>       password: root
>   output:
>     ansi:
>       # 开启日志输出支持颜色显示
>       enabled: always
>   application:
>     name: demo-application
>     
> mybatis-plus:
>   global-config:
>     db-config:
>       id-type: auto   # 设置id策略为数据库自增
> ```
>
> **配置日志便于调试**
>
> ```yaml
> mybatis-plus:
>   global-config:
>     db-config:
>       id-type: auto   # 设置id策略为数据库自增
>   configuration:
>     log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
> ```
>
> **Mapper接口开发**
>
> ```java
> /**
>  * Book实体类Mapper接口
>  * @author 刘淳
>  */
> @Mapper
> public interface BookMapper extends BaseMapper<Book> {
> }
> ```
>
> **测试Mapper接口及数据库连接**
>
> ```java
> /**
>  * BookMapper测试类
>  * @author 刘淳
>  */
> @SpringBootTest
> public class BookMapperTest {
>     @Autowired
>     private BookMapper bookMapper;
> 
>     @Test
>     void testBookMapper(){
>         Book book=new Book();
>         book.setName("小王子");
>         book.setType("童话");
>         book.setDescription("启蒙故事，充满道理");
>         int result = bookMapper.insert(book);
>         System.out.println(result);
>     }
> 
> }
> ```
>
> **测试结果**
>
> ```shell
> 18:27:25 [main] INFO  --- ltd.lccyj.mapper.BookMapperTest | Started BookMapperTest in 3.344 seconds (JVM running for 4.614)
> 18:27:25 [main] DEBUG --- ltd.lccyj.mapper.BookMapper.insert | ==>  Preparing: INSERT INTO book ( type, name, description ) VALUES ( ?, ?, ? )
> 18:27:25 [main] DEBUG --- ltd.lccyj.mapper.BookMapper.insert | ==> Parameters: 童话(String), 小王子(String), 启蒙故事，充满道理(String)
> 18:27:25 [main] DEBUG --- ltd.lccyj.mapper.BookMapper.insert | <==    Updates: 1
> ```
>
> **配置分页拦截器**
>
> * MybatisPlus配置类
>
>   ```java
>   /**
>    * MybatisPlus配置类
>    * @author 刘淳
>    */
>   @Configuration
>   public class MybatisPlusConfiguration {
>       @Bean
>       public MybatisPlusInterceptor mybatisPlusInterceptor(){
>           //定义MybatisPlus拦截器
>           MybatisPlusInterceptor interceptor=new MybatisPlusInterceptor();
>           //添加具体拦截器
>           interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
>           return interceptor;
>       }
>   }
>   ```

## 4. 业务层开发

> * MP提供了通用业务层接口`IService<T>`与业务层通用实现类`ServieceImpl<M,T>`
> * 在通用类基础上做功能重载或功能追加
> * 注意重载时不要覆盖原始操作，避免原始提供的功能丢失
>
> **业务层接口开发**
>
> ```java
> /**
>  * Book实体类业务层接口
>  * @author 刘淳
>  */
> public interface BookService extends IService<Book> {
> }
> ```
>
> **业务层实现类开发**
>
> ```java
> /**
>  * Book实体类业务层实现类
>  * @author 刘淳
>  */
> @Service
> public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
> }
> ```

## 5. 表现层开发

> * 基于Restful进行表现层接口开发
> * 使用Postman测试表现层接口功能
>
> **BookController**
>
> ```java
> /**
>  * Book实体类表现层控制器
>  * @author 刘淳
>  */
> @RestController
> @RequestMapping("/books")
> public class BookController {
> 
>     private final BookService bookService;
>     @Autowired
>     public BookController(BookService bookService) {
>         this.bookService = bookService;
>     }
> 
> }
> ```
>
> **getAll**
>
> > 查询所有书籍
>
> ```java
> /**
>  * 获取所有书籍数据
>  * @return 查询到的所有书籍
>  */
> @GetMapping
> public List<Book> getAll(){
>     return bookService.list();
> }
> ```
>
> > 测试接口
>
> ![image-20211128203523585](https://img.lccyj.ltd/img/image-20211128203523585.png)
>
> **getById**
>
> > 根据id查询书籍
>
> ```java
> /**
>  * 通过id查找书籍
>  * @param id 要查找书籍的id
>  * @return 查询到的书籍
>  */
> @GetMapping("/{id}")
> public Book getById(@PathVariable Integer id){
>     return bookService.getById(id);
> }
> ```
>
> > 测试接口
>
> ![image-20211129122704266](https://img.lccyj.ltd/img/image-20211129122704266.png)
>
> **save**
>
> > 添加书籍
>
> ```java
> /**
>  * 添加书籍
>  * @param book 要添加的书籍的数据
>  * @return 成功返回true 失败返回false
>  */
> @PostMapping
> public boolean save(@RequestBody Book book){
>     return bookService.save(book);
> }
> ```
>
> > 测试接口
>
> ![image-20211129130727760](https://img.lccyj.ltd/img/image-20211129130727760.png)
>
> ```shell
> ==>  Preparing: INSERT INTO book ( type, name, description ) VALUES ( ?, ?, ? )
> ==> Parameters: 世界名著(String), 呼啸山庄(String), 经典名著(String)
> <==    Updates: 1
> ```
>
> **update**
>
> > 修改书籍
>
> ```java
> /**
>  * 修改书籍
>  * @param book 要修改的书籍的数据
>  * @return  成功返回true 失败返回false
>  */
> @PutMapping
> public boolean update(@RequestBody Book book){
>     return bookService.updateById(book);
> }
> ```
>
> > 测试接口
>
> ![image-20211129130948717](https://img.lccyj.ltd/img/image-20211129130948717.png)
>
> ```shell
> ==>  Preparing: UPDATE book SET type=?, name=?, description=? WHERE id=?
> ==> Parameters: 世界名著(String), 呼啸山庄(String), 经典世界名著(String), 8(Integer)
> <==    Updates: 1
> ```
>
> **delete**
>
> > 根据id删除书籍
>
> ```java
> /**
>  * 根据id删除书籍
>  * @param id 要删除的书籍的id
>  * @return 成功返回true 失败返回false
>  */
> @DeleteMapping("/{id}")
> public boolean delete(@PathVariable Integer id){
>     return bookService.removeById(id);
> }
> ```
>
> > 测试接口
>
> ![image-20211129131256223](https://img.lccyj.ltd/img/image-20211129131256223.png)
>
> ```shell
> ==>  Preparing: DELETE FROM book WHERE id=?
> ==> Parameters: 8(Integer)
> <==    Updates: 1
> ```
>
> **getByPage**
>
> > 分页查询书籍
>
> * BookService接口
>
>   ```java
>   IPage<Book> getByPage(int currentPage,int pageSize);
>   ```
>
> * BookServiceImpl
>
>   ```java
>   /**
>    * 分页查询
>    *
>    * @param currentPage 当前页码
>    * @param pageSize    每页显示数量
>    * @return 包含所有分页相关数据的Page对象
>    */
>   @Override
>   public IPage<Book> getByPage(int currentPage, int pageSize) {
>       IPage<Book> page=new Page<>(currentPage,pageSize);
>       bookMapper.selectPage(page,null);
>       return page;
>   }
>   ```
>
> * BookController
>
>   ```java
>   /**
>    * 分页查询书籍
>    * @param currentPage 当前页码
>    * @param pageSize 每页显示数量
>    * @return 所有分页相关的数据
>    */
>   @GetMapping("/{currentPage}/{pageSize}")
>   public IPage<Book> getByPage(@PathVariable int currentPage,@PathVariable int pageSize){
>       return bookService.getByPage(currentPage, pageSize);
>   }
>   ```
>
> > 接口测试
>
> ![image-20211129133802998](https://img.lccyj.ltd/img/image-20211129133802998.png)

## 6. 表现层消息一致化

> **设计表现层返回数据的模型类，也称为前后端数据协议**
>
> ```java
> /**
>  * 前后端数据协议
>  * @author 刘淳
>  */
> @Data
> @AllArgsConstructor
> @NoArgsConstructor
> public class Result {
>     boolean flag;  // 结果标志
>     Object data;  //数据
> }
> ```
>
> **优化Controller实现数据统一**
>
> ```java
> /**
>  * Book实体类表现层控制器
>  *
>  * @author 刘淳
>  */
> @RestController
> @RequestMapping("/books")
> public class BookController {
> 
>     private final BookService bookService;
> 
>     @Autowired
>     public BookController(BookService bookService) {
>         this.bookService = bookService;
>     }
> 
>     /**
>      * 获取所有书籍数据
>      */
>     @GetMapping
>     public Result getAll() {
>         return new Result(true, bookService.list());
>     }
> 
>     /**
>      * 通过id查找书籍
>      *
>      * @param id 要查找书籍的id
>      */
>     @GetMapping("/{id}")
>     public Result getById(@PathVariable Integer id) {
>         return new Result(true, bookService.getById(id));
>     }
> 
>     /**
>      * 分页查询书籍
>      *
>      * @param currentPage 当前页码
>      * @param pageSize    每页显示数量
>      */
>     @GetMapping("/{currentPage}/{pageSize}")
>     public Result getByPage(@PathVariable int currentPage, @PathVariable int pageSize) {
>         return new Result(true, bookService.getByPage(currentPage, pageSize));
>     }
> 
>     /**
>      * 添加书籍
>      *
>      * @param book 要添加的书籍的数据
>      */
>     @PostMapping
>     public Result save(@RequestBody Book book) {
>         return new Result(bookService.save(book), null);
>     }
> 
>     /**
>      * 修改书籍
>      *
>      * @param book 要修改的书籍的数据
>      */
>     @PutMapping
>     public Result update(@RequestBody Book book) {
>         return new Result(bookService.updateById(book), null);
>     }
> 
>     /**
>      * 根据id删除书籍
>      *
>      * @param id 要删除的书籍的id
>      */
>     @DeleteMapping("/{id}")
>     public Result delete(@PathVariable Integer id) {
>         return new Result(bookService.removeById(id), null);
>     }
> 
> 
> }
> ```

## 7. 前端环境准备

> **单体工程中页面放在resources目录下的static目录中**
>
> ![image-20211129160711745](https://img.lccyj.ltd/img/image-20211129160711745.png)

## 8. 前后端协议联调

### 列表展示功能

```js
//钩子函数，VUE对象初始化完成后自动执行
created() {
    this.getAll();
},

methods: {
    //列表
    getAll() {
        axios.get("/books").then((res)=>{
            this.dataList=res.data.data;
        })
    },
```

![image-20211129162353194](https://img.lccyj.ltd/img/image-20211129162353194.png)



### 添加功能

```js
//弹出添加窗口
handleCreate() {
    this.dialogFormVisible=true;
    //每次打开窗口都重置表单数据
    this.resetForm();
},
    
//重置表单
resetForm() {
    this.formData={};
},
    
    
//添加
handleAdd () {
    //发送请求添加数据
    axios.post("/books",this.formData).then((res)=>{
        if (res.data.flag){
            //添加成功，关闭弹层
            this.dialogFormVisible=false;
            //提示用户添加成功
            this.$message.success("添加成功")
        }else{
            //添加失败
            this.$message.error("添加失败")
        }
    }).finally(()=>{
        //请求完成后不管成功与否都重新加载数据
        this.getAll();
    })
},
//取消
cancel(){
    //关闭弹层
    this.dialogFormVisible=false;
    this.$message.info("操作取消");
},
```

![image-20211129173603803](https://img.lccyj.ltd/img/image-20211129173603803.png)

![image-20211129173624335](https://img.lccyj.ltd/img/image-20211129173624335.png)

![image-20211129173642887](https://img.lccyj.ltd/img/image-20211129173642887.png)

### 修改功能

```js
//弹出编辑窗口
handleUpdate(row) {
    //发送请求根据id查询数据
    axios.get("/books/"+row.id).then((res)=>{
        if (res.data.flag && res.data.data!=null){
            //查询成功且结果不为null
            //数据传给表单
            this.formData=res.data.data;
            //显示编辑弹层
            this.dialogFormVisible4Edit=true;
        }else {
            //查询失败，提示用户
            this.$message.error("数据同步失败，自动刷新")
        }
    }).finally(()=>{
        //刷新列表
        this.getAll();
    })
},

//修改
handleEdit() {
    //发送请求修改数据
    axios.put("/books",this.formData).then((res)=>{
        //修改成功
        if (res.data.flag){
            //关闭窗口
            this.dialogFormVisible4Edit=false;
            this.$message.success("修改成功")
        }else {
            //修改失败提示用户
            this.$message.error("修改失败，请重试")
        }
    }).finally(()=>{
        //刷新列表
        this.getAll()
    })
},
```

![image-20211129180044215](https://img.lccyj.ltd/img/image-20211129180044215.png)

![image-20211129180057252](https://img.lccyj.ltd/img/image-20211129180057252.png)

## 9. 业务消息一致性处理

###  1. 异常统一处理

> 当出现异常时，后台返回数据将会再次不一致，可定义SpringMVC异常处理器对异常统一处理

* **优化前后端数据交互协议**

  ```java
  /**
   * 前后端数据协议
   *
   * @author 刘淳
   */
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public class Result {
      private boolean flag;  // 结果标志
      private Object data;  //数据
      private String message; //消息
  }
  ```

* **添加SpringMVC的异常处理器**

  ```java
  /**
   * SpringMVC异常处理器
   * @author 刘淳
   */
  @RestControllerAdvice
  public class ProjectException {
  
      //异常处理
      @ExceptionHandler
      public Result doException(Exception ex){
          ex.printStackTrace();
          return new Result(false,null,"服务器出现故障，请稍后再试");
      }
  }
  ```

### 2. 表现层统一处理消息

> 前台不处理提示消息，全部交由后端Controller统一处理

* **优化Controller**

  ```java
  /**
   * Book实体类表现层控制器
   *
   * @author 刘淳
   */
  @RestController
  @RequestMapping("/books")
  public class BookController {
  
      private final BookService bookService;
  
      @Autowired
      public BookController(BookService bookService) {
          this.bookService = bookService;
      }
  
      /**
       * 获取所有书籍数据
       */
      @GetMapping
      public Result getAll() {
          return new Result(true, bookService.list(),null);
      }
  
      /**
       * 通过id查找书籍
       *
       * @param id 要查找书籍的id
       */
      @GetMapping("/{id}")
      public Result getById(@PathVariable Integer id) {
          return new Result(true, bookService.getById(id),null);
      }
  
      /**
       * 分页查询书籍
       *
       * @param currentPage 当前页码
       * @param pageSize    每页显示数量
       */
      @GetMapping("/{currentPage}/{pageSize}")
      public Result getByPage(@PathVariable int currentPage, @PathVariable int pageSize) {
          return new Result(true, bookService.getByPage(currentPage, pageSize),null);
      }
  
      /**
       * 添加书籍
       *
       * @param book 要添加的书籍的数据
       */
      @PostMapping
      public Result save(@RequestBody Book book) {
          boolean flag = bookService.save(book);
          return new Result(flag, null,flag?"添加成功":"添加失败");
      }
  
      /**
       * 修改书籍
       *
       * @param book 要修改的书籍的数据
       */
      @PutMapping
      public Result update(@RequestBody Book book) {
          boolean flag = bookService.updateById(book);
          return new Result(flag, null,flag?"修改成功":"修改失败");
      }
  
      /**
       * 根据id删除书籍
       *
       * @param id 要删除的书籍的id
       */
      @DeleteMapping("/{id}")
      public Result delete(@PathVariable Integer id) {
          boolean flag = bookService.removeById(id);
          return new Result(flag, null,flag?"删除成功":"删除失败");
      }
  
  
  }
  ```

* **优化前端代码**

  ```js
  //钩子函数，VUE对象初始化完成后自动执行
  created() {
      this.getAll();
  },
  
  methods: {
      //列表
      getAll() {
          axios.get("/books").then((res) => {
              this.dataList = res.data.data;
          })
      },
  
      //弹出添加窗口
      handleCreate() {
          this.dialogFormVisible = true;
          //每次打开窗口都重置表单数据
          this.resetForm();
      },
  
      //重置表单
      resetForm() {
          this.formData = {};
      },
  
      //添加
      handleAdd() {
          //发送请求添加数据
          axios.post("/books", this.formData).then((res) => {
              if (res.data.flag) {
                  //添加成功，关闭弹层
                  this.dialogFormVisible = false;
                  //提示用户添加成功
                  this.$message.success(res.data.message)
              } else {
                  //添加失败
                  this.$message.error(res.data.message)
              }
          }).finally(() => {
              //请求完成后不管成功与否都重新加载数据
              this.getAll();
          })
              },
  
      //取消
      cancel() {
          //关闭弹层
          this.dialogFormVisible4Edit = false;
          this.dialogFormVisible = false;
          this.$message.info("操作取消");
      },
      // 删除
      handleDelete(row) {
          this.$confirm("此操作将永久删除数据，是否继续？", "提示", {type: 'info'}).then(() => {
              axios.delete("/books/" + row.id).then((res) => {
                  if (res.data.flag) {
                      this.$message.success(res.data.message)
                  } else {
                      this.$message.error(res.data.message)
                  }
              }).finally(() => {
                  this.getAll();
              })
                  }).catch(() => {
              this.$message.info("取消删除")
          });
      },
  
      //弹出编辑窗口
      handleUpdate(row) {
          //发送请求根据id查询数据
          axios.get("/books/" + row.id).then((res) => {
              if (res.data.flag && res.data.data != null) {
                  //查询成功且结果不为null
                  //数据传给表单
                  this.formData = res.data.data;
                  //显示编辑弹层
                  this.dialogFormVisible4Edit = true;
              } else {
                  //查询失败，提示用户
                  this.$message.error("数据同步失败")
              }
          }).finally(() => {
              //刷新列表
              this.getAll();
          })
              },
  
      //修改
      handleEdit() {
          //发送请求修改数据
          axios.put("/books", this.formData).then((res) => {
              //修改成功
              if (res.data.flag) {
                  //关闭窗口
                  this.dialogFormVisible4Edit = false;
                  this.$message.success(res.data.message)
              } else {
                  //修改失败提示用户
                  this.$message.error(res.data.message)
              }
          }).finally(() => {
              //刷新列表
              this.getAll()
          })
  		},
  ```

## 10. 分页功能实现

```js
//修改getAll方法实现分页列表展示
getAll() {
    //发送请求查询分页数据
    axios.get("/books/"+this.pagination.currentPage+"/"+this.pagination.pageSize).then((res) => {
        //将返回的数据分别赋给分页组件中的对应值
        this.pagination.total=res.data.data.total;
        this.pagination.currentPage=res.data.data.current;
        this.pagination.pageSize=res.data.data.size;
        this.dataList = res.data.data.records;
    })
},

//切换页码
handleCurrentChange(currentPage) {
    //修改页码为当前页码
    this.pagination.currentPage=currentPage;
    //更新数据
    this.getAll();
},
```

![image-20211129194328696](https://img.lccyj.ltd/img/image-20211129194328696.png)

![image-20211129194349536](https://img.lccyj.ltd/img/image-20211129194349536.png)

### 删除功能异常解决

> 上述分页操作会出现一个问题，当我们处于最后一页，且这一页只有一条数据时，我们将其删
>
> 除，但当前页码仍然为之前的页码，这会导致我们停留在一个空页，此时可以修改Controller代
>
> 码解决这个问题
>
> ```java
> /**
>  * 分页查询书籍
>  *
>  * @param currentPage 当前页码
>  * @param pageSize    每页显示数量
>  */
> @GetMapping("/{currentPage}/{pageSize}")
> public Result getByPage(@PathVariable int currentPage, @PathVariable int pageSize) {
>     IPage<Book> page = bookService.getByPage(currentPage, pageSize);
>     while (page.getCurrent()>page.getPages()){
>         //当前页码数大于总页码数时，使用总页码数再执行一次查询
>         page = bookService.getByPage((int)page.getPages(), pageSize);
>     }
>     return new Result(true,page,null);
> }
> ```

## 11.条件查询功能

* 前端绑定数据到分页数据模型

  ![image-20211129204649391](https://img.lccyj.ltd/img/image-20211129204649391.png)

  ![image-20211129204626449](https://img.lccyj.ltd/img/image-20211129204626449.png)

* 修改getAll方法

  ```js
  //分页列表展示
  getAll() {
      //拼接查询条件字符串
      let params="?type="+this.pagination.type;
      params+="&name="+this.pagination.name;
      params+="&description="+this.pagination.description;
      console.log(params);
      //发送请求查询分页数据
      axios.get("/books/"+this.pagination.currentPage+"/"+this.pagination.pageSize+params).then((res) => {
          //将返回的数据分别赋给分页组件中的对应值
          this.pagination.total=res.data.data.total;
          this.pagination.currentPage=res.data.data.current;
          this.pagination.pageSize=res.data.data.size;
          this.dataList = res.data.data.records;
      })
  },
  ```

* Service接口及实现

  ```java
  /**
   * 条件分页查询
   *
   * @param currentPage 当前页码
   * @param pageSize 每页显示数量
   * @param book 查询条件
   * @return 包含所有分页相关数据的Page对象
   */
  IPage<Book> getByPage(int currentPage, int pageSize,Book book);
  ```

  ```java
  /**
   * 条件分页查询
   *
   * @param currentPage 当前页码
   * @param pageSize    每页显示数量
   * @param book        查询条件
   * @return 包含所有分页相关数据的Page对象
   */
  @Override
  public IPage<Book> getByPage(int currentPage, int pageSize, Book book) {
      IPage<Book> page = new Page<>(currentPage, pageSize);
      LambdaQueryWrapper<Book> wrapper=new LambdaQueryWrapper<>();
      wrapper.like(Strings.isNotEmpty(book.getType()),Book::getType,book.getType());
      wrapper.like(Strings.isNotEmpty(book.getName()),Book::getName,book.getName());
      wrapper.like(Strings.isNotEmpty(book.getDescription()),Book::getDescription,book.getDescription());
      bookMapper.selectPage(page,wrapper);
      return page;
  }
  ```

* Controller

  ```java
  /**
   * 分页条件查询
   *
   * @param currentPage 当前页码
   * @param pageSize  每页显示数量
   * @param book 查询条件
   */
  @GetMapping("/{currentPage}/{pageSize}")
  public Result getByPage(@PathVariable int currentPage, @PathVariable int pageSize, Book book) {
      IPage<Book> page = bookService.getByPage(currentPage, pageSize,book);
      while (page.getCurrent()>page.getPages()){
          //当前页码数大于总页码数时，使用总页码数再执行一次查询
          page = bookService.getByPage((int)page.getPages(), pageSize);
      }
      return new Result(true,page,null);
  }
  ```

![image-20211129205107887](https://img.lccyj.ltd/img/image-20211129205107887.png)



