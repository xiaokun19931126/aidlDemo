package com.xiaokun.aidldemo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/01/26
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */

public class Book implements Parcelable
{
    public int bookId;
    public String bookName;

    public Book(int bookId, String bookName)
    {
        this.bookId = bookId;
        this.bookName = bookName;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.bookId);
        dest.writeString(this.bookName);
    }

    protected Book(Parcel in)
    {
        this.bookId = in.readInt();
        this.bookName = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>()
    {
        @Override
        public Book createFromParcel(Parcel source)
        {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size)
        {
            return new Book[size];
        }
    };
}
