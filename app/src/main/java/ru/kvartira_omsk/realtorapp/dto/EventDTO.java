package ru.kvartira_omsk.realtorapp.dto;

/**
 * Created by Иван on 17.03.2016.
 */
public class EventDTO {
    public long id;
    public String titleEvent;
    //private int iconID;

    public EventDTO() {
    }



    public EventDTO(String title) {
        this.titleEvent = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTitle() {
        return titleEvent;
    }

    public void setTitle(String title) {
        this.titleEvent = title;
    }
}
