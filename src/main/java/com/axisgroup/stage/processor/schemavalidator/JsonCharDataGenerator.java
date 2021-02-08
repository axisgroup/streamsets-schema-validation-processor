package com.axisgroup.stage.processor.schemavalidator;

//import com.google.common.annotations.VisibleForTesting;

import com.streamsets.pipeline.api.ProtoConfigurableEntity;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.ext.ContextExtensions;
import com.streamsets.pipeline.api.ext.JsonRecordWriter;
import com.streamsets.pipeline.api.ext.json.Mode;

import java.io.IOException;
import java.io.Writer;

//import com.streamsets.pipeline.lib.generator.DataGenerator;
//import com.streamsets.pipeline.lib.generator.DataGeneratorException;

public class JsonCharDataGenerator implements DataGenerator {

    private final JsonRecordWriter recordWriter;
    private final Mode mode;

    public JsonCharDataGenerator(ProtoConfigurableEntity.Context context, Writer writer, Mode mode) throws IOException {
        this.mode = mode;
        ContextExtensions ext = ((ContextExtensions) context);
        recordWriter = ext.createJsonRecordWriter(writer, mode);
    }

    //@VisibleForTesting
    boolean isArrayObjects() {
        return mode == Mode.ARRAY_OBJECTS;
    }

    @Override
    public void write(Record record) throws IOException, DataGeneratorException {
        try {
            recordWriter.write(record);
        } catch (IOException e) {
            if (e.getMessage().contains("FileRef")) {
                throw new DataGeneratorException(Errors.JSON_GENERATOR_01);
            } else {
                throw e;
            }
        }
    }

    @Override
    public void flush() throws IOException {
        recordWriter.flush();
    }

    @Override
    public void close() throws IOException {
        recordWriter.close();
    }
}