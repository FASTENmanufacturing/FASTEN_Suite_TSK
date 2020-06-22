package com.fasten.wp4.database.config;

import java.util.Properties;

import org.hibernate.usertype.DynamicParameterizedType;

import com.vladmihalcea.hibernate.type.array.internal.AbstractArrayType;
import com.vladmihalcea.hibernate.type.util.Configuration;
import com.vladmihalcea.hibernate.type.util.ParameterizedParameterType;

public class MatrixArrayType extends AbstractArrayType<int[][]> {

    public static final MatrixArrayType INSTANCE = new MatrixArrayType();

    public MatrixArrayType() {
        super(
            new MatrixArrayTypeDescriptor()
        );
    }

    public MatrixArrayType(Configuration configuration) {
        super(
            new MatrixArrayTypeDescriptor(), configuration
        );
    }

    public MatrixArrayType(Class arrayClass) {
        this();
        Properties parameters = new Properties();
        parameters.put(DynamicParameterizedType.PARAMETER_TYPE, new ParameterizedParameterType(arrayClass));
        setParameterValues(parameters);
    }

    public String getName() {
        return "int-array";
    }
}