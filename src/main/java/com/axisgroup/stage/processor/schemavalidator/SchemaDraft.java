package com.axisgroup.stage.processor.schemavalidator;

import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.Label;

@GenerateResourceBundle
public enum SchemaDraft implements  Label{

    FILE_PATH("Schema File Location"),
    SCHEMA("Add Schema Directly"),
    ;

    private final String label;

    SchemaDraft(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
