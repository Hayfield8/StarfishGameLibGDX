/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starfishcollectorch3;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 *
 * @author m_hay
 */
public abstract class GameBeta extends Game{
    
    protected Stage mainStage;

    @Override
    public void create() {
        mainStage = new Stage();
        initialize();
    }
    
    public abstract void initialize();
    
    @Override
    public void render(){
        float dt = Gdx.graphics.getDeltaTime();
        
        //act method
        mainStage.act(dt);
        
        //defined by user
        update(dt);
        
        //clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //draw the graphics
        mainStage.draw();                                                                                             
    }
    
    public abstract void update(float dt);
    
}

