package com.khjxiaogu.dao;

public class WhereStatement {
	class WhereExpr {
		String key = null;
		Object val;
		String expr = "?";

		public WhereExpr(String expr) { this.expr = expr; }

		public WhereExpr(String key, Object val) {
			this.key = key;
			this.val = val;
		}

		public WhereExpr(String key, Object val, String expr) {
			this.key = key;
			this.val = val;
			this.expr = expr;
		}
	}

	public WhereStatement() {}

}
