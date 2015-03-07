/*
 * @(#)Colors.java      1.0    2008/30/06
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

import java.awt.Color;

/**
 * Classe que defineix els diferents colors utilitzats en el joc.
 * 
 * @author Marc Pujol <marc@la3.org>
 */
public class Colors {
  
  /**
   * Color de fons del joc.
   */
  public static Color FONS = new Color(219, 211, 160);
  
  /**
   * Color interior dels invasors.
   */
  public static Color INVASOR_DINS = new Color(238, 229, 050);
  
  /**
   * Color del traç dels invasors.
   */
  public static Color INVASOR_TRAS = new Color(210, 143, 035);
  
  /**
   * Color interior del jugador.
   */
  public static Color JUGADOR_DINS = new Color(255,255,255, 128);
  
  /**
   * Color del traç del jugador i els textos.
   */
  public static Color JUGADOR_TRAS = new Color(0,0,0);
  
  /**
   * Color dels trets.
   */
  public static Color TRET = new Color(255, 255, 255);
  
  /**
   * Color de la banda sobre la que es situa el text de
   * "partida acabada".
   */
  public static Color BANDA = new Color(255,255,255,100);
  
}
