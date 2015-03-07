package es.uab.pa.invaders.worldview;
import java.awt.*;

/**
 * Esta clase implementa las transformadas Mundo-Pantalla.
 * 
 * @author Vicenç Soler
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class World extends Object
{



/**
 * Comment for <code>VYmax</code> Coordenada Y inferior de la pantalla.
 */
public	 int VYmax;
/**
 * Comment for <code>VYmin</code> Coordenada Y superior de la pantalla.
 */
public	 int VYmin;
/**
 * Comment for <code>VXmax</code> Coordenada X derecha de la pantalla. 
 */
public	 int VXmax;
/**
 * Comment for <code>VXmin</code> Coordenada X izquierda de la pantalla.
 */
public	 int VXmin;

/**
 * Comment for <code>WYmax</code> Coordenada Y inferior del mundo.
 */
public	double WYmax;
/**
 * Comment for <code>WYmin</code> Coordenada Y superior del mundo.
 */
public	double WYmin;
/**
 * Comment for <code>WXmax</code> Coordenada X derecha del mundo.
 */
public	double WXmax;
/**
 * Comment for <code>WXmin</code> Coordenada X izquierda del mundo.
 */
public	double WXmin;
	

	
	/**
	 * Comment for <code>g</code> Objeto Graphics de la Pantalla.
	 */
	Graphics g;
	/**
	 * Comment for <code>Y_invertida</code> El (0.0) en coordenadas de la pantalla es la esquina superior izquierda y en ejes cartesianos, la inferior izquierda.
	 */
	boolean Y_invertida=false;

	/**
	 * Comment for <code>DECIMALS</code> Usado para algunas operaciones de transformación. 
	 */
	public static int DECIMALS=2;

/**
 * Constructor por defecto. Sólo inicializa a 0 las coordenadas del Mundo.
 */
public World()
{

	// En un principio, pero después se modifica en función
	// de las dimensiones de la ciudad.

	WXmax = 0.;//6000.;
	WYmax = 0.;//6000.;
	WXmin = 0.;
	WYmin = 0.;

}
/**
 * Construye un objeto World a partir de otro World, realizando una copia exacta.
 * @param m El objeto World a copiar.
 */
public World(World m)
{
	VXmax = m.VXmax;
	VYmax = m.VYmax;
	VXmin = m.VXmin;
	VYmin = m.VYmin;

	WXmax = m.WXmax;
	WYmax = m.WYmax;
	WXmin = m.WXmin;
	WYmin = m.WYmin;



}

/**
 * Construye un objeto World a partir de 4 puntos:2 de la ventana de Pantalla y 2 de la ventana del Mundo. 
 * @param vmin Punto superior izquierda de la Pantalla.
 * @param vmax Punto inferior derecha de la Pantalla.
 * @param wmin Punto superior izquierda del Mundo.
 * @param wmax Punto inferior derecha del Mundo.
 */
public World (Point vmin, Point vmax, WPoint wmin,WPoint wmax) {
	VXmin=vmin.x;
	VYmin=vmin.y;
	VXmax=vmax.x;
	VYmax=vmax.y;
	
	WXmin=wmin.x;
	WYmin=wmin.y;
	WXmax=wmax.x;
	WYmax=wmax.y;
}
	
/**
 * Construye un objeto World a partir de los 8 valores que guarda World. 
 * @param nVXmax
 * @param nVYmax
 * @param nVXmin
 * @param nVYmin
 * @param nWXmax
 * @param nWYmax
 * @param nWXmin
 * @param nWYmin
 */
public World( int nVXmax, int nVYmax, int nVXmin, int nVYmin,double nWXmax,double nWYmax,double nWXmin,double nWYmin)
{

	VXmax = nVXmax;
	VYmax = nVYmax;
	VXmin = nVXmin;
	VYmin = nVYmin;

	WXmax = nWXmax;
	WYmax = nWYmax;
	WXmin = nWXmin;
	WYmin = nWYmin;
	
}

//////////////////////////////////////////////////////////////////////
// Funciones miembro
//////////////////////////////////////////////////////////////////////

/**
 * Retorna un Rectangle correspondiente al rectángulo de las dimensiones de la Pantalla.
 * @return el objeto Rectangle.
 */
public Rectangle getRectangleView() {
	 return new Rectangle(VXmin,VYmin,VXmax-VXmin,VYmax-VYmin);
}

/**
 * Retorna un WRectangle correspondiente al rectángulo de las dimensiones del Mundo
 * @return el objeto WRectangle.
 */
public WRectangle getWRectangleWorld() {
	 return new WRectangle(WXmin,WYmin,WXmax-WXmin,WYmax-WYmin);
}

/**
 * Actualiza la variable Y_invertida.
 * @param b Es el valor a asignar a la variable Y_invertida. 
 */
public void setYInverted(boolean b) {
	Y_invertida=b;
}

/**
 * Retorna el valor de la variable Y_invertida.
 * @return el valor de Y_invertida.
 */
public boolean getYInverted() {
	return Y_invertida;
}


/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
public String toString() {
	String s="World=("+WXmin+","+WYmin+") a ("+WXmax+","+WYmax+") ";
	s+="View=("+VXmin+","+VYmin+") a ("+VXmax+","+VYmax+")";
	return s;
}

