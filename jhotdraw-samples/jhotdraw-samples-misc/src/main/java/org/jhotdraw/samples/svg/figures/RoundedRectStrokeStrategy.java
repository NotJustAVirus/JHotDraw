package org.jhotdraw.samples.svg.figures;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Path2D;

public class RoundedRectStrokeStrategy implements RectStrokeStrategy {
    @Override
    public void drawStroke(Graphics2D g, RoundRectangle2D.Double rect) {
        
        Path2D.Double p = new Path2D.Double();
        double aw = rect.arcwidth / 2d;
        double ah = rect.archeight / 2d;

        p.moveTo(rect.x + aw, rect.y);
        p.lineTo(rect.x + rect.width - aw, rect.y);
        p.curveTo(rect.x + rect.width - aw * ACV, rect.y,
                  rect.x + rect.width, rect.y + ah * ACV,
                  rect.x + rect.width, rect.y + ah);

        p.lineTo(rect.x + rect.width, rect.y + rect.height - ah);
        p.curveTo(rect.x + rect.width, rect.y + rect.height - ah * ACV,
                  rect.x + rect.width - aw * ACV, rect.y + rect.height,
                  rect.x + rect.width - aw, rect.y + rect.height);

        p.lineTo(rect.x + aw, rect.y + rect.height);
        p.curveTo(rect.x + aw * ACV, rect.y + rect.height,
                  rect.x, rect.y + rect.height - ah * ACV,
                  rect.x, rect.y + rect.height - ah);

        p.lineTo(rect.x, rect.y + ah);
        p.curveTo(rect.x, rect.y + ah * ACV,
                  rect.x + aw * ACV, rect.y,
                  rect.x + aw, rect.y);

        p.closePath();
        g.draw(p);
    }

    private static final double ACV = computeACV();

    private static double computeACV() {
        double angle = Math.PI / 4.0;
        double a = 1.0 - Math.cos(angle);
        double b = Math.tan(angle);
        double c = Math.sqrt(1.0 + b * b) - 1 + a;
        double cv = 4.0 / 3.0 * a * b / c;
        return 1.0 - cv;
    }
}
