import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * NOTE
 * This program was written for a very specific screen size and easily breaks if changed. 
 * It is my intention to fix this but I may never find the time.
 * 
 * @author Paul
 *
 */
public class BasketBot {
	public static final int ONE_SECOND = 1000;
	public static final int TWO_SECOND = ONE_SECOND * 2;
	public static final int X_OFFSET = 460;
	public static final int Y_OFFSET = 140;
	public static final int GAME_WIDTH = 456;
	public static final int GAME_HEIGHT = 800;
	public static final int MIDDLE_OF_BALL_OFFSET = 64;
	
	public static void main(String[] args) throws AWTException, InterruptedException {
		final Robot robot = new Robot();
		setupGame(robot);
		
		Point basketPoint = getBasketTarget(robot);
		while (basketPoint != null) {
			Point startPoint = MouseInfo.getPointerInfo().getLocation();
			int difY = startPoint.y - basketPoint.y;
			int difX = startPoint.x - basketPoint.x;
			if (isStationary(robot, basketPoint)) {
				throwInDirection(robot, startPoint.x, startPoint.y, 5 * difX / 10, 6 * difY / 8);
			} else {
				while (Math.abs(startPoint.x - getBasketTarget(robot).x) > 70) {  }
				throwInDirection(robot, startPoint.x, startPoint.y, 0, (59 * difY) / 40 - 270);
			}
			basketPoint = getBasketTarget(robot);	
		}
	}
	
	public static boolean isStationary(Robot robot, Point basketPoint) {
		return basketPoint.x == getBasketTarget(robot).x;
	}
	
	public static Point getBasketTarget(Robot robot) {
		BufferedImage gameCap = robot.createScreenCapture(new Rectangle(X_OFFSET, Y_OFFSET, GAME_WIDTH, GAME_HEIGHT));
		for (int x = 0; x < gameCap.getWidth(); x++) {
			for (int y = 0; y < gameCap.getHeight(); y++) {
				int clr = gameCap.getRGB(x, y);
				if (((clr & 0x00FF0000) >> 16) == 253 && ((clr & 0x0000FF00) >> 8) == 105 && (clr & 0x000000FF) == 23) {
					return new Point(x + X_OFFSET + MIDDLE_OF_BALL_OFFSET, y + Y_OFFSET);
				}
			}
		}
		return null;
	}

	public static void altTab(Robot robot) {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.delay(ONE_SECOND);
		robot.keyRelease(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public static void setupGame(Robot robot) {
		altTab(robot);
		robot.delay(ONE_SECOND);
		robot.mouseMove(689, 860);
	}
	
	public static void throwInDirection(Robot robot, int startX, int startY, int distanceX, int distanceY) {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseMove(startX - distanceX, startY - distanceY);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.delay(TWO_SECOND);
		robot.mouseMove(startX, startY);
		robot.delay(ONE_SECOND);
	}

}
