import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PaintBrushApp extends JFrame {

    // Components
    private DrawingPanel drawingPanel;
    private JPanel controlPanel;
    private JButton clearButton;
    private JButton undoButton;
    private JToggleButton lineButton;
    private JToggleButton rectangleButton;
    private JToggleButton ovalButton;
    private JToggleButton pencilButton;
    private JToggleButton eraserButton;
    private JRadioButton solidRadioButton;
    private JRadioButton dottedRadioButton;
    private JPanel colorPanel; // Updated to JPanel for color palette
    private JLabel statusLabel;

    // Enum for drawing tools
    enum Tool {
        LINE, RECTANGLE, OVAL, PENCIL, ERASER
    }

    // Current settings
    private Tool currentTool = Tool.PENCIL;
    private Color currentColor = Color.BLACK;
    private boolean isSolid = true;
    private boolean isDotted = false;

    public PaintBrushApp() {
        super("Paint Brush");

        // Initialize components
        drawingPanel = new DrawingPanel();
        controlPanel = new JPanel();
        clearButton = createButton("Clear");
        undoButton = createButton("Undo");
        lineButton = createToggleButton("Line");
        rectangleButton = createToggleButton("Rectangle");
        ovalButton = createToggleButton("Oval");
        pencilButton = createToggleButton("Pencil");
        eraserButton = createToggleButton("Eraser");
        solidRadioButton = new JRadioButton("Solid", true);
        dottedRadioButton = new JRadioButton("Dotted");
        colorPanel = createColorPalettePanel(); // Updated to use color palette panel
        statusLabel = new JLabel("Tool: Pencil | Color: Black | Mode: Solid");

        // Group toggle buttons so that only one can be selected at a time
        ButtonGroup toolGroup = new ButtonGroup();
        toolGroup.add(lineButton);
        toolGroup.add(rectangleButton);
        toolGroup.add(ovalButton);
        toolGroup.add(pencilButton);
        toolGroup.add(eraserButton);

        // Group radio buttons so that only one can be selected at a time
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(solidRadioButton);
        modeGroup.add(dottedRadioButton);

        // Layout setup
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Tool buttons panel
        JPanel toolPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        toolPanel.add(lineButton);
        toolPanel.add(rectangleButton);
        toolPanel.add(ovalButton);
        toolPanel.add(pencilButton);

        // Control buttons panel
        JPanel controlButtonsPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        controlButtonsPanel.add(eraserButton);
        controlButtonsPanel.add(clearButton);
        controlButtonsPanel.add(undoButton);

        // Mode buttons panel
        JPanel modePanel = new JPanel(new GridLayout(1, 2, 5, 5));
        modePanel.add(solidRadioButton);
        modePanel.add(dottedRadioButton);

        // Add components to control panel
        controlPanel.add(toolPanel);
        controlPanel.add(colorPanel); // Updated to add color palette panel
        controlPanel.add(modePanel);
        controlPanel.add(controlButtonsPanel);

        // Add panels to main frame
        add(controlPanel, BorderLayout.NORTH);
        add(drawingPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Event listeners
        clearButton.addActionListener(e -> drawingPanel.clear());
        undoButton.addActionListener(e -> drawingPanel.undo());
        lineButton.addActionListener(e -> setCurrentTool(Tool.LINE));
        rectangleButton.addActionListener(e -> setCurrentTool(Tool.RECTANGLE));
        ovalButton.addActionListener(e -> setCurrentTool(Tool.OVAL));
        pencilButton.addActionListener(e -> setCurrentTool(Tool.PENCIL));
        eraserButton.addActionListener(e -> setCurrentTool(Tool.ERASER));
        solidRadioButton.addActionListener(e -> {
            isSolid = true;
            isDotted = false;
            drawingPanel.setSolid(isSolid);
            drawingPanel.setDotted(isDotted);
            updateStatusLabel();
        });
        dottedRadioButton.addActionListener(e -> {
            isDotted = true;
            isSolid = false;
            drawingPanel.setSolid(isSolid);
            drawingPanel.setDotted(isDotted);
            updateStatusLabel();
        });

        // Frame settings
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Set default tool and color
        pencilButton.setSelected(true);
        updateStatusLabel();
    }

    // Helper method to create buttons with specified text
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(80, 30));
        button.setFont(new Font("Arial", Font.PLAIN, 14)); // Example font settings
        return button;
    }

    // Helper method to create toggle buttons with specified tool name
    private JToggleButton createToggleButton(String toolName) {
        JToggleButton button = new JToggleButton(toolName);
        button.setToolTipText(toolName);
        button.setPreferredSize(new Dimension(80, 30));
        button.setFont(new Font("Arial", Font.PLAIN, 14)); // Example font settings
        return button;
    }

    // Helper method to create the color palette panel
    private JPanel createColorPalettePanel() {
        JPanel palettePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.CYAN,
                Color.MAGENTA, Color.PINK, Color.GRAY}; // Example colors

        for (Color color : colors) {
            JToggleButton colorButton = createColorToggleButton("", color);
            palettePanel.add(colorButton);
            colorButton.addActionListener(e -> setCurrentColor(color));
        }

        return palettePanel;
    }

    // Helper method to create color toggle buttons
    private JToggleButton createColorToggleButton(String text, Color color) {
        JToggleButton button = new JToggleButton(text);
        button.setBackground(color);
        button.setPreferredSize(new Dimension(25, 25));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    // Method to set the current drawing tool
    private void setCurrentTool(Tool tool) {
        this.currentTool = tool;
        updateStatusLabel();
    }

    // Method to set the current drawing color
    private void setCurrentColor(Color color) {
        this.currentColor = color;
        drawingPanel.setCurrentColor(color);
        updateStatusLabel();
    }

    // Method to update the status label with current tool, color, and mode
    private void updateStatusLabel() {
        String mode = isSolid ? "Solid" : "Dotted";
        statusLabel.setText("Tool: " + currentTool.toString() + " | Color: " + colorToString(currentColor) + " | Mode: " + mode);
    }

    // Method to convert color to string representation
    private String colorToString(Color color) {
        if (color.equals(Color.BLACK)) return "Black";
        if (color.equals(Color.RED)) return "Red";
        if (color.equals(Color.BLUE)) return "Blue";
        if (color.equals(Color.GREEN)) return "Green";
        return "Custom";
    }

    // Main method to start the application
    public static void main(String[] args) {
        // Set Look and Feel to match system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(PaintBrushApp::new);
    }

    // Inner class representing the drawing panel
    class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener {

        private ArrayList<Shape> shapes;
        private Shape currentShape;
        private boolean isSolid = true;
        private boolean isDotted = false;

        public DrawingPanel() {
            setBackground(Color.WHITE);
            shapes = new ArrayList<>();
            setPreferredSize(new Dimension(800, 500));
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        // Method to paint shapes on the panel
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Shape shape : shapes) {
                shape.draw(g);
            }
            if (currentShape != null) {
                currentShape.draw(g);
            }
        }

        // MouseListener and MouseMotionListener methods
        @Override
        public void mouseClicked(MouseEvent e) {}

        @Override
        public void mousePressed(MouseEvent e) {
            switch (currentTool) {
                case LINE:
                    currentShape = new Line(e.getX(), e.getY(), e.getX(), e.getY(), currentColor, isDotted);
                    break;
                case RECTANGLE:
                    currentShape = new RectangleShape(e.getX(), e.getY(), 0, 0, currentColor, isDotted);
                    break;
                case OVAL:
                    currentShape = new OvalShape(e.getX(), e.getY(), 0, 0, currentColor, isDotted);
                    break;
                case PENCIL:
                    currentShape = new Pencil(e.getX(), e.getY(), currentColor, isDotted);
                    break;
                case ERASER:
                    currentShape = new Eraser(e.getX(), e.getY());
                    break;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (currentTool == Tool.ERASER) {
                if (currentShape != null) {
                    currentShape.updateEnd(e.getX(), e.getY());
                    shapes.add(currentShape);
                    currentShape = new Eraser(e.getX(), e.getY());
                    repaint();
                }
            } else {
                if (currentShape != null) {
                    currentShape.updateEnd(e.getX(), e.getY());
                    repaint();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (currentShape != null && currentTool != Tool.ERASER) {
                shapes.add(currentShape);
                currentShape = null;
                repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}

        // Method to clear all shapes
        public void clear() {
            shapes.clear();
            repaint();
        }

        // Method to undo the last drawn shape
        public void undo() {
            if (!shapes.isEmpty()) {
                shapes.remove(shapes.size() - 1);
                repaint();
            }
        }

        // Unused method in this version
        public void setCurrentColor(Color color) {
            // Method not needed for this approach as colors are set directly from buttons
        }

        // Setter for solid mode
        public void setSolid(boolean solid) {
            isSolid = solid;
        }

        // Setter for dotted mode
        public void setDotted(boolean dotted) {
            isDotted = dotted;
        }
    }

    // Interface for drawing shapes
    interface Shape {
        void draw(Graphics g);
        void updateEnd(int x, int y);
    }

    // Inner class representing a line shape
    class Line implements Shape {
        private int startX, startY, endX, endY;
        private Color color;
        private boolean isDotted;

        public Line(int startX, int startY, int endX, int endY, Color color, boolean isDotted) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            this.color = color;
            this.isDotted = isDotted;
        }

        // Method to draw the line
        @Override
        public void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            if (isDotted) {
                float[] dash = { 5.0f, 5.0f };
                BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
                g2d.setStroke(bs);
            } else {
                g2d.setStroke(new BasicStroke());
            }
            g2d.drawLine(startX, startY, endX, endY);
        }

        // Method to update the end point of the line
        @Override
        public void updateEnd(int x, int y) {
            this.endX = x;
            this.endY = y;
        }
    }

    // Inner class representing a rectangle shape
    class RectangleShape implements Shape {
        private int x, y, width, height;
        private Color color;
        private boolean isDotted;

        public RectangleShape(int x, int y, int width, int height, Color color, boolean isDotted) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
            this.isDotted = isDotted;
        }

        // Method to draw the rectangle
        @Override
        public void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            if (isDotted) {
                float[] dash = { 5.0f, 5.0f };
                BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
                g2d.setStroke(bs);
            } else {
                g2d.setStroke(new BasicStroke());
            }
            g2d.drawRect(x, y, width, height);
        }

        // Method to update the end point of the rectangle
        @Override
        public void updateEnd(int x, int y) {
            this.width = Math.abs(x - this.x);
            this.height = Math.abs(y - this.y);
            this.x = Math.min(x, this.x);
            this.y = Math.min(y, this.y);
        }
    }

    // Inner class representing an oval shape
    class OvalShape implements Shape {
        private int x, y, width, height;
        private Color color;
        private boolean isDotted;

        public OvalShape(int x, int y, int width, int height, Color color, boolean isDotted) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
            this.isDotted = isDotted;
        }

        // Method to draw the oval
        @Override
        public void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            if (isDotted) {
                float[] dash = { 5.0f, 5.0f };
                BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
                g2d.setStroke(bs);
            } else {
                g2d.setStroke(new BasicStroke());
            }
            g2d.drawOval(x, y, width, height);
        }

        // Method to update the end point of the oval
        @Override
        public void updateEnd(int x, int y) {
            this.width = Math.abs(x - this.x);
            this.height = Math.abs(y - this.y);
            this.x = Math.min(x, this.x);
            this.y = Math.min(y, this.y);
        }
    }

    // Inner class representing a pencil shape (freehand drawing)
    class Pencil implements Shape {
        private ArrayList<Point> points;
        private Color color;
        private boolean isDotted;

        public Pencil(int startX, int startY, Color color, boolean isDotted) {
            this.points = new ArrayList<>();
            this.points.add(new Point(startX, startY));
            this.color = color;
            this.isDotted = isDotted;
        }

        // Method to draw the pencil (freehand drawing)
        @Override
        public void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            if (isDotted) {
                float[] dash = { 5.0f, 5.0f };
                BasicStroke bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
                g2d.setStroke(bs);
            } else {
                g2d.setStroke(new BasicStroke());
            }
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Method to update the end point of the pencil drawing
        @Override
        public void updateEnd(int x, int y) {
            points.add(new Point(x, y));
        }
    }

    // Inner class representing an eraser shape
    class Eraser implements Shape {
        private int x, y, width, height;
        private Color color;

        public Eraser(int x, int y) {
            this.x = x;
            this.y = y;
            this.width = 10;
            this.height = 10;
            this.color = Color.WHITE; // Eraser color set to white (background color)
        }

        // Method to draw the eraser
        @Override
        public void draw(Graphics g) {
            g.setColor(color);
            g.fillRect(x, y, width, height);
        }

        // Method to update the position of the eraser
        @Override
        public void updateEnd(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
