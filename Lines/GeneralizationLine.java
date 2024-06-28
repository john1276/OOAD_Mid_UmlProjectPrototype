package Lines;

import java.awt.*;

public class GeneralizationLine extends BaseLine{
    public GeneralizationLine(int Sx, int Sy, int Ex, int Ey){
        super(Sx, Sy, Ex, Ey);
    }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(StartX,StartY,EndX,EndY);
        int x1=StartX, y1=StartY, x2=EndX, y2=EndY;
        int dx = x2 - x1, dy = y2 - y1;
        int d=ArrowLength,h=ArrowLength;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.drawPolygon(xpoints, ypoints, 3);
    }
}
