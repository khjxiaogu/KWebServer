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

public class InsertStatementBuilder implements InputStatement<InsertStatementBuilder> {
	private class InsertPair{
		String key;
		String expr;
		public InsertPair(String key, String expr) {
			super();
			this.key = key;
			this.expr = expr;
		}
		public InsertPair(String key) {
			super();
			this.key = key;
			this.expr="?";
		}
		
	}
	String table;
	Connection conn;
	protected ArrayList<InsertPair> inserts = new ArrayList<>();
	public InsertStatementBuilder(String table, Connection conn) {
		this.table = table;
		this.conn = conn;
	}


	@Override
	public InsertStatementBuilder set(String key) { inserts.add(new InsertPair(key));return this; }
	
	@Override
	public InsertStatementBuilder set(String key,String expr) { inserts.add(new InsertPair(key,expr));return this; }
	
	@Override
	public String getSQL() {
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(table);
		sql.append("(");
		StringBuilder datas = new StringBuilder("(");
		Iterator<InsertPair> it = inserts.iterator();
		if (it.hasNext()) {
			while (true) {
				InsertPair expr = it.next();
				sql.append(expr.key);
				datas.append(expr.expr);
				if (it.hasNext()) {
					sql.append(", ");
					datas.append(",");
				} else {
					sql.append(") VALUES ");
					sql.append(datas.append(")").toString());
					break;
				}
			}
		}
		return sql.toString();
	}

	@Override
	public boolean execute(Object[] data) {

		try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
			int len = data.length;
			for (int i = 0; i < len; i++) { ps.setObject(i+1, data[i]); }
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public PreparedStatement build() throws SQLException {
		return conn.prepareStatement(getSQL());
	}


	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
