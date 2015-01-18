package com.hhn.controll.sign;

import com.hhn.pojo.*;
import com.hhn.service.IExportSignService;
import com.hhn.util.Constants;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import util.BaseUserAction;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lenovo on 2014/12/2.
 */
@Controller
public class ExportSign extends BaseUserAction {
    public static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final static long MAX_SIZE = 10 * 1024 * 1024; //长传文件不能超过10M
    private final static String[] allowedContentTypes = new String[]{"application/x-excel","application/vnd.ms-excel","application/octet-stream"};//限制文件类型

    @Autowired
    private IExportSignService exportSignService;

    @RequestMapping("/exportSign.do")
    public ModelAndView exportSign(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("exportSign");
        List<AccountUser> userList = new ArrayList<AccountUser>();
        StringBuffer failParse = new StringBuffer(); //解析失败的行
        List<String> existList = new ArrayList<String>(); //存在相同的行
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultiValueMap<String, MultipartFile> multiValueMap = multipartRequest.getMultiFileMap();
            List<MultipartFile> files = multiValueMap.get("file");
            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    StringBuffer msg = new StringBuffer();
                    if (!validate(file, msg)) {
                        view.addObject("resultCode","N");
                        view.addObject("resultMsg", msg.toString());
                        return view;
                    }
                }
              //解析借款人资料
              MultipartFile fileFirst = files.get(0);
              parseExcelFirst(fileFirst, userList, failParse, existList);
              //解析借款人抵押资料
//            MultipartFile file1 = files.get(1);
//            parseExcel1(file1,paraMap);
//            //解析借款人项目资料
//            MultipartFile file2 = files.get(2);
//            parseExcel2(file2,paraMap);
//            //解析借款人银行资料
//            MultipartFile file3 = files.get(3);
//            parseExcel3(file3,paraMap);

