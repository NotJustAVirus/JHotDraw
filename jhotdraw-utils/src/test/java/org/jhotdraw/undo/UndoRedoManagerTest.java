package org.jhotdraw.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.Action;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit tests for UndoRedoManager business functionality.
 */
public class UndoRedoManagerTest {

    private UndoRedoManager manager;

    @BeforeMethod
    public void setUp() {
        manager = new UndoRedoManager();
    }

    @Test
    public void testInfrastructure() {
        assertTrue(true, "Test infrastructure is working");
    }

    @Test
    public void testInitialState() {
        // Manager should start with no edits
        assertFalse(manager.canUndo(), "New manager should not be able to undo");
        assertFalse(manager.canRedo(), "New manager should not be able to redo");
        assertFalse(manager.hasSignificantEdits(), "New manager should not have significant edits");
    }

    @Test
    public void testAddEditEnablesUndo() {
        // Add a significant edit
        AbstractUndoableEdit edit = new AbstractUndoableEdit();
        manager.addEdit(edit);

        assertTrue(manager.canUndo(), "Should be able to undo after adding edit");
        assertFalse(manager.canRedo(), "Should not be able to redo after adding edit");
        assertTrue(manager.hasSignificantEdits(), "Should have significant edits after adding edit");
    }

    @Test
    public void testUndoRedo() {
        // Create a simple undoable edit
        TestEdit edit = new TestEdit();
        manager.addEdit(edit);

        // Undo the edit
        manager.undo();
        assertTrue(edit.undoCalled, "Undo should have been called on the edit");
        assertFalse(manager.canUndo(), "Should not be able to undo after undoing only edit");
        assertTrue(manager.canRedo(), "Should be able to redo after undo");

        // Redo the edit
        manager.redo();
        assertTrue(edit.redoCalled, "Redo should have been called on the edit");
        assertTrue(manager.canUndo(), "Should be able to undo after redo");
        assertFalse(manager.canRedo(), "Should not be able to redo after redo");
    }

    @Test
    public void testMultipleEdits() {
        TestEdit edit1 = new TestEdit();
        TestEdit edit2 = new TestEdit();
        TestEdit edit3 = new TestEdit();

        manager.addEdit(edit1);
        manager.addEdit(edit2);
        manager.addEdit(edit3);

        // Undo all three edits in reverse order
        manager.undo();
        assertTrue(edit3.undoCalled, "Edit 3 should be undone first");

        manager.undo();
        assertTrue(edit2.undoCalled, "Edit 2 should be undone second");

        manager.undo();
        assertTrue(edit1.undoCalled, "Edit 1 should be undone last");

        assertFalse(manager.canUndo(), "Should not be able to undo after undoing all edits");
        assertTrue(manager.canRedo(), "Should be able to redo after undoing all edits");
    }

    @Test
    public void testDiscardAllEdits() {
        manager.addEdit(new AbstractUndoableEdit());
        manager.addEdit(new AbstractUndoableEdit());

        assertTrue(manager.hasSignificantEdits(), "Should have significant edits");
        assertTrue(manager.canUndo(), "Should be able to undo");

        manager.discardAllEdits();

        assertFalse(manager.hasSignificantEdits(), "Should not have significant edits after discard");
        assertFalse(manager.canUndo(), "Should not be able to undo after discard");
        assertFalse(manager.canRedo(), "Should not be able to redo after discard");
    }

    @Test
    public void testActionsEnabledState() {
        Action undoAction = manager.getUndoAction();
        Action redoAction = manager.getRedoAction();

        assertNotNull(undoAction, "Undo action should not be null");
        assertNotNull(redoAction, "Redo action should not be null");

        assertFalse(undoAction.isEnabled(), "Undo action should be disabled initially");
        assertFalse(redoAction.isEnabled(), "Redo action should be disabled initially");

        manager.addEdit(new AbstractUndoableEdit());

        assertTrue(undoAction.isEnabled(), "Undo action should be enabled after adding edit");
        assertFalse(redoAction.isEnabled(), "Redo action should still be disabled");

        manager.undo();

        assertFalse(undoAction.isEnabled(), "Undo action should be disabled after undo");
        assertTrue(redoAction.isEnabled(), "Redo action should be enabled after undo");
    }

    @Test
    public void testIgnoreEditsWhileUndoRedoInProgress() {
        TestEdit edit1 = new TestEdit() {
            @Override
            public void undo() {
                super.undo();
                // Try to add another edit during undo - it should be ignored
                manager.addEdit(new AbstractUndoableEdit());
            }
        };

        manager.addEdit(edit1);
        
        manager.undo();
        
        // After undo, we should not be able to undo again because the edit
        // added during undo should have been ignored
        assertFalse(manager.canUndo(), "Edits added during undo should be ignored");
    }

    @Test(expectedExceptions = CannotUndoException.class)
    public void testUndoThrowsExceptionWhenCannotUndo() {
        manager.undo();
    }

    @Test(expectedExceptions = CannotRedoException.class)
    public void testRedoThrowsExceptionWhenCannotRedo() {
        manager.redo();
    }

    /**
     * Helper class for testing undo/redo behavior
     */
    private static class TestEdit extends AbstractUndoableEdit {
        boolean undoCalled = false;
        boolean redoCalled = false;

        @Override
        public void undo() {
            super.undo();
            undoCalled = true;
        }

        @Override
        public void redo() {
            super.redo();
            redoCalled = true;
        }
    }
}
