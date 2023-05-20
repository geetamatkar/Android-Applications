package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

public class Notes implements Serializable {
    public Notes(){}
    public String title;
    public String lastUpdate, content;
    public Notes(String newTitle, String newLastUpdate, String newBody) {
       title = newTitle;
        lastUpdate = newLastUpdate;
        content = newBody;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String modifyTitle) {
        this.title = modifyTitle;
    }

    public String getUpdate() {
        return lastUpdate;
    }

    public void setUpdate(String modifyLastUpdate) {
        this.lastUpdate = modifyLastUpdate;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String modifyContent) {
        this.content = modifyContent;
    }

    @NonNull
    public String toString() {

        try
        {

            StringWriter sw = new StringWriter();
            JsonWriter jf = new JsonWriter(sw);
            jf.setIndent("  ");

            // saving notes data in json format
            jf.beginObject();
            jf.name("notesHeading").value(getTitle());
            jf.name("notesUpdate").value(getUpdate());
            jf.name("notesContent").value(getContent());
            jf.endObject();
            jf.close();

            String retStr = sw.toString();
            return retStr;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}