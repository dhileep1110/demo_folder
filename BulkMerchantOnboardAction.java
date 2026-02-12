package com.fss.maps.merchantsales.merchantonboard.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.fss.maps.batch.gen.genCommon.GenObjectPool;
import com.fss.maps.batch.mps.mpsConstants;
import com.fss.maps.common.bean.SessionBean;
import com.fss.maps.common.services.MasterListService;
import com.fss.maps.common.util.AttachmentMalwareScannerUtil;
import com.fss.maps.common.util.logger.LoggerUtil;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardBean;
import com.fss.maps.merchantsales.merchantonboard.dao.MerchantOnboardDAO;
import com.fss.maps.merchantsales.merchantonboard.service.MerchantOnboardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

public class BulkMerchantOnboardAction extends ActionSupport implements Preparable, ServletRequestAware
{
	LoggerUtil loggerUtil = new LoggerUtil(BulkMerchantOnboardAction.class);
	private Integer userId = null;
	private Integer instId = null;
	private String userGroup = null;
	private String dateFormat = null;
	private final String multiCheckKey = "52";
	private final String FileName = "BULKUpload_";
	private MerchantOnboardBean merchantOnboardBean = new MerchantOnboardBean();
	private MerchantOnboardService merchantOnboardService;
	private MerchantOnboardDAO merchantOnboardDAO;
	
	private HttpServletRequest httpServletRequest; 
	private HttpServletResponse httpServletResponse;
	private SessionBean sessionBean;
	private String uploadType;
	private File uploadFile;
	private String uploadFileName;
	private String searchValue;
	private List<MerchantOnboardBean> reportList;
	private String title;
	private String fileName;
	private List<MerchantOnboardBean> successRecordList;
	private List<MerchantOnboardBean> failureRecordList;
	private AttachmentMalwareScannerUtil scannerUtil = new AttachmentMalwareScannerUtil();
	private static final String CLAMAV_KEY = "104";
	private MasterListService masterListService;
	    
	public MasterListService getMasterListService() {
		return masterListService;
	}
	public void setMasterListService(MasterListService masterListService) {
		this.masterListService = masterListService;
	}
	public AttachmentMalwareScannerUtil getScannerUtil() {
		return scannerUtil;
	}
	public void setScannerUtil(AttachmentMalwareScannerUtil scannerUtil) {
		this.scannerUtil = scannerUtil;
	}
	public void setServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
	public SessionBean getSessionBean() {
		return sessionBean;
	}
	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
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
	public MerchantOnboardDAO getMerchantOnboardDAO() {
		return merchantOnboardDAO;
	}
	public void setMerchantOnboardDAO(MerchantOnboardDAO merchantOnboardDAO) {
		this.merchantOnboardDAO = merchantOnboardDAO;
	}
	public String getUploadType() {
		return uploadType;
	}
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public List<MerchantOnboardBean> getReportList() {
		return reportList;
	}
	public void setReportList(List<MerchantOnboardBean> reportList) {
		this.reportList = reportList;
	}	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<MerchantOnboardBean> getSuccessRecordList() {
		return successRecordList;
	}
	public void setSuccessRecordList(List<MerchantOnboardBean> successRecordList) {
		this.successRecordList = successRecordList;
	}
	public List<MerchantOnboardBean> getFailureRecordList() {
		return failureRecordList;
	}
	public void setFailureRecordList(List<MerchantOnboardBean> failureRecordList) {
		this.failureRecordList = failureRecordList;
	}
	
