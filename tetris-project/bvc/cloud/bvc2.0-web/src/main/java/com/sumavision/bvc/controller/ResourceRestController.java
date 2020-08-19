package com.sumavision.bvc.controller;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suma.venus.resource.base.bo.BundleBody;
import com.suma.venus.resource.bo.CreateBundleRequest;
import com.suma.venus.resource.bo.CreateBundleResponse;
import com.suma.venus.resource.bo.DeleteBundleRequest;
import com.suma.venus.resource.bo.DeleteBundleResponse;
import com.suma.venus.resource.bo.UpdateBundleRequest;
import com.suma.venus.resource.bo.UpdateBundleResponse;
import com.suma.venus.resource.service.BundleService;
import com.suma.venus.resource.service.ResourceService;
import com.sumavision.bvc.common.RestResult;
import com.sumavision.bvc.common.RestResultGenerator;
import com.sumavision.bvc.feign.ResourceRemoteService;
import com.sumavision.bvc.repository.ResourceRespository;
import com.sumavision.bvc.vo.ResourceVO;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/api/resource")
@Slf4j
public class ResourceRestController {
	@Autowired
	ResourceRespository resourceRespository;

	@Autowired
	private ResourceService resourceService;

	@Autowired
	ResourceRemoteService resourceRemoteService;
	
	@Autowired
	BundleService bundleService;
	
	private static String excelHeader[] = { "资源名称", "资源类型","设备账号", "设备密码", "字冠/号码",
			"经度", "纬度","协议类型"};
	
	private static int rowElementsNum = 7;

	@RequestMapping("")
	public String resource() {
		return "resource";
	}

	@RequestMapping("/list")
	@ResponseBody
	public RestResult<String> listResource(@RequestParam("bundle_name") String bundle_name, @RequestParam Integer page) {
		List<BundleBody> bundles = resourceService.querybundleInfos("ipc");
		if(!StringUtils.isEmpty(bundle_name)){
			Iterator<BundleBody> iterator = bundles.iterator();
			while(iterator.hasNext()){
				BundleBody bundleBody = iterator.next();
				if(bundleBody.getBundle_name() == null || !bundleBody.getBundle_name().contains(bundle_name)){
					iterator.remove();
				}
			}
		}
		List<ResourceVO> resourceVOs= (bundles == null) ? null :bundles.stream()
															    .map(a-> ResourceVO.parseResourVOfromBundleBody(a))
															    .collect(toList());
		JSONArray resourceArray = JSONArray.parseArray(JSON.toJSONString(resourceVOs));
		
		JSONObject retDataJson = new JSONObject();
		retDataJson.put("resourceList", resourceArray.toString());
		return RestResultGenerator.genResult(true, retDataJson.toString(), "ok");
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public RestResult<String> createBuddle(ResourceVO resourceVO) {
		CreateBundleRequest createBundleRequest = resourceVO.ConvertToCreateBundleRequest(resourceVO);
		CreateBundleResponse createBundleResponse = resourceRemoteService.createBundle(createBundleRequest);
		return RestResultGenerator.genResult(true,JSONObject.toJSON(createBundleResponse).toString(), "ok");

	}

	@RequestMapping("/edit")
	@ResponseBody
	public RestResult<String> updateBundle(ResourceVO resourceVO) {
		UpdateBundleRequest  updateBundleRequest= resourceVO.ConvertToUpadateBundleRequest(resourceVO);
		UpdateBundleResponse updateBundleResponse = resourceRemoteService.updateBundle(updateBundleRequest);
		return RestResultGenerator.genResult(true,JSONObject.toJSON(updateBundleResponse).toString(), "ok");	
	}

	@RequestMapping("/delete")
	@ResponseBody
	public RestResult<String> deleteBundle(ResourceVO resourceVO) {
		DeleteBundleRequest  deleteBundleRequest= resourceVO.ConvertToDeleteBundleRequest(resourceVO);
		DeleteBundleResponse updateBundleResponse = resourceRemoteService.deleteBundle(deleteBundleRequest);
		return RestResultGenerator.genResult(true,JSONObject.toJSON(updateBundleResponse).toString(), "ok");
	}
	
	@RequestMapping(value="/importExcel", method = RequestMethod.POST)
    @ResponseBody
    public Map importEnterPriseQuota(@RequestParam("file") MultipartFile proFile, HttpServletRequest request) {
		XSSFWorkbook wb = null;
        XSSFSheet sheet = null;
        String fileDir = request.getSession().getServletContext().getRealPath("/tmp");
        File dir = new File(fileDir);
        File file = null;
        InputStream is = null;
        Map<String,String> resultMap = new HashMap<String, String>();
        try {
        	file = new File(fileDir, proFile.getOriginalFilename());
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            proFile.transferTo(file);
        	is = new FileInputStream(file);
            wb = new XSSFWorkbook(is);
            sheet = wb.getSheetAt(0);
            
            int deviceCount = sheet.getLastRowNum();
            int successNum = 0;
            boolean emptyOrValid = false;
            for (int i = 1; i <= deviceCount; i++) {
            	try {
            		XSSFRow row = sheet.getRow(i);
            		emptyOrValid = false;
            		ResourceVO resourceVO = new ResourceVO();
            		for(int j=0;j<row.getLastCellNum();j++){
            			XSSFCell cell = row.getCell(j);     
            			String cellContent = null;
            			if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            				long longvalue = Math.round(cell.getNumericCellValue());
            				if(Double.parseDouble(longvalue+".0") == cell.getNumericCellValue()){
            					cellContent = String.valueOf(longvalue);
            				}else{
            					cellContent = String.valueOf(cell.getNumericCellValue());
            				}
            			}else{
            				cellContent = cell.getStringCellValue().trim();
            			}
            			if(j > rowElementsNum){
            				break;
            			}
            			/*if(StringUtils.isEmpty(cellContent)){
            				emptyOrValid = true;
            				break;
            			}*/
            			if(j==0){
            				if(StringUtils.isEmpty(cellContent)){
            					emptyOrValid = true;
            					break;
            				}
                			resourceVO.setBundle_name(cellContent);
                		}else if(j==1){                			
                			resourceVO.setDevice_type(cellContent);
                		}else if(j==2){
                			resourceVO.setUsername(cellContent);
                		}else if(j==3){
                			resourceVO.setPassword(cellContent);
                		}else if(j==4){
                			resourceVO.setCall_number(cellContent);
                		}else if(j==5){
                			resourceVO.setDevice_longitude(cellContent);
                		}else if(j==6){
                			resourceVO.setDevice_latitude(cellContent);
                		}else if(j==7){                			
                			resourceVO.setProtocol(cellContent);
                		}
            		}
            		if(emptyOrValid){
            			continue;
            		}
                    CreateBundleRequest createBundleRequest = resourceVO.ConvertToCreateBundleRequest(resourceVO);
            		CreateBundleResponse createBundleResponse = resourceRemoteService.createBundle(createBundleRequest);
            		if(createBundleResponse != null && createBundleResponse.getCreate_bundle_response() != null && 
            				"success".equals(createBundleResponse.getCreate_bundle_response().getResult())){
            			successNum++;
            		}
				} catch (Exception e) {
					log.error("analyse excel row " + i + " failed", e);
				}            	
                
            }
            resultMap.put("resultCode", "-1");
            if(successNum > 0){            	
            	resultMap.put("resultCode", "0");
            	resultMap.put("successNum", String.valueOf(successNum));          
            	resultMap.put("totalNum", String.valueOf(deviceCount));          
            }
        } catch (IOException e) {
            log.error("read excel failed", e);
            resultMap.put("resultCode", "-1");          
        }finally {
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			
			if(file != null){
				file.delete();
			}
		}
        return resultMap;
        
    }
	
