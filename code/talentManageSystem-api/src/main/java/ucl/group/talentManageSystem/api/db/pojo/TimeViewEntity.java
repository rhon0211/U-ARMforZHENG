package ucl.group.talentManageSystem.api.db.pojo;

import java.util.List;

/**
 * 这里使用@Data注解会报错，返回给前端的不是xAxis而是xaxis
 */
public class TimeViewEntity {
    private List<String> xAxis;
    private List<Series> series;



    public static class Series {
        private String type;
        private List<Integer> data;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis) {
        this.xAxis = xAxis;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }
}
