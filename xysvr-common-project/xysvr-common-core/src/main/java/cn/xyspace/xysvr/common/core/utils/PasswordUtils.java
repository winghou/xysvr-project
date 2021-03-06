/*
 * Copyright (c) 2014 Nanjing Xiaoyou Information Technology Co.,Ltd. All rights reserved.
 */

package cn.xyspace.xysvr.common.core.utils;

import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

/**
 * 密码工具类。
 * 
 * @author ChenFangjie(2014/12/30 9:44:08)
 * 
 * @since 1.0.0
 * 
 * @version 1.0.0
 *
 */
public class PasswordUtils {

    // 让工具类彻底不可以实例化
    private PasswordUtils() {
        throw new Error("工具类不可以实例化！");
    }

    public static final String HASH_ALGORITHM = "SHA-1";

    public static final int HASH_INTERATIONS = 1024;

    /**
     * 验证明文密码与正确的密文密码是否相等。
     * 
     * @param password
     *            正确的密文密码
     * @param salt
     *            密码盐
     * @param enterPassword
     *            明文密码
     * @return 相等返回true，否则返回false
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    public static boolean matches(String password, String salt, String enterPassword) {

        if (password == null || salt == null || enterPassword == null) {
            return false;
        }

        return password.equals(encryptPassword(enterPassword, Encodes.decodeHex(salt)));
    }

    /**
     * 给定明文密码生成安全密码，经过1024次 sha-1 hash。
     * 
     * @param plainPassword
     *            明文密码
     * @param salt
     *            密码盐
     * @return 安全密码
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    public static String encryptPassword(String plainPassword, byte[] salt) {
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
        return Encodes.encodeHex(hashPassword);
    }

}
