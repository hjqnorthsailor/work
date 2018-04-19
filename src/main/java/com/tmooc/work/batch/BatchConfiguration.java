package com.tmooc.work.batch;

import com.tmooc.work.entity.HuoYue;
import com.tmooc.work.entity.Student;
import com.tmooc.work.util.FastDFSClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;
    @Bean
    @StepScope
    public ItemReader<HuoYue> HuoYueReader(@Value("#{jobParameters['remoteFilePath']}")String remoteFilePath,
                                     @Value("#{jobParameters['localFilePath']}")String localFilePath) throws IOException {
        log.info("参数"+remoteFilePath);
        MyItemReader<HuoYue> reader = new MyItemReader(remoteFilePath,localFilePath,HuoYue.class,fastDFSClientWrapper);
        return reader;
    }
    @Bean
    public ItemWriter<HuoYue> HuoYueWriter() {
        JpaItemWriter writer = new JpaItemWriter<HuoYue>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
    @Bean
    @StepScope
    public ItemReader<Student> StudentReader(@Value("#{jobParameters['remoteFilePath']}")String remoteFilePath,
                                       @Value("#{jobParameters['localFilePath']}")String localFilePath) throws IOException {
        log.info("参数"+remoteFilePath);
        ItemReader<Student> reader = new MyItemReader(remoteFilePath,localFilePath,Student.class,fastDFSClientWrapper);
        return reader;
    }
    @Bean
    public ItemWriter<Student> StudentWriter() {
        JpaItemWriter writer = new JpaItemWriter<Student>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Validator<HuoYue> huoYueBeanValidator() {
            return new MyBeanValidator<>();
    }
    @Bean
    public ItemProcessor<HuoYue, HuoYue> huoYueProcessor(Validator<HuoYue>  huoYueBeanValidator) {
        MyProcessor<HuoYue> processor= new MyProcessor();
        processor.setValidator( huoYueBeanValidator);
        return processor;
    }
    @Bean
    public Validator<Student> studentBeanValidator() {
        return new MyBeanValidator<>();
    }
    @Bean
    public ItemProcessor<Student, Student> studentProcessor(Validator<Student>  studentBeanValidator) {
        MyProcessor<Student> processor= new MyProcessor();
        processor.setValidator(studentBeanValidator);
        return processor;
    }
    /**
     * 导入达到率表
     * @param step1
     * @param jobListener
     * @return
     */
    @Bean
    public Job importHuoYue(Step step1,MyJobListener jobListener) {
        return jobs.get("importHuoYue")
                .incrementer(new RunIdIncrementer())
                .flow(step1)//为Job指定Step
                .end()
                .listener(jobListener)//绑定监听器
                .build();
    }

    /**
     * 导入学员表
     * @param step2
     * @param jobListener
     * @return
     */
    @Bean
    public Job importStudent(Step step2,MyJobListener jobListener) {
        return jobs.get("importHuoYue")
                .incrementer(new RunIdIncrementer())
                .flow(step2)//为Job指定Step
                .end()
                .listener(jobListener)//绑定监听器
                .build();
    }
    @Bean
    protected Step step2(ItemReader<Student> studentReader,ItemProcessor<Student, Student> studentProcessor,ItemWriter<Student> studentWriter) {
        return steps.get("step2")
                .<Student, Student> chunk(100)
                .reader(studentReader)
                .processor(studentProcessor)
                .writer(studentWriter)
                //设置每个Job通过并发方式执行，一般来讲一个Job就让它串行完成的好
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                //并发任务数为 10,默认为4
//                .throttleLimit(10)
                .build();
    }
    @Bean
    protected Step step1(ItemReader<HuoYue> huoYueReader,ItemProcessor<HuoYue, HuoYue> huoYueProcessor,ItemWriter<HuoYue> huoYueWriter) {
        return steps.get("step1")
                .<HuoYue, HuoYue> chunk(100)
                .reader(huoYueReader)
                .processor(huoYueProcessor)
                .writer(huoYueWriter)
                //设置每个Job通过并发方式执行，一般来讲一个Job就让它串行完成的好
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                //并发任务数为 10,默认为4
//                .throttleLimit(10)
                .build();
    }
    @Bean
    public MyJobListener jobListener(){
        return new MyJobListener();
    }
}
