<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="d" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">

function fn_back(){
	document.getElementById("legalname").value= null;
	if(document.getElementById("referralNo")){
		var refNo = document.getElementById("referralNo").value;
		document.getElementById("referralNo").value= null;
		$("#frmAddMerchantOnboardDetails").attr("action","<%=request.getContextPath()%>/merchantsales/merchantOnboardSearchResultPage.action?refNo="+refNo);
	} else {
		$("#frmAddMerchantOnboardDetails").attr("action","<%=request.getContextPath()%>/merchantsales/merchantOnboardMaker.action");
	}
	$("#frmAddMerchantOnboardDetails").submit();
}

function fn_customizeAccordion(event, tabName){
	var allPanels = $('.accordion').find('.content').hide();
	if(tabName=="STORE"){
		$(event).next().slideDown('slow');
	}
}

function fn_updateDBANameIntoStore(val){
	var storeCount = document.getElementById('storeCount').value;
	for(var i=1; i<=storeCount; i++){
		if (document.getElementById("STORE"+i+"_storeTradingName")) {
			document.getElementById("STORE"+i+"_storeTradingName").value = val.value;
		}
	}
}

function fn_addStore() {
	var noOfStoresReq = parseInt(document.getElementById('noOfStoresReq').value);
	var storeCount = parseInt(document.getElementById('storeCount').value);
	var tempCount = 0;
	for(var i=1; i<=storeCount; i++){
		var elementverify = document.getElementById("STORE"+i+"_details");
	    if (elementverify) {
			tempCount = tempCount+1;
		}
	}
	if(tempCount>=noOfStoresReq){
		alert("If you want to add one more Additional Stores, kindly change the No.Of Stores Required in the 'Merchant Details Tab'.");
		document.getElementById('noOfStoresReq').focus();
		return false;
	}
	
	//var merchantEntityList = document.getElementById('merchantEntityList').innerHTML;
	var tempStoreCount = storeCount;
	storeCount = storeCount+1;
	var accordion_Details = document.getElementById('accordion_Details');
	var storename = 'STORE'+storeCount;
	var acctMapLvl = $("input:radio[name='merchantOnboardBean.acctMapLvl']:checked").val();
	var dbaname = document.getElementById('dbaname').value;
	var mercAggrrelm = document.getElementById("mercAggrrelm").value;
	var acctdetails= `<td colspan="4" class="d-flex">
			<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].useSameAsMercAccountForStore" onchange="fn_useSameAsMercAccountForStore(this, '`+storename+`', '`+tempStoreCount+`');"
			value="0" list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
				<a id="`+storename+`_AccountModalLink" href="#" data-toggle="modal" data-target="#`+storename+`_AccountModal" style="display:none; align-content: center; margin-bottom: 10px !important; margin-top: 0px !important;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
				<!-- STORE AccountModal - START -->
				<div class="modal modal-wide fade" id="`+storename+`_AccountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog modal-xl">
						<div class="modal-content">
							<div class="modal-header">
							<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.useSameAsMercAccountForStore" /></h4>
								<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
							</div>
							<div class="modal-body">
								<table border="0" width="100%" cellspacing="4">
									<tr id="`+storename+`_accountDetails_Row1">
										<td class="formtext"><label><s:text name="MerchantOnboardMakerAction.accountWithBank"/>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
										</td>
										<td>
											<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeacctWithBank" headerKey="" 
												headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
												list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}"
												listKey="key" id="`+storename+`_storeacctWithBank" listValue="value" cssClass="select_styled" >
											</s:select>
										</td>
										<td><label><s:text name="MerchantOnboardMakerAction.defaultAccount" /></label></td>
										<td><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storedefAccount" value="0"
											list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
										</td>
									</tr>
									<tr id="`+storename+`_accountDetails_Row2">
										<td id="`+storename+`_acctBranch" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.acctBranch"></s:text></label>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
										</td>
										<td id="`+storename+`_acctBranchDetails" style="display:none;">
											<input type="hidden" id="`+storename+`_cStoreacctBranch" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].cStoreacctBranch" />
											<input type="text" id="`+storename+`_storeacctBranch" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeacctBranch" 
												size="20" maxlength="50" autocomplete="off" onkeypress="AllowAlphaNumericWithSpace();fn_storeacctBranch('`+storename+`');" 
												class="position-relative" />
										</td>
										<td id="`+storename+`_bankName" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.bankName"></s:text></label>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
										</td>
										<td id="`+storename+`_bankNameText" style="display:none;" class="formtext">
											<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeacctBankText" maxlength="50" cssClass="formtextbox"/>
										</td>
										<td id="`+storename+`_branchName" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.branchName"></s:text></label>
										</td>
										<td id="`+storename+`_branchNameText" style="display:none;" class="formtext">
											<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeacctBranchText" maxlength="50" cssClass="formtextbox"/>
										</td>
									</tr>
									<tr id="`+storename+`_accountDetails_Row3">
										<td><label><s:text name="MerchantOnboardMakerAction.IFSC"></s:text></label>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
										<td>
											<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeifsc" maxlength="11" cssClass="formtextbox" /></td>
										<td><label><s:text name="MerchantOnboardMakerAction.accountname"></s:text></label></td>
										<td>
											<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeaccountname" label="MerchantOnboardMakerAction.accountname" maxlength="40" cssClass="formtextbox" />
										</td>
									</tr>
									<tr id="`+storename+`_accountDetails_Row4">
										<td><label><s:text name="MerchantOnboardMakerAction.currency"></s:text></label>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
										<td>
											<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storecurrency" cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
												list="crcyList" listKey="key" listValue="value"></s:select>
										</td>
										<td><label><s:text name="MerchantOnboardMakerAction.accountNumber"></s:text></label>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
										<td>
											<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeacctNumber" onkeypress="AllowAlpaNumeric();" maxlength="36" cssClass="formtextbox"/>
										</td>
									</tr>
									<tr id="`+storename+`_accountDetails_Row5">
										<td><label><s:text name="MerchantOnboardMakerAction.cifNumber"></s:text></label>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
										<td>
											<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storecifNumber" onkeypress="AllowNumeric();" maxlength="7" cssClass="formtextbox"/>
										</td>
										<%-- <td><label><s:text name="MerchantOnboardMakerAction.stmtfrequency"></s:text></label>
											<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
										</td>
										<td>
											<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storestmtfreq" 
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
			</td>`;
	
	var newRow = document.createElement('div');
	newRow.id = storename+"_details";
	newRow.classList.add('group');
	
	newRow.innerHTML = `
		<h2 onclick="fn_customizeAccordion(this, 'STORE');"><s:text name="MerchantOnboardMakerAction.storeDetails" /> - `+storeCount+`
			<img src="${pageContext.request.contextPath}/images/trash_icon.png" alt="trashbutton" type="button" class="trash-icon" 
			style="float: right; height: 25px; margin-top: 3px;"
			onclick="document.getElementById('`+storename+`_details').remove();  fn_merchantEntityValue();" />	 
		</h2>
		<div class="content">
		<table border="0" width="100%" cellspacing="4" id="`+storename+`_tableDetails" >
			<tr>					
				<td><label><s:text name="MerchantOnboardMakerAction.sid"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td>
					<s:hidden name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeSqid" cssClass="formtextbox" maxlength="10" />
			    	<s:if test="autoGenerateSid == 1">
						<input type="text" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeCode" value="AUTO GENERATE"
							style="color: black;background: lightgray;" class="formtextbox" maxlength="15" readonly="readonly">
					</s:if>
			    	<s:else>
						<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeCode" cssClass="formtextbox" maxlength="15"></s:textfield>
					</s:else>
			    </td>
				<td><label><s:text name="MerchantOnboardMakerAction.storeTradeName"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeTradingName"
					id="`+storename+`_storeTradingName" value="`+dbaname+`" maxlength="40" readonly="true" cssClass="formtextbox"></s:textfield>
				</td>
		    </tr>
		    <tr>
				<td><label><s:text name="MerchantOnboardMakerAction.storeemail"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield key="email" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeEmail"
						maxlength="45" cssClass="formtextbox" /></td>
				<td><label><s:text name="MerchantOnboardMakerAction.merchantGrade" />
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].merchantGrade" 
						 cssClass="select_styled" 
						 headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						 list="merGradeList" listKey="key" listValue="value" ></s:select>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.natureOfBusiness"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].natureOfBusiness" 
						 cssClass="select_styled" id="`+storename+`_natureOfBusiness"
						 headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						 list="natureOfBusinessCatgList" listKey="key" listValue="value" ></s:select>
				</td>	
				<td><label><s:text name="MerchantOnboardMakerAction.prfDocsProv"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>
					<select id="`+storename+`_prfDocsProv" multiple="multiple" disabled="disabled"></select>
					<input type="hidden" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].prfDocsProv" id="`+storename+`_prfDocsProvValue" >
				</td>
			</tr>
			<tr>
				<%--<td><label><s:text name="MerchantOnboardMakerAction.mcc"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>	
				<td>
					<input type="hidden" id="`+storename+`_cmccAutoComplete" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].cmccAutoComplete"
						value=""/>
					<input type="text" id="`+storename+`_mccautocomplete" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].mccautocomplete"
						value="" size="20" maxlength="4" autocomplete="off" class="position-relative"
						onkeypress="fn_storeMccAutocomplete('`+storename+`')"/>
				</td>--%>
				<td><label><s:text name="MerchantOnboardMakerAction.mvv"></s:text></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].mvv" maxlength="10" cssClass="formtextbox"></s:textfield></td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.relatonShipManager"></s:text></label></td>
				<td><s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].relatonShipManager" 
					 cssClass="select_styled" 
					 headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
					 list="relShipManagerList" listKey="key" listValue="value" ></s:select>
				</td>
				<td width="22%"><label><s:text name="MerchantOnboardMakerAction.incpStatus"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td>							
					<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].incorpstatus" 
						 cssClass="select_styled" 
						 headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						 list="incorpStatusList" listKey="key" listValue="value" ></s:select>
				</td>	
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.gstflag" /></label></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].gstFlag"
						value="0" list="#{'0':getText('MerchantOnboardMakerAction.include'),'1':getText('MerchantOnboardMakerAction.exclude')}" />
				</td>
				<td>
					<label><s:text name="MerchantOnboardMakerAction.gstno"></s:text>
					<%--<span class="star"><s:text name="CommonAction.mandatoryIndicator"/></span>--%>
					</label></td>
				<td>
					<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].gstNumber" cssclass="formtextbox"
					onkeypress="AllowNumeric();" maxlength="15" oninput="this.value = this.value.replace(/[^0-9]/g, '')" onPaste="return false" AUTOCOMPLETE="OFF" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.govtRegMerchant"/></label></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].govtRegMerchant" onchange="fn_govtRegMerchant(this, '`+storename+`');"
						value="0" list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
				</td>
				<td id="`+storename+`_govRegLabel" style="display:none;" >
					<label><s:text name="MerchantOnboardMakerAction.homeCountryId" />
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>		
				<td id="`+storename+`_govRegValue" style="display:none;" >
					<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].homeCountryId" 
						 cssClass="select_styled" 
						 headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						 list="homeCountryIdList" listKey="key" listValue="value" ></s:select>
				</td>
			</tr>
			<tr>
				<td id="`+storename+`_cyberMercID1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.cyberMercID"></s:text></label></td>
				<td id="`+storename+`_cyberMercID2" style="display: none;"><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].cyberMercID" maxlength="30" class="formtextbox"></s:textfield>
				</td>
				<td id="`+storename+`_cyberMercPass1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.cyberMercPass"></s:text></label></td>
				<td id="`+storename+`_cyberMercPass2" style="display: none;"><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].cyberMercPass" maxlength="70" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td id="`+storename+`_cyberMercKey1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.cyberMercKey"></s:text></label></td>
				<td id="`+storename+`_cyberMercKey2" style="display: none;"><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].cyberMercKey" maxlength="50" cssClass="formtextbox"></s:textfield>
				</td>
			</tr>
			<tr>
				<td id="`+storename+`_aaniPayStoreID1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.aaniPayStoreID" ></s:text></label></td>
				<td id="`+storename+`_aaniPayStoreID2" style="display: none;"><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].aaniPayStoreID" maxlength="16" onkeypress="AllowNumeric();" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.webSiteName"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].webSiteName" maxlength="40" cssClass="formtextbox"></s:textfield>
				</td>
				<td><label><s:text name="MerchantOnboardMakerAction.websiteAddress"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].websiteAddress" maxlength="40" cssClass="formtextbox" ></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.paymentFrecForCard"/></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].paymentFrecForCard" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						listKey="key" listValue="value" headerKey="" id="`+storename+`_paymentFrecForCard"
						list="#{'1':getText('MerchantOnboardMakerAction.defaultSelect'),
								'1':getText('MerchantOnboardMakerAction.daily'),
								'5':getText('MerchantOnboardMakerAction.tplusn'),
								'4':getText('MerchantOnboardMakerAction.monthly')}"
						cssClass="select_styled" ></s:select>
				</td>
				<td id="`+storename+`_paymentFrecForCard1" style="display: none;">
					<label><s:text name="MerchantOnboardMakerAction.paymentFrecForCardDays"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td id="`+storename+`_paymentFrecForCard2" style="display: none;">
					<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].paymentFrecForCardDays" maxlength="4" cssClass="formtextbox" onkeypress="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield>
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.aaniPayTransPayment" /></label></td>
				<td><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].aaniPayTransPayment" 
						value="0" onchange="fn_aaniPayTransPayment_Update(this, '`+storename+`', '`+tempStoreCount+`');"
						list="#{'0':getText('MerchantOnboardMakerAction.deffered'),'1':getText('MerchantOnboardMakerAction.realtime')}" />
				</td>
			</tr>
			<tr id="`+storename+`_aaniPayTransPayment">
				<td><label><s:text name="MerchantOnboardMakerAction.paymentFrecForAaniPay"/></label></td>
				<td>
					<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].paymentFrecForAaniPay" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
						listKey="key" listValue="value" headerKey="" id="`+storename+`_paymentFrecForAaniPay" 
						list="#{'1':getText('MerchantOnboardMakerAction.defaultSelect'),
								'1':getText('MerchantOnboardMakerAction.daily'),
								'5':getText('MerchantOnboardMakerAction.tplusn'),
								'4':getText('MerchantOnboardMakerAction.monthly')}"
						cssClass="select_styled" ></s:select>
				</td>
				<td id="`+storename+`_paymentFrecForAaniPay1" style="display: none;">
					<label><s:text name="MerchantOnboardMakerAction.paymentFrecForAaniPayDays"></s:text>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
				<td id="`+storename+`_paymentFrecForAaniPay2" style="display: none;">
					<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].paymentFrecForAaniPayDays" maxlength="4" cssClass="formtextbox" onkeypress="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield>
				</td>
			</tr>
			<tr id="`+storename+`_pricingDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.storePriceDetailsRequired" /></label></td>
				<td colspan="4" class="d-flex"><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storePriceDetailsRequired" 
						value="0" onchange="fn_storePriceDetailsRequired(this, '`+storename+`', '`+tempStoreCount+`');"
						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="`+storename+`_PriceModalLink" href="#" data-toggle="modal" data-target="#`+storename+`_PriceModal" style="display:none; margin-bottom: 10px !important; margin-top: 0px !important; align-content: center;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
							<!-- STORE PriceModal - START -->
							<div class="modal modal-wide fade" id="`+storename+`_PriceModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog modal-xl">
									<div class="modal-content">
										<div class="modal-header">
										<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.storePriceDetailsRequired" /></h4>
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
										</div>
										<div class="modal-body">
											<table border="0" width="100%" cellspacing="4">
												<tr id="`+storename+`_pricingInfo_Row1">
													<td><label><s:text name="MerchantOnboardMakerAction.captureMerchantServiceFeeAt" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
													</td>
													<td>
														<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeCaptureServiceFeeAt" value="2"
															list="#{'2':getText('MerchantOnboardMakerAction.storeLevel'),'3':getText('MerchantOnboardMakerAction.terminalLevel')}" />
								
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeePlanType" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeFeePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
															value="1" list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" onchange="getStoreServiceFeePlan('`+storename+`', '`+tempStoreCount+`');" />
													</td>
												</tr>
												<tr id="`+storename+`_pricingInfo_Row2">
													<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeeplan" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeMerchantServiceFeePlan" headerKey=""
															headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
															id="`+storename+`_storeservicefeeplan" list="feePlanList"  listKey="value" 
															listValue="value" cssClass="select_styled">
														</s:select>
													</td>
													/* <td><label><s:text name="MerchantOnboardMakerAction.mercFeeDeduWaiver" /></label></td>
													<td><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeMercFeeDeduWaiver" value="0"
														list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
													</td> */
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
			<tr id="`+storename+`_addressDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.useSameAsMercAddressForStore"/></label></td> 
				<td colspan="4" class="d-flex">
					<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].useSameAsMercAddressForStore" 
						value="0" onchange="fn_useSameAsMercAddressForStore(this, '`+storename+`', '`+tempStoreCount+`');"
						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="`+storename+`_AddressModalLink" href="#" data-toggle="modal" data-target="#`+storename+`_AddressModal" style="display:none; margin-bottom: 10px !important; margin-top: 0px !important; align-content: center;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
							<!-- STORE AddressModal - START -->
							<div class="modal modal-wide fade" id="`+storename+`_AddressModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog modal-xl">
									<div class="modal-content">
										<div class="modal-header">
										<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.useSameAsMercAddressForStore" /></h4>
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
										</div>
										<div class="modal-body">
											<table border="0" width="100%" cellspacing="4">
												<tr id="`+storename+`_addressInfo_Row1">
													<td><label><s:text name="MerchantOnboardMakerAction.addressline1"/></label>
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeaddressline1" maxlength="50" cssClass="formtextbox" />
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.addressline2"/></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeaddressline2" maxlength="50" cssClass="formtextbox" />
													</td>
												</tr>
												<tr id="`+storename+`_addressInfo_Row2">
													<td><label><s:text name="MerchantOnboardMakerAction.country"/></label>
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
													<td>
														<input type="hidden" id="`+storename+`_ccountry" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeccountry" value="${merchantOnboardBean.ccountry}" readonly="readonly"/>
														<input type="text" id="`+storename+`_country" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storecountry" value="${merchantOnboardBean.country}" readonly="readonly" class="position-relative"/>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.state"/></label>
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
													<td>
														<input type="hidden" id="`+storename+`_cstate" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storecstate" value="" />
														<input type="text" id="`+storename+`_state" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storestate" value="" size="20" maxlength="40" autocomplete="off" 
														onkeypress="AllowAlphabeticWithSpace();fn_storeStateDetails('`+storename+`');" class="position-relative"/>
													</td>
												</tr>
												<tr id="`+storename+`_addressInfo_Row3">
													<td><label><s:text name="MerchantOnboardMakerAction.citysuburb"/></label>
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
													<td>
														<input type="hidden" id="`+storename+`_cselectcity" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storecselectcity" value="" />
														<input type="text" id="`+storename+`_selectcity" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeselectcity" value="" size="20" maxlength="40" autocomplete="off" 
														onkeypress="AllowAlphabeticWithSpace();fn_storeCityDetails('`+storename+`');" class="position-relative"/>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.postalcode"/></label>
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storepostalcode" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')" />
													</td>
												</tr>
												<tr id="`+storename+`_addressInfo_Row4">
													<td><label><s:text name="MerchantOnboardMakerAction.businessContactEmailID"/></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storebusinessContactEmailID" maxlength="50" cssClass="formtextbox" />
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.businessContactMobileNo"/></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storebusinessContactMobileNo" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</td>			
												</tr>
												<tr id="`+storename+`_addressInfo_Row5">
													<td><label><s:text name="MerchantOnboardMakerAction.technicalContactEmailID"/></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storetechnicalContactEmailID" maxlength="50" cssClass="formtextbox" />
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.technicalContactMobileNo"/></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storetechnicalContactMobileNo" maxlength="15" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
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
			<tr id="`+storename+`_riskDetails">
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.storeRiskDetailsRequired" /></label></td>
				<td colspan="4" class="d-flex"><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeRiskDetailsRequired" 
						value="0" onchange="fn_storeRiskDetailsRequired(this, '`+storename+`', '`+tempStoreCount+`');"
						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							<a id="`+storename+`_RiskModalLink" href="#" data-toggle="modal" data-target="#`+storename+`_RiskModal" style="display:none; margin-bottom: 10px !important; margin-top: 0px !important; align-content: center;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
							<!-- STORE RiskModal - START -->
							<div class="modal modal-wide fade" id="`+storename+`_RiskModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
								<div class="modal-dialog modal-xl">
									<div class="modal-content">
										<div class="modal-header">
										<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.storeRiskDetailsRequired" /></h4>
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
										</div>
										<div class="modal-body">
											<table border="0" width="100%" cellspacing="4">
												<tr id="`+storename+`_riskInfo_Row1">
													<td><label><s:text name="MerchantOnboardMakerAction.riskCategory" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator" /></span></label></td>
													<td>
														<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].riskCategory" headerKey="" 
															headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
															list="riskCategoryList" listKey="key" listValue="value" cssClass="select_styled">
														</s:select>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.padssPCIdssCertExpDate" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].padssPCIdssCertExpDate" style="width: 200px !important;" 
															cssClass="date-picker" readonly="true" id="`+storename+`_padssPCIdssCertExpDate" required="true"></s:textfield>
														<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="`+storename+`_eff" 
															onclick="javascript:NewCssCal(`+storename+`_padssPCIdssCertExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
															class="calendar-icon">	
													</td>
												</tr>
												<tr id="`+storename+`_riskInfo_Row2">
													<td><label><s:text name="MerchantOnboardMakerAction.cloudCertExpDate" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].cloudCertExpDate" style="width: 200px !important;" 
															cssClass="date-picker" readonly="true" id="`+storename+`_cloudCertExpDate" required="true"></s:textfield>
														<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="`+storename+`_cloudCertExpDate1"  
															onclick="javascript:NewCssCal('`+storename+`_cloudCertExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
															class="calendar-icon">
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.tradeLicenseExpDate" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].tradeLicenseExpDate" style="width: 200px !important;" 
															cssClass="date-picker" readonly="true" id="`+storename+`_tradeLicenseExpDate" required="true"></s:textfield>
														<img src="<%=request.getContextPath()%>/images/calendar-icon.png" id="`+storename+`_tradeLicenseExpDate1"  
															onclick="javascript:NewCssCal('`+storename+`_tradeLicenseExpDate','<%=(String)request.getSession().getAttribute("DATE-FRMT")%>','<%=(String)request.getSession().getAttribute("DATE-SEP")%>','dropdown'),lightbox(null)"  
															class="calendar-icon">	
													</td>
												</tr>
												<tr id="`+storename+`_riskInfo_Row3">
													<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField1" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].additionalUDFField1" maxlength="10" cssClass="formtextbox"/>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue1" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].additionalUDFValue1" maxlength="10" cssClass="formtextbox"/>
													</td>
												</tr>
												<tr id="`+storename+`_riskInfo_Row4">
													<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField2" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].additionalUDFField2" maxlength="10" cssClass="formtextbox"/>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue2" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].additionalUDFValue2" maxlength="10" cssClass="formtextbox"/>
													</td>
												</tr>
												<tr id="`+storename+`_riskInfo_Row5">
													<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFField3" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].additionalUDFField3" maxlength="10" cssClass="formtextbox"/>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.additionalUDFValue3" /></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].additionalUDFValue3" maxlength="10" cssClass="formtextbox"/>
													</td>
												</tr>
												<tr id="`+storename+`_riskInfo_Row6">
													<td><label><s:text name="MerchantOnboardMakerAction.transCountperMonth" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].transCountperMonth" 
															id="`+storename+`_transCountperMonth" maxlength="7" cssClass="formtextbox" 
															oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.averageTicketSize" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].averageTicketSize" 
															id="`+storename+`_averageTicketSize" maxlength="25" cssClass="formtextbox" 
															onchange="calculateQuaterlySaleAmt('`+storename+`', '`+tempStoreCount+`');" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</td>
												</tr>
												<tr id="`+storename+`_riskInfo_Row7">
													<td><label><s:text name="MerchantOnboardMakerAction.quarterlySale" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].quarterlySale" 
															id="`+storename+`_quarterlySale" maxlength="25" readonly="true" 
															cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.transToRefundPercent" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].transToRefundPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</td>
												</tr>
												<tr id="`+storename+`_riskInfo_Row8">
													<td><label><s:text name="MerchantOnboardMakerAction.transToChargebackPercent" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].transToChargebackPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</td>
													<td><label><s:text name="MerchantOnboardMakerAction.transPercent" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].transPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
													</td>
												</tr>
												<tr id="`+storename+`_riskInfo_Row9">
													<td><label><s:text name="MerchantOnboardMakerAction.internationalTransPercent" />
														<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
													<td>
														<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].internationalTransPercent" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/>
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
			<tr id="`+storename+`_accountDetails" >
				<td colspan="1"><label><s:text name="MerchantOnboardMakerAction.useSameAsMercAccountForStore"/>
					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td> 
				`+acctdetails+`
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.storeType"/></td>
				<td>
					<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].storeType" value="2"
						list="#{'1':getText('MerchantOnboardMakerAction.Online'),'2':getText('MerchantOnboardMakerAction.Offline')}" />
				</td>
			</tr>
			<tr>
				<td><label><s:text name="MerchantOnboardMakerAction.isTerminal"/></label></td>
				<td><div id="checkbox">
					<s:checkbox name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].termneed" id="`+storename+`_termneeded" fieldValue="1" labelposition="before" theme="simple" onchange="fn_StoreTermNeed(this, '`+storename+`', '`+tempStoreCount+`'); fn_merchantEntityValue();"/>
					<input type="hidden" id="`+storename+`_termCount" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].termCount" value="0">
				</div></td>
				<td colspan="2" align="center" id="`+storename+`_docLinkRow">
					<a href="#" class="global-links" onclick="javascript:openWindowAttachmentsForStore('`+storename+`', '`+tempStoreCount+`', '${strTempFoldername}', '${merchantOnboardBean.refSqid}');">Add/View Attached Documents For Store `+storeCount+`</a>
					<s:hidden name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].tempFolderName" id="`+storename+`_tempFolderName"></s:hidden>
				</td>
			</tr>
			<tr id="`+storename+`_terminalDetails" style="display:none;">
				<td colspan="6">
					<table style="width: 100%;" id="`+storename+`_terminalTable"></table>
					<table style="width: 100%;">
						<tr>
							<td align="right" colspan="6"><input type="button" name="addTerminalButton" value='Additional Terminal' class="btn btn-primary" onclick="fn_addTerminalDetails('`+storename+`', '`+tempStoreCount+`'); fn_merchantEntityValue();" />
								<div style="display:none;"><s:select id="`+storename+`_lstProuctType" cssClass="select_styled" headerValue="--Select--" list="lstProuctType" listKey="key" listValue="value" /></div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</div>`;

	accordion_Details.appendChild(newRow);
	var allPanels = $('.accordion').find('.content').hide();
	document.getElementById('storeCount').value = storeCount;
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].merchantGrade']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].merchantGrade']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].natureOfBusiness']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].natureOfBusiness']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].relatonShipManager']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].relatonShipManager']").val(mercAggrrelm).selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].incorpstatus']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].incorpstatus']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].homeCountryId']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].homeCountryId']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].paymentFrecForCard']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].paymentFrecForCard']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].paymentFrecForAaniPay']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].paymentFrecForAaniPay']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storeMerchantServiceFeePlan']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storeMerchantServiceFeePlan']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].riskCategory']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].riskCategory']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storeacctWithBank']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storeacctWithBank']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storecurrency']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storecurrency']").val('').selectmenu('refresh');
	//$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storestmtfreq']").selectmenu();
	//$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storestmtfreq']").val('').selectmenu('refresh');
	acctdetails =''; dbaname ='';mercAggrrelm='';

	$("#"+storename+"_natureOfBusiness").selectmenu({
		change : function(){
			fn_getPrfDocsProvDetailsUsingNOB(storename);
		}
	});
	$("#"+storename+"_paymentFrecForCard").selectmenu({
		change : function(){	
			fn_getPaymentFrecForCard(storename);
		}
	});
	$("#"+storename+"_paymentFrecForAaniPay").selectmenu({
		change : function(){	
			fn_getPaymentFrecForAaniPay(storename);
		}
	});
	$("#"+storename+"_storeacctWithBank").selectmenu({
		change : function(){	
			fn_storeAcctWithBank(this, storename);
		}
	});
	/* $("select[id$='merchantEntityName']").each(function() {
        $(this).on("change", function() {
            fn_merchantEntityValue();
        });
    }); */
}