	public void prepare() throws Exception {
		userId = httpServletRequest.getSession().getAttribute("userId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("userId").toString());
		instId = httpServletRequest.getSession().getAttribute("instId") == null ? null : Integer.parseInt(httpServletRequest.getSession().getAttribute("instId").toString());
		dateFormat = (String) ServletActionContext.getContext().getSession().get("INST-DATEFRMT");
		userGroup = (String) httpServletRequest.getSession().getAttribute("SPRING_SECURITY_FORM_USERNAME_KEY");
		merchantOnboardBean.setInstCode(instId);
		merchantOnboardBean.setUserId(userId.toString());
	}
	
	public String showUploadPage() {
		try {
			if (!(merchantOnboardService.getMultiCheckFlag(multiCheckKey)).equals("N")) {
				addActionMessage("Master Bulk Upload Data is already running by some other user, please try later.");
			}
			uploadType = "";
		} catch (Exception e) {
			loggerUtil.error("Error while upload page", e);
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String uploadBulkMerchantOnboard() {
		loggerUtil.audit("Bulk Upload File Page of Merchant Onboard - Entering", true, userGroup);
		loggerUtil.logMethodEntry("uploadBulkMerchantOnboard");
		mpsConstants mpsConstants = null;
		StringBuffer filepath = null;
		StringBuffer descfilename = null;
		Integer output = 0;
		boolean response = false;
		try {
			
			// Code added by Narayanan S on 15-04-2025 for Attachment File Scanner - starts
			String clamAVCheck = masterListService.getClamAVCheck(CLAMAV_KEY);
		    if (clamAVCheck != null && "1".equals(clamAVCheck)) {
				if (!System.getProperty("os.name").startsWith("Windows")){
					if (uploadFile.isFile() && uploadFile!=null) {
						try {
							if (scannerUtil.ping()) {
								byte[] fileBytes = Files.readAllBytes(uploadFile.toPath());
		
									try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
										response = scannerUtil.scan(inputStream);
										if (response) {
											addActionError(getText("Malware found in the uploaded file - ") + uploadFileName);
											return ERROR;
							            }
									} catch (Exception e) {
										loggerUtil.error("Scan failed: " + uploadFileName + " " + e.getMessage());
										addActionError(getText("Failed to scan the file - ") + uploadFileName);
										return ERROR;
									}
							} else {
								loggerUtil.error("ClamAV not reachable.");
								addActionError(getText("ClamAV not reachable"));
								return ERROR;
							}
						}catch (Exception e) {
							loggerUtil.error("Error while scanning: " + uploadFileName + " " + e.getMessage());
							addActionError(getText("Error during file scan - ") + uploadFileName);
							return ERROR;
						}
					}
				}
		    }
			// Code added by Narayanan S on 15-04-2025 for Attachment File Scanner - ends
			
			if(!(merchantOnboardService.getMultiCheckFlag(multiCheckKey)).equals("N")){
				addActionMessage("Master Bulk Upload Data is already running by some other user, please try later.");
			}else {
				if(uploadType.equals("0")){
					loggerUtil.info("Bulk Merchant Onboard - Upload type not selected");
					addActionError("Please select Bulk Merchant Onboard - Upload type to process");
					return INPUT;
				}else if(uploadType.equals("24")){
					merchantOnboardService.setMultiCheckFlag(multiCheckKey);
					mpsConstants = GenObjectPool.getInstance().getMpsConstantsObj(Integer.parseInt(sessionBean.getInstCode()));
					if(mpsConstants == null) {
						addActionError("mpsConstants not loaded properly, some property file might be missing");
					}
					filepath = new StringBuffer(mpsConstants.getPropValue("MERCHANT_ONBOARDING_FILE_UPLOAD"));
					if(uploadFileName.endsWith(".xlsx")) {
						descfilename = new StringBuffer(FileName+(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))+".xlsx");
					}else {
						descfilename = new StringBuffer(FileName+(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()))+".xls");
					}
					
					merchantOnboardService.moveToBulkUploadFolder(instId, getUploadFile(), filepath.toString(), descfilename.toString());
					
					output = merchantOnboardService.processBulkUploadMerchantFile(merchantOnboardBean, filepath+descfilename.toString(), descfilename.toString(), uploadType, sessionBean);
					if(output > 0 && merchantOnboardBean.getErrorDesc().equalsIgnoreCase("SUCCESS") == true) {
					    merchantOnboardService.writeToBackup(filepath.toString(), descfilename.toString(), instId, output);
					    reportList = merchantOnboardService.showDataOfFile(uploadType, descfilename.toString());
					} else {
					    merchantOnboardService.writeToBackup(filepath.toString(), descfilename.toString(), instId, output);
					    
					    if (merchantOnboardBean.getErrorDesc() != null && !merchantOnboardBean.getErrorDesc().isEmpty()) {
					        addActionError(merchantOnboardBean.getErrorDesc());
					    } else {
					        addActionError("Error while processing the FileName (Auto-generate): " + descfilename);
					    }
					    return ERROR;
					}
					/*if(output>0 && merchantOnboardBean.getErrorDesc().equalsIgnoreCase("SUCCESS") == true) {
						merchantOnboardService.writeToBackup(filepath.toString(), descfilename.toString(), instId, output);
						reportList = merchantOnboardService.showDataOfFile(uploadType, descfilename.toString());
					} else {
						merchantOnboardService.writeToBackup(filepath.toString(), descfilename.toString(), instId, output);
						addActionError("Error while processing the FileName (Auto-generate): "+descfilename);
						return ERROR;*/
					 /*else {
						 merchantOnboardService.writeToBackup(filepath.toString(), descfilename.toString(), instId, output);
						 reportList = merchantOnboardService.showDataOfFile(uploadType, descfilename.toString());
						 if (merchantOnboardBean.getErrorDesc() != null && merchantOnboardBean.getErrorDesc().startsWith("Duplicate")) {
							 addActionError(merchantOnboardBean.getErrorDesc());
						 } else {
							 addActionError("Error while processing the FileName (Auto-generate): " + descfilename);
						 }
						 return ERROR;*/
//					}
					
					//if(merchantOnboardBean.getErrorDesc().equalsIgnoreCase("SUCCESS") == false) {
					//	addActionError("Error while processing the FileName (Auto-generate): "+descfilename);
					//}
					//addActionMessage("Uploaded File Name (Auto-generate): "+descfilename);
					//addActionMessage(merchantOnboardService.getBulkUpldLogDetailsCount(descfilename.toString()));
					//reportList = merchantOnboardService.showDataOfFile(uploadType, descfilename.toString());
				}
			}
		}catch(Exception e){
			if (e.getMessage() != null && e.getMessage().toLowerCase().contains("virus")) {
			    addActionError(getText("file contains a Malware!"));
			}
			loggerUtil.error("Error while uploadBulkMerchantOnboard method", e);
			return ERROR;
		}finally {
			uploadType = ""; mpsConstants = null; filepath = null; descfilename = null; output = 0;
			merchantOnboardService.resetMultiCheckFlag(multiCheckKey);
		}
		loggerUtil.logMethodExit("uploadBulkMerchantOnboard");
		loggerUtil.audit("Bulk Upload File Page of Merchant Onboard - Exit", true, userGroup);
		return SUCCESS;
	}
	
