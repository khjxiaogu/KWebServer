/**
 * KWebserver
 * Copyright (C) 2021  khjxiaogu
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
		String expr = "";

		public UpdateExpr(String key) { this.expr = key+" = ?"; }

		public UpdateExpr(String key, String expr) {
			if(key!=null)
				this.expr = key+" = ";
			if(expr!=null)
				this.expr += expr;
			else
				this.expr += "?";
		}
	}

	protected ArrayList<UpdateExpr> inserts = new ArrayList<>();
	@Override
	public UpdateStatementBuilder set(String key) {
		inserts.add(new UpdateExpr(key));
		return this;
	}
	public UpdateStatementBuilder set(String key, String expr) {
		inserts.add(new UpdateExpr(key, expr));
		return this;
	}

	@Override
	public String getSQL() {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(table);
		sql.append(" SET ");
		Iterator<UpdateExpr> it = inserts.iterator();
		if (it.hasNext()) {
			while (true) {
				UpdateExpr expr = it.next();
				sql.append(expr.expr);
				if (it.hasNext()) {
					sql.append(", ");
				} else {
					break;
				}
			}
		}
		return sql.toString();
	}

	@Override
	public boolean execute() {
		try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public PreparedStatement build() throws SQLException {
		return conn.prepareStatement(getSQL());
	}
	@Override
	public boolean execute(Object[] data) {
		try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
			int len = data.length;
			for (int i = 0; i < len; i++) { ps.setObject(i+1, data[i]); }
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