function fn_useSameAsMercAccountForStore(val, storename, tempStoreCount){
	if (val.value == '1')
	{
		$("#"+storename+"_AccountModalLink").show();
	} else {
		$("#"+storename+"_AccountModalLink").hide();
	}
}

function fn_useSameAsMercAddressForStore(val, storename, tempStoreCount){
	if (val.value == '1')
	{
		$("#"+storename+"_AddressModalLink").show();
	} else {
		$("#"+storename+"_AddressModalLink").hide();
	}
}

function fn_storePriceDetailsRequired(val, storename, tempStoreCount){
	if (val.value == '1')
	{
		$("#"+storename+"_PriceModalLink").show();
	} else {
		$("#"+storename+"_PriceModalLink").hide();
	}
}

function fn_storeRiskDetailsRequired(val, storename, tempStoreCount){
	if (val.value == '1')
	{
		$("#"+storename+"_RiskModalLink").show();
	} else {
		$("#"+storename+"_RiskModalLink").hide();
	}
}

function fn_govtRegMerchant(val, storename){
	if(val.value == 1){
		$("#"+storename+"_govRegLabel").show();
		$("#"+storename+"_govRegValue").show();
	}else{
		$("#"+storename+"_govRegLabel").hide();
		$("#"+storename+"_govRegValue").hide();
	}
}

