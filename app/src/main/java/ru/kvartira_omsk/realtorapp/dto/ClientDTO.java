package ru.kvartira_omsk.realtorapp.dto;

/**
 * Created by Иван on 17.03.2016.
 */
public class ClientDTO {
    public long id;
    public String titleClient;
    public String typeClient;
    //private int iconID;

    public ClientDTO() {
    }



    public ClientDTO(String title) {
        this.titleClient = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTitle() {
        return titleClient;
    }

    public void setTitle(String title) {
        this.titleClient = title;
    }

    public String getTypeClient() { return typeClient; }

    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }
}
