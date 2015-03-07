/*
 * @(#)CacheImatges.java      1.0    2008/30/06
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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.HashMap;

import es.uab.pa.invaders.worldview.World;

/**
 * Cache on es generen i emmagatzemen les versions bitmap de les imatges
 * vectorials del joc per tall de millorar-ne l'eficiència.
 * 
 * @author Marc Pujol <marc@la3.org>
 */
public class CacheImatges {
  
  /**
   * Joc per al que generem les imatges.
   */
  private Joc joc;
  
  /**
   * Taula on es guarden les imatges generades, indexades
   * pel nom.
   */
  private HashMap<String, Image> imatges;
  
  /**
   * Construeix una nova cache d'imatges, per al joc donat.
   * 
   * @param joc joc per al que generem les imatges.
   */
  public CacheImatges(Joc joc) {
    this.joc = joc;
    this.imatges = new HashMap<String, Image>();
  }

  /**
   * Esborra les imatges de la caché, forçant la seva regeneració. Aquest
   * mètode s'utilitza quan la finestra canvia de mida.
   */
  public void esborra() {
    this.imatges = new HashMap<String, Image>();
  }
  
  /**
   * Retorna la representació bitmap de la imatge indicada (per a la
   * mida actual de la finestra), generant-la prèviament si és
   * necessari.
   * 
   * @param nom Nom de la imatge a retornar.
   * @return Representació bitmap de la imatge indicada.
   */
  public Image get(String nom) {
    if (this.imatges.containsKey(nom)) {
      return this.imatges.get(nom);
    }
    
    World mon = joc.getMon();
    Image img = null;
    Object r = null;
    try {
      Method m = this.getClass().getDeclaredMethod("get"+nom, new Class[]{double.class});      
      final double ratio = (mon.VXmax - mon.VXmin)/Joc.WIDTH;
      r = m.invoke(this, ratio);
    } catch (NoSuchMethodException e) {
      return null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    if (r instanceof Image) {
      img = (Image)r;
      this.imatges.put(nom, img);
    }
    
    return img;
  }
  
  /**
   * Retorna un bitmap de la imatge d'un invasor escalada
   * segons la relació d'aspecte entre món i pantalla donada.
   * 
   * @param ratio relació d'aspecte món-pantalla.
   * @return imatge bitmap d'un invasor.
   */
  @SuppressWarnings("unused")
  private Image getInvasor(double ratio) {
    final int width = (int)Math.floor(Invasor.WIDTH * ratio);
    final int height = (int)Math.floor(Invasor.HEIGHT * ratio);
    
    Image img = getImage(width, height);
    Graphics2D g = getGraphics(img);

    g.setColor(Colors.INVASOR_DINS);
    g.fillOval(0, 0, width-1, height-1);
    g.setColor(Colors.INVASOR_TRAS);
    g.drawOval(0, 0, width-1, height-1);

    return img;
  }

  /**
   * Retorna un bitmap de la imatge del jugador escalada
   * segons la relació d'aspecte entre món i pantalla donada.
   * 
   * @param ratio relació d'aspecte món-pantalla.
   * @return imatge bitmap del jugador.
   */
  @SuppressWarnings("unused")
  private Image getJugador(double ratio) {
    final int width = (int) (Jugador.WIDTH * ratio);
    final int height = (int) (Jugador.HEIGHT * ratio);
    
    Image img = getImage(width, height);
    Graphics2D g = getGraphics(img);
    
    g.setColor(Colors.JUGADOR_DINS);
    g.fillPolygon(new int[] {0,width/2,width,width/2}, new int[] {height,0,height,height-height/3}, 4);
    g.setColor(Colors.JUGADOR_TRAS);
    g.drawPolygon(new int[] {0,width/2,width,width/2}, new int[] {height,0,height,height-height/3}, 4);
    
    return img;
  }
  
  /**
   * Retorna un bitmap de la imatge d'un tret escalada
   * segons la relació d'aspecte entre món i pantalla donada.
   * 
   * @param ratio relació d'aspecte món-pantalla.
   * @return imatge bitmap d'un tret.
   */
  @SuppressWarnings("unused")
  private Image getTret(double ratio) {
    final int width = (int) Math.max(Tret.WIDTH * ratio, 1);
    final int height = (int) Math.max(Tret.HEIGHT * ratio, 1);
    
    Image img = getImage(width, height);
    Graphics2D g = (Graphics2D)img.getGraphics();
    
    g.setColor(Colors.TRET);
    g.fillRect(0, 0, width, height);
    
    return img;
  }
  
  /**
   * Funció auxiliar per no repetir el codi de creació d'imatges
   * i establiment de la qualitat.
   * 
   * @param width Amplada de la imatge.
   * @param height Alçada de la imatge.
   * @return Nova imatge off-screen per representar l'sprite.
   */
  private Image getImage(int width, int height) {
    Image img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    return img;
  }
  
  /**
   * Funció auxiliar per no repetir el codi d'establiment dels
   * paràmetres de qualitat.
   * 
   * @param img Imatge on s'ha de dibuixar.
   * @return Objecte graphics de la imatge amb els paràmetres ajustats.
   */
  private Graphics2D getGraphics(Image img) {
    Graphics2D g = (Graphics2D) img.getGraphics();
    // Set best alpha interpolation quality
    g.setRenderingHint(
        RenderingHints.KEY_ALPHA_INTERPOLATION, 
        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING, 
        RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(
        RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
    g.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    return g;
  }
  
}
