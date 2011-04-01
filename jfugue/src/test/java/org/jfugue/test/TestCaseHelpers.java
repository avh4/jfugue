package org.jfugue.test;

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestCaseHelpers {

	protected static final void format(String format, Object... objects) {
		System.out.format(format, objects);
	}

    protected static Object invokeRestrictedMethod(Object objectArg, Class classArg, final String methodName, @SuppressWarnings("rawtypes") final Class [] methodArgTypes,  final Object [] methodArgs) {
        try {
	    Method method = classArg.getDeclaredMethod(methodName, methodArgTypes);
	    method.setAccessible(true);
            return method.invoke(objectArg, methodArgs);
        } catch(NoSuchMethodException _e) {
	    fail("Could not find method " + methodName);
        } catch(IllegalAccessException _e){
	    fail("Could not access method " + methodName);
        } catch(InvocationTargetException _e){
	    fail("Could not invoke method " + methodName + " on given object");
        } catch(IllegalArgumentException _e){
	    fail("Wrong arguments provided for " + methodName);
        }
        return null;
    }

}
