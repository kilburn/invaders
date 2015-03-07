/*
 * @(#)Jugador.java      1.0    2008/30/06
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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;

/**
 * Classe que representa al jugador.
 * 
 * @author Marc Pujol <marc@la3.org>
 */
public class Jugador extends Actor implements KeyListener {
  
  /**
   * Amplada del jugador en dimensions món.
   */
  public static int WIDTH = 40;
  
  /**
   * Alçada del jugador en dimensions món.
   */
  public static int HEIGHT = 60;

  /**
   * Velocitat del jugador quan l'usuari el fa moure
   * (per ara és constant).
   */
  private static double VELOCITAT = 0.5;
  
  /**
   * Velocitat del jugador quan l'usuari el fa moure
   * (per ara és constant).
   */
  private static int MAX_VELOCITAT = 5;
  
  /**
   * Mínim de temps (en milisegons) entre trets efectuats
   * pel jugador.
   */
  private static int MIN_ENTRE_TRETS = 300;
  
  /**
   * Indicadors de direcció.
   */
  private boolean dreta, esquerra;
  
  /**
   * Indicador de temps de l'últim tret, per controlar
   * que el jugador no pugui disparar contínuament.
   */
  private long ultimTret;
  
  /**
   * Crea un nou jugador per al joc indicat.
   * 
   * @param joc Joc en el que participa el nou jugador.
   */
  public Jugador(Joc joc) {
    super(joc);
    this.ultimTret = 0;
    this.dreta = false;
    this.esquerra = false;
  }
  
  @Override public void moure() {
    super.moure();
    this.actualitzaVelocitat();
  }
  
  /**
   * Indica que s'ha produït una col·lisió amb un altre actor.
   * 
   * @param a Actor amb el que s'ha produït la col·lisió.
   */
  public void colisiona(Actor a) {

    // Els trets de jugador no el maten a ell mateix
    if (a instanceof Tret && ((Tret)a).getTipus() == Tret.TRET_JUGADOR) {
      return;
    }
    
    this.setPerEsborrar(true);
  }

  /**
   * Retorna el nom de la imatge que representa al jugador.
   */
  @Override public String getNomImatge() {
    return "Jugador";
  }
  
  /**
   * Retorna l'alçada del jugador en dimensions món.
   * 
   * @return alçada del jugador en dimensions món.
   */
  @Override public int getHeight() {
    return HEIGHT;
  }

  /**
   * Retorna l'amplada del jugador en dimensions món.
   * 
   * @return l'amplada del jugador en dimensions món.
   */
  @Override public int getWidth() {
    return WIDTH;
  }
  
  /**
   * Retorna el perfil d'un invasor en forma d'àrea, per realitzar
   * la detecció de col·lisions.
   */
  public Area getCurrentArea() {
    GeneralPath p = new GeneralPath();
    p.moveTo((float)(posicio.x), (float)(posicio.y + HEIGHT));
    p.lineTo((float)(posicio.x + WIDTH/2), (float)posicio.y);
    p.lineTo((float)(posicio.x + WIDTH), (float)(posicio.y + HEIGHT));
      
    return new Area(p);
  }
  
  /**
   * Actualitza la velocitat del jugador segons les tecles
   * que estan premudes.
   */
  private void actualitzaVelocitat() {
    //this.velocity.x = 0;
    if (esquerra && this.velocity.x > -MAX_VELOCITAT)
      this.velocity.x += -VELOCITAT;
    if (dreta && this.velocity.x < MAX_VELOCITAT)
      this.velocity.x += VELOCITAT;
    if (!esquerra && !dreta && this.velocity.x != 0) {
      this.velocity.x += (this.velocity.x > 0 ? -VELOCITAT : VELOCITAT); 
    }
  }

  /**
   * Funció que es crida quan l'usuari prem una tecla i actualitza
   * la direcció del jugador si es tracta de les fletxes dretes o
   * esquerra.
   * 
   * @param e Event que porta la informació sobre la tecla premuda.
   */
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
    case KeyEvent.VK_LEFT:  esquerra = true; break;
    case KeyEvent.VK_RIGHT: dreta = true; break;
    case KeyEvent.VK_SPACE: this.fire(); return;
    default: return;
    }
    
    //this.actualitzaVelocitat();
  }

  /**
   * Dispara un tret a la posició actual del jugador.
   */
  private void fire() {
    long temps = System.currentTimeMillis();
    if (temps - this.ultimTret >= MIN_ENTRE_TRETS) {
      final Tret t = new Tret(this.joc, Tret.TRET_JUGADOR);
      t.setPosicio(this.posicio.x + WIDTH/2 - Tret.WIDTH/2, this.posicio.y - Tret.HEIGHT);
      t.setVelocity(0, -2.5);
      this.joc.afegirActor(t);
      this.ultimTret = temps;
    }
  }

  /**
   * Funció que es crida quan l'usuari allibera una tecla i actualitza
   * la direcció del jugador si es tracta de les fletxes dretes o
   * esquerra.
   * 
   * @param e Event que porta la informació sobre la tecla alliberada.
   */
  public void keyReleased(KeyEvent e) {
    switch(e.getKeyCode()) {
    case KeyEvent.VK_LEFT:  esquerra = false; break;
    case KeyEvent.VK_RIGHT: dreta = false; break;
    default: return;
    }
    
    //this.actualitzaVelocitat();
  }

  /**
   * No utilitzat, aquest mètode només hi és per complir
   * amb la interfície <code>KeyListener</code>.
   */
  public void keyTyped(KeyEvent e) {}
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb el jugador donat.
   * 
   * @param jugador jugador amb el que s'ha de comprovar si col·lisionem.
   * @return <code>false</code> perquè el jugador mai xoca amb ell mateix.
   */
  public boolean hasColisio(Jugador jugador){
    return false;
  }
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb el tret donat.
   * 
   * @param tret tret amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest tret, <code>false</code> sinó.
   */
  public boolean hasColisio(Tret tret){
    Area a = this.getArea();
    return a.intersects(tret.posicio.x, tret.posicio.y, Tret.WIDTH, Tret.HEIGHT);
  }
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb l'invasor donat.
   * 
   * @param invasor invasor amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest invasor, <code>false</code> sinó.
   */
  public boolean hasColisio(Invasor invasor){
    Area a = new Area(this.getArea());
    Area b = invasor.getArea();
    a.intersect(b);
    return !a.isEmpty();
  }

}
