package com.fss.maps.merchantsales.merchantonboard.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.fss.maps.batch.gen.genCommon.InstHashcodeMapper;
import com.fss.maps.common.bean.KeyValueBean;
import com.fss.maps.common.bean.MapsBaseBean;
import com.fss.maps.common.bean.SessionBean;
import com.fss.maps.common.pathvalidation.PathCleaner;
import com.fss.maps.common.pathvalidation.PathTraversalDetector;
import com.fss.maps.common.pathvalidation.PathValidator;
import com.fss.maps.common.services.MasterListService;
import com.fss.maps.common.services.ParameterValuesServiceImpl;
import com.fss.maps.common.util.EmailService;
import com.fss.maps.common.util.logger.LoggerUtil;
import com.fss.maps.merchantsales.dao.releaseMerchant.ReleaseMerchantDAO;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardAttachementBean;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardBean;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardStoreBean;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardTerminalBean;
import com.fss.maps.merchantsales.merchantonboard.dao.MerchantOnboardDAO;
import com.fss.maps.useradmin.service.UserServiceImpl;

import net.sf.json.JSONObject;

public class MerchantOnboardServiceImpl implements MerchantOnboardService {
	
	LoggerUtil loggerUtil = new LoggerUtil(MerchantOnboardServiceImpl.class);
	private DataSource jdbcDataSource;
	private MerchantOnboardDAO merchantOnboardDAO;
	private MasterListService masterListService;
	private ReleaseMerchantDAO releaseMerchantDAO;
	private ParameterValuesServiceImpl parameterValuesServiceImpl;
	private TransactionTemplate transactionTemplate;
	private UserServiceImpl userService;
	private EmailService emailService;
	
	// Lists for Validation 
	private List<String> cryptographicMethodsList;
	private List<String> routingPlanList;
	private List<String> processingPlanNameList;
	private List<String> bankerSalesList;
	private List<String> customerCountryList;
	private List<String> customerCityList;
	private List<String> customerStateList;
	private List<String> feePlanNameList;
	private List<String> mccList;
	private List<String> acctBranchList;
	private List<String> crcyList;
	private List<String> lstProuctTypeList;
	private List<String> recurringChargePlanList;
	private List<String> transSourceList;
	private List<String> merGradeList;
	private List<String> merCountryOriginList;
	private List<String> incorpStatusList;
	private List<String> documtCatgList;
	private List<String> cardGroupList;
	private static final int EXPECTED_COLUMN_COUNT = 55;
	
	public DataSource getJdbcDataSource() {
		return jdbcDataSource;
	}
	public void setJdbcDataSource(DataSource jdbcDataSource) {
		this.jdbcDataSource = jdbcDataSource;
	}
	public MerchantOnboardDAO getMerchantOnboardDAO() {
		return merchantOnboardDAO;
	}
	public void setMerchantOnboardDAO(MerchantOnboardDAO merchantOnboardDAO) {
		this.merchantOnboardDAO = merchantOnboardDAO;
	}
	public MasterListService getMasterListService() {
		return masterListService;
	}
	public void setMasterListService(MasterListService masterListService) {
		this.masterListService = masterListService;
	}
	public ParameterValuesServiceImpl getParameterValuesServiceImpl() {
		return parameterValuesServiceImpl;
	}
	public void setParameterValuesServiceImpl(ParameterValuesServiceImpl parameterValuesServiceImpl) {
		this.parameterValuesServiceImpl = parameterValuesServiceImpl;
	}
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
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
	
	public String getMultiCheckFlag(String multiCheckKey) {
		return merchantOnboardDAO.getMultiCheckFlag(multiCheckKey);
	}

	public void moveToBulkUploadFolder(Integer instId, File uploadFile, String filePath, String filename) {
		merchantOnboardDAO.moveToBulkUploadFolder(instId, uploadFile, filePath, filename);
	}
	
	public void writeToBackup(String filepath, String filename, Integer instid, Integer type) {
		merchantOnboardDAO.writeToBackup(filepath, filename, instid, type);
	}
	
	public void setMultiCheckFlag(String multiCheckKey) {
		merchantOnboardDAO.setMultiCheckkFlag(multiCheckKey);		
	}
	
