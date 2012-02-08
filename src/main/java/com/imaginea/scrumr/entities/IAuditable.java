package com.imaginea.scrumr.entities;

import java.util.Date;

public interface IAuditable {

    public String getCreator();

    public void setCreator(String inUserId);

    public Date getCreated();

    public void setCreated(Date inCreatedDate);

    public String getChanger();

    public void setChanger(String inUserId);

    public Date getChanged();

    public void setChanged(Date inChangedDate);
    
}
