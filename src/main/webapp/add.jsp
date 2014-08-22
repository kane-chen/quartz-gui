<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
function addOneJobData(){
	var index = $("#jobDataMapDataId tr").length;
	$("#jobDataMapDataId").append("<tr id='jobDatasTr'><td><input type='text' class='required' name='jobDataMap["+index+"].key' /></td><td><input type='text' class='required' name='jobDataMap["+index+"].value' /></td><td align='center'><input type='button' value='rem' onclick='deletecurtr(this);' /></td></tr>");
}
function deletecurtr(obj){
	$(obj).parent().parent().remove();  //删除当前行
}

</script>
</head>
<body>
	<form action="add.xhtml">
		<table>
			<tr>
				<td>jobName</td>
				<td><input type="text" name="jobName"/></td>
			</tr>		
			<tr>
				<td>groupName</td>
				<td><input type="text" name="groupName"/></td>
			</tr>		
			<tr>
				<td>jobClassName</td>
				<td><input type="text" name="jobClassName"/></td>
			</tr>		
			<tr>
				<td>cronExp</td>
				<td><input type="text" name="cronExp"/></td>
			</tr>
			<tr>
				<td>parameters</td>
				<td>
					<table width="400" id="jobDatasTable">
					<thead>
						<tr><td><input type="button" value="add"  onclick="addOneJobData();" ></td></tr>
						<tr>
							<td align="center" width="33%">jobDataMapKey</td>
							<td align="center" width="33%">jobDataMapValue</td>
							<td align="center" width="33%">remove</td>
						</tr>
					</thead>
					<tbody id="jobDataMapDataId">
					</tbody>
				</table>
				</td>
			</tr>		
			<tr>
				<td colspan="2">
					<input type="submit">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>