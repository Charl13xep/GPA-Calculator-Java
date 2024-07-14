JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(150, (int) (getHeight() * heightRatio))); // Specific button height
            button.setFont(label.getFont().deriveFont(Font.PLAIN, (float) (getHeight() * heightRatio * 0.3)));
            button.addActionListener(e -> {