/*
 * @(#)UndoActionInternal.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.undo;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.undo.*;

/**
 * Internal Undo Action for use in a menu bar.
 * This action is used internally by {@link UndoRedoManager} and should not
 * be used directly by application code.
 *
 * @version $Id$
 */
class UndoActionInternal extends AbstractAction {

    private static final long serialVersionUID = 1L;
    private final UndoRedoManager manager;

    /**
     * Creates a new UndoActionInternal.
     *
     * @param manager the UndoRedoManager that this action operates on
     */
    public UndoActionInternal(UndoRedoManager manager) {
        this.manager = manager;
        setEnabled(false);
    }

    /**
     * Invoked when an action occurs.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        try {
            manager.undo();
        } catch (CannotUndoException e) {
            System.out.println("Cannot undo: " + e.getMessage());
        }
    }
}
