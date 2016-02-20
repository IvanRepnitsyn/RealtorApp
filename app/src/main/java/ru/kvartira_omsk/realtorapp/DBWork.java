package ru.kvartira_omsk.realtorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DBWork extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "realtorarm.db";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_TABLE_OBJECTS = "myobjects";
    private static final String DATABASE_TABLE_EVENTS = "myevents";
    private static final String DATABASE_TABLE_CLIENTS = "myclients";

    // поля таблицы
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAMEOBJECT = "nameobject";
    public static final String COLUMN_IDCLIENT = "idclient";
    public static final String COLUMN_OBJECTADDRESS = "objectaddress";
    public static final String COLUMN_PRICECLIENT = "priceclient";




    private static final String OBJECTSTABLE_CREATE = "create table "
            + DATABASE_TABLE_OBJECTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAMEOBJECT
            + " text not null, " + COLUMN_IDCLIENT + " text,"
            + COLUMN_OBJECTADDRESS + " text,"
            + COLUMN_PRICECLIENT + " text" + ");";


    public static final String COLUMN_IDOBJECT = "idobject";
    public static final String COLUMN_NAMEEVENT = "nameeevent";
    public static final String COLUMN_TYPEEVENT = "typeevent";
    public static final String COLUMN_DATEEVENT = "dateevent";
    public static final String COLUMN_TIMEEVENT = "timeevent";
    public static final String COLUMN_PLACEEVENT = "placeevent";
    public static final String COLUMN_INFOEVENT = "infoevent";


    private static final String EVENTSTABLE_CREATE = "create table "
            + DATABASE_TABLE_EVENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_IDOBJECT + " text,"
            + COLUMN_IDCLIENT + " text,"
            + COLUMN_TYPEEVENT + " text not null, "
            + COLUMN_NAMEEVENT + " text,"
            + COLUMN_DATEEVENT + " text,"
            + COLUMN_TIMEEVENT + " text,"
            + COLUMN_PLACEEVENT + " text,"
            + COLUMN_INFOEVENT + " text" + ");";


    public static final String COLUMN_NAMECLIENT = "nameclient";
    public static final String COLUMN_TYPECLIENT = "typeclient";
    public static final String COLUMN_PHONECLIENT = "phoneclient";
    public static final String COLUMN_MAILCLIENT = "mailclient";

    private static final String CLIENTSTABLE_CREATE = "create table "
            + DATABASE_TABLE_CLIENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_NAMECLIENT + " text,"
            + COLUMN_TYPECLIENT + " text not null, "
            + COLUMN_PHONECLIENT + " text,"
            + COLUMN_MAILCLIENT + " text" + ");";




    public DBWork(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }






    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(OBJECTSTABLE_CREATE);
        db.execSQL(EVENTSTABLE_CREATE);
        db.execSQL(CLIENTSTABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        Log.w(DBWork.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS myobjects");
        db.execSQL("DROP TABLE IF EXISTS myevents");
        db.execSQL("DROP TABLE IF EXISTS myclients");
        onCreate(db);
    }

    /**
     * Создаёт новый элемент списка объектов. Если создан успешно - возвращается
     * номер строки rowId, иначе -1
     */
    public long createNewObject (String nameobject, String idclient,
                                 String objectaddress, String priceclient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentObjects(nameobject, idclient,
                objectaddress, priceclient);

        long row = db.insert(DATABASE_TABLE_OBJECTS, null, initialValues);
        db.close();

        return row;
    }

    public long createNewEvent (String idobject, String idclient,  String nameevent,
                                String typeevent, String dateevent,
                                String timeevent, String placeevent,
                                String infoevent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentEvents(idobject, idclient, nameevent,
                typeevent, dateevent, timeevent, placeevent, infoevent);

        long row = db.insert(DATABASE_TABLE_EVENTS, null, initialValues);
        db.close();

        return row;
    }

    public long createNewClient (String nameclient, String typeclient,
                                 String phoneclient, String mailclient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = createContentClients(nameclient,
                typeclient, phoneclient, mailclient);

        long row = db.insert(DATABASE_TABLE_CLIENTS, null, initialValues);
        db.close();

        return row;
    }


    /**
     * Обновляет список
     */


    public boolean updateObject(long rowId, String nameobject, String idclient,
                                String objectaddress, String priceclient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentObjects(nameobject, idclient,
                objectaddress, priceclient);

        return db.update(DATABASE_TABLE_OBJECTS, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }

    public boolean updateEvent (long rowId, String idobject, String idclient, String nameevent,
                                String typeevent, String dateevent,
                                String timeevent, String placeevent, String infoevent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentEvents(idobject, idclient, nameevent,
                typeevent, dateevent, timeevent, placeevent, infoevent);

        return db.update(DATABASE_TABLE_EVENTS, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }

    public boolean updateClient(long rowId, String nameclient, String typeclient,
                                String phoneclient, String mailclient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = createContentClients(nameclient, typeclient,
                phoneclient, mailclient);

        return db.update(DATABASE_TABLE_CLIENTS, updateValues, COLUMN_ID + "=" + rowId,
                null) > 0;
    }
    /**
     * Удаляет элемент списка
     */
    public void deleteObject(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_OBJECTS, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    public void deleteEvent(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_EVENTS, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    public void deleteClient(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_CLIENTS, COLUMN_ID + "=" + rowId, null);
        db.close();
    }

    /**
     * Возвращает курсор со всеми элементами списка дел
     *
     * @return курсор с результатами всех записей
     */
    public Cursor getAllObjects() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE_OBJECTS, new String[] { COLUMN_ID,
                        COLUMN_NAMEOBJECT, COLUMN_IDCLIENT, COLUMN_OBJECTADDRESS,
                        COLUMN_PRICECLIENT }, null,
                null, null, null, null);
    }

    public Cursor getAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE_EVENTS, new String[] { COLUMN_ID,
                        COLUMN_IDOBJECT, COLUMN_IDCLIENT, COLUMN_NAMEEVENT, COLUMN_TYPEEVENT,
                        COLUMN_DATEEVENT, COLUMN_TIMEEVENT, COLUMN_PLACEEVENT, COLUMN_INFOEVENT }, null,
                null, null, null, null);
    }

    public Cursor getAllClients() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(DATABASE_TABLE_CLIENTS, new String[] { COLUMN_ID,
                        COLUMN_NAMECLIENT, COLUMN_TYPECLIENT,
                        COLUMN_PHONECLIENT, COLUMN_MAILCLIENT }, null,
                null, null, null, null);
    }

    public List<String> getAllObjectsSpinner(){
        List<String> objectlabels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_OBJECTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                objectlabels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return objectlabels;
    }

    public List<String> getAllClientsSpinner(){
        List<String> clientlabels = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_CLIENTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                clientlabels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return clientlabels;
    }


    /**
     * Возвращает курсор с указанной записи
     */
    public Cursor getObject(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE_OBJECTS,
                new String[] { COLUMN_ID,
                        COLUMN_NAMEOBJECT, COLUMN_IDCLIENT, COLUMN_OBJECTADDRESS,
                        COLUMN_PRICECLIENT }, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getObjectforName(String NameObject) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE_OBJECTS,
                new String[] { COLUMN_ID,
                        COLUMN_NAMEOBJECT, COLUMN_IDCLIENT, COLUMN_OBJECTADDRESS,
                        COLUMN_PRICECLIENT }, COLUMN_NAMEOBJECT + "=" + NameObject, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getEvent(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE_EVENTS,
                new String[] { COLUMN_ID,
                        COLUMN_IDOBJECT, COLUMN_IDCLIENT, COLUMN_NAMEEVENT, COLUMN_TYPEEVENT,
                        COLUMN_DATEEVENT, COLUMN_TIMEEVENT, COLUMN_PLACEEVENT, COLUMN_INFOEVENT }, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getClient(long rowId) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE_CLIENTS,
                new String[] { COLUMN_ID,
                        COLUMN_NAMECLIENT, COLUMN_TYPECLIENT,
                        COLUMN_PHONECLIENT, COLUMN_MAILCLIENT }, COLUMN_ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        db.close();
        return mCursor;
    }

    public Cursor getClientforName(String NameClient) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(true, DATABASE_TABLE_CLIENTS,
                new String[] { COLUMN_ID,
                        COLUMN_NAMECLIENT, COLUMN_TYPECLIENT, COLUMN_PHONECLIENT,
                        COLUMN_MAILCLIENT }, COLUMN_NAMECLIENT + "=" + NameClient, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    /*
	 * Создаёт пару ключ-значение и записывает в базу
	 */
    private ContentValues createContentObjects(String nameobject, String idclient,
                                               String objectaddress, String priceclient) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMEOBJECT, nameobject);
        values.put(COLUMN_IDCLIENT, idclient);
        values.put(COLUMN_OBJECTADDRESS, objectaddress);
        values.put(COLUMN_PRICECLIENT, priceclient);
        return values;
    }

    private ContentValues createContentEvents(String idobject, String idclient, String nameevent,
                                              String typeevent, String dateevent,
                                              String timeevent, String placeevent,
                                              String infoevent) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IDOBJECT, idobject);
        values.put(COLUMN_IDCLIENT, idclient);
        values.put(COLUMN_NAMEEVENT, nameevent);
        values.put(COLUMN_TYPEEVENT, typeevent);
        values.put(COLUMN_DATEEVENT, dateevent);
        values.put(COLUMN_TIMEEVENT, timeevent);
        values.put(COLUMN_PLACEEVENT, placeevent);
        values.put(COLUMN_INFOEVENT, infoevent);
        return values;
    }

    private ContentValues createContentClients(String nameclient, String typeclient,
                                               String phoneclient, String mailclient) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMECLIENT, nameclient);
        values.put(COLUMN_TYPECLIENT, typeclient);
        values.put(COLUMN_PHONECLIENT, phoneclient);
        values.put(COLUMN_MAILCLIENT, mailclient);
        return values;
    }

}
