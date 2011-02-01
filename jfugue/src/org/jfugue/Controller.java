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

package org.jfugue;

import java.util.Map;

import org.jfugue.util.MapUtils;

/**
 * Contains information for MIDI Controller Events.
 *
 *@author David Koelle
 *@version 2.0
 */
public final class Controller implements JFugueElement
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	byte index;
    byte value;

    /** Creates a new Controller object */
    public Controller()
    {
        this.index = 0;
        this.value = 0;
    }

    /**
     * Creates a new Controller object, with the specified controller index and value.
     *
     * @param index the index of the controller to set
     * @param value the byte value used to set the controller
     */
    public Controller(byte index, byte value)
    {
        this.index = index;
        this.value = value;
    }

    /**
     * TODO: (Implement a static factory instead of this constructor?)  This method, which is currently not supported,
     * is intended to take an integer, which contains the MSB and LSB bytes of a controller event,
     * and parse the integer to the MSB and LSB.  Currently, this feature is handled by MusicStringParser, and
     * has not been rolled into the Controller class (where it should really belong).  One difficulty to consider
     * is that an integer representing an MSB and an LSB actually means that there are two Controller instances,
     * not just one.  Of course, the Controller class itself represents only one controller event.
     *
     * @param index
     * @param value
     */
//    public Controller(int index, byte value)
//    {
//        throw new UnsupportedOperationException("Controller(int index, byte value) is not supported.  If you're using a byte for the controller index, cast your index appropriately to use Controller(byte index, byte value).");
//    }

    /**
     * Sets the index of the controller event for this object.
     * @param index the index of the controller
     */
    public void setIndex(byte index)
    {
        this.index = index;
    }

    /**
     * Returns the index of the controller event for this object.
     * @return the index of the controller
     */
    public byte getIndex()
    {
        return this.index;
    }

    /**
     * Sets the value of the controller event for this object.
     * @param value the byte value used to set the controller
     */
    public void setValue(byte value)
    {
        this.value = value;
    }

    /**
     * Returns the value of the controller event for this object.
     * @return the value of the controller
     */
    public byte getValue()
    {
        return this.value;
    }

    /**
     * Returns the Music String representing this element and all of its settings.
     * For a Controller object, the Music String is <code>X</code><i>index</i>=<i>value</i>
     * @return the Music String for this element
     */
    public String getMusicString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("X");
        buffy.append(getIndex());
        buffy.append("=");
        buffy.append(getValue());
        return buffy.toString();
    }

    /**
     * Returns verification string in this format:
     * Controller: index={#}, value={#}
     * @version 4.0
     */
    public String getVerifyString()
    {
        StringBuffer buffy = new StringBuffer();
        buffy.append("Controller: index=");
        buffy.append(getIndex());
        buffy.append(", value=");
        buffy.append(getValue());
        return buffy.toString();
    }

    public static final byte BANK_SELECT_COARSE = 0;
    public static final byte MOD_WHEEL_COARSE = 1;
    public static final byte BREATH_COARSE = 2;
    public static final byte FOOT_PEDAL_COARSE = 4;
    public static final byte PORTAMENTO_TIME_COARSE = 5;
    public static final byte DATA_ENTRY_COARSE = 6;
    public static final byte VOLUME_COARSE = 7;
    public static final byte BALANCE_COARSE = 8;
    public static final byte PAN_POSITION_COARSE = 10;
    public static final byte EXPRESSION_COARSE = 11;
    public static final byte EFFECT_CONTROL_1_COARSE = 12;
    public static final byte EFFECT_CONTROL_2_COARSE = 13;

    public static final byte SLIDER_1 = 16;
    public static final byte SLIDER_2 = 17;
    public static final byte SLIDER_3 = 18;
    public static final byte SLIDER_4 = 19;

    public static final byte BANK_SELECT_FINE = 32;
    public static final byte MOD_WHEEL_FINE = 33;
    public static final byte BREATH_FINE = 34;
    public static final byte FOOT_PEDAL_FINE = 36;
    public static final byte PORTAMENTO_TIME_FINE = 37;
    public static final byte DATA_ENTRY_FINE = 38;
    public static final byte VOLUME_FINE = 39;
    public static final byte BALANCE_FINE = 40;
    public static final byte PAN_POSITION_FINE = 42;
    public static final byte EXPRESSION_FINE = 43;
    public static final byte EFFECT_CONTROL_1_FINE = 44;
    public static final byte EFFECT_CONTROL_2_FINE = 45;

    public static final byte HOLD_PEDAL = 64;
    public static final byte HOLD = 64;
    public static final byte PORTAMENTO = 65;
    public static final byte SUSTENUTO_PEDAL = 66;
    public static final byte SUSTENUTO = 66;
    public static final byte SOFT_PEDAL = 67;
    public static final byte SOFT = 67;
    public static final byte LEGATO_PEDAL = 68;
    public static final byte LEGATO = 68;
    public static final byte HOLD_2_PEDAL = 69;
    public static final byte HOLD_2 = 69;

    public static final byte SOUND_VARIATION = 70;
    public static final byte VARIATION = 70;
    public static final byte SOUND_TIMBRE = 71;
    public static final byte TIMBRE = 71;
    public static final byte SOUND_RELEASE_TIME = 72;
    public static final byte RELEASE_TIME = 72;
    public static final byte SOUND_ATTACK_TIME = 73;
    public static final byte ATTACK_TIME = 73;
    public static final byte SOUND_BRIGHTNESS = 74;
    public static final byte BRIGHTNESS = 74;
    public static final byte SOUND_CONTROL_6 = 75;
    public static final byte CONTROL_6 = 75;
    public static final byte SOUND_CONTROL_7 = 76;
    public static final byte CONTROL_7 = 76;
    public static final byte SOUND_CONTROL_8 = 77;
    public static final byte CONTROL_8 = 77;
    public static final byte SOUND_CONTROL_9 = 78;
    public static final byte CONTROL_9 = 78;
    public static final byte SOUND_CONTROL_10 = 79;
    public static final byte CONTROL_10 = 79;

    public static final byte GENERAL_PURPOSE_BUTTON_1 = 80;
    public static final byte GENERAL_BUTTON_1 = 80;
    public static final byte BUTTON_1 = 80;
    public static final byte GENERAL_PURPOSE_BUTTON_2 = 81;
    public static final byte GENERAL_BUTTON_2 = 81;
    public static final byte BUTTON_2 = 81;
    public static final byte GENERAL_PURPOSE_BUTTON_3 = 82;
    public static final byte GENERAL_BUTTON_3 = 82;
    public static final byte BUTTON_3 = 82;
    public static final byte GENERAL_PURPOSE_BUTTON_4 = 83;
    public static final byte GENERAL_BUTTON_4 = 83;
    public static final byte BUTTON_4 = 83;

    public static final byte EFFECTS_LEVEL = 91;
    public static final byte EFFECTS = 91;
    public static final byte TREMULO_LEVEL = 92;
    public static final byte TREMULO = 92;
    public static final byte CHORUS_LEVEL = 93;
    public static final byte CHORUS = 93;
    public static final byte CELESTE_LEVEL = 94;
    public static final byte CELESTE = 94;
    public static final byte PHASER_LEVEL = 95;
    public static final byte PHASER = 95;

    public static final byte DATA_BUTTON_INCREMENT = 96;
    public static final byte DATA_BUTTON_INC = 96;
    public static final byte BUTTON_INC = 96;
    public static final byte DATA_BUTTON_DECREMENT = 97;
    public static final byte DATA_BUTTON_DEC = 97;
    public static final byte BUTTON_DEC = 97;

    public static final byte NON_REGISTERED_COARSE = 98;
    public static final byte NON_REGISTERED_FINE = 99;
    public static final byte REGISTERED_COARSE = 100;
    public static final byte REGISTERED_FINE = 101;

    public static final byte ALL_SOUND_OFF = 120;
    public static final byte ALL_CONTROLLERS_OFF = 121;
    public static final byte LOCAL_KEYBOARD = 122;
    public static final byte ALL_NOTES_OFF = 123;
    public static final byte OMNI_MODE_OFF = 124;
    public static final byte OMNI_OFF = 124;
    public static final byte OMNI_MODE_ON = 125;
    public static final byte OMNI_ON = 125;
    public static final byte MONO_OPERATION = 126;
    public static final byte MONO = 126;
    public static final byte POLY_OPERATION = 127;
    public static final byte POLY = 127;

        //
        // Combined Controller names
        // (index = coarse_controller_index * 128 + fine_controller_index)
        //
    public static final int BANK_SELECT = 16383;
    public static final int MOD_WHEEL = 161;
    public static final int BREATH = 290;
    public static final int FOOT_PEDAL = 548;
    public static final int PORTAMENTO_TIME = 677;
    public static final int DATA_ENTRY = 806;
    public static final int VOLUME = 935;
    public static final int BALANCE = 1064;
    public static final int PAN_POSITION = 1322;
    public static final int EXPRESSION = 1451;
    public static final int EFFECT_CONTROL_1 = 1580;
    public static final int EFFECT_CONTROL_2 = 1709;
    public static final int NON_REGISTERED = 12770;
    public static final int REGISTERED = 13028;

        //
        // Values for controllers
        //
    public static final byte ON = 127;
    public static final byte OFF = 0;
    public static final byte DEFAULT = 64;

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Controller other = (Controller) obj;
        if (this.index != other.index) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.index;
        hash = 97 * hash + this.value;
        return hash;
    }

    public static final Map<String,String> DICT_MAP;
    static {
    	DICT_MAP = MapUtils.convertArrayToImutableMap(new String[][] {
    			//
    	        // Controller names
    	        //
    	        {"BANK_SELECT_COARSE"          ,"0"},
    	        {"MOD_WHEEL_COARSE"            ,"1"},
    	        {"BREATH_COARSE"               ,"2"},
    	        {"FOOT_PEDAL_COARSE"           ,"4"},
    	        {"PORTAMENTO_TIME_COARSE"      ,"5"},
    	        {"DATA_ENTRY_COARSE"           ,"6"},
    	        {"VOLUME_COARSE"               ,"7"},
    	        {"BALANCE_COARSE"              ,"8"},
    	        {"PAN_POSITION_COARSE"         ,"10"},
    	        {"EXPRESSION_COARSE"           ,"11"},
    	        {"EFFECT_CONTROL_1_COARSE"     ,"12"},
    	        {"EFFECT_CONTROL_2_COARSE"     ,"13"},

    	        {"SLIDER_1"                    ,"16"},
    	        {"SLIDER_2"                    ,"17"},
    	        {"SLIDER_3"                    ,"18"},
    	        {"SLIDER_4"                    ,"19"},

    	        {"BANK_SELECT_FINE"            ,"32"},
    	        {"MOD_WHEEL_FINE"              ,"33"},
    	        {"BREATH_FINE"                 ,"34"},
    	        {"FOOT_PEDAL_FINE"             ,"36"},
    	        {"PORTAMENTO_TIME_FINE"        ,"37"},
    	        {"DATA_ENTRY_FINE"             ,"38"},
    	        {"VOLUME_FINE"                 ,"39"},
    	        {"BALANCE_FINE"                ,"40"},
    	        {"PAN_POSITION_FINE"           ,"42"},
    	        {"EXPRESSION_FINE"             ,"43"},
    	        {"EFFECT_CONTROL_1_FINE"       ,"44"},
    	        {"EFFECT_CONTROL_2_FINE"       ,"45"},

    	        {"HOLD_PEDAL"                  ,"64"},
    	        {"HOLD"                        ,"64"},
    	        {"PORTAMENTO"                  ,"65"},
    	        {"SUSTENUTO_PEDAL"             ,"66"},
    	        {"SUSTENUTO"                   ,"66"},
    	        {"SOFT_PEDAL"                  ,"67"},
    	        {"SOFT"                        ,"67"},
    	        {"LEGATO_PEDAL"                ,"68"},
    	        {"LEGATO"                      ,"68"},
    	        {"HOLD_2_PEDAL"                ,"69"},
    	        {"HOLD_2"                      ,"69"},

    	        {"SOUND_VARIATION"             ,"70"},
    	        {"VARIATION"                   ,"70"},
    	        {"SOUND_TIMBRE"                ,"71"},
    	        {"TIMBRE"                      ,"71"},
    	        {"SOUND_RELEASE_TIME"          ,"72"},
    	        {"RELEASE_TIME"                ,"72"},
    	        {"SOUND_ATTACK_TIME"           ,"73"},
    	        {"ATTACK_TIME"                 ,"73"},
    	        {"SOUND_BRIGHTNESS"            ,"74"},
    	        {"BRIGHTNESS"                  ,"74"},
    	        {"SOUND_CONTROL_6"             ,"75"},
    	        {"CONTROL_6"                   ,"75"},
    	        {"SOUND_CONTROL_7"             ,"76"},
    	        {"CONTROL_7"                   ,"76"},
    	        {"SOUND_CONTROL_8"             ,"77"},
    	        {"CONTROL_8"                   ,"77"},
    	        {"SOUND_CONTROL_9"             ,"78"},
    	        {"CONTROL_9"                   ,"78"},
    	        {"SOUND_CONTROL_10"            ,"79"},
    	        {"CONTROL_10"                  ,"79"},

    	        {"GENERAL_PURPOSE_BUTTON_1"    ,"80"},
    	        {"GENERAL_BUTTON_1"            ,"80"},
    	        {"BUTTON_1"                    ,"80"},
    	        {"GENERAL_PURPOSE_BUTTON_2"    ,"81"},
    	        {"GENERAL_BUTTON_2"            ,"81"},
    	        {"BUTTON_2"                    ,"81"},
    	        {"GENERAL_PURPOSE_BUTTON_3"    ,"82"},
    	        {"GENERAL_BUTTON_3"            ,"82"},
    	        {"BUTTON_3"                    ,"82"},
    	        {"GENERAL_PURPOSE_BUTTON_4"    ,"83"},
    	        {"GENERAL_BUTTON_4"            ,"83"},
    	        {"BUTTON_4"                    ,"83"},

    	        {"EFFECTS_LEVEL"               ,"91"},
    	        {"EFFECTS"                     ,"91"},
    	        {"TREMULO_LEVEL"               ,"92"},
    	        {"TREMULO"                     ,"92"},
    	        {"CHORUS_LEVEL"                ,"93"},
    	        {"CHORUS"                      ,"93"},
    	        {"CELESTE_LEVEL"               ,"94"},
    	        {"CELESTE"                     ,"94"},
    	        {"PHASER_LEVEL"                ,"95"},
    	        {"PHASER"                      ,"95"},

    	        {"DATA_BUTTON_INCREMENT"       ,"96"},
    	        {"DATA_BUTTON_INC"             ,"96"},
    	        {"BUTTON_INC"                  ,"96"},
    	        {"DATA_BUTTON_DECREMENT"       ,"97"},
    	        {"DATA_BUTTON_DEC"             ,"97"},
    	        {"BUTTON_DEC"                  ,"97"},

    	        {"NON_REGISTERED_COARSE"       ,"98"},
    	        {"NON_REGISTERED_FINE"         ,"99"},
    	        {"REGISTERED_COARSE"           ,"100"},
    	        {"REGISTERED_FINE"             ,"101"},

    	        {"ALL_SOUND_OFF"               ,"120"},
    	        {"ALL_CONTROLLERS_OFF"         ,"121"},
    	        {"LOCAL_KEYBOARD"              ,"122"},
    	        {"ALL_NOTES_OFF"               ,"123"},
    	        {"OMNI_MODE_OFF"               ,"124"},
    	        {"OMNI_OFF"                    ,"124"},
    	        {"OMNI_MODE_ON"                ,"125"},
    	        {"OMNI_ON"                     ,"125"},
    	        {"MONO_OPERATION"              ,"126"},
    	        {"MONO"                        ,"126"},
    	        {"POLY_OPERATION"              ,"127"},
    	        {"POLY"                        ,"127"},

    	        //
    	        // Combined Controller names
    	        // {index = coarse_controller_index * 128 + fine_controller_index)
    	        //
    	        {"BANK_SELECT"                ,"16383"},
    	        {"MOD_WHEEL"                  ,"161"},
    	        {"BREATH"                     ,"290"},
    	        {"FOOT_PEDAL"                 ,"548"},
    	        {"PORTAMENTO_TIME"            ,"677"},
    	        {"DATA_ENTRY"                 ,"806"},
    	        {"VOLUME"                     ,"935"},
    	        {"BALANCE"                    ,"1064"},
    	        {"PAN_POSITION"               ,"1322"},
    	        {"EXPRESSION"                 ,"1451"},
    	        {"EFFECT_CONTROL_1"           ,"1580"},
    	        {"EFFECT_CONTROL_2"           ,"1709"},
    	        {"NON_REGISTERED"             ,"12770"},
    	        {"REGISTERED"                 ,"13028"},

    	        //
    	        // Values for controllers
    	        //
    	        {"ON"                         ,"127"},
    	        {"OFF"                        ,"0"},
    	        {"DEFAULT"                    ,"64"},  			
    	});
    }
    
    public void acceptVisitor(ElementVisitor visitor) {
    	visitor.visit(this);
    }
    
    // TODO Implement JFugueElementFactory
}