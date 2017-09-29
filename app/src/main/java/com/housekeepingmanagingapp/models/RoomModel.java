package com.housekeepingmanagingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by hp on 7/3/2017.
 */

public class RoomModel implements Parcelable
{
   private String createdByAdmin;
   private String  date;
    private String  roomFloorNumber;
    private String  roomId;

    private String  roomNumber;


    private String  roomType;


    public  RoomModel()
    {

    }

    public String getCreatedByAdmin() {
        return createdByAdmin;
    }

    public void setCreatedByAdmin(String createdByAdmin) {
        this.createdByAdmin = createdByAdmin;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getRoomFloorNumber() {
        return roomFloorNumber;
    }

    public void setRoomFloorNumber(String roomFloorNumber) {
        this.roomFloorNumber = roomFloorNumber;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }


    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }



    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {

        dest.writeString(this.roomId);
        dest.writeString(this.roomFloorNumber);
        dest.writeString(this.roomType);
        dest.writeString(this.roomNumber);
        dest.writeString(this.createdByAdmin);
    }

    protected RoomModel(Parcel in)
    {
        //this.categories = in.createTypedArrayList(CategoryModel.CREATOR);
        //this.RSVP = in.createStringArrayList();
        this.roomId = in.readString();
        this.roomFloorNumber = in.readString();
        this.roomType = in.readString();
        this.roomNumber = in.readString();
        this.createdByAdmin = in.readString();

    }

    public static final Parcelable.Creator<RoomModel> CREATOR = new Parcelable.Creator<RoomModel>()
    {
        @Override
        public RoomModel createFromParcel(Parcel source)
        {
            return new RoomModel(source);
        }

        @Override
        public RoomModel[] newArray(int size)
        {
            return new RoomModel[size];
        }
    };
}

