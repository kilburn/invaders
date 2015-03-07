/*
 * @(#)Finestra.java      1.0    2008/30/06
 *  
 * Copyright (c) 2008, Marc Pujol <marc@la3.org>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name Marc Pujol nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package es.uab.pa.invaders;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import es.uab.pa.invaders.worldview.World;

/**
 * Finestra principal del joc, on es motra la pantalla
 * i les diferents opcions.
 * 
 * @author Marc Pujol <marc@la3.org>
 */
public class Finestra extends Frame {
  
  /**
   * Identificador perquè sigui serialitzable. 
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * Amplada per defecte de la pantalla.
   */
  public static int WIDTH = 500;
  
  /**
   * Alçada per defecte de la pantalla.
   */
  public static int HEIGHT = 500;

  /**
   * Buffer "off-screen" on es dibuixa el joc abans de
   * mostrar-lo per pantalla, evitant així efectes 
   * de parpadeig indesitjats.
   */
  Image buffer;
  
  /**
   * Conversor món-pantalla.
   */
  private World mon;
  
  /**
   * Referència a l'objecte director del joc.
   */
  private Joc joc;
  
  /**
   * Constructor per defecte, que inicialitza el joc.
   */
  public Finestra() {
    
    // Inicialitzem el "món"
    this.mon = new World();
    this.mon.setYInverted(true);
    this.mon.setDimensionView(WIDTH, HEIGHT, 0, 0);
    this.mon.setDimensionWorld(Joc.WIDTH, Joc.HEIGHT, 0, 0);
    
    this.setPreferredSize(this.mon.getRectangleView().getSize());
    this.pack();
    
    // Inicialitzem el buffer
    this.buffer = this.createImage(1, 1);
    this.creaBuffer();
    
    // Observador que s'encarrega de finalitzar el programa en
    // tancar la finestra principal.
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    
    // Observador que s'encarrega de bloquejar l'aspect ratio de la
    // finestra de joc i recrear el buffer off-screen quan el tamany
    // de la pantalla canvia.
    this.addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent e) {
        
        // Sempre hauria de ser tipus finestra.
        final Component c  = e.getComponent();
        if (!(c instanceof Finestra)) {
          return;
        }
        final Finestra f = (Finestra)c;
        
        // Bloquegem l'aspect ratio
        Dimension d  = f.getSize();
        double ratio = d.getWidth() / d.getHeight();
        if (ratio > 1) {
          c.setSize(d.width, (int)(d.height*ratio));
        } else if (ratio < 1) {
          f.setSize((int)(d.width/ratio), d.height);
        }
        d = f.getSize();
        
        // Redimensionem el "mon"
        f.getMon().setDimensionView(d.width, d.height, 0, 0);
        
        // Creem un nou buffer pel nou tamany
        creaBuffer();
        
        // Regenerem les imatges
        if (joc != null) {
          joc.regeneraImatges();
        }
        
      }
      
    });
    
    this.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (joc != null && joc.isAcabat()) {
          switch(e.getKeyCode()) {
          case KeyEvent.VK_T:
            joc = new Joc(Finestra.this);
            new Thread(joc).start();
            break;
          }
        }
      }
    });
    
    this.setVisible(true);
    this.joc = new Joc(this);
    new Thread(joc).start();
  }
  
  /**
   * Crea el buffer fora de pantalla per a permetre
   * utilitzar la tècnica del doble-buffer.
   */
  private void creaBuffer() {
    final int ample = this.mon.VXmax - this.mon.VXmin;
    final int alt   = this.mon.VYmax - this.mon.VXmin;
    synchronized (this.buffer) {
      this.buffer = this.createImage(ample, alt);
      Graphics2D g = (Graphics2D)this.buffer.getGraphics();
      g.setColor(Colors.FONS);
      g.fillRect(0, 0, ample, alt);
    }
  }

  /**
   * Redibuixa la finestra.
   */
  @Override public void paint(Graphics g) {
    synchronized (this.buffer) {
      g.drawImage(this.buffer, 0, 0, this); 
    }
  }

  /**
   * Retorna el buffer fora de pantalla que utilitzem
   * per a realitzar el doble-buffering.
   * 
   * @return buffer fora de pantalla.
   */
  public Image getBuffer() {
    return this.buffer;
  }
  
  /**
   * Retorna l'objecte conversor mon-pantalla-mon.
   * 
   * @return conversor mon-pantalla-mon.
   */
  public World getMon() {
    return this.mon;
  }
  
  /**
   * Estableix un nou buffer fora de pantalla per a 
   * realitzar el doble-buffering.
   * 
   * @param buffer nou buffer fora de pantalla.
   */
  public void setBuffer(Image buffer) {
    this.buffer = buffer;
  }

}
