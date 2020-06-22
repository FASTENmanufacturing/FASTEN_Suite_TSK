package com.fasten.wp4.database.config;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL95Dialect;

import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;

public class PostgreSQL95ArrayAndJsonDialect extends PostgreSQL95Dialect {

	public PostgreSQL95ArrayAndJsonDialect() {
		super();
		this.registerColumnType(Types.ARRAY, "array");
		this.registerHibernateType(Types.OTHER, JsonNodeBinaryType.class.getName());
	}
}
