//package com.guwukeji.varyswebbase.common.util.huawei;
//
//import ch.ethz.ssh2.Connection;
//import ch.ethz.ssh2.Session;
//import ch.ethz.ssh2.StreamGobbler;
//
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.imageio.ImageIO;
//
//import org.assertj.core.util.Lists;
//import org.bytedeco.javacpp.opencv_core.IplImage;
//import org.bytedeco.javacv.FFmpegFrameGrabber;
//import org.bytedeco.javacv.Frame;
//import org.opencv.core.Mat;
//import org.opencv.imgcodecs.Imgcodecs;
//import org.opencv.videoio.VideoCapture;
//
//public class FFmpegUtil {
//
//	public static Connection getOpenedConnection(String host, String username, String password) throws IOException {
//		Connection conn = new Connection(host);
//		conn.connect(); // make sure the connection is opened
//		boolean isAuthenticated = conn.authenticateWithPassword(username, password);
//		if (isAuthenticated == false)
//			throw new IOException("Authentication failed.");
//		return conn;
//	}
//
//	public static void run2() {
//		// 读取视频文件
//		VideoCapture cap = new VideoCapture("F:/mysour/csdn/2.mp4");
//		System.out.println(cap.isOpened());
//		// 判断视频是否打开
//		if (cap.isOpened()) {
//			// 总帧数
//			double frameCount = cap.get(1);
//			System.out.println("视频总帧数:" + frameCount);
//			// 帧率
//			double fps = cap.get(1);
//			System.out.println("视频帧率" + fps);
//			// 时间长度
//			double len = frameCount / fps;
//			System.out.println("视频总时长:" + len);
//			Double d_s = new Double(len);
//			System.out.println(d_s.intValue());
//			Mat frame = new Mat();
//			for (int i = 0; i < d_s.intValue(); i++) {
//				// 设置视频的位置(单位:毫秒)
//				cap.set(1, i * 1000);
//				// 读取下一帧画面
//				if (cap.read(frame)) {
//					System.out.println("正在保存");
//					// 保存画面到本地目录
//					Imgcodecs.imwrite("F:/mysour/video/output/" + i + ".jpg", frame);
//				}
//			}
//			// 关闭视频文件
//			cap.release();
//		}
//	}
//
//	@SuppressWarnings("resource")
//	public static String execShellScript(String host, String username, String password, String cmd, int port)
//			throws IOException {
//		Connection conn = null;
//		Session sess = null;
//		InputStream stdout = null;
//		BufferedReader br = null;
//		StringBuffer buffer = new StringBuffer("exec result:");
//		buffer.append(System.getProperty("line.separator"));// 换行
//		try {
//			conn = getOpenedConnection(host, username, password);
//			sess = conn.openSession();
//			sess.execCommand(cmd);
//			stdout = new StreamGobbler(sess.getStdout());
//			br = new BufferedReader(new InputStreamReader(stdout));
//			while (true) {
//				// attention: do not comment this block, or you will hit
//				// NullPointerException
//				// when you are trying to read exit status
//				String line = br.readLine();
//				if (line == null)
//					break;
//				buffer.append(line);
//				buffer.append(System.getProperty("line.separator"));// 换行
//			}
//		} finally {
//			sess.close();
//			conn.close();
//		}
//		return buffer.toString();
//	}
//
//	/**
//	 * 获取第一秒第一帧的缩略图 -- （cmd(windows): ffmpeg.exe -ss 00:00:01 -y -i test1.mp4
//	 * -vframes 1 new.jpg）
//	 *
//	 * @param ffmpegPath
//	 *            ffmpeg.exe文件路径，可在rest或者admin中进行配置，使用配置文件进行读取
//	 * @param videoInputPath
//	 *            视频文件路径（输入）
//	 * @param coverOutputPath
//	 *            缩略图输出路径
//	 * @throws IOException
//	 */
//	public static void getVideoCover(String ffmpegPath, String videoInputPath, String coverOutputPath, String position)
//			throws IOException {
//		// 构建命令
//		List<String> command = new ArrayList<>();
//		command.add(ffmpegPath);
//		command.add("-i");
//		command.add(videoInputPath);
//		command.add("-y");
//		command.add("-f");
//		command.add("image2");
//		command.add("-ss");
//		command.add(position);
//		command.add("-t");
//		command.add("0.01");
//		command.add("-s");
//		command.add("720x360");
//		command.add(coverOutputPath);
//		// 执行操作
//		ProcessBuilder builder = new ProcessBuilder(command);
//		Process process = builder.start();
//		InputStream errorStream = process.getErrorStream();
//		InputStreamReader isr = new InputStreamReader(errorStream);
//		BufferedReader br = new BufferedReader(isr);
//		String line = "";
//		while ((line = br.readLine()) != null) {
//		}
//		if (br != null) {
//			br.close();
//		}
//		if (isr != null) {
//			isr.close();
//		}
//		if (errorStream != null) {
//			errorStream.close();
//		}
//	}
//
//	// /**
//	// * 获取指定视频的帧并保存为图片至指定目录
//	// * @param videofile 源视频文件路径
//	// * @param framefile 截取帧的图片存放路径
//	// * @throws Exception
//	// */
//	// public static void fetchFrame(String videofile, String framefile)
//	// throws Exception {
//	// long start = System.currentTimeMillis();
//	// File targetFile = new File(framefile);
//	// FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videofile);
//	// ff.start();
//	// int lenght = ff.getLengthInFrames();
//	// int i = 0;
//	// Frame f = null;
//	// while (i < lenght) {
//	// // 过滤前100帧
//	// f = ff.grabFrame();
//	// if ((i > 100) && (f.image != null)) {
//	// break;
//	// }
//	// i++;
//	// }
//	// IplImage img = f.;
//	// int owidth = img.width();
//	// int oheight = img.height();
//	// // 对截取的帧进行等比例缩放
//	// int width = 270;
//	// int height = (int) (((double) width / owidth) * oheight);
//	// BufferedImage bi = new BufferedImage(width, height,
//	// BufferedImage.TYPE_3BYTE_BGR);
//	// bi.getGraphics().drawImage(f.image.getBufferedImage().getScaledInstance(width,
//	// height, Image.SCALE_SMOOTH),
//	// 0, 0, null);
//	// ImageIO.write(bi, "jpg", targetFile);
//	// //ff.flush();
//	// ff.stop();
//	// System.out.println(System.currentTimeMillis() - start);
//	// }
//
//	private static ThreadLocal<Long> time = new ThreadLocal<>();
//
//	public static void main(String[] args) {
//		time.set(System.currentTimeMillis());
//		long length = 9 * 60 + 41;
//
//		String videoInputPath = "C:\\Users\\admin\\Desktop\\新建文件夹\\123.mp4";
//		String ffmpegPath = "D:\\development_kits\\ffmpeg\\ffmpeg.exe";
//		try {
//			long time = length / 11;
//			long currentTimeMillis = System.currentTimeMillis();
//			String path = "C:\\Users\\admin\\Desktop\\新建文件夹\\" + currentTimeMillis;
//			File file = new File(path);
//			if (!file.exists()) {
//				file.mkdirs();
//			}
//			for (int i = 0; i < 10; i++) {
//				String position = String.valueOf((i + 1) * time);
//				String coverOutputPath = path + "/" + (i + 1) + ".jpg";
//				getVideoCover(ffmpegPath, videoInputPath, coverOutputPath, position);
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(System.currentTimeMillis() - time.get());
//		// String cmd = "ffmpeg -y -i rtsp://user:password@ip:port -ss 00:00:01 -vframes
//		// 1 -f image2 -vcodec png image.png;echo end";
//		// try {
//		// String info = FFmpegUtil.execShellScript("ip", "user", "password", cmd, 22);
//		//
//		// System.out.println("info is:" + info);
//		// } catch (IOException e) {
//		// e.printStackTrace();
//		// }
//	}
//
//}
