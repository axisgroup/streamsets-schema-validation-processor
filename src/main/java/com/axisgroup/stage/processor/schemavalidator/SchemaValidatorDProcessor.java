package com.axisgroup.stage.processor.schemavalidator;

import com.streamsets.pipeline.api.*;
import com.streamsets.pipeline.api.base.configurablestage.DProcessor;

@StageDef(
        version = 1,
        label = "Schema Validator",
        description = "",
        icon = "default.png",
        onlineHelpRefUrl = ""
)
@ConfigGroups(Groups.class)
@GenerateResourceBundle
public class SchemaValidatorDProcessor extends DProcessor {


    @ConfigDef(
            required = true,
            type = ConfigDef.Type.MODEL,
            defaultValue = "/",
            label = "Input Field",
            description = "Enter field path for schema file.",
            displayPosition = 10,
            group = "SCHEMA"

    )
    @FieldSelectorModel(singleValued = true)
    public String inputField;


    @ConfigDef(
            required = true,
            type = ConfigDef.Type.MODEL,
            defaultValue = "FILE_PATH",
            label = "Schema",
            displayPosition = 50,
            group = "SCHEMA"
    )
    @ValueChooserModel(SchemaChooserValues.class)
    public SchemaDraft schemaOption;

    @ConfigDef(
            required = false,
            type = ConfigDef.Type.STRING,
            defaultValue = "${runtime:loadResource('<spec_file>', false)}",
            label = "Schema File",
            displayPosition = 70,
            dependsOn = "schemaOption",
            triggeredByValue = "FILE_PATH",
            group = "SCHEMA"
    )
    @FieldSelectorModel
    public String schemaFile;

    @ConfigDef(
            required = false,
            type = ConfigDef.Type.TEXT,
            mode = ConfigDef.Mode.JSON,
            defaultValue = "Add Spec File Def Here",
            label = "Schema",
            displayPosition = 70,
            dependsOn = "schemaOption",
            triggeredByValue = "SCHEMA",
            group = "SCHEMA"
    )
    @FieldSelectorModel
    public String schema;

    /** {@inheritDoc} */



    @Override
    protected Processor createProcessor() {
        return new SchemaValidatorProcessor(inputField, schemaOption, schema, schemaFile);
    }

}
