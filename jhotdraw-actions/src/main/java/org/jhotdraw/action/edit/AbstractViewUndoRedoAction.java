/*
 * @(#)AbstractViewUndoRedoAction.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.action.edit;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.AbstractAction;
import org.jhotdraw.action.AbstractViewAction;
import org.jhotdraw.api.app.Application;
import org.jhotdraw.api.app.View;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * Base class for view-bound Undo/Redo actions that delegate to the real action
 * stored in the {@link View}'s {@link javax.swing.ActionMap}.
 */
abstract class AbstractViewUndoRedoAction extends AbstractViewAction {

    private static final long serialVersionUID = 1L;

    private final String actionId;
    private final ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.action.Labels");

    private final PropertyChangeListener actionPropertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            if ((name == null && AbstractAction.NAME == null) || (name != null && name.equals(AbstractAction.NAME))) {
                putValue(AbstractAction.NAME, evt.getNewValue());
            } else if ("enabled".equals(name)) {
                updateEnabledState();
            }
        }
    };

    protected AbstractViewUndoRedoAction(Application app, View view, String actionId) {
        super(app, view);
        this.actionId = actionId;
        labels.configureAction(this, actionId);
    }

    @Override
    protected void updateView(View oldValue, View newValue) {
        super.updateView(oldValue, newValue);
        if (newValue != null) {
            Action a = newValue.getActionMap().get(actionId);
            if (a != null && a != this) {
                putValue(AbstractAction.NAME, a.getValue(AbstractAction.NAME));
                updateEnabledState();
            }
        }
    }

    @Override
    protected void installViewListeners(View p) {
        super.installViewListeners(p);
        Action a = p.getActionMap().get(actionId);
        if (a != null && a != this) {
            a.addPropertyChangeListener(actionPropertyListener);
        }
    }

    @Override
    protected void uninstallViewListeners(View p) {
        super.uninstallViewListeners(p);
        Action a = p.getActionMap().get(actionId);
        if (a != null && a != this) {
            a.removePropertyChangeListener(actionPropertyListener);
        }
    }

    protected void updateEnabledState() {
        boolean isEnabled = false;
        Action realAction = getRealAction();
        if (realAction != null && realAction != this) {
            isEnabled = realAction.isEnabled();
        }
        setEnabled(isEnabled);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action realAction = getRealAction();
        if (realAction != null && realAction != this) {
            realAction.actionPerformed(e);
        }
    }

    private Action getRealAction() {
        return (getActiveView() == null) ? null : getActiveView().getActionMap().get(actionId);
    }
}
