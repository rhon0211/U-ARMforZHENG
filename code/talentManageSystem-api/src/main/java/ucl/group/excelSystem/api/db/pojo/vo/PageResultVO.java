package ucl.group.excelSystem.api.db.pojo.vo;


import lombok.Data;

import java.util.List;

@Data
public class PageResultVO<T> {
    private Long total;
    private Integer page;
    private Integer pageSize;
    private List<T> data;

    public PageResultVO(Long total, Integer page, Integer pageSize, List<T> data) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.data = data;
    }
}
