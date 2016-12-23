package ru.kvartira_omsk.realtorapp.dto;

/**
 * Created by Иван on 17.03.2016.
 */
public class ObjectDTO {
    public long id;
    public String titleObject;
    //private int iconID;

    public ObjectDTO() {
    }



    public ObjectDTO(String title) {
        this.titleObject = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTitle() {
        return titleObject;
    }

    public void setTitle(String title) {
        this.titleObject = title;
    }
}
