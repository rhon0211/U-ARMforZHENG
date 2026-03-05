package ucl.group.excelSystem.api.controller.request;


import lombok.Data;

import java.util.List;

@Data
public class TradeRequest {

    private List<Integer> projectIds;

    private String month;
}
