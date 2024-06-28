package Objects;

import java.util.ArrayList;

public class GropObject extends BaseObject{
    ArrayList<BaseObject> ContainObjs=new ArrayList<>();

    public GropObject(int x1, int y1,int width,int height) {
        super(x1, y1,width,height);
        init();
        this.setDecorationVisible(true);
    }
    public void AddObj(BaseObject Obj){
        ContainObjs.add(Obj);
    }
    public void ClearObj(){
        ContainObjs.clear();
    }
    public void MoveToNewPosition(int deltaX, int deltaY) {
        this.Posx += deltaX;
        this.Posy += deltaY;
        this.DecObj.MoveToNewPosition(deltaX,deltaY);
        for(int i=0;i<ContainObjs.size();i++)
        {
            ContainObjs.get(i).MoveToNewPosition(deltaX,deltaY);
        }
    }
}
