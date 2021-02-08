package com.axisgroup.stage.processor.schemavalidator;

import com.streamsets.pipeline.api.Record;

import java.io.Closeable;
import java.io.IOException;

public interface DataGenerator extends Closeable {

    public void write(Record record)  throws IOException, DataGeneratorException;

    public void flush() throws IOException;

    @Override
    public void close() throws IOException;

}