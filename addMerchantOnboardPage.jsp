<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="d" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Quick Merchant OnBoard Details</title>
<style>
.trash-icon {
	width: 20px;
	height: 20px !important;
	cursor: pointer;
	text-align: center;
}

.position-relative {
	position: relative;
}
.typeahead.dropdown-menu {
    position: absolute;
    top: 100%;   
    left: 0;
    width: 100%; 
  }
#calBorder {
	/* 		z-index: 100 !important; */
	z-index: 100;
}

.modal {
	z-index: 98;
}

.modal-backdrop {
	z-index: 97;
}

select, .inner input[type="text"], textarea, .inner input[type="password"],
	.file-upload, input[type="search"] {
	width: 225px !important;
}

.file-upload {
	padding: 0px !important;
}

#STORE1_prfDocsProv+.select2 .select2-selection.select2-selection--multiple
	{
	border-radius: 8px;
	border: 1px solid #dee2e6 !important;
}

.tab-pane.show.active {
	border: none !important;
}

.nav.nav-tabs {
	border: none !important;
}

.nav.nav-tabs .nav-item {
	border: none !important;
}

.nav.nav-tabs .nav-link {
	border: none !important;
}

.nav-tabs>li {
	background: #f7f7f7;
	/* border-bottom: 2px solid var(--primary-color); */
	/* line-height: 29px; */
	border-radius: 8px;
	border: 1px solid #efefef;
}

.select2-search__field {
	padding: 12px !important;
	margin: 4px !important;
	margin-left: 2px !important;
}

.select2-selection__choice__remove {
	background: var(--primary-color) !important;
	color: white !important;
}

.select2-selection__choice__display {
	background: white !important;
	padding: 6px !important;
}
</style>

<jsp:include page="addMerchantOnboardJSPage.jsp"></jsp:include>
 
</head>
<body>
	<section class="section-bg">
	<div class="clearfix"></div>
	<div class="divider dashed"></div>	
	<div class="box1">
	<s:form name="frmAddMerchantOnboardDetails" id="frmAddMerchantOnboardDetails" action="addMerchantOnboardPageDetails" method="POST" enctype="multipart/form-data">
	<c:choose><c:when test="${merchantOnboardBean.additionalNewStoreFlag eq 1}">
	<div class="box-head1"><s:text name="MerchantOnboardMakerAction.addNewStoreTitle" /></div>
	</c:when><c:otherwise>
	<div class="box-head1"><s:text name="MerchantOnboardMakerAction.addTitle" /></div>
	</c:otherwise></c:choose>
	<div class="clearfix"></div>
	
	<div class="container-fuild">
	<table  width="100%" cellspacing="3">
		<tr><td>
		
	<!-- Referral Details -->
	
	<div class="container-fuild">
		<table width="90%" border="0" align="center">
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
	<table width="90%" align="center">
	<tr>
	<td>
	<div class="accordion" id="accordion_Details">
	<c:choose><c:when test="${merchantOnboardBean.additionalNewStoreFlag eq 1}">
	<input type="hidden" name="referralNo" id="referralNo" value="${merchantOnboardBean.refSqid}">
	<input type="hidden" name="merchantOnboardBean.refSqid" id="merchantOnboardBeanrefSqid" value="${merchantOnboardBean.refSqid}">
	<input type="hidden" name="merchantOnboardBean.refNo" id="merchantOnboardBeanrefNo" value="${merchantOnboardBean.refSqid}">
	<s:hidden name="merchantOnboardBean.additionalNewStoreFlag" />
	<style>
	input[disabled] {
        color: black;
    }
    .ui-selectmenu-disabled {
      	color: black;
    	opacity: 1 !important;
    }
	.pointerevents-none {
	    pointer-events: none;
	}
	</style>
	<div class="group">
	<h2><s:text name="MerchantOnboardMakerAction.merchantDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
		
		<tr>					
			<td><label><s:text name="MerchantOnboardMakerAction.mid"></s:text></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td>
				<s:textfield name="merchantOnboardBean.mid" cssClass="formtextbox" disabled="true" ></s:textfield>
				<input type="hidden" name="merchantOnboardBean.mid" value="${merchantOnboardBean.mid}" >
			</td>						
				
			<td><label><s:text name="MerchantOnboardMakerAction.salesconsultant"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>	
			<td> 
				<input type="text" name="merchantOnboardBean.bankersalesNames" value="${merchantOnboardBean.bankersalesNames}" class="position-relative" disabled="disabled" />
			</td>
		</tr>
			
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.legalname"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>	
			<td><s:textfield name="merchantOnboardBean.legalname" cssClass="formtextbox" disabled="true" />
				<input type="hidden" name="merchantOnboardBean.legalname" id="legalname" value="${merchantOnboardBean.legalname}" >
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.dbaname"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>				
			<td align="left"><s:textfield name="merchantOnboardBean.dbaname" cssClass="formtextbox" disabled="true" />
				<input type="hidden" id="dbaname" name="merchantOnboardBean.dbaname" value="${merchantOnboardBean.dbaname}" >
			</td>
		</tr>
		
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.ownername"/></label></td>
			<td><s:textfield name="merchantOnboardBean.ownername" maxlength="40" cssClass="formtextbox" disabled="true" /></td>
			<td><label><s:text name="MerchantOnboardMakerAction.mercAggr"/></label></td>
			<td>
				<input type="text" name="merchantOnboardBean.mercAggrName" value="${merchantOnboardBean.mercAggrName}" class="position-relative" disabled="disabled" />
				<input type="hidden" id="mercAggrrelm" name="merchantOnboardBean.mercAggrrelm" value="${merchantOnboardBean.mercAggrrelm}" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.existingCustomer"/></label></td> 
			<td>
				<s:radio name="merchantOnboardBean.existingCustomer"
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" disabled="true" />
			</td>
			<td id="merchantPortalRequired1" <c:if test="${merchantOnboardBean.mercAggr ne null}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.merchantPortalRequired"/></label></td> 
			<td id="merchantPortalRequired2" <c:if test="${merchantOnboardBean.mercAggr ne null}"> style="display:none;" </c:if> >
				<s:radio name="merchantOnboardBean.merchantPortalRequired"
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" disabled="true" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.accountMappingLevel" /></label></td>
			<td>
				<s:radio name="merchantOnboardBean.acctMapLvl" 
					list="#{'0':getText('MerchantOnboardMakerAction.aggrLevel'),'1':getText('MerchantOnboardMakerAction.mercLevel')}" 
					cssClass="pointerevents-none" onchange="return false;" />
			</td>
			<td id="merchantAccountRequired1" <c:if test="${merchantOnboardBean.acctMapLvl ne 0}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.merchantAccountRequired"/></label></td> 
			<td id="merchantAccountRequired2" <c:if test="${merchantOnboardBean.acctMapLvl ne 0}"> style="display:none;" </c:if> >
				<s:radio name="merchantOnboardBean.merchantAccountRequired"
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" disabled="true" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.merchantmcc"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>	
			<td>
				<input type="hidden" name="merchantOnboardBean.cmccMerchantAutoComplete" value="${merchantOnboardBean.cmccMerchantAutoComplete}"/>
				<input type="text" name="merchantOnboardBean.mccmerchantautocomplete" value="${merchantOnboardBean.mccmerchantautocomplete}" class="position-relative" disabled="disabled" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.noOfStoresReq"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td>
				<s:textfield name="merchantOnboardBean.noOfStoresReq" id="noOfStoresReq" cssClass="formtextbox" disabled="true" />
				<input type="hidden" name="merchantOnboardBean.noOfStoresReq" value="${merchantOnboardBean.noOfStoresReq}" >
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.mercLogoUpld"/></label></td>
			<td>
				<c:choose>
					<c:when test="${merchantOnboardBean.mercLogoDisplayData ne null}">
						<img src="data:image/jpg;base64, ${merchantOnboardBean.mercLogoDisplayData}" style="width: 200px;height: 50px;" />
					</c:when>
					<c:otherwise> File Not Uploaded </c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
	</div>
	</div>	 
	<!-- End of the Referral Details -->
	
	<div class="group">
	<h2><s:text name="MerchantOnboardMakerAction.ownershipDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
	
	    <!-- Owner Type -->
	    <tr>
	        <td><label><s:text name="MerchantOnboardMakerAction.ownerType"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td>
	            <s:radio name="merchantOnboardBean.ownerType"
	                list="#{'1':getText('MerchantOnboardMakerAction.Person'),'2':getText('MerchantOnboardMakerAction.Entity')}" disabled="true" />
	        </td>
	    </tr>
	
	    <tr id="individualNameRow" <c:if test="${merchantOnboardBean.ownerType ne 1}"> style="display:none;" </c:if> >
	        <td><label><s:text name="MerchantOnboardMakerAction.individualName"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td><s:textfield name="merchantOnboardBean.individualName" readonly="true" cssClass="formtextbox"/></td>
	    </tr>
	
	    <tr id="parentEntityRow" <c:if test="${merchantOnboardBean.ownerType ne 2}"> style="display:none;" </c:if> >
	        <td><label><s:text name="MerchantOnboardMakerAction.parentEntityName"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td><s:textfield name="merchantOnboardBean.parentEntityName" readonly="true" cssClass="formtextbox"/></td>
	
