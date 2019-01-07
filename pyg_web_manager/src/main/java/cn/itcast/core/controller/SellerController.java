package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.service.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.http.HttpResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 商家审核管理
 */
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Reference
    private SellerService sellerService;

    /**
     * 分页高级查询
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody Seller seller, Integer page, Integer rows){
        PageResult pageResult = sellerService.findPage(seller, page, rows);
        return pageResult;
    }

    @RequestMapping("/findOne")
    public Seller findOne(String id){
        Seller one = sellerService.findOne(id);
        return one;
    }

    /**
     * 商家审核
     * @param sellerId   需要审核的商家id
     * @param status     审核状态
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(String sellerId, String status) {
        try {
            sellerService.updateStatus(sellerId, status);
            return new Result(true, "审核成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "审核失败!");
        }
    }
    /**
     * 审核数据导入
     *
     * @return
     */
    @RequestMapping("/importXls")
    public Result importXls(){
        try {

            return new Result(true, "导入成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "导入失败!");
        }
    }
    /**
     * 审核数据导出
     *
     * @return
     */
    @RequestMapping("/exportXls")
    public void exportXls(HttpServletResponse response){
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
            try{
            //将导入的数据查询出来
            List<Seller> sellerList = sellerService.findAll();
            //创建poi导出数据相关内容
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            //建立新的sheet对象（excel的表单）
            HSSFSheet sheet=wb.createSheet("审核表");
            //创建标题行
            HSSFRow row = sheet.createRow(0);
            //获取行中的cell
            row.createCell(0).setCellValue("商家ID");
            row.createCell(1).setCellValue("公司名称");
            row.createCell(2).setCellValue("店铺名称");
            row.createCell(3).setCellValue("联系人姓名");
            row.createCell(4).setCellValue("公司电话");
            //循环创建行根据读取的数据
            for (int i=0;i<sellerList.size();i++) {
                row = sheet.createRow(i+1);
                Seller seller = sellerList.get(i);
                row.createCell(i).setCellValue(seller.getSellerId());
                row.createCell(i+1).setCellValue(seller.getName());
                row.createCell(i+2).setCellValue(seller.getNickName());
                row.createCell(i+3).setCellValue(seller.getLinkmanName());
                row.createCell(i+4).setCellValue(seller.getMobile());
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            wb.write(os);
            byte[] content = os.toByteArray();
            String fileName= "审核表.xls";
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面

            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes(), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            os.close();
            is.close();
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if(bis!=null) {
                        bis.close();
                    }
                    if(bos!=null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }
}
