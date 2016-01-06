<!DOCTYPE html>
<html>
<head>
  	<title>调度中心</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>使用说明</h1>
			<!--
			<ol class="breadcrumb">
				<li><a><i class="fa fa-dashboard"></i>调度中心</a></li>
				<li class="active">使用教程</li>
			</ol>
			-->
		</section>

		<!-- Main content -->
		<section class="content">
			<div class="callout callout-info">
				<h4>简介：FERRARI</h4>
				<p>基于quartz封装实现的任务集群调度管理平台.</p>
				<p>1、简单：通过Web页面对任务进行操作，简单易上手.</p>
				<p>2、动态：支持动态修改任务状态，暂停/恢复/执行任务，即时生效.</p>
				<p>3、集群：任务信息持久化到db中，支持Job服务器集群(高可用)，一个任务只会在其中一台服务器上执行.</p>
            </div>
            
            <!--
            <div class="callout callout-default">
				<h4>特点：</h4>
				<p>1、简单：支持通过Web页面对任务进行CRUD操作，操作简单，一分钟上手.</p>
				<p>2、动态：支持动态修改任务状态，动态暂停/恢复任务，即时生效.</p>
				<p>3、集群：任务信息持久化到db中，支持Job服务器集群(高可用)，一个任务只会在其中一台服务器上执行.</p>
            </div>
            
            <div class="callout callout-default">
				<h4>分层模型：</h4>
				<p>1、基础：基于quartz封装调度层，通过CRONTAB自定义任务执行时间，最终执行自定义JobBean的execute方法，如需多个任务，需要开发多个JobBean实现.</p>
				<p>2、分层：上述基础调度模型存在一定局限，调度层和任务层耦合，当新任务上线势必影响任务的正常调度，因此规划将调度系统分层为：调度层 + 任务层 + 通讯层.</p>
				<p>
				 	<div class="row">
				      	<div class="col-xs-offset-1 col-xs-11">
				      		<p>》调度模块：维护任务的调度信息，负责定时/周期性的发出调度请求.</p>
							<p>》任务模块：具体的任务逻辑，负责接收调度模块的调度请求，执行任务逻辑.</p>
							<p>》通讯模块：负责调度模块和任务模块之间的通讯.</p>
							<p>(总而言之，一条完整任务由 “调度信息” 和 “任务信息” 组成.)</p>
				      	</div>      
			   		</div>
				</p>
            </div>
            -->
            
            <div class="callout callout-default">
				<h4>新增任务属性说明</h4>
				<p>1、任务Key【必填】：任务名称的全局唯一标识.</p>
				<p>2、任务Crontab【必填】：任务执行的时间表达式.</p>
				<p>3、任务描述【必填】：任务的简述.</p>
				<p>4、任务机器【必填】：任务所在机器的ip地址，比如127.0.0.1:8080.</p>
				<p>5、期望执行的类名【必填】：任务class名，包含package.</p>
				<p>6、期望执行的方法【必填】：方法名.</p>
				<p>7、方法入参【选填】：方法入参，多个参数用,分隔.</p>
            </div>
            
            <div class="callout callout-default">
				<h4>任务应用方接入说明</h4>
				<p>1、maven依赖:<br/>
				&ltdependency><br/>
    				&nbsp;&nbsp;&nbsp;&nbsp; &ltgroupId>com.dianping&lt/groupId><br/>
    				&nbsp;&nbsp;&nbsp;&nbsp; &ltartifactId>ferrari-core&lt/artifactId><br/>
    				&nbsp;&nbsp;&nbsp;&nbsp; &ltversion>1.0.0-SNAPSHOT&lt/version><br/>
   	 			&lt/dependency>
				</p>
				<p>2、web.xml配置servlet入口:<br/>
				&ltservlet><br/>
				&nbsp;&nbsp;&nbsp;&nbsp; &ltservlet-name>FerrariServlet&lt/servlet-name><br/>
				&nbsp;&nbsp;&nbsp;&nbsp; &ltservlet-class>com.cip.ferrari.core.FerrariDirectServlet&lt/servlet-class><br/>
				&nbsp;&nbsp;&nbsp;&nbsp; &ltload-on-startup>1&lt/load-on-startup><br/>
				&lt/servlet><br/>

				&ltservlet-mapping><br/>
				&nbsp;&nbsp;&nbsp;&nbsp; &ltservlet-name> FerrariServlet&lt/servlet-name><br/>
				&nbsp;&nbsp;&nbsp;&nbsp; &lturl-pattern>/ferraricontainer/*&lt/url-pattern><br/>
				&lt/servlet-mapping>
				</p>
				<p>3、开始写你的任务类及方法，类名、方法、入参在新增任务时配置</p>
            </div>
            
            <div class="callout callout-default">
				<h4>联系我们</h4>
				<p>xueli.xue@dianping.com</p>
				<p>tengkai.yuan@dianping.com</p>
            </div>
		</section>
		<!-- /.content -->
	</div>
	<!-- /.content-wrapper -->
	
	<!-- footer -->
	<@netCommon.commonFooter />
	<!-- control -->
	<@netCommon.commonControl />
</div>
<@netCommon.commonScript />
</body>
</html>
