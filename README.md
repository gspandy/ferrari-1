# Ferrari说明
#### 如果阅读完文档后，还有任何疑问，请mail to [tengkai.yuan@dianping.com, xueli.xue@dianping.com]

**Ferrari**是一种云调度服务平台，基于quartz实现定时调度。ferrari本身不执行任何任务，只是进行触发任务执行（通过http远程触发）.

## ======Quick Start======
任务应用方只需按照如下步骤，即可接入.
### Step一. 依赖

```
<groupId>com.dianping</groupId>
<artifactId>ferrari-core</artifactId>
<version>1.0.0</version>
```
### Step二. 配置web.xml

```
<servlet>
     <servlet-name>FerrariServlet</servlet-name>
     <servlet-class>com.cip.ferrari.core.FerrariDirectServlet</servlet-class>
     <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
     <servlet-name> FerrariServlet</servlet-name>
     <url-pattern>/ferraricontainer/*</url-pattern>
</servlet-mapping>
```

完成以上2步，接下来在应用中写任务类、方法，部署启动就可。

**注意点:**

应用中的任务类是 ***[多例]***

最后，到任务调度中心(ferrari-admin-web应用)配置新增一个任务即可。


