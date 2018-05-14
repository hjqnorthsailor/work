package com.tmooc.work.util;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper=new ObjectMapper();
    static {
        //DeserializationConfig反序列化相关配置属性
        //设置忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        //序列化对象所需配置
        //  是否允许一个类型没有注解表明打算被序列化。默认true，抛出一个异常；否则序列化一个空对象，比如没有任何属性。
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
    }
    public static <T> String obj2String(T src){
        if (src==null){
            return  null;
        }
        try {
            return src instanceof String? (String)src:objectMapper.writeValueAsString(src);
        } catch (IOException e) {
            log.warn("解析对象为字符串时出错,error:{}",e);
            return null;
        }
    }
    public static <T> T string2Obj(String src, TypeReference<T> tTypeReference){
        if (src==null||tTypeReference==null){
            return  null;
        }
        try {
            return (T)(tTypeReference.getType().equals(String.class)?src:objectMapper.readValue(src,tTypeReference));
        } catch (IOException e) {
            log.warn("解析字符串为对象时出错,String:{},TypeReference:{},error:{}",src,tTypeReference.getType(),e);
            return null;
        }
    }
}
