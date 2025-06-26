package ui.componentes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class RoundedButton extends JButton {
    private Color hoverColor = new Color(0, 150, 255);
    private Color pressedColor = new Color(0, 100, 200);
    private int cornerRadius = 20;
    private boolean hovered = false;

    public RoundedButton(String text) {
        super(text);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(new Color(0, 120, 215));
        setBorder(new EmptyBorder(8, 20, 8, 20));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color currentColor = getBackground();
        if (getModel().isPressed()) {
            currentColor = pressedColor;
        } else if (hovered) {
            currentColor = hoverColor;
        }

        // Fondo redondeado
        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Texto centrado
        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(getText(), 
            getWidth()/2 - g2.getFontMetrics().stringWidth(getText())/2,
            getHeight()/2 + g2.getFontMetrics().getAscent()/3
        );
        
        g2.dispose();
    }

    // Personalizar colores
    public void setHoverColor(Color color) {
        this.hoverColor = color;
    }

    public void setPressedColor(Color color) {
        this.pressedColor = color;
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    // Tamaño preferido para diseño
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width + 40, 45);
    }
}