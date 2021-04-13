/**
 * Copyright (C) 2018 iDEA foundation pvt.,Ltd
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.builderstrom.user.repository.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.builderstrom.user.repository.database.databaseModels.AccessSettingTS;
import com.builderstrom.user.repository.database.databaseModels.Categories;
import com.builderstrom.user.repository.database.databaseModels.DbCompanyDocuments;
import com.builderstrom.user.repository.database.databaseModels.DbDigitalDocs;
import com.builderstrom.user.repository.database.databaseModels.DbRFI;
import com.builderstrom.user.repository.database.databaseModels.DbSnag;
import com.builderstrom.user.repository.database.databaseModels.DbTimeSheetTask;
import com.builderstrom.user.repository.database.databaseModels.DbTimesheet;
import com.builderstrom.user.repository.database.databaseModels.DbToDo;
import com.builderstrom.user.repository.database.databaseModels.Diary;
import com.builderstrom.user.repository.database.databaseModels.DiaryMetaDataDb;
import com.builderstrom.user.repository.database.databaseModels.Domain;
import com.builderstrom.user.repository.database.databaseModels.Drawings;
import com.builderstrom.user.repository.database.databaseModels.Gallery;
import com.builderstrom.user.repository.database.databaseModels.GeneralSetting;
import com.builderstrom.user.repository.database.databaseModels.Login;
import com.builderstrom.user.repository.database.databaseModels.Permission;
import com.builderstrom.user.repository.database.databaseModels.ProjectDocs;
import com.builderstrom.user.repository.database.databaseModels.ReportProblem;
import com.builderstrom.user.repository.database.databaseModels.ShareDocs;
import com.builderstrom.user.repository.database.databaseModels.ShortcutCategories;
import com.builderstrom.user.repository.database.databaseModels.SignedProject;
import com.builderstrom.user.repository.database.databaseModels.ToDoCompleteStatus;
import com.builderstrom.user.repository.database.databaseModels.UserProjects;
import com.builderstrom.user.repository.database.databaseModels.Users;
import com.builderstrom.user.repository.retrofit.modals.CompanyDocument;
import com.builderstrom.user.repository.retrofit.modals.DigitalDoc;
import com.builderstrom.user.repository.retrofit.modals.DigitalFormModel;
import com.builderstrom.user.repository.retrofit.modals.PDocsDataModel;
import com.builderstrom.user.repository.retrofit.modals.PojoMyItem;
import com.builderstrom.user.repository.retrofit.modals.PojoToDo;
import com.builderstrom.user.repository.retrofit.modals.ProjectDetails;
import com.builderstrom.user.repository.retrofit.modals.SyncToDoModel;
import com.builderstrom.user.utils.BuilderStormApplication;
import com.builderstrom.user.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 48;/* changed from 47->48 on 26th Oct 2020*/
    private static final String DATABASE_NAME = "builderstorm_db";

    // Initialize database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(Domain.CREATE_TABLE);
        db.execSQL(SignedProject.CREATE_TABLE);
        db.execSQL(Gallery.CREATE_TABLE);
        db.execSQL(Diary.CREATE_TABLE);
        db.execSQL(ReportProblem.CREATE_TABLE);
        db.execSQL(Login.CREATE_TABLE);
        db.execSQL(UserProjects.CREATE_TABLE);
        db.execSQL(Drawings.CREATE_TABLE);
        db.execSQL(DbRFI.CREATE_TABLE);
        db.execSQL(DbSnag.CREATE_TABLE);
        db.execSQL(Users.CREATE_TABLE);
        db.execSQL(Categories.CREATE_TABLE);
        db.execSQL(ShortcutCategories.CREATE_TABLE);
        db.execSQL(DiaryMetaDataDb.CREATE_TABLE);
        db.execSQL(Permission.CREATE_TABLE);
        db.execSQL(GeneralSetting.CREATE_TABLE);
        db.execSQL(DbDigitalDocs.CREATE_TABLE);
        db.execSQL(DbTimesheet.CREATE_TABLE);
        db.execSQL(DbTimeSheetTask.CREATE_TABLE);
        db.execSQL(AccessSettingTS.CREATE_TABLE);
        db.execSQL(ProjectDocs.CREATE_TABLE);
        db.execSQL(DbCompanyDocuments.CREATE_TABLE);
        db.execSQL(ShareDocs.CREATE_TABLE);
        db.execSQL(DbToDo.CREATE_TABLE);
        db.execSQL(ToDoCompleteStatus.CREATE_TABLE);
        Log.e("onCreate", "LocalDataBase");

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        Log.e("onUpgrade", "LocalDataBase");
        if (newVersion > oldVersion) {
            Log.e("onUpgrade", "LocalDataBase");
//            db.execSQL(Diary.ALTER_TABLE);

//            db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");

            db.execSQL("DROP TABLE IF EXISTS " + Domain.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SignedProject.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Gallery.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Diary.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ReportProblem.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Login.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + UserProjects.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Drawings.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DbRFI.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Users.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Categories.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ShortcutCategories.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DbSnag.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DiaryMetaDataDb.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Permission.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + GeneralSetting.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DbDigitalDocs.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DbTimesheet.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DbTimeSheetTask.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + AccessSettingTS.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ProjectDocs.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DbCompanyDocuments.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ShareDocs.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + DbToDo.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ToDoCompleteStatus.TABLE_NAME);

//              Remove all local preferences on Upgrade DataBase
            BuilderStormApplication.mPrefs.onDBUpgrade();
            onCreate(db);
        }
    }

    public void clearUsers() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(Login.TABLE_NAME, null, null);
//        closeDataBase(db);

//        db.execSQL("delete from " + Users.TABLE_NAME);
//        closeDataBase(db);
    }

    /***********************************************************************************************************
     *                                              Domain operations
     * ********************************************************************************************************/

    public long insertDomain(String domainName, String siteID) {
        long id = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            values.put(Domain.COLUMN_NAME, domainName);
            values.put(Domain.COLUMN_SITE_ID, siteID);
            id = db.insert(Domain.TABLE_NAME, null, values);
            closeDataBase(db);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private void closeDataBase(SQLiteDatabase db) {
//        if (db != null && db.isOpen()) {
//            db.releaseReference();
//        }
    }

    public long updateDomain(String domainName, String siteID) {
        long id = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Domain.COLUMN_SITE_ID, siteID);
            id = db.update(Domain.TABLE_NAME, values,
                    Domain.COLUMN_NAME + " = ?",
                    new String[]{String.valueOf(domainName)});
            closeDataBase(db);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public Domain getDomain(String domainName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Domain.TABLE_NAME,
                new String[]{Domain.COLUMN_ID, Domain.COLUMN_NAME, Domain.COLUMN_SITE_ID, Domain.COLUMN_TIMESTAMP},
                Domain.COLUMN_NAME + "=?",
                new String[]{String.valueOf(domainName)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Domain domain = new Domain(
                    cursor.getInt(cursor.getColumnIndex(Domain.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Domain.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(Domain.COLUMN_SITE_ID)),
                    cursor.getString(cursor.getColumnIndex(Domain.COLUMN_TIMESTAMP))
            );
            cursor.close();
            return domain;
        }
        return null;
    }

    /***********************************************************************************************************
     *                                              Permission operations
     * ********************************************************************************************************/

    public long insertPermission(String siteID, String userID, String userPermission,
                                 String permissionSection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Permission.COLUMN_SITE_ID, siteID);
        values.put(Permission.COLUMN_USER_ID, userID);
        values.put(Permission.COLUMN_USER_PERMISSION, userPermission);
        values.put(Permission.COLUMN_PERMISSION_SECTION, permissionSection);
        long id = db.insert(Permission.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updatePermission(String siteID, String userID, String userPermission,
                                 String permissionSection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Permission.COLUMN_USER_PERMISSION, userPermission);
        values.put(Permission.COLUMN_PERMISSION_SECTION, permissionSection);
        long id = db.update(Permission.TABLE_NAME, values,
                Permission.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        Permission.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)});
        closeDataBase(db);

        return id;
    }