	public void resetMultiCheckFlag(String multiCheckKey) {
		merchantOnboardDAO.resetMultiCheckFlag(multiCheckKey);
	}
	public List<String> getCryptographicMethodsList() {
		return cryptographicMethodsList;
	}
	public void setCryptographicMethodsList(List<String> cryptographicMethodsList) {
		this.cryptographicMethodsList = cryptographicMethodsList;
	}
	public List<String> getRoutingPlanList() {
		return routingPlanList;
	}
	public void setRoutingPlanList(List<String> routingPlanList) {
		this.routingPlanList = routingPlanList;
	}
	public List<String> getProcessingPlanNameList() {
		return processingPlanNameList;
	}
	public void setProcessingPlanNameList(List<String> processingPlanNameList) {
		this.processingPlanNameList = processingPlanNameList;
	}
	public List<String> getBankerSalesList() {
		return bankerSalesList;
	}
	public void setBankerSalesList(List<String> bankerSalesList) {
		this.bankerSalesList = bankerSalesList;
	}
	public List<String> getCustomerCountryList() {
		return customerCountryList;
	}
	public void setCustomerCountryList(List<String> customerCountryList) {
		this.customerCountryList = customerCountryList;
	}
	public List<String> getCustomerCityList() {
		return customerCityList;
	}
	public void setCustomerCityList(List<String> customerCityList) {
		this.customerCityList = customerCityList;
	}
	public List<String> getCustomerStateList() {
		return customerStateList;
	}
	public void setCustomerStateList(List<String> customerStateList) {
		this.customerStateList = customerStateList;
	}
	public List<String> getFeePlanNameList() {
		return feePlanNameList;
	}
	public void setFeePlanNameList(List<String> feePlanNameList) {
		this.feePlanNameList = feePlanNameList;
	}
	public List<String> getMccList() {
		return mccList;
	}
	public void setMccList(List<String> mccList) {
		this.mccList = mccList;
	}
	public List<String> getAcctBranchList() {
		return acctBranchList;
	}
	public void setAcctBranchList(List<String> acctBranchList) {
		this.acctBranchList = acctBranchList;
	}
	public List<String> getCrcyList() {
		return crcyList;
	}
	public void setCrcyList(List<String> crcyList) {
		this.crcyList = crcyList;
	}
	public List<String> getLstProuctTypeList() {
		return lstProuctTypeList;
	}
	public void setLstProuctTypeList(List<String> lstProuctTypeList) {
		this.lstProuctTypeList = lstProuctTypeList;
	}
	public List<String> getRecurringChargePlanList() {
		return recurringChargePlanList;
	}
	public void setRecurringChargePlanList(List<String> recurringChargePlanList) {
		this.recurringChargePlanList = recurringChargePlanList;
	}
	public List<String> getTransSourceList() {
		return transSourceList;
	}
	public void setTransSourceList(List<String> transSourceList) {
		this.transSourceList = transSourceList;
	}
	public List<String> getMerGradeList() {
		return merGradeList;
	}
	public void setMerGradeList(List<String> merGradeList) {
		this.merGradeList = merGradeList;
	}
	public List<String> getMerCountryOriginList() {
		return merCountryOriginList;
	}
	public void setMerCountryOriginList(List<String> merCountryOriginList) {
		this.merCountryOriginList = merCountryOriginList;
	}
	public List<String> getIncorpStatusList() {
		return incorpStatusList;
	}
	public void setIncorpStatusList(List<String> incorpStatusList) {
		this.incorpStatusList = incorpStatusList;
	}
	public List<String> getDocumtCatgList() {
		return documtCatgList;
	}
	public void setDocumtCatgList(List<String> documtCatgList) {
		this.documtCatgList = documtCatgList;
	}
	public List<String> getCardGroupList() {
		return cardGroupList;
	}
	public void setCardGroupList(List<String> cardGroupList) {
		this.cardGroupList = cardGroupList;
	}
	public ReleaseMerchantDAO getReleaseMerchantDAO() {
		return releaseMerchantDAO;
	}
	public void setReleaseMerchantDAO(ReleaseMerchantDAO releaseMerchantDAO) {
		this.releaseMerchantDAO = releaseMerchantDAO;
	}
	public Integer processBulkUploadMerchantFile(MerchantOnboardBean merchantBean, String srcFilePathwithname, String filename, String uploadType, SessionBean sessionBean) {
		InstHashcodeMapper.setInstitution(sessionBean.getInstCode());
		Integer output = 0; 
		List<MerchantOnboardBean> merchantOnboardBean = null;
		List<String> merchantErrorList = new ArrayList<String>();
		String status = null;
		try {
			merchantOnboardBean = merchantOnboardDAO.getMasterUploadData(uploadType);
			output = readBulkData(merchantBean, srcFilePathwithname, filename, merchantOnboardBean, uploadType, sessionBean.getUserId(), sessionBean.getInstCode(), merchantErrorList);
			if(output > 0 && merchantErrorList.isEmpty()) {
				status = (String) transactionTemplate.execute(new TransactionCallback() {
					public String doInTransaction(TransactionStatus ts) {
						String result = "";
						String procStatus = "OK";
						loggerUtil.debug("Before calling procedure: SP_BULK_UPLD");
						try {
							procStatus = merchantOnboardDAO.callBulkUpldProcedure(sessionBean);
						} catch (Exception e) {
							loggerUtil.error("Error occure while calling callBulkUpldProcedure : " + e.getMessage());
						}
						if (procStatus.equals("OK")) {
							result = "OK";
						} else {
							result = procStatus;
							ts.setRollbackOnly();
						}
						return result;
					}
				});
						
				if (!"OK".equals(status)) {
		            merchantBean.setErrorDesc("Procedure SP_BULK_UPLD failed with status: " + status);
		            loggerUtil.debug("Status of procedure SP_BULK_UPLD is : " + status);
		        } else {
		            merchantBean.setErrorDesc("SUCCESS");
		        }
			}
		} catch (Exception e) {
			loggerUtil.error("Error while processing processBulkUploadMerchantFile", e);
		} finally {
			merchantOnboardBean = null;
			merchantErrorList.clear();
			merchantErrorList = null;
			status = null;
		}
		return output;
	}
	
	private Integer readBulkData(MerchantOnboardBean merchantBean, String srcFilePathwithname, String filename, List<MerchantOnboardBean> merchantOnboardBean, String uploadType,
			String userId, String instCode, List<String> merchantErrorList) {
	    
	    Integer output = 0;
	    XSSFWorkbook workbook = null;
	    try {
	        if (!new PathValidator(new PathCleaner(), new PathTraversalDetector()).validatePath(srcFilePathwithname)) {
	            loggerUtil.error("Invalid file path: " + srcFilePathwithname);
	            return 0;
	        }

	        workbook = new XSSFWorkbook(new FileInputStream(srcFilePathwithname));
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        int totalRows = sheet.getPhysicalNumberOfRows();

	        if (totalRows <= 1) {
	            loggerUtil.error("No data rows found in the uploaded Excel file: " + filename);
	            String errorMsg = "No data rows found in the uploaded Excel file : " + filename +". Please ensure at least one record is present below the header.";
	            merchantErrorList.add(errorMsg);
	            merchantBean.setErrorDesc(errorMsg);
	            return 0;
	        }
	        
	        for (int rowIndex = 1; rowIndex < totalRows; rowIndex++) {
	            XSSFRow row = sheet.getRow(rowIndex);
	            if (row == null) continue;

	            List<String> cellValues = new ArrayList<>();
	            int notBlankCount = 0;

	            for (int colIndex = 0; colIndex < EXPECTED_COLUMN_COUNT; colIndex++) {
	                XSSFCell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
	                String cellValue = new DataFormatter().formatCellValue(cell).trim().replaceAll("\\P{Print}", "").replace("$", "").replace("&", "");
	                cellValue = StringEscapeUtils.escapeHtml4(cellValue);
	                if (!cellValue.trim().isEmpty()) notBlankCount++;
	                cellValues.add(cellValue);
	            }

	            if (cellValues.size() > EXPECTED_COLUMN_COUNT) {
	                cellValues = cellValues.subList(0, EXPECTED_COLUMN_COUNT);
	            }

	            int firstDataCol = -1;
	            for (int i = 0; i < cellValues.size(); i++) {
	                if (!cellValues.get(i).trim().isEmpty()) {
	                    firstDataCol = i;
	                    break;
	                }
	            }
	            if (firstDataCol > 0) {
	                List<String> aligned = new ArrayList<>(Collections.nCopies(EXPECTED_COLUMN_COUNT, ""));
	                int shiftOffset = EXPECTED_COLUMN_COUNT - (cellValues.size() - firstDataCol);
	                if (shiftOffset < 0) shiftOffset = 0;
	                for (int i = firstDataCol; i < cellValues.size(); i++) {
	                    aligned.set(i - firstDataCol + shiftOffset, cellValues.get(i));
	                }
	                cellValues = aligned;
	            }
	            
	            if (notBlankCount > 0) {
	            	output = validateBulkField(merchantBean, cellValues, rowIndex, uploadType, merchantOnboardBean, userId, instCode, srcFilePathwithname, filename, merchantErrorList);	
	            }
	            
	            /*if (notBlankCount > 0) {
	            	legalName = cellValues.get(4) != null ? cellValues.get(4).trim() : "";
	                custPan = cellValues.get(18) != null ? cellValues.get(18).trim() : "";
	
				    if (!legalName.isEmpty() && !custPan.isEmpty()) {
				        dupCount = merchantOnboardDAO.getDuplicateCount(legalName, custPan);
				        if (dupCount == 0) {
				        	output = validateBulkField(merchantBean, cellValues, rowIndex, uploadType, merchantOnboardBean, userId, instCode, srcFilePathwithname, filename, merchantErrorList);
				        } else {
				        	if (!duplicateLegalNames.contains(legalName)) { duplicateLegalNames.add(legalName); }
				            if (!duplicatePANs.contains(custPan)) { duplicatePANs.add(custPan); }
				        }
				    }
	            }*/
	        }
	        /*if (!duplicateLegalNames.isEmpty() && !duplicatePANs.isEmpty()) {
	            String errorMsg = "Duplicate record exists for the Legal Name: "+ String.join(", ", duplicateLegalNames) + ", PAN: " + String.join(", ", duplicatePANs);
	            merchantBean.setErrorDesc(errorMsg);
	        }*/
	    } catch (Exception e) {
	        loggerUtil.error("Error while reading Bulk Upload File: ", e);
	    } finally {
	        try {
	            if (workbook != null) workbook.close();
	        } catch (IOException e) {
	            loggerUtil.error("Error closing workbook: ", e);
	        }
	    }

	    return output;
	}
	
