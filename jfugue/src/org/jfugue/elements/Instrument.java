/*
 * JFugue - API for Music Programming
 * Copyright (C) 2003-2008  David Koelle
 *
 * http://www.jfugue.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

package org.jfugue.elements;

import java.io.IOException;
import java.util.Map;

import org.jfugue.JFugueException;
import org.jfugue.factories.JFugueElementFactory;
import org.jfugue.parsers.ParserContext;
import org.jfugue.parsers.ParserError;
import org.jfugue.util.MapUtils;
import org.jfugue.visitors.ElementVisitor;

/**
 * Represents instrument changes, also known as <i>patch changes</i>.
 *
 *@author David Koelle
 *@version 2.0
 */
public final class Instrument implements JFugueElement
{
    public static class Factory extends JFugueElementFactory<Instrument> {
    	private static Instrument.Factory instance;
		private Factory() {}
		public static Instrument.Factory getInstance() {
			if (instance == null)
				instance = new Instrument.Factory();
			return instance;
		}
		
		public Instrument createElement(ParserContext context)
				throws IOException, IllegalArgumentException, JFugueException,
				ParserError {
			context.readOneOfTheChars('I', 'i');
			return context.fireInstrumentEvent(new Instrument(context.readByte()));
		}

		public Class<Instrument> type() {
			return Instrument.class;
		}

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private byte instrument;

    /**
     * Creates a new Instrument object, with the specified instrument number.
     * @param instrument the number of the instrument to use
     */
    public Instrument(byte instrument)
    {
        setInstrument(instrument);
    }

    /**
     * Sets the value of the instrument for this object.
     * @param instrument the number of the instrument to use
     */
    public void setInstrument(byte instrument)
    {
        this.instrument = instrument;
    }

    /**
     * Returns the instrument used in this object
     * @return the instrument used in this object
     */
    public byte getInstrument()
    {
        return instrument;
    }

    /**
     * Returns the name of the instrument used in this object
     * @return the name of the instrument used in this object
     */
    public String getInstrumentName()
    {
        return INSTRUMENT_NAME[getInstrument()];
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For an Instrument object, the Music String is <code>I</code><i>instrument-number</i>
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("I[");
        buffy.append(INSTRUMENT_NAME[getInstrument()]);
        buffy.append("]");
        return buffy.toString();
    }

    /**
     * Returns verification string in this format:
     * Instrument: instrument={#}
     */
    public String getVerifyString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("Instrument: instrument=");
        buffy.append(getInstrument());
        return buffy.toString();
    }

    public static final String[] INSTRUMENT_NAME = new String[] {
        "Piano",
        "Bright_Acoustic",
        "Electric_Grand",
        "Honkey_Tonk",
        "Electric_Piano",
        "Electric_Piano_2",
        "Harpischord",
        "Clavinet",
        "Celesta",
        "Glockenspiel",

        "Music_Box",
        "Vibraphone",
        "Marimba",
        "Xylophone",
        "Tubular_Bells",
        "Dulcimer",
        "Drawbar_Organ",
        "Percussive_Organ",
        "Rock_Organ",
        "Church_Organ",

        "Reed_Organ",
        "Accordian",
        "Harmonica",
        "Tango_Accordian",
        "Guitar",
        "Steel_String_Guitar",
        "Electric_Jazz_Guitar",
        "Electric_Clean_Guitar",
        "Electric_muted_Guitar",
        "Overdriven_Guitar",
        "Distortion_Guitar",

        "Guitar_Harmonics",
        "Acoustic_Bass",
        "Electric_Bass_Finger",
        "Electric_Bass_Pick",
        "Fretless_Bass",
        "Slap_Bass_1",
        "Slap_Bass_2",
        "Synth_Bass_1",
        "Synth_Bass_2",

        "Violin",
        "Viola",
        "Cello",
        "Contrabass",
        "Tremolo_Strings",
        "Pizzicato_Strings",
        "Orchestral_Strings",
        "Timpani",
        "String_Ensemble_1",
        "String_Ensemble_2",

        "Synth_strings_1",
        "Synth_strings_2",
        "Choir_Aahs",
        "Voice_Oohs",
        "Synth_Voice",
        "Orchestra_Hit",
        "Trumpet",
        "Trombone",
        "Tuba",
        "Muted_Trumpet",

        "French_Horn",
        "Brass_Section",
        "Synth_brass_1",
        "Synth_brass_2",
        "Soprano_Sax",
        "Alto_Sax",
        "Tenor_Sax",
        "Baritone_Sax",
        "Oboe",
        "English_Horn",

        "Bassoon",
        "Clarinet",
        "Piccolo",
        "Flute",
        "Recorder",
        "Pan_Flute",
        "Blown_Bottle",
        "Skakuhachi",
        "Whistle",
        "Ocarina",

        "Square",
        "Sawtooth",
        "Calliope",
        "Chiff",
        "Charang",
        "Voice",
        "Fifths",
        "Basslead",
        "New_Age",
        "Warm",

        "Polysynth",
        "Choir",
        "Bowed",
        "Metallic",
        "Halo",
        "Sweep",
        "Rain",
        "Soundtrack",
        "Crystal",
        "Atmosphere",

        "Brightness",
        "Goblins",
        "Echoes",
        "Sci-fi",
        "Sitar",
        "Banjo",
        "Shamisen",
        "Koto",
        "Kalimba",
        "Bagpipe",

        "Fiddle",
        "Shanai",
        "Tinkle_Bell",
        "Agogo",
        "Steel_Drums",
        "Woodblock",
        "Taiko_Drum",
        "Melodic_Tom",
        "Synth_Drum",
        "Reverse_Cymbal",

        "Guitar_Fret_Noise",
        "Breath_Noise",
        "Seashore",
        "Bird_Tweet",
        "Telephone_Ring",
        "Helicopter",
        "Applause",
        "Gunshot" };

