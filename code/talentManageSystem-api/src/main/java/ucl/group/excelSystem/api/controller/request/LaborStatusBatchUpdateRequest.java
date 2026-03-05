package ucl.group.excelSystem.api.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class LaborStatusBatchUpdateRequest {
    private List<Integer> ids;
    private Integer laborStatus;

}
