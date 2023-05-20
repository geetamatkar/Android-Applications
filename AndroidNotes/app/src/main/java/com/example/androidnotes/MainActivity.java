package com.example.androidnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private RecyclerView recyclerView;
    private final List<Notes> allNotes = new ArrayList<>();
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::getNotesListResult);
        recyclerView = findViewById(R.id.recycler_main);
        // get list of notes saved from last session as JSON file
        getNotesFromJSON();
    }

    @Override
    public void onClick(View view) {
        int notePosition; // stores the position of the note selected
        Notes notesAtPos;
        notePosition = recyclerView.getChildLayoutPosition(view);
        notesAtPos = allNotes.get(notePosition);

        if (notesAtPos != null)
        {
            Intent dataIntent = new Intent(this, Add.class);
            dataIntent.putExtra("NOTES CLASS", notesAtPos);
            dataIntent.putExtra("position", notePosition);

            // launch activity
            resultLauncher.launch(dataIntent);
        }
    }

    @Override
    public boolean onLongClick(View view)
    {
        int notePosition;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // get the position of the note selected
        notePosition = recyclerView.getChildLayoutPosition(view);

        // display the message and get response from user
        dialogBuilder.setTitle("Delete this note?");
        AlertDialog dlg = dialogBuilder.create();
        dlg.show();

        // if user clicks No, then dismiss
        dialogBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        // if user clicks Yes, then remove selected notes from list and reset recycle view
        dialogBuilder.setPositiveButton("Yes", (dialog, which) -> { dialog.dismiss();
            allNotes.remove(notePosition);
            setRecycleView();
            WriteJson();
        });

        return false;
    }

    private void getNotesFromJSON(){
        try
        {
            StringBuilder stringBuilder = new StringBuilder();
            String str;
            // read the json file using input stream and buffer reader
            InputStream inStreamFile = getApplicationContext().openFileInput("AndroidNotes.json");
            InputStreamReader inputFileReader = new InputStreamReader(inStreamFile, StandardCharsets.UTF_8);
            BufferedReader bufferReaderFile = new BufferedReader(inputFileReader);

            if(bufferReaderFile != null)
            {
                while ((str = bufferReaderFile.readLine()) != null) stringBuilder.append(str);
            }
            else
                return;
            JSONArray jsonItems = new JSONArray(stringBuilder.toString());
            int len = jsonItems.length();
            for (int i = 0; i < len; i++) {
                JSONObject obJson = jsonItems.getJSONObject(i);

                // fetch the data using id references
                String strBody = obJson.getString("notesContent");
                String strDate = obJson.getString("notesDate");
                String strTitle = obJson.getString("notesTitle");

                Notes notesObjLine = new Notes(strTitle, strDate, strBody);
                if(notesObjLine != null)
                    allNotes.add(notesObjLine);
                else
                {
                    Toast.makeText(getApplicationContext(), "Null notes object", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            // finally update the current view with the latest changes
            setRecycleView();
        }
        catch (Exception e) {
            e.printStackTrace();
            allNotes.clear();

            setRecycleView();
        }
    }

    public void setRecycleView()
    {
        try
        {
            int totalNotes = allNotes.size();
            setTitle("AndroidNotes (" + totalNotes + ")"); // title with number of existing notes

            // Collections used for sorting the notes according to the last modified date and time
            Collections.sort(allNotes, (notesOne, notesTwo) -> {
                if (notesOne.getUpdate() == null || notesTwo.getUpdate() == null)
                    return 0;


                return notesOne.getUpdate().compareTo(notesTwo.getUpdate());
            });

            // To view in descending order, reverse the collection
            Collections.reverse(allNotes);

            // using adapter class, call the constructor and set the adaptor.
            NotesListAdapter nListAdapter = new NotesListAdapter(allNotes, this);
            recyclerView.setAdapter(nListAdapter);

            // setting of layout manager for recycler view
            LinearLayoutManager linearLayoutMngr = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutMngr);
        }
        catch(Exception ex)
        {
            Toast.makeText(this, "SetRecycleView() Exception ", Toast.LENGTH_SHORT).show();
            ex.getStackTrace();
        }
    }


    private void getNotesListResult(ActivityResult activityResult)
    {
        try
        {
            int position;
            Intent intentD = activityResult.getData();
            // checking for null condition
            if (activityResult == null || activityResult.getData() == null) {
                return;
            }

            if (activityResult.getResultCode() != RESULT_OK) {
                return;
            }

            position = intentD.getIntExtra("position",-1);


            Notes notesObj = (Notes) intentD.getSerializableExtra("NOTES CLASS");

            if (notesObj == null) {
                return;
            }
            // if the object in the list not set for position then set new Notes obj for that position else add the object at the end
            Notes newNotes;
            newNotes = new Notes(notesObj.getTitle(), notesObj.getUpdate(), notesObj.getContent());
            if (position != -1)
            {
                allNotes.set(position, newNotes);
            }
            else
            {
                allNotes.add(newNotes);
            }

            WriteJson();   // Saving notes in the json file
        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
    }

    private void WriteJson()
    {
        try {
            // Save the notes list in the json file using file output stream
            FileOutputStream fileOutputStr = getApplicationContext().openFileOutput("AndroidNotes.json", Context.MODE_PRIVATE);

            // printwriterapi to write to the file
            PrintWriter printWriter = new PrintWriter(fileOutputStr);
            printWriter.print(allNotes);
            printWriter.close();
            fileOutputStr.close();
            setRecycleView();
        }
        catch(Exception ex)
        {
            ex.getStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int idMenu;
        Intent intent;
        idMenu = item.getItemId();

        if (idMenu == R.id.info)
        {
            // get the activity of the about menu to launch that activity
            intent = new Intent(this, About.class);
            startActivity(intent);

            return true;
        }
        else if (idMenu == R.id.add)
        {
            intent = new Intent(this, Add.class);
            resultLauncher.launch(intent);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}