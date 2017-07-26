# 安卓校园问答应用服务器使用文档

## 概述
这是软件工程大实验。    
需求是做一个Android APP，该App是一个以`兴趣`和`问题`驱动的知识咨询共享服务平台。这个文档描述的是该应用的服务器部分。服务器使用JAVA编写，提供RESTful API供前端使用。

## 使用说明
#### 1. 准备
安装[Tomcat-8.5.16](http://tomcat.apache.org/download-80.cgi)     
安装[MySQL5](https://dev.mysql.com/downloads/mysql/)      
数据库配置:      
1. 配置hibernate, hibernate的配置文件是src\main\resources\hibernate.cfg.xml     
```xml
    <property name="connection.username">root</property>
    <property name="connection.password">root</property>
```
把这两项设置为你的MySQL数据库的用户名和密码        
2. 配置MySQL:     
进入MySQL，创建数据库:      
```bash 
mysql> CREATE DATABASE IF NOT EXISTS seg default charset=utf8;
```


#### 2. 启动
在根目录下输入命令       
```bash
gradle tomcatRun
```

## 使用的开源框架和插件
#### 1. SpringMVC        
    Spring MVC属于SpringFrameWork的后续产品，已经融合在Spring Web Flow里面。Spring 框架提供了构建 Web 应用程序的全功能 MVC 模块。

#### 2. Hibernate        
    Hibernate是一个开放源代码的对象关系映射框架，它对JDBC进行了非常轻量级的对象封装，它将POJO与数据库表建立映射关系，是一个全自动的orm框架，hibernate可以自动生成SQL语句，自动执行，使得Java程序员可以随心所欲的使用对象编程思维来操纵数据库。 Hibernate可以应用在任何使用JDBC的场合，既可以在Java的客户端程序使用，也可以在Servlet/JSP的Web应用中使用，最具革命意义的是，Hibernate可以在应用EJB的J2EE架构中取代CMP，完成数据持久化的重任。

#### 3. Gradle
    Gradle是一个基于Apache Ant和Apache Maven概念的项目自动化构建工具。它使用一种基于Groovy的特定领域语言(DSL)来声明项目设置，抛弃了基于XML的各种繁琐配置。

#### 4. Lucene
    Lucene是apache软件基金会4 jakarta项目组的一个子项目，是一个开放源代码的全文检索引擎工具包，但它不是一个完整的全文检索引擎，而是一个全文检索引擎的架构，提供了完整的查询引擎和索引引擎，部分文本分析引擎（英文与德文两种西方语言）。Lucene的目的是为软件开发人员提供一个简单易用的工具包，以方便的在目标系统中实现全文检索的功能，或者是以此为基础建立起完整的全文检索引擎。Lucene是一套用于全文检索和搜寻的开源程式库，由Apache软件基金会支持和提供。Lucene提供了一个简单却强大的应用程式接口，能够做全文索引和搜寻。在Java开发环境里Lucene是一个成熟的免费开源工具。就其本身而言，Lucene是当前以及最近几年最受欢迎的免费Java信息检索程序库。

#### 5. 极光推送(JPush)
    JPush是经过考验的大规模APP推送平台，每天推送消息数超过5亿条。 开发者集成SDK后，可以通过调用API推送消息。同时，JPush提供可视化的web端控制台发送通知，统计分析推送效果。 JPush全面支持 Android, iOS, Winphone 三大手机平台。JPush提供四种消息形式：通知，自定义消息，富媒体和本地通知。