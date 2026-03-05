package ucl.group.excelSystem.api;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import ucl.group.excelSystem.api.common.utils.TableUtils;
import ucl.group.talentManageSystem.api.Application;


@SpringBootTest(classes = Application.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TableUtilsTest {

	@SuppressWarnings("deprecation")
	@Test
	public void testGetNextNextYearFebruary() {
		// 测试用例1：输入为2024-02-01
		LocalDate date1 = LocalDate.of(2024, 2, 1);
		LocalDate result1 = TableUtils.getNextNextYearFebruary(date1);
		System.out.println(result1);
		assertEquals(LocalDate.of(2026, 2, 1), result1);

		// 测试用例2：输入为2024-01-01
		LocalDate date2 = LocalDate.of(2024, 1, 1);
		LocalDate result2 = TableUtils.getNextNextYearFebruary(date2);
		System.out.println(result2);
		assertEquals(LocalDate.of(2025, 2, 1), result2);

		// 测试用例3：输入为2023-12-31
		LocalDate date3 = LocalDate.of(2023, 12, 31);
		LocalDate result3 = TableUtils.getNextNextYearFebruary(date3);
		System.out.println(result3);
		assertEquals(LocalDate.of(2025, 2, 1), result3);
	}

	@Test
	public void testGetYearMonthsBetween() {

		// 测试用例1：开始日期和结束日期为同一个月
		LocalDate dateStart1 = LocalDate.of(2024, 3, 1);
		LocalDate dateEnd1 = LocalDate.of(2024, 3, 1);
		List<String> expected1 = new ArrayList<>();
		expected1.add("2024-03");
		List<String> result1 = TableUtils.getYearMonthsBetween(dateStart1, dateEnd1);
		System.out.println(result1);
		assertEquals(expected1, result1);

		// 测试用例2：开始日期和结束日期为不同的月份
		LocalDate dateStart2 = LocalDate.of(2024, 1, 1);
		LocalDate dateEnd2 = LocalDate.of(2024, 3, 1);
		List<String> expected2 = new ArrayList<>();
		expected2.add("2024-01");
		expected2.add("2024-02");
		expected2.add("2024-03");
		List<String> result2 = TableUtils.getYearMonthsBetween(dateStart2, dateEnd2);
		System.out.println(result2);
		assertEquals(expected2, result2);

		// 测试用例3：跨年份的月份
		LocalDate dateStart3 = LocalDate.of(2023, 11, 1);
		LocalDate dateEnd3 = LocalDate.of(2024, 2, 1);
		List<String> expected3 = new ArrayList<>();
		expected3.add("2023-11");
		expected3.add("2023-12");
		expected3.add("2024-01");
		expected3.add("2024-02");
		List<String> result3 = TableUtils.getYearMonthsBetween(dateStart3, dateEnd3);
		System.out.println(result3);
		assertEquals(expected3, result3);

		// 测试用例4：仅有一个月份的日期
		LocalDate dateStart4 = LocalDate.of(2024, 5, 1);
		LocalDate dateEnd4 = LocalDate.of(2024, 5, 1);
		List<String> expected4 = new ArrayList<>();
		expected4.add("2024-05");
		List<String> result4 = TableUtils.getYearMonthsBetween(dateStart4, dateEnd4);
		System.out.println(result4);
		assertEquals(expected4, result4);

		// 测试用例5：倒序日期
		LocalDate dateStart5 = LocalDate.of(2024, 3, 1);
		LocalDate dateEnd5 = LocalDate.of(2023, 3, 1);
		List<String> expected5 = new ArrayList<>();
		List<String> result5 = TableUtils.getYearMonthsBetween(dateStart5, dateEnd5);
		System.out.println(result5);
		assertEquals(expected5, result5);

		// 测试用例6：间隔两年日期
		LocalDate dateStart6 = LocalDate.of(2024, 2, 1);
		LocalDate dateEnd6 = LocalDate.of(2026, 2, 1);
		List<String> expected6 = new ArrayList<>();
		expected6.add("2024-02");
		expected6.add("2024-03");
		expected6.add("2024-04");
		expected6.add("2024-05");
		expected6.add("2024-06");
		expected6.add("2024-07");
		expected6.add("2024-08");
		expected6.add("2024-09");
		expected6.add("2024-10");
		expected6.add("2024-11");
		expected6.add("2024-12");
		expected6.add("2025-01");
		expected6.add("2025-02");
		expected6.add("2025-03");
		expected6.add("2025-04");
		expected6.add("2025-05");
		expected6.add("2025-06");
		expected6.add("2025-07");
		expected6.add("2025-08");
		expected6.add("2025-09");
		expected6.add("2025-10");
		expected6.add("2025-11");
		expected6.add("2025-12");
		expected6.add("2026-01");
		expected6.add("2026-02");
		List<String> result6 = TableUtils.getYearMonthsBetween(dateStart6, dateEnd6);
		System.out.println(result6);
		assertEquals(expected6, result6);
	}

}
