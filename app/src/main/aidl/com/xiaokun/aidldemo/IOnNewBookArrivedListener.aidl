// IOnNewBookArrivedListener.aidl
package com.xiaokun.aidldemo;
import com.xiaokun.aidldemo.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {

      void onNewBookArrived(in Book newBook);

}
