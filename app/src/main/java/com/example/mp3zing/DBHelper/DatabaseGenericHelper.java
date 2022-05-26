package com.example.mp3zing.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mp3zing.model.modelAppOrApi.ParamFilterQuery;
import com.example.mp3zing.model.modelDatabase.BaseClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DatabaseGenericHelper extends SQLiteOpenHelper {

    public static String nameDatabase = "dataTest2";
    private String tableDatabase = "";
    private BaseClass entityBaseClass;

    public DatabaseGenericHelper(Context context, BaseClass entityBaseClass){
        super(context, nameDatabase, null, 1);
        tableDatabase = entityBaseClass.getClass().getSimpleName();
        this.entityBaseClass = entityBaseClass;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String buildColumnStringCreate = "id integer primary key";

        Field[] listProperty =  entityBaseClass.getClass().getDeclaredFields();
        for(Field field : listProperty)
        {
            String typeProperty = field.getType().getSimpleName();
            if(typeProperty == "String")
                typeProperty = "Text";
            else if(typeProperty == "Integer")
                typeProperty = "integer";
            buildColumnStringCreate+= ", " + field.getName()+ " " + typeProperty;
        }
        String createTable = "CREATE TABLE " + this.tableDatabase + "(" + buildColumnStringCreate + ")";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists " + this.tableDatabase);

        onCreate(sqLiteDatabase);
    }

    public BaseClass getRecordById(int id) throws IllegalAccessException, InstantiationException {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String [] columns = new String[entityBaseClass.getClass().getDeclaredFields().length];
        Field[] listProperty =  entityBaseClass.getClass().getDeclaredFields();
        for(int i = 0; i < listProperty.length;i++)
        {
            columns[i] = listProperty[i].getName();
        }
        Cursor cursor = sqLiteDatabase.query(tableDatabase,columns,"id=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor != null)
            cursor.moveToFirst();

        BaseClass record = entityBaseClass.getClass().newInstance();
        record.initWithCursor(cursor);
        cursor.close();
        return record;
    }

    public List<? extends BaseClass> getList(@Nullable List<ParamFilterQuery> listFilter)
    {
        List<BaseClass> listResult = new ArrayList<BaseClass>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String stringQuery = "SELECT * FROM " + this.tableDatabase;
        Field[] listProperty =  entityBaseClass.getClass().getDeclaredFields();
        String conditionSqlFilter = " WHERE";
        String [] stringArgs = new String[listFilter!= null ? listFilter.size(): 0];

        if(listFilter != null)
        {
            int indexFilter = 0;
            for(ParamFilterQuery paramFilter: listFilter) {
                switch (paramFilter.typeFilter)
                {
                    case "OR":
                    case "AND":
                        if(indexFilter != 0)
                            conditionSqlFilter+= " " + paramFilter.typeFilter + " " + paramFilter.fieldName + "=?";
                        else
                            conditionSqlFilter+= " " + paramFilter.fieldName + "=?";
                        stringArgs[indexFilter] = paramFilter.valueField.toString();
                        break;
                    case "LIKE":
                        if(indexFilter != 0)
                            conditionSqlFilter+= " AND " + paramFilter.fieldName + " Like ?";
                        else
                            conditionSqlFilter+= " " + paramFilter.fieldName + " Like ?";
                        stringArgs[indexFilter] = "%" + paramFilter.valueField.toString() + "%";
                        break;
                }
            }
        }

        if(listFilter != null && listFilter.size() != 0)
            stringQuery += conditionSqlFilter;
        Cursor cursor = sqLiteDatabase.rawQuery(stringQuery,stringArgs);
        if(cursor != null && cursor.moveToFirst())
        {
            do {
                try {
                    BaseClass objectResult = entityBaseClass.getClass().newInstance();
                    objectResult.initWithCursor(cursor);
                    listResult.add(objectResult);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }while(cursor.moveToNext());
        }
        cursor.close();

        return listResult;
    }

    public long deleteById(int idRecord)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(this.tableDatabase,"id=?", new String[]{String.valueOf(idRecord)});
    }

    public long insertRecord(BaseClass newRecord){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        Field[] listProperty =  entityBaseClass.getClass().getDeclaredFields();
        for(int i = 0; i < listProperty.length;i++)
        {
            try {
                listProperty[i].setAccessible(true);
                String fieldName = listProperty[i].getName();
                Object valueField = null;
                valueField = listProperty[i].get(newRecord);
                if(fieldName != "id")
                    contentValue.put(fieldName,valueField.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return sqLiteDatabase.insert(this.tableDatabase,null,contentValue);
    }

    public long updateRecord(BaseClass editRecord) throws IllegalAccessException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        Field[] listProperty =  entityBaseClass.getClass().getDeclaredFields();
        for(int i = 0; i < listProperty.length;i++)
        {
            listProperty[i].setAccessible(true);
            String fieldName = listProperty[i].getName();
            Object valueField = listProperty[i].get(editRecord);
            if(fieldName != "id")
                contentValue.put(fieldName,valueField.toString());
        }
        return sqLiteDatabase.update(this.tableDatabase,contentValue,"id=?",new String[]{String.valueOf(editRecord.getId())});
    }
}
