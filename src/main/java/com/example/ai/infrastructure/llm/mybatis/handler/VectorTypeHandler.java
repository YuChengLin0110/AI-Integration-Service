package com.example.ai.infrastructure.llm.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/*
 * 將 Java 的 float[] 或 List<Float[]> 轉成 PostgreSQL PGVector，反向讀取也自動轉回 Java 陣列
 * */
public class VectorTypeHandler extends BaseTypeHandler<float[]>{
	
	// 可以把StringBuilder 放入 TrheadLocal 重複使用，減少 new 跟 銷毀 的 GC 開銷
	// 方法中可以這樣取得 StringBuilder sb = sbThreadLocal.get();
	// 但要記得使用前要先清空 比較安全 sb.setLength(0)
//	private static final ThreadLocal<StringBuilder> sbThreadLocal = ThreadLocal.withInitial(() -> new StringBuilder());
	
	 /**
     * 將 float[] 轉換成 PostgreSQL vector 欄位可接受的格式
     */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, float[] parameter, JdbcType jdbcType)
			throws SQLException {
		// 將 float[] 轉成 PGVector 格式字串，例如 [0.12,0.34,0.56]
//		String vectorString = Arrays.toString(parameter);
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int idx = 0 ; idx < parameter.length; idx++) {
			if(idx > 0) {
				sb.append(",");
			}
			
			sb.append(parameter[idx]);
		}
		
		sb.append("]");
		
		ps.setObject(i, sb.toString(), Types.OTHER); // PGVector 使用 Types.OTHER
		
	}
	
	/**
     * 從 ResultSet 透過欄位名稱讀取 vector
     */
	@Override
	public float[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String vectorString = rs.getString(columnName);
		return parseVector(vectorString);
	}
	
	/**
     * 從 ResultSet 透過欄位索引讀取 vector
     */
	@Override
	public float[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String vectorString = rs.getString(columnIndex);
        return parseVector(vectorString);
	}
	
	/**
     * 從 CallableStatement 透過欄位索引讀取 vector（存儲過程回傳）
     */
	@Override
	public float[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String vectorString = cs.getString(columnIndex);
        return parseVector(vectorString);
	}
	
	/*
	 * 將字串 [0.12, 0.34, 0.56] 解析回 float[]
	 * */
	private float[] parseVector(String s) {
		if(s == null || s.length() < 2) {
			return new float[0];
		}
		
		// 移除首尾中括號
		s = s.substring(1, s.length() - 1);
		if(s.isEmpty()) {
			return new float[0];
		}
		
		String[] parts = s.split(",");
		float[] result = new float[parts.length];
		for(int i = 0 ; i < parts.length ; i++) {
			result[i] = Float.parseFloat(parts[i]);
		}
		
		return result;
	}

}
