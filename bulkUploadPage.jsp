<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="d" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<meta charset="utf-8">
<script type="text/JavaScript"> _helpId=425; </script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bulk Merchant Upload</title>
<style type="text/css">
  #waitDiv {
    display: none;
    text-align: center;
    padding: 20px;
    font-weight: bold;
  }
  #waitDiv img {
	width: 50px;
    height: 50px;
    margin-top: 10px;
  }
</style>
<script type="text/JavaScript"> 
	$(function(){
		$('#selExtract').selectmenu({
			change : function(){	
				var uploadType = document.getElementById("selExtract").value;
				if(uploadType == 20 || uploadType == 21){
					document.getElementById("uploadFile").value ='';
					//$(".textfield").show();
					$(".uploadfield").hide();
				}else if(uploadType == 24){
					//document.getElementById("searchValue").value ='';
					//$(".textfield").hide();
					$(".uploadfield").show();
				}else{
					$(".uploadfield").hide();
					//$(".textfield").hide();
				}
				setuploadFileName();
			}
		});
	});
	
	function OnLoad(){
		document.getElementById("selExtract").value ='0';
		$(".uploadfield").hide();
		//$(".textfield").hide();
	}
	
	function setuploadFileName(){
		var uploadFile = document.getElementById("uploadFile").value;
		var filename = uploadFile.replace("C:\\fakepath\\", "");
		document.getElementById("uploadFileName").value = filename;
	}
	
	function OnSubmit(){
		var uploadType = document.getElementById("selExtract").value;
		if(uploadType == 0){
			alert("Please select Upload Category...");
			return false;
		}else if(uploadType == 24){
			var uploadFile = document.getElementById("uploadFile").value;
			if (!uploadFile) {
				alert("Please select a file.");
				return false;
			}
			var f = uploadFile.split(".");
			if(uploadFile == ''){
				return false;
			}
			if(uploadFile.split(".")[f.length - 1] == 'xls' || uploadFile.split(".")[f.length - 1] == 'xlsx'){
				//return true;
				$("#waitDiv").show();
				$("#submitBtn").prop("disabled", true).val("Processing...");
				return true;
			}else{
				alert("It should support only xls and xlsx file...");
				return false;
			}
		}		
	}
	
	<%--  function showSuccessRecords(filename, uploadtype)
	 {	 
		window.open("<%=request.getContextPath()%>/merchantsales/showBulkUploadReportDetails.action?reportType=SUCCESS&fileName="+filename+"&uploadType="+uploadtype,'popup', 'top=50,left=100,scrollbars=1,width=1100,height=600');
	 }
	 
	 function showErrorRecords(filename, uploadtype)
	 {	 
		window.open("<%=request.getContextPath()%>/merchantsales/showBulkUploadReportDetails.action?reportType=FAILURE&fileName="+filename+"&uploadType="+uploadtype,'popup', 'top=50,left=100,scrollbars=1,width=1100,height=600');
	 } --%>
