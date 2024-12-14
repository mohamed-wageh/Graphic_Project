/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.media.opengl.GLEventListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Tyba
 */

public abstract class Listener implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {
 
    protected String assetsFolderName = "Assets//Alphabet";
    
}
