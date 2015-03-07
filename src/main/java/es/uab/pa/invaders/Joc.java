/*
 * @(#)Joc.java      1.0    2008/30/06
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

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import es.uab.pa.invaders.worldview.World;

/**
 * Classe que representa el propi joc, l'inicia, l'atura
 * i coordina els moviments de tots els actors implicats.
 * 
 * @author Marc Pujol <marc@la3.org>
 */
public class Joc implements Runnable {
  
  /**
   * Ajust (invers) de velocitat del joc.
   */
  private static int FPS = 80;
  
  /**
   * Amplada del món.
   */
  public static double WIDTH = 1000;
  
  /**
   * Alçada del món.
   */
  public static double HEIGHT = 1000;
  
  /**
   * Indicador de joc en curs.
   */
  public static int JOC_EN_CURS = 0;
  
  /**
   * Indicador de joc guanyat.
   */
  public static int JOC_GUANYAT = 1;
  
  /**
   * Indicador de joc perdut.
   */
  public static int JOC_PERDUT = 2;
  
  /**
   * Finestra on es mostra el joc.
   */
  private Finestra finestra;
  
  /**
   * Llista d'actors que estan intervenint en el joc.
   */
  private ArrayList<Actor> actors;
  
  /**
   * Cache/Generador d'imatges.
   */
  private CacheImatges cacheImatges;
  
  /**
   * Frames per segon.
   */
  private int fps = 0;
  
  /**
   * Estat actual del joc.
   */
  private int estat;
  
  /**
   * Conversor mon-pantalla i pantalla-món
   */
  private World mon;
  
  /**
   * Constructor per defecte, que llença un nou joc en la
   * finestra donada.
   * 
   * @param f Finestra on s'ha de mostrar el joc.
   */
  public Joc(Finestra f) {
    this.finestra = f;
    this.mon = f.getMon();
  }

  /**
   * Inicialitza el joc.
   */
  private void inici() {
    this.estat = JOC_EN_CURS;
    this.cacheImatges = new CacheImatges(this);
    this.actors = new ArrayList<Actor>();
    
    // Variables últils per afegir actors
    final int n_invasors = 10;
    final int n_files    = 3;
    final double xinc   = WIDTH/(n_invasors+5);
    
    // Invasors
    double ypos   = Invasor.HEIGHT*2.5;
    for (int i=0; i<n_files; i++) {
      double xbase  = xinc * (2 + .5*(i%2));
      for (int j=0; j<n_invasors; j++) {
        Invasor inv = new Invasor(this, xinc*2);
        inv.setPosicio(xbase, ypos);
        inv.setVelocity(1, 0.08);
        this.actors.add(inv);
        xbase += xinc;
      }
      ypos += Invasor.HEIGHT*1.5;
    }
    
    // Jugador
    Jugador jugador = new Jugador(this);
    jugador.setPosicio(WIDTH/2 - Jugador.WIDTH/2, HEIGHT - Jugador.HEIGHT);
    this.finestra.addKeyListener(jugador);
    this.actors.add(jugador);
  }
  
  /**
   * Realitza els moviments de tots els actors del joc.
   */
  private void moviments() {
    int len = this.actors.size()-1;
    for (int i=len; i>=0; i--) {
      this.actors.get(i).moure();
    }
  }
  
  /**
   * Comprova les possibles col·lisions entre actors, desencadenant
   * el que sigui convenient.
   */
  private void colisions() {
    final int len = this.actors.size();
    for (int i=0; i<len-1; i++) {
      final Actor a1 = this.actors.get(i);
      for (int j=i+1; j<len; j++) {

        final Actor a2 = this.actors.get(j);
        if (a1.hasColisio(a2)){ 
          a1.colisiona(a2);
          a2.colisiona(a1);
        }
        
      }
    }
  }
  
  /**
   * "Neteja" el joc, eliminant els actors pendents d'esborrar
   * (perquè han desaparegut i/o mort) i comprovem que quedi
   * almenys un invasor i el jugador, o el donem per acabat.
   */
  private void netejar() {
    final int max = this.actors.size()-1;
    int n_invasors = 0;
    for (int i=max; i>=0; i--) {
      final Actor a = this.actors.get(i);
      if (a.isPerEsborrar()) {
        this.actors.remove(i);
        if (a instanceof Jugador) {
          this.estat = JOC_PERDUT;
        }
      } else if (a instanceof Invasor) {
        n_invasors++;
      }
    }
    
    // Mirem si no queden invasors -> Jugador ha guanyat!
    if (n_invasors == 0) {
      this.estat = JOC_GUANYAT;
    }
  }

