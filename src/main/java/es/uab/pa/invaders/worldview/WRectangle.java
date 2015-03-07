package es.uab.pa.invaders.worldview;
import java.awt.*;

/**
 * 
 * Implementa un rectángulo para el Mundo mediante las coordenadas del punto superior izquierda, una anchura y una altura.
 * 
 * La diferencia con respecto a la clase <code>java.awt.Rectangle</code> es que usa coordendas <code>double</double>.
 * 
 * 
 * @author Vicenç soler
 *
 * 
 */
public class WRectangle extends Object  
{
	/**
	 * La Coordenada X del WRectangle
	 */
	double x;
	/**
	 * La Coordenada Y del WRectangle
	 */
	double y;
	/**
	 * La anchura del WRectangle
	 */
	double amp;
	/**
	 * La altura del WRectangle
	 */
	double al;
	
/**
 * Constructor por defecto. Inicializa a 0 las 4 variables miembro de WRectangle.
 */
public WRectangle()
{
	x=0;y=0;amp=0;al=0;
}

/**
 * Constructor a partir de 2 puntos, las 2 esquinas opuestas del WRectangle.
 * @param p1 El punto inicial, la esquina superior izquierda.
 * @param p2 El punto final, la esquina inferior derecha.
 */
public WRectangle(WPoint p1,WPoint p2)
{
	x = p1.x;
	y = p1.y;
	amp = p2.x-p1.x;
	al = p2.y-p1.y;
	
}

/**
 * Constructor a partir de un Rectangle. Tiene en cuenta la constante World.DECIMALS para dividir cada coordenada por 10^DECIMALS.
 * @param p El Rectangle
 */
public WRectangle(Rectangle p)
{
	x = p.x/Math.pow(10,World.DECIMALS);
	y = p.y/Math.pow(10,World.DECIMALS);
	amp = p.width/Math.pow(10,World.DECIMALS);
	al = p.height/Math.pow(10,World.DECIMALS);
	
}

/**
 * Construye un WRectangle a partir de otro WRectangle, generando una copia exacta.
 * @param p El WRectangle a copiar.
 */
public WRectangle(WRectangle p)
{
	x = p.x;
	y = p.y;
	amp = p.amp;
	al = p.al;
	
}


/**
 * Constructor a partir de un punto (x,y) correspondiente a la esquina superior izquierda, una anchura y una altura.
 * @param x1 La coordenada X del punto inicial, el superior izquierda.
 * @param y1 La coordenada Y del punto inicial, el superior izquierda.
 * @param amp1 La anchura.
 * @param al1 La altura.
 */
public WRectangle(double x1,double y1,double amp1,double al1)
{
	x = x1;
	y = y1;
	amp=amp1;
	al=al1;
}

/**
 * Retorna el punto superior izquierda.
 * @return el WPoint superior izquierda.
 */
public WPoint p1() {
	return new WPoint(x,y);
}

/**
 * Retorna el punto inferior derecha.
 * @return el WPoint inferior derecha
 */
public WPoint p2() {
	return new WPoint(x+amp,y+al);
}	

/**
 * Cambia el punto superior izquierda. Mantiene intactas la anchura y la altura.
 * @param p el nuevo punto superior izquierda.
 */
public void setP1(WPoint p) {
	x=p.x;y=p.y;
}

/**
 * Cambia el punto inferior derecha. Mantiene intactas la anchura y la altura.
 * @param p el nuevo punto inferior derecha.
 */
public void setP2(WPoint p) {
	amp=p.x-x;al=p.y-y;
}

/**
 * Retorna el punto central del rectángulo. 
 * @return el WPoint correspondiente al centro del rectángulo
 */
public WPoint centre() {
	return new WPoint(x+amp/2,y-al/2);
}
/**
 * Convierte a Rectangle, simplemente multiplicando cada coordenada por 10^numDecimals.
 * Usado para aprovechar funciones de Rectangle.
 * @param numDecimals el número de decimales que se convierten en enteros.
 * @return el nuevo Rectangle
 */
public Rectangle toRectangle(int numDecimals) {
	return new Rectangle((int)(x*Math.pow(10,numDecimals)),(int)(y*Math.pow(10,numDecimals)),(int)(amp*Math.pow(10,numDecimals)),(int)(al*Math.pow(10,numDecimals)));
}

/**
 * Verifica si dos WRectangle's se intersectan.
 * @param r El WRectangle con el que se quiere comprobar la intersección
 * @return true o false, si intersectan o no.
 */
public boolean intersects(WRectangle r) {
	return toRectangle(World.DECIMALS).intersects(r.toRectangle(World.DECIMALS));
}

/**
 * Examina si el WRectangle contiene un WPoint.
 * @param p El WPoint que se quiere examinar.
 * @return true o false (si lo contiene o no)
 */
public boolean contains(WPoint p) {
	return toRectangle(World.DECIMALS).contains(p.toPoint(World.DECIMALS));
}

/**
 * Crea un nuevo WRectangle a partir del actual, trasladándolo según el desplazamiento marcado en los parámetros.
 * No modifica el WRectangle con el que se está trabajando.
 * @param x1 Es el desplazamiento X. No es la coordenada de destino, sino el desplazamiento que se aplica a cada punto del WRectangle
 * @param y1 Es el desplazamiento Y. No es la coordenada de destino, sino el desplazamiento que se aplica a cada punto del WRectangle
 * @return El nuevo WRectangle trasladado. 
 */
public WRectangle virtualTraslate(double x1,double y1) {
	return new WRectangle(x+x1,y+y1,amp,al);
}

/**
 * Crea un nuevo WRectangle a partir del actual, trasladandolo según el desplazamiento marcado en el parámetro.
 * No modifica el WRectangle con el que se está trabajando.
 * @param p Es el desplazamiento marcado en el WPoint que se pasa. No es la coordenada de destino, sino el desplazamiento que se aplica a cada punto del WRectangle, tanto para las X como para las Y.
 * @return El nuevo WRectangle trasladado.
 */
public WRectangle virtualTraslate(WPoint p)
{
	return virtualTraslate(p.x,p.y);
}

/**
 * Traslada todos los puntos del WRectangle un desplazamiento (x1,y1).
 * @param x1 Desplazamiento de las X.
 * @param y1 Desplazamiento de las Y.
 */
public void traslate(double x1,double y1) {
	x+=x1;y+=y1;
}

//els 4 punts han d'haver estat creats
/**
 * Retorna un WPolygon correspondiente a la rotación del WRectangle actual, un ángulo determinado y el centro de rotación es el centro del WRectangle.
 * No se modifica el WRectangle actual.  
 * @param angle El ángulo de rotación. Expresado en radianes.
 * @return El WPolygon resultado de la rotación. 
 */
public WPolygon rotate(double angle) {
	WPoint p1,p2,p3,p4;
	double x[]=new double[4],y[]=new double[4];
	WPoint c=new WPoint((p1().x+p2().x)/2,(p1().y+p2().y)/2);
	p1=p1().rotate(angle,c);
	p3=p2().rotate(angle,c);
	p2=(new WPoint(p2().x,p1().y)).rotate(angle,c);
	p4=(new WPoint(p1().x,p2().y)).rotate(angle,c);
	x[0]=p1.x;y[0]=p1.y;x[1]=p2.x;y[1]=p2.y;x[2]=p3.x;y[2]=p3.y;x[3]=p4.x;y[3]=p4.y;
	return new WPolygon(x,y);
}

/**
 * Retorna un nuevo WPolygon correspondiente a la rotación del WRectangle actual, un ángulo determinado y con respecto a un centro de rotación.
 * No se modifica el WPolygon actual.  
 * @param angle El ángulo de rotación. Expresado en radianes.
 * @param c El WPoint correspondiente al centro de rotación.
 * @return El WPolygon resultado de la rotación. 
 */
public WPolygon rotate(double angle,WPoint c) {
	WPoint p1,p2,p3,p4;
	double x[]=new double[4],y[]=new double[4];
	
	p1=p1().rotate(angle,c);
	p3=p2().rotate(angle,c);
	p2=(new WPoint(p2().x,p1().y)).rotate(angle,c);
	p4=(new WPoint(p1().x,p2().y)).rotate(angle,c);
	x[0]=p1.x;y[0]=p1.y;x[1]=p2.x;y[1]=p2.y;x[2]=p3.x;y[2]=p3.y;x[3]=p4.x;y[3]=p4.y;
	return new WPolygon(x,y);
}


/**
 * Transformada del WRectangle, de Mundo a Pantalla.
 * @param world El objeto World al que se aplica la transformada.
 * @return El Rectangle resultado de aplicar la transformada.
 */
public Rectangle worldToView(World world)
{
	Point p1=world.worldToView(p1()),p2=world.worldToView(p2());
	return new Rectangle(p1.x,p1.y,Math.abs(p2.x-p1.x),Math.abs(p2.y-p1.y));
}
/**
 * Transformada de la Pantalla al Mundo.
 * @param world El objeto World al que se aplica la transformada. 
 * @param rec1 El Rectangle a transformar.
 * @return El WRectangle resultado de aplicar la transformada.
 */
public WRectangle viewToWorld(World world,Rectangle rec1)
{
	Point p1=new Point(rec1.x,rec1.y),p2=new Point(rec1.x+rec1.width,rec1.y+rec1.height);
	return new WRectangle(world.viewToWorld(p1),world.viewToWorld(p2));
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
public String toString()
{
	return p1()+" "+p2();
}

}




