/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starfishcollectorch3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 *
 * @author m_hay
 */
public class Launcher {
    public static void main(String[] args){
        Game myGame = new StarfishCollector();
        LwjglApplication launcher = new LwjglApplication(myGame, "Starfish COllector", 800, 600);
    }
    
}