/**
 * Transforma un punto x,y de Mundo a Pantalla.
 * @param x La coordenada X del punto.
 * @param y La coordenada Y del punto.
 * @return el punto transformado.
 */
public Point worldToView(double x,double y) {
	return worldToView(new WPoint(x,y));
}

/**
 * Transforma un WPoint de Mundo a Pantalla
 * @param Wp El WPoint a transformar.
 * @return El Point transformado.
 */
public Point worldToView(WPoint Wp)
{
	double wy;
	Point Vp=new Point();

	wy=WYmax-Wp.y;	// El eje de coordenadas esta abajo en coord. mundo
	if (Y_invertida) wy=Wp.y;

	Vp.x = (int) ((Wp.x-WXmin) * ((VXmax-VXmin) / (WXmax-WXmin)) + VXmin);
	Vp.y = (int) (wy * ((VYmax-VYmin) / (WYmax-WYmin)) + VYmin);
	return Vp;
}

/**
 * Transforma un punto x,y de Pantalla a Mundo.
 * @param x La coordenada X del punto.
 * @param y La coordenada Y del punto.
 * @return el punto transformado.
 */
public WPoint viewToWorld(int x,int y) {
	return viewToWorld(new Point(x,y));
}

/**
 * Transforma un Point de Pantalla a Mundo.
 * @param Vp El Point a transformar.
 * @return El WPoint transformado.
 */
public WPoint viewToWorld(Point Vp)
{

	int vy;
	WPoint Wp=new WPoint();
	
	vy = VYmax-Vp.y;
	if (Y_invertida) vy=Vp.y;

	Wp.x = ((double) (Vp.x-VXmin)*(WXmax-WXmin)/((double) VXmax-VXmin))+WXmin;
	Wp.y = ((double) (vy-VYmin)*(WYmax-WYmin)/((double) VYmax-VYmin))+WYmin;
	return Wp;	

}

/**
 * Transforma un WRectangle de Mundo a Pantalla.
 * @param rec1 El WRectangle a transformar.
 * @return El Rectangle transformado.
 */
public Rectangle worldToView(WRectangle rec1)
{
	Point p1=worldToView(rec1.p1()),p2=worldToView(rec1.p2());
	return new Rectangle(p1.x,p1.y,Math.abs(p2.x-p1.x),Math.abs(p2.y-p1.y));
}

/**
 * Transforma un Rectangle de Pantalla a Mundo.
 * @param rec1 El Rectangle a transformar.
 * @return El WRectangle transformado.
 */
public WRectangle viewToWorld(Rectangle rec1)
{
	Point p1=new Point(rec1.x,rec1.y),p2=new Point(rec1.x+rec1.width,rec1.y+rec1.height);
	return new WRectangle(viewToWorld(p1),viewToWorld(p2));
}

/**
 * Transforma un WPolygon de Mundo a Pantalla.
 * @param pol1 El WPolygon a transformar.
 * @return El Polygon transformado.
 */
public Polygon worldToView(WPolygon pol1)
{
	Polygon pol=pol1.toPolygon(0);
	Point p;
	WPoint wp=new WPoint(0,0);
	for (int i=0;i<pol.npoints;i++) {
		wp.x=pol1.x[i];wp.y=pol1.y[i];
		p=worldToView(wp);
		pol.xpoints[i]=p.x;
  	pol.ypoints[i]=p.y;
  }
	return pol;
}

/**
 * Transforma un Polygon de Pantalla a Mundo.
 * @param pol1 El Polygon a transformar.
 * @return El WPolygon transformado.
 */
public WPolygon viewToWorld(Polygon pol1)
{
	WPolygon pol=new WPolygon(pol1);
	Point p=new Point(0,0);
	WPoint wp;
	for (int i=0;i<pol.x.length;i++) {
		p.x=pol1.xpoints[i];p.y=pol1.ypoints[i];
		wp=viewToWorld(p);
  	pol.x[i]=wp.x;
  	pol.y[i]=wp.y;
  }
	return pol;
}	

/**
 * Redimensiona la ventana del Mundo.
 * @param nWXmax
 * @param nWYmax
 * @param nWXmin
 * @param nWYmin
 */
public void setDimensionWorld(double nWXmax,double nWYmax,double nWXmin,double nWYmin)
{

	WXmax = nWXmax;
	WYmax = nWYmax;
	WXmin = nWXmin;
	WYmin = nWYmin;
}

/**
 * Redimensiona la ventana de la Pantalla.
 * @param nVXmax
 * @param nVYmax
 * @param nVXmin
 * @param nVYmin
 */
public void setDimensionView( int nVXmax, int nVYmax, int nVXmin, int nVYmin)
{
	VXmax = nVXmax;
	VYmax = nVYmax;
	VXmin = nVXmin;
	VYmin = nVYmin;
}

/**
 * @param max
 * @param min
 */
public void setDimensionWorld(double max, double min)
{

	WXmax = max;
	WYmax = max;
	WXmin = min;
	WYmin = min;
}

/**
 * Asigna valor a la variable g.
 * @param g1 El objeto Graphics a asignar.
 */
public void setGraphics(Graphics g1) {
	g=g1;
}

/**
 * Retorna el valor de la variable g.
 * @return El valor de la variable g.
 */
public Graphics getGraphics() {
  return g;
}

	
	
}