	private Integer validateBulkField(MerchantOnboardBean merchantBean,List<String> merchantBodyList, int recordNum, String uploadType,
		List<MerchantOnboardBean> merchantOnboardBean, String userId, String instCode, String srcFilePathwithname, String filename, List<String> merchantErrorList)
	{
		Integer output = 0, recStus = 0, dupCount = 0, bulkLogRefId = 0, bulkAppNmbr = 0, recStusToInsert = 0;
		String fieldErrorDescBulk = "OK", isMandatoryFlag = null, validationType = null, fieldDesc = null, condValidType = null, 
				possibleValues = null, tokenval = null, mercCode = null, custEmail = null, custPan = null, errorDesc = null;
		List<String> rowErrors = new ArrayList<>();
		boolean hasRowErrors = false, isErrorDescEmpty = false;
	    //List<String> duplicateLegalNames = new ArrayList<>();
		//List<String> duplicatePANs = new ArrayList<>();
		try {
			
			bulkLogRefId = merchantOnboardDAO.getBulkLogRefId();
			bulkAppNmbr = merchantOnboardDAO.getBulkAppNmbr();
			merchantOnboardDAO.insertBulkUpldLogDetails(Integer.parseInt(instCode), bulkLogRefId, filename, merchantBodyList.toString().substring(1, merchantBodyList.toString().length() - 1), bulkAppNmbr);
			
			for (int i = 0; i < merchantBodyList.size(); i++) {
				fieldErrorDescBulk = "";
				isMandatoryFlag = merchantOnboardBean.get(i).getMandatoryFlag();
				validationType = merchantOnboardBean.get(i).getValidationType();
				fieldDesc = merchantOnboardBean.get(i).getFieldDesc();
				condValidType = merchantOnboardBean.get(i).getCondValidType();
				possibleValues = merchantOnboardBean.get(i).getPosibleValues();
				tokenval = merchantBodyList.get(i) == null ? "" : merchantBodyList.get(i).trim();
				
				if (isMandatoryFlag.equals("Y") || condValidType.equals("Y") || isMandatoryFlag.equals("N") && tokenval.length() > 0) 
				{
					if(possibleValues!=null && !possibleValues.equals("")){
						boolean check = true;
						String values[] = possibleValues.split(",");
						for (String val : values) {
							//if(val.contains(tokenval)){
							if (val.trim().equalsIgnoreCase(tokenval.trim())) {
								check = false;
								break;
							}
						}
						if(check){
							fieldErrorDescBulk = fieldDesc+" : Field should contain possible values ("+possibleValues+") only";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					
					if (validationType.equals("AL")) { // Alpha
						if (!isAlpha(tokenval)) {
							fieldErrorDescBulk = fieldDesc + " : Field should have Alphabet only";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						if (tokenval.length() > merchantOnboardBean.get(i).getFieldLength()) {
							fieldErrorDescBulk = fieldDesc + " : Field length should not be greater than " + merchantOnboardBean.get(i).getFieldLength();
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					if (validationType.equals("ALS")) { // Alpha with Space
						if (!isAlphaWithSpace(tokenval)) {
							fieldErrorDescBulk = fieldDesc + " : Field should have Alphabet only with space";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						if (tokenval.length() > merchantOnboardBean.get(i).getFieldLength()) {
							fieldErrorDescBulk = fieldDesc + " : Field length should not be greater than " + merchantOnboardBean.get(i).getFieldLength();
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					if (validationType.equals("N")) { // Numeric
						if (!isNumeric(tokenval)) {
							fieldErrorDescBulk = fieldDesc + " : Field should have numbers only";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						if (tokenval.length() > merchantOnboardBean.get(i).getFieldLength()) {
							fieldErrorDescBulk = fieldDesc + " : Field length should not be greater than " + merchantOnboardBean.get(i).getFieldLength();
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					if (validationType.equals("AN")) {// Alphanumeric
						if (!isAlphaNumeric(tokenval)) {
							fieldErrorDescBulk = fieldDesc + " : Field should have alphanumeric values only";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						if (tokenval.length() > merchantOnboardBean.get(i).getFieldLength()) {
							fieldErrorDescBulk = fieldDesc + " : Field length should not be greater than " + merchantOnboardBean.get(i).getFieldLength();
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					if (validationType.equals("ANS")) {// Alphanumeric with space
						if (!isAlphaNumericWithSpace(tokenval)) {
							fieldErrorDescBulk = fieldDesc + " : Field should have alphanumeric with space values only";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						if (tokenval.length() > merchantOnboardBean.get(i).getFieldLength()) {
							fieldErrorDescBulk = fieldDesc + " : Field length should not be greater than " + merchantOnboardBean.get(i).getFieldLength();
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					if (validationType.equals("EMAIL")) {// Valid email check
						if (!isValidEmail(tokenval)) {
							fieldErrorDescBulk = fieldDesc + " : Field should have proper email Id";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						if (tokenval.length() > merchantOnboardBean.get(i).getFieldLength()) {
							fieldErrorDescBulk = fieldDesc + " : Field length should not be greater than " + merchantOnboardBean.get(i).getFieldLength();
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					if (validationType.equals("ANSPCL")) {// Alphanumeric with special chars
						if (!isAplhaNumericWithSpaceAndSpecialChar(tokenval)) {
							fieldErrorDescBulk = fieldDesc + " : Field should have alphanumeric with special chars values only";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						if (tokenval.length() > merchantOnboardBean.get(i).getFieldLength()) {
							fieldErrorDescBulk = fieldDesc + " : Field length should not be greater than " + merchantOnboardBean.get(i).getFieldLength();
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					if (validationType.equals("PAN")) {// Valid PAN check
						if (!isValidPAN(tokenval)) {
							fieldErrorDescBulk = fieldDesc + " : Field should have proper PAN format";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						if (tokenval.length() > merchantOnboardBean.get(i).getFieldLength()) {
							fieldErrorDescBulk = fieldDesc + " : Field length should not be greater than " + merchantOnboardBean.get(i).getFieldLength();
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					
					if (isMandatoryFlag.equals("Y") && tokenval.equals("")) {
						if(merchantBodyList.get(i) == null || merchantBodyList.get(i).equals("")) {
							fieldErrorDescBulk = fieldDesc + " : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
					}
					
					// 2 -- MID
					if(i == 2 && merchantBodyList.get(2).length() > 9) {
						fieldErrorDescBulk = fieldDesc + " :  should be less than or equal to 9 digit values";
						recStus = 3;
						if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
					}
					
					// 23 -- SID
					if(i == 23 && (merchantBodyList.get(23).length() < 8 || merchantBodyList.get(23).length() > 15)) {
						fieldErrorDescBulk = fieldDesc + " : should contain between 8 and 15 digit values";
						recStus = 3;
						if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
					}
					
					// 49 -- TID
//					if(i == 49 && merchantBodyList.get(39).equals("YES") && merchantBodyList.get(49).length() > 8) {
					if(i == 49 && merchantBodyList.get(49).length() > 8) {
						fieldErrorDescBulk = fieldDesc + " : Should less than or equal to 8 digit values";
						recStus = 3;
						if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
					}
										
					// 34 -- GST Flag
					/*if ((i == 31 && merchantBodyList.get(31).equals("YES"))){
						String gstno = merchantBodyList.get(32) == null ? "" : merchantBodyList.get(32).trim();
						String condValidType1 = merchantOnboardBean.get(32).getCondValidType() == null ? "N" : merchantOnboardBean.get(32).getCondValidType();
						if (condValidType1.equals("Y") && gstno.equals("")) {
							fieldErrorDescBulk = "GST Number : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { merchantErrorList.add(fieldErrorDescBulk); }
						}
						gstno = null; condValidType1 = null;
					}*/
					
					// 34 -- IS Govt Registered Merchant
					if ((i == 34 && merchantBodyList.get(34).equalsIgnoreCase("YES"))){
						String mco = merchantBodyList.get(35) == null ? "" : merchantBodyList.get(35).trim();
						String condValidType1 = merchantOnboardBean.get(35).getCondValidType() == null ? "N" : merchantOnboardBean.get(35).getCondValidType();
						if (condValidType1.equalsIgnoreCase("Y") && mco.trim().isEmpty()) {
							fieldErrorDescBulk = "Merchant Country Origin : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						mco = null; condValidType1 = null;
					}
					
					// 39 -- IS Terminal Required
					if ((i == 39 && merchantBodyList.get(39).equalsIgnoreCase("YES"))){
						String termID = merchantBodyList.get(49) == null ? "" : merchantBodyList.get(49).trim();
						String condValidType1 = merchantOnboardBean.get(49).getCondValidType() == null ? "N" : merchantOnboardBean.get(49).getCondValidType();
						if (condValidType1.equalsIgnoreCase("Y") && termID.trim().isEmpty()) {
							fieldErrorDescBulk = "Terminal ID : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						termID = null; condValidType1 = null;
					}
					
					// 40 -- Account with Bank
					if ((i == 40 && merchantBodyList.get(40).equalsIgnoreCase("YES"))){
						String acctBranch = merchantBodyList.get(41) == null ? "" : merchantBodyList.get(41).trim();
						String condValidType1 = merchantOnboardBean.get(41).getCondValidType() == null ? "N" : merchantOnboardBean.get(41).getCondValidType();
						if (condValidType1.equalsIgnoreCase("Y") && acctBranch.trim().isEmpty()) {
							fieldErrorDescBulk = "Account Branch : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						acctBranch = null; condValidType1 = null;
					} else if ((i == 40 && merchantBodyList.get(40).equalsIgnoreCase("NO"))){ // 44 -- Bank Name
						String bankName = merchantBodyList.get(42) == null ? "" : merchantBodyList.get(43).trim();
						String condValidType1 = merchantOnboardBean.get(42).getCondValidType() == null ? "N" : merchantOnboardBean.get(42).getCondValidType();
						if (condValidType1.equalsIgnoreCase("Y") && bankName.trim().isEmpty()) {
							fieldErrorDescBulk = "Bank Name : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { rowErrors.add(fieldErrorDescBulk); }
						}
						bankName = null; condValidType1 = null;
					}
					
					// 48 -- Geo Coordinates
					/*if ((i == 48 && merchantBodyList.get(48).equals("YES"))){
						String latitude = merchantBodyList.get(49) == null ? "" : merchantBodyList.get(49).trim();
						String condValidType1 = merchantOnboardBean.get(49).getCondValidType() == null ? "N" : merchantOnboardBean.get(49).getCondValidType();
						if (condValidType1.equals("Y") && latitude.equals("")) {
							fieldErrorDescBulk = "Latitude : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { merchantErrorList.add(fieldErrorDescBulk); }
						}							
						String longitude = merchantBodyList.get(50) == null ? "" : merchantBodyList.get(50).trim();
						String condValidType2 = merchantOnboardBean.get(50).getCondValidType() == null ? "N" : merchantOnboardBean.get(50).getCondValidType();
						if (condValidType2.equals("Y") && longitude.equals("")) {
							fieldErrorDescBulk = "Longitude : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { merchantErrorList.add(fieldErrorDescBulk); }
						}
						latitude = null; condValidType1 = null; longitude = null; condValidType2 = null;
					}*/
														
					// 52 -- Enable QR
					/*if ((i == 52 && merchantBodyList.get(52).equals("YES"))){
						String appId = merchantBodyList.get(53) == null ? "" : merchantBodyList.get(53).trim();
						String condValidType1 = merchantOnboardBean.get(53).getCondValidType() == null ? "N" : merchantOnboardBean.get(53).getCondValidType();
						if (condValidType1.equals("Y") && appId.equals("")) {
							fieldErrorDescBulk = "Application ID : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { merchantErrorList.add(fieldErrorDescBulk); }
						}
						String authToken = merchantBodyList.get(54) == null ? "" : merchantBodyList.get(54).trim();
						String condValidType2 = merchantOnboardBean.get(54).getCondValidType() == null ? "N" : merchantOnboardBean.get(54).getCondValidType();
						if (condValidType2.equals("Y") && authToken.equals("")) {
							fieldErrorDescBulk = "Authentication Token : Field Should not be Empty";
							recStus = 3;
							if (!(fieldErrorDescBulk.equalsIgnoreCase("") || fieldErrorDescBulk.equalsIgnoreCase("OK"))) { merchantErrorList.add(fieldErrorDescBulk); }
						}
						appId = null; condValidType1 = null; authToken = null; condValidType2 = null;
					}*/
				}
			}
			
			/*if (!rowErrors.isEmpty()) {
				merchantErrorList.add(rowErrors.toString());
				output = merchantOnboardDAO.insertTempTable(merchantBean, merchantBodyList, merchantBodyList.size(), srcFilePathwithname, filename,
						userId, instCode, recStus, recordNum, rowErrors.toString().substring(1, rowErrors.toString().length() - 1), bulkLogRefId, bulkAppNmbr);
			} else {
				output = merchantOnboardDAO.insertTempTable(merchantBean, merchantBodyList, merchantBodyList.size(), srcFilePathwithname, filename,
						userId, instCode, recStus, recordNum, fieldErrorDescBulk, bulkLogRefId, bulkAppNmbr);
			}*/
			
			try {
				mercCode = merchantBodyList.get(2).trim() != null || !merchantBodyList.get(2).trim().isEmpty() ? merchantBodyList.get(2).trim() : "";
				custEmail = merchantBodyList.get(17).trim() != null || !merchantBodyList.get(17).trim().isEmpty() ? merchantBodyList.get(17).trim() : "";
			    custPan = merchantBodyList.get(18).trim() != null || !merchantBodyList.get(18).trim().isEmpty() ? merchantBodyList.get(18).trim() : "";
			    if (!mercCode.isEmpty() && !custEmail.isEmpty() && !custPan.isEmpty()) {
			        dupCount = merchantOnboardDAO.getDuplicateCount(mercCode, custEmail, custPan);
			        hasRowErrors = (rowErrors != null && !rowErrors.isEmpty());

			        if (dupCount == 0) {
			        	recStusToInsert = hasRowErrors ? recStus : 0; // if not duplicate recstus = 3 (for Error) if no errors then recstus = 0
			            errorDesc = hasRowErrors ? rowErrors.toString().substring(1, rowErrors.toString().length() - 1) : fieldErrorDescBulk;

			            if (hasRowErrors) { merchantErrorList.add(rowErrors.toString()); }

			            output = merchantOnboardDAO.insertTempTable(merchantBean, merchantBodyList, merchantBodyList.size(), srcFilePathwithname, filename, 
			            		userId, instCode, recStusToInsert, recordNum, errorDesc, bulkLogRefId, bulkAppNmbr);
			        } else {
			        	recStusToInsert = 6;
				        String duplicateMsg = "Duplicate record is present for Application No. " + bulkAppNmbr;
				        if (hasRowErrors) {
				            errorDesc = duplicateMsg + " | " + rowErrors.toString().substring(1, rowErrors.toString().length() - 1);
				            merchantErrorList.add(rowErrors.toString());
				        } else {
				            errorDesc = duplicateMsg;
				        }
						output = merchantOnboardDAO.insertTempTable(merchantBean, merchantBodyList, merchantBodyList.size(), srcFilePathwithname, filename, 
								userId, instCode, recStusToInsert, recordNum, errorDesc, bulkLogRefId, bulkAppNmbr);
			        }
			    } else {
			    	recStusToInsert = 3;
			    	hasRowErrors = (rowErrors != null && !rowErrors.isEmpty());
			    	errorDesc = hasRowErrors ? rowErrors.toString().substring(1, rowErrors.toString().length() - 1) : fieldErrorDescBulk;
			    	output = merchantOnboardDAO.insertTempTable(merchantBean, merchantBodyList, merchantBodyList.size(), srcFilePathwithname, filename, 
		            		userId, instCode, recStusToInsert, recordNum, errorDesc , bulkLogRefId, bulkAppNmbr);
			    }
			} catch (Exception e) {
			    loggerUtil.error("Error during duplicate record check: ", e);
			}
			
			merchantBodyList.clear();
			rowErrors.clear();
		} catch (Exception e) {
			merchantOnboardDAO.updateBulkUpldLogDetails(bulkLogRefId, "Failure - "+e.getMessage(), 2);
			loggerUtil.error("Error while inserting into temp table : ", e);
		}
		finally {
			fieldErrorDescBulk = null; rowErrors = null; bulkLogRefId = null; isMandatoryFlag = null; validationType = null; fieldDesc = null; 
			condValidType = null; possibleValues = null; tokenval = null; mercCode = null; custEmail = null; custPan = null; errorDesc = null;
		}
		return output;
	}
	
	public static boolean isAlpha(String content) {
		if (content == null) {
			return false;
		}

		Pattern numeric = Pattern.compile("[a-zA-Z]*");
		Matcher matcher = numeric.matcher(content);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNumeric(String content) {
		if (content == null) {
			return false;
		}
		Pattern numeric = Pattern.compile("[0-9.]*");
		Matcher matcher = numeric.matcher(content);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isAlphaNumeric(String content) {
		if (content == null) {
			return false;
		}
		Pattern numeric = Pattern.compile("[0-9a-zA-Z]*");
		Matcher matcher = numeric.matcher(content);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isAlphaWithSpace(String content) {
	    if (content == null) {
			return false;
		}
	    Pattern alpha = Pattern.compile("[a-zA-Z\\s]*");
		Matcher matcher = alpha.matcher(content);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isAlphaNumericWithSpace(String content) {
		if (content == null) {
			return false;
		}
		Pattern numeric = Pattern.compile("[0-9a-zA-Z\\s]*");
		Matcher matcher = numeric.matcher(content);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isValidEmail(String number) {
		try {
			Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			Matcher matcher = pattern.matcher(number);

			if (!matcher.matches()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	public static boolean isAplhaNumericWithSpaceAndSpecialChar(String number) {
		try {
			if (number.length() == 1) {
				Pattern pattern = Pattern.compile("^[a-zA-Z0-9()\\-_@,\\.%\\s:;\\/]$");
				Matcher matcher = pattern.matcher(number);
				if (!matcher.matches()) {
					return false;
				}
			} else {
				Pattern pattern = Pattern.compile("^([a-zA-Z0-9()\\-_@,\\.%\\s:;\\/]+~?[a-zA-Z0-9()\\-_@,\\.%\\s:;\\/]+)+(~?[a-zA-Z0-9()\\-_@,\\.%\\s:;\\/]+)*$");
				Matcher matcher = pattern.matcher(number);

				if (!matcher.matches()) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	public static boolean isValidPAN(String number) {
		try {
			Pattern pattern = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]{1}$");
			Matcher matcher = pattern.matcher(number);

			if (!matcher.matches()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}
	
	public String getBulkUpldLogDetailsCount(String filename) {
		return merchantOnboardDAO.getBulkUpldLogDetailsCount(filename);
	}
	
	public List<MerchantOnboardBean> getmerchantOnboardSearchResultDetails(Integer instId, MerchantOnboardBean merchantOnboardBean, String dateFormat) {
		return merchantOnboardDAO.getmerchantOnboardSearchResultDetails(instId, merchantOnboardBean, dateFormat);
	}
	
	public List<MerchantOnboardBean> getmerchantOnboardCheckerResultPage(Integer instId, MerchantOnboardBean merchantOnboardBean, String dateFormat, Integer userId) {
		return merchantOnboardDAO.getmerchantOnboardCheckerResultPage(instId, merchantOnboardBean, dateFormat, userId);
	}
	
	public MerchantOnboardBean approveMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.approveMerchantOnBoard(merchantOnboardBean, instId);
	}
	
	public MerchantOnboardBean rejectMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.rejectMerchantOnBoard(merchantOnboardBean, instId);
	}
	
	public MerchantOnboardBean merchantOnBoardMethod(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.merchantOnBoardMethod(merchantOnboardBean, instId);
	}
	
	public MerchantOnboardBean reassignMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.reassignMerchantOnBoard(merchantOnboardBean, instId);
	}
	
	public String updateMerchantOnBoardMethod(MerchantOnboardBean merchantOnboardBean, Integer instId, String mode) {
		return merchantOnboardDAO.updateMerchantOnBoardMethod(merchantOnboardBean, instId, mode);
	}
	
	// Added Methods for Instant Onboard - Maker
	
	@Override
	public MerchantOnboardBean addMerchantDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		return merchantOnboardDAO.addMerchantDetails(merchantOnboardBean, instId, userId);
	}
	@Override
	public MerchantOnboardBean addPriceDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId, String entityLevel) {
		return merchantOnboardDAO.addPriceDetails(merchantOnboardBean, instId, userId, entityLevel);
	}
	@Override
	public MerchantOnboardBean addStoreWithAccountDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId, Integer userId) {
		return merchantOnboardDAO.addStoreWithAccountDetails(merchantOnboardBean, storeBean, instId, userId);
	}
	@Override
	public MerchantOnboardBean addTerminalWithAccountDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, MerchantOnboardTerminalBean terminalBean, Integer instId, Integer userId) {
		return merchantOnboardDAO.addTerminalWithAccountDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId);
	}
	@Override
	public MerchantOnboardBean addAccountDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		return merchantOnboardDAO.addAccountDetails(merchantOnboardBean, instId, userId);
	}
	@Override
	public void rollBackFunctionForMerchantOnboard(MerchantOnboardBean merchantOnboardBean) {
		merchantOnboardDAO.rollBackFunctionForMerchantOnboard(merchantOnboardBean);
	}
	@Override
	public MerchantOnboardBean updateMerchantDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		return merchantOnboardDAO.updateMerchantDetails(merchantOnboardBean, instId, userId);
	}
	@Override
	public MerchantOnboardBean updatePriceDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		return merchantOnboardDAO.updatePriceDetails(merchantOnboardBean, instId, userId);
	}
	@Override
	public MerchantOnboardBean updateAccountDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		return merchantOnboardDAO.updateAccountDetails(merchantOnboardBean, instId, userId);
	}

	@Override
	public MerchantOnboardBean updateStoreWithAccountDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId, Integer userId) throws Exception {
		return merchantOnboardDAO.updateStoreWithAccountDetails(merchantOnboardBean, storeBean, instId, userId);
	}

	@Override
	public MerchantOnboardBean updateTerminalWithAccountDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, MerchantOnboardTerminalBean terminalBean, Integer instId,
			Integer userId) throws Exception {
		return merchantOnboardDAO.updateTerminalWithAccountDetails(merchantOnboardBean, storeBean, terminalBean ,instId, userId);
	}

	@Override
	public void errorUpdateForMerchantOnboard(MerchantOnboardBean merchantOnboardBean, Integer userId) {
		merchantOnboardDAO.errorUpdateForMerchantOnboard(merchantOnboardBean, userId);
	}
	
	@Override
	public List<JSONObject> mccautocomplete() throws Exception {
		return merchantOnboardDAO.mccautocomplete();
	}
	@Override
	public List<JSONObject> getCountryDetailsList(int instId) throws Exception {
		return merchantOnboardDAO.getCountryDetailsList(instId);
	}
	@Override
	public List<JSONObject> getStateDetailsList(String countrycode, int instId) throws Exception {
		return merchantOnboardDAO.getStateDetailsList(countrycode, instId);
	}
	@Override
	public List<JSONObject> getCityDetailsList(String countrycode, String statecode, int instId) throws Exception {
		return merchantOnboardDAO.getCityDetailsList(countrycode, statecode, instId);
	}
	/*@Override
	public List<JSONObject> getDivisionDetailsList(String countrycode,int instId) throws Exception {
		return merchantOnboardDAO.getDivisionDetailsList(countrycode, instId);
	}
	@Override
	public List<JSONObject> getStateDetailsList(String divisioncode, int instId) throws Exception {
		return merchantOnboardDAO.getStateDetailsList(divisioncode, instId);
	}
	@Override
	public List<JSONObject> getCityDetailsList(String statecode, int instId) throws Exception {
		return merchantOnboardDAO.getCityDetailsList(statecode, instId);
	}
	@Override
	public List<JSONObject> getRegionDetailsList(String citycode, int instId) throws Exception {
		return merchantOnboardDAO.getRegionDetailsList(citycode, instId);
	}*/
	@Override
	public MerchantOnboardBean getMerchantDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) throws Exception {
		return merchantOnboardDAO.getMerchantDetails(merchantOnboardBean,instId);
	}
	@Override
	public MerchantOnboardBean getPricingDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) throws Exception {
		return merchantOnboardDAO.getPricingDetails(merchantOnboardBean,instId);
	}
	@Override
	public MerchantOnboardBean getAccountDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) throws Exception {
		return merchantOnboardDAO.getAccountDetails(merchantOnboardBean,instId);
	}
	@Override
	public List<MerchantOnboardStoreBean> getAllStoreAndTerminalDetails(MerchantOnboardBean merchantOnboardBean, Integer instId)
			throws Exception {
		return merchantOnboardDAO.getAllStoreAndTerminalDetails(merchantOnboardBean,instId);
	}
	@Override
	public List<MapsBaseBean> getCurrency() throws Exception {
		return merchantOnboardDAO.getCurrency();
	}
	@Override
	public List<JSONObject> getMercAggrList(Integer instId) throws Exception {
		return merchantOnboardDAO.getMercAggrList(instId);
	}
	@Override
	public List<JSONObject> getMerchantChargePlanForTerminal(Integer instId) throws Exception {
		return merchantOnboardDAO.getTerminalChargePlanForPricing(instId);
	}
	@Override
	public List<KeyValueBean> getTerminalProfileList(Integer instId) {
		return merchantOnboardDAO.getTerminalProfileList(instId);
	}
	@Override
	public List<KeyValueBean> getEcomTerminalProfileList(Integer instId) {
		return merchantOnboardDAO.getEcomTerminalProfileList(instId);
	}
	@Override
	public void updateAttachementPath(MerchantOnboardBean merchantOnboardBean, Integer instId) throws Exception {
		merchantOnboardDAO.updateAttachementPath(merchantOnboardBean, instId);		
	}
	@Override
	public List<KeyValueBean> getTerminalProductType(List<KeyValueBean> lstProuctType, Integer instId, String mercType) {
		return merchantOnboardDAO.getTerminalProductType(lstProuctType, instId, mercType);
	}
	
	// Attachments
	
	public String getPathPrefix() throws Exception{
		return merchantOnboardDAO.getPathPrefix();
	}
	public void doAttachment(MerchantOnboardAttachementBean merchantOnboardAttachementBean) throws Exception{
		merchantOnboardDAO.doAttachment(merchantOnboardAttachementBean);
	}
	public List<MerchantOnboardAttachementBean> getAttachmentsDetails(MerchantOnboardAttachementBean merchantOnboardAttachementBean)throws Exception {		
		return merchantOnboardDAO.getAttachmentsDetails(merchantOnboardAttachementBean);
	}
	public void doAttachmentWithoutReferral(MerchantOnboardAttachementBean merchantOnboardAttachementBean) throws Exception{
		merchantOnboardDAO.doAttachmentWithoutReferral(merchantOnboardAttachementBean);
	}
	public List<MerchantOnboardAttachementBean> getAttachmentsDetailsWithoutReferral(MerchantOnboardAttachementBean merchantOnboardAttachementBean) throws Exception {
		return merchantOnboardDAO.getAttachmentsDetailsWithoutReferral(merchantOnboardAttachementBean);
	}
	public void doDetachment(MerchantOnboardAttachementBean merchantOnboardAttachementBean) throws Exception{
		merchantOnboardDAO.doDetachment(merchantOnboardAttachementBean);
	}
	public void doDetachmentWithoutReferral(MerchantOnboardAttachementBean merchantOnboardAttachementBean) throws Exception{
		merchantOnboardDAO.doDetachmentWithoutReferral(merchantOnboardAttachementBean);
	}
	@Override
	public List<MerchantOnboardBean> showDataOfFile(String uploadType, String filename) {
		return merchantOnboardDAO.showDataOfFile(uploadType,filename);
	}
	@Override
	public List<MerchantOnboardBean> getBulkUploadReportDetails(String fileName, String reportType, String uploadType) {
		return merchantOnboardDAO.getBulkUploadReportDetails(fileName, reportType, uploadType);
	}
	@Override
	public List<MerchantOnboardBean> getSuccessRecordList(String fileName, String uploadType) {
		return merchantOnboardDAO.getSuccessRecordList(fileName, uploadType);
	}
	@Override
	public List<MerchantOnboardBean> getFailureRecordList(String fileName, String uploadType) {
		return merchantOnboardDAO.getFailureRecordList(fileName, uploadType);
	}
	@Override
	public void deleteTerminalDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId, Integer userId) {
		merchantOnboardDAO.deleteTerminalDetails(merchantOnboardBean, storeBean, instId, userId);
	}
	@Override
	public MerchantOnboardBean insertOrUpdateToProdTemp(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId) {
		return merchantOnboardDAO.insertOrUpdateToProdTemp(merchantOnboardBean, storeBean, instId);
	}
	@Override
	public String getAutoGenerateCode(Integer instId, Integer level) {
		return merchantOnboardDAO.getAutoGenerateCode(instId, level);
	}
	@Override
	public List<KeyValueBean> getMerchantEntityList(Integer instId, String mercType) {
		return merchantOnboardDAO.getMerchantEntityList(instId, mercType);
	}
	@Override
	public int getCheckAggrAcctDetails(Integer instId, String mercAggrID) {
		return merchantOnboardDAO.getCheckAggrAcctDetails(instId, mercAggrID);
	}
	@Override
	public void deleteAccountDetails(MerchantOnboardBean merchantOnboardBean, String accountlevel, Integer instId, Integer userId) {
		merchantOnboardDAO.deleteAccountDetails(merchantOnboardBean, accountlevel, instId, userId);
	}
	@Override
	public List<KeyValueBean> getTerminalProductTypeForEdit(List<KeyValueBean> termlstProuctType, Integer instId, String merchantEntityName) {
		return merchantOnboardDAO.getTerminalProductTypeForEdit(termlstProuctType, instId, merchantEntityName);
	}
	
	// WebService Call
	@Override
	public String sendReleasMerchantData(Integer instId, String refNo, String userId) {
		String result = null;
		result = releaseMerchantDAO.approveMerchantApplication(instId, Long.parseLong(refNo), Long.parseLong(userId), true);
		if(result.equalsIgnoreCase("S")==false) {
			merchantOnboardDAO.updateFailedMerchantStatus(instId, refNo, userId, result);
		}
		
		return result;
	}
	@Override
	public List<String> getReferralDetailRelease(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.getReferralDetailRelease(merchantOnboardBean, instId);
	}
	@Override
	public void updateFailedMerchantStatus(Integer instId, String refNo, String userId, String result) {
		merchantOnboardDAO.updateFailedMerchantStatus(instId, refNo, userId, result);
	}
	
	//DIB CHANGES
	public String getParamValue(String paramkey) {
		return merchantOnboardDAO.getParamValue(paramkey);
	}
	public String getCountryName(String countrycode) {
		return merchantOnboardDAO.getCountryName(countrycode);
	}
	public List<KeyValueBean> getMerchantTypeList(Integer instId){
		return merchantOnboardDAO.getMerchantTypeList(instId);
	}
	@Override
	public MerchantOnboardBean addStorePriceDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId, Integer userId, String entityLevel) {
		return merchantOnboardDAO.addStorePriceDetails(merchantOnboardBean, storeBean, instId, userId, entityLevel);
	}
	@Override
	public List<KeyValueBean> getNatureOfBusinessCatgList(Integer instId) {
		return merchantOnboardDAO.getNatureOfBusinessCatgList(instId);
	}
	@Override
	public List<KeyValueBean> getProofOfDocumentList(int instId, String natureOfBusiness) {
		return merchantOnboardDAO.getProofOfDocumentList(instId, natureOfBusiness);
	}
	public Integer mssUserCheck(String refNo, Integer instId) {
		return merchantOnboardDAO.mssUserCheck(refNo, instId);
	}
	public String findUserPasswordLength(String loginId) {
		return merchantOnboardDAO.findUserPasswordLength(loginId);
	}
	public String getGrpSqidForMssSuperAdmnUsr(Integer instId) {
		return merchantOnboardDAO.getGrpSqidForMssSuperAdmnUsr(instId);
	}
	public String getLoginURL(String loginUrlParmKey) {
		return merchantOnboardDAO.getLoginURL(loginUrlParmKey);
	}
	@Override
	public MerchantOnboardBean addTerminalPriceDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, MerchantOnboardTerminalBean terminalBean, Integer instId, Integer userId, String entityLevel) {
		return merchantOnboardDAO.addTerminalPriceDetails(merchantOnboardBean, storeBean, terminalBean, instId, userId, entityLevel);
	}
	@Override
	public List<KeyValueBean> getPaymentMethodList(Integer instId) {
		return merchantOnboardDAO.getPaymentMethodList(instId);
	}
	@Override
	public List<KeyValueBean> getPrfDocsProvList(Integer instId, String prfDocsProv) {
		return merchantOnboardDAO.getPrfDocsProvList(instId, prfDocsProv);
	}
	@Override
	public int checkDocumentValidation(int instId, String entitySeqId, String tempFolderName) {
		return merchantOnboardDAO.checkDocumentValidation(instId, entitySeqId, tempFolderName);
	}
	@Override
	public void updateAttachementPathForStore(Integer instId, String storelevel, Integer refNo, Integer storeSqid, String tempFolderName, byte totalStoreCount, byte storecount) {
		merchantOnboardDAO.updateAttachementPathForStore(instId, storelevel, refNo, storeSqid, tempFolderName, totalStoreCount, storecount);
	}
	@Override
	public String getTempFoldername() {
		return merchantOnboardDAO.getTempFoldername();
	}
	@Override
	public void removeUnusedAttachmentsFolderForStore(String folderPath, String valueOf) {
		merchantOnboardDAO.removeUnusedAttachmentsFolderForStore(folderPath, valueOf);
	}
	@Override
	public int checkSessionForMerchant(int instId, int refNo, int userId) {
		return merchantOnboardDAO.checkSessionForMerchant(instId, refNo, userId);
	}
	@Override
	public void updateSessionFlagForMerchant(int instId, int refNo, int status, int userId) {
		merchantOnboardDAO.updateSessionFlagForMerchant(instId, refNo, status, userId);
	}
	@Override
	public Integer checkSessionForMerchantActivity(int instId, int refNo, int userId) {
		return merchantOnboardDAO.checkSessionForMerchantActivity(instId, refNo, userId);
	}
	@Override
	public List<JSONObject> getAcctBranchList(int instId) throws Exception {
		return merchantOnboardDAO.getAcctBranchList(instId);
	}
	@Override
	public MerchantOnboardBean getMerchantLogoDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		return merchantOnboardDAO.getMerchantLogoDetails(merchantOnboardBean, instId, userId);
	}
	@Override
	public MerchantOnboardBean getMerchantMastDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.getMerchantMastDetails(merchantOnboardBean, instId);
	}
	@Override
	public MerchantOnboardBean getPricingMastDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.getPricingMastDetails(merchantOnboardBean, instId);
	}
	@Override
	public MerchantOnboardBean getAccountMastDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.getAccountMastDetails(merchantOnboardBean, instId);
	}
	@Override
	public void rollBackFunctionForAddnewStore(MerchantOnboardBean merchantOnboardBean) {
		merchantOnboardDAO.rollBackFunctionForAddnewStore(merchantOnboardBean);
	}
	@Override
	public void updateAddnewStoreFlag(MerchantOnboardBean merchantOnboardBean, Integer status, Integer userId) {
		merchantOnboardDAO.updateAddnewStoreFlag(merchantOnboardBean, status, userId);
	}
	@Override
	public MerchantOnboardBean getMerchantRemarksDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.getMerchantRemarksDetails(merchantOnboardBean, instId);
	}
	@Override
	public Integer getAdditionalNewStoreFlag(Integer instId, int refno, Integer userId) {
		return merchantOnboardDAO.getAdditionalNewStoreFlag(instId, refno, userId);
	}
	@Override
	public String getUserNameUsingID(int userId) {
		return merchantOnboardDAO.getUserNameUsingID(userId);
	}
	@Override
	public String getUserEmailID(int userId) {
		return merchantOnboardDAO.getUserEmailID(userId);
	}
	@Override
	public String getMakerEmailIDusingRefno(String refNo) {
		return merchantOnboardDAO.getMakerEmailIDusingRefno(refNo);
	}
	@Override
	public String getlistCheckerEmailIDs(int userId) {
		return merchantOnboardDAO.getlistCheckerEmailIDs(userId);
	}
	@Override
	public String getMerchantIDusingRefno(String refNo) {
		return merchantOnboardDAO.getMerchantIDusingRefno(refNo);
	}
	@Override
	public String getMerchantEmailIDusingRefno(String refNo) {
		return merchantOnboardDAO.getMerchantEmailIDusingRefno(refNo);
	}
	
	@Override
	public int getRecStatusByRefId(Integer instId, int refno) {
		return merchantOnboardDAO.getRecStatusByRefId(instId, refno);
	}
	@Override
	public int getMainStatusByRefId(Integer instId, int refno) {
		return merchantOnboardDAO.getMainStatusByRefId(instId, refno);
	}
	@Override
	public MerchantOnboardBean activateMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.activateMerchantOnBoard(merchantOnboardBean,instId);
	}
	@Override
	public MerchantOnboardBean deactivateMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return merchantOnboardDAO.deactivateMerchantOnBoard(merchantOnboardBean,instId);
	}
	@Override
	public Integer getNoOfStoresReqCheck(Integer instId, MerchantOnboardBean bean) {
		return merchantOnboardDAO.getNoOfStoresReqCheck(instId, bean);
	}
}