function fn_StoreTermNeed(val, storename, tempStoreCount){
	var value = val.checked ? "1" : "0";
    document.getElementById(storename+"_termneeded").value = value;
	if (value == '1')
	{
		$('#'+storename+'_terminalDetails').show();
		var termcount = parseInt(document.getElementById(storename+'_termCount').value);
		if(termcount == 0){
			fn_addTerminalDetails(storename, tempStoreCount);
		}
	}
	else
	{
		$('#'+storename+'_terminalDetails').hide();
		var table = document.getElementById(storename+'_terminalTable');
		while (table.rows.length > 0) { table.deleteRow(0); }
		document.getElementById(storename+'_termCount').value = 0;
	}
}

function fn_addTerminalDetails(storename, tempStoreCount) {
	var table = document.getElementById(storename+'_terminalTable');
	var textselect = document.getElementById(storename+'_lstProuctType').innerHTML;		
	var termcount = parseInt(document.getElementById(storename+'_termCount').value);
	//var cmccAutoComplete = document.getElementById(storename+'_cmccAutoComplete').value;
	//var mccautocomplete = document.getElementById(storename+'_mccautocomplete').value;
	tempStoreCount = parseInt(tempStoreCount);
	var tempTermCount = termcount;
	termcount = termcount+1;
	var acctMapLvl = $("input:radio[name='merchantOnboardBean.acctMapLvl']:checked").val();
	var acctdetails = `<td class="d-flex"><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].useSameAsStoreAccountForTerm" 
				onchange="fn_useSameAsStoreAccountForTerm(this, '`+storename+`_TERM`+termcount+`', '`+tempStoreCount+`', '`+tempTermCount+`');"
				value="0" list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
					<a id="`+storename+`_TERM`+termcount+`_AccountModalLink" href="#" data-toggle="modal" data-target="#`+storename+`_TERM`+termcount+`_AccountModal" style="display:none; margin-bottom: 10px !important; margin-top: 0px !important; align-content: center;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
					<!-- TERMINAL AccountModal - START -->
					<div class="modal modal-wide fade" id="`+storename+`_TERM`+termcount+`_AccountModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog modal-xl">
							<div class="modal-content">
								<div class="modal-header">
								<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.useSameAsStoreAccountForTerm" /></h4>
									<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
								</div>
								<div class="modal-body">
									<table border="0" width="100%" cellspacing="4">
										<tr id="`+storename+`_TERM`+termcount+`_accountinfo_Row1">
											<td class="formtext"><label><s:text name="MerchantOnboardMakerAction.accountWithBank"/>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
											</td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalacctWithBank" headerKey="" 
													headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" 
													list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}"
													listKey="key" id="`+storename+`_TERM`+termcount+`_terminalacctWithBank" listValue="value" cssClass="select_styled" >
												</s:select>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.defaultAccount" /></label></td>
											<td><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminaldefAccount" value="0"
												list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
											</td>
										</tr>
										<tr id="`+storename+`_TERM`+termcount+`_accountinfo_Row2">
											<td id="`+storename+`_TERM`+termcount+`_acctBranch" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.acctBranch"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
											</td>
											<td id="`+storename+`_TERM`+termcount+`_acctBranchDetails" style="display:none;">
												<input type="hidden" id="`+storename+`_TERM`+termcount+`_cTerminalacctBranch" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].cTerminalacctBranch" />
												<input type="text" id="`+storename+`_TERM`+termcount+`_terminalacctBranch" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalacctBranch" 
													size="20" maxlength="50" autocomplete="off" 
													onkeypress="AllowAlphaNumericWithSpace();fn_terminalacctBranch('`+storename+`_TERM`+termcount+`');" class="position-relative" />
											</td>
											<td id="`+storename+`_TERM`+termcount+`_bankName" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.bankName"></s:text></label>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span>
											</td>
											<td id="`+storename+`_TERM`+termcount+`_bankNameText" style="display:none;" class="formtext">
												<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalacctBankText" maxlength="50" cssClass="formtextbox"/>
											</td>
											<td id="`+storename+`_TERM`+termcount+`_branchName" style="display:none;" class="formtext"><label><s:text name="MerchantOnboardMakerAction.branchName"></s:text></label>
											</td>
											<td id="`+storename+`_TERM`+termcount+`_branchNameText" style="display:none;" class="formtext">
												<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalacctBranchText" maxlength="50" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="`+storename+`_TERM`+termcount+`_accountinfo_Row3">
											<td><label><s:text name="MerchantOnboardMakerAction.IFSC"></s:text>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></label></span>
											</td>
											<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalifsc" maxlength="11" cssClass="formtextbox" /></td>
											<td><label><s:text name="MerchantOnboardMakerAction.accountname"></s:text></label></td>
											<td>
												<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalaccountname" label="MerchantOnboardMakerAction.accountname" maxlength="20" cssClass="formtextbox" />
											</td>
										</tr>	
										<tr id="`+storename+`_TERM`+termcount+`_accountinfo_Row4">
											<td><label><s:text name="MerchantOnboardMakerAction.currency"></s:text>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></label></span>
											</td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalcurrency" 
													cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
													list="crcyList" listKey="key" listValue="value"></s:select>
											</td>
											<td><label><s:text name="MerchantOnboardMakerAction.accountNumber"></s:text>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalacctNumber" onkeypress="AllowAlpaNumeric();" maxlength="36" cssClass="formtextbox"/>
											</td>
										</tr>
										<tr id="`+storename+`_TERM`+termcount+`_accountinfo_Row5">
											<td><label><s:text name="MerchantOnboardMakerAction.cifNumber"></s:text>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
											<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalcifNumber" onkeypress="AllowNumeric();" maxlength="7" cssClass="formtextbox"/>
											</td>
											<%-- <td><label><s:text name="MerchantOnboardMakerAction.stmtfrequency"></s:text>
												<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
											</td>
											<td>
												<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalstmtfreq" 
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
		</td>`;
	var newRow = document.createElement('tr');
	newRow.innerHTML = 
		`<td>
		 	<ul class="nav nav-tabs" style="height: 15px;">
		 		<li class="nav-item" style="width: 50%; padding-left: 10px; border-right: none;">Terminal Details - `+termcount+` </li>
		 		<li class="nav-item" style="width: 50%; padding-right: 10px; border-left: none; text-align: right;">
		 			<img src="${pageContext.request.contextPath}/images/trash_icon.png" alt="trashbutton" type="button" class="trash-icon" onclick="fn_deleteTerminalDetails(this, '`+storename+`');  fn_merchantEntityValue();"/>
		 		</li>
		 	</ul>
		 	<div class="tab-content">
		 		<div class="tab-pane show active">
		 			<table id="`+storename+`_TERM`+termcount+`_details" align="center" border="0" width="100%" style="margin-top: 20px;"> 
		 				<tr> 
		 					<td><label><s:text name="MerchantOnboardMakerAction.productType"/> 
		 						<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label> 
		 					</td> 
							<td>
								<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].termProuctType" cssClass="select_styled"
									list="lstProuctType" listKey="key" listValue="value"></s:select>
								<!-- <select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].termProuctType" class="select_styled">
								    <c:forEach var="item" items="${lstProuctType}">
								        <option value="${item.key}" <c:if test="${item.key == '1'}">selected</c:if> >${item.value}</option>
								    </c:forEach>
								</select> -->
							</td>
							<td><label><s:text name="MerchantOnboardMakerAction.tid"></s:text>
								<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
		 					<td>
			 					<s:if test="autoGenerateTid == 0">
									<s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].termCode" cssClass="formtextbox" maxlength="15"></s:textfield>
								</s:if>
								<s:elseif test="autoGenerateTid == 1">
									<input type="text" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].termCode" value="AUTO GENERATE" 
										style="color: black;background: lightgray;" class="formtextbox" maxlength="15" readonly="readonly">
								</s:elseif>
		 					</td>
		 				</tr>
		 				<tr>
			 				<td><label><s:text name="MerchantOnboardMakerAction.terminalName"></s:text>
								<span class="star"><s:text name="CommonAction.mandatoryIndicator" /> </span></label></td>
							<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalName" cssClass="formtextbox" maxlength="25" ></s:textfield></td>
			 				<td><label><s:text name="MerchantOnboardMakerAction.paymentMethod" />
			 					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			 				<td colspan="3">
				 				<s:checkboxlist name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].paymentMethod" 
			 						listKey="key" listValue="value" list="paymentMethodList"></s:checkboxlist>
			 				</td>
			 			</tr>
						<tr>
			 				<td id="`+storename+`_TERM`+termcount+`_aaniPayTermID1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.aaniPayTermID" /></label></td>
							<td id="`+storename+`_TERM`+termcount+`_aaniPayTermID2" style="display: none;"><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].aaniPayTermID" maxlength="16" onkeypress="AllowNumeric();" cssClass="formtextbox" ></s:textfield>
							</td>
						</tr>
		 				<tr>
							<%--<td><label><s:text name="MerchantOnboardMakerAction.terminalmcc"/>
			 					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
							<td> 
								<input type="hidden" id="`+storename+`_TERM`+termcount+`_cterminalmccautoComplete" 
									name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].cterminalmccautoComplete" 
									value="`+cmccAutoComplete+`" />
								<input type="text" id="`+storename+`_TERM`+termcount+`_terminalmccautocomplete" 
									name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalmccautocomplete" 
									value="`+mccautocomplete+`" size="20" maxlength="4" autocomplete="off" class="position-relative" onkeypress="fn_terminalMccAutocomplete('`+storename+`_TERM`+termcount+`')"/>
							</td>--%>
							<td><label><s:text name="MerchantOnboardMakerAction.terminalchargeplan"/></label></td>	
							<td><input type="text" id="`+storename+`_TERM`+termcount+`_dftTrmlRcrngChrgePlnIdname" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].dftTrmlRcrngChrgePlnIdname" 
									value="" size="20" maxlength="40" autocomplete="off" class="position-relative" 
									onkeypress="fn_RcrngChrgePln('`+storename+`_TERM`+termcount+`');" /> 
								<input type="hidden" id="`+storename+`_TERM`+termcount+`_RcrngChrgePlnId" name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].dftTrmlRcrngChrgePlnId" value="" />
							</td>
		 				</tr>
		 				<tr>
		 					<td><label><s:text name="MerchantOnboardMakerAction.terminalRecurringPlanWaiver" /></label></td>
		 					<td><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalRecurringPlanWaiver" 
									value="0" list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}"/>
							</td>
						</tr>
		 				<tr>
						 	<td><label><s:text name="MerchantOnboardMakerAction.enableRiskProfile"/>
						 		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td></td>
						 	<td>
								<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalRiskProfileIndc" 
									onchange="showRiskDetail(this, '`+storename+`_TERM`+termcount+`');"
		  							value="0" list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
							</td>
						 	<td id="`+storename+`_TERM`+termcount+`_riskLabel" style="display:none"><label><s:text name="MerchantOnboardMakerAction.riskProfile" />
						 		<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
						 	<td id="`+storename+`_TERM`+termcount+`_riskValue" style="display:none">
						 		<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalRiskProfile" 
							 		cssClass="select_styled" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
									list="ecomRiskProfileNamesList" listKey="key" listValue="value"></s:select>
						 	</td>
					 	</tr>
					 	<tr id="`+storename+`_TERM`+termcount+`_accountdetails">
							<td><label><s:text name="MerchantOnboardMakerAction.useSameAsStoreAccountForTerm"/>
		 						<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
		 					</td>
							`+acctdetails+`
		 				</tr>
			 			<tr id="`+storename+`_TERM`+termcount+`_pricingDetails">
			 				<td><label><s:text name="MerchantOnboardMakerAction.terminalPriceDetailsRequired" />
			 					<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
			 				<td class='d-flex'><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalPriceDetailsRequired" 
			 						value="0" onchange="fn_terminalPriceDetailsRequired(this, '`+storename+`_TERM`+termcount+`', '`+tempStoreCount+`', '`+tempTermCount+`');"
			 						list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
			 							<a id="`+storename+`_TERM`+termcount+`_PriceModalLink" href="#" data-toggle="modal" data-target="#`+storename+`_TERM`+termcount+`_PriceModal" style="display:none; align-content: center; margin-bottom: 10px !important; margin-top: 0px !important;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
										<!-- TERMINAL PriceModal - START -->
										<div class="modal modal-wide fade" id="`+storename+`_TERM`+termcount+`_PriceModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
											<div class="modal-dialog modal-xl">
												<div class="modal-content">
													<div class="modal-header">
													<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.terminalPriceDetailsRequired" /></h4>
														<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
													</div>
													<div class="modal-body">
														<table border="0" width="100%" cellspacing="4">
															<tr id="`+storename+`_TERM`+termcount+`_pricingInfo_Row1">
																<td><label><s:text name="MerchantOnboardMakerAction.captureMerchantServiceFeeAt" />
																<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label>
																</td>
																<td>
																	<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalCaptureServiceFeeAt" value="3"
																		list="#{'3':getText('MerchantOnboardMakerAction.terminalLevel')}" />
													
																</td>
																<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeePlanType" />
																	<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td>
																	<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalFeePlanType" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}"
																		value="1" list="#{'1':getText('MerchantOnboardMakerAction.transactionBased'),'2':getText('MerchantOnboardMakerAction.tireBased')}" onchange="getTerminalServiceFeePlan('`+storename+`_TERM`+termcount+`', '`+tempStoreCount+`' , '`+tempTermCount+`');" />
																</td>
															</tr>
															<tr id="`+storename+`_TERM`+termcount+`_pricingInfo_Row2">
																<td><label><s:text name="MerchantOnboardMakerAction.merchantServiceFeeplan" />
																	<span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td>
																	<s:select name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalMerchantServiceFeePlan" id="`+storename+`_TERM`+termcount+`_terminalservicefeeplan" 
																		listKey="value" listValue="value" headerKey="" headerValue="%{getText('MerchantOnboardMakerAction.defaultSelect')}" list="feePlanList" 
																		cssClass="select_styled" >
																	</s:select>
																</td>
																/* <td><label><s:text name="MerchantOnboardMakerAction.mercFeeDeduWaiver" /></label></td>
																<td><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].terminalMercFeeDeduWaiver" value="0"
																	list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
																</td> */
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
			 			<tr id="`+storename+`_TERM`+termcount+`_RRFdetails">
							<td><label><s:text name="MerchantOnboardMakerAction.rollingReserveFundRequired"/></label></td>
							<td class='d-flex'><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].rollingReserveFundRequired" 
									onchange="fn_RRFdetailsForTerm(this, '`+storename+`_TERM`+termcount+`', '`+tempStoreCount+`', '`+tempTermCount+`');"
									value="0" list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
										<a id="`+storename+`_TERM`+termcount+`_RRFModalLink" href="#" data-toggle="modal" data-target="#`+storename+`_TERM`+termcount+`_RRFModal" style="display:none;  align-content: center; margin-bottom: 10px !important; margin-top: 0px !important;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
										<!-- TERMINAL RRFModal - START -->
										<div class="modal modal-wide fade" id="`+storename+`_TERM`+termcount+`_RRFModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
											<div class="modal-dialog modal-xl">
												<div class="modal-content">
													<div class="modal-header">
													<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.rollingReserveFundRequired" /></h4>
														<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
													</div>
													<div class="modal-body">
														<table border="0" width="100%" cellspacing="4">
															<tr id="`+storename+`_TERM`+termcount+`_RRFdetails_Row1">
																<td>
																	<label><s:text name="MerchantOnboardMakerAction.rrfIndc"/></label>
																</td>
																<td nowrap="nowrap">
																	<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].rrfIndc" 
																		id="`+storename+`_TERM`+termcount+`_rrfIndc" onchange="rrfFieldsEnable(this, '`+storename+`_TERM`+termcount+`', '`+tempStoreCount+`', '`+tempTermCount+`');" value="0"
																		list="#{'0':getText('MerchantOnboardMakerAction.none'),'1':getText('MerchantOnboardMakerAction.only%'),
																		'2':getText('MerchantOnboardMakerAction.fixedAmountOnly'),'3':getText('MerchantOnboardMakerAction.both')}" />
																</td>
															</tr>
															<tr id="`+storename+`_TERM`+termcount+`_RRFdetails_Row2">
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domestic"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv2"><label><s:text name="MerchantOnboardMakerAction.domestic"/></label></td>
																<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].domestic" maxlength="3" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv7" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv8" ><label><s:text name="MerchantOnboardMakerAction.domesticFixedAmount"/></label></td>
																<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].domesticFixedAmount" maxlength="10" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
															</tr>
															<tr id="`+storename+`_TERM`+termcount+`_RRFdetails_Row3">													
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv3" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv4"><label><s:text name="MerchantOnboardMakerAction.interNational"></s:text></label></td>
																<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].interNational" maxlength="3" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv9" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/> <span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv10" ><label><s:text name="MerchantOnboardMakerAction.internationalFixedAmount"/></label></td>
																<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].internationalFixedAmount" maxlength="10" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>			
															</tr>
															<tr id="`+storename+`_TERM`+termcount+`_RRFdetails_Row4">	
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv5" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label> </td>
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv6"><label><s:text name="MerchantOnboardMakerAction.domesticHoldingDays"></s:text></label></td>
																<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].domesticHoldingDays" maxlength="4" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv11" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td id="`+storename+`_TERM`+termcount+`_RRFdiv12"><label><s:text name="MerchantOnboardMakerAction.internationalHoldingDays"/> </label></td>
																<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].internationalHoldingDays" maxlength="4" cssClass="formtextbox" readonly="true" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
															</tr>
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
						<%-- <tr id="`+storename+`_TERM`+termcount+`_SDRdetails">
							<td><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryRequired"/></label></td>
							<td class='d-flex'><s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].securityDepositRecoveryRequired" 
									onchange="fn_SDRdetailsForTerm(this, '`+storename+`_TERM`+termcount+`', '`+tempStoreCount+`', '`+tempTermCount+`');"
									value="0" list="#{'1':getText('MerchantOnboardMakerAction.Yes'),'0':getText('MerchantOnboardMakerAction.No')}" />
										<a id="`+storename+`_TERM`+termcount+`_SDRModalLink" href="#" data-toggle="modal" data-target="#`+storename+`_TERM`+termcount+`_SDRModal" style="display:none;  align-content: center; margin-bottom: 10px !important; margin-top: 0px !important;  margin-left: 10px;" >&nbsp;<span>View Details</span></a>
										<!-- TERMINAL SDRModal - START -->
										<div class="modal modal-wide fade" id="`+storename+`_TERM`+termcount+`_SDRModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
											<div class="modal-dialog modal-xl">
												<div class="modal-content">
													<div class="modal-header">
													<h4 class="modal-title"><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryRequired" /></h4>
														<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
													</div>
													<div class="modal-body">
														<table border="0" width="100%" cellspacing="4">
															<tr id="`+storename+`_TERM`+termcount+`_SDRdetails_Row1">
																<td>
																	<label><s:text name="MerchantOnboardMakerAction.sdrIndc"/></label>
																</td>
																<td nowrap="nowrap">
																	<s:radio name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].sdrIndc" 
																		id="`+storename+`_TERM`+termcount+`_sdrIndc" onchange="sdrFieldsEnable(this, '`+storename+`_TERM`+termcount+`', '`+tempStoreCount+`', '`+tempTermCount+`');" value="0"  
																		list="#{'0':getText('MerchantOnboardMakerAction.amount'),'1':getText('MerchantOnboardMakerAction.only%')}" />
																</td>
															</tr>
															<tr id="`+storename+`_TERM`+termcount+`_SDRdetails_Row2">
																<td id="`+storename+`_TERM`+termcount+`_SDRdiv1" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryPercentage"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td id="`+storename+`_TERM`+termcount+`_SDRdiv2"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryAmount"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].securityDepositRecoveryAmount" maxlength="6" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"></s:textfield></td>	
																<td id="`+storename+`_TERM`+termcount+`_SDRdiv3" style="display: none;"><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td id="`+storename+`_TERM`+termcount+`_SDRdiv4" ><label><s:text name="MerchantOnboardMakerAction.securityDepositRecoveryDays"/><span class="star"><s:text name="CommonAction.mandatoryIndicator"></s:text></span></label></td>
																<td><s:textfield name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].lstTerminalBean[`+tempTermCount+`].securityDepositRecoveryDays" maxlength="3" cssClass="formtextbox" oninput="this.value = this.value.replace(/[^0-9]/g, '')"/></td>
															</tr>
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
		 		</div>
		 	</div>
		</td>`;
	
	table.appendChild(newRow);
	document.getElementById(storename+'_termCount').value = termcount;
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].termProuctType']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].termProuctType']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalRiskProfile']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalRiskProfile']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalacctWithBank']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalacctWithBank']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalcurrency']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalcurrency']").val('').selectmenu('refresh');
	//$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalstmtfreq']").selectmenu();
	//$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalstmtfreq']").val('').selectmenu('refresh');
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalMerchantServiceFeePlan']").selectmenu();
	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalMerchantServiceFeePlan']").val('').selectmenu('refresh');
	acctdetails='';
	
	$("#"+storename+"_TERM"+termcount+"_terminalacctWithBank").selectmenu({
		change : function(){	
			fn_termAcctWithBank(this, storename+"_TERM"+termcount);
		}
	});
}