	@RequestMapping(value="/exportExcel")
    @ResponseBody
    public String exportEnterPriseQuota(HttpServletResponse response){
		InputStream in = null;
		try{
            String name = new String("信源导出信息表"+".xlsx");
            String fileName = new String(name.getBytes("gb2312"), "iso8859-1");
            XSSFWorkbook  wb = new XSSFWorkbook();
            XSSFSheet workSheet = wb.createSheet();
            
            XSSFRow row_1 = workSheet.createRow(0);
            setExcelHeader(row_1);
            
            for (int i = 0; i <= rowElementsNum; i++) {
                workSheet.setColumnWidth(i, 20*256);
            }
            
            List<BundleBody> bundles = resourceService.querybundleInfos("ipc");    		
    		List<ResourceVO> resourceVOs= (bundles == null) ? null :bundles.stream()
    															    .map(a-> ResourceVO.parseResourVOfromBundleBody(a))
    															    .collect(toList());
    		int rowNum = 1;
            if(null != resourceVOs && !resourceVOs.isEmpty()){
                for (ResourceVO resourceVO : resourceVOs) {
                    XSSFRow row = workSheet.createRow(rowNum);
                    XSSFCell row_cell00 =  row.createCell(0);
                    row_cell00.setCellValue(resourceVO.getBundle_name());
                    XSSFCell row_cell01 =  row.createCell(1);            
                    row_cell01.setCellValue(resourceVO.getDevice_type());
                    XSSFCell row_cell02 =  row.createCell(2);            
                    row_cell02.setCellValue(resourceVO.getUsername());
                    XSSFCell row_cell03 =  row.createCell(3);            
                    row_cell03.setCellValue(resourceVO.getPassword());
                    XSSFCell row_cell04 =  row.createCell(4);            
                    row_cell04.setCellValue(resourceVO.getCall_number());
                    XSSFCell row_cell05 =  row.createCell(5);            
                    row_cell05.setCellValue(resourceVO.getDevice_longitude());
                    XSSFCell row_cell06 =  row.createCell(6);            
                    row_cell06.setCellValue(resourceVO.getDevice_latitude());
                    XSSFCell row_cell07 =  row.createCell(7);            
                    row_cell07.setCellValue(resourceVO.getProtocol());
                    rowNum++;
                }
            }            
            File efile;
            String os = System.getProperties().getProperty("os.name").toLowerCase();
            if (os.contains("win")){
            	efile = new File("ipcExecel.csv");
            }else{
            	efile = new File("/temp/ipcExecel.csv");
            }
            OutputStream out = new FileOutputStream(efile);
            wb.write(out);//写入File
            out.close();
            response.setContentType("text/html;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			in = new FileInputStream(efile);
			OutputStream excelOut = response.getOutputStream();
			byte[] b = new byte[500];
			int length = -1;
			while((length =in.read(b)) != -1){
				excelOut.write(b, 0, length);
			}
			excelOut.flush();
        }catch(Exception e){
            log.error("exportIPC fail:",e);
            return "导出信息失败";
        }finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
        return "success";
	}
	
	/**
     * 创建IPC导出的表格头信息。
     * 
     */
    private void setExcelHeader(XSSFRow row){
    	
    	for(int i=0; i<excelHeader.length; i++){
            XSSFCell cell =  row.createCell(i);
            cell.setCellValue(excelHeader[i]);
    	}
    }
}
