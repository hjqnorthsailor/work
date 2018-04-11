package com.tmooc.work.batch;


import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

public class MyProcessor<T> extends ValidatingItemProcessor<T> {
    @Override
    public T process(T item) throws ValidationException {
        super.process(item);
        return item;
    }
}