function fn_deleteTerminalDetails(terminalTable, storename){
	var table = document.getElementById(storename+'_terminalTable');
	var rowCount = table.rows.length;
    if(rowCount <= 1){
        alert("Atleast One Row is Mandatory");
        return;
    }else{
    	terminalTable.parentNode.parentNode.parentNode.parentNode.remove();
    }
}

function fn_useSameAsStoreAccountForTerm(val, termname, tempStoreCount, tempTermCount){
	if(val.value == 1)
	{
		$("#"+termname+"_AccountModalLink").show();
	} else {
		$("#"+termname+"_AccountModalLink").hide();
	}
}

function fn_terminalPriceDetailsRequired(val, termname, tempStoreCount, tempTermCount){
	if(val.value == 1)
	{
		$("#"+termname+"_PriceModalLink").show();
	} else {
		$("#"+termname+"_PriceModalLink").hide();
	}
}

function fn_storeCountryDetails(storename){
	$("#"+storename+"_country").typeahead({ source: function (query, process) {
			$("#"+storename+"_ccountry").val('');
	        return $.ajax({
	            url: "${pageContext.request.contextPath}/merchantsales/countryDetails.action",
	            type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });  
	    },
		matcher: function (obj) {
		    var item = JSON.parse(obj);
		    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase());
		},
		sorter: function (items) {          
			var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
		    while (aItem = items.shift()) {
		       var item = JSON.parse(aItem);
		       if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
		       else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
		       else caseInsensitive.push(JSON.stringify(item));
		    }
	        return beginswith.concat(caseSensitive, caseInsensitive);
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&');
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
	        return '<strong>' + match + '</strong>';
	       });
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        $("#"+storename+"_ccountry").val(item.id);
	        return item.name;
	    }
	});
}

