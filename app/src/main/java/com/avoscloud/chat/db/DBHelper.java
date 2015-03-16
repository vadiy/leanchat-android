package com.avoscloud.chat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.avos.avoscloud.AVUser;

/**
 * Created by lzw on 14-5-28.
 */
public class DBHelper extends SQLiteOpenHelper {
  private static final int DB_VER = 6;
  private static DBHelper currentUserDBHelper;

  private DBHelper(Context context, String name, int version) {
    super(context, name, null, version);
  }

  public synchronized static DBHelper getCurrentUserInstance(Context context) {
    if (currentUserDBHelper == null) {
      AVUser user = AVUser.getCurrentUser();
      if (user == null) {
        throw new NullPointerException("current user is null");
      }
      String name = "chat_" + user.getObjectId() + ".db3";
      currentUserDBHelper = new DBHelper(context, name, DB_VER);
    }
    return currentUserDBHelper;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    MsgsTable.getInstance().createTable(db);
    RoomsTable.getInstance().createTable(db);
  }

  @Override
  public void onOpen(SQLiteDatabase db) {
    super.onOpen(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    switch (newVersion) {
      case 6:
        MsgsTable msgsTable = MsgsTable.getInstance();
        msgsTable.dropTable(db);
        msgsTable.createTable(db);
        RoomsTable roomsTable = RoomsTable.getInstance();
        roomsTable.dropTable(db);
        roomsTable.createTable(db);
      case 2:
      case 1:
        break;
    }
  }

  public synchronized void closeHelper() {
    MsgsTable.getInstance().close();
    RoomsTable.getInstance().close();
    currentUserDBHelper = null;
  }
}
