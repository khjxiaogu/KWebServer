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
	class InsertExpr {
		String key;
		Object data;

		public InsertExpr(String key, Object data) {
			this.key = key;
			this.data = data;
		}
	}

	String table;
	Connection conn;
	protected ArrayList<InsertExpr> inserts = new ArrayList<>();

	public InsertStatementBuilder(String table, Connection conn) {
		this.table = table;
		this.conn = conn;
	}

	@Override
	public InsertStatementBuilder set(String key, Object val) { inserts.add(new InsertExpr(key,val));return this; }

	@Override
	public String getSQL() {
		StringBuilder sql = new StringBuilder("INSERT INTO");
		sql.append(table);
		sql.append("(");
		StringBuilder datas = new StringBuilder("(");
		Iterator<InsertExpr> it = inserts.iterator();
		if (it.hasNext()) {
			while (true) {
				InsertExpr expr = it.next();
				if (expr.key != null) { sql.append(expr.key); }
				datas.append("?");
				if (it.hasNext()) {
					sql.append(", ");
					datas.append(",");
				} else {
					sql.append(") VALUES");
					sql.append(datas.toString());
					break;
				}
			}
		}
		return sql.toString();
	}

	@Override
	public boolean execute() {

		try (PreparedStatement ps = conn.prepareStatement(getSQL())) {
			int len = inserts.size();
			for (int i = 0; i < len; i++) { ps.setObject(i, inserts.get(i).data); }
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