function fn_storeStateDetails(storename){
	$("#"+storename+"_state").typeahead({ source: function (query, process) {
		$("#"+storename+"_cstate").val('');
			return $.ajax({
				url: "${pageContext.request.contextPath}/merchantsales/stateDetails.action?countrycode="+document.getElementById(storename+"_ccountry").value,
				type: 'post',
				data: { query: query },
				dataType: 'json',
				headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
				success: function (result) {
						var resultList = result.map(function (item) {
						var aItem = { id: item.value, name: item.desc};
						return JSON.stringify(aItem);
					});
					return process(resultList);
				}
			});
	    },
		matcher: function (obj) {
		    var item = JSON.parse(obj);
		    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
		sorter: function (items) {          
		   var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
		    while (aItem = items.shift()) {
		        var item = JSON.parse(aItem);
		        if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
		        else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
		        else caseInsensitive.push(JSON.stringify(item));
		    }
		    return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
		    var item = JSON.parse(obj);
		    var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
		    return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
		        return '<strong>' + match + '</strong>'
		    })
		},
		updater: function (obj) {
		    var item = JSON.parse(obj);
		    $("#"+storename+"_cstate").val(item.id);
	     	return item.name;
		}
	});
}

function fn_storeCityDetails(storename){
	$("#"+storename+"_selectcity").typeahead({source: function (query, process) {
		$("#"+storename+"_cselectcity").val('');
	    return $.ajax({
	        url: "${pageContext.request.contextPath}/merchantsales/cityDetails.action?countrycode="+document.getElementById(storename+"_ccountry").value+"&statecode="+document.getElementById(storename+"_cstate").value,
	        type: 'post',
	        data: { query: query },
	        dataType: 'json',
	        headers: {
				'X-CSRF-Token': document.getElementById("_csrf").value
			},
	        success: function (result) {
	       			var resultList = result.map(function (item) {
	        		var aItem = { id: item.value, name: item.desc};
	            	return JSON.stringify(aItem);
				});
	        	return process(resultList);
	        }
	    });
	},
	matcher: function (obj) {
	    var item = JSON.parse(obj);
	    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
	},
	sorter: function (items) {          
	   var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
	    while (aItem = items.shift()) {
	        var item = JSON.parse(aItem);
	        if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
	        else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
	        else caseInsensitive.push(JSON.stringify(item));
	    }
	    return beginswith.concat(caseSensitive, caseInsensitive)
	},
	highlighter: function (obj) {
	    var item = JSON.parse(obj);
	    var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	    return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
	        return '<strong>' + match + '</strong>'
	    })
	},
	updater: function (obj) {
	    var item = JSON.parse(obj);
	    $("#"+storename+"_cselectcity").val(item.id);
	 	return item.name;
	}
	});
}

function showRiskDetail(val, storename){
	if(val.value == 1){
		$('#'+storename+'_riskLabel').show(); $('#'+storename+'_riskValue').show();
	}else{
		$('#'+storename+'_riskLabel').hide(); $('#'+storename+'_riskValue').hide();
	}
}

function getServiceFeePlan(){
	var feePlan = $("input:radio[name='merchantOnboardBean.feePlanType']:checked").val();
	var action = "serviceFeePlanInstMercOnboard.action?feePlan="+feePlan;
	loadDataForAjaxSelectBox(action,'merchantservicefeeplan',false);
}

function getStoreServiceFeePlan(storename, tempStoreCount){
	$("#"+storename+"_storeservicefeeplan").selectmenu({ change : function(){} });
	var feePlan = $("input:radio[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].storeFeePlanType']:checked").val();
	var action = "serviceFeePlanInstMercOnboard.action?feePlan="+feePlan;
	loadDataForAjaxSelectBox(action,storename+'_storeservicefeeplan',false);
}

function getTerminalServiceFeePlan(termname, tempStoreCount, tempTermCount){
	$("#"+termname+"_terminalservicefeeplan").selectmenu({ change : function(){} });
	var feePlan = $("input:radio[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].terminalFeePlanType']:checked").val();
	var action = "serviceFeePlanInstMercOnboard.action?feePlan="+feePlan;
	loadDataForAjaxSelectBox(action,termname+'_terminalservicefeeplan',false);
}

function AllowAlphabeticWithSpace(){
 	 if(  (event.keyCode > 96 && event.keyCode < 123 )  ||  ( event.keyCode >= 65 && event.keyCode <= 90 ) || event.keyCode == 32 ){
		event.returnValue = true; 
     } else {
         event.returnValue = false;
     }
 }
 
