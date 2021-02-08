package com.axisgroup.stage.processor.schemavalidator;

import com.streamsets.pipeline.api.ErrorCode;
import com.streamsets.pipeline.api.GenerateResourceBundle;

@GenerateResourceBundle
public enum Errors implements ErrorCode {
    SCHEMA_01("Input Field cannot be null"),
    SCHEMA_03("Spec File '{}'  does not exists"),
    SCHEMA_04("Invalid spec file format '{}'"),
    SCHEMA_05("Schema '{}'  is empty"),
    SCHEMA_06("Invalid spec format '{}'"),
    JSON_GENERATOR_00("Record '{}' root field should be List instead of '{}'"),
    JSON_GENERATOR_01("Record Field of type FileRef is unsupported for JSON serialization")
    ;

    private final String msg;
    Errors(String msg) {
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