<%-- 	    <tr id="uboPctRow" <c:if test="${merchantOnboardBean.ownerType ne 2}"> style="display:none;" </c:if> > --%>
	        <td><label><s:text name="MerchantOnboardMakerAction.uboShareholderPctEntity"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	       <td><s:textfield name="merchantOnboardBean.uboShareholderPctEntity" readonly="true" cssClass="formtextbox"/></td>
	    </tr>
	
	    <tr>
	        <td><label><s:text name="MerchantOnboardMakerAction.countryOfIncorp"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td><s:textfield name="merchantOnboardBean.countryOfIncorp" readonly="true" cssClass="formtextbox"/></td>
	
	        <td><label><s:text name="MerchantOnboardMakerAction.dateOfIncorp"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td><s:textfield name="merchantOnboardBean.dateOfIncorp" readonly="true" cssClass="formtextbox"/>
	        	<img src="<%=request.getContextPath()%>/images/calendar-icon.png" onclick="javascript:NewCssCal('dateOfIncorp','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>', '<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)" class="calendar-icon">
	        </td>
	    </tr>
	
	    <tr>
	        <td class="formtext">
	       		<label><s:text name="MerchantOnboardMakerAction.businessType"/>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td></label>
	        <td>
	            <s:select name="merchantOnboardBean.businessType" cssClass="select_styled" list="#{'1':getText('MerchantOnboardMakerAction.Owner'),'2':getText('MerchantOnboardMakerAction.Partner')}"
				 headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" disabled="true" />
	        </td>
	    </tr>
	
	    <tr>
	        <td><label><s:text name="MerchantOnboardMakerAction.emiratesIdNo"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td><s:textfield name="merchantOnboardBean.emiratesIdNo" readonly="true" cssClass="formtextbox"/></td>
	    
	        <td><label><s:text name="MerchantOnboardMakerAction.emiratesIdExpiry"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td><s:textfield name="merchantOnboardBean.emiratesIdExpiry" readonly="true" cssClass="formtextbox"/>
	        	<img src="<%=request.getContextPath()%>/images/calendar-icon.png" onclick="javascript:NewCssCal('emiratesIdExpiry','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>', '<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)" class="calendar-icon">
	        </td>
	    </tr>
	
	    <tr>
	        <td><label><s:text name="MerchantOnboardMakerAction.passportNumber"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td><s:textfield name="merchantOnboardBean.passportNumber" readonly="true" cssClass="formtextbox"/></td>
	    
	        <td><label><s:text name="MerchantOnboardMakerAction.passportExpiry"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	        <td><s:textfield name="merchantOnboardBean.passportExpiry" readonly="true" cssClass="formtextbox"/>
	        	<img src="<%=request.getContextPath()%>/images/calendar-icon.png" onclick="javascript:NewCssCal('passportExpiry', '<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>', 'dropdown'),lightbox(null)" class="calendar-icon">
	        </td>
	    </tr>
	
	    <tr>
	        <td><label><s:text name="MerchantOnboardMakerAction.nationality"/></label>
	            <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
	         <td colspan="3">
			     <s:select name="merchantOnboardBean.nationality" list="homeCountryIdList" listKey="key" listValue="value" headerKey=""
                      headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" cssClass="select_styled" disabled="true"/>
			 </td>
	    </tr>
	
	</table>
	</div>
	</div>
	
	<!-- Customer Details -->
	<div class="group">
	<h2><s:text name="MerchantOnboardMakerAction.customerDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
		<tr>		
			<td><label><s:text name="MerchantOnboardMakerAction.firstname"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.firstname1" cssClass="formtextbox" disabled="true" /></td>
			<td><label><s:text name="MerchantOnboardMakerAction.lastname"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.lastname1" cssClass="formtextbox" disabled="true" /></td>
		</tr>	
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.addressline1"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.addressline1" cssClass="formtextbox" disabled="true" /></td>
			<td><label><s:text name="MerchantOnboardMakerAction.addressline2"/></label></td>
			<td><s:textfield name="merchantOnboardBean.addressline2" cssClass="formtextbox" disabled="true" /></td>
		</tr>
	    <tr>				
			<td><label><s:text name="MerchantOnboardMakerAction.country"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>	
			<td>
				<input type="text" name="merchantOnboardBean.country" value="${merchantOnboardBean.country}" class="position-relative" disabled="disabled" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.state"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>	
			<td>
				<input type="text" name="merchantOnboardBean.state" value="${merchantOnboardBean.state}" class="position-relative" disabled="disabled" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.citysuburb"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>	
			<td>
				<input type="text" name="merchantOnboardBean.selectcity" value="${merchantOnboardBean.selectcity}" class="position-relative" disabled="disabled" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.postalcode"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.postalcode" cssClass="formtextbox" disabled="true" />
			</td>
		</tr>
		<tr>	
			<td><label><s:text name="MerchantOnboardMakerAction.phone"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.phone" cssClass="formtextbox" disabled="true" /></td>			
			<td><label><s:text name="MerchantOnboardMakerAction.email"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.contactEmail" cssClass="formtextbox" disabled="true" /></td>
		</tr>	
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.pancard"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.cpancard" cssClass="formtextbox" disabled="true" /></td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.businessContactEmailID"/></label></td>
			<td>
				<s:textfield name="merchantOnboardBean.businessContactEmailID" cssClass="formtextbox" disabled="true" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.businessContactMobileNo"/></label></td>
			<td>
				<s:textfield name="merchantOnboardBean.businessContactMobileNo" cssClass="formtextbox" disabled="true" />
			</td>			
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.technicalContactEmailID"/></label></td>
			<td>
				<s:textfield name="merchantOnboardBean.technicalContactEmailID" cssClass="formtextbox" disabled="true" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.technicalContactMobileNo"/></label></td>
			<td>
				<s:textfield name="merchantOnboardBean.technicalContactMobileNo" cssClass="formtextbox" disabled="true" />
			</td>			
		</tr>
	</table>
	</div>
	</div>
	<!-- Customer Details End -->
	
	<!-- Account Details -->
	<div <c:if test="${merchantOnboardBean.merchantAccountRequired == 0}"> style="display:none;" </c:if> >
	<div class="group">
	<h2><s:text name="MerchantOnboardMakerAction.accountDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
		<tr>
			<td class="formtext"><label><s:text name="MerchantOnboardMakerAction.accountWithBank"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
			</td>
			<td><s:select name="merchantOnboardBean.acctWithBank" cssClass="select_styled" listKey="key" listValue="value"
				list="#{'1':'Yes','0':'No'}" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" disabled="true" ></s:select>
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.defaultAccount" />
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:radio name="merchantOnboardBean.defAccount"
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" disabled="true" />
			</td>
		</tr>
		
		<tr>
			<td class="formtext" id="acctBranch" <c:if test="${merchantOnboardBean.acctWithBank ne '1'}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.acctBranch"></s:text></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
			</td>
			<td class="formtext" id="acctBranchDetails" <c:if test="${merchantOnboardBean.acctWithBank ne '1'}"> style="display:none;" </c:if> >
				<input type="text" name="merchantOnboardBean.acctBranch" value="${merchantOnboardBean.acctBranch}" class="position-relative" disabled="disabled" />
			</td>
				
			<td class="formtext" id="bankName" <c:if test="${merchantOnboardBean.acctWithBank ne '0'}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.bankName"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
			</td>
			<td class="formtext" id="bankNameText" <c:if test="${merchantOnboardBean.acctWithBank ne '0'}"> style="display:none;" </c:if> >
				<s:textfield name="merchantOnboardBean.acctBankText" cssClass="formtextbox" disabled="true" />
			</td>
			
			<td class="formtext" id="branchName" <c:if test="${merchantOnboardBean.acctWithBank ne '0'}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.branchName"></s:text></label>
			</td>
			<td class="formtext" id="branchNameText" <c:if test="${merchantOnboardBean.acctWithBank ne '0'}"> style="display:none;" </c:if> >
				<s:textfield name="merchantOnboardBean.acctBranchText" cssClass="formtextbox" disabled="true" />
			</td>
		</tr>
		<tr>		
			<td><label><s:text name="MerchantOnboardMakerAction.IFSC"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:textfield name="merchantOnboardBean.ifsc" cssClass="formtextbox" disabled="true" /></td>
			<td><label><s:text name="MerchantOnboardMakerAction.accountname"></s:text></label></td>
			<td><s:textfield name="merchantOnboardBean.accountname" label="MerchantOnboardMakerAction.accountname" cssClass="formtextbox" disabled="true" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.currency"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:select name="merchantOnboardBean.currency" id="currency" cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
				 list="crcyList" listKey="key" listValue="value" disabled="true" ></s:select>
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.accountNumber"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:textfield name="merchantOnboardBean.acctNumber" cssClass="formtextbox" disabled="true"/>
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.cifNumber"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:textfield name="merchantOnboardBean.cifNumber" cssClass="formtextbox" disabled="true"/>
			<%-- <td><label><s:text name="MerchantOnboardMakerAction.stmtfrequency"></s:text></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
			</td>
			<td>
				<s:select name="merchantOnboardBean.stmtfreq" cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					list="stmttFreqList" listKey="key" listValue="value"></s:select>
			</td> --%>
		</tr>	
	</table>
	</div>
	</div>
	</div>	
	<!-- End of the Account Details -->
	
	<!-- Pricing Details -->
	<div class="group">
	<h2><s:text name="MerchantOnboardMakerAction.pricingDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.captureMerchantServiceFeeAt" /></label>
			</td>
			<td colspan="3">
				<s:radio name="merchantOnboardBean.captureServiceFeeAt" 
					list="#{'1':getText('MerchantOnboardMakerAction.merchantLevel'),'2':getText('MerchantOnboardMakerAction.storeLevel'),'3':getText('MerchantOnboardMakerAction.terminalLevel')}" disabled="true" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeePlanType" /></label></td>
			<td colspan="3">
				<s:radio name="merchantOnboardBean.feePlanType" id="feePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" disabled="true" />
			</td>
	    </tr>
		<tr>
			<td width="30%"><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeeplan" />
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td width="50%" colspan="2">
				<c:choose>
					<c:when test="${merchantOnboardBean.feePlanType eq 2}">
						<s:select name="merchantOnboardBean.merchantServiceFeePlan" headerKey="" 
							cssClass="select_styled" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
							list="feePlanListFortire" listKey="value" listValue="value" disabled="true" ></s:select>
					</c:when>
					<c:otherwise>
						<s:select name="merchantOnboardBean.merchantServiceFeePlan" headerKey="" 
							cssClass="select_styled" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
							list="feePlanList" listKey="value" listValue="value" disabled="true" ></s:select>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<%-- <tr>
			<td><label><s:text name="MerchantOnboardMakerAction.mercFeeDeduWaiver" /></label></td>
			<td><s:radio name="merchantOnboardBean.mercFeeDeduWaiver" 
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" disabled="true" />
			</td>
		</tr> --%>
		<tr>	
			<td><label><s:text name="MerchantOnboardMakerAction.merchanttype"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td> 
			<td colspan="3">
				<c:forEach var="mercTypelist" items="${merchantTypeList}">
			        <input type="checkbox" name="merchantOnboardBean.merchantType" checked="checked" onclick="return false;"
			        	onchange="return false;" value="${mercTypelist.key}" fieldvalue="${mercTypelist.key}" readonly="readonly" >
					<label for="merchType-${mercTypelist.key}" class="checkboxLabel">${mercTypelist.value}</label>
   				</c:forEach>
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.merchantEntity"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></label></td>
			<td>
				<select name="merchantOnboardBean.merchantEntityName" id="merchantEntityName" multiple="multiple" onchange="fn_merchantEntityValue();" disabled="disabled" >
					<c:forEach var="entity" items="${merchantEntityList}"> 
						<option value="${entity.key}" <c:if test="${fn:contains(merchantOnboardBean.merchantEntityName, entity.key)}">selected="selected"</c:if> >
							${entity.value}</option>
					</c:forEach>
				</select>
				<input type="hidden" id="merchantEntityName" name="merchantOnboardBean.merchantEntityName" value="${merchantOnboardBean.merchantEntityName}" />
			</td>
		</tr>
		<tr>
			<td id="aaniPayMercID1" <c:if test="${merchantOnboardBean.merchantEntityName ne '10' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.aaniPayMercID" /></label></td>
			<td id="aaniPayMercID2" <c:if test="${merchantOnboardBean.merchantEntityName ne '10' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
				<s:textfield name="merchantOnboardBean.aaniPayMercID" maxlength="7" onkeypress="AllowAlpaNumeric();" cssClass="formtextbox" disabled="true"></s:textfield>
			</td>
		</tr>
		<tr id="SDRdetails">
			<td><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryRequired"/></label></td>
			<td class='d-flex'>
				<c:choose>
					<c:when test="${merchantOnboardBean.securityDepositRecoveryRequired eq 1}">
						<s:radio name="merchantOnboardBean.securityDepositRecoveryRequired" onchange="fn_SDRdetailsForTerm(this);"
							list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
						<%-- <a id="SDRModalLink" href="#" data-toggle="modal" data-target="#SDRModal" style="padding:8px 8px; display:inline-block;">&nbsp;<span>View Details</span></a> --%>
					</c:when>
					<c:otherwise>
						<s:radio name="merchantOnboardBean.securityDepositRecoveryRequired" onchange="fn_SDRdetailsForTerm(this);"
							list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
						<%-- <a id="SDRModalLink" href="#" data-toggle="modal" data-target="#SDRModal" style="display:none; padding:8px 8px;" >&nbsp;<span>View Details</span></a> --%>
					</c:otherwise>	
				</c:choose>
				<!-- TERMINAL SDRModal - START -->
				<%-- <div class="modal modal-wide fade" id="SDRModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog modal-xl modal-dialog-centered">
						<div class="modal-content">
							<div class="modal-header">
								<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryRequired" /></h4>
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							
							</div>
							<div class="modal-body">
								<table border="0" width="100%" cellspacing="4"> --%>
									<tr id="SDRdetails_Row0">
										<td><label><s:text name="MerchantOnboardMakerAction.securityDepositLimit"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
										<td><s:textfield name="merchantOnboardBean.securityDepositLimit" maxlength="10" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
									</tr>
									<%-- <c:choose>
										<c:when test="${merchantOnboardBean.sdrIndc eq 1}">
											<tr id="SDRdetails_Row1">
												<td>
													<label><s:text name="MerchantOnboardMakerAction.sdrIndc"/></label>
												</td>
												<td nowrap="nowrap">
													<s:radio name="merchantOnboardBean.sdrIndc" id="sdrIndc" onchange="sdrFieldsEnable(this);" 
														value="1" list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
												</td>
												<td id="SDRdiv1"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
												<td id="SDRdiv2" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
												<td id="SDRdiv3"><s:textfield name="merchantOnboardBean.securityDepositRecoveryPercentage" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
												<td id="SDRdiv4" style="display: none;"><s:textfield name="merchantOnboardBean.securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr id="SDRdetails_Row1">
												<td>
													<label><s:text name="MerchantOnboardMakerAction.sdrIndc"/></label>
												</td>
												<td nowrap="nowrap">
													<s:radio name="merchantOnboardBean.sdrIndc" id="sdrIndc" onchange="sdrFieldsEnable(this);" 
														value="0" list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
												</td>
												<td id="SDRdiv1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
												<td id="SDRdiv2"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
												<td id="SDRdiv3" style="display: none;"><label><s:textfield name="merchantOnboardBean.securityDepositRecoveryPercentage" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
												<td id="SDRdiv4"><s:textfield name="merchantOnboardBean.securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
											</tr>
										</c:otherwise>
									</c:choose> --%>
								<tr id="SDRdetails_Row1">
									<td><label><s:text name="MerchantOnboardMakerAction.sdrIndc" /></label></td>
									<td nowrap="nowrap"><s:radio name="merchantOnboardBean.sdrIndc" id="sdrIndc"
											onchange="sdrFieldsEnable(this);" list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
									</td>
								</tr>
								<tr id="SDRdetails_Row2">
									<td id="SDRdiv1"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage" />
										<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label>
									</td>
									
									<td id="SDRdiv2" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount" />
										<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span> </label>
									</td>
									
									<td id="SDRdiv3">
										<s:textfield name="merchantOnboardBean.securityDepositRecoveryPercentage" maxlength="6" cssClass="formtextbox"
											oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
									</td>
								
									<td id="SDRdiv4" style="display: none;">
										<s:textfield name="merchantOnboardBean.securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox"
											oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
									</td>
								</tr>
							<%-- </table>
							</div>
							<div class="modal-footer text-center d-block">
								<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
							</div>
						</div>
					</div>
				</div> --%>
				<!-- TERMINAL SDRModal - END -->
			</td>
		</tr>
	</table>
	</div>
	</div> 
	<!-- End of the Pricing Details -->
	</c:when><c:otherwise>
	<div class="group">
	<h2 onclick="fn_customizeAccordion(this, 'REFERRAL');"><s:text name="MerchantOnboardMakerAction.merchantDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
		
		<tr>					
			<td><label><s:text name="MerchantOnboardMakerAction.mid"></s:text></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td>
				<s:if test="autoGenerateMid == 1">
					<s:textfield name="merchantOnboardBean.mid" value="AUTO GENERATE" cssClass="formtextbox" minlength="8" maxlength="9" style="color: black;background: lightgray;" readonly="true"></s:textfield>
				</s:if>
				<s:else>
					<s:textfield name="merchantOnboardBean.mid" cssClass="formtextbox" minlength="8" maxlength="9" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield>
				</s:else>
			</td>						
				
			<td><label><s:text name="MerchantOnboardMakerAction.salesconsultant"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>	
			<td> 
				<input type="hidden" id="bankerSalesSqid" name="merchantOnboardBean.bankerSalesSqid" value="${merchantOnboardBean.bankerSalesSqid}" /> 
				<input type="text" id="bankersalesNames" name="merchantOnboardBean.bankersalesNames" value="${merchantOnboardBean.bankersalesNames}" 
					size="20" maxlength="40" onkeypress="AllowAlphabeticWithSpace();" class="position-relative" />
			</td>
		</tr>
			
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.legalname"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>	
			<td><s:textfield name="merchantOnboardBean.legalname" id="legalname" minlength="2" maxlength="60" cssClass="formtextbox" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.dbaname"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>				
			<td align="left"><s:textfield name="merchantOnboardBean.dbaname" id="dbaname" maxlength="40" cssClass="formtextbox" 
				onchange="fn_updateDBANameIntoStore(this);"/>
			</td>
		</tr>
		
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.ownername"/></label></td>
			<td><s:textfield name="merchantOnboardBean.ownername" maxlength="40" cssClass="formtextbox" /></td>
			<td><label><s:text name="MerchantOnboardMakerAction.mercAggr"/></label></td>
			<td>
				<input type="hidden" id="mercAggrID" name="merchantOnboardBean.mercAggr" value="${merchantOnboardBean.mercAggr}" /> 
				<input type="text" id="mercAggr" name="merchantOnboardBean.mercAggrName" value="${merchantOnboardBean.mercAggrName}"
					size="20" maxlength="40" autocomplete="off" class="position-relative" onchange="changeMercAggre();"/>
				<input type="hidden" id="mercAggrrelm" name="merchantOnboardBean.mercAggrrelm" value="${merchantOnboardBean.mercAggrrelm}" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.existingCustomer"/></label></td> 
			<td>
				<s:radio name="merchantOnboardBean.existingCustomer"
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
			</td>
			<td id="merchantPortalRequired1" <c:if test="${merchantOnboardBean.mercAggr ne null}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.merchantPortalRequired"/></label></td> 
			<td id="merchantPortalRequired2" <c:if test="${merchantOnboardBean.mercAggr ne null}"> style="display:none;" </c:if> >
				<s:radio name="merchantOnboardBean.merchantPortalRequired"
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.accountMappingLevel" /></label></td>
			<td>
				<s:radio name="merchantOnboardBean.acctMapLvl" 
					list="#{'0':getText('MerchantOnboardMakerAction.aggrLevel'),'1':getText('MerchantOnboardMakerAction.mercLevel')}" 
					onchange="fn_useSameAsAggrAccountForMerchant(this);" />
					<br>
				<span id="acctErrorMsg" style="color: red;padding-left: 10px;"></span>
			</td>
			<td id="merchantAccountRequired1" <c:if test="${merchantOnboardBean.acctMapLvl ne 0}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.merchantAccountRequired"/></label></td> 
			<td id="merchantAccountRequired2" <c:if test="${merchantOnboardBean.acctMapLvl ne 0}"> style="display:none;" </c:if> >
				<s:radio name="merchantOnboardBean.merchantAccountRequired" onchange="fn_merchantAccountRequired(this);" 
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.merchantmcc"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>	
			<td>
				<input type="hidden" id="cmccMerchantAutoComplete" name="merchantOnboardBean.cmccMerchantAutoComplete" value="${merchantOnboardBean.cmccMerchantAutoComplete}"/>
				<input type="text" id="mccmerchantautocomplete" name="merchantOnboardBean.mccmerchantautocomplete"
					value="${merchantOnboardBean.mccmerchantautocomplete}" size="20" maxlength="4" autocomplete="off" class="position-relative"
					onkeypress="fn_merchantMccAutocomplete();"/>
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.noOfStoresReq"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td>
				<s:textfield name="merchantOnboardBean.noOfStoresReq" id="noOfStoresReq" maxlength="4" cssClass="formtextbox" onkeypress="this.value = this.value.replace(/[^0-9]/g, '')" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.mercLogoUpld"/></label></td>
			<td>
				<c:if test="${merchantOnboardBean.mercLogoDisplayData ne null}">
					<img src="data:image/jpg;base64, ${merchantOnboardBean.mercLogoDisplayData}" style="width: 200px;height: 50px;" />
					<br>
				</c:if>
				<s:file name="merchantOnboardBean.mercLogo" id="fileUpload" onchange="uploadfile();" />
				<s:hidden name="uploadMercLogoFileName" id="uploadMercLogoFileName" />
			</td>
		</tr>
		
	</table>
	</div>
	</div>	 
	<!-- End of the Referral Details -->
	
	<!-- Start of the Ownership Details -->
	<div class="group">
    <h2 onclick="fn_customizeAccordion(this, 'OWNERSHIP');"> <s:text name="MerchantOnboardMakerAction.ownershipDetails" /> </h2>
    <div class="content">
        <table border="0" width="100%" cellspacing="4">
			<tr>
			    <td>
			        <label><s:text name="MerchantOnboardMakerAction.ownerType"/></label>
			        <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span>
			    </td>
			    <td>
			        <s:radio name="merchantOnboardBean.ownerType"
			            list="#{'1':getText('MerchantOnboardMakerAction.Person'),'2':getText('MerchantOnboardMakerAction.Entity')}"
			            onchange="toggleOwnerFields(this)" />
			    </td>
			</tr>

            <tr id="individualNameRow" <c:if test="${merchantOnboardBean.ownerType ne 1}"> style="display:none;" </c:if> >
                <td><label><s:text name="MerchantOnboardMakerAction.individualName"/></label>
                    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
                <td><s:textfield name="merchantOnboardBean.individualName" cssClass="formtextbox"/></td>
            </tr>
			<tr id="parentEntityRow" <c:if test="${merchantOnboardBean.ownerType ne 2}"> style="display:none;" </c:if> >
			    <td>
			        <label><s:text name="MerchantOnboardMakerAction.parentEntityName"/></label>
			        <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span>
			    </td>
			    <td>
			        <s:textfield name="merchantOnboardBean.parentEntityName" cssClass="formtextbox"  />
			    </td>
			    <td>
			        <label><s:text name="MerchantOnboardMakerAction.uboShareholderPctEntity"/></label>
			        <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span>
			    </td>
			    <td>
			        <s:textfield name="merchantOnboardBean.uboShareholderPctEntity" cssClass="formtextbox" />
			    </td>
			</tr>
			
            <tr>
                <td><label><s:text name="MerchantOnboardMakerAction.countryOfIncorp"/></label>
                    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
                <td><s:textfield name="merchantOnboardBean.countryOfIncorp" maxlength="20" cssClass="formtextbox" /></td>

                <td> <label><s:text name="MerchantOnboardMakerAction.dateOfIncorp"/></label> 
                     <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span> 
                </td> <td> <s:textfield name="merchantOnboardBean.dateOfIncorp" id="dateOfIncorp" cssClass="date-picker" readonly="true" required="true" /> 
                <img src="<%=request.getContextPath()%>/images/calendar-icon.png" onclick="javascript:NewCssCal('dateOfIncorp','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>', '<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)" class="calendar-icon"> </td>
            </tr>

            <tr>
				<td>
				    <label><s:text name="MerchantOnboardMakerAction.businessType"/></label>
				    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span>
				</td>
				<td>
				    <s:select name="merchantOnboardBean.businessType" list="#{'1':getText('MerchantOnboardMakerAction.Owner'),'2':getText('MerchantOnboardMakerAction.Partner')}" 
				        headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" cssClass="select_styled" />
				</td>

