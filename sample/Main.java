package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
/**
 * 推箱子游戏
 *
 *推箱子游戏思路：
 1.画地图
 (1)地图是通过二维数组构成的
 (2)数组中装数字，这些数组具有特殊含义，比如0--空地  1--通道   2-墙 3-箱子  4--目标位置 5--玩家
 (3)计算数组坐标和窗口坐标之间的关系
 (4)需要变量，记录玩家站在什么位置上,(0,3)，记录玩家的坐标
 (5)给容器添加键盘事件
 ①判断上下左右   上下左右：37 38 39 40
 */
public class Main extends Application {
    private static int[][] map;//游戏操作的地图
    private static int[][] map2;//作为参考的地图
    private static String direction = "bottom";//玩家朝向  默认向下
    private int x;//玩家当前在数组中的坐标
    private int y;//玩家当前在数组中的坐标
    /**
     * 静态代码块：不能手动调用，类被加载的时候就执行静态代码块中的代码
     * 初始化地图
     */
    static {
        map = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,4,3,1,1,2,4,3,1,2},
                {2,1,1,1,1,2,2,2,1,2},
                {2,2,2,1,5,1,1,1,1,2},
                {2,1,1,1,1,1,2,2,2,2},
                {2,4,3,1,1,1,1,3,4,2},
                {2,2,2,2,2,2,2,2,2,2}
        };
        map2 = new int[][]{
                {2,2,2,2,2,2,2,2,2,2},
                {2,4,3,1,1,2,4,3,1,2},
                {2,1,1,1,1,2,2,2,1,2},
                {2,2,2,1,1,1,1,1,1,2},
                {2,1,1,1,1,1,2,2,2,2},
                {2,4,3,1,1,1,1,3,4,2},
                {2,2,2,2,2,2,2,2,2,2}
        };
    }


    public void start(Stage stage) {
        try {
            AnchorPane root = new AnchorPane();
            Scene scene = new Scene(root,490,340);
            //调用方法,完成游戏相关操作
            game(root,scene);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 游戏相关操作
     * @param root
     * @param scene
     */
    private void game(AnchorPane root, Scene scene) {
        //创建画布
        Canvas canvas = new Canvas(500,350);
        //通过画布获取画笔
        GraphicsContext g2d = canvas.getGraphicsContext2D();
        //画地图  调用方法画地图
        drawMap(g2d);
        root.getChildren().add(canvas);
        //玩游戏
        play(scene,g2d);
    }

    public void f_alert_informationDialog(String p_message){
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle("win");
        _alert.setContentText(p_message);
        _alert.show();
    }

    private void win() {
        boolean win = true;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map2[i][j]==4&&map[i][j]!=3) {
                    win = false;
                }
            }
        }
        if (win) {
            f_alert_informationDialog("win");
        }
    }
    /**
     * 玩游戏
     * 1.添加键盘事件
     * 	按了上键？
     * 	按了下键？
     * 	按了左键？
     * 	按了右键？
     * 		如果要走的方向(上边)是通道或目标点(1/4)    要动
     * 			1.把玩家当前位置设置 通道 1
     * 			2.把玩家要走的位置位置 5
     * 			3.记录玩家要走的位置
     * 		如果要走的方向(上边)是箱子 3    要动
     * 			1.判断箱子要走的方向(上边)通道或目标点		 要动
     * 			2.如果要走的方向(上边)是箱子   不动
     * 			3.有可能是墙   不动
     * 		如果要走的方向(上边)是墙 2	不动
     *
     *
     * @param scene
     * @param g2d
     */
    private void play(Scene scene, GraphicsContext g2d) {
        //场景添加键盘事件
        scene.setOnKeyPressed(new EventHandler<Event>() {
            public void handle(Event event) {
                //获取键码
                KeyEvent ke = (KeyEvent)event;
                //强转
                KeyCode code = ke.getCode();
                switch (code) {
                    case UP:
                        direction  = "top";
                        //通道和目标点
                        if (map[x-1][y]==1||map[x-1][y]==4) {
                            //1.将玩家当前位置还原
                            if (map2[x][y]==4) {
                                map[x][y] = 4;
                            }else {
                                map[x][y] = 1;
                            }
                            //3.将玩家移动过去
                            map[x-1][y] = 5;
                            //4.记录玩家的当前坐标
                            x-=1;
                            drawMap(g2d);
                        }
                        //如果是箱子
                        if (map[x-1][y]==3) {
                            //继续判断箱子的上边
                            //如果是通道或目标点
                            if (map[x-1-1][y]==1||map[x-1-1][y]==4) {
                                //移动玩家
                                if (map2[x][y]==4) {
                                    map[x][y] = 4;
                                }else {
                                    map[x][y] = 1;
                                }
                                //3.将玩家移动过去
                                map[x-1][y] = 5;
                                //4.记录玩家的当前坐标
                                x-=1;
                                //移动箱子
                                //1.将箱子当前的位子不需要还原
                                //3.移动箱子
                                map[x-1-1][y] = 3;
                                win();
                            }
                        }
                        break;
                    case DOWN:
                        direction  = "bottom";
                        //通道和目标点
                        if (map[x+1][y]==1||map[x+1][y]==4) {
                            if (map2[x][y]==4) {
                                map[x][y] = 4;
                            }else {
                                map[x][y] = 1;
                            }
                            //3.将玩家移动过去
                            map[x+1][y] = 5;
                            //4.记录玩家的当前坐标
                            x+=1;
                            drawMap(g2d);
                        }
                        //如果是箱子
                        if (map[x+1][y]==3) {
                            //继续判断箱子的上边
                            //如果是通道或目标点
                            if (map[x+1+1][y]==1||map[x+1+1][y]==4) {
                                //移动玩家
                                if (map2[x][y]==4) {
                                    map[x][y] = 4;
                                }else {
                                    map[x][y] = 1;
                                }
                                //3.将玩家移动过去
                                map[x+1][y] = 5;
                                //4.记录玩家的当前坐标
                                x+=1;
                                //移动箱子
                                //1.将箱子当前的位子不需要还原
                                //3.移动箱子
                                map[x+1+1][y] = 3;
                                win();
                            }
                        }
                        break;
                    case LEFT:
                        direction  = "left";
                        //通道和目标点
                        if (map[x][y-1]==1||map[x][y-1]==4) {
                            if (map2[x][y]==4) {
                                map[x][y] = 4;
                            }else {
                                map[x][y] = 1;
                            }
                            //3.将玩家移动过去
                            map[x][y-1] = 5;
                            //4.记录玩家的当前坐标
                            y-=1;
                            drawMap(g2d);
                            break;
                        }
                        //如果是箱子
                        if (map[x][y-1]==3) {
                            //继续判断箱子的上边
                            //如果是通道或目标点
                            if (map[x][y-1-1]==1||map[x][y-1-1]==4) {
                                //移动玩家
                                if (map2[x][y]==4) {
                                    map[x][y] = 4;
                                }else {
                                    map[x][y] = 1;
                                }
                                //3.将玩家移动过去
                                map[x][y-1] = 5;
                                //移动箱子
                                //1.将箱子当前的位子不需要还原
                                //3.移动箱子
                                map[x][y-1-1] = 3;
                                //4.记录玩家的当前坐标
                                y-=1;
                                //重画
                                drawMap(g2d);
                                win();
                            }
                        }
                        break;
                    case RIGHT:
                        direction  = "right";
                        //通道和目标点
                        if (map[x][y+1]==1||map[x][y+1]==4) {
                            if (map2[x][y]==4) {
                                map[x][y] = 4;
                            }else {
                                map[x][y] = 1;
                            }
                            //3.将玩家移动过去
                            map[x][y+1] = 5;
                            //4.记录玩家的当前坐标
                            y+=1;
                            drawMap(g2d);
                            break;
                        }
                        //如果是箱子
                        if (map[x][y+1]==3) {
                            //继续判断箱子的上边
                            //如果是通道或目标点
                            if (map[x][y+1+1]==1||map[x][y+1+1]==4) {
                                //移动玩家
                                if (map2[x][y]==4) {
                                    map[x][y] = 4;
                                }else {
                                    map[x][y] = 1;
                                }
                                //3.将玩家移动过去
                                map[x][y+1] = 5;

                                //移动箱子
                                //1.将箱子当前的位子不需要还原
                                //3.移动箱子
                                map[x][y+1+1] = 3;
                                //4.记录玩家的当前坐标
                                y+=1;
                                drawMap(g2d);
                                win();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }
    /**
     * 画地图的方法
     * @param root
     */
    private void drawMap(GraphicsContext g2d) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                /**
                 * Image img  图片
                 * double x	  开始放的X坐标
                 * double y  开始放的Y坐标
                 * double w  画多宽/压缩画
                 * double h  画多高/压缩画
                 */
                switch (map[i][j]) {
                    case 0:
                        Image land =new Image(getClass().getResource("/Sokoban_pack/PNG/Ground_Grass.png").toString());
                        g2d.drawImage(land,j*50,i*50,50,50);
                        break;
                    case 1:
                        Image way =new Image(getClass().getResource("/Sokoban_pack/PNG/GroundGravel_Grass.png").toString());
                        g2d.drawImage(way,j*50,i*50,50,50);
                        break;
                    case 2:
                        Image wall =new Image(getClass().getResource("/Sokoban_pack/PNG/Wall_Beige.png").toString());
                        g2d.drawImage(wall,j*50,i*50,50,50);
                        break;
                    case 3:
                        Image box_way =new Image(getClass().getResource("/Sokoban_pack/PNG/Crate_Beige.png").toString());
                        g2d.drawImage(box_way,j*50,i*50,50,50);
                        Image box =new Image(getClass().getResource("/Sokoban_pack/PNG/Crate_Beige.png").toString());
                        g2d.drawImage(box,j*50,i*50,50,50);
                        break;
                    case 4:
                        Image way2 =new Image(getClass().getResource("/Sokoban_pack/PNG/GroundGravel_Sand.png").toString());
                        g2d.drawImage(way2,j*50,i*50,50,50);
                        Image box_end =new Image(getClass().getResource("/Sokoban_pack/PNG/EndPoint_Red.png").toString());
                        g2d.drawImage(box_end,j*50+10,i*50+10,30,30);
                        break;
                    case 5:
                        //记录玩家的初始位置
                        x= i;
                        y = j;
                        Image way3 =new Image(getClass().getResource("/Sokoban_pack/PNG/EndPoint_Yellow.png").toString());
                        g2d.drawImage(way3,j*50,i*50,50,50);
                        Image player = new Image(getClass().getResource("/Sokoban_pack/PNG/Character1"+direction+".png").toString());
                        g2d.drawImage(player,j*50,i*50,50,50);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
