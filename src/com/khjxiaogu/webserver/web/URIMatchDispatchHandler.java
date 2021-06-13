package com.khjxiaogu.webserver.web;

import java.util.ArrayList;
import java.util.List;

import com.khjxiaogu.webserver.builder.Patcher;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;

// TODO: Auto-generated Javadoc
/**
 * Class URIMatchDispatchHandler. 根据URI调用回调的类 判断成功后，从请求的当前路径中去除路径字符串 只要开头符合字符串即可
 *
 * 如请求路径为 /foo/bar 监听方法有 1./bar 2./foo 3./ 会选用2号监听器并且设置请求路径为/bar
 * 设置的前后不影响判断顺序，优先判断长的规则
 *
 * @author: khjxiaogu file: URIMatchDispatchHandler.java time: 2020年5月8日
 */
public class URIMatchDispatchHandler implements ContextHandler<URIMatchDispatchHandler>, Patcher {

	/**
	 * The ctxs.<br />
	 * 成员 ctxs.
	 */
	protected List<CallBackContext> ctxs = new ArrayList<>();

	/**
	 * The wildcard.<br />
	 * 成员 wildcard.
	 */
	protected CallBack wildcard;

	/**
	 * Instantiates a new URIMatchDispatchHandler.<br />
	 * 新建一个URIMatchDispatchHandler类<br />
	 */
	public URIMatchDispatchHandler() {}

	@Override
	public URIMatchDispatchHandler createContext(String string, CallBack handler) {
		if (string.equals("/")) {
			wildcard = handler;
		} else {
			ctxs.add(new CallBackContext(string, handler));
		}
		ctxs.sort((c1, c2) -> {
		    return c2.rule.compareTo(c1.rule);
		});
		return this;
	}

	@Override
	public URIMatchDispatchHandler removeContext(String rule) {
		if (rule != null) { ctxs.removeIf(T -> T.rule.equals(rule)); }
		return this;
	}

	@Override
	public <T extends ContextHandler<T>> T patchSite(T site) {
		for (CallBackContext ctx : ctxs) { site.createContext(ctx.rule, ctx.val); }
		return site;
	}

	@Override
	public void call(Request req, Response res) {
		if (req.getCurrentPath() == null) { if (wildcard != null) { wildcard.call(req, res); } return; }
		for (CallBackContext hctx : ctxs)
			if (req.getCurrentPath().startsWith(hctx.rule)) {
				req.SkipPath(hctx.rule);
				hctx.val.call(req, res);
				return;
			}
		if (wildcard != null) { wildcard.call(req, res); }
	}
}
