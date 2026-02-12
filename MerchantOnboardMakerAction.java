package com.fss.maps.merchantsales.merchantonboard.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.fss.maps.KeyUtility.KeyConfigUtils;
import com.fss.maps.common.bean.EmailBean;
import com.fss.maps.common.bean.KeyValueBean;
import com.fss.maps.common.bean.MapsBaseBean;
import com.fss.maps.common.bean.ParameterBean;
import com.fss.maps.common.bean.SessionBean;
import com.fss.maps.common.exception.MapsGenException;
import com.fss.maps.common.services.MasterListService;
import com.fss.maps.common.services.ParameterValuesService;
import com.fss.maps.common.util.AttachmentMalwareScannerUtil;
import com.fss.maps.common.util.EmailService;
import com.fss.maps.common.util.MAPSUtils;
import com.fss.maps.common.util.logger.LoggerUtil;
import com.fss.maps.keyconfig.bean.KEYConfigBean;
import com.fss.maps.merchantsales.bean.terminal.CardAcceptanceDetailBean;
import com.fss.maps.merchantsales.merchantonboard.bean.AESEncDecUtil;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardBean;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardStoreBean;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardTerminalBean;
import com.fss.maps.merchantsales.merchantonboard.service.MerchantOnboardService;
import com.fss.maps.useradmin.service.UserServiceImpl;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class MerchantOnboardMakerAction extends ActionSupport implements Preparable, ServletRequestAware {
	
	private LoggerUtil logUtil = new LoggerUtil(MerchantOnboardMakerAction.class);
	private Integer userId = null;
	private Integer instId = null;
	private String userGroup = null;
	private String dateFormat = null;
	private HttpServletRequest httpServletRequest; 
	private HttpServletResponse httpServletResponse;
	private MerchantOnboardBean merchantOnboardBean = new MerchantOnboardBean();
	private MerchantOnboardStoreBean merchantOnboardStoreBean = new MerchantOnboardStoreBean();
	private MerchantOnboardTerminalBean merchantOnboardTerminalBean = new MerchantOnboardTerminalBean();
	private List<MerchantOnboardStoreBean> listOfStoreBeans;
	private MerchantOnboardService merchantOnboardService = null;
	private List<MerchantOnboardBean> listofmerchants;
	private ParameterValuesService parameterValuesServiceImpl;
	private MasterListService masterListService;
	private static final String MidInstParam = "401";
	private Integer autoGenerateMid;
	private static final String SidInstParam = "402";
	private Integer autoGenerateSid = null;
	private static final String TidInstParam = "403";
	private Integer autoGenerateTid = null;
	private static final String TRANSACTIONSOURCE = "9, 11";
	private static final String merchantLevel = "1";
	private static final String storeLevel = "2";
	private static final String terminalLevel = "3";
	private static final String InstitutionCountry = "4";
	private String strTempFoldername;
	private UserServiceImpl userService;
	private EmailService emailService;
	private SessionBean sessionBean;
	
	private List<KeyValueBean> mercAggrList;
	private List<KeyValueBean> relShipManagerList;
	private List<KeyValueBean> cntryList;
	private List<KeyValueBean> stateList;
	private List<KeyValueBean> cityList;
	private List<KeyValueBean> ccityList;
	private List<KeyValueBean> cstateList;
	private List<KeyValueBean> feePlanList;
	private List<KeyValueBean> feePlanListFortire;
	private List<KeyValueBean> merGradeList;
	private List<KeyValueBean> merchantTypeList;
	private List<KeyValueBean> riskCategoryList;
	//private List<KeyValueBean> terminalChargePlanList;
	private List<KeyValueBean> merchantEntityList;
	//private List<KeyValueBean> branchList;
	private List<MapsBaseBean> crcyList;
	private List<KeyValueBean> lstProuctType;
	//private List<KeyValueBean> cryptographicMethodsList;
	//private List<KeyValueBean> brandList;
	//private List<KeyValueBean> ecomProcessingPlanNameList;
	private List<KeyValueBean> mccList;
	private List<KeyValueBean> acquirerIDList=new ArrayList<KeyValueBean>();
	private List<KeyValueBean> incorpStatusList;
	//private List<KeyValueBean> documtCatgList;
	//private List<KeyValueBean> cardGroupList;
	//private List<KeyValueBean> terminalProfileList;
	//private List<KeyValueBean> ecomProfileList;
	private List<KeyValueBean> homeCountryIdList;
	private List<CardAcceptanceDetailBean> cardAccptDetailList = new ArrayList<CardAcceptanceDetailBean>();
	private List<KeyValueBean> ecomRiskProfileNamesList = new ArrayList<KeyValueBean>();
	private List<KeyValueBean> natureOfBusinessCatgList;
	private List<KeyValueBean> paymentMethodList;
	KEYConfigBean keyConfigBean=null;
	public static final String KID = "KID";
	public static final String KEY = "KEY";
	private AttachmentMalwareScannerUtil scannerUtil = new AttachmentMalwareScannerUtil();
	private String uploadMercLogoFileName;
	private static final String CLAMAV_KEY = "104";
	public String getUploadMercLogoFileName() {
		return uploadMercLogoFileName;
	}
	public void setUploadMercLogoFileName(String uploadMercLogoFileName) {
		this.uploadMercLogoFileName = uploadMercLogoFileName;
	}
	public AttachmentMalwareScannerUtil getScannerUtil() {
		return scannerUtil;
	}
	public void setScannerUtil(AttachmentMalwareScannerUtil scannerUtil) {
		this.scannerUtil = scannerUtil;
	}
	private File fileUpload;
	
	public File getFileUpload() {
		return fileUpload;
	}
	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}
	
	public SessionBean getSessionBean() {
		return sessionBean;
	}
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}
	@Override
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getInstId() {
		return instId;
	}
	public void setInstId(Integer instId) {
		this.instId = instId;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
	public HttpServletResponse getHttpServletResponse() {
		return httpServletResponse;
	}
	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}	
	public MerchantOnboardBean getMerchantOnboardBean() {
		return merchantOnboardBean;
	}
	public void setMerchantOnboardBean(MerchantOnboardBean merchantOnboardBean) {
		this.merchantOnboardBean = merchantOnboardBean;
	}
	public MerchantOnboardService getMerchantOnboardService() {
		return merchantOnboardService;
	}
	public void setMerchantOnboardService(MerchantOnboardService merchantOnboardService) {
		this.merchantOnboardService = merchantOnboardService;
	}
	public List<MerchantOnboardBean> getListofmerchants() {
		return listofmerchants;
	}
	public void setListofmerchants(List<MerchantOnboardBean> listofmerchants) {
		this.listofmerchants = listofmerchants;
	}
	public ParameterValuesService getParameterValuesServiceImpl() {
		return parameterValuesServiceImpl;
	}
	public void setParameterValuesServiceImpl(ParameterValuesService parameterValuesServiceImpl) {
		this.parameterValuesServiceImpl = parameterValuesServiceImpl;
	}
	public Integer getAutoGenerateMid() {
		return autoGenerateMid;
	}
	public void setAutoGenerateMid(Integer autoGenerateMid) {
		this.autoGenerateMid = autoGenerateMid;
	}
	public Integer getAutoGenerateSid() {
		return autoGenerateSid;
	}
	public void setAutoGenerateSid(Integer autoGenerateSid) {
		this.autoGenerateSid = autoGenerateSid;
	}
	public Integer getAutoGenerateTid() {
		return autoGenerateTid;
	}
	public void setAutoGenerateTid(Integer autoGenerateTid) {
		this.autoGenerateTid = autoGenerateTid;
	}
	public MasterListService getMasterListService() {
		return masterListService;
	}
	public void setMasterListService(MasterListService masterListService) {
		this.masterListService = masterListService;
	}
	public List<KeyValueBean> getMercAggrList() {
		return mercAggrList;
	}
	public void setMercAggrList(List<KeyValueBean> mercAggrList) {
		this.mercAggrList = mercAggrList;
	}
	public List<KeyValueBean> getRelShipManagerList() {
		return relShipManagerList;
	}
	public void setRelShipManagerList(List<KeyValueBean> relShipManagerList) {
		this.relShipManagerList = relShipManagerList;
	}
	public List<KeyValueBean> getCntryList() {
		return cntryList;
	}
	public void setCntryList(List<KeyValueBean> cntryList) {
		this.cntryList = cntryList;
	}
	public List<KeyValueBean> getStateList() {
		return stateList;
	}
	public void setStateList(List<KeyValueBean> stateList) {
		this.stateList = stateList;
	}
	public List<KeyValueBean> getCityList() {
		return cityList;
	}
	public void setCityList(List<KeyValueBean> cityList) {
		this.cityList = cityList;
	}
	public List<KeyValueBean> getCcityList() {
		return ccityList;
	}
	public void setCcityList(List<KeyValueBean> ccityList) {
		this.ccityList = ccityList;
	}
	public List<KeyValueBean> getCstateList() {
		return cstateList;
	}
	public void setCstateList(List<KeyValueBean> cstateList) {
		this.cstateList = cstateList;
	}
	public List<KeyValueBean> getFeePlanList() {
		return feePlanList;
	}
	public void setFeePlanList(List<KeyValueBean> feePlanList) {
		this.feePlanList = feePlanList;
	}
	public List<KeyValueBean> getFeePlanListFortire() {
		return feePlanListFortire;
	}
	public void setFeePlanListFortire(List<KeyValueBean> feePlanListFortire) {
		this.feePlanListFortire = feePlanListFortire;
	}
	public List<KeyValueBean> getMerGradeList() {
		return merGradeList;
	}
	public void setMerGradeList(List<KeyValueBean> merGradeList) {
		this.merGradeList = merGradeList;
	}
	public List<KeyValueBean> getMerchantTypeList() {
		return merchantTypeList;
	}
	public void setMerchantTypeList(List<KeyValueBean> merchantTypeList) {
		this.merchantTypeList = merchantTypeList;
	}
	public List<KeyValueBean> getRiskCategoryList() {
		return riskCategoryList;
	}
	public void setRiskCategoryList(List<KeyValueBean> riskCategoryList) {
		this.riskCategoryList = riskCategoryList;
	}
	public List<KeyValueBean> getMerchantEntityList() {
		return merchantEntityList;
	}
	public void setMerchantEntityList(List<KeyValueBean> merchantEntityList) {
		this.merchantEntityList = merchantEntityList;
	}
	public List<MapsBaseBean> getCrcyList() {
		return crcyList;
	}
	public void setCrcyList(List<MapsBaseBean> crcyList) {
		this.crcyList = crcyList;
	}
	public List<KeyValueBean> getLstProuctType() {
		return lstProuctType;
	}
	public void setLstProuctType(List<KeyValueBean> lstProuctType) {
		this.lstProuctType = lstProuctType;
	}
	public List<KeyValueBean> getMccList() {
		return mccList;
	}
	public void setMccList(List<KeyValueBean> mccList) {
		this.mccList = mccList;
	}
	public List<KeyValueBean> getAcquirerIDList() {
		return acquirerIDList;
	}
	public void setAcquirerIDList(List<KeyValueBean> acquirerIDList) {
		this.acquirerIDList = acquirerIDList;
	}
	public List<KeyValueBean> getIncorpStatusList() {
		return incorpStatusList;
	}
	public void setIncorpStatusList(List<KeyValueBean> incorpStatusList) {
		this.incorpStatusList = incorpStatusList;
	}
	public List<KeyValueBean> getHomeCountryIdList() {
		return homeCountryIdList;
	}
	public void setHomeCountryIdList(List<KeyValueBean> homeCountryIdList) {
		this.homeCountryIdList = homeCountryIdList;
	}
	public List<CardAcceptanceDetailBean> getCardAccptDetailList() {
		return cardAccptDetailList;
	}
	public void setCardAccptDetailList(List<CardAcceptanceDetailBean> cardAccptDetailList) {
		this.cardAccptDetailList = cardAccptDetailList;
	}
	public List<KeyValueBean> getEcomRiskProfileNamesList() {
		return ecomRiskProfileNamesList;
	}
	public void setEcomRiskProfileNamesList(List<KeyValueBean> ecomRiskProfileNamesList) {
		this.ecomRiskProfileNamesList = ecomRiskProfileNamesList;
	}
	public MerchantOnboardStoreBean getMerchantOnboardStoreBean() {
		return merchantOnboardStoreBean;
	}
	public void setMerchantOnboardStoreBean(MerchantOnboardStoreBean merchantOnboardStoreBean) {
		this.merchantOnboardStoreBean = merchantOnboardStoreBean;
	}
	public MerchantOnboardTerminalBean getMerchantOnboardTerminalBean() {
		return merchantOnboardTerminalBean;
	}
	public void setMerchantOnboardTerminalBean(MerchantOnboardTerminalBean merchantOnboardTerminalBean) {
		this.merchantOnboardTerminalBean = merchantOnboardTerminalBean;
	}
	public List<MerchantOnboardStoreBean> getListOfStoreBeans() {
		return listOfStoreBeans;
	}
	public void setListOfStoreBeans(List<MerchantOnboardStoreBean> listOfStoreBeans) {
		this.listOfStoreBeans = listOfStoreBeans;
	}
	public List<KeyValueBean> getNatureOfBusinessCatgList() {
		return natureOfBusinessCatgList;
	}
	public void setNatureOfBusinessCatgList(List<KeyValueBean> natureOfBusinessCatgList) {
		this.natureOfBusinessCatgList = natureOfBusinessCatgList;
	}
	public List<KeyValueBean> getPaymentMethodList() {
		return paymentMethodList;
	}
	public void setPaymentMethodList(List<KeyValueBean> paymentMethodList) {
		this.paymentMethodList = paymentMethodList;
	}
	public String getStrTempFoldername() {
		return strTempFoldername;
	}
	public void setStrTempFoldername(String strTempFoldername) {
		this.strTempFoldername = strTempFoldername;
	}
	public UserServiceImpl getUserService() {
		return userService;
	}
	public void setUserService(UserServiceImpl userService) {
		this.userService = userService;
	}
	public EmailService getEmailService() {
		return emailService;
	}
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@Override
	public void prepare() throws Exception {
		try{
			userId = httpServletRequest.getSession().getAttribute("userId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("userId").toString());
			instId = httpServletRequest.getSession().getAttribute("instId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("instId").toString());
			dateFormat = (String) ServletActionContext.getContext().getSession().get("INST-DATEFRMT");
			userGroup = (String) httpServletRequest.getSession().getAttribute("SPRING_SECURITY_FORM_USERNAME_KEY");
			merchantOnboardBean.setInstCode(instId);
			merchantOnboardBean.setUserId(userId.toString());
		}catch(Exception e){
			logUtil.error("Error occured in prepare method", e);
		}
	}
	
	private void getBasicDetailsForAdd() throws Exception {	
		
		try {	
			strTempFoldername = merchantOnboardService.getTempFoldername();
			ParameterBean parameterBean1 = parameterValuesServiceImpl.getParameterData(MidInstParam, instId);
			setAutoGenerateMid(Integer.parseInt(parameterBean1.getParameterValue()));
			ParameterBean parameterBean = parameterValuesServiceImpl.getParameterData(SidInstParam, instId);
			setAutoGenerateSid(Integer.parseInt(parameterBean.getParameterValue()));
			ParameterBean parameterBean2 = parameterValuesServiceImpl.getParameterData(TidInstParam, instId);
			setAutoGenerateTid(Integer.parseInt(parameterBean2.getParameterValue()));
			relShipManagerList = masterListService.getRelShipManagerList(instId);			
			feePlanList = masterListService.getFeePlanList(instId);
			feePlanListFortire  = masterListService.getTierFeePlanList(instId);
			merGradeList=masterListService.getMercGradeList(instId);
			String query=" SELECT MAR_RSCG_SQID,MAR_RSCG_DESC FROM MPS_ADMN_RSCG_MAST  WHERE MAR_INST_SQID="+instId+" ORDER BY MAR_RSCG_SQID ";
			riskCategoryList= masterListService.getList(query);
			merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, null);
			merchantTypeList = merchantOnboardService.getMerchantTypeList(instId);
			crcyList=merchantOnboardService.getCurrency();
			homeCountryIdList = masterListService.getCountryList(instId);
			lstProuctType = merchantOnboardService.getTerminalProductType(lstProuctType, instId, null);
			incorpStatusList = masterListService.getIncorpStatus();
			ecomRiskProfileNamesList = masterListService.getEcommRiskProfiles(Integer.parseInt(instId.toString()));
			natureOfBusinessCatgList= merchantOnboardService.getNatureOfBusinessCatgList(instId);
			paymentMethodList = merchantOnboardService.getPaymentMethodList(instId);
			//terminalChargePlanList = masterListService.getMerchantChargePlanForTerminal(instId);
			//branchList = getMasterListService().getBranchList(instId)
			//cryptographicMethodsList = masterListService.getCryptoGraphicMethodsList(instId);
			//brandList = masterListService.getBrandList(instId,userId);
			//ecomProcessingPlanNameList = masterListService.getProcessingPlanNameList(instId);
			//documtCatgList =masterListService.getDocumentCatgList(instId);
			//cardGroupList=masterListService.getCardGroupList(instId);
			//terminalProfileList=merchantOnboardService.getTerminalProfileList(instId);
			//ecomProfileList=merchantOnboardService.getEcomTerminalProfileList(instId);
		} 
		catch (Exception e) {
			logUtil.error("Error occured in getBasicDetailsForAdd", e);
			throw e;
		}
	}
	
	public String addMerchantOnboardPage() {
		String refNo = "", additionalNewStoreFlag = "0";
		try {
			if (httpServletRequest.getParameter("referralNo") != null) { refNo = httpServletRequest.getParameter("referralNo"); } 
			else { refNo = merchantOnboardBean.getRefNo(); }

			if (merchantOnboardBean.getRefNo() == null) {
				if (refNo != null && !("".equals(refNo))) {
					merchantOnboardBean.setRefNo(refNo);
				}
			}
			
			if (httpServletRequest.getParameter("additionalNewStoreFlag") != null) { additionalNewStoreFlag = httpServletRequest.getParameter("additionalNewStoreFlag"); } 
			else if(merchantOnboardBean.getAdditionalNewStoreFlag() != null) { additionalNewStoreFlag = merchantOnboardBean.getAdditionalNewStoreFlag()+""; }
			if (merchantOnboardBean.getAdditionalNewStoreFlag() == null) {
				if (additionalNewStoreFlag != null && !("".equals(additionalNewStoreFlag))) {
					merchantOnboardBean.setAdditionalNewStoreFlag(Integer.parseInt(additionalNewStoreFlag));
				}
			}
			
			if(merchantOnboardBean.getRefNo()!=null && !merchantOnboardBean.getRefNo().equalsIgnoreCase("")) {
				if(merchantOnboardBean.getAdditionalNewStoreFlag()==null) {
					merchantOnboardBean.setAdditionalNewStoreFlag(merchantOnboardService.getAdditionalNewStoreFlag(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId));
				}
			} else {
				merchantOnboardBean.setAdditionalNewStoreFlag(0);
			}
			
			if(merchantOnboardBean.getAdditionalNewStoreFlag() == 1) {
				int sessionFlag = merchantOnboardService.checkSessionForMerchant(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId);
				if(sessionFlag==0) {
					merchantOnboardBean = merchantOnboardService.getMerchantMastDetails(merchantOnboardBean,instId);
					merchantOnboardBean = merchantOnboardService.getPricingMastDetails(merchantOnboardBean,instId);
					merchantOnboardBean = merchantOnboardService.getAccountMastDetails(merchantOnboardBean,instId);
					//merchantOnboardBean = merchantOnboardService.getMerchantRemarksDetails(merchantOnboardBean,instId);
					//merchantOnboardBean.setLstStoreBean(merchantOnboardService.getAllStoreAndTerminalDetails(merchantOnboardBean, instId));
					merchantOnboardBean.setRemarks(null);
					merchantOnboardBean = getDefaultValuesSet(merchantOnboardBean);
					getBasicDetailsForAdd();
					merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
					merchantTypeList = merchantOnboardService.getMerchantTypeList(instId);
					merchantOnboardService.updateSessionFlagForMerchant(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), 1, userId);
					return SUCCESS;
				} else {
					addActionError(getText("SESSERROR0001")+" "+merchantOnboardBean.getRefNo());
					merchantOnboardBean.setRefNo(null);
					listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
					return INPUT;
				}
			} else {
				getBasicDetailsForAdd();
				merchantOnboardStoreBean.setTermneed(null);
				merchantOnboardBean = getDefaultValuesSet(merchantOnboardBean);
				merchantOnboardBean.setCcountry(merchantOnboardService.getParamValue(InstitutionCountry));
				if(merchantOnboardBean.getCcountry()!=null) {
					merchantOnboardBean.setCountry(merchantOnboardService.getCountryName(merchantOnboardBean.getCcountry()));
				}
				return SUCCESS;
			}
		} catch (Exception e) {
			addActionError(getText("MerchantOnboardMakerAction.errorLoadingPage"));
			logUtil.error("Error while loading the merchant onboarding details", e);
			merchantOnboardBean.setRefNo(null);
			listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
			return ERROR;
		}
	}
	
	/*public void prepareAddMerchantOnboardPageDetails() throws Exception {
		try{
			userId = httpServletRequest.getSession().getAttribute("userId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("userId").toString());
			instId = httpServletRequest.getSession().getAttribute("instId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("instId").toString());
			merchantOnboardBean.setInstCode(instId);
			merchantOnboardBean.setUserId(userId.toString());
			getBasicDetailsForAdd();
		}catch(Exception e){
			logUtil.error("Error in prepareAddSinglePageDetails", e);
		}
	}*/
	
	public String addMerchantOnboardPageDetails() {
		logUtil.audit("Add Merchant Onboard Page Details - Entering", true, userGroup);
		logUtil.logMethodEntry("addMerchantOnboardPageDetails()");
		boolean response = false;
		try {
			userId = httpServletRequest.getSession().getAttribute("userId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("userId").toString());
			instId = httpServletRequest.getSession().getAttribute("instId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("instId").toString());
			merchantOnboardBean.setInstCode(instId);
			merchantOnboardBean.setUserId(userId.toString());
			if(merchantOnboardBean.getRefNo()!=null && !merchantOnboardBean.getRefNo().equalsIgnoreCase("")) {
				if(merchantOnboardBean.getAdditionalNewStoreFlag()==null) {
					merchantOnboardBean.setAdditionalNewStoreFlag(merchantOnboardService.getAdditionalNewStoreFlag(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId));
				}
			} else {
				merchantOnboardBean.setAdditionalNewStoreFlag(0);
			}
			
			if(merchantOnboardBean.getAdditionalNewStoreFlag() == 1) {
				int sessionFlag = merchantOnboardService.checkSessionForMerchant(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId);
				
				ParameterBean parameterBean1 = parameterValuesServiceImpl.getParameterData(MidInstParam, instId);
				setAutoGenerateMid(Integer.parseInt(parameterBean1.getParameterValue()));
				ParameterBean parameterBean = parameterValuesServiceImpl.getParameterData(SidInstParam, instId);
				setAutoGenerateSid(Integer.parseInt(parameterBean.getParameterValue()));
				ParameterBean parameterBean2 = parameterValuesServiceImpl.getParameterData(TidInstParam, instId);
				setAutoGenerateTid(Integer.parseInt(parameterBean2.getParameterValue()));
				
				if(sessionFlag==0) {
					String validateMerchantDetails = validateForAddNewStoreAfterMerchantOnBoard();
					if (validateMerchantDetails != null && validateMerchantDetails.equalsIgnoreCase("input")) {
						merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
						if(merchantOnboardBean.getLstStoreBean() !=null && merchantOnboardBean.getLstStoreBean().size() != 0) {
							for (int i = 0; i < merchantOnboardBean.getLstStoreBean().size(); i++) {
			            		MerchantOnboardStoreBean storeBean = merchantOnboardBean.getLstStoreBean().get(i);
					            if (storeBean != null) {
					            	merchantOnboardBean.getLstStoreBean().get(i).setTermneed(storeBean.getTermneed() == null ? "0" : storeBean.getTermneed().equals("true") ? "1" : storeBean.getTermneed().equals("false") ? "0" : storeBean.getTermneed());
					            	merchantOnboardBean.getLstStoreBean().get(i).setMerchantEntityName(TRANSACTIONSOURCE);
					            	merchantOnboardBean.getLstStoreBean().get(i).setPrfDocsProvList(
					            	merchantOnboardService.getPrfDocsProvList(instId, storeBean.getPrfDocsProv()));
					            }
							}
						}
						merchantOnboardBean = merchantOnboardService.getMerchantMastDetails(merchantOnboardBean, instId);
						merchantOnboardBean = merchantOnboardService.getPricingMastDetails(merchantOnboardBean,instId);
						merchantOnboardBean = merchantOnboardService.getAccountMastDetails(merchantOnboardBean,instId);
						merchantOnboardBean = merchantOnboardService.getMerchantRemarksDetails(merchantOnboardBean,instId);
						getBasicDetailsForAdd();
						return INPUT;
					}
					
					byte storecount = 0, terminalcount = 0, totalStoreCount = 0;
					for (MerchantOnboardStoreBean storeBean : merchantOnboardBean.getLstStoreBean()) {
						if (storeBean != null) {
					    	totalStoreCount++;
					    }
					}
					for (MerchantOnboardStoreBean storeBean : merchantOnboardBean.getLstStoreBean()) {
						if (storeBean != null) {
						    storecount++;
						    if(getAutoGenerateSid()==1) {
						    	storeBean.setStoreCode(merchantOnboardService.getAutoGenerateCode(instId, 2));
						    }
						    storeBean.setTermneed(storeBean.getTermneed() == null ? "0" : storeBean.getTermneed().equals("true") ? "1" : storeBean.getTermneed().equals("false") ? "0" : storeBean.getTermneed());
						    storeBean.setMerchantEntityName(TRANSACTIONSOURCE);
						    merchantOnboardBean = merchantOnboardService.addStoreWithAccountDetails(merchantOnboardBean, storeBean, instId, userId);
						    if (storeBean.getStorePriceDetailsRequired().equals("1") && merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")){
						    	merchantOnboardBean = merchantOnboardService.addStorePriceDetails(merchantOnboardBean, storeBean, instId, userId, storeLevel);
						        if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
					        		addActionError("Error occured while adding new Store -"+storecount+" with Price Details.");
								    merchantOnboardService.rollBackFunctionForAddnewStore(merchantOnboardBean);
								    getBasicDetailsForAdd();
								    return ERROR;
								}
						    }
						    if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") && storeBean.getTermneed() != null && storeBean.getTermneed().equals("1")) {
								List<MerchantOnboardTerminalBean> terminalBeans = storeBean.getLstTerminalBean();
							    if (terminalBeans != null) {
							    	for (MerchantOnboardTerminalBean terminalBean : terminalBeans) {
							        	if (terminalBean != null) {
							            	terminalcount++;
							                if(getAutoGenerateTid()==1) {
							                	terminalBean.setTermCode(merchantOnboardService.getAutoGenerateCode(instId, 3));
							                }
							                if(terminalBean.getDftTrmlRcrngChrgePlnId() == null || terminalBean.getDftTrmlRcrngChrgePlnId().trim().equals("")) {
							            		terminalBean.setDftTrmlRcrngChrgePlnId(null);
							            		terminalBean.setDftTrmlRcrngChrgePlnIdname(null);
							    			} 
							                			
							                keyConfigBean=masterListService.getDEKClearKey(String.valueOf(instId));
							                if(keyConfigBean!=null && (keyConfigBean.getClearDEKKey()!=null && !keyConfigBean.getClearDEKKey().isEmpty())){
							                	Map<String, String> maps = new AESEncDecUtil().generateTerminalKeys();
								                terminalBean.setTerminalGenerateRandomKey(maps.get(KID));
							                	terminalBean.setTerminalSecretKey(KeyConfigUtils.DataEncrypt(KeyConfigUtils.stringToByteArray(maps.get(KEY)),keyConfigBean.getKeyAlgDesc(), keyConfigBean.getClearDEKKey()));
							                }
							                merchantOnboardBean = merchantOnboardService.addTerminalWithAccountDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId);
							                if (terminalBean.getTerminalPriceDetailsRequired().equals("1") && merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")){
										    	merchantOnboardBean = merchantOnboardService.addTerminalPriceDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId, terminalLevel);
										        if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
					                            	addActionError("Error occured while adding new Store -"+storecount+" - Terminal- "+terminalcount+" with Price Details.");
									                merchantOnboardService.rollBackFunctionForAddnewStore(merchantOnboardBean);
									                getBasicDetailsForAdd();
									                return ERROR;
											    }
										    }
							                if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
					                        	addActionError("Error occured while adding new Store-"+storecount+" - Terminal-"+terminalcount+" with Account Details.");
							                    merchantOnboardService.rollBackFunctionForAddnewStore(merchantOnboardBean);
							                    getBasicDetailsForAdd();
							                    return ERROR;
							                }
							            }
							       }
							       merchantOnboardBean = merchantOnboardService.insertOrUpdateToProdTemp(merchantOnboardBean, storeBean, instId);
							       terminalcount=0;
							   }
						    } else if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
					        	addActionError("Error occured while adding new Store-"+storecount+" with Account Details.");
					            merchantOnboardService.rollBackFunctionForAddnewStore(merchantOnboardBean);
					            getBasicDetailsForAdd();
					            return ERROR;
							 }
							 if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
					        	merchantOnboardService.updateAddnewStoreFlag(merchantOnboardBean, 1, userId);
							 	merchantOnboardService.updateAttachementPathForStore(instId, storeLevel, merchantOnboardBean.getRefSqid(),
							   	storeBean.getStoreSqid(), storeBean.getTempFolderName(), totalStoreCount, storecount);
							 }
							 if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
								String listCheckerEmailIDs = merchantOnboardService.getlistCheckerEmailIDs(userId);
								if (listCheckerEmailIDs != null && !listCheckerEmailIDs.isEmpty()) {
									sendNotificationEmailToCheckers(listCheckerEmailIDs, storeBean.getStoreCode(), "file", userId, merchantOnboardBean.getLegalname(), merchantOnboardBean.getRemarks());
								}
							}
						}
					}
					
					if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
						String makerEmailID = merchantOnboardService.getUserEmailID(userId);
	                	if (makerEmailID != null && !makerEmailID.isEmpty()) {
	                		sendAcknowledgementEmailToMaker(makerEmailID, userId, merchantOnboardBean.getMid(), merchantOnboardBean.getLegalname());
						}
					}
					addActionMessage(getText("MerchantOnboardMakerAction.addSuccess")+" "+merchantOnboardBean.getRefSqid());
					logUtil.logMethodExit("addMerchantOnboardPageDetails()");
					logUtil.audit("Add New Store - Merchant Onboard Page Details - Exit", true, userGroup);
					merchantOnboardBean.setOnboardMethod(null);
					merchantOnboardBean.setSelectSearchBy(null);
					merchantOnboardBean.setLegalname(null);
					merchantOnboardBean.setRefNo(null);
					merchantOnboardBean.setApplNum(null);
					merchantOnboardBean.setRefNo(null);
					listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
					return SUCCESS;
				} else {
					addActionError(getText("SESSERROR0001")+" "+merchantOnboardBean.getRefNo());
					merchantOnboardBean.setRefNo(null);
					listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
					return SUCCESS;
				}
			} else {
			
				// Code added by Narayanan S on 15-04-2025 for Attachment File Scanner - starts
				String clamAVCheck = masterListService.getClamAVCheck(CLAMAV_KEY);
			    if (clamAVCheck != null && "1".equals(clamAVCheck)) {
					if (!System.getProperty("os.name").startsWith("Windows")){
						if (merchantOnboardBean.getMercLogo().isFile() && merchantOnboardBean.getMercLogo()!=null) {
							try {
								if (scannerUtil.ping()) {
									byte[] fileBytes = Files.readAllBytes(merchantOnboardBean.getMercLogo().toPath());
		
										try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
											response = scannerUtil.scan(inputStream);
											if (response) {
												addActionError(getText("attachmentsaction.malware_Found") + uploadMercLogoFileName);
												return ERROR;
								            }
										} catch (Exception e) {
											logUtil.error("Scan failed: " + uploadMercLogoFileName + " " + e.getMessage());
											addActionError(getText("attachmentsaction.scan_Failed") + uploadMercLogoFileName);
											return ERROR;
										}
								} else {
									logUtil.error("ClamAV not reachable.");
									addActionError(getText("attachmentsaction.clamAV_Unreachable"));
									return ERROR;
								}
							}catch (Exception e) {
								logUtil.error("Error while scanning: " + uploadMercLogoFileName + " " + e.getMessage());
								addActionError(getText("attachmentsaction.error_Scan") + uploadMercLogoFileName);
								return ERROR;
							}
						}
					}
			    }
				// Code added by Narayanan S on 15-04-2025 for Attachment File Scanner - ends
				
				if (merchantOnboardBean.getMercLogo() != null) { 
				    byte[] imageData = new byte[(int) merchantOnboardBean.getMercLogo().length()];
				    try (FileInputStream fis = new FileInputStream(merchantOnboardBean.getMercLogo())) { 
				        fis.read(imageData); 
				    }
				    merchantOnboardBean.setMercLogoUpldData(imageData); 
				    httpServletRequest.getSession().setAttribute("imageData", imageData); 
				    merchantOnboardBean.setMercLogoDisplayData(Base64.getEncoder().encodeToString(imageData)); 
				} else {
				    byte[] sessionImageData = (byte[]) httpServletRequest.getSession().getAttribute("imageData");
				    if (sessionImageData != null) {
				        merchantOnboardBean.setMercLogoUpldData(sessionImageData);
				        merchantOnboardBean.setMercLogoDisplayData(Base64.getEncoder().encodeToString(sessionImageData));
				    }
				}
	
				String validateMerchantDetails = validateMerchantDetails();
				if (validateMerchantDetails != null && validateMerchantDetails.equalsIgnoreCase("input")) {
					merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
					if(merchantOnboardBean.getLstStoreBean() !=null && merchantOnboardBean.getLstStoreBean().size() != 0) {
						for (int i = 0; i < merchantOnboardBean.getLstStoreBean().size(); i++) {
		            		MerchantOnboardStoreBean storeBean = merchantOnboardBean.getLstStoreBean().get(i);
				            if (storeBean != null) {
				            	merchantOnboardBean.getLstStoreBean().get(i).setTermneed(storeBean.getTermneed() == null ? "0" : storeBean.getTermneed().equals("true") ? "1" : storeBean.getTermneed().equals("false") ? "0" : storeBean.getTermneed());
				            	merchantOnboardBean.getLstStoreBean().get(i).setMerchantEntityName(TRANSACTIONSOURCE);
				            	merchantOnboardBean.getLstStoreBean().get(i).setPrfDocsProvList(
				            	merchantOnboardService.getPrfDocsProvList(instId, storeBean.getPrfDocsProv()));
				            }
						}
					}
					getBasicDetailsForAdd();
					return INPUT;
				}
				if(getAutoGenerateMid()==1) {
					merchantOnboardBean.setMid(merchantOnboardService.getAutoGenerateCode(instId, 1));
				}
				if(merchantOnboardBean.getMercAggr()== null || merchantOnboardBean.getMercAggr().trim().equals("")) {
					merchantOnboardBean.setMercAggr(null);
					merchantOnboardBean.setMercAggrName(null);
					merchantOnboardBean.setMercAggrrelm(null);
				}
				
				merchantOnboardBean.setIncorpstatus("0");
				
				merchantOnboardBean = merchantOnboardService.addMerchantDetails(merchantOnboardBean, instId, userId);
				if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
				    merchantOnboardBean = merchantOnboardService.addPriceDetails(merchantOnboardBean, instId, userId, merchantLevel);
				    if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") && (merchantOnboardBean.getAcctMapLvl() != 0 
				    		|| (merchantOnboardBean.getAcctMapLvl() == 0 && merchantOnboardBean.getMerchantAccountRequired()==1))) {
				    	merchantOnboardBean = merchantOnboardService.addAccountDetails(merchantOnboardBean, instId, userId);
				    	if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
	                    	merchantOnboardService.rollBackFunctionForMerchantOnboard(merchantOnboardBean);
	                    	addActionError("Error occured while adding Merchant Account Details.");
	                    	getBasicDetailsForAdd();
			                return ERROR;
				    	}
				    }
				    	if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
				    		byte storecount = 0, terminalcount = 0, totalStoreCount = 0;
				    		for (MerchantOnboardStoreBean storeBean : merchantOnboardBean.getLstStoreBean()) {
				    			if (storeBean != null) {
				    				totalStoreCount++;
				    			}
				    		}
				    		for (MerchantOnboardStoreBean storeBean : merchantOnboardBean.getLstStoreBean()) {
					        	if (storeBean != null) {
					            	storecount++;
					            	if(getAutoGenerateSid()==1) {
					            		storeBean.setStoreCode(merchantOnboardService.getAutoGenerateCode(instId, 2));
					    			}
					            	storeBean.setTermneed(storeBean.getTermneed() == null ? "0" : storeBean.getTermneed().equals("true") ? "1" : storeBean.getTermneed().equals("false") ? "0" : storeBean.getTermneed());
					            	storeBean.setMerchantEntityName(TRANSACTIONSOURCE);
					            	merchantOnboardBean = merchantOnboardService.addStoreWithAccountDetails(merchantOnboardBean, storeBean, instId, userId);
					                if (storeBean.getStorePriceDetailsRequired().equals("1") && merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")){
					                	merchantOnboardBean = merchantOnboardService.addStorePriceDetails(merchantOnboardBean, storeBean, instId, userId, storeLevel);
					                	if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
						                	addActionError("Error occured while adding Store -"+storecount+" with Price Details.");
							                merchantOnboardService.rollBackFunctionForMerchantOnboard(merchantOnboardBean);
							                getBasicDetailsForAdd();
							                return ERROR;
							            }
					                }
					                if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") && storeBean.getTermneed() != null && storeBean.getTermneed().equals("1")) {
						                List<MerchantOnboardTerminalBean> terminalBeans = storeBean.getLstTerminalBean();
						                if (terminalBeans != null) {
						                    for (MerchantOnboardTerminalBean terminalBean : terminalBeans) {
						                        if (terminalBean != null) {
						                        	terminalcount++;
						                			if(getAutoGenerateTid()==1) {
						                				terminalBean.setTermCode(merchantOnboardService.getAutoGenerateCode(instId, 3));
						                			}
						                			if(terminalBean.getDftTrmlRcrngChrgePlnId() == null || terminalBean.getDftTrmlRcrngChrgePlnId().trim().equals("")) {
									            		terminalBean.setDftTrmlRcrngChrgePlnId(null);
									            		terminalBean.setDftTrmlRcrngChrgePlnIdname(null);
									    			} 
						                			
						                			keyConfigBean=masterListService.getDEKClearKey(String.valueOf(instId));
						                			if(keyConfigBean!=null && (keyConfigBean.getClearDEKKey()!=null && !keyConfigBean.getClearDEKKey().isEmpty())){
						                				Map<String, String> maps = new AESEncDecUtil().generateTerminalKeys();
							                			terminalBean.setTerminalGenerateRandomKey(maps.get(KID));
						                				terminalBean.setTerminalSecretKey(KeyConfigUtils.DataEncrypt(KeyConfigUtils.stringToByteArray(maps.get(KEY)),keyConfigBean.getKeyAlgDesc(), keyConfigBean.getClearDEKKey()));
						                			}
						                            merchantOnboardBean = merchantOnboardService.addTerminalWithAccountDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId);
						                            if (terminalBean.getTerminalPriceDetailsRequired().equals("1") && merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")){
									                	merchantOnboardBean = merchantOnboardService.addTerminalPriceDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId, terminalLevel);
									                	if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
								                            addActionError("Error occured while adding Store -"+storecount+" - Terminal- "+terminalcount+" with Price Details.");
								                            merchantOnboardService.rollBackFunctionForMerchantOnboard(merchantOnboardBean);
								                            getBasicDetailsForAdd();
												            return ERROR;
										                }
									                }
						                            if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
						                            	addActionError("Error occured while adding Store-"+storecount+" - Terminal-"+terminalcount+" with Account Details.");
						                            	merchantOnboardService.rollBackFunctionForMerchantOnboard(merchantOnboardBean);
						                            	getBasicDetailsForAdd();
										                return ERROR;
						                            }
						                        }
						                    }
						                    merchantOnboardBean = merchantOnboardService.insertOrUpdateToProdTemp(merchantOnboardBean, storeBean, instId);
						                    terminalcount=0;
						                }
					                } else if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
					                	addActionError("Error occured while adding Store-"+storecount+" with Account Details.");
						                merchantOnboardService.rollBackFunctionForMerchantOnboard(merchantOnboardBean);
						                getBasicDetailsForAdd();
						                return ERROR;
						            }
					                if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
					                	merchantOnboardService.updateAttachementPathForStore(instId, storeLevel, merchantOnboardBean.getRefSqid(),
					                		storeBean.getStoreSqid(), storeBean.getTempFolderName(), totalStoreCount, storecount);
					                }
					                if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
					                	String listCheckerEmailIDs = merchantOnboardService.getlistCheckerEmailIDs(userId);
										if (listCheckerEmailIDs != null && !listCheckerEmailIDs.isEmpty()) {
											sendNotificationEmailToCheckers(listCheckerEmailIDs, storeBean.getStoreCode(), "file", userId, merchantOnboardBean.getLegalname(), merchantOnboardBean.getRemarks());
										}
									}
					            }
					        }
				    	} else {
					        merchantOnboardService.rollBackFunctionForMerchantOnboard(merchantOnboardBean);
					        addActionError("Error occured while adding Price Details.");
					        getBasicDetailsForAdd();
					        return ERROR;
					    }
				} else {
				    merchantOnboardService.rollBackFunctionForMerchantOnboard(merchantOnboardBean);
				    addActionError("Error occured while adding Merchant Details.");
				    getBasicDetailsForAdd();
				    return ERROR;
				}
				
				if(merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
                	String makerEmailID = merchantOnboardService.getUserEmailID(userId);
                	if (makerEmailID != null && !makerEmailID.isEmpty()) {
                		sendAcknowledgementEmailToMaker(makerEmailID, userId, merchantOnboardBean.getMid(), merchantOnboardBean.getLegalname());
					}
				}
				addActionMessage(getText("MerchantOnboardMakerAction.addSuccess")+" "+merchantOnboardBean.getRefSqid());
				logUtil.logMethodExit("addMerchantOnboardPageDetails()");
				logUtil.audit("Add Merchant Onboard Page Details - Exit", true, userGroup);
				merchantOnboardBean.setOnboardMethod(null);
				merchantOnboardBean.setSelectSearchBy(null);
				merchantOnboardBean.setLegalname(null);
				merchantOnboardBean.setRefNo(null);
				merchantOnboardBean.setApplNum(null);
				merchantOnboardBean.setRefNo(null);
				listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
				return SUCCESS;		
			}
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().toLowerCase().contains("virus")) {
			    addActionError(getText("attachmentsaction.os_Level_Malware_Detected"));
			}
			logUtil.error("Error in addMerchantOnboardPageDetails()", e);
			addActionError(getText("MerchantOnboardMakerAction.addError"));
			try {
				getBasicDetailsForAdd();
			} catch (Exception e1) {
				logUtil.error("Error in getBasicDetailsForAdd()", e1);
			}
			return ERROR;
		}
	}
	
	public String showMerchantOnboardSearchPage() {
		try {
			getBasicDetailsForAdd();
		} catch (Exception e) {
			logUtil.error("Error in showMerchantOnboardSearchPage()", e);
		}
		return SUCCESS;
	}

	public String merchantOnboardSearchResultPage() {
		
		try {
			instId = httpServletRequest.getSession().getAttribute("instId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("instId").toString());
			dateFormat = (String) ServletActionContext.getContext().getSession().get("INST-DATEFRMT");
			merchantOnboardBean.setRefNo(null);
			listofmerchants =  merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
			if(httpServletRequest.getParameter("refNo") != null) {
				merchantOnboardService.updateSessionFlagForMerchant(instId, Integer.parseInt(httpServletRequest.getParameter("refNo")), 0, userId);
			}
		}catch (Exception e) {
			logUtil.error("Error in merchantOnboardSearchResultPage method", e);
		}
		return SUCCESS;
	}
	
	public String viewMerchantOnboardDetailsPage() {
		
		String refNo = "", additionalNewStoreFlag = "";
		try{
			if (httpServletRequest.getParameter("referralNo") != null) { refNo = httpServletRequest.getParameter("referralNo"); } 
			else { refNo = merchantOnboardBean.getRefNo(); }

			if (merchantOnboardBean.getRefNo() == null) {
				if (refNo != null && !("".equals(refNo))) {
					merchantOnboardBean.setRefNo(refNo);
				}
			}
			
			if (httpServletRequest.getParameter("additionalNewStoreFlag") != null) { additionalNewStoreFlag = httpServletRequest.getParameter("additionalNewStoreFlag"); } 
			else if(merchantOnboardBean.getAdditionalNewStoreFlag() != null) { additionalNewStoreFlag = merchantOnboardBean.getAdditionalNewStoreFlag()+""; }
			if (merchantOnboardBean.getAdditionalNewStoreFlag() == null) {
				if (additionalNewStoreFlag != null && !("".equals(additionalNewStoreFlag))) {
					merchantOnboardBean.setAdditionalNewStoreFlag(Integer.parseInt(additionalNewStoreFlag));
				}
			}
			
			if(merchantOnboardBean.getRefNo()!=null && !merchantOnboardBean.getRefNo().equalsIgnoreCase("")) {
				if(merchantOnboardBean.getAdditionalNewStoreFlag()==null) {
					merchantOnboardBean.setAdditionalNewStoreFlag(merchantOnboardService.getAdditionalNewStoreFlag(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId));
				}
			} else {
				merchantOnboardBean.setAdditionalNewStoreFlag(0);
			}
			
			if(merchantOnboardBean.getAdditionalNewStoreFlag() == 1) {
				
				merchantOnboardBean = merchantOnboardService.getMerchantMastDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getPricingMastDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getAccountMastDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getMerchantRemarksDetails(merchantOnboardBean,instId);
				merchantOnboardBean.setLstStoreBean(merchantOnboardService.getAllStoreAndTerminalDetails(merchantOnboardBean, instId));
				
				merchantOnboardBean = getDefaultValuesSet(merchantOnboardBean);
				getBasicDetailsForAdd();
				merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
				merchantTypeList = merchantOnboardService.getMerchantTypeList(instId);
				return SUCCESS;
				
			} else {
				merchantOnboardBean = merchantOnboardService.getMerchantDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getPricingDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getAccountDetails(merchantOnboardBean,instId);
				merchantOnboardBean.setLstStoreBean(merchantOnboardService.getAllStoreAndTerminalDetails(merchantOnboardBean, instId));
				
				merchantOnboardBean = getDefaultValuesSet(merchantOnboardBean);
				getBasicDetailsForAdd();
				merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
				merchantTypeList = merchantOnboardService.getMerchantTypeList(instId);
				return SUCCESS;
			}
			
			
		}catch(Exception e){
			logUtil.error("Error in viewMerchantOnboardDetailsPage", e);
			merchantOnboardBean.setRefNo(null);
			listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
			return ERROR;
		}
	}
	
	public String editMerchantOnboardDetailsPage() {
		logUtil.audit("Edit Merchant Onboard Details Page - Entering", true, userGroup);
		logUtil.logMethodEntry("editMerchantOnboardDetailsPage");
		String refNo = "", additionalNewStoreFlag = "0";
		try{
			
			if (httpServletRequest.getParameter("referralNo") != null) { refNo = httpServletRequest.getParameter("referralNo"); } 
			else { refNo = merchantOnboardBean.getRefNo(); }

			if (merchantOnboardBean.getRefNo() == null) {
				if (refNo != null && !("".equals(refNo))) {
					merchantOnboardBean.setRefNo(refNo);
				}
			}
			
			if (httpServletRequest.getParameter("additionalNewStoreFlag") != null) { additionalNewStoreFlag = httpServletRequest.getParameter("additionalNewStoreFlag"); } 
			else { additionalNewStoreFlag = String.valueOf(merchantOnboardBean.getAdditionalNewStoreFlag()); }
			if (merchantOnboardBean.getAdditionalNewStoreFlag() == null) {
				if (additionalNewStoreFlag != null && !("".equals(additionalNewStoreFlag))) {
					merchantOnboardBean.setAdditionalNewStoreFlag(Integer.parseInt(additionalNewStoreFlag));
				}
			}
			
			if(merchantOnboardBean.getRefNo()!=null && !merchantOnboardBean.getRefNo().equalsIgnoreCase("")) {
				if(merchantOnboardBean.getAdditionalNewStoreFlag()==null) {
					merchantOnboardBean.setAdditionalNewStoreFlag(merchantOnboardService.getAdditionalNewStoreFlag(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId));
				}
			} else {
				merchantOnboardBean.setAdditionalNewStoreFlag(0);
			}
			
			if(merchantOnboardBean.getAdditionalNewStoreFlag() == 1) {
				int sessionFlag = merchantOnboardService.checkSessionForMerchant(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId);
				if(sessionFlag==0) {
					merchantOnboardBean = merchantOnboardService.getMerchantMastDetails(merchantOnboardBean,instId);
					merchantOnboardBean = merchantOnboardService.getPricingMastDetails(merchantOnboardBean,instId);
					merchantOnboardBean = merchantOnboardService.getAccountMastDetails(merchantOnboardBean,instId);
					merchantOnboardBean = merchantOnboardService.getMerchantRemarksDetails(merchantOnboardBean,instId);
					merchantOnboardBean.setLstStoreBean(merchantOnboardService.getAllStoreAndTerminalDetails(merchantOnboardBean, instId));
					
					
					merchantOnboardBean = getDefaultValuesSet(merchantOnboardBean);
					getBasicDetailsForAdd();
					merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
					merchantTypeList = merchantOnboardService.getMerchantTypeList(instId);
					merchantOnboardService.updateSessionFlagForMerchant(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), 1, userId);
					logUtil.logMethodExit("editMerchantOnboardDetailsPage");
					logUtil.audit("Edit Merchant Onboard Details Page - Exit", true, userGroup);
					return SUCCESS;
				}else {
					addActionError(getText("SESSERROR0001")+" "+merchantOnboardBean.getRefNo());
					merchantOnboardBean.setRefNo(null);
					listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
					return ERROR;
				}
			} else {
				int sessionFlag = merchantOnboardService.checkSessionForMerchant(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId);
				if(sessionFlag==0) {
					merchantOnboardBean = merchantOnboardService.getMerchantDetails(merchantOnboardBean,instId);
					merchantOnboardBean = merchantOnboardService.getPricingDetails(merchantOnboardBean,instId);
					merchantOnboardBean = merchantOnboardService.getAccountDetails(merchantOnboardBean,instId);
					merchantOnboardBean.setLstStoreBean(merchantOnboardService.getAllStoreAndTerminalDetails(merchantOnboardBean, instId));
					
					merchantOnboardBean = getDefaultValuesSet(merchantOnboardBean);
					getBasicDetailsForAdd();
					merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
					merchantTypeList = merchantOnboardService.getMerchantTypeList(instId);
					merchantOnboardService.updateSessionFlagForMerchant(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), 1, userId);
					logUtil.logMethodExit("editMerchantOnboardDetailsPage");
					logUtil.audit("Edit Merchant Onboard Details Page - Exit", true, userGroup);
					return SUCCESS;
				}else {
					addActionError(getText("SESSERROR0001")+" "+merchantOnboardBean.getRefNo());
					merchantOnboardBean.setRefNo(null);
					listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
					return ERROR;
				}
			}
			
		}catch(Exception e){
			logUtil.error("Error in editMerchantOnboardDetailsPage", e);
			merchantOnboardBean.setRefNo(null);
			listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
			return ERROR;
		}
	}
	
	public MerchantOnboardBean getMerchantOnboardDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		try {
			if(merchantOnboardBean.getAdditionalNewStoreFlag() == 1) {
				merchantOnboardBean = merchantOnboardService.getMerchantMastDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getPricingMastDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getAccountMastDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getMerchantRemarksDetails(merchantOnboardBean,instId);
				merchantOnboardBean.setLstStoreBean(merchantOnboardService.getAllStoreAndTerminalDetails(merchantOnboardBean, instId));
				
				merchantOnboardBean = getDefaultValuesSet(merchantOnboardBean);
				getBasicDetailsForAdd();
				merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
				merchantTypeList = merchantOnboardService.getMerchantTypeList(instId);
			} else {
				merchantOnboardBean = merchantOnboardService.getMerchantDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getPricingDetails(merchantOnboardBean,instId);
				merchantOnboardBean = merchantOnboardService.getAccountDetails(merchantOnboardBean,instId);
				merchantOnboardBean.setLstStoreBean(merchantOnboardService.getAllStoreAndTerminalDetails(merchantOnboardBean, instId));
	
				merchantOnboardBean = getDefaultValuesSet(merchantOnboardBean);
				getBasicDetailsForAdd();
				merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
				merchantTypeList = merchantOnboardService.getMerchantTypeList(instId);
			}
		} catch (Exception e) {
			logUtil.error("Error in getMerchantOnboardDetails"+ e.getMessage());
		}
		return merchantOnboardBean;
	}
	
	public String updateMerchantOnboardPageDetails() {
		logUtil.audit("Update Merchant Onboard Page Details - Entering", true, userGroup);
		logUtil.logMethodEntry("updateMerchantOnboardPageDetails");
		boolean response = false;
		try {
			userId = httpServletRequest.getSession().getAttribute("userId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("userId").toString());
			instId = httpServletRequest.getSession().getAttribute("instId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("instId").toString());
			merchantOnboardBean.setInstCode(instId);
			merchantOnboardBean.setUserId(userId.toString());
			
			int sessionActivityFlag = merchantOnboardService.checkSessionForMerchantActivity(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId);
			if(sessionActivityFlag==0) {
				addActionError("Merchant Update Activity Expired try again...");
				merchantOnboardBean.setOnboardMethod(null);
				merchantOnboardBean.setSelectSearchBy(null);
				merchantOnboardBean.setLegalname(null);
				merchantOnboardBean.setRefNo(null);
				merchantOnboardBean.setApplNum(null);
				merchantOnboardBean.setRefNo(null);
				listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
				return SUCCESS;
			}
			
			if(merchantOnboardBean.getRefNo()!=null && !merchantOnboardBean.getRefNo().equalsIgnoreCase("")) {
				if(merchantOnboardBean.getAdditionalNewStoreFlag()==null) {
					merchantOnboardBean.setAdditionalNewStoreFlag(merchantOnboardService.getAdditionalNewStoreFlag(instId, Integer.parseInt(merchantOnboardBean.getRefNo()), userId));
				}
			} else {
				merchantOnboardBean.setAdditionalNewStoreFlag(0);
			}
			
			if(merchantOnboardBean.getAdditionalNewStoreFlag() == 1) {
				
				ParameterBean parameterBean1 = parameterValuesServiceImpl.getParameterData(MidInstParam, instId);
				setAutoGenerateMid(Integer.parseInt(parameterBean1.getParameterValue()));
				ParameterBean parameterBean = parameterValuesServiceImpl.getParameterData(SidInstParam, instId);
				setAutoGenerateSid(Integer.parseInt(parameterBean.getParameterValue()));
				ParameterBean parameterBean2 = parameterValuesServiceImpl.getParameterData(TidInstParam, instId);
				setAutoGenerateTid(Integer.parseInt(parameterBean2.getParameterValue()));
				
				String validateMerchantDetails = validateForAddNewStoreAfterMerchantOnBoard();
				if (validateMerchantDetails != null && validateMerchantDetails.equalsIgnoreCase("input")) {
					merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
					if(merchantOnboardBean.getLstStoreBean() !=null && merchantOnboardBean.getLstStoreBean().size() != 0) {
						for (int i = 0; i < merchantOnboardBean.getLstStoreBean().size(); i++) {
		            		MerchantOnboardStoreBean storeBean = merchantOnboardBean.getLstStoreBean().get(i);
				            if (storeBean != null) {
				            	merchantOnboardBean.getLstStoreBean().get(i).setTermneed(storeBean.getTermneed() == null ? "0" : storeBean.getTermneed().equals("true") ? "1" : storeBean.getTermneed().equals("false") ? "0" : storeBean.getTermneed());
				            	merchantOnboardBean.getLstStoreBean().get(i).setMerchantEntityName(TRANSACTIONSOURCE);
				            	merchantOnboardBean.getLstStoreBean().get(i).setPrfDocsProvList(
				            	merchantOnboardService.getPrfDocsProvList(instId, storeBean.getPrfDocsProv()));
				            }
						}
					}
					getBasicDetailsForAdd();
					return INPUT;
				}
				
				merchantOnboardBean.setIncorpstatus("0");
				
				byte storecount = 0, terminalcount =0;
				for (MerchantOnboardStoreBean storeBean : merchantOnboardBean.getLstStoreBean()) {
					if (storeBean != null && storeBean.getStoreSqid() != null && !storeBean.getStoreSqid().equals("")) {
						storecount++;
						storeBean.setTermneed(storeBean.getTermneed() == null ? "0" : storeBean.getTermneed().equals("true") ? "1" : storeBean.getTermneed().equals("false") ? "0" : storeBean.getTermneed());
						storeBean.setMerchantEntityName(TRANSACTIONSOURCE);
						merchantOnboardBean = merchantOnboardService.updateStoreWithAccountDetails(merchantOnboardBean, storeBean, instId, userId);
						if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") && storeBean.getTermneed() != null && storeBean.getTermneed().equals("1")) {
							List<MerchantOnboardTerminalBean> terminalBeans = storeBean.getLstTerminalBean();
						    if (terminalBeans != null) {
						    	for (MerchantOnboardTerminalBean terminalBean : terminalBeans) {
						    		if (terminalBean != null && terminalBean.getTermSqid() != null) {
						    			terminalcount++;
						    			if(terminalBean.getDftTrmlRcrngChrgePlnId() == null || terminalBean.getDftTrmlRcrngChrgePlnId().trim().equals("")) {
						            		terminalBean.setDftTrmlRcrngChrgePlnId(null);
						            		terminalBean.setDftTrmlRcrngChrgePlnIdname(null);
						    			}
							            merchantOnboardBean = merchantOnboardService.updateTerminalWithAccountDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId);
							            if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
							               	merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
							               	merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
							               	addActionError("Error occured while updating Add new Store-"+storecount+" - Terminal-"+terminalcount+" with Account Details.");
							               	getBasicDetailsForAdd();
							               	return ERROR;
							            }						                        
							         }else if(terminalBean != null) {
							         	terminalcount++;
							            if(getAutoGenerateTid()==1) {
							               terminalBean.setTermCode(merchantOnboardService.getAutoGenerateCode(instId, 3));
							            }
							            if(terminalBean.getDftTrmlRcrngChrgePlnId() == null || terminalBean.getDftTrmlRcrngChrgePlnId().trim().equals("")) {
						            		terminalBean.setDftTrmlRcrngChrgePlnId(null);
						            		terminalBean.setDftTrmlRcrngChrgePlnIdname(null);
						    			} 
										keyConfigBean=masterListService.getDEKClearKey(String.valueOf(instId));
							            if(keyConfigBean!=null && (keyConfigBean.getClearDEKKey()!=null && !keyConfigBean.getClearDEKKey().isEmpty())){
							            	Map<String, String> maps = new AESEncDecUtil().generateTerminalKeys();
								            terminalBean.setTerminalGenerateRandomKey(maps.get(KID));
							            	terminalBean.setTerminalSecretKey(KeyConfigUtils.DataEncrypt(KeyConfigUtils.stringToByteArray(maps.get(KEY)),keyConfigBean.getKeyAlgDesc(), keyConfigBean.getClearDEKKey()));
							            }
							            merchantOnboardBean = merchantOnboardService.addTerminalWithAccountDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId);
							            if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
							                merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
							                merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
							                addActionError("Error occured while adding Add new Store-"+storecount+" - Terminal-"+terminalcount+" with Account Details.");
							                getBasicDetailsForAdd();
							                return ERROR;
							            }
										if (terminalBean.getTerminalPriceDetailsRequired().equals("1") && merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")){
											merchantOnboardBean = merchantOnboardService.addTerminalPriceDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId, terminalLevel);
										    if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
										    	merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
										        getMerchantOnboardDetails(merchantOnboardBean, instId);
									            addActionError("Error occured while adding Store -"+storecount+" - Terminal- "+terminalcount+" with Price Details.");
									            getBasicDetailsForAdd();
									            return ERROR;
											}
									    }
							         }
							     }
							      merchantOnboardBean = merchantOnboardService.insertOrUpdateToProdTemp(merchantOnboardBean, storeBean, instId);
							      terminalcount=0;
						     }
						}else if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") && storeBean.getTermneed() != null && storeBean.getTermneed().equals("0")) {
						      merchantOnboardService.deleteTerminalDetails(merchantOnboardBean, storeBean, instId, userId);
			            }else if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
			                  merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
			                  merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
			                  addActionError("Error occured while updating Add new Store-"+storecount+" with Account Details.");
			                  getBasicDetailsForAdd();
			                  return ERROR;
			            }
					}
					storecount = 0;
				}
				
				merchantOnboardService.updateAddnewStoreFlag(merchantOnboardBean, 1, userId);
				
				addActionMessage(getText("MerchantOnboardMakerAction.updateSuccess") + " " + merchantOnboardBean.getRefSqid());
				logUtil.logMethodExit("updateMerchantOnboardPageDetails()");
				logUtil.audit("Update Merchant Onboard Page Details - Exit", true, userGroup);
				merchantOnboardBean.setOnboardMethod(null);
				merchantOnboardBean.setSelectSearchBy(null);
				merchantOnboardBean.setLegalname(null);
				merchantOnboardBean.setRefNo(null);
				merchantOnboardBean.setApplNum(null);
				merchantOnboardBean.setRefNo(null);
				listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
				return SUCCESS;
			} else {
				
				// Code added by Narayanan S on 15-04-2025 for Attachment File Scanner - starts
				String clamAVCheck = masterListService.getClamAVCheck(CLAMAV_KEY);
			    if (clamAVCheck != null && "1".equals(clamAVCheck)) {
					if (!System.getProperty("os.name").startsWith("Windows")){
						if (merchantOnboardBean.getMercLogo().isFile() && merchantOnboardBean.getMercLogo()!=null) {
							try {
								if (scannerUtil.ping()) {
									byte[] fileBytes = Files.readAllBytes(merchantOnboardBean.getMercLogo().toPath());
		
										try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
											response = scannerUtil.scan(inputStream);
											if (response) {
												addActionError(getText("MerchantOnboardMakerAction.malware_Found") + uploadMercLogoFileName);
												return ERROR;
								            }
										} catch (Exception e) {
											logUtil.error("Scan failed: " + uploadMercLogoFileName + " " + e.getMessage());
											addActionError(getText("MerchantOnboardMakerAction.scan_Failed") + uploadMercLogoFileName);
											return ERROR;
										}
								} else {
									logUtil.error("ClamAV not reachable.");
									addActionError(getText("MerchantOnboardMakerAction.clamAV_Unreachable"));
									return ERROR;
								}
							}catch (Exception e) {
								logUtil.error("Error while scanning: " + uploadMercLogoFileName + " " + e.getMessage());
								addActionError(getText("MerchantOnboardMakerAction.error_Scan") + uploadMercLogoFileName);
								return ERROR;
							}
						}
					}
			    }
				// Code added by Narayanan S on 15-04-2025 for Attachment File Scanner - ends
				
				if (merchantOnboardBean.getMercLogo() != null) { 
				    byte[] imageData = new byte[(int) merchantOnboardBean.getMercLogo().length()];
				    try (FileInputStream fis = new FileInputStream(merchantOnboardBean.getMercLogo())) { 
				        fis.read(imageData); 
				    }
				    merchantOnboardBean.setMercLogoUpldData(imageData); 
				    httpServletRequest.getSession().setAttribute("imageData", imageData); 
				    merchantOnboardBean.setMercLogoDisplayData(Base64.getEncoder().encodeToString(imageData)); 
				} else {
				    byte[] sessionImageData = (byte[]) httpServletRequest.getSession().getAttribute("imageData");
				    if (sessionImageData != null) {
				        merchantOnboardBean.setMercLogoUpldData(sessionImageData);
				        merchantOnboardBean.setMercLogoDisplayData(Base64.getEncoder().encodeToString(sessionImageData));
				    } else {
				    	merchantOnboardBean = merchantOnboardService.getMerchantLogoDetails(merchantOnboardBean, instId, userId);
				    }
				}
				
				String validateMerchantDetails = validateMerchantDetails();
				if (validateMerchantDetails != null && validateMerchantDetails.equalsIgnoreCase("input")) {
					merchantEntityList = merchantOnboardService.getMerchantEntityList(instId, merchantOnboardBean.getMerchantType());
					if(merchantOnboardBean.getLstStoreBean() !=null && merchantOnboardBean.getLstStoreBean().size() != 0) {
						for (int i = 0; i < merchantOnboardBean.getLstStoreBean().size(); i++) {
		            		MerchantOnboardStoreBean storeBean = merchantOnboardBean.getLstStoreBean().get(i);
				            if (storeBean != null) {
				            	merchantOnboardBean.getLstStoreBean().get(i).setTermneed(storeBean.getTermneed() == null ? "0" : storeBean.getTermneed().equals("true") ? "1" : storeBean.getTermneed().equals("false") ? "0" : storeBean.getTermneed());
				            	merchantOnboardBean.getLstStoreBean().get(i).setMerchantEntityName(TRANSACTIONSOURCE);
				            	merchantOnboardBean.getLstStoreBean().get(i).setPrfDocsProvList(
				            	merchantOnboardService.getPrfDocsProvList(instId, storeBean.getPrfDocsProv()));
				            }
						}
					}
					getBasicDetailsForAdd();
					return INPUT;
				}
				
				if(merchantOnboardBean.getMercAggr()== null || merchantOnboardBean.getMercAggr().trim().equals("")) {
					merchantOnboardBean.setMercAggr(null);
					merchantOnboardBean.setMercAggrName(null);
					merchantOnboardBean.setMercAggrrelm(null);
				}
				merchantOnboardBean.setIncorpstatus("0");
				
				merchantOnboardBean = merchantOnboardService.updateMerchantDetails(merchantOnboardBean, instId, userId);
				if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
					merchantOnboardBean = merchantOnboardService.updatePriceDetails(merchantOnboardBean, instId, userId);
					if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
						if(merchantOnboardBean.getAcctMapLvl() != null  && (merchantOnboardBean.getAcctMapLvl() != 0 
					    		|| (merchantOnboardBean.getAcctMapLvl() == 0 && merchantOnboardBean.getMerchantAccountRequired()==1)) ) {
							merchantOnboardBean = merchantOnboardService.updateAccountDetails(merchantOnboardBean, instId, userId);
						}else if(merchantOnboardBean.getAcctMapLvl() != null && (merchantOnboardBean.getAcctMapLvl() != 0 
					    		|| (merchantOnboardBean.getAcctMapLvl() == 0 && merchantOnboardBean.getMerchantAccountRequired()==0))){
							merchantOnboardService.deleteAccountDetails(merchantOnboardBean, merchantLevel, instId, userId);
						}
						if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
		                	merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
		                	merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
		                	addActionError("Error occured while updating Merchant Account Details.");
		                	getBasicDetailsForAdd();
		                	return ERROR;
				    	}
				    }else if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
				    	merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
				    	merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
				    	addActionError("Error occured while updating Price Details.");
				    	getBasicDetailsForAdd();
				    	return ERROR;
				    }
						if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")) {
							byte storecount = 0, terminalcount =0;
							for (MerchantOnboardStoreBean storeBean : merchantOnboardBean.getLstStoreBean()) {
								 if (storeBean != null && storeBean.getStoreSqid() != null && !storeBean.getStoreSqid().equals("")) {
									storecount++;
									storeBean.setTermneed(storeBean.getTermneed() == null ? "0" : storeBean.getTermneed().equals("true") ? "1" : storeBean.getTermneed().equals("false") ? "0" : storeBean.getTermneed());
									storeBean.setMerchantEntityName(TRANSACTIONSOURCE);
									merchantOnboardBean = merchantOnboardService.updateStoreWithAccountDetails(merchantOnboardBean, storeBean, instId, userId);
									if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") && storeBean.getTermneed() != null && storeBean.getTermneed().equals("1")) {
							            List<MerchantOnboardTerminalBean> terminalBeans = storeBean.getLstTerminalBean();
						                	if (terminalBeans != null) {
							                    for (MerchantOnboardTerminalBean terminalBean : terminalBeans) {
							                        if (terminalBean != null && terminalBean.getTermSqid() != null) {
							                        	terminalcount++;
							                        	if(terminalBean.getDftTrmlRcrngChrgePlnId() == null || terminalBean.getDftTrmlRcrngChrgePlnId().trim().equals("")) {
										            		terminalBean.setDftTrmlRcrngChrgePlnId(null);
										            		terminalBean.setDftTrmlRcrngChrgePlnIdname(null);
										    			}
							                            merchantOnboardBean = merchantOnboardService.updateTerminalWithAccountDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId);
							                            if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
							                            	merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
							                            	merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
							                            	addActionError("Error occured while updating Store-"+storecount+" - Terminal-"+terminalcount+" with Account Details.");
							                            	getBasicDetailsForAdd();
							                            	return ERROR;
							                            }						                        
							                        }else if(terminalBean != null) {
							                        	terminalcount++;
							                        	if(getAutoGenerateTid()==1) {
							                				terminalBean.setTermCode(merchantOnboardService.getAutoGenerateCode(instId, 3));
							                			}
							                        	if(terminalBean.getDftTrmlRcrngChrgePlnId() == null || terminalBean.getDftTrmlRcrngChrgePlnId().trim().equals("")) {
										            		terminalBean.setDftTrmlRcrngChrgePlnId(null);
										            		terminalBean.setDftTrmlRcrngChrgePlnIdname(null);
										    			} 
														keyConfigBean=masterListService.getDEKClearKey(String.valueOf(instId));
														if(keyConfigBean!=null && (keyConfigBean.getClearDEKKey()!=null && !keyConfigBean.getClearDEKKey().isEmpty())){
															Map<String, String> maps = new AESEncDecUtil().generateTerminalKeys();
															terminalBean.setTerminalGenerateRandomKey(maps.get(KID));
															terminalBean.setTerminalSecretKey(KeyConfigUtils.DataEncrypt(KeyConfigUtils.stringToByteArray(maps.get(KEY)),keyConfigBean.getKeyAlgDesc(), keyConfigBean.getClearDEKKey()));
														}
							                            merchantOnboardBean = merchantOnboardService.addTerminalWithAccountDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId);
							                            if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
							                            	merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
							                            	merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
							                            	addActionError("Error occured while adding Store-"+storecount+" - Terminal-"+terminalcount+" with Account Details.");
							                            	getBasicDetailsForAdd();
							                            	return ERROR;
							                            }
							                            if (terminalBean.getTerminalPriceDetailsRequired().equals("1") && merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS")){
										                	merchantOnboardBean = merchantOnboardService.addTerminalPriceDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId, terminalLevel);
										                	if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
										                		merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
										                		getMerchantOnboardDetails(merchantOnboardBean, instId);
									                            addActionError("Error occured while adding Store -"+storecount+" - Terminal- "+terminalcount+" with Price Details.");
									                            getBasicDetailsForAdd();
									                            return ERROR;
											                }
										                }
							                        }
							                    }
							                    merchantOnboardBean = merchantOnboardService.insertOrUpdateToProdTemp(merchantOnboardBean, storeBean, instId);
							                    terminalcount=0;
						                	}
						            }else if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") && storeBean.getTermneed() != null && storeBean.getTermneed().equals("0")) {
						            	merchantOnboardService.deleteTerminalDetails(merchantOnboardBean, storeBean, instId, userId);
			                        }else if (merchantOnboardBean.getErrorstatus().equalsIgnoreCase("SUCCESS") == false) {
			                          	merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
			                          	merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
			                          	addActionError("Error occured while updating Store-"+storecount+" with Account Details.");
			                          	getBasicDetailsForAdd();
			                          	return ERROR;
			                        }
								}
								storecount = 0;
							}
						}else {
					        merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
					        merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
					        addActionError("Error occured while updating Price Details.");				        
					        getBasicDetailsForAdd();
					        return ERROR;
					    }
				}else {
			        merchantOnboardService.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
			        merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
			        addActionError("Error occured while updating Merchant Details.");
			        getBasicDetailsForAdd();
			        return ERROR;
			    }
				
				addActionMessage(getText("MerchantOnboardMakerAction.updateSuccess") + " " + merchantOnboardBean.getRefSqid());
				logUtil.logMethodExit("updateMerchantOnboardPageDetails()");
				logUtil.audit("Update Merchant Onboard Page Details - Exit", true, userGroup);
				merchantOnboardBean.setOnboardMethod(null);
				merchantOnboardBean.setSelectSearchBy(null);
				merchantOnboardBean.setLegalname(null);
				merchantOnboardBean.setRefNo(null);
				merchantOnboardBean.setApplNum(null);
				merchantOnboardBean.setRefNo(null);
				listofmerchants = merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
				return SUCCESS;
			}
		} catch (Exception e) {
			logUtil.error("Error in updateMerchantOnboardPageDetails", e);
			if (e.getMessage() != null && e.getMessage().toLowerCase().contains("virus")) {
			    addActionError(getText("MerchantOnboardMakerAction.os_Level_Malware_Detected"));
			}
			addActionError(getText("MerchantOnboardMakerAction.updateError"));
			merchantOnboardBean = getMerchantOnboardDetails(merchantOnboardBean, instId);
			try {
				getBasicDetailsForAdd();
			} catch (Exception e1) {
				logUtil.error("Error in getBasicDetailsForAdd", e1);
			}
			return ERROR;
		}
	}
	
	//For New Onboard Flow - Activate OR Deactivate Merchant(s)--Added by Dhileep
	public String activateORdeactivateMerchantOnboardDetails() {
		
		logUtil.audit("Activate OR Deactivate Merchant Onboard Details - Entering", true, userGroup);
		logUtil.logMethodEntry("activateORdeactivateMerchantOnboardDetails");
		String refNo = null, action = null;
		
		try {
				if(httpServletRequest.getParameter("referralNo") != null){ refNo = httpServletRequest.getParameter("referralNo"); } else { refNo = merchantOnboardBean.getRefNo(); }
				if(merchantOnboardBean.getRefNo() == null) { merchantOnboardBean.setRefNo(refNo); }
				if(httpServletRequest.getParameter("action") != null) { action = httpServletRequest.getParameter("action"); } else { action = merchantOnboardBean.getViewAction(); }
				if(merchantOnboardBean.getViewAction() == null) { if (action != null && !("".equals(action))) { merchantOnboardBean.setViewAction(action); } }
				if(merchantOnboardBean.getRemarks() == null) {	merchantOnboardBean.setRemarks(httpServletRequest.getParameter("remarkmsg")); }
				if(merchantOnboardBean.getOnboardMethod() == null) { merchantOnboardBean.setOnboardMethod(httpServletRequest.getParameter("remarkmsg")); }
				
				List<MerchantOnboardStoreBean> listofStoreDetails= merchantOnboardService.getAllStoreAndTerminalDetails(merchantOnboardBean, instId);
								
				if(merchantOnboardBean.getViewAction().equalsIgnoreCase("Activate")){
					
					merchantOnboardBean = merchantOnboardService.activateMerchantOnBoard(merchantOnboardBean,instId);
					
					if(merchantOnboardBean.getFlag() != null && merchantOnboardBean.getFlag().equalsIgnoreCase("Activated")) {
						
						for(MerchantOnboardStoreBean storebean :listofStoreDetails) {
		                	merchantWelcomeLetEnvironmentEmail(merchantOnboardBean.getRefNo(), merchantOnboardService.getMerchantIDusingRefno(merchantOnboardBean.getRefNo()), 
		                			 merchantOnboardService.getMerchantEmailIDusingRefno(merchantOnboardBean.getRefNo()), storebean);
		                }
						listofStoreDetails = null;
						addActionMessage(getText("MSLV500001")+" "+merchantOnboardBean.getRefNo());
						merchantOnboardBean.setRefNo(null);
						listofmerchants =  merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
						logUtil.logMethodExit("activateORdeactivateMerchantOnboardDetails");
						logUtil.audit("Activate OR Deactivate Merchant Onboard Details - Exit", true, userGroup);
						return SUCCESS;
					}else {
						addActionError(getText("MSLV500002")+" "+merchantOnboardBean.getRefNo());
						merchantOnboardBean.setRefNo(null);
						listofmerchants =  merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
						logUtil.logMethodExit("activateORdeactivateMerchantOnboardDetails");
						logUtil.audit("Activate OR Deactivate Merchant Onboard Details - Exit", true, userGroup);
						return ERROR;
					   }
				   } else if(merchantOnboardBean.getViewAction().equalsIgnoreCase("Deactivate")) {
						    
						  merchantOnboardBean = merchantOnboardService.deactivateMerchantOnBoard(merchantOnboardBean,instId);
						    
						  if(merchantOnboardBean.getFlag() != null && merchantOnboardBean.getFlag().equalsIgnoreCase("Deactivated")) {
						    addActionMessage(getText("MSLV500003")+" "+merchantOnboardBean.getRefNo());
						    merchantOnboardBean.setRefNo(null);
							listofmerchants =  merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
							logUtil.logMethodExit("activateORdeactivateMerchantOnboardDetails");
							logUtil.audit("Activate OR Deactivate Merchant Onboard Details - Exit", true, userGroup);
							return SUCCESS;
						  }else {
						    addActionError(getText("MSLV500004")+" "+merchantOnboardBean.getRefNo());
						    merchantOnboardBean.setRefNo(null);
						    listofmerchants =  merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
							logUtil.logMethodExit("activateORdeactivateMerchantOnboardDetails");
							logUtil.audit("Activate OR Deactivate Merchant Onboard Details - Exit", true, userGroup);
							return ERROR;
						    }
						}
		 } catch(Exception e) {
			merchantOnboardBean.setRefNo(null);
			listofmerchants =  merchantOnboardService.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
			logUtil.error("Error in actiavteORdeactiavteMerchantOnboardDetails", e);
			logUtil.audit("Activate OR Deactivate Merchant Onboard Details - Exit", true, userGroup);
			return ERROR;
		 } finally {refNo = null; action = null;}
		
		return SUCCESS;
		
	}
	
	// Validation for Both Add and Edit Screen - Starts
	private String validateMerchantDetails() {
		String valid = null;
		try {
			ParameterBean parameterBean1 = parameterValuesServiceImpl.getParameterData(MidInstParam, instId);
			setAutoGenerateMid(Integer.parseInt(parameterBean1.getParameterValue()));
			ParameterBean parameterBean = parameterValuesServiceImpl.getParameterData(SidInstParam, instId);
			setAutoGenerateSid(Integer.parseInt(parameterBean.getParameterValue()));
			ParameterBean parameterBean2 = parameterValuesServiceImpl.getParameterData(TidInstParam, instId);
			setAutoGenerateTid(Integer.parseInt(parameterBean2.getParameterValue()));
		} catch (MapsGenException e) {
			logUtil.error("Error in getBasicDetailsForAdd", e);
		}
		
		//Referral Details
		if(getAutoGenerateMid()==0) {
			if(merchantOnboardBean.getMid()== null || merchantOnboardBean.getMid().trim().equals("")) {
				addActionError(getText("MSLV300000"));
				valid = "input";
			}	
			if(!(MAPSUtils.isNumeric(merchantOnboardBean.getMid()))){
				addActionError(getText("MSLV300001"));
				valid= "input";
			}
			if(merchantOnboardBean.getMid().trim().length() > 9) {
				addActionError(getText("MSLV400001"));
				valid = "input";
			}
		}
		if(merchantOnboardBean.getBankerSalesSqid()==null || merchantOnboardBean.getBankerSalesSqid().trim().equals("")) {
			merchantOnboardBean.setBankerSalesSqid(null);
			merchantOnboardBean.setBankersalesNames(null);
			addActionError(getText("MSLV300002"));
			valid = "input";
		}
		if(merchantOnboardBean.getLegalname()== null || merchantOnboardBean.getLegalname().trim().equals("")) {
			addActionError(getText("MSLV300003"));
			valid = "input";
		} else {
			if(merchantOnboardBean.getLegalname().startsWith(" ") || merchantOnboardBean.getLegalname().endsWith(" ")) {
	            addActionError(getText("MSLV300004"));
	            valid = "input";
			}
			if(merchantOnboardBean.getLegalname().length()< 2 || merchantOnboardBean.getLegalname().length() > 120) {
				addActionError(getText("MSLV300005"));
				valid = "input";
			}
			if(!(merchantOnboardBean.getLegalname().chars().allMatch(c -> Character.isLetterOrDigit(c)|| Character.isSpaceChar(c) || c == '&' || c == '.' || c == '(' || c == ')'))) {
                addActionError(getText("MSLV300006"));
                valid = "input";
            }
		}
		if(merchantOnboardBean.getDbaname()== null || merchantOnboardBean.getDbaname().trim().equals("")) {
			addActionError(getText("MSLV300007"));
			valid = "input";
		} else {
			if(merchantOnboardBean.getDbaname().startsWith(" ") || merchantOnboardBean.getDbaname().endsWith(" ")) {
	            addActionError(getText("MSLV300008"));
	            valid = "input";
	        }
			if(!merchantOnboardBean.getDbaname().chars().allMatch(c -> Character.isLetterOrDigit(c)|| Character.isSpaceChar(c) || c == '&' || c == '.' || c == '(' || c == ')')) {
				addActionError(getText("MSLV300009"));
				valid = "input";
			}		
		}
		if(merchantOnboardBean.getMercAggr()== null || merchantOnboardBean.getMercAggr().trim().equals("")) {
			merchantOnboardBean.setMercAggr(null);
			merchantOnboardBean.setMercAggrName(null);
			merchantOnboardBean.setMercAggrrelm(null);
		}
		
		// Customer Details
		if(merchantOnboardBean.getFirstname1()== null || merchantOnboardBean.getFirstname1().trim().equals("")) {
			addActionError(getText("MSLV300011"));
			valid = "input";
		} else {
			if(merchantOnboardBean.getFirstname1().startsWith(" ") || merchantOnboardBean.getFirstname1().endsWith(" ")) {
	            addActionError(getText("MSLV300012"));
	            valid = "input";
			}
			if(merchantOnboardBean.getFirstname1().length()< 2 || merchantOnboardBean.getFirstname1().length() > 100) {
				addActionError(getText("MSLV300013"));
				valid = "input";
			}
			if(!(merchantOnboardBean.getFirstname1().chars().allMatch(c -> Character.isLetter(c) || Character.isSpaceChar(c) || c == '-' || c == '\''))) {
                addActionError(getText("MSLV300014"));
                valid = "input";
            }
		}
		if(merchantOnboardBean.getLastname1()== null || merchantOnboardBean.getLastname1().trim().equals("")) {
			addActionError(getText("MSLV300114"));
			valid = "input";
		}
		else {
			if (merchantOnboardBean.getLastname1().startsWith(" ") || merchantOnboardBean.getLastname1().endsWith(" ")) {
	            addActionError(getText("MSLV300115"));
	            valid = "input";
			}
			if (merchantOnboardBean.getLastname1().length()< 1 || merchantOnboardBean.getLastname1().length() > 100) {
				addActionError(getText("MSLV300116"));
				valid = "input";
			}
			boolean isValid = merchantOnboardBean.getLastname1().chars().allMatch(c -> Character.isLetter(c) || Character.isSpaceChar(c) || c == '-' || c == '\'');
			if (!isValid) {
                addActionError(getText("MSLV300117"));
                valid = "input";
            }
		}
		if(merchantOnboardBean.getAddressline1()== null || merchantOnboardBean.getAddressline1().trim().equals("")) {
			addActionError(getText("MSLV300015"));
			valid = "input";
		} else {
			if(merchantOnboardBean.getAddressline1().startsWith(" ") || merchantOnboardBean.getAddressline1().endsWith(" ")) {
				addActionError(getText("MSLV300016"));
			    valid = "input";
		    }
		    if(!merchantOnboardBean.getAddressline1().matches("^[A-Za-z0-9;:.,`_ -]+$")) {
			    addActionError(getText("MSLV300017"));
			    valid = "input";
		    }
		}
		if(merchantOnboardBean.getCstate()== null || merchantOnboardBean.getCstate().trim().equals("")) {
			merchantOnboardBean.setCstate(null);
			merchantOnboardBean.setState(null);
			addActionError(getText("MSLV300019"));
			valid = "input";
		}
		if(merchantOnboardBean.getCselectcity()== null || merchantOnboardBean.getCselectcity().trim().equals("")) {
			merchantOnboardBean.setCselectcity(null);
			merchantOnboardBean.setSelectcity(null);
			addActionError(getText("MSLV300020"));
			valid = "input";
		}
		if(merchantOnboardBean.getPostalcode()== null || merchantOnboardBean.getPostalcode().trim().equals("")) {
			addActionError(getText("MSLV300021"));
			valid = "input";
		}
		if(merchantOnboardBean.getPhone()== null || merchantOnboardBean.getPhone().trim().equals("")) {
			addActionError(getText("MSLV300022"));
			valid = "input";
		} else {
			/*if(merchantOnboardBean.getPhone().trim().length() < 8 || merchantOnboardBean.getPhone().trim().length() > 10) {
			    addActionError(getText("MSLV300023")); 
			    valid = "input";
			}*/
		    if(merchantOnboardBean.getPhone().startsWith(" ") || merchantOnboardBean.getPhone().endsWith(" ")) {
	            addActionError(getText("MSLV300024"));
	            valid = "input";
	        }
		    /*if(merchantOnboardBean.getPhone().trim().equals("0")) {
		        addActionError(getText("MSLV300025"));
		        valid = "input";
		    }*/
		}
		if(merchantOnboardBean.getContactEmail()== null || merchantOnboardBean.getContactEmail().trim().equals("")) {
			addActionError(getText("MSLV300026"));
			valid = "input";
		} else {
			if(!merchantOnboardBean.getContactEmail().matches("^([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7})(\\s*,\\s*[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,7})*$")) {
			    addActionError(getText("MSLV300027")); 
			    valid = "input";
			}
		}
		if(merchantOnboardBean.getCpancard()== null || merchantOnboardBean.getCpancard().trim().equals("")) {
			addActionError(getText("MSLV300028"));
			valid = "input";
		} else {
			if(merchantOnboardBean.getCpancard().startsWith(" ") || merchantOnboardBean.getCpancard().endsWith(" ")) {
			    addActionError(getText("MSLV300029"));
			    valid = "input";
		    }
		}
		
		// Merchant Account Details - acctMapLvl --> 0-Aggregator Level, 1-Merchant Level, 2-Store Level and 3-Terminal Level
		if(merchantOnboardBean.getAcctMapLvl() != null && merchantOnboardBean.getAcctMapLvl() != 0
				|| (merchantOnboardBean.getAcctMapLvl() == 0 && merchantOnboardBean.getMerchantAccountRequired() == 1)) 
		{
			if(merchantOnboardBean.getAcctWithBank()!=null && merchantOnboardBean.getAcctWithBank() == 0) {
				if(merchantOnboardBean.getAcctBankText() == null || merchantOnboardBean.getAcctBankText().equals("")) {
					addActionError(getText("MSLV300072"));
					valid = "input";
				} else {
					merchantOnboardBean.setAcctBranch(null);
					merchantOnboardBean.setcAcctBranch(null);
				}
			} else if (merchantOnboardBean.getAcctWithBank()!=null && merchantOnboardBean.getAcctWithBank() == 1) {
				if(merchantOnboardBean.getcAcctBranch() == null || merchantOnboardBean.getcAcctBranch().equals("")
						|| merchantOnboardBean.getcAcctBranch().equalsIgnoreCase("--Select--")) {
					merchantOnboardBean.setAcctBranch(null);
					merchantOnboardBean.setcAcctBranch(null);
					addActionError(getText("MSLV300071"));
					valid = "input";
				} else {
					merchantOnboardBean.setAcctBankText(null);
				}
			} else {
				addActionError(getText("MSLV300070"));
				valid = "input";
			}
			if(merchantOnboardBean.getIfsc()== null || merchantOnboardBean.getIfsc().equals("")) {
				addActionError(getText("MSLV300073"));
				valid = "input";
			}
			if(!(merchantOnboardBean.getIfsc().length() == 8 || merchantOnboardBean.getIfsc().length() == 11)) {
				addActionError(getText("MSLV300074"));
				valid = "input";
			}
			if(merchantOnboardBean.getCurrency() == null || merchantOnboardBean.getCurrency().equals("") 
					|| merchantOnboardBean.getCurrency().equalsIgnoreCase("--Select--")) {
				addActionError(getText("MSLV300075"));
				valid = "input";
			}
			if(merchantOnboardBean.getAcctNumber() == null || merchantOnboardBean.getAcctNumber().trim().equals("")) {
				addActionError(getText("MSLV300076"));
				valid = "input";
			} else {
				if(merchantOnboardBean.getAcctNumber().startsWith(" ") || merchantOnboardBean.getAcctNumber().endsWith(" ")) {
					addActionError(getText("MSLV300077"));
					valid = "input";
				}
				if(merchantOnboardBean.getAcctNumber().length() < 6 || merchantOnboardBean.getAcctNumber().length() > 36) {
					addActionError(getText("MSLV300079"));
					valid = "input";
				}
			}
			String cifNumber = Optional.ofNullable(merchantOnboardBean.getCifNumber()).map(String::trim).filter(s -> !s.isEmpty() && !"null".equalsIgnoreCase(s)).orElse(null);
			if (cifNumber == null) {
				addActionError(getText("MSLV300121"));
				valid = "input";
			} else if (cifNumber.length() != 7) {
				addActionError(getText("MSLV300122"));
				valid = "input";
			}
		} else {
			merchantOnboardBean.setAcctWithBank(null);
			merchantOnboardBean.setAcctBankText(null);
			merchantOnboardBean.setAcctBranchText(null);
			merchantOnboardBean.setAcctBranch(null);
			merchantOnboardBean.setIfsc(null);
			merchantOnboardBean.setCurrency(null);
			merchantOnboardBean.setAcctNumber(null);
			merchantOnboardBean.setCifNumber(null);
			merchantOnboardBean.setDefAccount(null);
		}
		
		// Merchant Pricing Details - captureServiceFeeAt --> 1-Merchant Level, 2-Store Level and 3-Terminal Level
		if(merchantOnboardBean.getMerchantServiceFeePlan()== null || merchantOnboardBean.getMerchantServiceFeePlan().trim().equals("")
				|| merchantOnboardBean.getMerchantServiceFeePlan().equalsIgnoreCase("--Select--")
				|| merchantOnboardBean.getMerchantServiceFeePlan().equals(" ")) {
			addActionError(getText("MSLV300031"));
			valid = "input";
		}
		if(merchantOnboardBean.getMerchantType()== null || merchantOnboardBean.getMerchantType().equals("")) {
			addActionError(getText("MSLV300032"));
			valid = "input";
		}
		
		//Store Details
		if(merchantOnboardBean.getNoOfStoresReq() == null || merchantOnboardBean.getNoOfStoresReq().equals("")) {
			addActionError(getText("MSLV300054"));
			valid = "input";
		}
		// Multiple Store and Terminal Validation
		// Store Details --> Basic Store Details, Address Details, Account Details, Risk Details and Price Details
		// Terminal Details --> Basic Terminal Details, Account Details, Price Details, Rolling Reserve Fund Details and Security Deposit Recovery Details
		List<MerchantOnboardStoreBean> storeBeans = merchantOnboardBean.getLstStoreBean();
        if(storeBeans != null) {
          	for(int i = 0; i < storeBeans.size(); i++) {
           		MerchantOnboardStoreBean storeBean = storeBeans.get(i);
           		if(storeBean != null) {
           			if(getAutoGenerateSid()==0) {
            			if(storeBean.getStoreCode() == null || storeBean.getStoreCode().trim().equals("")) {
            				addActionError(getText("MSLV300049",new String[]{String.valueOf(i+1)}));
            				valid = "input";
            			}	
            			if(!(MAPSUtils.isAlphaNumeric(storeBean.getStoreCode()))){
            				addActionError(getText("MSLV300050",new String[]{String.valueOf(i+1)}));
            				valid = "input";
            			}
            			if (storeBean.getStoreCode().trim().length() < 8 || storeBean.getStoreCode().trim().length() > 15) {
            			    addActionError(getText("MSLV4000030", new String[]{String.valueOf(i+1)}));
            			    valid = "input";
            			}
            		}
           			if(storeBean.getStoreTradingName() == null || storeBean.getStoreTradingName().trim().equals("")) {
						addActionError(getText("MSLV300051",new String[]{String.valueOf(i+1)}));
						valid = "input";
					}
        			if(!(storeBean.getStoreTradingName().chars().allMatch(c -> Character.isLetterOrDigit(c)|| Character.isSpaceChar(c) || c == '&' || c == '.' || c == '(' || c == ')'))) {
                        addActionError(getText("MSLV300118",new String[]{String.valueOf(i+1)}));
                        valid = "input";
                    }
           			if(storeBean.getStoreEmail() == null || storeBean.getStoreEmail().trim().equals("")) {
						addActionError(getText("MSLV300062",new String[]{String.valueOf(i+1)}));
						valid = "input";
					} else {
						if(!storeBean.getStoreEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
						    addActionError(getText("MSLV300063",new String[]{String.valueOf(i+1)}));
						    valid = "input";
						}
					}
           			if(storeBean.getMerchantGrade() == null || storeBean.getMerchantGrade().trim().equals("")
							|| storeBean.getMerchantGrade().equalsIgnoreCase("--Select--")) {
						addActionError(getText("MSLV300052",new String[]{String.valueOf(i+1)}));
						valid = "input";
					}
           			if(storeBean.getNatureOfBusiness() == null || storeBean.getNatureOfBusiness().trim().equals("")
							|| storeBean.getNatureOfBusiness().equalsIgnoreCase("--Select--")) {
						addActionError(getText("MSLV400009",new String[]{String.valueOf(i+1)}));
						valid = "input";
					}
           			if(storeBean.getPrfDocsProv() == null || storeBean.getPrfDocsProv().trim().equals("")) {
						addActionError(getText("MSLV300061",new String[]{String.valueOf(i+1)}));
						valid = "input";
					}
           			if(storeBean.getMccautocomplete() == null || storeBean.getMccautocomplete().trim().equals("")) {
						addActionError(getText("MSLV300053",new String[]{String.valueOf(i+1)}));
						valid = "input";
					} else {
						if(storeBean.getCmccAutoComplete() == null || storeBean.getCmccAutoComplete().trim().equals("")) {
							addActionError(getText("MSLV4000010",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
					}
           			if(storeBean.getIncorpstatus() == null || storeBean.getIncorpstatus().trim().equals("")
							|| storeBean.getIncorpstatus().equalsIgnoreCase("--Select--")) {
						addActionError(getText("MSLV300056",new String[]{String.valueOf(i+1)}));
						valid = "input";
					}
           			if(storeBean.getGstNumber() == null || storeBean.getGstNumber().trim().equals("")) {
						addActionError(getText("MSLV300059",new String[]{String.valueOf(i+1)}));
						valid = "input";
					}
           			if(storeBean.getGovtRegMerchant() != null && storeBean.getGovtRegMerchant().equals("1")) {
						if(storeBean.getHomeCountryId() == null || storeBean.getHomeCountryId().trim().equals("")
								|| storeBean.getHomeCountryId().trim().equalsIgnoreCase("--Select--")) {
							addActionError(getText("MSLV300060",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
					} else {
						storeBean.setGovtRegMerchant("0");
						storeBean.setHomeCountryId(null);
					}
					if(storeBean.getWebSiteName() == null || storeBean.getWebSiteName().trim().equals("")) {
						addActionError(getText("MSLV300058",new String[]{String.valueOf(i+1)}));
						valid = "input";
					}
           			if(storeBean.getWebsiteAddress() == null || storeBean.getWebsiteAddress().trim().equals("")) {
						addActionError(getText("MSLV300057",new String[]{String.valueOf(i+1)}));
						valid = "input";
					}
           			if(storeBean.getPaymentFrecForCard() != null && storeBean.getPaymentFrecForCard().equals("5")) {
						if(storeBean.getPaymentFrecForCardDays() == null || storeBean.getPaymentFrecForCardDays() < 1) {
							addActionError(getText("MSLV4000011",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
					}
           			if(storeBean.getAaniPayTransPayment() != null && storeBean.getAaniPayTransPayment().equals("0")) {
           				if(storeBean.getPaymentFrecForAaniPay() != null && storeBean.getPaymentFrecForAaniPay().equals("5")) {
    						if(storeBean.getPaymentFrecForAaniPayDays() == null || storeBean.getPaymentFrecForAaniPayDays() < 1) {
    							addActionError(getText("MSLV4000012",new String[]{String.valueOf(i+1)}));
    							valid = "input";
    						}
    					}
           			}
           			// Store Pricing Details
           			if(storeBean.getStorePriceDetailsRequired()!= null && storeBean.getStorePriceDetailsRequired().equals("1")) {
	           			if(storeBean.getStoreMerchantServiceFeePlan()== null || storeBean.getStoreMerchantServiceFeePlan().trim().equals("")
	           					|| storeBean.getStoreMerchantServiceFeePlan().equalsIgnoreCase("--Select--")
	           					|| storeBean.getStoreMerchantServiceFeePlan().equals(" ")) {
	           				addActionError(getText("MSLV4000013",new String[]{String.valueOf(i+1)}));
	           				valid = "input";
	           			}
           			} else {
           				storeBean.setStoreCaptureServiceFeeAt(null);
           				storeBean.setStoreFeePlanType(null);
           				storeBean.setStoreMerchantServiceFeePlan(null);
           				storeBean.setStoreMercFeeDeduWaiver(null);
           				storeBean.setStorePriceDetailsRequired("0");
           			}
           			//Store Address Details
           			if(storeBean.getUseSameAsMercAddressForStore()!= null && storeBean.getUseSameAsMercAddressForStore().equals("1")) {
           				if(storeBean.getStoreaddressline1() == null || storeBean.getStoreaddressline1().trim().equals("")) {
           					addActionError(getText("MSLV300081",new String[]{String.valueOf(i+1)}));
           					valid = "input";
           				} else {
           					if(storeBean.getStoreaddressline1().startsWith(" ") || storeBean.getStoreaddressline1().endsWith(" ")) {
           						addActionError(getText("MSLV300082",new String[]{String.valueOf(i+1)}));
           					    valid = "input";
           				    }
           				    if(!storeBean.getStoreaddressline1().matches("^[A-Za-z0-9;:.,`_ -]+$")) {
           					    addActionError(getText("MSLV300083",new String[]{String.valueOf(i+1)}));
           					    valid = "input";
           				    }
           				}
           				if(storeBean.getStorecstate() == null || storeBean.getStorecstate().trim().equals("")) {
           					storeBean.setStorecstate(null);
           					storeBean.setStorestate(null);
           					addActionError(getText("MSLV300085",new String[]{String.valueOf(i+1)}));
           					valid = "input";
           				}
           				if(storeBean.getStorecselectcity()== null || storeBean.getStorecselectcity().trim().equals("")) {
           					storeBean.setStorecselectcity(null);
           					storeBean.setStoreselectcity(null);
           					addActionError(getText("MSLV300086",new String[]{String.valueOf(i+1)}));
           					valid = "input";
           				}
           				if(storeBean.getStorepostalcode() == null || storeBean.getStorepostalcode().trim().equals("")) {
           					addActionError(getText("MSLV300087",new String[]{String.valueOf(i+1)}));
           					valid = "input";
           				}
           			} else {
           				storeBean.setStoreaddressline1(null);
           				storeBean.setStorecountry(null);
           				storeBean.setStorestate(null);
           				storeBean.setStoreselectcity(null);
           				storeBean.setStorepostalcode(null);
           				storeBean.setStorebusinessContactEmailID(null);
           				storeBean.setStorebusinessContactMobileNo(null);
           				storeBean.setStoretechnicalContactEmailID(null);
           				storeBean.setStoretechnicalContactMobileNo(null);
           				storeBean.setUseSameAsMercAddressForStore("0");
           			}
           			// Store Risk Details
           			if(storeBean.getStoreRiskDetailsRequired() != null && storeBean.getStoreRiskDetailsRequired().equals("1")) {
						if (storeBean.getRiskCategory() == null || storeBean.getRiskCategory().trim().equals("")
								|| storeBean.getRiskCategory().equalsIgnoreCase("--Select--")) {
							addActionError(getText("MSLV300112",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getPadssPCIdssCertExpDate() != null && storeBean.getPadssPCIdssCertExpDate().trim().length()!=0) {
							if(isValidDate(storeBean.getPadssPCIdssCertExpDate())==false)
							{
								addActionError(getText("MSLV4000027",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
						}
						if (storeBean.getCloudCertExpDate() != null && storeBean.getCloudCertExpDate().trim().length()!=0) {
							if(isValidDate(storeBean.getCloudCertExpDate())==false)
							{
								addActionError(getText("MSLV4000028",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
						}
						if (storeBean.getTradeLicenseExpDate() != null && storeBean.getTradeLicenseExpDate().trim().length()!=0) {
							if(isValidDate(storeBean.getTradeLicenseExpDate())==false)
							{
								addActionError(getText("MSLV4000029",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
						}
						if (storeBean.getTransCountperMonth() == null || storeBean.getTransCountperMonth()==0) {
							addActionError(getText("MSLV400002",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getAverageTicketSize() == null || storeBean.getAverageTicketSize()==0) {
							addActionError(getText("MSLV400003",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getQuarterlySale() == null || storeBean.getQuarterlySale()==0) {
							addActionError(getText("MSLV400004",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getTransToRefundPercent() == null || storeBean.getTransToRefundPercent()==0) {
							addActionError(getText("MSLV400005",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getTransToChargebackPercent() == null || storeBean.getTransToChargebackPercent()==0) {
							addActionError(getText("MSLV400006",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getTransPercent() == null || storeBean.getTransPercent()==0) {
							addActionError(getText("MSLV400007",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getInternationalTransPercent() == null || storeBean.getInternationalTransPercent()==0) {
							addActionError(getText("MSLV400008",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
					}else {
						storeBean.setRiskCategory("2");
						storeBean.setPadssPCIdssCertExpDate(null);
						storeBean.setCloudCertExpDate(null);
						storeBean.setTradeLicenseExpDate(null);
						storeBean.setAdditionalUDFField1(null);
						storeBean.setAdditionalUDFField2(null);
						storeBean.setAdditionalUDFField3(null);
						storeBean.setAdditionalUDFValue1(null);
						storeBean.setAdditionalUDFValue2(null);
						storeBean.setAdditionalUDFValue3(null);
						storeBean.setTransCountperMonth(null);
						storeBean.setAverageTicketSize(null);
						storeBean.setQuarterlySale(null);
						storeBean.setTransToRefundPercent(null);
						storeBean.setTransToChargebackPercent(null);
						storeBean.setTransPercent(null);
						storeBean.setInternationalTransPercent(null);
						storeBean.setStoreRiskDetailsRequired("0");
					}
           			// Store Account Details
           			if(storeBean.getUseSameAsMercAccountForStore() != null && storeBean.getUseSameAsMercAccountForStore().equals("1")) {
						if (storeBean.getStoreacctWithBank()!=null && storeBean.getStoreacctWithBank() == 0) {
							if (storeBean.getStoreacctBankText() == null || storeBean.getStoreacctBankText().equals("")) {
								addActionError(getText("MSLV300090",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}else {
								storeBean.setStoreacctBranch(null);
								storeBean.setcStoreacctBranch(null);
							}
						}else if (storeBean.getStoreacctWithBank()!=null && storeBean.getStoreacctWithBank() == 1) {
							if (storeBean.getcStoreacctBranch() == null || storeBean.getcStoreacctBranch().equals("") 
									|| storeBean.getcStoreacctBranch().equalsIgnoreCase("--Select--")) {
								storeBean.setStoreacctBranch(null);
								storeBean.setcStoreacctBranch(null);
								addActionError(getText("MSLV300089",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}else {
								storeBean.setStoreacctBankText(null);
							}
						}else {
							addActionError(getText("MSLV300088",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getStoreifsc()== null || storeBean.getStoreifsc().equals("")) {
							addActionError(getText("MSLV300091",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if(!(storeBean.getStoreifsc().length() == 8 || storeBean.getStoreifsc().length() == 11)) {
							addActionError(getText("MSLV300092",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getStorecurrency() == null || storeBean.getStorecurrency().equals("")
								|| storeBean.getStorecurrency().equalsIgnoreCase("--Select--")) {
							addActionError(getText("MSLV300093",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if (storeBean.getStoreacctNumber() == null || storeBean.getStoreacctNumber().trim().equals("")) {
							addActionError(getText("MSLV300094",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}else {
							if(storeBean.getStoreacctNumber().startsWith(" ") || storeBean.getStoreacctNumber().endsWith(" ")) {
								addActionError(getText("MSLV300095",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getStoreacctNumber().length() < 6 || storeBean.getStoreacctNumber().length() > 36) {
								addActionError(getText("MSLV300097",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
						}
											
						String storecifNumber = Optional.ofNullable(storeBean.getStorecifNumber()).map(String::trim).filter(s -> !s.isEmpty() && !"null".equalsIgnoreCase(s)).orElse(null);
						if(storecifNumber == null) {
							addActionError(getText("MSLV300119",new String[]{String.valueOf(i+1)}));
							valid = "input";
						} else if (storecifNumber.length() != 7) {
						    addActionError(getText("MSLV300123",new String[]{String.valueOf(i+1)}));
						    valid = "input";
						}
					}else {
						storeBean.setStoreacctWithBank(null);
						storeBean.setStoreacctBranch(null);
						storeBean.setStoreacctBranchText(null);
						storeBean.setStoreacctBankText(null);
						storeBean.setStoreifsc(null);
						storeBean.setStorecurrency(null);
						storeBean.setStoreacctNumber(null);
						storeBean.setStorecifNumber(null);
						storeBean.setStoredefAccount(0);
						storeBean.setUseSameAsMercAccountForStore("0");
					}
           			//Terminal Details
           			if(storeBean.getTermneed() != null && storeBean.getTermneed().equals("1")) {
						List<MerchantOnboardTerminalBean> terminalBeans = storeBean.getLstTerminalBean();
						if(terminalBeans != null  && !terminalBeans.isEmpty()) {
							for(int j = 0; j < terminalBeans.size(); j++) {
								MerchantOnboardTerminalBean terminalBean = terminalBeans.get(j);
								//Basic Terminal Details
								if(terminalBean != null) {
									if(terminalBean.getTermProuctType() == null || terminalBean.getTermProuctType().trim().equals("")) {
										terminalBean.setTermProuctType("1");
									}
									if(getAutoGenerateTid()==0) {
				            			if(terminalBean.getTermCode() == null || terminalBean.getTermCode().trim().equals("")) {
				            				addActionError(getText("MSLV300067",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
				            				valid = "input";
				            			}	
				            			if(!(MAPSUtils.isAlphaNumeric(terminalBean.getTermCode()))){
				            				addActionError(getText("MSLV300068",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
				            				valid = "input";
				            			}
				            			if(terminalBean.getTermCode().trim().length() > 8) {
				            				addActionError(getText("MSLV4000031",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
				            				valid = "input";
				            			}
									}
					            	if(terminalBean.getPaymentMethod() == null || terminalBean.getPaymentMethod().length == 0) {
					            		addActionError(getText("MSLV4000026",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
					    				valid = "input";
					            	}
					            	if(terminalBean.getTerminalmccautocomplete() == null || terminalBean.getTerminalmccautocomplete().trim().equals("")) {
					    				addActionError(getText("MSLV4000014",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
					    				valid = "input";
					    			} else if(terminalBean.getCterminalmccautoComplete() == null || terminalBean.getCterminalmccautoComplete().trim().equals("")) {
				    					addActionError(getText("MSLV4000015",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
				    					valid = "input";
			    					}
					            	if(terminalBean.getDftTrmlRcrngChrgePlnId() == null || terminalBean.getDftTrmlRcrngChrgePlnId().trim().equals("")) {
					            		terminalBean.setDftTrmlRcrngChrgePlnId(null);
					            		terminalBean.setDftTrmlRcrngChrgePlnIdname(null);
					    			} 
				            		//Terminal Account Details
				            		if(terminalBean.getUseSameAsStoreAccountForTerm() != null && terminalBean.getUseSameAsStoreAccountForTerm().equals("1")) {
										if (terminalBean.getTerminalacctWithBank()!=null && terminalBean.getTerminalacctWithBank() == 0) {
											if (terminalBean.getTerminalacctBankText() == null || terminalBean.getTerminalacctBankText().equals("")) {
												addActionError(getText("MSLV300100",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}else {
												terminalBean.setTerminalacctBranch(null);
												terminalBean.setcTerminalacctBranch(null);
											}
										}else if (terminalBean.getTerminalacctWithBank()!=null && terminalBean.getTerminalacctWithBank() == 1) {
											if (terminalBean.getcTerminalacctBranch() == null || terminalBean.getcTerminalacctBranch().equals("") 
													|| terminalBean.getcTerminalacctBranch().equalsIgnoreCase("--Select--")) {
												terminalBean.setTerminalacctBranch(null);
												terminalBean.setcTerminalacctBranch(null);
												addActionError(getText("MSLV300099",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}else {
												terminalBean.setTerminalacctBankText(null);
											}
										}else {
											addActionError(getText("MSLV300098",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
											valid = "input";
										}
										if (terminalBean.getTerminalifsc()== null || terminalBean.getTerminalifsc().equals("")) {
											addActionError(getText("MSLV300101",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
											valid = "input";
										}
										if (!(terminalBean.getTerminalifsc().length() == 8 || terminalBean.getTerminalifsc().length() == 11)) {
											addActionError(getText("MSLV300102",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
											valid = "input";
										}
										if (terminalBean.getTerminalcurrency() == null || terminalBean.getTerminalcurrency().equals("")
												|| terminalBean.getTerminalcurrency().equalsIgnoreCase("--Select--")) {
											addActionError(getText("MSLV300103",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
											valid = "input";
										}
										if (terminalBean.getTerminalacctNumber() == null || terminalBean.getTerminalacctNumber().trim().equals("")) {
											addActionError(getText("MSLV300104",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
											valid = "input";
										}else {
											if(terminalBean.getTerminalacctNumber().startsWith(" ") || terminalBean.getTerminalacctNumber().endsWith(" ")) {
												addActionError(getText("MSLV300105",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if (terminalBean.getTerminalacctNumber().length() < 6 || terminalBean.getTerminalacctNumber().length() > 36) {
												addActionError(getText("MSLV300107",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
										}
										
										String terminalcifNumber = Optional.ofNullable(terminalBean.getTerminalcifNumber()).map(String::trim).filter(s -> !s.isEmpty() && !"null".equalsIgnoreCase(s)).orElse(null);
										if(terminalcifNumber == null) {
											addActionError(getText("MSLV300120",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
											valid = "input";
										} else if (terminalcifNumber.length() != 7) {
										    addActionError(getText("MSLV300124",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
										    valid = "input";
										}
									}else {
										terminalBean.setTerminalacctWithBank(null);
										terminalBean.setTerminalacctBranch(null);
										terminalBean.setTerminalacctBranchText(null);
										terminalBean.setTerminalacctBankText(null);
										terminalBean.setTerminalifsc(null);
										terminalBean.setTerminalcurrency(null);
										terminalBean.setTerminalacctNumber(null);
										terminalBean.setTerminalcifNumber(null);
										terminalBean.setTerminaldefAccount(0);
										terminalBean.setUseSameAsStoreAccountForTerm("0");
									}
				            		//Terminal Pricing Details
				           			if(terminalBean.getTerminalPriceDetailsRequired()!= null && terminalBean.getTerminalPriceDetailsRequired().equals("1")) {
					           			if(terminalBean.getTerminalMerchantServiceFeePlan()== null || terminalBean.getTerminalMerchantServiceFeePlan().trim().equals("")
					           					|| terminalBean.getTerminalMerchantServiceFeePlan().equalsIgnoreCase("--Select--")
					           					|| terminalBean.getTerminalMerchantServiceFeePlan().equals(" ")) {
					           				addActionError(getText("MSLV4000016",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
					           				valid = "input";
					           			}
				           			} else {
				           				terminalBean.setTerminalCaptureServiceFeeAt(null);
				           				terminalBean.setTerminalFeePlanType(null);
				           				terminalBean.setTerminalMerchantServiceFeePlan(null);
				           				terminalBean.setTerminalMercFeeDeduWaiver(null);
				           				terminalBean.setTerminalPriceDetailsRequired("0");
				           			}
				           			//Terminal Rolling Reserve Fund Details
				           			if(terminalBean.getRollingReserveFundRequired()!= null && terminalBean.getRollingReserveFundRequired().equals(1)) {
					           			if(terminalBean.getRrfIndc()!= null && terminalBean.getRrfIndc().equals(1)) {
					           				if(terminalBean.getDomestic()== null || terminalBean.getDomestic().equals("")) {
												addActionError(getText("MSLV4000017",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getDomesticHoldingDays()== null || terminalBean.getDomesticHoldingDays().equals("")) {
												addActionError(getText("MSLV4000018",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getInterNational()== null || terminalBean.getInterNational().equals("")) {
												addActionError(getText("MSLV4000019",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getInternationalHoldingDays()== null || terminalBean.getInternationalHoldingDays().equals("")) {
												addActionError(getText("MSLV4000020",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											terminalBean.setDomesticFixedAmount(null);
											terminalBean.setInternationalFixedAmount(null);
					           			} else if(terminalBean.getRrfIndc()!= null && terminalBean.getRrfIndc().equals(2)) {
					           				if(terminalBean.getDomesticFixedAmount()== null || terminalBean.getDomesticFixedAmount().equals("")) {
												addActionError(getText("MSLV4000021",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getDomesticHoldingDays()== null || terminalBean.getDomesticHoldingDays().equals("")) {
												addActionError(getText("MSLV4000018",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getInternationalFixedAmount()== null || terminalBean.getInternationalFixedAmount().equals("")) {
												addActionError(getText("MSLV4000022",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getInternationalHoldingDays()== null || terminalBean.getInternationalHoldingDays().equals("")) {
												addActionError(getText("MSLV4000020",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											terminalBean.setDomestic(null);
											terminalBean.setInterNational(null);
					           			} else if(terminalBean.getRrfIndc()!= null && terminalBean.getRrfIndc().equals(3)) {
											if(terminalBean.getDomestic()== null || terminalBean.getDomestic().equals("")) {
												addActionError(getText("MSLV4000017",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
					           				if(terminalBean.getDomesticFixedAmount()== null || terminalBean.getDomesticFixedAmount().equals("")) {
												addActionError(getText("MSLV4000021",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input"; 
											}
											if(terminalBean.getDomesticHoldingDays()== null || terminalBean.getDomesticHoldingDays().equals("")) {
												addActionError(getText("MSLV4000018",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getInterNational()== null || terminalBean.getInterNational().equals("")) {
												addActionError(getText("MSLV4000019",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input"; 
											}
											if(terminalBean.getInternationalFixedAmount()== null || terminalBean.getInternationalFixedAmount().equals("")) {
												addActionError(getText("MSLV4000022",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input"; 
											}
											if(terminalBean.getInternationalHoldingDays()== null || terminalBean.getInternationalHoldingDays().equals("")) {
												addActionError(getText("MSLV4000020",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input"; 
											}
					           			} else {
											terminalBean.setRrfIndc(0);
											terminalBean.setDomestic(null);
											terminalBean.setDomesticFixedAmount(null);
											terminalBean.setDomesticHoldingDays(null);
											terminalBean.setInterNational(null);
											terminalBean.setInternationalFixedAmount(null);
											terminalBean.setInternationalHoldingDays(null);
										}
				           			} else {
										terminalBean.setRrfIndc(0);
										terminalBean.setDomestic(null);
				           				terminalBean.setDomesticFixedAmount(null);
										terminalBean.setDomesticHoldingDays(null);
										terminalBean.setInterNational(null);
										terminalBean.setInternationalFixedAmount(null);
										terminalBean.setInternationalHoldingDays(null);
				           			}
									//Terminal Security Deposit Recovery Details
				           			if(terminalBean.getSecurityDepositRecoveryRequired()!= null && terminalBean.getSecurityDepositRecoveryRequired().equals(1)) {
					           			if(terminalBean.getSdrIndc()!= null && terminalBean.getSdrIndc().equals(0)) {
					           				if(terminalBean.getSecurityDepositRecoveryAmount()== null || terminalBean.getSecurityDepositRecoveryAmount().equals("")) {
												addActionError(getText("MSLV4000023",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getSecurityDepositRecoveryDays()== null || terminalBean.getSecurityDepositRecoveryDays().equals("")) {
												addActionError(getText("MSLV4000024",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
					           			}else if(terminalBean.getSdrIndc()!= null && terminalBean.getSdrIndc().equals(1)) {
					           				if(terminalBean.getSecurityDepositRecoveryAmount()== null || terminalBean.getSecurityDepositRecoveryAmount().equals("")) {
												addActionError(getText("MSLV4000025",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if(terminalBean.getSecurityDepositRecoveryDays()== null || terminalBean.getSecurityDepositRecoveryDays().equals("")) {
												addActionError(getText("MSLV4000024",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
					           			}
					           		}
								}
								terminalBean = null;
							}
						}
           			} else {
           				storeBean.setTermneed("0");
           			}
           		}
           		storeBean = null;
           	}
        }
        return valid;
	}
	// Validation for Both Add and Edit Screen - Ends
	
	// Validation for Both Add Store and Edit Screen - Starts
	private String validateForAddNewStoreAfterMerchantOnBoard() {
		String valid = null;
		
		// Add new Store and Terminal Validation after Onboard
		// Store Details --> Basic Store Details, Address Details, Account Details, Risk Details and Price Details
		// Terminal Details --> Basic Terminal Details, Account Details, Price Details, Rolling Reserve Fund Details and Security Deposit Recovery Details
		List<MerchantOnboardStoreBean> storeBeans = merchantOnboardBean.getLstStoreBean();
	    if(storeBeans != null) {
	      	for(int i = 0; i < storeBeans.size(); i++) {
	       		MerchantOnboardStoreBean storeBean = storeBeans.get(i);
	         		if(storeBean != null){
	         				Integer noOfStoresReqCheck = merchantOnboardService.getNoOfStoresReqCheck(instId, merchantOnboardBean);
	         				if(merchantOnboardBean.getNoOfStoresReq() < noOfStoresReqCheck) {
	         					addActionError(getText("No.Of Stores should not be less than actual No.Of Stores"));
	         			        valid = "input";
	         				}
	           			if(getAutoGenerateSid()==0) {
	            			if(storeBean.getStoreCode() == null || storeBean.getStoreCode().trim().equals("")) {
	            				addActionError(getText("MSLV300049",new String[]{String.valueOf(i+1)}));
	            				valid = "input";
	            			}	
	            			if(!(MAPSUtils.isAlphaNumeric(storeBean.getStoreCode()))){
	            				addActionError(getText("MSLV300050",new String[]{String.valueOf(i+1)}));
	            				valid = "input";
	            			}
	            			if (storeBean.getStoreCode().trim().length() < 8 || storeBean.getStoreCode().trim().length() > 15) {
	            			    addActionError(getText("MSLV4000030", new String[]{String.valueOf(i+1)}));
	            			    valid = "input";
	            			}
	            		}
	           			if(storeBean.getStoreTradingName() == null || storeBean.getStoreTradingName().trim().equals("")) {
							addActionError(getText("MSLV300051",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
	           			if(storeBean.getStoreEmail() == null || storeBean.getStoreEmail().trim().equals("")) {
							addActionError(getText("MSLV300062",new String[]{String.valueOf(i+1)}));
							valid = "input";
						} else {
							if(!storeBean.getStoreEmail().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
							    addActionError(getText("MSLV300063",new String[]{String.valueOf(i+1)}));
							    valid = "input";
							}
						}
	           			if(storeBean.getMerchantGrade() == null || storeBean.getMerchantGrade().trim().equals("")
								|| storeBean.getMerchantGrade().equalsIgnoreCase("--Select--")) {
							addActionError(getText("MSLV300052",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
	           			if(storeBean.getNatureOfBusiness() == null || storeBean.getNatureOfBusiness().trim().equals("")
								|| storeBean.getNatureOfBusiness().equalsIgnoreCase("--Select--")) {
							addActionError(getText("MSLV400009",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
	           			if(storeBean.getPrfDocsProv() == null || storeBean.getPrfDocsProv().trim().equals("")) {
							addActionError(getText("MSLV300061",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
	           			if(storeBean.getMccautocomplete() == null || storeBean.getMccautocomplete().trim().equals("")) {
							addActionError(getText("MSLV300053",new String[]{String.valueOf(i+1)}));
							valid = "input";
						} else {
							if(storeBean.getCmccAutoComplete() == null || storeBean.getCmccAutoComplete().trim().equals("")) {
								addActionError(getText("MSLV4000010",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
						}
	           			if(storeBean.getIncorpstatus() == null || storeBean.getIncorpstatus().trim().equals("")
								|| storeBean.getIncorpstatus().equalsIgnoreCase("--Select--")) {
							addActionError(getText("MSLV300056",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
	           			if(storeBean.getGstNumber() == null || storeBean.getGstNumber().trim().equals("")) {
							addActionError(getText("MSLV300059",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
						if(storeBean.getGovtRegMerchant() != null && storeBean.getGovtRegMerchant().equals("1")) {
							if(storeBean.getHomeCountryId() == null || storeBean.getHomeCountryId().trim().equals("")
									|| storeBean.getHomeCountryId().trim().equalsIgnoreCase("--Select--")) {
								addActionError(getText("MSLV300060",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
						} else {
							storeBean.setGovtRegMerchant("0");
							storeBean.setHomeCountryId(null);
						}
						if(storeBean.getWebSiteName() == null || storeBean.getWebSiteName().trim().equals("")) {
							addActionError(getText("MSLV300058",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
	           			if(storeBean.getWebsiteAddress() == null || storeBean.getWebsiteAddress().trim().equals("")) {
							addActionError(getText("MSLV300057",new String[]{String.valueOf(i+1)}));
							valid = "input";
						}
	           			if(storeBean.getPaymentFrecForCard() != null && storeBean.getPaymentFrecForCard().equals("5")) {
							if(storeBean.getPaymentFrecForCardDays() == null || storeBean.getPaymentFrecForCardDays() < 1) {
								addActionError(getText("MSLV4000011",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
						}
	           			if(storeBean.getAaniPayTransPayment() != null && storeBean.getAaniPayTransPayment().equals("0")) {
	           				if(storeBean.getPaymentFrecForAaniPay() != null && storeBean.getPaymentFrecForAaniPay().equals("5")) {
	    						if(storeBean.getPaymentFrecForAaniPayDays() == null || storeBean.getPaymentFrecForAaniPayDays() < 1) {
	    							addActionError(getText("MSLV4000012",new String[]{String.valueOf(i+1)}));
	    							valid = "input";
	    						}
	    					}
	           			}
	           			// Store Pricing Details
	           			if(storeBean.getStorePriceDetailsRequired()!= null && storeBean.getStorePriceDetailsRequired().equals("1")) {
		           			if(storeBean.getStoreMerchantServiceFeePlan()== null || storeBean.getStoreMerchantServiceFeePlan().trim().equals("")
		           					|| storeBean.getStoreMerchantServiceFeePlan().equalsIgnoreCase("--Select--")
		           					|| storeBean.getStoreMerchantServiceFeePlan().equals(" ")) {
		           				addActionError(getText("MSLV4000013",new String[]{String.valueOf(i+1)}));
		           				valid = "input";
		           			}
	           			} else {
	           				storeBean.setStoreCaptureServiceFeeAt(null);
	           				storeBean.setStoreFeePlanType(null);
	           				storeBean.setStoreMerchantServiceFeePlan(null);
	           				storeBean.setStoreMercFeeDeduWaiver(null);
	           				storeBean.setStorePriceDetailsRequired("0");
	           			}
	           			//Store Address Details
	           			if(storeBean.getUseSameAsMercAddressForStore()!= null && storeBean.getUseSameAsMercAddressForStore().equals("1")) {
	           				if(storeBean.getStoreaddressline1() == null || storeBean.getStoreaddressline1().trim().equals("")) {
	           					addActionError(getText("MSLV300081",new String[]{String.valueOf(i+1)}));
	           					valid = "input";
	           				} else {
	           					if(storeBean.getStoreaddressline1().startsWith(" ") || storeBean.getStoreaddressline1().endsWith(" ")) {
	           						addActionError(getText("MSLV300082",new String[]{String.valueOf(i+1)}));
	           					    valid = "input";
	           				    }
	           				    if(!storeBean.getStoreaddressline1().matches("^[A-Za-z0-9;:.,`_ -]+$")) {
	           					    addActionError(getText("MSLV300083",new String[]{String.valueOf(i+1)}));
	           					    valid = "input";
	           				    }
	           				}
	           				if(storeBean.getStorecstate() == null || storeBean.getStorecstate().trim().equals("")) {
	           					storeBean.setStorecstate(null);
	           					storeBean.setStorestate(null);
	           					addActionError(getText("MSLV300085",new String[]{String.valueOf(i+1)}));
	           					valid = "input";
	           				}
	           				if(storeBean.getStorecselectcity()== null || storeBean.getStorecselectcity().trim().equals("")) {
	           					storeBean.setStorecselectcity(null);
	           					storeBean.setStoreselectcity(null);
	           					addActionError(getText("MSLV300086",new String[]{String.valueOf(i+1)}));
	           					valid = "input";
	           				}
	           				if(storeBean.getStorepostalcode() == null || storeBean.getStorepostalcode().trim().equals("")) {
	           					addActionError(getText("MSLV300087",new String[]{String.valueOf(i+1)}));
	           					valid = "input";
	           				}
	           			} else {
	           				storeBean.setStoreaddressline1(null);
	           				storeBean.setStorecountry(null);
	           				storeBean.setStorestate(null);
	           				storeBean.setStoreselectcity(null);
	           				storeBean.setStorepostalcode(null);
	           				storeBean.setStorebusinessContactEmailID(null);
	           				storeBean.setStorebusinessContactMobileNo(null);
	           				storeBean.setStoretechnicalContactEmailID(null);
	           				storeBean.setStoretechnicalContactMobileNo(null);
	           				storeBean.setUseSameAsMercAddressForStore("0");
	           			}
	           			// Store Risk Details
	           			if(storeBean.getStoreRiskDetailsRequired() != null && storeBean.getStoreRiskDetailsRequired().equals("1")) {
							if (storeBean.getRiskCategory() == null || storeBean.getRiskCategory().trim().equals("")
									|| storeBean.getRiskCategory().equalsIgnoreCase("--Select--")) {
								addActionError(getText("MSLV300112",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getPadssPCIdssCertExpDate() != null && storeBean.getPadssPCIdssCertExpDate().trim().length()!=0) {
								if(isValidDate(storeBean.getPadssPCIdssCertExpDate())==false)
								{
									addActionError(getText("MSLV4000027",new String[]{String.valueOf(i+1)}));
									valid = "input";
								}
							}
							if (storeBean.getCloudCertExpDate() != null && storeBean.getCloudCertExpDate().trim().length()!=0) {
								if(isValidDate(storeBean.getCloudCertExpDate())==false)
								{
									addActionError(getText("MSLV4000028",new String[]{String.valueOf(i+1)}));
									valid = "input";
								}
							}
							if (storeBean.getTradeLicenseExpDate() != null && storeBean.getTradeLicenseExpDate().trim().length()!=0) {
								if(isValidDate(storeBean.getTradeLicenseExpDate())==false)
								{
									addActionError(getText("MSLV4000029",new String[]{String.valueOf(i+1)}));
									valid = "input";
								}
							}
							if (storeBean.getTransCountperMonth() == null || storeBean.getTransCountperMonth()==0) {
								addActionError(getText("MSLV400002",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getAverageTicketSize() == null || storeBean.getAverageTicketSize()==0) {
								addActionError(getText("MSLV400003",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getQuarterlySale() == null || storeBean.getQuarterlySale()==0) {
								addActionError(getText("MSLV400004",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getTransToRefundPercent() == null || storeBean.getTransToRefundPercent()==0) {
								addActionError(getText("MSLV400005",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getTransToChargebackPercent() == null || storeBean.getTransToChargebackPercent()==0) {
								addActionError(getText("MSLV400006",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getTransPercent() == null || storeBean.getTransPercent()==0) {
								addActionError(getText("MSLV400007",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getInternationalTransPercent() == null || storeBean.getInternationalTransPercent()==0) {
								addActionError(getText("MSLV400008",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
						}else {
							storeBean.setRiskCategory("2");
							storeBean.setPadssPCIdssCertExpDate(null);
							storeBean.setCloudCertExpDate(null);
							storeBean.setTradeLicenseExpDate(null);
							storeBean.setAdditionalUDFField1(null);
							storeBean.setAdditionalUDFField2(null);
							storeBean.setAdditionalUDFField3(null);
							storeBean.setAdditionalUDFValue1(null);
							storeBean.setAdditionalUDFValue2(null);
							storeBean.setAdditionalUDFValue3(null);
							storeBean.setTransCountperMonth(null);
							storeBean.setAverageTicketSize(null);
							storeBean.setQuarterlySale(null);
							storeBean.setTransToRefundPercent(null);
							storeBean.setTransToChargebackPercent(null);
							storeBean.setTransPercent(null);
							storeBean.setInternationalTransPercent(null);
							storeBean.setStoreRiskDetailsRequired("0");
						}
	           			// Store Account Details
	           			if(storeBean.getUseSameAsMercAccountForStore() != null && storeBean.getUseSameAsMercAccountForStore().equals("1")) {
							if (storeBean.getStoreacctWithBank()!=null && storeBean.getStoreacctWithBank() == 0) {
								if (storeBean.getStoreacctBankText() == null || storeBean.getStoreacctBankText().equals("")) {
									addActionError(getText("MSLV300090",new String[]{String.valueOf(i+1)}));
									valid = "input";
								}else {
									storeBean.setcStoreacctBranch(null);
									storeBean.setStoreacctBranch(null);
								}
							}else if (storeBean.getStoreacctWithBank()!=null && storeBean.getStoreacctWithBank() == 1) {
								if (storeBean.getcStoreacctBranch() == null || storeBean.getcStoreacctBranch().equals("") 
										|| storeBean.getcStoreacctBranch().equalsIgnoreCase("--Select--")) {
									storeBean.setcStoreacctBranch(null);
									storeBean.setStoreacctBranch(null);
									addActionError(getText("MSLV300089",new String[]{String.valueOf(i+1)}));
									valid = "input";
								}else {
									storeBean.setStoreacctBankText(null);
								}
							}else {
								addActionError(getText("MSLV300088",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getStoreifsc()== null || storeBean.getStoreifsc().equals("")) {
								addActionError(getText("MSLV300091",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (!(storeBean.getStoreifsc().length() == 8 || storeBean.getStoreifsc().length() == 11)) {
								addActionError(getText("MSLV300092",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getStorecurrency() == null || storeBean.getStorecurrency().equals("")
									|| storeBean.getStorecurrency().equalsIgnoreCase("--Select--")) {
								addActionError(getText("MSLV300093",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}
							if (storeBean.getStoreacctNumber() == null || storeBean.getStoreacctNumber().trim().equals("")) {
								addActionError(getText("MSLV300094",new String[]{String.valueOf(i+1)}));
								valid = "input";
							}else {
								if(storeBean.getStoreacctNumber().startsWith(" ") || storeBean.getStoreacctNumber().endsWith(" ")) {
									addActionError(getText("MSLV300095",new String[]{String.valueOf(i+1)}));
									valid = "input";
								}
								if (storeBean.getStoreacctNumber().length() < 6 || storeBean.getStoreacctNumber().length() > 36) {
									addActionError(getText("MSLV300097",new String[]{String.valueOf(i+1)}));
									valid = "input";
								}
							}
							String storecifNumber = Optional.ofNullable(storeBean.getStorecifNumber()).map(String::trim).filter(s -> !s.isEmpty() && !"null".equalsIgnoreCase(s)).orElse(null);
							if(storecifNumber == null) {
								addActionError(getText("MSLV300119",new String[]{String.valueOf(i+1)}));
								valid = "input";
							} else if (storecifNumber.length() != 7) {
							    addActionError(getText("MSLV300123",new String[]{String.valueOf(i+1)}));
							    valid = "input";
							}
						}else {
							storeBean.setStoreacctWithBank(null);
							storeBean.setStoreacctBranch(null);
							storeBean.setStoreacctBranchText(null);
							storeBean.setStoreacctBankText(null);
							storeBean.setStoreifsc(null);
							storeBean.setStorecurrency(null);
							storeBean.setStoreacctNumber(null);
							storeBean.setStorecifNumber(null);
							storeBean.setStoredefAccount(0);
							storeBean.setUseSameAsMercAccountForStore("0");
						}
	           			//Terminal Details
	           			if(storeBean.getTermneed() != null && storeBean.getTermneed().equals("1")) {
							List<MerchantOnboardTerminalBean> terminalBeans = storeBean.getLstTerminalBean();
							if(terminalBeans != null  && !terminalBeans.isEmpty()) {
								for(int j = 0; j < terminalBeans.size(); j++) {
									MerchantOnboardTerminalBean terminalBean = terminalBeans.get(j);
									//Basic Terminal Details
									if(terminalBean != null) {
										if(terminalBean.getTermProuctType() == null || terminalBean.getTermProuctType().trim().equals("")) {
											terminalBean.setTermProuctType("1");
										}
										if(getAutoGenerateTid()==0) {
					            			if(terminalBean.getTermCode()== null || terminalBean.getTermCode().trim().equals("")) {
					            				addActionError(getText("MSLV300067",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
					            				valid = "input";
					            			}	
					            			if(!(MAPSUtils.isAlphaNumeric(terminalBean.getTermCode()))){
					            				addActionError(getText("MSLV300068",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
					            				valid = "input";
					            			}
					            			if(terminalBean.getTermCode().trim().length() > 8){
					            				addActionError(getText("MSLV4000031",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
					            				valid = "input";
					            			}
										}
						            	if(terminalBean.getPaymentMethod() == null || terminalBean.getPaymentMethod().length == 0) {
						            		addActionError(getText("MSLV4000026",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
						    				valid = "input";
						            	}
						            	if(terminalBean.getTerminalmccautocomplete() == null || terminalBean.getTerminalmccautocomplete().trim().equals("")) {
						    				addActionError(getText("MSLV4000014",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
						    				valid = "input";
						    			} else if(terminalBean.getCterminalmccautoComplete() == null || terminalBean.getCterminalmccautoComplete().trim().equals("")) {
					    					addActionError(getText("MSLV4000015",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
					    					valid = "input";
				    					}
					            		//Terminal Account Details
					            		if(terminalBean.getUseSameAsStoreAccountForTerm() != null && terminalBean.getUseSameAsStoreAccountForTerm().equals("1")) {
											if (terminalBean.getTerminalacctWithBank()!=null && terminalBean.getTerminalacctWithBank() == 0) {
												if (terminalBean.getTerminalacctBankText() == null || terminalBean.getTerminalacctBankText().equals("")) {
													addActionError(getText("MSLV300100",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}else {
													terminalBean.setcTerminalacctBranch(null);
													terminalBean.setTerminalacctBranch(null);
												}
											}else if (terminalBean.getTerminalacctWithBank()!=null && terminalBean.getTerminalacctWithBank() == 1) {
												if (terminalBean.getcTerminalacctBranch() == null || terminalBean.getcTerminalacctBranch().equals("") 
														|| terminalBean.getcTerminalacctBranch().equalsIgnoreCase("--Select--")) {
													terminalBean.setcTerminalacctBranch(null);
													terminalBean.setTerminalacctBranch(null);
													addActionError(getText("MSLV300099",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}else {
													terminalBean.setTerminalacctBankText(null);
												}
											}else {
												addActionError(getText("MSLV300098",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if (terminalBean.getTerminalifsc()== null || terminalBean.getTerminalifsc().equals("")) {
												addActionError(getText("MSLV300101",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if (!(terminalBean.getTerminalifsc().length() == 8 || terminalBean.getTerminalifsc().length() == 11)) {
												addActionError(getText("MSLV300102",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if (terminalBean.getTerminalcurrency() == null || terminalBean.getTerminalcurrency().equals("")
													|| terminalBean.getTerminalcurrency().equalsIgnoreCase("--Select--")) {
												addActionError(getText("MSLV300103",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}
											if (terminalBean.getTerminalacctNumber() == null || terminalBean.getTerminalacctNumber().trim().equals("")) {
												addActionError(getText("MSLV300104",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											}else {
												if(terminalBean.getTerminalacctNumber().startsWith(" ") || terminalBean.getTerminalacctNumber().endsWith(" ")) {
													addActionError(getText("MSLV300105",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if (terminalBean.getTerminalacctNumber().length() < 6 || terminalBean.getTerminalacctNumber().length() > 36) {
													addActionError(getText("MSLV300107",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
											}
											String terminalcifNumber = Optional.ofNullable(terminalBean.getTerminalcifNumber()).map(String::trim).filter(s -> !s.isEmpty() && !"null".equalsIgnoreCase(s)).orElse(null);
											if(terminalcifNumber == null) {
												addActionError(getText("MSLV300120",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
												valid = "input";
											} else if (terminalcifNumber.length() != 7) {
											    addActionError(getText("MSLV300124",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
											    valid = "input";
											}
										}else {
											terminalBean.setTerminalacctWithBank(null);
											terminalBean.setTerminalacctBranch(null);
											terminalBean.setTerminalacctBranchText(null);
											terminalBean.setTerminalacctBankText(null);
											terminalBean.setTerminalifsc(null);
											terminalBean.setTerminalcurrency(null);
											terminalBean.setTerminalacctNumber(null);
											terminalBean.setTerminalcifNumber(null);
											terminalBean.setTerminaldefAccount(0);
											terminalBean.setUseSameAsStoreAccountForTerm("0");
										}
					            		//Terminal Pricing Details
					           			if(terminalBean.getTerminalPriceDetailsRequired()!= null && terminalBean.getTerminalPriceDetailsRequired().equals("1")) {
						           			if(terminalBean.getTerminalMerchantServiceFeePlan()== null || terminalBean.getTerminalMerchantServiceFeePlan().trim().equals("")
						           					|| terminalBean.getTerminalMerchantServiceFeePlan().equalsIgnoreCase("--Select--")
						           					|| terminalBean.getTerminalMerchantServiceFeePlan().equals(" ")) {
						           				addActionError(getText("MSLV4000016",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
						           				valid = "input";
						           			}
					           			} else {
					           				terminalBean.setTerminalCaptureServiceFeeAt(null);
					           				terminalBean.setTerminalFeePlanType(null);
					           				terminalBean.setTerminalMerchantServiceFeePlan(null);
					           				terminalBean.setTerminalMercFeeDeduWaiver(null);
					           				terminalBean.setTerminalPriceDetailsRequired("0");
					           			}
					           			//Terminal Rolling Reserve Fund Details
					           			if(terminalBean.getRollingReserveFundRequired()!= null && terminalBean.getRollingReserveFundRequired().equals(1)) {
						           			if(terminalBean.getRrfIndc()!= null && terminalBean.getRrfIndc().equals(1)) {
						           				if(terminalBean.getDomestic()== null || terminalBean.getDomestic().equals("")) {
													addActionError(getText("MSLV4000017",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getDomesticHoldingDays()== null || terminalBean.getDomesticHoldingDays().equals("")) {
													addActionError(getText("MSLV4000018",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getInterNational()== null || terminalBean.getInterNational().equals("")) {
													addActionError(getText("MSLV4000019",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getInternationalHoldingDays()== null || terminalBean.getInternationalHoldingDays().equals("")) {
													addActionError(getText("MSLV4000020",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												terminalBean.setDomesticFixedAmount(null);
												terminalBean.setInternationalFixedAmount(null);
						           			} else if(terminalBean.getRrfIndc()!= null && terminalBean.getRrfIndc().equals(2)) {
						           				if(terminalBean.getDomesticFixedAmount()== null || terminalBean.getDomesticFixedAmount().equals("")) {
													addActionError(getText("MSLV4000021",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getDomesticHoldingDays()== null || terminalBean.getDomesticHoldingDays().equals("")) {
													addActionError(getText("MSLV4000018",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getInternationalFixedAmount()== null || terminalBean.getInternationalFixedAmount().equals("")) {
													addActionError(getText("MSLV4000022",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getInternationalHoldingDays()== null || terminalBean.getInternationalHoldingDays().equals("")) {
													addActionError(getText("MSLV4000020",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												terminalBean.setDomestic(null);
												terminalBean.setInterNational(null);
						           			} else if(terminalBean.getRrfIndc()!= null && terminalBean.getRrfIndc().equals(3)) {
												if(terminalBean.getDomestic()== null || terminalBean.getDomestic().equals("")) {
													addActionError(getText("MSLV4000017",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
						           				if(terminalBean.getDomesticFixedAmount()== null || terminalBean.getDomesticFixedAmount().equals("")) {
													addActionError(getText("MSLV4000021",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input"; 
												}
												if(terminalBean.getDomesticHoldingDays()== null || terminalBean.getDomesticHoldingDays().equals("")) {
													addActionError(getText("MSLV4000018",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getInterNational()== null || terminalBean.getInterNational().equals("")) {
													addActionError(getText("MSLV4000019",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input"; 
												}
												if(terminalBean.getInternationalFixedAmount()== null || terminalBean.getInternationalFixedAmount().equals("")) {
													addActionError(getText("MSLV4000022",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input"; 
												}
												if(terminalBean.getInternationalHoldingDays()== null || terminalBean.getInternationalHoldingDays().equals("")) {
													addActionError(getText("MSLV4000020",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input"; 
												}
						           			} else {
												terminalBean.setRrfIndc(0);
												terminalBean.setDomestic(null);
												terminalBean.setDomesticFixedAmount(null);
												terminalBean.setDomesticHoldingDays(null);
												terminalBean.setInterNational(null);
												terminalBean.setInternationalFixedAmount(null);
												terminalBean.setInternationalHoldingDays(null);
											}
					           			} else {
											terminalBean.setRrfIndc(0);
											terminalBean.setDomestic(null);
					           				terminalBean.setDomesticFixedAmount(null);
											terminalBean.setDomesticHoldingDays(null);
											terminalBean.setInterNational(null);
											terminalBean.setInternationalFixedAmount(null);
											terminalBean.setInternationalHoldingDays(null);
					           			}
										//Terminal Security Deposit Recovery Details
					           			if(terminalBean.getSecurityDepositRecoveryRequired()!= null && terminalBean.getSecurityDepositRecoveryRequired().equals(1)) {
						           			if(terminalBean.getSdrIndc()!= null && terminalBean.getSdrIndc().equals(0)) {
						           				if(terminalBean.getSecurityDepositRecoveryAmount()== null || terminalBean.getSecurityDepositRecoveryAmount().equals("")) {
													addActionError(getText("MSLV4000023",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getSecurityDepositRecoveryDays()== null || terminalBean.getSecurityDepositRecoveryDays().equals("")) {
													addActionError(getText("MSLV4000024",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
						           			}else if(terminalBean.getSdrIndc()!= null && terminalBean.getSdrIndc().equals(1)) {
						           				if(terminalBean.getSecurityDepositRecoveryAmount()== null || terminalBean.getSecurityDepositRecoveryAmount().equals("")) {
													addActionError(getText("MSLV4000025",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
												if(terminalBean.getSecurityDepositRecoveryDays()== null || terminalBean.getSecurityDepositRecoveryDays().equals("")) {
													addActionError(getText("MSLV4000024",new String[]{String.valueOf(i+1), String.valueOf(j+1)}));
													valid = "input";
												}
						           			}
						           		}
									}
									terminalBean = null;
								}
							}
	           			} else {
	           				storeBean.setTermneed("0");
	           			}
	           		}
	           		storeBean = null;
	           	}
	        }
	    return valid;
	}
		
	private MerchantOnboardBean getDefaultValuesSet(MerchantOnboardBean merchantOnboardBean) {
		
		if(merchantOnboardBean.getCaptureServiceFeeAt() == null || merchantOnboardBean.getCaptureServiceFeeAt().length() == 0) {
			merchantOnboardBean.setCaptureServiceFeeAt("1");
		}
		if(merchantOnboardBean.getAcctMapLvl() == null) {
			merchantOnboardBean.setAcctMapLvl(1);
		}
		if(merchantOnboardBean.getDefAccount() == null || merchantOnboardBean.getDefAccount()+"".length() == 0) {
			merchantOnboardBean.setDefAccount(0);
		}
		if(merchantOnboardBean.getFeePlanType() == null || merchantOnboardBean.getFeePlanType()==0) {
			merchantOnboardBean.setFeePlanType(1);
		}
		if(merchantOnboardBean.getIntchgPlus() == null || merchantOnboardBean.getIntchgPlus().length() == 0) {
			merchantOnboardBean.setIntchgPlus("0");
		}
		if(merchantOnboardBean.getExistingCustomer() == null || merchantOnboardBean.getExistingCustomer() == 0) {
			merchantOnboardBean.setExistingCustomer(0);
		}
		if(merchantOnboardBean.getMerchantPortalRequired() == null || merchantOnboardBean.getMerchantPortalRequired() == 0) {
			merchantOnboardBean.setMerchantPortalRequired(0);
		}
		if(merchantOnboardBean.getMercFeeDeduWaiver() == null || merchantOnboardBean.getMercFeeDeduWaiver() == 0) {
			merchantOnboardBean.setMercFeeDeduWaiver(0);
		}
		if (merchantOnboardBean.getMercAggr() != null) {
		    merchantOnboardBean.setMerchantAccountRequired(0);
		} else if (merchantOnboardBean.getAcctMapLvl() == 0 && merchantOnboardBean.getMerchantAccountRequired() == null) {
		    merchantOnboardBean.setMerchantAccountRequired(0);
		} else {
		    merchantOnboardBean.setMerchantAccountRequired(1);
		}
		return merchantOnboardBean;
	}

	private Map<String, String> MakerSearchBy;
    public Map<String, String> getMakerSearchBy() {
        if (MakerSearchBy == null) {
        	MakerSearchBy = new LinkedHashMap<>();
        	MakerSearchBy.put("0", getText("MerchantOnboardMakerAction.searchAll"));
        	MakerSearchBy.put("1", getText("MerchantOnboardMakerAction.searchPending"));
        	MakerSearchBy.put("2", getText("MerchantOnboardMakerAction.searchReassigned"));
        	MakerSearchBy.put("3", getText("MerchantOnboardMakerAction.searchError"));
        	MakerSearchBy.put("4", getText("MerchantOnboardMakerAction.searchApproved"));
        	MakerSearchBy.put("5", getText("MerchantOnboardMakerAction.searchRejected"));
        	//MakerSearchBy.put("6", getText("MerchantOnboardMakerAction.searchDuplicate"));
        }
        return MakerSearchBy;
    }
    
    public void sendAcknowledgementEmailToMaker(String tomail, Integer userId, String Mid, String mercName) {
		try
		{
			EmailBean emailBean = new EmailBean();
			emailBean.setPriority("1");
			emailBean.setTo(tomail);
			emailBean.setSubject(getText("makerAcknowledgement.email.subject").replace("<mid>", Mid));
			emailBean.setVelocityTemplateName("SendAcknowledgementEmailToMaker.html");
			
			Map<String, Object> bodyMap = new HashMap<String, Object>();
			bodyMap.put("user_id", merchantOnboardService.getUserNameUsingID(userId));
			bodyMap.put("mer_id", Mid);
			bodyMap.put("mer_name", mercName);
			bodyMap.put("instName", sessionBean.getInstName());
			emailBean.setBodyVariables(bodyMap);
			emailService.sendEmail(emailBean);
		}catch (Exception e) {
			logUtil.error("Error while sending merchant acknowledgement mail to maker", e);
		}
	}
    
    public void sendNotificationEmailToCheckers(String MercEmail, String Mid, String file, Integer userId, String mercName, String remark) {
		try
		{
			EmailBean emailBean = new EmailBean();
			emailBean.setPriority("1");
			emailBean.setTo(MercEmail);
			emailBean.setSubject(getText("makerSend.email.subject").replace("<mid>", Mid));
			emailBean.setVelocityTemplateName("EditMakerSendEmail.html");
			
			Map<String, Object> bodyMap = new HashMap<String, Object>();
			bodyMap.put("file", file);
			bodyMap.put("user_id", merchantOnboardService.getUserNameUsingID(userId));
			bodyMap.put("mer_id", Mid);
			bodyMap.put("mer_name", mercName);
			bodyMap.put("remark", remark);
			bodyMap.put("instName", sessionBean.getInstName());
			emailBean.setBodyVariables(bodyMap);
			emailService.sendEmail(emailBean);
		}catch (Exception e) {
			logUtil.error("Error while sending merchant notification mail to checkers", e);
		}
	}
    
    private boolean isValidDate(String dateStr) {
		try
		{
			String sysDateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			Date inputDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
            Date sysDate = new SimpleDateFormat("dd/MM/yyyy").parse(sysDateString);
            if (!inputDate.after(sysDate)) {
            	return false;
            }
            return true;
		} catch (Exception e) {
			return false;
		}
	}
    
	public void merchantWelcomeLetEnvironmentEmail(String refNo, String mid, String merchantmail, MerchantOnboardStoreBean storebean) {
		try
		{
			EmailBean emailBean = new EmailBean();
			emailBean.setPriority("1");
			emailBean.setTo(storebean.getStoreEmail());
			emailBean.setSubject(getText("merchantwelcome.email.subject").replace("<Inst_Name>", sessionBean.getInstName()));
			emailBean.setVelocityTemplateName("MerchantWelcomeLetEnvironment.html");
			
			Map<String, Object> bodyMap = new HashMap<String, Object>();
			bodyMap.put("instName", sessionBean.getInstName());
			bodyMap.put("storeCode", storebean.getStoreCode());
			StringBuilder mlist = new StringBuilder();
            
			if(storebean.getUseSameAsMercAccountForStore()!=null && storebean.getUseSameAsMercAccountForStore().equalsIgnoreCase("1")) {
		    	mlist.append("<table border='1'>");
	            mlist.append("<thead><tr><th>Account Name</th><th>Account Number</th><th>Branch Name</th></tr></thead>");
	            mlist.append("<tbody><tr><td>"+(storebean.getStoreaccountname()==null?"":storebean.getStoreaccountname())+"</td>");
	      	   	mlist.append("<td>"+(storebean.getStoreacctNumber()==null?"":storebean.getStoreacctNumber())+"</td>");
	      	   	if(storebean.getStoreacctWithBank()==1)
	      	   	{
	      	   		mlist.append("<td>"+(storebean.getStoreacctBranch()==null?"":storebean.getStoreacctBranch())+"</td>");
	      	   	} else {
	      	   		mlist.append("<td>"+(storebean.getStoreacctBranchText()==null?"":storebean.getStoreacctBranchText())+"</td>");
	      	   	}
	      	   	mlist.append("</tr></tbody></table>");
			} else {
				MerchantOnboardBean merchantOnboardBean = new MerchantOnboardBean();
				merchantOnboardBean.setRefNo(refNo);
				merchantOnboardBean = merchantOnboardService.getAccountMastDetails(merchantOnboardBean,instId);
				mlist.append("<table border='1'>");
	            mlist.append("<thead><tr><th>Account Name</th><th>Account Number</th><th>Branch Name</th></tr></thead>");
	            mlist.append("<tbody><tr><td>"+(merchantOnboardBean.getAccountname()==null?"":merchantOnboardBean.getAccountname())+"</td>");
	      	   	mlist.append("<td>"+(merchantOnboardBean.getAcctNumber()==null?"":merchantOnboardBean.getAcctNumber())+"</td>");
	      	   	if(merchantOnboardBean.getAcctWithBank()==1)
	      	   	{
	      	   		mlist.append("<td>"+(merchantOnboardBean.getAcctBranch()==null?"":merchantOnboardBean.getAcctBranch())+"</td>");
	      	   	} else {
	      	   		mlist.append("<td>"+(merchantOnboardBean.getAcctBranchText()==null?"":merchantOnboardBean.getAcctBranchText())+"</td>");
	      	   	}
	      	   	mlist.append("</tr></tbody></table>");
			}
            
			bodyMap.put("mList", mlist.toString());
			bodyMap.put("storeEmailId", storebean.getStoreEmail());
			emailBean.setBodyVariables(bodyMap);
			emailService.sendEmail(emailBean);
		}catch (Exception e) {
			logUtil.error("Error creating Merchant Welcome Let Environment Email ", e);
		}
	}

}