package ru.kvartira_omsk.realtorapp.dto;

/**
 * Created by Иван on 17.03.2016.
 */
public class ClientDTO {
    private String titleClient;
    //private int iconID;

    public ClientDTO(String title) {
        this.titleClient = title;
    }

    public String getTitle() {
        return titleClient;
    }

    public void setTitle(String title) {
        this.titleClient = title;
    }
}
