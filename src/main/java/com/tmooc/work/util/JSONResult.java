package com.tmooc.work.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义响应结构
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JSONResult implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1095372491391467494L;

	// 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    public static JSONResult ok(Object data) {
        return new JSONResult(data);
    }

    public static JSONResult ok() {
        return new JSONResult(null);
    }

    public static JSONResult error(CodeMsg codeMsg){
        return new JSONResult(codeMsg,null);
    }

    public JSONResult(CodeMsg codeMsg, Object data) {
        this.status = codeMsg.getStatus();
        this.msg = codeMsg.getMsg();
        this.data = data;
    }

    public JSONResult(Object data) {
        this.status = CodeMsg.SUCCESS.getStatus();
        this.msg =CodeMsg.SUCCESS.getMsg();
        this.data = data;
    }

    /**
     * 将json结果集转化为JSONResult对象
     *
     * @param jsonData json数据
     * @param clazz JSONResult中的object类型
     * @return
     */
    public static JSONResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, JSONResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return builder().status(jsonNode.get("status").intValue()).msg(jsonNode.get("msg").asText()).data(obj).build();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static JSONResult format(String json) {
        try {
            return MAPPER.readValue(json, JSONResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object是集合转化
     *
     * @param jsonData json数据
     * @param clazz 集合中的类型
     * @return
     */
    public static JSONResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return builder().status(jsonNode.get("status").intValue()).msg(jsonNode.get("msg").asText()).data(obj).build();
        } catch (Exception e) {
            return null;
        }
    }

}
