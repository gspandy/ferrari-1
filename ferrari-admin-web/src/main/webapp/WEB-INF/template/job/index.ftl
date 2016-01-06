<!DOCTYPE html>
<html>
<head>
  	<title>调度中心</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">
  
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
			<h1>调度管理</h1>
			<!-- 
			<ol class="breadcrumb">
				<li><a><i class="fa fa-dashboard"></i>调度管理</a></li>
				<li class="active">调度中心</li>
			</ol>
			-->
		</section>
		
		<!-- Main content -->
	    <section class="content">
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-header">
			            	<h3 class="box-title">调度列表</h3>
			            	<button class="btn btn-info btn-xs addFerrari" type="button">+新增任务</button>
			            	<#-- <button class="btn btn-info btn-xs add" type="button">+新增任务[ferrari-client]</button> -->
			            </div>
			            <div class="box-body">
			              	<table id="job_list" class="table table-bordered table-striped">
				                <thead>
					            	<tr>
					                	<th>调度key</th>
					                  	<th>cron</th>
					                  	<!--<th>类路径</th>-->
					                  	<th>参数</th>
					                  	<th>状态</th>
					                  	<th>操作</th>
					                </tr>
				                </thead>
				                <tbody>
			                		<#if jobList?exists && jobList?size gt 0>
									<#list jobList as item>
									<tr>
					            		<td>${item['TriggerKey'].name}</td>
					                  	<td>${item['Trigger'].cronExpression}</td>
					                  	<!--<td>${item['JobDetail'].jobClass}</td>-->
					                  	<td>
					                  		<#assign jobDataMap = item['JobDetail'].jobDataMap />
					                  		<#if jobDataMap?exists && jobDataMap?keys?size gt 0>
					                  			<#list jobDataMap?keys as key>
					                  				${key}	=	${jobDataMap[key]}	<br>
					                  			</#list>
					                  		</#if>
					                  	</td>
					                  	<td state="${item['TriggerState']}" >
					                  		<#if item['TriggerState'] == 'NORMAL'>
					                  			<button class="btn btn-block btn-success" type="button">SCHEDULED</button>
					                  		<#elseif item['TriggerState'] == 'PAUSED'>
					                  			<button class="btn btn-block btn-warning" type="button">SUSPEND</button>
					                  		<#else>
					                  			<button class="btn btn-block" type="button">${item['TriggerState']}</button>
					                  		</#if>
					                  	</td>
					                  	<td>
											<p name="${item['TriggerKey'].name}" group="${item['TriggerKey'].group}" 
												cronExpression="${item['Trigger'].cronExpression}" jobClassName="${item['JobDetail'].jobClass}" jobDesc="${job_desc?if_exists}" >
												<#if item['TriggerState'] == 'NORMAL'>
													<button class="btn btn-info btn-xs job_operate" type="job_pause" type="button">暂停</button>
												<#elseif item['TriggerState'] == 'PAUSED'>
													<button class="btn btn-info btn-xs job_operate" type="job_resume" type="button">恢复</button>
												</#if>
												<button class="btn btn-info btn-xs job_operate" type="job_trigger" type="button">执行</button>
												<button class="btn btn-info btn-xs update" type="button">更新cron</button>
											  	<button class="btn btn-danger btn-xs job_operate" type="job_del" type="button">删除</button>
											  	<button class="btn btn-warning btn-xs" type="job_del" type="button" 
											  		onclick="javascript:window.open('${request.contextPath}/joblog?jobName=${item['TriggerKey'].name}')" >运行历史</button>
											</p>
					                  	</td>
					                </tr>
									</#list>
									</#if>
				                </tbody>
				                <tfoot>
					            	<tr>
					                  	<th>调度key</th>
					                  	<th>cron</th>
					                  	<!--<th>类路径</th>-->
					                  	<th>参数</th>
					                  	<th>状态</th>
					                  	<th>操作</th>
					                </tr>
				                </tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
	<!-- control -->
	<@netCommon.commonControl />
</div>

<!-- job新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增调度信息</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
						<label for="firstname" class="col-sm-3 control-label">任务Key</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="triggerKeyName" placeholder="请输入任务Key(全局唯一标识)" minlength="4" maxlength="100" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">任务Crontab</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="cronExpression" placeholder="请输入任务Crontab表达式[允许修改]" maxlength="100" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">任务描述</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="job_desc" placeholder="请输入任务描述[不支持修改]" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">任务URL</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="job_url" placeholder="请输入任务URL[不支持修改]" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">任务handler</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="handleName" placeholder="请输入任务handler[不支持修改]" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<button type="submit" class="btn btn-primary">保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
							<button type="button" class="btn btn-info pull-right addParam">+ arg</button>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- job新增.模态框[点评Ferrari定制任务] -->
<div class="modal fade" id="addFerrariModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增任务调度信息</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
						<label for="firstname" class="col-sm-3 control-label">任务Key</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="triggerKeyName" placeholder="请输入任务Key[不支持修改,全局唯一标识]" minlength="4" maxlength="100" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">任务Crontab</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="cronExpression" placeholder="请输入任务Cron[允许修改]" maxlength="100" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">任务描述</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="job_desc" placeholder="任务描述[不支持修改]" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">任务机器</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="job_address" placeholder="机器地址IP:PORT[不支持修改]" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">期望执行的类名</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="run_class" placeholder="期望执行的类,包含package[不支持修改]" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">期望执行的方法</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="run_method" placeholder="期望运行的方法[不支持修改]" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-3 control-label">方法入参</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="run_method_args" placeholder="方法入参，多个参数用,分隔[不支持修改,可以为空]" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-9">
							<button type="submit" class="btn btn-primary">保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >更新cron</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">任务Key</label>
						<div class="col-sm-10"><input type="text" class="form-control" name="triggerKeyName" placeholder="请输入任务Key" minlength="4" maxlength="100" readonly ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">任务Cron</label>
						<div class="col-sm-10"><input type="text" class="form-control" name="cronExpression" placeholder="请输入任务Cron" maxlength="100" ></div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<@netCommon.commonScript />
<@netCommon.comAlert />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script>var base_url = '${request.contextPath}';</script>
<script src="${request.contextPath}/static/js/job.index.1.js"></script>
</body>
</html>
