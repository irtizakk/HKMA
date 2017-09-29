package com.housekeepingmanagingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 7/1/2017.
 */

public class ManagerModel implements Parcelable
{

 private String contactNumber;

 private String createdByAdmin;
 private String date;
 private String email;
 private String fullName;

private String locationId;

 private String locationName;
 private String password;
 private String   profilePic;

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    public ManagerModel()
    {

    }


    public String getCreatedByAdmin() {
        return createdByAdmin;
    }

    public void setCreatedByAdmin(String createdByAdmin) {
        this.createdByAdmin = createdByAdmin;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }





    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        if(profilePic == null || profilePic.equals(""))
        {
            profilePic = "";
            return profilePic;
        }
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {

        dest.writeString(this.email);
        dest.writeString(this.profilePic);
        dest.writeString(this.userId);
        dest.writeString(this.password);
        dest.writeString(this.createdByAdmin);
        dest.writeString(this.date);
        dest.writeString(this.contactNumber);
        dest.writeString(this.fullName);
        dest.writeString(this.locationId);
        dest.writeString(this.locationName);
    }

    protected ManagerModel(Parcel in)
    {
        //this.categories = in.createTypedArrayList(CategoryModel.CREATOR);
        //this.RSVP = in.createStringArrayList();
        this.email = in.readString();
        this.profilePic = in.readString();
        this.userId = in.readString();
        this.password = in.readString();
        this.createdByAdmin = in.readString();
        this.date = in.readString();
        this.contactNumber = in.readString();
        this.fullName = in.readString();
        this.locationId = in.readString();
        this.locationName = in.readString();
    }

    public static final Creator<ManagerModel> CREATOR = new Creator<ManagerModel>()
    {
        @Override
        public ManagerModel createFromParcel(Parcel source)
        {
            return new ManagerModel(source);
        }

        @Override
        public ManagerModel[] newArray(int size)
        {
            return new ManagerModel[size];
        }
    };
}