    public static final byte PIANO = 0;
    public static final byte ACOUSTIC_GRAND = 0;
    public static final byte BRIGHT_ACOUSTIC = 1;
    public static final byte ELECTRIC_GRAND = 2;
    public static final byte HONKEY_TONK = 3;
    public static final byte ELECTRIC_PIANO = 4;
    public static final byte ELECTRIC_PIANO_1 = 4;
    public static final byte ELECTRIC_PIANO_2 = 5;
    public static final byte HARPISCHORD = 6;
    public static final byte CLAVINET = 7;
    public static final byte CELESTA = 8;
    public static final byte GLOCKENSPIEL = 9;

    public static final byte MUSIC_BOX = 10;
    public static final byte VIBRAPHONE = 11;
    public static final byte MARIMBA = 12;
    public static final byte XYLOPHONE = 13;
    public static final byte TUBULAR_BELLS = 14;
    public static final byte DULCIMER = 15;
    public static final byte DRAWBAR_ORGAN = 16;
    public static final byte PERCUSSIVE_ORGAN = 17;
    public static final byte ROCK_ORGAN = 18;
    public static final byte CHURCH_ORGAN = 19;

    public static final byte REED_ORGAN = 20;
    public static final byte ACCORDIAN = 21;
    public static final byte HARMONICA = 22;
    public static final byte TANGO_ACCORDIAN = 23;
    public static final byte GUITAR = 24;
    public static final byte NYLON_STRING_GUITAR = 24;
    public static final byte STEEL_STRING_GUITAR = 25;
    public static final byte ELECTRIC_JAZZ_GUITAR = 26;
    public static final byte ELECTRIC_CLEAN_GUITAR = 27;
    public static final byte ELECTRIC_MUTED_GUITAR = 28;
    public static final byte OVERDRIVEN_GUITAR = 29;

    public static final byte DISTORTION_GUITAR = 30;
    public static final byte GUITAR_HARMONICS = 31;
    public static final byte ACOUSTIC_BASS = 32;
    public static final byte ELECTRIC_BASS_FINGER = 33;
    public static final byte ELECTRIC_BASS_PICK = 34;
    public static final byte FRETLESS_BASS = 35;
    public static final byte SLAP_BASS_1 = 36;
    public static final byte SLAP_BASS_2 = 37;
    public static final byte SYNTH_BASS_1 = 38;
    public static final byte SYNTH_BASS_2 = 39;

    public static final byte VIOLIN = 40;
    public static final byte VIOLA = 41;
    public static final byte CELLO = 42;
    public static final byte CONTRABASS = 43;
    public static final byte TREMOLO_STRINGS = 44;
    public static final byte PIZZICATO_STRINGS = 45;
    public static final byte ORCHESTRAL_STRINGS = 46;
    public static final byte TIMPANI = 47;
    public static final byte STRING_ENSEMBLE_1 = 48;
    public static final byte STRING_ENSEMBLE_2 = 49;

