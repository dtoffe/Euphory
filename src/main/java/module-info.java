/*
 * Copyright (c) 2024 Daniel Toffetti
 *
 * This file is licensed under the MIT License.
 * You may obtain a copy of the license at:
 * https://opensource.org/licenses/MIT
 *
 * Alternatively, see the LICENSE file included with this source code for the full license text.
 */
module org.github.euphory {
    requires java.logging;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;
    requires transitive javafx.graphics;
    requires javafx.swing;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires javafx.base;

    opens org.github.euphory to javafx.fxml;
    exports org.github.euphory;
    exports org.github.euphory.tags;
}
