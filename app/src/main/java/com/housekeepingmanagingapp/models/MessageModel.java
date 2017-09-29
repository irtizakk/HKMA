package com.housekeepingmanagingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageModel implements Parcelable
{
    private String date;
	private String senderId;
	private String senderName;
	private boolean read;
	private String chatUid;
	private String imageUrl;
	private String recieverName;
	private String text;
	private String recieverId;

	public String getImageUrl()
{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	public MessageModel()
	{

	}
	public MessageModel(String chatUid, String imageUrl , String date, boolean read, String recieverId, String recieverName, String senderId, String senderName, String text)
	{

		this.senderId = senderId;
		this.senderName = senderName;
		this.imageUrl = imageUrl;
		this.chatUid = chatUid;
		this.date = date;
		this.read = read;
		this.recieverName = recieverName;
		this.text = text;
		this.recieverId = recieverId;
	}

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setSenderId(String senderId){
		this.senderId = senderId;
	}

	public String getSenderId(){
		return senderId;
	}

	public void setSenderName(String senderName){
		this.senderName = senderName;
	}

	public String getSenderName(){
		return senderName;
	}

	public void setRead(boolean read){
		this.read = read;
	}

	public boolean isRead(){
		return read;
	}

	public void setChatUid(String chatUid){
		this.chatUid = chatUid;
	}

	public String getChatUid(){
		return chatUid;
	}

	public void setRecieverName(String recieverName){
		this.recieverName = recieverName;
	}

	public String getRecieverName(){
		return recieverName;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){

		return text;
	}

	public void setRecieverId(String recieverId){
		this.recieverId = recieverId;
	}

	public String getRecieverId(){
		return recieverId;
	}



	@Override
	public int describeContents()
	{
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		//dest.writeString(this.id);
		dest.writeString(this.chatUid);
		dest.writeString(this.date);
		dest.writeString(this.recieverId);
		dest.writeString(this.recieverName);
		dest.writeString(this.senderId);
		dest.writeString(this.senderName);
		dest.writeString(this.text);




	}

	protected MessageModel(Parcel in)
	{
		//this.id = in.readString();
		this.chatUid = in.readString();
		this.date = in.readString();
		this.recieverId = in.readString();
		this.recieverName = in.readString();
		this.senderId = in.readString();
		this.senderName = in.readString();
		this.text = in.readString();

	}

	public static final Parcelable.Creator<MessageModel> CREATOR = new Parcelable.Creator<MessageModel>()
	{
		@Override
		public MessageModel createFromParcel(Parcel source)
		{
			return new MessageModel(source);
		}

		@Override
		public MessageModel[] newArray(int size)
		{
			return new MessageModel[size];
		}
	};
}
