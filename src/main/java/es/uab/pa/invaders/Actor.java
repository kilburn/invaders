/*
 * @(#)Actor.java      1.0    2008/30/06
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

import es.uab.pa.invaders.worldview.WPoint;
import es.uab.pa.invaders.worldview.WRectangle;

/**
 * Classe base que representa un "actor", és a dir, qualsevol
 * objecte que intervé en el joc.
 * 
 * @author Marc Pujol <marc@la3.org>
 */
public abstract class Actor {
  
  /**
   * Posició actual de l'objecte.
   */
  protected WPoint posicio;
  
  /**
   * Velocitat.
   */
  protected WPoint velocity;
  
  /**
   * Joc al que pertany aquest actor.
   */
  protected Joc joc;
  
  /**
   * Indica si un element ha estat marcat per esborrar,
   * es a dir, ha de desaparèixer del joc.
   */
  private boolean perEsborrar;
  
  /**
   * Àrea ocupada per aquest actor.
   */
  private Area area;
  
  /**
   * Crea un nou actor per al joc indicat.
   * 
   * @param joc
   */
  public Actor(Joc joc) {
    this.joc = joc;
    this.posicio = new WPoint(0,0);
    this.velocity = new WPoint(0,0);
  }
  
  /**
   * Avança "un pas" l'objecte en qüestió.
   */
  public void moure() {
    this.posicio.x = interval(0, this.posicio.x+this.velocity.x, Joc.WIDTH-this.getWidth());
    this.posicio.y = interval(0, this.posicio.y+this.velocity.y, Joc.HEIGHT);
    this.area = null;
  }
  
  /**
   * Retorna l'identificador del dibuix que representa aquest
   * objecte.
   */
  public abstract String getNomImatge();
  
  public abstract int getWidth();
  public abstract int getHeight();

  /**
   * Obtenir la posició d'aquest actor dins el món.
   * 
   * @return posició actual d'aquest actor dins el món.
   */
  public WPoint getPosicio() {
    return posicio;
  }

  /**
   * Obtenir la velocitat d'aquest actor.
   * 
   * @return velocitat actual d'aquest actor.
   */
  public WPoint getVelocity() {
    return velocity;
  }

  /**
   * Canvia la posició d'aquest actor.
   * 
   * @param posicio Nova posició d'aquest actor.
   */
  public void setPosicio(WPoint posicio) {
    this.posicio = posicio;
  }
  
  /**
   * Canvia la posició d'aquest actor.
   * 
   * @param x Coordenada "x" de la nova posició d'aquest actor.
   * @param y Coordenada "y" de la nova posició d'aquest actor.
   */
  public void setPosicio(double x, double y) {
    this.posicio = new WPoint(x,y);
  }

  /**
   * Canvia la velocitat d'aquest actor.
   * 
   * @param velocity Nova velocitat d'aquest actor.
   */
  public void setVelocity(WPoint velocity) {
    this.velocity = velocity;
  }
  
  /**
   * Canvia la velocitat d'aquest actor.
   * 
   * @param x Coordenada "x" de la nova velocitat d'aquest actor.
   * @param y Coordenada "y" de la nova velocitat d'aquest actor.
   */
  public void setVelocity(double x, double y) {
    this.velocity = new WPoint(x,y);
  }
  
  /**
   * Força un valor a trobar-se dins l'interval determinat per
   * min i max (ambdós inclosos).
   * 
   * @param min Mínim valor a retornar.
   * @param value Valor a retornar si es troba dins l'interval.
   * @return max Màxim valor a retornar.
   */
  private static double interval(double min, double value, double max) {
    if (value < min) return min;
    if (value > max) return max;
    return value;
  }

  /**
   * Consulta si aquest actor està marcat per esborrar.
   * 
   * @return <code>True</code> si està marcat per esborrar, <code>False</code> sinó.
   */
  public boolean isPerEsborrar() {
    return perEsborrar;
  }

  /**
   * Marca o desmarca aquest actor com a "per esborrar".
   * 
   * param <code>True</code> si s'ha de marcar per esborrar, <code>False</code> sinó.
   */
  public void setPerEsborrar(boolean perEsborrar) {
    this.perEsborrar = perEsborrar;
  }
  
  /**
   * Retorna el rectangle que compren aquest actor.
   */
  public WRectangle getBox() {
    return new WRectangle(this.posicio.x, this.posicio.y, this.getWidth(), this.getHeight());
  }
  
  /**
   * Retorna l'àrea que ocupa aquest actor (ús intern).
   * 
   * @return area que ocupa aquest actor.
   */
  protected abstract Area getCurrentArea();
  
  /**
   * Retorna l'àrea que ocupa aquest actor (ús extern).
   * 
   * @return area que ocupa aquest actor.
   */
  public final Area getArea() {
    if (this.area == null) {
      this.area = this.getCurrentArea();
    }
    return this.area;
  }
  
  /**
   * Indica que s'ha produït una col·lisió amb un altre actor.
   * 
   * @param a Actor amb el que s'ha produït la col·lisió.
   */
  public abstract void colisiona(Actor a);
  
  /**
   * Delega la detecció de col·lisions al mètode apropiat
   * segons el tipus d'actor amb el que hem de comprovar si
   * s'ha produït una col·lisió.
   * 
   * @param a Actor amb el que s'ha de realitzar la comprovació.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest actor, <code>false</code> sinó.
   */
  public final boolean hasColisio(Actor a) {
    if (a instanceof Tret) {
      return this.hasColisio((Tret)a);
    }
    if (a instanceof Invasor) {
      return this.hasColisio((Invasor)a);
    }
    if (a instanceof Jugador) {
      return this.hasColisio((Jugador)a);
    }
    return false;
  }
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb el jugador donat.
   * 
   * @param jugador jugador amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest jugador, <code>false</code> sinó.
   */
  public abstract boolean hasColisio(Jugador jugador);
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb el tret donat.
   * 
   * @param tret tret amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest tret, <code>false</code> sinó.
   */
  public abstract boolean hasColisio(Tret tret);
  
  /**
   * Comprova si s'ha efectuat una col·lisió amb l'invasor donat.
   * 
   * @param invasor invasor amb el que s'ha de comprovar si col·lisionem.
   * @return <code>true</code> si s'ha produït una col·lisió amb
   * aquest invasor, <code>false</code> sinó.
   */
  public abstract boolean hasColisio(Invasor invasor);

}
