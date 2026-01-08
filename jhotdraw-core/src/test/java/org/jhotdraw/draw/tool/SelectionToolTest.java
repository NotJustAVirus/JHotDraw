package org.jhotdraw.draw.tool;

import org.jhotdraw.draw.*;
import org.jhotdraw.draw.event.FigureSelectionListener;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.handle.Handle;
import org.junit.jupiter.api.Test;


import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SelectionToolTest {

    @Test
    void mousePressed_onEmptySpace_withoutShift() {
        // Arrange: fake view that is enabled and records calls
        FakeDrawingView view = new FakeDrawingView(true);

        // Arrange: fake editor that returns that view for the event source container
        FakeDrawingEditor editor = new FakeDrawingEditor(view);

        // Real SelectionTool (no subclass / no override)
        SelectionTool tool = new SelectionTool();
        tool.activate(editor); // sets editor inside AbstractTool

        // Mouse event source MUST be a Container (Panel is fine)
        MouseEvent evt = mouseEvent(false /*shiftDown*/);

        // Act
        tool.mousePressed(evt);

        // Assert: these are the behaviors in the refactored branch
        assertTrue(view.clearSelectionCalled, "Expected selection to be cleared when shift is NOT down");
        assertEquals(0, view.lastHandleDetailLevel, "Expected handle detail level to be reset to 0");
    }

    private MouseEvent mouseEvent(boolean shiftDown) {
        int modifiers = shiftDown ? InputEvent.SHIFT_DOWN_MASK : 0;
        return new MouseEvent(
                new Panel(),                 // Panel = Container (required by AbstractTool)
                MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(),
                modifiers,
                10,
                10,
                1,
                false
        );
    }

    /**
     * Minimal editor that makes AbstractTool.mousePressed work:
     * it must return a view for the event source container.
     */
    static class FakeDrawingEditor implements DrawingEditor {
        private final DrawingView view;

        FakeDrawingEditor(DrawingView view) {
            this.view = view;
        }

        @Override
        public DrawingView findView(Container c) {
            return view;
        }

        @Override
        public <T> void setDefaultAttribute(AttributeKey<T> key, T value) {

        }

        @Override
        public <T> T getDefaultAttribute(AttributeKey<T> key) {
            return null;
        }

        @Override
        public void applyDefaultAttributesTo(Figure f) {

        }

        @Override
        public Map<AttributeKey<?>, Object> getDefaultAttributes() {
            return Map.of();
        }

        @Override
        public <T> void setHandleAttribute(AttributeKey<T> key, T value) {

        }

        @Override
        public <T> T getHandleAttribute(AttributeKey<T> key) {
            return null;
        }

        @Override
        public void setInputMap(InputMap newValue) {

        }

        @Override
        public InputMap getInputMap() {
            return null;
        }

        @Override
        public void setActionMap(ActionMap newValue) {

        }

        @Override
        public ActionMap getActionMap() {
            return null;
        }

        @Override
        public void setEnabled(boolean newValue) {

        }

        @Override
        public boolean isEnabled() {
            return false;
        }

        @Override
        public DrawingView getActiveView() {
            return view;
        }

        // Unused methods for this test:
        @Override public void setActiveView(DrawingView newValue) {

        }

        @Override public Set<DrawingView> getDrawingViews() {
            return Collections.singleton(view);
        }

        @Override public void add(DrawingView view) {

        }

        @Override public void remove(DrawingView view) {

        }

        @Override public org.jhotdraw.draw.tool.Tool getTool() {
            return null;
        }

        @Override public void setCursor(Cursor c) {

        }

        @Override public void setTool(org.jhotdraw.draw.tool.Tool t) {

        }

        @Override public void addPropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override public void removePropertyChangeListener(PropertyChangeListener listener) {

        }

        public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {

        }
        public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {

        }}

    /**
     * Fake DrawingView: enabled, no handle, no figure => SelectionTool takes empty-space branch.
     * Records clearSelection + setHandleDetailLevel calls.
     */
    static class FakeDrawingView implements DrawingView {
        private final boolean enabled;

        boolean clearSelectionCalled = false;
        int lastHandleDetailLevel = -1;

        FakeDrawingView(boolean enabled) {
            this.enabled = enabled;
        }

        @Override public boolean isEnabled() { return enabled; }

        // Force SelectionTool to treat click as empty space:
        @Override public Handle findHandle(Point p) { return null; }
        @Override public Figure findFigure(Point p) { return null; }

        @Override public void clearSelection() { clearSelectionCalled = true; }
        @Override public void setHandleDetailLevel(int newValue) { lastHandleDetailLevel = newValue; }

        // Minimal required by SelectionTool.mousePressed flow:
        @Override public Drawing getDrawing() { return null; }
        @Override public Point2D.Double viewToDrawing(Point p) { return new Point2D.Double(p.x, p.y); }
        @Override public Set<Figure> getSelectedFigures() { return Collections.emptySet(); }
        @Override public Collection<Handle> getCompatibleHandles(Handle handle) { return List.of(); }

        // Not needed methods
        @Override public void repaintHandles() {

        }

        @Override public void addPropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override public void removePropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override public void addMouseListener(java.awt.event.MouseListener l) {

        }

        @Override public void removeMouseListener(java.awt.event.MouseListener l) {

        }

        @Override public void addKeyListener(java.awt.event.KeyListener l) {

        }

        @Override public void removeKeyListener(java.awt.event.KeyListener l) {

        }

        @Override public void addMouseMotionListener(java.awt.event.MouseMotionListener l) {

        }

        @Override public void removeMouseMotionListener(java.awt.event.MouseMotionListener l) {

        }

        @Override public void addMouseWheelListener(java.awt.event.MouseWheelListener l) {

        }

        @Override public void removeMouseWheelListener(java.awt.event.MouseWheelListener l) {

        }

        @Override public void setDrawing(Drawing d) {

        }

        @Override public void setCursor(Cursor c) {

        }

        @Override public boolean isFigureSelected(Figure checkFigure) {
            return false;
        }

        @Override public void addToSelection(Figure figure) {

        }

        @Override public void addToSelection(Collection<Figure> figures) {

        }

        @Override public void removeFromSelection(Figure figure) {

        }

        @Override public void toggleSelection(Figure figure) {

        }

        @Override public void selectAll() {

        }

        @Override public int getSelectionCount() {
            return 0;
        }

        @Override public void setActiveHandle(Handle newValue) {

        }
        @Override public Handle getActiveHandle() {
            return null;
        }

        @Override public Collection<Figure> findFigures(Rectangle r) {
            return List.of();
        }

        @Override public Collection<Figure> findFiguresWithin(Rectangle r) {
            return List.of();
        }

        @Override public void addNotify(DrawingEditor editor) {

        }

        @Override public void removeNotify(DrawingEditor editor) {

        }

        @Override public DrawingEditor getEditor() {
            return null;
        }

        @Override public void addFigureSelectionListener(FigureSelectionListener fsl) {

        }

        @Override public void removeFigureSelectionListener(FigureSelectionListener fsl) {

        }

        @Override public void requestFocus() {

        }

        @Override public Point drawingToView(Point2D.Double p) {
            return new Point((int)p.x, (int)p.y);
        }

        @Override public Rectangle drawingToView(Rectangle2D.Double p) {
            return new Rectangle((int)p.x, (int)p.y, (int)p.width, (int)p.height);
        }

        @Override public Rectangle2D.Double viewToDrawing(Rectangle p) {
            return new Rectangle2D.Double(p.x, p.y, p.width, p.height);
        }

        @Override public Constrainer getConstrainer() {
            return null;
        }

        @Override public void setVisibleConstrainer(Constrainer constrainer) {

        }

        @Override public Constrainer getVisibleConstrainer() {
            return null;
        }

        @Override public void setInvisibleConstrainer(Constrainer constrainer) {

        }

        @Override public Constrainer getInvisibleConstrainer() {
            return null;
        }

        @Override public void setConstrainerVisible(boolean newValue) {

        }

        @Override public boolean isConstrainerVisible() {
            return false;
        }

        @Override public JComponent getComponent() {
            return new JPanel();
        }

        @Override public AffineTransform getDrawingToViewTransform() {
            return new AffineTransform();
        }

        @Override public double getScaleFactor() {
            return 1.0;
        }

        @Override public void setScaleFactor(double newValue) {

        }

        @Override public int getHandleDetailLevel() {
            return lastHandleDetailLevel;
        }

        @Override public void setEnabled(boolean newValue) {

        }
    }
}
