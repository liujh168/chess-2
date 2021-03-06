package com.chess.utilities;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

import com.chess.model.Point;

/**
 * 获取屏幕棋盘中的棋子，棋盘左上角需和屏幕坐标(0,0)对齐
 * 
 * @author 86
 * @date 2017年10月11日 下午3:36:37
 */
public class ScreenUtilities {
	private static final int CChessDefaultWidth = 1022;// 新中国象棋棋盘默认宽度
	private static final int CChessDefaultHeight = 734;// 新中国象棋默认高度
	private static final int ChessPieceDefaultWidth = 34;// 新中国象棋棋盘格子默认宽度
	private static final int ChessPieceDefaultHeight = 28;// 新中国象棋默认格子高度
	private static final Point startP = new Point(422, 86);// 车坐标点;
	private static final Point[] points = new Point[90];// 可以放棋子的格子坐标
	private static final String[] flag = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i" };
	private static final Map<String, Point> bestmoveMap = new HashMap<>();
	private static final Map<Point, MBFImage> needCheckPointMBF = new HashMap<>();// 需要检查的点的图像
	private static final Map<Point, Integer[]> needCheckPoint = new HashMap<>();// 需要检查的点的坐标、宽度和高度
	static {
		for (int i = 0; i < 90; i++) {
			int indexOfLine = i % 9;
			int lineNum = i / 9;
			points[i] = new Point(startP.x + 57 * indexOfLine, startP.y + 57 * lineNum);

		}
		int length = points.length;
		for (int index = 0; index < length; index++) {
			int flagIndex = index % 9;
			int lineNum = 9 - index / 9;
			bestmoveMap.put(flag[flagIndex] + lineNum, points[index]);
		}
		try {
			needCheckPointMBF.put(new Point(409, 676), ImageUtilities.readMBF(new File("checkImage/start.png")));
			needCheckPointMBF.put(new Point(593, 395), ImageUtilities.readMBF(new File("checkImage/sure.png")));
			needCheckPointMBF.put(new Point(584, 394), ImageUtilities.readMBF(new File("checkImage/agree.png")));
			needCheckPointMBF.put(new Point(543, 408), ImageUtilities.readMBF(new File("checkImage/continuePlay.png")));
			needCheckPointMBF.put(new Point(644, 424), ImageUtilities.readMBF(new File("checkImage/continueTest.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		needCheckPoint.put(new Point(409, 676), new Integer[] { 53, 29 });
		needCheckPoint.put(new Point(593, 395), new Integer[] { 81, 29 });
		needCheckPoint.put(new Point(584, 394), new Integer[] { 78, 28 });
		needCheckPoint.put(new Point(543, 408), new Integer[] { 101, 24 });
		needCheckPoint.put(new Point(644, 424), new Integer[] { 99,26 });
	}

	public static List<MBFImage> grapChessPiecesByScreen() throws AWTException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(CChessDefaultWidth, CChessDefaultHeight);
		Rectangle screenRectangle = new Rectangle(screenSize);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		List<MBFImage> chessPiecesMBFImages = new ArrayList<>();
		for (Point p : points) {
			BufferedImage pointImage = image.getSubimage(p.x, p.y, ChessPieceDefaultWidth, ChessPieceDefaultHeight);
			MBFImage createMBFImage = ImageUtilities.createMBFImage(pointImage, true);
			chessPiecesMBFImages.add(createMBFImage);
		}
		return chessPiecesMBFImages;
	}

	public static Map<Point, MBFImage> grapNeedCheckImageByScreen() throws AWTException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(CChessDefaultWidth, CChessDefaultHeight);
		Rectangle screenRectangle = new Rectangle(screenSize);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		Map<Point, MBFImage> needCheckPointMBF = new HashMap<>();
		for (Entry<Point, Integer[]> entry : needCheckPoint.entrySet()) {
			Point p = entry.getKey();
			Integer[] widthAndHeight = entry.getValue();
			BufferedImage pointImage = image.getSubimage(p.x, p.y, widthAndHeight[0], widthAndHeight[1]);
			MBFImage createMBFImage = ImageUtilities.createMBFImage(pointImage, true);
			needCheckPointMBF.put(p, createMBFImage);
		}
		return needCheckPointMBF;
	}

	public static List<MBFImage> grapChessPiecesByScreen2() throws AWTException, IOException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(CChessDefaultWidth, CChessDefaultHeight);
		Rectangle screenRectangle = new Rectangle(screenSize);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		List<MBFImage> chessPiecesMBFImages = new ArrayList<>();
		for (int index = 0; index < points.length; index++) {
			BufferedImage pointImage = image.getSubimage(points[index].x, points[index].y, ChessPieceDefaultWidth,
					ChessPieceDefaultHeight);
			File f = new File("chessImage87");
			if (!f.exists())
				f.mkdirs();
			f = new File(f, index + ".png");
			ImageIO.write(pointImage, "png", f);
		}
		return chessPiecesMBFImages;
	}
	public static List<MBFImage> grapChessPiecesByScreen3() throws AWTException, IOException {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(CChessDefaultWidth, CChessDefaultHeight);
		Rectangle screenRectangle = new Rectangle(screenSize);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		List<MBFImage> chessPiecesMBFImages = new ArrayList<>();
		BufferedImage pointImage = image.getSubimage(644, 424, 99,
				26);
		File f = new File("chessImage87");
		if (!f.exists())
			f.mkdirs();
		f = new File(f, 1 + ".png");
		ImageIO.write(pointImage, "png", f);
		return chessPiecesMBFImages;
	}

	public static void main(String[] args) throws AWTException, InterruptedException, IOException {
		grapChessPiecesByScreen3();
	}

	public static Point needClick() throws AWTException {
		HistogramModel model = new HistogramModel(4, 4, 4);
		Map<Point, MBFImage> grapNeedCheckImageByScreen = grapNeedCheckImageByScreen();
		for (Entry<Point, MBFImage> mbf : grapNeedCheckImageByScreen.entrySet()) {
			Point key = mbf.getKey();
			MBFImage value = mbf.getValue();
			MBFImage check = needCheckPointMBF.get(key);
			model.estimateModel(check);
			MultidimensionalHistogram checkH = model.histogram.clone();
			model.estimateModel(value);
			MultidimensionalHistogram mbfH = model.histogram.clone();
			double distanceScore = checkH.compare(mbfH, DoubleFVComparison.EUCLIDEAN);
			if (distanceScore < 0.086) {
				Integer[] sizes = needCheckPoint.get(key);
				return new Point(key.x + sizes[0] / 2, +key.y + sizes[1] / 2);
			}
		}
		return null;
	}

	public static Point[] getPointsByBestmove(String bestmove) {
		String firstClick = bestmove.substring(0, 2);
		String secondClick = bestmove.substring(2, 4);
		Point firstPoint = bestmoveMap.get(firstClick);
		Point secondPoint = bestmoveMap.get(secondClick);
		return new Point[] { new Point(firstPoint.x + ChessPieceDefaultWidth / 2, firstPoint.y + ChessPieceDefaultHeight / 2),
				new Point(secondPoint.x + ChessPieceDefaultWidth / 2, secondPoint.y + ChessPieceDefaultHeight / 2) };

	}
}
