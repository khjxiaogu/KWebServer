package com.khjxiaogu.webserver.wrappers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.khjxiaogu.webserver.annotations.Adapter;
import com.khjxiaogu.webserver.annotations.Filter;
import com.khjxiaogu.webserver.annotations.FilterClass;
import com.khjxiaogu.webserver.annotations.ForceProtocol;
import com.khjxiaogu.webserver.annotations.GetAs;
import com.khjxiaogu.webserver.annotations.GetBy;
import com.khjxiaogu.webserver.annotations.GetByStr;
import com.khjxiaogu.webserver.annotations.Header;
import com.khjxiaogu.webserver.annotations.HttpMethod;
import com.khjxiaogu.webserver.annotations.HttpPath;
import com.khjxiaogu.webserver.annotations.PostQuery;
import com.khjxiaogu.webserver.annotations.Query;
import com.khjxiaogu.webserver.web.CallBack;
import com.khjxiaogu.webserver.web.ContextHandler;
import com.khjxiaogu.webserver.web.ForceSecureHandler;
import com.khjxiaogu.webserver.web.MethodContext;
import com.khjxiaogu.webserver.web.MethodRestrictHandler;
import com.khjxiaogu.webserver.web.ServiceClass;
import com.khjxiaogu.webserver.web.URIMatchDispatchHandler;
import com.khjxiaogu.webserver.web.lowlayer.Request;
import com.khjxiaogu.webserver.web.lowlayer.Response;
import com.khjxiaogu.webserver.wrappers.inadapters.HeaderValue;
import com.khjxiaogu.webserver.wrappers.inadapters.PostQueryValue;
import com.khjxiaogu.webserver.wrappers.inadapters.QueryValue;

