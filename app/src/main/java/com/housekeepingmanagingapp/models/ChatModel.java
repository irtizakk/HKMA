package com.housekeepingmanagingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatModel implements Parcelable
{
	//private String id;
	private String senderId;
	private String senderName;
	private String chatUid;
	private String receiverName;
	private String text;
	private String receiverId;
	private String senderImage;
	private String receiverImage;
	private String date;


	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}



//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}

	public ChatModel()
	{

	}
	public ChatModel(String chatUid , String recieverId, String recieverName, String senderId, String senderName, String text)
	{

		this.senderId = senderId;
		this.senderName = senderName;
		this.chatUid = chatUid;
		this.receiverName = recieverName;
		this.text = text;
		this.receiverId = recieverId;
	}

	public String getSenderImage()
	{
		return senderImage;
	}

	public void setSenderImage(String senderImage)
	{
		this.senderImage = senderImage;
	}

	public String getRecieverImage() {
		return receiverImage;
	}

	public void setRecieverImage(String receiverImage) {
		this.receiverImage = receiverImage;
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

	public void setChatUid(String chatUid){
		this.chatUid = chatUid;
	}

	public String getChatUid(){
		return chatUid;
	}

	public void setRecieverName(String receiverName){
		this.receiverName = receiverName;
	}

	public String getRecieverName(){
		return receiverName;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setRecieverId(String receiverId){
		this.receiverId = receiverId;
	}

	public String getRecieverId(){
		return receiverId;
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
		dest.writeString(this.receiverId);
		dest.writeString(this.receiverName);
		dest.writeString(this.senderId);
		dest.writeString(this.senderName);
		dest.writeString(this.text);
		dest.writeString(this.senderImage);
		dest.writeString(this.receiverImage);
		dest.writeString(this.date);




	}

	protected ChatModel(Parcel in)
	{
		//this.id = in.readString();
		this.chatUid = in.readString();
		this.receiverId= in.readString();
		this.receiverName = in.readString();
		this.senderId = in.readString();
		this.senderName = in.readString();
		this.text = in.readString();
		this.senderImage = in.readString();
		this.receiverImage= in.readString();
		this.date= in.readString();

	}

	public static final Parcelable.Creator<ChatModel> CREATOR = new Parcelable.Creator<ChatModel>()
	{
		@Override
		public ChatModel createFromParcel(Parcel source)
		{
			return new ChatModel(source);
		}

		@Override
		public ChatModel[] newArray(int size)
		{
			return new ChatModel[size];
		}
	};


}
