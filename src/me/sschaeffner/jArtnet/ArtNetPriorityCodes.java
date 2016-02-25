/*
 * Copyright (C) 2015  Simon Schaeffner <simon.schaeffner@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.sschaeffner.jArtnet;

/**
 * A collection of all Priority Codes of the Art-Net 3 specification.
 *
 * These codes are used in ArtPoll and ArtDiagData packages.
 *
 * @author sschaeffner
 */
public class ArtNetPriorityCodes {
    public static final byte DP_LOW = (byte) 0x10;
    public static final byte DP_MID = (byte) 0x40;
    public static final byte DP_HIGH = (byte) 0x80;
    public static final byte DP_CRITICAL = (byte) 0xE0;
    public static final byte DP_VOLATILE = (byte) 0xF0;
}