//    public long updateUserPermission(String siteID, String userID, String userPermission) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(Permission.COLUMN_USER_PERMISSION, userPermission);
//        long id = db.update(Permission.TABLE_NAME, values,
//                Permission.COLUMN_SITE_ID + " = ?"
//                        + " AND " +
//                        Permission.COLUMN_USER_ID + "=?",
//                new String[]{String.valueOf(siteID), String.valueOf(userID)});
//       closeDataBase(db);
//        return id;
//    }


    public Permission getPermission(String siteID, String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Permission.TABLE_NAME,
                new String[]{Permission.COLUMN_ID, Permission.COLUMN_SITE_ID, Permission.COLUMN_USER_ID, Permission.COLUMN_USER_PERMISSION, Permission.COLUMN_PERMISSION_SECTION, Permission.COLUMN_TIMESTAMP},
                Permission.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        Permission.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            Permission permission = new Permission(
                    cursor.getInt(cursor.getColumnIndex(Permission.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Permission.COLUMN_SITE_ID)),
                    cursor.getString(cursor.getColumnIndex(Permission.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(Permission.COLUMN_USER_PERMISSION)),
                    cursor.getString(cursor.getColumnIndex(Permission.COLUMN_PERMISSION_SECTION)),
                    cursor.getString(cursor.getColumnIndex(Permission.COLUMN_TIMESTAMP))
            );
            cursor.close();
            return permission;
        }
        return null;
    }

    /***********************************************************************************************************
     *                                              General Settings
     * ********************************************************************************************************/

    public long insertGeneralSetting(String siteID, String userID, String setting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GeneralSetting.COLUMN_SITE_ID, siteID);
        values.put(GeneralSetting.COLUMN_USER_ID, userID);
        values.put(GeneralSetting.COLUMN_SETTING, setting);
        long id = db.insert(GeneralSetting.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }


    public long updateGeneralSetting(String siteID, String userID, String setting) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GeneralSetting.COLUMN_SETTING, setting);
        long id = db.update(GeneralSetting.TABLE_NAME, values,
                GeneralSetting.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        GeneralSetting.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)});
        closeDataBase(db);
        return id;
    }

    public GeneralSetting getGeneralSetting(String siteID, String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(GeneralSetting.TABLE_NAME,
                new String[]{GeneralSetting.COLUMN_SETTING},
                GeneralSetting.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        GeneralSetting.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            GeneralSetting setting = new GeneralSetting(cursor.getString(cursor.getColumnIndex(GeneralSetting.COLUMN_SETTING)));
            cursor.close();
            return setting;
        }
        return null;
    }


    /***********************************************************************************************************
     *                                              USER operations
     * ********************************************************************************************************/

    public long insertUser(String siteID, String userID, String projectID, String section,
                           String userData) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Users.COLUMN_SITE_ID, siteID);
        values.put(Users.COLUMN_USER_ID, userID);
        values.put(Users.COLUMN_PROJECT_ID, projectID);
        values.put(Users.COLUMN_SECTION_NAME, section);
        values.put(Users.COLUMN_ITEM_DATA, userData);
        long id = db.insert(Users.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;

    }

    public long updateUsersList(String siteID, String userID, String section,
                                String userData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Users.COLUMN_ITEM_DATA, userData);
        long id = db.update(Users.TABLE_NAME, values,
                Users.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        Users.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Users.COLUMN_SECTION_NAME + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), section});
        closeDataBase(db);
        return id;
    }

    public Users getUserDataBySection(String siteID, String userID, String section) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(Users.TABLE_NAME,
                    new String[]{Users.COLUMN_ITEM_DATA},
                    Users.COLUMN_SITE_ID + "=?"
                            + " AND " +
                            Users.COLUMN_USER_ID + "=?"
                            + " AND " +
                            Users.COLUMN_SECTION_NAME + "=?",
                    new String[]{String.valueOf(siteID), String.valueOf(userID), section}, null,
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                Users userData = new Users(
                        cursor.getString(cursor.getColumnIndex(Users.COLUMN_ITEM_DATA))
                );
                cursor.close();
                return userData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***********************************************************************************************************
     *                                              CATEGORIES operations
     * ********************************************************************************************************/

    public long insertCategory(String siteID, String userID, String section, String itemData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Categories.COLUMN_SITE_ID, siteID);
        values.put(Categories.COLUMN_USER_ID, userID);
        values.put(Categories.COLUMN_SECTION_NAME, section);
        values.put(Categories.COLUMN_ITEM_DATA, itemData);
        long id = db.insert(Categories.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updateCategoryList(String siteID, String userID, String section, String itemData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Categories.COLUMN_ITEM_DATA, itemData);
        long id = db.update(Categories.TABLE_NAME, values,
                Categories.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        Categories.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Categories.COLUMN_SECTION_NAME + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), section});
        closeDataBase(db);
        return id;
    }

    public Categories getCategoryBySection(String siteID, String userID, String section) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(Categories.TABLE_NAME,
                    new String[]{Categories.COLUMN_ITEM_DATA},
                    Categories.COLUMN_SITE_ID + "=?"
                            + " AND " +
                            Categories.COLUMN_USER_ID + "=?"
                            + " AND " +
                            Categories.COLUMN_SECTION_NAME + "=?",
                    new String[]{String.valueOf(siteID), String.valueOf(userID), section}, null,
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                Categories itemData = new Categories(cursor.getString(cursor.getColumnIndex(Categories.COLUMN_ITEM_DATA))
                );
                cursor.close();
                return itemData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Categories getAllCategory(String siteID, String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(Categories.TABLE_NAME,
                    new String[]{Categories.COLUMN_ITEM_DATA},
                    Categories.COLUMN_SITE_ID + "=?"
                            + " AND " +
                            Categories.COLUMN_USER_ID + "=?",
                    new String[]{String.valueOf(siteID), String.valueOf(userID)}, null,
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                Categories itemData = new Categories(cursor.getString(cursor.getColumnIndex(Categories.COLUMN_ITEM_DATA))
                );
                cursor.close();
                return itemData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***********************************************************************************************************
     *                                              Meta Data
     * *******************************************************************************************************
     * */


    public long insertMetaDataBySection(String siteID, String userID, String projectID, String
            userName, String section, String diaryData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DiaryMetaDataDb.COLUMN_SITE_ID, siteID);
        values.put(DiaryMetaDataDb.COLUMN_USER_ID, userID);
        values.put(DiaryMetaDataDb.COLUMN_PROJECT_ID, projectID);
        values.put(DiaryMetaDataDb.COLUMN_USER, userName);
        values.put(DiaryMetaDataDb.COLUMN_SECTION_NAME, section);
        values.put(DiaryMetaDataDb.COLUMN_ITEM_DATA, diaryData);
        long id = db.insert(DiaryMetaDataDb.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updateMetaDataBySection(String siteID, String projectID, String sectionName,
                                        String diaryData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DiaryMetaDataDb.COLUMN_ITEM_DATA, diaryData);
        long id = db.update(DiaryMetaDataDb.TABLE_NAME, values,
                DiaryMetaDataDb.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        DiaryMetaDataDb.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        DiaryMetaDataDb.COLUMN_SECTION_NAME + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(projectID), sectionName});
        closeDataBase(db);
        return id;
    }

    public DiaryMetaDataDb getMetaDataBySection(String siteID, String projectID,
                                                String sectionName) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(DiaryMetaDataDb.TABLE_NAME,
                    new String[]{DiaryMetaDataDb.COLUMN_ID, DiaryMetaDataDb.COLUMN_SITE_ID, DiaryMetaDataDb.COLUMN_USER_ID, DiaryMetaDataDb.COLUMN_PROJECT_ID, DiaryMetaDataDb.COLUMN_USER,
                            DiaryMetaDataDb.COLUMN_SECTION_NAME, DiaryMetaDataDb.COLUMN_ITEM_DATA, DiaryMetaDataDb.COLUMN_TIMESTAMP},
                    DiaryMetaDataDb.COLUMN_SITE_ID + "=?"
                            + " AND " +
                          /*  DiaryMetaDataDb.COLUMN_PROJECT_ID + "=?"
                            + " AND " +*/
                            DiaryMetaDataDb.COLUMN_SECTION_NAME + "=?",
                    new String[]{String.valueOf(
                            siteID), /*String.valueOf(projectID),*/ String.valueOf(sectionName)},
                    null, null, null, null);

            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                DiaryMetaDataDb diary = new DiaryMetaDataDb();
                do {
                    diary.setId(cursor.getInt(cursor.getColumnIndex(DiaryMetaDataDb.COLUMN_ID)));
                    diary.setSiteID(cursor.getString(
                            cursor.getColumnIndex(DiaryMetaDataDb.COLUMN_SITE_ID)));
                    diary.setUserID(cursor.getString(
                            cursor.getColumnIndex(DiaryMetaDataDb.COLUMN_USER_ID)));
                    diary.setProjectID(cursor.getString(
                            cursor.getColumnIndex(DiaryMetaDataDb.COLUMN_PROJECT_ID)));
                    diary.setUsername(
                            cursor.getString(cursor.getColumnIndex(DiaryMetaDataDb.COLUMN_USER)));
                    diary.setSectionName(cursor.getString(
                            cursor.getColumnIndex(DiaryMetaDataDb.COLUMN_SECTION_NAME)));
                    diary.setMetaData(cursor.getString(
                            cursor.getColumnIndex(DiaryMetaDataDb.COLUMN_ITEM_DATA)));
                    diary.setTimestamp(cursor.getString(
                            cursor.getColumnIndex(DiaryMetaDataDb.COLUMN_TIMESTAMP)));
                } while (cursor.moveToNext());

                cursor.close();
                return diary;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    /***********************************************************************************************************
     *                                              Projects in Local DB
     * ********************************************************************************************************/

    public long insertProjects(String siteID, String userID, String projects) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserProjects.COLUMN_SITE_ID, siteID);
        values.put(UserProjects.COLUMN_USER_ID, userID);
        values.put(UserProjects.COLUMN_PROJECTS, projects);
        long id = db.insert(UserProjects.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public UserProjects getUserProjects(String siteID, String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserProjects.TABLE_NAME,
                new String[]{UserProjects.COLUMN_ID, UserProjects.COLUMN_SITE_ID, UserProjects.COLUMN_USER_ID, UserProjects.COLUMN_PROJECTS, UserProjects.COLUMN_TIMESTAMP},
                UserProjects.COLUMN_USER_ID + "=?"
                        + " AND " +
                        UserProjects.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userID), String.valueOf(siteID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            UserProjects projects = new UserProjects(
                    cursor.getInt(cursor.getColumnIndex(UserProjects.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(UserProjects.COLUMN_SITE_ID)),
                    cursor.getString(cursor.getColumnIndex(UserProjects.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(UserProjects.COLUMN_PROJECTS)),
                    cursor.getString(cursor.getColumnIndex(UserProjects.COLUMN_TIMESTAMP))
            );
            // close the db connection
            cursor.close();
            return projects;
        }

        return null;
    }

    public List<ProjectDetails> getUserProjectList(String siteID, String userID) {
        List<ProjectDetails> projectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserProjects.TABLE_NAME,
                new String[]{UserProjects.COLUMN_ID, UserProjects.COLUMN_SITE_ID, UserProjects.COLUMN_USER_ID, UserProjects.COLUMN_PROJECTS, UserProjects.COLUMN_TIMESTAMP},
                UserProjects.COLUMN_USER_ID + "=?"
                        + " AND " +
                        UserProjects.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userID), String.valueOf(siteID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String projectString = cursor.getString(cursor.getColumnIndex(UserProjects.COLUMN_PROJECTS));
            if (null != projectString && !projectString.isEmpty()) {
                projectList.addAll(new Gson().fromJson(projectString,
                        new TypeToken<List<ProjectDetails>>() {
                        }.getType()));
            }
            cursor.close();
        }

        return projectList;
    }

    public long updateProjects(String siteID, String userID, String projects) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserProjects.COLUMN_PROJECTS, projects);
        long id = db.update(UserProjects.TABLE_NAME, values,
                UserProjects.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        UserProjects.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)});
        closeDataBase(db);
        return id;
    }

    /***********************************************************************************************************
     *                                              Login
     * *********************************************************************************************************/

    public long insertCredentials(boolean isPin, String user_name, String password, String userPin,
                                  String siteID, String userID, String userName, String email,
                                  String profileImage, String pushToken, Integer isSigned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!isPin) {
            values.put(Login.COLUMN_USERNAME, user_name);
            values.put(Login.COLUMN_PASSWORD, password);
        }
        values.put(Login.COLUMN_PIN, userPin);
        values.put(Login.COLUMN_SITE_ID, siteID);
        values.put(Login.COLUMN_USER_ID, userID);
        values.put(Login.COLUMN_USER_NAME, userName);
        values.put(Login.COLUMN_USER_EMAIL, email);
        values.put(Login.COLUMN_PROFILE_PIC, profileImage);
        values.put(Login.COLUMN_FIREBASE_TOKEN, pushToken);
        values.put(Login.COLUMN_IS_LOGIN, isSigned);
        long id = db.insert(Login.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updateCredentials(boolean isPin, String user_name, String password, String userPin,
                                  String siteID, String userID, String userName, String email,
                                  String profileImage, String pushToken, Integer isSigned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!isPin) {
            values.put(Login.COLUMN_USERNAME, user_name);
            values.put(Login.COLUMN_PASSWORD, password);
        }
        values.put(Login.COLUMN_PIN, userPin);
        values.put(Login.COLUMN_USER_NAME, userName);
        values.put(Login.COLUMN_USER_EMAIL, email);
        values.put(Login.COLUMN_PROFILE_PIC, profileImage);
        values.put(Login.COLUMN_FIREBASE_TOKEN, pushToken);
        values.put(Login.COLUMN_IS_LOGIN, isSigned);
        long id = db.update(Login.TABLE_NAME, values,
                Login.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        Login.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)});
        closeDataBase(db);
        return id;
    }

    public long updatePin(String userPin, String siteID, String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Login.COLUMN_PIN, userPin);
        long id = db.update(Login.TABLE_NAME, values,
                Login.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        Login.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)});
        closeDataBase(db);
        return id;
    }

    public long updatePassword(String password, String siteID, String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Login.COLUMN_PASSWORD, password);
        long id = db.update(Login.TABLE_NAME, values,
                Login.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        Login.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)});
        closeDataBase(db);
        return id;
    }

    public Login getLoginCredential(String userName, String userPassword, String siteID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Login.TABLE_NAME,
                new String[]{Login.COLUMN_ID, Login.COLUMN_USER_ID, Login.COLUMN_USER_NAME, Login.COLUMN_USER_EMAIL, Login.COLUMN_PROFILE_PIC},
                Login.COLUMN_USERNAME + "=?"
                        + " AND " +
                        Login.COLUMN_PASSWORD + "=?"
                        + " AND " +
                        Login.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userName), String.valueOf(userPassword), String.valueOf(
                        siteID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Login credentials = new Login(
                    cursor.getInt(cursor.getColumnIndex(Login.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_PROFILE_PIC))
            );
            cursor.close();
            return credentials;
        }

        return null;
    }

    public Login getLoginCredential(String userID, String siteID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Login.TABLE_NAME,
                new String[]{Login.COLUMN_ID, Login.COLUMN_USER_ID, Login.COLUMN_USER_NAME},
                Login.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Login.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userID), String.valueOf(siteID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            Login credentials = new Login(
                    cursor.getInt(cursor.getColumnIndex(Login.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_USER_ID))
            );
            cursor.close();
            return credentials;
        }

        return null;
    }

    public boolean getLoginPin(String userID, String siteID) {
        boolean isPin = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Login.TABLE_NAME,
                new String[]{Login.COLUMN_PIN},
                Login.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Login.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userID), String.valueOf(siteID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String pinValue = cursor.getString(cursor.getColumnIndex(Login.COLUMN_PIN));

            if (pinValue != null && !pinValue.isEmpty()) {
                isPin = true;
            }
            // close the db connection
            cursor.close();
        }

        return isPin;
    }

    public Login checkLoginPin(String userID, String siteID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Login.TABLE_NAME,
                new String[]{Login.COLUMN_ID, Login.COLUMN_USER_ID},
                Login.COLUMN_USER_ID + "=?" + " AND " + Login.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userID), String.valueOf(siteID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            Login pinData = new Login(
                    cursor.getInt(cursor.getColumnIndex(Login.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_USER_ID)));
            cursor.close();
            return pinData;
        }

        return null;
    }

    public Login getLoginWithPin(String userPin, String siteID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Login.TABLE_NAME,
                new String[]{Login.COLUMN_ID, Login.COLUMN_USER_ID, Login.COLUMN_USER_NAME, Login.COLUMN_USER_EMAIL, Login.COLUMN_PROFILE_PIC},
                Login.COLUMN_PIN + "=?"
                        + " AND " +
                        Login.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userPin), String.valueOf(siteID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            // prepare Crendential object
            Login pinData = new Login(
                    cursor.getInt(cursor.getColumnIndex(Login.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_USER_NAME)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_PROFILE_PIC))
            );
            // close the db connection
            cursor.close();
            return pinData;
        }

        return null;
    }

    public long updateLogOutStatus(String siteID, String userID, String firebaseToken, Integer isSigned) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Login.COLUMN_FIREBASE_TOKEN, firebaseToken);
        values.put(Login.COLUMN_IS_LOGIN, isSigned);
        long id = db.update(Login.TABLE_NAME, values,
                Login.COLUMN_SITE_ID + " = ?" + " AND " + Login.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID)});
        closeDataBase(db);
        return id;
    }

    public Login syncLogOutStatus(String siteID, String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Login.TABLE_NAME,
                new String[]{Login.COLUMN_FIREBASE_TOKEN, Login.COLUMN_IS_LOGIN},
                Login.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Login.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userID), String.valueOf(siteID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            Login login = new Login(
                    cursor.getString(cursor.getColumnIndex(Login.COLUMN_FIREBASE_TOKEN)),
                    cursor.getInt(cursor.getColumnIndex(Login.COLUMN_IS_LOGIN))

            );
            cursor.close();
            return login;
        }

        return null;
    }

    public boolean noLoginStatus() {
        List<Integer> loginEntries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Login.TABLE_NAME,
                new String[]{Login.COLUMN_ID},
                Login.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Login.COLUMN_SITE_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getUserId(), BuilderStormApplication.mPrefs.getSiteId()}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                loginEntries.add(cursor.getInt(cursor.getColumnIndex(Login.COLUMN_ID)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return loginEntries.isEmpty();
    }


    /***********************************************************************************************************
     *                                              Project SignIn operations
     * ********************************************************************************************************/

    public long insertSignedProject(
            String siteID, String userID, String projectID,
            String userStartTime, String userEndTime,
            int isSigned, String projectData,
            String startLatitude, String startLongitude,
            String endLatitude, String endLongitude
    ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SignedProject.COLUMN_SITE_ID, siteID);
        values.put(SignedProject.COLUMN_USER_ID, userID);
        values.put(SignedProject.COLUMN_PROJECT_ID, projectID);
        values.put(SignedProject.COLUMN_TIME_SIGN_IN, userStartTime);
        values.put(SignedProject.COLUMN_TIME_SIGN_OUT, userEndTime);
        values.put(SignedProject.COLUMN_PROJECT_STATUS, isSigned);
        values.put(SignedProject.COLUMN_ITEM_DATA, projectData);
        values.put(SignedProject.COLUMN_START_LATITUDE, startLatitude);
        values.put(SignedProject.COLUMN_START_LONGITUDE, startLongitude);
        values.put(SignedProject.COLUMN_END_LATITUDE, endLatitude);
        values.put(SignedProject.COLUMN_END_LONGITUDE, endLongitude);
        /*Extra Params */
        long id = db.insert(SignedProject.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }


    public void deleteSignInformation(String siteID, String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(SignedProject.TABLE_NAME,
                    SignedProject.COLUMN_SITE_ID + "=?"
                            + " AND " +
                            SignedProject.COLUMN_USER_ID + "=?",
                    new String[]{String.valueOf(siteID), String.valueOf(userID)});
            closeDataBase(db);
        } catch (Exception e) {
            e.printStackTrace();
            closeDataBase(db);
        }
    }

    public void deleteSignInformation(String siteID, String userID, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SignedProject.TABLE_NAME,
                SignedProject.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        SignedProject.COLUMN_USER_ID + "=?"
                        + " AND " +
                        SignedProject.COLUMN_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(id)});
        closeDataBase(db);
    }

    public SignedProject getSignedProject(String siteID, String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SignedProject.TABLE_NAME,
                new String[]{SignedProject.COLUMN_ID, SignedProject.COLUMN_SITE_ID,
                        SignedProject.COLUMN_USER_ID, SignedProject.COLUMN_PROJECT_ID,
                        SignedProject.COLUMN_TIMESTAMP, SignedProject.COLUMN_TIME_SIGN_IN,
                        SignedProject.COLUMN_TIME_SIGN_OUT, SignedProject.COLUMN_PROJECT_STATUS,
                        SignedProject.COLUMN_ITEM_DATA, SignedProject.COLUMN_START_LATITUDE,
                        SignedProject.COLUMN_START_LONGITUDE, SignedProject.COLUMN_END_LATITUDE,
                        SignedProject.COLUMN_END_LONGITUDE
                },
                SignedProject.COLUMN_USER_ID + "=?"
                        + " AND " +
                        SignedProject.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        SignedProject.COLUMN_PROJECT_STATUS + "=?",
                new String[]{String.valueOf(userID), String.valueOf(siteID), "0"},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            SignedProject projects = new SignedProject(
                    cursor.getInt(cursor.getColumnIndex(SignedProject.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_SITE_ID)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_PROJECT_ID)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_TIME_SIGN_IN)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_TIME_SIGN_OUT)),
                    cursor.getInt(cursor.getColumnIndex(SignedProject.COLUMN_PROJECT_STATUS)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_ITEM_DATA)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_START_LATITUDE)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_START_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_END_LATITUDE)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_END_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_TIMESTAMP)));
            // close the db connection
            cursor.close();
            return projects;
        }
        return null;
    }

    public List<SignedProject> getSignedProjects(String siteID, String userID) {
        List<SignedProject> projectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SignedProject.TABLE_NAME,
                new String[]{SignedProject.COLUMN_ID,
                        SignedProject.COLUMN_SITE_ID, SignedProject.COLUMN_USER_ID,
                        SignedProject.COLUMN_PROJECT_ID, SignedProject.COLUMN_TIMESTAMP,
                        SignedProject.COLUMN_TIME_SIGN_IN, SignedProject.COLUMN_TIME_SIGN_OUT,
                        SignedProject.COLUMN_PROJECT_STATUS, SignedProject.COLUMN_ITEM_DATA,
                        SignedProject.COLUMN_START_LATITUDE, SignedProject.COLUMN_START_LONGITUDE,
                        SignedProject.COLUMN_END_LATITUDE, SignedProject.COLUMN_END_LONGITUDE,
                        SignedProject.COLUMN_STANDARD_BREAKS, SignedProject.COLUMN_SELECTED_BREAKS,
                        SignedProject.COLUMN_OVERTIMES
                },
                SignedProject.COLUMN_USER_ID + "=?"
                        + " AND " +
                        SignedProject.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(userID), String.valueOf(siteID)}, null, null, null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                projectList.add(new SignedProject(
                        cursor.getInt(cursor.getColumnIndex(SignedProject.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_SITE_ID)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_USER_ID)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_PROJECT_ID)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_TIME_SIGN_IN)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_TIME_SIGN_OUT)),
                        cursor.getInt(cursor.getColumnIndex(SignedProject.COLUMN_PROJECT_STATUS)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_ITEM_DATA)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_START_LATITUDE)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_START_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_END_LATITUDE)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_END_LONGITUDE)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_OVERTIMES)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_STANDARD_BREAKS)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_SELECTED_BREAKS)),
                        cursor.getString(cursor.getColumnIndex(SignedProject.COLUMN_TIMESTAMP))
                ));
                // close the db connection
            } while (cursor.moveToNext());
            cursor.close();
            return projectList;
        }
        return projectList;
    }

    public long updateSignedProject(String siteID, String userID, String projectId, int status,
                                    String endTime, String breaks, String overtimes, String selectedIds,
                                    String endLatitude, String endLongitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SignedProject.COLUMN_STANDARD_BREAKS, breaks);
        values.put(SignedProject.COLUMN_OVERTIMES, overtimes);
        values.put(SignedProject.COLUMN_SELECTED_BREAKS, selectedIds);
        values.put(SignedProject.COLUMN_TIME_SIGN_OUT, endTime);
        values.put(SignedProject.COLUMN_PROJECT_STATUS, status);
        values.put(SignedProject.COLUMN_END_LATITUDE, endLatitude);
        values.put(SignedProject.COLUMN_END_LONGITUDE, endLongitude);
        long id = db.update(SignedProject.TABLE_NAME, values,
                SignedProject.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        SignedProject.COLUMN_USER_ID + "=?"
                        + " AND " +
                        SignedProject.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectId)});
        closeDataBase(db);
        return id;
    }

    public boolean anySignRecord() {
        List<Integer> projectList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(SignedProject.TABLE_NAME,
                new String[]{SignedProject.COLUMN_ID, SignedProject.COLUMN_PROJECT_STATUS,},
                SignedProject.COLUMN_USER_ID + "=?"
                        + " AND " +
                        SignedProject.COLUMN_SITE_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getUserId(), BuilderStormApplication.mPrefs.getSiteId()}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                projectList.add(cursor.getInt(cursor.getColumnIndex(SignedProject.COLUMN_ID)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return projectList.isEmpty();
    }


    /***********************************************************************************************************
     *                                              Drawing Table data operations
     * ********************************************************************************************************/

    public long insertDrawing(String siteID, String userID, String projectID, String rowID,
                              int sync, String drawingItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Drawings.COLUMN_SITE_ID, siteID);
        values.put(Drawings.COLUMN_USER_ID, userID);
        values.put(Drawings.COLUMN_PROJECT_ID, projectID);
        values.put(Drawings.COLUMN_ROW_ID, rowID);
        values.put(Drawings.COLUMN_SYNC, sync);
        values.put(Drawings.COLUMN_ITEM_DATA, drawingItem);
        long id = db.insert(Drawings.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updateDrawing(String siteID, String userID, String projectID, String rowID,
                              int sync, String drawingItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Drawings.COLUMN_SYNC, sync);
        values.put(Drawings.COLUMN_ITEM_DATA, drawingItem);
        long id = db.update(Drawings.TABLE_NAME, values,
                Drawings.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_ROW_ID
                        + " = ?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(projectID), String.valueOf(rowID)});
        closeDataBase(db);
        return id;
    }

    public List<Drawings> getDrawings(String siteID, String userID, String projectID) {
        List<Drawings> localDrawing = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Drawings.TABLE_NAME,
                new String[]{Drawings.COLUMN_ID, Drawings.COLUMN_SITE_ID, Drawings.COLUMN_USER_ID, Drawings.COLUMN_PROJECT_ID, Drawings.COLUMN_ROW_ID, Drawings.COLUMN_SYNC, Drawings.COLUMN_ITEM_DATA, Drawings.COLUMN_TIMESTAMP},
                Drawings.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Drawings drawings = new Drawings();
                drawings.setId(cursor.getInt(cursor.getColumnIndex(Drawings.COLUMN_ID)));
                drawings.setSiteID(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_SITE_ID)));
                drawings.setUserID(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_USER_ID)));
                drawings.setProjectID(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_PROJECT_ID)));
                drawings.setRowID(cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_ROW_ID)));
                drawings.setSync(cursor.getInt(cursor.getColumnIndex(Drawings.COLUMN_SYNC)) == 1);
                drawings.setDrawingItem(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_ITEM_DATA)));
                drawings.setTimestamp(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_TIMESTAMP)));
                localDrawing.add(0, drawings);

            } while (cursor.moveToNext());

            cursor.close();
            return localDrawing;
        }
        return localDrawing;

    }

    // Get drawing which are added at offline mode
    public List<Drawings> getDrawingsOffline(String siteID) {
        List<Drawings> offlineDrawing = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Drawings.TABLE_NAME,
                new String[]{Drawings.COLUMN_ID, Drawings.COLUMN_SITE_ID, Drawings.COLUMN_USER_ID, Drawings.COLUMN_PROJECT_ID, Drawings.COLUMN_ROW_ID, Drawings.COLUMN_SYNC, Drawings.COLUMN_ITEM_DATA, Drawings.COLUMN_TIMESTAMP},
                Drawings.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_SYNC + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(0)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Drawings drawings = new Drawings();
                drawings.setId(cursor.getInt(cursor.getColumnIndex(Drawings.COLUMN_ID)));
                drawings.setSiteID(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_SITE_ID)));
                drawings.setUserID(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_USER_ID)));
                drawings.setProjectID(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_PROJECT_ID)));
                drawings.setRowID(cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_ROW_ID)));
                drawings.setSync(cursor.getInt(cursor.getColumnIndex(Drawings.COLUMN_SYNC)) == 1);
                drawings.setDrawingItem(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_ITEM_DATA)));
                drawings.setTimestamp(
                        cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_TIMESTAMP)));
                offlineDrawing.add(drawings);

            } while (cursor.moveToNext());

            cursor.close();
            return offlineDrawing;
        }
        return offlineDrawing;

    }

    public boolean noDrawingsToSync() {
        List<Integer> offlineDrawing = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Drawings.TABLE_NAME, new String[]{Drawings.COLUMN_ID},
                Drawings.COLUMN_SITE_ID + "=?" + " AND " + Drawings.COLUMN_SYNC + "=?",
                new String[]{String.valueOf(
                        BuilderStormApplication.mPrefs.getSiteId()), String.valueOf(0)}, null, null,
                null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                offlineDrawing.add(cursor.getInt(cursor.getColumnIndex(Drawings.COLUMN_ID)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return offlineDrawing.isEmpty();

    }

    public List<String> getSyncedDrawings(String siteID, String userID, String projectID) {
        List<String> offlineDrawing = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Drawings.TABLE_NAME,
                new String[]{Drawings.COLUMN_ROW_ID},
                Drawings.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                offlineDrawing.add(cursor.getString(cursor.getColumnIndex(Drawings.COLUMN_ROW_ID)));
            } while (cursor.moveToNext());

            cursor.close();
            return offlineDrawing;
        }
        return offlineDrawing;
    }

    public void deleteDrawing(String siteID, int isSync) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Drawings.TABLE_NAME,
                Drawings.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        Drawings.COLUMN_SYNC + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(isSync)});
        closeDataBase(db);
    }


    /***********************************************************************************************************
     *                                              Project Gallery operations
     * ********************************************************************************************************/

    public long insertProjectGallery(String siteID, String userID, String projectID,
                                     String galleryID, String title, String latitude,
                                     String longitude, String metaData, String images) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Gallery.COLUMN_SITE_ID, siteID);
        values.put(Gallery.COLUMN_USER_ID, userID);
        values.put(Gallery.COLUMN_PROJECT_ID, projectID);
        values.put(Gallery.COLUMN_GALLERY_ID, galleryID);
        values.put(Gallery.COLUMN_TITLE, title);
        values.put(Gallery.COLUMN_LATITUDE, latitude);
        values.put(Gallery.COLUMN_LONGITUDE, longitude);
        values.put(Gallery.COLUMN_IMAGES, images);
        values.put(Gallery.COLUMN_METADATA, metaData);
        long id = db.insert(Gallery.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public List<Gallery> getProjectGallery(String siteID, String userID, String projectID) {
        List<Gallery> localGallery = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Gallery.TABLE_NAME,
                new String[]{Gallery.COLUMN_ID, Gallery.COLUMN_SITE_ID, Gallery.COLUMN_USER_ID,
                        Gallery.COLUMN_PROJECT_ID, Gallery.COLUMN_GALLERY_ID, Gallery.COLUMN_TITLE,
                        Gallery.COLUMN_LATITUDE, Gallery.COLUMN_LONGITUDE, Gallery.COLUMN_IMAGES,
                        Gallery.COLUMN_TIMESTAMP, Gallery.COLUMN_METADATA},
                Gallery.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        Gallery.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Gallery.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Gallery gallery = new Gallery();
                gallery.setId(cursor.getInt(cursor.getColumnIndex(Gallery.COLUMN_ID)));
                gallery.setSiteID(cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_SITE_ID)));
                gallery.setUserID(cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_USER_ID)));
                gallery.setProjectID(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_PROJECT_ID)));
                gallery.setGalleryID(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_GALLERY_ID)));
                gallery.setGalleryTitle(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_TITLE)));
                gallery.setLatitude(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_LATITUDE)));
                gallery.setLongitude(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_LONGITUDE)));
                gallery.setImages(cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_IMAGES)));
                gallery.setMetaData(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_METADATA)));
                gallery.setTimestamp(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_TIMESTAMP)));
                localGallery.add(0, gallery);

            } while (cursor.moveToNext());

            cursor.close();
            return localGallery;
        }
        return localGallery;

    }

    public List<Gallery> getProjectGalleryOffline(String siteID) {
        List<Gallery> localGallery = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Gallery.TABLE_NAME,
                new String[]{Gallery.COLUMN_ID, Gallery.COLUMN_SITE_ID, Gallery.COLUMN_USER_ID,
                        Gallery.COLUMN_PROJECT_ID, Gallery.COLUMN_GALLERY_ID, Gallery.COLUMN_TITLE,
                        Gallery.COLUMN_LATITUDE, Gallery.COLUMN_LONGITUDE, Gallery.COLUMN_IMAGES,
                        Gallery.COLUMN_TIMESTAMP, Gallery.COLUMN_METADATA},
                Gallery.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(siteID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Gallery gallery = new Gallery();
                gallery.setId(cursor.getInt(cursor.getColumnIndex(Gallery.COLUMN_ID)));
                gallery.setSiteID(cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_SITE_ID)));
                gallery.setUserID(cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_USER_ID)));
                gallery.setProjectID(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_PROJECT_ID)));
                gallery.setGalleryID(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_GALLERY_ID)));
                gallery.setGalleryTitle(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_TITLE)));
                gallery.setLatitude(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_LATITUDE)));
                gallery.setLongitude(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_LONGITUDE)));
                gallery.setImages(cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_IMAGES)));
                gallery.setMetaData(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_METADATA)));
                gallery.setTimestamp(
                        cursor.getString(cursor.getColumnIndex(Gallery.COLUMN_TIMESTAMP)));
                localGallery.add(0, gallery);
            } while (cursor.moveToNext());

            cursor.close();
            return localGallery;
        }
        return localGallery;

    }

    public Boolean noGalleryToSync() {
        List<Integer> localGallery = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Gallery.TABLE_NAME,
                new String[]{Gallery.COLUMN_ID}, Gallery.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(BuilderStormApplication.mPrefs.getSiteId())},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                localGallery.add(cursor.getColumnIndex(Gallery.COLUMN_ID));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return localGallery.isEmpty();

    }

    public long updateProjectGallery(String title, String rowID, String images) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Gallery.COLUMN_TITLE, title);
        values.put(Gallery.COLUMN_IMAGES, images);
        long id = db.update(Gallery.TABLE_NAME, values,
                Gallery.COLUMN_ID
                        + " = ?",
                new String[]{String.valueOf(rowID)});
        closeDataBase(db);
        return id;
    }

    public void deleteGallery(String rowID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Gallery.TABLE_NAME,
                Gallery.COLUMN_ID + "=?",
                new String[]{String.valueOf(rowID)});
        closeDataBase(db);
    }


    /***********************************************************************************************************
     *                                              Project DiaryData operations
     * ********************************************************************************************************/

    public long insertDiary(String siteID, String userID, String projectID, String username,
                            String title, String description, String time, String date,
                            String metaData, String metaDataView, String diaryData, String laborData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Diary.COLUMN_SITE_ID, siteID);
        values.put(Diary.COLUMN_USER_ID, userID);
        values.put(Diary.COLUMN_PROJECT_ID, projectID);
        values.put(Diary.COLUMN_USER, username);
        values.put(Diary.COLUMN_TITLE, title);
        values.put(Diary.COLUMN_DESCRIPTION, description);
        values.put(Diary.COLUMN_TIME, time);
        values.put(Diary.COLUMN_DATE, date);
        values.put(Diary.COLUMN_META_DATA_VIEW, metaDataView);
        values.put(Diary.COLUMN_META_DATA, metaData); /* meta data added*/
        values.put(Diary.COLUMN_ITEM_DATA, diaryData);
        values.put(Diary.COLUMN_LABOR_DATA, laborData);
        long id = db.insert(Diary.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public List<Diary> getProjectDailyDiary(String siteID, String userID, String projectID,
                                            String date) {
        List<Diary> localDiary = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Diary.TABLE_NAME,
                new String[]{Diary.COLUMN_ID, Diary.COLUMN_SITE_ID, Diary.COLUMN_USER_ID, Diary.COLUMN_PROJECT_ID, Diary.COLUMN_USER,
                        Diary.COLUMN_TITLE, Diary.COLUMN_DESCRIPTION, Diary.COLUMN_TIME, Diary.COLUMN_DATE, Diary.COLUMN_ITEM_DATA,
                        Diary.COLUMN_META_DATA, Diary.COLUMN_TIMESTAMP, Diary.COLUMN_META_DATA_VIEW, Diary.COLUMN_LABOR_DATA},
                Diary.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        Diary.COLUMN_USER_ID + "=?"
                        + " AND " +
                        Diary.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        Diary.COLUMN_DATE + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID), String.valueOf(date)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Diary diary = new Diary();
                diary.setId(cursor.getInt(cursor.getColumnIndex(Diary.COLUMN_ID)));
                diary.setSiteID(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_SITE_ID)));
                diary.setUserID(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_USER_ID)));
                diary.setProjectID(
                        cursor.getString(cursor.getColumnIndex(Diary.COLUMN_PROJECT_ID)));
                diary.setUsername(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_USER)));
                diary.setTitle(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_TITLE)));
                diary.setDescription(
                        cursor.getString(cursor.getColumnIndex(Diary.COLUMN_DESCRIPTION)));
                diary.setTime(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_TIME)));
                diary.setDate(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_DATE)));
                diary.setMetaDataView(cursor.getString(
                        cursor.getColumnIndex(Diary.COLUMN_META_DATA_VIEW)));/* column metadata */
                diary.setMetaData(cursor.getString(
                        cursor.getColumnIndex(Diary.COLUMN_META_DATA)));/* column metadata */
                diary.setDiaryData(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_ITEM_DATA)));
                diary.setTimestamp(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_TIMESTAMP)));
                diary.setSiteLabor(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_LABOR_DATA)));
                localDiary.add(0, diary);
            } while (cursor.moveToNext());
            cursor.close();
            return localDiary;
        }
        return localDiary;
    }

    public List<Diary> getProjectDiaryOffline(String siteID) {
        List<Diary> localDiary = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Diary.TABLE_NAME,
                new String[]{Diary.COLUMN_ID, Diary.COLUMN_SITE_ID, Diary.COLUMN_USER_ID, Diary.COLUMN_PROJECT_ID, Diary.COLUMN_USER,
                        Diary.COLUMN_TITLE, Diary.COLUMN_DESCRIPTION, Diary.COLUMN_TIME, Diary.COLUMN_DATE, Diary.COLUMN_ITEM_DATA,
                        Diary.COLUMN_META_DATA, Diary.COLUMN_TIMESTAMP, Diary.COLUMN_META_DATA_VIEW, Diary.COLUMN_LABOR_DATA},
                Diary.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(siteID)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    Diary diary = new Diary();
                    diary.setId(cursor.getInt(cursor.getColumnIndex(Diary.COLUMN_ID)));
                    diary.setSiteID(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_SITE_ID)));
                    diary.setUserID(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_USER_ID)));
                    diary.setProjectID(
                            cursor.getString(cursor.getColumnIndex(Diary.COLUMN_PROJECT_ID)));
                    diary.setUsername(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_USER)));
                    diary.setTitle(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_TITLE)));
                    diary.setDescription(
                            cursor.getString(cursor.getColumnIndex(Diary.COLUMN_DESCRIPTION)));
                    diary.setTime(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_TIME)));
                    diary.setDate(cursor.getString(cursor.getColumnIndex(Diary.COLUMN_DATE)));
                    diary.setMetaData(cursor.getString(
                            cursor.getColumnIndex(Diary.COLUMN_META_DATA)));/* column metadata */
                    diary.setMetaDataView(cursor.getString(cursor.getColumnIndex(
                            Diary.COLUMN_META_DATA_VIEW)));/* column metadata */
                    diary.setDiaryData(
                            cursor.getString(cursor.getColumnIndex(Diary.COLUMN_ITEM_DATA)));
                    diary.setSiteLabor(
                            cursor.getString(cursor.getColumnIndex(Diary.COLUMN_LABOR_DATA)));
                    diary.setTimestamp(
                            cursor.getString(cursor.getColumnIndex(Diary.COLUMN_TIMESTAMP)));
                    localDiary.add(0, diary);
                } while (cursor.moveToNext());
                cursor.close();
                return localDiary;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "getProject Diaries: " + e.getLocalizedMessage());
            }
        }
        return localDiary;
    }

    public boolean noDiaryToSync() {
        List<Integer> localDiary = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Diary.TABLE_NAME, new String[]{Diary.COLUMN_ID},
                Diary.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(BuilderStormApplication.mPrefs.getSiteId())},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    localDiary.add(cursor.getColumnIndex(Diary.COLUMN_ID));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return localDiary.isEmpty();
    }

    public void deleteDiary(Integer rowID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Diary.TABLE_NAME, Diary.COLUMN_ID + "=?", new String[]{String.valueOf(rowID)});
        closeDataBase(db);
    }

    public long updateProjectDiary(int rowID, String title, String description, String time,
                                   String diaryData, String metaData, String metaDataView) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Diary.COLUMN_TITLE, title);
        values.put(Diary.COLUMN_DESCRIPTION, description);
        values.put(Diary.COLUMN_TIME, time);
        values.put(Diary.COLUMN_META_DATA_VIEW, metaDataView);
        values.put(Diary.COLUMN_META_DATA, metaData);
        values.put(Diary.COLUMN_ITEM_DATA, diaryData);
        long id = db.update(Diary.TABLE_NAME, values, Diary.COLUMN_ID + " = ?",
                new String[]{String.valueOf(rowID)});
        closeDataBase(db);
        return id;
    }


    public long updateProjectDiary(int rowID, String title, String description, String time,
                                   String diaryData, String laborData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Diary.COLUMN_TITLE, title);
        values.put(Diary.COLUMN_DESCRIPTION, description);
        values.put(Diary.COLUMN_TIME, time);
        values.put(Diary.COLUMN_ITEM_DATA, diaryData);
        values.put(Diary.COLUMN_LABOR_DATA, laborData);
        long id = db.update(Diary.TABLE_NAME, values, Diary.COLUMN_ID + " = ?",
                new String[]{String.valueOf(rowID)});
        closeDataBase(db);
        return id;
    }


    /***********************************************************************************************************
     *                                              RFIS operations
     * ********************************************************************************************************/

    public long insertRFI(String siteID, String userID, String projectID, String rowId,
                          String username,
                          String toUsers, String ccUsers, String title, String description,
                          String time,
                          String date, String rfiData, String metaData, String isAnswered,
                          String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbRFI.COLUMN_SITE_ID, siteID);
        values.put(DbRFI.COLUMN_USER_ID, userID);
        values.put(DbRFI.COLUMN_ROW_ID, rowId);
        values.put(DbRFI.COLUMN_PROJECT_ID, projectID);
        values.put(DbRFI.COLUMN_USER, username);
        values.put(DbRFI.COLUMN_TO_USERS, toUsers);
        values.put(DbRFI.COLUMN_CC_USERS, ccUsers);
        values.put(DbRFI.COLUMN_TITLE, title);
        values.put(DbRFI.COLUMN_DESCRIPTION, description);
        values.put(DbRFI.COLUMN_TIME, time);
        values.put(DbRFI.COLUMN_DATE, date);
        values.put(DbRFI.COLUMN_METADATA, metaData);
        values.put(DbRFI.COLUMN_ITEM_DATA, rfiData);
        values.put(DbRFI.COLUMN_IS_ANSWERED, isAnswered);
        values.put(DbRFI.COLUMN_ANSWERED_ADDED, "0");
        values.put(DbRFI.COLUMN_STATUS, status);
        long id = db.insert(DbRFI.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updateSyncRFI(String siteID, String userID, String projectID, String rowId, String username,
                              String toUsers, String ccUsers, String title, String description, String time,
                              String date, String rfiData, String metaData, String isAnswered, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbRFI.COLUMN_USER, username);
        values.put(DbRFI.COLUMN_TO_USERS, toUsers);
        values.put(DbRFI.COLUMN_CC_USERS, ccUsers);
        values.put(DbRFI.COLUMN_TITLE, title);
        values.put(DbRFI.COLUMN_DESCRIPTION, description);
        values.put(DbRFI.COLUMN_TIME, time);
        values.put(DbRFI.COLUMN_DATE, date);
        values.put(DbRFI.COLUMN_METADATA, metaData);
        values.put(DbRFI.COLUMN_ITEM_DATA, rfiData);
        values.put(DbRFI.COLUMN_IS_ANSWERED, isAnswered);
        values.put(DbRFI.COLUMN_ANSWERED_ADDED, "0");
        values.put(DbRFI.COLUMN_STATUS, status);

        long id = db.update(DbRFI.TABLE_NAME, values,
                DbRFI.COLUMN_SITE_ID + "=?" + " AND " + DbRFI.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbRFI.COLUMN_PROJECT_ID + "=?" + " AND " + DbRFI.COLUMN_ROW_ID + "=?",
                new String[]{siteID, userID, projectID, rowId});
        closeDataBase(db);
        return id;
    }

    public long updateRFI(String rowId, String rfiData, String isAnswered, String answerAdded) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbRFI.COLUMN_ITEM_DATA, rfiData);
        values.put(DbRFI.COLUMN_IS_ANSWERED, isAnswered);
        values.put(DbRFI.COLUMN_ANSWERED_ADDED, answerAdded);

        long id = db.update(DbRFI.TABLE_NAME, values,
                DbRFI.COLUMN_ROW_ID + " = ?",
                new String[]{rowId});
        closeDataBase(db);
        return id;
    }

    public List<DbRFI> getProjectRFIs(String siteID, String userID, String projectID) {
        List<DbRFI> localRfi = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbRFI.TABLE_NAME,
                new String[]{DbRFI.COLUMN_ID, DbRFI.COLUMN_ROW_ID, DbRFI.COLUMN_SITE_ID, DbRFI.COLUMN_USER_ID, DbRFI.COLUMN_PROJECT_ID, DbRFI.COLUMN_USER,
                        DbRFI.COLUMN_TO_USERS, DbRFI.COLUMN_CC_USERS, DbRFI.COLUMN_IS_ANSWERED, DbRFI.COLUMN_STATUS,
                        DbRFI.COLUMN_TITLE, DbRFI.COLUMN_DESCRIPTION, DbRFI.COLUMN_TIME, DbRFI.COLUMN_DATE, DbRFI.COLUMN_ITEM_DATA
                        , DbRFI.COLUMN_TIMESTAMP, DbRFI.COLUMN_METADATA},
                DbRFI.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbRFI.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbRFI.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    DbRFI rfi = new DbRFI();
                    rfi.setId(cursor.getInt(cursor.getColumnIndex(DbRFI.COLUMN_ID)));
                    rfi.setRowId(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_ROW_ID)));
                    rfi.setSiteID(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_SITE_ID)));
                    rfi.setUserID(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_USER_ID)));
                    rfi.setProjectID(
                            cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_PROJECT_ID)));
                    rfi.setUsername(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_USER)));
                    rfi.setToUsers(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_TO_USERS)));
                    rfi.setCcUsers(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_CC_USERS)));
                    rfi.setTitle(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_TITLE)));
                    rfi.setDescription(
                            cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_DESCRIPTION)));
                    rfi.setTime(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_TIME)));
                    rfi.setMetaData(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_METADATA)));
                    rfi.setDate(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_DATE)));
                    rfi.setIsAnswered(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_IS_ANSWERED)).equals("1"));
                    rfi.setStatus(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_STATUS)));
                    rfi.setRfiData(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_ITEM_DATA)));
                    rfi.setTimestamp(
                            cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_TIMESTAMP)));
                    localRfi.add(0, rfi);
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return localRfi;
        }
        return localRfi;
    }

    public List<String> getRfiIdList(String siteID, String userID, String projectID) {
        List<String> syncIdList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbRFI.TABLE_NAME,
                new String[]{DbRFI.COLUMN_ROW_ID},
                DbRFI.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbRFI.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbRFI.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    syncIdList.add(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_ROW_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
            return syncIdList;
        }
        return syncIdList;
    }

    public List<DbRFI> getRFIToSync(String siteID) {
        List<DbRFI> localRfi = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbRFI.TABLE_NAME,
                new String[]{DbRFI.COLUMN_ID, DbRFI.COLUMN_ROW_ID, DbRFI.COLUMN_SITE_ID, DbRFI.COLUMN_USER_ID, DbRFI.COLUMN_PROJECT_ID, DbRFI.COLUMN_USER,
                        DbRFI.COLUMN_TO_USERS, DbRFI.COLUMN_CC_USERS,
                        DbRFI.COLUMN_TITLE, DbRFI.COLUMN_DESCRIPTION, DbRFI.COLUMN_TIME, DbRFI.COLUMN_DATE, DbRFI.COLUMN_ITEM_DATA,
                        DbRFI.COLUMN_STATUS, DbRFI.COLUMN_IS_ANSWERED, DbRFI.COLUMN_ANSWERED_ADDED, DbRFI.COLUMN_TIMESTAMP, DbRFI.COLUMN_METADATA},
                DbRFI.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(siteID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                DbRFI rfi = new DbRFI();
                rfi.setId(cursor.getInt(cursor.getColumnIndex(DbRFI.COLUMN_ID)));
                rfi.setRowId(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_ROW_ID)));
                rfi.setSiteID(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_SITE_ID)));
                rfi.setUserID(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_USER_ID)));
                rfi.setProjectID(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_PROJECT_ID)));
                rfi.setUsername(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_USER)));
                rfi.setToUsers(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_TO_USERS)));
                rfi.setCcUsers(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_CC_USERS)));
                rfi.setTitle(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_TITLE)));
                rfi.setDescription(
                        cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_DESCRIPTION)));
                rfi.setTime(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_TIME)));
                rfi.setMetaData(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_METADATA)));
                rfi.setDate(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_DATE)));
                rfi.setIsAnswered(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_IS_ANSWERED)).equals("1"));
                rfi.setAnsweredAdded(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_ANSWERED_ADDED)).equals("1"));
                rfi.setStatus(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_STATUS)));
                rfi.setRfiData(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_ITEM_DATA)));
                rfi.setTimestamp(cursor.getString(cursor.getColumnIndex(DbRFI.COLUMN_TIMESTAMP)));
                localRfi.add(rfi);
            } while (cursor.moveToNext());

            cursor.close();
            return localRfi;
        }
        return localRfi;
    }

    public boolean noRfiToSync() {
        List<Integer> localRfi = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbRFI.TABLE_NAME, new String[]{DbRFI.COLUMN_ID},
                DbRFI.COLUMN_SITE_ID + "=?",
                new String[]{String.valueOf(BuilderStormApplication.mPrefs.getSiteId())},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                localRfi.add(cursor.getColumnIndex(DbRFI.COLUMN_ID));
            } while (cursor.moveToNext());
            cursor.close();

        }
        return localRfi.isEmpty();
    }

    public void deleteRFI(int rowID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbRFI.TABLE_NAME,
                DbRFI.COLUMN_ID + "=?",
                new String[]{String.valueOf(rowID)});
        closeDataBase(db);
    }


    /***********************************************************************************************************
     *                                              SnagList operations
     * ********************************************************************************************************/

    public long insertSnag(String siteID, String userID, String projectID, String rowId,
                           String username, String toUsers, String ccUsers,
                           String location, String packNo, String description, String dueDate,
                           String itemData, String status, String markStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbSnag.COLUMN_SITE_ID, siteID);
        values.put(DbSnag.COLUMN_USER_ID, userID);
        values.put(DbSnag.COLUMN_PROJECT_ID, projectID);
        values.put(DbSnag.COLUMN_ROW_ID, rowId);
        values.put(DbSnag.COLUMN_USER_NAME, username);
        values.put(DbSnag.COLUMN_TO_USERS, toUsers);
        values.put(DbSnag.COLUMN_CC_USERS, ccUsers);
        values.put(DbSnag.COLUMN_LOCATION, location);
        values.put(DbSnag.COLUMN_PACK_NO, packNo);
        values.put(DbSnag.COLUMN_DESCRIPTION, description);
        values.put(DbSnag.COLUMN_DUE_DATE, dueDate);
        values.put(DbSnag.COLUMN_ITEM_DATA, itemData);
        values.put(DbSnag.COLUMN_STATUS, status);
        values.put(DbSnag.COLUMN_MARK_STATUS, markStatus);
        long id = db.insert(DbSnag.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updateSnag(String siteID, String userID, String projectID, String rowId, String itemData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbSnag.COLUMN_ITEM_DATA, itemData);
        long id = db.update(DbSnag.TABLE_NAME, values,
                DbSnag.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbSnag.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbSnag.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        DbSnag.COLUMN_ROW_ID + "=?",
                new String[]{siteID, userID, projectID, rowId});
        closeDataBase(db);
        return id;
    }

    public List<DbSnag> getDbSnagList(String siteID, String userID, String projectID) {
        List<DbSnag> snagList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbSnag.TABLE_NAME,
                new String[]{DbSnag.COLUMN_ID, DbSnag.COLUMN_ROW_ID, DbSnag.COLUMN_SITE_ID,
                        DbSnag.COLUMN_USER_ID, DbSnag.COLUMN_PROJECT_ID, DbSnag.COLUMN_USER_NAME,
                        DbSnag.COLUMN_TO_USERS, DbSnag.COLUMN_CC_USERS, DbSnag.COLUMN_STATUS,
                        DbSnag.COLUMN_DESCRIPTION, DbSnag.COLUMN_DUE_DATE, DbSnag.COLUMN_ITEM_DATA,
                        DbSnag.COLUMN_TIMESTAMP, DbSnag.COLUMN_LOCATION, DbSnag.COLUMN_PACK_NO,
                        DbSnag.COLUMN_MARK_STATUS},
                DbSnag.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbSnag.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbSnag.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    DbSnag snag = new DbSnag();
                    snag.setId(cursor.getInt(cursor.getColumnIndex(DbSnag.COLUMN_ID)));
                    snag.setRowId(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_ROW_ID)));
                    snag.setSiteId(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_SITE_ID)));
                    snag.setUserId(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_USER_ID)));
                    snag.setProjectId(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_PROJECT_ID)));
                    snag.setUserName(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_USER_NAME)));
                    snag.setToUsers(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_TO_USERS)));
                    snag.setCcUsers(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_CC_USERS)));
                    snag.setPackageNo(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_PACK_NO)));
                    snag.setLocation(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_LOCATION)));
                    snag.setDescription(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_DESCRIPTION)));
                    snag.setDueDate(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_DUE_DATE)));
                    snag.setStatus(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_STATUS)));
                    snag.setMarkStatus(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_MARK_STATUS)));
                    snag.setItemData(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_ITEM_DATA)));
                    snag.setTimeStamp(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_TIMESTAMP)));
                    snagList.add(0, snag);
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return snagList;
        }
        return snagList;
    }

    public List<String> getSyncSnagIdList(String siteID, String userID, String projectID) {
        List<String> syncIdList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbSnag.TABLE_NAME,
                new String[]{DbSnag.COLUMN_ROW_ID},
                DbSnag.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbSnag.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbSnag.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    syncIdList.add(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_ROW_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
            return syncIdList;
        }
        return syncIdList;
    }

    public List<DbSnag> getSnagToSync(String siteID) {
        List<DbSnag> syncIdList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbSnag.TABLE_NAME,
                new String[]{DbSnag.COLUMN_ID, DbSnag.COLUMN_ROW_ID, DbSnag.COLUMN_SITE_ID,
                        DbSnag.COLUMN_USER_ID, DbSnag.COLUMN_PROJECT_ID, DbSnag.COLUMN_USER_NAME,
                        DbSnag.COLUMN_TO_USERS, DbSnag.COLUMN_CC_USERS, DbSnag.COLUMN_STATUS,
                        DbSnag.COLUMN_DESCRIPTION, DbSnag.COLUMN_DUE_DATE, DbSnag.COLUMN_ITEM_DATA,
                        DbSnag.COLUMN_TIMESTAMP, DbSnag.COLUMN_LOCATION, DbSnag.COLUMN_PACK_NO,
                        DbSnag.COLUMN_MARK_STATUS},
                DbSnag.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbSnag.COLUMN_ROW_ID + "=?",
                new String[]{String.valueOf(siteID), "-1"}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    DbSnag snag = new DbSnag();
                    snag.setId(cursor.getInt(cursor.getColumnIndex(DbSnag.COLUMN_ID)));
                    snag.setRowId(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_ROW_ID)));
                    snag.setSiteId(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_SITE_ID)));
                    snag.setUserId(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_USER_ID)));
                    snag.setProjectId(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_PROJECT_ID)));
                    snag.setUserName(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_USER_NAME)));
                    snag.setToUsers(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_TO_USERS)));
                    snag.setCcUsers(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_CC_USERS)));
                    snag.setPackageNo(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_PACK_NO)));
                    snag.setLocation(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_LOCATION)));
                    snag.setDescription(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_DESCRIPTION)));
                    snag.setDueDate(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_DUE_DATE)));
                    snag.setStatus(cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_STATUS)));
                    snag.setMarkStatus(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_MARK_STATUS)));
                    snag.setItemData(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_ITEM_DATA)));
                    snag.setTimeStamp(
                            cursor.getString(cursor.getColumnIndex(DbSnag.COLUMN_TIMESTAMP)));
                    syncIdList.add(snag);
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return syncIdList;
        }

        return syncIdList;
    }

    public boolean noSnagToSync() {
        List<Integer> syncIdList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbSnag.TABLE_NAME, new String[]{DbSnag.COLUMN_ID},
                DbSnag.COLUMN_SITE_ID + "=?" + " AND " + DbSnag.COLUMN_ROW_ID + "=?",
                new String[]{String.valueOf(BuilderStormApplication.mPrefs.getSiteId()), "-1"},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    syncIdList.add(cursor.getColumnIndex(DbSnag.COLUMN_ID));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return syncIdList.isEmpty();
    }

    public void deleteSnags(int rowID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbSnag.TABLE_NAME, DbSnag.COLUMN_ID + "=?", new String[]{String.valueOf(rowID)});
        closeDataBase(db);
    }

    /***********************************************************************************************************
     *                                              Digital Documents operations
     * ********************************************************************************************************/
    public Long insertDigitalDocument(String siteID, String userID, String projectID,
                                      String templateId, String templateName, String recurrenceType,
                                      String document, String categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbDigitalDocs.COLUMN_SITE_ID, siteID);
        values.put(DbDigitalDocs.COLUMN_USER_ID, userID);
        values.put(DbDigitalDocs.COLUMN_ASSIGNED_USER, "");
        values.put(DbDigitalDocs.COLUMN_PROJECT_ID, projectID);
        values.put(DbDigitalDocs.COLUMN_CATEGORY_ID, categoryId);
        values.put(DbDigitalDocs.COLUMN_DOC_TYPE, 0);
        values.put(DbDigitalDocs.COLUMN_ISSUED_BY, "");
        values.put(DbDigitalDocs.COLUMN_MY_ITEM, "");
        values.put(DbDigitalDocs.COLUMN_TEMPLATE_ID, templateId);
        values.put(DbDigitalDocs.COLUMN_TEMPLATE_NAME, templateName);
        values.put(DbDigitalDocs.COLUMN_ROW_ID, "0");
        values.put(DbDigitalDocs.COLUMN_RECURRENCE_TYPE, recurrenceType);
        values.put(DbDigitalDocs.COLUMN_COMPLETE_DATE, "");
        values.put(DbDigitalDocs.COLUMN_DOCUMENT, document);
        values.put(DbDigitalDocs.COLUMN_DOCUMENT_SAVED_DATA, "");
        long id = db.insert(DbDigitalDocs.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public Long insertMyItem(String siteID, String userID, String projectID, PojoMyItem myItem, String document, String catSection, String categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbDigitalDocs.COLUMN_SITE_ID, siteID);
        values.put(DbDigitalDocs.COLUMN_USER_ID, userID);
        values.put(DbDigitalDocs.COLUMN_ASSIGNED_USER, "");
        values.put(DbDigitalDocs.COLUMN_PROJECT_ID, projectID);
        values.put(DbDigitalDocs.COLUMN_CATEGORY_ID, myItem.getCategory());
        values.put(DbDigitalDocs.COLUMN_ISSUED_BY, myItem.getIssueId());
        values.put(DbDigitalDocs.COLUMN_DOC_TYPE, 1);
        values.put(DbDigitalDocs.COLUMN_MY_ITEM, new Gson().toJson(myItem));
        values.put(DbDigitalDocs.COLUMN_TEMPLATE_ID, myItem.getTemplateId());
        values.put(DbDigitalDocs.COLUMN_TEMPLATE_NAME, myItem.getTemplateTitle());
        values.put(DbDigitalDocs.COLUMN_ROW_ID, "0");
        values.put(DbDigitalDocs.COLUMN_COMPLETE_DATE, "");
        values.put(DbDigitalDocs.COLUMN_RECURRENCE_TYPE, myItem.getRecurrence_type());
        values.put(DbDigitalDocs.COLUMN_DOCUMENT, document);
        values.put(DbDigitalDocs.COLUMN_DOCUMENT_SAVED_DATA, "");
        values.put(DbDigitalDocs.COLUMN_CAT_SECTION, catSection);
        values.put(DbDigitalDocs.COLUMN_COMPANY_CAT_ID, categoryId);
        long id = db.insert(DbDigitalDocs.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public Long saveDigitalDocumentValues(String siteID, String userID, String assignedUser, String projectID,
                                          String myItem, String templateId, Integer customDocumentId, Integer projectDocumentId, String savedData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbDigitalDocs.COLUMN_SITE_ID, siteID);
        values.put(DbDigitalDocs.COLUMN_USER_ID, userID);
        values.put(DbDigitalDocs.COLUMN_ASSIGNED_USER, assignedUser);
        values.put(DbDigitalDocs.COLUMN_PROJECT_ID, projectID);
        values.put(DbDigitalDocs.COLUMN_TEMPLATE_ID, templateId);
        values.put(DbDigitalDocs.COLUMN_CUSTOM_DOCUMENT_ID, String.valueOf(customDocumentId));
        values.put(DbDigitalDocs.COLUMN_PROJECT_DOCUMENT_ID, String.valueOf(projectDocumentId));
        values.put(DbCompanyDocuments.COLUMN_CATEGORY_ID, "");
        values.put(DbDigitalDocs.COLUMN_ROW_ID, -1);
        values.put(DbDigitalDocs.COLUMN_MY_ITEM, myItem);
        values.put(DbDigitalDocs.COLUMN_DOCUMENT_SAVED_DATA, savedData);
        long id = db.insert(DbDigitalDocs.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public List<DigitalDoc> getOfflineDiDocListByCategory(String siteId, String userId, String categoryId) {
        List<DigitalDoc> docsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                new String[]{DbDigitalDocs.COLUMN_ID, DbDigitalDocs.COLUMN_ROW_ID,
                        DbDigitalDocs.COLUMN_SITE_ID, DbDigitalDocs.COLUMN_USER_ID,
                        DbDigitalDocs.COLUMN_PROJECT_ID, DbDigitalDocs.COLUMN_DOCUMENT,
                        DbDigitalDocs.COLUMN_TIMESTAMP, DbDigitalDocs.COLUMN_DOCUMENT_SAVED_DATA,
                        DbDigitalDocs.COLUMN_TEMPLATE_ID, DbDigitalDocs.COLUMN_TEMPLATE_NAME,
                        DbDigitalDocs.COLUMN_CATEGORY_ID, DbDigitalDocs.COLUMN_DOC_TYPE,
                        DbDigitalDocs.COLUMN_RECURRENCE_TYPE, DbDigitalDocs.COLUMN_COMPLETE_DATE},
                DbDigitalDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_USER_ID + "=?",
                new String[]{siteId, userId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    DbDigitalDocs doc = new DbDigitalDocs();
                    doc.setRowId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_ROW_ID)));
                    doc.setCategoryId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_CATEGORY_ID)));
                    doc.setTemplateId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_TEMPLATE_ID)));
                    doc.setTemplateName(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_TEMPLATE_NAME)));
                    doc.setDocType(cursor.getInt(cursor.getColumnIndex(DbDigitalDocs.COLUMN_DOC_TYPE)));
                    doc.setRecurrenceType(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_RECURRENCE_TYPE)));
                    doc.setCompleteDate(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_COMPLETE_DATE)));

                    if (doc.getDocType() == 0 && !doc.getCategoryId().isEmpty()
                            && doc.getTemplateId() != null && !doc.getTemplateId().isEmpty()) {
                        if (categoryId.equals("0")) {
                            docsList.add(0, new DigitalDoc(doc.getTemplateName(), doc.getTemplateId(),
                                    doc.getCategoryId(), doc.getRecurrenceType()));
                        } else if (categoryId.equals(doc.getCategoryId())) {
                            docsList.add(0, new DigitalDoc(doc.getTemplateName(), doc.getTemplateId(),
                                    doc.getCategoryId(), doc.getRecurrenceType()));
                        }
                    }

                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return docsList;
    }

    public List<DbDigitalDocs> getToSyncDiDocList() {
        List<DbDigitalDocs> docsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                new String[]{DbDigitalDocs.COLUMN_ID, DbDigitalDocs.COLUMN_ROW_ID,
                        DbDigitalDocs.COLUMN_SITE_ID, DbDigitalDocs.COLUMN_USER_ID, DbDigitalDocs.COLUMN_ASSIGNED_USER,
                        DbDigitalDocs.COLUMN_PROJECT_ID, DbDigitalDocs.COLUMN_TIMESTAMP, DbDigitalDocs.COLUMN_MY_ITEM,
                        DbDigitalDocs.COLUMN_DOCUMENT_SAVED_DATA, DbDigitalDocs.COLUMN_TEMPLATE_ID,
                        DbDigitalDocs.COLUMN_CUSTOM_DOCUMENT_ID, DbDigitalDocs.COLUMN_PROJECT_DOCUMENT_ID},
                DbDigitalDocs.COLUMN_ROW_ID + "=?",
                new String[]{/*siteId,*/ String.valueOf(-1)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    DbDigitalDocs doc = new DbDigitalDocs();
                    doc.setId(cursor.getInt(cursor.getColumnIndex(DbDigitalDocs.COLUMN_ID)));
                    doc.setRowId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_ROW_ID)));
                    doc.setSiteId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_SITE_ID)));
                    doc.setUserId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_USER_ID)));
                    doc.setAssigned_user(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_ASSIGNED_USER)));
                    doc.setProjectId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_PROJECT_ID)));
                    doc.setTemplateId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_TEMPLATE_ID)));
                    doc.setCustomDocumentId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_CUSTOM_DOCUMENT_ID)));
                    doc.setProjectDocumentId(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_PROJECT_DOCUMENT_ID)));
                    doc.setSavedData(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_DOCUMENT_SAVED_DATA)));
                    doc.setMyItem(cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_MY_ITEM)));
                    docsList.add(doc);

                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return docsList;
        }
        return docsList;
    }

    public boolean noDiDocToSync() {
        List<Integer> docsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                new String[]{DbDigitalDocs.COLUMN_ID, DbDigitalDocs.COLUMN_DOCUMENT_SAVED_DATA},
                DbDigitalDocs.COLUMN_ROW_ID + "=?",
                new String[]{/*siteId,*/ String.valueOf(-1)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    docsList.add(cursor.getColumnIndex(DbDigitalDocs.COLUMN_ID));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return docsList.isEmpty();
    }

    public List<String> getSyncedDiDocList(String siteId, String userId, int docType) {
        List<String> idList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                new String[]{DbDigitalDocs.COLUMN_TEMPLATE_ID, DbDigitalDocs.COLUMN_DOC_TYPE},
                DbDigitalDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_USER_ID + "=?",
                new String[]{siteId, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    if (docType == cursor.getInt(cursor.getColumnIndex(DbDigitalDocs.COLUMN_DOC_TYPE))) {
                        idList.add(cursor.getString(
                                cursor.getColumnIndex(DbDigitalDocs.COLUMN_TEMPLATE_ID)));
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return idList;
        }
        return idList;
    }

    public List<String> getSyncedItemDocList(String siteId, String userId, String projectId, int docType) {
        List<String> idList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                new String[]{DbDigitalDocs.COLUMN_TEMPLATE_ID, DbDigitalDocs.COLUMN_DOC_TYPE},
                DbDigitalDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_PROJECT_ID + "=?",
                new String[]{siteId, userId, projectId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    if (docType == cursor.getInt(cursor.getColumnIndex(DbDigitalDocs.COLUMN_DOC_TYPE))) {
                        idList.add(cursor.getString(
                                cursor.getColumnIndex(DbDigitalDocs.COLUMN_TEMPLATE_ID)));
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return idList;
        }
        return idList;
    }

    public List<String> getSyncedShortDocList(String siteId, String userId, String projectId, int docType, String catSection, String categoryId) {
        List<String> idList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                new String[]{DbDigitalDocs.COLUMN_TEMPLATE_ID, DbDigitalDocs.COLUMN_DOC_TYPE},
                DbDigitalDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_CAT_SECTION + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_COMPANY_CAT_ID + "=?",
                new String[]{siteId, userId, projectId, catSection, categoryId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    if (docType == cursor.getInt(cursor.getColumnIndex(DbDigitalDocs.COLUMN_DOC_TYPE))) {
                        idList.add(cursor.getString(
                                cursor.getColumnIndex(DbDigitalDocs.COLUMN_TEMPLATE_ID)));
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return idList;
        }
        return idList;
    }

    public DigitalFormModel getDDTemplateForm(String siteID, String templateId) {
        DigitalFormModel formModel;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                    new String[]{DbDigitalDocs.COLUMN_ID, DbDigitalDocs.COLUMN_DOCUMENT,
                            DbDigitalDocs.COLUMN_ROW_ID},
                    DbDigitalDocs.COLUMN_SITE_ID + "=?"
                            + " AND " +
                            DbDigitalDocs.COLUMN_TEMPLATE_ID + "=?"
                            + " AND " +
                            DbDigitalDocs.COLUMN_ROW_ID + "=?",
                    new String[]{siteID, templateId, String.valueOf(0)}, null, null, null, null);

            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                do {
                    String documentData = cursor.getString(
                            cursor.getColumnIndex(DbDigitalDocs.COLUMN_DOCUMENT));
                    formModel = new Gson().fromJson(documentData, DigitalFormModel.class);
                    formModel.setDeleteRowID(cursor.getInt(cursor.getColumnIndex(DbDigitalDocs.COLUMN_ID)));
                } while (cursor.moveToNext());
                cursor.close();
                return formModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCompleteDate(String siteID, String userID, Integer rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbDigitalDocs.COLUMN_COMPLETE_DATE, CommonMethods.getCurrentDate(CommonMethods.DF_5));
        long id = db.update(DbDigitalDocs.TABLE_NAME, values,
                DbDigitalDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_ID + "=?",
                new String[]{siteID, userID, String.valueOf(rowId)});
        Log.e("update draw", String.valueOf(id));
        closeDataBase(db);
    }

    public void deleteDDTemplateRow(int columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbDigitalDocs.TABLE_NAME, DbDigitalDocs.COLUMN_ID + "=?",
                new String[]{String.valueOf(columnId)});
        closeDataBase(db);
    }

    public String getMyItemRowId(String siteId, String projectId, String templateId) {
        String columnId = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                new String[]{DbDigitalDocs.COLUMN_ID},
                DbDigitalDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_TEMPLATE_ID + "=?",
                new String[]{siteId, projectId, templateId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    columnId = cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_ID));
                } while (cursor.moveToNext());
                cursor.close();
                return columnId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return columnId;
    }

    public List<PojoMyItem> getMyItemsDb(String siteId, String userId, String projectId, String categoryId, String issuedBy, String catSection, String companyProjectCatId) {
        List<PojoMyItem> myItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbDigitalDocs.TABLE_NAME,
                new String[]{DbDigitalDocs.COLUMN_ID, DbDigitalDocs.COLUMN_ROW_ID,
                        DbDigitalDocs.COLUMN_SITE_ID, DbDigitalDocs.COLUMN_USER_ID,
                        DbDigitalDocs.COLUMN_PROJECT_ID, DbDigitalDocs.COLUMN_TIMESTAMP,
                        DbDigitalDocs.COLUMN_MY_ITEM, DbDigitalDocs.COLUMN_DOC_TYPE,
                        DbDigitalDocs.COLUMN_CATEGORY_ID, DbDigitalDocs.COLUMN_ISSUED_BY,
                        DbDigitalDocs.COLUMN_CAT_SECTION, DbDigitalDocs.COLUMN_COMPANY_CAT_ID,
                        DbDigitalDocs.COLUMN_COMPLETE_DATE, DbDigitalDocs.COLUMN_RECURRENCE_TYPE},
                DbDigitalDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        DbDigitalDocs.COLUMN_USER_ID + "=?",
                new String[]{siteId, projectId, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    String dbCategoryId = cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_CATEGORY_ID));
                    String dbIssuedBy = cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_ISSUED_BY));
                    String dbComCatId = cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_COMPANY_CAT_ID));
                    String dbCatSection = cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_CAT_SECTION));
                    String completedDate = cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_COMPLETE_DATE));
                    String recurrenceType = cursor.getString(cursor.getColumnIndex(DbDigitalDocs.COLUMN_RECURRENCE_TYPE));
                    int docType = cursor.getInt(cursor.getColumnIndex(DbDigitalDocs.COLUMN_DOC_TYPE));

                    if (docType == 1) {
                        Log.e("complete Date num ", String.valueOf(completedDate));
                        Log.e("current Date num ", CommonMethods.getCurrentDate(CommonMethods.DF_3));
                        PojoMyItem item = new Gson().fromJson(cursor.getString(
                                cursor.getColumnIndex(DbDigitalDocs.COLUMN_MY_ITEM)), PojoMyItem.class);
                        if (categoryId.equals("0") && issuedBy.equals("0")) {
                            if (CommonMethods.validRecurringCondition(completedDate, recurrenceType)) {
                                myItems.add(0, item);
                            }
                        } else if (categoryId.equals("0") || issuedBy.equals("0")) {
                            if (categoryId.equalsIgnoreCase("0")
                                    && issuedBy.equalsIgnoreCase(dbIssuedBy)) {
                                if (CommonMethods.validRecurringCondition(completedDate, recurrenceType)) {
                                    myItems.add(0, item);
                                }
                            } else if (issuedBy.equalsIgnoreCase("0")
                                    && categoryId.equalsIgnoreCase(dbCategoryId)) {
                                if (CommonMethods.validRecurringCondition(completedDate, recurrenceType)) {
                                    myItems.add(0, item);
                                }
                            }
                        } else if (categoryId.equalsIgnoreCase(dbCategoryId)
                                && issuedBy.equalsIgnoreCase(dbIssuedBy)) {
                            if (CommonMethods.validRecurringCondition(completedDate, recurrenceType)) {
                                myItems.add(0, item);
                            }
                        } else if (catSection.equalsIgnoreCase(dbCatSection)
                                && companyProjectCatId.equalsIgnoreCase(dbComCatId)) {
                            if (CommonMethods.validRecurringCondition(completedDate, recurrenceType)) {
                                myItems.add(0, item);
                            }
                        }
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.e("complete date", myItems.toString());
        return myItems;
    }

    public Long insertShareDocument(String siteID, String userID, String documentData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShareDocs.COLUMN_SITE_ID, siteID);
        values.put(ShareDocs.COLUMN_USER_ID, userID);
        values.put(ShareDocs.COLUMN_DATA_ITEM, documentData);
        long id = db.insert(ShareDocs.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public List<ShareDocs> getToSyncShareDocs(String siteId, String userId) {
        List<ShareDocs> shareDocsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ShareDocs.TABLE_NAME,
                new String[]{ShareDocs.COLUMN_ID, ShareDocs.COLUMN_SITE_ID, ShareDocs.COLUMN_USER_ID, ShareDocs.COLUMN_DATA_ITEM, ShareDocs.COLUMN_TIMESTAMP},
                ShareDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ShareDocs.COLUMN_USER_ID + "=?",
                new String[]{siteId, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    ShareDocs doc = new ShareDocs();
                    doc.setId(cursor.getInt(cursor.getColumnIndex(ShareDocs.COLUMN_ID)));
                    doc.setSiteID(cursor.getString(cursor.getColumnIndex(ShareDocs.COLUMN_SITE_ID)));
                    doc.setUserID(cursor.getString(cursor.getColumnIndex(ShareDocs.COLUMN_USER_ID)));
                    doc.setDocumentData(cursor.getString(cursor.getColumnIndex(ShareDocs.COLUMN_DATA_ITEM)));
                    doc.setTimestamp(cursor.getString(cursor.getColumnIndex(ShareDocs.COLUMN_TIMESTAMP)));
                    shareDocsList.add(doc);
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return shareDocsList;
        }
        return shareDocsList;
    }

    public void deleteShareDocsRow(int columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ShareDocs.TABLE_NAME, ShareDocs.COLUMN_ID + "=?",
                new String[]{String.valueOf(columnId)});
        closeDataBase(db);
    }

    public boolean noShareDocToSync() {
        List<Integer> docsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ShareDocs.TABLE_NAME, new String[]{ShareDocs.COLUMN_ID},
                ShareDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ShareDocs.COLUMN_USER_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getUserId()},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    docsList.add(cursor.getInt(cursor.getColumnIndex(ShareDocs.COLUMN_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return docsList.isEmpty();
    }

    /***********************************************************************************************************
     *                                              Support Section operations
     * ********************************************************************************************************/
    public long insertReport(String userEmail, String userID, String description,
                             String reportFiles) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ReportProblem.COLUMN_USER_ID, userID);
        values.put(ReportProblem.COLUMN_EMAIL, userEmail);
        values.put(ReportProblem.COLUMN_DESCRIPTION, description);
        values.put(ReportProblem.COLUMN_ITEM_DATA, reportFiles);
        long id = db.insert(ReportProblem.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public List<ReportProblem> getIssueReports(String userID) {
        List<ReportProblem> localReports = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ReportProblem.TABLE_NAME,
                new String[]{ReportProblem.COLUMN_ID, ReportProblem.COLUMN_USER_ID, ReportProblem.COLUMN_EMAIL,
                        ReportProblem.COLUMN_DESCRIPTION, ReportProblem.COLUMN_ITEM_DATA,
                        ReportProblem.COLUMN_TIMESTAMP},
                ReportProblem.COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userID)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    ReportProblem issue = new ReportProblem();
                    issue.setId(cursor.getInt(cursor.getColumnIndex(ReportProblem.COLUMN_ID)));
                    issue.setUserID(
                            cursor.getString(cursor.getColumnIndex(ReportProblem.COLUMN_USER_ID)));
                    issue.setUserEmail(
                            cursor.getString(cursor.getColumnIndex(ReportProblem.COLUMN_EMAIL)));
                    issue.setDescription(cursor.getString(
                            cursor.getColumnIndex(ReportProblem.COLUMN_DESCRIPTION)));
                    issue.setLogged_files(cursor.getString(
                            cursor.getColumnIndex(ReportProblem.COLUMN_ITEM_DATA)));
                    issue.setTimestamp(cursor.getString(
                            cursor.getColumnIndex(ReportProblem.COLUMN_TIMESTAMP)));
                    localReports.add(issue);
                } while (cursor.moveToNext());
                cursor.close();
                return localReports;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("TAG", "getReports: " + e.getLocalizedMessage());
            }
        }
        return localReports;
    }

    public void deleteReport(Integer rowID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ReportProblem.TABLE_NAME, ReportProblem.COLUMN_ID + "=?",
                new String[]{String.valueOf(rowID)});
        closeDataBase(db);
    }

    /***********************************************************************************************************
     *                                              Timesheet operations
     *********************************************************************************************************/
    public Long insertTimeSheetTasks(String siteID, String userID, String projectID,
                                     String projectTask, String projectItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbTimeSheetTask.COLUMN_SITE_ID, siteID);
        values.put(DbTimeSheetTask.COLUMN_USER_ID, userID);
        values.put(DbTimeSheetTask.COLUMN_PROJECT_ID, projectID);
        values.put(DbTimeSheetTask.COLUMN_TASK_LIST, projectTask);
        values.put(DbTimeSheetTask.COLUMN_ITEMS_LIST, projectItems);
        long id = db.insert(DbTimeSheetTask.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public Long insertAccessSetting(String siteID, String userID, String accessSettingTS) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccessSettingTS.COLUMN_SITE_ID, siteID);
        values.put(AccessSettingTS.COLUMN_USER_ID, userID);
        values.put(AccessSettingTS.COLUMN_USER_SETTING, accessSettingTS);
        long id = db.insert(AccessSettingTS.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public Long insertTimeSheet(String date, String siteId, String userId, String overView, Integer totalTime, String notes, String viewType, String typeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbTimesheet.COLUMN_SITE_ID, siteId);
        values.put(DbTimesheet.COLUMN_USER_ID, userId);
        values.put(DbTimesheet.COLUMN_DATE, date);
        values.put(DbTimesheet.COLUMN_OVERVIEW, overView);
        values.put(DbTimesheet.COLUMN_NOTES, notes);
        values.put(DbTimesheet.COLUMN_TIME, totalTime);
        values.put(DbTimesheet.COLUMN_TYPE, viewType);
        values.put(DbTimesheet.COLUMN_TYPE_NAME, typeName);
        long id = db.insert(DbTimesheet.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public AccessSettingTS timeSheetSetting(String siteId, String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(AccessSettingTS.TABLE_NAME,
                new String[]{
                        AccessSettingTS.COLUMN_ID, AccessSettingTS.COLUMN_SITE_ID,
                        AccessSettingTS.COLUMN_USER_ID, AccessSettingTS.COLUMN_USER_SETTING,
                        AccessSettingTS.COLUMN_TIMESTAMP
                },
                AccessSettingTS.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        AccessSettingTS.COLUMN_USER_ID + "=?",
                new String[]{siteId, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                AccessSettingTS settingTS = new AccessSettingTS();
                do {
                    settingTS.setId(cursor.getInt(cursor.getColumnIndex(AccessSettingTS.COLUMN_ID)));
                    settingTS.setSiteID(cursor.getString(cursor.getColumnIndex(AccessSettingTS.COLUMN_SITE_ID)));
                    settingTS.setUserID(cursor.getString(cursor.getColumnIndex(AccessSettingTS.COLUMN_USER_ID)));
                    settingTS.setSetting(cursor.getString(cursor.getColumnIndex(AccessSettingTS.COLUMN_USER_SETTING)));
                    settingTS.setTimestamp(cursor.getString(cursor.getColumnIndex(AccessSettingTS.COLUMN_TIMESTAMP)));
                } while (cursor.moveToNext());
                cursor.close();
                return settingTS;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public DbTimesheet getTimeSheet(String siteId, String userId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbTimesheet.TABLE_NAME,
                new String[]{DbTimesheet.COLUMN_ID, DbTimesheet.COLUMN_SITE_ID,
                        DbTimesheet.COLUMN_USER_ID, DbTimesheet.COLUMN_DATE,
                        DbTimesheet.COLUMN_OVERVIEW, DbTimesheet.COLUMN_TIME,
                        DbTimesheet.COLUMN_TYPE, DbTimesheet.COLUMN_TYPE_NAME,
                        DbTimesheet.COLUMN_NOTES, DbTimesheet.COLUMN_TIMESTAMP},
                DbTimesheet.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbTimesheet.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbTimesheet.COLUMN_DATE + "=?",
                new String[]{siteId, userId, date}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                DbTimesheet timesheet = new DbTimesheet();
                do {
                    timesheet.setId(cursor.getInt(cursor.getColumnIndex(DbTimesheet.COLUMN_ID)));
                    timesheet.setTotalTime(cursor.getInt(cursor.getColumnIndex(DbTimesheet.COLUMN_TIME)));
                    timesheet.setSiteId(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_SITE_ID)));
                    timesheet.setUserId(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_USER_ID)));
                    timesheet.setOverView(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_OVERVIEW)));
                    timesheet.setType(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_TYPE)));
                    timesheet.setTypeName(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_TYPE_NAME)));
                    timesheet.setNotes(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_NOTES)));

                } while (cursor.moveToNext());
                cursor.close();
                return timesheet;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<DbTimesheet> getTimeSheetsToSync(String siteId, String userId) {
        List<DbTimesheet> timesheetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbTimesheet.TABLE_NAME,
                new String[]{DbTimesheet.COLUMN_ID, DbTimesheet.COLUMN_SITE_ID,
                        DbTimesheet.COLUMN_USER_ID, DbTimesheet.COLUMN_DATE,
                        DbTimesheet.COLUMN_OVERVIEW, DbTimesheet.COLUMN_TIME,
                        DbTimesheet.COLUMN_NOTES, DbTimesheet.COLUMN_TIMESTAMP},
                DbTimesheet.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbTimesheet.COLUMN_USER_ID + "=?",
                new String[]{siteId, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {

                do {
                    DbTimesheet timesheet = new DbTimesheet();
                    timesheet.setId(cursor.getInt(cursor.getColumnIndex(DbTimesheet.COLUMN_ID)));
                    timesheet.setTotalTime(cursor.getInt(cursor.getColumnIndex(DbTimesheet.COLUMN_TIME)));
                    timesheet.setSiteId(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_SITE_ID)));
                    timesheet.setUserId(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_USER_ID)));
                    timesheet.setOverView(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_OVERVIEW)));
                    timesheet.setNotes(cursor.getString(cursor.getColumnIndex(DbTimesheet.COLUMN_NOTES)));
                    timesheetList.add(timesheet);

                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return timesheetList;
    }


    public DbTimeSheetTask getTSTasks(String siteId, String projectId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbTimeSheetTask.TABLE_NAME,
                new String[]{DbTimeSheetTask.COLUMN_ID, DbTimeSheetTask.COLUMN_SITE_ID, DbTimeSheetTask.COLUMN_USER_ID,
                        DbTimeSheetTask.COLUMN_PROJECT_ID, DbTimeSheetTask.COLUMN_TASK_LIST, DbTimeSheetTask.COLUMN_ITEMS_LIST, DbTimeSheetTask.COLUMN_TIMESTAMP},
                DbTimeSheetTask.COLUMN_SITE_ID + "=?" + " AND " + DbTimeSheetTask.COLUMN_PROJECT_ID + "=?",
                new String[]{siteId, projectId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                DbTimeSheetTask timesheetTasks = new DbTimeSheetTask();
                do {
                    timesheetTasks.setId(cursor.getInt(cursor.getColumnIndex(DbTimeSheetTask.COLUMN_ID)));
                    timesheetTasks.setSiteID(cursor.getString(cursor.getColumnIndex(DbTimeSheetTask.COLUMN_SITE_ID)));
                    timesheetTasks.setUserID(cursor.getString(cursor.getColumnIndex(DbTimeSheetTask.COLUMN_USER_ID)));
                    timesheetTasks.setProjectID(cursor.getString(cursor.getColumnIndex(DbTimeSheetTask.COLUMN_PROJECT_ID)));
                    timesheetTasks.setProjectTasks(cursor.getString(cursor.getColumnIndex(DbTimeSheetTask.COLUMN_TASK_LIST)));
                    timesheetTasks.setProjectItems(cursor.getString(cursor.getColumnIndex(DbTimeSheetTask.COLUMN_ITEMS_LIST)));
                    timesheetTasks.setTimestamp(cursor.getString(cursor.getColumnIndex(DbTimeSheetTask.COLUMN_TIMESTAMP)));
                } while (cursor.moveToNext());
                cursor.close();
                return timesheetTasks;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public long updateTimeSheet(String siteID, String userId, String date, Integer totalTime, String overView, String notes, String viewType, String typeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbTimesheet.COLUMN_OVERVIEW, overView);
        values.put(DbTimesheet.COLUMN_NOTES, notes);
        values.put(DbTimesheet.COLUMN_TIME, totalTime);
        values.put(DbTimesheet.COLUMN_TYPE, viewType);
        values.put(DbTimesheet.COLUMN_TYPE_NAME, typeName);
        long id = db.update(DbTimesheet.TABLE_NAME, values,
                DbTimesheet.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        DbTimesheet.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbTimesheet.COLUMN_DATE + "=?",
                new String[]{siteID, userId, date});
        closeDataBase(db);
        return id;
    }

    public long updateTimeSheetTask(String siteID, String projectId, String tasks, String priceWorks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbTimeSheetTask.COLUMN_TASK_LIST, tasks);
        values.put(DbTimeSheetTask.COLUMN_ITEMS_LIST, priceWorks);
        long id = db.update(DbTimeSheetTask.TABLE_NAME, values,
                DbTimeSheetTask.COLUMN_SITE_ID + " = ?"
//                        + " AND " +
//                        DbTimeSheetTask.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbTimeSheetTask.COLUMN_PROJECT_ID + "=?",
                new String[]{siteID, projectId});
        closeDataBase(db);
        return id;
    }

    public boolean noTimeSheetToSync() {
        List<Integer> docsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbTimesheet.TABLE_NAME,
                new String[]{DbTimesheet.COLUMN_ID},
                DbTimesheet.COLUMN_SITE_ID + "=?" + " AND " +
                        DbTimesheet.COLUMN_USER_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getSiteId(),
                        BuilderStormApplication.mPrefs.getUserId()},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    docsList.add(cursor.getInt(cursor.getColumnIndex(DbTimesheet.COLUMN_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return docsList.isEmpty();
    }

    public void deleteTimeSheet(int columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbTimesheet.TABLE_NAME, DbTimesheet.COLUMN_ID + "=?",
                new String[]{String.valueOf(columnId)});
        closeDataBase(db);
    }


    /***********************************************************************************************************
     *                                              Projects Documents
     * ********************************************************************************************************/
    public long insertProjectDocs(String siteID, String userID, String projectID, String categoryID, String rowID, String projectItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProjectDocs.COLUMN_SITE_ID, siteID);
        values.put(ProjectDocs.COLUMN_USER_ID, userID);
        values.put(ProjectDocs.COLUMN_PROJECT_ID, projectID);
        values.put(ProjectDocs.COLUMN_CATEGORY_ID, categoryID);
        values.put(ProjectDocs.COLUMN_ROW_ID, rowID);
        values.put(ProjectDocs.COLUMN_DATA_ITEM, projectItem);
        long id = db.insert(ProjectDocs.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }


    public long updateProjectDocOffline(String siteID, String userID, String projectID, Integer tableRowId, PDocsDataModel document) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProjectDocs.COLUMN_CATEGORY_ID, document.getCategoryUid());
        values.put(ProjectDocs.COLUMN_DATA_ITEM, new Gson().toJson(document));
        long id = db.update(ProjectDocs.TABLE_NAME, values,
                ProjectDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_USER_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_ID + "=?",
                new String[]{siteID, userID, projectID, String.valueOf(tableRowId)});
        closeDataBase(db);
        return id;
    }

    public long updateProjectDocFromOnline(String siteID, String userID, PDocsDataModel document) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ProjectDocs.COLUMN_CATEGORY_ID, document.getCategoryUid());
        values.put(ProjectDocs.COLUMN_DATA_ITEM, new Gson().toJson(document));
        long id = db.update(ProjectDocs.TABLE_NAME, values,
                ProjectDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_USER_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_ROW_ID + "=?",
                new String[]{siteID, userID, document.getProjectUid(), String.valueOf(document.getUid())});
        closeDataBase(db);
        return id;
    }

    public List<PDocsDataModel> getProjectDocuments(String siteId, String userId, String
            projectId, String categoryId) {
        List<PDocsDataModel> pDocsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ProjectDocs.TABLE_NAME,
                new String[]{ProjectDocs.COLUMN_DATA_ITEM, ProjectDocs.COLUMN_CATEGORY_ID, ProjectDocs.COLUMN_ID},
                ProjectDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_USER_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_PROJECT_ID + "=?",
                new String[]{siteId, userId, projectId}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    String columnData = cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_DATA_ITEM));
                    String catId = cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_CATEGORY_ID));
                    String tableRowId = cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_ID));
                    if (categoryId == null || categoryId.isEmpty()) {
                        pDocsList.add(0, new Gson().fromJson(columnData, PDocsDataModel.class));
                        pDocsList.get(0).setTableRowId(Integer.parseInt(tableRowId));
                    } else if (catId.equalsIgnoreCase(categoryId)) {
                        pDocsList.add(0, new Gson().fromJson(columnData, PDocsDataModel.class));
                        pDocsList.get(0).setTableRowId(Integer.parseInt(tableRowId));
                    }

                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pDocsList;
    }

    public List<ProjectDocs> getAllProjectDocuments(String siteId, String userId) {
        List<ProjectDocs> documentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ProjectDocs.TABLE_NAME,
                new String[]{ProjectDocs.COLUMN_ID, ProjectDocs.COLUMN_SITE_ID,
                        ProjectDocs.COLUMN_USER_ID, ProjectDocs.COLUMN_PROJECT_ID, ProjectDocs.COLUMN_CATEGORY_ID,
                        ProjectDocs.COLUMN_DATA_ITEM},
                ProjectDocs.COLUMN_SITE_ID + "=?"
                        + " AND " + ProjectDocs.COLUMN_USER_ID + "=?"
                        + " AND " + ProjectDocs.COLUMN_ROW_ID + "=?",
                new String[]{siteId, userId, "0"}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    ProjectDocs document = new ProjectDocs();
                    document.setId(cursor.getInt(cursor.getColumnIndex(ProjectDocs.COLUMN_ID)));
                    document.setCategoryID(cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_CATEGORY_ID)));
                    document.setSiteID(cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_SITE_ID)));
                    document.setProjectData(cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_DATA_ITEM)));
                    document.setUserID(cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_USER_ID)));
                    document.setProjectID(cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_PROJECT_ID)));
                    documentList.add(document);
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return documentList;
    }

    public boolean noProDocToSync() {
        List<Integer> docsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ProjectDocs.TABLE_NAME, new String[]{ProjectDocs.COLUMN_ID},
                ProjectDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_USER_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getSiteId(), BuilderStormApplication.mPrefs.getUserId()},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    docsList.add(cursor.getInt(cursor.getColumnIndex(ProjectDocs.COLUMN_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return docsList.isEmpty();
    }


    public void deleteProjectDocById(int columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProjectDocs.TABLE_NAME, ProjectDocs.COLUMN_ID + "=?",
                new String[]{String.valueOf(columnId)});
        closeDataBase(db);
    }

    public List<String> getDocsIdList(String siteID, String userID, String projectID) {
        List<String> syncIdList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ProjectDocs.TABLE_NAME,
                new String[]{ProjectDocs.COLUMN_ROW_ID},
                ProjectDocs.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_USER_ID + "=?"
                        + " AND " +
                        ProjectDocs.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(
                        projectID)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    syncIdList.add(cursor.getString(cursor.getColumnIndex(ProjectDocs.COLUMN_ROW_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return syncIdList;
    }

    /***********************************************************************************************************
     *                                              Company Documents
     * ********************************************************************************************************/
    public long insertCompanyDocs(String rowId, String siteId, String userId, String
            categoryId, String projects) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbCompanyDocuments.COLUMN_ROW_ID, rowId);
        values.put(DbCompanyDocuments.COLUMN_SITE_ID, siteId);
        values.put(DbCompanyDocuments.COLUMN_USER_ID, userId);
        values.put(DbCompanyDocuments.COLUMN_CATEGORY_ID, categoryId);
        values.put(DbCompanyDocuments.COLUMN_DATA_ITEM, projects);
        long id = db.insert(DbCompanyDocuments.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public List<CompanyDocument> getCompanyDocuments(String siteId, String userId, String categoryId) {
        List<CompanyDocument> documentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbCompanyDocuments.TABLE_NAME,
                new String[]{DbCompanyDocuments.COLUMN_DATA_ITEM, DbCompanyDocuments.COLUMN_CATEGORY_ID},
                DbCompanyDocuments.COLUMN_SITE_ID + "=?"
                        + " AND " + DbCompanyDocuments.COLUMN_USER_ID + "=?",
                new String[]{siteId, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    String columnData = cursor.getString(cursor.getColumnIndex(
                            DbCompanyDocuments.COLUMN_DATA_ITEM));
                    String catId = cursor.getString(cursor.getColumnIndex(
                            DbCompanyDocuments.COLUMN_CATEGORY_ID));
                    if (categoryId == null || categoryId.isEmpty()) {
                        documentList.add(0, new Gson().fromJson(columnData, CompanyDocument.class));
                    } else if (catId.equalsIgnoreCase(categoryId)) {
                        documentList.add(0, new Gson().fromJson(columnData, CompanyDocument.class));
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return documentList;
    }

    public List<String> getSyncedCompanyDocs(String siteID, String userId) {
        List<String> syncList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbCompanyDocuments.TABLE_NAME,
                new String[]{DbCompanyDocuments.COLUMN_ROW_ID},
                DbCompanyDocuments.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbCompanyDocuments.COLUMN_USER_ID + "=?",
                new String[]{siteID, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    syncList.add(cursor.getString(cursor.getColumnIndex(DbCompanyDocuments.COLUMN_ROW_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return syncList;
    }

    public List<DbCompanyDocuments> getAllCompanyDocumentsToSync(String siteId, String userId) {
        List<DbCompanyDocuments> documentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbCompanyDocuments.TABLE_NAME,
                new String[]{DbCompanyDocuments.COLUMN_ID, DbCompanyDocuments.COLUMN_SITE_ID,
                        DbCompanyDocuments.COLUMN_USER_ID, DbCompanyDocuments.COLUMN_CATEGORY_ID,
                        DbCompanyDocuments.COLUMN_DATA_ITEM, DbCompanyDocuments.COLUMN_ROW_ID},
                DbCompanyDocuments.COLUMN_SITE_ID + "=?"
                        + " AND " + DbCompanyDocuments.COLUMN_USER_ID + "=?"
                        + " AND " + DbCompanyDocuments.COLUMN_ROW_ID + "=?",
                new String[]{siteId, userId, "0"}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    DbCompanyDocuments document = new DbCompanyDocuments();
                    document.setId(cursor.getInt(cursor.getColumnIndex(DbCompanyDocuments.COLUMN_ID)));
                    document.setCategoryId(cursor.getString(cursor.getColumnIndex(
                            DbCompanyDocuments.COLUMN_CATEGORY_ID)));
                    document.setSiteId(cursor.getString(cursor.getColumnIndex(
                            DbCompanyDocuments.COLUMN_SITE_ID)));
                    document.setCompanyData(cursor.getString(cursor.getColumnIndex(
                            DbCompanyDocuments.COLUMN_DATA_ITEM)));
                    document.setUserId(cursor.getString(cursor.getColumnIndex(
                            DbCompanyDocuments.COLUMN_USER_ID)));
                    documentList.add(document);
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return documentList;
    }

    public boolean noComDocToSync() {
        List<Integer> docsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbCompanyDocuments.TABLE_NAME,
                new String[]{DbCompanyDocuments.COLUMN_ID},
                DbCompanyDocuments.COLUMN_SITE_ID + "=?" + " AND " +
                        DbCompanyDocuments.COLUMN_USER_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getSiteId(),
                        BuilderStormApplication.mPrefs.getUserId()},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    docsList.add(cursor.getInt(cursor.getColumnIndex(DbCompanyDocuments.COLUMN_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return docsList.isEmpty();
    }

    public long updateCompanyDocFromOnline(String siteID, String userID, CompanyDocument document) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbCompanyDocuments.COLUMN_CATEGORY_ID, document.getCategory());
        values.put(DbCompanyDocuments.COLUMN_DATA_ITEM, new Gson().toJson(document));
        long id = db.update(DbCompanyDocuments.TABLE_NAME, values,
                DbCompanyDocuments.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbCompanyDocuments.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbCompanyDocuments.COLUMN_ROW_ID + "=?",
                new String[]{siteID, userID, document.getId()});
        closeDataBase(db);
        return id;
    }

    public void deleteCompanyDocById(int columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbCompanyDocuments.TABLE_NAME, DbCompanyDocuments.COLUMN_ID + "=?",
                new String[]{String.valueOf(columnId)});
        closeDataBase(db);
    }

    /* **********************************************************************************************************
     *                                              To_Do_s listing
     * ********************************************************************************************************/
    public long insertToDoItem(String siteId, String userId, String projectId, String rowId, String categoryId, String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbToDo.COLUMN_ROW_ID, rowId);
        values.put(DbToDo.COLUMN_SITE_ID, siteId);
        values.put(DbToDo.COLUMN_USER_ID, userId);
        values.put(DbToDo.COLUMN_PROJECT_ID, projectId);
        values.put(DbToDo.COLUMN_CATEGORY_ID, categoryId);
        values.put(DbToDo.COLUMN_DATA_ITEM, item);
        long id = db.insert(DbToDo.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }


    public Long updateToDoItem(String siteId, String userId, PojoToDo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbToDo.COLUMN_CATEGORY_ID, String.valueOf(todo.getCategory()));
        values.put(DbToDo.COLUMN_DATA_ITEM, new Gson().toJson(todo));
        long id = db.update(DbToDo.TABLE_NAME, values,
                DbToDo.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbToDo.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbToDo.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        DbToDo.COLUMN_ROW_ID + "=?",
                new String[]{siteId, userId, String.valueOf(todo.getProjectId()),
                        String.valueOf(todo.getId())});
        closeDataBase(db);
        return id;
    }

    public Long updateToDoOffline(String siteId, String userId, PojoToDo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbToDo.COLUMN_CATEGORY_ID, String.valueOf(todo.getCategory()));
        values.put(DbToDo.COLUMN_DATA_ITEM, new Gson().toJson(todo));
        long id = db.update(DbToDo.TABLE_NAME, values,
                DbToDo.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbToDo.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbToDo.COLUMN_PROJECT_ID + "=?"
                        + " AND " +
                        DbToDo.COLUMN_ID + "=?",
                new String[]{siteId, userId, String.valueOf(todo.getProjectId()),
                        String.valueOf(todo.getColumn_id())});
        closeDataBase(db);
        return id;
    }


    public List<PojoToDo> getAllToDos(String siteId, String userId, String projectId) {
        List<PojoToDo> toDoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbToDo.TABLE_NAME,
                new String[]{DbToDo.COLUMN_ID, DbToDo.COLUMN_SITE_ID, DbToDo.COLUMN_USER_ID,
                        DbToDo.COLUMN_CATEGORY_ID, DbToDo.COLUMN_DATA_ITEM, DbToDo.COLUMN_ROW_ID},
                DbToDo.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        DbToDo.COLUMN_USER_ID + "=?"
                        + " AND " +
                        DbToDo.COLUMN_PROJECT_ID + "=?",
                new String[]{siteId, userId, projectId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    String columnId = cursor.getString(cursor.getColumnIndex(DbToDo.COLUMN_ID));
                    String item = cursor.getString(cursor.getColumnIndex(DbToDo.COLUMN_DATA_ITEM));
                    PojoToDo toDo = new Gson().fromJson(item, PojoToDo.class);
                    if (toDo != null) {
                        setStatus(toDo);
                        toDo.setColumn_id(columnId != null ? Integer.parseInt(columnId) : 0);
                        toDoList.add(0, toDo);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return toDoList;
    }

    private void setStatus(PojoToDo toDo) {
        long days;
        DateFormat commonFormat = CommonMethods.getDateFormat(CommonMethods.DF_2);
        try {
            if (toDo.getStatus() != 4) {
                if (toDo.getCompletedOn() != null && !toDo.getCompletedOn().equals("")) {
                    toDo.setStatus(4);
                } else if (CommonMethods.compareDates(toDo.getDueDate(), CommonMethods.getCurrentDate(CommonMethods.DF_2))) {
                    toDo.setStatus(3);
                } else {
                    days = CommonMethods.daysBetween(CommonMethods.getTodayDate(CommonMethods.DF_2), commonFormat.parse(toDo.getDueDate()));
                    if (days < 30)
                        toDo.setStatus(2);
                    else {
                        toDo.setStatus(1);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            toDo.setStatus(1);
        }

    }

    public List<String> getSyncedToDos(String siteId, String userId, String projectId) {
        List<String> toDoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbToDo.TABLE_NAME,
                new String[]{DbToDo.COLUMN_ID, DbToDo.COLUMN_SITE_ID, DbToDo.COLUMN_USER_ID,
                        DbToDo.COLUMN_CATEGORY_ID, DbToDo.COLUMN_DATA_ITEM, DbToDo.COLUMN_ROW_ID},
                DbToDo.COLUMN_SITE_ID + "=?"
                        + " AND " + DbToDo.COLUMN_PROJECT_ID + "=?"
                        + " AND " + DbToDo.COLUMN_USER_ID + "=?",
                new String[]{siteId, projectId, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    toDoList.add(cursor.getString(cursor.getColumnIndex(DbToDo.COLUMN_ROW_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return toDoList;
    }


    public boolean noToDoToSync() {
        List<Integer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbToDo.TABLE_NAME,
                new String[]{DbToDo.COLUMN_ID},
                DbToDo.COLUMN_SITE_ID + "=?" + " AND " + DbToDo.COLUMN_USER_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getSiteId(),
                        BuilderStormApplication.mPrefs.getUserId()},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    list.add(cursor.getInt(cursor.getColumnIndex(DbToDo.COLUMN_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list.isEmpty();

    }

    public List<SyncToDoModel> getToDosToSync(String siteId, String userId) {
        List<SyncToDoModel> toDoList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DbToDo.TABLE_NAME,
                new String[]{DbToDo.COLUMN_ID, DbToDo.COLUMN_SITE_ID, DbToDo.COLUMN_USER_ID,
                        DbToDo.COLUMN_CATEGORY_ID, DbToDo.COLUMN_DATA_ITEM, DbToDo.COLUMN_ROW_ID},
                DbToDo.COLUMN_SITE_ID + "=?"
                        + " AND " + DbToDo.COLUMN_USER_ID + "=?"
                        + " AND " + DbToDo.COLUMN_ROW_ID + "=?",
                new String[]{siteId, userId, "0"}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    int columnId = cursor.getInt(cursor.getColumnIndex(DbToDo.COLUMN_ID));
                    String item = cursor.getString(cursor.getColumnIndex(DbToDo.COLUMN_DATA_ITEM));
                    PojoToDo toDo = new Gson().fromJson(item, PojoToDo.class);
                    if (toDo != null) {
                        toDoList.add(0, new SyncToDoModel(columnId, toDo));
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return toDoList;
    }

    public void deleteToDoById(int columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DbToDo.TABLE_NAME, DbToDo.COLUMN_ID + "=?",
                new String[]{String.valueOf(columnId)});
        closeDataBase(db);
    }

    public long insertToDoResolvedStatus(String siteId, String userId, Integer todoId, Integer status, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ToDoCompleteStatus.COLUMN_SITE_ID, siteId);
        values.put(ToDoCompleteStatus.COLUMN_USER_ID, userId);
        values.put(ToDoCompleteStatus.COLUMN_TODO_ID, todoId);
        values.put(ToDoCompleteStatus.COLUMN_STATUS, status);
        values.put(ToDoCompleteStatus.COLUMN_DATE, date);
        long id = db.insert(ToDoCompleteStatus.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updateToDoResolvedStatus(String siteId, String userId, Integer todoId, Integer status, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ToDoCompleteStatus.COLUMN_STATUS, status);
        values.put(ToDoCompleteStatus.COLUMN_DATE, date);
        long id = db.update(ToDoCompleteStatus.TABLE_NAME, values,
                ToDoCompleteStatus.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ToDoCompleteStatus.COLUMN_USER_ID + "=?"
                        + " AND " +
                        ToDoCompleteStatus.COLUMN_TODO_ID + "=?",
                new String[]{siteId, userId, String.valueOf(todoId)});
        closeDataBase(db);
        return id;
    }

    public boolean isToDoItemResolved(Integer todoId) {
        List<Integer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ToDoCompleteStatus.TABLE_NAME,
                new String[]{ToDoCompleteStatus.COLUMN_ID},
                ToDoCompleteStatus.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ToDoCompleteStatus.COLUMN_USER_ID + "=?"
                        + " AND " +
                        ToDoCompleteStatus.COLUMN_TODO_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getSiteId(),
                        BuilderStormApplication.mPrefs.getUserId(), String.valueOf(todoId)},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    list.add(cursor.getInt(cursor.getColumnIndex(DbToDo.COLUMN_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list.isEmpty();
    }

    public boolean noToDoStatusToSync() {
        List<Integer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ToDoCompleteStatus.TABLE_NAME,
                new String[]{ToDoCompleteStatus.COLUMN_ID},
                ToDoCompleteStatus.COLUMN_SITE_ID + "=?"
                        + " AND " +
                        ToDoCompleteStatus.COLUMN_USER_ID + "=?",
                new String[]{BuilderStormApplication.mPrefs.getSiteId(),
                        BuilderStormApplication.mPrefs.getUserId()},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    list.add(cursor.getInt(cursor.getColumnIndex(DbToDo.COLUMN_ID)));
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list.isEmpty();
    }

    public List<ToDoCompleteStatus> getToDoStautsToSync(String siteId, String userId) {
        List<ToDoCompleteStatus> toDoStatusList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ToDoCompleteStatus.TABLE_NAME,
                new String[]{ToDoCompleteStatus.COLUMN_ID, ToDoCompleteStatus.COLUMN_SITE_ID, ToDoCompleteStatus.COLUMN_USER_ID,
                        ToDoCompleteStatus.COLUMN_TODO_ID, ToDoCompleteStatus.COLUMN_STATUS, ToDoCompleteStatus.COLUMN_DATE},
                ToDoCompleteStatus.COLUMN_SITE_ID + "=?"
                        + " AND " + DbToDo.COLUMN_USER_ID + "=?",
                new String[]{siteId, userId}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    ToDoCompleteStatus status = new ToDoCompleteStatus();
                    status.setId(cursor.getInt(cursor.getColumnIndex(ToDoCompleteStatus.COLUMN_ID)));
                    status.setSiteId(cursor.getString(cursor.getColumnIndex(ToDoCompleteStatus.COLUMN_SITE_ID)));
                    status.setUserId(cursor.getString(cursor.getColumnIndex(ToDoCompleteStatus.COLUMN_USER_ID)));
                    status.setTodoId(cursor.getInt(cursor.getColumnIndex(ToDoCompleteStatus.COLUMN_TODO_ID)));
                    status.setStatus(cursor.getInt(cursor.getColumnIndex(ToDoCompleteStatus.COLUMN_STATUS)));
                    status.setDate(cursor.getString(cursor.getColumnIndex(ToDoCompleteStatus.COLUMN_DATE)));
                    if (status != null) {
                        toDoStatusList.add(status);
                    }
                } while (cursor.moveToNext());
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return toDoStatusList;
    }

    public void deleteToDoStatusId(int columnId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ToDoCompleteStatus.TABLE_NAME, ToDoCompleteStatus.COLUMN_ID + "=?",
                new String[]{String.valueOf(columnId)});
        closeDataBase(db);
    }


    /***********************************************************************************************************
     *                                            SHORTCUT CATEGORIES operations
     * ********************************************************************************************************/

    public long insertShortcutCat(String siteID, String userID, String projectId, String itemData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShortcutCategories.COLUMN_SITE_ID, siteID);
        values.put(ShortcutCategories.COLUMN_USER_ID, userID);
        values.put(ShortcutCategories.COLUMN_PROJECT_ID, projectId);
        values.put(ShortcutCategories.COLUMN_ITEM_DATA, itemData);
        long id = db.insert(ShortcutCategories.TABLE_NAME, null, values);
        closeDataBase(db);
        return id;
    }

    public long updateShortcutCat(String siteID, String userID, String projectId, String itemData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ShortcutCategories.COLUMN_ITEM_DATA, itemData);
        long id = db.update(ShortcutCategories.TABLE_NAME, values,
                ShortcutCategories.COLUMN_SITE_ID + " = ?"
                        + " AND " +
                        ShortcutCategories.COLUMN_USER_ID + "=?"
                        + " AND " +
                        ShortcutCategories.COLUMN_PROJECT_ID + "=?",
                new String[]{String.valueOf(siteID), String.valueOf(userID), String.valueOf(projectId)});
        closeDataBase(db);
        return id;
    }

    public ShortcutCategories getShortcutCat(String siteID, String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query(ShortcutCategories.TABLE_NAME,
                    new String[]{ShortcutCategories.COLUMN_ITEM_DATA},
                    ShortcutCategories.COLUMN_SITE_ID + "=?"
                            + " AND " +
                            ShortcutCategories.COLUMN_USER_ID + "=?",
                    new String[]{String.valueOf(siteID), String.valueOf(userID)}, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                ShortcutCategories itemData = new ShortcutCategories(cursor.getString(cursor.getColumnIndex(ShortcutCategories.COLUMN_ITEM_DATA))
                );
                cursor.close();
                return itemData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
