// IBookManager.aidl
package com.xiaokun.aidldemo;
import com.xiaokun.aidldemo.Book;
// Declare any non-default types here with import statements

interface IBookManager {

    List<Book> getBookList();

    void addBook(in Book book);

}
