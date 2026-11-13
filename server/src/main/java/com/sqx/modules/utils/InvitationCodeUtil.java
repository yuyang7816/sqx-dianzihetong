package com.sqx.modules.utils;

import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 邀请码生成解密工具类
 * @author fang
 * @date 2020/7/8
 */
@Component
public class InvitationCodeUtil {

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        InvitationCodeUtil.userService = userService;
    }


    /** 自定义进制（选择你想要的进制数，不能重复且最好不要0、1这些容易混淆的字符） */
    private static final char[] r=new char[]{ 'M', 'J', 'U', 'D', 'Z', 'X', '9', 'C', '7', 'P','E', '8', '6', 'B', 'G', 'H', 'S', '2', '5',  'F', 'R', '4','Q', 'W', 'K', '3', 'V', 'Y', 'T', 'N'};

    /** 定义一个字符用来补全邀请码长度（该字符前面是计算出来的邀请码，后面是用来补全用的） */
    private static final char b='A';

    /** 进制长度 */
    private static final int binLen=r.length;

    /** 邀请码长度 */
    private static final int s=6;


    /** 补位字符串 */
    private static final String e="KSLFXFR";

    /**
     * 根据ID生成六位随机码
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        char[] buf=new char[32];
        int charPos=32;

        while((id / binLen) > 0) {
            int ind=(int)(id % binLen);
            buf[--charPos]=r[ind];
            id /= binLen;
        }
        buf[--charPos]=r[(int)(id % binLen)];
        String str=new String(buf, charPos, (32 - charPos));
        // 不够长度的自动补全
        if(str.length() < s) {
            StringBuilder sb=new StringBuilder();
            sb.append(e.subSequence(0, s-str.length()));
            str+=sb.toString();
        }
        return str;
    }

    public static String toSerialCode() {
        //元素

        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random rand = new Random();
        while (true) {
            for (int i = 10; i > 1; i--) {
                int index = rand.nextInt(i);
                int tmp = array[index];
                array[index] = array[i - 1];
                array[i - 1] = tmp;
            }
            int result = 0;
            for (int i = 0; i < 6; i++) {
                result = result * 10 + array[i];
            }

            String sixString = Integer.toString(result);
            if (sixString.length() == 5) {
                sixString = "0" + sixString;

            }
            String str = sixString;
            UserEntity userEntity = userService.queryByInvitationCode(str);
            if (userEntity == null) {
                return str;
            }
        }
    }
    /**
     * 根据随机码生成ID
     * @param code 随机码
     * @return ID
     */
    public static long codeToId(String code) {
        char[] chs;
        chs = code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j=0; j < binLen; j++) {
                if(chs[i] == r[j]) {
                    ind=j;
                    break;
                }
            }
            if(chs[i] == b) {
                break;
            }
            if(i > 0) {
                res=res * binLen + ind;
            } else {
                res=ind;
            }
        }
        return res;
    }






}