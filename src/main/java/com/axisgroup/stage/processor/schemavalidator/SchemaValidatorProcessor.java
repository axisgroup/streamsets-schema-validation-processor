package com.axisgroup.stage.processor.schemavalidator;

import com.streamsets.pipeline.api.base.OnRecordErrorException;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.bazaarvoice.jolt.JsonUtils;
import com.streamsets.pipeline.api.Field;
import com.streamsets.pipeline.api.Record;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.base.SingleLaneRecordProcessor;
import com.streamsets.pipeline.api.ext.json.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.List;

public class SchemaValidatorProcessor extends SingleLaneRecordProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(SchemaValidatorProcessor.class);
    /**
     * Gives access to the UI configuration of the stage provided by the {@link SchemaValidatorDProcessor} class.
     */

    private final String inputField;
    private final String schemaFile;
    private final String schema;
    private final SchemaDraft schemaOption;
    private final Schema schemaSchema;



    public SchemaValidatorProcessor(String inputField, SchemaDraft schemaOption, String schema, String schemaFile){
        this.inputField = inputField;
        this.schemaOption = schemaOption;
        this.schema = schema;
        this.schemaFile = schemaFile;

        // Get schema
        InputStream schemaInputStream = null;
        JSONObject objSchema = null;
        if(schemaOption.toString() == "FILE_PATH"){
            try{
                schemaInputStream = new FileInputStream(schemaFile);
                objSchema = new JSONObject(new JSONTokener(schemaInputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            objSchema = new JSONObject(new JSONTokener(schema));
        }

        // Build SchemaLoader
        SchemaLoader loader = SchemaLoader.builder()
                .schemaJson(objSchema)
                .draftV7Support()
                .build();
        this.schemaSchema = loader.load().build();

    }

    //public abstract String getConfig();

    /** {@inheritDoc} */
    @Override
    protected List<ConfigIssue> init() {
        // Validate configuration values and open any required resources.
        List<ConfigIssue> issues = super.init();

        if(inputField.isEmpty()) {
            issues.add(
                    getContext().createConfigIssue(
                            Groups.SCHEMA.name(), "", Errors.SCHEMA_01)
            );
        }



//        if(schemaOption.toString() == "FILE_PATH") {
//            if (schemaFile.isEmpty()) {
//                issues.add(
//                        getContext().createConfigIssue(
//                                Groups.SCHEMA.name(), schemaFile, Errors.SCHEMA_03)
//                );
//            } else {
//                try {
//                    JsonUtils.jsonToList(schemaFile);
//                } catch (RuntimeException e) {
//                    issues.add(
//                            getContext().createConfigIssue(
//                                    Groups.SCHEMA.name(), "", Errors.SCHEMA_04, e)
//                    );
//                }
//            }
//        } else {
//            if (schema.isEmpty()) {
//                issues.add(
//                        getContext().createConfigIssue(
//                                Groups.SCHEMA.name(), schema, Errors.SCHEMA_05)
//                );
//            } else {
//                try {
//                    JsonUtils.jsonToList(schema);
//                } catch (RuntimeException e) {
//                    issues.add(
//                            getContext().createConfigIssue(
//                                    Groups.SCHEMA.name(), "", Errors.SCHEMA_06, e)
//                    );
//                }
//            }
//        }

        // If issues is not empty, the UI will inform the user of each configuration issue in the list.
        return issues;
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        // Clean up any open resources.
        super.destroy();
    }

    /** {@inheritDoc} */
    @Override
    protected void process(Record record, SingleLaneBatchMaker batchMaker) throws StageException {
        // TODO: Implement your record processing here, then add to the output batch.

        // Serialize input field
        Field field = record.get(inputField);
        Record tempRecord = getContext().createRecord(record.getHeader().getSourceId());
        tempRecord.set(field);
        Writer writer = new StringWriter();
        try (JsonCharDataGenerator generator = new JsonCharDataGenerator(getContext(), writer, Mode.MULTIPLE_OBJECTS)) {
            generator.write(tempRecord);
        } catch (IOException e) {
            //throw new OnRecordErrorException(Errors.JSON_03, e.toString(), e);
        }

        JSONObject inputJson = new JSONObject(new JSONTokener(writer.toString()));
        // Validate input against schema
       try {
            schemaSchema.validate(inputJson);
        }catch (ValidationException e) {
           throw new OnRecordErrorException(record, Errors.SCHEMA_06, e.getMessage());

        }

        batchMaker.addRecord(record);

    }

}