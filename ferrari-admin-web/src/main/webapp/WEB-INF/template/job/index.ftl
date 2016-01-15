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
		<section class="content-header"><h1>调度管理</h1></section>
		
		<!-- Main content -->
	    <section class="content">
	    	<div class="row">
	    		<div class="col-xs-4">
	              	<div class="input-group">
	                	<span class="input-group-addon">任务组</span>
                		<select class="form-control" id="jobGroup" >
                			<#list groupEnum as group>
                				<option value="${group}" >${group.desc}</option>
                			</#list>
	                  	</select>
	              	</div>
	            </div>
	            <div class="col-xs-4">
	              	<div class="input-group">
	                	<span class="input-group-addon">任务名</span>
	                	<input type="text" class="form-control" id="jobName" autocomplete="on" >
	              	</div>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-info" id="searchBtn">搜索</button>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-warning addFerrari" type="button">+新增任务</button>
	            	<!-- <button class="btn btn-info btn-xs add" type="button">+新增任务[ferrari-client]</button> -->
	            </div>
          	</div>
	    
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-body">
			              	<table id="job_list" class="table table-bordered table-striped">
				                <thead>
					            	<tr>
					            		<th class="id" >id</th>
					            		<th class="addTime" >创建时间</th>
					            		<th class="updateTime" >更新时间</th>
					            		<th class="jobGroup" >任务组</th>
					                	<th class="jobName" >任务名</th>
					                  	<th class="jobKey" >任务key</th>
					                  	<th class="jobDesc" >描述</th>
					                  	<th class="owner" >负责人</th>
					                  	<th class="mailReceives" >邮件联系人</th>
					                  	<th class="failAlarmNum" >报警阀值</th>
					                  	<th class="isDeleted" >是否已删除</th>
					                  	<th class="jobCron" >Cron</th>
					                  	<th class="jobClass" >任务类</th>
					                  	<th class="jobData" >任务数据</th>
					                  	<th class="jobStatus" >状态</th>
					                  	<th class="操作" >操作</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
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
						<label for="lastname" class="col-sm-3 control-label">任务Cron</label>
						<div class="col-sm-9"><input type="text" class="form-control" name="cronExpression" placeholder="请输入任务Cron表达式[允许修改]" maxlength="100" ></div>
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
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增任务调度信息</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">任务组</label>
						<div class="col-sm-4">
							<select class="form-control" name="jobGroup" >
		            			<#list groupEnum as group>
		            				<option value="${group}" >${group.desc}</option>
		            			</#list>
		                  	</select>
						</div>
						
						<label for="firstname" class="col-sm-2 control-label">任务名</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="jobName" placeholder="请输入任务Key" minlength="4" maxlength="100" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">Cron</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="cronExpression" placeholder="请输入任务Cron" maxlength="100" ></div>
						
						<label for="lastname" class="col-sm-2 control-label">描述</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="job_desc" placeholder="任务描述" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">执行机器地址</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="job_address" placeholder="机器地址IP:PORT" maxlength="200" ></div>
						<label for="lastname" class="col-sm-2 control-label">执行类</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="run_class" placeholder="执行类" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">执行方法</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="run_method" placeholder="执行方法" maxlength="200" ></div>
						<label for="lastname" class="col-sm-2 control-label">执行方法入参</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="run_method_args" placeholder="执行方法入参" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">负责人</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="owner" placeholder="负责人" maxlength="200" ></div>
						<label for="lastname" class="col-sm-2 control-label">邮件联系人</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="mailReceives" placeholder="邮件联系人，多个用,分隔" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">连续报警阀值</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="failAlarmNum" placeholder="连续失败次数报警阀值" maxlength="200" ></div>
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
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >编辑任务信息</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<input type="hidden" name="triggerKeyName"  readonly>
					<div class="form-group">
						<label for="firstname" class="col-sm-2 control-label">任务组</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="jobGroup"  minlength="4" maxlength="100" readonly></div>
						<label for="firstname" class="col-sm-2 control-label">任务名</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="jobName"  minlength="4" maxlength="100" readonly></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">Cron</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="cronExpression" placeholder="请输入任务Cron" maxlength="100" ></div>
						
						<label for="lastname" class="col-sm-2 control-label">描述</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="job_desc" placeholder="任务描述" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">执行机器地址</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="job_address" placeholder="机器地址IP:PORT" maxlength="200" ></div>
						<label for="lastname" class="col-sm-2 control-label">执行类</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="run_class" placeholder="执行类" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">执行方法</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="run_method" placeholder="执行方法" maxlength="200" ></div>
						<label for="lastname" class="col-sm-2 control-label">执行方法入参</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="run_method_args" placeholder="执行方法入参" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">负责人</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="owner" placeholder="负责人" maxlength="200" ></div>
						<label for="lastname" class="col-sm-2 control-label">邮件联系人</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="mailReceives" placeholder="邮件联系人，多个用,分隔" maxlength="200" ></div>
					</div>
					<div class="form-group">
						<label for="lastname" class="col-sm-2 control-label">连续报警阀值</label>
						<div class="col-sm-4"><input type="text" class="form-control" name="failAlarmNum" placeholder="连续失败次数报警阀值" maxlength="200" ></div>
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
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script>var base_url = '${request.contextPath}';</script>
<script src="${request.contextPath}/static/js/job.index.1.js"></script>
</body>
</html>
