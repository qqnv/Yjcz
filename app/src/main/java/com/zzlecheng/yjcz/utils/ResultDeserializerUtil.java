package com.zzlecheng.yjcz.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.zzlecheng.yjcz.base.BaseBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 说明：自定义映射实体类
 */

public class ResultDeserializerUtil implements JsonDeserializer<BaseBean<?>> {

    @Override
    public BaseBean<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        BaseBean baseBean = new BaseBean<>();
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.get("code") != null) {
                baseBean.setCode(jsonObject.get("code").getAsString());
            }
            if (jsonObject.get("msg") != null) {
                baseBean.setMsg(jsonObject.get("msg").getAsString());
            }

            Type itemType;
            try {
                itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                if (jsonObject.get("data") != null) {
                    baseBean.setData(context.deserialize(jsonObject.get("data"), itemType));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return baseBean;
        }
        return baseBean;

    }
}