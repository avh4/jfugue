package org.jfugue.elements.interfaces;

import org.jfugue.Environment;

public interface IntExp extends Expression<Integer> {

	public static class Dict implements IntExp {
		private String key;
		
		public Dict(String key) {
			this.key = key;
		}
		
		public String getKey() {
			return key;
		}

		public String toString() {
			return "[" + key + "]";
		}
		
		public Integer eval(Environment environment) {
			return environment.getIntFromDictionary(key);
		}

	}

	public static class Literal implements IntExp {
		private Integer value;
		
		public Literal(Integer value) {
			this.value = value;
		}

		public String toString() {
			return value.toString();
		}

		public Integer eval(Environment environment) {
			return value;
		}
		
	}

}
