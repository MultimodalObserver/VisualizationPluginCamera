/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamvisualizationplugin;

import java.awt.BorderLayout;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javax.swing.JPanel;

/**
 *
 * @author Khrikhri
 */
public class PanelCamara extends JPanel{
    
    private File MEDIA_URL;
    private Media media;
    private MediaPlayer mediaPlayer;
    MediaView mediaView;
    public long end;
    private boolean isPlaying=false;
    private boolean isSync = false;
    double velocidad = 1;
    long seekSlider=0;
    int cambio = 0;
    double deltaT;
     
    private void initFxLater(JFXPanel panel){
        Group root = new Group();
         
        try {
            // create media player
            media = new Media(MEDIA_URL.toURI().toURL().toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(PanelCamara.class.getName()).log(Level.SEVERE, null, ex);
        }
        mediaPlayer = new MediaPlayer(media);   
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                end=(long)media.getDuration().toMillis();
            }
        });
        //mediaPlayer.setRate(0.9);
               // create mediaView and add media player to the viewer
        mediaView = new MediaView(mediaPlayer);
                DoubleProperty width = mediaView.fitWidthProperty();
                DoubleProperty height = mediaView.fitHeightProperty();
                width.bind(Bindings.selectDouble(mediaView.sceneProperty(),"width"));
                height.bind(Bindings.selectDouble(mediaView.sceneProperty(),"height"));
        Scene scene = new Scene(root, mediaView.getFitWidth(), mediaView.getFitHeight());
        ((Group)scene.getRoot()).getChildren().add(mediaView);
         
        panel.setScene(scene);
    }
    
    
    public PanelCamara(File file){
        
        MEDIA_URL = file;
        final JFXPanel jFXPanel = new JFXPanel();
        this.setLayout(new BorderLayout());
        initFxLater(jFXPanel);
        this.setSize(640,480);         
        add(jFXPanel);
    }
 
    public JPanel getPanel() {
        return this;
    }
    
    public void play(long millis){
        if(millis<end){
            mediaPlayer.play();
            isPlaying=true;           
            
             /*if(!isPlaying){
                mediaPlayer.setRate(velocidad);
                velocidad=1;
                mediaPlayer.play();
                isPlaying=true;
            }
             if(isSync){
                if(millis%150==0){            
                    deltaT = mediaPlayer.getCurrentTime().toMillis()-millis;
                    if((int)deltaT>0){
                        if(velocidad>=0.1){
                            velocidad = velocidad-0.05;
                            mediaPlayer.setRate(velocidad);                    
                        }
                    }else if((int)deltaT<0){
                        if(velocidad<=1.1){
                            velocidad = velocidad+0.05;       
                            mediaPlayer.setRate(velocidad);             
                        }
                    } 
                }                 
            }*/   
        }         
    }
    
    public void play2(long millis){
        if(mediaPlayer.getCurrentTime().toMillis()>millis){
            mediaPlayer.pause();
        }
    }
    
    public void stop(){
        mediaPlayer.stop();
        isPlaying=false;
    }
    public void pause(){
        mediaPlayer.pause();
        isPlaying=false;
    }
    public void sync(boolean sync){
        isSync=sync;
    }
    
    public void current(long sw){
        new Thread(new Runnable() {

            @Override
            public void run() {
                    mediaPlayer.seek(Duration.millis(sw)); 
            }
        }).start();
    } 
}
