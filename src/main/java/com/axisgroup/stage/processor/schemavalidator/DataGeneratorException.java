package com.axisgroup.stage.processor.schemavalidator;

import com.streamsets.pipeline.api.ErrorCode;
import com.streamsets.pipeline.api.StageException;

public class DataGeneratorException extends StageException {

    public DataGeneratorException(ErrorCode errorCode, Object... params) {
        super(errorCode, params);
    }

}
