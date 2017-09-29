package com.housekeepingmanagingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hp on 9/20/2017.
 */

public class TaskModel implements Parcelable
{
    private ArrayList<TaskStatusModel> taskArray = new ArrayList<>();
    private String taskId;
    private String housekeeperId;
    private String roomId;
    private String roomNo;
    private String roomType;
    private String inspectionComment;
    private String descriptionComment;
    private String roomFloor;
    private String housekeeperName;
    private String taskCreatedDate ;
    private String taskAssignDate ;
    private String locationId;
    private String createdByAdmin;
    private String managerId;
    private String locationName;
    private String taskMainStatus;
    private String taskViewByHouseKeeper;

    public String getLostAndFoundItemsDescription()
    {
        if(lostAndFoundItemsDescription == null)
        {
            return "";
        }
        return lostAndFoundItemsDescription;
    }

    public void setLostAndFoundItemsDescription(String lostAndFoundItemsDescription)
    {
        this.lostAndFoundItemsDescription = lostAndFoundItemsDescription;
    }

    public ArrayList<String> getLostAndFoundItemsImages()
    {
        if(lostAndFoundItemsImages == null)
        {
            return new ArrayList<>();
        }
        return lostAndFoundItemsImages;
    }

    public void setLostAndFoundItemsImages(ArrayList<String> lostAndFoundItemsImages) {
        this.lostAndFoundItemsImages = lostAndFoundItemsImages;
    }

    private String lostAndFoundItemsDescription;
    private ArrayList<String> lostAndFoundItemsImages = new ArrayList<>();

    public String getHousekeeperTaskRating() {
        if(housekeeperTaskRating.equals("") || housekeeperTaskRating == null)
        {
            return  housekeeperTaskRating = "0.0";
        }
        return housekeeperTaskRating;
    }

    public void setHousekeeperTaskRating(String housekeeperTaskRating) {
        this.housekeeperTaskRating = housekeeperTaskRating;
    }

    private String housekeeperTaskRating;

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    private String managerName;



    public String getDescriptionComment() {
        return descriptionComment;
    }

    public void setDescriptionComment(String descriptionComment) {
        this.descriptionComment = descriptionComment;
    }

    public String getInspectionComment() {
        return inspectionComment;
    }

    public void setInspectionComment(String inspectionComment) {
        this.inspectionComment = inspectionComment;
    }



    public String getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(String roomFloor) {
        this.roomFloor = roomFloor;
    }


    public String getTaskViewByHouseKeeper()
    {
        return taskViewByHouseKeeper;
    }

    public void setTaskViewByHouseKeeper(String taskViewByHouseKeeper)
    {
        this.taskViewByHouseKeeper = taskViewByHouseKeeper;
    }


    public TaskModel()
    {

    }
        public TaskModel(Parcel in)
    {
        taskArray= in.createTypedArrayList(TaskStatusModel.CREATOR);
        taskId = in.readString();
        housekeeperId = in.readString();
        roomId = in.readString();
        roomFloor = in.readString();
        roomNo = in.readString();
        roomType = in.readString();
        housekeeperName = in.readString();
        taskCreatedDate = in.readString();
        taskAssignDate = in.readString();
        locationId = in.readString();
        createdByAdmin = in.readString();
        managerId = in.readString();
        locationName = in.readString();
        taskMainStatus = in.readString();
        taskViewByHouseKeeper = in.readString();
        descriptionComment = in.readString();
        inspectionComment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeList(taskArray);
        dest.writeString(taskId);
        dest.writeString(housekeeperId);
        dest.writeString(roomId);
        dest.writeString(roomFloor);
        dest.writeString(roomNo);
        dest.writeString(roomType);
        dest.writeString(housekeeperName);
        dest.writeString(taskCreatedDate);
        dest.writeString(taskAssignDate);
        dest.writeString(locationId);
        dest.writeString(createdByAdmin);
        dest.writeString(managerId);
        dest.writeString(locationName);
        dest.writeString(taskMainStatus);
        dest.writeString(taskViewByHouseKeeper);
        dest.writeString(descriptionComment);
        dest.writeString(inspectionComment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TaskModel> CREATOR = new Creator<TaskModel>() {
        @Override
        public TaskModel createFromParcel(Parcel in) {
            return new TaskModel(in);
        }

        @Override
        public TaskModel[] newArray(int size) {
            return new TaskModel[size];
        }
    };

    public String getTaskCreatedDate() {
        return taskCreatedDate;
    }

    public void setTaskCreatedDate(String taskCreatedDate) {
        this.taskCreatedDate = taskCreatedDate;
    }

    public ArrayList<TaskStatusModel> getTaskArray() {
        return taskArray;
    }

    public void setTaskArray(ArrayList<TaskStatusModel> taskArray) {
        this.taskArray = taskArray;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getHousekeeperId() {
        return housekeeperId;
    }

    public void setHousekeeperId(String housekeeperId) {
        this.housekeeperId = housekeeperId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getHousekeeperName() {
        return housekeeperName;
    }

    public void setHousekeeperName(String housekeeperName) {
        this.housekeeperName = housekeeperName;
    }

    public String getTaskAssignDate() {
        return taskAssignDate;
    }

    public void setTaskAssignDate(String taskAssignDate) {
        this.taskAssignDate = taskAssignDate;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getCreatedByAdmin() {
        return createdByAdmin;
    }

    public void setCreatedByAdmin(String createdByAdmin) {
        this.createdByAdmin = createdByAdmin;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTaskMainStatus() {
        return taskMainStatus;
    }

    public void setTaskMainStatus(String taskMainStatus) {
        this.taskMainStatus = taskMainStatus;
    }





}
