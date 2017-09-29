package com.housekeepingmanagingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 9/20/2017.
 */

public class TaskStatusModel implements Parcelable
{
    private String taskName;

    public  TaskStatusModel()
    {

    }
    public TaskStatusModel(Parcel in) {
        taskName = in.readString();
        taskStatus = in.readString();
    }

    public static final Creator<TaskStatusModel> CREATOR = new Creator<TaskStatusModel>() {
        @Override
        public TaskStatusModel createFromParcel(Parcel in) {
            return new TaskStatusModel(in);
        }

        @Override
        public TaskStatusModel[] newArray(int size) {
            return new TaskStatusModel[size];
        }
    };

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    private String taskStatus;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(taskName);
        parcel.writeString(taskStatus);
    }
}
