package ucl.group.excelSystem.api.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucl.group.excelSystem.api.db.pojo.vo.SalesVO;
import ucl.group.excelSystem.api.service.SalesService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Setter
@RestController
@RequestMapping("/api/v2/sales")
public class SalesController {

    @Resource
    private SalesService salesService;

    @GetMapping("/getExcel/{selectedYear}")
    public void getExcel(@PathVariable String selectedYear, HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            //response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            String fileName = "csv" + selectedYear;
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");

            List<SalesVO> data = salesService.getData(selectedYear);

            EasyExcel.write(response.getOutputStream(), SalesVO.class).excelType(ExcelTypeEnum.XLSX).sheet("csv").doWrite(data);
        } catch (Exception e) {
            throw new RuntimeException("导出失败", e);
        }
    }

}
