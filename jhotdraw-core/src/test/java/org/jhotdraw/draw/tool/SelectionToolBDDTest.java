package org.jhotdraw.draw.tool;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.junit5.ScenarioTest;
import org.jhotdraw.draw.*;
import org.jhotdraw.draw.event.FigureSelectionListener;
import org.jhotdraw.draw.event.HandleListener;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.handle.Handle;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SelectionToolBDDTest extends ScenarioTest<
        SelectionToolBDDTest.GivenSelection,
        SelectionToolBDDTest.WhenSelection,
        SelectionToolBDDTest.ThenSelection> {

    @Test
    void click_empty_space_without_shift_clears_selection() {
        given().an_enabled_view_with_no_handle_and_no_figure()
                .and().a_real_selection_tool_is_active();

        when().the_user_clicks_empty_space_without_shift();

        then().selection_is_cleared()
                .and().handle_detail_level_is_reset_to_0();
    }

    @Test
    void click_empty_space_with_shift_keeps_selection() {
        given().an_enabled_view_with_no_handle_and_no_figure()
                .and().a_real_selection_tool_is_active();

        when().the_user_clicks_empty_space_with_shift();

        then().selection_is_not_cleared();
    }

    @Test
    void click_on_handle_does_not_clear_selection() {
        given().an_enabled_view_with_a_handle_under_cursor()
                .and().a_real_selection_tool_is_active();

        when().the_user_clicks_empty_space_without_shift();

        then().selection_is_not_cleared();
    }

    static class GivenSelection extends Stage<GivenSelection> {

        @ProvidedScenarioState FakeDrawingView view;
        @ProvidedScenarioState DrawingEditor editor;
        @ProvidedScenarioState SelectionTool tool;

        GivenSelection an_enabled_view_with_no_handle_and_no_figure() {
            view = new FakeDrawingView(true);
            view.handleUnderCursor = null;
            view.figureUnderCursor = null;
            editor = new FakeDrawingEditor(view);
            return self();
        }

        GivenSelection an_enabled_view_with_a_handle_under_cursor() {
            view = new FakeDrawingView(true);
            view.handleUnderCursor = new FakeHandle();
            view.figureUnderCursor = null;
            editor = new FakeDrawingEditor(view);
            return self();
        }

        GivenSelection a_real_selection_tool_is_active() {
            tool = new SelectionTool();
            tool.activate(editor);
            return self();
        }
    }

    static class WhenSelection extends Stage<WhenSelection> {

        @ExpectedScenarioState SelectionTool tool;

        WhenSelection the_user_clicks_empty_space_without_shift() {
            tool.mousePressed(mouseEvent(false));
            return self();
        }

        WhenSelection the_user_clicks_empty_space_with_shift() {
            tool.mousePressed(mouseEvent(true));
            return self();
        }
    }

    static class ThenSelection extends Stage<ThenSelection> {

        @ExpectedScenarioState FakeDrawingView view;

        ThenSelection selection_is_cleared() {
            assertThat(view.clearSelectionCalled).isTrue();
            return self();
        }

        ThenSelection selection_is_not_cleared() {
            assertThat(view.clearSelectionCalled).isFalse();
            return self();
        }

        ThenSelection handle_detail_level_is_reset_to_0() {
            assertThat(view.lastHandleDetailLevel).isEqualTo(0);
            return self();
        }
    }

    // -------------------- helpers --------------------
    static MouseEvent mouseEvent(boolean shiftDown) {
        int modifiers = shiftDown ? InputEvent.SHIFT_DOWN_MASK : 0;
        return new MouseEvent(
                new Panel(), // Container required by AbstractTool
                MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(),
                modifiers,
                10, 10,
                1,
                false
        );
    }

    // -------------------- minimal fakes (allowed) --------------------

    static class FakeDrawingEditor implements DrawingEditor {
        private final DrawingView view;

        FakeDrawingEditor(DrawingView view) {
            this.view = view;
        }

        @Override public DrawingView findView(Container c) { return view; }

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

        @Override public DrawingView getActiveView() { return view; }

        // keep ONLY methods that exist in your DrawingEditor interface
        @Override public void setActiveView(DrawingView newValue) {

        }

        @Override public Set<DrawingView> getDrawingViews() {
            return Collections.singleton(view);
        }

        @Override public void add(DrawingView v) {

        }

        @Override public void remove(DrawingView v) {

        }

        @Override public org.jhotdraw.draw.tool.Tool getTool() {
            return null;
        }

        @Override
        public void setCursor(Cursor c) {

        }

        @Override public void setTool(org.jhotdraw.draw.tool.Tool t) {

        }

        @Override public void addPropertyChangeListener(PropertyChangeListener listener) { }
        @Override public void removePropertyChangeListener(PropertyChangeListener listener) { }
    }

    static class FakeDrawingView implements DrawingView {
        private final boolean enabled;

        boolean clearSelectionCalled = false;
        int lastHandleDetailLevel = -1;

        Handle handleUnderCursor;
        Figure figureUnderCursor;

        FakeDrawingView(boolean enabled) { this.enabled = enabled; }

        @Override public boolean isEnabled() { return enabled; }

        @Override
        public void repaintHandles() {

        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {

        }

        @Override
        public void addMouseListener(MouseListener l) {

        }

        @Override
        public void removeMouseListener(MouseListener l) {

        }

        @Override
        public void addKeyListener(KeyListener l) {

        }

        @Override
        public void removeKeyListener(KeyListener l) {

        }

        @Override
        public void addMouseMotionListener(MouseMotionListener l) {

        }

        @Override
        public void removeMouseMotionListener(MouseMotionListener l) {

        }

        @Override
        public void addMouseWheelListener(MouseWheelListener l) {

        }

        @Override
        public void removeMouseWheelListener(MouseWheelListener l) {

        }

        @Override public Handle findHandle(Point p) { return handleUnderCursor; }
        @Override public Figure findFigure(Point p) { return figureUnderCursor; }

        @Override
        public Collection<Figure> findFigures(Rectangle r) {
            return List.of();
        }

        @Override
        public Collection<Figure> findFiguresWithin(Rectangle r) {
            return List.of();
        }

        @Override
        public void addNotify(DrawingEditor editor) {

        }

        @Override
        public void removeNotify(DrawingEditor editor) {

        }

        @Override
        public DrawingEditor getEditor() {
            return null;
        }

        @Override
        public void addFigureSelectionListener(FigureSelectionListener fsl) {

        }

        @Override
        public void removeFigureSelectionListener(FigureSelectionListener fsl) {

        }

        @Override
        public void requestFocus() {

        }

        @Override
        public Point drawingToView(Point2D.Double p) {
            return null;
        }

        @Override public void clearSelection() { clearSelectionCalled = true; }

        @Override
        public void selectAll() {

        }

        @Override public void setHandleDetailLevel(int newValue) { lastHandleDetailLevel = newValue; }

        @Override
        public int getHandleDetailLevel() {
            return 0;
        }

        @Override
        public void setEnabled(boolean newValue) {

        }

        // minimal methods SelectionTool touches
        @Override public Drawing getDrawing() { return null; }

        @Override
        public void setDrawing(Drawing d) {

        }

        @Override
        public void setCursor(Cursor c) {

        }

        @Override
        public boolean isFigureSelected(Figure checkFigure) {
            return false;
        }

        @Override
        public void addToSelection(Figure figure) {

        }

        @Override
        public void addToSelection(Collection<Figure> figures) {

        }

        @Override
        public void removeFromSelection(Figure figure) {

        }

        @Override
        public void toggleSelection(Figure figure) {

        }

        @Override public Point2D.Double viewToDrawing(Point p) { return new Point2D.Double(p.x, p.y); }

        @Override
        public Rectangle drawingToView(Rectangle2D.Double p) {
            return null;
        }

        @Override
        public Rectangle2D.Double viewToDrawing(Rectangle p) {
            return null;
        }

        @Override
        public Constrainer getConstrainer() {
            return null;
        }

        @Override
        public void setVisibleConstrainer(Constrainer constrainer) {

        }

        @Override
        public Constrainer getVisibleConstrainer() {
            return null;
        }

        @Override
        public void setInvisibleConstrainer(Constrainer constrainer) {

        }

        @Override
        public Constrainer getInvisibleConstrainer() {
            return null;
        }

        @Override
        public void setConstrainerVisible(boolean newValue) {

        }

        @Override
        public boolean isConstrainerVisible() {
            return false;
        }

        @Override
        public JComponent getComponent() {
            return null;
        }

        @Override
        public AffineTransform getDrawingToViewTransform() {
            return null;
        }

        @Override
        public double getScaleFactor() {
            return 0;
        }

        @Override
        public void setScaleFactor(double newValue) {

        }

        @Override public Set<Figure> getSelectedFigures() { return Collections.emptySet(); }

        @Override
        public int getSelectionCount() {
            return 0;
        }

        @Override public Collection<Handle> getCompatibleHandles(Handle handle) { return List.of(); }

        @Override
        public void setActiveHandle(Handle newValue) {

        }

        @Override
        public Handle getActiveHandle() {
            return null;
        }


    }

    static class FakeHandle implements Handle {

        @Override
        public Figure getOwner() {
            return null;
        }

        @Override
        public void setView(DrawingView view) {

        }

        @Override
        public void addHandleListener(HandleListener l) {

        }

        @Override
        public void removeHandleListener(HandleListener l) {

        }

        @Override
        public Rectangle getBounds() {
            return null;
        }

        @Override
        public Rectangle getDrawingArea() {
            return null;
        }

        @Override
        public boolean contains(Point p) {
            return false;
        }

        @Override
        public void draw(Graphics2D g) {

        }

        @Override
        public void invalidate() {

        }

        @Override
        public void dispose() {

        }

        @Override
        public Cursor getCursor() {
            return null;
        }

        @Override
        public boolean isCombinableWith(Handle handle) {
            return false;
        }

        @Override
        public void trackStart(Point anchor, int modifiersEx) {

        }

        @Override
        public void trackStep(Point anchor, Point lead, int modifiersEx) {

        }

        @Override
        public void trackEnd(Point anchor, Point lead, int modifiersEx) {

        }

        @Override
        public void trackDoubleClick(Point p, int modifiersEx) {

        }

        @Override
        public void viewTransformChanged() {

        }

        @Override
        public Collection<Handle> createSecondaryHandles() {
            return List.of();
        }

        @Override
        public String getToolTipText(Point p) {
            return "";
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}
