import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class BasketBot {
	public static final int X_OFFSET = 460;
	public static final int Y_OFFSET = 140;
	
	public static void main(String[] args) throws AWTException, InterruptedException {
		Robot robot = new Robot();
		setupGame(robot);
		
		Point basketPoint = getBasketTarget(robot);
		while (basketPoint != null) {
			Point startPoint = MouseInfo.getPointerInfo().getLocation();
			int difY = startPoint.y - basketPoint.y;
			int difX = startPoint.x - basketPoint.x;
			if (isStationary(robot, basketPoint)) {
				throwInDirection(robot, startPoint.x, startPoint.y, 5 * difX / 10, 6 * difY / 8);
			} else {
				/// Throwing Up Only
				while (Math.abs(startPoint.x - getBasketTarget(robot).x) > 70) {  }
				//throwInDirection(robot, startPoint.x, startPoint.y, 0, (59*difY)/40 - difY - 30);
				/// top manual modification, bottom from wolfram - neither seems to fail or achieve top points.
				throwInDirection(robot, startPoint.x, startPoint.y, 0, (59*difY)/40 - 270);
			}
			System.out.println(difY);
			basketPoint = getBasketTarget(robot);	
		}
	}
	
	public static boolean isStationary(Robot robot, Point basketPoint) {
		robot.delay(50);
		return basketPoint.x == getBasketTarget(robot).x;
	}
	
	public static Point getBasketTarget(Robot robot) {
		BufferedImage gameCap = robot.createScreenCapture(new Rectangle(X_OFFSET, Y_OFFSET, 456, 800));
		for (int x = 0; x < gameCap.getWidth(); x++) {
			for (int y = 0; y < gameCap.getHeight(); y++) {
				int clr = gameCap.getRGB(x, y);
				if (((clr & 0x00ff0000) >> 16) == 253 && ((clr & 0x0000ff00) >> 8) == 105 && (clr & 0x000000ff) == 23) {
					return new Point(x + X_OFFSET + 64, y + Y_OFFSET);
				}
			}
		}
		return null;
	}

	public static void altTab(Robot robot) {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.delay(1000);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public static void setupGame(Robot robot) {
		altTab(robot);
		robot.delay(1000);
		robot.mouseMove(689, 860);
	}
	
	public static void throwInDirection(Robot robot, int startX, int startY, int distanceX, int distanceY) {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseMove(startX - distanceX, startY - distanceY);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.delay(2000);
		robot.mouseMove(startX, startY);
		robot.delay(1000);
	}

}
