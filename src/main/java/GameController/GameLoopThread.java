//package GameController;
//
//import javafx.application.Platform;
//
//public class GameLoopThread implements Runnable {
//    private static final int FPS = 60;
//    private static final long FRAME_TIME = 1000 / FPS;
//    private volatile boolean running = false;
//
//    private final StartGameController controller;
//    private final GameManager gameManager = GameManager.getInstance();
//
//    public GameLoopThread(StartGameController controller) {
//        this.controller = controller;
//    }
//
//    public void start() {
//        running = true;
//        Thread thread = new Thread(this, "GameLoopThread");
//        thread.setDaemon(true);
//        thread.start();
//    }
//
//    public void stop() {
//        running = false;
//    }
//
//    @Override
//    public void run() {
//        final long FRAME_TIME_NS = 1_000_000_000L / FPS;
//
//        while (running) {
//            long startTime = System.nanoTime();
//
//            // ✅ Logic chạy trên thread phụ
//            gameManager.updateLogicOnly(controller);
//
//            // ✅ Render UI chỉ 1 lần mỗi frame
//            Platform.runLater(() -> gameManager.renderUI(controller));
//
//            long elapsed = System.nanoTime() - startTime;
//            long sleepTime = FRAME_TIME_NS - elapsed;
//            if (sleepTime > 0) {
//                try {
//                    Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        }
//    }
//
//
//}
