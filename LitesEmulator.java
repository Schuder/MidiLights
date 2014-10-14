import TurtleGraphics.*;
import java.awt.Color;

public class LitesEmulator
{
	public static void main(String[] args)
	{
		Pen p = new StandardPen();
		Shape s = new Triangle(20, 20, 20, 10, 0, 8);
		s.draw(p);
		s = new Triangle(-20, -20, -20, -10, 0, -8);
		s.stretchBy(2);
		s.draw(p);
	}
}