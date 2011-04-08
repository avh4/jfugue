package org.jfugue.elements.interfaces;

import org.jfugue.Environment;

public interface ByteExp extends Expression<Byte> {

	public static class Dict implements ByteExp {
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

		public Byte eval(Environment environment) {
			return environment.getByteFromDictionary(key);
		}

	}

	public static class Literal implements ByteExp {
		private Byte value;
		
		public Literal(Byte value) {
			this.value = value;
		}
		
		public String toString() {
			return value.toString();
		}

		public Byte eval(Environment environment) {
			return value;
		}
		
	}
	
	public static class MSB implements ByteExp {
		private IntExp intExp;

		public MSB(IntExp intExp) {
			this.intExp = intExp;
		}
		
		public String toString() {
			return "MSB(" + intExp.toString() + ")";
		}

		public Byte eval(Environment environment) {
			return (byte) (intExp.eval(environment) / 128);
		}
		
	}

	public static class LSB implements ByteExp {
		private IntExp intExp;

		public LSB(IntExp intExp) {
			this.intExp = intExp;
		}

		public String toString() {
			return "LSB(" + intExp.toString() + ")";
		}
		
		public Byte eval(Environment environment) {
			return (byte) (intExp.eval(environment) % 128);
		}
		
	}
}
