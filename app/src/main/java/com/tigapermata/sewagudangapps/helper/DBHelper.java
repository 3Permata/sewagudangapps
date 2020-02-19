package com.tigapermata.sewagudangapps.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tigapermata.sewagudangapps.model.putaway.DataPutAway;
import com.tigapermata.sewagudangapps.model.LoginResponse;
import com.tigapermata.sewagudangapps.model.SavedId;
import com.tigapermata.sewagudangapps.model.SavedIdInbound;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    //logcat tag
    private static final String LOG = "DBHelper";

    //database version
    private static final int DATABASE_VERSION = 1;

    //database name
    private static final String DATABASE_NAME = "SewaGudangApps";

    //table name
    private static final String TABLE_TOKEN = "TokenDB";
    private static final String TABLE_ID_PG = "IdDB";
    private static final String TABLE_ID_INBOUND = "IdInbound";
    private static final String TABLE_PUT_AWAY = "PutAwayDB";

    //common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    //token table
    private static final String KEY_ID_USER = "id_user";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_TOTAL_GUDANG = "total_gudang";
    private static final String KEY_TOTAL_CUSTOMER = "total_customer";
    private static final String KEY_TOTAL_PROJECT = "total_project";

    //ids table
    private static final String KEY_ID_GUDANG = "id_gudang";
    private static final String KEY_NAMA_GUDANG = "nama_gudang";
    private static final String KEY_ID_PROJECT = "id_project";
    private static final String KEY_NAMA_PROJECT = "nama_project";

    //saved id on inbound
    private static final String KEY_ID_INBOUND = "id_inbound";
    private static final String KEY_ID_INCOMING = "id_incoming";

    //put away table
    private static final String KEY_OLD_LOCATOR = "old_locator";
    private static final String KEY_FILTER = "filter";
    private static final String KEY_ITEM = "item";
    private static final String KEY_NEW_LOCATOR = "new_locator";

    //table create statements
    //tokens table create statements
    private static final String CREATE_TABLE_TOKEN = "CREATE TABLE "
            + TABLE_TOKEN + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_USER + " TEXT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_TOKEN + " TEXT,"
            + KEY_TOTAL_GUDANG + " TEXT,"
            + KEY_TOTAL_CUSTOMER + " TEXT,"
            + KEY_TOTAL_PROJECT + " TEXT"
            + ")";

    //ids table create statements
    private static final String CREATE_TABLE_IDS = "CREATE TABLE "
            + TABLE_ID_PG + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_GUDANG + " TEXT,"
            + KEY_NAMA_GUDANG+ " TEXT,"
            + KEY_ID_PROJECT + " TEXT,"
            + KEY_NAMA_PROJECT + " TEXT"
            + ")";

    //id on inbound create statement
    private static final String CREATE_TABLE_INBOUND = "CREATE TABLE "
            + TABLE_ID_INBOUND + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_INBOUND + " TEXT,"
            + KEY_ID_INCOMING + " TEXT"
            + ")";

    //put away create statement
    private static final String CREATE_TABLE_PUT_AWAY = "CREATE TABLE "
            + TABLE_PUT_AWAY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_OLD_LOCATOR + " TEXT,"
            + KEY_FILTER + " TEXT,"
            + KEY_ITEM + " TEXT,"
            + KEY_NEW_LOCATOR + " TEXT"
            + ")";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating required tables
        db.execSQL(CREATE_TABLE_TOKEN);
        db.execSQL(CREATE_TABLE_IDS);
        db.execSQL(CREATE_TABLE_INBOUND);
        db.execSQL(CREATE_TABLE_PUT_AWAY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOKEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ID_PG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ID_INBOUND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUT_AWAY);

        onCreate(db);
    }


    //...................."TOKEN" table methods .................//

    //create token
    public void addToken(LoginResponse response) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_USER, response.getIdUser());
        values.put(KEY_USERNAME, response.getUsername());
        values.put(KEY_TOKEN, response.getToken());
        values.put(KEY_TOTAL_GUDANG, response.getTotalGudang());
        values.put(KEY_TOTAL_CUSTOMER, response.getTotalCustomer());
        values.put(KEY_TOTAL_PROJECT, response.getTotalProject());

        db.insert(TABLE_TOKEN, null, values);
        db.close();
    }

    //get single token
    public LoginResponse getTokenn() {
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TOKEN, new String[]{
                        KEY_ID,
                        KEY_ID_USER,
                        KEY_USERNAME,
                        KEY_TOKEN,
                        KEY_TOTAL_GUDANG,
                        KEY_TOTAL_CUSTOMER,
                        KEY_TOTAL_PROJECT},
                KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        LoginResponse token = new LoginResponse(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        return token;
    }


    //update token
    public int updateToken(LoginResponse response) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_USER, response.getIdUser());
        values.put(KEY_TOKEN, response.getToken());
        values.put(KEY_TOTAL_GUDANG, response.getTotalGudang());
        values.put(KEY_TOTAL_CUSTOMER, response.getTotalCustomer());
        values.put(KEY_TOTAL_PROJECT, response.getTotalProject());

        return db.update(TABLE_TOKEN, values,
                KEY_ID + "=?", new String[]{String.valueOf(1)});
    }

    //count token
    public int getTokenCount() {
        String newPairQuery = "SELECT * FROM " + TABLE_TOKEN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(newPairQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //delete token
    public void deleteToken() {
        int id = 1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TOKEN, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    //...................."IDS" table methods .................//
    //create ids
    public void addIDS(SavedId projectxGudang) {
        SQLiteDatabase dbs = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_GUDANG, projectxGudang.getIdGudang());
        values.put(KEY_NAMA_GUDANG, projectxGudang.getNamaGudang());
        values.put(KEY_ID_PROJECT, projectxGudang.getIdProject());
        values.put(KEY_NAMA_PROJECT, projectxGudang.getNamaProject());

        dbs.insert(TABLE_ID_PG, null, values);
        dbs.close();
    }

    public int getIdsCount() {
        String newPairQuery = "SELECT * FROM " + TABLE_ID_PG;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(newPairQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateIds(SavedId ids) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_GUDANG, ids.getIdGudang());
        values.put(KEY_NAMA_GUDANG, ids.getNamaGudang());
        values.put(KEY_ID_PROJECT, ids.getIdProject());
        values.put(KEY_NAMA_PROJECT, ids.getNamaProject());

        return db.update(TABLE_ID_PG, values,
                KEY_ID + "=?", new String[]{String.valueOf(1)});
    }

    //get single ids
    public SavedId getIds() {
        int id = 1;
        SQLiteDatabase dbs = this.getReadableDatabase();
        Cursor cursor = dbs.query(TABLE_ID_PG, new String[]{
                        KEY_ID,
                        KEY_ID_GUDANG,
                        KEY_NAMA_GUDANG,
                        KEY_ID_PROJECT,
                        KEY_NAMA_PROJECT},
                KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        SavedId ids = new SavedId(
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));

        return ids;
    }

    //delete ids
    public void deleteIds() {
        int id = 1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ID_PG, KEY_ID + "=?", new String[]{String.valueOf(id)});
    }

    //...................."id on inbound" table methods .................//

    //create saved id
    public void addIdInbound(SavedIdInbound inbound) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_INBOUND, inbound.getIdInbound());
        values.put(KEY_ID_INCOMING, inbound.getIdIncoming());

        database.insert(TABLE_ID_INBOUND, null, values);
        database.close();
    }

    public int getIdInboundCount() {
        String pairQuery = "SELECT * FROM " + TABLE_ID_INBOUND;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(pairQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int updateIdInbound(SavedIdInbound idInbound) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_INBOUND, idInbound.getIdInbound());
        values.put(KEY_ID_INCOMING, idInbound.getIdIncoming());

        return database.update(TABLE_ID_INBOUND, values,
                KEY_ID + "=?", new String[]{String.valueOf(1)});
    }

    public SavedIdInbound getIdInbound() {
        int id = 1;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_ID_INBOUND, new String[]{
                KEY_ID,
                KEY_ID_INBOUND,
                KEY_ID_INCOMING},
                KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        SavedIdInbound idInbound = new SavedIdInbound(
                cursor.getString(1),
                cursor.getString(2));

        return idInbound;
    }

    //clear database token
    public void clearDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ TABLE_TOKEN;
        db.execSQL(clearDBQuery);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOKEN);
        db.execSQL(CREATE_TABLE_TOKEN);

    }

    //...................."PUT AWAY" table methods .................//
    //add put away data
    public void addPutAway(DataPutAway putAway) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OLD_LOCATOR, putAway.getOldLocator());
        values.put(KEY_FILTER, putAway.getFilter());
        values.put(KEY_ITEM, putAway.getItem());
        values.put(KEY_NEW_LOCATOR, putAway.getNewLocator());

        database.insert(TABLE_PUT_AWAY, null, values);
        database.close();
    }

    //get all put away data
    public ArrayList<DataPutAway> getAllPutAwayData() {
        ArrayList<DataPutAway> putAwayArrayList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PUT_AWAY, null);
        while (cursor.moveToNext()) {
            String oldLocator = cursor.getString(1);
            String filter = cursor.getString(2);
            String item = cursor.getString(3);
            String newLocator = cursor.getString(4);

            DataPutAway newData = new DataPutAway(oldLocator, filter, item, newLocator);
            putAwayArrayList.add(newData);
        }
        return putAwayArrayList;
    }

    public int getPutAwayCount() {
        String pairQuery = "SELECT * FROM " + TABLE_PUT_AWAY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(pairQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //clear database token
    public void clearPutAway() {
        SQLiteDatabase db = getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+ TABLE_PUT_AWAY;
        db.execSQL(clearDBQuery);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUT_AWAY);
        db.execSQL(CREATE_TABLE_PUT_AWAY);

    }
}
