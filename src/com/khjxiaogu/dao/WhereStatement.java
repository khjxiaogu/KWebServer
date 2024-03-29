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

import java.util.List;

public class WhereStatement {
	class WhereExpr {
		String key = null;
		Object val;
		String expr = "= ?";

		public WhereExpr(String expr) { this.expr = expr; }

		public WhereExpr(String key,  String expr) {
			this.key = key;
			this.val = val;
			this.expr = expr;
		}
	}
	class StatementRelation{
		boolean and;
		List<WhereExpr> exprs;
		WhereStatement sup;
		StatementRelation parent;
		public StatementRelation and(String expr) {
			if(and) {
				exprs.add(new WhereExpr(expr));
				return this;
			}else
				return new StatementRelation();
		}
	}
	public WhereStatement() {}

}
