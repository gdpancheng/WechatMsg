package com.fuck.wechatmsg;

import com.google.protobuf.CodedInputStream;

public class ProtobufRawReader {
	private CodedInputStream in;
	
	private ProtobufRawReader(byte[] data) {
		in = CodedInputStream.newInstance(data);
	}
	
	public void read(Parser<?> parser){
		try{
			while(!in.isAtEnd()){
				int tag = in.readTag();
				int index = tag>>3;
				int type = tag&0x7;
				
				if(!parser.read(in, index, type)){
					break;
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public static <T> T read(byte[] data,Parser<T> parser){
		new ProtobufRawReader(data).read(parser);
		return parser.get();
	}
	
	public static void skip(CodedInputStream in,int type) throws Exception{
		@SuppressWarnings("unused")
		Object val = null;
		switch(type){
			case 0:
				val = in.readInt32();break;
			case 1:
				val = in.readRawVarint64();break;
			case 2:
				val = in.readString();break;
			case 3:
			case 4:
				val = in.readString();break;
			case 5:
				val = in.readFloat();
		}
	}
	
	public static interface Parser<T>{
		public boolean read(CodedInputStream in, int index, int type) throws Exception;
		
		public T get();
	}
}
