/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starfishcollectorch3;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 *
 * @author m_hay
 */
public class Starfish extends BaseActor{
    
    private boolean collected;
    
    public Starfish(float x, float y, Stage s) {
        super(x, y, s);
        
        loadTexture("assets/starfish.png");
        
        setBoundaryPolygon(8);
        
        Action spin = Actions.rotateBy(30, 1);
        this.addAction(Actions.forever(spin));
        collected = false;
    }
    
    public boolean isCollected(){
        return collected;
    }
    
    public void collect(){
        collected = true;
        clearActions();
        addAction(Actions.fadeOut(1));
        addAction(Actions.after(Actions.removeActor()));
    }
}
