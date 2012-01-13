package org.jfugue.elements.interfaces;

import org.jfugue.Environment;

public interface LongExp extends Expression<Long> {

	public static class Dict implements LongExp {
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
		
		public Long eval(Environment environment) {
			return environment.getLongFromDictionary(key);
		}

	}

	public static class Literal implements LongExp {
		private Long value;
		
		public Literal(Long value) {
			this.value = value;
		}

		public String toString() {
			return value.toString();
		}

		public Long eval(Environment environment) {
			return value;
		}
		
	}

}
