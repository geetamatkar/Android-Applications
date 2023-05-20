package com.example.androidnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class Add extends AppCompatActivity {
    private int position;
    Notes notesObj;
    private int counter;
    EditText title, content;

    private void initializeData(){
        // initialise all class variables
        notesObj = new Notes();
        counter = 0;
        position = -1;
        content = findViewById(R.id.content);
        title = findViewById(R.id.title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initializeData();
        modifyCurrentData();

        //TextWatcher Controls for note title and contents
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counter++;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counter++;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_savenote, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        // check if no changes are made to the note
        if (counter == 0) {
            Add.super.onBackPressed();
        }
        else
        {
            // create dialog for user responses
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Note: " + title.getText().toString() + "\n" + "Save note?");
            // If User selects No, then dismiss the changes. If selects Yes, then save the changes from the user
            dialogBuilder.setNegativeButton("No", (dialog, which) -> {dialog.dismiss();
                Add.super.onBackPressed();
            });

            dialogBuilder.setPositiveButton("Yes", (dialog, which) -> { dialog.dismiss();
                saveModifiedNote();
            });
            AlertDialog altDialog = dialogBuilder.create();
            altDialog.show();


        }
    }
    private void modifyCurrentData()
    {
        // get data if there is existing
        Intent dataIntent = getIntent();
        if (!dataIntent.hasExtra("NOTES CLASS"))
        {
            title.setText("");
            content.setText("");
        }
        else
        {
            notesObj = (Notes) dataIntent.getSerializableExtra("NOTES CLASS");
            if(notesObj != null)
            {
                position = dataIntent.getIntExtra("position",-1);
                // set controls
                title.setText(notesObj.getTitle());
                content.setText(notesObj.getContent());
            }
            else
            {
                Toast.makeText(this, "Null value returned", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }


    public void saveModifiedNote()
    {
        // check if title is empty or not
        if (!TextUtils.isEmpty(title.getText().toString()))
        {
            // check if object is null or not
            if(notesObj != null)
            {
                notesObj.setTitle(title.getText().toString());
                notesObj.setContent(content.getText().toString());
                notesObj.setUpdate("" + new Date());
                // create a new intent to save object and its position
                Intent dataIntent = new Intent();
                dataIntent.putExtra("NOTES CLASS", notesObj);
                dataIntent.putExtra("position", position);
                setResult(RESULT_OK, dataIntent);
                finish();
            }
            else
                Toast.makeText(this, "saveModifiedNote() returned null value", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Please enter title\nCancel this note or keep changes?");
            dialogBuilder.setPositiveButton("Ok", (dialog, which) -> { dialog.dismiss();
                Add.super.onBackPressed();
            });
            dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog altDialog = dialogBuilder.create();
            altDialog.show();
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() != R.id.save_menu)
        {
            return super.onOptionsItemSelected(item);
        }
        else
        {
            // check if no changes are made to the note or not
            if (counter == 0) {
                Add.super.onBackPressed();
                return false;
            }
            // check if title is empty or not
            if(!TextUtils.isEmpty(title.getText().toString())) {

                if(notesObj != null)
                {
                    notesObj.setTitle(title.getText().toString());
                    notesObj.setContent(content.getText().toString());
                    notesObj.setUpdate("" + new Date());
                    // create a new intent to save object and its position
                    Intent dataIntent = new Intent();
                    dataIntent.putExtra("NOTES CLASS", notesObj);
                    dataIntent.putExtra("position", position);

                    setResult(RESULT_OK, dataIntent);
                    finish();
                }
            }
            else
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                dialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    Add.super.onBackPressed();
                });

                dialogBuilder.setTitle("No Title Entered, Note not saved!\n Do you want to exit?");
                AlertDialog altDialog = dialogBuilder.create();
                altDialog.show();
            }

            return true;
        }
    }
}