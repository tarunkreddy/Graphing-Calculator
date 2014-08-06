package Tests;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class GrapherFrame extends JFrame{
	
	public static ArrayList<Point> DNE;
	
	public static ArrayList<Point> zero;
	public static ArrayList<Point> firstDerivativeZero;
	public static ArrayList<Point> secondDerivativeZero;
	public static ArrayList<Point> relativeExtrema;
	public static ArrayList<Point> inflectionPoints;
	public static double xmin;
	public static double xmax;
	public static double ymin;
	public static double ymax;
	public static double evalUnit;
	public static double yInterval;
	public static int x0;
	public static int y0;
	public static ArrayList<Point> originalFunction = new ArrayList<Point>();
	public static ArrayList<Point> originalFunctionPixels;
	public static ArrayList<Point> firstDerivative = new ArrayList<Point>();
	public static ArrayList<Point> firstDerivativePixels;
	public static ArrayList<Point> secondDerivative = new ArrayList<Point>();
	public static ArrayList<Point> secondDerivativePixels;
	public static ArrayList<Double> asymptotes;
	public static ArrayList<Double> holes;
	private static final int FRAME_WIDTH = 250;
	private static final int FRAME_HEIGHT = 500;
	private JLabel expLabel;
	private JTextField expField;
	private JLabel xminLabel;
	private JTextField xminField;
	private JLabel xmaxLabel;
	private JTextField xmaxField;
	private JLabel yminLabel;
	private JTextField yminField;
	private JLabel ymaxLabel;
	private JTextField ymaxField;
	private JButton button;
	private JPanel panel;
	public GrapherFrame() {
		
		createTextField();
		createButton();
		createPanel();
		
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
	}
	
	private void createTextField() {
		expLabel = new JLabel("Type Expression Here: ");
		
		final int FIELD_WIDTH = 15;
		expField = new JTextField(FIELD_WIDTH);
		expField.setText("");
		xminLabel = new JLabel("X-Min: ");
		xminField = new JTextField(FIELD_WIDTH);
		xminField.setText("");
		xmaxLabel = new JLabel("X-Max: ");
		xmaxField = new JTextField(FIELD_WIDTH);
		xmaxField.setText("");
		yminLabel = new JLabel("Y-Min: ");
		yminField = new JTextField(FIELD_WIDTH);
		yminField.setText("");
		ymaxLabel = new JLabel("Y-Max: ");
		ymaxField = new JTextField(FIELD_WIDTH);
		ymaxField.setText("");
		
		

		
	}
	
	public static ArrayList<Point> convertToPixels(ArrayList<Point> function) {
		ArrayList<Point> pixelFunction = new ArrayList<Point>();
		
		for (int i = 0; i < function.size();i++) {
			Point p = function.get(i);
			Point pixelP = new Point(i, (int) (y0-(p.y/yInterval)));
			pixelFunction.add(pixelP);
			
		}
		return pixelFunction;
	}
	
	
	public static Point convertToPixels(Point p) {
		Point pixelPoint = new Point(x0 + p.x/evalUnit, y0 -(p.y/yInterval) );	
		return pixelPoint;
	}
	
	private void createButton() {
		button = new JButton("Graph");
		
		class EvaluateListener implements ActionListener
		{
			public void actionPerformed(ActionEvent event) {
				String expression = expField.getText();
				Poly poly = new Poly(expression);
				xmin = Double.parseDouble(xminField.getText());
				xmax = Double.parseDouble(xmaxField.getText());
				ymin = Double.parseDouble(yminField.getText());
				ymax = Double.parseDouble(ymaxField.getText());
				evalUnit = (Math.abs(xmin) + Math.abs(xmax))/600;
				yInterval = (Math.abs(ymin) + Math.abs(ymax))/600;
				originalFunction = poly.Evaluate(xmin, xmax, evalUnit);
				x0 = (int) ((Math.abs(xmin))/evalUnit);
				y0 = (int) ((Math.abs(ymax))/yInterval);
			    originalFunctionPixels = convertToPixels(originalFunction);
			    for (Point p : originalFunction) {
			    	double x = p.x;
			    	double y = Poly.takeDerivative(p.x);
			    	Point pDeriv = new Point(x, y);
			    	firstDerivative.add(pDeriv);
                }
			    firstDerivativePixels = convertToPixels(firstDerivative);
			    for (Point p : firstDerivative) {
			    	double x = p.x;
			    	double y = Poly.takeSecondDerivative(p.x);
			    	Point pDeriv = new Point(x, y);
			    	secondDerivative.add(pDeriv);
			    } 
			    secondDerivativePixels = convertToPixels(secondDerivative);
				Solver solver = new Solver();
				zero = solver.solve(originalFunction);
				firstDerivativeZero = solver.solve(firstDerivative);
				relativeExtrema = solver.findRelativeExtrema(firstDerivativeZero);
				secondDerivativeZero = solver.solve(secondDerivative);
				inflectionPoints = solver.findInflectionPoints(secondDerivativeZero);
				solver.findGaps(originalFunction);
				JFrame frame = new Grapher();
				frame.getContentPane().add(new GrapherPanel());
				frame.setTitle("Graph of " + expression);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setSize(600, 600);
				frame.setVisible (true);


			}
		}
		
		ActionListener listener = new EvaluateListener();
		button.addActionListener(listener);
	}
	
	private void createPanel() {
		panel = new JPanel();
		panel.add(expLabel);
		panel.add(expField);
		panel.add(xminLabel);
		panel.add(xminField);
		panel.add(xmaxLabel);
		panel.add(xmaxField);
		panel.add(yminLabel);
		panel.add(yminField);
		panel.add(ymaxLabel);
		panel.add(ymaxField);
		panel.add(button);
		add(panel);
	
	}
}
