package com.xiaokun.aidldemo;

import java.io.Serializable;

/**
 * Created by 肖坤 on 2018/1/28.
 *
 * @author 肖坤
 * @date 2018/1/28
 */

public class User implements Serializable
{
    public static final long serialVersionUID = 234234325L;

    public int userId;
    public String userName;
    public boolean isMale;

    public User(int userId, String userName, boolean isMale)
    {
        this.userId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }
}
