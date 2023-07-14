package com.travel.travellingbug.models;

public class ChatAppMsgDTO {
    public final static String MSG_TYPE_SENT = "MSG_TYPE_SENT";

    public final static String MSG_TYPE_RECEIVED = "MSG_TYPE_RECEIVED";
    private String msgTime;
    // Message content.
    private String msgContent;
    // Message type.
    private String msgType;
    private String msgId;

    public ChatAppMsgDTO(String msgTime, String msgContent, String msgType, String msgId) {
        this.msgTime = msgTime;
        this.msgContent = msgContent;
        this.msgType = msgType;
        this.msgId = msgId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public ChatAppMsgDTO(String msgType, String msgContent, String msgTime) {
        this.msgType = msgType;
        this.msgContent = msgContent;
        this.msgTime = msgTime;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
