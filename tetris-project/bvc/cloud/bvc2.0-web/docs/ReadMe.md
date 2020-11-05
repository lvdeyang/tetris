# Read Me

- 本工程为bvc2.0搭建的基础工程

- 运行环境：myEcplise2016 2016 CI 6；Maven工程

- 项目使用一些注解，需要在myElispe中安装lombok插件

  - 方法一：双击lombok-1.16.10.jar，指定myeclipse安装位置，重启myeclipse

  - 方法二：

    ①手动将lombok-1.16.10.jar拷贝到myeclipse安装的根目录（和myeclipse.ini同级），并改名为lombok.jar

    ②编辑myeclipse.ini，在文件后面追加两行，并保存：

    ```
    -Xbootclasspath/a:lombok.jar
    -javaagent:lombok.jar
    ```

    ③重启myeclipse

- 启动spring-boot的bvc2.0-web工程，需导入venus资源层（svn://192.165.152.13/sumard5/Project/Venus/products/resource）和消息服务的工程（svn://192.165.152.13/sumard5/Project/Venus/products/mq）

  - 配置消息队列:/venus-message-service/src/main/resources/properties

    registerStatus.properties，配置可用的ActiveMQ的ip，port

    ```
    #============================================================================
    # Properties
    #============================================================================
    MQ.BeUse = true
    MQ.ServerIP = 10.10.40.24
    MQ.ServerPort = 61616
    MQ.FileFtpUrl = ftp://admin:admin@10.10.40.52:2121/activemq-file/
    fileDownloadUrl =d\:\\testMqFile\\
    ```

    

  

  