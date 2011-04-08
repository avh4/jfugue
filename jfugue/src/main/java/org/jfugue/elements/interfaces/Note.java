package org.jfugue.elements.interfaces;

public interface Note extends Element {

	public Value getValue();
	public Chord getChord();
	public Duration getDuration();
	public Velocity getVelocity();
	
	public interface Value extends SubElements {
		public boolean isNatural();
		public ByteExp getValue();
		public ByteExp getOctave();
	}

	public interface Chord extends SubElements {
		public ByteExp[] getIntervals();
		public ByteExp getInversion();
	}
	
	public interface Duration extends SubElements {
		public DoubleExp getDecimalDuration();
	}
	
	public interface Velocity extends SubElements {
		public ByteExp getAttack();
		public ByteExp getDecay();
	}
	
}
