package com.example.myapplication.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.myapplication.dto.response.CostResponse;
import com.example.myapplication.dto.response.StatusAssigmentAndUserManageResponse;
import com.example.myapplication.dto.response.StatusAssigmentOfTeamManageResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUltil {
    public  static  void createExcelAndDownloaded(Context context, Map<Integer,List<CostResponse>> response, String filename){
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        CellStyle style = workbook.createCellStyle();
        CellStyle styleAssigment = workbook.createCellStyle();
        CellStyle stylePropertie=workbook.createCellStyle();
        CellStyle styleUsername =workbook.createCellStyle();
        CellStyle style1= workbook.createCellStyle();


        Font fontAssigment = workbook.createFont();
//
        styleAssigment.setFillForegroundColor(new XSSFColor(new byte[] { (byte) 255, (byte) 153, 0 }));
        styleAssigment.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleAssigment.setFont(fontAssigment);
        //căn giữa
        styleAssigment.setAlignment(HorizontalAlignment.CENTER);


        //  set border
        BorderStyle borderStyle = BorderStyle.THIN; // Có thể sử dụng THIN, MEDIUM, DASHED, vv

// Thiết lập viền cho tất cả các phần của ô
        styleAssigment.setBorderTop(borderStyle);
        styleAssigment.setBorderBottom(borderStyle);
        styleAssigment.setBorderLeft(borderStyle);
        styleAssigment.setBorderRight(borderStyle);


        style1.setBorderTop(borderStyle);
        style1.setBorderBottom(borderStyle);
        style1.setBorderLeft(borderStyle);
        style1.setBorderRight(borderStyle);

//        cho username
        Font fontUserName = workbook.createFont();
//
        styleUsername.setFillForegroundColor(new XSSFColor(new byte[] { (byte)  230, (byte) 184, (byte) 175}));
        styleUsername.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleUsername.setFont(fontUserName);
        //căn giữa
        styleUsername.setAlignment(HorizontalAlignment.CENTER);



// Thiết lập viền cho tất cả các phần của ô
        styleUsername.setBorderTop(borderStyle);
        styleUsername.setBorderBottom(borderStyle);
        styleUsername.setBorderLeft(borderStyle);
        styleUsername.setBorderRight(borderStyle);






        Font fontProperties = workbook.createFont();



        stylePropertie.setFillForegroundColor(new XSSFColor(new byte[] { (byte) 208, (byte) 224, (byte)227 }));
        stylePropertie.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylePropertie.setFont(fontProperties);
        //căn giữa
        stylePropertie.setAlignment(HorizontalAlignment.CENTER);


// Thiết lập viền cho tất cả các phần của ô
        stylePropertie.setBorderTop(borderStyle);
        stylePropertie.setBorderBottom(borderStyle);
        stylePropertie.setBorderLeft(borderStyle);
        stylePropertie.setBorderRight(borderStyle);



//        màu
        Font font = workbook.createFont();
//
        style.setFillForegroundColor(new XSSFColor(new byte[] { 0, (byte) 255, 0 }));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);
        //căn giữa
        style.setAlignment(HorizontalAlignment.CENTER);


        //  set border
        style.setBorderTop(borderStyle);
        style.setBorderBottom(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        Row title = sheet.createRow(0);
        for(int i =0;i<5;i++)
        {
            title.createCell(i);
        }
        title.getCell(0).setCellStyle(style);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(region);
        title.getCell(0).setCellValue("Bảng báo cáo chi phí ");
        sheet.createRow(1);


        Integer countRow=1;

        for (Map.Entry<Integer, List<CostResponse>> entry : response.entrySet()) {
            Integer key = entry.getKey();
            List<CostResponse> value = entry.getValue();
//            cách ra 2 dòng
            Row h1=sheet.createRow(countRow);
            countRow++;
            h1=sheet.createRow(countRow);
            countRow++;
            Row h= sheet.createRow(countRow);
            countRow++;
            Cell cell1=h.createCell(0);
            cell1.setCellStyle(styleAssigment);
            cell1.setCellValue(value.get(0).getNameAssigment());
            Integer countCell=1;

            cell1=h.createCell(countCell);
            cell1.setCellValue("price");
            cell1.setCellStyle(stylePropertie);
            countCell++;
            cell1=h.createCell(countCell);
            cell1.setCellStyle(stylePropertie);
            countCell++;
            cell1.setCellValue("refundDate");


            // Sử dụng foreach để duyệt qua từng phần tử trong danh sách value
            for (CostResponse item : value) {
                countCell=0;
                Row h3=sheet.createRow(countRow);
                countRow++;
                Cell celli = h3.createCell(countCell);
                celli.setCellStyle(styleUsername);
                celli.setCellValue(item.getCostName());
                countCell++;
                celli= h3.createCell(countCell);
                celli.setCellStyle(style1);
                celli.setCellValue(String.valueOf(item.getPrice()));
                countCell++;
                celli = h3.createCell(countCell);
                celli.setCellStyle(style1);
                celli.setCellValue(item.getRefundDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                countCell++;

            }

        }
        try {
            File directory = new File(context.getExternalFilesDir(null) + "/your_directory/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, filename);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            openFile(context,filename);
            Toast.makeText(context, "Excel file created and downloaded!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void createExcelAndDownload(Context context, StatusAssigmentOfTeamManageResponse response, String fileName) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet 1");
        CellStyle style = workbook.createCellStyle();
        CellStyle styleAssigment = workbook.createCellStyle();
        CellStyle stylePropertie=workbook.createCellStyle();
        CellStyle styleUsername =workbook.createCellStyle();
        CellStyle style1= workbook.createCellStyle();


        Font fontAssigment = workbook.createFont();
//
        styleAssigment.setFillForegroundColor(new XSSFColor(new byte[] { (byte) 255, (byte) 153, 0 }));
        styleAssigment.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleAssigment.setFont(fontAssigment);
        //căn giữa
        styleAssigment.setAlignment(HorizontalAlignment.CENTER);


        //  set border
        BorderStyle borderStyle = BorderStyle.THIN; // Có thể sử dụng THIN, MEDIUM, DASHED, vv

// Thiết lập viền cho tất cả các phần của ô
        styleAssigment.setBorderTop(borderStyle);
        styleAssigment.setBorderBottom(borderStyle);
        styleAssigment.setBorderLeft(borderStyle);
        styleAssigment.setBorderRight(borderStyle);


        style1.setBorderTop(borderStyle);
        style1.setBorderBottom(borderStyle);
        style1.setBorderLeft(borderStyle);
        style1.setBorderRight(borderStyle);

//        cho username
        Font fontUserName = workbook.createFont();
//
        styleUsername.setFillForegroundColor(new XSSFColor(new byte[] { (byte)  230, (byte) 184, (byte) 175}));
        styleUsername.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleUsername.setFont(fontUserName);
        //căn giữa
        styleUsername.setAlignment(HorizontalAlignment.CENTER);



// Thiết lập viền cho tất cả các phần của ô
        styleUsername.setBorderTop(borderStyle);
        styleUsername.setBorderBottom(borderStyle);
        styleUsername.setBorderLeft(borderStyle);
        styleUsername.setBorderRight(borderStyle);






        Font fontProperties = workbook.createFont();



        stylePropertie.setFillForegroundColor(new XSSFColor(new byte[] { (byte) 208, (byte) 224, (byte)227 }));
        stylePropertie.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        stylePropertie.setFont(fontProperties);
        //căn giữa
        stylePropertie.setAlignment(HorizontalAlignment.CENTER);


// Thiết lập viền cho tất cả các phần của ô
        stylePropertie.setBorderTop(borderStyle);
        stylePropertie.setBorderBottom(borderStyle);
        stylePropertie.setBorderLeft(borderStyle);
        stylePropertie.setBorderRight(borderStyle);



//        màu
        Font font = workbook.createFont();
//
        style.setFillForegroundColor(new XSSFColor(new byte[] { 0, (byte) 255, 0 }));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);
        //căn giữa
        style.setAlignment(HorizontalAlignment.CENTER);


        //  set border
        style.setBorderTop(borderStyle);
        style.setBorderBottom(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        Row title = sheet.createRow(0);
        for(int i =0;i<5;i++)
        {
           title.createCell(i);
        }
        title.getCell(0).setCellStyle(style);
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(region);
        title.getCell(0).setCellValue("Bảng báo cáo tiến trình của nhóm "+response.getTeamName());
        Map<Integer,List<StatusAssigmentAndUserManageResponse>> listMap= new HashMap<>();
        List<StatusAssigmentAndUserManageResponse> responseSAAUMResponse = response.getStatusAssigmentAndUser();
        for(StatusAssigmentAndUserManageResponse response1:responseSAAUMResponse)
        {
            if (listMap.containsKey(response1.getIdAssigment())) {
                // Nếu key đã tồn tại, lấy danh sách hiện có và thêm vào
                List<StatusAssigmentAndUserManageResponse> existingList = listMap.get(response1.getIdAssigment());
                existingList.add(response1);
            } else {
                // Nếu key chưa tồn tại, tạo một danh sách mới và thêm vào map
                List<StatusAssigmentAndUserManageResponse> newList = new ArrayList<>();
                newList.add(response1);
                listMap.put(response1.getIdAssigment(), newList);
            }
        }
        sheet.createRow(1);


        Integer countRow=1;

        for (Map.Entry<Integer, List<StatusAssigmentAndUserManageResponse>> entry : listMap.entrySet()) {
            Integer key = entry.getKey();
            List<StatusAssigmentAndUserManageResponse> value = entry.getValue();
//            cách ra 2 dòng
            Row h1=sheet.createRow(countRow);
            countRow++;
            h1=sheet.createRow(countRow);
            countRow++;
            Row h= sheet.createRow(countRow);
            countRow++;
            Cell cell1=h.createCell(0);
            cell1.setCellStyle(styleAssigment);
            cell1.setCellValue(value.get(0).getNameAssignment());
            Integer countCell=1;

            cell1=h.createCell(countCell);
            cell1.setCellValue("process");
            cell1.setCellStyle(stylePropertie);
            countCell++;
            cell1=h.createCell(countCell);
            cell1.setCellStyle(stylePropertie);
            countCell++;
            cell1.setCellValue("status");

            // Sử dụng foreach để duyệt qua từng phần tử trong danh sách value
            for (StatusAssigmentAndUserManageResponse item : value) {
                countCell=0;

                Row h3=sheet.createRow(countRow);
                countRow++;
                Cell celli = h3.createCell(countCell);
                celli.setCellStyle(styleUsername);
                celli.setCellValue(item.getUsername());
                countCell++;
                celli= h3.createCell(countCell);
                celli.setCellStyle(style1);
                celli.setCellValue(item.getProcess().toString());
                countCell++;
                celli = h3.createCell(countCell);
                celli.setCellStyle(style1);
                celli.setCellValue(item.getStatus().toString());
                countCell++;

            }

        }
        try {
            File directory = new File(context.getExternalFilesDir(null) + "/your_directory/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            openFile(context,fileName);
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
