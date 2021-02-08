package com.axisgroup.stage.processor.schemavalidator;

import com.streamsets.pipeline.api.base.BaseEnumChooserValues;

public class SchemaChooserValues extends BaseEnumChooserValues {
    public SchemaChooserValues(){
        super(SchemaDraft.class);
    }
}
