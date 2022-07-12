package com.server.customer.api.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class DefaultZeroPayService implements ZeroPayService {

    @Override
    public JSONObject preparePayment() throws Exception {
        String _MID_ = "ZP2206000318";
        String _STORE_KEY_ = "46647133516238446b7236444a707366";
        String _AUTH_KEY_ = "QSd4f0mmCLrV0H5PM8xcA.786RkWOUkIBihiodu1YN.6MEZbvo3Yy";
        String payr_ci = "";
        String payr_clph_no = "";

        JSONObject json = new JSONObject();

        json.put("mid" , _MID_);
        json.put("mode" , "development");
        json.put("merchantOrderID", "20200723_order_id1234");
        json.put("merchantUserKey", "test_mall_userkey");
        json.put("productName" , "[오더프레쉬] 못난이(흠과) 사과 5kg (과수크기 랜덤발송) 포함 총2건");
        json.put("quantity" , 3);
        json.put("totalAmount" , 1500);
        json.put("taxFreeAmount" , 0);
        json.put("vatAmount" , 137);
        json.put("approvalURL" , "http://test.co.kr/succ_url");
        json.put("cancelURL" , "http://test.co.kr/cancel_url");
        json.put("failURL" , "http://test.co.kr/fail_url");
        json.put("apiCallYn" , "N");
        json.put("payrCi" , payr_ci);
        json.put("clphNo" , payr_clph_no);
        json.put("zip_no" , "");

        JSONArray productItems = new JSONArray();

        JSONObject productItem = new JSONObject();

        productItem.put("seq", 1);
        productItem.put("name", "[오더프레쉬] 못난이(흠과) 사과 5kg (과수크기 랜덤발송)");
        productItem.put("category", "F");
        productItem.put("count", 2);
        productItem.put("amount", 1000);
        productItem.put("biz_no", "123456790");

        productItems.add(productItem);

        productItem = new JSONObject();

        productItem.put("seq", 2);
        productItem.put("name", "실속혼합과일선물세트5호(사과4과/배1과)");
        productItem.put("category", "F");
        productItem.put("count", 1);
        productItem.put("amount", 500);
        productItem.put("biz_no", "123456790");

        productItems.add(productItem);

        json.put("productItems", productItems);

        String reqEV = EncryptAesToHexa(json.toString(), _STORE_KEY_);
        String reqVV = getHmacSha256(json.toString(),_STORE_KEY_);

        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddkkmmss");
        String date = sf.format(d);

        JSONObject postBody = new JSONObject();

        postBody.put("MID" , _MID_);
        postBody.put("RQ_DTIME", date);
        postBody.put("TNO" , date);
        postBody.put("EV" , reqEV);
        postBody.put("VV" , reqVV);
        postBody.put("RC" , "");
        postBody.put("RM" , "");

        String url = "https://zpg.dev-zeropaypoint.or.kr/" + "api_v1_payment_reserve.jct";

        byte[] resMessage = null;
        HttpURLConnection conn;

        try{
            conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Authorization", "OnlineAK " + _AUTH_KEY_);
            conn.setUseCaches(false);

            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());

            os.write(String.valueOf(postBody));
            os.flush();
            os.close();

            DataInputStream in = new DataInputStream(conn.getInputStream());
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buf = new byte[2048];

            int bcout = 0;

            while(true){
                int n = in.read(buf);
                if(n==-1)
                    break;
                bout.write(buf,0,n);
            }

            bout.flush();
            resMessage = bout.toByteArray();
            conn.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }

        if(resMessage != null){
            String temp = new String(resMessage,"UTF-8");
            JSONObject resJSON = JSONObject.fromObject(temp.trim());

            String RC = resJSON.getString("RC");
            String RM = resJSON.getString("RM");

            // RC가 정상이 아닌 경우 오류 처리
            if (!RC.equals("0000")) {
                throw new Exception(RM);
            }

            String resEV = resJSON.getString("EV");
            String resVV = resJSON.getString("VV");
            String str = "";
            if(VerifyMac(_STORE_KEY_,resEV,resVV))
                str = DecryptAesFromHexa(resEV,_STORE_KEY_);
            JSONObject rtn = JSONObject.fromObject(str);
            if("000".equals((String)rtn.get("code"))) {
                String redirectURL = "";
                boolean isMobile = false;
                if (isMobile) {
                    redirectURL = ((JSONObject)rtn.get("data")).getString("redirectURLMobile");
                } else {
                    redirectURL = ((JSONObject)rtn.get("data")).getString("redirectURLPC");
                }
//                response.sendRedirect(redirectURL); // PG 화면 호출
                return rtn;
            } else {
                // 오류처리
            }
        } else {
            // 오류처리
        }
        return json;
    }

    public static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00};

    public static String EncryptAesToHexa(String input, String key) throws
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException
    {
        if(input == null || key == null || input.length() < 1 || key.length() < 1) {
            return null;
        }
        AlgorithmParameterSpec iv = new IvParameterSpec(ivBytes);
        SecretKeySpec k = new SecretKeySpec(changeHex2Byte(key), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, k, iv);
        byte[] inBytes = input.getBytes("UTF-8");
        byte[] encBytes = c.doFinal(inBytes);
        return changeBytes2Hex(encBytes);
    }
    public static String DecryptAesFromHexa(String input, String key) throws Exception {
        if(input == null || key == null || input.length() < 1 || key.length() < 1) {
            return null;
        }
        AlgorithmParameterSpec iv = new IvParameterSpec(ivBytes);
        SecretKeySpec k = new SecretKeySpec(changeHex2Byte(key), "AES");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, k, iv);
        String decString = new String(c.doFinal(changeHex2Byte(input)));
        return decString;
    }
    public static String getHmacSha256(String input, String key) throws
            NoSuchAlgorithmException, InvalidKeyException,
            IllegalStateException, UnsupportedEncodingException
    {
        if(input == null || key == null || input.length() < 1 || key.length() < 1 ) {
            return null;
        }

        SecretKeySpec keySpec = new SecretKeySpec(changeHex2Byte(key), "HmacSHA256");

        Mac mac = Mac.getInstance("HmacSHA256");

        mac.init(keySpec);

        byte[] inBytes = input.getBytes("UTF-8");
        byte[] encBytes = mac.doFinal(inBytes);
        return changeBytes2Hex(encBytes);
    }
    public static boolean VerifyMac(String skey, String data, String hmac) throws Exception {
        String decryptedData = DecryptAesFromHexa(data, skey);
        String checkHmac = getHmacSha256(decryptedData, skey);

        if (hmac.equals(checkHmac))
            return true;
        return false;
    }
    public static byte[] changeHex2Byte(String hex) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < hex.length(); i += 2) {
            int b = Integer.parseInt(hex.substring(i, i + 2), 16);
            baos.write(b);
        }
        return baos.toByteArray();
    }
    public static String changeBytes2Hex(byte[] data){

        return Hex.encodeHexString(data);
    }
}