public class ServiceClassWrapper extends URIMatchDispatchHandler {
	// public static SystemLogger logger=new SystemLogger("反射容器");
	public ServiceClassWrapper(JsonObject Arg, ClassProvider cp) throws InstantiationException, IllegalAccessException,
	        IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		this(ServiceClassWrapper.getClassFromJsonObject(Arg, cp));
	}

	private ServiceClass iobj;
	private Map<String, ContextHandler<?>> paths = new HashMap<>();

	public ServiceClassWrapper(ServiceClass object) throws InstantiationException, IllegalAccessException,
	        IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		iobj = object;
		Class<?> clazz = object.getClass();
		object.getLogger().info("正在载入...");
		Map<String, Integer> meths = new HashMap<>();
		Method[] mets = clazz.getDeclaredMethods();
		for (Method m : mets) {// check if method dispatch needed
			HttpPath[] annos = m.getAnnotationsByType(HttpPath.class);
			if (annos.length == 0) { continue; }
			for (HttpPath anno : annos) {
				int ia = meths.getOrDefault(anno.value(), 0) + 1;
				meths.put(anno.value(), ia);
			}
		}
		for (Method m : mets) {// do adding service
			HttpPath[] annos = m.getAnnotationsByType(HttpPath.class);
			Filter[] fannos = m.getAnnotationsByType(Filter.class);
			FilterClass[] fcannos=m.getAnnotationsByType(FilterClass.class);
			if (annos.length == 0) { continue; }
			LinkedList<HttpFilter> hfs = new LinkedList<>();
			for (Filter fc:fannos) { 
				hfs.add(FilterManager.getFilter(fc.value())); 
			}
			for (FilterClass fc:fcannos) { 
				hfs.add(fc.value().getConstructor().newInstance()); 
			}
			HttpFilter[] hf=hfs.toArray(new HttpFilter[0]);
			object.getLogger().info("Patching Method:" + m.getName());
			CallBack crn;
			if (m.getAnnotation(Adapter.class) == null) {
				crn = new MethodServiceProvider(m, object, hf);
			} else {
				crn = new MethodAdaptProvider(m, object, hf);
			}
			ForceProtocol prot = m.getAnnotation(ForceProtocol.class);
			if (prot != null) { crn = new ForceSecureHandler(crn, prot.value()).getListener(); }
			HttpMethod[] hms = m.getAnnotationsByType(HttpMethod.class);

			for (HttpPath anno : annos) {
				int ia = meths.get(anno.value());
				if (ia == 1) {
					if (hms.length > 0) {
						MethodRestrictHandler mrh = new MethodRestrictHandler(crn);
						for (HttpMethod met : hms) { mrh.addMethod(met.value()); }
						crn = mrh;
					}
					this.createContext(anno.value(), crn);
				} else {
					ContextHandler<?> ctxh = paths.get(anno.value());
					if (ctxh == null) {
						ctxh = new MethodContext();
						this.createContext(anno.value(), ctxh);
						paths.put(anno.value(), ctxh);
					}
					for (HttpMethod met : hms) { ctxh.createContext(met.value(), crn); }

				}
			}
		}
	}

	@FunctionalInterface
	public interface ClassProvider {
		public Class<?> forName(String name);
	}

	public static <T> T getClassFromJsonObject(JsonObject jo, ClassProvider cl)
	        throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	        NoSuchMethodException, SecurityException {
		Class<?> clazz = cl.forName(jo.get("class").getAsString());
		JsonArray ja = null;
		JsonElement je = jo.get("args");
		if (je == null) {} else if (je.isJsonArray()) {
			ja = je.getAsJsonArray();
		} else if (!je.isJsonNull()) { ja = new JsonArray(); ja.add(je); }
		return ServiceClassWrapper.initObjectWithJsonArray(ja, clazz, cl);
	}

	@SuppressWarnings("unchecked")
	public static <T> T initObjectWithJsonArray(JsonArray Args, Class<?> clazz, ClassProvider cl)
	        throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	        NoSuchMethodException, SecurityException {
		if (Args != null && Args.size() > 0) {
			Constructor<?>[] ctors = clazz.getConstructors();
			Object[] params = new Object[Args.size()];
			for (Constructor<?> ctor : ctors)
				if (ctor.getParameterCount() == Args.size()) {
					Class<?>[] pms = ctor.getParameterTypes();
					boolean canuse = true;
					for (int i = 0; i < Args.size(); i++)
						if (!ServiceClassWrapper.JsonTypeEquals(Args.get(i), pms[i], cl)) { canuse = false; break; }
					if (!canuse) { continue; }
					for (int i = 0; i < Args.size(); i++) {
						params[i] = ServiceClassWrapper.CastJsonElement(Args.get(i), pms[i], cl);
					}
					ctor.setAccessible(true);
					return (T) ctor.newInstance(params);
				}
		} else
			return (T) clazz.getConstructor().newInstance();
		throw new InvalidParameterException("No Constuctor match for class " + clazz.getSimpleName());
	}

	public static boolean JsonTypeEquals(JsonElement je, Class<?> clazz, ClassProvider cl) {
		if (je.isJsonPrimitive()) {
			JsonPrimitive jp = je.getAsJsonPrimitive();
			if (jp.isBoolean() && (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class)))
				return true;
			else if (jp.isNumber()) {
				if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(int.class)
				        || clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(float.class)
				        || clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(double.class)
				        || clazz.isAssignableFrom(Character.class) || clazz.isAssignableFrom(char.class)
				        || clazz.isAssignableFrom(Byte.class) || clazz.isAssignableFrom(byte.class)
				        || clazz.isAssignableFrom(BigInteger.class) || clazz.isAssignableFrom(BigDecimal.class)
				        || clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(short.class)
				        || clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(long.class)
				        || clazz.isAssignableFrom(String.class))
					return true;
			} else if (jp.isString())
				if (clazz.isAssignableFrom(String.class))
					return true;
		} else if (je.isJsonObject())
			return clazz.isAssignableFrom(cl.forName(je.getAsJsonObject().get("class").getAsString()));
		else if (je.isJsonArray() && clazz.isArray()) {
			JsonArray ja = je.getAsJsonArray();
			for (int i = 0; i < ja.size(); i++)
				if (!ServiceClassWrapper.JsonTypeEquals(ja.get(i), clazz.getComponentType(), cl))
					return false;
			return true;
		} else if (je.isJsonNull())
			return true;
		return false;
	}

	public static Object CastJsonElement(JsonElement je, Class<?> clazz, ClassProvider cl)
	        throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	        NoSuchMethodException, SecurityException {
		if (je.isJsonPrimitive()) {
			JsonPrimitive jp = je.getAsJsonPrimitive();
			if (jp.isBoolean() && (clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(boolean.class)))
				return jp.getAsBoolean();
			else if (jp.isNumber()) {
				if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(int.class))
					return jp.getAsInt();
				else if (clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(float.class))
					return jp.getAsFloat();
				else if (clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(double.class))
					return jp.getAsDouble();
				else if (clazz.isAssignableFrom(Character.class) || clazz.isAssignableFrom(char.class))
					return jp.getAsCharacter();
				else if (clazz.isAssignableFrom(Byte.class) || clazz.isAssignableFrom(byte.class))
					return jp.getAsByte();
				else if (clazz.isAssignableFrom(BigInteger.class))
					return jp.getAsBigInteger();
				else if (clazz.isAssignableFrom(BigDecimal.class))
					return jp.getAsBigDecimal();
				else if (clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(short.class))
					return jp.getAsShort();
				else if (clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(long.class))
					return jp.getAsLong();
				else if (clazz.isAssignableFrom(String.class))
					return jp.getAsString();
				else
					throw new InvalidParameterException(
					        jp.getClass() + " cannot be convert to " + clazz.getSimpleName());
			} else if (jp.isString()) {
				if (clazz.isAssignableFrom(String.class))
					return jp.getAsString();
				throw new InvalidParameterException(jp.getClass() + " cannot be convert to " + clazz.getSimpleName());
			} else
				throw new InvalidParameterException(jp.getClass() + " cannot be convert to " + clazz.getSimpleName());
		} else if (je.isJsonObject())
			return ServiceClassWrapper.getClassFromJsonObject(je.getAsJsonObject(), cl);
		else if (je.isJsonArray()) {
			if (!clazz.isArray())
				throw new InvalidParameterException(je.getClass() + " cannot be convert to " + clazz.getSimpleName());
			JsonArray ja = je.getAsJsonArray();
			Object[] objs = new Object[ja.size()];
			for (int i = 0; i < ja.size(); i++) {
				objs[i] = ServiceClassWrapper.CastJsonElement(ja.get(i), clazz.getComponentType(), cl);
			}
		} else if (je.isJsonNull()) {} else
			throw new InvalidParameterException(je.getClass() + " cannot be convert to " + clazz.getSimpleName());
		return null;
	}

	public ServiceClass getObject() { return iobj; }

}

