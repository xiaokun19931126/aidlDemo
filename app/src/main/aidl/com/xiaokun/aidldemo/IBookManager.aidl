// IBookManager.aidl
package com.xiaokun.aidldemo;
import com.xiaokun.aidldemo.Book;
import com.xiaokun.aidldemo.IOnNewBookArrivedListener;
// Declare any non-default types here with import statements

interface IBookManager {

    List<Book> getBookList();

    void addBook(in Book book);

    void registerListener(IOnNewBookArrivedListener listner);

    void unRegisterListener(IOnNewBookArrivedListener listener);

    void add(int a , int b);

}
