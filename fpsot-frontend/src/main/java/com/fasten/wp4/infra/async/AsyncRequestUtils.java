package com.fasten.wp4.infra.async;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Deque;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.internal.NamedRunnable;

public class AsyncRequestUtils {
	
	@SuppressWarnings("unchecked")
	public static AsyncCallStatus getStatus(Dispatcher dispatcher, Call call) {
		
		if(call.isCanceled()) {
			return AsyncCallStatus.Canceled;
		}
		
		Deque<NamedRunnable> readyCalls = null;
		Deque<NamedRunnable> runningCalls = null;
		Deque<Call> executedCalls = null;

		try {

			for (Field f : dispatcher.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if("readyCalls".equals(f.getName())){
					readyCalls = (Deque<NamedRunnable>) f.get(dispatcher);
				}else if("runningCalls".equals(f.getName())){
					runningCalls=(Deque<NamedRunnable>) f.get(dispatcher);
				}else if("executedCalls".equals(f.getName())){
					executedCalls=(Deque<Call>) f.get(dispatcher);
				}
			}
			
			for (NamedRunnable readyNamedRunnableCall : readyCalls) {
				Call readyCall = null;
				for (Method method : readyNamedRunnableCall.getClass().getDeclaredMethods()) {
					method.setAccessible(true);
					if("get".equals(method.getName())){
						readyCall=(Call) method.invoke(readyNamedRunnableCall, new Object[]{});
					}
				}
				if(call.equals(readyCall)) {
					return AsyncCallStatus.Ready;
				}
			}
			
			for (NamedRunnable runningNamedRunnableCall : runningCalls) {
				Call runningCall = null;
				for (Method method : runningNamedRunnableCall.getClass().getDeclaredMethods()) {
					method.setAccessible(true);
					if("get".equals(method.getName())){
						runningCall=(Call) method.invoke(runningNamedRunnableCall, new Object[]{});
					}
				}
				if(call.equals(runningCall)) {
					return AsyncCallStatus.Running;
				}
			}
			for (Call executed : executedCalls) {
				if(call.equals(executed)) {
					return AsyncCallStatus.Executed;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(call.isExecuted()) {
			return AsyncCallStatus.Executed;
		}else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static AsyncCall getAsyncCallFrom(Dispatcher dispatcher, Call call,String uid) {
		
		AsyncCall asyncCall = new AsyncCall();
		asyncCall.setId(uid);
		asyncCall.setCall(call);
		asyncCall.setDispatcher(dispatcher);
		asyncCall.setStart(new Date());
		
		Deque<NamedRunnable> readyCalls = null;
		Deque<NamedRunnable> runningCalls = null;
		Deque<Call> executedCalls = null;

		try {

			for (Field f : dispatcher.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if("readyCalls".equals(f.getName())){
					readyCalls = (Deque<NamedRunnable>) f.get(dispatcher);
				}else if("runningCalls".equals(f.getName())){
					runningCalls=(Deque<NamedRunnable>) f.get(dispatcher);
				}else if("executedCalls".equals(f.getName())){
					executedCalls=(Deque<Call>) f.get(dispatcher);
				}
			}
			
			NamedRunnable nr = null;
			for (NamedRunnable readyNamedRunnableCall : readyCalls) {
				Call readyCall = null;
				for (Method method : readyNamedRunnableCall.getClass().getDeclaredMethods()) {
					method.setAccessible(true);
					if("get".equals(method.getName())){
						readyCall=(Call) method.invoke(readyNamedRunnableCall, new Object[]{});
					}
				}
				if(call.equals(readyCall)) {
					asyncCall.setStatus(AsyncCallStatus.Ready);
					nr=readyNamedRunnableCall;
				}
			}
			
			for (NamedRunnable runningNamedRunnableCall : runningCalls) {
				Call runningCall = null;
				for (Method method : runningNamedRunnableCall.getClass().getDeclaredMethods()) {
					method.setAccessible(true);
					if("get".equals(method.getName())){
						runningCall=(Call) method.invoke(runningNamedRunnableCall, new Object[]{});
					}
				}
				if(call.equals(runningCall)) {
					asyncCall.setStatus(AsyncCallStatus.Running);
					nr=runningNamedRunnableCall;
				}
			}
			
			for (Call executed : executedCalls) {
				if(call.equals(executed)) {
					asyncCall.setStatus(AsyncCallStatus.Executed);
				}
			}
			
			if(nr!=null) {
				asyncCall.setTag(getTag(nr));
				asyncCall.setHost(getHost(nr));
				asyncCall.setName(getName(nr));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return asyncCall;
	}
	
	
	private static Object getTag(NamedRunnable nr) {
		try {
			Object tag = null;
			for (Method method : nr.getClass().getDeclaredMethods()) {
				method.setAccessible(true);
				if("tag".equals(method.getName())){
					tag= method.invoke(nr, new Object[]{});
				}
			}
			return tag;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getHost(NamedRunnable nr) {
		try {
			String host = null;
			for (Method method : nr.getClass().getDeclaredMethods()) {
				method.setAccessible(true);
				if("host".equals(method.getName())){
					host= (String) method.invoke(nr, new Object[]{});
				}
			}
			return host;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getName(NamedRunnable nr) {
		try {
			String name = null;
			for (Field f : nr.getClass().getSuperclass().getDeclaredFields()) {
				f.setAccessible(true);
				if("name".equals(f.getName())){
					name=(String) f.get(nr);
				}
			}
			return name;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getName(Object o) {
		try {
			String name = null;
			for (Field f : o.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if("name".equals(f.getName())){
					name=(String) f.get(o);
				}
			}
			return name;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Object update(AsyncCall asyncCall) {
		asyncCall.setStatus(getStatus(asyncCall.getDispatcher(), asyncCall.getCall()));
		return asyncCall;
	}
}