    public static final byte SYNTH_STRINGS_1 = 50;
    public static final byte SYNTH_STRINGS_2 = 51;
    public static final byte CHOIR_AAHS = 52;
    public static final byte VOICE_OOHS = 53;
    public static final byte SYNTH_VOICE = 54;
    public static final byte ORCHESTRA_HIT = 55;
    public static final byte TRUMPET = 56;
    public static final byte TROMBONE = 57;
    public static final byte TUBA = 58;
    public static final byte MUTED_TRUMPET = 59;

    public static final byte FRENCH_HORN = 60;
    public static final byte BRASS_SECTION = 61;
    public static final byte SYNTHBRASS_1 = 62;
    public static final byte SYNTH_BRASS_1 = 62;
    public static final byte SYNTHBRASS_2 = 63;
    public static final byte SYNTH_BRASS_2 = 63;
    public static final byte SOPRANO_SAX = 64;
    public static final byte ALTO_SAX = 65;
    public static final byte TENOR_SAX = 66;
    public static final byte BARITONE_SAX = 67;
    public static final byte OBOE = 68;
    public static final byte ENGLISH_HORN = 69;

    public static final byte BASSOON = 70;
    public static final byte CLARINET = 71;
    public static final byte PICCOLO = 72;
    public static final byte FLUTE = 73;
    public static final byte RECORDER = 74;
    public static final byte PAN_FLUTE = 75;
    public static final byte BLOWN_BOTTLE = 76;
    public static final byte SKAKUHACHI = 77;
    public static final byte WHISTLE = 78;
    public static final byte OCARINA = 79;

    public static final byte LEAD_SQUARE = 80;
    public static final byte SQUARE = 80;
    public static final byte LEAD_SAWTOOTH = 81;
    public static final byte SAWTOOTH = 81;
    public static final byte LEAD_CALLIOPE = 82;
    public static final byte CALLIOPE = 82;
    public static final byte LEAD_CHIFF = 83;
    public static final byte CHIFF = 83;
    public static final byte LEAD_CHARANG = 84;
    public static final byte CHARANG = 84;
    public static final byte LEAD_VOICE = 85;
    public static final byte VOICE = 85;
    public static final byte LEAD_FIFTHS = 86;
    public static final byte FIFTHS = 86;
    public static final byte LEAD_BASSLEAD = 87;
    public static final byte BASSLEAD = 87;
    public static final byte PAD_NEW_AGE = 88;
    public static final byte NEW_AGE = 88;
    public static final byte PAD_WARM = 89;
    public static final byte WARM = 89;

    public static final byte PAD_POLYSYNTH = 90;
    public static final byte POLYSYNTH = 90;
    public static final byte PAD_CHOIR = 91;
    public static final byte CHOIR = 91;
    public static final byte PAD_BOWED = 92;
    public static final byte BOWED = 92;
    public static final byte PAD_METALLIC = 93;
    public static final byte METALLIC = 93;
    public static final byte PAD_HALO = 94;
    public static final byte HALO = 94;
    public static final byte PAD_SWEEP = 95;
    public static final byte SWEEP = 95;
    public static final byte FX_RAIN = 96;
    public static final byte RAIN = 96;
    public static final byte FX_SOUNDTRACK = 97;
    public static final byte SOUNDTRACK = 97;
    public static final byte FX_CRYSTAL = 98;
    public static final byte CRYSTAL = 98;
    public static final byte FX_ATMOSPHERE = 99;
    public static final byte ATMOSPHERE = 99;

    public static final byte FX_BRIGHTNESS = 100;
    public static final byte BRIGHTNESS = 100;
    public static final byte FX_GOBLINS = 101;
    public static final byte GOBLINS = 101;
    public static final byte FX_ECHOES = 102;
    public static final byte ECHOES = 102;
    public static final byte FX_SCI = 103;
    public static final byte SCI = 103;
    public static final byte SITAR = 104;
    public static final byte BANJO = 105;
    public static final byte SHAMISEN = 106;
    public static final byte KOTO = 107;
    public static final byte KALIMBA = 108;
    public static final byte BAGPIPE = 109;

    public static final byte FIDDLE = 110;
    public static final byte SHANAI = 111;
    public static final byte TINKLE_BELL = 112;
    public static final byte AGOGO = 113;
    public static final byte STEEL_DRUMS = 114;
    public static final byte WOODBLOCK = 115;
    public static final byte TAIKO_DRUM = 116;
    public static final byte MELODIC_TOM = 117;
    public static final byte SYNTH_DRUM = 118;
    public static final byte REVERSE_CYMBAL = 119;

