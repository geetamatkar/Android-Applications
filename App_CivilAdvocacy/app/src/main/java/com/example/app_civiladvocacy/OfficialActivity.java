package com.example.app_civiladvocacy;

import java.io.Serializable;

public class OfficialActivity implements Serializable {
    private String name, party, title;

    private String photoURL = null;
    private String website = null;

    private String address = null, city = null, state = null, pincode = null ;
    private String fbLink = null, ytlink = null ,twitterLink = null;
    private String phone = null, emailId = null ;




    public OfficialActivity(String name, String title, String party, String photoURL, String fbLink, String twitterLink, String ytlink,
                            String address, String city, String state, String pincode, String phone, String emailId, String website) {
        this.name = name;
        this.title = title;
        this.party = party;
        this.photoURL = photoURL;
        this.fbLink = fbLink;
        this.twitterLink = twitterLink;
        this.ytlink = ytlink;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.phone = phone;
        this.emailId = emailId;
        this.website = website;
    }

    public OfficialActivity() {
        this.name = "Geeta";
        this.title = "Queen";
        this.party = "Correct";
        this.address = "US address";
        this.phone = "312-534-9244";
        this.emailId = "geeta@gmail.com";
        this.website = "usgov.com";
        this.fbLink = "geeta.fb.com";
        this.twitterLink = "geeta.twitter.com";
        this.ytlink = "geeta.youtube.com";
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getParty() {
        return party;
    }
    public String getWebsite() {
        return website;
    }
    public String getPhone() {
        return phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getEmail() {
        return emailId;
    }

    public String getFbAccountLink() {
        return fbLink;
    }

    public String getTwitterAccountLink() {
        return twitterLink;
    }

    public String getYoutubeAccountLink() {
        return ytlink;
    }

    public String getAddress() {
        return address;
    }
    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return pincode;
    }





    @Override
    public String toString() {
        return "Official{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", party='" + party + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", fbAccountLink='" + fbLink + '\'' +
                ", twitterLink='" + twitterLink + '\'' +
                ", ytlink='" + ytlink + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", emailId='" + emailId + '\'' +
                ", website='" + website + '\'' +
                '}';
    }


}
