/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package starfishcollectorch3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;

/**
 *
 * @author m_hay
 */
public class BaseActor extends Actor{
    
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;
    private Vector2 velocityVec;
    private Vector2 accelerationVec;
    private float acceleration;
    private float maxSpeed;
    private float deceleration;
    private Polygon boundaryPolygon;

    
    public BaseActor(float x, float y, Stage s){
        //call constructor from Actor class
        super();
        //perform additional initialization tasks
        velocityVec = new Vector2(0,0);
        setPosition(x,y);
        
        accelerationVec = new Vector2(0,0);
        acceleration = 0;
        
        maxSpeed = 1000;
        deceleration = 0;
        
        
        s.addActor(this); 
        animation = null;
        elapsedTime = 0;
        animationPaused = false;
    }
    
    public void setAnimation(Animation<TextureRegion> anim){
        animation = anim;
        TextureRegion tr = animation.getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize(w, h);
        setOrigin(w/2, h/2);
        if (boundaryPolygon == null){
            setBoundaryRectangle();
        }
    }
    
    public void setAnimationPaused(boolean pause){
        animationPaused = pause;
    }
    
    @Override
    public void act(float dt){
        super.act(dt);
        
        if(!animationPaused){
            elapsedTime += dt;
        }
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha){
        
        super.draw(batch, parentAlpha);
        
        //apply tint effect
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        
        if(animation != null && isVisible()){
            batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(), 
                    getOriginX(), getOriginY(), getWidth(), getHeight(), 
                    getScaleX(), getScaleY(), getRotation());
        }
    
    }
    
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, 
            float frameDuration, boolean loop){
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        
        for (int n = 0; n < fileCount; n++){
            String fileName = fileNames[n];
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));    
        }
        
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration,
                textureArray);
        
        if(loop){
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        else {
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        }
        
        if(animation == null){
            setAnimation(anim);
        }
        
        return anim;
    }
    
    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, 
            int rows, int cols, float frameDuration, boolean loop){
        
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() /cols;
        int frameHeight = texture.getHeight() / rows;
        
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        
        Array<TextureRegion> textureArray = new Array<TextureRegion>();
        
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                textureArray.add(temp[r][c]);
            }
        }
        
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        
        if(loop){
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        else {
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        }
        
        if(animation == null){
            setAnimation(anim);
        }
        
        return anim;
    }

    public Animation<TextureRegion> loadTexture(String fileName){
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return loadAnimationFromFiles(fileNames, 1, true);        
    }
    
    public boolean isAnimationFinished(){
        return animation.isAnimationFinished(elapsedTime);
    }
    
    //methods for movement
    public void setSpeed(float speed){
        //if length is zero, then assume motion angle is zero degrees
        if(velocityVec.len() == 0){
            velocityVec.set(speed, 0);
        }
        else{
            velocityVec.setLength(speed);
        }
    }
    
    public float getSpeed(){
        return velocityVec.len();
    }
    
    public void setMotionAngle(float angle){
        velocityVec.setAngle(angle);
    }
    
    public float getMotionAngle(){
       return velocityVec.angle(); 
    }
    
    public boolean isMoving(){
        return (getSpeed() > 0);
    }
    
    public void setAcceleration(float acc){
        acceleration = acc;
    }
    
    public void accelerateAtAngle(float angle){
        accelerationVec.add(new Vector2(acceleration, 0).setAngle(angle));
    }
    
    public void accelerateForward(){
        accelerateAtAngle(getRotation());
    }
    
    public void setMaxSpeed(float ms){
        maxSpeed = ms;
    }
    
    public void setDeceleration(float dec){
        deceleration = dec;
    }
    
    public void applyPhysics(float dt){
        //apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);
        
        float speed = getSpeed();
        
        //decrease speed(decelerate) when not accelerating
        if(accelerationVec.len() == 0){
            speed -= deceleration * dt;
        }
        
        //keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);
        
        //update velocity
        setSpeed(speed);
        
        //apply velocity
        moveBy(velocityVec.x * dt, velocityVec.y * dt);
        
        //reset acceleration
        accelerationVec.set(0,0);
                
    }
    
    public void setBoundaryRectangle(){
        float w = getWidth();
        float h = getHeight();
        float[] vertices = {0,0, w,0, w,h, 0,h};
        boundaryPolygon = new Polygon(vertices);
    }
    
    public void setBoundaryPolygon(int numSides){
        float w = getWidth();
        float h = getHeight();
        
        float[] vertices = new float[2*numSides];
        for(int i = 0; i < numSides; i++){
            float angle = i * 6.28f / numSides;
            //x coordinate
            vertices[2*i] = w/2 * MathUtils.cos(angle) + w/2;
            //y-coordinate
            vertices[2*i+1] = h/2 * MathUtils.sin(angle) + h/2;   
        }
        boundaryPolygon = new Polygon(vertices);
    }
    
    public Polygon getBoundaryPolygon(){
        boundaryPolygon.setPosition(getX(), getY());
        boundaryPolygon.setOrigin(getOriginX(), getOriginY());
        boundaryPolygon.setRotation(getRotation());
        boundaryPolygon.setScale(getScaleX(),getScaleY());
        return boundaryPolygon;
    }
    
    public boolean overlaps(BaseActor other){
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();
        
        //initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())){
            return false;
        }
        return Intersector.overlapConvexPolygons(poly1, poly2);
    }
    
    public void centreAtPosition(float x, float y){
        setPosition(x - getWidth()/2, y - getHeight()/2);
    }
    
    public void centreAtActor(BaseActor other){
        centreAtPosition(other.getX() + other.getWidth()/2, 
                other.getY() + other.getHeight()/2 );
    }
    
    public void setOpacity(float opacity){
        this.getColor().a = opacity;
    }
    
}