$(document).ready(function() {
	$('#state option,#cstate option').prop('selected', function() {
	    return this.defaultSelected;
	});
		
	$("#bankersalesNames").typeahead({
	    source: function (query, process) {
	    	$("#bankerSalesSqid").val('');
	        return $.ajax({
	            url: "${pageContext.request.contextPath}/merchantsales/BankersalesNamesautoComplete.action?source="+$("#selectSource").val()+"&q="+$("#bankersalesNames").val(),
	            type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.label};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },
		matcher: function (obj) {
		   var item = JSON.parse(obj);
		   return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
		sorter: function (items) {          
		   var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
		   while (aItem = items.shift()) {
		       var item = JSON.parse(aItem);
		       if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
		       else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
		       else caseInsensitive.push(JSON.stringify(item));
		   }
	       return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
	            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById("bankerSalesSqid").value = item.id;
           	return item.name;
	    }
	});
});

$(document).ready(function() {
	$("#state").typeahead({
		source: function (query, process) {
			$("#cstate").val('');
	        return $.ajax({
	            url: "${pageContext.request.contextPath}/merchantsales/stateDetails.action?countrycode="+document.getElementById("ccountry").value,
	            type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },

		matcher: function (obj) {
		    var item = JSON.parse(obj);
		    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
	    sorter: function (items) {          
	        var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
	        while (aItem = items.shift()) {
	        var item = JSON.parse(aItem);
	        if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
	        else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
	        else caseInsensitive.push(JSON.stringify(item));
		    }
		 	return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById("cstate").value = item.id;
         	return item.name;
	    }
	});
});

$(document).ready(function() {
	$("#selectcity").typeahead({
	    source: function (query, process) {
	    	$("#cselectcity").val('');
	        return $.ajax({
	            url: "${pageContext.request.contextPath}/merchantsales/cityDetails.action?countrycode="+document.getElementById("ccountry").value+"&statecode="+document.getElementById("cstate").value,
	            type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },

		matcher: function (obj) {
		    var item = JSON.parse(obj);
		    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
	    sorter: function (items) {          
            var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
	        while (aItem = items.shift()) {
	        var item = JSON.parse(aItem);
	        if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
	        else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
	        else caseInsensitive.push(JSON.stringify(item));
	        }
	        return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById("cselectcity").value = item.id;
         	return item.name;
	    }
	});
});

function fn_merchantMccAutocomplete() {
	$("#mccmerchantautocomplete").typeahead({
	    source: function (query, process) {
	    	$("#cmccMerchantAutoComplete").val('');
	        return $.ajax({
	        	url: "${pageContext.request.contextPath}/merchantsales/mccautocomplete.action",
	        	type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },

		matcher: function (obj) {
		   var item = JSON.parse(obj);
		   return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
		sorter: function (items) {          
		   var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
		   while (aItem = items.shift()) {
		   var item = JSON.parse(aItem);
		   if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
		   else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
		   else caseInsensitive.push(JSON.stringify(item));
		   }
           return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById("cmccMerchantAutoComplete").value = item.id;
	        /*var storeCount = document.getElementById('storeCount').value;
	    	for(var i=1; i<=storeCount; i++){
	    		var storename = "STORE"+i; var tempStoreCount = i-1;
	    		if (document.getElementById(storename+"_cmccAutoComplete")) {
					document.getElementById(storename+"_cmccAutoComplete").value = item.id;
				}
				if (document.getElementById(storename+"_mccautocomplete")) {
					document.getElementById(storename+"_mccautocomplete").value = item.name;
				}
	    	}
			var termCount = document.getElementById(storename+'_termCount').value;
			for(var j=1; j<=termCount; j++){
				var termname = storename+"_TERM"+j;var tempTermCount = j-1;
				if (document.getElementById(termname+"_cterminalmccautoComplete")) {
					document.getElementById(termname+"_cterminalmccautoComplete").value = item.id;
				}
				if (document.getElementById(termname+"_terminalmccautoComplete")) {
					document.getElementById(termname+"_terminalmccautoComplete").value = item.name;
				}
			}*/
	        return item.name;
	    }
	});
}

function fn_storeMccAutocomplete(storename) {
	$("#"+storename+"_mccautocomplete").typeahead({
	    source: function (query, process) {
	    	$("#"+storename+"_cmccAutoComplete").val('');
	        return $.ajax({
	        	url: "${pageContext.request.contextPath}/merchantsales/mccautocomplete.action",
	        	type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },

		matcher: function (obj) {
		   var item = JSON.parse(obj);
		   return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
		sorter: function (items) {          
		   var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
		   while (aItem = items.shift()) {
		   var item = JSON.parse(aItem);
		   if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
		   else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
		   else caseInsensitive.push(JSON.stringify(item));
		   }
           return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById(storename+"_cmccAutoComplete").value = item.id;
			/*var termCount = document.getElementById(storename+'_termCount').value;
			for(var j=1; j<=termCount; j++){
				var termname = storename+"_TERM"+j;var tempTermCount = j-1;
				if (document.getElementById(termname+"_cterminalmccautoComplete")) {
					document.getElementById(termname+"_cterminalmccautoComplete").value = item.id;
				}
				if (document.getElementById(termname+"_terminalmccautoComplete")) {
					document.getElementById(termname+"_terminalmccautoComplete").value = item.name;
				}
			}*/
	        return item.name;
	    }
	});
}

function fn_terminalMccAutocomplete(termname) {
	$("#"+termname+"_terminalmccautocomplete").typeahead({
	    source: function (query, process) {
	    	$("#"+termname+"_cterminalmccautoComplete").val('');
	        return $.ajax({
	        	url: "${pageContext.request.contextPath}/merchantsales/mccautocomplete.action",
	        	type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },

		matcher: function (obj) {
		   var item = JSON.parse(obj);
		   return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
		sorter: function (items) {          
		   var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
		   while (aItem = items.shift()) {
		   var item = JSON.parse(aItem);
		   if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
		   else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
		   else caseInsensitive.push(JSON.stringify(item));
		   }
           return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById(termname+"_cterminalmccautoComplete").value = item.id;
	        return item.name;
	    }
	});
}

function fn_RcrngChrgePln(storename) {		
	$("#"+storename+"_dftTrmlRcrngChrgePlnIdname").typeahead({
	    source: function (query, process) {
	    	$("#"+storename+"_RcrngChrgePlnId").val('');
	        return $.ajax({
	            url: "${pageContext.request.contextPath}/merchantsales/dftTrmlRcrngChrgePlan.action",
	            type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },

		matcher: function (obj) {
		    var item = JSON.parse(obj);
		    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
	    sorter: function (items) {          
            var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
	        while (aItem = items.shift()) {
	           var item = JSON.parse(aItem);
	           if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
	           else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
	           else caseInsensitive.push(JSON.stringify(item));
	        }
	        return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById(storename+"_RcrngChrgePlnId").value = item.id;
         	return item.name;
	    }
	});
}

$(document).ready(function() {
	$("#mercAggr").typeahead({
	    source: function (query, process) {
	    	$("#mercAggrID").val('');
	        return $.ajax({
	            url: "${pageContext.request.contextPath}/merchantsales/aggregatorDetails.action",
	            type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc, relm: item.relm};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },

		matcher: function (obj) {
		   	var item = JSON.parse(obj);
		    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
		sorter: function (items) { 
		    var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
		    while (aItem = items.shift()) {
		       var item = JSON.parse(aItem);
		       if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
		       else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
		       else caseInsensitive.push(JSON.stringify(item));
		 	}
	        return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById("mercAggr").value = item.name;
	        document.getElementById("mercAggrID").value = item.id;
	        document.getElementById("mercAggrrelm").value = item.relm;
	        if(item.id){
	        	$("#merchantPortalRequired1").hide();
    			$("#merchantPortalRequired2").hide();
    			$('input:radio[name="merchantOnboardBean.merchantPortalRequired"]').filter('[value=0]').prop('checked',true);		        	
	        }else{
    			$("#merchantPortalRequired1").show();
    			$("#merchantPortalRequired2").show();
    			$('input:radio[name="merchantOnboardBean.merchantPortalRequired"]').filter('[value=0]').prop('checked',true);
	        }
	        document.getElementById("acctErrorMsg").innerHTML="";
			$("#accountdetailstag").show();
			$('input:radio[name="merchantOnboardBean.acctMapLvl"]').filter('[value=1]').prop('checked',true);
			fn_enableAccountDetails();
			fn_updaterelationShipManager(item.relm);
         	return item.name;
	    }
	});
});

function fn_updaterelationShipManager(val)
{
	if (val === undefined) {
		val='';
	}
	document.getElementById("mercAggrrelm").value = val;
	var storeCount = document.getElementById('storeCount').value;
	for(var i=1; i<=storeCount; i++){
		var storename = "STORE"+i; var tempStoreCount = i-1;
		var selectElement = document.querySelector(`select[name="merchantOnboardBean.lstStoreBean[`+tempStoreCount+`].relatonShipManager"]`);
	    if (selectElement) {
	    	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].relatonShipManager']").selectmenu();
	    	$("select[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].relatonShipManager']").val(val).selectmenu('refresh');
	    }
	}
}

function getAjaxData(action){
  var funcData ="";
	 $.ajax({
		  	type :'GET',
		  	url : action,
			cache :false,
			async: false,
			headers: {
				'X-CSRF-Token': document.getElementById("_csrf").value
			},
			error : function(error) {
			},
			success: function(data) {
				funcData = data;
			}
		});
	 return funcData;
}

function changeMercAggre(){
	var mercAggr = document.getElementById("mercAggr").value;
	if(!mercAggr){
		document.getElementById("mercAggrID").value = "";
		document.getElementById("mercAggrrelm").value = "";
		fn_updaterelationShipManager('');
		$("#merchantPortalRequired1").show();
		$("#merchantPortalRequired2").show();
		$('input:radio[name="merchantOnboardBean.merchantPortalRequired"]').filter('[value=0]').prop('checked',true);
	}
	if(document.getElementById("mercAggrID").value.trim().length != 0){
		fn_updaterelationShipManager(document.getElementById("mercAggrrelm").value);
		$("#merchantPortalRequired1").hide();
		$("#merchantPortalRequired2").hide();
		$('input:radio[name="merchantOnboardBean.merchantPortalRequired"]').filter('[value=0]').prop('checked',true);
	}else{
		$("#merchantPortalRequired1").show();
		$("#merchantPortalRequired2").show();
		$('input:radio[name="merchantOnboardBean.merchantPortalRequired"]').filter('[value=0]').prop('checked',true);
	}
	
	document.getElementById("acctErrorMsg").innerHTML="";
	$("#accountdetailstag").show();
	$('input:radio[name="merchantOnboardBean.acctMapLvl"]').filter('[value=1]').prop('checked',true);
	$("#merchantAccountRequired1").hide();
	$("#merchantAccountRequired2").hide();
	$('input:radio[name="merchantOnboardBean.merchantAccountRequired"]').filter('[value=1]').prop('checked',true);
	fn_enableAccountDetails();
}

function fn_useSameAsAggrAccountForMerchant(val)
{
	if (val.value != 0)
	{
		document.getElementById("acctErrorMsg").innerHTML="";
		$("#accountdetailstag").show();
		$("#merchantAccountRequired1").hide();
		$("#merchantAccountRequired2").hide();
		$('input:radio[name="merchantOnboardBean.merchantAccountRequired"]').filter('[value=1]').prop('checked',true);
		fn_enableAccountDetails();
	}else {
		var mercAggrID = document.getElementById("mercAggrID").value;
		if(!mercAggrID){
			alert("Select Merchant Aggergator");
			document.getElementById("acctErrorMsg").innerHTML="";
			$("#accountdetailstag").show();
			$("#merchantAccountRequired1").hide();
    		$("#merchantAccountRequired2").hide();
    		$('input:radio[name="merchantOnboardBean.merchantAccountRequired"]').filter('[value=1]').prop('checked',true);
			$('input:radio[name="merchantOnboardBean.acctMapLvl"]').filter('[value=1]').prop('checked',true);
			fn_enableAccountDetails();
		}else {
		    $.ajax({
	        	url: "${pageContext.request.contextPath}/merchantsales/checkAggrAcctDetails.action?mercAggrID="+mercAggrID,
	            type: 'post',
		        dataType: 'text',
		        headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
		        success: function(data) {
		        	if(data!=0){
		        		$("#accountdetailstag").hide();
		        		//fn_disableAccountDetails();
		        		$("#merchantAccountRequired1").show();
		        		$("#merchantAccountRequired2").show();
		        		$('input:radio[name="merchantOnboardBean.merchantAccountRequired"]').filter('[value=0]').prop('checked',true);
		        	}else{
		        		document.getElementById("acctErrorMsg").innerHTML="Account Details are not available for the selected Merchant Aggregator";
		    			$("#accountdetailstag").show();
		    			$("#merchantAccountRequired1").hide();
		        		$("#merchantAccountRequired2").hide();
		        		$('input:radio[name="merchantOnboardBean.merchantAccountRequired"]').filter('[value=1]').prop('checked',true);
						$('input:radio[name="merchantOnboardBean.acctMapLvl"]').filter('[value=1]').prop('checked',true);
						fn_enableAccountDetails();
		        	}
		        },
		        error: function() {
		        	document.getElementById("acctErrorMsg").innerHTML="";
					$("#accountdetailstag").show();
					$("#merchantAccountRequired1").hide();
	        		$("#merchantAccountRequired2").hide();
	        		$('input:radio[name="merchantOnboardBean.merchantAccountRequired"]').filter('[value=1]').prop('checked',true);
					$('input:radio[name="merchantOnboardBean.acctMapLvl"]').filter('[value=1]').prop('checked',true);
					fn_enableAccountDetails();
		        }
		    });
		}
	}
}	

function fn_merchantAccountRequired(val) {
	if (val.value != 0)
	{
		$('input:radio[name="merchantOnboardBean.merchantAccountRequired"]').filter('[value=1]').prop('checked',true);
		$("#accountdetailstag").show();
	}else {
		$('input:radio[name="merchantOnboardBean.merchantAccountRequired"]').filter('[value=0]').prop('checked',true);
		$("#accountdetailstag").hide();
	}
}

function openWindowAttachmentsForStore(storename, tempStoreCount, strTempFoldername, refno){
	tempStoreCount = parseInt(tempStoreCount);
	tempStoreCount = tempStoreCount+1;
	var tempFolderName = strTempFoldername+"0002"+tempStoreCount;
	document.getElementById(storename+'_tempFolderName').value = tempFolderName;
	if(refno) {
		var action='<%=request.getContextPath()%>/merchantsales/addMerchantAttachmentsDetails.action?storeRefNum='+tempFolderName+'&entityRefNum='+refno+'&entitySeqId=2&requestSeqId=MSPR';
	} else {
		var action='<%=request.getContextPath()%>/merchantsales/addMerchantAttachmentsDetails.action?storeRefNum='+tempFolderName+'&entityRefNum='+strTempFoldername+'&entitySeqId=2&requestSeqId=MSPR';
	}
 	newwindow = window.open(action,'mywindow','top=50,left=20,scrollbars=1,width=1200,height=500');
 	
 	newwindow.onload = () => { newwindow.document.querySelectorAll('input[type="file"]').forEach((input) => { input.style.display = 'none'; const wrapper = newwindow.document.createElement('div'); wrapper.className = 'file-upload'; const button = newwindow.document.createElement('button'); button.type = 'button'; button.className = 'choose-file-btn'; button.textContent = 'Choose File'; const fileNameSpan = newwindow.document.createElement('span'); fileNameSpan.className = 'file-selected'; fileNameSpan.textContent = 'No Files Selected'; input.parentNode.insertBefore(wrapper, input); wrapper.appendChild(button); wrapper.appendChild(fileNameSpan); wrapper.appendChild(input); button.addEventListener('click', () => input.click()); input.addEventListener('change', () => { fileNameSpan.textContent = input.files[0]?.name || 'No Files Selected'; }); }); };
 	
	if(window.focus){
		newwindow.focus();
	}
	return false;
}

function fn_MerchantAcctType(val) {
	if (val == '1') {
        $("#acctBranch").show();
        $("#acctBranchDetails").show();
        $("#bankName").hide();
        $("#bankNameText").hide();
        $("#branchName").hide();
        $("#branchNameText").hide();
    } else if (val == '0') {
        $("#acctBranch").hide();
        $("#acctBranchDetails").hide();
        $("#bankName").show();
        $("#bankNameText").show();
        $("#branchName").show();
        $("#branchNameText").show();
    } else {
        $("#acctBranch").hide();
        $("#acctBranchDetails").hide();
        $("#bankName").hide();
        $("#bankNameText").hide();
        $("#branchName").hide();
        $("#branchNameText").hide();
	}
}

function fn_storeAcctWithBank(val, storename){
    if (val.value == '1') {
    	$("#"+storename+"_bankName").hide();
        $("#"+storename+"_bankNameText").hide();
        $("#"+storename+"_branchName").hide();
        $("#"+storename+"_branchNameText").hide();
        $("#"+storename+"_acctBranch").show();
        $("#"+storename+"_acctBranchDetails").show();
    } else if (val.value == '0') {
    	
        $("#"+storename+"_acctBranch").hide();
        $("#"+storename+"_acctBranchDetails").hide();
        $("#"+storename+"_bankName").show();
        $("#"+storename+"_bankNameText").show();
        $("#"+storename+"_branchName").show();
        $("#"+storename+"_branchNameText").show();
    } else {
        $("#"+storename+"_acctBranch").hide();
        $("#"+storename+"_acctBranchDetails").hide();
        $("#"+storename+"_bankName").hide();
        $("#"+storename+"_bankNameText").hide();
        $("#"+storename+"_branchName").hide();
        $("#"+storename+"_branchNameText").hide();
	}
}
	
function fn_termAcctWithBank(val, termname){
    if (val.value == '1') {
        $("#"+termname+"_acctBranch").show();
        $("#"+termname+"_acctBranchDetails").show();
        $("#"+termname+"_bankName").hide();
        $("#"+termname+"_bankNameText").hide();
        $("#"+termname+"_branchName").hide();
        $("#"+termname+"_branchNameText").hide();
    } else if (val.value == '0') {
        $("#"+termname+"_acctBranch").hide();
        $("#"+termname+"_acctBranchDetails").hide();
        $("#"+termname+"_bankName").show();
        $("#"+termname+"_bankNameText").show();
        $("#"+termname+"_branchName").show();
        $("#"+termname+"_branchNameText").show();
    } else {
        $("#"+termname+"_acctBranch").hide();
        $("#"+termname+"_acctBranchDetails").hide();
        $("#"+termname+"_bankName").hide();
        $("#"+termname+"_bankNameText").hide();
        $("#"+termname+"_branchName").hide();
        $("#"+termname+"_branchNameText").hide();
	}
}

function fn_disableAccountDetails()
{
	var storeCount = document.getElementById('storeCount').value;
	for(var i=1; i<=storeCount; i++){
		var storename = "STORE"+i; var tempStoreCount = i-1;
		var elementverify = document.getElementById(storename+"_accountDetails");
		if (elementverify) {
			$('input:radio[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].useSameAsMercAccountForStore"]').filter('[value=0]').prop('checked',true);
			$('input[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].useSameAsMercAccountForStore"]').attr('disabled', true);
			$("#"+storename+"_AccountModalLink").hide();
			
			var termCount = document.getElementById(storename+'_termCount').value;
			for(var j=1; j<=termCount; j++){
				var termname = "STORE"+i+"_TERM"+j;var tempTermCount = j-1;
				$('input:radio[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].lstTerminalBean['+tempTermCount+'].useSameAsStoreAccountForTerm"]').filter('[value=0]').prop('checked',true);
				$('input[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].lstTerminalBean['+tempTermCount+'].useSameAsStoreAccountForTerm"]').attr('disabled', true);
				$("#"+termname+"_AccountModalLink").hide();
			}
		}
	}
}

function fn_enableAccountDetails()
{
	var storeCount = document.getElementById('storeCount').value;
	for(var i=1; i<=storeCount; i++){
		var storename = "STORE"+i; var tempStoreCount = i-1;
		var elementverify = document.getElementById(storename+"_accountDetails");
		if (elementverify) {
			$('input[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].useSameAsMercAccountForStore"]').attr('disabled', false);
			
			var termCount = document.getElementById(storename+'_termCount').value;
			for(var j=1; j<=termCount; j++){
				var tempTermCount = j-1;
				$('input[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].lstTerminalBean['+tempTermCount+'].useSameAsStoreAccountForTerm"]').attr('disabled', false);
			}
		}
	}
}

$(document).ready(function () {
	$('select[id$="_prfDocsProv"]').each(function () {
		var selectId = $(this).attr('id');
		
		if (selectId && selectId.includes("_prfDocsProv")) {
			var storename = selectId.replace("_prfDocsProv", "");
			var select = $(this);
			var docLinkRow = $('#'+storename+'_docLinkRow');

			if (select[0].options.length === 1) {
				if (select[0].options[0].text.trim().toLowerCase() === 'no document') {
					docLinkRow.hide();
				} else {
					docLinkRow.show();
				}
			}
		}
	});
});

$(document).ready(function() {
	$(".select_styled").selectmenu({
		change : function(){	
			var selectId = $(this).attr('id');
			if(selectId && selectId.includes("_natureOfBusiness")) {
	        	fn_getPrfDocsProvDetailsUsingNOB(selectId.replace("_natureOfBusiness", ""));
	        }
			else if(selectId && selectId.includes("_paymentFrecForCard")) {
	        	fn_getPaymentFrecForCard(selectId.replace("_paymentFrecForCard", ""));
	        }
			else if(selectId && selectId.includes("_paymentFrecForAaniPay")) {
	        	fn_getPaymentFrecForAaniPay(selectId.replace("_paymentFrecForAaniPay", ""));
	        }
			else if(selectId && selectId.includes("_storeacctWithBank")) {
				fn_storeAcctWithBank(this, selectId.replace("_storeacctWithBank", ""));
	        }
			else if(selectId && selectId.includes("_terminalacctWithBank")) {
	        	fn_termAcctWithBank(this, selectId.replace("_terminalacctWithBank", ""));
	        }
			else if(selectId && selectId.includes("acctType")) {
				fn_MerchantAcctType(this.value);
	        }
		}
	});
	$("select[id$='merchantEntityName']").each(function() {
        fn_merchantEntityValue();
    });
});

function fn_getPrfDocsProvDetailsUsingNOB(storename){
	var temp = document.getElementById(storename+"_natureOfBusiness").value;
	var status = temp.split("~")[0];
	var value = temp.split("~")[1];
	if(status == 0) {
	   	alert("Merchant not met eligibility criteria to onboard..");
	}
	$.ajax({
		url: 'proofOfDocumentList.action?natureOfBusinessID='+value, 
		method: 'POST',
		dataType: 'text',
		headers: {
			'X-CSRF-Token': document.getElementById("_csrf").value
		},
		success: function(data) {
			document.getElementById(storename+'_prfDocsProvValue').value = data.split("~")[0];
			document.getElementById(storename+'_prfDocsProv').innerHTML = data.split("~")[1];

			var select = document.getElementById(storename+'_prfDocsProv');
			var docLinkTd = $('#'+storename+'_docLinkRow');

			if (select && docLinkTd) {
				let onlyOption = select.options.length === 1 ? select.options[0].text.trim().toLowerCase() : "";
				if (onlyOption === "no document") {
					docLinkTd.hide();
				} else {
					docLinkTd.show();
				}
			}
		},
		error: function() {
		    alert('Error occurred while fetching state information.');
		}
	});
}

function fn_getPaymentFrecForCard(storename){
	var val = document.getElementById(storename+"_paymentFrecForCard").value;
	if(val == 5) {
		$("#"+storename+"_paymentFrecForCard1").show();
		$("#"+storename+"_paymentFrecForCard2").show();
	} else {
		$("#"+storename+"_paymentFrecForCard1").hide();
		$("#"+storename+"_paymentFrecForCard2").hide();
	}
}

function fn_aaniPayTransPayment_Update(val, storename, tempStoreCount){
	if(val.value == 0) {
		$("#"+storename+"_aaniPayTransPayment").show();
	} else {
		$("#"+storename+"_aaniPayTransPayment").hide();
	}
}

function fn_getPaymentFrecForAaniPay(storename){
	var val = document.getElementById(storename+"_paymentFrecForAaniPay").value;
	if(val == 5) {
		$("#"+storename+"_paymentFrecForAaniPay1").show();
		$("#"+storename+"_paymentFrecForAaniPay2").show();
	} else {
		$("#"+storename+"_paymentFrecForAaniPay1").hide();
		$("#"+storename+"_paymentFrecForAaniPay2").hide();
	}
}

function uploadfile(){
	var filename = document.getElementById("fileUpload");
	var uploadmerclogofilename = document.getElementById("fileUpload").value.replace("C:\\fakepath\\", "");
	document.getElementById("uploadMercLogoFileName").value = uploadmerclogofilename;
	
	if(Math.round(filename.files.item(0).size / 1024) >= 256 ){
		alert("File size must be less than 256KB.");
		filename.value = '';
		return false;
	}
	
	var filenameArray = (filename.files.item(0).name).split(".");
	var count = 0;
	var fileformat = ["JPG", "PNG"];
	for(var i=0; i<filenameArray.length;i++){
		for(var j=0; j<fileformat.length;j++){
			if(filenameArray[i].toUpperCase()==fileformat[j].toUpperCase()){count++;}
		}
	}
	if(count != 1){
		alert("File is invalid format.");
		filename.value = '';
		return false;
	}
}

function fn_RRFdetailsForTerm(val, termname, tempStoreCount, tempTermCount){
	if(val.value == 1)
	{
		$("#"+termname+"_RRFModalLink").show();
	} else {
		$("#"+termname+"_RRFModalLink").hide();
	}
}
	
function fn_SDRdetailsForTerm(val, termname, tempStoreCount, tempTermCount){
	if(val.value == 1)
	{
		$("#"+termname+"_SDRModalLink").show();
	} else {
		$("#"+termname+"_SDRModalLink").hide();
	}
}

/* function fn_SDRdetailsForTerm(val){
	if(val.value == 1)
	{
		//$("#SDRModalLink").show();
		$("#SDRdetails_Row0").show();
		$("#SDRdetails_Row1").show();
		//$("#SDRdetails_Row2").show();
	} else {
		//$("#SDRModalLink").hide();
		$("#SDRdetails_Row0").show();
		$("#SDRdetails_Row1").hide();
		//$("#SDRdetails_Row2").hide();
	}
} */

function fn_SDRdetailsForTerm(val) {
    if (val.value == 1) {
        $("#SDRdetails_Row0").show();
        $("#SDRdetails_Row1").show();
        $("#SDRdetails_Row2").show();

        var sdrIndcValue = $("input[name='merchantOnboardBean.sdrIndc']:checked").val();
        if (sdrIndcValue != null && sdrIndcValue != undefined) {
            sdrFieldsEnable({ value: sdrIndcValue });
        }

    } else {
        $("#SDRdetails_Row0").show();
        $("#SDRdetails_Row1").hide();
        $("#SDRdetails_Row2").hide();
    }
}

function rrfFieldsEnable(val, termname, tempStoreCount, tempTermCount){
	tempStoreCount = parseInt(tempStoreCount);
    tempTermCount = parseInt(tempTermCount);
	if($('input[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].lstTerminalBean['+tempTermCount+'].rrfIndc"]:checked').val() == '1'){					
		$("#"+termname+"_RRFdiv1").show();
		$("#"+termname+"_RRFdiv3").show();
		$("#"+termname+"_RRFdiv5").show();
		$("#"+termname+"_RRFdiv2").hide();
		$("#"+termname+"_RRFdiv4").hide();
		$("#"+termname+"_RRFdiv6").hide();
		
		$("#"+termname+"_RRFdiv7").hide();
		$("#"+termname+"_RRFdiv9").hide();
		$("#"+termname+"_RRFdiv12").hide();
		$("#"+termname+"_RRFdiv8").show();
		$("#"+termname+"_RRFdiv10").show();
		$("#"+termname+"_RRFdiv11").show();
		
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domestic']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].interNational']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticHoldingDays']").attr('readonly', false);
		
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticFixedAmount']").attr('readonly', true);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalFixedAmount']").attr('readonly', true);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalHoldingDays']").attr('readonly', false);
		
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticFixedAmount']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalFixedAmount']").val("");
		 
	}else if($('input[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].lstTerminalBean['+tempTermCount+'].rrfIndc"]:checked').val() == '2'){
		$("#"+termname+"_RRFdiv1").hide();
		$("#"+termname+"_RRFdiv3").hide();
		$("#"+termname+"_RRFdiv6").hide();
		$("#"+termname+"_RRFdiv2").show();
		$("#"+termname+"_RRFdiv4").show();
		$("#"+termname+"_RRFdiv5").show();
		
		$("#"+termname+"_RRFdiv7").show();
		$("#"+termname+"_RRFdiv9").show();
		$("#"+termname+"_RRFdiv11").show();
		$("#"+termname+"_RRFdiv8").hide();
		$("#"+termname+"_RRFdiv10").hide();
		$("#"+termname+"_RRFdiv12").hide();
		 
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticFixedAmount']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalFixedAmount']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalHoldingDays']").attr('readonly', false);
		
		 $("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticHoldingDays']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domestic']").attr('readonly', true);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].interNational']").attr('readonly', true);
		
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domestic']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].interNational']").val("");
		
	}else if($('input[name="merchantOnboardBean.lstStoreBean['+tempStoreCount+'].lstTerminalBean['+tempTermCount+'].rrfIndc"]:checked').val() == '3'){
		$("#"+termname+"_RRFdiv1").show();
		$("#"+termname+"_RRFdiv3").show();
		$("#"+termname+"_RRFdiv5").show();
		$("#"+termname+"_RRFdiv2").hide();
		$("#"+termname+"_RRFdiv4").hide();
		$("#"+termname+"_RRFdiv6").hide();
		
		$("#"+termname+"_RRFdiv7").show();
		$("#"+termname+"_RRFdiv9").show();
		$("#"+termname+"_RRFdiv11").show();
		$("#"+termname+"_RRFdiv8").hide();
		$("#"+termname+"_RRFdiv10").hide();
		$("#"+termname+"_RRFdiv12").hide();
		 
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domestic']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].interNational']").attr('readonly', false);
	    $("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticHoldingDays']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalHoldingDays']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticFixedAmount']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalFixedAmount']").attr('readonly', false);
	}else{
		$("#"+termname+"_RRFdiv1").hide();
		$("#"+termname+"_RRFdiv3").hide();
		$("#"+termname+"_RRFdiv5").hide();
		$("#"+termname+"_RRFdiv2").show();
		$("#"+termname+"_RRFdiv4").show();
		$("#"+termname+"_RRFdiv6").show();
		
		$("#"+termname+"_RRFdiv7").hide();
		$("#"+termname+"_RRFdiv9").hide();
		$("#"+termname+"_RRFdiv11").hide();
		$("#"+termname+"_RRFdiv8").show();
		$("#"+termname+"_RRFdiv10").show();
		$("#"+termname+"_RRFdiv12").show();

		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domestic']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].interNational']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticHoldingDays']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalHoldingDays']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticFixedAmount']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalFixedAmount']").val("");

		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domestic']").attr('readonly', true);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].interNational']").attr('readonly', true);
	    $("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticHoldingDays']").attr('readonly', true);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalHoldingDays']").attr('readonly', true);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].domesticFixedAmount']").attr('readonly', true);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].internationalFixedAmount']").attr('readonly', true);
	}
}

function sdrFieldsEnable(val, termname, tempStoreCount, tempTermCount){
	tempStoreCount = parseInt(tempStoreCount);
    tempTermCount = parseInt(tempTermCount);
    if(val.value == 1){	
		$("#"+termname+"_SDRdiv1").show();
		$("#"+termname+"_SDRdiv3").show();
		$("#"+termname+"_SDRdiv2").hide();
		$("#"+termname+"_SDRdiv4").hide();
		
		//$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].securityDepositRecoveryAmount']").attr('readonly', false);
		//$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].securityDepositRecoveryDays']").attr('readonly', false);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].securityDepositRecoveryAmount']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].securityDepositRecoveryDays']").val("");
		 
	}else{
		$("#"+termname+"_SDRdiv1").hide();
		$("#"+termname+"_SDRdiv3").hide();
		$("#"+termname+"_SDRdiv2").show();
		$("#"+termname+"_SDRdiv4").show();
		 
		//$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].securityDepositRecoveryAmount']").attr('readonly', true);
		//$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].securityDepositRecoveryDays']").attr('readonly', true);
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].securityDepositRecoveryAmount']").val("");
		$("input[name='merchantOnboardBean.lstStoreBean["+tempStoreCount+"].lstTerminalBean["+tempTermCount+"].securityDepositRecoveryDays']").val("");
	}				
}

function sdrFieldsEnable(val) {
    if (val.value == 1) {
        $("#SDRdiv1").show();
        $("#SDRdiv2").hide();
        $("#SDRdiv3").show();
        $("#SDRdiv4").hide();
        
        $("input[name='merchantOnboardBean.securityDepositRecoveryAmount']").val("");
    } else {
        $("#SDRdiv1").hide();
        $("#SDRdiv2").show();
        $("#SDRdiv3").hide();
        $("#SDRdiv4").show();
        
        $("input[name='merchantOnboardBean.securityDepositRecoveryPercentage]").val("");
    }
}

function fn_documentValidation()
{
	var remarks = document.getElementById('remarks').value;
	if(remarks.length == 0 || remarks.trim() === ""){
		alert("Enter Remarks");
		return false;
	}
	document.getElementById('submitButton').disabled = true;
	document.getElementById('submitButton').value="Validating";
	var tempStoreCount = 0;
	var storeCount = parseInt(document.getElementById('storeCount').value);
	var i=1;
	var param = "";
	let allStoresValid = true;
	for(i=1; i<=storeCount; i++){
		tempStoreCount = i; 
		if (document.getElementById("STORE"+i+"_details")) {
	    	var len = document.getElementById("STORE"+i+'_prfDocsProvValue').value.split(", ").length;
	    	if(len == 0 || document.getElementById("STORE"+i+'_prfDocsProvValue').value.trim() === ""){
	    		alert("Select Nature of Business Category for Store "+tempStoreCount);
	    		allStoresValid = false;
				break;
	    	}
	    	var tempFolderName = document.getElementById("STORE"+i+'_tempFolderName').value;
	    	var docSelect = document.getElementById("STORE"+i+"_prfDocsProv");
	    	
			if (docSelect && docSelect.options.length > 0 && docSelect.options[0].text !== "No Document"){
		    	if(tempFolderName.length == 0 || tempFolderName.trim() === ""){
		    		alert("Kindly Upload the Proof of Documents for Store "+tempStoreCount);
		    		allStoresValid = false;
					break;
				}
				if(param=="")
				{
					param = ""+i+"%3A"+len+"%3A"+tempFolderName;
				}else{
					param = param+"-"+i+"%3A"+len+"%3A"+tempFolderName;
				}
			}
		}
	}

	if (!allStoresValid) {
		document.getElementById('submitButton').disabled = false;
		document.getElementById('submitButton').value = "Submit";
		return false;
	}
	
	if(param!=="")
	{
		$.ajax({
		   	url: "${pageContext.request.contextPath}/merchantsales/checkDocumentValidation.action?entitySeqId=2&tempFolderName="+param,
		    type: 'post',
		    dataType: 'text',
		    headers: {
				'X-CSRF-Token': document.getElementById("_csrf").value
			},
		    success: function(data) {
				if(data == "0"){
					document.getElementById('submitButton').disabled = true;
					document.getElementById('submitButton').value="Submitting";
					$("#frmAddMerchantOnboardDetails").attr("action","<%=request.getContextPath()%>/merchantsales/addMerchantOnboardPageDetails.action");
					$("#frmAddMerchantOnboardDetails").submit();
					return true;
				} else {
					alert(data);
					document.getElementById('submitButton').disabled = false;
					document.getElementById('submitButton').value="Submit";
					return false;
				}
		    },
		    error: function() {
		    	alert("Error while document validation...");
		    	document.getElementById('submitButton').disabled = false;
				document.getElementById('submitButton').value="Submit";
				return false;
		    }
		});
		return false;
	} else {
		document.getElementById('submitButton').disabled = true;
		document.getElementById('submitButton').value = "Submitting";
		$("#frmAddMerchantOnboardDetails").attr("action", "<%=request.getContextPath()%>/merchantsales/addMerchantOnboardPageDetails.action");
		$("#frmAddMerchantOnboardDetails").submit();
		return true;
	}
}

function calculateQuaterlySaleAmt(storename, tempStoreCount){
	if ((document.getElementById(storename+'_transCountperMonth').value != "" ) || 
			(document.getElementById(storename+'_averageTicketSize').value != "")){
		document.getElementById(storename+'_quarterlySale').value = (document.getElementById(storename+'_transCountperMonth').value)*
			(document.getElementById(storename+'_averageTicketSize').value)*(3);
	}
}

$(document).ready(function() {
	$("#mercAcctBranch").typeahead({
		source: function (query, process) {
			$("#cAcctBranch").val('');
	        return $.ajax({
	            url: "${pageContext.request.contextPath}/merchantsales/acctBranch.action",
	            type: 'post',
	            data: { query: query },
	            dataType: 'json',
	            headers: {
					'X-CSRF-Token': document.getElementById("_csrf").value
				},
	            success: function (result) {
	            	    var resultList = result.map(function (item) {
	            		var aItem = { id: item.value, name: item.desc};
	                    return JSON.stringify(aItem);
					});
	                return process(resultList);
	            }
	        });
	    },

		matcher: function (obj) {
		    var item = JSON.parse(obj);
		    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
		},
	    sorter: function (items) {          
	        var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
	        while (aItem = items.shift()) {
	        var item = JSON.parse(aItem);
	        if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
	        else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
	        else caseInsensitive.push(JSON.stringify(item));
		    }
		 	return beginswith.concat(caseSensitive, caseInsensitive)
		},
		highlighter: function (obj) {
	        var item = JSON.parse(obj);
	        var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	        return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
            return '<strong>' + match + '</strong>'
	        })
	    },
	    updater: function (obj) {
	        var item = JSON.parse(obj);
	        document.getElementById("cAcctBranch").value = item.id;
         	return item.name;
	    }
	});
});

function fn_storeacctBranch(storename){
	$("#"+storename+"_storeacctBranch").typeahead({source: function (query, process) {
		$("#"+storename+"_cStoreacctBranch").val('');
	    return $.ajax({
	        url: "${pageContext.request.contextPath}/merchantsales/acctBranch.action",
	        type: 'post',
	        data: { query: query },
	        dataType: 'json',
	        headers: {
				'X-CSRF-Token': document.getElementById("_csrf").value
			},
	        success: function (result) {
	       			var resultList = result.map(function (item) {
	        		var aItem = { id: item.value, name: item.desc};
	            	return JSON.stringify(aItem);
				});
	        	return process(resultList);
	        }
	    });
	},
	matcher: function (obj) {
	    var item = JSON.parse(obj);
	    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
	},
	sorter: function (items) {          
	   var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
	    while (aItem = items.shift()) {
	        var item = JSON.parse(aItem);
	        if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
	        else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
	        else caseInsensitive.push(JSON.stringify(item));
	    }
	    return beginswith.concat(caseSensitive, caseInsensitive)
	},
	highlighter: function (obj) {
	    var item = JSON.parse(obj);
	    var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	    return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
	        return '<strong>' + match + '</strong>'
	    })
	},
	updater: function (obj) {
	    var item = JSON.parse(obj);
	    $("#"+storename+"_cStoreacctBranch").val(item.id);
	 	return item.name;
	}
	});
}

function fn_terminalacctBranch(termname){
	$("#"+termname+"_terminalacctBranch").typeahead({source: function (query, process) {
		$("#"+termname+"_cTerminalacctBranch").val('');
	    return $.ajax({
	        url: "${pageContext.request.contextPath}/merchantsales/acctBranch.action",
	        type: 'post',
	        data: { query: query },
	        dataType: 'json',
	        headers: {
				'X-CSRF-Token': document.getElementById("_csrf").value
			},
	        success: function (result) {
	       			var resultList = result.map(function (item) {
	        		var aItem = { id: item.value, name: item.desc};
	            	return JSON.stringify(aItem);
				});
	        	return process(resultList);
	        }
	    });
	},
	matcher: function (obj) {
	    var item = JSON.parse(obj);
	    return ~item.name.toLowerCase().indexOf(this.query.toLowerCase())
	},
	sorter: function (items) {          
	   var beginswith = [], caseSensitive = [], caseInsensitive = [], item;
	    while (aItem = items.shift()) {
	        var item = JSON.parse(aItem);
	        if (!item.name.toLowerCase().indexOf(this.query.toLowerCase())) beginswith.push(JSON.stringify(item));
	        else if (~item.name.indexOf(this.query)) caseSensitive.push(JSON.stringify(item));
	        else caseInsensitive.push(JSON.stringify(item));
	    }
	    return beginswith.concat(caseSensitive, caseInsensitive)
	},
	highlighter: function (obj) {
	    var item = JSON.parse(obj);
	    var query = this.query.replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	    return item.name.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
	        return '<strong>' + match + '</strong>'
	    })
	},
	updater: function (obj) {
	    var item = JSON.parse(obj);
	    $("#"+termname+"_cTerminalacctBranch").val(item.id);
	 	return item.name;
	}
	});
}

