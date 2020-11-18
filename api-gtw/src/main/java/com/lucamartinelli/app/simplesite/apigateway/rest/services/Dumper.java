package com.lucamartinelli.app.simplesite.apigateway.rest.services;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dumper {
	
	private static final Logger log = Logger.getLogger(Dumper.class.getCanonicalName());
	
	/**
	 * Dump the message and any other objects in input
	 * The objects is logged with standard method if is possible
	 * or in other cases via reflection
	 */
	public static void dump(String msg, Object... objects) {
		try {
			StringBuffer sb = new StringBuffer("DUMP:\n");
			sb.append(msg);
			
			for (Object o : objects) {
				if (o == null)
					sb.append("null").append('\n');
				else if (o instanceof String)
					sb.append(String.valueOf(o)).append('\n');
				else if (o.getClass().isArray())
					sb.append(Arrays.toString((Object[]) o)).append('\n');
				else if (o.getClass().isPrimitive())
					sb.append(o.getClass().getName()).append(o).append('\n');
				else if (o instanceof Collection<?>)
					sb.append(o).append('\n');
				else if (o.getClass().getMethod("toString").getDeclaringClass() != Object.class)
					sb.append(o.getClass().getSimpleName() + " : ").append(o.toString()).append('\n');
				else
					sb.append(getInfoFromGeneric(o)).append('\n');
			}
			
			log.log(Level.FINE, sb.toString());
		} catch (NoSuchMethodException | SecurityException
				| IllegalArgumentException | IllegalAccessException e) {
			log.log(Level.WARNING, "Exception in DUMP method: ", e);
		}
	}

	
	
	
	private static String getInfoFromGeneric(Object o) throws IllegalArgumentException, 
			IllegalAccessException, NoSuchMethodException, SecurityException {
		if (o == null)
			return "null";
		
		final Field[] fields = o.getClass().getDeclaredFields();
		final StringBuffer sb = new StringBuffer(o.getClass().getSimpleName() + " [");
		for (Field f : fields) {
			if (f.isSynthetic() ||
					f.getDeclaringClass() != o.getClass())
				continue;
			
			f.setAccessible(true);
			
			if (f.get(o) == null)
				sb.append(f.getName()).append("=null, ");
			else if (f.getType().isPrimitive())
				sb.append(f.getName()).append('=').append(f.get(o)).append(", ");
			else if (f.getType() == String.class)
				sb.append(f.getName()).append('=').append(String.valueOf(f.get(o))).append(", ");
			else if (f.getType().isArray())
				sb.append(f.getName()).append('=').append(Arrays.toString((Object[]) f.get(o))).append(", ");
			else if (f.get(o) instanceof Collection<?>)
				sb.append(f.getName()).append('=').append(f.get(o)).append(", ");
			else if (f.getType().getMethod("toString").getDeclaringClass() != Object.class)
				sb.append(f.getName()).append('=').append(f.get(o).toString()).append(", ");
			else
				sb.append(f.getName()).append('=').append(getInfoFromGeneric(f.get(o))).append(", ");
		}
		return sb.delete(sb.length() - 2, sb.length()).append(']').toString();
	}

}
