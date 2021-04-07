/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starfishcollectorch3;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *
 * @author m_hay
 */
public class Whirlpool extends BaseActor{
    
    public Whirlpool(float x, float y, Stage s) {
        super(x, y, s);
        
        loadAnimationFromSheet("assets/whirlpool.png", 2, 5, 0.1f, false);
    }
    
    public void act(float dt){
        super.act(dt);
        
        if (isAnimationFinished()){
            remove();
        }
    }
    
    
    
    
}
