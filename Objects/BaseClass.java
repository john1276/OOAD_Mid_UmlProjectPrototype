package Objects;

import java.awt.*;

public class BaseClass extends BaseObject{
    public BaseClass(int x,int y){
        super(x,y);

    }
    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(this.Posx, this.Posy, this.width, this.height);
        g.setColor(Color.BLACK);
        g.drawString(this.name, this.Posx + this.width / 4, this.Posy + this.height / 4);
    }
}
