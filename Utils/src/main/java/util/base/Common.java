package util.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.io.IOUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Common {
	
	private final static String STRING_EMPTY = "";
	
	//static Logger logger = Logger.getLogger(Utils.class);
	
    public static boolean FileExist(String path){
        try {
            File tempFile = new File(path);
            return tempFile.exists() && tempFile.length() > 0;
        } catch (Exception e) {}
        return false;
    }
    
    public static boolean FileDelete(String path){
        try {
            File tempFile = new File(path);
            if (!tempFile.exists()) return true;
            tempFile.delete();
            return !tempFile.exists();
        } catch (Exception e) {}
        return false;
    }
    
    public static boolean FolderExist(String path){
        try {
        	try {
        		File tempFile = new File(path);
                return tempFile.exists() && tempFile.isDirectory();
            } catch (Exception e) {}
            return false;
        } catch (Exception e) {}
        return false;
    }
    
    public static boolean FolderCreate(String path){
    	try {
            if (FolderExist(path)) return true;
            File tempFile = new File(path);
            tempFile.mkdirs();
            return !tempFile.exists();
        } catch (Exception e) {}
        return false;
    }
    
    public static boolean FolderDelete(String path){
    	try {
            if (!FolderExist(path)) return true;
            File tempFile = new File(path);
            tempFile.delete();
            return !tempFile.exists();
        } catch (Exception e) {}
        return false;
    }
    
    public static String getArgsStringValue(String[] args, String key){
        String result = STRING_EMPTY;
        if (args == null) return result;
        if (args.length < 1) return result;
        key = key + STRING_EMPTY;
        for (int i = 0; i < args.length; i++)
        {
            if (key.equals(args[i]))
            {
                i++;
                if (i >= args.length)
                {
                    System.exit(1);
                }
                result = args[i];
                break;
            }
        }
        return result;
    }
    
	public static long getLongTimeStamp() {
        //TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        final Instant instant = Instant.now();
        final long timeStampMillis = instant.toEpochMilli();
        return timeStampMillis / 1000;
    }
	
	public static long getLongUtcTimeStamp() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		long time = cal.getTimeInMillis();
        return time / 1000;
    }
	
	public static String getDatetimeString() {
		String result = "";
		
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = new Date(System.currentTimeMillis());
		result = formatter.format(date);
		
		return result;
	}
	
    public static String getArgsStringValue(String[] args, int index){
        String result = STRING_EMPTY;
        if (args == null) return result;
        if (args.length < 1) return result;
        if (args.length <= index) return result;
        
        result = args[index];
        if ((result + STRING_EMPTY).startsWith("-")) return STRING_EMPTY;
        
        return result;
    }
    
    public static boolean getArgsBoolValue(String[] args, String key){
        boolean result = false;
        if (args == null) return result;
        if (args.length < 1) return result;
        key = key + STRING_EMPTY;
        for (int i = 0; i < args.length; i++)
        {
            if (key.equals(args[i]))
            {
                result = true;
                break;
            }
        }
        return result;
    }
    
    public static int getArgsIntegerValue(String[] args, String key){
        int result = 0;
        String strResult = getArgsStringValue(args, key);
        try {
            result = Integer.parseInt(strResult);
        } finally{
            
        }
        return result;
    }
    
    public static boolean isStringEmpty(String strValue){
        if (strValue == null) return true;
        if (STRING_EMPTY.equals(strValue)) return true;
        if (STRING_EMPTY.equals(strValue.trim())) return true;
        return false;
    }
    

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    public static String getHexString(byte[] data) {
    	StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    public String getHexString(String data) {
    	StringBuilder sb = new StringBuilder();
        for (byte b : data.getBytes()) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
     
    public static String getHexString(InputStream data) {
    	StringBuilder sb = new StringBuilder();
    	byte[] dataArr = null;
		try {
			dataArr = IOUtils.toByteArray(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
        for (byte b : dataArr) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    public static byte[] getFromBase64(String data) {
    	return Base64.getDecoder().decode(data);
    }
    
    public static String getBase64String(byte[] data) {
    	return new String(Base64.getEncoder().encode(data));
    }
    
    public static String getBase64String(String data) {
    	return new String(Base64.getEncoder().encode(data.getBytes()));
    }
    
    public static String getBase64String(InputStream data) {
    	byte[] dataArr = null;
		try {
			dataArr = IOUtils.toByteArray(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return new String(Base64.getEncoder().encode(dataArr));
    }
    
    public static byte[] getHashSHA256(InputStream data) {
		try {
			byte[] dataArr = IOUtils.toByteArray(data);
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return digest.digest(dataArr);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static byte[] getHashSHA1(InputStream data) {
		try {
			byte[] dataArr = IOUtils.toByteArray(data);
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			return digest.digest(dataArr);
		} catch (IOException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static String toJsonString(Object data) {
    	try {
			if (data == null) {
				return STRING_EMPTY;
			}
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	        return gson.toJson(data);
		} catch (Exception e) {
			return STRING_EMPTY;
		}
    } 
    
    public static String toJsonPrettyString(Object data) {
    	try {
			if (data == null) {
				return STRING_EMPTY;
			}
			Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	        return gson.toJson(data);
		} catch (Exception e) {
			return STRING_EMPTY;
		}
    } 

    public static <T> T fromJson(Class<T> xclass, String data) {
    	try {
	    	if (isStringEmpty(data)) {
				return null;
			}
			
	    	Gson converter = new Gson();
	    	T result = converter.fromJson(data, xclass);
	        return result;
		} catch (Exception e) {
			return null;
		}
    } 
    
    public static void logException(String message, Exception ex){
        //logger.error(message);
        //logger.error(ex);
        //logger.debug(message);
        //logger.debug(ex.getMessage());
        
        String fullClassName = STRING_EMPTY;
        String methodName = STRING_EMPTY;
        int lineNumber = 0;

        try {
            int dept = 2;
            fullClassName = Thread.currentThread().getStackTrace()[dept].getClassName();
            //className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            methodName = Thread.currentThread().getStackTrace()[dept].getMethodName();
            lineNumber = Thread.currentThread().getStackTrace()[dept].getLineNumber();
        } finally {

        }
        String metadata = "- ["+fullClassName+"].["+methodName+"].[Line "+lineNumber+"] - ";
        String stackTrace = Arrays.toString(ex.getStackTrace());
        System.out.println(metadata + message + "\nError: " +  ex.getMessage() + "\n Stack trace: " + stackTrace);
    }
    
    public static void logException(Exception ex){
        //logger.error(message);
        //logger.error(ex);
        //logger.debug(message);
        //logger.debug(ex.getMessage());
        
        String fullClassName = STRING_EMPTY;
        String methodName = STRING_EMPTY;
        int lineNumber = 0;

        try {
            int dept = 2;
            fullClassName = Thread.currentThread().getStackTrace()[dept].getClassName();
            //className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            methodName = Thread.currentThread().getStackTrace()[dept].getMethodName();
            lineNumber = Thread.currentThread().getStackTrace()[dept].getLineNumber();
        } finally {

        }
        String metadata = "- ["+fullClassName+"].["+methodName+"].[Line "+lineNumber+"] - ";
        String stackTrace = Arrays.toString(ex.getStackTrace());
        System.out.println(metadata + "\nError: " +  ex.getMessage() + "\n Stack trace: " + stackTrace);
    }
}