<%--                 <td><label><s:text name="MerchantOnboardMakerAction.uboShareholderPct"/></label>
                    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
                <td><s:textfield name="merchantOnboardBean.uboShareholderPct" cssClass="formtextbox" /></td> --%>
            </tr>

            <tr>
                <td><label><s:text name="MerchantOnboardMakerAction.emiratesIdNo"/></label>
                    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
                <td><s:textfield name="merchantOnboardBean.emiratesIdNo" maxlength="20" cssClass="formtextbox" /></td>

                <td> <label><s:text name="MerchantOnboardMakerAction.emiratesIdExpiry"/></label> 
                    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span> </td> 
                <td> <s:textfield name="merchantOnboardBean.emiratesIdExpiry" id="emiratesIdExpiry" cssClass="date-picker" readonly="true" required="true" /> 
                <img src="<%=request.getContextPath()%>/images/calendar-icon.png" onclick="javascript:NewCssCal('emiratesIdExpiry','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>', '<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)" class="calendar-icon"> </td>
            </tr>

            <tr>
                <td><label><s:text name="MerchantOnboardMakerAction.passportNumber"/></label>
                    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
                <td><s:textfield name="merchantOnboardBean.passportNumber" maxlength="30" cssClass="formtextbox" /></td>

                <td> <label><s:text name="MerchantOnboardMakerAction.passportExpiry"/></label> 
                     <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span> </td> 
                <td> <s:textfield name="merchantOnboardBean.passportExpiry" id="passportExpiry" cssClass="date-picker" readonly="true" required="true" /> 
                <img src="<%=request.getContextPath()%>/images/calendar-icon.png" onclick="javascript:NewCssCal('passportExpiry','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>', '<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)" class="calendar-icon"> </td>
            </tr>

            <tr>
                <td><label><s:text name="MerchantOnboardMakerAction.nationality"/></label>
                    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
			    <td colspan="3">
			        <s:select name="merchantOnboardBean.nationality" list="homeCountryIdList" listKey="key" listValue="value" headerKey=""
                        headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" cssClass="select_styled"/>
			    </td>
            </tr>

        </table>
    </div>
