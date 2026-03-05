package ucl.group.excelSystem.api.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateProTechForm {

	@NotNull(message = "projectTechnicianIdは空欄にできません")
	private Long projectTechnicianId;
	private Long parentId;
	@NotNull(message = "technicianIdは空欄にできません")
	private Long technicianId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("cBeginMonth")
	private LocalDate cBeginMonth;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("cEndMonth")
	private LocalDate cEndMonth;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("realStopMonth")
	private LocalDate realStopMonth;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("cRealBeginMonth")
	private LocalDate cRealBeginMonth;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("cRealEndMonth")
	private LocalDate cRealEndMonth;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("stopMonth")
	private LocalDate stopMonth;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("hBeginMonth")
	private LocalDate hBeginMonth;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("hEndMonth")
	private LocalDate hEndMonth;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty("priceMonth")
	private LocalDate priceMonth;

	@JsonProperty("cHigherHours")
	private BigDecimal cHigherHours;

	@JsonProperty("cIncreasePrice")
	private BigDecimal cIncreasePrice;

	@JsonProperty("cLowerHours")
	private BigDecimal cLowerHours;

	@JsonProperty("cPrice")
	private BigDecimal cPrice;

	@JsonProperty("cReductPrice")
	private BigDecimal cReductPrice;

	@JsonProperty("hHigherHours")
	private BigDecimal hHigherHours;

	@JsonProperty("hIncreasePrice")
	private BigDecimal hIncreasePrice;

	@JsonProperty("hLowerHours")
	private BigDecimal hLowerHours;

	@JsonProperty("hPrice")
	private BigDecimal hPrice;

	@JsonProperty("hReductPrice")
	private BigDecimal hReductPrice;

	@JsonProperty("standardHours")
	private BigDecimal standardHours;

	@JsonProperty("status")
	private String status;

	@JsonProperty("remark")
	private String remark;

	@JsonProperty("selected")
	private String selected;

	@JsonProperty("contract")
	private String contract;

	@JsonProperty("contractNum")
	private String contractNum;

}
