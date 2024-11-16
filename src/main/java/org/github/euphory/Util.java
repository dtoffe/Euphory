/*
 * Copyright (c) 2024 Daniel Toffetti
 *
 * This file is licensed under the MIT License.
 * You may obtain a copy of the license at:
 * https://opensource.org/licenses/MIT
 *
 * Alternatively, see the LICENSE file included with this source code for the full license text.
 */
package org.github.euphory;

import javafx.util.Duration;

/**
 * @author Daniel Toffetti
 * 
 */
public class Util {
    
    public static String toFormattedTime(Duration duration) {
        return String.format("%02d:%02d:%02d",
                            (long)duration.toHours(),
                            (long)(duration.toMinutes() % 60),
                            (long)(duration.toSeconds() % 60));
    }
    
}