</div>
<!-- End of the Ownership Details -->
	
	<!-- Customer Details -->
	<div class="group">
	<h2 onclick="fn_customizeAccordion(this, 'CUSTOMER');"><s:text name="MerchantOnboardMakerAction.customerDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
		<tr>		
			<td><label><s:text name="MerchantOnboardMakerAction.firstname"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.firstname1" maxlength="40" cssClass="formtextbox" /></td>
			<td><label><s:text name="MerchantOnboardMakerAction.lastname"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.lastname1" maxlength="40" cssClass="formtextbox" /></td>
		</tr>	
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.addressline1"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.addressline1" maxlength="50" cssClass="formtextbox" /></td>
			<td><label><s:text name="MerchantOnboardMakerAction.addressline2"/></label></td>
			<td><s:textfield name="merchantOnboardBean.addressline2" maxlength="50" cssClass="formtextbox" /></td>
		</tr>
	    <tr>				
			<td><label><s:text name="MerchantOnboardMakerAction.country"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>	
			<td>
				<input type="hidden" id="ccountry" name="merchantOnboardBean.ccountry" value="${merchantOnboardBean.ccountry}" readonly="readonly" />
				<input type="text" id="country" name="merchantOnboardBean.country" value="${merchantOnboardBean.country}" size="20" maxlength="40" autocomplete="off" readonly="readonly" class="position-relative" />
			</td>
			 <td><label><s:text name="MerchantOnboardMakerAction.state"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>	
			<td>
				<input type="hidden" id="cstate" name="merchantOnboardBean.cstate" value="${merchantOnboardBean.cstate}" />
				<input type="text" id="state" name="merchantOnboardBean.state" value="${merchantOnboardBean.state}" size="20" maxlength="40" autocomplete="off" onkeypress="AllowAlphabeticWithSpace();" class="position-relative" />
			</td> 
			
			<%-- <td><label><s:text name="MerchantOnboardMakerAction.state"/></label>
                    <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span></td>
			    <td colspan="3">
			        <s:select name="merchantOnboardBean.state" list="homeCountryIdList" listKey="key" listValue="value" headerKey=""
                        headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" cssClass="select_styled"/>
			    </td> --%>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.citysuburb"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>	
			<td>
				<input type="hidden" id="cselectcity" name="merchantOnboardBean.cselectcity" value="${merchantOnboardBean.cselectcity}" />
				<input type="text" id="selectcity" name="merchantOnboardBean.selectcity" value="${merchantOnboardBean.selectcity}" size="20" maxlength="40" autocomplete="off" onkeypress="AllowAlphabeticWithSpace();" class="position-relative" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.postalcode"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.postalcode" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
			</td>
		</tr>
		<tr>	
			<td><label><s:text name="MerchantOnboardMakerAction.phone"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.phone" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>			
			<td><label><s:text name="MerchantOnboardMakerAction.email"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.contactEmail" maxlength="200" cssClass="formtextbox" /></td>
		</tr>	
		<tr>
			<td></td>
			<td></td>
			<td></td>
			<td><sup><font class="noteMsg"> (You can enter more than one Email ID comma separated)</font></sup></td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.pancard"/></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
			<td><s:textfield name="merchantOnboardBean.cpancard" id="pancard" maxlength="18" cssClass="formtextbox" /></td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.businessContactEmailID"/></label></td>
			<td>
				<s:textfield name="merchantOnboardBean.businessContactEmailID" maxlength="200" cssClass="formtextbox" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.businessContactMobileNo"/></label></td>
			<td>
				<c:choose>
					<c:when test="merchantOnboardBean.businessContactMobileNo == 0">
						<s:textfield name="merchantOnboardBean.businessContactMobileNo" value="" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
					</c:when>
					<c:otherwise>
						<s:textfield name="merchantOnboardBean.businessContactMobileNo" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
					</c:otherwise>
				</c:choose>
			</td>			
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.technicalContactEmailID"/></label></td>
			<td>
				<s:textfield name="merchantOnboardBean.technicalContactEmailID" maxlength="200" cssClass="formtextbox" />
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.technicalContactMobileNo"/></label></td>
			<td>
				<c:choose>
					<c:when test="merchantOnboardBean.technicalContactMobileNo == 0">
						<s:textfield name="merchantOnboardBean.technicalContactMobileNo" value="" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
					</c:when>
					<c:otherwise>
						<s:textfield name="merchantOnboardBean.technicalContactMobileNo" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
					</c:otherwise>
				</c:choose>
			</td>			
		</tr>
	</table>
	</div>
	</div>
	<!-- Customer Details End -->
	
	<!-- Account Details -->
	<div id="accountdetailstag" <div <c:if test="${merchantOnboardBean.merchantAccountRequired == 0}"> style="display:none;" </c:if> >
	<div class="group">
	<h2 onclick="fn_customizeAccordion(this, 'ACCOUNT');"><s:text name="MerchantOnboardMakerAction.accountDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
		<tr>
			<td class="formtext"><label><s:text name="MerchantOnboardMakerAction.accountWithBank"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
			</td>
			<td><s:select name="merchantOnboardBean.acctWithBank" id="acctType" cssClass="select_styled" listKey="key" listValue="value"
				list="#{'1':'Yes','0':'No'}" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"></s:select>
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.defaultAccount" />
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:radio name="merchantOnboardBean.defAccount"
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
			</td>
		</tr>
		
		<tr>
			<td class="formtext" id="acctBranch" <c:if test="${merchantOnboardBean.acctWithBank ne '1'}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.acctBranch"></s:text></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
			</td>
			<td class="formtext" id="acctBranchDetails" <c:if test="${merchantOnboardBean.acctWithBank ne '1'}"> style="display:none;" </c:if> >
				<input type="hidden" id="cAcctBranch" name="merchantOnboardBean.cAcctBranch" value="${merchantOnboardBean.cAcctBranch}" />
				<input type="text" id="mercAcctBranch" name="merchantOnboardBean.acctBranch" value="${merchantOnboardBean.acctBranch}" 
					size="20" maxlength="50" autocomplete="off" onkeypress="AllowAlphaNumericWithSpace();" class="position-relative" />
			</td>
				
			<td class="formtext" id="bankName" <c:if test="${merchantOnboardBean.acctWithBank ne '0'}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.bankName"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
			</td>
			<td class="formtext" id="bankNameText" <c:if test="${merchantOnboardBean.acctWithBank ne '0'}"> style="display:none;" </c:if> >
				<s:textfield name="merchantOnboardBean.acctBankText" maxlength="50" cssClass="formtextbox"/>
			</td>
			
			<td class="formtext" id="branchName" <c:if test="${merchantOnboardBean.acctWithBank ne '0'}"> style="display:none;" </c:if> >
				<label><s:text name="MerchantOnboardMakerAction.branchName"></s:text></label>
			</td>
			<td class="formtext" id="branchNameText" <c:if test="${merchantOnboardBean.acctWithBank ne '0'}"> style="display:none;" </c:if> >
				<s:textfield name="merchantOnboardBean.acctBranchText" maxlength="50" cssClass="formtextbox"/>
			</td>
		</tr>
		<tr>		
			<td><label><s:text name="MerchantOnboardMakerAction.IFSC"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:textfield name="merchantOnboardBean.ifsc" maxlength="11" cssClass="formtextbox" /></td>
			<td><label><s:text name="MerchantOnboardMakerAction.accountname"></s:text></label></td>
			<td><s:textfield name="merchantOnboardBean.accountname" label="MerchantOnboardMakerAction.accountname" maxlength="20"
			    cssClass="formtextbox" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.currency"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:select name="merchantOnboardBean.currency" id="currency" cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
				 list="crcyList" listKey="key" listValue="value"></s:select>
			</td>
			<td><label><s:text name="MerchantOnboardMakerAction.accountNumber"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:textfield name="merchantOnboardBean.acctNumber" onkeypress="AllowAlpaNumeric();" maxlength="36" cssClass="formtextbox" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.cifNumber"></s:text>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td><s:textfield name="merchantOnboardBean.cifNumber" onkeypress="AllowNumeric();" maxlength="7" cssClass="formtextbox" />
			</td>
			<%-- <td><label><s:text name="MerchantOnboardMakerAction.stmtfrequency"></s:text></label>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
			</td>
			<td>
				<s:select name="merchantOnboardBean.stmtfreq" cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
					list="stmttFreqList" listKey="key" listValue="value"></s:select>
			</td> --%>
		</tr>
	</table>
	</div>
	</div>
	</div>	
	<!-- End of the Account Details -->
	
	<!-- Pricing Details -->
	<div class="group">
	<h2 onclick="fn_customizeAccordion(this, 'PRICE');"><s:text name="MerchantOnboardMakerAction.pricingDetails" /></h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4">
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.captureMerchantServiceFeeAt" /></label>
			</td>
			<td colspan="3">
				<s:radio name="merchantOnboardBean.captureServiceFeeAt" 
					list="#{'1':getText('MerchantOnboardMakerAction.merchantLevel'),'2':getText('MerchantOnboardMakerAction.storeLevel'),'3':getText('MerchantOnboardMakerAction.terminalLevel')}" />
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeePlanType" /></label></td>
			<td colspan="3">
				<s:radio name="merchantOnboardBean.feePlanType" id="feePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" onchange="getServiceFeePlan();" />
			</td>
	    </tr>
		<tr>
			<td width="30%"><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeeplan" />
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			<td width="50%" colspan="2">
				<c:choose>
					<c:when test="${merchantOnboardBean.feePlanType eq 2}">
						<s:select name="merchantOnboardBean.merchantServiceFeePlan" id="merchantservicefeeplan" headerKey="" 
							cssClass="select_styled" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
							list="feePlanListFortire" listKey="value" listValue="value" ></s:select>
					</c:when>
					<c:otherwise>
						<s:select name="merchantOnboardBean.merchantServiceFeePlan" id="merchantservicefeeplan" headerKey="" 
							cssClass="select_styled" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
							list="feePlanList" listKey="value" listValue="value" ></s:select>
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<%-- <tr>
			<td><label><s:text name="MerchantOnboardMakerAction.mercFeeDeduWaiver" /></label></td>
			<td><s:radio name="merchantOnboardBean.mercFeeDeduWaiver" 
					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
			</td>
		</tr> --%>
		<tr>	
			<td><label><s:text name="MerchantOnboardMakerAction.merchanttype"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td> 
			<td colspan="3">
				<c:forEach var="mercTypelist" items="${merchantTypeList}">
			        <input type="checkbox" name="merchantOnboardBean.merchantType" checked="checked" readonly="readonly" onclick="return false;"
			        	onchange="return false;" value="${mercTypelist.key}" id="merchType-${mercTypelist.key}" fieldvalue="${mercTypelist.key}">
					<label for="merchType-${mercTypelist.key}" class="checkboxLabel">${mercTypelist.value}</label>
   				</c:forEach>
			</td>
		</tr>
		<tr>
			<td><label><s:text name="MerchantOnboardMakerAction.merchantEntity"/>
				<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
			<td>
				<%-- <s:select id="merchantEntityName" list="merchantEntityList" listKey="key" listValue="value" multiple="true" onchange="fn_merchantEntityValue();" /> --%>
				<select name="merchantOnboardBean.merchantEntityName" id="merchantEntityName" multiple="multiple" onchange="fn_merchantEntityValue();">
					<c:forEach var="entity" items="${merchantEntityList}"> 
						<option value="${entity.key}" <c:if test="${fn:contains(merchantOnboardBean.merchantEntityName, entity.key)}">selected="selected"</c:if> >
							${entity.value}</option>
					</c:forEach>
				 </select>
			</td>
		</tr>
		<tr>
			<td id="aaniPayMercID1"><label><s:text name="MerchantOnboardMakerAction.aaniPayMercID" /></label></td>
				<td id="aaniPayMercID2">
				<s:textfield name="merchantOnboardBean.aaniPayMercID" id="aaniPayMercID" maxlength="7" onkeypress="AllowAlpaNumeric();" cssClass="formtextbox"></s:textfield>
			</td>
		</tr>
		
		<tr id="SDRdetails">
			<td><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryRequired"/></label></td>
			<td class='d-flex'>
				<c:choose>
					<c:when test="${merchantOnboardBean.securityDepositRecoveryRequired eq 1}">
						<s:radio name="merchantOnboardBean.securityDepositRecoveryRequired" onchange="fn_SDRdetailsForTerm(this);"
							list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
						<%-- <a id="SDRModalLink" href="#" data-toggle="modal" data-target="#SDRModal" style="padding:8px 8px; display:inline-block;">&nbsp;<span>View Details</span></a> --%>
					</c:when>
					<c:otherwise>
						<s:radio name="merchantOnboardBean.securityDepositRecoveryRequired" onchange="fn_SDRdetailsForTerm(this);"
							list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
						<%-- <a id="SDRModalLink" href="#" data-toggle="modal" data-target="#SDRModal" style="display:none; padding:8px 8px;" >&nbsp;<span>View Details</span></a> --%>
					</c:otherwise>	
				</c:choose>
				<!-- TERMINAL SDRModal - START -->
				<%-- <div class="modal modal-wide fade" id="SDRModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog modal-xl modal-dialog-centered">
						<div class="modal-content">
							<div class="modal-header">
								<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryRequired" /></h4>
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							
							</div>
							<div class="modal-body">
								<table border="0" width="100%" cellspacing="4"> --%>
									<tr id="SDRdetails_Row0">
										<td><label><s:text name="MerchantOnboardMakerAction.securityDepositLimit"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
										<td><s:textfield name="merchantOnboardBean.securityDepositLimit" maxlength="10" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
									</tr>
									<%-- <c:choose>
										<c:when test="${merchantOnboardBean.sdrIndc eq 1}">
											<tr id="SDRdetails_Row1">
												<td>
													<label><s:text name="MerchantOnboardMakerAction.sdrIndc"/></label>
												</td>
												<td nowrap="nowrap">
													<s:radio name="merchantOnboardBean.sdrIndc" id="sdrIndc" onchange="sdrFieldsEnable(this);" 
														value="1" list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
												</td>
												<td id="SDRdiv1"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
												<td id="SDRdiv2" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
												<td id="SDRdiv3"><s:textfield name="merchantOnboardBean.securityDepositRecoveryPercentage" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
												<td id="SDRdiv4" style="display: none;"><s:textfield name="merchantOnboardBean.securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr id="SDRdetails_Row1">
												<td>
													<label><s:text name="MerchantOnboardMakerAction.sdrIndc"/></label>
												</td>
												<td nowrap="nowrap">
													<s:radio name="merchantOnboardBean.sdrIndc" id="sdrIndc" onchange="sdrFieldsEnable(this);" 
														value="0" list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
												</td>
												<td id="SDRdiv1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
												<td id="SDRdiv2"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
												<td id="SDRdiv3" style="display: none;"><label><s:textfield name="merchantOnboardBean.securityDepositRecoveryPercentage" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
												<td id="SDRdiv4"><s:textfield name="merchantOnboardBean.securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
											</tr>
										</c:otherwise>
									</c:choose> --%>
								<tr id="SDRdetails_Row1">
									<td><label><s:text name="MerchantOnboardMakerAction.sdrIndc" /></label></td>
									<td nowrap="nowrap"><s:radio name="merchantOnboardBean.sdrIndc" id="sdrIndc"
										onchange="sdrFieldsEnable(this);" list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
									</td>
								</tr>
								<tr id="SDRdetails_Row2">
									<td id="SDRdiv1"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage" />
										<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label>
									</td>
									
									<td id="SDRdiv2" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount" />
										<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span> </label>
									</td>
									
									<td id="SDRdiv3">
										<s:textfield name="merchantOnboardBean.securityDepositRecoveryPercentage" maxlength="6" cssClass="formtextbox"
											oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
									</td>
								
									<td id="SDRdiv4" style="display: none;">
										<s:textfield name="merchantOnboardBean.securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox"
											oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
									</td>
								</tr>
								<%-- </table>
							</div>
							<div class="modal-footer text-center d-block">
								<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
							</div>
						</div>
					</div>
				</div> --%>
				<!-- TERMINAL SDRModal - END -->
			</td>
		</tr>
	</table>
	</div>
	</div> 
	<!-- End of the Pricing Details -->
	</c:otherwise></c:choose>
		
	<!-- Store Details -->
	<s:set var="storeCount" value="0" />
	<s:iterator value="merchantOnboardBean.lstStoreBean" var="store" status="status">
	<c:if test="${store ne null}">
	<s:set var="storeIndex" value="%{#status.Index}" />	
	<s:set var="storeCount" value="%{#storeIndex + 1}" />
	<s:set var="storeName" value="%{'STORE' + #storeCount}" />
	<div class="group" id="${storeName}_details" >
	<h2 onclick="fn_customizeAccordion(this, 'EXISTSTORE');">
		<s:text name="MerchantOnboardMakerAction.storeDetails" /> <c:if test="${storeCount ne 1}"> - <s:property value="#storeCount" /></c:if>
		<c:if test="${storeCount ne 1}">
			<img src="${pageContext.request.contextPath}/images/trash_icon.png" alt="trashbutton" type="button" class="trash-icon"
					style="float: right; height: 25px; margin-top: 3px;"
					onclick="document.getElementById('${storeName}_details').remove();  fn_merchantEntityValue();" />
		</c:if>
	</h2>
	<div class="content">
	<table border="0" width="100%" cellspacing="4" id="${storeName}_tableDetails">
		   	<tr>
			    <td><label><s:text name="MerchantOnboardMakerAction.sid"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td>
					<s:hidden name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeSqid" cssClass="formtextbox" maxlength="10" />
			    	<s:if test="autoGenerateSid == 1">
						<input type="text" name="merchantOnboardBean.lstStoreBean[${storeIndex}].storeCode" value="${store.storeCode}"
							style="color: black;background: lightgray;" class="formtextbox" maxlength="15" readonly="readonly">
					</s:if>
			    	<s:else>
						<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeCode" cssClass="formtextbox" maxlength="15"></s:textfield>
					</s:else>
			    </td>
				<td><label><s:text name="MerchantOnboardMakerAction.storeTradeName"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeTradingName"
					id="%{storeName}_storeTradingName" maxlength="40" cssClass="formtextbox"></s:textfield>
				</td>
		    </tr>
		    <tr>
				<td><label><s:text name="MerchantOnboardMakerAction.storeemail"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield key="email" name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeEmail"
						maxlength="45" cssClass="formtextbox" /></td>
				<td><label><s:text name="MerchantOnboardMakerAction.merchantGrade" />
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].merchantGrade" headerKey=""
					headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" list="merGradeList" listKey="key"
					listValue="value" cssClass="select_styled">
					</s:select>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.natureOfBusiness"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].natureOfBusiness" headerKey="" 
						headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" list="natureOfBusinessCatgList" listKey="key"
						id="%{storeName}_natureOfBusiness" listValue="value" cssClass="select_styled">
					</s:select>
				</td>	
				<td><label><s:text name="MerchantOnboardMakerAction.prfDocsProv"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>
					<select id="${storeName}_prfDocsProv" multiple="multiple" disabled="disabled">
					<c:forEach var="prfdocsList" items="${store.prfDocsProvList}">
						<option selected="selected">${prfdocsList.value}</option>
					</c:forEach>
					</select>
					<input type="hidden" name="merchantOnboardBean.lstStoreBean[${storeIndex}].prfDocsProv" id="${storeName}_prfDocsProvValue" 
						value="${store.prfDocsProv}">
				</td>
			</tr>
			<tr>
				<%-- <td><label><s:text name="MerchantOnboardMakerAction.mcc"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>	
				<td>
					<input type="hidden" id="${storeName}_cmccAutoComplete" name="merchantOnboardBean.lstStoreBean[${storeIndex}].cmccAutoComplete"
						value="${store.cmccAutoComplete}"/>
					<input type="text" id="${storeName}_mccautocomplete" name="merchantOnboardBean.lstStoreBean[${storeIndex}].mccautocomplete"
						value="${store.mccautocomplete}" size="20" maxlength="4" autocomplete="off" class="position-relative"
						onkeypress="fn_storeMccAutocomplete('${storeName}')"/>
				</td> --%>
				<td><label><s:text name="MerchantOnboardMakerAction.mvv"></s:text></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].mvv" maxlength="10" cssClass="formtextbox"></s:textfield></td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.relatonShipManager"></s:text></label></td>
				<td><s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].relatonShipManager" cssClass="select_styled" headerKey=""
					 headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					 list="relShipManagerList" listKey="key" listValue="value"></s:select>
				</td>
				<td width="22%"><label><s:text name="MerchantOnboardMakerAction.incpStatus"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>							
					<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].incorpstatus" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					headerKey="" list="incorpStatusList" listKey="key" listValue="value" cssClass="select_styled" /></td>	
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.gstflag" /></label></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].gstFlag"
						list="#{'0':getText('MerchantOnboardMakerAction.include'),'1':getText('MerchantOnboardMakerAction.exclude')}" />
				</td>
				<td>
					<label><s:text name="MerchantOnboardMakerAction.gstno"></s:text>
					<%-- <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span> --%>
					</label></td>
				<td>
					<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].gstNumber" cssclass="formtextbox"
					onkeypress="AllowNumeric();" maxlength="15"	onPaste="return false" AUTOCOMPLETE="OFF" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.govtRegMerchant"/></label></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].govtRegMerchant" onchange="fn_govtRegMerchant(this, '%{storeName}');"
						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
				</td>
				<td id="${storeName}_govRegLabel" <c:if test="${store.govtRegMerchant ne '1'}"> style="display:none;" </c:if> >
					<label><s:text name="MerchantOnboardMakerAction.homeCountryId" />
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>		
				<td id="${storeName}_govRegValue" <c:if test="${store.govtRegMerchant ne '1'}"> style="display:none;" </c:if> >
					<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].homeCountryId" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					listKey="key" listValue="value" headerKey="" list="homeCountryIdList"
					cssClass="select_styled" ></s:select>
				</td>
			</tr>
			<tr>
				<td id="${storeName}_cyberMercID1" <c:if test="${merchantOnboardBean.merchantEntityName ne '5' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
					<label><s:text name="MerchantOnboardMakerAction.cyberMercID"/></label></td>
				<td id="${storeName}_cyberMercID2" <c:if test="${merchantOnboardBean.merchantEntityName ne '5' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
					<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].cyberMercID" maxlength="30" cssClass="formtextbox"></s:textfield>
				</td>
				<td id="${storeName}_cyberMercPass1" <c:if test="${merchantOnboardBean.merchantEntityName ne '5' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
					<label><s:text name="MerchantOnboardMakerAction.cyberMercPass"/></label></td>
				<td id="${storeName}_cyberMercPass2" <c:if test="${merchantOnboardBean.merchantEntityName ne '5' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
					<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].cyberMercPass" maxlength="70" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td id="${storeName}_cyberMercKey1" <c:if test="${merchantOnboardBean.merchantEntityName ne '5' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
					<label><s:text name="MerchantOnboardMakerAction.cyberMercKey"/></label></td>
				<td id="${storeName}_cyberMercKey2" <c:if test="${merchantOnboardBean.merchantEntityName ne '5' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
					<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].cyberMercKey" maxlength="50" cssClass="formtextbox"></s:textfield>
				</td>
			</tr>
			<tr>
				<td id="${storeName}_aaniPayStoreID1" <c:if test="${merchantOnboardBean.merchantEntityName ne '10' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
					<label><s:text name="MerchantOnboardMakerAction.aaniPayStoreID"/></label></td>
				<td id="${storeName}_aaniPayStoreID2" <c:if test="${merchantOnboardBean.merchantEntityName ne '10' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
					<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].aaniPayStoreID" maxlength="16" onkeypress="AllowNumeric();" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.webSiteName"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].webSiteName" maxlength="40" cssClass="formtextbox"></s:textfield>
				</td>
				<td><label><s:text name="MerchantOnboardMakerAction.websiteAddress"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].websiteAddress" maxlength="40" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.paymentFrecForCard"/></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].paymentFrecForCard" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						listKey="key" listValue="value" headerKey="" id="%{storeName}_paymentFrecForCard" 
						list="#{'1':getText('MerchantOnboardMakerAction.defaultSelect'),
								'1':getText('MerchantOnboardMakerAction.daily'),
								'5':getText('MerchantOnboardMakerAction.tplusn'),
								'4':getText('MerchantOnboardMakerAction.monthly')}"
						cssClass="select_styled" ></s:select>
				</td>
				<td id="${storeName}_paymentFrecForCard1" <c:if test="${store.paymentFrecForCard ne 5}"> style="display: none;" </c:if> >
					<label><s:text name="MerchantOnboardMakerAction.paymentFrecForCardDays"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td id="${storeName}_paymentFrecForCard2" <c:if test="${store.paymentFrecForCard ne 5}"> style="display: none;" </c:if> >
					<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].paymentFrecForCardDays" maxlength="4" cssClass="formtextbox" 
						onkeypress="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.aaniPayTransPayment" /></label></td>
				<td><s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].aaniPayTransPayment"
						onchange="fn_aaniPayTransPayment_Update(this, '%{storeName}', '%{storeIndex}');"
						list="#{'0':getText('MerchantOnboardMakerAction.deffered'),'1':getText('MerchantOnboardMakerAction.realtime')}" />
				</td>
			</tr>
			<tr id="${storeName}_aaniPayTransPayment" <c:if test="${store.aaniPayTransPayment ne 0}"> style="display: none;" </c:if> >
				<td><label><s:text name="MerchantOnboardMakerAction.paymentFrecForAaniPay"/></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].paymentFrecForAaniPay" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						listKey="key" listValue="value" headerKey="" id="%{storeName}_paymentFrecForAaniPay" 
						list="#{'1':getText('MerchantOnboardMakerAction.defaultSelect'),
								'1':getText('MerchantOnboardMakerAction.daily'),
								'5':getText('MerchantOnboardMakerAction.tplusn'),
								'4':getText('MerchantOnboardMakerAction.monthly')}"
						cssClass="select_styled" ></s:select>
				</td>
				<td id="${storeName}_paymentFrecForAaniPay1" <c:if test="${store.paymentFrecForAaniPay ne 5}"> style="display: none;" </c:if> >
					<label><s:text name="MerchantOnboardMakerAction.paymentFrecForAaniPayDays"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td id="${storeName}_paymentFrecForAaniPay2" <c:if test="${store.paymentFrecForAaniPay ne 5}"> style="display: none;" </c:if> >
					<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].paymentFrecForAaniPayDays" maxlength="4" cssClass="formtextbox" 
						onkeypress="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield>
				</td>
			</tr>
			<tr id="${storeName}_pricingDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.storePriceDetailsRequired" /></label></td>
				<td colspan="4">
					<c:choose>
						<c:when test="${store.storePriceDetailsRequired eq 1}">
							<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storePriceDetailsRequired" 
								onchange="fn_storePriceDetailsRequired(this, '%{storeName}', '%{storeIndex}');"
								list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="${storeName}_PriceModalLink" href="#" data-toggle="modal" data-target="#${storeName}_PriceModal" >&nbsp;<span>View Details</span></a>
						</c:when>
						<c:otherwise>
							<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storePriceDetailsRequired" 
								onchange="fn_storePriceDetailsRequired(this, '%{storeName}', '%{storeIndex}');"
								list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="${storeName}_PriceModalLink" href="#" data-toggle="modal" data-target="#${storeName}_PriceModal" style="display:none;" >&nbsp;<span>View Details</span></a>
						</c:otherwise>	
					</c:choose>
					<!-- STORE PriceModal - START -->
					<div class="modal modal-wide fade" id="${storeName}_PriceModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl modal-dialog-centered">
							<div class="modal-content">
								<div class="modal-header">
									<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.storePriceDetailsRequired" /></h4>
																	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="${storeName}_pricingInfo_Row1">
											<td><label><s:text name="MerchantOnboardMakerAction.captureMerchantServiceFeeAt" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
											</td>
											<td>
												<c:choose>
													<c:when test="${store.storeCaptureServiceFeeAt eq 3}">
														<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeCaptureServiceFeeAt"
															list="#{'2':getText('MerchantOnboardMakerAction.storeLevel'),'3':getText('MerchantOnboardMakerAction.terminalLevel')}" value="3" />
													</c:when>
													<c:otherwise>
														<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeCaptureServiceFeeAt"
															list="#{'2':getText('MerchantOnboardMakerAction.storeLevel'),'3':getText('MerchantOnboardMakerAction.terminalLevel')}" value="2" />
													</c:otherwise>
												</c:choose>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeePlanType" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<c:choose>
													<c:when test="${store.storeFeePlanType eq 2}">
														<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeFeePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
															list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" 
															onchange="getStoreServiceFeePlan('%{storeName}', '%{storeIndex}');" value="2"  />
													</c:when>
													<c:otherwise>
														<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeFeePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
															list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" 
															onchange="getStoreServiceFeePlan('%{storeName}', '%{storeIndex}');" value="1"  />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr id="${storeName}_pricingInfo_Row2">
											<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeeplan" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<c:choose>
													<c:when test="${store.storeFeePlanType eq 2}">
														<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeMerchantServiceFeePlan" 
															id="%{storeName}_storeservicefeeplan" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
															cssClass="select_styled" list="feePlanListFortire" listKey="value" listValue="value" ></s:select>
													</c:when>
													<c:otherwise>
														<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeMerchantServiceFeePlan" 
															id="%{storeName}_storeservicefeeplan" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
															cssClass="select_styled" list="feePlanList" listKey="value" listValue="value" ></s:select>
													</c:otherwise>
												</c:choose>
											</td>
											<%-- <td><label><s:text name="MerchantOnboardMakerAction.mercFeeDeduWaiver" /></label></td>
											<td>
												<c:choose>
													<c:when test="${store.storeMercFeeDeduWaiver eq 1}">
														<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeMercFeeDeduWaiver"
															list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" value="1" />
													</c:when>
													<c:otherwise>
														<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeMercFeeDeduWaiver"
															list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" value="0" />
													</c:otherwise>
												</c:choose>
											</td> --%>
										</tr>
									</table>
								</div>
								<div class="modal-footer text-center d-block">
									<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
								</div>
							</div>
						</div>
					</div>
					<!-- STORE PriceModal - END -->
				</td>
			</tr>
			
			<tr id="${storeName}_addressDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.useSameAsMercAddressForStore"/></label></td> 
				<td colspan="4">
					<c:choose>
						<c:when test="${store.useSameAsMercAddressForStore eq 1}">
							<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].useSameAsMercAddressForStore" 
								onchange="fn_useSameAsMercAddressForStore(this, '%{storeName}', '%{storeIndex}');"
								list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="${storeName}_AddressModalLink" href="#" data-toggle="modal" data-target="#${storeName}_AddressModal" >&nbsp;<span>View Details</span></a>
						</c:when>
						<c:otherwise>
							<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].useSameAsMercAddressForStore" 
								onchange="fn_useSameAsMercAddressForStore(this, '%{storeName}', '%{storeIndex}');"
								list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="${storeName}_AddressModalLink" href="#" data-toggle="modal" data-target="#${storeName}_AddressModal" style="display:none;" >&nbsp;<span>View Details</span></a>
						</c:otherwise>	
					</c:choose>
					<!-- STORE AddressModal - START -->
					<div class="modal modal-wide fade" id="${storeName}_AddressModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl modal-dialog-centered">
							<div class="modal-content">
								<div class="modal-header">
									<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.useSameAsMercAddressForStore" /></h4>
																	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="${storeName}_addressInfo_Row1">
											<td><label><s:text name="MerchantOnboardMakerAction.addressline1"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeaddressline1" maxlength="50" cssClass="formtextbox" />
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.addressline2"/></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeaddressline2" maxlength="50" cssClass="formtextbox" />
											</td>
										</tr>
										<tr id="${storeName}_addressInfo_Row2">
											<td><label><s:text name="MerchantOnboardMakerAction.country"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<input type="hidden" id="${storeName}_ccountry" name="merchantOnboardBean.lstStoreBean[${storeIndex}].storeccountry" value="${merchantOnboardBean.ccountry}" readonly="readonly"/>
												<input type="text" id="${storeName}_country" name="merchantOnboardBean.lstStoreBean[${storeIndex}].storecountry" value="${merchantOnboardBean.country}" readonly="readonly" class="position-relative"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.state"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<input type="hidden" id="${storeName}_cstate" name="merchantOnboardBean.lstStoreBean[${storeIndex}].storecstate" value="${store.storecstate}" />
												<input type="text" id="${storeName}_state" name="merchantOnboardBean.lstStoreBean[${storeIndex}].storestate" value="${store.storestate}" size="20" maxlength="40" autocomplete="off" 
												onkeypress="AllowAlphabeticWithSpace();fn_storeStateDetails('${storeName}');" class="position-relative"/>
											</td>
										</tr>
										<tr id="${storeName}_addressInfo_Row3">
											<td><label><s:text name="MerchantOnboardMakerAction.citysuburb"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<input type="hidden" id="${storeName}_cselectcity" name="merchantOnboardBean.lstStoreBean[${storeIndex}].storecselectcity" value="${store.storecselectcity}" />
												<input type="text" id="${storeName}_selectcity" name="merchantOnboardBean.lstStoreBean[${storeIndex}].storeselectcity" value="${store.storeselectcity}" size="20" maxlength="40" autocomplete="off" 
												onkeypress="AllowAlphabeticWithSpace();fn_storeCityDetails('${storeName}');" class="position-relative"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.postalcode"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storepostalcode" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
											</td>
										</tr>
										<tr id="${storeName}_addressInfo_Row4">
											<td><label><s:text name="MerchantOnboardMakerAction.businessContactEmailID"/></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storebusinessContactEmailID" maxlength="50" cssClass="formtextbox" />
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.businessContactMobileNo"/></label></td>
											<td>
												<c:choose>
													<c:when test="${store.storebusinessContactMobileNo eq 0}">
														<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storebusinessContactMobileNo" value="" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</c:when>
													<c:otherwise>
														<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storebusinessContactMobileNo" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</c:otherwise>
												</c:choose>
											</td>			
										</tr>
										<tr id="${storeName}_addressInfo_Row5">
											<td><label><s:text name="MerchantOnboardMakerAction.technicalContactEmailID"/></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storetechnicalContactEmailID" maxlength="50" cssClass="formtextbox" />
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.technicalContactMobileNo"/></label></td>
											<td>
												<c:choose>
													<c:when test="${store.storetechnicalContactMobileNo eq 0}">
														<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storetechnicalContactMobileNo" value="" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</c:when>
													<c:otherwise>
														<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storetechnicalContactMobileNo" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</table>
								</div>
								<div class="modal-footer text-center d-block">
									<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
								</div>
							</div>
						</div>
					</div>
					<!-- STORE AddressModal - END -->
				</td>								
			</tr>
			
			<tr id="${storeName}_riskDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.storeRiskDetailsRequired" /></label></td>
				<td colspan="4">
					<c:choose>
						<c:when test="${store.storeRiskDetailsRequired eq 1}">
							<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeRiskDetailsRequired" 
								onchange="fn_storeRiskDetailsRequired(this, '%{storeName}', '%{storeIndex}');"
								list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="${storeName}_RiskModalLink" href="#" data-toggle="modal" data-target="#${storeName}_RiskModal" >&nbsp;<span>View Details</span></a>
						</c:when>
						<c:otherwise>
							<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeRiskDetailsRequired" 
								onchange="fn_storeRiskDetailsRequired(this, '%{storeName}', '%{storeIndex}');"
								list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="${storeName}_RiskModalLink" href="#" data-toggle="modal" data-target="#${storeName}_RiskModal" style="display:none;" >&nbsp;<span>View Details</span></a>
						</c:otherwise>	
					</c:choose>
					<!-- STORE RiskModal - START -->
					<div class="modal modal-wide fade" id="${storeName}_RiskModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl modal-dialog-centered">
							<div class="modal-content">
								<div class="modal-header">
									<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.storeRiskDetailsRequired" /></h4>
																	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="${storeName}_riskInfo_Row1">
											<td><label><s:text name="MerchantOnboardMakerAction.riskCategory" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].riskCategory" headerKey="" 
													headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
													list="riskCategoryList" listKey="key" listValue="value" cssClass="select_styled">
												</s:select>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.padssPCIdssCertExpDate" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].padssPCIdssCertExpDate" style="width: 200px !important;" 
													cssClass="date-picker" readonly="true" id="%{storeName}_padssPCIdssCertExpDate" required="true"></s:textfield>
												<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="${storeName}_eff" 
													onclick="javascript:NewCssCal('${storeName}_padssPCIdssCertExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
													class="calendar-icon">	
											</td>
										</tr>
										<tr id="${storeName}_riskInfo_Row2">
											<td><label><s:text name="MerchantOnboardMakerAction.cloudCertExpDate" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].cloudCertExpDate" style="width: 200px !important;" 
													cssClass="date-picker" readonly="true" id="%{storeName}_cloudCertExpDate" required="true"></s:textfield>
												<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="${storeName}_cloudCertExpDate1"  
													onclick="javascript:NewCssCal('${storeName}_cloudCertExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
													class="calendar-icon">
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.tradeLicenseExpDate" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].tradeLicenseExpDate" style="width: 200px !important;" 
													cssClass="date-picker" readonly="true" id="%{storeName}_tradeLicenseExpDate" required="true"></s:textfield>
												<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="${storeName}_tradeLicenseExpDate1"  
													onclick="javascript:NewCssCal('${storeName}_tradeLicenseExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
													class="calendar-icon">	
											</td>
										</tr>
										<tr id="${storeName}_riskInfo_Row3">
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField1" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].additionalUDFField1" maxlength="10" cssClass="formtextbox"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue1" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].additionalUDFValue1" maxlength="10" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="${storeName}_riskInfo_Row4">
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField2" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].additionalUDFField2" maxlength="10" cssClass="formtextbox"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue2" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].additionalUDFValue2" maxlength="10" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="${storeName}_riskInfo_Row5">
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField3" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].additionalUDFField3" maxlength="10" cssClass="formtextbox"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue3" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].additionalUDFValue3" maxlength="10" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="${storeName}_riskInfo_Row6">
											<td><label><s:text name="MerchantOnboardMakerAction.transCountperMonth" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].transCountperMonth" 
													id="%{storeName}_transCountperMonth" maxlength="7" cssClass="formtextbox" 
													oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.averageTicketSize" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].averageTicketSize" 
													id="%{storeName}_averageTicketSize" maxlength="25" cssClass="formtextbox" 
													onchange="calculateQuaterlySaleAmt('%{storeName}', '%{storeIndex}');" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
										</tr>
										<tr id="${storeName}_riskInfo_Row7">
											<td><label><s:text name="MerchantOnboardMakerAction.quarterlySale" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].quarterlySale" 
													id="%{storeName}_quarterlySale" maxlength="25" readonly="true" 
													cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.transToRefundPercent" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].transToRefundPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
										</tr>
										<tr id="${storeName}_riskInfo_Row8">
											<td><label><s:text name="MerchantOnboardMakerAction.transToChargebackPercent" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].transToChargebackPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.transPercent" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].transPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
										</tr>
										<tr id="${storeName}_riskInfo_Row9">
											<td><label><s:text name="MerchantOnboardMakerAction.internationalTransPercent" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].internationalTransPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
										</tr>
									</table>
								</div>
								<div class="modal-footer text-center d-block">
									<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
								</div>
							</div>
						</div>
					</div>
					<!-- STORE RiskModal - END -->
				</td>
			</tr>
			<tr id="${storeName}_accountDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.useSameAsMercAccountForStore"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td> 
				<td colspan="4">
					<c:choose>
						<c:when test="${store.useSameAsMercAccountForStore eq '1'}">
							<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].useSameAsMercAccountForStore" 
								onchange="fn_useSameAsMercAccountForStore(this, '%{storeName}', '%{storeIndex}');"
								list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="${storeName}_AccountModalLink" href="#" data-toggle="modal" data-target="#${storeName}_AccountModal" >&nbsp;<span>View Details</span></a>
						</c:when>
						<c:otherwise>
							<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].useSameAsMercAccountForStore" 
								onchange="fn_useSameAsMercAccountForStore(this, '%{storeName}', '%{storeIndex}');"
								list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="${storeName}_AccountModalLink" href="#" data-toggle="modal" data-target="#${storeName}_AccountModal" style="display:none;" >&nbsp;<span>View Details</span></a>
						</c:otherwise>	
					</c:choose>
					<!-- STORE AccountModal - START -->
					<div class="modal modal-wide fade" id="${storeName}_AccountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl modal-dialog-centered">
							<div class="modal-content">
								<div class="modal-header">
									<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.useSameAsMercAccountForStore" /></h4>
																	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="${storeName}_accountDetails_Row1">
											<td class="formtext"><label><s:text name="MerchantOnboardMakerAction.accountWithBank"/>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
											</td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeacctWithBank" headerKey="" 
													headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
													list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}"
													listKey="key" id="%{storeName}_storeacctWithBank" listValue="value" cssClass="select_styled" >
												</s:select>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.defaultAccount" /></label></td>
											<td>
												<c:choose>
													<c:when test="${store.storedefAccount eq 1}">
														<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storedefAccount" value="1"
															list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
													</c:when>
													<c:otherwise>
														<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storedefAccount" value="0"
															list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr id="${storeName}_accountDetails_Row2">
											<td id="${storeName}_acctBranch" <c:if test="${store.storeacctWithBank ne '1'}"> style="display:none;" </c:if> class="formtext">
												<label><s:text name="MerchantOnboardMakerAction.acctBranch"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
											</td>
											<td id="${storeName}_acctBranchDetails" <c:if test="${store.storeacctWithBank ne '1'}"> style="display:none;" </c:if> >
												<input type="hidden" id="${storeName}_cStoreacctBranch" name="merchantOnboardBean.lstStoreBean[${storeIndex}].cStoreacctBranch" value="${store.cStoreacctBranch}" />
												<input type="text" id="${storeName}_storeacctBranch" name="merchantOnboardBean.lstStoreBean[${storeIndex}].storeacctBranch" value="${store.storeacctBranch}" 
													size="20" maxlength="50" autocomplete="off" onkeypress="AllowAlphaNumericWithSpace();fn_storeacctBranch('${storeName}');" 
													class="position-relative" />
											</td>
											<td id="${storeName}_bankName" <c:if test="${store.storeacctWithBank ne '0'}"> style="display:none;" </c:if> class="formtext">
												<label><s:text name="MerchantOnboardMakerAction.bankName"></s:text>					
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
											</td>
											<td id="${storeName}_bankNameText" <c:if test="${store.storeacctWithBank ne '0'}"> style="display:none;" </c:if> class="formtext">
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeacctBankText" maxlength="50" cssClass="formtextbox"/>
											</td>
											<td id="${storeName}_branchName" <c:if test="${store.storeacctWithBank ne '0'}"> style="display:none;" </c:if> class="formtext">
												<label><s:text name="MerchantOnboardMakerAction.branchName"></s:text></label>
											</td>
											<td id="${storeName}_branchNameText" <c:if test="${store.storeacctWithBank ne '0'}"> style="display:none;" </c:if> class="formtext">
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeacctBranchText" maxlength="50" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="${storeName}_accountDetails_Row3">
											<td><label><s:text name="MerchantOnboardMakerAction.IFSC"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeifsc" maxlength="11" cssClass="formtextbox" /></td>
											<td><label><s:text name="MerchantOnboardMakerAction.accountname"></s:text></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeaccountname" label="MerchantOnboardMakerAction.accountname" maxlength="40" cssClass="formtextbox" />
											</td>
										</tr>
										<tr id="${storeName}_accountDetails_Row4">
											<td><label><s:text name="MerchantOnboardMakerAction.currency"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storecurrency" cssClass="select_styled" headerKey="" 
												 headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
												 list="crcyList" listKey="key" listValue="value"></s:select>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.accountNumber"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeacctNumber" onkeypress="AllowAlpaNumeric();" maxlength="36" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr>
											<td><label><s:text name="MerchantOnboardMakerAction.cifNumber"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storecifNumber" onkeypress="AllowNumeric();" maxlength="7" cssClass="formtextbox"/>
											</td>
											<%-- <td><label><s:text name="MerchantOnboardMakerAction.stmtfrequency"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
											</td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storestmtfreq" 
													cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
													list="stmttFreqList" listKey="key" listValue="value"></s:select>
											</td> --%>
										</tr>
									</table>
								</div>
								<div class="modal-footer text-center d-block">
									<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
								</div>
							</div>
						</div>
					</div>
					<!-- STORE AccountModal - END -->
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.storeType"/></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].storeType"
						list="#{'1':getText('MerchantOnboardMakerAction.Online'),'2':getText('MerchantOnboardMakerAction.Offline')}" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.isTerminal"/></label></td>
				<td><div id="checkbox">
					<c:choose>
						<c:when test="${store.termneed eq '1'}">
							<s:checkbox name="merchantOnboardBean.lstStoreBean[%{storeIndex}].termneed" id="%{storeName}_termneeded"
								value="1" fieldValue="1" labelposition="before" theme="simple" onchange="fn_StoreTermNeed(this, '%{storeName}', '%{storeIndex}'); fn_merchantEntityValue();"/>
						</c:when>
						<c:otherwise>	
							<s:checkbox name="merchantOnboardBean.lstStoreBean[%{storeIndex}].termneed" id="%{storeName}_termneeded"
								value="0" fieldValue="1" labelposition="before" theme="simple" onchange="fn_StoreTermNeed(this, '%{storeName}', '%{storeIndex}'); fn_merchantEntityValue();"/>	
						</c:otherwise>
					</c:choose>
					</div>
				</td>
				<td colspan="2" align="center" id="${storeName}_docLinkRow">
					<c:choose>
						<c:when test="${store.tempFolderName ne null}">
							<c:set var="storetempFolderName" value="${fn:substring(store.tempFolderName, 0, 8)}" />
						</c:when>
						<c:otherwise>
						    <c:set var="storetempFolderName" value="${strTempFoldername}" />
						</c:otherwise>
					</c:choose>
					<a href="#" class="global-links" onclick="javascript:openWindowAttachmentsForStore('${storeName}', '${storeIndex}', '${storetempFolderName}', '${merchantOnboardBean.refSqid}');">Add/View Attached Documents For Store <s:property value="#storeCount" /></a>
					<s:hidden name="merchantOnboardBean.lstStoreBean[%{storeIndex}].tempFolderName" id="%{storeName}_tempFolderName"></s:hidden>
				</td>
			</tr>
			
			<tr id="${storeName}_terminalDetails" <c:if test="${store.termneed eq '0'}"> style="display:none;" </c:if> >
				<td colspan="6">
					<table style="width: 100%;" id="${storeName}_terminalTable">
					<s:set var="terminalCount" value="0" />
					<s:set var="terminalIndex" value="0" />
					<s:iterator value="#store.lstTerminalBean" var="terminal" status="termstatus">
					<c:if test="${terminal ne null}">
						<s:set var="terminalIndex" value="%{#termstatus.Index}" />
						<s:set var="terminalCount" value="%{#terminalIndex + 1}" />
						<s:set var="terminalName" value="%{#storeName+'_TERM'+ #terminalCount}" />
						<tr>
							<td>
							 	<ul class="nav nav-tabs" style="height: 15px;">
							 		<li class="nav-item" style="width: 50%; padding-left: 10px; border-right: none;">Terminal Details - <s:property value='terminalCount' /> </li>
							 		<li class="nav-item" style="width: 50%; padding-right: 10px; border-left: none; text-align: right;">
							 			<img src="${pageContext.request.contextPath}/images/trash_icon.png" alt="trashbutton" type="button" class="trash-icon" onclick="fn_deleteTerminalDetails(this, '${storeName}');  fn_merchantEntityValue();"/>
							 		</li>
							 	</ul>
							 	<div class="tab-content"><div class="tab-pane show active">
							 	<table id="${terminalName}_details" align="center" border="0" width="100%" style="margin-top: 20px;"> 
							 		<tr> 
					 					<td><label><s:text name="MerchantOnboardMakerAction.productType"/> 
					 						<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label> 
					 					</td> 
										<td>
											<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].termProuctType" cssClass="select_styled"
												list="lstProuctType" listKey="key" listValue="value"></s:select>
											<%-- <select name="merchantOnboardBean.lstStoreBean[${storeIndex}].lstTerminalBean[${terminalIndex}].termProuctType" class="select_styled">
											    <c:forEach var="item" items="${lstProuctType}">
											        <option value="${item.key}" <c:if test="${item.key == '1'}">selected</c:if> >${item.value}</option>
											    </c:forEach>
											</select> --%>
										</td>
										<td><label><s:text name="MerchantOnboardMakerAction.tid"></s:text>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
					 					<td>
					 						<s:hidden name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].termSqid" cssClass="formtextbox" maxlength="30" />
						 					<s:if test="autoGenerateTid == 0">
												<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].termCode" cssClass="formtextbox" maxlength="15"></s:textfield>
											</s:if>
											<s:elseif test="autoGenerateTid == 1">
												<input type="text" name="merchantOnboardBean.lstStoreBean[${storeIndex}].lstTerminalBean[${terminalIndex}].termCode" value="${terminal.termCode}" 
													style="color: black;background: lightgray;" class="formtextbox" maxlength="15" readonly="readonly">
											</s:elseif>
					 					</td>
					 				</tr>
					 				<tr>
					 					<td><label><s:text name="MerchantOnboardMakerAction.terminalName"></s:text>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator" /> </span></label></td>
										<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalName" cssClass="formtextbox" maxlength="25" ></s:textfield></td>
						 				<td><label><s:text name="MerchantOnboardMakerAction.paymentMethod" />
						 					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
						 				<td colspan="3">
						 					<s:checkboxlist name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].paymentMethod" 
						 						listKey="key" listValue="value" list="paymentMethodList"></s:checkboxlist>
						 				</td>
						 			</tr>
									<tr>
						 				<td id="${terminalName}_aaniPayTermID1" <c:if test="${merchantOnboardBean.merchantEntityName ne '10' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
						 					<label><s:text name="MerchantOnboardMakerAction.aaniPayTermID" /></label></td>
										<td id="${terminalName}_aaniPayTermID2" <c:if test="${merchantOnboardBean.merchantEntityName ne '10' and merchantOnboardBean.merchantEntityName ne '5,10'}"> style="display: none;" </c:if> >
											<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].aaniPayTermID" maxlength="16" onkeypress="AllowNumeric();" cssClass="formtextbox" ></s:textfield>
										</td>
									</tr>
					 				<tr>
										<%-- <td><label><s:text name="MerchantOnboardMakerAction.terminalmcc"/>
				 							<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
										<td> 
											<input type="hidden" id="${terminalName}_cterminalmccautoComplete" 
												name="merchantOnboardBean.lstStoreBean[${storeIndex}].lstTerminalBean[${terminalIndex}].cterminalmccautoComplete" 
												value="${terminal.cterminalmccautoComplete}" />
											<input type="text" id="${terminalName}_terminalmccautocomplete" 
												name="merchantOnboardBean.lstStoreBean[${storeIndex}].lstTerminalBean[${terminalIndex}].terminalmccautocomplete" 
												value="${terminal.terminalmccautocomplete}" size="20" maxlength="4" autocomplete="off" class="position-relative" 
												onkeypress="fn_terminalMccAutocomplete('${terminalName}')"/>
										</td> --%>
										<td><label><s:text name="MerchantOnboardMakerAction.terminalchargeplan"/></label></td>	
										<td><input type="text" id="${terminalName}_dftTrmlRcrngChrgePlnIdname" name="merchantOnboardBean.lstStoreBean[${storeIndex}].lstTerminalBean[${terminalIndex}].dftTrmlRcrngChrgePlnIdname" 
												value="${terminal.dftTrmlRcrngChrgePlnIdname}" size="20" maxlength="40" autocomplete="off" class="position-relative" 
												onkeypress="fn_RcrngChrgePln('${terminalName}');" /> 
											<input type="hidden" id="${terminalName}_RcrngChrgePlnId" name="merchantOnboardBean.lstStoreBean[${storeIndex}].lstTerminalBean[${terminalIndex}].dftTrmlRcrngChrgePlnId" 
												value="${terminal.dftTrmlRcrngChrgePlnId}" />
										</td>
					 				</tr>
					 				<tr>
					 					<td><label><s:text name="MerchantOnboardMakerAction.terminalRecurringPlanWaiver" /></label></td>
					 					<td><s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalRecurringPlanWaiver" 
												list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}"/>
										</td>
									</tr>
					 				<tr>
									 	<td><label><s:text name="MerchantOnboardMakerAction.enableRiskProfile"/>
									 		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td></td>
									 	<td>
											<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalRiskProfileIndc" 
												onchange="showRiskDetail(this, '%{terminalName}');"
					  							list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
										</td>
									 	<td id="${terminalName}_riskLabel" <c:if test="${terminal.terminalRiskProfileIndc ne '1'}"> style="display:none;" </c:if> >
									 		<label><s:text name="MerchantOnboardMakerAction.riskProfile" />
									 		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
									 	<td id="${terminalName}_riskValue" <c:if test="${terminal.terminalRiskProfileIndc ne '1'}"> style="display:none;" </c:if> >
									 		<s:select list="ecomRiskProfileNamesList" name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalRiskProfile" 
									 			headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" listKey="key" listValue="value" cssClass="select_styled" ></s:select>
									 	</td>
								 	</tr>
								 	<tr id="${terminalName}_accountdetails">
										<td><label><s:text name="MerchantOnboardMakerAction.useSameAsStoreAccountForTerm"/>
					 						<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
					 					</td>
										<td>
											<c:choose>
												<c:when test="${terminal.useSameAsStoreAccountForTerm eq '1'}">
													<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].useSameAsStoreAccountForTerm" 
											  			onchange="fn_useSameAsStoreAccountForTerm(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
											  			list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
											  		<a id="${terminalName}_AccountModalLink" href="#" data-toggle="modal" data-target="#${terminalName}_AccountModal" >&nbsp;<span>View Details</span></a>
												</c:when>
												<c:otherwise>
													<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].useSameAsStoreAccountForTerm" 
											  			onchange="fn_useSameAsStoreAccountForTerm(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
											  			list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
											  		<a id="${terminalName}_AccountModalLink" href="#" data-toggle="modal" data-target="#${terminalName}_AccountModal" style="display:none;" >&nbsp;<span>View Details</span></a>
												</c:otherwise>	
											</c:choose>
											<!-- TERMINAL AccountModal - START -->
											<div class="modal modal-wide fade" id="${terminalName}_AccountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
												<div class="modal-dialog modal-xl modal-dialog-centered">
													<div class="modal-content">
														<div class="modal-header">
															<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.useSameAsStoreAccountForTerm" /></h4>
															<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
														
														</div>
														<div class="modal-body">
															<table border="0" width="100%" cellspacing="4">
																<tr id="${terminalName}_accountinfo_Row1">
																	<td class="formtext"><label><s:text name="MerchantOnboardMakerAction.accountWithBank"/>
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
																	</td>
																	<td>
																		<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalacctWithBank" headerKey="" 
																			headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
																			list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}"
																			listKey="key" id="%{terminalName}_terminalacctWithBank" listValue="value" cssClass="select_styled" >
																		</s:select>
																	</td>
																	<td><label><s:text name="MerchantOnboardMakerAction.defaultAccount" /></label></td>
																	<td>
																		<c:choose>
																			<c:when test="${terminal.terminaldefAccount eq 1}">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminaldefAccount" value="1"
																					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
																			</c:when>
																			<c:otherwise>
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminaldefAccount" value="0"
																					list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
																			</c:otherwise>
																		</c:choose>					
																	</td>
																</tr>
																<tr id="${terminalName}_accountinfo_Row2">
																	<td id="${terminalName}_acctBranch" <c:if test="${terminal.terminalacctWithBank ne '1'}"> style="display:none;" </c:if> class="formtext">
																		<label><s:text name="MerchantOnboardMakerAction.acctBranch"></s:text></label>
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
																	</td>
																	<td id="${terminalName}_acctBranchDetails" <c:if test="${terminal.terminalacctWithBank ne '1'}"> style="display:none;" </c:if> >
																		<input type="hidden" id="${terminalName}_cTerminalacctBranch" name="merchantOnboardBean.lstStoreBean[${storeIndex}].lstTerminalBean[${terminalIndex}].cTerminalacctBranch" 
																			value="${terminal.cTerminalacctBranch}" />
																		<input type="text" id="${terminalName}_terminalacctBranch" name="merchantOnboardBean.lstStoreBean[${storeIndex}].lstTerminalBean[${terminalIndex}].terminalacctBranch" 
																			value="${terminal.terminalacctBranch}" size="20" maxlength="50" autocomplete="off" 
																			onkeypress="AllowAlphaNumericWithSpace();fn_terminalacctBranch('${terminalName}');" class="position-relative" />
																	</td>
																	<td id="${terminalName}_bankName" <c:if test="${terminal.terminalacctWithBank ne '0'}"> style="display:none;" </c:if> class="formtext">
																		<label><s:text name="MerchantOnboardMakerAction.bankName"></s:text>
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
																	</td>
																	<td id="${terminalName}_bankNameText" <c:if test="${terminal.terminalacctWithBank ne '0'}"> style="display:none;" </c:if> class="formtext">
																		<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalacctBankText" maxlength="50" cssClass="formtextbox"/>
																	</td>
																	<td id="${terminalName}_branchName" <c:if test="${terminal.terminalacctWithBank ne '0'}"> style="display:none;" </c:if> class="formtext">
																		<label><s:text name="MerchantOnboardMakerAction.branchName"></s:text></label>
																	</td>
																	<td id="${terminalName}_branchNameText" <c:if test="${terminal.terminalacctWithBank ne '0'}"> style="display:none;" </c:if> class="formtext">
																		<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalacctBranchText" maxlength="50" cssClass="formtextbox"/>
																	</td>
																</tr>
																<tr id="${terminalName}_accountinfo_Row3">
																	<td><label><s:text name="MerchantOnboardMakerAction.IFSC"></s:text>
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></label></span>
																	</td>
																	<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalifsc" maxlength="11" cssClass="formtextbox" /></td>
																	<td><label><s:text name="MerchantOnboardMakerAction.accountname"></s:text></label></td>
																	<td>
																		<s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalaccountname" label="MerchantOnboardMakerAction.accountname" maxlength="20" cssClass="formtextbox" />
																	</td>
																</tr>	
																<tr id="${terminalName}_accountinfo_Row4">
																	<td><label><s:text name="MerchantOnboardMakerAction.currency"></s:text>
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></label></span>
																	</td>
																	<td>
																		<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalcurrency" 
																			cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
																			list="crcyList" listKey="key" listValue="value"></s:select>
																	</td>
																	<td><label><s:text name="MerchantOnboardMakerAction.accountNumber"></s:text>
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																	<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalacctNumber" onkeypress="AllowAlpaNumeric();" maxlength="36" cssClass="formtextbox"/>
																	</td>
																</tr>
																<tr id="${terminalName}_accountinfo_Row5">
																	<td><label><s:text name="MerchantOnboardMakerAction.cifNumber"></s:text>
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																	<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalcifNumber" onkeypress="AllowNumeric();" maxlength="7" cssClass="formtextbox"/>
																	</td>
																	<%-- <td><label><s:text name="MerchantOnboardMakerAction.stmtfrequency"></s:text>
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
																	</td>
																	<td>
																		<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalstmtfreq" 
																			cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
																			list="stmttFreqList" listKey="key" listValue="value"></s:select>
																	</td> --%>
																</tr>
															</table>
														</div>
														<div class="modal-footer text-center d-block">
															<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
														</div>
													</div>
												</div>
											</div>
											<!-- TERMINAL AccountModal - END -->
										</td>
					 				</tr>
						 			<tr id="${terminalName}_pricingDetails">
						 				<td><label><s:text name="MerchantOnboardMakerAction.terminalPriceDetailsRequired" />
						 					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
						 				<td>
						 					<c:choose>
												<c:when test="${terminal.terminalPriceDetailsRequired eq 1}">
													<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalPriceDetailsRequired" 
								 						onchange="fn_terminalPriceDetailsRequired(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
								 						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
								 					<a id="${terminalName}_PriceModalLink" href="#" data-toggle="modal" data-target="#${terminalName}_PriceModal" >&nbsp;<span>View Details</span></a>
												</c:when>
												<c:otherwise>
													<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalPriceDetailsRequired" 
								 						onchange="fn_terminalPriceDetailsRequired(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
								 						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
								 					<a id="${terminalName}_PriceModalLink" href="#" data-toggle="modal" data-target="#${terminalName}_PriceModal" style="display:none;" >&nbsp;<span>View Details</span></a>
												</c:otherwise>	
											</c:choose>
											<!-- TERMINAL PriceModal - START -->
											<div class="modal modal-wide fade" id="${terminalName}_PriceModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
												<div class="modal-dialog modal-xl modal-dialog-centered">
													<div class="modal-content">
														<div class="modal-header">
															<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.terminalPriceDetailsRequired" /></h4>
																													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
														
														</div>
														<div class="modal-body">
															<table border="0" width="100%" cellspacing="4">
																<tr id="${terminalName}_pricingInfo_Row1">
																	<td><label><s:text name="MerchantOnboardMakerAction.captureMerchantServiceFeeAt" />
																	<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
																	</td>
																	<td>
																		<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalCaptureServiceFeeAt" value="3"
																			list="#{'3':getText('MerchantOnboardMakerAction.terminalLevel')}" />
														
																	</td>
																	<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeePlanType" />
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																	<td>
																		<c:choose>
																			<c:when test="${terminal.terminalFeePlanType eq 2}">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalFeePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
																					list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" value="2"
																					onchange="getTerminalServiceFeePlan('%{terminalName}', '%{storeIndex}', '%{terminalIndex}');" />
																			</c:when>
																			<c:otherwise>
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalFeePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
																					list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" value="1"
																					onchange="getTerminalServiceFeePlan('%{terminalName}', '%{storeIndex}', '%{terminalIndex}');" />
																			</c:otherwise>
																		</c:choose>
																	</td>
																</tr>
																<tr id="${terminalName}_pricingInfo_Row2">
																	<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeeplan" />
																		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																	<td>
																		<c:choose>
																			<c:when test="${terminal.terminalFeePlanType eq 2}">
																				<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalMerchantServiceFeePlan"
																				    id="%{terminalName}_terminalservicefeeplan" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
																					cssClass="select_styled" list="feePlanListFortire" listKey="value" listValue="value" ></s:select>
																			</c:when>
																			<c:otherwise>
																				<s:select name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalMerchantServiceFeePlan" 
																				    id="%{terminalName}_terminalservicefeeplan" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
																					cssClass="select_styled" list="feePlanList" listKey="value" listValue="value" ></s:select>
																			</c:otherwise>
																		</c:choose>
																	</td>
																	<%-- <td><label><s:text name="MerchantOnboardMakerAction.mercFeeDeduWaiver" /></label></td>
																	<td>
																		<c:choose>
																			<c:when test="${terminal.terminalMercFeeDeduWaiver eq 1}">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalMercFeeDeduWaiver" value="1"
																				list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
																			</c:when>
																			<c:otherwise>
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].terminalMercFeeDeduWaiver" value="0"
																				list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
																			</c:otherwise>
																		</c:choose>
																	</td> --%>
																</tr>
															</table>
														</div>
														<div class="modal-footer text-center d-block">
															<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
														</div>
													</div>
												</div>
											</div>
											<!-- TERMINAL PriceModal - END -->
						 				</td>
						 			</tr>
						 			<tr id="${terminalName}_RRFdetails">
										<td><label><s:text name="MerchantOnboardMakerAction.rollingReserveFundRequired"/></label></td>
										<td>
											<c:choose>
												<c:when test="${terminal.rollingReserveFundRequired eq 1}">
													<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].rollingReserveFundRequired" 
														onchange="fn_RRFdetailsForTerm(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
														list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
													<a id="${terminalName}_RRFModalLink" href="#" data-toggle="modal" data-target="#${terminalName}_RRFModal" >&nbsp;<span>View Details</span></a>
												</c:when>
												<c:otherwise>
													<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].rollingReserveFundRequired" 
														onchange="fn_RRFdetailsForTerm(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
														list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
													<a id="${terminalName}_RRFModalLink" href="#" data-toggle="modal" data-target="#${terminalName}_RRFModal" style="display:none;" >&nbsp;<span>View Details</span></a>
												</c:otherwise>	
											</c:choose>
											<!-- TERMINAL RRFModal - START -->
											<div class="modal modal-wide fade" id="${terminalName}_RRFModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
												<div class="modal-dialog modal-xl modal-dialog-centered">
													<div class="modal-content">
														<div class="modal-header">
															<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.rollingReserveFundRequired" /></h4>
																													<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
														
														</div>
														<div class="modal-body">
															<table border="0" width="100%" cellspacing="4">
																<c:choose>
																	<c:when test="${terminal.rrfIndc eq 1}">
																		<tr id="${terminalName}_RRFdetails_Row1">
																			<td>
																				<label><s:text name="MerchantOnboardMakerAction.rrfIndc"/></label>
																			</td>
																			<td nowrap="nowrap">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].rrfIndc" 
																					id="%{terminalName}_rrfIndc" onchange="rrfFieldsEnable(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
																					value="1" list="#{'0':getText('MerchantOnboardMakerAction.none'),'1':getText('MerchantOnboardMakerAction.only%'),
																					'2':getText('MerchantOnboardMakerAction.fixedAmountOnly'),'3':getText('MerchantOnboardMakerAction.both')}" />
																			</td>
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row2">
																			<td id="${terminalName}_RRFdiv1"><label><s:text name="MerchantOnboardMakerAction.domestic"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv2" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domestic"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domestic" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_RRFdiv7" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv8"><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domesticFixedAmount" maxlength="10" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row3">													
																			<td id="${terminalName}_RRFdiv3"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv4" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].interNational" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_RRFdiv9" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv10"><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].internationalFixedAmount" maxlength="10" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>			
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row4">	
																			<td id="${terminalName}_RRFdiv5"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label> </td>
																			<td id="${terminalName}_RRFdiv6" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domesticHoldingDays" maxlength="4" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
																			<td id="${terminalName}_RRFdiv11"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv12" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/> </label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].internationalHoldingDays" maxlength="4" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																		</tr>
																	</c:when>
																	<c:when test="${terminal.rrfIndc eq 2}">
																		<tr id="${terminalName}_RRFdetails_Row1">
																			<td>
																				<label><s:text name="MerchantOnboardMakerAction.rrfIndc"/></label>
																			</td>
																			<td nowrap="nowrap">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].rrfIndc" 
																					id="%{terminalName}_rrfIndc" onchange="rrfFieldsEnable(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
																					value="2" list="#{'0':getText('MerchantOnboardMakerAction.none'),'1':getText('MerchantOnboardMakerAction.only%'),
																					'2':getText('MerchantOnboardMakerAction.fixedAmountOnly'),'3':getText('MerchantOnboardMakerAction.both')}" />
																			</td>
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row2">
																			<td id="${terminalName}_RRFdiv1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domestic"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv2"><label><s:text name="MerchantOnboardMakerAction.domestic"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domestic" maxlength="3" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_RRFdiv7"><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv8" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domesticFixedAmount" maxlength="10" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row3">													
																			<td id="${terminalName}_RRFdiv3" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv4"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].interNational" maxlength="3" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_RRFdiv9"><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv10" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].internationalFixedAmount" maxlength="10" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>			
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row4">	
																			<td id="${terminalName}_RRFdiv5"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label> </td>
																			<td id="${terminalName}_RRFdiv6" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domesticHoldingDays" maxlength="4" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
																			<td id="${terminalName}_RRFdiv11"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv12" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/> </label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].internationalHoldingDays" maxlength="4" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																		</tr>
																	</c:when>
																	<c:when test="${terminal.rrfIndc eq 3}">
																		<tr id="${terminalName}_RRFdetails_Row1">
																			<td>
																				<label><s:text name="MerchantOnboardMakerAction.rrfIndc"/></label>
																			</td>
																			<td nowrap="nowrap">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].rrfIndc" 
																					id="%{terminalName}_rrfIndc" onchange="rrfFieldsEnable(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
																					value="3" list="#{'0':getText('MerchantOnboardMakerAction.none'),'1':getText('MerchantOnboardMakerAction.only%'),
																					'2':getText('MerchantOnboardMakerAction.fixedAmountOnly'),'3':getText('MerchantOnboardMakerAction.both')}" />
																			</td>
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row2">
																			<td id="${terminalName}_RRFdiv1"><label><s:text name="MerchantOnboardMakerAction.domestic"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv2" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domestic"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domestic" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_RRFdiv7"><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv8" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domesticFixedAmount" maxlength="10" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row3">													
																			<td id="${terminalName}_RRFdiv3"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv4" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].interNational" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_RRFdiv9"><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv10" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].internationalFixedAmount" maxlength="10" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>			
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row4">	
																			<td id="${terminalName}_RRFdiv5"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label> </td>
																			<td id="${terminalName}_RRFdiv6" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domesticHoldingDays" maxlength="4" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
																			<td id="${terminalName}_RRFdiv11"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv12" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/> </label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].internationalHoldingDays" maxlength="4" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																		</tr>
																	</c:when>
																	<c:otherwise>
																		<tr id="${terminalName}_RRFdetails_Row1">
																			<td>
																				<label><s:text name="MerchantOnboardMakerAction.rrfIndc"/></label>
																			</td>
																			<td nowrap="nowrap">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].rrfIndc" 
																					id="%{terminalName}_rrfIndc" onchange="rrfFieldsEnable(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
																					value="0" list="#{'0':getText('MerchantOnboardMakerAction.none'),'1':getText('MerchantOnboardMakerAction.only%'),
																					'2':getText('MerchantOnboardMakerAction.fixedAmountOnly'),'3':getText('MerchantOnboardMakerAction.both')}" />
																			</td>
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row2">
																			<td id="${terminalName}_RRFdiv1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domestic"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv2"><label><s:text name="MerchantOnboardMakerAction.domestic"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domestic" maxlength="3" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_RRFdiv7" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv8" ><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domesticFixedAmount" maxlength="10" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row3">													
																			<td id="${terminalName}_RRFdiv3" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv4"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].interNational" maxlength="3" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_RRFdiv9" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv10" ><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].internationalFixedAmount" maxlength="10" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>			
																		</tr>
																		<tr id="${terminalName}_RRFdetails_Row4">	
																			<td id="${terminalName}_RRFdiv5" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label> </td>
																			<td id="${terminalName}_RRFdiv6"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].domesticHoldingDays" maxlength="4" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
																			<td id="${terminalName}_RRFdiv11" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_RRFdiv12"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/> </label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].internationalHoldingDays" maxlength="4" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																		</tr>
																	</c:otherwise>
																</c:choose>			
															</table>
														</div>
														<div class="modal-footer text-center d-block">
															<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
														</div>
													</div>
												</div>
											</div>
											<!-- TERMINAL RRFModal - END -->
										</td>
									</tr>
									<%-- <tr id="${terminalName}_SDRdetails">
										<td><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryRequired"/></label></td>
										<td>
											<c:choose>
												<c:when test="${terminal.securityDepositRecoveryRequired eq 1}">
													<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].securityDepositRecoveryRequired" 
														onchange="fn_SDRdetailsForTerm(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
														list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
													<a id="${terminalName}_SDRModalLink" href="#" data-toggle="modal" data-target="#${terminalName}_SDRModal" >&nbsp;<span>View Details</span></a>
												</c:when>
												<c:otherwise>
													<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].securityDepositRecoveryRequired" 
														onchange="fn_SDRdetailsForTerm(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');"
														list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
													<a id="${terminalName}_SDRModalLink" href="#" data-toggle="modal" data-target="#${terminalName}_SDRModal" style="display:none;" >&nbsp;<span>View Details</span></a>
												</c:otherwise>	
											</c:choose>
											<!-- TERMINAL SDRModal - START -->
											<div class="modal modal-wide fade" id="${terminalName}_SDRModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
												<div class="modal-dialog modal-xl">
													<div class="modal-content">
														<div class="modal-header">
															<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryRequired" /></h4>
														<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
														
														</div>
														<div class="modal-body">
															<table border="0" width="100%" cellspacing="4">
																<c:choose>
																	<c:when test="${terminal.sdrIndc eq 1}">
																		<tr id="${terminalName}_SDRdetails_Row1">
																			<td>
																				<label><s:text name="MerchantOnboardMakerAction.sdrIndc"/></label>
																			</td>
																			<td nowrap="nowrap">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].sdrIndc" 
																					id="%{terminalName}_sdrIndc" onchange="sdrFieldsEnable(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');" 
																					value="1" list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
																			</td>
																		</tr>
																		<tr id="${terminalName}_SDRdetails_Row2">
																			<td id="${terminalName}_SDRdiv1"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_SDRdiv2" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_SDRdiv3"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_SDRdiv4" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].securityDepositRecoveryDays" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
																		</tr>
																	</c:when>
																	<c:otherwise>
																		<tr id="${terminalName}_SDRdetails_Row1">
																			<td>
																				<label><s:text name="MerchantOnboardMakerAction.sdrIndc"/></label>
																			</td>
																			<td nowrap="nowrap">
																				<s:radio name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].sdrIndc" 
																					id="%{terminalName}_sdrIndc" onchange="sdrFieldsEnable(this, '%{terminalName}', '%{storeIndex}', '%{terminalIndex}');" 
																					value="0" list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
																			</td>
																		</tr>
																		<tr id="${terminalName}_SDRdetails_Row2">
																			<td id="${terminalName}_SDRdiv1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_SDRdiv2"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																			<td id="${terminalName}_SDRdiv3" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td id="${terminalName}_SDRdiv4" ><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																			<td><s:textfield name="merchantOnboardBean.lstStoreBean[%{storeIndex}].lstTerminalBean[%{terminalIndex}].securityDepositRecoveryDays" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
																		</tr>
																	</c:otherwise>
																</c:choose>
															</table>
														</div>
														<div class="modal-footer text-center d-block">
															<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
														</div>
													</div>
												</div>
											</div>
											<!-- TERMINAL SDRModal - END -->
										</td>
									</tr> --%>
							 	</table>
						 		</div></div>
							</td>
						</tr>
					</c:if>
					</s:iterator>
					</table>
					<table style="width: 100%;">
						<tr>
							<td align="right" colspan="6"><input type="button" name="addTerminalButton" value='Additional Terminal' class="btn btn-primary" onclick="fn_addTerminalDetails('${storeName}', '${storeIndex}'); fn_merchantEntityValue();" />
								<div style="display:none;"><s:select id="%{storeName}_lstProuctType" cssClass="select_styled" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" list="lstProuctType" listKey="key" listValue="value" /></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<input type="hidden" id="${storeName}_termCount" name="merchantOnboardBean.lstStoreBean[${storeIndex}].termCount" value="${terminalCount}">
	</div>
	</div>
	</c:if>
	</s:iterator>
	<c:if test="${storeCount eq 0}">
		<div class="group" id="STORE1_details" >
		<h2 onclick="fn_customizeAccordion(this, 'EXISTSTORE');"><s:text name="MerchantOnboardMakerAction.storeDetails" /></h2>
		<div class="content">
		<table border="0" width="100%" cellspacing="4" id="STORE1_tableDetails">
		   	<tr>
			    <td><label><s:text name="MerchantOnboardMakerAction.sid"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td>
					<s:hidden name="merchantOnboardBean.lstStoreBean[0].storeSqid" cssClass="formtextbox" maxlength="10" />
			    	<s:if test="autoGenerateSid == 1">
						<input type="text" name="merchantOnboardBean.lstStoreBean[0].storeCode" value="AUTO GENERATE"
							style="color: black;background: lightgray;" class="formtextbox" maxlength="15" readonly="readonly">
					</s:if>
			    	<s:else>
						<s:textfield name="merchantOnboardBean.lstStoreBean[0].storeCode" cssClass="formtextbox" maxlength="15"></s:textfield>
					</s:else>
			    </td>
				<td><label><s:text name="MerchantOnboardMakerAction.storeTradeName"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[0].storeTradingName"
					id="STORE1_storeTradingName" maxlength="40" value="%{merchantOnboardBean.dbaname}" cssClass="formtextbox"></s:textfield>
				</td>
		    </tr>
		    <tr>
				<td><label><s:text name="MerchantOnboardMakerAction.storeemail"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield key="email" name="merchantOnboardBean.lstStoreBean[0].storeEmail"
						maxlength="45" cssClass="formtextbox" /></td>
				<td><label><s:text name="MerchantOnboardMakerAction.merchantGrade" />
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[0].merchantGrade" headerKey=""
					headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" list="merGradeList" listKey="key"
					listValue="value" cssClass="select_styled">
					</s:select>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.natureOfBusiness"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[0].natureOfBusiness" headerKey="" 
						headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" list="natureOfBusinessCatgList" listKey="key"
						id="STORE1_natureOfBusiness" listValue="value" cssClass="select_styled" >
					</s:select>
				</td>	
				<td><label><s:text name="MerchantOnboardMakerAction.prfDocsProv"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>
					<select id="STORE1_prfDocsProv" multiple="multiple" disabled="disabled"></select>
					<input type="hidden" name="merchantOnboardBean.lstStoreBean[0].prfDocsProv" id="STORE1_prfDocsProvValue" >
				</td>
			</tr>
			<tr>
				<%-- <td><label><s:text name="MerchantOnboardMakerAction.mcc"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>	
				<td>
					<input type="hidden" id="STORE1_cmccAutoComplete" name="merchantOnboardBean.lstStoreBean[0].cmccAutoComplete"
						value=""/>
					<input type="text" id="STORE1_mccautocomplete" name="merchantOnboardBean.lstStoreBean[0].mccautocomplete"
						value="" size="20" maxlength="4" autocomplete="off" class="position-relative"
						onkeypress="fn_storeMccAutocomplete('STORE1')"/>
				</td> --%>
				<td><label><s:text name="MerchantOnboardMakerAction.mvv"></s:text></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[0].mvv" maxlength="10" cssClass="formtextbox"></s:textfield></td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.relatonShipManager"></s:text></label></td>
				<td><s:select name="merchantOnboardBean.lstStoreBean[0].relatonShipManager" cssClass="select_styled" headerKey=""
					 headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" value="%{merchantOnboardBean.mercAggrrelm}"
					 list="relShipManagerList" listKey="key" listValue="value"></s:select>
				</td>
				<td width="22%"><label><s:text name="MerchantOnboardMakerAction.incpStatus"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>							
					<s:select name="merchantOnboardBean.lstStoreBean[0].incorpstatus" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					headerKey="" list="incorpStatusList" listKey="key" listValue="value" cssClass="select_styled" /></td>	
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.gstflag" /></label></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[0].gstFlag" 
						value="0" list="#{'0':getText('MerchantOnboardMakerAction.include'),'1':getText('MerchantOnboardMakerAction.exclude')}" />
				</td>
				<td>
					<label><s:text name="MerchantOnboardMakerAction.gstno"></s:text>
					<%-- <span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span> --%>
					</label></td>
				<td>
					<s:textfield name="merchantOnboardBean.lstStoreBean[0].gstNumber" cssclass="formtextbox"
					onkeypress="AllowNumeric();" maxlength="15" oninput="this.value = this.value.replace(/[^0-9]/g, '')" onPaste="return false" AUTOCOMPLETE="OFF" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.govtRegMerchant"/></label></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[0].govtRegMerchant" onchange="fn_govtRegMerchant(this, 'STORE1');"
						value="0" list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
				</td>
				<td id="STORE1_govRegLabel" <c:if test="${store.govtRegMerchant ne '1'}"> style="display:none;" </c:if> >
					<label><s:text name="MerchantOnboardMakerAction.homeCountryId" />
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>		
				<td id="STORE1_govRegValue" <c:if test="${store.govtRegMerchant ne '1'}"> style="display:none;" </c:if> >
					<s:select name="merchantOnboardBean.lstStoreBean[0].homeCountryId" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					listKey="key" listValue="value" headerKey="" list="homeCountryIdList"
					cssClass="select_styled" ></s:select>
				</td>
			</tr>
			<tr>
				<td id="STORE1_cyberMercID1" style="display: none"><label><s:text name="MerchantOnboardMakerAction.cyberMercID"/></label></td>
				<td id="STORE1_cyberMercID2" style="display: none"><s:textfield name="merchantOnboardBean.lstStoreBean[0].cyberMercID" maxlength="30" class="formtextbox"></s:textfield>
				</td>
				<td id="STORE1_cyberMercPass1" style="display: none"><label><s:text name="MerchantOnboardMakerAction.cyberMercPass"/></label></td>
				<td id="STORE1_cyberMercPass2" style="display: none"><s:textfield name="merchantOnboardBean.lstStoreBean[0].cyberMercPass" maxlength="70" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td id="STORE1_cyberMercKey1" style="display: none"><label><s:text name="MerchantOnboardMakerAction.cyberMercKey"/></label></td>
				<td id="STORE1_cyberMercKey2" style="display: none"><s:textfield name="merchantOnboardBean.lstStoreBean[0].cyberMercKey" maxlength="50" cssClass="formtextbox"></s:textfield>
				</td>
			</tr>
			<tr>
				<td id="STORE1_aaniPayStoreID1" style="display: none"><label><s:text name="MerchantOnboardMakerAction.aaniPayStoreID"/></label></td>
				<td id="STORE1_aaniPayStoreID2" style="display: none"><s:textfield name="merchantOnboardBean.lstStoreBean[0].aaniPayStoreID" maxlength="16" onkeypress="AllowNumeric();" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.webSiteName"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[0].webSiteName" maxlength="40" cssClass="formtextbox"></s:textfield>
				</td>
				<td><label><s:text name="MerchantOnboardMakerAction.websiteAddress"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[0].websiteAddress" maxlength="40" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.paymentFrecForCard"/></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[0].paymentFrecForCard" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						listKey="key" listValue="value" headerKey="" id="STORE1_paymentFrecForCard" 
						list="#{'1':getText('MerchantOnboardMakerAction.defaultSelect'),
								'1':getText('MerchantOnboardMakerAction.daily'),
								'5':getText('MerchantOnboardMakerAction.tplusn'),
								'4':getText('MerchantOnboardMakerAction.monthly')}"
						cssClass="select_styled" ></s:select>
				</td>
				<td id="STORE1_paymentFrecForCard1" style="display: none;">
					<label><s:text name="MerchantOnboardMakerAction.paymentFrecForCardDays"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td id="STORE1_paymentFrecForCard2" style="display: none;">
					<s:textfield name="merchantOnboardBean.lstStoreBean[0].paymentFrecForCardDays" maxlength="4" cssClass="formtextbox" onkeypress="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.aaniPayTransPayment" /></label></td>
				<td><s:radio name="merchantOnboardBean.lstStoreBean[0].aaniPayTransPayment" 
						value="0" onchange="fn_aaniPayTransPayment_Update(this, 'STORE1', '0');"
						list="#{'0':getText('MerchantOnboardMakerAction.deffered'),'1':getText('MerchantOnboardMakerAction.realtime')}" />
				</td>
			</tr>
			<tr id="STORE1_aaniPayTransPayment">
				<td><label><s:text name="MerchantOnboardMakerAction.paymentFrecForAaniPay"/></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[0].paymentFrecForAaniPay" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						listKey="key" listValue="value" headerKey="" id="STORE1_paymentFrecForAaniPay" 
						list="#{'1':getText('MerchantOnboardMakerAction.defaultSelect'),
								'1':getText('MerchantOnboardMakerAction.daily'),
								'5':getText('MerchantOnboardMakerAction.tplusn'),
								'4':getText('MerchantOnboardMakerAction.monthly')}"
						cssClass="select_styled" ></s:select>
				</td>
				<td id="STORE1_paymentFrecForAaniPay1" style="display: none;">
					<label><s:text name="MerchantOnboardMakerAction.paymentFrecForAaniPayDays"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td id="STORE1_paymentFrecForAaniPay2" style="display: none;">
					<s:textfield name="merchantOnboardBean.lstStoreBean[0].paymentFrecForAaniPayDays" maxlength="4" cssClass="formtextbox" onkeypress="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield>
				</td>
			</tr>
			<tr id="STORE1_pricingDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.storePriceDetailsRequired" /></label></td>
				<td colspan="4" class="d-flex">
					<s:radio name="merchantOnboardBean.lstStoreBean[0].storePriceDetailsRequired" 
						value="0" onchange="fn_storePriceDetailsRequired(this, 'STORE1', '0');"
						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
					<a id="STORE1_PriceModalLink" href="#" style="display:none; margin-bottom: 10px !important; margin-top: 0px !important; align-content: center;  margin-left: 10px;" data-toggle="modal" data-target="#STORE1_PriceModal" style="display:none;" >&nbsp;<span>View Details</span></a>
					<!-- STORE PriceModal - START -->
					<div class="modal modal-wide fade" id="STORE1_PriceModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl modal-dialog-centered">
							<div class="modal-content">
								<div class="modal-header">
									<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.storePriceDetailsRequired" /></h4>
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
									
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="STORE1_pricingInfo_Row1">
											<td><label><s:text name="MerchantOnboardMakerAction.captureMerchantServiceFeeAt" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
											</td>
											<td>
												<s:radio name="merchantOnboardBean.lstStoreBean[0].storeCaptureServiceFeeAt" value="2"
													list="#{'2':getText('MerchantOnboardMakerAction.storeLevel'),'3':getText('MerchantOnboardMakerAction.terminalLevel')}" />
						
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeePlanType" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:radio name="merchantOnboardBean.lstStoreBean[0].storeFeePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
													value="1" list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" onchange="getStoreServiceFeePlan('STORE1', '0');" />
											</td>
										</tr>
										<tr id="STORE1_pricingInfo_Row2">
											<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeeplan" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[0].storeMerchantServiceFeePlan" headerKey=""
													headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
													id="STORE1_storeservicefeeplan" list="feePlanList"  listKey="value" 
													listValue="value" cssClass="select_styled">
												</s:select>
											</td>
											<%-- <td><label><s:text name="MerchantOnboardMakerAction.mercFeeDeduWaiver" /></label></td>
											<td><s:radio name="merchantOnboardBean.lstStoreBean[0].storeMercFeeDeduWaiver" value="0"
												list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
											</td> --%>
										</tr>
									</table>
								</div>
								<div class="modal-footer text-center d-block">
									<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
								</div>
							</div>
						</div>
					</div>
					<!-- STORE PriceModal - END -->
				</td>
			</tr>		
			<tr id="STORE1_addressDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.useSameAsMercAddressForStore"/></label></td> 
				<td colspan="4" class="d-flex">
					<s:radio name="merchantOnboardBean.lstStoreBean[0].useSameAsMercAddressForStore" 
						value="0" onchange="fn_useSameAsMercAddressForStore(this, 'STORE1', '0');"
						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
					<a id="STORE1_AddressModalLink" href="#" data-toggle="modal" data-target="#STORE1_AddressModal" style="display:none; align-content: center;margin-bottom: 10px !important; margin-top: 0px !important;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
					<!-- STORE AddressModal - START -->
					<div class="modal modal-wide fade" id="STORE1_AddressModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl modal-dialog-centered">
							<div class="modal-content">
								<div class="modal-header">
									<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.useSameAsMercAddressForStore" /></h4>
																<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="STORE1_addressInfo_Row1">
											<td><label><s:text name="MerchantOnboardMakerAction.addressline1"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storeaddressline1" maxlength="50" cssClass="formtextbox" />
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.addressline2"/></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storeaddressline2" maxlength="50" cssClass="formtextbox" />
											</td>
										</tr>
										<tr id="STORE1_addressInfo_Row2">
											<td><label><s:text name="MerchantOnboardMakerAction.country"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<input type="hidden" id="STORE1_ccountry" name="merchantOnboardBean.lstStoreBean[0].storeccountry" value="${merchantOnboardBean.ccountry}" readonly="readonly"/>
												<input type="text" id="STORE1_country" name="merchantOnboardBean.lstStoreBean[0].storecountry" value="${merchantOnboardBean.country}" readonly="readonly" class="position-relative"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.state"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<input type="hidden" id="STORE1_cstate" name="merchantOnboardBean.lstStoreBean[0].storecstate" value="" />
												<input type="text" id="STORE1_state" name="merchantOnboardBean.lstStoreBean[0].storestate" value="" size="20" maxlength="40" autocomplete="off" 
												onkeypress="AllowAlphabeticWithSpace();fn_storeStateDetails('STORE1');" class="position-relative"/>
											</td>
										</tr>
										<tr id="STORE1_addressInfo_Row3">
											<td><label><s:text name="MerchantOnboardMakerAction.citysuburb"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<input type="hidden" id="STORE1_cselectcity" name="merchantOnboardBean.lstStoreBean[0].storecselectcity" value="" />
												<input type="text" id="STORE1_selectcity" name="merchantOnboardBean.lstStoreBean[0].storeselectcity" value="" size="20" maxlength="40" autocomplete="off" 
												onkeypress="AllowAlphabeticWithSpace();fn_storeCityDetails('STORE1');" class="position-relative"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.postalcode"/></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storepostalcode" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
											</td>
										</tr>
										<tr id="STORE1_addressInfo_Row4">
											<td><label><s:text name="MerchantOnboardMakerAction.businessContactEmailID"/></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storebusinessContactEmailID" maxlength="50" cssClass="formtextbox" />
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.businessContactMobileNo"/></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storebusinessContactMobileNo" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>			
										</tr>
										<tr id="STORE1_addressInfo_Row5">
											<td><label><s:text name="MerchantOnboardMakerAction.technicalContactEmailID"/></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storetechnicalContactEmailID" maxlength="50" cssClass="formtextbox" />
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.technicalContactMobileNo"/></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storetechnicalContactMobileNo" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>			
										</tr>
									</table>
								</div>
								<div class="modal-footer text-center d-block">
									<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
								</div>
							</div>
						</div>
					</div>
					<!-- STORE AddressModal - END -->
				</td>								
			</tr>
			<tr id="STORE1_riskDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.storeRiskDetailsRequired" /></label></td>
				<td colspan="4" class="d-flex">
					<s:radio name="merchantOnboardBean.lstStoreBean[0].storeRiskDetailsRequired" 
						value="0" onchange="fn_storeRiskDetailsRequired(this, 'STORE1', '0');"
						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
					<a id="STORE1_RiskModalLink" href="#" data-toggle="modal" data-target="#STORE1_RiskModal" style="display:none; align-content: center; margin-bottom: 10px !important; margin-top: 0px !important;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
					<!-- STORE RiskModal - START -->
					<div class="modal modal-wide fade" id="STORE1_RiskModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl modal-dialog-centered">
							<div class="modal-content">
								<div class="modal-header">
																	<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.storeRiskDetailsRequired" /></h4>
								
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="STORE1_riskInfo_Row1">
											<td><label><s:text name="MerchantOnboardMakerAction.riskCategory" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[0].riskCategory" headerKey="" 
													headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
													list="riskCategoryList" listKey="key" listValue="value" cssClass="select_styled">
												</s:select>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.padssPCIdssCertExpDate" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].padssPCIdssCertExpDate" style="width: 200px !important;" 
													cssClass="date-picker" readonly="true" id="STORE1_padssPCIdssCertExpDate" required="true"></s:textfield>
												<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="STORE1_eff" 
													onclick="javascript:NewCssCal('STORE1_padssPCIdssCertExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
													class="calendar-icon">	
											</td>
										</tr>
										<tr id="STORE1_riskInfo_Row2">
											<td><label><s:text name="MerchantOnboardMakerAction.cloudCertExpDate" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].cloudCertExpDate" style="width: 200px !important;" 
													cssClass="date-picker" readonly="true" id="STORE1_cloudCertExpDate" required="true"></s:textfield>
												<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="STORE1_cloudCertExpDate1"  
													onclick="javascript:NewCssCal('STORE1_cloudCertExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
													class="calendar-icon">
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.tradeLicenseExpDate" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].tradeLicenseExpDate" style="width: 200px !important;" 
													cssClass="date-picker" readonly="true" id="STORE1_tradeLicenseExpDate" required="true"></s:textfield>
												<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="STORE1_tradeLicenseExpDate1"  
													onclick="javascript:NewCssCal('STORE1_tradeLicenseExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
													class="calendar-icon">	
											</td>
										</tr>
										<tr id="STORE1_riskInfo_Row3">
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField1" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].additionalUDFField1" maxlength="10" cssClass="formtextbox"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue1" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].additionalUDFValue1" maxlength="10" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="STORE1_riskInfo_Row4">
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField2" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].additionalUDFField2" maxlength="10" cssClass="formtextbox"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue2" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].additionalUDFValue2" maxlength="10" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="STORE1_riskInfo_Row5">
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField3" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].additionalUDFField3" maxlength="10" cssClass="formtextbox"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue3" /></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].additionalUDFValue3" maxlength="10" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="STORE1_riskInfo_Row6">
											<td><label><s:text name="MerchantOnboardMakerAction.transCountperMonth" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].transCountperMonth" 
													id="STORE1_transCountperMonth" maxlength="7" cssClass="formtextbox" 
													oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.averageTicketSize" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].averageTicketSize" 
													id="STORE1_averageTicketSize" maxlength="25" cssClass="formtextbox" 
													onchange="calculateQuaterlySaleAmt('STORE1', '0');" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
										</tr>
										<tr id="STORE1_riskInfo_Row7">
											<td><label><s:text name="MerchantOnboardMakerAction.quarterlySale" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].quarterlySale" 
													id="STORE1_quarterlySale" maxlength="25" readonly="true" 
													cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.transToRefundPercent" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].transToRefundPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
										</tr>
										<tr id="STORE1_riskInfo_Row8">
											<td><label><s:text name="MerchantOnboardMakerAction.transToChargebackPercent" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].transToChargebackPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.transPercent" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].transPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
										</tr>
										<tr id="STORE1_riskInfo_Row9">
											<td><label><s:text name="MerchantOnboardMakerAction.internationalTransPercent" />
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].internationalTransPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
											</td>
										</tr>
									</table>
								</div>
								<div class="modal-footer text-center d-block">
									<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
								</div>
							</div>
						</div>
					</div>
					<!-- STORE RiskModal - END -->
				</td>
			</tr>			
			<tr id="STORE1_accountDetails" >
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.useSameAsMercAccountForStore"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td> 
				<td colspan="4" class="d-flex">
					<s:radio name="merchantOnboardBean.lstStoreBean[0].useSameAsMercAccountForStore" 
						value="0" onchange="fn_useSameAsMercAccountForStore(this, 'STORE1', '0');"
						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
					<a id="STORE1_AccountModalLink" href="#" data-toggle="modal" data-target="#STORE1_AccountModal" style="display:none; align-content: center; margin-bottom: 10px !important; margin-top: 0px !important; margin-left: 10px;" >&nbsp;<span>View Details</span></a>
					<!-- STORE AccountModal - START -->
					<div class="modal modal-wide fade" id="STORE1_AccountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl modal-dialog-centered">
							<div class="modal-content">
								<div class="modal-header">
																	<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.useSameAsMercAccountForStore" /></h4>
								
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="STORE1_accountDetails_Row1">
											<td class="formtext"><label><s:text name="MerchantOnboardMakerAction.accountWithBank"/>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
											</td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[0].storeacctWithBank" headerKey="" 
													headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
													list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}"
													listKey="key" id="STORE1_storeacctWithBank" listValue="value" cssClass="select_styled" >
												</s:select>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.defaultAccount" /></label></td>
											<td><s:radio name="merchantOnboardBean.lstStoreBean[0].storedefAccount" value="0"
												list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
											</td>
										</tr>
										<tr id="STORE1_accountDetails_Row2">
											<td id="STORE1_acctBranch" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.acctBranch"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
											</td>
											<td id="STORE1_acctBranchDetails" style="display:none;">
												<input type="hidden" id="STORE1_cStoreacctBranch" name="merchantOnboardBean.lstStoreBean[0].cStoreacctBranch" />
												<input type="text" id="STORE1_storeacctBranch" name="merchantOnboardBean.lstStoreBean[0].storeacctBranch" 
													size="20" maxlength="50" autocomplete="off" onkeypress="AllowAlphaNumericWithSpace();fn_storeacctBranch('STORE1');" 
													class="position-relative" />
											</td>
											<td id="STORE1_bankName" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.bankName"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
											</td>
											<td id="STORE1_bankNameText" style="display:none;" class="formtext">
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storeacctBankText" maxlength="50" cssClass="formtextbox"/>
											</td>
											<td id="STORE1_branchName" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.branchName"></s:text></label>
											</td>
											<td id="STORE1_branchNameText" style="display:none;" class="formtext">
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storeacctBranchText" maxlength="50" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="STORE1_accountDetails_Row3">
											<td><label><s:text name="MerchantOnboardMakerAction.IFSC"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storeifsc" maxlength="11" cssClass="formtextbox" /></td>
											<td><label><s:text name="MerchantOnboardMakerAction.accountname"></s:text></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storeaccountname" label="MerchantOnboardMakerAction.accountname" maxlength="40" cssClass="formtextbox" />
											</td>
										</tr>
										<tr id="STORE1_accountDetails_Row4">
											<td><label><s:text name="MerchantOnboardMakerAction.currency"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[0].storecurrency" cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
													list="crcyList" listKey="key" listValue="value"></s:select>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.accountNumber"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storeacctNumber" onkeypress="AllowAlpaNumeric();" maxlength="36" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr>
											<td><label><s:text name="MerchantOnboardMakerAction.cifNumber"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[0].storecifNumber" onkeypress="AllowNumeric();" maxlength="7" cssClass="formtextbox"/>
											</td>
											<%-- <td><label><s:text name="MerchantOnboardMakerAction.stmtfrequency"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
											</td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[0].storestmtfreq"
													cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
													list="stmttFreqList" listKey="key" listValue="value"></s:select>
											</td> --%>
										</tr>
									</table>
								</div>
								<div class="modal-footer text-center d-block">
									<input type="button" class="btn btn-primary" value="Submit" data-dismiss="modal" aria-hidden="true">
								</div>
							</div>
						</div>
					</div>
					<!-- STORE AccountModal - END -->
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.storeType"/></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[0].storeType" value="1"
						list="#{'1':getText('MerchantOnboardMakerAction.Online'),'2':getText('MerchantOnboardMakerAction.Offline')}" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.isTerminal"/></label></td>
				<td><div id="checkbox">
					<s:checkbox name="merchantOnboardBean.lstStoreBean[0].termneed" id="STORE1_termneeded" fieldValue="1" labelposition="before" theme="simple" onchange="fn_StoreTermNeed(this, 'STORE1', '0'); fn_merchantEntityValue();"/>
					<input type="hidden" id="STORE1_termCount" name="merchantOnboardBean.lstStoreBean[0].termCount" value="0">
				</div></td>
				<td colspan="2" align="center" id="STORE1_docLinkRow">
					<a href="#" class="global-links" onclick="javascript:openWindowAttachmentsForStore('STORE1', '0', '${strTempFoldername}', '${merchantOnboardBean.refSqid}');">Add/View Attached Documents For Store 1</a>
					<s:hidden name="merchantOnboardBean.lstStoreBean[0].tempFolderName" id="STORE1_tempFolderName"></s:hidden>
				</td>
			</tr>
			
			<tr id="STORE1_terminalDetails" style="display:none;">
				<td colspan="6">
					<table style="width: 100%;" id="STORE1_terminalTable"></table>
					<table style="width: 100%;">
						<tr>
							<td align="right" colspan="6"><input type="button" name="addTerminalButton" value='Additional Terminal' class="btn btn-primary" onclick="fn_addTerminalDetails('STORE1', '0'); fn_merchantEntityValue();" />
								<div style="display:none;"><s:select id="STORE1_lstProuctType" cssClass="select_styled" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" list="lstProuctType" listKey="key" listValue="value" /></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</div>
		</div>
		<s:set var="storeCount" value="1" />
	</c:if>	
	<!-- End of the Store Details -->
		
	</div>
	</div>
    </td>
	</tr>
	</table>
	</div>
	<div style="display:none">
		<input type="hidden" id="storeCount" name="merchantOnboardBean.storeCount" value="${storeCount}">
		<%-- <s:select id="merchantEntityList" list="merchantEntityList" listKey="key" listValue="value" 
       		headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
       		cssClass="select_styled"></s:select> --%>
	</div>
		<table cellSpacing=0 cellPadding=0 width="100%" align="left">
			<tr>
				<td align="center" class="row">
					<div class="col-lg-1"></div>
					<div class="col-lg-3" align="right"><label><s:text name="MerchantOnboardMakerAction.remarks" /><span class="must"> <s:text name="CommonAction.mandatoryIndicator"></s:text> </span></label></div>
					<div class="col-lg-4"><s:textarea name="merchantOnboardBean.remarks" id="remarks" cssClass="textareaWidthHeight" rows="4" cols="60" wrap="true" required="required"></s:textarea></div>
					<div class="col-lg-3"></div>
				</td>
			</tr>
		</table>
		<table border="0" align="center" cellspacing="3">
<!-- 			<tr><td align="center"> -->
<!-- 					<a href="#" class="global-links" onclick="javascript:openWindowAttachmentsForMerchant();">Add/View Attached Documents</a> -->
<!-- 				</td> -->
<!-- 			</tr>					 -->
			
			<tr><td colspan="2" align="center">
					<input type="button" value='Additional Store' class="btn btn-primary" onclick="fn_addStore(); fn_merchantEntityValue();">
					<s:submit cssClass="btn btn-primary" id="submitButton" disabled="false" onclick="return fn_documentValidation(); disableButtons();"/> 
					<input type="button" id="bButton" name="bButton" value='<s:text name="CommonAction.back"/>' class="btn btn-primary" onclick="fn_back();">
				</td>
			</tr>
		</table>
	</td>
	</tr>
	</table>
	</div>
		<s:token></s:token>
		<input type="hidden" id="_csrf" name="_csrf" value="${csrfToken}" />
	</s:form>
	</div>
	</section>
</body>
</html>