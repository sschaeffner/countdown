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
 * A collection of all Style Codes of the Art-Net 3 specification.
 *
 * These Codes are used in ArtPollReply packages.
 *
 * @author sschaeffner
 */
public class ArtnetStyleCodes {
    public static final byte ST_NODE = 0x00;
    public static final byte ST_CONTROLLER = 0x01;
    public static final byte ST_MEDIA = 0x02;
    public static final byte ST_ROUTE = 0x03;
    public static final byte ST_BACKUP = 0x04;
    public static final byte ST_CONFIG = 0x05;
    public static final byte ST_VISUAL = 0x06;

    public static String toName(byte code) {
        switch (code) {
            case ST_NODE:
                return "node";
            case ST_CONTROLLER:
                return "controller";
            case ST_MEDIA:
                return "media";
            case ST_ROUTE:
                return "route";
            case ST_BACKUP:
                return "backup";
            case ST_CONFIG:
                return "config";
            case ST_VISUAL:
                return "visual";
            default:
                throw new IllegalArgumentException("0x" + Integer.toHexString(code) + " is an unknown style code.");
        }
    }
}
