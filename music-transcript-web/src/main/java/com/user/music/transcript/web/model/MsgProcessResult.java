package com.user.music.transcript.web.model;

import com.user.music.transcript.web.enums.MsgResultEnum;
import lombok.Data;

@Data
public class MsgProcessResult<T> {

    private MsgResultEnum msgResultEnum;
    private T result;

    public static <T> MsgProcessResult<T> success(){
        MsgProcessResult<T> msgProcessResult = new MsgProcessResult<T>();
        msgProcessResult.setMsgResultEnum(MsgResultEnum.SUCCESS);
        return msgProcessResult;
    }

    public static <T> MsgProcessResult<T> discard(){
        MsgProcessResult<T> msgProcessResult = new MsgProcessResult<T>();
        msgProcessResult.setMsgResultEnum(MsgResultEnum.DISCARD);
        return msgProcessResult;
    }

    public static <T> MsgProcessResult<T> retry(){
        MsgProcessResult<T> msgProcessResult = new MsgProcessResult<T>();
        msgProcessResult.setMsgResultEnum(MsgResultEnum.RETRY);
        return msgProcessResult;
    }


}
