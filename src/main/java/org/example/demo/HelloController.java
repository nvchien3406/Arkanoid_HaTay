package org.example.demo;

import Models.Brick; // 👈 import từ package Models
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private AnchorPane gamePane;

    private List<Brick> bricks = new ArrayList<>();

    public void initialize() {
        int rows = 5;
        int cols = 10;
        double brickWidth = 32;
        double brickHeight = 16;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = 50 + col * brickWidth;
                double y = 50 + row * brickHeight;

                // Gạch có 3 máu => 3 frame trong sprite sheet
                Brick brick = new Brick(x, y, brickWidth, brickHeight, 6,"strong", "/images/BlueBrick.png");
                gamePane.getChildren().add(brick.getImageView());
                bricks.add(brick);
            }
        }
    }
    public void bamnut(ActionEvent e) {
        if (!bricks.isEmpty()) {
            bricks.get(0).takeHit();
        }
    }
}