  /**
   * Redibuixa l'estat del joc a la imatge que fa de
   * <em>buffer offscreen</em>.
   */
  private void repintar() {
    Image buffer = finestra.getBuffer();
    
    // Joc i gui corren en threads diferents, així que hem
    // de controlar l'accés al buffer.
    synchronized(buffer) {
      Graphics g = buffer.getGraphics();
      g.setColor(Colors.FONS);
      g.fillRect(0, 0, buffer.getWidth(null), buffer.getHeight(null));
      
      synchronized(this.actors) {
        // Ho recorrem invers perquè surtin els trets a sobre tot
        // lo altre
        final int len = this.actors.size()-1;
        for(int i=len; i>=0; i--) {
          this.pinta(g, this.actors.get(i));
        }
      }
      
      // Mostrar fps
      g.setColor(Colors.JUGADOR_TRAS);
      g.drawString(this.fps + " fps", 0, 50);
    }
  }

  /**
   * Funció principal del joc, que l'inicialitza i el fa córrer
   * fins al final.
   */
  public void run() {
    inici();
    while(this.estat == JOC_EN_CURS) {
      long ultimTemps = System.currentTimeMillis();
      this.moviments();
      this.colisions();
      this.netejar();
      this.repintar();
      this.finestra.repaint();
      long t = 1000/FPS - System.currentTimeMillis() + ultimTemps;
      if (t>0) {
        try {
          Thread.sleep(t);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      ultimTemps = System.currentTimeMillis() - ultimTemps;
      this.fps = ultimTemps==0 ? 0 : (int)(1000/ultimTemps);
    }
    fi();
  }
  
  /**
   * Indica que el joc s'ha acabat i el seu resultat
   */
  private void fi() {
    Image buffer = this.finestra.getBuffer();
    synchronized(buffer) {
      
      Graphics2D g = (Graphics2D)buffer.getGraphics();
      
      final double ratio = (mon.VXmax - mon.VXmin)/WIDTH;
      g.setTransform(AffineTransform.getScaleInstance(ratio, ratio));
      
      g.setColor(Colors.BANDA);
      g.fillRect(0, (int)(HEIGHT/2 - HEIGHT/20), (int)WIDTH, (int)(HEIGHT/9));
      
      g.setColor(Colors.JUGADOR_TRAS);
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      final String msg = (this.estat == JOC_GUANYAT) ? "guanyat!" : "perdut!";
      g.setFont(g.getFont().deriveFont(Font.BOLD).deriveFont(50f));
      g.drawString("Has " + msg, (int)(WIDTH/2 - 150), (int)(HEIGHT/2 + 10));
      g.setFont(g.getFont().deriveFont(20f));
      g.drawString("Prem -t- per tornar a començar", (int)(WIDTH/2 - 160), (int)(HEIGHT/2 + 40));
    }
    this.finestra.repaint();
  }


  /**
   * Dibuixa un actor utilitzant l'objecte graphics donat.
   * 
   * @param g Objecte graphics amb el que dibuixar.
   * @param a Actor a dibuixar.
   */
  public void pinta(Graphics g, Actor a) {
    Image i = this.cacheImatges.get(a.getNomImatge());
    if (i != null) {
      final Point p = this.mon.worldToView(a.getPosicio());
      g.drawImage(i, p.x, p.y, null);
    }
  }

  /**
   * Força el joc a regenerar les imatges, utilitzat quan la
   * finestra és redimensionada.
   */
  public void regeneraImatges() {
    this.cacheImatges.esborra();
  }
  
  /**
   * Afegeix un nou actor al joc.
   * 
   * @param actor Actor a afegir.
   */
  public void afegirActor(Actor a) {
    if (a==null) {
      throw new RuntimeException("Estàs flipant!");
    }
    synchronized(this.actors) {
      this.actors.add(a);
    }
  }

  /**
   * Indica si el joc està acabat o no.
   * 
   * @return <code>true</code> si el joc està acabat, <code>false</code> sinó.
   */
  public boolean isAcabat() {
    return this.estat > JOC_EN_CURS;
  }
  
  /**
   * Força el final del joc.
   * 
   * @param estat nou estat del joc.
   */
  public void setAcabat(int estat) {
    this.estat = estat;
  }
  
  /**
   * Retorna l'objecte conversor pantalla-mon-pantalla.
   * 
   * @return objecte conversor pantalla-mon-pantalla.
   */
  public World getMon() {
    return this.mon;
  }

}
