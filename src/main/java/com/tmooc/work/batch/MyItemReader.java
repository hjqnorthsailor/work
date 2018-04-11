package com.tmooc.work.batch;

import com.tmooc.work.util.FastDFSClientWrapper;
import com.tmooc.work.util.MyExcelUtils;
import com.tmooc.work.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.springframework.batch.item.*;

import java.io.IOException;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 从fastdfs下载excel并解析读取
 */
@Slf4j
public class MyItemReader<T> implements ItemReader<T>, ItemStream {
    private List<T> items;
    private Class<T> clazz;
    private int currentIndex = 0;
    private static final String CURRENT_INDEX = "current.index";
    public MyItemReader(String remoteFilePath,String localFilePath,FastDFSClientWrapper fastDFSClientWrapper) throws IOException {
        clazz=ReflectUtils.getClassGenricType(getClass());
        System.out.println(clazz.getSimpleName());
        init(remoteFilePath,localFilePath,fastDFSClientWrapper);
    }
    public T newInstance(){
        T newInstance=null;
        Class<T> entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return newInstance;
    }
    public void init(String remoteFilePath,String localFilePath,FastDFSClientWrapper fastDFSClientWrapper) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String filePath = localFilePath + remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);//本地文件名
        boolean isDowload = fastDFSClientWrapper.executeDownloadFile(httpClient, remoteFilePath, filePath, "UTF-8", true);
        if (isDowload) {
            items = MyExcelUtils.importExcel(filePath, 0, 1,clazz);
            System.out.println(items.size());
        }
    }
    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (currentIndex < items.size()) {
            return items.get(currentIndex++);
        }
        return null;
    }

    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if(executionContext.containsKey(CURRENT_INDEX)){
            currentIndex = new Long(executionContext.getLong(CURRENT_INDEX)).intValue();
        }
        else{
            currentIndex = 0;
        }
    }

    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putLong(CURRENT_INDEX, new Long(currentIndex).longValue());
    }

    public void close() throws ItemStreamException {}
}