package com.zjhz.teacher.bean;

import java.io.Serializable;
public class Message  implements Serializable {

	//消息类型，// 1表示群发消息   // 2危险区域    //3出入校   4会议通知
	private int msgNature;

	public int getMsgNature() {
		return msgNature;
	}

	public void setMsgNature(int msgNature) {
		this.msgNature = msgNature;
	}

	private String createTime = "";
	//消息群发
	private String msgId = "";
	private String title = "";
	private String content = "";
	private int status = -1;//是否已
	private String linkId = "";
	private String nickName = "";

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	//会议通知
//	private String meetId = "";
//	private String meetingContent = "";
//	private String meetingTitle = "";
//	private int isTake = -1;

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

//	public int getIsTake() {
//		return isTake;
//	}
//
//	public void setIsTake(int isTake) {
//		this.isTake = isTake;
//	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}