    public static final byte GUITAR_FRET_NOISE = 120;
    public static final byte BREATH_NOISE = 121;
    public static final byte SEASHORE = 122;
    public static final byte BIRD_TWEET = 123;
    public static final byte TELEPHONE_RING = 124;
    public static final byte HELICOPTER = 125;
    public static final byte APPLAUSE = 126;
    public static final byte GUNSHOT = 127;

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Instrument other = (Instrument) obj;
        if (this.instrument != other.instrument) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.instrument;
        return hash;
    }

    public static final Map<String,String> DICT_MAP;
    static {
    	DICT_MAP = MapUtils.convertArrayToImutableMap(new String[][] {
    	        //
    	        // Instrument names
    	        //
    	        {"PIANO"                    ,"0"},
    	        {"ACOUSTIC_GRAND"           ,"0"},
    	        {"BRIGHT_ACOUSTIC"          ,"1"},
    	        {"ELECTRIC_GRAND"           ,"2"},
    	        {"HONKEY_TONK"              ,"3"},
    	        {"ELECTRIC_PIANO"           ,"4"},
    	        {"ELECTRIC_PIANO_1"         ,"4"},
    	        {"ELECTRIC_PIANO_2"         ,"5"},
    	        {"HARPISCHORD"              ,"6"},
    	        {"CLAVINET"                 ,"7"},
    	        {"CELESTA"                  ,"8"},
    	        {"GLOCKENSPIEL"             ,"9"},

    	        {"MUSIC_BOX"                ,"10"},
    	        {"VIBRAPHONE"               ,"11"},
    	        {"MARIMBA"                  ,"12"},
    	        {"XYLOPHONE"                ,"13"},
    	        {"TUBULAR_BELLS"            ,"14"},
    	        {"DULCIMER"                 ,"15"},
    	        {"DRAWBAR_ORGAN"            ,"16"},
    	        {"PERCUSSIVE_ORGAN"         ,"17"},
    	        {"ROCK_ORGAN"               ,"18"},
    	        {"CHURCH_ORGAN"             ,"19"},

    	        {"REED_ORGAN"               ,"20"},
    	        {"ACCORDIAN"                ,"21"},
    	        {"HARMONICA"                ,"22"},
    	        {"TANGO_ACCORDIAN"          ,"23"},
    	        {"GUITAR"                   ,"24"},
    	        {"NYLON_STRING_GUITAR"      ,"24"},
    	        {"STEEL_STRING_GUITAR"      ,"25"},
    	        {"ELECTRIC_JAZZ_GUITAR"     ,"26"},
    	        {"ELECTRIC_CLEAN_GUITAR"    ,"27"},
    	        {"ELECTRIC_MUTED_GUITAR"    ,"28"},
    	        {"OVERDRIVEN_GUITAR"        ,"29"},

    	        {"DISTORTION_GUITAR"        ,"30"},
    	        {"GUITAR_HARMONICS"         ,"31"},
    	        {"ACOUSTIC_BASS"            ,"32"},
    	        {"ELECTRIC_BASS_FINGER"     ,"33"},
    	        {"ELECTRIC_BASS_PICK"       ,"34"},
    	        {"FRETLESS_BASS"            ,"35"},
    	        {"SLAP_BASS_1"              ,"36"},
    	        {"SLAP_BASS_2"              ,"37"},
    	        {"SYNTH_BASS_1"             ,"38"},
    	        {"SYNTH_BASS_2"             ,"39"},

    	        {"VIOLIN"                   ,"40"},
    	        {"VIOLA"                    ,"41"},
    	        {"CELLO"                    ,"42"},
    	        {"CONTRABASS"               ,"43"},
    	        {"TREMOLO_STRINGS"          ,"44"},
    	        {"PIZZICATO_STRINGS"        ,"45"},
    	        {"ORCHESTRAL_STRINGS"       ,"46"},
    	        {"TIMPANI"                  ,"47"},
    	        {"STRING_ENSEMBLE_1"        ,"48"},
    	        {"STRING_ENSEMBLE_2"        ,"49"},

    	        {"SYNTH_STRINGS_1"          ,"50"},
    	        {"SYNTH_STRINGS_2"          ,"51"},
    	        {"CHOIR_AAHS"               ,"52"},
    	        {"VOICE_OOHS"               ,"53"},
    	        {"SYNTH_VOICE"              ,"54"},
    	        {"ORCHESTRA_HIT"            ,"55"},
    	        {"TRUMPET"                  ,"56"},
    	        {"TROMBONE"                 ,"57"},
    	        {"TUBA"                     ,"58"},
    	        {"MUTED_TRUMPET"            ,"59"},

    	        {"FRENCH_HORN"              ,"60"},
    	        {"BRASS_SECTION"            ,"61"},
    	        {"SYNTHBRASS_1"             ,"62"},
    	        {"SYNTH_BRASS_1"            ,"62"},
    	        {"SYNTHBRASS_2"             ,"63"},
    	        {"SYNTH_BRASS_2"            ,"63"},
    	        {"SOPRANO_SAX"              ,"64"},
    	        {"ALTO_SAX"                 ,"65"},
    	        {"TENOR_SAX"                ,"66"},
    	        {"BARITONE_SAX"             ,"67"},
    	        {"OBOE"                     ,"68"},
    	        {"ENGLISH_HORN"             ,"69"},

    	        {"BASSOON"                  ,"70"},
    	        {"CLARINET"                 ,"71"},
    	        {"PICCOLO"                  ,"72"},
    	        {"FLUTE"                    ,"73"},
    	        {"RECORDER"                 ,"74"},
    	        {"PAN_FLUTE"                ,"75"},
    	        {"BLOWN_BOTTLE"             ,"76"},
    	        {"SKAKUHACHI"               ,"77"},
    	        {"WHISTLE"                  ,"78"},
    	        {"OCARINA"                  ,"79"},

    	        {"LEAD_SQUARE"              ,"80"},
    	        {"SQUARE"                   ,"80"},
    	        {"LEAD_SAWTOOTH"            ,"81"},
    	        {"SAWTOOTH"                 ,"81"},
    	        {"LEAD_CALLIOPE"            ,"82"},
    	        {"CALLIOPE"                 ,"82"},
    	        {"LEAD_CHIFF"               ,"83"},
    	        {"CHIFF"                    ,"83"},
    	        {"LEAD_CHARANG"             ,"84"},
    	        {"CHARANG"                  ,"84"},
    	        {"LEAD_VOICE"               ,"85"},
    	        {"VOICE"                    ,"85"},
    	        {"LEAD_FIFTHS"              ,"86"},
    	        {"FIFTHS"                   ,"86"},
    	        {"LEAD_BASSLEAD"            ,"87"},
    	        {"BASSLEAD"                 ,"87"},
    	        {"PAD_NEW_AGE"              ,"88"},
    	        {"NEW_AGE"                  ,"88"},
    	        {"PAD_WARM"                 ,"89"},
    	        {"WARM"                     ,"89"},

    	        {"PAD_POLYSYNTH"            ,"90"},
    	        {"POLYSYNTH"                ,"90"},
    	        {"PAD_CHOIR"                ,"91"},
    	        {"CHOIR"                    ,"91"},
    	        {"PAD_BOWED"                ,"92"},
    	        {"BOWED"                    ,"92"},
    	        {"PAD_METALLIC"             ,"93"},
    	        {"METALLIC"                 ,"93"},
    	        {"PAD_HALO"                 ,"94"},
    	        {"HALO"                     ,"94"},
    	        {"PAD_SWEEP"                ,"95"},
    	        {"SWEEP"                    ,"95"},
    	        {"FX_RAIN"                  ,"96"},
    	        {"RAIN"                     ,"96"},
    	        {"FX_SOUNDTRACK"            ,"97"},
    	        {"SOUNDTRACK"               ,"97"},
    	        {"FX_CRYSTAL"               ,"98"},
    	        {"CRYSTAL"                  ,"98"},
    	        {"FX_ATMOSPHERE"            ,"99"},
    	        {"ATMOSPHERE"               ,"99"},

    	        {"FX_BRIGHTNESS"            ,"100"},
    	        {"BRIGHTNESS"               ,"100"},
    	        {"FX_GOBLINS"               ,"101"},
    	        {"GOBLINS"                  ,"101"},
    	        {"FX_ECHOES"                ,"102"},
    	        {"ECHOES"                   ,"102"},
    	        {"FX_SCI-FI"                ,"103"},
    	        {"SCI-FI"                   ,"103"},
    	        {"SITAR"                    ,"104"},
    	        {"BANJO"                    ,"105"},
    	        {"SHAMISEN"                 ,"106"},
    	        {"KOTO"                     ,"107"},
    	        {"KALIMBA"                  ,"108"},
    	        {"BAGPIPE"                  ,"109"},

    	        {"FIDDLE"                   ,"110"},
    	        {"SHANAI"                   ,"111"},
    	        {"TINKLE_BELL"              ,"112"},
    	        {"AGOGO"                    ,"113"},
    	        {"STEEL_DRUMS"              ,"114"},
    	        {"WOODBLOCK"                ,"115"},
    	        {"TAIKO_DRUM"               ,"116"},
    	        {"MELODIC_TOM"              ,"117"},
    	        {"SYNTH_DRUM"               ,"118"},
    	        {"REVERSE_CYMBAL"           ,"119"},

    	        {"GUITAR_FRET_NOISE"        ,"120"},
    	        {"BREATH_NOISE"             ,"121"},
    	        {"SEASHORE"                 ,"122"},
    	        {"BIRD_TWEET"               ,"123"},
    	        {"TELEPHONE_RING"           ,"124"},
    	        {"HELICOPTER"               ,"125"},
    	        {"APPLAUSE"                 ,"126"},
    	        {"GUNSHOT"                  ,"127"},

    	        //
    	        // Percussion names
    	        //
    	        {"ACOUSTIC_BASS_DRUM"     ,"35"},
    	        {"BASS_DRUM"              ,"36"},
    	        {"SIDE_STICK"             ,"37"},
    	        {"ACOUSTIC_SNARE"         ,"38"},
    	        {"HAND_CLAP"              ,"39"},

    	        {"ELECTRIC_SNARE"         ,"40"},
    	        {"LOW_FLOOR_TOM"          ,"41"},
    	        {"CLOSED_HI_HAT"          ,"42"},
    	        {"HIGH_FLOOR_TOM"         ,"43"},
    	        {"PEDAL_HI_HAT"           ,"44"},
    	        {"LOW_TOM"                ,"45"},
    	        {"OPEN_HI_HAT"            ,"46"},
    	        {"LOW_MID_TOM"            ,"47"},
    	        {"HI_MID_TOM"             ,"48"},
    	        {"CRASH_CYMBAL_1"         ,"49"},

    	        {"HIGH_TOM"               ,"50"},
    	        {"RIDE_CYMBAL_1"          ,"51"},
    	        {"CHINESE_CYMBAL"         ,"52"},
    	        {"RIDE_BELL"              ,"53"},
    	        {"TAMBOURINE"             ,"54"},
    	        {"SPLASH_CYMBAL"          ,"55"},
    	        {"COWBELL"                ,"56"},
    	        {"CRASH_CYMBAL_2"         ,"57"},
    	        {"VIBRASLAP"              ,"58"},
    	        {"RIDE_CYMBAL_2"          ,"59"},

    	        {"HI_BONGO"               ,"60"},
    	        {"LOW_BONGO"              ,"61"},
    	        {"MUTE_HI_CONGA"          ,"62"},
    	        {"OPEN_HI_CONGA"          ,"63"},
    	        {"LOW_CONGA"              ,"64"},
    	        {"HIGH_TIMBALE"           ,"65"},
    	        {"LOW_TIMBALE"            ,"66"},
    	        {"HIGH_AGOGO"             ,"67"},
    	        {"LOW_AGOGO"              ,"68"},
    	        {"CABASA"                 ,"69"},

    	        {"MARACAS"                ,"70"},
    	        {"SHORT_WHISTLE"          ,"71"},
    	        {"LONG_WHISTLE"           ,"72"},
    	        {"SHORT_GUIRO"            ,"73"},
    	        {"LONG_GUIRO"             ,"74"},
    	        {"CLAVES"                 ,"75"},
    	        {"HI_WOOD_BLOCK"          ,"76"},
    	        {"LOW_WOOD_BLOCK"         ,"77"},
    	        {"MUTE_CUICA"             ,"78"},
    	        {"OPEN_CUICA"             ,"79"},

    	        {"MUTE_TRIANGLE"          ,"80"},
    	        {"OPEN_TRIANGLE"          ,"81"},
    	});
    }
    
    public void acceptVisitor(ElementVisitor visitor) {
    	visitor.visit(this);
    }
    
    
}