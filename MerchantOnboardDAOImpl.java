package com.fss.maps.merchantsales.merchantonboard.dao;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.fss.maps.KeyUtility.AzureKeyConfigUtils;
import com.fss.maps.batch.gen.genCommon.GenObjectPool;
import com.fss.maps.batch.intchg.intchgCommon.IntchgConstants;
import com.fss.maps.batch.sanfileutils.SANFileReader;
import com.fss.maps.common.bean.KeyValueBean;
import com.fss.maps.common.bean.MapsBaseBean;
import com.fss.maps.common.bean.SessionBean;
import com.fss.maps.common.dao.LogWebServiceDataDao;
import com.fss.maps.common.dao.MasterListDAO;
import com.fss.maps.common.pathvalidation.PathCleaner;
import com.fss.maps.common.pathvalidation.PathTraversalDetector;
import com.fss.maps.common.pathvalidation.PathValidator;
import com.fss.maps.common.services.LogWebServiceDataService;
import com.fss.maps.common.util.MapsConstants;
import com.fss.maps.common.util.logger.LoggerUtil;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardAttachementBean;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardBean;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardStoreBean;
import com.fss.maps.merchantsales.merchantonboard.bean.MerchantOnboardTerminalBean;

import net.sf.json.JSONObject;

public class MerchantOnboardDAOImpl implements MerchantOnboardDAO{

	LoggerUtil loggerUtil =new LoggerUtil(MerchantOnboardDAOImpl.class);
	public static final String ATTACHED = "1";
	public static final String DEATTACHED = "0";
	public static final String APPROVED = "1";
	public static final String ATTACHMENT_SOURCE_MAPS = "0";
	public static final String ATTACHMENT_SOURCE_MSS = "1";
	private static final String SUCCESS = null;
	public static final Integer MERCHANT_ENTY_REF_NUM = 1;
	private static final String ERROR = null;
	private static final Integer ACTIVE=1;
	private static final Integer ONBOARDED=5;
	private static final int TEMPFOLDERNAME_LEN = 8;
	private static final String merchantLevel = "1";
	private static final String storeLevel = "2";
	private static final String terminalLevel = "3";
	private static final String MERCHANTONBOARDCHECKERSURL = "/merchantsales/merchantOnboardCheckerResultPage.action";
	private static final String GROUPTYPE = "5";
	
	private MasterListDAO masterListDAO;
	private String aggrMCC = null;
	private LogWebServiceDataDao logWebServiceDataDao = null;
	private LogWebServiceDataService logWebServiceDataService = null;
	private String vpasMpan;
	private String mcardMpan;
	private String uniqIdent;
	private String jcpMpan;
	private String npsbMpan;
	private String terminalId;
	private String pgStoreId;
	private String productName;
	private List<KeyValueBean> mccList = new ArrayList<KeyValueBean>();
	private List<HashMap<Object, Object>> defSupList = null;
	
	/**Fix for the Path Manipulation**/
	PathCleaner cleaner = new PathCleaner();
    PathTraversalDetector detector = new PathTraversalDetector();
    PathValidator validator = new PathValidator(cleaner, detector);
    /**Fix for the Path Manipulation**/
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public MasterListDAO getMasterListDAO() {
		return masterListDAO;
	}
	public void setMasterListDAO(MasterListDAO masterListDAO) {
		this.masterListDAO = masterListDAO;
	}
	public String getAggrMCC() {
		return aggrMCC;
	}
	public void setAggrMCC(String aggrMCC) {
		this.aggrMCC = aggrMCC;
	}	
	public String getVpasMpan() {
		return vpasMpan;
	}
	public void setVpasMpan(String vpasMpan) {
		this.vpasMpan = vpasMpan;
	}
	public String getMcardMpan() {
		return mcardMpan;
	}
	public void setMcardMpan(String mcardMpan) {
		this.mcardMpan = mcardMpan;
	}
	public String getUniqIdent() {
		return uniqIdent;
	}
	public void setUniqIdent(String uniqIdent) {
		this.uniqIdent = uniqIdent;
	}	
	public String getJcpMpan() {
		return jcpMpan;
	}
	public void setJcpMpan(String jcpMpan) {
		this.jcpMpan = jcpMpan;
	}
	public String getNpsbMpan() {
		return npsbMpan;
	}
	public void setNpsbMpan(String npsbMpan) {
		this.npsbMpan = npsbMpan;
	}	
	public LogWebServiceDataService getLogWebServiceDataService() {
		return logWebServiceDataService;
	}	
	public void setLogWebServiceDataService(LogWebServiceDataService logWebServiceDataService) {
		this.logWebServiceDataService = logWebServiceDataService;
	}
	public LogWebServiceDataDao getLogWebServiceDataDao() {
		return logWebServiceDataDao;
	}
	public void setLogWebServiceDataDao(LogWebServiceDataDao logWebServiceDataDao) {
		this.logWebServiceDataDao = logWebServiceDataDao;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getPgStoreId() {
		return pgStoreId;
	}
	public void setPgStoreId(String pgStoreId) {
		this.pgStoreId = pgStoreId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<KeyValueBean> getMccList() {
		return mccList;
	}
	public void setMccList(List<KeyValueBean> mccList) {
		this.mccList = mccList;
	}
	
	private AzureKeyConfigUtils azureKeyConfigUtils;
	 
	public AzureKeyConfigUtils getAzureKeyConfigUtils() {
		return azureKeyConfigUtils;
	}
 
	public void setAzureKeyConfigUtils(AzureKeyConfigUtils azureKeyConfigUtils) {
		this.azureKeyConfigUtils = azureKeyConfigUtils;
	}
	
	
	public String getMultiCheckFlag(String multiCheckKey) {
		return jdbcTemplate.queryForObject("SELECT MGP_PARM_VALU FROM MPS_GENR_PARM_MAST where MGP_PARM_KEY = ?", String.class, multiCheckKey);
	}
	
	public void moveToBulkUploadFolder(Integer instId, File uploadFile, String filePath, String filename) {
		SANFileReader sanFileReader = null;
		try {
			sanFileReader = new SANFileReader(instId);
			sanFileReader.setSMBAuthenticationObject(filePath);
			if (!sanFileReader.isDirectory()) {
				sanFileReader.mkdir();
			}
			if(validator.validatePath(filePath)){
				FileUtils.copyFile(uploadFile, new File(filePath+filename));
				sanFileReader.close();
			}
		}catch (Exception e) {
			loggerUtil.error("Error while moving file to backup - writeToBackup method "+ e.getMessage());
		}finally {
			sanFileReader = null;
		}
	}

	public void writeToBackup(String filepath, String filename, Integer instId, Integer type) {
		IntchgConstants IntchgConstants = null;
		StringBuffer msOutPutFilePath = null;
		SANFileReader sanFileReader = null;
		File srcfile = null;
		try {
			sanFileReader = new SANFileReader(instId);
			sanFileReader.setSMBAuthenticationObject(filepath);
			if (sanFileReader.exists()) {
				IntchgConstants = GenObjectPool.getInstance().getIntchgConstantsObj(instId);
				if(type > 0) {
					msOutPutFilePath = new StringBuffer(IntchgConstants.getPropValue("UPLOAD_BACKUP_PATH"));
				} else {
					msOutPutFilePath = new StringBuffer(filepath+"Uploadfailed/");
				}
				msOutPutFilePath = msOutPutFilePath.append(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "/");
				sanFileReader.setSMBAuthenticationObject(msOutPutFilePath.toString());
				if (!sanFileReader.isDirectory()) {
					sanFileReader.mkdir();
				}
				if(validator.validatePath(filepath)){
					srcfile = new File(filepath+filename);
					FileUtils.copyFile(srcfile, new File(msOutPutFilePath+filename));
					sanFileReader.close();
					if(srcfile.exists()) {
						boolean mkdir=srcfile.delete();
					}
				}
			}
			
		}catch (Exception e) {
			loggerUtil.error("Error while moving file to backup - writeToBackup method "+ e.getMessage());
		}finally {
			IntchgConstants = null;
			msOutPutFilePath = null;
			sanFileReader = null;
			srcfile = null;
		}
	}
	
	public void setMultiCheckkFlag(String multiCheckKey) {
		jdbcTemplate.update("update MPS_GENR_PARM_MAST set MGP_PARM_VALU='Y' where MGP_PARM_KEY=?",multiCheckKey);
	}
	
	public void resetMultiCheckFlag(String multiCheckKey) {
		jdbcTemplate.update("update MPS_GENR_PARM_MAST set MGP_PARM_VALU='N' where MGP_PARM_KEY=?",multiCheckKey);
	}
	
	private Object handleNullOrEmpty(String value) {
	    return (value == null || value.trim().isEmpty()) ? null : value;
	}
	/*public Integer insertTempTable(MerchantOnboardBean merchantBean, List<String> merchantBodyList, int size, String srcFilePathwithname, String filename, String userId,
			String instCode, int recStus, int recordNum, String merchantErrorList, Integer bulkLogRefId, Integer bulkAppNmbr)
	{
		StringBuilder insertQuery = new StringBuilder();
		Integer output = 0;
		try
		{
					
			insertQuery.append(" INSERT INTO MPS_BULK_UPLD_TEMP (MBU_INST_SQID,MBU_APPL_NMBR,MBU_MER_CODE,MBU_REC_UQID,MBU_SALE_EXEC,MBU_LEGL_NAME,MBU_DBA_NAME,MBU_OWNR_NAME,MBU_MAPG_LEVL,MBU_CUST_FNAM,MBU_CUST_LNAM, ");  
			insertQuery.append(" MBU_CUST_ADD1,MBU_CUST_ADD2,MBU_CUST_CNRY,MBU_CUST_STAT,MBU_CUST_CITY,MBU_CUST_PINC,MBU_CUST_PHNO,MBU_CUST_EMAL, ");   
			//insertQuery.append(" MBU_CUST_PAN,MBU_MSF_LEVL,MBU_MSF_TYPE,MBU_MSF_PLAN,MBU_MERC_TYPE,MBU_STOR_CODE,MBU_STTR_NAME,MBU_MERC_GRAD,MBU_MERC_MVV, "); // As MBU_MERC_MVV is not mandatory
			insertQuery.append(" MBU_CUST_PAN,MBU_MSF_LEVL,MBU_MSF_TYPE,MBU_MSF_PLAN,MBU_MERC_TYPE,MBU_STOR_CODE,MBU_STTR_NAME,MBU_MERC_GRAD, ");
			insertQuery.append(" MBU_MERC_MCC,MBU_NOOF_STRQ,MBU_INCR_STUS,MBU_RISK_CATG,MBU_TRAN_SRCE,MBU_WEBS_ADDR,MBU_GST_FLAG,MBU_GST_NUMR, ");    
			insertQuery.append(" MBU_GORG_MERC,MBU_HOME_CRCYID,MBU_NATR_BUSN,MBU_PROF_DOCM,MBU_STOR_EMAL,MBU_TERM_FLAG,MBU_DMAC_INDC,MBU_ACCT_BRCH,MBU_BANK_NAME,MBU_BRCH_NAME, ");   
			insertQuery.append(" MBU_IFSC_CODE,MBU_ACCT_NAME,MBU_ACCT_CRCY,MBU_ACCT_NMBR,MBU_DEFT_ACCT, ");
			//insertQuery.append(" MBU_TERM_ID,MBU_TMPD_TYPE,MBU_NOOF_TMRQ,MBU_TOTL_TCNT,MBU_RCCG_PLAN, "); // As MBU_RCCG_PLAN is not mandatory
			insertQuery.append(" MBU_TERM_ID,MBU_TMPD_TYPE,MBU_NOOF_TMRQ,MBU_TOTL_TCNT, ");
			insertQuery.append(" MBU_RISK_NAME,MBU_INST_SYDT,MBU_INST_USID,MBU_REC_STUS,MBU_FILE_NAME,MBU_FILE_PATH,MBU_ERR_MSG) ");
			insertQuery.append(" VALUES (?, ?, ?, SEQ_SPOB_MERC_REC_UQID.nextval, ?, ?, ?, ?, ?, ?, ?, "); //11
			insertQuery.append(" ?, ?, ?, ?, ?, ?, ?, ?, "); //8
			//insertQuery.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, "); //9
			insertQuery.append(" ?, ?, ?, ?, ?, ?, ?, ?, "); //8
			insertQuery.append(" ?, ?, ?, ?, ?, ?, ?, ?, "); //8
			insertQuery.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "); //10
			insertQuery.append(" ?, ?, ?, ?, ?, "); //5
			//insertQuery.append(" ?, ?, ?, ?, ?, "); //5
			insertQuery.append(" ?, ?, ?, ?, "); //4
			insertQuery.append(" ?, SYSDATE, ?, ?, ?, ?, ?) "); //7 

			output = jdbcTemplate.update(insertQuery.toString(), instCode,
					//handleNullOrEmpty(merchantBodyList.get(1).trim()), 
					bulkAppNmbr, handleNullOrEmpty(merchantBodyList.get(2).trim()), handleNullOrEmpty(merchantBodyList.get(3).trim()), 
					handleNullOrEmpty(merchantBodyList.get(4).trim()), handleNullOrEmpty(merchantBodyList.get(5).trim()), handleNullOrEmpty(merchantBodyList.get(6).trim()), 
					handleNullOrEmpty(merchantBodyList.get(7).trim()), handleNullOrEmpty(merchantBodyList.get(8).trim()), handleNullOrEmpty(merchantBodyList.get(9).trim()), 
					handleNullOrEmpty(merchantBodyList.get(10).trim()), handleNullOrEmpty(merchantBodyList.get(11).trim()), handleNullOrEmpty(merchantBodyList.get(12).trim()), 
					handleNullOrEmpty(merchantBodyList.get(13).trim()), handleNullOrEmpty(merchantBodyList.get(14).trim()), handleNullOrEmpty(merchantBodyList.get(15).trim()), 
					handleNullOrEmpty(merchantBodyList.get(16).trim()),	handleNullOrEmpty(merchantBodyList.get(17).trim()), handleNullOrEmpty(merchantBodyList.get(18).trim()),	
					handleNullOrEmpty(merchantBodyList.get(19).trim()), handleNullOrEmpty(merchantBodyList.get(20).trim()), handleNullOrEmpty(merchantBodyList.get(21).trim()), 
					handleNullOrEmpty(merchantBodyList.get(22).trim()), handleNullOrEmpty(merchantBodyList.get(23).trim()), handleNullOrEmpty(merchantBodyList.get(24).trim()), 
					handleNullOrEmpty(merchantBodyList.get(25).trim()), //handleNullOrEmpty(merchantBodyList.get(26).trim()),	
					handleNullOrEmpty(merchantBodyList.get(26).trim()), 
					handleNullOrEmpty(merchantBodyList.get(27).trim()),	handleNullOrEmpty(merchantBodyList.get(28).trim()), handleNullOrEmpty(merchantBodyList.get(29).trim()),	
					handleNullOrEmpty(merchantBodyList.get(30).trim()), handleNullOrEmpty(merchantBodyList.get(31).trim()),	handleNullOrEmpty(merchantBodyList.get(32).trim()), 
					handleNullOrEmpty(merchantBodyList.get(33).trim()),	handleNullOrEmpty(merchantBodyList.get(34).trim()), handleNullOrEmpty(merchantBodyList.get(35).trim()),	
					handleNullOrEmpty(merchantBodyList.get(36).trim()), handleNullOrEmpty(merchantBodyList.get(37).trim()),	handleNullOrEmpty(merchantBodyList.get(38).trim()), 
					handleNullOrEmpty(merchantBodyList.get(39).trim()), handleNullOrEmpty(merchantBodyList.get(40).trim()), handleNullOrEmpty(merchantBodyList.get(41).trim()),	
					handleNullOrEmpty(merchantBodyList.get(42).trim()), handleNullOrEmpty(merchantBodyList.get(43).trim()),	handleNullOrEmpty(merchantBodyList.get(44).trim()), 
					handleNullOrEmpty(merchantBodyList.get(45).trim()), handleNullOrEmpty(merchantBodyList.get(46).trim()), handleNullOrEmpty(merchantBodyList.get(47).trim()),	
					handleNullOrEmpty(merchantBodyList.get(48).trim()), handleNullOrEmpty(merchantBodyList.get(49).trim()), handleNullOrEmpty(merchantBodyList.get(50).trim()), 
					handleNullOrEmpty(merchantBodyList.get(51).trim()), handleNullOrEmpty(merchantBodyList.get(52).trim()), //handleNullOrEmpty(merchantBodyList.get(54).trim()), 
					handleNullOrEmpty(merchantBodyList.get(53).trim()), userId, recStus, filename, srcFilePathwithname, merchantErrorList);
			
			updateBulkUpldLogDetails(bulkLogRefId, "Uploaded", 1);
			merchantBean.setErrorDesc("SUCCESS");
		}
		catch(Exception e)
		{
			//updateBulkUpldLogDetails(bulkLogRefId, "Failure - "+(e.getMessage().indexOf("ORA") != -1 ? e.getMessage().substring(e.getMessage().indexOf("ORA")) : e.getMessage()), 2);
			String errorMessage = e.getMessage();
			if (errorMessage != null && errorMessage.contains("ORA")) {
			    int oraIndex = errorMessage.indexOf("ORA");
			    errorMessage = errorMessage.substring(oraIndex);
			} else if (e.getCause() != null) {
			    errorMessage = e.getCause().getMessage();
			}
			updateBulkUpldLogDetails(bulkLogRefId, "Failure - " + errorMessage, 2);
			merchantBean.setErrorDesc("ERROR");
			loggerUtil.error("Error while inserting into MPS_BULK_UPLD_TEMP "+ e.getMessage());
		} finally {
			insertQuery = null;
		}
		return output;
	}*/
	
	public Integer insertTempTable(MerchantOnboardBean merchantBean, List<String> merchantBodyList, int size, String srcFilePathwithname, String filename, String userId, String instCode, 
	        int recStus, int recordNum, String merchantErrorList, Integer bulkLogRefId, Integer bulkAppNmbr)
	{
	    StringBuilder insertQuery = new StringBuilder();
	    Integer output = 0;
	    try
	    {
	        insertQuery.append(" INSERT INTO MPS_BULK_UPLD_TEMP (MBU_INST_SQID,MBU_APPL_NMBR,MBU_MER_CODE,MBU_REC_UQID,MBU_SALE_EXEC,MBU_LEGL_NAME,MBU_DBA_NAME,MBU_OWNR_NAME,MBU_MAPG_LEVL,MBU_CUST_FNAM,MBU_CUST_LNAM, ");  
	        insertQuery.append(" MBU_CUST_ADD1,MBU_CUST_ADD2,MBU_CUST_CNRY,MBU_CUST_STAT,MBU_CUST_CITY,MBU_CUST_PINC,MBU_CUST_PHNO,MBU_CUST_EMAL, ");   
	        insertQuery.append(" MBU_CUST_PAN,MBU_MSF_LEVL,MBU_MSF_TYPE,MBU_MSF_PLAN,MBU_MERC_TYPE,MBU_STOR_CODE,MBU_STTR_NAME,MBU_MERC_GRAD, ");
	        insertQuery.append(" MBU_MERC_MCC,MBU_NOOF_STRQ,MBU_INCR_STUS,MBU_RISK_CATG,MBU_TRAN_SRCE,MBU_WEBS_ADDR,MBU_GST_FLAG,MBU_GST_NUMR, ");    
	        insertQuery.append(" MBU_GORG_MERC,MBU_HOME_CRCYID,MBU_NATR_BUSN,MBU_PROF_DOCM,MBU_STOR_EMAL,MBU_TERM_FLAG,MBU_DMAC_INDC,MBU_ACCT_BRCH,MBU_BANK_NAME,MBU_BRCH_NAME, ");   
	        insertQuery.append(" MBU_IFSC_CODE,MBU_ACCT_NAME,MBU_ACCT_CRCY,MBU_ACCT_NMBR,MBU_DEFT_ACCT, ");
	        insertQuery.append(" MBU_TERM_ID,MBU_TMPD_TYPE,MBU_RISK_NAME,MBU_SRCE_BANK, ");
	        insertQuery.append(" MBU_AANI_MID,MBU_AANI_SID,MBU_AANI_TID,MBU_INST_SYDT,MBU_INST_USID,MBU_REC_STUS,MBU_FILE_NAME,MBU_FILE_PATH,MBU_ERR_MSG) ");
	        insertQuery.append(" VALUES (?, ?, ?, SEQ_SPOB_MERC_REC_UQID.nextval, ");

	        for (int i = 0; i < 53; i++) {
	            insertQuery.append("?, ");
	        }

	        insertQuery.append("SYSDATE, ?, ?, ?, ?, ?)");

	        List<Object> params = new ArrayList<>();
	        params.add(instCode);
	        params.add(bulkAppNmbr);

	        for (int i = 2; i <= 55 && i < merchantBodyList.size(); i++) {
	            params.add(handleNullOrEmpty(merchantBodyList.get(i).trim()));
	        }

	        params.add(userId); 
	        params.add(recStus);
	        params.add(filename);
	        params.add(srcFilePathwithname);
	        params.add(merchantErrorList);

	        output = jdbcTemplate.update(insertQuery.toString(), params.toArray());

	        updateBulkUpldLogDetails(bulkLogRefId, "Uploaded", 1);
	        merchantBean.setErrorDesc("SUCCESS");
	        params.clear();
	    } catch (Exception e) {
	    	String errorMessage = e.getMessage();
			if (errorMessage != null && errorMessage.contains("ORA")) {
			    int oraIndex = errorMessage.indexOf("ORA");
			    errorMessage = errorMessage.substring(oraIndex);
			} else if (e.getCause() != null) {
			    errorMessage = e.getCause().getMessage();
			}
			updateBulkUpldLogDetails(bulkLogRefId, "Failure - " + errorMessage, 2);
			merchantBean.setErrorDesc("ERROR");
			loggerUtil.error("Error while inserting into MPS_BULK_UPLD_TEMP "+ e.getMessage());
		} finally {
			insertQuery = null;
		}
	    return output;
	}
	
	/*public void insertToBulkExepDetl(String merchantErrorList, List<String> merchantBodyList, Integer bulkAppNmbr, String instCode, String userId) {
		StringBuilder insertQuery = new StringBuilder("INSERT INTO MPS_BULK_EXEP_DETL (MBE_INST_SQID, MBE_EXEP_SQID, MBE_ACTL_VALU, MBE_INST_DATE, MBE_INST_USID, MBE_APPL_NMBR) "
				+ " VALUES (?,?,?,SYSDATE,?,?)");
		try {
			for (int i = 0; i < merchantBodyList.size(); i++) {
				jdbcTemplate.update(insertQuery.toString(), instCode, "131", handleNullOrEmpty(merchantBodyList.get(i).trim()), userId, bulkAppNmbr);
			}
		} catch (Exception e) {
			loggerUtil.error("Error while insert into the mps_bulk_exep_detl "+ e.getMessage());
		} finally {
			insertQuery = null;
		}
	}*/
	
	@Override
	public String callBulkUpldProcedure(SessionBean sessionBean) {
		String message = null;
		CallableStatement stmt = null;
		try {
			stmt = jdbcTemplate.getDataSource().getConnection().prepareCall("CALL SP_BULK_UPLD(?, ?, ?, ?)");
			stmt.setInt(1, Integer.parseInt(sessionBean.getInstId()));
			stmt.setInt(2, Integer.parseInt(sessionBean.getUserId()));
			stmt.setString(3, getClearDEK());
			stmt.registerOutParameter(4, Types.VARCHAR);
			stmt.execute();
			message = stmt.getString(4);
			loggerUtil.info("Procedure of SP_BULK_UPLD Out Message : " + message);
		} catch (Exception e) {
			loggerUtil.error("Error occurs while Procedure Calling "+ e.getMessage());
		}finally {
			stmt = null;
		}
		return message;
	}
	
	@Override
	public List<MerchantOnboardBean> getMasterUploadData(String uploadType) {
		List<MerchantOnboardBean> list = null;
		StringBuffer queryStr = new StringBuffer();
		try {
			queryStr.append(" SELECT MBM_UPLD_TYPE, MBM_SEQN_NUMR, MBM_FELD_DESC, MBM_VALD_TYPE, MBM_FELD_LNTH, MBM_MNDT_FLAG, "
					+ " MBM_CLMN_NAME, MBM_DFLT_FLAG, MBM_DPND_FLAG, MBM_PSBL_VALU, MBM_COND_MAND FROM MPS_BUPD_MAST_DATA "
					+ " WHERE MBM_UPLD_TYPE = ? AND MBM_REC_STUS ='Y' ORDER BY MBM_SEQN_NUMR ASC ");
			
			list = jdbcTemplate.query(queryStr.toString(), new RowMapper<MerchantOnboardBean>(){
				public MerchantOnboardBean mapRow(ResultSet rs, int indx) throws SQLException {
					MerchantOnboardBean bean = new MerchantOnboardBean();
					bean.setUploadType(rs.getString("MBM_UPLD_TYPE"));
					bean.setSequenceNumber(rs.getInt("MBM_SEQN_NUMR"));
					bean.setFieldDesc(rs.getString("MBM_FELD_DESC"));
					bean.setValidationType(rs.getString("MBM_VALD_TYPE"));
					bean.setFieldLength(rs.getInt("MBM_FELD_LNTH"));
					bean.setMandatoryFlag(rs.getString("MBM_MNDT_FLAG"));
					bean.setColumnName(rs.getString("MBM_CLMN_NAME"));
					bean.setIsDependent(rs.getString("MBM_DPND_FLAG"));
					bean.setDefaultValue(rs.getString("MBM_DFLT_FLAG"));
					bean.setPosibleValues(rs.getString("MBM_PSBL_VALU"));
					bean.setCondValidType(rs.getString("MBM_COND_MAND"));
					return bean;
				}
			},uploadType);
		} catch (Exception e) {
			loggerUtil.error("Error while in getMasterUploadData - method "+e.getMessage());
		}finally {
			queryStr = null;
		}
		return list;
	}
	
	public Integer getBulkLogRefId() {
		return jdbcTemplate.queryForObject("SELECT SEQ_BULK_LOG.NEXTVAL FROM DUAL", Integer.class);
	}
	
	public Integer getBulkAppNmbr() {
		return jdbcTemplate.queryForObject("SELECT SEQ_APPL_NMBR.NEXTVAL FROM DUAL", Integer.class);
	}
	
	public void insertBulkUpldLogDetails(Integer instId, Integer bulkLogRefid, String uploadFileName, String data, Integer bulkAppNmbr) {
		data = data.replaceAll(", ", "~");
		jdbcTemplate.update("INSERT INTO MPS_SPOB_BULK_LOG(MSB_INST_SQID, MSB_REFR_SQID, MSB_FILE_NAME, MSB_RECD_DATA, MSB_RECD_STUS, MSB_INST_SYDT, MSB_APPL_NMBR) VALUES (?, ?, ?, ?, 0, SYSDATE, ?)", 
			instId, bulkLogRefid, uploadFileName, data, bulkAppNmbr);
	}
	
	public void updateBulkUpldLogDetails(Integer bulkLogRefid, String desc, Integer status) {
		jdbcTemplate.update("UPDATE MPS_SPOB_BULK_LOG SET MSB_RECD_STUS = ?, MSB_RECD_DESC = ? WHERE MSB_REFR_SQID = ?", status, desc, bulkLogRefid);
	}
	
	public String getBulkUpldLogDetailsCount(String filename) {
		String output = null;
		try {
			output = jdbcTemplate.queryForObject(" SELECT 'TOTAL RECD = ' || SUM(CASE WHEN MSB_RECD_STUS = 1 THEN 1 WHEN MSB_RECD_STUS = 2 THEN 1 "
					+ "WHEN MSB_RECD_STUS = 3 THEN 1 WHEN MSB_RECD_STUS = 4 THEN 1 ELSE 0 END) || '  -  ' || "
					+ "'UPLOADED = ' || SUM(CASE WHEN MSB_RECD_STUS = 1 THEN 1 ELSE 0 END) || ', ' || "
					+ "'FAILURE = ' || SUM(CASE WHEN MSB_RECD_STUS = 2 THEN 1 ELSE 0 END) || ', ' || "
					+ "'REJECTED = ' || SUM(CASE WHEN MSB_RECD_STUS = 3 THEN 1 ELSE 0 END) || ' and ' || "
					+ "'DUPLICATE = ' || SUM(CASE WHEN MSB_RECD_STUS = 4 THEN 1 ELSE 0 END) AS COUNT "
					+ "FROM MPS_SPOB_BULK_LOG WHERE MSB_FILE_NAME = ?", String.class, filename);
			
		} catch (Exception e) {
			loggerUtil.error("Error while in getMasterUploadData - method "+e.getMessage());
		}
		return output;
	}
	
	public List<MerchantOnboardBean> getmerchantOnboardSearchResultDetails(Integer instId, MerchantOnboardBean merchantOnboardBean, String dateFormat) {
		List<MerchantOnboardBean> list = null; 
		StringBuffer strQry = new StringBuffer();
		try 
		{
			strQry.append(" SELECT MSM_REFR_SQID , MSM_LEGL_NAME, MSM_APPL_NMBR, ")
		          .append(" TO_CHAR(MSM_INST_INDT, ?) AS Referral_Date, CASE WHEN MSM_AGGR_NAME IS NULL THEN 'Direct Merchant' ELSE 'Partner Merchant' END AS Merchant_Type, ")
		          .append(" MSM_REC_STUS, MSM_STOR_FLAG, MSM_SRCE_BANK, ")
		          .append(" CASE WHEN MSM_SRCE_BANK = 'IB' THEN NVL(CONSD.MAM_CSNT_STUS, 'PENDING') ") //for Consent mail status
	              .append(" ELSE '' END AS CONSENT_STATUS, ") //for Consent mail status
		          .append(" NVL (REFR.ACTV_CNT, 0) AS ACTV_MERH FROM MPS_SPOB_MERC_TEMP ")
		          .append(" LEFT JOIN ( SELECT MMM_REFR_SQID, COUNT(*) AS ACTV_CNT ")
		          .append(" FROM MPS_MERC_MERH_DETL ")
		          .append(" WHERE MMM_MCUR_STUS IN (1) ")
		          .append(" GROUP BY MMM_REFR_SQID ) ")
		          .append(" REFR ON REFR.MMM_REFR_SQID = MSM_REFR_SQID ")
		          .append(" LEFT JOIN ( SELECT MAM_APPL_NO, MAX(MAM_CSNT_STUS) AS MAM_CSNT_STUS ") //IB-records visibility
	              .append(" FROM MPS_EMAIL_AUDT_LOG GROUP BY MAM_APPL_NO )")   //IB-records visibility
	              .append(" CONSD ON CONSD.MAM_APPL_NO = MSM_APPL_NMBR ")  //IB-records visibility
			      .append(" WHERE MSM_INST_SQID = ? ")
					
				  //IB-records visibility
				/*.append(" AND ( MSM_SRCE_BANK IS NULL ")
			      .append(" OR ( MSM_SRCE_BANK = 'IB' AND CONSD.MAM_CSNT_STUS = 'APPROVED') )") */
			      .append(" AND NOT EXISTS ( SELECT 1 FROM MPS_MERC_MERH_DETL D ")
				  .append(" WHERE D.MMM_REFR_SQID = MSM_REFR_SQID AND D.MMM_MCUR_STUS IN (0, 90, 91) ) ");
			
			if (merchantOnboardBean.getOnboardMethod() != null && merchantOnboardBean.getOnboardMethod().equalsIgnoreCase("1")) {
				strQry.append(" AND MSM_AGGR_NAME IS NULL ");
				if (merchantOnboardBean.getLegalname()!= null && merchantOnboardBean.getLegalname() != "" && merchantOnboardBean.getLegalname() != " " && merchantOnboardBean.getLegalname().length() != 0 && !merchantOnboardBean.getLegalname().isEmpty()) {
					strQry.append(" AND UPPER(MSM_LEGL_NAME) LIKE UPPER('%"+merchantOnboardBean.getLegalname()+"%') ");
				}
			}else if (merchantOnboardBean.getOnboardMethod() != null && merchantOnboardBean.getOnboardMethod().equalsIgnoreCase("2")) {
				strQry.append(" AND MSM_AGGR_NAME IS NOT NULL ");
				if (merchantOnboardBean.getLegalname()!= null && merchantOnboardBean.getLegalname() != "" && merchantOnboardBean.getLegalname() != " " && merchantOnboardBean.getLegalname().length() != 0 && !merchantOnboardBean.getLegalname().isEmpty()) {
					strQry.append(" AND UPPER(MSM_AGGR_NAME) LIKE UPPER('%"+merchantOnboardBean.getLegalname()+"%') ");
				}
			} else {
				if (merchantOnboardBean.getLegalname()!= null && merchantOnboardBean.getLegalname() != "" && merchantOnboardBean.getLegalname() != " " && merchantOnboardBean.getLegalname().length() != 0 && !merchantOnboardBean.getLegalname().isEmpty()) {
					strQry.append(" AND ( UPPER(MSM_LEGL_NAME) LIKE UPPER('%"+merchantOnboardBean.getLegalname()+"%') "
							+ " OR UPPER(MSM_AGGR_NAME) LIKE UPPER('%"+merchantOnboardBean.getLegalname()+"%') ) ");
				}
			}
			
			if (merchantOnboardBean.getSelectSearchBy() != null && merchantOnboardBean.getSelectSearchBy().equalsIgnoreCase("1")) {
				strQry.append(" AND MSM_REC_STUS IN (0, 1) ");
			}else if (merchantOnboardBean.getSelectSearchBy() != null && merchantOnboardBean.getSelectSearchBy().equalsIgnoreCase("2")) {
				strQry.append(" AND MSM_REC_STUS IN (8) ");
			}else if (merchantOnboardBean.getSelectSearchBy() != null && merchantOnboardBean.getSelectSearchBy().equalsIgnoreCase("3")) {
				strQry.append(" AND MSM_REC_STUS IN (3, 4) ");
			}else if (merchantOnboardBean.getSelectSearchBy() != null && merchantOnboardBean.getSelectSearchBy().equalsIgnoreCase("4")) {
				strQry.append(" AND MSM_REC_STUS IN (5) ");
			}else if (merchantOnboardBean.getSelectSearchBy() != null && merchantOnboardBean.getSelectSearchBy().equalsIgnoreCase("5")) {
				strQry.append(" AND MSM_REC_STUS IN (7) ");
			}
			
			if (merchantOnboardBean.getRefNo()!= null && merchantOnboardBean.getRefNo() != "" && merchantOnboardBean.getRefNo() != " " && merchantOnboardBean.getRefNo().length() != 0 && !merchantOnboardBean.getRefNo().isEmpty()) {
				strQry.append(" AND MSM_REFR_SQID LIKE '%"+merchantOnboardBean.getRefNo()+"%' ");
			}
			
			if (merchantOnboardBean.getApplNum()!= null && merchantOnboardBean.getApplNum() != "" && merchantOnboardBean.getApplNum() != " " && merchantOnboardBean.getApplNum().length() != 0 && !merchantOnboardBean.getApplNum().isEmpty()) {
				strQry.append(" AND MSM_APPL_NMBR LIKE '%"+merchantOnboardBean.getApplNum()+"%' ");
			}
			
			strQry.append(" ORDER BY MSM_REFR_SQID DESC");
			
			list = jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
				public MerchantOnboardBean mapRow(ResultSet arg0, int arg1) throws SQLException {
					MerchantOnboardBean searchBean = new MerchantOnboardBean();
					String refNo = arg0.getString(1);
					int activeMerchant = arg0.getInt("ACTV_MERH");
					String consent = arg0.getString("CONSENT_STATUS");	
					searchBean.setAdditionalNewStoreFlag(arg0.getInt("MSM_STOR_FLAG"));
					searchBean.setRefNo(refNo);
					searchBean.setRefNoHref(refNo);
					searchBean.setRefNoHrefEdit("<INPUT type='submit' id='refNo"+refNo+"' name='refNo"+refNo+"' value='"+refNo+"' onclick=\"fn_view('"+refNo+"', '0');\"  class='hrefButton' > " );
					searchBean.setLegalname(arg0.getString(2));
					searchBean.setRefDate(arg0.getString(4));
					searchBean.setOnboardMethod(arg0.getString(5));
					searchBean.setSrcType(arg0.getString(8));
					searchBean.setCsntStus(consent);
					
					//StringBuilder actionBtns = new StringBuilder();
					
					// new onboard changes
					if ("IB".equalsIgnoreCase(arg0.getString(8)) && !"APPROVED".equalsIgnoreCase(consent)) {
						searchBean.setRefRecStatus("<INPUT type='submit' id='btnDisabled' value='EDIT' class='hrefButton' onclick=\"fn_edit('"+refNo+"', '"+searchBean.getAdditionalNewStoreFlag()+"');\" disabled />");
	                }
					else if(arg0.getString(6).equalsIgnoreCase("5")!=true && arg0.getString(6).equalsIgnoreCase("6")!=true && arg0.getString(6).equalsIgnoreCase("7")!=true && arg0.getString(6).equalsIgnoreCase("9")!=true) {
					    searchBean.setRefRecStatus("<INPUT type='submit' value='EDIT' class='hrefButton' onclick=\"fn_edit('"+refNo+"', '"+searchBean.getAdditionalNewStoreFlag()+"');\" />");
					  //actionBtns.append("<INPUT type='submit' value='EDIT' class='hrefButton' onclick=\"fn_edit('"+refNo+"', '"+searchBean.getAdditionalNewStoreFlag()+"');\" />");
					}
					else if(arg0.getString(6).equalsIgnoreCase("5") && searchBean.getAdditionalNewStoreFlag()==1) {
					  //searchBean.setRefNoHrefEdit("<INPUT type='submit' id='refNo"+refNo+"' name='refNo"+refNo+"' value='"+refNo+"' onclick=\"fn_view('"+refNo+"', '1');\"  class='hrefButton' style='background-color:#28A745; color:white; border:none;' > " );
					    searchBean.setRefRecStatus("<INPUT type='submit' value='EDIT NEW STORE' class='hrefButton' onclick=\"fn_edit('"+refNo+"', '"+searchBean.getAdditionalNewStoreFlag()+"');\" />");
   					  //actionBtns.append("<INPUT type='submit' value='EDIT NEW STORE' class='hrefButton' onclick=\"fn_edit('"+refNo+"', '"+searchBean.getAdditionalNewStoreFlag()+"');\" />");
					}
					else if(arg0.getString(6).equalsIgnoreCase("5") && searchBean.getAdditionalNewStoreFlag()==0) {
						
						if(activeMerchant>0) {
						  //searchBean.setRefNoHrefEdit("<INPUT type='submit' id='refNo"+refNo+"' name='refNo"+refNo+"' value='"+refNo+"' onclick=\"fn_view('"+refNo+"', '1');\"  class='hrefButton' style='background-color:#28A745; color:white; border:none;' > " );
						    searchBean.setRefRecStatus("<INPUT type='submit' value='ADD NEW STORE' class='hrefButton' onclick=\"fn_addNewStore('"+refNo+"');\" />");
						  //actionBtns.append("<INPUT type='submit' value='ADD NEW STORE' class='hrefButton' onclick=\"fn_edit('"+refNo+"', '"+searchBean.getAdditionalNewStoreFlag()+"');\" />");
						}
						else {
						  //searchBean.setRefNoHrefEdit("<INPUT type='submit' id='refNo"+refNo+"' name='refNo"+refNo+"' value='"+refNo+"' onclick=\"fn_view('"+refNo+"', '1');\"  class='hrefButton' > " );
						    searchBean.setRefRecStatus("<INPUT type='submit' id='btnDisabled' value='ADD NEW STORE' class='hrefButton' onclick=\"fn_addNewStore('"+refNo+"');\" disabled />");
						  //actionBtns.append("<INPUT type='submit' id='btnDisabled' value='ADD NEW STORE' class='hrefButton' onclick=\"fn_addNewStore('"+refNo+"');\" disabled />");
						}
					}
					
					
					if(arg0.getString(6).equalsIgnoreCase("5") && activeMerchant == 0) {
					    searchBean.setActivateBtn("<INPUT type='button' value='ACTIVATE' onclick=\"fn_activateDeactivate('Activate','"+refNo+"');\" />");
					    searchBean.setDeactivateBtn("<INPUT type='button' value='DEACTIVATE' onclick=\"fn_activateDeactivate('Deactivate','"+refNo+"');\" style='background: #9b1e2f !important;' />");
					}
				//	searchBean.setRefRecStatus(actionBtns.toString());
					
					switch (arg0.getByte("MSM_REC_STUS")) {
					case 0:
						searchBean.setStatus("PENDING");
						break;
					case 1:
						searchBean.setStatus("PENDING");
						break;
					case 2:
						searchBean.setStatus("APPROVED");
						break;
					case 3:
						searchBean.setStatus("ERROR");
						break;
					case 4:
						searchBean.setStatus("ERROR");
						break;
					case 5: // new onboard changes
						if (activeMerchant>0) {
						searchBean.setStatus("MERCHANT ONBOARDED");
                        } else {
                        searchBean.setStatus("APPROVED BY COMPLIANCE"); 
                        }
						break;
					case 6:
						searchBean.setStatus("DUPLICATE");
						break;
					case 7:
						searchBean.setStatus("REJECTED");
						break;
					case 8:
						searchBean.setStatus("REASSIGNED BY COMPLIANCE");
						break;
					case 9:
						searchBean.setStatus("DEACTIVATED");
						break;
					default:
						searchBean.setStatus("PENDING");
						break;
					}
					
					searchBean.setApplNum(arg0.getString(3));
					return searchBean;
				}
			}, dateFormat, instId);
			
		} catch (Exception e) {
			loggerUtil.error("Error occured in getmerchantOnboardSearchResultDetails() - "+e.getMessage());
		} finally {
			strQry = null;
		}		
		return list;
	}
	
	public List<MerchantOnboardBean> getmerchantOnboardCheckerResultPage(Integer instId, MerchantOnboardBean merchantOnboardBean, String dateFormat, Integer userId) {
		List<MerchantOnboardBean> list = null; 
		StringBuffer strQry = null;
		try 
		{
			strQry = new StringBuffer(" SELECT MSM_REFR_SQID , MSM_LEGL_NAME , TO_CHAR(MSM_INST_INDT, ?) AS MSM_INST_INDT, "
					+ " MSM_CUST_PHNO, MSM_ONBD_MTHD, MSM_REC_STUS, MSM_APPL_NMBR  FROM MPS_SPOB_MERC_TEMP ");
			strQry.append(" WHERE MSM_INST_SQID = ? AND (MSM_REC_STUS = ? OR (MSM_REC_STUS = ? AND MSM_STOR_FLAG = ?)) "
					+ " AND MSM_INST_USER <> ? AND ( MSM_LUPD_USER IS NULL OR MSM_LUPD_USER <> ? ) ORDER BY MSM_REFR_SQID DESC ");
			
			list = jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
				public MerchantOnboardBean mapRow(ResultSet arg0, int arg1) throws SQLException {
					MerchantOnboardBean searchBean = new MerchantOnboardBean();
					String refNo = arg0.getString(1);
					searchBean.setRefNo(refNo);
					searchBean.setRefNoHref(refNo);
					searchBean.setLegalname(arg0.getString(2));
					searchBean.setRefDate(arg0.getString(3));
					searchBean.setCustomernumber(arg0.getString(4));
					searchBean.setOnboardMethod(arg0.getString(5));
					searchBean.setRecStatus(Byte.parseByte(arg0.getString(6)));
					searchBean.setApplNum(arg0.getString(7));
					searchBean.setSelectedCheckBoxRefId(refNo);
					searchBean.setRemarkbulk(refNo);
					searchBean.setApprovebtn(refNo);
				        searchBean.setRejectbtn(refNo);
					searchBean.setReassignbtn(refNo);
					return searchBean;
				}
			}, dateFormat, instId, ACTIVE, ONBOARDED, ACTIVE, userId, userId);
			
		} catch (Exception e) {
			loggerUtil.error("Error occured in getmerchantOnboardCheckerResultPage() - "+e.getMessage());
		} finally {
			strQry = null;
		}		
		return list;
	}
	
	public MerchantOnboardBean approveMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		int output = 0;
		StringBuilder strQry = null;
		try 
		{
			strQry = new StringBuilder(" UPDATE MPS_SPOB_MERC_TEMP SET MSM_ERR_DESC = ?, "
					+ "MSM_LUPD_USER = ?, MSM_LUPD_INDT = SYSDATE WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID IN ( ? ) ");
			output = jdbcTemplate.update(strQry.toString(), 
					merchantOnboardBean.getRemarks()!=null && merchantOnboardBean.getRemarks()!="" ? "APPROVED - " + merchantOnboardBean.getRemarks() : "APPROVED", 
					merchantOnboardBean.getUserId(), instId, merchantOnboardBean.getRefNo());
			if (output > 0) {
				merchantOnboardBean.setFlag("Approved");
				//merchantOnboardBean.setErrorDesc("");
			} else {
				merchantOnboardBean.setFlag("Failed");
				merchantOnboardBean.setErrorDesc("ERROR");
				updateFailedMerchantStatus(instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), "Failed");
			}
		} catch (Exception e) {
			loggerUtil.error("approveMerchantOnBoard - error occured"+ e.getMessage());
			merchantOnboardBean.setErrorDesc("ERROR");
			updateFailedMerchantStatus(instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), "ERROR - "+e.getMessage());
		} finally {
			output = 0;
			strQry = null;
		}
		return merchantOnboardBean;
	}
	
	public MerchantOnboardBean rejectMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		int output = 0;
		StringBuilder strQry = null;
		try 
		{
			int additionalStoreFlag = jdbcTemplate.queryForObject("SELECT MSM_STOR_FLAG FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", Integer.class, merchantOnboardBean.getRefNo());
			String remarks = jdbcTemplate.queryForObject("SELECT MSM_ERR_DESC FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", String.class, merchantOnboardBean.getRefNo());
			if(additionalStoreFlag == 1) {
				strQry = new StringBuilder(" UPDATE MPS_SPOB_MERC_TEMP SET MSM_ERR_DESC = ?, MSM_REC_STUS = ?, MSM_STOR_FLAG = ?, "
						+ "MSM_LUPD_USER = ?, MSM_LUPD_INDT = SYSDATE " + "WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID IN ( ? )");
				output = jdbcTemplate.update(strQry.toString(), 
						remarks,5, 0, merchantOnboardBean.getUserId(), instId, merchantOnboardBean.getRefNo());
				jdbcTemplate.update("UPDATE MPS_SPOB_STOR_TEMP SET MSS_REC_STUS = ?, MSS_ERR_DESC = ? WHERE MSS_REC_STUS = ? AND MSS_REFR_SQID = ? ".toString(), 
						7, 
						merchantOnboardBean.getRemarks()!=null && merchantOnboardBean.getRemarks()!="" ? "REJECTED - " + merchantOnboardBean.getRemarks() : "REJECTED",
						ACTIVE, merchantOnboardBean.getRefNo());
				if (output > 0) {
					merchantOnboardBean.setFlag("Rejected");
				} else {
					merchantOnboardBean.setFlag("Failed");
					merchantOnboardBean.setErrorDesc("ERROR");
					updateFailedMerchantStatus(instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), "Failed");
				}
			} else {
				strQry = new StringBuilder(" UPDATE MPS_SPOB_MERC_TEMP SET MSM_ERR_DESC = ?, MSM_REC_STUS = ?, "
						+ "MSM_LUPD_USER = ?, MSM_LUPD_INDT = SYSDATE " + "WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID IN ( ? )");
				output = jdbcTemplate.update(strQry.toString(), 
						merchantOnboardBean.getRemarks()!=null && merchantOnboardBean.getRemarks()!="" ? "REJECTED - " + merchantOnboardBean.getRemarks() : "REJECTED",
						7, merchantOnboardBean.getUserId(), instId, merchantOnboardBean.getRefNo());
				if (output > 0) {
					merchantOnboardBean.setFlag("Rejected");
				} else {
					merchantOnboardBean.setFlag("Failed");
					merchantOnboardBean.setErrorDesc("ERROR");
					updateFailedMerchantStatus(instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), "Failed");
				}
			}
		} catch (Exception e) {
			loggerUtil.error("rejectMerchantOnBoard - error occured "+ e.getMessage());
			merchantOnboardBean.setErrorDesc("ERROR");
			updateFailedMerchantStatus(instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), "ERROR - "+e.getMessage());
		} finally {
			output = 0;
			strQry = null;
		}
		return merchantOnboardBean;
	}
	
	public MerchantOnboardBean merchantOnBoardMethod(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		CallableStatement stmt = null;
		try 
		{
			stmt = jdbcTemplate.getDataSource().getConnection().prepareCall("CALL SP_MERC_ONBOARD(?, ?, ?)");
			stmt.setInt(1, instId);
			stmt.setInt(2, Integer.parseInt(merchantOnboardBean.getUserId()));
			stmt.registerOutParameter(3, Types.VARCHAR);
			stmt.execute();
			merchantOnboardBean.setFlag(stmt.getString(3));
			if (merchantOnboardBean.getFlag() == null || !"OK".equalsIgnoreCase(merchantOnboardBean.getFlag())) {
				jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_REC_STUS = 3, MSM_ERR_DESC = ? where MSM_REFR_SQID = ?" ,
						"ERROR - "+merchantOnboardBean.getFlag(), merchantOnboardBean.getRefNo());
				throw new Exception(merchantOnboardBean.getFlag());
			}
		} catch (Exception e) {
			loggerUtil.error("merchantOnBoardMethod - error occured "+ e.getMessage());
			merchantOnboardBean.setErrorDesc("ERROR");
		} finally {
			stmt = null;
		}
		return merchantOnboardBean;
	}
	
	public MerchantOnboardBean reassignMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		int output = 0;
		StringBuilder strQry = null;
		try 
		{
			int additionalStoreFlag = jdbcTemplate.queryForObject("SELECT MSM_STOR_FLAG FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", Integer.class, merchantOnboardBean.getRefNo());
			String remarks = jdbcTemplate.queryForObject("SELECT MSM_ERR_DESC FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", String.class, merchantOnboardBean.getRefNo());
			if(additionalStoreFlag == 1) {
				strQry = new StringBuilder(" UPDATE MPS_SPOB_MERC_TEMP SET MSM_ERR_DESC = ?, MSM_REC_STUS = ?, MSM_STOR_FLAG = ?, "
						+ "MSM_LUPD_USER = ?, MSM_LUPD_INDT = SYSDATE " + "WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID IN ( ? )");
				output = jdbcTemplate.update(strQry.toString(), 
						remarks,5, 1, merchantOnboardBean.getUserId(), instId, merchantOnboardBean.getRefNo());
				jdbcTemplate.update("UPDATE MPS_SPOB_STOR_TEMP SET MSS_REC_STUS = ?, MSS_ERR_DESC = ? WHERE MSS_REC_STUS = ? AND MSS_REFR_SQID = ? ".toString(), 
						8, 
						merchantOnboardBean.getRemarks()!=null && merchantOnboardBean.getRemarks()!="" ? "REASSIGNED - " + merchantOnboardBean.getRemarks() : "REASSIGNED",
						ACTIVE, merchantOnboardBean.getRefNo());
				if (output > 0) {
					merchantOnboardBean.setFlag("Reassigned");
				} else {
					merchantOnboardBean.setFlag("Failed");
					updateFailedMerchantStatus(instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), "Failed");
				}
			} else {
				strQry = new StringBuilder(" UPDATE MPS_SPOB_MERC_TEMP SET MSM_ERR_DESC = ?, MSM_REC_STUS = ?, "
						+ "MSM_LUPD_USER = ?, MSM_LUPD_INDT = SYSDATE WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID IN ( ? )");
				output = jdbcTemplate.update(strQry.toString(), 
						merchantOnboardBean.getRemarks()!=null && merchantOnboardBean.getRemarks()!="" ? "REASSIGNED - " + merchantOnboardBean.getRemarks() : "REASSIGNED",
						8, merchantOnboardBean.getUserId(), instId, merchantOnboardBean.getRefNo());
				if (output > 0) {
					merchantOnboardBean.setFlag("Reassigned");
				} else {
					merchantOnboardBean.setFlag("Failed");
					updateFailedMerchantStatus(instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), "Failed");
				}
			}
			
		} catch (Exception e) {
			loggerUtil.error("reassignMerchantOnBoard - error occured "+ e.getMessage());
			updateFailedMerchantStatus(instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), "ERROR - "+e.getMessage());
		} finally {
			output = 0;
			strQry = null;
		}
		return merchantOnboardBean;
	}
	
	@Override
	public String updateMerchantOnBoardMethod(MerchantOnboardBean merchantOnboardBean, Integer instId, String mode) {
		String result=null, sqlQry=null;
		Integer count = 0;
		try {
	        if (mode.equalsIgnoreCase("BULK")) {

	        	sqlQry = "UPDATE MPS_SPOB_MERC_TEMP SET MSM_REC_STUS = 2 where MSM_REC_STUS IN (1, 5) AND MSM_REFR_SQID IN ("
					+ String.join(", ", Collections.nCopies(Arrays.asList(merchantOnboardBean.getRefNoWithRemarks().split(",")).size(), "?")) + ") ";
							
				jdbcTemplate.update(sqlQry, Arrays.asList(merchantOnboardBean.getRefNoWithRemarks().split(",")).stream().toArray());
				result = "Approved";
					
	        }else {
	        	
				count = jdbcTemplate.queryForObject("SELECT MSM_REC_STUS FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID IN (?)" , Integer.class, merchantOnboardBean.getRefNo());
				int additionalStoreFlag = jdbcTemplate.queryForObject("SELECT MSM_STOR_FLAG FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", Integer.class, merchantOnboardBean.getRefNo());
				if(count != null ) {
					if(count == 1 || (additionalStoreFlag == 1 && count == 5)) {
						jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_REC_STUS = 2 where MSM_REFR_SQID = ?" ,merchantOnboardBean.getRefNo());
						result = "Approved";
					}
					else if((additionalStoreFlag != 1 && count == 5) || count==2) {
						result = "Already this Merchant is Approved - Referral No. "+merchantOnboardBean.getRefNo();
					}else if(count==7) {
						result = "Already this Merchant is Rejected - Referral No. "+merchantOnboardBean.getRefNo();
					}else if(count==8){
						result = "Already this Merchant is Reassigned - Referral No. "+merchantOnboardBean.getRefNo();
					}
				}
	        }
		} catch (Exception e) {
	        loggerUtil.error("Error occurred in updateMerchantOnBoardMethod: " + e.getMessage(), e);
	        updateFailedMerchantStatus(instId, mode.equalsIgnoreCase("BULK") ? merchantOnboardBean.getRefNoWithRemarks() : merchantOnboardBean.getRefNo(), merchantOnboardBean.getUserId(), e.getMessage());
	        result = "Update Merchant OnBoard Status - Failed for Referral No. " + (mode.equalsIgnoreCase("BULK") ? merchantOnboardBean.getRefNoWithRemarks() : merchantOnboardBean.getRefNo());
	    }finally {
	    	count = 0;
	    	sqlQry=null;
		}
		
		return result;
	}
	
	// Added Methods for Instant Onboard - Start
	
	@Override
	public MerchantOnboardBean addMerchantDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		StringBuilder strQry = new StringBuilder();
		try {
			
			merchantOnboardBean.setRefSqid(jdbcTemplate.queryForObject("select SEQ_MME_REFR_SQID.nextval as count from dual", Integer.class));

			merchantOnboardBean.setOwnername(Optional.ofNullable(merchantOnboardBean.getOwnername()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setLastname1(Optional.ofNullable(merchantOnboardBean.getLastname1()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setAddressline2(Optional.ofNullable(merchantOnboardBean.getAddressline2()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setBusinessContactEmailID(Optional.ofNullable(merchantOnboardBean.getBusinessContactEmailID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setTechnicalContactEmailID(Optional.ofNullable(merchantOnboardBean.getTechnicalContactEmailID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setMercAggrName(Optional.ofNullable(merchantOnboardBean.getMercAggrName()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setMercAggr(Optional.ofNullable(merchantOnboardBean.getMercAggr()).filter(s -> !s.trim().isEmpty()).orElse(null));
			
			strQry.append(" INSERT INTO MPS_SPOB_MERC_TEMP( MSM_INST_SQID, MSM_REC_UQID, MSM_MERC_CODE, MSM_SALE_EXEC, MSM_LEGL_NAME, ")
			.append(" MSM_DBA_NAME, MSM_OWNR_NAME, MSM_AGGR_NAME, MSM_CUST_FNAM, MSM_CUST_LNAM, MSM_CUST_ADD1, MSM_CUST_ADD2, MSM_CUST_CNTY, MSM_CUST_STAT, MSM_CUST_CITY, MSM_CUST_PCOD, MSM_CUST_PHNO, MSM_CUST_EMAL, ")
			.append(" MSM_PRCE_METY, MSM_PRCE_CRCY, MSM_MSVF_LEVL, MSM_MSFP_NAME, MSM_ITCH_MCC, MSM_PAN_UQID, MSM_REFR_SQID, MSM_INST_USER, MSM_INST_INDT, MSM_REC_STUS, MSM_ACCT_MAPG_LEVL, MSM_AGGR_SQID, MSM_DEFT_STNM, ")
			.append(" MSM_EXST_CUST, MSM_MPOR_REQR, MSM_BUSN_EMAL, MSM_BUSN_MOBL, MSM_TECH_EMAL, MSM_TECH_MOBL, MSM_ERR_DESC, MSM_MERC_LOGO, MSM_SESN_FLAG, MSM_SESN_DATE, MSM_SESN_USER, MSM_ACCT_FLAG, ")
			.append(" MSM_OWNR_TYPE, MSM_PRNT_ENTY, MSM_CNRY_INCP, MSM_DATE_INCP, MSM_UBO_PERC, MSM_INDV_NAME, MSM_BUSN_TYPE, MSM_EMRT_ID, MSM_EMRTID_EXPR, MSM_PASP_NMBR, MSM_PASP_EXPR, MSM_MERC_NATY, ")
			.append(" MSM_ANIP_MERCODE, MSM_SCDP_FLAG, MSM_MCC_CODE) VALUES ( ")
			.append(" ?,SEQ_SPOB_MERC_REC_UQID.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,CX_DF_NE(?,?,?),?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,0,SYSDATE,?,?, ")
			.append(" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ");

	jdbcTemplate.update(strQry.toString(), instId, merchantOnboardBean.getMid(),
			merchantOnboardBean.getBankersalesNames(), merchantOnboardBean.getLegalname(), merchantOnboardBean.getDbaname(),
			merchantOnboardBean.getOwnername(), merchantOnboardBean.getMercAggrName(), merchantOnboardBean.getFirstname1(),
			merchantOnboardBean.getLastname1(), merchantOnboardBean.getAddressline1(), merchantOnboardBean.getAddressline2(),
			merchantOnboardBean.getCountry(), merchantOnboardBean.getState(), merchantOnboardBean.getSelectcity(),
			merchantOnboardBean.getPostalcode(), merchantOnboardBean.getPhone(), merchantOnboardBean.getContactEmail(),
			merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
			merchantOnboardBean.getCurrency()!=null && merchantOnboardBean.getCurrency() != "" ? merchantOnboardBean.getCurrency().split("-")[1] : null,
			merchantOnboardBean.getCaptureServiceFeeAt(),
			merchantOnboardBean.getMerchantServiceFeePlan()!=null && merchantOnboardBean.getMerchantServiceFeePlan() != "" ? merchantOnboardBean.getMerchantServiceFeePlan().split("-")[1] : null,
			//merchantOnboardBean.getCmccAutoComplete(),
			merchantOnboardBean.getCmccMerchantAutoComplete(),
			merchantOnboardBean.getCpancard(), instId, getClearDEK()!=null||!getClearDEK().isEmpty()?getClearDEK():null,
			merchantOnboardBean.getRefSqid(), userId, ACTIVE,
			merchantOnboardBean.getAcctMapLvl(), merchantOnboardBean.getMercAggr(),merchantOnboardBean.getDbaname(),
			merchantOnboardBean.getExistingCustomer(),merchantOnboardBean.getMerchantPortalRequired(),
			merchantOnboardBean.getBusinessContactEmailID(), merchantOnboardBean.getBusinessContactMobileNo(),
			merchantOnboardBean.getTechnicalContactEmailID(), merchantOnboardBean.getTechnicalContactMobileNo(),
			merchantOnboardBean.getRemarks(), merchantOnboardBean.getMercLogoUpldData(), userId, merchantOnboardBean.getMerchantAccountRequired(),
			// Ownership Details values 
			merchantOnboardBean.getOwnerType(), merchantOnboardBean.getParentEntityName(),
			merchantOnboardBean.getCountryOfIncorp(), merchantOnboardBean.getDateOfIncorp()==null || merchantOnboardBean.getDateOfIncorp().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(merchantOnboardBean.getDateOfIncorp()),
			merchantOnboardBean.getUboShareholderPctEntity(), merchantOnboardBean.getIndividualName(),
			merchantOnboardBean.getBusinessType(),
			merchantOnboardBean.getEmiratesIdNo(), merchantOnboardBean.getEmiratesIdExpiry()==null || merchantOnboardBean.getEmiratesIdExpiry().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(merchantOnboardBean.getEmiratesIdExpiry()),
			merchantOnboardBean.getPassportNumber(), merchantOnboardBean.getPassportExpiry()==null || merchantOnboardBean.getPassportExpiry().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(merchantOnboardBean.getPassportExpiry()),
			merchantOnboardBean.getNationality(), merchantOnboardBean.getAaniPayMercID(), merchantOnboardBean.getSecurityDepositRecoveryRequired(), merchantOnboardBean.getCmccMerchantAutoComplete());
		} catch (Exception e) {
			loggerUtil.error("Error while adding merchant details "+ e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		} finally {
			strQry = null;
		}
		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean addPriceDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId, String entityLevel) {
		StringBuilder strQry = new StringBuilder();
		StringBuilder strQry1 = new StringBuilder();
		try {
			strQry.append("INSERT INTO MPS_SPOB_PRIC_TEMP(")
					.append(" MSP_REFR_SQID,MSP_METY_SQID,MSP_FPLN_TYPE,MSP_FPLN_NAME,MSP_CAPT_MSFA, ")
					.append(" MSP_FPLN_SQID,MSP_STOR_REQD,MSP_INTC_PLUS,MSP_REC_STUS,MSP_FEE_WAVR, ")
					.append(" MSP_ENTY_SQID ")
					.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?) ");
			jdbcTemplate.update(strQry.toString(), 
					merchantOnboardBean.getRefSqid(),
					merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
					merchantOnboardBean.getFeePlanType(),
					merchantOnboardBean.getMerchantServiceFeePlan(), 
					merchantOnboardBean.getCaptureServiceFeeAt(),
					merchantOnboardBean.getMerchantServiceFeePlan()!=null && merchantOnboardBean.getMerchantServiceFeePlan() != "" ? merchantOnboardBean.getMerchantServiceFeePlan().split("-")[0] : null,
					merchantOnboardBean.getNoOfStoresReq(),
					0,//-->merchantOnboardBean.getIntchgPlus(),
					ACTIVE,
					//merchantOnboardBean.getMercFeeDeduWaiver(),
					Optional.ofNullable(merchantOnboardBean.getMercFeeDeduWaiver()).filter(v -> v != 0).orElse(0),
					entityLevel);
			if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 1) {
				strQry1.append("INSERT INTO MPS_SPOB_SCDP_TEMP( MMS_SCDP_SQID, MMS_INST_SQID, MMS_REFR_SQID, MMS_DEPS_LIMT, "
						+ " MMS_CALC_TYPE, MMS_INDP_AMNT, MMS_INDP_PERC, MMS_ACTIVE_FLAG, MMS_INSD_USID, MMS_INSD_SYDT " 
						+ " ) VALUES (SEQ_MMS_SCDP_SQID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE) ");
				
				jdbcTemplate.update(strQry1.toString(), instId, merchantOnboardBean.getRefSqid(), merchantOnboardBean.getSecurityDepositLimit(), 
					merchantOnboardBean.getSdrIndc() == 0 ? "F" : "P", merchantOnboardBean.getSecurityDepositRecoveryAmount(), 
				    merchantOnboardBean.getSecurityDepositRecoveryPercentage(), ACTIVE, userId );
			} else if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 0) {
				strQry1.append("INSERT INTO MPS_SPOB_SCDP_TEMP( MMS_SCDP_SQID, MMS_INST_SQID, MMS_REFR_SQID, MMS_DEPS_LIMT, "
						+ "  MMS_ACTIVE_FLAG, MMS_INSD_USID, MMS_INSD_SYDT " 
						+ " ) VALUES (SEQ_MMS_SCDP_SQID.NEXTVAL, ?, ?, ?, ?, ?, SYSDATE) ");
				
				jdbcTemplate.update(strQry1.toString(), instId, merchantOnboardBean.getRefSqid(), merchantOnboardBean.getSecurityDepositLimit(), 
					ACTIVE, userId );
			}
			
		} catch (Exception e) {
			loggerUtil.error("Error while adding Price details "+ e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		} finally {
			strQry = null; strQry1 = null;
		}
		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean addStoreWithAccountDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId, Integer userId) {
		StringBuilder strQry = new StringBuilder();
		StringBuilder strQry1 = new StringBuilder();
		Integer output = 0;
		int seqUDFsqid = 0;
		try {
            storeBean.setStoreSqid(jdbcTemplate.queryForObject("select SEQ_MMS_STOR_SQID.nextval as count from dual", Integer.class));
            	
        	storeBean.setMvv(Optional.ofNullable(storeBean.getMvv()).filter(s -> !s.trim().isEmpty()).orElse(null));
        	storeBean.setCyberMercID(Optional.ofNullable(storeBean.getCyberMercID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			storeBean.setCyberMercPass(Optional.ofNullable(storeBean.getCyberMercPass()).filter(s -> !s.trim().isEmpty()).orElse(null));
			storeBean.setCyberMercKey(Optional.ofNullable(storeBean.getCyberMercKey()).filter(s -> !s.trim().isEmpty()).orElse(null));
			storeBean.setAaniPayStoreID(Optional.ofNullable(storeBean.getAaniPayStoreID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			storeBean.setPaymentFrecForCard(Optional.ofNullable(storeBean.getPaymentFrecForCard()).filter(v -> v != 0).orElse(1));  
			storeBean.setPaymentFrecForAaniPay(Optional.ofNullable(storeBean.getPaymentFrecForAaniPay()).filter(v -> v != 0).orElse(1));
			
	            strQry.append("INSERT INTO MPS_SPOB_STOR_TEMP( MSS_INST_SQID, MSS_REFR_SQID, MSS_STOR_SQID, ")
			      	  .append("MSS_STOR_CODE, MSS_STTD_NAME, MSS_MERC_GRAD, ")
			      	  .append("MSS_METY_SQID, MSS_MCC_CODE, MSS_MVV_FIELD, ")
			      	  .append("MSS_GST_NUMR, MSS_GST_FLAG, ")
			      	  .append("MSS_ENTY_SQID, MMS_WBST_NAME, MMS_WEBS_ADDR, ")
			      	  .append("MSS_EMAIL_ADID, MSS_INCR_STUS, MSS_PROF_DOCM, ")
			      	  .append("MSS_TERM_FLAG, MSS_REC_STUS, MSS_CRPT_SQID, ")
			      	  .append("MSS_GEO_CORD, MSS_GEO_LATD, MSS_GEO_CPER, ")
			      	  .append("MSS_LINE_RCPT1, MSS_LINE_RCPT2, MSS_LINE_RCPT3, MSS_NATR_BUSN, MSS_ADDR_FLAG, MSS_ACCT_FLAG, ")
			      	  .append("MSS_CUST_ADD1, MSS_CUST_ADD2, MSS_CUST_CNTY, MSS_CUST_STAT, MSS_CUST_CITY, MSS_CUST_PCOD, " )
			      	  .append("MSS_MERC_RELM, MSS_GOVT_REGMER, MSS_HOME_CRCYID, MSS_MERC_MVV, MSS_INSD_SYDT, ")
			      	  .append("MSS_BUSN_EMAL, MSS_BUSN_MOBL , MSS_TECH_EMAL, MSS_TECH_MOBL, ")
			      	  .append("MSS_RISK_REQD, MSS_RSCG_SQID, MSS_PCI_EXPY, MSS_CLUD_EXPY, MSS_TRAD_EXPY, ")
			      	  .append("MSS_MTHY_TCNT, MSS_AVTS_AMNT, MSS_QSAL_AMNT, MSS_TXNS_REFD_PERC, MSS_TXNS_CHBK_PERC, MSS_TXNS_PERC, MSS_TXNS_INT_PERC, ")
			      	  .append("MSS_CYBS_MID, MSS_CYBS_PWD, MSS_CYBS_MKEY, MSS_PYMT_FREQ_CARD, ")
			      	  .append("MSS_FREQ_DAYS_CARD, MSS_TXNS_PYMT, MSS_PYMT_FREQ_AANI, MSS_FREQ_DAYS_AANI, MSS_PRIC_REQD, MSS_STOR_TYPE, MSS_ANIP_STORCODE ")
	            	  .append(" ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE, ")
	            	  .append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
	        
	            output = jdbcTemplate.update(strQry.toString(),instId,
							merchantOnboardBean.getRefSqid(),
							storeBean.getStoreSqid(),
							storeBean.getStoreCode(),
							storeBean.getStoreTradingName(),
							storeBean.getMerchantGrade()==null||storeBean.getMerchantGrade().equals("")?null:Integer.parseInt(storeBean.getMerchantGrade()),
							merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
							//storeBean.getCmccAutoComplete(),
							merchantOnboardBean.getCmccMerchantAutoComplete(),
							storeBean.getMvv(),
							storeBean.getGstNumber()==null||storeBean.getGstNumber().equals("")?null:storeBean.getGstNumber(),
							storeBean.getGstFlag()==null||storeBean.getGstFlag().equals("")?0:Integer.parseInt(storeBean.getGstFlag()),
							//storeBean.getMerchantEntityName(),
							String.join(",", merchantOnboardBean.getMerchantEntityName()),
							storeBean.getWebSiteName(),
							storeBean.getWebsiteAddress(),
							storeBean.getStoreEmail(),
							storeBean.getIncorpstatus()==null?null:Integer.parseInt(storeBean.getIncorpstatus()),
							storeBean.getPrfDocsProv(),
							storeBean.getTermneed()==null?0:storeBean.getTermneed().equalsIgnoreCase("false")?0:storeBean.getTermneed().equalsIgnoreCase("true")?1:Integer.parseInt(storeBean.getTermneed()),
							ACTIVE,
							storeBean.getCryptoMethod()==null?null:Integer.parseInt(storeBean.getCryptoMethod()),
							storeBean.getGeoCoordinates()==null||storeBean.getGeoCoordinates().equals("")?null:Integer.parseInt(storeBean.getGeoCoordinates()),
							storeBean.getLatitude()==null?null:storeBean.getLatitude(),
							storeBean.getLongitude()==null?null:storeBean.getLongitude(),
							storeBean.getLine1Rec()==null?null:storeBean.getLine1Rec(),
							storeBean.getLine2Rec()==null?null:storeBean.getLine2Rec(),
							storeBean.getLine3Rec()==null?null:storeBean.getLine3Rec(),
							storeBean.getNatureOfBusiness()==null?null:storeBean.getNatureOfBusiness().split("~")[1],
							storeBean.getUseSameAsMercAddressForStore()==null||storeBean.getUseSameAsMercAddressForStore().equals("")?"0":storeBean.getUseSameAsMercAddressForStore(),
							storeBean.getUseSameAsMercAccountForStore()==null||storeBean.getUseSameAsMercAccountForStore().equals("")?"0":storeBean.getUseSameAsMercAccountForStore(),
							storeBean.getStoreaddressline1()==null?null:storeBean.getStoreaddressline1(),
							storeBean.getStoreaddressline2()==null?null:storeBean.getStoreaddressline2(),
							storeBean.getStorecountry()==null?null:storeBean.getStorecountry(),
							storeBean.getStorestate()==null?null:storeBean.getStorestate(),
							storeBean.getStoreselectcity()==null?null:storeBean.getStoreselectcity(),
							storeBean.getStorepostalcode()==null?null:storeBean.getStorepostalcode(),
							storeBean.getRelatonShipManager()==null||storeBean.getRelatonShipManager().equals("")?null:Integer.parseInt(storeBean.getRelatonShipManager()),
							storeBean.getGovtRegMerchant()==null||storeBean.getGovtRegMerchant().equals("")?"0":Integer.parseInt(storeBean.getGovtRegMerchant()),
							storeBean.getHomeCountryId(), //MCO
							storeBean.getMvv(), storeBean.getStorebusinessContactEmailID(), storeBean.getStorebusinessContactMobileNo(), 
							storeBean.getStoretechnicalContactEmailID(), storeBean.getStoretechnicalContactMobileNo(),
							storeBean.getStoreRiskDetailsRequired()==null||storeBean.getStoreRiskDetailsRequired().equals("")?"0":storeBean.getStoreRiskDetailsRequired(),
							storeBean.getRiskCategory()==null?null:Integer.parseInt(storeBean.getRiskCategory()),
							storeBean.getPadssPCIdssCertExpDate()==null || storeBean.getPadssPCIdssCertExpDate().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(storeBean.getPadssPCIdssCertExpDate()),
							storeBean.getCloudCertExpDate() == null || storeBean.getCloudCertExpDate().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(storeBean.getCloudCertExpDate()),
							storeBean.getTradeLicenseExpDate() == null || storeBean.getTradeLicenseExpDate().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(storeBean.getTradeLicenseExpDate()),
							storeBean.getTransCountperMonth(),storeBean.getAverageTicketSize(),
							storeBean.getQuarterlySale(),storeBean.getTransToRefundPercent(),
							storeBean.getTransToChargebackPercent(),storeBean.getTransPercent(),
							storeBean.getInternationalTransPercent(),
							storeBean.getCyberMercID(),
							storeBean.getCyberMercPass(),
							storeBean.getCyberMercKey(),
							//storeBean.getAaniPayMercID(),
							storeBean.getPaymentFrecForCard(),
							storeBean.getPaymentFrecForCardDays(),
							storeBean.getAaniPayTransPayment(),
							storeBean.getPaymentFrecForAaniPay(),
							storeBean.getPaymentFrecForAaniPayDays(),
							storeBean.getStorePriceDetailsRequired(),
							storeBean.getStoreType(),
							storeBean.getAaniPayStoreID());
	            
	            // Same as Merchant Account
	            if (output != 0 && storeBean.getUseSameAsMercAccountForStore().equals("1")) {
	                
	                strQry1.append("INSERT INTO MPS_SPOB_ACCT_TEMP( MSA_REFR_SQID, MSA_DMAC_INDC, MSA_ACCT_CRCY, MSA_ACCT_NUMB, MSA_IFSC_CODE, ")
	                   	   .append("MSA_ACCT_BRAN, MSA_MAPG_LEVL, MSA_ACCT_NAME, MSA_REC_STUS, MSA_MERC_CODE, MSA_STOR_CODE, MSA_DEFT_ACCT, MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ) ")
	                   	   .append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
	                
	                jdbcTemplate.update(strQry1.toString(),	
						merchantOnboardBean.getRefSqid(), 
						storeBean.getStoreacctWithBank(), 
						storeBean.getStorecurrency()!=null && storeBean.getStorecurrency() != "" ? storeBean.getStorecurrency().split("-")[0] : null,
						storeBean.getStoreacctNumber(), storeBean.getStoreifsc(), storeBean.getcStoreacctBranch(),
						storeLevel, storeBean.getStoreaccountname(), ACTIVE, merchantOnboardBean.getMid(),
						storeBean.getStoreSqid(), storeBean.getStoredefAccount(), storeBean.getStoreacctBankText(),
						storeBean.getStoreacctBranchText(), storeBean.getStorecifNumber(), storeBean.getStorestmtfreq());
	            }
	            
	            //Store UDF Details
	            if(output != 0 && ((storeBean.getAdditionalUDFField1() != null && storeBean.getAdditionalUDFField1().trim().length() != 0) ||
	               (storeBean.getAdditionalUDFValue1() != null && storeBean.getAdditionalUDFValue1().trim().length() != 0))) {
	            	if(seqUDFsqid == 0) {
		            	seqUDFsqid = jdbcTemplate.queryForObject("SELECT SEQ_MAU_UDF_SQID.NEXTVAL FROM DUAL", Integer.class);
		            }
	            	jdbcTemplate.update("INSERT INTO MPS_ADMN_UDF_TEMP(MAU_INST_SQID,MAU_UDF_SQID,MAU_CAPT_LEVL,MAU_UDF_CAPTN,MAU_DATA_TYPE,MAU_UDF_DATA, "
	            			+ " MAU_REFR_SQID,MAU_UDF_STUS,MAU_INSD_USID,MAU_INSD_SYDT,MAU_FILD_ID) "
	            			+ " VALUES (?,?,?,?,?,?,?,?,?,(select now()),?)",
	            			instId,seqUDFsqid,storeLevel,storeBean.getAdditionalUDFField1(),3,storeBean.getAdditionalUDFValue1(),
	            			storeBean.getStoreSqid(),ACTIVE,userId,1);
	            }
	            if(output != 0 && ((storeBean.getAdditionalUDFField2() != null && storeBean.getAdditionalUDFField2().trim().length() != 0) ||
	 	           (storeBean.getAdditionalUDFValue2() != null && storeBean.getAdditionalUDFValue2().trim().length() != 0))) {
	            	if(seqUDFsqid == 0) {
		            	seqUDFsqid = jdbcTemplate.queryForObject("SELECT SEQ_MAU_UDF_SQID.NEXTVAL FROM DUAL", Integer.class);
		            }
	            	jdbcTemplate.update("INSERT INTO MPS_ADMN_UDF_TEMP(MAU_INST_SQID,MAU_UDF_SQID,MAU_CAPT_LEVL,MAU_UDF_CAPTN,MAU_DATA_TYPE,MAU_UDF_DATA, "
	            			+ " MAU_REFR_SQID,MAU_UDF_STUS,MAU_INSD_USID,MAU_INSD_SYDT,MAU_FILD_ID) "
	            			+ " VALUES (?,?,?,?,?,?,?,?,?,(select now()),?)",
	            			instId,seqUDFsqid,storeLevel,storeBean.getAdditionalUDFField2(),3,storeBean.getAdditionalUDFValue2(),
	            			storeBean.getStoreSqid(),ACTIVE,userId,2);
	 	        }
	            if(output != 0 && ((storeBean.getAdditionalUDFField3() != null && storeBean.getAdditionalUDFField3().trim().length() != 0) ||
	 	           (storeBean.getAdditionalUDFValue3() != null && storeBean.getAdditionalUDFValue3().trim().length() != 0))) {
	            	if(seqUDFsqid == 0) {
		            	seqUDFsqid = jdbcTemplate.queryForObject("SELECT SEQ_MAU_UDF_SQID.NEXTVAL FROM DUAL", Integer.class);
		            }
	            	jdbcTemplate.update("INSERT INTO MPS_ADMN_UDF_TEMP(MAU_INST_SQID,MAU_UDF_SQID,MAU_CAPT_LEVL,MAU_UDF_CAPTN,MAU_DATA_TYPE,MAU_UDF_DATA, "
	            			+ " MAU_REFR_SQID,MAU_UDF_STUS,MAU_INSD_USID,MAU_INSD_SYDT,MAU_FILD_ID) "
	            			+ " VALUES (?,?,?,?,?,?,?,?,?,(select now()),?)",
	            			instId,seqUDFsqid,storeLevel,storeBean.getAdditionalUDFField3(),3,storeBean.getAdditionalUDFValue3(),
	            			storeBean.getStoreSqid(),ACTIVE,userId,3);
	 	        }
	    } catch (Exception e) {
			loggerUtil.error("Error while adding in addStoreWithAccountDetails() "+ e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}finally {
			strQry = null;
			strQry1 = null;
			output = 0;
			seqUDFsqid = 0;
		}
		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean addTerminalWithAccountDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, MerchantOnboardTerminalBean terminalBean, Integer instId, Integer userId) {
		StringBuilder strQry1 = new StringBuilder();	
		StringBuilder strQry = new StringBuilder();
		Integer output = 0;
		String paymentMethod = null;
		try {
			terminalBean.setAaniPayTermID(Optional.ofNullable(terminalBean.getAaniPayTermID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			terminalBean.setTermSqid(jdbcTemplate.queryForObject("select SEQ_MME_TERM_SQID.nextval as count from dual", Integer.class));
			
			strQry.append("INSERT INTO MPS_SPOB_TERM_TEMP( MST_INST_SQID, MST_REFR_SQID, MST_STOR_SQID, MST_TERM_SQID, ")
			  	  .append("MST_TERM_TID, MST_TMPD_SQID, MST_REC_STUS, MST_RISK_FLAG, MST_RISK_NAME, MST_BRAND_SQID, ")
			  	  .append("MST_PLAN_NAME, MST_TPRF_SQID, MST_CARD_GRUP, MST_ENQR_CODE, MST_APP_ID, MST_AUTH_TOK, MST_POS_NUM, ")
			  	  .append("MST_ACCT_FLAG, MST_MRCP_SQID, MST_INSD_SYDT, ")
			  	  .append("MST_RRF_REQD, MST_RRF_INDC, MST_DOM_REC_PERC, MST_INT_REC_PERC, ")
			  	  .append("MST_DOM_FIXD_AMT, MST_INT_FIXD_AMT, MST_DOM_HDAY, MST_INT_HDAY, ")
			  	  .append("MST_SD_RECV_REQD, MST_SD_INDC, MST_SD_PERC_AMT, MST_SD_NOOF_DAYS, MST_PRIC_REQD, MST_MCC_CODE, ")
			  	  //.append("MST_RECR_FEE_WAVR, MST_PYMT_METD, MST_RSRC_KEY, MST_RSRC_ID, MST_ANIP_MERCODE, MST_ANIP_STORECODE, MST_ANIP_TERMCODE ")
			  	  //.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			  	  .append("MST_RECR_FEE_WAVR, MST_PYMT_METD, MST_RSRC_KEY, MST_RSRC_ID, MST_ANIP_TERMCODE, MST_CLIENT_ID, MST_CLIENT_SECRET, MST_TERM_NAME ")
			  	  .append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		
			if(terminalBean.getPaymentMethod() != null && !terminalBean.getPaymentMethod().equals("")) {
		        for (byte i = 0; i < terminalBean.getPaymentMethod().length; i++) {
		        	paymentMethod = (i==0) ? terminalBean.getPaymentMethod()[i] : paymentMethod + ","+terminalBean.getPaymentMethod()[i];
		        }
			}
				
			output = jdbcTemplate.update(strQry.toString(),instId,
						merchantOnboardBean.getRefSqid(),
						storeBean.getStoreSqid(),
						terminalBean.getTermSqid(),
						terminalBean.getTermCode(),
						terminalBean.getTermProuctType()==null?null:Integer.parseInt(terminalBean.getTermProuctType()),
						ACTIVE,
						terminalBean.getTerminalRiskProfileIndc()==null||terminalBean.getTerminalRiskProfileIndc().equals("")?0:Integer.parseInt(terminalBean.getTerminalRiskProfileIndc()),
						terminalBean.getTerminalRiskProfile(),
						null,//-->brand,
						terminalBean.getProcessingPlanName(),
						null,//-->termProfileId,
						terminalBean.getCardGroup()==null||terminalBean.getCardGroup().equals("")?0:terminalBean.getCardGroup(),
						terminalBean.getEnableQRcode(),
						terminalBean.getAppId(),
						terminalBean.getAuthToken(),
						terminalBean.getPos(),
						terminalBean.getUseSameAsStoreAccountForTerm()==null||terminalBean.getUseSameAsStoreAccountForTerm().equals("")?0:terminalBean.getUseSameAsStoreAccountForTerm(),
						terminalBean.getDftTrmlRcrngChrgePlnId()==null||terminalBean.getDftTrmlRcrngChrgePlnId().equals("")?null:Integer.parseInt(terminalBean.getDftTrmlRcrngChrgePlnId()),
						terminalBean.getRollingReserveFundRequired(),
						terminalBean.getRrfIndc(),
						terminalBean.getDomestic(),
						terminalBean.getInterNational(),
						terminalBean.getDomesticFixedAmount(),
						terminalBean.getInternationalFixedAmount(),
						terminalBean.getDomesticHoldingDays(),
						terminalBean.getInternationalHoldingDays(),
						terminalBean.getSecurityDepositRecoveryRequired(),
						terminalBean.getSdrIndc(),
						terminalBean.getSecurityDepositRecoveryAmount(),
						terminalBean.getSecurityDepositRecoveryDays(),
						terminalBean.getTerminalPriceDetailsRequired(),
						//terminalBean.getCterminalmccautoComplete(),
						merchantOnboardBean.getCmccMerchantAutoComplete(),
						terminalBean.getTerminalRecurringPlanWaiver(),
						paymentMethod,
						terminalBean.getTerminalSecretKey(),
						terminalBean.getTerminalGenerateRandomKey(),
						//terminalBean.getAaniPayMercID(),
						//terminalBean.getAaniPayStoreID(),
						terminalBean.getAaniPayTermID(),
						terminalBean.getTerminalClientId(),
						terminalBean.getTerminalClientSecretKey(),
						terminalBean.getTerminalName()
						);
				
				if (output != 0 && terminalBean.getUseSameAsStoreAccountForTerm().equals("1")) {
	                
					strQry1.append("INSERT INTO MPS_SPOB_ACCT_TEMP( MSA_REFR_SQID, MSA_DMAC_INDC, MSA_ACCT_CRCY, MSA_ACCT_NUMB, MSA_IFSC_CODE, ")
	            	   	   .append("MSA_ACCT_BRAN, MSA_MAPG_LEVL, MSA_ACCT_NAME, MSA_REC_STUS, MSA_MERC_CODE, MSA_STOR_CODE, MSA_TERM_CODE, MSA_DEFT_ACCT, MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ) ")
	            	       .append("VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					
	                jdbcTemplate.update(strQry1.toString(),	
						merchantOnboardBean.getRefSqid(), 
						terminalBean.getTerminalacctWithBank(), 
						terminalBean.getTerminalcurrency()!=null && terminalBean.getTerminalcurrency() != "" ? terminalBean.getTerminalcurrency().split("-")[0] : null,
						terminalBean.getTerminalacctNumber(), terminalBean.getTerminalifsc(), terminalBean.getcTerminalacctBranch(),
						terminalLevel, terminalBean.getTerminalaccountname(), ACTIVE, merchantOnboardBean.getMid(), storeBean.getStoreSqid(),
						terminalBean.getTermSqid(), terminalBean.getTerminaldefAccount(), terminalBean.getTerminalacctBankText(), 
						terminalBean.getTerminalacctBranchText(), terminalBean.getTerminalcifNumber(), terminalBean.getTerminalstmtfreq());
	            }
		}catch (Exception e) {
			loggerUtil.error("Error while adding in addTerminalWithAccountDetails() "+ e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}finally {
			strQry = null;
			strQry1 = null;
			paymentMethod = null;
		}
		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean addAccountDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		StringBuilder strQry = new StringBuilder();
		try {
			merchantOnboardBean.setAccountname(Optional.ofNullable(merchantOnboardBean.getAccountname()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setAcctBranchText(Optional.ofNullable(merchantOnboardBean.getAcctBranchText()).filter(s -> !s.trim().isEmpty()).orElse(null));

			strQry.append(" INSERT INTO MPS_SPOB_ACCT_TEMP( MSA_REFR_SQID, MSA_DMAC_INDC, MSA_ACCT_CRCY, MSA_ACCT_NUMB, MSA_DEFT_ACCT, MSA_IFSC_CODE, "
					+ " MSA_ACCT_BRAN, MSA_MAPG_LEVL, MSA_ACCT_NAME, MSA_REC_STUS, MSA_MERC_CODE, MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ ) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ");

			jdbcTemplate.update(strQry.toString(), merchantOnboardBean.getRefSqid(), merchantOnboardBean.getAcctWithBank(), 
					merchantOnboardBean.getCurrency()!=null && merchantOnboardBean.getCurrency() != "" ? merchantOnboardBean.getCurrency().split("-")[0] : null,
					merchantOnboardBean.getAcctNumber(), merchantOnboardBean.getDefAccount(), merchantOnboardBean.getIfsc(), merchantOnboardBean.getcAcctBranch(),
					merchantLevel, merchantOnboardBean.getAccountname(), ACTIVE, merchantOnboardBean.getMid(), 
					merchantOnboardBean.getAcctBankText(), merchantOnboardBean.getAcctBranchText(), merchantOnboardBean.getCifNumber(), merchantOnboardBean.getStmtfreq());
			
		} catch (Exception e) {
			loggerUtil.error("Error while adding account details "+ e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}
		return merchantOnboardBean;
	}
	
	@Override
	public void rollBackFunctionForMerchantOnboard(MerchantOnboardBean merchantOnboardBean) {
		try
		{
			if(merchantOnboardBean.getRefSqid() != 0)
			{
				jdbcTemplate.update("DELETE FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("DELETE FROM MPS_SPOB_PRIC_TEMP WHERE MSP_REFR_SQID = ? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("DELETE FROM MPS_SPOB_STOR_TEMP WHERE MSS_REFR_SQID = ? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("DELETE FROM MPS_SPOB_TERM_TEMP WHERE MST_REFR_SQID = ? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("DELETE FROM MPS_SPOB_ACCT_TEMP WHERE MSA_REFR_SQID = ? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("DELETE FROM MPS_SPOB_SCDP_TEMP WHERE MMS_REFR_SQID = ? ", merchantOnboardBean.getRefSqid());
			}
		}catch(Exception e){
			loggerUtil.error("Error while rollback merchant details "+ e.getMessage());
		}		
	}
	
	@Override
	public MerchantOnboardBean updateMerchantDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		StringBuffer updateQuery = new StringBuffer();
		try {
			
			merchantOnboardBean.setOwnername(Optional.ofNullable(merchantOnboardBean.getOwnername()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setLastname1(Optional.ofNullable(merchantOnboardBean.getLastname1()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setAddressline2(Optional.ofNullable(merchantOnboardBean.getAddressline2()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setBusinessContactEmailID(Optional.ofNullable(merchantOnboardBean.getBusinessContactEmailID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setTechnicalContactEmailID(Optional.ofNullable(merchantOnboardBean.getTechnicalContactEmailID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setMercAggrName(Optional.ofNullable(merchantOnboardBean.getMercAggrName()).filter(s -> !s.trim().isEmpty()).orElse(null));
			merchantOnboardBean.setMercAggr(Optional.ofNullable(merchantOnboardBean.getMercAggr()).filter(s -> !s.trim().isEmpty()).orElse(null));
			
			updateQuery.append(" UPDATE MPS_SPOB_MERC_TEMP SET ");
			updateQuery.append(" MSM_SALE_EXEC=?, MSM_LEGL_NAME=?, ");
			updateQuery.append(" MSM_DBA_NAME=?, MSM_OWNR_NAME=?, MSM_AGGR_NAME=?, MSM_CUST_FNAM=?, MSM_CUST_LNAM=?, MSM_CUST_ADD1=?, ");
			updateQuery.append(" MSM_CUST_ADD2=?, MSM_CUST_CNTY=?, MSM_CUST_STAT=?, MSM_CUST_CITY=?, MSM_CUST_PCOD=?, MSM_CUST_PHNO=?, ");
			updateQuery.append(" MSM_CUST_EMAL=?, MSM_PRCE_METY=?, ");
			updateQuery.append(" MSM_PRCE_CRCY=?, MSM_MSVF_LEVL=?, MSM_MSFP_NAME=?, ");
			updateQuery.append(" MSM_ITCH_MCC=?, ");
			updateQuery.append(" MSM_PAN_UQID=CX_DF_NE(?,?,?),MSM_LUPD_USER=?, ");
			updateQuery.append(" MSM_LUPD_INDT=SYSDATE, ");
			updateQuery.append(" MSM_REC_STUS=?, MSM_ACCT_MAPG_LEVL=?, MSM_AGGR_SQID=?, MSM_DEFT_STNM=?, ");
			updateQuery.append(" MSM_EXST_CUST=?, MSM_MPOR_REQR=?, MSM_BUSN_EMAL=?, MSM_BUSN_MOBL=?, ");
			updateQuery.append(" MSM_TECH_EMAL=?, MSM_TECH_MOBL=?, MSM_ERR_DESC=?, MSM_SESN_FLAG=0, ");
			updateQuery.append(" MSM_SESN_DATE=SYSDATE, MSM_SESN_USER=?, MSM_MERC_LOGO=?, MSM_ACCT_FLAG =?, ");
			updateQuery.append(" MSM_OWNR_TYPE=?, MSM_PRNT_ENTY=?, MSM_CNRY_INCP=?, MSM_DATE_INCP=?, MSM_UBO_PERC=?, MSM_INDV_NAME=?, MSM_BUSN_TYPE=?, ");
			updateQuery.append(" MSM_EMRT_ID=?, MSM_EMRTID_EXPR=?, MSM_PASP_NMBR=?, MSM_PASP_EXPR=?, MSM_MERC_NATY=?, MSM_ANIP_MERCODE=?, MSM_SCDP_FLAG=?, MSM_MCC_CODE=? ");
			updateQuery.append(" WHERE MSM_REFR_SQID = ? and MSM_INST_SQID = ? ");
			
			jdbcTemplate.update(updateQuery.toString(),
					merchantOnboardBean.getBankersalesNames(),
					merchantOnboardBean.getLegalname(), merchantOnboardBean.getDbaname(), merchantOnboardBean.getOwnername(),
					merchantOnboardBean.getMercAggrName(), merchantOnboardBean.getFirstname1(), merchantOnboardBean.getLastname1(), 
					merchantOnboardBean.getAddressline1(), merchantOnboardBean.getAddressline2(), merchantOnboardBean.getCountry(), 
					merchantOnboardBean.getState(), merchantOnboardBean.getSelectcity(), merchantOnboardBean.getPostalcode(),
					merchantOnboardBean.getPhone(), merchantOnboardBean.getContactEmail(),
					merchantOnboardBean.getMerchantType().replaceAll(" ", ""), 
					merchantOnboardBean.getCurrency()!=null && merchantOnboardBean.getCurrency() != "" ? merchantOnboardBean.getCurrency().split("-")[1] : null,
					merchantOnboardBean.getCaptureServiceFeeAt(),
					merchantOnboardBean.getMerchantServiceFeePlan()!=null && merchantOnboardBean.getMerchantServiceFeePlan() != "" ? merchantOnboardBean.getMerchantServiceFeePlan().split("-")[1] : null,
					//merchantOnboardBean.getCmccAutoComplete(),
					merchantOnboardBean.getCmccMerchantAutoComplete(),
					merchantOnboardBean.getCpancard(), instId, getClearDEK()!=null||!getClearDEK().isEmpty()?getClearDEK():null,
					userId, ACTIVE,
					merchantOnboardBean.getAcctMapLvl(),
					merchantOnboardBean.getMercAggr(), merchantOnboardBean.getDbaname(),
					merchantOnboardBean.getExistingCustomer(), merchantOnboardBean.getMerchantPortalRequired(),
					merchantOnboardBean.getBusinessContactEmailID(), merchantOnboardBean.getBusinessContactMobileNo(),
					merchantOnboardBean.getTechnicalContactEmailID(), merchantOnboardBean.getTechnicalContactMobileNo(),
					merchantOnboardBean.getRemarks(), userId, merchantOnboardBean.getMercLogoUpldData(),
					merchantOnboardBean.getMerchantAccountRequired(),
					// Ownership details 
					merchantOnboardBean.getOwnerType(), merchantOnboardBean.getParentEntityName(),
					merchantOnboardBean.getCountryOfIncorp(), merchantOnboardBean.getDateOfIncorp()==null || merchantOnboardBean.getDateOfIncorp().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(merchantOnboardBean.getDateOfIncorp()),
					merchantOnboardBean.getUboShareholderPctEntity(), merchantOnboardBean.getIndividualName(),
					merchantOnboardBean.getBusinessType(),
					merchantOnboardBean.getEmiratesIdNo(), merchantOnboardBean.getEmiratesIdExpiry()==null || merchantOnboardBean.getEmiratesIdExpiry().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(merchantOnboardBean.getEmiratesIdExpiry()),
					merchantOnboardBean.getPassportNumber(), merchantOnboardBean.getPassportExpiry()==null || merchantOnboardBean.getPassportExpiry().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(merchantOnboardBean.getPassportExpiry()),
					merchantOnboardBean.getNationality(), merchantOnboardBean.getAaniPayMercID(), merchantOnboardBean.getSecurityDepositRecoveryRequired(), merchantOnboardBean.getCmccMerchantAutoComplete(),
					merchantOnboardBean.getRefSqid(), instId);

		} catch (Exception e) {
			loggerUtil.error("Error while updating in updateMerchantDetails() "+e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}finally {
			updateQuery = null;
		}
		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean updatePriceDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		StringBuffer updateQuery = new StringBuffer();
		StringBuffer updateQuery1 = new StringBuffer();
		StringBuffer insertSql = new StringBuffer();
		try {			
			updateQuery.append(" UPDATE MPS_SPOB_PRIC_TEMP SET MSP_METY_SQID=?, MSP_FPLN_TYPE=?, MSP_FPLN_NAME=?, "
					+ " MSP_CAPT_MSFA=?, MSP_FPLN_SQID=?, "
					+ " MSP_STOR_REQD=?, MSP_INTC_PLUS=?, MSP_REC_STUS=?, MSP_FEE_WAVR=? WHERE MSP_REFR_SQID=? ");
			
			jdbcTemplate.update(updateQuery.toString(), merchantOnboardBean.getMerchantType().replaceAll(" ", ""), merchantOnboardBean.getFeePlanType(),
					merchantOnboardBean.getMerchantServiceFeePlan(),
					merchantOnboardBean.getCaptureServiceFeeAt(),
					merchantOnboardBean.getMerchantServiceFeePlan()!=null && merchantOnboardBean.getMerchantServiceFeePlan() != "" ? merchantOnboardBean.getMerchantServiceFeePlan().split("-")[0] : null,
					merchantOnboardBean.getNoOfStoresReq(), 
					0,//merchantOnboardBean.getIntchgPlus(), 
					ACTIVE,
					//merchantOnboardBean.getMercFeeDeduWaiver(),
					Optional.ofNullable(merchantOnboardBean.getMercFeeDeduWaiver()).filter(v -> v != 0).orElse(0),
					merchantOnboardBean.getRefSqid());
			
			int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_SPOB_SCDP_TEMP WHERE MMS_REFR_SQID=?", Integer.class, merchantOnboardBean.getRefSqid());

			if (count > 0) {
				if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 1) {
					updateQuery1.append("UPDATE MPS_SPOB_SCDP_TEMP SET MMS_DEPS_LIMT=?, MMS_CALC_TYPE=?, MMS_INDP_AMNT=?, MMS_INDP_PERC=?, " 
							+ " MMS_LUPD_USID=?, MMS_LUPD_SYDT=SYSDATE, MMS_ACTIVE_FLAG=? WHERE MMS_REFR_SQID=?" );
					
					//merchantOnboardBean.setSecurityDepositCalcType(merchantOnboardBean.getSdrIndc() == 0 ? "F" : "P");
					jdbcTemplate.update(updateQuery1.toString(), merchantOnboardBean.getSecurityDepositLimit(), 
						merchantOnboardBean.getSdrIndc() == 0 ? "F" : "P", merchantOnboardBean.getSecurityDepositRecoveryAmount(), 
					    merchantOnboardBean.getSecurityDepositRecoveryPercentage(), userId, ACTIVE, merchantOnboardBean.getRefSqid());
				} else if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 0) {
					updateQuery1.append("UPDATE MPS_SPOB_SCDP_TEMP SET MMS_DEPS_LIMT=?, MMS_CALC_TYPE=0, " 
							+ " MMS_LUPD_USID=?, MMS_LUPD_SYDT=SYSDATE, MMS_ACTIVE_FLAG=? WHERE MMS_REFR_SQID=?" );
					
					//merchantOnboardBean.setSecurityDepositCalcType(merchantOnboardBean.getSdrIndc() == 0 ? "F" : "P");
					jdbcTemplate.update(updateQuery1.toString(), merchantOnboardBean.getSecurityDepositLimit(), userId, ACTIVE, merchantOnboardBean.getRefSqid());
				}
			} else {
			    if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 1) {
			        insertSql.append("INSERT INTO MPS_SPOB_SCDP_TEMP (MMS_INST_SQID, MMS_REFR_SQID, MMS_DEPS_LIMT, MMS_CALC_TYPE, MMS_INDP_AMNT, MMS_INDP_PERC, MMS_LUPD_USID, MMS_LUPD_SYDT, MMS_ACTIVE_FLAG) "
			                  + "VALUES (?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)");
			        jdbcTemplate.update(insertSql.toString(), instId, merchantOnboardBean.getRefSqid(), merchantOnboardBean.getSecurityDepositLimit(),
			                merchantOnboardBean.getSdrIndc() == 0 ? "F" : "P", merchantOnboardBean.getSecurityDepositRecoveryAmount(),
			                merchantOnboardBean.getSecurityDepositRecoveryPercentage(), userId, ACTIVE);
			    } else {
			    	insertSql.append("INSERT INTO MPS_SPOB_SCDP_TEMP (MMS_INST_SQID, MMS_REFR_SQID, MMS_DEPS_LIMT, MMS_CALC_TYPE, MMS_LUPD_USID, MMS_LUPD_SYDT, MMS_ACTIVE_FLAG) "
			                  + "VALUES (?, ?, ?, ?, ?, SYSDATE, ?)");
			        jdbcTemplate.update(insertSql.toString(), instId, merchantOnboardBean.getRefSqid(), merchantOnboardBean.getSecurityDepositLimit(), merchantOnboardBean.getSdrIndc() == 0 ? "F" : "P", userId, ACTIVE);
			    }
			}
			
		} catch (Exception e) {
			loggerUtil.error("Error while updating in updatePriceDetails() "+e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}finally {
			updateQuery = null; insertSql = null;
		}
		return merchantOnboardBean;
	}
	
	@Override
	public void deleteTerminalDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId, Integer userId) {
		jdbcTemplate.update("DELETE FROM MPS_SPOB_TERM_TEMP WHERE MST_INST_SQID = ? and MST_REFR_SQID=? and MST_STOR_SQID=? ", 
				instId, merchantOnboardBean.getRefSqid(), storeBean.getStoreSqid());
		
		jdbcTemplate.update("DELETE FROM MPS_SPOB_PROD_TEMP WHERE MSP_STOR_SQID=? ", storeBean.getStoreSqid());
		
		jdbcTemplate.update("DELETE FROM MPS_SPOB_ACCT_TEMP WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_STOR_CODE = ? AND MSA_MAPG_LEVL = ? ", 
				merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid() ,storeBean.getStoreSqid(), terminalLevel);
	}
	
	public MerchantOnboardBean insertOrUpdateToProdTemp(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId){
		List<KeyValueBean> lst = null;
		String prodType = "";
		try {
			
			StringBuffer selectQuery = new StringBuffer("SELECT MST_TMPD_SQID, COUNT(NVL(MST_TERM_TID,0)) AS NO_OF_TERM FROM MPS_SPOB_TERM_TEMP "
					+ " WHERE MST_INST_SQID = ? AND MST_REFR_SQID = ? AND MST_STOR_SQID = ? "
					+ " GROUP BY MST_TMPD_SQID ORDER BY MST_TMPD_SQID ");
			
			lst = (List<KeyValueBean>) jdbcTemplate.query(selectQuery.toString(),
					new RowMapper<KeyValueBean>() {
						public KeyValueBean mapRow(ResultSet rs, int rowNum) throws SQLException {
							KeyValueBean bean = new KeyValueBean();
							bean.setKey(rs.getString(1));
							bean.setValue(rs.getString(2));
							return bean;
						}
					}, instId, merchantOnboardBean.getRefSqid(), storeBean.getStoreSqid());

			for(KeyValueBean beanvalue : lst) {
				
				if(prodType.equalsIgnoreCase("")) {
					prodType = beanvalue.getKey();
				}else {
					prodType = prodType + ", " +beanvalue.getKey();
				}
				int termExstCount = 0;
				String val = jdbcTemplate.queryForObject("select NVL((SELECT MSP_TOTL_TCNT FROM MPS_SPOB_PROD_TEMP WHERE MSP_TMPD_SQID = ? AND MSP_STOR_SQID = ?),0) from dual ", String.class, beanvalue.getKey(), storeBean.getStoreSqid());
				if(val!=null && val.equalsIgnoreCase("")!=true ) {
					termExstCount = Integer.parseInt(val);
				}
				if(termExstCount > 0) {
					
					jdbcTemplate.update("UPDATE MPS_SPOB_PROD_TEMP SET MSP_TOTL_TCNT = ? , MSP_EXST_TCNT = ? WHERE MSP_TMPD_SQID = ? AND MSP_STOR_SQID = ? ",
							beanvalue.getValue(), 
							termExstCount,
							beanvalue.getKey(),
							storeBean.getStoreSqid());		
				}
				/*else {
					
					jdbcTemplate.update("INSERT INTO MPS_SPOB_PROD_TEMP ( MSP_STOR_SQID,MSP_TMPD_SQID,MSP_TOTL_TCNT,MSP_EXST_TCNT,MSP_INSD_SYDT ) VALUES (?,?,?,?,SYSDATE)",
							storeBean.getStoreSqid(),
							beanvalue.getKey(),
							beanvalue.getValue(),
							termExstCount);					
				}*/			
			}
			
			if(prodType.equalsIgnoreCase("")!=true) {				
				jdbcTemplate.update("DELETE MPS_SPOB_PROD_TEMP WHERE MSP_TMPD_SQID NOT IN ("
						+ Stream.generate(() -> "?").limit(prodType.split(",").length).collect(Collectors.joining(", "))
						+ " ) AND MSP_STOR_SQID = ? ", Stream.concat(Arrays.asList(prodType.split(",")).stream(), Arrays.asList(storeBean.getStoreSqid()).stream()).collect(Collectors.toList()).toArray());
			}
			
		}catch (Exception e) {
			loggerUtil.error("Error while updating in insertOrUpdateToProdTemp() "+e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}finally {
			lst = null;
			prodType = null;
		}
		return merchantOnboardBean;
	}
	
	@Override
	public MerchantOnboardBean updateAccountDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		
		StringBuffer updateQuery = new StringBuffer();
		try {
			
			int exitacctornot = jdbcTemplate.queryForObject("SELECT NVL((SELECT COUNT(*) FROM MPS_SPOB_ACCT_TEMP WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_MAPG_LEVL = ?),0) FROM DUAL ", 
					Integer.class, merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(), merchantLevel);
			if(exitacctornot > 0) {
				
				updateQuery.append(" UPDATE MPS_SPOB_ACCT_TEMP SET "
								 + " MSA_DMAC_INDC=?, MSA_ACCT_CRCY=?, "
								 + " MSA_ACCT_NUMB=?, MSA_DEFT_ACCT=?, MSA_IFSC_CODE=?,"
								 + " MSA_ACCT_BRAN=?, MSA_ACCT_NAME=?, MSA_REC_STUS=?, "
								 + " MSA_BANK_NAME=?, MSA_BRCH_NAME=?, MSA_CIF_NMBR=?, MSA_STMT_FREQ=? "
								 + " WHERE MSA_REFR_SQID=? AND MSA_MERC_CODE=? AND MSA_MAPG_LEVL = ? ");
				
				jdbcTemplate.update(updateQuery.toString(), merchantOnboardBean.getAcctWithBank(),
						merchantOnboardBean.getCurrency()!=null && merchantOnboardBean.getCurrency() != "" ? merchantOnboardBean.getCurrency().split("-")[0] : null,
						merchantOnboardBean.getAcctNumber(), merchantOnboardBean.getDefAccount(), merchantOnboardBean.getIfsc(), 
						merchantOnboardBean.getcAcctBranch(), 
						merchantOnboardBean.getAccountname(), ACTIVE,
						merchantOnboardBean.getAcctBankText(), merchantOnboardBean.getAcctBranchText(), merchantOnboardBean.getCifNumber(), merchantOnboardBean.getStmtfreq(),
						merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(), merchantLevel);
			}else {
				
				updateQuery.append(" INSERT INTO MPS_SPOB_ACCT_TEMP( MSA_REFR_SQID, MSA_DMAC_INDC, MSA_ACCT_CRCY, MSA_ACCT_NUMB, MSA_DEFT_ACCT, MSA_IFSC_CODE, "
								 + " MSA_ACCT_BRAN, MSA_MAPG_LEVL, MSA_ACCT_NAME, MSA_REC_STUS, MSA_MERC_CODE, MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ "
								 + " ) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ");

				jdbcTemplate.update(updateQuery.toString(),	merchantOnboardBean.getRefSqid(), merchantOnboardBean.getAcctWithBank(), 
						merchantOnboardBean.getCurrency()!=null && merchantOnboardBean.getCurrency() != "" ? merchantOnboardBean.getCurrency().split("-")[0] : null,
						merchantOnboardBean.getAcctNumber(), merchantOnboardBean.getDefAccount(), merchantOnboardBean.getIfsc(), 
						merchantOnboardBean.getcAcctBranch(), merchantLevel, merchantOnboardBean.getAccountname(), ACTIVE, 
						merchantOnboardBean.getMid(), merchantOnboardBean.getAcctBankText(), merchantOnboardBean.getAcctBranchText(), merchantOnboardBean.getCifNumber(),
						merchantOnboardBean.getStmtfreq());
			}
			
		} catch (Exception e) {
			loggerUtil.error("Error while updating in updateAccountDetails() "+e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}finally {
			updateQuery = null;
		}
		return merchantOnboardBean;
	}
	
	@Override
	public MerchantOnboardBean updateStoreWithAccountDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId, Integer userId) {
		StringBuffer updateQuery = new StringBuffer();
		StringBuffer updateQuery1 = new StringBuffer();
		StringBuffer updateQuery2 = new StringBuffer();
		Integer output = 0;
		int seqUDFsqid = 0;
		try {
			
			storeBean.setMvv(Optional.ofNullable(storeBean.getMvv()).filter(s -> !s.trim().isEmpty()).orElse(null));
        	storeBean.setCyberMercID(Optional.ofNullable(storeBean.getCyberMercID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			storeBean.setCyberMercPass(Optional.ofNullable(storeBean.getCyberMercPass()).filter(s -> !s.trim().isEmpty()).orElse(null));
			storeBean.setCyberMercKey(Optional.ofNullable(storeBean.getCyberMercKey()).filter(s -> !s.trim().isEmpty()).orElse(null));
			//storeBean.setAaniPayMercID(Optional.ofNullable(storeBean.getAaniPayMercID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			storeBean.setPaymentFrecForCard(Optional.ofNullable(storeBean.getPaymentFrecForCard()).filter(v -> v != 0).orElse(1));  
			storeBean.setPaymentFrecForAaniPay(Optional.ofNullable(storeBean.getPaymentFrecForAaniPay()).filter(v -> v != 0).orElse(1));  
			
			updateQuery.append ("UPDATE MPS_SPOB_STOR_TEMP SET "
			       		+ " MSS_STOR_CODE =?,MSS_STTD_NAME=?, MSS_MERC_GRAD=?,MSS_METY_SQID=?, "
			       		+ " MSS_MCC_CODE=?, MSS_MVV_FIELD=?, MSS_GEO_CORD = ?, "
			       		+ " MSS_CRPT_SQID=?, MSS_LINE_RCPT1=?,MSS_LINE_RCPT2=?, MSS_LINE_RCPT3=?, "
			       		+ " MMS_WBST_NAME = ?, MMS_WEBS_ADDR = ?, MSS_EMAIL_ADID= ?,"
			       		+ " MSS_GST_NUMR=?, MSS_GST_FLAG=?, MSS_ENTY_SQID=?,MSS_INCR_STUS = ?,"
			       		+ " MSS_PROF_DOCM = ?, MSS_GEO_LATD = ?, MSS_GEO_CPER = ?, MSS_REC_STUS=1, MSS_TERM_FLAG = ?, MSS_NATR_BUSN = ?, "
			       		+ " MSS_MERC_RELM = ?, MSS_GOVT_REGMER = ?, MSS_HOME_CRCYID = ?, MSS_MERC_MVV = ?, "
			       		+ " MSS_ADDR_FLAG = ? , MSS_ACCT_FLAG = ?, MSS_CUST_ADD1 = ?, MSS_CUST_ADD2 = ? , MSS_CUST_CNTY = ?, "
			       		+ " MSS_CUST_STAT = ?, MSS_CUST_CITY = ?, MSS_CUST_PCOD = ?, "
			       		+ " MSS_BUSN_EMAL = ?, MSS_BUSN_MOBL = ?, MSS_TECH_EMAL = ?, MSS_TECH_MOBL = ?, "
			       		+ " MSS_RISK_REQD = ?, MSS_RSCG_SQID = ?, MSS_PCI_EXPY = ?, MSS_CLUD_EXPY = ?, MSS_TRAD_EXPY = ?, "
			       		+ " MSS_MTHY_TCNT = ?, MSS_AVTS_AMNT = ?, MSS_QSAL_AMNT = ?, MSS_TXNS_REFD_PERC = ?, MSS_TXNS_CHBK_PERC = ?, MSS_TXNS_PERC = ?, MSS_TXNS_INT_PERC = ?, "
			       		+ " MSS_CYBS_MID = ?, MSS_CYBS_PWD = ?, MSS_CYBS_MKEY = ?, MSS_PYMT_FREQ_CARD = ?, "
			       		+ " MSS_FREQ_DAYS_CARD = ?, MSS_TXNS_PYMT = ?, MSS_PYMT_FREQ_AANI = ?, MSS_FREQ_DAYS_AANI = ?, MSS_PRIC_REQD = ?, "
			       		+ " MSS_STOR_TYPE = ?, MSS_ANIP_STORCODE = ? "
			       		+ " WHERE MSS_INST_SQID=? AND MSS_REFR_SQID=? AND MSS_STOR_SQID=?");
			
			output = jdbcTemplate.update(updateQuery.toString(), storeBean.getStoreCode(), storeBean.getStoreTradingName(),
							storeBean.getMerchantGrade()==null||storeBean.getMerchantGrade().equals("")?null:Integer.parseInt(storeBean.getMerchantGrade()),
							merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
							//storeBean.getCmccAutoComplete(),
							merchantOnboardBean.getCmccMerchantAutoComplete(),
							storeBean.getMvv(), 
							storeBean.getGeoCoordinates()==null||storeBean.getGeoCoordinates().equals("")?null:Integer.parseInt(storeBean.getGeoCoordinates()),
							storeBean.getCryptoMethod()==null||storeBean.getCryptoMethod().equals("")?null:Integer.parseInt(storeBean.getCryptoMethod()),
							storeBean.getLine1Rec(),
							storeBean.getLine2Rec(), storeBean.getLine3Rec(),
							storeBean.getWebSiteName(), storeBean.getWebsiteAddress(),
							storeBean.getStoreEmail(), 
							storeBean.getGstNumber()==null||storeBean.getGstNumber().equals("")?null:storeBean.getGstNumber(),
							storeBean.getGstFlag()==null||storeBean.getGstFlag().equals("")?0:Integer.parseInt(storeBean.getGstFlag()),
							//storeBean.getMerchantEntityName(),
							String.join(",", merchantOnboardBean.getMerchantEntityName()),
							storeBean.getIncorpstatus()==null||storeBean.getIncorpstatus().equals("")?null:Integer.parseInt(storeBean.getIncorpstatus()),
							storeBean.getPrfDocsProv(), storeBean.getLatitude(),
							storeBean.getLongitude(), 
							storeBean.getTermneed()==null?0:storeBean.getTermneed().equalsIgnoreCase("false")?0:storeBean.getTermneed().equalsIgnoreCase("true")?1:Integer.parseInt(storeBean.getTermneed()),
							storeBean.getNatureOfBusiness()==null?null:storeBean.getNatureOfBusiness().split("~")[1],
							storeBean.getRelatonShipManager()==null||storeBean.getRelatonShipManager().equals("")?null:Integer.parseInt(storeBean.getRelatonShipManager()),
							storeBean.getGovtRegMerchant()==null||storeBean.getGovtRegMerchant().equals("")?"0":Integer.parseInt(storeBean.getGovtRegMerchant()),
							storeBean.getHomeCountryId(), //MCO
							storeBean.getMvv(),
							storeBean.getUseSameAsMercAddressForStore()==null||storeBean.getUseSameAsMercAddressForStore().equals("")?"0":storeBean.getUseSameAsMercAddressForStore(),
							storeBean.getUseSameAsMercAccountForStore()==null||storeBean.getUseSameAsMercAccountForStore().equals("")?"0":storeBean.getUseSameAsMercAccountForStore(),
							storeBean.getStoreaddressline1()==null?null:storeBean.getStoreaddressline1(),
							storeBean.getStoreaddressline2()==null?null:storeBean.getStoreaddressline2(),
							storeBean.getStorecountry()==null?null:storeBean.getStorecountry(),
							storeBean.getStorestate()==null?null:storeBean.getStorestate(),
							storeBean.getStoreselectcity()==null?null:storeBean.getStoreselectcity(),
							storeBean.getStorepostalcode()==null?null:storeBean.getStorepostalcode(),
							storeBean.getStorebusinessContactEmailID(), storeBean.getStorebusinessContactMobileNo(), 
							storeBean.getStoretechnicalContactEmailID(), storeBean.getStoretechnicalContactMobileNo(),
							storeBean.getStoreRiskDetailsRequired()==null||storeBean.getStoreRiskDetailsRequired().equals("")?"0":storeBean.getStoreRiskDetailsRequired(),
							storeBean.getRiskCategory()==null||storeBean.getRiskCategory().equals("")?null:Integer.parseInt(storeBean.getRiskCategory()),
							storeBean.getPadssPCIdssCertExpDate()==null || storeBean.getPadssPCIdssCertExpDate().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(storeBean.getPadssPCIdssCertExpDate()),
							storeBean.getCloudCertExpDate()==null || storeBean.getCloudCertExpDate().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(storeBean.getCloudCertExpDate()),
							storeBean.getTradeLicenseExpDate()==null || storeBean.getTradeLicenseExpDate().trim().isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(storeBean.getTradeLicenseExpDate()),
							storeBean.getTransCountperMonth(),storeBean.getAverageTicketSize(),
							storeBean.getQuarterlySale(),storeBean.getTransToRefundPercent(),
							storeBean.getTransToChargebackPercent(),storeBean.getTransPercent(),
							storeBean.getInternationalTransPercent(),
							storeBean.getCyberMercID(),
							storeBean.getCyberMercPass(),
							storeBean.getCyberMercKey(),
							storeBean.getPaymentFrecForCard(),
							storeBean.getPaymentFrecForCardDays(),
							storeBean.getAaniPayTransPayment(),
							storeBean.getPaymentFrecForAaniPay(),
							storeBean.getPaymentFrecForAaniPayDays(),
							storeBean.getStorePriceDetailsRequired(),
							storeBean.getStoreType(), storeBean.getAaniPayStoreID(),
							instId, merchantOnboardBean.getRefSqid(), storeBean.getStoreSqid());
			
			if(output !=0 && storeBean.getUseSameAsMercAccountForStore().equals("1")){
				
				storeBean.setcStoreacctBranch(Optional.ofNullable(storeBean.getcStoreacctBranch()).filter(s -> !s.trim().isEmpty()).orElse(null));
				storeBean.setStoreaccountname(Optional.ofNullable(storeBean.getStoreaccountname()).filter(s -> !s.trim().isEmpty()).orElse(null));
				storeBean.setStoreacctBranchText(Optional.ofNullable(storeBean.getStoreacctBranchText()).filter(s -> !s.trim().isEmpty()).orElse(null));
				
				int existacctornot = jdbcTemplate.queryForObject("SELECT NVL((SELECT COUNT(*) FROM MPS_SPOB_ACCT_TEMP WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_STOR_CODE = ? AND MSA_MAPG_LEVL = ?),0) FROM DUAL ", 
						Integer.class, merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(), storeBean.getStoreSqid(), storeLevel);
				if(existacctornot > 0) {
					
					updateQuery1.append(" UPDATE MPS_SPOB_ACCT_TEMP SET MSA_DMAC_INDC=?, MSA_ACCT_CRCY=?, "
								+ " MSA_ACCT_NUMB=?, MSA_IFSC_CODE=?, MSA_ACCT_BRAN=?, MSA_ACCT_NAME=?, MSA_DEFT_ACCT=?, "
								+ " MSA_REC_STUS=?, MSA_BANK_NAME=?, MSA_BRCH_NAME=?, MSA_CIF_NMBR=?, MSA_STMT_FREQ=? "
								+ " WHERE MSA_REFR_SQID=? AND MSA_MERC_CODE=? AND MSA_STOR_CODE=? AND MSA_MAPG_LEVL=? ");	
					jdbcTemplate.update(updateQuery1.toString(), storeBean.getStoreacctWithBank(),
							storeBean.getStorecurrency()!=null && storeBean.getStorecurrency() != "" ? storeBean.getStorecurrency().split("-")[0] : null,
							storeBean.getStoreacctNumber(), storeBean.getStoreifsc(), 
							storeBean.getcStoreacctBranch(), storeBean.getStoreaccountname(), storeBean.getStoredefAccount(),
							ACTIVE, 
							storeBean.getStoreacctBankText(), storeBean.getStoreacctBranchText(), storeBean.getStorecifNumber(), storeBean.getStorestmtfreq(),
							merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(),	storeBean.getStoreSqid(), storeLevel);
				} else {
					updateQuery1.append("INSERT INTO MPS_SPOB_ACCT_TEMP( MSA_REFR_SQID, MSA_DMAC_INDC, MSA_ACCT_CRCY, MSA_ACCT_NUMB, MSA_IFSC_CODE, ")
		               			.append("MSA_ACCT_BRAN, MSA_MAPG_LEVL, MSA_ACCT_NAME, MSA_REC_STUS, MSA_MERC_CODE, MSA_STOR_CODE, MSA_DEFT_ACCT, ")
		               			.append("MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ ")
		               			.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
					
					jdbcTemplate.update(updateQuery1.toString(),	
						merchantOnboardBean.getRefSqid(), 
						storeBean.getStoreacctWithBank(), 
						storeBean.getStorecurrency()!=null && storeBean.getStorecurrency() != "" ? storeBean.getStorecurrency().split("-")[0] : null,
						storeBean.getStoreacctNumber(), storeBean.getStoreifsc(), storeBean.getcStoreacctBranch(),
						storeLevel,	storeBean.getStoreaccountname(), ACTIVE, merchantOnboardBean.getMid(), storeBean.getStoreSqid(),
						storeBean.getStoredefAccount(),	storeBean.getStoreacctBankText(), storeBean.getStoreacctBranchText(), 
						storeBean.getStorecifNumber(), storeBean.getStorestmtfreq() );
				}
				
			}else if(output !=0 && storeBean.getUseSameAsMercAccountForStore().equals("0")){
				jdbcTemplate.update("DELETE FROM MPS_SPOB_ACCT_TEMP WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_STOR_CODE = ? AND MSA_MAPG_LEVL = ? ", 
						merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(), storeBean.getStoreSqid(), storeLevel);
			}
			
			//Store Pricing Details
			if(output !=0 && storeBean.getStorePriceDetailsRequired().equals("1")){
				
				int existpriceornot = jdbcTemplate.queryForObject("SELECT NVL((SELECT COUNT(*) FROM MPS_SPOB_PRIC_TEMP WHERE MSP_REFR_SQID = ? AND MSP_ENTY_SQID = ?),0) FROM DUAL ", 
						Integer.class, storeBean.getStoreSqid(), storeLevel);
				if(existpriceornot > 0) {
					
					updateQuery2.append(" UPDATE MPS_SPOB_PRIC_TEMP SET MSP_METY_SQID=?, MSP_FPLN_TYPE=?, MSP_FPLN_NAME=?, MSP_CAPT_MSFA=?, MSP_FPLN_SQID=?, "
							+ "	MSP_INTC_PLUS=?, MSP_REC_STUS=?, MSP_FEE_WAVR=? WHERE MSP_REFR_SQID=? AND MSP_ENTY_SQID=?");	
					jdbcTemplate.update(updateQuery2.toString(), 
							merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
							storeBean.getStoreFeePlanType(),
							storeBean.getStoreMerchantServiceFeePlan(), 
							storeBean.getStoreCaptureServiceFeeAt(),
							storeBean.getStoreMerchantServiceFeePlan()!=null && storeBean.getStoreMerchantServiceFeePlan() != "" ? storeBean.getStoreMerchantServiceFeePlan().split("-")[0] : null,
							0, ACTIVE, 
							//storeBean.getStoreMercFeeDeduWaiver(),
							Optional.ofNullable(storeBean.getStoreMercFeeDeduWaiver()).filter(v -> v != 0).orElse(0),
							storeBean.getStoreSqid(), storeLevel);
				} else {
					updateQuery2.append("INSERT INTO MPS_SPOB_PRIC_TEMP(MSP_REFR_SQID,MSP_METY_SQID,MSP_FPLN_TYPE,MSP_FPLN_NAME,MSP_CAPT_MSFA,MSP_FPLN_SQID, ")
							.append(" MSP_INTC_PLUS,MSP_REC_STUS,MSP_FEE_WAVR,MSP_ENTY_SQID ")
							.append(") VALUES (?,?,?,?,?,?,?,?,?,?) ");
					jdbcTemplate.update(updateQuery2.toString(), 
							storeBean.getStoreSqid(),
							merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
							storeBean.getStoreFeePlanType(),
							storeBean.getStoreMerchantServiceFeePlan(), 
							storeBean.getStoreCaptureServiceFeeAt(),
							storeBean.getStoreMerchantServiceFeePlan()!=null && storeBean.getStoreMerchantServiceFeePlan() != "" ? storeBean.getStoreMerchantServiceFeePlan().split("-")[0] : null,
							0,ACTIVE, 
							//storeBean.getStoreMercFeeDeduWaiver(),
							Optional.ofNullable(storeBean.getStoreMercFeeDeduWaiver()).filter(v -> v != 0).orElse(0),
							storeLevel);
				}
				
			}else if(output !=0 && storeBean.getStorePriceDetailsRequired().equals("0")){
				jdbcTemplate.update("DELETE FROM MPS_SPOB_PRIC_TEMP WHERE MSP_REFR_SQID = ? AND MSP_ENTY_SQID = ? ", 
						storeBean.getStoreSqid(), storeLevel);
			}
			
			//Store UDF Details
			if(output !=0) {

				int udfCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=?", 
							   Integer.class, instId, storeBean.getStoreSqid(), storeLevel);

				if(udfCount == 0){
					seqUDFsqid = jdbcTemplate.queryForObject("SELECT SEQ_MAU_UDF_SQID.NEXTVAL FROM DUAL", Integer.class);
				}else{
					seqUDFsqid = jdbcTemplate.queryForObject("SELECT DISTINCT MAU_UDF_SQID FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? "
								+ " AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=?", Integer.class, instId, storeBean.getStoreSqid(), storeLevel);
				}

				jdbcTemplate.update("UPDATE MPS_ADMN_UDF_TEMP SET MAU_UDF_STUS=0 WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=?", 
							   instId, storeBean.getStoreSqid(), storeLevel);

				for(byte i=1; i<=3; i++) {
					if(i==1 && ((storeBean.getAdditionalUDFField1() != null && storeBean.getAdditionalUDFField1().trim().length() != 0) ||
						(storeBean.getAdditionalUDFValue1() != null && storeBean.getAdditionalUDFValue1().trim().length() != 0))) {
							
						udfCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=?", 
									   Integer.class, instId, storeBean.getStoreSqid(), storeLevel, 1);
									   
						if(udfCount == 0) {
							jdbcTemplate.update("INSERT INTO MPS_ADMN_UDF_TEMP(MAU_INST_SQID,MAU_UDF_SQID,MAU_CAPT_LEVL,MAU_UDF_CAPTN,MAU_DATA_TYPE,MAU_UDF_DATA, "
								+ " MAU_REFR_SQID,MAU_UDF_STUS,MAU_INSD_USID,MAU_INSD_SYDT,MAU_FILD_ID) "
								+ " VALUES (?,?,?,?,?,?,?,?,?,(select now()),?)",
								instId,seqUDFsqid,storeLevel,storeBean.getAdditionalUDFField1(),3,storeBean.getAdditionalUDFValue1(),
								storeBean.getStoreSqid(),ACTIVE,userId,1);
						}else{
							jdbcTemplate.update("UPDATE MPS_ADMN_UDF_TEMP SET MAU_UDF_STUS=1, MAU_UDF_CAPTN=?, MAU_UDF_DATA=? "
								+ " WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=?", 
								storeBean.getAdditionalUDFField1(), storeBean.getAdditionalUDFValue1(), 
								instId, storeBean.getStoreSqid(), storeLevel, 1);
						}
					}
					if(i==2 && ((storeBean.getAdditionalUDFField2() != null && storeBean.getAdditionalUDFField2().trim().length() != 0) ||
						(storeBean.getAdditionalUDFValue2() != null && storeBean.getAdditionalUDFValue2().trim().length() != 0))) {
						
						udfCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=?", 
								   Integer.class, instId, storeBean.getStoreSqid(), storeLevel, 2);
									   
						if(udfCount == 0) {
							jdbcTemplate.update("INSERT INTO MPS_ADMN_UDF_TEMP(MAU_INST_SQID,MAU_UDF_SQID,MAU_CAPT_LEVL,MAU_UDF_CAPTN,MAU_DATA_TYPE,MAU_UDF_DATA, "
								+ " MAU_REFR_SQID,MAU_UDF_STUS,MAU_INSD_USID,MAU_INSD_SYDT,MAU_FILD_ID) "
								+ " VALUES (?,?,?,?,?,?,?,?,?,(select now()),?)",
								instId,seqUDFsqid,storeLevel,storeBean.getAdditionalUDFField2(),3,storeBean.getAdditionalUDFValue2(),
								storeBean.getStoreSqid(),ACTIVE,userId,2);
						}else{
							jdbcTemplate.update("UPDATE MPS_ADMN_UDF_TEMP SET MAU_UDF_STUS=1, MAU_UDF_CAPTN=?, MAU_UDF_DATA=? "
								+ " WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=?", 
								storeBean.getAdditionalUDFField2(), storeBean.getAdditionalUDFValue2(), 
								instId, storeBean.getStoreSqid(), storeLevel, 2);
						}
					}
					if(i==3 && ((storeBean.getAdditionalUDFField3() != null && storeBean.getAdditionalUDFField3().trim().length() != 0) ||
					  (storeBean.getAdditionalUDFValue3() != null && storeBean.getAdditionalUDFValue3().trim().length() != 0))){
					
					  udfCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=?", 
							  	 Integer.class, instId, storeBean.getStoreSqid(), storeLevel, 3);
							   
					  if(udfCount == 0) {
						    jdbcTemplate.update("INSERT INTO MPS_ADMN_UDF_TEMP(MAU_INST_SQID,MAU_UDF_SQID,MAU_CAPT_LEVL,MAU_UDF_CAPTN,MAU_DATA_TYPE,MAU_UDF_DATA, "
								+ " MAU_REFR_SQID,MAU_UDF_STUS,MAU_INSD_USID,MAU_INSD_SYDT,MAU_FILD_ID) "
								+ " VALUES (?,?,?,?,?,?,?,?,?,(select now()),?)",
								instId,seqUDFsqid,storeLevel,storeBean.getAdditionalUDFField3(),3,storeBean.getAdditionalUDFValue3(),
								storeBean.getStoreSqid(),ACTIVE,userId,3);
						}else{
							jdbcTemplate.update("UPDATE MPS_ADMN_UDF_TEMP SET MAU_UDF_STUS=1, MAU_UDF_CAPTN=?, MAU_UDF_DATA=? "
								+ " WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=?", 
								storeBean.getAdditionalUDFField3(), storeBean.getAdditionalUDFValue3(), 
								instId, storeBean.getStoreSqid(), storeLevel, 3);
						}
					}
				}
			}
		} catch (Exception e) {
			loggerUtil.error("Error while updating in updateStoreWithAccountDetails() "+e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}finally {
			updateQuery = null;
			updateQuery1 = null;
			updateQuery2 = null;
			output = 0;
			seqUDFsqid = 0;
		}
		return merchantOnboardBean;
	}

	@Override
	public MerchantOnboardBean updateTerminalWithAccountDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, 
			MerchantOnboardTerminalBean terminalBean, Integer instId, Integer userId) {
		StringBuffer updateQuery = new StringBuffer();
		StringBuffer updateQuery1 = new StringBuffer();
		StringBuffer updateQuery2 = new StringBuffer();
		String termAcctBranch = null;
		Integer output = 0;
		String paymentMethod = null;
		try {
			terminalBean.setAaniPayTermID(Optional.ofNullable(terminalBean.getAaniPayTermID()).filter(s -> !s.trim().isEmpty()).orElse(null));
			if(terminalBean.getPaymentMethod() != null && !terminalBean.getPaymentMethod().equals("")) {
		        for (byte i = 0; i < terminalBean.getPaymentMethod().length; i++) {
		        	paymentMethod = (i==0) ? terminalBean.getPaymentMethod()[i] : paymentMethod + ","+terminalBean.getPaymentMethod()[i];
		        }
			}
			
			updateQuery.append(" UPDATE MPS_SPOB_TERM_TEMP SET MST_TERM_TID = ?, MST_TMPD_SQID = ?, MST_REC_STUS = 1, "
		    		+" MST_RISK_FLAG = ?, MST_RISK_NAME = ?, MST_BRAND_SQID = ?, MST_PLAN_NAME = ?, MST_TPRF_SQID = ?, MST_CARD_GRUP = ?, " 
		    		+" MST_ENQR_CODE = ?, MST_APP_ID = ?, MST_AUTH_TOK = ?, " 
		    		+" MST_POS_NUM = ?, MST_ACCT_FLAG = ?, MST_MRCP_SQID  = ?, "
		    		+ "MST_RRF_REQD = ?, MST_RRF_INDC = ?, MST_DOM_REC_PERC = ?, MST_INT_REC_PERC = ?, "
		    		+ "MST_DOM_FIXD_AMT = ?, MST_INT_FIXD_AMT = ?, MST_DOM_HDAY = ?, MST_INT_HDAY = ?, "
		    		+ "MST_SD_RECV_REQD = ?, MST_SD_INDC = ?, MST_SD_PERC_AMT = ?, MST_SD_NOOF_DAYS = ?, "
		    		+ "MST_PRIC_REQD = ?, MST_MCC_CODE = ?, MST_RECR_FEE_WAVR = ?, MST_PYMT_METD = ?, MST_ANIP_TERMCODE = ?, MST_TERM_NAME = ? "
		    		//+ "MST_ANIP_MERCODE = ?, MST_ANIP_STORECODE = ?, MST_ANIP_TERMCODE = ? "
		    		+" WHERE MST_INST_SQID = ? AND MST_REFR_SQID = ? AND MST_STOR_SQID = ? AND MST_TERM_SQID = ? ");
			
			output = jdbcTemplate.update(updateQuery.toString(),
						 terminalBean.getTermCode(),
						 terminalBean.getTermProuctType()==null?null:Integer.parseInt(terminalBean.getTermProuctType()),
						 terminalBean.getTerminalRiskProfileIndc()==null||terminalBean.getTerminalRiskProfileIndc().equals("")?0:Integer.parseInt(terminalBean.getTerminalRiskProfileIndc()),
						 terminalBean.getTerminalRiskProfile(),
						 null,//-->brand,
						 terminalBean.getProcessingPlanName(),
						 null,//-->termProfileId,
						 terminalBean.getCardGroup()==null||terminalBean.getCardGroup().equals("")?0:terminalBean.getCardGroup(),
						 terminalBean.getEnableQRcode(),
						 terminalBean.getAppId(),
						 terminalBean.getAuthToken(),
						 terminalBean.getPos(),
						 terminalBean.getUseSameAsStoreAccountForTerm()==null||terminalBean.getUseSameAsStoreAccountForTerm().equals("")?0:terminalBean.getUseSameAsStoreAccountForTerm(),
						 terminalBean.getDftTrmlRcrngChrgePlnId()==null||terminalBean.getDftTrmlRcrngChrgePlnId().equals("")?null:Integer.parseInt(terminalBean.getDftTrmlRcrngChrgePlnId()),
						 terminalBean.getRollingReserveFundRequired(),
						 terminalBean.getRrfIndc(),
						 terminalBean.getDomestic(),
						 terminalBean.getInterNational(),
						 terminalBean.getDomesticFixedAmount(),
						 terminalBean.getInternationalFixedAmount(),
						 terminalBean.getDomesticHoldingDays(),
						 terminalBean.getInternationalHoldingDays(),
						 terminalBean.getSecurityDepositRecoveryRequired(),
						 terminalBean.getSdrIndc(),
						 terminalBean.getSecurityDepositRecoveryAmount(),
						 terminalBean.getSecurityDepositRecoveryDays(),
						 terminalBean.getTerminalPriceDetailsRequired(),
						 //terminalBean.getCterminalmccautoComplete(),
						 merchantOnboardBean.getCmccMerchantAutoComplete(),
						 terminalBean.getTerminalRecurringPlanWaiver(),
						 paymentMethod, 
						 //terminalBean.getAaniPayMercID(), terminalBean.getAaniPayStoreID(),
						 terminalBean.getAaniPayTermID(), terminalBean.getTerminalName(),
						 instId, merchantOnboardBean.getRefSqid(), storeBean.getStoreSqid(), terminalBean.getTermSqid());
			
			if(output != 0 && terminalBean.getUseSameAsStoreAccountForTerm().equals("1")) {
				
				int existtermacctornot = jdbcTemplate.queryForObject("SELECT NVL((SELECT COUNT(*) FROM MPS_SPOB_ACCT_TEMP WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_STOR_CODE = ? AND MSA_TERM_CODE = ? AND MSA_MAPG_LEVL = ?),0) FROM DUAL ", 
						Integer.class, merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(), storeBean.getStoreSqid(), terminalBean.getTermSqid(), terminalLevel);
				
				if(existtermacctornot > 0) {
					
					updateQuery1.append(" UPDATE MPS_SPOB_ACCT_TEMP SET MSA_DMAC_INDC=?, MSA_ACCT_CRCY=?, "
							+ " MSA_ACCT_NUMB=?, MSA_IFSC_CODE=?, MSA_ACCT_BRAN=?, MSA_ACCT_NAME=?, MSA_DEFT_ACCT=?, "
							+ " MSA_REC_STUS=?, MSA_BANK_NAME=?, MSA_BRCH_NAME=?, MSA_CIF_NMBR=?, MSA_STMT_FREQ=? "
							+ " WHERE MSA_REFR_SQID=? AND MSA_MERC_CODE = ? AND MSA_STOR_CODE = ? AND MSA_TERM_CODE = ? AND MSA_MAPG_LEVL=?");
														
					jdbcTemplate.update(updateQuery1.toString(), terminalBean.getTerminalacctWithBank(),
						terminalBean.getTerminalcurrency()!=null && terminalBean.getTerminalcurrency() != "" ? terminalBean.getTerminalcurrency().split("-")[0] : null,
						terminalBean.getTerminalacctNumber(), terminalBean.getTerminalifsc(), 
						terminalBean.getcTerminalacctBranch(), terminalBean.getTerminalaccountname(),
						terminalBean.getTerminaldefAccount(), ACTIVE, terminalBean.getTerminalacctBankText(), terminalBean.getTerminalacctBranchText(),
						terminalBean.getTerminalcifNumber(), terminalBean.getTerminalstmtfreq(),
						merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(), storeBean.getStoreSqid(), terminalBean.getTermSqid(), terminalLevel);
				} else {
					updateQuery1.append("INSERT INTO MPS_SPOB_ACCT_TEMP( MSA_REFR_SQID, MSA_DMAC_INDC, MSA_ACCT_CRCY, MSA_ACCT_NUMB, MSA_IFSC_CODE, ")
			            	    .append("MSA_ACCT_BRAN, MSA_MAPG_LEVL, MSA_ACCT_NAME, MSA_REC_STUS, MSA_MERC_CODE, MSA_STOR_CODE, MSA_TERM_CODE, MSA_DEFT_ACCT, ")
			            	    .append("MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ ")
			            	    .append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					
					jdbcTemplate.update(updateQuery1.toString(),	
							merchantOnboardBean.getRefSqid(), 
							terminalBean.getTerminalacctWithBank(), 
							terminalBean.getTerminalcurrency()!=null && terminalBean.getTerminalcurrency() != "" ? terminalBean.getTerminalcurrency().split("-")[0] : null,
							terminalBean.getTerminalacctNumber(), terminalBean.getTerminalifsc(), terminalBean.getcTerminalacctBranch(),
							terminalLevel, terminalBean.getTerminalaccountname(), ACTIVE, 
							merchantOnboardBean.getMid(), storeBean.getStoreSqid(), terminalBean.getTermSqid(),
							terminalBean.getTerminaldefAccount(), terminalBean.getTerminalacctBankText(), terminalBean.getTerminalacctBranchText(),
							terminalBean.getTerminalcifNumber(), terminalBean.getTerminalstmtfreq());
				}
				
			}else if(output != 0 && terminalBean.getUseSameAsStoreAccountForTerm().equals("0")) {
				jdbcTemplate.update("DELETE FROM MPS_SPOB_ACCT_TEMP WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_STOR_CODE = ? AND MSA_TERM_CODE = ? AND MSA_MAPG_LEVL = ? ", 
						merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(), storeBean.getStoreSqid(), terminalBean.getTermSqid(), terminalLevel);
			}
			
			//Terminal Pricing Details
			if(output !=0 && terminalBean.getTerminalPriceDetailsRequired().equals("1")){

				int existtermpriceornot = jdbcTemplate.queryForObject("SELECT NVL((SELECT COUNT(*) FROM MPS_SPOB_PRIC_TEMP WHERE MSP_REFR_SQID = ? AND MSP_ENTY_SQID = ?),0) FROM DUAL ", 
						Integer.class, terminalBean.getTermSqid(), terminalLevel);
				if(existtermpriceornot > 0) {
					
					updateQuery2.append(" UPDATE MPS_SPOB_PRIC_TEMP SET MSP_METY_SQID=?, MSP_FPLN_TYPE=?, MSP_FPLN_NAME=?, MSP_CAPT_MSFA=?, MSP_FPLN_SQID=?, "
							+ "	MSP_INTC_PLUS=?, MSP_REC_STUS=?, MSP_FEE_WAVR=? WHERE MSP_REFR_SQID=? AND MSP_ENTY_SQID=?");	
					jdbcTemplate.update(updateQuery2.toString(), 
							merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
							terminalBean.getTerminalFeePlanType(),
							terminalBean.getTerminalMerchantServiceFeePlan(), 
							terminalBean.getTerminalCaptureServiceFeeAt(),
							terminalBean.getTerminalMerchantServiceFeePlan()!=null && terminalBean.getTerminalMerchantServiceFeePlan() != "" ? terminalBean.getTerminalMerchantServiceFeePlan().split("-")[0] : null,
							0, ACTIVE, 
							//terminalBean.getTerminalMercFeeDeduWaiver(),
							Optional.ofNullable(terminalBean.getTerminalMercFeeDeduWaiver()).filter(v -> v != 0).orElse(0),
							terminalBean.getTermSqid(), terminalLevel);
				} else {
					updateQuery2.append("INSERT INTO MPS_SPOB_PRIC_TEMP(MSP_REFR_SQID,MSP_METY_SQID,MSP_FPLN_TYPE,MSP_FPLN_NAME,MSP_CAPT_MSFA,MSP_FPLN_SQID, ")
							.append(" MSP_INTC_PLUS,MSP_REC_STUS,MSP_FEE_WAVR,MSP_ENTY_SQID,MSP_MRCP_SQID ")
							.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?) ");
					jdbcTemplate.update(updateQuery2.toString(), 
							terminalBean.getTermSqid(),
							merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
							terminalBean.getTerminalFeePlanType(),
							terminalBean.getTerminalMerchantServiceFeePlan(), 
							terminalBean.getTerminalCaptureServiceFeeAt(),
							terminalBean.getTerminalMerchantServiceFeePlan()!=null && terminalBean.getTerminalMerchantServiceFeePlan() != "" ? terminalBean.getTerminalMerchantServiceFeePlan().split("-")[0] : null,
							0, ACTIVE, 
							//terminalBean.getTerminalMercFeeDeduWaiver(),
							Optional.ofNullable(terminalBean.getTerminalMercFeeDeduWaiver()).filter(v -> v != 0).orElse(0),
							terminalLevel,
							terminalBean.getDftTrmlRcrngChrgePlnId()==null||terminalBean.getDftTrmlRcrngChrgePlnId().equals("")?null:Integer.parseInt(terminalBean.getDftTrmlRcrngChrgePlnId()));
				}
				
			}else if(output !=0 && storeBean.getStorePriceDetailsRequired().equals("0")){
				jdbcTemplate.update("DELETE FROM MPS_SPOB_PRIC_TEMP WHERE MSP_REFR_SQID = ? AND MSP_ENTY_SQID = ? ", 
						storeBean.getStoreSqid(), terminalLevel);
			}
				
		}catch (Exception e) {
			loggerUtil.error("Error while updating in updateTerminalWithAccountDetails() "+e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		}finally {
			updateQuery = null;
			updateQuery1 = null;
			updateQuery2 = null;
			termAcctBranch = null;
			output = 0;
		}
		return merchantOnboardBean;
	}

	@Override
	public void errorUpdateForMerchantOnboard(MerchantOnboardBean merchantOnboardBean, Integer userId) {
		try
		{
			if(merchantOnboardBean.getRefSqid() != 0)
			{
				jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_REC_STUS=3, MSM_LUPD_USER=?, MSM_LUPD_INDT=SYSDATE "
								 + " WHERE MSM_REFR_SQID=? ", userId, merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("UPDATE MPS_SPOB_PRIC_TEMP SET MSP_REC_STUS=3 WHERE MSP_REFR_SQID=? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("UPDATE MPS_SPOB_STOR_TEMP SET MSS_REC_STUS=3 WHERE MSS_REFR_SQID=? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("UPDATE MPS_SPOB_TERM_TEMP SET MST_REC_STUS=3 WHERE MST_REFR_SQID=? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("UPDATE MPS_SPOB_ACCT_TEMP SET MSA_REC_STUS=3 WHERE MSA_REFR_SQID=? ", merchantOnboardBean.getRefSqid());
			}
		}catch(Exception e){
			loggerUtil.error("Error while rollback merchant details "+ e.getMessage());
		}
	}
	
	@Override
	public List<net.sf.json.JSONObject> mccautocomplete() throws Exception {
		List<net.sf.json.JSONObject> mcclist = null;
		StringBuilder strQry = new StringBuilder();

		try {
			strQry.append("SELECT MGV_MCC_CODE, CASE WHEN MGV_MCC_DESC IS NULL THEN MGV_MCC_CODE "
					+ " ELSE UPPER(MGV_MCC_DESC) END AS MGV_MCC_DESC FROM MPS_GENR_VMCC_MAST ");
			mcclist = (List<net.sf.json.JSONObject>) jdbcTemplate.query(strQry.toString(),
					new RowMapper<net.sf.json.JSONObject>() {
						public net.sf.json.JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
							net.sf.json.JSONObject json = new net.sf.json.JSONObject();
								json.put("value", rs.getString(1));
								json.put("desc", rs.getString(1) + " - " + rs.getString(2));
							return json;
						}
					});

		} catch (Exception e) {
			loggerUtil.error("Error while fetching MCC details "+ e.getMessage());
		}
		return mcclist;
	}
	@Override
	public List<net.sf.json.JSONObject> getCountryDetailsList(int instId) throws Exception {
		
		StringBuilder strQry = new StringBuilder(" SELECT MGC_CNRY_SQID, MGC_CNRY_NAME FROM MPS_GENR_CNRY_MAST WHERE MGC_CNRY_STUS = 1 ORDER BY MGC_CNRY_NAME ");

		return jdbcTemplate.query(strQry.toString(), new RowMapper<net.sf.json.JSONObject>() {
			@Override
			public net.sf.json.JSONObject mapRow(ResultSet rs, int arg1) throws SQLException {
				net.sf.json.JSONObject jsonObject =new net.sf.json.JSONObject();
				jsonObject.put("value", rs.getString("MGC_CNRY_SQID"));
				jsonObject.put("desc", (rs.getString("MGC_CNRY_NAME")).trim());
				return jsonObject;
			}
		});
	}
	@Override
	public List<net.sf.json.JSONObject> getStateDetailsList(String countrycode, int instId) throws Exception {
		List<net.sf.json.JSONObject> stateDetailsList = null;
		StringBuilder strQry = new StringBuilder();
		try {

			strQry.append(" SELECT MAS_STAT_SQID stateSeq ,MAS_STAT_NAME stateName FROM MPS_ADMN_STAT_MAST WHERE MAS_CNRY_SQID= ? ");
			strQry.append(" AND MAS_STAT_STUS = 1 AND MAS_INST_SQID = ? ORDER BY MAS_STAT_NAME ");

			stateDetailsList = (List<net.sf.json.JSONObject>) jdbcTemplate.query(strQry.toString(),
					new RowMapper<net.sf.json.JSONObject>() {
						public net.sf.json.JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
							net.sf.json.JSONObject json = new net.sf.json.JSONObject();
							json.put("value", rs.getString(1));
							json.put("desc", rs.getString(2).trim());
							return json;
						}
					}, countrycode, instId);

		} catch (Exception e) {
			loggerUtil.error("Error while fetching state details "+ e.getMessage());
		}
		return stateDetailsList;
	}
	@Override
	public List<net.sf.json.JSONObject> getCityDetailsList(String countrycode, String statecode, int instId) throws Exception {
		List<net.sf.json.JSONObject> cityDetailsList = null;
		StringBuilder strQry = new StringBuilder();
		try {

			strQry.append(" SELECT MAC_CITY_SQID cityseq, MAC_CITY_NAME cityName FROM MPS_ADMN_CITY_MAST ");
			strQry.append(" WHERE MAC_CNRY_SQID= ? AND MAC_STAT_SQID=? AND MAC_CITY_STUS= 1 AND MAC_INST_SQID =? ");
			strQry.append("	ORDER BY MAC_CITY_NAME ");

			cityDetailsList = (List<net.sf.json.JSONObject>) jdbcTemplate.query(strQry.toString(),
					new RowMapper<net.sf.json.JSONObject>() {
						public net.sf.json.JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
							net.sf.json.JSONObject json = new net.sf.json.JSONObject();
							json.put("value", rs.getString(1));
							json.put("desc", rs.getString(2).trim());
							return json;
						}
					}, countrycode, statecode, instId);

		} catch (Exception e) {
			loggerUtil.error("Error while fetching city details "+ e.getMessage());
		}
		return cityDetailsList;
	}
		
//	singlepage Edit details
	@Override
	public MerchantOnboardBean getMerchantDetails(MerchantOnboardBean merchantOnboardBean, Integer instId)
			throws Exception {
		StringBuffer strQry = new StringBuffer(
				"SELECT MSM_REFR_SQID, MSM_MERC_CODE, MSM_SALE_EXEC, "
				+ " NVL((SELECT MMS_SLEN_SQID FROM MPS_MERC_SLEN_DETL WHERE UPPER(MMS_SLEN_NAME) IN (UPPER(MSM_SALE_EXEC))), NULL) AS MMS_SLEN_SQID, "
				+ " MSM_LEGL_NAME, MSM_DBA_NAME, "
				+ " MSM_OWNR_NAME, MSM_AGGR_NAME, "
				+ " MSM_AGGR_SQID, "
				+ " NVL((SELECT MAG_MERC_RELM FROM MPS_ADMN_AGGR_MAST WHERE MAG_AGGR_SQID = MSM_AGGR_SQID), NULL) AS MAG_MERC_RELM, "
				+ " MSM_CUST_FNAM, MSM_CUST_LNAM, "
				+ " MSM_CUST_ADD1, MSM_CUST_ADD2, MSM_CUST_CNTY, MSM_CUST_STAT, "
				+ " MSM_CUST_CITY, MSM_CUST_PCOD, MSM_CUST_PHNO, MSM_CUST_EMAL, "
				+ " CX_DF_ED(MSM_PAN_UQID,?,?) AS MSM_PAN_UQID,MSM_ONBD_MTHD, "
				+ " CASE WHEN MSM_ACCT_MAPG_LEVL IS NULL THEN 1 ELSE MSM_ACCT_MAPG_LEVL END AS MSM_ACCT_MAPG_LEVL, "
				+ " MSM_EXST_CUST, MSM_MPOR_REQR, MSM_BUSN_EMAL, MSM_BUSN_MOBL, "
				+ " MSM_TECH_EMAL, MSM_TECH_MOBL, MSM_MERC_LOGO, MSM_ERR_DESC, MSM_STOR_FLAG,MSM_ACCT_FLAG, "
	            + " MSM_OWNR_TYPE, MSM_PRNT_ENTY, MSM_CNRY_INCP, MSM_DATE_INCP, MSM_UBO_PERC, "
	            + " MSM_INDV_NAME, MSM_BUSN_TYPE, MSM_EMRT_ID, MSM_EMRTID_EXPR, "
	            + " MSM_PASP_NMBR, MSM_PASP_EXPR, MSM_MERC_NATY, MSM_ANIP_MERCODE, MSM_SCDP_FLAG, "
	            + " (SELECT MGV_MCC_CODE||'-'||CASE WHEN MGV_MCC_DESC is null THEN MGV_MCC_CODE "
	    		+ " ELSE UPPER(MGV_MCC_DESC) END AS MSM_MCC_CODE FROM MPS_GENR_VMCC_MAST "
	    		+ "	WHERE MGV_MCC_CODE = MSM_MCC_CODE) AS MSM_MCC_CODE "
				+ " FROM MPS_SPOB_MERC_TEMP M "
				+ " WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID = ? ORDER BY MSM_REFR_SQID ASC ");

		jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
			public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
				  merchantOnboardBean.setMid(rs.getString("MSM_MERC_CODE"));
				  merchantOnboardBean.setBankersalesNames(rs.getString("MSM_SALE_EXEC"));
				  merchantOnboardBean.setBankerSalesSqid(rs.getString("MMS_SLEN_SQID"));
				  merchantOnboardBean.setLegalname(rs.getString("MSM_LEGL_NAME"));
				  merchantOnboardBean.setDbaname(rs.getString("MSM_DBA_NAME"));
				  merchantOnboardBean.setOwnername(rs.getString("MSM_OWNR_NAME"));
				  merchantOnboardBean.setMercAggr(rs.getString("MSM_AGGR_SQID"));
				  merchantOnboardBean.setMercAggrName(rs.getString("MSM_AGGR_NAME"));
				  merchantOnboardBean.setMercAggrrelm(rs.getString("MAG_MERC_RELM")==null?"":rs.getString("MAG_MERC_RELM"));
				  merchantOnboardBean.setFirstname1(rs.getString("MSM_CUST_FNAM"));
				  merchantOnboardBean.setLastname1(rs.getString("MSM_CUST_LNAM"));
				  merchantOnboardBean.setAddressline1(rs.getString("MSM_CUST_ADD1"));
				  merchantOnboardBean.setAddressline2(rs.getString("MSM_CUST_ADD2"));
				  merchantOnboardBean.setCountry(rs.getString("MSM_CUST_CNTY"));
				  merchantOnboardBean.setState(rs.getString("MSM_CUST_STAT"));
				  merchantOnboardBean.setSelectcity(rs.getString("MSM_CUST_CITY"));
				  merchantOnboardBean.setPostalcode(rs.getString("MSM_CUST_PCOD"));
				  merchantOnboardBean.setPhone(rs.getString("MSM_CUST_PHNO"));
				  merchantOnboardBean.setContactEmail(rs.getString("MSM_CUST_EMAL"));
				  merchantOnboardBean.setCpancard(rs.getString("MSM_PAN_UQID"));
				  merchantOnboardBean.setRefSqid(rs.getString("MSM_REFR_SQID")==null?null:Integer.parseInt(rs.getString("MSM_REFR_SQID")));
				  merchantOnboardBean.setOnboardMethod(rs.getString("MSM_ONBD_MTHD"));
				  merchantOnboardBean.setAcctMapLvl(rs.getInt("MSM_ACCT_MAPG_LEVL"));
				  merchantOnboardBean.setExistingCustomer(rs.getInt("MSM_EXST_CUST"));
				  merchantOnboardBean.setMerchantPortalRequired(rs.getInt("MSM_MPOR_REQR"));
				  merchantOnboardBean.setBusinessContactEmailID(rs.getString("MSM_BUSN_EMAL"));
				  merchantOnboardBean.setBusinessContactMobileNo(rs.getString("MSM_BUSN_MOBL")==null?null:rs.getLong("MSM_BUSN_MOBL"));
				  merchantOnboardBean.setTechnicalContactEmailID(rs.getString("MSM_TECH_EMAL"));
				  merchantOnboardBean.setTechnicalContactMobileNo(rs.getString("MSM_TECH_MOBL")==null?null:rs.getLong("MSM_TECH_MOBL"));
				  merchantOnboardBean.setMercLogoUpldData(rs.getBytes("MSM_MERC_LOGO"));
				  if(merchantOnboardBean.getMercLogoUpldData()!=null) {
					  merchantOnboardBean.setMercLogoDisplayData(Base64.getEncoder().encodeToString(merchantOnboardBean.getMercLogoUpldData())); 
				  }
				  merchantOnboardBean.setRemarks(rs.getString("MSM_ERR_DESC"));
				  merchantOnboardBean.setAdditionalNewStoreFlag(rs.getInt("MSM_STOR_FLAG"));
				  merchantOnboardBean.setMerchantAccountRequired(rs.getString("MSM_ACCT_FLAG")==null?null:rs.getInt("MSM_ACCT_FLAG"));
				  
				  merchantOnboardBean.setOwnerType(rs.getString("MSM_OWNR_TYPE")); 
				  merchantOnboardBean.setBusinessType(rs.getString("MSM_BUSN_TYPE")); 
				  merchantOnboardBean.setParentEntityName(rs.getString("MSM_PRNT_ENTY"));
				  merchantOnboardBean.setCountryOfIncorp(rs.getString("MSM_CNRY_INCP"));
				  merchantOnboardBean.setDateOfIncorp(rs.getString("MSM_DATE_INCP"));
				  if(merchantOnboardBean.getDateOfIncorp() != null && merchantOnboardBean.getDateOfIncorp().trim().length() != 0) {
					  try {
						  merchantOnboardBean.setDateOfIncorp(new SimpleDateFormat("dd/MM/yyyy")
								  .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(merchantOnboardBean.getDateOfIncorp())));
					  } catch (ParseException e) {
						  loggerUtil.error("Error while converting the Date format for Date of Incorporation - "+ e.getMessage());
					  }
		          }
				  merchantOnboardBean.setUboShareholderPctEntity(rs.getString("MSM_UBO_PERC"));
				  merchantOnboardBean.setIndividualName(rs.getString("MSM_INDV_NAME"));
				  merchantOnboardBean.setEmiratesIdNo(rs.getString("MSM_EMRT_ID"));
				  merchantOnboardBean.setEmiratesIdExpiry(rs.getString("MSM_EMRTID_EXPR"));
				  if(merchantOnboardBean.getEmiratesIdExpiry() != null && merchantOnboardBean.getEmiratesIdExpiry().trim().length() != 0) {
					  try {
						  merchantOnboardBean.setEmiratesIdExpiry(new SimpleDateFormat("dd/MM/yyyy")
								  .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(merchantOnboardBean.getEmiratesIdExpiry())));
					  } catch (ParseException e) {
						  loggerUtil.error("Error while converting the Date format for Emirates ID Expiry - "+ e.getMessage());
					  }
		          }
				  merchantOnboardBean.setPassportNumber(rs.getString("MSM_PASP_NMBR"));
				  merchantOnboardBean.setPassportExpiry(rs.getString("MSM_PASP_EXPR"));
				  if(merchantOnboardBean.getPassportExpiry() != null && merchantOnboardBean.getPassportExpiry().trim().length() != 0) {
					  try {
						  merchantOnboardBean.setPassportExpiry(new SimpleDateFormat("dd/MM/yyyy")
								  .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(merchantOnboardBean.getPassportExpiry())));
					  } catch (ParseException e) {
						  loggerUtil.error("Error while converting the Date format for Passport Expiry - "+ e.getMessage());
					  }
		          }
				  merchantOnboardBean.setNationality(rs.getString("MSM_MERC_NATY"));
				  merchantOnboardBean.setAaniPayMercID(rs.getString("MSM_ANIP_MERCODE"));
				  merchantOnboardBean.setSecurityDepositRecoveryRequired(rs.getInt("MSM_SCDP_FLAG"));
				  merchantOnboardBean.setMccmerchantautocomplete(rs.getString("MSM_MCC_CODE"));
				  merchantOnboardBean.setCmccMerchantAutoComplete(rs.getString("MSM_MCC_CODE")==null?null:rs.getString("MSM_MCC_CODE").split("-")[0]);
				  
				  return merchantOnboardBean;
			}
		}, instId, getClearDEK()!=null||!getClearDEK().isEmpty()?getClearDEK():null, instId, merchantOnboardBean.getRefNo());
		
		 String countryNamePattern = merchantOnboardBean.getCountry().trim()+"%";
		 String stateNamePattern = merchantOnboardBean.getState().trim()+"%";
		 String cityNamePattern = merchantOnboardBean.getSelectcity().trim()+"%";
		 merchantOnboardBean.setCcountry(jdbcTemplate.queryForObject("SELECT NVL((SELECT MGC_CNRY_SQID FROM MPS_GENR_CNRY_MAST WHERE MGC_CNRY_NAME LIKE ? ), 0) ",new Object[]{countryNamePattern}, String.class));
		 merchantOnboardBean.setCstate(jdbcTemplate.queryForObject("SELECT NVL((SELECT MAS_STAT_SQID FROM MPS_ADMN_STAT_MAST WHERE MAS_CNRY_SQID = ? AND MAS_STAT_NAME LIKE ? ), 0) ",new Object[] { merchantOnboardBean.getCcountry(), stateNamePattern}, String.class));
		 merchantOnboardBean.setCselectcity(jdbcTemplate.queryForObject("SELECT NVL((SELECT MAC_CITY_SQID FROM MPS_ADMN_CITY_MAST WHERE MAC_CNRY_SQID = ? AND MAC_STAT_SQID = ? AND MAC_CITY_NAME LIKE ? ), 0) ",new Object[] { merchantOnboardBean.getCcountry(), merchantOnboardBean.getCstate(), cityNamePattern}, String.class));
		 	
		 String entySqid = jdbcTemplate.queryForObject( " SELECT MSS_ENTY_SQID FROM MPS_SPOB_STOR_TEMP WHERE MSS_INST_SQID = ? AND MSS_REFR_SQID = ? AND ROWNUM = 1 ", String.class, instId, merchantOnboardBean.getRefNo());
		 merchantOnboardBean.setMerchantEntityName(Optional.ofNullable(entySqid).map(v -> Arrays.stream(v.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.joining(","))).orElse(null)); 

		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean getAccountDetails(MerchantOnboardBean merchantOnboardBean, Integer instId)
			throws Exception {
		if(merchantOnboardBean.getMerchantAccountRequired()==1) {
			StringBuffer strQry = new StringBuffer(
					"SELECT MSA_DMAC_INDC, MSA_ACCT_BRAN, MSA_IFSC_CODE, MSA_ACCT_NUMB, "
					+ " (SELECT MGC_CRCY_SQID||'-'||MGC_CRCY_DESC FROM MPS_GENR_CRCY_MAST "
					+ " WHERE MGC_CRCY_SQID = A.MSA_ACCT_CRCY) AS MSA_ACCT_CRCY, "
					+ " MSA_MAPG_LEVL, MSA_DEFT_ACCT, MSA_ACCT_NAME, MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ "
					+ " FROM MPS_SPOB_ACCT_TEMP A WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? "
					+ " AND MSA_MAPG_LEVL = ? ORDER BY MSA_REFR_SQID ASC ");
			jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
				public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
					merchantOnboardBean.setAcctWithBank(rs.getString("MSA_DMAC_INDC")==null?null:Integer.parseInt(rs.getString("MSA_DMAC_INDC")));
					if(merchantOnboardBean.getAcctWithBank()==1) {
						merchantOnboardBean.setcAcctBranch(rs.getString("MSA_ACCT_BRAN"));
						if(merchantOnboardBean.getcAcctBranch()!=null && merchantOnboardBean.getcAcctBranch().trim().length()!=0) {
							merchantOnboardBean.setAcctBranch(jdbcTemplate.queryForObject("SELECT MAM_MEBR_CODE || '-' || MAM_MEBR_NAME FROM MPS_ADMN_MEBR_MAST WHERE MAM_INST_SQID = ? AND MAM_MEBR_CODE = ? ", String.class , instId, merchantOnboardBean.getcAcctBranch()));
						}
					}
					merchantOnboardBean.setIfsc(rs.getString("MSA_IFSC_CODE"));
					merchantOnboardBean.setAcctNumber(rs.getString("MSA_ACCT_NUMB"));
					merchantOnboardBean.setCurrency(rs.getString("MSA_ACCT_CRCY"));
					merchantOnboardBean.setDefAccount(rs.getString("MSA_DEFT_ACCT")==null?null:Integer.parseInt(rs.getString("MSA_DEFT_ACCT")));
					merchantOnboardBean.setAccountname(rs.getString("MSA_ACCT_NAME"));
					merchantOnboardBean.setAcctBankText(rs.getString("MSA_BANK_NAME"));
					merchantOnboardBean.setAcctBranchText(rs.getString("MSA_BRCH_NAME"));
					merchantOnboardBean.setCifNumber(rs.getString("MSA_CIF_NMBR"));
					merchantOnboardBean.setStmtfreq(rs.getInt("MSA_STMT_FREQ"));
					
					return merchantOnboardBean;
				}
			}, merchantOnboardBean.getRefNo(), merchantOnboardBean.getMid(), merchantLevel);
		}
		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean getPricingDetails(MerchantOnboardBean merchantOnboardBean, Integer instId)
			throws Exception {
		
		StringBuffer strQry = new StringBuffer(" SELECT MSP_REFR_SQID, MSP_METY_SQID, MSP_FPLN_TYPE, MSP_FPLN_NAME, "
				+ " MSP_STOR_REQD, MSP_CAPT_MSFA, "
				+ " MSP_INTC_PLUS, MSP_FEE_WAVR FROM MPS_SPOB_PRIC_TEMP P WHERE MSP_REFR_SQID = ?");// AND MSP_ENTY_SQID = ?");
		
		jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
			public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
				merchantOnboardBean.setMerchantType(rs.getString("MSP_METY_SQID"));
				merchantOnboardBean.setFeePlanType(rs.getString("MSP_FPLN_TYPE")==null?null:Integer.parseInt(rs.getString("MSP_FPLN_TYPE"))); // added after deleting the column in merc_temp - MSM_MSVF_PLTY
				merchantOnboardBean.setMerchantServiceFeePlan(rs.getString("MSP_FPLN_NAME"));
				merchantOnboardBean.setNoOfStoresReq(rs.getString("MSP_STOR_REQD")==null?null:Integer.parseInt(rs.getString("MSP_STOR_REQD")));
				merchantOnboardBean.setCaptureServiceFeeAt(rs.getString("MSP_CAPT_MSFA"));
				merchantOnboardBean.setIntchgPlus(rs.getString("MSP_INTC_PLUS"));
				merchantOnboardBean.setMercFeeDeduWaiver(rs.getInt("MSP_FEE_WAVR"));
				
				return merchantOnboardBean;
			}
		},merchantOnboardBean.getRefNo());//, merchantLevel);
		
		if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 1) {
			StringBuffer strQry1 = new StringBuffer(" SELECT MMS_DEPS_LIMT, MMS_CALC_TYPE, MMS_INDP_AMNT, MMS_INDP_PERC FROM MPS_SPOB_SCDP_TEMP WHERE MMS_REFR_SQID=? AND MMS_ACTIVE_FLAG = ?");
			
			jdbcTemplate.query(strQry1.toString(), new RowMapper<MerchantOnboardBean>() {
				public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
					merchantOnboardBean.setSecurityDepositLimit(rs.getInt("MMS_DEPS_LIMT"));
					//merchantOnboardBean.setSecurityDepositCalcType(rs.getString("MMS_CALC_TYPE"));
					merchantOnboardBean.setSdrIndc(rs.getString("MMS_CALC_TYPE").equals("F") ? 0 : 1);
					merchantOnboardBean.setSecurityDepositRecoveryAmount(rs.getLong("MMS_INDP_AMNT"));
					merchantOnboardBean.setSecurityDepositRecoveryPercentage(rs.getInt("MMS_INDP_PERC"));
					
					return merchantOnboardBean;
				}
			},merchantOnboardBean.getRefNo(), ACTIVE);
		} else if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 0) {
			StringBuffer strQry1 = new StringBuffer(" SELECT MMS_DEPS_LIMT FROM MPS_SPOB_SCDP_TEMP WHERE MMS_REFR_SQID=? AND MMS_ACTIVE_FLAG = ?");
			
			jdbcTemplate.query(strQry1.toString(), new RowMapper<MerchantOnboardBean>() {
				public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
					merchantOnboardBean.setSecurityDepositLimit(rs.getInt("MMS_DEPS_LIMT"));
					return merchantOnboardBean;
				}
			},merchantOnboardBean.getRefNo(), ACTIVE);
		}

		return merchantOnboardBean;
	}
	
	@Override
	public List<MerchantOnboardStoreBean> getAllStoreAndTerminalDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) throws Exception {
		List<MerchantOnboardStoreBean> ListStoreBeans = null;
		
		StringBuffer strQry = new StringBuffer(
				"SELECT MSS_STOR_CODE,MSS_STOR_SQID,MSS_STTD_NAME, MSS_MERC_GRAD, MSS_MVV_FIELD, "
				+ "(SELECT MGV_MCC_CODE||'-'||CASE WHEN MGV_MCC_DESC is null THEN MGV_MCC_CODE "
				+ " ELSE UPPER(MGV_MCC_DESC) END AS MSS_MCC_CODE FROM MPS_GENR_VMCC_MAST "
				+ "	WHERE MGV_MCC_CODE = S.MSS_MCC_CODE) AS MSS_MCC_CODE, "
				+ " MSS_GST_NUMR,MSS_GST_FLAG, "
				+ " MSS_CRPT_SQID,"
				+ " MSS_GEO_CORD, MSS_GEO_LATD, MSS_GEO_CPER, MSS_LINE_RCPT1, MSS_LINE_RCPT2, MSS_LINE_RCPT3,"
				+ " MSS_ENTY_SQID, MMS_WBST_NAME, MMS_WEBS_ADDR,MSS_INCR_STUS,"
				+ " MSS_PROF_DOCM,MSS_EMAIL_ADID,MSS_TERM_FLAG, "
				+ " (SELECT MNB_NABU_STUS||'~'||MNB_NATR_SQID FROM MPS_NATR_BUSN_MAST WHERE MNB_INST_SQID= MSS_INST_SQID AND MNB_NATR_SQID = MSS_NATR_BUSN) AS MSS_NATR_BUSN, "
				+ " MSS_ACCT_FLAG,MSS_ADDR_FLAG,MSS_CUST_ADD1,MSS_CUST_ADD2,"
				+ " CASE WHEN S.MSS_CUST_CNTY IS NULL THEN NULL ELSE (SELECT MGC_CNRY_SQID||'-'||MGC_CNRY_NAME FROM MPS_GENR_CNRY_MAST WHERE MGC_CNRY_NAME LIKE S.MSS_CUST_CNTY || '%' AND MGC_CNRY_STUS = 1) END AS MSS_CUST_CNTY,"
				+ " CASE WHEN S.MSS_CUST_STAT IS NULL THEN NULL ELSE (SELECT MAS_STAT_SQID ||'-'|| MAS_STAT_NAME FROM MPS_ADMN_STAT_MAST WHERE MAS_CNRY_SQID IN "
				+ "    (SELECT MGC_CNRY_SQID FROM MPS_GENR_CNRY_MAST WHERE MGC_CNRY_NAME LIKE S.MSS_CUST_CNTY || '%') AND MAS_STAT_NAME LIKE S.MSS_CUST_STAT || '%' AND MAS_STAT_STUS = 1) END AS MSS_CUST_STAT,  "
				+ " CASE WHEN S.MSS_CUST_STAT IS NULL THEN NULL ELSE (SELECT MAC_CITY_SQID ||'-'|| MAC_CITY_NAME FROM MPS_ADMN_CITY_MAST WHERE MAC_CITY_NAME = S.MSS_CUST_CITY AND MAC_CNRY_SQID IN "
				+ "    (SELECT MGC_CNRY_SQID FROM MPS_GENR_CNRY_MAST WHERE MGC_CNRY_NAME LIKE S.MSS_CUST_CNTY || '%') AND MAC_STAT_SQID IN "
				+ "    (SELECT MAS_STAT_SQID FROM MPS_ADMN_STAT_MAST WHERE MAS_STAT_NAME LIKE S.MSS_CUST_STAT || '%') AND MAC_CITY_STUS= 1) END AS MSS_CUST_CITY, "
				+ " MSS_CUST_PCOD,MSS_MERC_RELM,MSS_GOVT_REGMER,MSS_HOME_CRCYID,MSS_MERC_MVV, "
				+ " MSS_BUSN_EMAL, MSS_BUSN_MOBL, MSS_TECH_EMAL, MSS_TECH_MOBL, "
				+ " MSS_RISK_REQD, MSS_RSCG_SQID, MSS_PCI_EXPY, MSS_CLUD_EXPY, MSS_TRAD_EXPY, "
		      	+ " MSS_MTHY_TCNT, MSS_AVTS_AMNT, MSS_QSAL_AMNT, MSS_TXNS_REFD_PERC, MSS_TXNS_CHBK_PERC, MSS_TXNS_PERC, MSS_TXNS_INT_PERC,"
		      	+ " MSS_CYBS_MID, MSS_CYBS_PWD, MSS_CYBS_MKEY, MSS_PYMT_FREQ_CARD, "
		      	+ " MSS_FREQ_DAYS_CARD, MSS_PYMT_FREQ_AANI, MSS_TXNS_PYMT, MSS_FREQ_DAYS_AANI, MSS_PRIC_REQD, MSS_STOR_TYPE, MSS_ANIP_STORCODE "
				+ " FROM MPS_SPOB_STOR_TEMP S WHERE MSS_INST_SQID = ? AND MSS_REFR_SQID = ? "
		//		+ " AND MSS_REC_STUS NOT IN (5, 7) "
				+ " ORDER BY MSS_STOR_SQID ASC ");

				ListStoreBeans = jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardStoreBean>() {
					public MerchantOnboardStoreBean mapRow(ResultSet rs, int arg1) throws SQLException {
						MerchantOnboardStoreBean storeBean = new MerchantOnboardStoreBean();
						storeBean.setStoreCode(rs.getString("MSS_STOR_CODE"));
						storeBean.setStoreSqid(rs.getString("MSS_STOR_SQID")==null?null:Integer.parseInt(rs.getString("MSS_STOR_SQID")));
						storeBean.setStoreTradingName(rs.getString("MSS_STTD_NAME"));
						storeBean.setMerchantGrade(rs.getString("MSS_MERC_GRAD")==null?"":rs.getString("MSS_MERC_GRAD"));
						storeBean.setMvv(rs.getString("MSS_MVV_FIELD"));
						storeBean.setMccautocomplete(rs.getString("MSS_MCC_CODE"));
						storeBean.setCmccAutoComplete(rs.getString("MSS_MCC_CODE")==null?null:rs.getString("MSS_MCC_CODE").split("-")[0]);
						storeBean.setGstNumber(rs.getString("MSS_GST_NUMR"));
						storeBean.setGstFlag(rs.getString("MSS_GST_FLAG"));
						storeBean.setWebsiteAddress(rs.getString("MMS_WEBS_ADDR"));
						storeBean.setWebSiteName(rs.getString("MMS_WBST_NAME"));
						storeBean.setIncorpstatus(rs.getString("MSS_INCR_STUS"));
						//storeBean.setMerchantEntityName(rs.getString("MSS_ENTY_SQID") != null ? rs.getString("MSS_ENTY_SQID") : null);
						//storeBean.setMerchantEntityName(Optional.ofNullable(rs.getString("MSS_ENTY_SQID")).map(v -> Arrays.stream(v.split(",")).map(String::trim).collect(Collectors.joining(","))).orElse(null));
						merchantOnboardBean.setMerchantEntityName(Optional.ofNullable(rs.getString("MSS_ENTY_SQID")).map(v -> Arrays.stream(v.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.joining(","))).orElse(null));
						storeBean.setPrfDocsProv(rs.getString("MSS_PROF_DOCM") != null ? rs.getString("MSS_PROF_DOCM") : null);
						storeBean.setStoreEmail(rs.getString("MSS_EMAIL_ADID"));
						storeBean.setTermneed(rs.getString("MSS_TERM_FLAG") != null && rs.getString("MSS_TERM_FLAG") != ""  ? rs.getString("MSS_TERM_FLAG") : "0");
						storeBean.setCryptoMethod(rs.getString("MSS_CRPT_SQID"));
						storeBean.setGeoCoordinates(rs.getString("MSS_GEO_CORD") != null && rs.getString("MSS_GEO_CORD") != ""  ? rs.getString("MSS_GEO_CORD") : "0");
						storeBean.setLatitude(rs.getString("MSS_GEO_LATD"));
						storeBean.setLongitude(rs.getString("MSS_GEO_CPER"));
						storeBean.setLine1Rec(rs.getString("MSS_LINE_RCPT1")!=null && rs.getString("MSS_LINE_RCPT1")!= "" ? rs.getString("MSS_LINE_RCPT1") : "");
						storeBean.setLine2Rec(rs.getString("MSS_LINE_RCPT2")!=null && rs.getString("MSS_LINE_RCPT2")!= "" ? rs.getString("MSS_LINE_RCPT2") : "");
						storeBean.setLine3Rec(rs.getString("MSS_LINE_RCPT3")!=null && rs.getString("MSS_LINE_RCPT3")!= "" ? rs.getString("MSS_LINE_RCPT3") : "");
						storeBean.setNatureOfBusiness(rs.getString("MSS_NATR_BUSN"));
						storeBean.setUseSameAsMercAccountForStore(rs.getString("MSS_ACCT_FLAG"));
						storeBean.setUseSameAsMercAddressForStore(rs.getString("MSS_ADDR_FLAG"));
						storeBean.setStoreaddressline1(rs.getString("MSS_CUST_ADD1"));
						storeBean.setStoreaddressline2(rs.getString("MSS_CUST_ADD2"));
						storeBean.setStoreccountry(rs.getString("MSS_CUST_CNTY")==null?null:rs.getString("MSS_CUST_CNTY").split("-")[0]);
						storeBean.setStorecstate(rs.getString("MSS_CUST_STAT")==null?null:rs.getString("MSS_CUST_STAT").split("-")[0]);
						storeBean.setStorecselectcity(rs.getString("MSS_CUST_CITY")==null?null:rs.getString("MSS_CUST_CITY").split("-")[0]);
						storeBean.setStorecountry(rs.getString("MSS_CUST_CNTY")==null?null:rs.getString("MSS_CUST_CNTY").split("-")[1]);
						storeBean.setStorestate(rs.getString("MSS_CUST_STAT")==null?null:rs.getString("MSS_CUST_STAT").split("-")[1]);
						storeBean.setStoreselectcity(rs.getString("MSS_CUST_CITY")==null?null:rs.getString("MSS_CUST_CITY").split("-")[1]);
						storeBean.setStorepostalcode(rs.getString("MSS_CUST_PCOD"));
						storeBean.setRelatonShipManager(rs.getString("MSS_MERC_RELM"));
						storeBean.setGovtRegMerchant(rs.getString("MSS_GOVT_REGMER"));
						storeBean.setHomeCountryId(rs.getString("MSS_HOME_CRCYID"));//MCO
						storeBean.setMvv(rs.getString("MSS_MERC_MVV"));
						storeBean.setStorebusinessContactEmailID(rs.getString("MSS_BUSN_EMAL"));
						storeBean.setStorebusinessContactMobileNo(rs.getString("MSS_BUSN_MOBL")==null?null:rs.getLong("MSS_BUSN_MOBL"));
						storeBean.setStoretechnicalContactEmailID(rs.getString("MSS_TECH_EMAL"));
						storeBean.setStoretechnicalContactMobileNo(rs.getString("MSS_TECH_MOBL")==null?null:rs.getLong("MSS_TECH_MOBL"));
						storeBean.setStoreRiskDetailsRequired(rs.getString("MSS_RISK_REQD"));
						storeBean.setRiskCategory(rs.getString("MSS_RSCG_SQID"));
						storeBean.setPadssPCIdssCertExpDate(rs.getString("MSS_PCI_EXPY"));
						storeBean.setCloudCertExpDate(rs.getString("MSS_CLUD_EXPY"));
						storeBean.setTradeLicenseExpDate(rs.getString("MSS_TRAD_EXPY"));
						storeBean.setTransCountperMonth(rs.getString("MSS_MTHY_TCNT")==null?null:rs.getInt("MSS_MTHY_TCNT"));
						storeBean.setAverageTicketSize(rs.getString("MSS_AVTS_AMNT")==null?null:rs.getInt("MSS_AVTS_AMNT"));
						storeBean.setQuarterlySale(rs.getString("MSS_QSAL_AMNT")==null?null:rs.getInt("MSS_QSAL_AMNT"));
						storeBean.setTransToRefundPercent(rs.getString("MSS_TXNS_REFD_PERC")==null?null:rs.getInt("MSS_TXNS_REFD_PERC"));
						storeBean.setTransToChargebackPercent(rs.getString("MSS_TXNS_CHBK_PERC")==null?null:rs.getInt("MSS_TXNS_CHBK_PERC"));
						storeBean.setTransPercent(rs.getString("MSS_TXNS_PERC")==null?null:rs.getInt("MSS_TXNS_PERC"));
			            storeBean.setInternationalTransPercent(rs.getString("MSS_TXNS_INT_PERC")==null?null:rs.getInt("MSS_TXNS_INT_PERC"));
			            storeBean.setCyberMercID(rs.getString("MSS_CYBS_MID"));
			            storeBean.setCyberMercPass(rs.getString("MSS_CYBS_PWD"));
			            storeBean.setCyberMercKey(rs.getString("MSS_CYBS_MKEY"));
			            storeBean.setPaymentFrecForCard(rs.getInt("MSS_PYMT_FREQ_CARD"));
			            storeBean.setPaymentFrecForCardDays(rs.getInt("MSS_FREQ_DAYS_CARD"));
			            storeBean.setAaniPayTransPayment(rs.getInt("MSS_TXNS_PYMT"));
			            storeBean.setPaymentFrecForAaniPay(rs.getInt("MSS_PYMT_FREQ_AANI"));
			            storeBean.setPaymentFrecForAaniPayDays(rs.getInt("MSS_FREQ_DAYS_AANI"));
			            storeBean.setStorePriceDetailsRequired(rs.getString("MSS_PRIC_REQD"));
			            storeBean.setStoreType(rs.getInt("MSS_STOR_TYPE"));
			            //storeBean.setMerchantEntityNameList(getMerchantEntityNameList(instId, storeBean.getMerchantEntityName()));
			            storeBean.setAaniPayStoreID(rs.getString("MSS_ANIP_STORCODE"));
			            storeBean.setPrfDocsProvList(getProofOfDocumentList(instId, storeBean.getNatureOfBusiness().split("~")[1]));
						
			            if(storeBean.getPadssPCIdssCertExpDate() != null && storeBean.getPadssPCIdssCertExpDate().trim().length() != 0) {
			            	try {
								storeBean.setPadssPCIdssCertExpDate(new SimpleDateFormat("dd/MM/yyyy")
								         .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(storeBean.getPadssPCIdssCertExpDate())));
							} catch (ParseException e) {
								loggerUtil.error("Error while converting the Date format for PadssPCIdssCertExpDate - "+ e.getMessage());
							}
			            }
			            if(storeBean.getCloudCertExpDate() != null && storeBean.getCloudCertExpDate().trim().length() != 0) {
			            	try {
								storeBean.setCloudCertExpDate(new SimpleDateFormat("dd/MM/yyyy")
								         .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(storeBean.getCloudCertExpDate())));
							} catch (ParseException e) {
								loggerUtil.error("Error while converting the Date format for CloudCertExpDate - "+ e.getMessage());
							}
			            }
			            if(storeBean.getTradeLicenseExpDate() != null && storeBean.getTradeLicenseExpDate().trim().length() != 0) {
			            	try {
								storeBean.setTradeLicenseExpDate(new SimpleDateFormat("dd/MM/yyyy")
								         .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(storeBean.getTradeLicenseExpDate())));
							} catch (ParseException e) {
								loggerUtil.error("Error while converting the Date format for TradeLicenseExpDate - "+ e.getMessage());
							}
			            }
						if(storeBean.getUseSameAsMercAccountForStore() != null && storeBean.getUseSameAsMercAccountForStore().equals("1")) {
							storeBean = getStoreAccountDetails(storeBean, merchantOnboardBean.getRefNo(), merchantOnboardBean.getMid());
						}
						
						if(storeBean.getStorePriceDetailsRequired() != null && storeBean.getStorePriceDetailsRequired().equals("1")) {
							storeBean = getStorePriceDetails(storeBean);
						}
						
						int udfCount = 0;
						
						for(byte i=1; i<=3; i++) {
								
							if(i==1) {
								udfCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=? AND MAU_UDF_STUS=?", 
										Integer.class, instId, storeBean.getStoreSqid(), storeLevel, 1, ACTIVE);
								if(udfCount>0) {
									String udfData = jdbcTemplate.queryForObject("SELECT MAU_UDF_CAPTN || '-' || MAU_UDF_DATA FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? "
											+ "	AND MAU_CAPT_LEVL=? AND MAU_UDF_STUS=? AND MAU_FILD_ID=?",String.class ,instId, storeBean.getStoreSqid(), storeLevel, ACTIVE, 1);
									storeBean.setAdditionalUDFField1(udfData.split("-")[0]);
									storeBean.setAdditionalUDFValue1(udfData.split("-")[1]);
								}
							}else if(i==2) {
								udfCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=? AND MAU_UDF_STUS=?", 
										Integer.class, instId, storeBean.getStoreSqid(), storeLevel, 2, ACTIVE);
								if(udfCount>0) {
									String udfData = jdbcTemplate.queryForObject("SELECT MAU_UDF_CAPTN || '-' || MAU_UDF_DATA FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? "
											+ "	AND MAU_CAPT_LEVL=? AND MAU_UDF_STUS=? AND MAU_FILD_ID=?",String.class ,instId, storeBean.getStoreSqid(), storeLevel, ACTIVE, 2);
									storeBean.setAdditionalUDFField2(udfData.split("-")[0]);
									storeBean.setAdditionalUDFValue2(udfData.split("-")[1]);
								}
							}else if(i==3) {
								udfCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? AND MAU_CAPT_LEVL=? AND MAU_FILD_ID=? AND MAU_UDF_STUS=?", 
										Integer.class, instId, storeBean.getStoreSqid(), storeLevel, 3, ACTIVE);
								if(udfCount>0) {
									String udfData = jdbcTemplate.queryForObject("SELECT MAU_UDF_CAPTN || '-' || MAU_UDF_DATA FROM MPS_ADMN_UDF_TEMP WHERE MAU_INST_SQID=? AND MAU_REFR_SQID=? "
											+ "	AND MAU_CAPT_LEVL=? AND MAU_UDF_STUS=? AND MAU_FILD_ID=?",String.class ,instId, storeBean.getStoreSqid(), storeLevel, ACTIVE, 3);
									storeBean.setAdditionalUDFField3(udfData.split("-")[0]);
									storeBean.setAdditionalUDFValue3(udfData.split("-")[1]);
								}
							}
						}
						
						if(storeBean.getTermneed() != null && storeBean.getTermneed().equals("1")) {
							try {
								storeBean.setLstTerminalBean(getAllTerminalDetails(storeBean, merchantOnboardBean.getMid(), merchantOnboardBean.getRefNo(), instId));
							} catch (Exception e) {
								loggerUtil.error("Error while fetching the Terminal Details - "+ e.getMessage());
							}
						}
						
						if(storeBean.getGstFlag() == null || storeBean.getGstFlag().length() == 0) {
							storeBean.setGstFlag("0");
						}
						if(storeBean.getGovtRegMerchant() == null || storeBean.getGovtRegMerchant().length() == 0) {
							storeBean.setGovtRegMerchant("0");
						}
						if(storeBean.getGeoCoordinates() == null || storeBean.getGeoCoordinates().length() == 0) {
							storeBean.setGeoCoordinates("0");
						}
						if(storeBean.getLine1Rec() == null || storeBean.getLine1Rec().length() == 0 || storeBean.getLine1Rec().equals("0")) {
							storeBean.setLine1Rec(null);
						}
						if(storeBean.getLine2Rec() == null || storeBean.getLine2Rec().length() == 0 || storeBean.getLine2Rec().equals("0")) {
							storeBean.setLine2Rec(null);
						}
						if(storeBean.getLine3Rec() == null || storeBean.getLine3Rec().length() == 0 || storeBean.getLine3Rec().equals("0")) {
							storeBean.setLine3Rec(null);
						}
						if(storeBean.getUseSameAsMercAddressForStore() == null || storeBean.getUseSameAsMercAddressForStore().length() == 0) {
							storeBean.setUseSameAsMercAddressForStore("0");
						}
						if(storeBean.getUseSameAsMercAccountForStore() == null || storeBean.getUseSameAsMercAccountForStore().length() == 0) {
							storeBean.setUseSameAsMercAccountForStore("0");
						}
						if(storeBean.getStorePriceDetailsRequired() == null || storeBean.getStorePriceDetailsRequired().length() == 0) {
							storeBean.setStorePriceDetailsRequired("0");
						}
						if(storeBean.getStoreRiskDetailsRequired() == null || storeBean.getStoreRiskDetailsRequired().length() == 0) {
							storeBean.setStoreRiskDetailsRequired("0");
						}
						if(storeBean.getStoreType() == null || storeBean.getStoreType()+"".length() == 0) {
							storeBean.setStoreType(1);
						}
						return storeBean;
					}

				}, instId, merchantOnboardBean.getRefNo());

		return ListStoreBeans;
	}
	
	private MerchantOnboardStoreBean getStoreAccountDetails(MerchantOnboardStoreBean storeBean, String refNo, String mid) {
		StringBuffer strQry = new StringBuffer(
				"SELECT MSA_DMAC_INDC, MSA_ACCT_BRAN, MSA_IFSC_CODE, MSA_ACCT_NUMB, "
				+ " (SELECT MGC_CRCY_SQID||'-'||MGC_CRCY_DESC FROM MPS_GENR_CRCY_MAST "
				+ " WHERE MGC_CRCY_SQID = A.MSA_ACCT_CRCY) AS MSA_ACCT_CRCY, "
				+ " MSA_ACCT_NAME, MSA_DEFT_ACCT, MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ FROM MPS_SPOB_ACCT_TEMP A "
				+ " WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_STOR_CODE = ? "
				+ " AND MSA_MAPG_LEVL = ? ORDER BY MSA_REFR_SQID ASC ");
		jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardStoreBean>() {
			public MerchantOnboardStoreBean mapRow(ResultSet rs, int arg1) throws SQLException {
				storeBean.setStoreacctWithBank(rs.getString("MSA_DMAC_INDC")==null?null:Integer.parseInt(rs.getString("MSA_DMAC_INDC")));
				if(storeBean.getStoreacctWithBank()==1) {
					storeBean.setcStoreacctBranch(rs.getString("MSA_ACCT_BRAN"));
					if(storeBean.getcStoreacctBranch()!=null && storeBean.getcStoreacctBranch().trim().length()!=0) {
						storeBean.setStoreacctBranch(jdbcTemplate.queryForObject("SELECT MAM_MEBR_CODE || '-' || MAM_MEBR_NAME FROM MPS_ADMN_MEBR_MAST WHERE MAM_MEBR_CODE = ? ",String.class, storeBean.getcStoreacctBranch()));
					}
				}
				storeBean.setStoreifsc(rs.getString("MSA_IFSC_CODE"));
				storeBean.setStoreacctNumber(rs.getString("MSA_ACCT_NUMB"));
				storeBean.setStorecurrency(rs.getString("MSA_ACCT_CRCY"));
				storeBean.setStoreaccountname(rs.getString("MSA_ACCT_NAME"));
				storeBean.setStoredefAccount(rs.getString("MSA_DEFT_ACCT")==null?0:rs.getInt("MSA_DEFT_ACCT"));
				storeBean.setStoreacctBankText(rs.getString("MSA_BANK_NAME"));
				storeBean.setStoreacctBranchText(rs.getString("MSA_BRCH_NAME"));
				storeBean.setStorecifNumber(rs.getString("MSA_CIF_NMBR"));
				storeBean.setStorestmtfreq(rs.getInt("MSA_STMT_FREQ"));
				
				return storeBean;
			}
		}, refNo, mid, storeBean.getStoreSqid(), storeLevel);
		
		return storeBean;
	}
	
	private MerchantOnboardStoreBean getStorePriceDetails(MerchantOnboardStoreBean storeBean) {
						
		StringBuffer strQry = new StringBuffer(" SELECT MSP_REFR_SQID, MSP_FPLN_TYPE, MSP_FPLN_NAME, MSP_CAPT_MSFA, MSP_FEE_WAVR "
				+ " FROM MPS_SPOB_PRIC_TEMP P WHERE MSP_REFR_SQID = ? AND MSP_ENTY_SQID = ?");
		
		jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardStoreBean>() {
			public MerchantOnboardStoreBean mapRow(ResultSet rs, int arg1) throws SQLException {
				storeBean.setStoreFeePlanType(rs.getString("MSP_FPLN_TYPE")==null?null:Integer.parseInt(rs.getString("MSP_FPLN_TYPE"))); // added after deleting the column in merc_temp - MSM_MSVF_PLTY
				storeBean.setStoreMerchantServiceFeePlan(rs.getString("MSP_FPLN_NAME"));
				storeBean.setStoreCaptureServiceFeeAt(rs.getString("MSP_CAPT_MSFA"));
				storeBean.setStoreMercFeeDeduWaiver(rs.getInt("MSP_FEE_WAVR"));
				
				return storeBean;
			}
		},storeBean.getStoreSqid(), storeLevel);
		
		return storeBean;
	}
	
	public List<MerchantOnboardTerminalBean> getAllTerminalDetails(MerchantOnboardStoreBean storeBean, String mid, String refNo, Integer instId) throws Exception {
		
		StringBuffer strQry = new StringBuffer(" SELECT MST_TERM_SQID,MST_TMPD_SQID,MST_TERM_TID,MST_BRAND_SQID,MST_PLAN_NAME,MST_CARD_GRUP, "
											 + " MST_POS_NUM,MST_ENQR_CODE,MST_APP_ID,MST_AUTH_TOK,MST_RISK_NAME, "
											 + " MST_RISK_FLAG,MST_TPRF_SQID,MST_ACCT_FLAG,"
											 + " (SELECT MRP_RCPL_DESC FROM MPS_RCHG_PLAN_MAST WHERE MRP_RCPL_CATG ='3' AND MRP_RCPL_SQID= MST_MRCP_SQID) AS MST_MRCP_NAME, "
											 + " MST_MRCP_SQID, "
											 + " MST_RRF_REQD, MST_RRF_INDC, MST_DOM_REC_PERC, MST_INT_REC_PERC, "
											 + " MST_DOM_FIXD_AMT, MST_INT_FIXD_AMT, MST_DOM_HDAY, MST_INT_HDAY, "
											 + " MST_SD_RECV_REQD, MST_SD_INDC, MST_SD_PERC_AMT, MST_SD_NOOF_DAYS, MST_PRIC_REQD, "
											 + " (SELECT MGV_MCC_CODE||'-'||CASE WHEN MGV_MCC_DESC is null THEN MGV_MCC_CODE "
											 + " ELSE UPPER(MGV_MCC_DESC) END AS MST_MCC_CODE FROM MPS_GENR_VMCC_MAST "
											 + " WHERE MGV_MCC_CODE = T.MST_MCC_CODE) AS MST_MCC_CODE, "
											 + " MST_RRF_REQD, MST_RRF_INDC, MST_DOM_REC_PERC, MST_INT_REC_PERC, "
											 + " MST_DOM_FIXD_AMT, MST_INT_FIXD_AMT, MST_DOM_HDAY, MST_INT_HDAY, "
											 + " MST_SD_RECV_REQD, MST_SD_INDC, MST_SD_PERC_AMT, MST_SD_NOOF_DAYS, "
											 + " MST_PRIC_REQD, MST_MCC_CODE, MST_RECR_FEE_WAVR, MST_PYMT_METD, MST_ANIP_TERMCODE, MST_TERM_NAME "
											 //+ " MST_ANIP_MERCODE, MST_ANIP_STORECODE, MST_ANIP_TERMCODE "
											 + " FROM MPS_SPOB_TERM_TEMP T WHERE MST_INST_SQID = ? AND MST_REFR_SQID = ? "
											 + " AND MST_STOR_SQID = ? AND MST_REC_STUS != 5 ");
		List<MerchantOnboardTerminalBean> list = jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardTerminalBean>() {
			public MerchantOnboardTerminalBean mapRow(ResultSet rs, int arg1) throws SQLException {
				MerchantOnboardTerminalBean terminalBean = new MerchantOnboardTerminalBean();
				terminalBean.setTermSqid(rs.getString("MST_TERM_SQID")==null?null:Integer.parseInt(rs.getString("MST_TERM_SQID")));
				terminalBean.setTermProuctType(rs.getString("MST_TMPD_SQID"));
				terminalBean.setTermCode(rs.getString("MST_TERM_TID"));
				terminalBean.setBrand(rs.getString("MST_BRAND_SQID") != null ? rs.getString("MST_BRAND_SQID").split(",") : null);
				terminalBean.setProcessingPlanName(rs.getString("MST_PLAN_NAME"));
				terminalBean.setCardGroup(rs.getString("MST_CARD_GRUP")==null?null:Integer.parseInt(rs.getString("MST_CARD_GRUP")));
				/*if(storeBean.getMerchantEntityName() != null && storeBean.getMerchantEntityName().equals("1")) {
					terminalBean.setPosterminalProfileId(rs.getString("MST_TPRF_SQID"));
					terminalBean.setEcomterminalProfileId(null);
				}else if(storeBean.getMerchantEntityName() != null && storeBean.getMerchantEntityName().equals("2")) {
					terminalBean.setEcomterminalProfileId(rs.getString("MST_TPRF_SQID"));
					terminalBean.setPosterminalProfileId(null);
				}else {
					terminalBean.setEcomterminalProfileId(null);
					terminalBean.setPosterminalProfileId(null);
				}*/
				
				terminalBean.setEnableQRcode(rs.getString("MST_ENQR_CODE"));
				terminalBean.setAppId(rs.getString("MST_APP_ID"));
				terminalBean.setAuthToken(rs.getString("MST_AUTH_TOK"));
				terminalBean.setTerminalRiskProfile(rs.getString("MST_RISK_NAME"));
				terminalBean.setTerminalRiskProfileIndc(rs.getString("MST_RISK_FLAG"));
				terminalBean.setPos(rs.getString("MST_POS_NUM"));
				terminalBean.setUseSameAsStoreAccountForTerm(rs.getString("MST_ACCT_FLAG"));
				terminalBean.setDftTrmlRcrngChrgePlnId(rs.getString("MST_MRCP_SQID"));				
				terminalBean.setDftTrmlRcrngChrgePlnIdname(rs.getString("MST_MRCP_NAME"));
				
				terminalBean.setRollingReserveFundRequired(rs.getInt("MST_RRF_REQD"));
				terminalBean.setRrfIndc(rs.getInt("MST_RRF_INDC"));
				terminalBean.setDomestic(rs.getString("MST_DOM_REC_PERC")==null?null:rs.getInt("MST_DOM_REC_PERC"));
				terminalBean.setInterNational(rs.getString("MST_INT_REC_PERC")==null?null:rs.getInt("MST_INT_REC_PERC"));
				terminalBean.setDomesticFixedAmount(rs.getString("MST_DOM_FIXD_AMT")==null?null:rs.getLong("MST_DOM_FIXD_AMT"));
				terminalBean.setInternationalFixedAmount(rs.getString("MST_INT_FIXD_AMT")==null?null:rs.getLong("MST_INT_FIXD_AMT"));
				terminalBean.setDomesticHoldingDays(rs.getString("MST_DOM_HDAY")==null?null:rs.getInt("MST_DOM_HDAY"));
				terminalBean.setInternationalHoldingDays(rs.getString("MST_INT_HDAY")==null?null:rs.getInt("MST_INT_HDAY"));
				terminalBean.setSecurityDepositRecoveryRequired(rs.getInt("MST_SD_RECV_REQD"));
				terminalBean.setSdrIndc(rs.getInt("MST_SD_INDC"));
				terminalBean.setSecurityDepositRecoveryAmount(rs.getString("MST_SD_PERC_AMT")==null?null:rs.getLong("MST_SD_PERC_AMT"));
				terminalBean.setSecurityDepositRecoveryDays(rs.getString("MST_SD_NOOF_DAYS")==null?null:rs.getInt("MST_SD_NOOF_DAYS"));
				terminalBean.setTerminalPriceDetailsRequired(rs.getString("MST_PRIC_REQD"));
				terminalBean.setTerminalmccautocomplete(rs.getString("MST_MCC_CODE"));
				terminalBean.setCterminalmccautoComplete(rs.getString("MST_MCC_CODE")==null?null:rs.getString("MST_MCC_CODE").split("-")[0]);
				terminalBean.setTerminalRecurringPlanWaiver(rs.getInt("MST_RECR_FEE_WAVR"));
				terminalBean.setTerminalName(rs.getString("MST_TERM_NAME"));
				if(rs.getString("MST_PYMT_METD") != null) {
					String[] splitArray = (rs.getString("MST_PYMT_METD").contains("1,3,6")?rs.getString("MST_PYMT_METD").replaceFirst("1,3,6", ""):rs.getString("MST_PYMT_METD")).split(",");
					String[] newArray = new String[splitArray.length + 1];
					newArray[0] = "1,3,6";
					System.arraycopy(splitArray, 0, newArray, 1, splitArray.length);
					terminalBean.setPaymentMethod(newArray);
					splitArray=null;newArray=null;
				} else {
					terminalBean.setPaymentMethod(null);
				}
				//terminalBean.setAaniPayMercID(rs.getString("MST_ANIP_MERCODE"));
				//terminalBean.setAaniPayStoreID(rs.getString("MST_ANIP_STORECODE"));
				terminalBean.setAaniPayTermID(rs.getString("MST_ANIP_TERMCODE"));
				
				if(terminalBean.getUseSameAsStoreAccountForTerm() !=null && terminalBean.getUseSameAsStoreAccountForTerm().equals("1")) {
					terminalBean = getTerminalAccountDetails(terminalBean, refNo, mid, storeBean.getStoreSqid());
				}
				if(terminalBean.getTerminalPriceDetailsRequired() !=null && terminalBean.getTerminalPriceDetailsRequired().equals("1")) {
					terminalBean = getTerminalPriceDetails(terminalBean);
				}
				if(terminalBean.getTerminalPriceDetailsRequired() ==null || terminalBean.getTerminalPriceDetailsRequired().length() == 0) {
					terminalBean.setTerminalPriceDetailsRequired("0");
				}
				if(terminalBean.getUseSameAsStoreAccountForTerm() ==null || terminalBean.getUseSameAsStoreAccountForTerm().length() == 0) {
					terminalBean.setUseSameAsStoreAccountForTerm("0");
				}
				if(terminalBean.getTerminalRiskProfileIndc() == null || terminalBean.getTerminalRiskProfileIndc().length() == 0) {
					terminalBean.setTerminalRiskProfileIndc("0");
				}
				if(terminalBean.getEnableQRcode() == null || terminalBean.getEnableQRcode().length() == 0) {
					terminalBean.setEnableQRcode("0");
				}
				
				return terminalBean;
			}

		},instId, refNo, storeBean.getStoreSqid());

		return list;
	}
	
	private MerchantOnboardTerminalBean getTerminalAccountDetails(MerchantOnboardTerminalBean terminalBean,	String refNo, String mid, Integer storeSqid) {
		StringBuffer strQry = new StringBuffer(
				"SELECT MSA_DMAC_INDC, MSA_ACCT_BRAN, MSA_IFSC_CODE, MSA_ACCT_NUMB, "
				+ " (SELECT MGC_CRCY_SQID||'-'||MGC_CRCY_DESC FROM MPS_GENR_CRCY_MAST "
				+ " WHERE MGC_CRCY_SQID = A.MSA_ACCT_CRCY) AS MSA_ACCT_CRCY,"
				+ " MSA_ACCT_NAME, MSA_DEFT_ACCT, MSA_BANK_NAME, MSA_BRCH_NAME, MSA_CIF_NMBR, MSA_STMT_FREQ FROM MPS_SPOB_ACCT_TEMP A "
				+ " WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_STOR_CODE = ? "
				+ " AND MSA_TERM_CODE = ? AND MSA_MAPG_LEVL = ? ORDER BY MSA_REFR_SQID ASC ");
		jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardTerminalBean>() {
			public MerchantOnboardTerminalBean mapRow(ResultSet rs, int arg1) throws SQLException {
				terminalBean.setTerminalacctWithBank(rs.getString("MSA_DMAC_INDC")==null?null:Integer.parseInt(rs.getString("MSA_DMAC_INDC")));
				if(terminalBean.getTerminalacctWithBank()==1) {
					terminalBean.setcTerminalacctBranch(rs.getString("MSA_ACCT_BRAN"));
					if(terminalBean.getcTerminalacctBranch()!=null && terminalBean.getcTerminalacctBranch().trim().length()!=0) {
						terminalBean.setTerminalacctBranch(jdbcTemplate.queryForObject("SELECT MAM_MEBR_CODE || '-' || MAM_MEBR_NAME FROM MPS_ADMN_MEBR_MAST WHERE MAM_MEBR_CODE = ? ",String.class, terminalBean.getcTerminalacctBranch()));
					}
				}
				terminalBean.setTerminalifsc(rs.getString("MSA_IFSC_CODE"));
				terminalBean.setTerminalacctNumber(rs.getString("MSA_ACCT_NUMB"));
				terminalBean.setTerminalcurrency(rs.getString("MSA_ACCT_CRCY"));
				terminalBean.setTerminalaccountname(rs.getString("MSA_ACCT_NAME"));
				terminalBean.setTerminaldefAccount(rs.getString("MSA_DEFT_ACCT")==null?0:rs.getInt("MSA_DEFT_ACCT"));
				terminalBean.setTerminalacctBankText(rs.getString("MSA_BANK_NAME"));
				terminalBean.setTerminalacctBranchText(rs.getString("MSA_BRCH_NAME"));
				terminalBean.setTerminalcifNumber(rs.getString("MSA_CIF_NMBR"));
				terminalBean.setTerminalstmtfreq(rs.getInt("MSA_STMT_FREQ"));
				
				return terminalBean;
			}
		}, refNo, mid, storeSqid, terminalBean.getTermSqid(), terminalLevel);

		
		return terminalBean;
	}
	
	private MerchantOnboardTerminalBean getTerminalPriceDetails(MerchantOnboardTerminalBean terminalBean) {
		
		StringBuffer strQry = new StringBuffer(" SELECT MSP_REFR_SQID, MSP_FPLN_TYPE, MSP_FPLN_NAME, MSP_CAPT_MSFA, MSP_FEE_WAVR "
				+ " FROM MPS_SPOB_PRIC_TEMP P WHERE MSP_REFR_SQID = ? AND MSP_ENTY_SQID = ?");
		
		jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardTerminalBean>() {
			public MerchantOnboardTerminalBean mapRow(ResultSet rs, int arg1) throws SQLException {
				terminalBean.setTerminalFeePlanType(rs.getString("MSP_FPLN_TYPE")==null?null:Integer.parseInt(rs.getString("MSP_FPLN_TYPE"))); // added after deleting the column in merc_temp - MSM_MSVF_PLTY
				terminalBean.setTerminalMerchantServiceFeePlan(rs.getString("MSP_FPLN_NAME"));
				terminalBean.setTerminalCaptureServiceFeeAt(rs.getString("MSP_CAPT_MSFA"));
				terminalBean.setTerminalMercFeeDeduWaiver(rs.getInt("MSP_FEE_WAVR"));
				
				return terminalBean;
			}
		},terminalBean.getTermSqid(), terminalLevel);
		
		return terminalBean;
	}
	
	@Override
	public List<MapsBaseBean> getCurrency() throws Exception {
		List currencyList = null;

		StringBuffer query = new StringBuffer();
		/**query.append(
				" SELECT MGC_CRCY_SQID, MGC_CRCY_DESC FROM MPS_GENR_CRCY_MAST CURR_MAST, MPS_ADMN_INST_PRAQ PERMITTED_CURR ");
		query.append("  WHERE MGC_CRCY_STUS = 1 AND CURR_MAST.MGC_CRCY_SQID = PERMITTED_CURR.MAI_CRCY_SQID ");
		query.append("  UNION ");
		query.append(
				" SELECT MGC_CRCY_SQID, MGC_CRCY_DESC FROM MPS_GENR_CRCY_MAST CURR_MAST, MPS_ADMN_INST_PRST SETTLEMENT_CURR ");
		query.append(
				" WHERE MGC_CRCY_STUS = 1  AND CURR_MAST.MGC_CRCY_SQID = SETTLEMENT_CURR.MAI_CRCY_SQID ORDER BY MGC_CRCY_DESC ");**/
		
		query.append(" SELECT MGC_CRCY_SQID, MGC_CRCY_DESC FROM MPS_GENR_CRCY_MAST CURR_MAST, MPS_ADMN_INST_PRST SETTLEMENT_CURR ");
		query.append(" WHERE MGC_CRCY_STUS = 1 AND CURR_MAST.MGC_CRCY_SQID = SETTLEMENT_CURR.MAI_CRCY_SQID ");
		query.append(" AND SETTLEMENT_CURR.MAI_CRCY_STUS='1' ORDER BY MGC_CRCY_DESC ");

		RowMapper<KeyValueBean> mapper = new RowMapper<KeyValueBean>() {
			public KeyValueBean mapRow(ResultSet rs, int rowNum) throws SQLException {
				KeyValueBean keyValueBean = new KeyValueBean();
				keyValueBean.setKey(rs.getString("MGC_CRCY_SQID")+"-"+rs.getString("MGC_CRCY_DESC"));
				keyValueBean.setValue(rs.getString("MGC_CRCY_DESC"));
				return keyValueBean;
			}
		};

		currencyList = jdbcTemplate.query(query.toString(), mapper);
		return currencyList;
	}
	@Override
	public List<net.sf.json.JSONObject> getMercAggrList(Integer instId) throws Exception {
		List<net.sf.json.JSONObject> mgalist = null;
		StringBuilder strQry = new StringBuilder();
		try {

			strQry.append(" SELECT MAG_AGGR_SQID, MAG_AGGR_NAME, MAG_MERC_RELM FROM MPS_ADMN_AGGR_MAST WHERE ");
			strQry.append(" MAG_AGGR_STUS = 1 ORDER BY MAG_AGGR_NAME ");

			mgalist = (List<net.sf.json.JSONObject>) jdbcTemplate.query(strQry.toString(),
					new RowMapper<net.sf.json.JSONObject>() {
						public net.sf.json.JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
							net.sf.json.JSONObject json = new net.sf.json.JSONObject();
							json.put("value", rs.getString(1));
							json.put("desc", rs.getString(2).trim());
							json.put("relm", rs.getString(3)==null?null:rs.getString(3).trim());
							return json;
						}
					});
		} catch (Exception e) {
			loggerUtil.error("Error while fetching aggregator details "+ e.getMessage());
		}
		return mgalist;
	}
	@Override
	public List<net.sf.json.JSONObject> getTerminalChargePlanForPricing(Integer instId) throws Exception {
		List<net.sf.json.JSONObject> terchplaList = null;
		StringBuilder strQry = new StringBuilder();
		try {

			strQry.append(" select mrp_rcpl_sqid, mrp_rcpl_desc from mps_rchg_plan_mast where mrp_rcpl_catg ='3' ");

			terchplaList = (List<net.sf.json.JSONObject>) jdbcTemplate.query(strQry.toString(),
					new RowMapper<net.sf.json.JSONObject>() {
						public net.sf.json.JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
							net.sf.json.JSONObject json = new net.sf.json.JSONObject();
							json.put("value", rs.getString(1));
							json.put("desc", rs.getString(2).trim());
							return json;
						}
					});
		} catch (Exception e) {
			loggerUtil.error("Error while fetching recurring charge plan details "+ e.getMessage());
		}
		return terchplaList;
	}
	@Override
	public List<KeyValueBean> getTerminalProfileList(Integer instId) {
		List<KeyValueBean> terminalProfileList = new ArrayList<KeyValueBean>();
		String query = "";
		try
		{
			query = " select MMT_TPRF_SQID, MMT_TPRF_NAME from mps_merc_tprf_mast where MMT_INST_SQID =?  and MMT_MERC_TYPE = 2 ";
			
			RowMapper<KeyValueBean> mapper = new RowMapper<KeyValueBean>() 
					{
						@Override
						public KeyValueBean mapRow(ResultSet rs, int arg1)	throws SQLException 
						{
							KeyValueBean bean = new KeyValueBean();
							bean.setKey(rs.getString("MMT_TPRF_SQID"));
							bean.setValue(rs.getString("MMT_TPRF_NAME"));
							return bean;
						}
						
					};
					terminalProfileList = jdbcTemplate.query(query, mapper,instId);
		}
		catch(Exception e)
		{
			loggerUtil.error("Error while fetching terminal profile details "+ e.getMessage());
		}
		return terminalProfileList;
	}
	@Override
	public List<KeyValueBean> getEcomTerminalProfileList(Integer instId) {
		List<KeyValueBean> terminalProfileList = new ArrayList<KeyValueBean>();
		String query = "";
		try
		{
			query = " select MMT_TPRF_SQID, MMT_TPRF_NAME from mps_merc_tprf_mast where MMT_INST_SQID =?  and MMT_MERC_TYPE = 1 ";
			
			RowMapper<KeyValueBean> mapper = new RowMapper<KeyValueBean>() 
					{
						@Override
						public KeyValueBean mapRow(ResultSet rs, int arg1)	throws SQLException 
						{
							KeyValueBean bean = new KeyValueBean();
							bean.setKey(rs.getString("MMT_TPRF_SQID"));
							bean.setValue(rs.getString("MMT_TPRF_NAME"));
							return bean;
						}
						
					};
					terminalProfileList = jdbcTemplate.query(query, mapper,instId);
		}
		catch(Exception e)
		{
			loggerUtil.error("Error while fetching ecom terminal profile details "+ e.getMessage());
		}
		return terminalProfileList;
	}
	@Override
	public void updateAttachementPath(MerchantOnboardBean merchantOnboardBean, Integer instId) throws Exception {
		List<String> fileList = new ArrayList<String>();
		String fileName = "";
		String filePath = "";
		SANFileReader sanFileReader = null;
		sanFileReader = new SANFileReader(instId);
		try {
			
			fileList = jdbcTemplate.query(
					"SELECT MAA_FILE_NAME||'--'||MAA_FILE_PATH as data FROM MPS_SPOB_ADMN_ATCH_DETL WHERE  MAA_INST_SQID=? AND MAA_ENTY_SQID=? AND MAA_ENTY_REFN=?  ",
					new RowMapper<String>() {
						public String mapRow(ResultSet rs, int arg1) throws SQLException {
							return rs.getString("data");
						}
					}, instId, 1, 1);
			
			if (!fileList.equals(null)) {
				for (String rec : fileList) {
					String values[] = rec.split("--");
					fileName = values[0];
					filePath = values[1];
					String destPath = "";
					if (filePath.endsWith("/1/")) {
						destPath = filePath.replace("/1/", "/" + merchantOnboardBean.getRefSqid() + "/");
					}
					sanFileReader.moveFile(filePath, destPath, fileName);
					jdbcTemplate.update("UPDATE MPS_SPOB_ADMN_ATCH_DETL SET  MAA_FILE_PATH=?, MAA_ENTY_REFN=? WHERE "
							+ " MAA_INST_SQID=?  AND MAA_ENTY_REFN=? AND  MAA_FILE_PATH=? AND MAA_FILE_NAME=? ",
							destPath, merchantOnboardBean.getRefSqid(), instId, 1, filePath, fileName );
				}
			}
			sanFileReader.close();
		} catch (Exception e) {
			loggerUtil.error("Error while updating path "+ e.getMessage());
		}finally {
			fileList = null;			
			fileName = null;
			filePath = null;
		}
	}
	@Override
	public List<KeyValueBean> getTerminalProductType(List<KeyValueBean> lstProuctType, Integer instId, String mercType) {
		StringBuilder strQry = new StringBuilder();
		try {
				strQry.append(" SELECT DISTINCT MMT_TMPD_SQID, MMT_TMPD_DESC, MMT_METY_SQID FROM MPS_MERC_TMPD_MAST  "
						+ " WHERE MMT_INST_SQID = ? AND MMT_TMPD_STUS IN (1) ORDER BY MMT_TMPD_SQID ASC ");
				lstProuctType = (List<KeyValueBean>) jdbcTemplate.query(strQry.toString(),
						new RowMapper<KeyValueBean>() {
							public KeyValueBean mapRow(ResultSet rs, int rowNum) throws SQLException {
								KeyValueBean bean = new KeyValueBean();
								bean.setKey(rs.getString(1));
								bean.setValue(rs.getString(2));
								bean.setQuery(rs.getString(3));
								return bean;
							}
						}, instId);
		} catch (Exception e) {
			loggerUtil.error("Error while fetching terminal product type details "+ e.getMessage());
		}finally {
			strQry = null;
		}
		return lstProuctType;
	}
	
	public List<KeyValueBean> getTerminalProductTypeForEdit(List<KeyValueBean> lstProuctType, Integer instId, String entitySqid) {
		StringBuilder strQry = new StringBuilder();
		try {
			
				strQry.append(" SELECT DISTINCT TMPD.MMT_TMPD_SQID, TMPD.MMT_TMPD_DESC, TMPD.MMT_METY_SQID FROM MPS_MERC_TMPD_MAST TMPD "
						+ " WHERE TMPD.MMT_INST_SQID = ? AND TMPD.MMT_TMPD_STUS IN (1,2) "
						+ " AND TMPD.MMT_METY_SQID IN (SELECT MTS_TXSR_TYPE FROM MPS_TXNS_SRCE_MAST WHERE MTS_TXSR_SQID IN ( "
						+ Stream.generate(() -> "?").limit(entitySqid.split(",").length).collect(Collectors.joining(", "))
						+ " ) ");
				strQry.append(" ORDER BY TMPD.MMT_TMPD_SQID ASC ");
				lstProuctType = (List<KeyValueBean>) jdbcTemplate.query(strQry.toString(),
						new RowMapper<KeyValueBean>() {
							public KeyValueBean mapRow(ResultSet rs, int rowNum) throws SQLException {
								KeyValueBean bean = new KeyValueBean();
								bean.setKey(rs.getString(1));
								bean.setValue(rs.getString(2));
								bean.setQuery(rs.getString(3));
								return bean;
							}
						}, Stream.concat(Arrays.asList(instId).stream(), Arrays.asList(entitySqid.split(",")).stream())
								.collect(Collectors.toList()).toArray());
			
		} catch (Exception e) {
			loggerUtil.error("Error while fetching terminal product type details for edit "+ e.getMessage());
		}finally {
			strQry = null;
		}
		return lstProuctType;
	}

	// Added Methods for Instant Onboard - End
	
	// Attachments - Starts
	
	public String getPathPrefix() throws Exception {
		return jdbcTemplate.queryForObject("select MGP_PARM_VALU from mps_genr_parm_mast where MGP_PARM_KEY = 18", String.class);
	}
	
	public void doAttachment(MerchantOnboardAttachementBean attachmentsBean) throws Exception {
		StringBuffer stringBuffQry = new StringBuffer();
		stringBuffQry.append("insert into MPS_SPOB_ADMN_ATCH_DETL(MAA_INST_SQID,MAA_RECD_SQID,MAA_ENTY_SQID,");
		stringBuffQry.append("MAA_ENTY_REFN,MAA_FILE_NAME,MAA_FILE_PATH,MAA_ATCH_STUS, MAA_ATCH_SRCE, MAA_REQT_SQID, ");
		stringBuffQry.append("MAA_INSD_USID,MAA_INSD_SYDT,MAA_INSD_FIDT ");
		if (attachmentsBean.getStoreRefNum() != null && !attachmentsBean.getStoreRefNum().equals("")) {
			stringBuffQry.append(" ,MAA_ENTY_SREF ");
		}
		stringBuffQry.append(" ) ");
		stringBuffQry.append("values(?,SEQ_MAA_RECD_SQID.nextval,?,?,?,?,?,0,?,?,SYSDATE,FUN_GETFIDATE(?) ");

		if (attachmentsBean.getStoreRefNum() != null && !attachmentsBean.getStoreRefNum().equals("")) {
			stringBuffQry.append(" ,? ");
		}
		stringBuffQry.append(" ) ");

		if (attachmentsBean.getStoreRefNum() != null && !attachmentsBean.getStoreRefNum().equals("")) {
			jdbcTemplate.update(stringBuffQry.toString(), new Object[] { attachmentsBean.getInstId(),
					attachmentsBean.getEntitySeqId(), attachmentsBean.getEntityRefNum(), attachmentsBean.getFileName(),
					attachmentsBean.getFilePath(), ATTACHED, attachmentsBean.getRequestSeqId(),
					attachmentsBean.getUserId(), attachmentsBean.getInstId(), attachmentsBean.getStoreRefNum() });
		} else {
			jdbcTemplate.update(stringBuffQry.toString(),
					new Object[] { attachmentsBean.getInstId(), attachmentsBean.getEntitySeqId(),
							attachmentsBean.getEntityRefNum(), attachmentsBean.getFileName(),
							attachmentsBean.getFilePath(), ATTACHED, attachmentsBean.getRequestSeqId(),
							attachmentsBean.getUserId(), attachmentsBean.getInstId() });
		}
	}
	
	public List<MerchantOnboardAttachementBean> getAttachmentsDetails(MerchantOnboardAttachementBean attachmentsBean) throws Exception {

		StringBuffer strBuffQry = new StringBuffer();
		String dateFormat = attachmentsBean.getDateFormat() + " HH24:MI:SS";
		Object[] args = null;
		if (attachmentsBean.getEntitySeqId() == 1) {
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND MAA_ENTY_SREF IS NULL ");
			strBuffQry.append("UNION ALL ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ADDR_MKCK ADDR ");
			strBuffQry.append(
					"ON ADDR.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ADDR.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND ADDR.MMA_MEST_STUS = ? AND MAA_ENTY_SREF IS NULL ");
			strBuffQry.append("UNION ALL  ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_LEGL_TRDG_MKCK LEGL ");
			strBuffQry.append(
					"ON LEGL.MLT_INST_SQID = ATCH.MAA_INST_SQID AND LEGL.MLT_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND LEGL.MLT_REQT_STUS = ? AND MAA_ENTY_SREF IS NULL ");
			strBuffQry.append("UNION ALL ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT, ?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ACCT_MKCK ACCT ");
			strBuffQry.append(
					"ON ACCT.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ACCT.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND ACCT.MAA_REQT_STUS = ? AND MAA_ENTY_SREF IS NULL ");
			
			strBuffQry.append("UNION ALL ");
			strBuffQry.append("SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, 'API USER' as MAD_USER_NAME, "
					+ " TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, "
					+ " ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, 'API USER' AS MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH "
					+ " WHERE ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND MAA_ENTY_SREF IS NULL "
					+ " AND MAA_INSD_USID = 0 ");
			
			strBuffQry.append("ORDER BY MAA_INSD_SYDT ");
			args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS, 1,
					attachmentsBean.getEntityRefNum(), dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, 1, attachmentsBean.getEntityRefNum(), APPROVED, dateFormat,
					attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, 1, attachmentsBean.getEntityRefNum(),
					APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, 1,
					attachmentsBean.getEntityRefNum(), APPROVED, dateFormat, ATTACHED, ATTACHMENT_SOURCE_MAPS, 1, attachmentsBean.getEntityRefNum() };
		} else if (attachmentsBean.getEntitySeqId() == 2) {
			if (attachmentsBean.getStoreRefNum() != null && !attachmentsBean.getStoreRefNum().equals("")) {
				
				strBuffQry.append(" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, "
						+ " ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID,MAA_ENTY_SREF FROM MPS_SPOB_ADMN_ATCH_DETL ATCH "
						+ " INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ? "
						+ " AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND MAA_ENTY_SREF = ?  ");
				strBuffQry.append(" UNION ALL ");
				strBuffQry.append(" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, 'API USER' Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, "
						+ " ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, 'API USER' MAD_LGIN_SQID, MAA_ENTY_SREF FROM MPS_SPOB_ADMN_ATCH_DETL ATCH "
						+ " WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_INSD_USID = 0 "
						+ " AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND MAA_ENTY_SREF = ? ");
				strBuffQry.append(" ORDER BY MAA_INSD_SYDT ");
				
				args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS, 2,
						attachmentsBean.getEntityRefNum(), attachmentsBean.getStoreRefNum(), 
						dateFormat, attachmentsBean.getInstId(), ATTACHED,
						ATTACHMENT_SOURCE_MAPS, 2, attachmentsBean.getEntityRefNum(), attachmentsBean.getStoreRefNum() };
				
			} else {
				strBuffQry.append(
						"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
				strBuffQry.append(
						"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ? ");
				strBuffQry.append(
						"AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND MAA_ENTY_SREF IS NULL ");
				strBuffQry.append("UNION ALL ");
				strBuffQry.append(
						"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
				strBuffQry.append(
						"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ADDR_MKCK ADDR ");
				strBuffQry.append(
						"ON ADDR.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ADDR.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
				strBuffQry.append(
						"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND ADDR.MMA_MEST_STUS = ? AND MAA_ENTY_SREF IS NULL ");
				strBuffQry.append("UNION ALL  ");
				strBuffQry.append(
						"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
				strBuffQry.append(
						"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_LEGL_TRDG_MKCK LEGL ");
				strBuffQry.append(
						"ON LEGL.MLT_INST_SQID = ATCH.MAA_INST_SQID AND LEGL.MLT_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
				strBuffQry.append(
						"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND LEGL.MLT_REQT_STUS = ? AND MAA_ENTY_SREF IS NULL ");
				strBuffQry.append("UNION ALL ");
				strBuffQry.append(
						"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT, ?) AS INSTDATE, ");
				strBuffQry.append(
						"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ACCT_MKCK ACCT ");
				strBuffQry.append(
						"ON ACCT.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ACCT.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
				strBuffQry.append(
						"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_ENTY_REFN = ? AND ACCT.MAA_REQT_STUS = ? AND MAA_ENTY_SREF IS NULL ");
				strBuffQry.append("ORDER BY MAA_INSD_SYDT ");
				args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS, 1,
						attachmentsBean.getEntityRefNum(), dateFormat, attachmentsBean.getInstId(), ATTACHED,
						ATTACHMENT_SOURCE_MSS, 1, attachmentsBean.getEntityRefNum(), APPROVED, dateFormat,
						attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, 1,
						attachmentsBean.getEntityRefNum(), APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED,
						ATTACHMENT_SOURCE_MSS, 1, attachmentsBean.getEntityRefNum(), APPROVED };
			}
		} else if (attachmentsBean.getEntitySeqId() == 3) {
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?,?) AND ATCH.MAA_ENTY_REFN = ?  ");
			strBuffQry.append("UNION ALL ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ADDR_MKCK ADDR ");
			strBuffQry.append(
					"ON ADDR.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ADDR.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?,?) AND ATCH.MAA_ENTY_REFN = ? AND ADDR.MMA_MEST_STUS = ? ");
			strBuffQry.append("UNION ALL  ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_LEGL_TRDG_MKCK LEGL ");
			strBuffQry.append(
					"ON LEGL.MLT_INST_SQID = ATCH.MAA_INST_SQID AND LEGL.MLT_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?,?) AND ATCH.MAA_ENTY_REFN = ? AND LEGL.MLT_REQT_STUS = ? ");
			strBuffQry.append("UNION ALL ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT, ?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ACCT_MKCK ACCT ");
			strBuffQry.append(
					"ON ACCT.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ACCT.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?,?) AND ATCH.MAA_ENTY_REFN = ? AND ACCT.MAA_REQT_STUS = ? ");
			strBuffQry.append("ORDER BY MAA_INSD_SYDT ");
			args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS, 1, 2, 3,
					attachmentsBean.getEntityRefNum(), dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, 1, 2, 3, attachmentsBean.getEntityRefNum(), APPROVED, dateFormat,
					attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, 1, 2, 3,
					attachmentsBean.getEntityRefNum(), APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, 1, 2, 3, attachmentsBean.getEntityRefNum(), APPROVED };
		} else {
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID=? AND ATCH.MAA_ENTY_REFN = ?  ");
			strBuffQry.append("UNION ALL ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ADDR_MKCK ADDR ");
			strBuffQry.append(
					"ON ADDR.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ADDR.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID=? AND ATCH.MAA_ENTY_REFN = ? AND ADDR.MMA_MEST_STUS = ? ");
			strBuffQry.append("UNION ALL  ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_LEGL_TRDG_MKCK LEGL ");
			strBuffQry.append(
					"ON LEGL.MLT_INST_SQID = ATCH.MAA_INST_SQID AND LEGL.MLT_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID = ?  AND ATCH.MAA_ENTY_REFN = ? AND LEGL.MLT_REQT_STUS = ? ");
			strBuffQry.append("UNION ALL ");
			strBuffQry.append(
					"SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT, ?) AS INSTDATE, ");
			strBuffQry.append(
					"ATCH.MAA_ENTY_REFN, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_SPOB_ADMN_ATCH_DETL ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ACCT_MKCK ACCT ");
			strBuffQry.append(
					"ON ACCT.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ACCT.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ? ");
			strBuffQry.append(
					"AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID = ? AND ATCH.MAA_ENTY_REFN = ? AND ACCT.MAA_REQT_STUS = ? ");
			strBuffQry.append("ORDER BY MAA_INSD_SYDT ");

			args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS,
					attachmentsBean.getEntitySeqId(), attachmentsBean.getEntityRefNum(), dateFormat,
					attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, attachmentsBean.getEntitySeqId(),
					attachmentsBean.getEntityRefNum(), APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, attachmentsBean.getEntitySeqId(), attachmentsBean.getEntityRefNum(),
					APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS,
					attachmentsBean.getEntitySeqId(), attachmentsBean.getEntityRefNum(), APPROVED };
		}
		List<List<MerchantOnboardAttachementBean>> list = jdbcTemplate.query(strBuffQry.toString(),
				new RowMapper<List<MerchantOnboardAttachementBean>>() {
					public List<MerchantOnboardAttachementBean> mapRow(ResultSet rs, int rowNum) throws SQLException {
						List<MerchantOnboardAttachementBean> attaList = new ArrayList<MerchantOnboardAttachementBean>();
						do {
							MerchantOnboardAttachementBean attachmentsBeanTmp = new MerchantOnboardAttachementBean();
							attachmentsBeanTmp.setRecordSqId(rs.getLong("MAA_RECD_SQID"));
							attachmentsBeanTmp.setSrNo(rs.getRow());
							attachmentsBeanTmp.setFileName(rs.getString("MAA_FILE_NAME"));
							attachmentsBeanTmp.setUserName(rs.getString("MAD_LGIN_SQID"));
							attachmentsBeanTmp.setAttachmentDate(rs.getString("INSTDATE"));
							attachmentsBeanTmp.setEntityRefNum(rs.getLong("MAA_ENTY_REFN"));
							attachmentsBeanTmp.setEntitySeqId(rs.getLong("MAA_ENTY_SQID"));
							attachmentsBeanTmp.setAttachSrce(rs.getString("MAA_ATCH_SRCE"));
							attachmentsBeanTmp.setStoreRefNum(rs.getString("MAA_ENTY_SREF"));
							attaList.add(attachmentsBeanTmp);
						} while (rs.next());
						return attaList;
					}
				}, args);
		
		return list.size() > 0 ? (List<MerchantOnboardAttachementBean>) list.get(0) : new ArrayList<MerchantOnboardAttachementBean>();

	}
	
	@Override
	public void doAttachmentWithoutReferral(MerchantOnboardAttachementBean attachmentsBean) throws Exception {
		StringBuffer stringBuffQry = new StringBuffer();
		stringBuffQry.append("insert into MPS_ADMN_ATCH_TEMP(MAA_INST_SQID,MAA_RECD_SQID,MAA_ENTY_SQID, ");
		stringBuffQry.append("MAA_PAN_NUM,MAA_FILE_NAME,MAA_FILE_PATH,MAA_ATCH_STUS, MAA_ATCH_SRCE, MAA_REQT_SQID, ");
		stringBuffQry.append("MAA_INSD_USID,MAA_INSD_SYDT,MAA_INSD_FIDT) ");
		stringBuffQry.append("values(?,(SELECT NVL(MAX(MAA_RECD_SQID),0)+1 FROM MPS_ADMN_ATCH_TEMP),?,?,?,?,?,0,?,?,SYSDATE,FUN_GETFIDATE(?))");
		jdbcTemplate.update(stringBuffQry.toString(),
				new Object[] { attachmentsBean.getInstId(), attachmentsBean.getEntitySeqId(),
						attachmentsBean.getEntityPanNo(), attachmentsBean.getFileName(), attachmentsBean.getFilePath(),
						ATTACHED, attachmentsBean.getRequestSeqId(), attachmentsBean.getUserId(),
						attachmentsBean.getInstId() });
	}
	
	@Override
	public List<MerchantOnboardAttachementBean> getAttachmentsDetailsWithoutReferral(MerchantOnboardAttachementBean attachmentsBean) throws Exception {

		StringBuffer strBuffQry = new StringBuffer();
		String dateFormat = attachmentsBean.getDateFormat() + " HH24:MI:SS";
		Object[] args = null;
		if (attachmentsBean.getEntitySeqId() == 1) {
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                    			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ?        ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_PAN_NUM = ?                                                                      		");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                    			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ADDR_MKCK ADDR  ");
			strBuffQry.append(
					" ON ADDR.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ADDR.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_PAN_NUM = ? AND ADDR.MMA_MEST_STUS = ?                                                                       	");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                   			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_LEGL_TRDG_MKCK LEGL  ");
			strBuffQry.append(
					" ON LEGL.MLT_INST_SQID = ATCH.MAA_INST_SQID AND LEGL.MLT_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_PAN_NUM = ? AND LEGL.MLT_REQT_STUS = ?                                                                       	");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT, ?) AS INSTDATE,                   			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ACCT_MKCK ACCT  ");
			strBuffQry.append(
					" ON ACCT.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ACCT.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?) AND ATCH.MAA_PAN_NUM = ? AND ACCT.MAA_REQT_STUS = ?                                                                       	");
			strBuffQry.append(
					" ORDER BY MAA_INSD_SYDT 																																								");

			args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS, 1,
					attachmentsBean.getEntityPanNo(), dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, 1, attachmentsBean.getEntityPanNo(), APPROVED, dateFormat,
					attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, 1, attachmentsBean.getEntityPanNo(),
					APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, 1,
					attachmentsBean.getEntityPanNo(), APPROVED };

		} else if (attachmentsBean.getEntitySeqId() == 2) {
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                    			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ?        ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?) AND ATCH.MAA_PAN_NUM = ?                                                                      		");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                    			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ADDR_MKCK ADDR  ");
			strBuffQry.append(
					" ON ADDR.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ADDR.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?) AND ATCH.MAA_PAN_NUM = ? AND ADDR.MMA_MEST_STUS = ?                                                                       	");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                   			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_LEGL_TRDG_MKCK LEGL  ");
			strBuffQry.append(
					" ON LEGL.MLT_INST_SQID = ATCH.MAA_INST_SQID AND LEGL.MLT_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?) AND ATCH.MAA_PAN_NUM = ? AND LEGL.MLT_REQT_STUS = ?                                                                       	");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT, ?) AS INSTDATE,                   			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ACCT_MKCK ACCT  ");
			strBuffQry.append(
					" ON ACCT.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ACCT.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?) AND ATCH.MAA_PAN_NUM = ? AND ACCT.MAA_REQT_STUS = ?                                                                       	");
			strBuffQry.append(
					" ORDER BY MAA_INSD_SYDT 																																								");

			args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS, 1, 2,
					attachmentsBean.getEntityPanNo(), dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, 1, 2, attachmentsBean.getEntityPanNo(), APPROVED, dateFormat,
					attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, 1, 2,
					attachmentsBean.getEntityPanNo(), APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, 1, 2, attachmentsBean.getEntityPanNo(), APPROVED };

		} else if (attachmentsBean.getEntitySeqId() == 3) {
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                    			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ?        ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?,?) AND ATCH.MAA_PAN_NUM = ?                                                                      		");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                    			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ADDR_MKCK ADDR  ");
			strBuffQry.append(
					" ON ADDR.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ADDR.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?,?) AND ATCH.MAA_PAN_NUM = ? AND ADDR.MMA_MEST_STUS = ?                                                                       	");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                   			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_LEGL_TRDG_MKCK LEGL  ");
			strBuffQry.append(
					" ON LEGL.MLT_INST_SQID = ATCH.MAA_INST_SQID AND LEGL.MLT_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?,?) AND ATCH.MAA_PAN_NUM = ? AND LEGL.MLT_REQT_STUS = ?                                                                       	");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT, ?) AS INSTDATE,                   			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ACCT_MKCK ACCT  ");
			strBuffQry.append(
					" ON ACCT.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ACCT.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID IN (?,?,?) AND ATCH.MAA_PAN_NUM = ? AND ACCT.MAA_REQT_STUS = ?                                                                       	");
			strBuffQry.append(
					" ORDER BY MAA_INSD_SYDT 																																								");
			args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS, 1, 2, 3,
					attachmentsBean.getEntityPanNo(), dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, 1, 2, 3, attachmentsBean.getEntityPanNo(), APPROVED, dateFormat,
					attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, 1, 2, 3,
					attachmentsBean.getEntityPanNo(), APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, 1, 2, 3, attachmentsBean.getEntityPanNo(), APPROVED };

		} else {

			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                    			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID WHERE ATCH.MAA_INST_SQID = ?        ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_STUS = ? AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID = ? AND ATCH.MAA_PAN_NUM = ?                                                                      		");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                    			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ADDR_MKCK ADDR  ");
			strBuffQry.append(
					" ON ADDR.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ADDR.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID = ? AND ATCH.MAA_PAN_NUM = ? AND ADDR.MMA_MEST_STUS = ?                                                                       	");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT,?) AS INSTDATE,                   			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_LEGL_TRDG_MKCK LEGL  ");
			strBuffQry.append(
					" ON LEGL.MLT_INST_SQID = ATCH.MAA_INST_SQID AND LEGL.MLT_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID = ? AND ATCH.MAA_PAN_NUM = ? AND LEGL.MLT_REQT_STUS = ?                                                                       	");
			strBuffQry.append(
					" UNION ALL                                                                                                                                                                           ");
			strBuffQry.append(
					" SELECT ATCH.MAA_ENTY_SQID, ATCH.MAA_ATCH_SRCE, ATCH.MAA_RECD_SQID, ATCH.MAA_FILE_NAME, Auth.Mad_User_Name, TO_CHAR(ATCH.MAA_INSD_SYDT, ?) AS INSTDATE,                   			");
			strBuffQry.append(
					" ATCH.MAA_PAN_NUM, ATCH.MAA_INSD_SYDT, AUTH.MAD_LGIN_SQID FROM MPS_ADMN_ATCH_TEMP ATCH INNER JOIN MPS_AUTH_DETL_MAST AUTH ON AUTH.MAD_USER_SQID = ATCH.MAA_INSD_USID INNER JOIN MSS_MEST_ACCT_MKCK ACCT  ");
			strBuffQry.append(
					" ON ACCT.MMA_INST_SQID = ATCH.MAA_INST_SQID AND ACCT.MMA_REQT_SQID = ATCH.MAA_REQT_SQID WHERE ATCH.MAA_INST_SQID = ? AND ATCH.MAA_ATCH_STUS = ?                                      ");
			strBuffQry.append(
					" AND ATCH.MAA_ATCH_SRCE = ? AND ATCH.MAA_ENTY_SQID = ? AND ATCH.MAA_PAN_NUM = ? AND ACCT.MAA_REQT_STUS = ?                                                                       	");
			strBuffQry.append(
					" ORDER BY MAA_INSD_SYDT 																																								");
			args = new Object[] { dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MAPS,
					attachmentsBean.getEntitySeqId(), attachmentsBean.getEntityPanNo(), dateFormat,
					attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS, attachmentsBean.getEntitySeqId(),
					attachmentsBean.getEntityPanNo(), APPROVED, dateFormat, attachmentsBean.getInstId(), ATTACHED,
					ATTACHMENT_SOURCE_MSS, attachmentsBean.getEntitySeqId(), attachmentsBean.getEntityPanNo(), APPROVED,
					dateFormat, attachmentsBean.getInstId(), ATTACHED, ATTACHMENT_SOURCE_MSS,
					attachmentsBean.getEntitySeqId(), attachmentsBean.getEntityPanNo(), APPROVED };
		}
		List<List<MerchantOnboardAttachementBean>> list = jdbcTemplate.query(strBuffQry.toString(),
				new RowMapper<List<MerchantOnboardAttachementBean>>() {
					public List<MerchantOnboardAttachementBean> mapRow(ResultSet rs, int rowNum) throws SQLException {
						List<MerchantOnboardAttachementBean> attaList = new ArrayList<MerchantOnboardAttachementBean>();
						do {
							MerchantOnboardAttachementBean attachmentsBeanTmp = new MerchantOnboardAttachementBean();
							attachmentsBeanTmp.setRecordSqId(rs.getLong("MAA_RECD_SQID"));
							attachmentsBeanTmp.setSrNo(rs.getRow());
							attachmentsBeanTmp.setFileName(rs.getString("MAA_FILE_NAME"));
							attachmentsBeanTmp.setUserName(rs.getString("MAD_LGIN_SQID"));
							attachmentsBeanTmp.setAttachmentDate(rs.getString("INSTDATE"));
							attachmentsBeanTmp.setEntityPanNo(rs.getString("MAA_PAN_NUM"));
							attachmentsBeanTmp.setEntitySeqId(rs.getLong("MAA_ENTY_SQID"));
							attachmentsBeanTmp.setAttachSrce(rs.getString("MAA_ATCH_SRCE"));
							attaList.add(attachmentsBeanTmp);
						} while (rs.next());
						return attaList;
					}
				}, args);

		return list.size() > 0 ? (List<MerchantOnboardAttachementBean>) list.get(0) : new ArrayList<MerchantOnboardAttachementBean>();
	}
	
	public void doDetachment(MerchantOnboardAttachementBean attachmentsBean) throws Exception {

		StringBuffer stringBuffQry = new StringBuffer();
		
		int instId = Long.valueOf(attachmentsBean.getInstId()).intValue();
		
		List<String> fileList = new ArrayList<String>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MAA_FILE_NAME||'--'||MAA_FILE_PATH as data from MPS_SPOB_ADMN_ATCH_DETL where  MAA_RECD_SQID = ?");
		
		fileList= jdbcTemplate.query(buffer.toString(),	new RowMapper<String>() {
				public String mapRow(ResultSet rs, int arg1) throws SQLException {						
					return rs.getString("data");
				}
			}, attachmentsBean.getRecordSqId());
	
		if (!fileList.equals(null)) {
			for (String rec : fileList) {
				String values[] = rec.split("--");
				String fileName = values[0];
				String filePath = values[1];
			
		stringBuffQry.append("update MPS_SPOB_ADMN_ATCH_DETL set MAA_FILE_PATH = ?, MAA_ATCH_STUS = ?, MAA_LUPD_USID = ?, "
				+ "MAA_LUPD_SYDT = SYSDATE, MAA_LUPD_FIDT = FUN_GETFIDATE(?) where MAA_ENTY_REFN = ? and MAA_INST_SQID = ? "
				+ "and MAA_RECD_SQID = ? and MAA_ENTY_SQID = ?");

		jdbcTemplate.update(stringBuffQry.toString(),
				new Object[] { attachmentsBean.getFilePath(), DEATTACHED, attachmentsBean.getUserId(),
						attachmentsBean.getInstId(), attachmentsBean.getEntityRefNum(), attachmentsBean.getInstId(),
						attachmentsBean.getRecordSqId(), attachmentsBean.getEntitySeqId() });
			}
		}
	}
	
	@Override
	public void doDetachmentWithoutReferral(MerchantOnboardAttachementBean attachmentsBean) throws Exception {
		StringBuffer stringBuffQry = new StringBuffer();
		stringBuffQry.append("update MPS_ADMN_ATCH_TEMP set MAA_FILE_PATH = ?, MAA_ATCH_STUS = ?, MAA_LUPD_USID = ?, "
				+ "MAA_LUPD_SYDT = SYSDATE, MAA_LUPD_FIDT = FUN_GETFIDATE(?) where MAA_PAN_NUM = ? and MAA_INST_SQID = ? "
				+ "and MAA_RECD_SQID = ? and MAA_ENTY_SQID = ?");

		jdbcTemplate.update(stringBuffQry.toString(),
				new Object[] { attachmentsBean.getFilePath(), DEATTACHED, attachmentsBean.getUserId(),
						attachmentsBean.getInstId(), attachmentsBean.getEntityPanNo(), attachmentsBean.getInstId(),
						attachmentsBean.getRecordSqId(), attachmentsBean.getEntitySeqId() });
	}
	
	// Attachments -Ends
	
	// Bulk Upload -starts	
	@Override
	public List<MerchantOnboardBean> showDataOfFile(String uploadType, String filename) {
		StringBuilder mSearch = new StringBuilder();
		List<MerchantOnboardBean> list = null;
		try {
			if(uploadType.equalsIgnoreCase("24"))
			{
						
				mSearch.append(" SELECT MBU_FILE_NAME, SUM (CASE WHEN MBU_REC_STUS = 1  THEN 1 ELSE 0 END) AS SUCCESS_COUNT, ");
				mSearch.append(" SUM (CASE WHEN MBU_REC_STUS <> 1  THEN 1 ELSE 0 END) AS FAILURE_COUNT, ");
				mSearch.append(" COUNT(MBU_FILE_NAME) AS TOTAL_COUNT, ");
				mSearch.append(" (SELECT MAD_LGIN_SQID FROM MPS_AUTH_DETL_MAST WHERE MAD_USER_SQID = MBU_INST_USID) MBU_INST_USID, ");
				mSearch.append(" TRUNC(MBU_INST_SYDT) AS MBU_INST_SYDT from MPS_BULK_UPLD_TEMP ");
				mSearch.append(" JOIN MPS_AUTH_DETL_MAST ON MBU_INST_USID = MAD_USER_SQID ");
				mSearch.append(" where MBU_FILE_NAME = ? ");
				mSearch.append(" GROUP BY MBU_FILE_NAME,MBU_INST_USID,TRUNC(MBU_INST_SYDT) ");
			}		

			list = jdbcTemplate.query(mSearch.toString(), new RowMapper<MerchantOnboardBean>() {

				public MerchantOnboardBean mapRow(ResultSet rs, int arg1)
						throws SQLException {
					MerchantOnboardBean bean = new MerchantOnboardBean();
						bean.setFileName(rs.getString("MBU_FILE_NAME"));
						bean.setSuccessCount(rs.getString("SUCCESS_COUNT"));
							if(bean.getSuccessCount()!=null && Integer.parseInt(bean.getSuccessCount())>0) {
								bean.setSuccessCount("<input name='grpId' value='"+bean.getSuccessCount()+"' type='button' "+" onclick=\"showSuccessRecords('"+rs.getString("MBU_FILE_NAME")+
								"','"+uploadType+"');\" class='hrefButton'/> "); }
						bean.setFailureCount(rs.getString("FAILURE_COUNT"));
							if(bean.getFailureCount()!=null && Integer.parseInt(bean.getFailureCount())>0) {
								bean.setFailureCount("<input name='grpId' value='"+bean.getFailureCount()+"' type='button' "+" onclick=\"showErrorRecords('"+rs.getString("MBU_FILE_NAME")+
								"','"+uploadType+"');\" class='hrefButton'/> "); }
						bean.setTotalCount(rs.getString("TOTAL_COUNT"));
						bean.setProcessedByUser(rs.getString("MBU_INST_USID"));
						bean.setProcessedDate(rs.getString("MBU_INST_SYDT"));
					return bean;
					
					}},filename);
		} catch (Exception e) {
			loggerUtil.error("Error while fetching the searchReportData " + e.getMessage());
		}
		return list;
	}
	
	public List<MerchantOnboardBean> getBulkUploadReportDetails(String fileName, String reportType, String uploadType) {
		StringBuilder mSearch = new StringBuilder();
		List<MerchantOnboardBean> list = null;
		try {
			if(uploadType.equals("24")){
				if(reportType.equalsIgnoreCase("FAILURE")) {
					mSearch.append(" SELECT ROWNUM,MBU_APPL_NMBR,MBU_CUST_PAN,MBU_LEGL_NAME,NVL(MBE_EROR_DESC,MBU_ERR_MSG) ERR_MSG FROM MPS_BULK_UPLD_TEMP ");
					mSearch.append(" LEFT JOIN MPS_BULK_EXEP_DETL ON MBU_APPL_NMBR = MBE_APPL_NMBR ");
					mSearch.append(" LEFT JOIN MPS_BULK_EXEP_MAST ON MBE_EROR_UQID = MBE_EXEP_SQID ");
					mSearch.append(" WHERE MBU_FILE_NAME = ? AND MBU_REC_STUS <> 1 ");
					
					list = jdbcTemplate.query(mSearch.toString(), new RowMapper<MerchantOnboardBean>() {
						
						public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
							MerchantOnboardBean bean = new MerchantOnboardBean();
								bean.setRecordNumber(rs.getString("ROWNUM"));
								bean.setApplNum(rs.getString("MBU_APPL_NMBR"));
								bean.setCpancard(rs.getString("MBU_CUST_PAN"));
								bean.setLegalname(rs.getString("MBU_LEGL_NAME"));
								bean.setReportErrorDesc(rs.getString("ERR_MSG").replaceAll(",", ".<br>"));
							return bean;
						}},fileName);
				}else if(reportType.equalsIgnoreCase("SUCCESS")) {
					mSearch.append(" SELECT ROWNUM,MBU_APPL_NMBR,MBU_CUST_PAN,MBU_LEGL_NAME,MBU_ERR_MSG FROM MPS_BULK_UPLD_TEMP ");
					mSearch.append(" WHERE MBU_FILE_NAME = ? AND MBU_REC_STUS = 1 ");
					
					list = jdbcTemplate.query(mSearch.toString(), new RowMapper<MerchantOnboardBean>() {

						public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
							MerchantOnboardBean bean = new MerchantOnboardBean();
								bean.setRecordNumber(rs.getString("ROWNUM"));
								bean.setApplNum(rs.getString("MBU_APPL_NMBR"));
								bean.setCpancard(rs.getString("MBU_CUST_PAN"));
								bean.setLegalname(rs.getString("MBU_LEGL_NAME"));
								bean.setReportErrorDesc(rs.getString("MBU_ERR_MSG"));
							return bean;

						}},fileName);
				}else {
					mSearch.append(" SELECT ROWNUM,MBU_APPL_NMBR,MBU_CUST_PAN,MBU_LEGL_NAME,NVL(MBE_EROR_DESC,MBU_ERR_MSG) ERR_MSG FROM MPS_BULK_UPLD_TEMP ");
					mSearch.append(" LEFT JOIN MPS_BULK_EXEP_DETL ON MBU_APPL_NMBR = MBE_APPL_NMBR ");
					mSearch.append(" LEFT JOIN MPS_BULK_EXEP_MAST ON MBE_EROR_UQID = MBE_EXEP_SQID ");
					mSearch.append(" WHERE MBU_FILE_NAME = ? ");
					
					list = jdbcTemplate.query(mSearch.toString(), new RowMapper<MerchantOnboardBean>() {
						
						public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
							MerchantOnboardBean bean = new MerchantOnboardBean();
								bean.setRecordNumber(rs.getString("ROWNUM"));
								bean.setApplNum(rs.getString("MBU_APPL_NMBR"));
								bean.setCpancard(rs.getString("MBU_CUST_PAN"));
								bean.setLegalname(rs.getString("MBU_LEGL_NAME"));
								bean.setReportErrorDesc(rs.getString("ERR_MSG").replaceAll(",", ".<br>"));
							return bean;
						}},fileName);
				}
			}
		}catch (Exception e) {
			loggerUtil.error("Error while retriving the getFailureRecordList" + e.getMessage());
		}finally {
			mSearch = null;
		}
		return list;
	}
	@Override
	public List<MerchantOnboardBean> getSuccessRecordList(String fileName, String uploadType) {
		StringBuilder mSearch = new StringBuilder();
		List<MerchantOnboardBean> list = null;
		try {
			if(uploadType.equals("24")){
				
				mSearch.append(" WITH success_data AS ( ");
				mSearch.append(" SELECT MBU_APPL_NMBR,MBU_CUST_PAN,MBU_LEGL_NAME,MBU_ERR_MSG FROM MPS_BULK_UPLD_TEMP A ");
				mSearch.append(" LEFT JOIN MPS_SPOB_BULK_LOG B ON A.MBU_FILE_NAME = B.MSB_FILE_NAME AND A.MBU_REC_STUS = B.MSB_RECD_STUS ");
				mSearch.append(" AND A.MBU_APPL_NMBR = B.MSB_APPL_NMBR ");
				mSearch.append(" WHERE A.MBU_FILE_NAME = ? AND A.MBU_REC_STUS = 1 ) ");
				mSearch.append(" SELECT ROWNUM, success_data.* ");
				mSearch.append(" FROM success_data ");
				
				list = jdbcTemplate.query(mSearch.toString(), new RowMapper<MerchantOnboardBean>() {

					public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
						MerchantOnboardBean bean = new MerchantOnboardBean();
							bean.setRecordNumber(rs.getString("ROWNUM"));
							bean.setApplNum(rs.getString("MBU_APPL_NMBR"));
							bean.setCpancard(rs.getString("MBU_CUST_PAN"));
							bean.setLegalname(rs.getString("MBU_LEGL_NAME"));
							bean.setReportErrorDesc(rs.getString("MBU_ERR_MSG"));
						return bean;

					}},fileName);
			}
		}catch (Exception e) {
			loggerUtil.error("Error while retriving the getSuccessRecordList" + e.getMessage());
		}finally {
			mSearch = null;
		}
		return list;
	}
	@Override
	public List<MerchantOnboardBean> getFailureRecordList(String fileName, String uploadType) {
		StringBuilder mSearch = new StringBuilder();
		List<MerchantOnboardBean> list = null;
		try {
			if(uploadType.equals("24")){
				
				mSearch.append(" WITH failure_data AS ( ");
				mSearch.append(" SELECT MBU_APPL_NMBR, MBU_CUST_PAN, MBU_LEGL_NAME, ");
				mSearch.append(" NVL(MBE_EROR_DESC, MBU_ERR_MSG) AS ERR_MSG ");
				mSearch.append(" FROM MPS_BULK_UPLD_TEMP ");
				mSearch.append(" LEFT JOIN MPS_BULK_EXEP_DETL ON MBU_APPL_NMBR = MBE_APPL_NMBR ");
				mSearch.append(" LEFT JOIN MPS_BULK_EXEP_MAST ON MBE_EROR_UQID = MBE_EXEP_SQID ");
				mSearch.append(" JOIN MPS_SPOB_BULK_LOG ON MBU_APPL_NMBR = MSB_APPL_NMBR ");
				mSearch.append(" WHERE MBU_FILE_NAME = ? ");
				mSearch.append(" AND MBU_REC_STUS <> 1 ");
				mSearch.append(" UNION ");
				mSearch.append(" SELECT ");
				mSearch.append(" TRIM(REGEXP_SUBSTR(MSB_RECD_DATA, '[^~]+', 1, 2)) AS MBU_APPL_NMBR, ");
				mSearch.append(" TRIM(REGEXP_SUBSTR(MSB_RECD_DATA, '[^~]+', 1, 18)) AS MBU_CUST_PAN, ");
				mSearch.append(" TRIM(REGEXP_SUBSTR(MSB_RECD_DATA, '[^~]+', 1, 4)) AS MBU_LEGL_NAME, ");
				mSearch.append(" CASE ");
				mSearch.append("   WHEN MSB_RECD_DESC LIKE '%Duplicate%' THEN 'Duplicate record is present' ");
				mSearch.append("   ELSE MSB_RECD_DESC ");
				mSearch.append(" END AS ERR_MSG ");
				mSearch.append(" FROM MPS_SPOB_BULK_LOG ");
				mSearch.append(" WHERE MSB_FILE_NAME = ? ");
				mSearch.append(" AND MSB_RECD_STUS = 2 ");
				mSearch.append(" ) ");
				mSearch.append(" SELECT ROWNUM, failure_data.* FROM failure_data ");
				
				list = jdbcTemplate.query(mSearch.toString(), new RowMapper<MerchantOnboardBean>() {
					
					public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
						MerchantOnboardBean bean = new MerchantOnboardBean();
							bean.setRecordNumber(rs.getString("ROWNUM"));
							bean.setApplNum(rs.getString("MBU_APPL_NMBR"));
							bean.setCpancard(rs.getString("MBU_CUST_PAN"));
							bean.setLegalname(rs.getString("MBU_LEGL_NAME"));
							//bean.setReportErrorDesc(rs.getString("ERR_MSG").replaceAll(",", ".<br>"));
							//bean.setReportErrorDesc(rs.getString("ERR_MSG").replaceAll(",", ".\n"));
							bean.setReportErrorDesc(rs.getString("ERR_MSG").replace("\\n", "\r\n").replace(",", ".\r\n").replaceAll("\\s{2,}", " ").trim());
						return bean;
					}},fileName, fileName);
			}
		}catch (Exception e) {
			loggerUtil.error("Error while retriving the getFailureRecordList" + e.getMessage());
		}finally {
			mSearch = null;
		}
		return list;
	}
	// Bulk Upload - Ends
	@Override
	public String getAutoGenerateCode(Integer instId, Integer level) {
		String code = null;
		CallableStatement stmt = null;
		try 
		{
			stmt = jdbcTemplate.getDataSource().getConnection().prepareCall("CALL SP_AUTO_GENERATE_CODE(?, ?, ?, ?)");
			stmt.setString(1, instId.toString());
			stmt.setInt(2, level); //1- merhcant, 2- Store, 3- Terminal
			stmt.registerOutParameter(3, Types.VARCHAR); // code
			stmt.registerOutParameter(4, Types.VARCHAR); // Error message
			stmt.execute();
			code = stmt.getString(3);
			
		} catch (Exception e) {
			loggerUtil.error("getAutoGenerateCode - error occured "+ e.getMessage());
		} finally {
			stmt = null;
		}
		return code;
		
	}
	@Override
	public List<KeyValueBean> getMerchantEntityList(Integer instId, String mercType) {
		List<KeyValueBean> mercEntities = null;
		StringBuilder query = null;
		try{
			if(mercType!=null && mercType.length()!=0 && mercType.equalsIgnoreCase(" ")==false) {
				query = new StringBuilder("SELECT MTS_TXSR_SQID, MTS_TXSR_DESC FROM MPS_TXNS_SRCE_MAST WHERE MTS_INST_SQID = ? AND MTS_TXSR_STUS = ? "
						+ " AND MTS_TXSR_TYPE IN ( "
						+ Stream.generate(() -> "?").limit(mercType.split(",").length).collect(Collectors.joining(", "))
						+ " ) ");
				mercEntities = jdbcTemplate.query(query.toString(), new RowMapper<KeyValueBean>() {

					@Override
					public KeyValueBean mapRow(ResultSet rs, int indx) throws SQLException {
						KeyValueBean bean = new KeyValueBean();
						bean.setKey(rs.getString("MTS_TXSR_SQID"));
						bean.setValue(rs.getString("MTS_TXSR_DESC"));
						return bean;
					}
				}, Stream.concat(Arrays.asList(instId,ACTIVE).stream(), Arrays.asList(mercType.split(",")).stream())
	                    .collect(Collectors.toList()).toArray());
			}else {
				query = new StringBuilder("SELECT MTS_TXSR_SQID, MTS_TXSR_DESC FROM MPS_TXNS_SRCE_MAST WHERE MTS_INST_SQID = ? AND MTS_TXSR_STUS = ? ");
				mercEntities = jdbcTemplate.query(query.toString(), new RowMapper<KeyValueBean>() {

					@Override
					public KeyValueBean mapRow(ResultSet rs, int indx) throws SQLException {
						KeyValueBean bean = new KeyValueBean();
						bean.setKey(rs.getString("MTS_TXSR_SQID"));
						bean.setValue(rs.getString("MTS_TXSR_DESC"));
						return bean;
					}
				}, instId, ACTIVE);
			}
		}catch(Exception e){
			loggerUtil.error("Error in getMerchantEntityList" + e.getMessage());
		}
		return mercEntities != null ? mercEntities : new ArrayList<KeyValueBean>();
	}
	@Override
	public int getCheckAggrAcctDetails(Integer instId, String mercAggrID) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_MERC_ACCT_DETL WHERE MMA_ENTY_SQID = 0 AND MMA_ACCT_STUS = 1 AND MMA_AGGR_SQID = ? AND MMA_INST_SQID = ?", Integer.class, mercAggrID, instId);
	}
	@Override
	public void deleteAccountDetails(MerchantOnboardBean merchantOnboardBean, String accountlevel,
			Integer instId, Integer userId) {
		jdbcTemplate.update("DELETE FROM MPS_SPOB_ACCT_TEMP WHERE MSA_REFR_SQID = ? AND MSA_MERC_CODE = ? AND MSA_MAPG_LEVL = ? ", 
				merchantOnboardBean.getRefSqid(), merchantOnboardBean.getMid(), accountlevel);
	}
	
	/** Added By Prasanth A & Narayanan S WebService Call - Start - 05-09-2024**/
	
	@Override
	public List<String> getReferralDetailRelease(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		StringBuilder sb = new StringBuilder();
		List<String> list = new ArrayList<String>();
		try {
			
			sb.append(" Select Mme_Refr_Sqid From Mps_Merc_Errf_Detl Where Mme_Inst_Sqid = ? "
					+ " And Mme_Refr_Sqid In (?) order by mme_refr_sqid desc ");
			
			for(String refno : merchantOnboardBean.getRefNoWithRemarks().split(",")) {
				list.add(String.valueOf(jdbcTemplate.queryForObject(sb.toString(), Integer.class, instId, Integer.parseInt(refno))));
			}
			
		}
		catch(Exception e){
			loggerUtil.error("Error while fetching from getReferralDetailRelease() - "+e.getMessage());
			merchantOnboardBean.setErrorDesc("ERROR");
			merchantOnboardBean.setFlag("Error while fetching from getReferralDetailRelease() - "+e.getMessage());
		}finally {
			sb = null;
		}
		return list.size() > 0 ? list : new ArrayList<String>();		
	}
	@Override
	public void updateFailedMerchantStatus(Integer instId, String refNo, String userId, String result) {
		try {
			jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_REC_STUS = 3, MSM_ERR_DESC = ? where MSM_REFR_SQID = ?" , "ERROR - "+result, refNo);
		}catch (Exception e) {
			loggerUtil.error("Error while fetching from updateFailedMerchantStatus() - "+e.getMessage());
		}
	}
		
	/** Added By Prasanth A & Narayanan S WebService Call - End - 05-09-2024 **/	
	
	//DIB CHANGES
	public String getParamValue(String paramkey) {
		return jdbcTemplate.queryForObject("SELECT MAI_PARM_VALU FROM MPS_ADMN_INST_PARM WHERE MAI_PARM_KEY = ?", String.class, paramkey);
	}
	public String getCountryName(String countrycode) {
		return jdbcTemplate.queryForObject("SELECT MGC_CNRY_NAME FROM MPS_GENR_CNRY_MAST WHERE MGC_CNRY_SQID = ?", String.class, countrycode);
	}
	public List<KeyValueBean> getMerchantTypeList(Integer instId){
		List<KeyValueBean> merchantTypeList = null;
		StringBuilder query = null;
		try{
			query = new StringBuilder("SELECT MMT_METY_SQID, MMT_METY_DESC FROM MPS_MERC_TYPE_MAST WHERE MMT_INST_SQID = ? AND MMT_METY_STUS = ? ");
			merchantTypeList = jdbcTemplate.query(query.toString(), new RowMapper<KeyValueBean>() {
				@Override
				public KeyValueBean mapRow(ResultSet rs, int indx) throws SQLException {
					KeyValueBean bean = new KeyValueBean();
					bean.setKey(rs.getString("MMT_METY_SQID"));
					bean.setValue(rs.getString("MMT_METY_DESC"));
					return bean;
				}
			}, instId, ACTIVE);
		}catch(Exception e){
			loggerUtil.error("Error in getMerchantTypeList" + e.getMessage());
		}
		return merchantTypeList != null ? merchantTypeList : new ArrayList<KeyValueBean>();
	}
	@Override
	public MerchantOnboardBean addStorePriceDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, Integer instId, Integer userId, String entityLevel) {
		StringBuilder strQry = new StringBuilder();
		try {
			strQry.append("INSERT INTO MPS_SPOB_PRIC_TEMP(MSP_REFR_SQID,MSP_METY_SQID,MSP_FPLN_TYPE,MSP_FPLN_NAME,MSP_CAPT_MSFA,MSP_FPLN_SQID, ")
					.append(" MSP_INTC_PLUS,MSP_REC_STUS,MSP_FEE_WAVR,MSP_ENTY_SQID ")
					.append(") VALUES (?,?,?,?,?,?,?,?,?,?) ");
			jdbcTemplate.update(strQry.toString(), 
					storeBean.getStoreSqid(),
					merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
					storeBean.getStoreFeePlanType(),
					storeBean.getStoreMerchantServiceFeePlan(), 
					storeBean.getStoreCaptureServiceFeeAt(),
					storeBean.getStoreMerchantServiceFeePlan()!=null && storeBean.getStoreMerchantServiceFeePlan() != "" ? storeBean.getStoreMerchantServiceFeePlan().split("-")[0] : null,
					0,ACTIVE, 
					//storeBean.getStoreMercFeeDeduWaiver(),
					Optional.ofNullable(storeBean.getStoreMercFeeDeduWaiver()).filter(v -> v != 0).orElse(0),
					entityLevel);
		} catch (Exception e) {
			loggerUtil.error("Error while adding Store Price details "+ e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		} finally {
			strQry = null;
		}
		return merchantOnboardBean;
	}
		
	@Override
	public List<KeyValueBean> getNatureOfBusinessCatgList(Integer instId) {
		List<KeyValueBean> natureOfBusinessCatgList = new ArrayList<KeyValueBean>();
		String query = "";
		try
		{
			query = " SELECT MNB_NABU_STUS||'~'||MNB_NATR_SQID AS SQIDWITHSTATUS, MNB_NATR_DESC AS NAME FROM MPS_NATR_BUSN_MAST WHERE MNB_INST_SQID = ? AND MNB_NABU_STUS = 1 ";
			
			RowMapper<KeyValueBean> mapper = new RowMapper<KeyValueBean>() 
					{
						@Override
						public KeyValueBean mapRow(ResultSet rs, int arg1)	throws SQLException 
						{
							KeyValueBean bean = new KeyValueBean();
							bean.setKey(rs.getString("SQIDWITHSTATUS"));
							bean.setValue(rs.getString("NAME"));
							return bean;
						}
						
					};
					natureOfBusinessCatgList = jdbcTemplate.query(query, mapper,instId);
		}
		catch(Exception e)
		{
			loggerUtil.error("Error while fetching getNatureOfBusinessCatgList details "+ e.getMessage());
		}
		return natureOfBusinessCatgList;
	}
	@Override
	public List<KeyValueBean> getProofOfDocumentList(int instId, String natureOfBusiness) {
		List<KeyValueBean> natureOfBusinessCatgList = new ArrayList<KeyValueBean>();
		String query = "";
		try
		{
			query = " SELECT MAD_DOCC_SQID,MAD_DOCC_DESC FROM MPS_ADMN_DOCC_MAST WHERE MAD_DOCC_STUS = ? AND MAD_INST_SQID = ? AND MAD_NATR_SQID=? ";
			
			RowMapper<KeyValueBean> mapper = new RowMapper<KeyValueBean>() 
					{
						@Override
						public KeyValueBean mapRow(ResultSet rs, int arg1)	throws SQLException 
						{
							KeyValueBean bean = new KeyValueBean();
							bean.setKey(rs.getString("MAD_DOCC_SQID"));
							bean.setValue(rs.getString("MAD_DOCC_DESC"));
							return bean;
						}
						
					};
					natureOfBusinessCatgList = jdbcTemplate.query(query, mapper, ACTIVE, instId, natureOfBusiness);
		}
		catch(Exception e)
		{
			loggerUtil.error("Error while fetching getProofOfDocumentList details "+ e.getMessage());
		}
		return natureOfBusinessCatgList;
	}
	
	public Integer mssUserCheck(String refNo, Integer instId) {
		Integer result = 0;
		StringBuilder query = new StringBuilder("");
		try {
			query.append("SELECT MMM_PORT_INDC FROM MPS_MERC_MERH_DETL WHERE  MMM_REFR_SQID=? AND MMM_INST_SQID=? ");
			result = jdbcTemplate.queryForObject(query.toString(), Integer.class, refNo, instId);
		} catch (Exception e) {
			loggerUtil.error("Error occurred in mssUserCheck - "+e.getMessage(), e);
		} finally {
			query = null;
		}
		return result;
	}
	
	public String findUserPasswordLength(String loginId) {
		
		StringBuilder sql = new StringBuilder();
    	sql.append("SELECT instparam.MAI_PARM_VALU as paramvalue ");
    	sql.append("FROM mps_auth_detl_mast usermast ,mps_auth_usin_mast userinst ,mps_genr_inst_mast instmast ,MPS_ADMN_INST_PARM instparam ");
    	sql.append("WHERE userinst.MAU_USER_SQID = usermast.MAD_USER_SQID ");
    	sql.append("AND userinst.MAU_INST_SQID = instmast.MGI_INST_SQID ");
    	sql.append("AND instmast.MGI_INST_SQID = instparam.MAI_INST_SQID ");
    	sql.append("AND instparam.MAI_PARM_KEY = ? ");
    	sql.append("AND usermast.MAD_LGIN_SQID = ? ");
    	sql.append("AND userinst.MAU_DFLT_INST = 1 ");
    	sql.append("AND instmast.MGI_INST_STUS = 1  ");
    	
    	StringBuilder sql2 = new StringBuilder();
    	sql2.append("SELECT  	parmmast.MAI_PARM_VALU AS paramvalue ");
    	sql2.append("FROM	MPS_ADMN_INST_PARM parmmast , MPS_GENR_INST_MAST instmast ");
    	sql2.append("WHERE	parmmast.MAI_PARM_KEY =  ? ");
    	sql2.append("AND	instmast.MGI_DEFT_INST = 1 ");
    	sql2.append("AND 	instmast.MGI_INST_SQID = parmmast.MAI_INST_SQID ");
    	String paramvalue = "";
    	try {
    		paramvalue = (String)jdbcTemplate.queryForMap(sql.toString(),MapsConstants.PSWD_LENGTH_KEY,loginId).get("PARAMVALUE");
    	}catch(EmptyResultDataAccessException empty){
    		paramvalue = (String)jdbcTemplate.queryForMap(sql2.toString(),MapsConstants.PSWD_LENGTH_KEY).get("PARAMVALUE");
    	}
    	return paramvalue;
	}
	
	public String getGrpSqidForMssSuperAdmnUsr(Integer instId) {
		String result = null;
		StringBuilder query = new StringBuilder("");
		try {
			query.append("select max(MAG_GRUP_SQID) from MPS_AUTH_GRUP_MAST where MAG_GRUP_TYPE = 8 AND MAG_INST_SQID = ? AND MAG_GRUP_SQID= ? ");
			result = jdbcTemplate.queryForObject(query.toString(), String.class, instId, 11);
		} catch (Exception e) {
			loggerUtil.error("Error occurred in getGrpSqidForMssSuperAdmnUsr - "+e.getMessage(), e);
		} finally {
			query = null;
		}
		return result;
	}
	
	public String getLoginURL(String loginUrlParmKey) {
		String result = null;
		StringBuilder query = new StringBuilder("");
		try {
			query.append("select MGP_PARM_VALU from mps_genr_parm_mast where MGP_PARM_KEY = ? ");
			result = jdbcTemplate.queryForObject(query.toString(), String.class, loginUrlParmKey);
		} catch (Exception e) {
			loggerUtil.error("Error occurred in getLoginURL - "+e.getMessage(), e);
		} finally {
			query = null;
		}
		return result;
	}
	@Override
	public MerchantOnboardBean addTerminalPriceDetails(MerchantOnboardBean merchantOnboardBean, MerchantOnboardStoreBean storeBean, MerchantOnboardTerminalBean terminalBean, Integer instId, Integer userId, String entityLevel) {
		StringBuilder strQry = new StringBuilder();
		try {
			strQry.append("INSERT INTO MPS_SPOB_PRIC_TEMP(MSP_REFR_SQID,MSP_METY_SQID,MSP_FPLN_TYPE,MSP_FPLN_NAME,MSP_CAPT_MSFA,MSP_FPLN_SQID, ")
					.append(" MSP_INTC_PLUS,MSP_REC_STUS,MSP_FEE_WAVR,MSP_ENTY_SQID,MSP_MRCP_SQID ")
					.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?) ");
			jdbcTemplate.update(strQry.toString(), 
					terminalBean.getTermSqid(),
					merchantOnboardBean.getMerchantType().replaceAll(" ", ""),
					terminalBean.getTerminalFeePlanType(),
					terminalBean.getTerminalMerchantServiceFeePlan(), 
					terminalBean.getTerminalCaptureServiceFeeAt(),
					terminalBean.getTerminalMerchantServiceFeePlan()!=null && terminalBean.getTerminalMerchantServiceFeePlan() != "" ? terminalBean.getTerminalMerchantServiceFeePlan().split("-")[0] : null,
					0, ACTIVE, 
					//terminalBean.getTerminalMercFeeDeduWaiver(), 
					Optional.ofNullable(terminalBean.getTerminalMercFeeDeduWaiver()).filter(v -> v != 0).orElse(0),
					entityLevel,
					terminalBean.getDftTrmlRcrngChrgePlnId()==null||terminalBean.getDftTrmlRcrngChrgePlnId().equals("")?null:Integer.parseInt(terminalBean.getDftTrmlRcrngChrgePlnId()));
		} catch (Exception e) {
			loggerUtil.error("Error while adding Terminal Price details "+ e.getMessage());
			merchantOnboardBean.setErrorstatus("ERROR");
		} finally {
			strQry = null;
		}
		return merchantOnboardBean;
	}
	@Override
	public List<KeyValueBean> getPaymentMethodList(Integer instId) {
		List<KeyValueBean> paymentMethodList = new ArrayList<KeyValueBean>();
		String query = "";
		try
		{
			query = " SELECT LISTAGG(MAP_PAYM_SQID, ',') WITHIN GROUP (ORDER BY MAP_PAYM_SQID) AS MAP_PAYM_SQID, 'Card' AS MAP_PAYM_NAME FROM MPS_ADMN_PYMT_MAST "
					+ " WHERE MAP_INST_SQID = ? AND MAP_PAYM_STUS = ? AND MAP_METY_SQID = ? AND MAP_PAYM_SQID IN ('1', '3', '6') "
					+ " UNION ALL "
					+ " SELECT TO_CHAR(MAP_PAYM_SQID), MAP_PAYM_NAME FROM MPS_ADMN_PYMT_MAST "
					+ " WHERE MAP_INST_SQID = ? AND MAP_PAYM_STUS = ? AND MAP_METY_SQID = ? AND MAP_PAYM_SQID NOT IN ('1', '3', '6')";
			
			RowMapper<KeyValueBean> mapper = new RowMapper<KeyValueBean>() 
			{
				@Override
				public KeyValueBean mapRow(ResultSet rs, int arg1)	throws SQLException 
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey(rs.getString("MAP_PAYM_SQID"));
					bean.setValue(rs.getString("MAP_PAYM_NAME"));
					return bean;
				}
			};
		paymentMethodList = jdbcTemplate.query(query, mapper, instId, ACTIVE, ACTIVE, instId, ACTIVE, ACTIVE);
		}
		catch(Exception e)
		{
			loggerUtil.error("Error while fetching getpaymentMethodList details "+ e.getMessage());
		}
		return paymentMethodList;
	}
	@Override
	public List<KeyValueBean> getPrfDocsProvList(Integer instId, String prfDocsProv) {
		List<KeyValueBean> rfDocsProvList = new ArrayList<KeyValueBean>();
		StringBuilder query = null;
		try
		{
			query = new StringBuilder(" SELECT MAD_DOCC_SQID,MAD_DOCC_DESC FROM MPS_ADMN_DOCC_MAST WHERE MAD_INST_SQID = ? AND MAD_DOCC_SQID IN ( "
					+ Stream.generate(() -> "?").limit(prfDocsProv.split(",").length).collect(Collectors.joining(", "))
					+ " ) ");
			
			rfDocsProvList = jdbcTemplate.query(query.toString(), new RowMapper<KeyValueBean>() {

				@Override
				public KeyValueBean mapRow(ResultSet rs, int indx) throws SQLException {
					KeyValueBean bean = new KeyValueBean();
					bean.setKey(rs.getString("MAD_DOCC_SQID"));
					bean.setValue(rs.getString("MAD_DOCC_DESC"));
					return bean;
				}
			}, Stream.concat(Arrays.asList(instId).stream(), Arrays.asList(prfDocsProv.split(",")).stream())
                    .collect(Collectors.toList()).toArray());
		}
		catch(Exception e)
		{
			loggerUtil.error("Error while fetching getPrfDocsProvList details "+ e.getMessage());
		}
		return rfDocsProvList;
	}
	@Override
	public int checkDocumentValidation(int instId, String entitySeqId, String tempFolderName) {
		if(entitySeqId.trim().equalsIgnoreCase("2")) {
			return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_SPOB_ADMN_ATCH_DETL WHERE MAA_INST_SQID = ? "
					+ "	AND MAA_ATCH_STUS = ? AND MAA_ENTY_SQID = ? AND MAA_ENTY_SREF = ?", 
					Integer.class, instId, ATTACHED, storeLevel, tempFolderName);
		} else {
			return 0;
		}
	}
	@Override
	public String getTempFoldername() {
		return ""+(int) (Math.pow(10, TEMPFOLDERNAME_LEN-1) + new Random().nextInt((int) Math.pow(10, TEMPFOLDERNAME_LEN) - (int) Math.pow(10, TEMPFOLDERNAME_LEN-1)));
	}
	@Override
	public void updateAttachementPathForStore(Integer instId, String storelevel, Integer refNo, Integer storeSqid,
			String tempFolderName, byte totalStoreCount, byte storecount) {
		List<String> fileList = new ArrayList<String>();
		String fileName = "";
		String filePath = "";
		String temprefNo = "";
		Integer storeFlag = 0;
		SANFileReader sanFileReader = null;
		sanFileReader = new SANFileReader(instId);
		try {
			
			storeFlag = jdbcTemplate.queryForObject("SELECT NVL(MSM_STOR_FLAG, 0) FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ?", Integer.class, refNo);
			
			temprefNo = tempFolderName.substring(0,TEMPFOLDERNAME_LEN);
			fileList = jdbcTemplate.query(
					"SELECT MAA_FILE_NAME||'--'||MAA_FILE_PATH as data FROM MPS_SPOB_ADMN_ATCH_DETL "
					+ " WHERE  MAA_INST_SQID=? AND MAA_ATCH_STUS =? AND MAA_ENTY_SQID=? AND MAA_ENTY_REFN=? AND MAA_ENTY_SREF =? ",
					new RowMapper<String>() {
						public String mapRow(ResultSet rs, int arg1) throws SQLException {
							return rs.getString("data");
						}
					}, instId, ATTACHED, storelevel, (storeFlag != null && storeFlag == 1) ? refNo : temprefNo, tempFolderName);
			
			if (!fileList.equals(null)) {
				for (String rec : fileList) {
					String values[] = rec.split("--");
					fileName = values[0];
					filePath = values[1];
					String destPath = "";
					destPath = filePath.replace("/"+tempFolderName+"/", "/"+storeSqid+"/");
					destPath = destPath.replace("/"+temprefNo+"/", "/"+refNo+"/");
				    sanFileReader.moveFile(filePath, destPath, fileName);
					jdbcTemplate.update("UPDATE MPS_SPOB_ADMN_ATCH_DETL SET  MAA_FILE_PATH=?, MAA_ENTY_REFN=?, MAA_ENTY_SREF=? WHERE "
							+ " MAA_INST_SQID=?  AND MAA_ENTY_REFN=? AND MAA_ENTY_SREF=? AND MAA_FILE_PATH=? "
							+ " AND MAA_FILE_NAME=? AND MAA_ATCH_STUS =? ",
							destPath, refNo, storeSqid, instId, (storeFlag != null && storeFlag == 1) ? refNo : temprefNo, tempFolderName, filePath, fileName, ATTACHED);
					values =null;destPath=null;
				}
				File tempFolder = new File(filePath);
			    if (tempFolder.exists() && tempFolder.isDirectory()) {
			        deleteFolder(tempFolder);
			    }
			} 

			sanFileReader.close();
			if( !"".equalsIgnoreCase(filePath) && filePath.trim().length()!=0) {
				if(totalStoreCount==storecount) {
					filePath = filePath.replace("/"+temprefNo+"/"+tempFolderName+"/", "");
					File folder = new File(filePath, temprefNo);
				    if (deleteFolder(folder)) {
				    	loggerUtil.info(temprefNo+" -- Folder deleted successfully.");
				    } else {
				    	loggerUtil.info(temprefNo+" -- Failed to delete folder.");
				    }
				}else {
					filePath = filePath.replace("/"+tempFolderName+"/", "");
					File folder = new File(filePath, tempFolderName);
				    if (deleteFolder(folder)) {
				    	loggerUtil.info(tempFolderName+" -- Folder deleted successfully.");
				    } else {
				    	loggerUtil.info(tempFolderName+" -- Failed to delete folder.");
				    }
				}
			}
			
		} catch (Exception e) {
			loggerUtil.error("Error while updating path "+ e.getMessage());
		}finally {
			fileList = null;			
			fileName = null;
			filePath = null;
			temprefNo = null;
			storeFlag = 0;
		}
	}	
	
	public static boolean deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
            return folder.delete();
        }
        return false;
    }
	@Override
	public void removeUnusedAttachmentsFolderForStore(String folderPath, String valueOf) {
		
		if (folderPath.endsWith("/")) {
			folderPath = folderPath.substring(0, folderPath.length() - 1);
	    }
		if(valueOf.equals("2")) {
			File dir = new File(folderPath);
	        
		    if (dir.exists() && dir.isDirectory()) {
		       Calendar calendar = Calendar.getInstance();
		       calendar.add(Calendar.DATE, -1);
		       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		       String yesterdayDate = sdf.format(calendar.getTime());

		       File[] files = dir.listFiles();
		       if (files != null) {
		          for (File file : files) {
		              if (file.isDirectory() && file.getName().length() == TEMPFOLDERNAME_LEN) {
		                  String lastModifiedDate = sdf.format(file.lastModified());
		                        
		                  if (lastModifiedDate.equals(yesterdayDate)) {
		                       deleteFolder(file);
		                       loggerUtil.info("Deleted folder: " + file.getAbsolutePath());
		                  }
		              }
		          }
		       }
		   }
		}
	}
	@Override
	public int checkSessionForMerchant(int instId, int refNo, int userId) {
		try {
			KeyValueBean bean = jdbcTemplate.queryForObject("SELECT MSM_SESN_FLAG, MSM_SESN_USER, CASE WHEN MSM_SESN_DATE >= (NOW() - INTERVAL '5 MINUTES') THEN 'VALID' "
					+ " ELSE 'INVALID' END AS MSM_SESN_DATE FROM MPS_SPOB_MERC_TEMP WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID = ? ", new RowMapper<KeyValueBean>() {
				        @Override
				        public KeyValueBean mapRow(ResultSet rs, int rowNum) throws SQLException {
				        	KeyValueBean bean = new KeyValueBean();
							bean.setKey(rs.getString("MSM_SESN_FLAG"));
							bean.setValue(rs.getString("MSM_SESN_DATE"));
							bean.setText(rs.getString("MSM_SESN_USER"));
							return bean;
				        }
				    },new Object[] { instId, refNo });
			
			if(bean!=null) {
				if(!Optional.ofNullable(bean.getKey()).orElse("").equalsIgnoreCase("0") && Optional.ofNullable(bean.getValue()).orElse("").equalsIgnoreCase("INVALID")) {
					jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_SESN_FLAG = ?, MSM_SESN_DATE = NOW(), MSM_SESN_USER = ? "
							+ " WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID = ? ", ACTIVE, userId, instId, refNo);
					return 0;
				}else if(Optional.ofNullable(bean.getText()).orElse("").equalsIgnoreCase(String.valueOf(userId))) {
					return 0;
				}
				return Integer.parseInt(Optional.ofNullable(bean.getKey()).orElse("2"));
			}
		}catch (Exception e) {
			loggerUtil.error("Error while fetching from checkSessionForMerchant() - "+e.getMessage());
		}
		return 2;
	}
	@Override
	public void updateSessionFlagForMerchant(int instId, int refNo, int status, int userId) {
		try {
			jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_SESN_FLAG = ?, MSM_SESN_DATE = NOW(), MSM_SESN_USER = ? "
					+ " WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID = ? ",  status, userId, instId, refNo);
		}catch (Exception e) {
			loggerUtil.error("Error while fetching from updateSessionFlagForMerchant() - "+e.getMessage());
		}
	}
	@Override
	public Integer checkSessionForMerchantActivity(int instId, int refNo, int userId) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) AS SESSIONFLAG "
				+" FROM MPS_SPOB_MERC_TEMP WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID = ? AND MSM_SESN_USER = ? AND MSM_SESN_FLAG = ? "
				+" AND MSM_SESN_DATE >= NOW() - INTERVAL '5 MINUTES' ", Integer.class, instId, refNo, userId, ACTIVE);
	}
	@Override
	public List<JSONObject> getAcctBranchList(int instId) throws Exception {
		List<net.sf.json.JSONObject> acctBranchList = null;
		StringBuilder strQry = new StringBuilder();
		try {
			strQry.append(" SELECT MAM_MEBR_CODE,MAM_MEBR_NAME FROM MPS_ADMN_MEBR_MAST WHERE MAM_INST_SQID = ? ");
			strQry.append(" AND MAM_MEBR_STUS = 1 ORDER BY MAM_MEBR_NAME ");

			acctBranchList = (List<net.sf.json.JSONObject>) jdbcTemplate.query(strQry.toString(),
				new RowMapper<net.sf.json.JSONObject>() {
					public net.sf.json.JSONObject mapRow(ResultSet rs, int rowNum) throws SQLException {
						net.sf.json.JSONObject json = new net.sf.json.JSONObject();
						json.put("value", rs.getString(1));
						json.put("desc", rs.getString(1) + " - " + rs.getString(2).trim());
						return json;
					}
				}, instId);
		} catch (Exception e) {
			loggerUtil.error("Error while fetching merchant account details "+ e.getMessage());
		}
		return acctBranchList;
	}
	@Override
	public MerchantOnboardBean getMerchantLogoDetails(MerchantOnboardBean merchantOnboardBean, Integer instId, Integer userId) {
		StringBuffer strQry = new StringBuffer();
		try {
			strQry.append(" SELECT MSM_MERC_LOGO FROM MPS_SPOB_MERC_TEMP WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID = ? ");
			
			jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
				public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
					  merchantOnboardBean.setMercLogoUpldData(rs.getBytes("MSM_MERC_LOGO"));
					  if(merchantOnboardBean.getMercLogoUpldData()!=null) {
						  merchantOnboardBean.setMercLogoDisplayData(Base64.getEncoder().encodeToString(merchantOnboardBean.getMercLogoUpldData())); 
					  }
					  return merchantOnboardBean;
				}
			}, instId, merchantOnboardBean.getRefNo());
		} catch (Exception e) {
			loggerUtil.error(" Error while fetching data from getMerchantLogoDetails() "+ e.getMessage());
		} finally {
			strQry = null;
		}
		return merchantOnboardBean;
	}
		@Override
	public MerchantOnboardBean getMerchantMastDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		StringBuffer strQry = new StringBuffer(
				"SELECT MMM_INST_SQID, MMM_REFR_SQID, MMM_MER_CODE, MMR_SLEN_SQID, "
				+" (SELECT MMS_SLEN_NAME FROM MPS_MERC_SLEN_DETL "
			    +" WHERE MMS_INST_SQID = MMM_INST_SQID AND MMS_SLEN_SQID = MMR_SLEN_SQID) AS MSM_SALE_EXEC,  "
				+" MMR_LEGL_NAME,MMR_DEFT_STNM,MMR_OWNR_NAME,MMR_MERC_AGGR, "
				+" (SELECT MAG_AGGR_NAME FROM MPS_ADMN_AGGR_MAST  "
				+"   WHERE MAG_AGGR_SQID = MMR_MERC_AGGR) AS MAG_AGGR_NAME,"
				+"(SELECT MAG_MERC_RELM FROM MPS_ADMN_AGGR_MAST  "
				+"   WHERE MAG_AGGR_SQID = MMR_MERC_AGGR) AS MAG_MERC_RELM, MMR_CUST_INDC, "
				+" MMM_PORT_INDC, MMM_MAPG_LEVL, MMM_MERC_LOGO,MMR_CUST_FNAM,MMR_CUST_LNAM, "
				+ "MMM_OWNR_TYPE, MMM_PRNT_ENTY, MMM_CNRY_INCP, MMM_DATE_INCP, MMM_UBO_PERC, MMM_INDV_NAME, "
				+ "MMM_BUSN_TYPE, MMM_EMRT_ID, MMM_EMRTID_EXPR, MMM_PASP_NMBR, MMM_PASP_EXPR, MMM_MERC_NATY, "
				+" MAA_ADDR_LIN1,MAA_ADDR_LIN2,MAA_CNRY_SQID,MAA_STAT_SQID,MAA_CITY_SQID, "
				+" MAA_PSTL_CODE,MAA_PHONE_NUM1,MAA_EMAL_ADID,MAA_BUSN_EMAL,MAA_MOBL_NUMB, "
				+" MAA_TECH_EMAL,MAA_TECH_MOBL,CX_DF_ED(MAA_UNIQ_IDNO,MMM_INST_SQID,?) AS MAA_UNIQ_IDNO, "
				+" MMM_ANIP_MERCODE, MMM_SCDP_FLAG, "
				+ " (SELECT MGV_MCC_CODE||'-'||CASE WHEN MGV_MCC_DESC is null THEN MGV_MCC_CODE "
		    	+ " ELSE UPPER(MGV_MCC_DESC) END AS MSM_MCC_CODE FROM MPS_GENR_VMCC_MAST "
		    	+ "	WHERE MGV_MCC_CODE = MMM_MCC_CODE) AS MMM_MCC_CODE "
				+" FROM MPS_MERC_MERH_DETL,MPS_MERC_REFR_DETL,MPS_ADMN_ADDR_MAST "
				+" WHERE MMR_INST_SQID = MMM_INST_SQID AND MMR_REFR_SQID = MMM_REFR_SQID "
				+" AND MAA_INST_SQID = MMM_INST_SQID AND MAA_ADDR_SQID = MMM_ALTR_ADID "
				+" AND MMM_INST_SQID = ? AND MMM_REFR_SQID = ?");

			try {
				jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
					public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
						  merchantOnboardBean.setMid(rs.getString("MMM_MER_CODE"));
						  merchantOnboardBean.setBankersalesNames(rs.getString("MSM_SALE_EXEC"));
						  merchantOnboardBean.setLegalname(rs.getString("MMR_LEGL_NAME"));
						  merchantOnboardBean.setDbaname(rs.getString("MMR_DEFT_STNM"));
						  merchantOnboardBean.setOwnername(rs.getString("MMR_OWNR_NAME"));
						  merchantOnboardBean.setMercAggr(rs.getString("MMR_MERC_AGGR"));
						  merchantOnboardBean.setMercAggrName(rs.getString("MAG_AGGR_NAME"));
						  merchantOnboardBean.setMercAggrrelm(rs.getString("MAG_MERC_RELM"));
						  merchantOnboardBean.setFirstname1(rs.getString("MMR_CUST_FNAM"));
						  merchantOnboardBean.setLastname1(rs.getString("MMR_CUST_LNAM"));
						  merchantOnboardBean.setAddressline1(rs.getString("MAA_ADDR_LIN1"));
						  merchantOnboardBean.setAddressline2(rs.getString("MAA_ADDR_LIN2"));
						  merchantOnboardBean.setCcountry(rs.getString("MAA_CNRY_SQID"));
						  merchantOnboardBean.setCstate(rs.getString("MAA_STAT_SQID"));
						  merchantOnboardBean.setCselectcity(rs.getString("MAA_CITY_SQID"));
						  merchantOnboardBean.setPostalcode(rs.getString("MAA_PSTL_CODE"));
						  merchantOnboardBean.setPhone(rs.getString("MAA_PHONE_NUM1"));
						  merchantOnboardBean.setContactEmail(rs.getString("MAA_EMAL_ADID"));
						  merchantOnboardBean.setCpancard(rs.getString("MAA_UNIQ_IDNO"));
						  merchantOnboardBean.setRefSqid(rs.getString("MMM_REFR_SQID")==null?null:Integer.parseInt(rs.getString("MMM_REFR_SQID")));
						  merchantOnboardBean.setAcctMapLvl(rs.getInt("MMM_MAPG_LEVL"));
						  merchantOnboardBean.setExistingCustomer(rs.getInt("MMR_CUST_INDC"));
						  merchantOnboardBean.setMerchantPortalRequired(rs.getInt("MMM_PORT_INDC"));
						  merchantOnboardBean.setBusinessContactEmailID(rs.getString("MAA_BUSN_EMAL"));
						  merchantOnboardBean.setBusinessContactMobileNo(rs.getString("MAA_MOBL_NUMB") != null && !rs.getString("MAA_MOBL_NUMB").trim().isEmpty() ? Long.parseLong(rs.getString("MAA_MOBL_NUMB").trim()) : null);
						  merchantOnboardBean.setTechnicalContactEmailID(rs.getString("MAA_TECH_EMAL"));
						  merchantOnboardBean.setTechnicalContactMobileNo(rs.getString("MAA_TECH_MOBL") != null && !rs.getString("MAA_TECH_MOBL").trim().isEmpty() ? Long.parseLong(rs.getString("MAA_TECH_MOBL").trim()) : null);
						  merchantOnboardBean.setMercLogoUpldData(rs.getBytes("MMM_MERC_LOGO"));
						  if(merchantOnboardBean.getMercLogoUpldData()!=null) {
							  merchantOnboardBean.setMercLogoDisplayData(Base64.getEncoder().encodeToString(merchantOnboardBean.getMercLogoUpldData())); 
						  }
						  //ownership details
						  merchantOnboardBean.setOwnerType(rs.getString("MMM_OWNR_TYPE")); 
						  merchantOnboardBean.setBusinessType(rs.getString("MMM_BUSN_TYPE")); 
						  merchantOnboardBean.setParentEntityName(rs.getString("MMM_PRNT_ENTY"));
						  merchantOnboardBean.setCountryOfIncorp(rs.getString("MMM_CNRY_INCP"));
						  merchantOnboardBean.setDateOfIncorp(rs.getString("MMM_DATE_INCP"));
						  if(merchantOnboardBean.getDateOfIncorp() != null && merchantOnboardBean.getDateOfIncorp().trim().length() != 0) {
							  try {
								  merchantOnboardBean.setDateOfIncorp(new SimpleDateFormat("dd/MM/yyyy")
										  .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(merchantOnboardBean.getDateOfIncorp())));
							  } catch (ParseException e) {
								  loggerUtil.error("Error while converting the Date format for Date of Incorporation - "+ e.getMessage());
							  }
				          }
						  merchantOnboardBean.setUboShareholderPctEntity(rs.getString("MMM_UBO_PERC"));
						  merchantOnboardBean.setIndividualName(rs.getString("MMM_INDV_NAME"));
						  merchantOnboardBean.setEmiratesIdNo(rs.getString("MMM_EMRT_ID"));
						  merchantOnboardBean.setEmiratesIdExpiry(rs.getString("MMM_EMRTID_EXPR"));
						  if(merchantOnboardBean.getEmiratesIdExpiry() != null && merchantOnboardBean.getEmiratesIdExpiry().trim().length() != 0) {
							  try {
								  merchantOnboardBean.setEmiratesIdExpiry(new SimpleDateFormat("dd/MM/yyyy")
										  .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(merchantOnboardBean.getEmiratesIdExpiry())));
							  } catch (ParseException e) {
								  loggerUtil.error("Error while converting the Date format for Emirates ID Expiry - "+ e.getMessage());
							  }
				          }
						  merchantOnboardBean.setPassportNumber(rs.getString("MMM_PASP_NMBR"));
						  merchantOnboardBean.setPassportExpiry(rs.getString("MMM_PASP_EXPR"));
						  if(merchantOnboardBean.getPassportExpiry() != null && merchantOnboardBean.getPassportExpiry().trim().length() != 0) {
							  try {
								  merchantOnboardBean.setPassportExpiry(new SimpleDateFormat("dd/MM/yyyy")
										  .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(merchantOnboardBean.getPassportExpiry())));
							  } catch (ParseException e) {
								  loggerUtil.error("Error while converting the Date format for Passport Expiry - "+ e.getMessage());
							  }
				          }
						  merchantOnboardBean.setNationality(rs.getString("MMM_MERC_NATY"));
						  merchantOnboardBean.setAaniPayMercID(rs.getString("MMM_ANIP_MERCODE"));
						  merchantOnboardBean.setSecurityDepositRecoveryRequired(rs.getInt("MMM_SCDP_FLAG"));
						  merchantOnboardBean.setMccmerchantautocomplete(rs.getString("MMM_MCC_CODE"));
						  merchantOnboardBean.setCmccMerchantAutoComplete(rs.getString("MMM_MCC_CODE")==null?null:rs.getString("MMM_MCC_CODE").split("-")[0]);
						  
						return merchantOnboardBean;
					}
				}, getClearDEK()!=null||!getClearDEK().isEmpty()?getClearDEK():null, instId, merchantOnboardBean.getRefNo());
			} catch (Exception e) {
				loggerUtil.error("Error in getMerchantMastDetails method ", e);
			}

			 merchantOnboardBean.setCountry(jdbcTemplate.queryForObject("SELECT MGC_CNRY_NAME FROM MPS_GENR_CNRY_MAST WHERE MGC_CNRY_SQID = ? ", String.class, merchantOnboardBean.getCcountry()));
			 merchantOnboardBean.setState(jdbcTemplate.queryForObject("SELECT MAS_STAT_NAME FROM MPS_ADMN_STAT_MAST WHERE MAS_STAT_SQID = ? ", String.class, merchantOnboardBean.getCstate()));
			 merchantOnboardBean.setSelectcity(jdbcTemplate.queryForObject("SELECT MAC_CITY_NAME FROM MPS_ADMN_CITY_MAST WHERE MAC_CITY_SQID = ? ", String.class, merchantOnboardBean.getCselectcity()));
			 merchantOnboardBean.setAdditionalNewStoreFlag(1);
			 
			 String entySqid = jdbcTemplate.queryForObject( " SELECT MMS_ENTY_SQID FROM MPS_MERC_STORE_DETL WHERE MMS_INST_SQID = ? AND MMS_REFR_SQID = ? AND ROWNUM = 1 ", String.class, instId, merchantOnboardBean.getRefNo());
			 merchantOnboardBean.setMerchantEntityName(Optional.ofNullable(entySqid).map(v -> Arrays.stream(v.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.joining(","))).orElse(null));
			  
		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean getPricingMastDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		StringBuffer strQry = new StringBuffer(" SELECT MMP_STOR_REQD,"
				+ " MMP_CAPT_MSFA,MMP_FPLN_TYPE,MMP_IFCT_SQID,MMP_FEE_WAVR "
				+ " FROM MPS_MERC_PRIC_DETL WHERE MMP_REFR_SQID = ? "); //AND MMP_ENTY_SQID = ?");

		jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
			public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
				merchantOnboardBean.setFeePlanType(rs.getString("MMP_FPLN_TYPE")==null?null:Integer.parseInt(rs.getString("MMP_FPLN_TYPE"))); 
				merchantOnboardBean.setMerchantServiceFeePlan(rs.getString("MMP_IFCT_SQID"));
				merchantOnboardBean.setNoOfStoresReq(rs.getString("MMP_STOR_REQD")==null?null:Integer.parseInt(rs.getString("MMP_STOR_REQD")));
				merchantOnboardBean.setCaptureServiceFeeAt(rs.getString("MMP_CAPT_MSFA"));
				merchantOnboardBean.setMercFeeDeduWaiver(rs.getInt("MMP_FEE_WAVR"));
				
				return merchantOnboardBean;
			}
		},merchantOnboardBean.getRefNo());//, merchantLevel);
		
		if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 1) {
			StringBuffer strQry1 = new StringBuffer(" SELECT MMS_DEPS_LIMT, MMS_CALC_TYPE, MMS_INDP_AMNT, MMS_INDP_PERC FROM MPS_MERC_SCDP_DETL WHERE MMS_REFR_SQID=? AND MMS_ACTIVE_FLAG = ?");
			
			jdbcTemplate.query(strQry1.toString(), new RowMapper<MerchantOnboardBean>() {
				public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
					merchantOnboardBean.setSecurityDepositLimit(rs.getInt("MMS_DEPS_LIMT"));
					//merchantOnboardBean.setSecurityDepositCalcType(rs.getString("MMS_CALC_TYPE"));
					merchantOnboardBean.setSdrIndc(rs.getString("MMS_CALC_TYPE").equals("F") ? 0 : 1);
					merchantOnboardBean.setSecurityDepositRecoveryAmount(rs.getLong("MMS_INDP_AMNT"));
					merchantOnboardBean.setSecurityDepositRecoveryPercentage(rs.getInt("MMS_INDP_PERC"));
					//merchantOnboardBean.setSdrIndc(merchantOnboardBean.getSecurityDepositCalcType().equals("F") ? 0 : 1);
					
					return merchantOnboardBean;
				}
			},merchantOnboardBean.getRefNo(), ACTIVE);
		} else if (merchantOnboardBean.getSecurityDepositRecoveryRequired() == 0) {
			StringBuffer strQry1 = new StringBuffer(" SELECT MMS_DEPS_LIMT FROM MPS_MERC_SCDP_DETL WHERE MMS_REFR_SQID=? AND MMS_ACTIVE_FLAG = ?");
			
			jdbcTemplate.query(strQry1.toString(), new RowMapper<MerchantOnboardBean>() {
				public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
					merchantOnboardBean.setSecurityDepositLimit(rs.getInt("MMS_DEPS_LIMT"));
					return merchantOnboardBean;
				}
			},merchantOnboardBean.getRefNo(), ACTIVE);
		}
		
		if(merchantOnboardBean.getFeePlanType()==1) {
			merchantOnboardBean.setMerchantServiceFeePlan(jdbcTemplate.queryForObject("SELECT MAF_FPLN_SQID||'-'||MAF_FPLN_NAME MAF_FPLN_DESC FROM MPS_ADMN_FPLN_HEAD WHERE MAF_FPLN_SQID = ? ", String.class, merchantOnboardBean.getMerchantServiceFeePlan()));
		}else if(merchantOnboardBean.getFeePlanType()==2) {
			merchantOnboardBean.setMerchantServiceFeePlan(jdbcTemplate.queryForObject("SELECT MAS_FPLN_SQID||'-'||MAS_FPLN_NAME MAS_FPLN_DESC from MPS_ADMN_SPLN_HEAD WHERE MAS_FPLN_SQID = ? ", String.class, merchantOnboardBean.getMerchantServiceFeePlan()));
		}

		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean getAccountMastDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_MERC_ACCT_DETL WHERE MMA_INST_SQID = ? AND MMA_REFR_SQID = ? "
				+ " AND MMA_MERC_SQID = ? AND MMA_ENTY_SQID = ? AND MMA_ACCT_STUS = ? ",
				Integer.class, instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getRefNo(), merchantLevel, ACTIVE);
		if(count > 0) {
			merchantOnboardBean.setMerchantAccountRequired(1);
			StringBuffer strQry = new StringBuffer(
					"SELECT MMA_DMAC_INDC,MMA_ACCT_BRAN,MMA_BANK_NAME,MMA_BRCH_NAME,MMA_IFSC_CODE, "
					+ " (SELECT MGC_CRCY_SQID||'-'||MGC_CRCY_DESC FROM MPS_GENR_CRCY_MAST "
					+ " WHERE MGC_CRCY_SQID = MMA_ACCT_CRCY) AS MMA_ACCT_CRCY, "
					+ " MMA_ACCT_NAME,MMA_ACCT_NUMB,MMA_DEFT_ACCT,MMA_CIF_NMBR"
					+ " FROM MPS_MERC_ACCT_DETL WHERE MMA_INST_SQID = ? AND MMA_REFR_SQID = ? "
					+ " AND MMA_MERC_SQID = ? AND MMA_ENTY_SQID = ? AND MMA_ACCT_STUS = ? ");
				jdbcTemplate.query(strQry.toString(), new RowMapper<MerchantOnboardBean>() {
					public MerchantOnboardBean mapRow(ResultSet rs, int arg1) throws SQLException {
						merchantOnboardBean.setAcctWithBank(rs.getString("MMA_DMAC_INDC")==null?null:Integer.parseInt(rs.getString("MMA_DMAC_INDC")));
						if(merchantOnboardBean.getAcctWithBank()==1) {
							merchantOnboardBean.setcAcctBranch(rs.getString("MMA_ACCT_BRAN")==null||rs.getString("MMA_ACCT_BRAN").trim().length()==0?rs.getString("MMA_BRCH_NAME"):rs.getString("MMA_ACCT_BRAN"));
							if(merchantOnboardBean.getcAcctBranch()!= null && merchantOnboardBean.getcAcctBranch().trim().length()!=0) {
								merchantOnboardBean.setAcctBranch(jdbcTemplate.queryForObject("SELECT MAM_MEBR_CODE || '-' || MAM_MEBR_NAME FROM MPS_ADMN_MEBR_MAST WHERE MAM_INST_SQID = ? AND MAM_MEBR_CODE = ? ", String.class, instId, merchantOnboardBean.getcAcctBranch()));
							}
						}
						merchantOnboardBean.setIfsc(rs.getString("MMA_IFSC_CODE"));
						merchantOnboardBean.setAcctNumber(rs.getString("MMA_ACCT_NUMB"));
						merchantOnboardBean.setCurrency(rs.getString("MMA_ACCT_CRCY"));
						merchantOnboardBean.setDefAccount(rs.getString("MMA_DEFT_ACCT")==null?null:Integer.parseInt(rs.getString("MMA_DEFT_ACCT")));
						merchantOnboardBean.setAccountname(rs.getString("MMA_ACCT_NAME"));
						merchantOnboardBean.setAcctBankText(rs.getString("MMA_BANK_NAME"));
						merchantOnboardBean.setAcctBranchText(rs.getString("MMA_BRCH_NAME"));
						merchantOnboardBean.setCifNumber(rs.getString("MMA_CIF_NMBR"));
						
						return merchantOnboardBean;
					}
				}, instId, merchantOnboardBean.getRefNo(), merchantOnboardBean.getRefNo(), merchantLevel, ACTIVE);
		} else {
			merchantOnboardBean.setMerchantAccountRequired(0);
		}

		return merchantOnboardBean;
	}
	@Override
	public void rollBackFunctionForAddnewStore(MerchantOnboardBean merchantOnboardBean) {
		try
		{
			if(merchantOnboardBean.getRefSqid() != 0)
			{
				jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_STOR_FLAG = 0 WHERE MSM_REFR_SQID = ? ",  merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("DELETE FROM MPS_SPOB_STOR_TEMP WHERE MSS_REC_STUS = 1 AND MSS_REFR_SQID = ? ", merchantOnboardBean.getRefSqid());
				jdbcTemplate.update("DELETE FROM MPS_SPOB_TERM_TEMP WHERE MST_REC_STUS = 1 AND MST_REFR_SQID = ? ", merchantOnboardBean.getRefSqid());
			}
		}catch(Exception e){
			loggerUtil.error("Error while rollback add new store details "+ e.getMessage());
		}	
	}
	@Override
	public void updateAddnewStoreFlag(MerchantOnboardBean merchantOnboardBean, Integer status, Integer userId, Integer instId) {
		if(status==0) {
			jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_STOR_FLAG = ?, MSM_ERR_DESC = ?, MSM_INST_USER = ?, MSM_LUPD_USER = ? WHERE MSM_REFR_SQID = ? ",  
					status, merchantOnboardBean.getRemarks()!=null && merchantOnboardBean.getRemarks()!="" ? "MERCHANT ONBOARDED - " + merchantOnboardBean.getRemarks() : "MERCHANT ONBOARDED", 
					userId, userId, merchantOnboardBean.getRefNo()==null?merchantOnboardBean.getRefSqid():merchantOnboardBean.getRefNo());
			Integer totalStoreCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_SPOB_STOR_TEMP WHERE MSS_REFR_SQID = ? AND MSS_REC_STUS IN (5) ", Integer.class, merchantOnboardBean.getRefNo()==null?merchantOnboardBean.getRefSqid():merchantOnboardBean.getRefNo());
			Integer maxNoOfStoreReq = jdbcTemplate.queryForObject("SELECT NVL((SELECT MSP_STOR_REQD FROM MPS_SPOB_PRIC_TEMP WHERE MSP_ENTY_SQID = 1 AND MSP_REFR_SQID = ?),0) ", Integer.class, merchantOnboardBean.getRefNo()==null?merchantOnboardBean.getRefSqid():merchantOnboardBean.getRefNo());
			if(maxNoOfStoreReq<=totalStoreCount) {
				jdbcTemplate.update("UPDATE MPS_SPOB_PRIC_TEMP SET MSP_STOR_REQD = ? WHERE MSP_ENTY_SQID = ? AND MSP_REFR_SQID = ? ", totalStoreCount, merchantLevel, merchantOnboardBean.getRefNo()==null?merchantOnboardBean.getRefSqid():merchantOnboardBean.getRefNo());
			}
			
			//add new store & terminal update -- Main Table
			Integer storeCnt = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_MERC_MERH_DETL M, MPS_MERC_STORE_DETL S " 
					+ " WHERE M.MMM_REFR_SQID = S.MMS_REFR_SQID AND M.MMM_MCUR_STUS = 1 AND MMS_INST_SQID = ? AND M.MMM_REFR_SQID IN (?)",
	               Integer.class,instId,merchantOnboardBean.getRefNo());

	        Integer termCnt = jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM MPS_MERC_MERH_DETL M, MPS_MERC_STORE_DETL S, MPS_MERC_TERM_DETL T " 
				+ " WHERE M.MMM_REFR_SQID = S.MMS_REFR_SQID AND S.MMS_STOR_SQID = T.MMT_STOR_SQID AND M.MMM_MCUR_STUS = 1 AND MMS_INST_SQID = ? AND M.MMM_REFR_SQID IN (?)",
	               Integer.class,instId, merchantOnboardBean.getRefNo());

				//Store activate
				if (storeCnt != null && storeCnt > 0) {
				jdbcTemplate.update("UPDATE MPS_MERC_STORE_DETL SET MMS_SCUR_STUS = ?, MMS_LUPD_USID = ?, MMS_LUPD_SYDT = SYSDATE "
							+ " WHERE MMS_INST_SQID = ? AND MMS_REFR_SQID IN (?)", 1,userId,instId, merchantOnboardBean.getRefNo());
				}
				
				//Terminal activate
				if (termCnt != null && termCnt > 0) {
				jdbcTemplate.update("UPDATE MPS_MERC_TERM_DETL SET MMT_CURR_STUS = ?, MMT_LUPD_USID = ?, MMT_LUPD_SYDT = SYSDATE "
							+ " WHERE MMT_INST_SQID = ? AND MMT_STOR_SQID "
							+ " IN (SELECT MMS_STOR_SQID FROM MPS_MERC_STORE_DETL WHERE MMS_REFR_SQID IN (?))",
				              1,userId,instId,merchantOnboardBean.getRefNo());
				}
				
		} else {
			jdbcTemplate.update("UPDATE MPS_SPOB_MERC_TEMP SET MSM_STOR_FLAG = ?, MSM_ERR_DESC = ?, MSM_INST_USER = ?, MSM_LUPD_USER = ? WHERE MSM_REFR_SQID = ? ",
					status, merchantOnboardBean.getRemarks(), userId, userId, merchantOnboardBean.getRefNo()==null?merchantOnboardBean.getRefSqid():merchantOnboardBean.getRefNo());
		}
		
	}
	@Override
	public MerchantOnboardBean getMerchantRemarksDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		merchantOnboardBean.setRemarks(jdbcTemplate.queryForObject("SELECT MSM_ERR_DESC FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", String.class, merchantOnboardBean.getRefSqid()));
		return merchantOnboardBean;
	}
	@Override
	public Integer getAdditionalNewStoreFlag(Integer instId, int refno, Integer userId) {
		return jdbcTemplate.queryForObject("SELECT MSM_STOR_FLAG FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", Integer.class, refno);
	}
	@Override
	public String getUserNameUsingID(int userId) {
		return jdbcTemplate.queryForObject("SELECT MAD_USER_NAME FROM MPS_AUTH_DETL_MAST WHERE MAD_USER_SQID = ? ", 
				String.class, userId);
	}
	@Override
	public String getUserEmailID(int userId) {
		return jdbcTemplate.queryForObject("SELECT MAD_USER_EMAL FROM MPS_AUTH_DETL_MAST WHERE MAD_USER_SQID = ? ", 
				String.class, userId);
	}
	@Override
	public String getMakerEmailIDusingRefno(String refNo) {
		return jdbcTemplate.queryForObject("SELECT MAD_USER_EMAL FROM MPS_AUTH_DETL_MAST WHERE MAD_USER_SQID = ( "
				+ " Select MSM_INST_USER from MPS_SPOB_MERC_TEMP where MSM_REFR_SQID = ? ) ", 
				String.class, refNo);
	}
	@Override
	public String getlistCheckerEmailIDs(int userId) {
		return jdbcTemplate.queryForObject("SELECT STRING_AGG(DISTINCT USEREMAIL, ',') AS EMAILID "
				+ "FROM (SELECT DISTINCT USERTABLE.MAD_USER_SQID USERSQID ,USERTABLE.MAD_USER_EMAL USEREMAIL "
				+ "	FROM MPS_AUTH_GRPG_MAST PROG, MPS_AUTH_USGP_MAST USGP, "
				+ "		MPS_AUTH_DETL_MAST USERTABLE, MPS_AUTH_GRUP_MAST GRUP "
				+ "	WHERE PROG.MAG_GRUP_SQID= USGP.MAU_GRUP_SQID "
				+ "		AND PROG.MAG_INST_SQID = USGP.MAU_INST_SQID "
				+ "		AND USGP.MAU_USER_SQID = USERTABLE.MAD_USER_SQID "
				+ "		AND USGP.MAU_GRUP_SQID = GRUP.MAG_GRUP_SQID "
				+ "		AND GRUP.MAG_GRUP_TYPE= ? "
				+ "		AND PROG.MAG_PROG_SQID =( SELECT MAP_PROG_SQID FROM MPS_AUTH_PROG_MAST "
				+ "			WHERE MAP_MENU_PATH = ? ) "
				+ "		AND USGP.MAU_USGP_STUS = ? "
				+ "		AND USERTABLE.MAD_USER_STUS= ? "
				+ "		AND USERTABLE.MAD_USER_SQID NOT IN (?) "
				+ "	) ", 
				String.class, GROUPTYPE, MERCHANTONBOARDCHECKERSURL, ACTIVE, ACTIVE, userId);
	}
	@Override
	public String getMerchantIDusingRefno(String refNo) {
		return jdbcTemplate.queryForObject("SELECT MSM_MERC_CODE FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", String.class, refNo);
	}
	@Override
	public String getMerchantEmailIDusingRefno(String refNo) {
		return jdbcTemplate.queryForObject("SELECT MSM_CUST_EMAL FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", String.class, refNo);
	}
	
	public Integer getDuplicateCount(String mercCode, String custEmail, String panNumber) {
	    return jdbcTemplate.queryForObject("SELECT NVL((SELECT COUNT(*) FROM MPS_BULK_UPLD_TEMP WHERE MBU_MER_CODE = ? AND MBU_CUST_EMAL = ? AND MBU_CUST_PAN = ?),0) FROM DUAL", 
				Integer.class, mercCode, custEmail, panNumber);
	}
	
	/**
	 * @return clearDEK
	 * @throws Exception
	 */
	@Override
	public String getClearDEK() throws Exception {
		String clearDEK = "";
		try {
//			clearDEK = azureKeyConfigUtils.getKEKandDEKfromKeyVault();
			clearDEK = "UFAQEFBQUFAQEFBQUFAQEFBQUFAQEFBQ";
			if (clearDEK == null || clearDEK.isEmpty()) {
				loggerUtil.error("Key retrieval failed: Empty or null value returned from Azure Key Vault");
				throw new Exception("Key retrieval failed: Empty or null value returned from Azure Key Vault");
			}
			int keyLength = clearDEK.length();
			if (keyLength != 32 && keyLength != 64) {
				loggerUtil.error("Invalid DEK length: " + keyLength + " characters (expected 32 or 64)");
				throw new Exception("Invalid DEK length: " + keyLength + " characters (expected 32 or 64)");
			}
		} catch (Exception e) {
			loggerUtil.error("Error while fetching key details from Azure Key Vault: " + e.getMessage(), e);
			throw new Exception("Error retrieving key from Azure Key Vault", e);
		}
		return clearDEK;
	}
	/*@Override
	public void getDeleteDuplicateDetails(String mercCode, String custEmail, String custPan) {
		jdbcTemplate.update("DELETE FROM MPS_BULK_UPLD_TEMP WHERE MBU_MER_CODE = ? AND MBU_CUST_EMAL = ? AND MBU_CUST_PAN = ? AND ", mercCode, custEmail, custPan);
	}
	@Override
	public Integer getDuplicateLogDetails(String mercCode, String custEmail, String custPan) {
		return jdbcTemplate.queryForObject("SELECT MBU_APPL_NMBR FROM MPS_BULK_UPLD_TEMP WHERE MBU_MER_CODE = ? AND MBU_CUST_EMAL = ? AND MBU_CUST_PAN = ?", 
				Integer.class, mercCode, custEmail, panNumber);
	}*/
	
	@Override
	public void getDeleteDuplicateDetails(String mercCode, String custEmail, String custPan) throws Exception {
	    String selectQuery = "SELECT MBU_APPL_NMBR FROM MPS_BULK_UPLD_TEMP " +
	                         "WHERE MBU_MER_CODE = ? AND MBU_CUST_EMAL = ? AND MBU_CUST_PAN = ?";

	    try {
	        List<String> applNumbers = jdbcTemplate.queryForList(
	                selectQuery, new Object[]{mercCode, custEmail, custPan}, String.class);

	        if (applNumbers != null && !applNumbers.isEmpty()) {
	            for (String applNum : applNumbers) {
	                jdbcTemplate.update("DELETE FROM MPS_BULK_UPLD_TEMP WHERE MBU_APPL_NMBR = ?", applNum);
	                jdbcTemplate.update("DELETE FROM MPS_SPOB_BULK_LOG WHERE MSB_APPL_NMBR = ?", applNum);
	            }
	        }

	    } catch (Exception e) {
	        loggerUtil.error("Error while deleting duplicate details for mercCode: "+ e.getMessage(), e);
	        throw new Exception(e);
	    }
	}
	@Override
	public MerchantOnboardBean activateMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		int output = 0;
		StringBuilder strQry = null;
		try 
		{
			strQry = new StringBuilder(" UPDATE MPS_MERC_MERH_DETL SET MMM_MCUR_STUS = ?, "
					+ "MMM_LUPD_USID = ?, MMM_LUPD_SYDT = SYSDATE WHERE MMM_INST_SQID = ? AND MMM_REFR_SQID IN ( ? ) ");
			output = jdbcTemplate.update(strQry.toString(),1,merchantOnboardBean.getUserId(), instId, merchantOnboardBean.getRefNo());
			if (output > 0) {
				merchantOnboardBean.setFlag("Activated");
				jdbcTemplate.update(" UPDATE MPS_SPOB_MERC_TEMP SET MSM_ERR_DESC = ?, MSM_LUPD_USER = ?, MSM_LUPD_INDT = SYSDATE WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID IN ( ? ) ",
						" Merchant - "+merchantOnboardBean.getFlag(),merchantOnboardBean.getUserId(),instId,merchantOnboardBean.getRefNo());
				
	            Integer storeCnt = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_MERC_MERH_DETL M, MPS_MERC_STORE_DETL S " 
	            					+ " WHERE M.MMM_REFR_SQID = S.MMS_REFR_SQID AND MMS_INST_SQID = ? AND M.MMM_REFR_SQID IN (?)",
	                               Integer.class,instId,merchantOnboardBean.getRefNo());

	            Integer termCnt = jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM MPS_MERC_MERH_DETL M, MPS_MERC_STORE_DETL S, MPS_MERC_TERM_DETL T " 
	            				+ " WHERE M.MMM_REFR_SQID = S.MMS_REFR_SQID AND S.MMS_STOR_SQID = T.MMT_STOR_SQID AND MMS_INST_SQID = ? AND M.MMM_REFR_SQID IN (?)",
	                               Integer.class,instId, merchantOnboardBean.getRefNo());

	            //Store activate
	            if (storeCnt != null && storeCnt > 0) {
	                jdbcTemplate.update("UPDATE MPS_MERC_STORE_DETL SET MMS_SCUR_STUS = ?, MMS_LUPD_USID = ?, MMS_LUPD_SYDT = SYSDATE "
	                			+ " WHERE MMS_INST_SQID = ? AND MMS_REFR_SQID IN (?)", 1,merchantOnboardBean.getUserId(),instId, merchantOnboardBean.getRefNo());
	            }

	            //Terminal activate
	            if (termCnt != null && termCnt > 0) {

	                jdbcTemplate.update("UPDATE MPS_MERC_TERM_DETL SET MMT_CURR_STUS = ?, MMT_LUPD_USID = ?, MMT_LUPD_SYDT = SYSDATE "
	                			+ " WHERE MMT_INST_SQID = ? AND MMT_STOR_SQID "
	                			+ " IN (SELECT MMS_STOR_SQID FROM MPS_MERC_STORE_DETL WHERE MMS_REFR_SQID IN (?))",
	                              1,merchantOnboardBean.getUserId(),instId,merchantOnboardBean.getRefNo());
	            }

				
			} else {
				merchantOnboardBean.setFlag("Failed");
				merchantOnboardBean.setErrorDesc("ERROR");
			}
		} catch (Exception e) {
			loggerUtil.error("activateMerchantOnBoard - error occured"+ e.getMessage());
			merchantOnboardBean.setErrorDesc("ERROR");
		} finally {
			output = 0;
			strQry = null;
		}
		return merchantOnboardBean;
	}
	@Override
	public MerchantOnboardBean deactivateMerchantOnBoard(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		int output = 0;
		StringBuilder strQry = null;
		try 
		{
			strQry = new StringBuilder(" UPDATE MPS_MERC_MERH_DETL SET MMM_MCUR_STUS = ?, "
					+ "MMM_LUPD_USID = ?, MMM_LUPD_SYDT = SYSDATE WHERE MMM_INST_SQID = ? AND MMM_REFR_SQID IN ( ? ) ");
			//MMM_CUR_STUS = 2 && MSM_REC_STUS = 9 --> Deactivate Merchant
			output = jdbcTemplate.update(strQry.toString(),2,merchantOnboardBean.getUserId(), instId, merchantOnboardBean.getRefNo());
			if (output > 0) {
				merchantOnboardBean.setFlag("Deactivated");
				jdbcTemplate.update(" UPDATE MPS_SPOB_MERC_TEMP SET MSM_ERR_DESC = ?, MSM_REC_STUS = ?, MSM_LUPD_USER = ?, MSM_LUPD_INDT = SYSDATE WHERE MSM_INST_SQID = ? AND MSM_REFR_SQID IN ( ? ) ",
						" Merchant - "+merchantOnboardBean.getFlag(),9,merchantOnboardBean.getUserId(),instId,merchantOnboardBean.getRefNo());
			} else {
				merchantOnboardBean.setFlag("Failed");
				merchantOnboardBean.setErrorDesc("ERROR");
			}
		} catch (Exception e) {
			loggerUtil.error("deactivateMerchantOnBoard - error occured"+ e.getMessage());
			merchantOnboardBean.setErrorDesc("ERROR");
		} finally {
			output = 0;
			strQry = null;
		}
		return merchantOnboardBean;
	}
	@Override
	public int getRecStatusByRefId(Integer instId, int refno) {
		return jdbcTemplate.queryForObject("SELECT MSM_REC_STUS FROM MPS_SPOB_MERC_TEMP WHERE MSM_REFR_SQID = ? ", Integer.class, refno);
	}
	@Override
	public int getMainStatusByRefId(Integer instId, int refno) {
		try {
			Integer status = jdbcTemplate.queryForObject("SELECT MMM_MCUR_STUS FROM MPS_MERC_MERH_DETL where MMM_REFR_SQID = ? ", Integer.class, refno);
			return (status != null) ? status : 0;
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			loggerUtil.error("Error fetching status for RefNo: " + refno, e);
	        return 0;
		}
	}
	
	public Integer activeMerchantCount(String refNo) {
        int count = 0;
	    try {
	        count = jdbcTemplate.queryForObject(" SELECT COUNT(*) FROM MPS_MERC_MERH_DETL WHERE MMM_REFR_SQID = ? AND  MMM_MCUR_STUS NOT IN ( 0 ) ", 
	        		               new Object[]{refNo}, Integer.class);
	    } catch (Exception e) {
	        loggerUtil.error("Error checking active merchant for RefNo " + refNo + " - " + e.getMessage());
	    }
	    return count;
	 }
	
	@Override
	public Integer getNoOfStoresReqCheck(Integer instId, MerchantOnboardBean bean) {
		return jdbcTemplate.queryForObject("SELECT MMP_STOR_REQD FROM MPS_MERC_MERH_DETL,MPS_MERC_PRIC_DETL "
				+ " WHERE MMM_REFR_SQID = MMP_REFR_SQID AND MMM_MCUR_STUS = 1 "
				+ " AND MMP_ENTY_SQID = 1 AND MMM_REFR_SQID = ?", 
				Integer.class, bean.getRefSqid());
	}
	@Override
	public List<KeyValueBean> getMerchantEntityNameList(Integer instId, String merchantEntityName) {
	    List<KeyValueBean> merchantEntityNameList = new ArrayList<>();
	    StringBuilder query = null;
	    try {
			String[] parts = Arrays.stream(merchantEntityName.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toArray(String[]::new);

			query = new StringBuilder("SELECT MTS_TXSR_SQID, MTS_TXSR_DESC FROM MPS_TXNS_SRCE_MAST "
					+ "WHERE MTS_INST_SQID = ? AND MTS_TXSR_STUS = ? " + "AND MTS_TXSR_SQID IN ("
					+ Stream.generate(() -> "?").limit(parts.length).collect(Collectors.joining(", ")) + ")");

	        merchantEntityNameList = jdbcTemplate.query(query.toString(), new RowMapper<KeyValueBean>() {
	            @Override
	            public KeyValueBean mapRow(ResultSet rs, int indx) throws SQLException {
	                KeyValueBean bean = new KeyValueBean();
	                bean.setKey(rs.getString("MTS_TXSR_SQID"));
	                bean.setValue(rs.getString("MTS_TXSR_DESC"));
	                return bean;
	            }
	        }, Stream.concat(Arrays.asList(instId, ACTIVE).stream(), Arrays.asList(parts).stream()).collect(Collectors.toList()).toArray());
	    } catch (Exception e) {
	        loggerUtil.error("Error while fetching getMerchantEntityNameList details " + e.getMessage());
	    }
	    return merchantEntityNameList;
	}
	@Override
	public boolean checkExistingMID(Integer instId, MerchantOnboardBean bean) {
	    return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MPS_MERC_MERH_DETL WHERE MMM_INST_SQID = ? AND MMM_MER_CODE = ? ", Integer.class, instId, bean.getMid()) > 0;
	}

	@Override
	public boolean checkExistingSID(Integer instId, MerchantOnboardStoreBean storeBean) {
	    return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MPS_MERC_STORE_DETL WHERE MMS_INST_SQID = ? AND MMS_STOR_CODE = ? ", Integer.class, instId, storeBean.getStoreCode()) > 0;
	}

	@Override
	public boolean checkExistingTID(Integer instId, MerchantOnboardTerminalBean terminalBean) {
	    return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MPS_MERC_TERM_DETL WHERE MMT_INST_SQID = ? AND MMT_TERM_CODE = ?", Integer.class, instId, terminalBean.getTermCode()) > 0;
	}

	@Override
	public boolean checkExistingAaniPayMID(Integer instId, MerchantOnboardBean bean) {
		return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MPS_MERC_MERH_DETL WHERE MMM_INST_SQID = ? AND MMM_ANIP_MERCODE = ?", Integer.class, instId, bean.getAaniPayMercID()) > 0;
	}
	
	@Override
	public boolean checkExistingAaniPaySID(Integer instId, MerchantOnboardStoreBean storeBean) {
		return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MPS_MERC_STORE_DETL WHERE MMS_INST_SQID = ? AND MMS_ANIP_STORCODE = ?", Integer.class, instId, storeBean.getAaniPayStoreID()) > 0;
	}
	
	@Override
	public boolean checkExistingAaniPayTID(Integer instId, MerchantOnboardTerminalBean bean) {
	    return jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MPS_MERC_TERM_DETL WHERE MMT_INST_SQID = ? AND MMT_ANIP_TERMCODE = ?", Integer.class, instId, bean.getAaniPayTermID()) > 0;
	}
	
/*	@Override
	public boolean checkExistingMerchantHierarchy(Integer instId, MerchantOnboardBean bean) {

	    List<String> mids = new ArrayList<>(), sids = new ArrayList<>(), tids = new ArrayList<>();
	    List<String> aaniPayMids = new ArrayList<>(), aaniPaySids = new ArrayList<>(), aaniPayTids = new ArrayList<>();

	    mids.add(bean.getMid());
	    aaniPayMids.add(bean.getAaniPayMercID());

	    for (MerchantOnboardStoreBean store : bean.getLstStoreBean()) {
	        sids.add(store.getStoreCode());
	        aaniPaySids.add(store.getAaniPayStoreID());

	        for (MerchantOnboardTerminalBean terminal : store.getLstTerminalBean()) {
	            tids.add(terminal.getTermCode());
	            aaniPayTids.add(terminal.getAaniPayTermID());
	        }
	    }

	    Integer mercCount = jdbcTemplate.queryForObject(" SELECT COUNT(1) FROM MPS_MERC_MERH_DETL WHERE MMM_INST_SQID = ? " +
	    		" AND (MMM_MER_CODE = ANY(string_to_array(?, ',')) OR MMM_ANIP_MERCODE = ANY(string_to_array(?, ',')))", Integer.class, instId, String.join(",", mids), String.join(",", aaniPayMids));
	    Integer storeCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MPS_MERC_STORE_DETL WHERE MMS_INST_SQID = ? " +
                "AND (MMS_STOR_CODE = ANY(string_to_array(?, ',')) OR MMS_ANIP_STORCODE = ANY(string_to_array(?, ',')))", Integer.class, instId, String.join(",", sids), String.join(",", aaniPaySids));
	    Integer termCount = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM MPS_MERC_TERM_DETL WHERE MMT_INST_SQID = ? " +
                "AND (MMT_TERM_CODE = ANY(string_to_array(?, ',')) OR MMT_ANIP_TERMCODE = ANY(string_to_array(?, ',')))", Integer.class, instId, String.join(",", tids), String.join(",", aaniPayTids));

	    return mercCount > 0 || storeCount > 0 || termCount > 0;
	}*/
	
	@Override
	public List<MerchantOnboardBean> getConsentRecordList(MerchantOnboardBean merchantOnboardBean) {
		 List<MerchantOnboardBean> list = null;
		 StringBuilder strQry = new StringBuilder();
		 try {
		        strQry.append(" SELECT MAM_CNST_SEQ, MAM_MERC_CODE, MAM_MERC_NAME, MAM_CNST_EMAIL, ")
	                  .append(" TO_CHAR(MAM_MAIL_SENT_DT, 'DD-MM-YYYY') SEND_DT, ")
	                  .append(" TO_CHAR(MAM_EXP_DT, 'DD-MM-YYYY') EXP_DT, ")
	                  .append(" CASE WHEN UPPER(MAM_CSNT_STUS) = 'APPROVED' THEN 'APPROVED' ")
	                  .append(" WHEN UPPER(MAM_CSNT_STUS) = 'REJECTED' THEN 'REJECTED' ")
	                  .append(" WHEN MAM_EXP_DT < SYSDATE THEN 'EXPIRED' ELSE 'PENDING' ")
	                  .append(" END AS CONSENT_STATUS FROM MPS_EMAIL_AUDT_LOG ")
	                  .append(" ORDER BY MAM_CNST_SEQ DESC ");

	        list = jdbcTemplate.query(strQry.toString(),new RowMapper<MerchantOnboardBean>() {

	            public MerchantOnboardBean mapRow(ResultSet rs,int rowNum) throws SQLException {
	                MerchantOnboardBean bean = new MerchantOnboardBean();
	                String consentStatus = rs.getString("CONSENT_STATUS");
	                bean.setCsntSeq(rs.getInt("MAM_CNST_SEQ"));
	                bean.setCsntMercID(rs.getString("MAM_MERC_CODE"));
	                bean.setCsntMercName(rs.getString("MAM_MERC_NAME"));
	                bean.setCsntEmail(rs.getString("MAM_CNST_EMAIL"));
	                bean.setCsntMailSentDt(rs.getString("SEND_DT"));
	                bean.setCsntMailExpDt(rs.getString("EXP_DT"));
	                bean.setCsntStus(consentStatus);
	                if ("PENDING".equalsIgnoreCase(consentStatus)
	                    || "EXPIRED".equalsIgnoreCase(consentStatus)
	                    || "REJECTED".equalsIgnoreCase(consentStatus)) {

	                    bean.setResendBtn( "<INPUT type='button' value='RESEND' "
	                      + " onclick=\"fn_resendConsent('"+ rs.getString("MAM_MERC_CODE")+ "');\""
	                      + " style='background: #9b1e2f !important;' />");

	                } else {
	                    bean.setResendBtn("&nbsp;");  
	                }
	                return bean;
	            }
	        });
		} catch (Exception e) {
			loggerUtil.error("Error occured in getConsentRecordList() - "+e.getMessage());
		} finally {
			strQry = null;
		}		
		return list;
	}
	
	@Override
	public Integer getBankerSales(Integer instId, String bankersalesNames) {
	    return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_MERC_SLEN_DETL a JOIN MPS_MERC_RFSC_MAST b ON b.MMR_RFSC_SQID = a.MMS_RFSC_SQID WHERE a.MMS_INST_SQID = ? "
	    		+ "AND UPPER(a.MMS_SLEN_NAME) LIKE UPPER(?)", Integer.class, instId, bankersalesNames);
	}
	@Override
	public Integer getOnboardedMerchantMastDetails(MerchantOnboardBean merchantOnboardBean, Integer instId) {
		return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MPS_MERC_MERH_DETL WHERE MMM_INST_SQID = ? AND MMM_REFR_SQID = ? AND MMM_MCUR_STUS = 1", Integer.class, instId, merchantOnboardBean.getRefNo());
	}
	@Override
	public boolean isFileNameExists(String uploadFileName, Integer instId) {
		return jdbcTemplate.queryForObject("SELECT COUNT(1) AS CNT FROM MPS_BULK_UPLD_TEMP WHERE MBU_FILE_NAME = ? AND MBU_INST_SQID = ?", Integer.class, uploadFileName, instId) > 0;
	}
}