package pro.ui.activity.xkgl.testbean;

import java.io.Serializable;

/**
 * Created by Tabjin on 2017/6/29.
 * Description:
 * What Changed:
 */
public class MineCall implements Serializable{
   public String name;
    public int state;

    public MineCall(String name, int state) {
        this.name = name;
        this.state = state;
    }

    public MineCall() {
    }
}
