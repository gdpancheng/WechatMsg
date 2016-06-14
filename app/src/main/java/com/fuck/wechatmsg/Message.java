package com.fuck.wechatmsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fuck.wechatmsg.ProtobufRawReader.Parser;
import com.google.protobuf.CodedInputStream;

public class Message {
	private String username;
	
	private String nickname;
	
	private long time;
	
	private String msg;
	
	private List<String> pics;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<String> getPics() {
		return pics;
	}

	public void setPics(List<String> pics) {
		this.pics = pics;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public static Message parse(byte[] contentData,byte[] attrBuf){
		final Message msg = ProtobufRawReader.read(contentData,new Parser<Message>() {
			
			private Message messgae = new Message();
			
			public boolean read(CodedInputStream in, int index, int type) throws Exception {
				
				if(index==4){
					messgae.setTime(in.readInt64());
				}else if(index==5){
					messgae.setMsg(in.readString());
				}else if(index==8){
					messgae.setPics(pics(in.readBytes().toByteArray()));
					return false;
				}else{
					ProtobufRawReader.skip(in, type);
				}
				
				return true;
			}
			
			public Message get() {
				return messgae;
			}
		});
		
		ProtobufRawReader.read(attrBuf, new Parser<Void>(){
			public boolean read(CodedInputStream in, int index, int type) throws Exception {
				if(index==2){
					msg.setUsername(in.readString());
				}else if(index==3){
					msg.setNickname(in.readString());
					return false;
				}else{
					ProtobufRawReader.skip(in, type);
				}
				
				return true;
			}

			public Void get() {
				return null;
			}
			
		});
		
		return msg;
	}
	
	private static List<String> pics(byte[] data){
		return ProtobufRawReader.read(data,new Parser<List<String>>() {
			private List<String> list = new ArrayList<String>();
					
			public boolean read(CodedInputStream in, int index, int type) throws Exception {
				if(index==2){
					in.readInt32();
				}else if(index==5){
					list.add(getPic(in.readBytes().toByteArray()));
				}else if(index>5){
					return false;
				}else{
					ProtobufRawReader.skip(in, type);
				}
				return true;
			}
			
			public List<String> get() {
				return list;
			}
		});
		
	}
	
	private static String getPic(byte[] data){
		return ProtobufRawReader.read(data,new Parser<String>() {
			private String pic;

			public boolean read(CodedInputStream in, int index, int type) throws Exception {
				if(index==4){
					pic = in.readString();
				}else{
					ProtobufRawReader.skip(in, type);
				}
				return true;
			}
			
			public String get() {
				return pic;
			}
			
		});
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		StringBuilder sb = new StringBuilder();
		sb.append("["+sdf.format(new Date(time*1000))+"]");
		sb.append(" ["+username+"|"+nickname+"] ");
		sb.append(msg);
		if(pics!=null){
			for(String pic : pics) {
				sb.append(pic+",");
			}
		}

		return sb.toString();
	}
	
}
