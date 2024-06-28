package Objects;

import java.awt.*;

public class UseCase extends BaseObject{
    public UseCase(int x,int y){
        super(x,y);
        init();
    }
    @Override
    public void paintComponent(Graphics g){
        g.setColor(Color.GRAY);
        g.fillOval(this.Posx, this.Posy, this.width, this.height);
        g.setColor(Color.BLACK);
        g.drawString(this.name, this.Posx + this.width / 4, this.Posy + this.height / 4);
    }
}
