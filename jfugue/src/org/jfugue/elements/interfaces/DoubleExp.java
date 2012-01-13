package org.jfugue.elements.interfaces;

import org.jfugue.Environment;

public interface DoubleExp extends Expression<Double> {

	public static class Dict implements DoubleExp {
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
		
		public Double eval(Environment environment) {
			return environment.getDoubleFromDictionary(key);
		}

	}

	public static class Literal implements DoubleExp {
		private Double value;
		
		public Literal(Double value) {
			this.value = value;
		}

		public String toString() {
			return value.toString();
		}

		public Double eval(Environment environment) {
			return value;
		}
		
	}

}
