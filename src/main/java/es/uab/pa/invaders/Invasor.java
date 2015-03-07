/*
 * @(#)Invasor.java      1.0    2008/30/06
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
import java.awt.geom.Ellipse2D;

import es.uab.pa.invaders.worldview.WPoint;

/**
 * Classe que representa a un invasor de l'espai.
 * 
 * @author Marc Pujol <marc@la3.org>
 */
public class Invasor extends Actor {

  /**
   * Amplada dels invasors en dimensions món.
   */
  public static int WIDTH = 42;
  
  /**
   * Alçada dels invasors en dimensions món.
   */
  public static int HEIGHT = 42;
  
  /**
   * Desviació lateral màxima de l'invasor.
   */
  private double xMaxDesviacio;
  
  /**
   * Posició "x" inicial (per controlar la desviació
   * màxima).
   */
  private double xInicial;

  /**
   * Crea un nou invasor per al joc indicat.
   * 
   * @param joc Joc del que forma part el nou invasor.
   * @param xMaxDesviacio Desviació lateral màxima d'aquest invasor
   * respecte el punt "x" inicial.
   */
  public Invasor(Joc joc, double xMaxDesviacio) {
    super(joc);
    this.xMaxDesviacio = xMaxDesviacio;
  }
  
  /**
   * Avança "un pas" l'invasor.
   */
  @Override public void moure() {
    super.moure();
    if (this.posicio.x <= xInicial - xMaxDesviacio) {
      this.velocity.x *= -1;
    } else if (this.posicio.x >= xInicial + xMaxDesviacio) {
      this.velocity.x *= -1;
    }
    
    if (this.posicio.y >= Joc.HEIGHT - HEIGHT) {
      this.joc.setAcabat(Joc.JOC_PERDUT);
    }
    
    if (Math.random() > 0.998) {
      final Tret t = new Tret(joc, Tret.TRET_INVASOR);
      t.setPosicio(this.posicio.x + WIDTH/2 - Tret.WIDTH/2, this.posicio.y + HEIGHT);
      t.setVelocity(0, 1 + Math.random());
      joc.afegirActor(t);
    }
  }
  
  /**
   * Indica que s'ha produït una col·lisió amb un altre actor.
   * 
   * @param a Actor amb el que s'ha produït la col·lisió.
   */
  public void colisiona(Actor a) {
    
    // Els trets d'invasor no maten als invasors
    if (a instanceof Tret && ((Tret)a).getTipus() == Tret.TRET_INVASOR) {
      return;
    }
    
    this.setPerEsborrar(true);
  }

  /**
   * Dibuixa el monstre en el buffer indicat.
   */
  @Override public String getNomImatge() {
    return "Invasor";
  }

  /**
   * Retorna l'alçada d'un invasor en dimensions món.
   * 
   * @return alçada d'un invasor en dimensions món.
   */
  @Override public int getHeight() {
    return HEIGHT;
  }

  /**
   * Retorna l'amplada d'un invasor en dimensions món.
   * 
   * @return l'amplada d'un invasor en dimensions món.
   */
  @Override public int getWidth() {
    return WIDTH;
  }
  
  /**
   * Canvia la posició d'aquest invasor.
   * 
   * @param x Coordenada <em>x</em> de la nova posició d'aquest invasor.
   * @param y Coordenada <em>y</em> de la nova posició d'aquest invasor.
   */
  @Override public void setPosicio(double x, double y) {
    super.setPosicio(x, y);
    this.xInicial = x;
  }

  /**
   * Canvia la posició d'aquest invasor.
   * 
   * @param posicio Nova posició d'aquest invasor.
   */
  @Override public void setPosicio(WPoint posicio) {
    super.setPosicio(posicio);
    this.xInicial = posicio.x;
  }

  /**
   * Retorna el perfil d'un invasor en forma d'àrea, per realitzar
   * la detecció de col·lisions.
   */
  public Area getCurrentArea() {
    return new Area(new Ellipse2D.Double(posicio.x, posicio.y, WIDTH, HEIGHT));
  }
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb el jugador donat.
   * 
   * @param jugador jugador amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest jugador, <code>false</code> sinó.
   */
  public boolean hasColisio(Jugador jugador){
    if (this.getBox().intersects(jugador.getBox())) {
        Area a = new Area(this.getArea());
        Area b = jugador.getArea();
        a.intersect(b);
        return !a.isEmpty();
    }
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
    if (   tret.posicio.y >= this.posicio.y && tret.posicio.y <= this.posicio.y + HEIGHT
       &&  tret.posicio.x >= this.posicio.x && tret.posicio.x <= this.posicio.x + WIDTH)
    {
      Area a = this.getArea();
      return a.contains(tret.posicio.x, tret.posicio.y);
    }
    return false;
  }
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb l'invasor donat.
   * 
   * @param invasor invasor amb el que s'ha de comprovar si col·lisionem.
   * @return <code>false</code>, perquè els invasors mai xoquen entre ells.
   */
  public boolean hasColisio(Invasor invasor){
    return false;
  }

}
