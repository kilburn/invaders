/*
 * @(#)Tret.java      1.0    2008/30/06
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

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * Classe que representa els trets del joc.
 * 
 * @author Marc Pujol <marc@la3.org>
 */
public class Tret extends Actor {
  
  /**
   * Amplada del jugador en dimensions món.
   */
  public static int WIDTH = 5;
  
  /**
   * Alçada del jugador en dimensions món.
   */
  public static int HEIGHT = 10;
  
  /**
   * Tipus dels trets efectuats pel jugador.
   */
  public static int TRET_JUGADOR = 0;
  
  /**
   * Tipus dels trets efectuats per els invasors.
   */
  public static int TRET_INVASOR = 1;
  
  /**
   * Tipus d'aquest tret
   */
  private int tipus;
  
  /**
   * Construeix un nou tret per al joc indicat.
   * 
   * @param joc Joc en el que actua aquest tret.
   */
  public Tret(Joc joc, int tipus) {
    super(joc);
    this.tipus = tipus;
  }
  
  /**
   * Avança "un pas" el tret.
   */
  @Override public void moure() {
    super.moure();
    if (this.posicio.y >= Joc.HEIGHT || this.posicio.y <= 0) {
      this.setPerEsborrar(true);
    }
  }
  
  /**
   * Indica que s'ha produït una col·lisió amb un altre actor.
   * 
   * @param a Actor amb el que s'ha produït la col·lisió.
   */
  public void colisiona(Actor a) {

    // Els trets de jugador no el maten a ell mateix
    if (this.tipus == TRET_JUGADOR && a instanceof Jugador) {
      return;
    }
    // Ni els d'invasor a invasors
    if (this.tipus == TRET_INVASOR && a instanceof Invasor) {
      return;
    }
    
    this.setPerEsborrar(true);
  }

  /**
   * Retorna el nom de la imatge que representa
   * el tret en el seu estat actual.
   * 
   * @return String nom de la imatge.
   */
  @Override public String getNomImatge() {
    return "Tret";
  }
  
  /**
   * Retorna l'alçada d'un tret en dimensions món.
   * 
   * @return alçada d'un tret en dimensions món.
   */
  @Override public int getHeight() {
    return HEIGHT;
  }

  /**
   * Retorna l'amplada d'un tret en dimensions món.
   * 
   * @return l'amplada d'un tret en dimensions món.
   */
  @Override public int getWidth() {
    return WIDTH;
  }
  
  /**
   * Retorna el perfil d'un tret en forma d'àrea, per realitzar
   * la detecció de col·lisions.
   */
  public Area getCurrentArea() {
    return new Area(new Rectangle2D.Double(posicio.x, posicio.y, WIDTH, HEIGHT));
  }

  /**
   * Retorna el tipus d'aquest tret.
   * 
   * @return tipus d'aquest tret.
   */
  public int getTipus() {
    return tipus;
  }
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb el jugador donat.
   * 
   * @param jugador jugador amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest jugador, <code>false</code> sinó.
   */
  public boolean hasColisio(Jugador jugador){
    Area a = jugador.getArea();
    return a.intersects(this.posicio.x, this.posicio.y, Tret.WIDTH, Tret.HEIGHT);
  }
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb el tret donat.
   * 
   * @param tret tret amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest tret, <code>false</code> sinó.
   */
  public boolean hasColisio(Tret tret){
    return this.tipus != tret.getTipus() 
        && this.getBox().intersects(tret.getBox());
  }
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb l'invasor donat.
   * 
   * @param invasor invasor amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest invasor, <code>false</code> sinó.
   */
  public boolean hasColisio(Invasor invasor){
    Area a = invasor.getArea();
    return a.contains(this.posicio.x, this.posicio.y);
  }

}
