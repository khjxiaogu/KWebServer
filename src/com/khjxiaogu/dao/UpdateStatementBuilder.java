package com.khjxiaogu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

public class UpdateStatementBuilder implements InputStatement<UpdateStatementBuilder> {
	String table;
	Connection conn;

	public UpdateStatementBuilder(String table, Connection conn) {
		this.table = table;
		this.conn = conn;
	}

	class UpdateExpr {
		String key = null;
		Object val;
		String expr = "?";

		public UpdateExpr(String expr) { this.expr = expr; }

		public UpdateExpr(String key, Object val) {
			this.key = key;
			this.val = val;
		}

		public UpdateExpr(String key, Object val, String expr) {
			this.key = key;
			this.val = val;
			this.expr = expr;
		}
	}

	protected ArrayList<UpdateExpr> inserts = new ArrayList<>();

	@Override
	public UpdateStatementBuilder set(String key, Object val) {
		inserts.add(new UpdateExpr(key, val));
		return this;
	}

	public UpdateStatementBuilder set(String expr) {
		inserts.add(new UpdateExpr(expr));
		return this;
	}

	public UpdateStatementBuilder set(String key, Object val, String expr) {
		inserts.add(new UpdateExpr(key, val, expr));
		return this;
	}
	@Override
	public String getSQL() {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(table);
		sql.append(" SET ");
		Iterator<UpdateExpr> it = inserts.iterator();
		if (it.hasNext())
			while (true) {
				UpdateExpr expr = it.next();
				if (expr.key != null) { sql.append(expr.key); sql.append(" = "); }
				sql.append(expr.expr);
				if (it.hasNext())
					sql.append(", ");
				else
					break;
			}
		return sql.toString();
	}
	@Override
	public boolean execute() {
		try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
			int len = inserts.size();
			for (int i = 0; i < len; i++)
				ps.setObject(i, inserts.get(i).val);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


}
