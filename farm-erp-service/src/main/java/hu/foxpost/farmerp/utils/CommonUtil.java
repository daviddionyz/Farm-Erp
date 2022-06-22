package hu.foxpost.farmerp.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Component
@Slf4j
@Data
public class CommonUtil {

//    public static String generate32LengthString(){
//        return RandomStringUtils.random(32,true,true);
//    }

    public final static String CURRENCY_HUF = "HUF";

    public static String decodeBase64(String base64Encoded){
        return new String(Base64.getDecoder().decode(base64Encoded));
    }

    public static DateTimeFormatter getDateFormatWithTime(){
        return DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
    }

    public static DateTimeFormatter getTOFDateFormatter(){
        return DateTimeFormatter.ofPattern("yyyy.MM.dd.");
    }

    public static DateTimeFormatter getOTPDateYear(){
        return DateTimeFormatter.ofPattern("yyyyMMdd");
    }

    public static DateTimeFormatter getOTPDateHour(){
        return DateTimeFormatter.ofPattern("HHmmss");
    }

    public static DateTimeFormatter getDefaultDateFormatter(){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    public static SimpleDateFormat getSimpleDateFormat(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public static DateTimeFormatter getExportDateFormatter(){
        return DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
    }
    public static LocalDateTime generateCurrentDate(){

        return LocalDateTime.parse(LocalDateTime.now().format(getDefaultDateFormatter()), getDefaultDateFormatter());
    }

}