             if(failParse.length()>0) {//如果存在解析错误，返回提示
                 view.addObject("failRow", failParse.substring(0, failParse.length() - 1));
                 return view;
             }
             if(existList.size()>0){
                 view.addObject("existRow1",existList);
                 return view;
             }
             //调用service接口写数据
             List<String> existRows = exportSignService.ExportSign(userList);
             if(existRows.size()>0){//如果存在重复数据，返回提示
                 view.addObject("existRow2", existRows);
             }
             view.addObject("resultCode","Y");
             view.addObject("resultMsg","Excel文件导入成功！");
            }
        }catch (Exception e){
            view.addObject("resultCode","N");
            view.addObject("resultMsg","Excel文件解析出错了！");
        }
        return view;
    }

    //解析借款人资料
    public void parseExcelFirst(MultipartFile file, List<AccountUser> userList,StringBuffer failParse, List<String> existList) throws Exception{
        Workbook book = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = book.getSheetAt(0);
        if(sheet!=null){
            int lastRowNum = sheet.getLastRowNum();
            Row row = null;
            for (int i = 2; i <= lastRowNum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    try {
                        //用户信息表
                        if ((getCellData(row, 0)==null || "".equals(getCellData(row,0))) &&
                                (getCellData(row, 8)==null || "".equals(getCellData(row, 8)))){
                            continue;
                        }
                        AccountUser user = new AccountUser();
                        user.setUser_type((byte) 0);
                        user.setUser_name(getCellData(row, 0));
                        user.setPassword("123456");
                        user.setReal_name(getCellData(row, 0));
                        String sex = getCellData(row, 1);
                        user.setId_mumber(getCellData(row, 8));
                        user.setCard_checked((byte) 1);
                        user.setMobile_phone(null);
                        user.setPhone_checked((byte) 0);
                        user.setEmail("");
                        user.setEmail_checked((byte) 0);
                        user.setSex("男".equals(sex.trim()) ? 'M' : 'F');
                        user.setBirthday(Constants.getBirthday(getCellData(row, 8)));
                        user.setEducation(Constants.getGradeCode(getCellData(row, 5)));
                        user.setMarriaged(Constants.getMarriedCode(getCellData(row, 3)));
                        user.setHome_address("");
                        user.setIndustry(getIndustryCode(getCellData(row, 15)));
                        user.setIncome(Constants.getIncomeCode(getCellData(row, 16)));
                        user.setPosition(Constants.getPositionCode(getCellData(row, 12)));
                        user.setUser_source((byte) 3);
                        user.setUser_status((byte) 1);
                        user.setCreate_time(new Date());
//                        if(userList.contains(user)){
//                            existList.add("第"+(i+1)+"行用户名:"+user.getUser_name()+",身份证号:"+user.getId_mumber()+"重复");
//                            continue;
//                        }
                        LoanDetail detail = new LoanDetail();
                        detail.setLoan_title(getCellData(row, 24));
                        detail.setLoan_amount(BigDecimal.valueOf(Double.valueOf(getCellData(row, 18))));
                        detail.setLoan_usage(getCellData(row, 24));
                        detail.setLoan_period(Float.valueOf(getCellData(row, 19)).shortValue());
                        detail.setAnnual_rate(BigDecimal.valueOf(Float.valueOf(getCellData(row, 22))));
                        detail.setLoan_rate(BigDecimal.valueOf(Float.valueOf(getCellData(row, 22))));
                        detail.setRepay_type(Constants.getRepayTypeCode(getCellData(row, 21)));
                        detail.setTender_day(Float.valueOf(getCellData(row, 23)).shortValue());
                        detail.setLoan_desc(getCellData(row, 25));
                        detail.setLoan_status((byte)2);
                        detail.setBorrower_type(Float.valueOf(getCellData(row, 27)).byteValue());
                        detail.setCreate_time(new Date());
                        detail.setUpdate_time(new Date());
                        user.setLoanDetail(detail);//借款信息

                        FundProduct product = new FundProduct();
                        product.setProduct_name(getCellData(row, 24));
                        product.setInvest_amount(BigDecimal.valueOf(Double.valueOf(getCellData(row, 18))));
                        product.setProduct_usage(getCellData(row, 24));
                        product.setLoan_period(Float.valueOf(getCellData(row, 19)).shortValue());
                        product.setAnnual_rate(BigDecimal.valueOf(Float.valueOf(getCellData(row, 22))));
                        product.setLoan_rate(BigDecimal.valueOf(Float.valueOf(getCellData(row, 22))));
                        product.setRepay_type(Constants.getRepayTypeCode(getCellData(row, 21)));
                        product.setTender_day(Float.valueOf(getCellData(row, 23)).shortValue());
                        product.setRemark(getCellData(row, 24));
                        product.setProduct_status((byte) 1);
                        product.setLoan_type(Float.valueOf(getCellData(row, 27)).byteValue());
                        product.setCreate_time(new Date());
                        product.setPublish_time(new Date());
                        user.setFundProduct(product);//投资产品信息

//                        if (detail.getBorrower_type() == 0) {//个人借款
//                            LoanPerson person = new LoanPerson();
//                            person.setEducation(Constants.getGradeCode(getCellData(row, 5)));
//                            person.setGraduate_school("");
//                            person.setGraduate_year(null);
//                            person.setResidence_type(Constants.getResidenceCode(getCellData(row, 4)));
//                            person.setMarriaged(Constants.getMarriedCode(getCellData(row, 3)));
//                            person.setCreate_time(new Date());
//                            user.setLoanPerson(person);//个人借款个人信息
//                            LoanPersonWork personWork = new LoanPersonWork();
//                            personWork.setCom_full_name(getCellData(row, 10));
//                            personWork.setPosition(Constants.getPositionCode(getCellData(row, 12)));
//                            personWork.setIncome(Constants.getIncomeCode(getCellData(row, 16)));
//                            personWork.setCom_type(Constants.getCompanyType(getCellData(row, 13)));
//                            personWork.setIndustry(getIndustryCode(getCellData(row, 15)));
//                            personWork.setWork_year(Float.valueOf(getCellData(row, 14)).shortValue());
//                            personWork.setCreate_time(new Date());
//                            user.setLoanPersonWork(personWork);//个人借款个人工作信息
//                        } else {//企业借款
//                            LoanCompany company = new LoanCompany();
//                            company.setLoan_company_id(1);//法人编号
//                            company.setCom_full_name(getCellData(row, 12));
//                            company.setCom_type(Constants.getCompanyType(getCellData(row, 13)));
//                            company.setIndustry(getIndustryCode(getCellData(row, 15)));
//                            company.setCreate_time(new Date());
//                            user.setLoanCompany(company);//企业借款法人信息
//
//                            LoanCorporation corporation = new LoanCorporation();
//                            corporation.setEducation(Constants.getGradeCode(getCellData(row, 5)));
//                            corporation.setResidence_type(Constants.getResidenceCode(getCellData(row, 4)));
//                            corporation.setMarriaged(Constants.getMarriedCode(getCellData(row, 3)));
//                            corporation.setCreate_time(new Date());
//                            user.setLoanCorporation(corporation);//企业借款企业信息
//                        }
                        userList.add(user);
                    }catch (Exception e){
                        failParse.append(String.valueOf(i + 1) + ",");
                        continue;
                    }
                }
            }
        }
    }
    //解析借款人抵押资料
    public void parseExcelSecond(MultipartFile file, Map<String,Object> map) throws Exception{

    }
    //解析借款人项目资料
    public void parseExcelThird(MultipartFile file, Map<String,Object> map) throws Exception{

    }
    //解析借款人银行资料
    public void parseExcelFouth(MultipartFile file, Map<String,Object> map) throws Exception{

    }

    //公司行业（0-计算机/互联网 /软件1-通信/电子2-金融/法律 /会计 /保险3-政府机关4-公共事业5-教育/培训6-媒体/广告
    // 7-零售/批发8->餐饮/酒店/旅馆9-交通运输10-房地产业11-能源业12-医疗/制药/卫生/保健13-建筑工程14-娱乐服务业
    // 15-体育/艺术16-农业17-公益组织18-其他）
    public Byte getIndustryCode(String industryStr){
        Iterator<Map.Entry<Byte, List<String>>> iterator = Constants.industryMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Byte, List<String>> ii = iterator.next();
            Byte key = ii.getKey();
            List<String> value = ii.getValue();
            if(value.contains(industryStr.trim())){
                return key;
            }
        }
        //默认其他
        return 18;
    }

    //上传文件验证
    public boolean validate(MultipartFile file,StringBuffer msg) {
        boolean flag = true;
        if (file.getSize() > MAX_SIZE){
            flag = false;
            msg.append("上传文件大小不能超过10M！");
        }
        if (!ArrayUtils.contains(allowedContentTypes, file.getContentType())){
            flag = false;
            msg.append("上传文件类型不对，请选择EXCEL文件！");
        }
        return flag;
    }
    /*
     *根据类型获取单元格数据
     * param row,num
     * return string
     */
    public String getCellData(Row row, int num){
        Cell cell = row.getCell(num);
        int type = cell.getCellType();
        String value = "";
        if (cell!=null){
        switch (type) {
            case 0:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    value = sFormat.format(date);
                } else {
                    double tempValue = cell.getNumericCellValue();
                    value = String.valueOf(tempValue);
                }
                break;
            case 1:
                value = cell.getStringCellValue();
                break;
            case 2:
                value = cell.getCellFormula();
                break;
            case 3:
                value = cell.getStringCellValue();
                break;
            case 4:
                boolean tempValue = cell.getBooleanCellValue();
                value = String.valueOf(tempValue);
                break;
            case 5:
                byte b = cell.getErrorCellValue();
                value = String.valueOf(b);
            default:
                break;
        }
        }
        return value;
    }

}