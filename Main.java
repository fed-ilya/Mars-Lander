//Решение задачи с сайта https://www.codingame.com/ide/puzzle/mars-lander-episode-2

import java.util.*;
import java.io.*;
import java.math.*;

/*
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 */

class Player {

    public static void main(String args[]) {
        final int save_vspeed = -40;
        final int save_hspeed = 20;

        final int VERT_SPEED_NO_ESCAPE = -38;
        final int VERT_SPEED_NO_ESCAPE_MAX_TILT = 22;

        final int HORIZ_SPEED_SAFE_MAX = 40;
        final int HORIZ_SPEED_COUNTER = 60;

        final int HORIZ_LANDING_DISTANCE_TO_LAND = 1000;
        final int Y_FACTOR_APPROACH = 300;
        final int X_FACTOR_APPROACH = 500;

        final int COUNTER_STEER_ANGLE = 45;

        Scanner in = new Scanner(System.in);
        int surfaceN = in.nextInt(); // the number of points used to draw the surface of Mars.
        int px1 = -1, px2 = -1, py = -1;
        boolean find_land = false;

        for (int i = 0; i < surfaceN; i++) {
            int landX = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
            int landY = in.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
            if(py == landY){
                px2 = landX;
                find_land = true;
            }
            if(!find_land){
                px1 = landX;
                py = landY;
            }
        }

        int sered_x = Math.abs(px2 + px1) / 2;

        // game loop
        while (true) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int hSpeed = in.nextInt(); // the horizontal speed (in m/s), can be negative.
            int vSpeed = in.nextInt(); // the vertical speed (in m/s), can be negative.
            int fuel = in.nextInt(); // the quantity of remaining fuel in liters.
            int rotate = in.nextInt(); // the rotation angle in degrees (-90 to 90).
            int power = in.nextInt(); // the thrust power (0 to 4).

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            int distance = X - sered_x;
            int old_angle = rotate;
            boolean falling_too_fast = false;

            // rotate power. rotate is the desired rotation angle. power is the desired thrust power.
            if(vSpeed < VERT_SPEED_NO_ESCAPE){
                falling_too_fast = true;
            }

            if(Math.abs(distance) > HORIZ_LANDING_DISTANCE_TO_LAND){ //корабль начинает изменять параметры незадолго до площадки для посадки
                if(Math.abs(hSpeed) < HORIZ_SPEED_SAFE_MAX){
                    final double tilt_angle = Math.atan2(Double.valueOf(Y - py), Double.valueOf(X - sered_x)) * 180 / Math.PI;
                    rotate = (int)Math.round(90 - tilt_angle);
                }
                else{
                    if(Math.abs(hSpeed) < HORIZ_SPEED_COUNTER && ((distance < 0 && hSpeed > 0) || (distance > 0 && hSpeed < 0))){
                        rotate = 0;
                    }
                    else{
                        if(hSpeed > 0){
                            rotate = COUNTER_STEER_ANGLE;
                        }
                        else{
                            rotate = -COUNTER_STEER_ANGLE;
                        }
                    }
                }
            }
            else{
                //Мы близки к цели
                power = 4;
                //Наклон, чтобы отменить горизонтальное движение
                final double tilt_angle = Math.atan2(Double.valueOf(Y - py), Double.valueOf(X - sered_x)) * 180 / Math.PI;

                if((distance < 0 && hSpeed > 0) || (distance > 0 && hSpeed < 0)){
                    //Движется в правильном направлении
                    if(Math.abs(hSpeed) >= save_hspeed){
                        if(hSpeed > 0){
                            rotate = (int)Math.round(180 - tilt_angle);
                        }
                        else{
                            rotate = -(int)tilt_angle;
                        }
                    }
                    else{
                        rotate = 0;
                    }
                }
                else{
                    if(Math.abs(Y - py) > Y_FACTOR_APPROACH){
                        if(hSpeed > 0){
                            rotate = (int)Math.round(180 - tilt_angle);
                        }
                        else{
                            rotate = -(int)tilt_angle;
                        }
                    }
                }
            }
            if((old_angle < 0 && rotate > 0) || (old_angle > 0 && rotate < 0)){
                if(Math.abs(old_angle) > VERT_SPEED_NO_ESCAPE_MAX_TILT && Math.abs(X - sered_x) < X_FACTOR_APPROACH * 5)
                    power = 0;
                else{
                    power = 3;
                }
            }
            else{
                if((Math.abs(X - sered_x) < X_FACTOR_APPROACH) && !falling_too_fast){
                    power = 2;
                }
                else{
                    power = 4;
                }
            }

            if(falling_too_fast){
                if(Math.abs(rotate) > VERT_SPEED_NO_ESCAPE_MAX_TILT){
                    if(rotate < 0){
                        rotate = -VERT_SPEED_NO_ESCAPE_MAX_TILT;
                    }
                    else{
                        rotate = VERT_SPEED_NO_ESCAPE_MAX_TILT;
                    }
                }
                power = 4;
            }
            if(Math.abs(rotate) > 90){
                if(rotate < 0){
                    rotate = -90;
                }
                else{
                    rotate = 90;
                }
            }
            System.out.println(rotate + " " + power);
        }
    }
}