</script>
</head>
<body onload="OnLoad()">
<section class="section-bg">
<div class="clearfix"></div>
<div class="box1">
<div id="form-container" align="left">
<s:form action="uploadBulkMerchantOnboard" method="post" cssClass="form-container" enctype="multipart/form-data" onsubmit="return OnSubmit();">	
			<div class="box-head1" align="left" ><s:text name="Bulk Merchant Upload" /></div>
			<div class="clearfix"></div>
			<div class="container-fuild">
				<table width="80%" border="0" align="center">
					<tr>			
						<td colspan="2" class="error">
							<s:fielderror />
							<s:actionerror />
						</td>			
					</tr>
					<tr>			
					<td colspan="2" class="actMessage">
						<s:actionmessage/>
					</td>			
				</tr>
				</table>
			</div>
			<div class="container-fuild">
		<table width="80%" align="center" cellspacing="4" cellpadding="4">
			<div style="text-align: right;">
    			<a href="<%=request.getContextPath() %>/sampletemplate/BULKUpload_YYYYMMDDHHMMSSSSS.xlsx" download="BULKUpload_YYYYMMDDHHMMSSSSS.xlsx">Click to download template</a>
			</div>
			<%-- <div style="text-align: right;">
			<a href="<%=request.getContextPath() %>/sampletemplate/BULKUpload_yyyyMMddHHmmssSSS.xlsx" download="BULKUpload_yyyyMMddHHmmssSSS.xlsx">
    			<img src="${pageContext.request.contextPath}/images/download-image.png" alt="Download" style="width: 35px; height: 35px; cursor: pointer; transition: filter 0.3s;"/>
			</a>- Click to download Sample Bulk Upload File </div> --%>
			
			<tr align="center">
        		<td width="26%"></td>
				<td width="16%"><label><s:text name="Upload Category"/></label>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
				</td>
				<td align="left">
					<%-- <s:select name="uploadType" id="selExtract" class="select_styled"
						list="#{'21':'Send Welcome Mail','24':'Bulk Merchant Onboard'}"
						headerKey="0" headerValue="--Select--">
					</s:select> --%>
					<s:select name="uploadType" id="selExtract" class="select_styled"
						list="#{'24':'Bulk Merchant Onboard'}"
						headerKey="0" headerValue="--Select--">
					</s:select>
				</td>
				<td></td>
			</tr>
			<tr align="center" class="uploadfield">
        		<td width="26%"></td>
				<td width="16%"><label>File</label></td>
				<td align="left">
					<s:file name="uploadFile" value="" id="uploadFile" onchange="setuploadFileName()" style="width: 225px;padding: 5px;margin: 0px;" />
				</td>
				<td></td>
			</tr>
			<%-- <tr align="center" class="textfield">
        		<td width="26%"></td>
				<td width="16%"><label><s:text name="Merchant/Store Code" /></label></span>
				</td>
				<td align="left">
					<input type="text" name="searchValue" id="searchValue">
				</td>
				<td></td>
			</tr> --%>
           
          </table>
	     </div>
				<div class="rowSubmit" style="margin-top: 15px;" align="center">
					<table border="0" align="center" cellspacing="4" cellpadding="4">
						<tr>
							<td  colspan="2" align="center">
								<%-- <s:submit name="submit" value="Process" onclick="return OnSubmit();" cssClass="btn btn-primary" /> --%>
								<s:submit name="submit" id="submitBtn" value="Process" cssClass="btn btn-primary" />
							</td>
						</tr>
					</table>
					
					<%-- <div id="waitDiv" style="display:none; text-align:center; margin-top: 10px;">
						<h3>Please wait.........<br>
						Your Request is being processed.<br></h3>
						<img src="<%=request.getContextPath()%>/images/loading.gif" alt="Loading..." style="width:50px; height:50px;" />
						<img alt="processing" src="<%=request.getContextPath()%>/images/progress_indicator.gif" style="width:50px; height:50px;" />
					</div> --%>
					
				</div>
			<s:hidden name="uploadFileName" id="uploadFileName" />
			</s:form>
			
			<div id="waitDiv">
				<h3>Please wait.........<br> Your Request is being processed.<br></h3>
				<img src="<%=request.getContextPath()%>/images/progress_indicator.gif" alt="Loading..." />
			</div>

				<%-- <c:if test="${not empty reportList}">
			<s:form name="showBulkUploadReportDetails" id="showBulkUploadReportDetails" method="POST" theme="simple" cssClass="form-container">
					<div class="box-head1" align="left">
						<s:text name="Bulk Merchanrt Onboard Result" />
					</div>
					<div class="clearfix"></div>
					<div class="container">
						<table width="80%" align="center">
							<tr><td><div class="ribbon" align="left">
										<s:text name="Bulk Merchanrt Onboard Result" />
							</div></td></tr>
							<tr><td><display:table class="its" id="data" name="reportList" pagesize="25" requestURI="">
										<display:column property="fileName" title="File Name" />
										<display:column property="successCount" title="Success Count" />
										<display:column property="failureCount" title="Failure/Duplicate Count" />
										<display:column property="totalCount" title="Total Count" />
										<display:column property="processedDate" title="Processed Date" />
										<display:column property="processedByUser" title="Processed By User" />
									</display:table></td>
							</tr>
						</table>
					</div>
			</s:form>
			</c:if> --%>
		</div>	
      </div></section>
    </body>
</html>