	/**public String showBulkUploadReportDetails(){
		String fileName = null, reportType = null;
		try{
		fileName = httpServletRequest.getParameter("fileName");
		reportType = httpServletRequest.getParameter("reportType");
	
		if(reportType.equalsIgnoreCase("SUCCESS")) {
			title = "Bulk Upload Success";
		}else {
			title = "Bulk Upload Failure";
		}
		reportList = merchantOnboardService.getBulkUploadReportDetails(fileName, reportType, uploadType);
			
		}catch(Exception e){
			loggerUtil.error("Error while getting records", e);
			addActionError("Error while getting records");
		}
		return SUCCESS;
	}**/
	
	public String showSuccessRecordAction(){
		try{
		fileName = httpServletRequest.getParameter("fileName");
		successRecordList = merchantOnboardService.getSuccessRecordList(fileName,uploadType);
		}catch(Exception ex){
			loggerUtil.error("Error while getting success records", ex);
			addActionError("Error while getting success records");
		}
		return SUCCESS;
	}
	
	public String showErrorRecordAction(){
		try{
		fileName = httpServletRequest.getParameter("fileName");
		failureRecordList = merchantOnboardService.getFailureRecordList(fileName, uploadType);
		}catch(Exception ex){
			loggerUtil.error("Exception while showing error records", ex);
			addActionError("Error while getting error records");
			return ERROR;
		}
		return SUCCESS;
	}
}
