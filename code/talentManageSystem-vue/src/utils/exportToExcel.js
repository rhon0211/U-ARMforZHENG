// src/utils/exportToExcel.js
import * as XLSX from 'xlsx';

function export_json_to_excel({ header, data, merges, filename }) {
  const worksheet = XLSX.utils.aoa_to_sheet([header, ...data]);

  // 设置合并单元格
  if (merges && merges.length > 0) {
    if (!worksheet['!merges']) worksheet['!merges'] = [];
    merges.forEach(merge => worksheet['!merges'].push(XLSX.utils.decode_range(merge)));
  }

  // 设置样式
  const cellStyles = {
    font: { name: "Arial", sz: 14, bold: true, color: { rgb: "FFFFFF" } },
    fill: { fgColor: { rgb: "4F81BD" } },
    alignment: { vertical: "center", horizontal: "center" }
  };

  // 应用样式到表头
  header.forEach((row, rowIndex) => {
    row.forEach((cell, colIndex) => {
      const cellAddress = XLSX.utils.encode_cell({ c: colIndex, r: rowIndex });
      if (!worksheet[cellAddress].s) {
        worksheet[cellAddress].s = cellStyles;
      }
    });
  });

  const workbook = XLSX.utils.book_new();
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');
  XLSX.writeFile(workbook, filename);
}

export { export_json_to_excel };
