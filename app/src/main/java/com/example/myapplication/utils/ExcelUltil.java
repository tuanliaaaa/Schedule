package com.example.myapplication.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUltil {

    public static void createExcelAndDownload(Context context, String fileName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Column 1");
        headerRow.createCell(1).setCellValue("Column 2");

        // Tạo một CellStyle với màu nền #ccc
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // Áp dụng CellStyle cho các cell trong hàng đầu tiên
        for (Cell cell : headerRow) {
            cell.setCellStyle(headerStyle);
        }

        try {
            File directory = new File(context.getExternalFilesDir(null) + "/your_directory/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            Toast.makeText(context, "Excel file created and downloaded!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public static void openFile(Context context, String filePath) {
        File file = new File(context.getExternalFilesDir(null) + "/your_directory/"+filePath);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(context, "No application available to open Excel file", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static String getMimeType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        String mimeType;
        switch (extension) {
            case "pdf":
                mimeType = "application/pdf";
                break;
            case "doc":
                mimeType = "application/msword";
                break;
            case "docx":
                mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                break;
            // Add more mime types for other file extensions if needed
            default:
                mimeType = "*/*";
                break;
        }
        return mimeType;
    }
}