class MethodServiceProvider implements CallBack {
	protected Method met;
	protected ServiceClass objthis;
	protected HttpFilter[] filters;

	public MethodServiceProvider(Method met, ServiceClass objthis, HttpFilter[] filters) {
		this.met = met;
		met.setAccessible(true);
		this.objthis = objthis;
		this.filters = filters;
	}

	@Override
	public void call(Request req, Response res) {
		try {
			if (doFilter(req, res)) { processResults(req, res, met.invoke(objthis, processParameters(req, res))); }
		} catch (Exception e) {
			exceptionHandler(req, res, e);
		}
	}

	protected boolean doFilter(Request req, Response res) throws Exception {
		if (filters != null) {
			FilterChainIterator fc = new FilterChainIterator(filters, objthis);
			while (fc.hasNext()) {
				if (!fc.next(req, res))
					return false;
			}
		}
		return true;
	}

	protected Object[] processParameters(Request req, Response res) throws Exception {
		return new Object[] { req, res };
	}

	/**
	 * @param req
	 * @param res
	 * @param o
	 */
	protected void processResults(Request req, Response res, Object o) {}

	/**
	 * @param req
	 */
	protected void exceptionHandler(Request req, Response res, Exception e) {
		e.printStackTrace(objthis.getLogger());
		if (!res.isWritten()) { res.write(500, null, "Internal Server Error".getBytes(StandardCharsets.UTF_8)); }
	}
}

class MethodAdaptProvider extends MethodServiceProvider {
	private InAdapter[] paramadap;
	private OutAdapter resultadap;

	@FunctionalInterface
	interface AnnotationHandler {
		InAdapter handle(Annotation anno) throws Exception;
	}

	private static Map<Class<? extends Annotation>, AnnotationHandler> handlers = new HashMap<>();
	static {
		MethodAdaptProvider.handlers.put(GetBy.class, anno -> ((GetBy) anno).value().getConstructor().newInstance());
		MethodAdaptProvider.handlers.put(GetByStr.class,
		        anno -> ((GetByStr) anno).value().getConstructor(String.class).newInstance(((GetByStr) anno).param()));
		MethodAdaptProvider.handlers.put(Query.class, anno -> new QueryValue(((Query) anno).value()));
		MethodAdaptProvider.handlers.put(GetAs.class, anno -> InAdapterManager.getAdapter(((GetAs) anno).value()));
		MethodAdaptProvider.handlers.put(PostQuery.class, anno -> new PostQueryValue(((PostQuery) anno).value()));
		MethodAdaptProvider.handlers.put(Header.class, anno -> new HeaderValue(((Header) anno).value()));
	}
	public MethodAdaptProvider(Method met, ServiceClass objthis, HttpFilter[] filters)
	        throws InstantiationException, IllegalAccessException,
	        IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(met, objthis, filters);
		paramadap = new InAdapter[met.getParameterCount()];
		int i = 0;
		for (Parameter param : met.getParameters()) {
			Annotation[] annos = param.getAnnotations();
			for (Annotation anno : annos) {
				AnnotationHandler ah = MethodAdaptProvider.handlers.get(anno.annotationType());
				if (ah != null) {
					try {
						paramadap[i] = ah.handle(anno);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			i++;
		}
		resultadap = met.getAnnotation(Adapter.class).value().getConstructor().newInstance();
	}

	@Override
	protected void exceptionHandler(Request req, Response res, Exception e) {
		e.printStackTrace(objthis.getLogger());
		objthis.getLogger().warning("error occured at " + objthis.getClass().getSimpleName() + "#" + met.getName());
		objthis.getLogger().warning(e.getMessage());
		if (!res.isWritten()) { res.write(500, null, "Internal Server Error".getBytes(StandardCharsets.UTF_8)); }
	}

	@Override
	protected Object[] processParameters(Request req, Response res) throws Exception {
		Object[] params = new Object[paramadap.length];
		if (paramadap.length > 0) {
			for (int i = 0; i < paramadap.length; i++)
				if (paramadap[i] != null) {
					try {
					params[i] = paramadap[i].handle(req, objthis);
					}catch(Exception e){
						objthis.getLogger().error("error when processing param "+i);
						throw e;
					}
				} else {
					params[i] = req;
				}
		}
		return params;
	}

	@Override
	protected void processResults(Request req, Response res, Object o) { resultadap.handle((ResultDTO) o, res); }
}