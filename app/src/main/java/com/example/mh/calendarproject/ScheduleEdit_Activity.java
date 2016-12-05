package com.example.mh.calendarproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by mh on 2016-11-24.
 */

public class ScheduleEdit_Activity extends Activity implements View.OnClickListener {
    MyDBHelper mDBHelper;
    int mId;
    String today;
    EditText editDate, editTitle, editTime,editTime1,editPlace, editMemo;
    private AlertDialog mDialog = null;
    SQLiteDatabase db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_edit);

        editDate = (EditText) findViewById(R.id.editdate);
        editTitle = (EditText) findViewById(R.id.edittitle);
        editTime = (EditText) findViewById(R.id.edittime);
        editTime1 = (EditText) findViewById(R.id.edittime1);
        editPlace = (EditText) findViewById(R.id.editplace);
        editMemo = (EditText) findViewById(R.id.editmemo);

        Intent intent = getIntent();
        mId = intent.getIntExtra("ParamID", -1);
        today = intent.getStringExtra("ParamDate");

        mDBHelper = new MyDBHelper(this, "Today_edit.db", null, 5);

        if (mId == -1) {
            editDate.setText(today);
        } else {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM today_edit WHERE _id='" + mId
                    + "'", null);

            if (cursor.moveToNext()) {
                editTitle.setText(cursor.getString(1));
                editDate.setText(cursor.getString(2));
                editTime.setText(cursor.getString(3));
                editTime1.setText(cursor.getString(4));
                editPlace.setText(cursor.getString(5));
                editMemo.setText(cursor.getString(6));
            }
            mDBHelper.close();
        }

        Button btn1 = (Button) findViewById(R.id.btnsave);
        btn1.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.btndel);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.btncancel);
        btn3.setOnClickListener(this);

        if (mId == -1) {
            btn2.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnsave:
                if (mId != -1) {
                    db.execSQL("UPDATE today_edit SET title='"
                            + editTitle.getText().toString() + "',date='"
                            + editDate.getText().toString() + "', time='"
                            + editTime.getText().toString() + "', time='"
                            + editTime1.getText().toString() + "', place='"
                            + editPlace.getText().toString() + "', memo='"
                            + editMemo.getText().toString() + "' WHERE _id='" + mId
                            + "';");
                } else {
                    db.execSQL("INSERT INTO today_edit VALUES(null, '"
                            + editTitle.getText().toString() + "', '"
                            + editDate.getText().toString() + "', '"
                            + editTime.getText().toString() + "', '"
                            + editTime1.getText().toString() + "', '"
                            + editPlace.getText().toString() + "', '"
                            + editMemo.getText().toString() + "');");
                }
                mDBHelper.close();
                setResult(RESULT_OK);
                break;
            case R.id.btndel:
                if (mId != -1) {
                    db.execSQL("DELETE FROM today_edit WHERE _id='" + mId + "';");
                    mDBHelper.close();
                }
                setResult(RESULT_OK);
                break;
            case R.id.btncancel:
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }
}