function toggleOwnerFields(val) {
	if (val.value == 1) {
    	$("#individualNameRow").show();
    	$("#parentEntityRow").hide();
    	$("#uboPctRow").hide();
    } else {
    	$("#individualNameRow").hide();
    	$("#parentEntityRow").show();
    	$("#uboPctRow").show();
    }
}

function fn_merchantEntityValue() {
	var storeCount = document.getElementById('storeCount').value;
	var select = document.getElementById("merchantEntityName");
	//alert("merchantEntityName==="+document.getElementById("merchantEntityName").value);
	//var hidden = document.getElementById("merchantEntityValue");
	//alert("merchantEntityValue=="+document.getElementById("merchantEntityValue").value);
	var selectedValues = Array.from(select.selectedOptions).map(o => o.value);
	//var selectedValues = Array.from(document.querySelectorAll("#merchantEntityName option:checked, #merchantEntityName input:checked")).map(el => el.value);
	//alert("merchantEntityValue (hidden)===" + hidden.value);
    //if (hidden) {
    //    hidden.value = selectedValues.join(",");
    //}
    
    $("[id$='cyberMercID1'], [id$='cyberMercID2'], [id$='cyberMercPass1'], [id$='cyberMercPass2'], [id$='cyberMercKey1'], [id$='cyberMercKey2'], [id$='aaniPayMercID1'], [id$='aaniPayMercID2'], [id$='aaniPayStoreID1'], [id$='aaniPayStoreID2'], [id$='aaniPayTermID1'], [id$='aaniPayTermID2']").hide();
    
    if (selectedValues.includes("5")) {
        $("#STORE1_cyberMercID1, #STORE1_cyberMercID2, #STORE1_cyberMercPass1, #STORE1_cyberMercPass2, #STORE1_cyberMercKey1, #STORE1_cyberMercKey2").show();

        for (var i = 1; i <= storeCount; i++) {
            var storeName = "STORE"+i;
            $("#"+storeName+"_cyberMercID1, #"+storeName+"_cyberMercID2, #"+storeName+"_cyberMercPass1, #"+storeName+"_cyberMercPass2, #"+storeName+"_cyberMercKey1, #"+storeName+"_cyberMercKey2").show();
        }
    }

    if (selectedValues.includes("10")) {
        $("#aaniPayMercID1, #aaniPayMercID2, #STORE1_aaniPayStoreID1, #STORE1_aaniPayStoreID2, #STORE1_TERM1_aaniPayTermID1, #STORE1_TERM1_aaniPayTermID2").show();

        for (var i = 1; i <= storeCount; i++) {
            var storeName = "STORE"+i;
            $("#aaniPayMercID1, #aaniPayMercID2, #"+storeName+"_aaniPayStoreID1, #"+storeName+"_aaniPayStoreID2").show();
            var termCount = document.getElementById(storeName+"_termCount").value;
            for (var j = 1; j <= termCount; j++) {
                var termName = storeName+"_TERM"+j;
                $("#aaniPayMercID1, #aaniPayMercID2, #"+storeName+"_aaniPayStoreID1, #"+storeName+"_aaniPayStoreID2, #"+termName+"_aaniPayTermID1, #"+termName+"_aaniPayTermID2").show();
            }
        }
    }
    fn_toggleIsTerminalForAaniPay(selectedValues, storeCount);
}

function fn_toggleIsTerminalForAaniPay(selectedValues, storeCount) {
	var aaniPaySelected = selectedValues.includes("10");
	for (var i = 1; i <= storeCount; i++) {
		var storeName = "STORE"+i;
		var chk = document.getElementById(storeName+"_termneeded");
		if (!chk) { continue; }
		var chkTd = $(chk).closest("td");
		var lblTd = chkTd.prev("td");
		if (aaniPaySelected) {
			lblTd.hide();
			chkTd.hide();
			if (chk.checked) { chk.checked = false; }
			$('#'+storeName+'_terminalDetails').hide();
			var termTable = document.getElementById(storeName+'_terminalTable');
			if (termTable) {
				while (termTable.rows.length > 0) { termTable.deleteRow(0); }
			}
			if (document.getElementById(storeName+'_termCount')) {
				document.getElementById(storeName+'_termCount').value = 0;
			}
		} else {
			lblTd.show();
			chkTd.show();
		}
	}
}

$(document).ready(function() {
    
    var selectedRadio = $("input[name='merchantOnboardBean.securityDepositRecoveryRequired']:checked");
    
    if (selectedRadio.length > 0) {
        fn_SDRdetailsForTerm(selectedRadio[0]);
    } else {
        $("#SDRdetails_Row1").hide();
        $("#SDRdetails_Row2").hide();
    }
    
    var selectedSdrIndc = $("input[name='merchantOnboardBean.sdrIndc']:checked");
    if (selectedSdrIndc.length > 0) {
        sdrFieldsEnable(selectedSdrIndc[0]);
    }
});

</script>

</head>
<body>
</body>
</html>