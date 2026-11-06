import org.json.JSONObject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedHashSet;
import java.util.Locale;

/** Interface desktop; usa a mesma consulta HTTPS e a mesma chave de ambiente do terminal. */
public final class ClimaDesktop extends JFrame {
    private static final Color INK = new Color(35, 58, 76);
    private static final Color MUTED = new Color(112, 132, 147);
    private static final Color BLUE = new Color(47, 111, 163);
    private final JTextField search = new JTextField();
    private final JButton consult = new JButton("Consultar cidade");
    private final JLabel location = label("Como está o tempo?", 29, true, INK);
    private final JLabel region = label("Escolha uma cidade para começar", 14, false, MUTED);
    private final JLabel temperature = label("—°", 86, false, INK);
    private final JLabel condition = label("Condições atuais", 19, false, INK);
    private final JLabel updated = label("Dados fornecidos pela WeatherAPI", 12, false, MUTED);
    private final JLabel feedback = label("Ex.: Rio de Janeiro, Curitiba ou Lisboa", 12, false, MUTED);
    private final JLabel feels = label("—", 27, true, INK), humidity = label("—", 27, true, INK);
    private final JLabel wind = label("—", 27, true, INK), pressure = label("—", 27, true, INK);
    private final WeatherArt art = new WeatherArt();
    private final JPanel recentPanel = new JPanel();
    private final LinkedHashSet<String> recent = new LinkedHashSet<>();

    public ClimaDesktop() {
        super("Clima Agora");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 740));
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.add(sidebar(), BorderLayout.WEST);
        JPanel main = new JPanel(new BorderLayout(0, 25));
        main.setBorder(new EmptyBorder(30, 36, 24, 36)); main.setBackground(Color.WHITE);
        main.add(toolbar(), BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout(0, 24)); body.setOpaque(false);
        body.add(hero(), BorderLayout.CENTER);
        JPanel details = new JPanel(new BorderLayout(0, 14)); details.setOpaque(false);
        details.add(label("O tempo em detalhes", 17, true, INK), BorderLayout.NORTH);
        JPanel metrics = new JPanel(new GridLayout(1, 4, 14, 0)); metrics.setOpaque(false);
        metrics.add(metric("SENSAÇÃO TÉRMICA", feels, "Como o corpo percebe"));
        metrics.add(metric("UMIDADE", humidity, "Umidade relativa do ar"));
        metrics.add(metric("VENTO", wind, "Velocidade do vento"));
        metrics.add(metric("PRESSÃO", pressure, "Pressão atmosférica"));
        details.add(metrics, BorderLayout.CENTER); body.add(details, BorderLayout.SOUTH);
        main.add(body, BorderLayout.CENTER);
        JPanel foot = new JPanel(new GridLayout(2, 1, 0, 8)); foot.setOpaque(false);
        foot.add(feedback); foot.add(updated); main.add(foot, BorderLayout.SOUTH);
        root.add(main, BorderLayout.CENTER); setContentPane(root);
        setSize(1170, 760); setLocationRelativeTo(null);
        consult.addActionListener(e -> query()); search.addActionListener(e -> query());
        getRootPane().setDefaultButton(consult);
    }

    private JPanel sidebar() {
        JPanel side = new JPanel(new BorderLayout(0, 38));
        side.setBackground(new Color(241, 245, 247)); side.setPreferredSize(new Dimension(225, 0));
        side.setBorder(new EmptyBorder(34, 24, 28, 24));
        JPanel brand = column();
        brand.add(label("CLIMA AGORA", 17, true, BLUE)); brand.add(Box.createVerticalStrut(7));
        brand.add(label("Seu tempo, de relance.", 12, false, MUTED)); side.add(brand, BorderLayout.NORTH);
        JPanel middle = column(); middle.add(label("CONSULTAS RECENTES", 10, true, MUTED));
        middle.add(Box.createVerticalStrut(16)); recentPanel.setOpaque(false);
        recentPanel.setLayout(new BoxLayout(recentPanel, BoxLayout.Y_AXIS));
        recentPanel.add(label("Suas cidades aparecem aqui", 11, false, MUTED));
        middle.add(recentPanel); side.add(middle, BorderLayout.CENTER);
        JPanel note = column(); note.add(label("CONDIÇÕES ATUAIS", 10, true, MUTED));
        note.add(Box.createVerticalStrut(8)); note.add(label("Temperaturas em Celsius", 11, false, MUTED));
        side.add(note, BorderLayout.SOUTH); return side;
    }

    private JPanel toolbar() {
        JPanel bar = new JPanel(new BorderLayout(14, 12)); bar.setOpaque(false);
        JPanel heading = column(); heading.add(label("Explorar o clima", 21, true, INK));
        heading.add(Box.createVerticalStrut(7)); heading.add(label("Busque uma cidade e veja as condições de agora.", 12, false, MUTED));
        bar.add(heading, BorderLayout.NORTH);
        search.setFont(new Font("Segoe UI", Font.PLAIN, 14)); search.setForeground(INK);
        search.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(216, 227, 234)), new EmptyBorder(12, 14, 12, 14)));
        search.setToolTipText("Nome da cidade ou cidade, país"); search.getAccessibleContext().setAccessibleName("Cidade");
        consult.setBackground(BLUE); consult.setForeground(Color.WHITE); consult.setFont(new Font("Segoe UI", Font.BOLD, 13));
        consult.setFocusPainted(false); consult.setBorder(new EmptyBorder(12, 20, 12, 20)); consult.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JPanel inputs = new JPanel(new BorderLayout(10, 0)); inputs.setOpaque(false); inputs.add(search, BorderLayout.CENTER); inputs.add(consult, BorderLayout.EAST);
        bar.add(inputs, BorderLayout.CENTER); return bar;
    }

    private JPanel hero() {
        RoundedPanel hero = new RoundedPanel(new Color(231, 241, 248));
        hero.setLayout(new BorderLayout(0, 12)); hero.setBorder(new EmptyBorder(26, 30, 24, 30));
        JPanel title = column(); title.add(location); title.add(Box.createVerticalStrut(5)); title.add(region);
        hero.add(title, BorderLayout.NORTH);
        JPanel info = new JPanel(new BorderLayout()); info.setOpaque(false);
        JPanel text = column(); text.add(temperature); text.add(condition); info.add(text, BorderLayout.CENTER);
        art.setPreferredSize(new Dimension(245, 190)); info.add(art, BorderLayout.EAST);
        hero.add(info, BorderLayout.CENTER); return hero;
    }

    private JPanel metric(String name, JLabel value, String hint) {
        if (value == wind || value == pressure) value.setFont(new Font("Segoe UI", Font.BOLD, 22));
        RoundedPanel panel = new RoundedPanel(new Color(247, 249, 250)); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 16, 20, 12)); panel.add(label(name, 9, true, MUTED));
        panel.add(Box.createVerticalStrut(15)); panel.add(value); panel.add(Box.createVerticalStrut(9));
        panel.add(label(hint, 10, false, MUTED)); return panel;
    }

    private void query() {
        String city = search.getText().strip();
        if (city.isBlank()) { feedback.setForeground(new Color(166, 80, 55)); feedback.setText("Digite o nome de uma cidade."); search.requestFocusInWindow(); return; }
        consult.setEnabled(false); search.setEnabled(false); consult.setText("Consultando…");
        feedback.setForeground(MUTED); feedback.setText("Buscando as condições atuais…");
        new SwingWorker<String, Void>() {
            protected String doInBackground() throws Exception { return ProjetoSistemaDeInformacoesClimaticasEmTempoReal.getDadosClimaticos(city); }
            protected void done() {
                try {
                    JSONObject data = new JSONObject(get()); JSONObject place = data.getJSONObject("location"), current = data.getJSONObject("current");
                    location.setText(place.getString("name")); region.setText(place.optString("region") + " · " + place.getString("country"));
                    temperature.setText(number(current.getDouble("temp_c")) + "°"); condition.setText(current.getJSONObject("condition").getString("text"));
                    feels.setText(number(current.getDouble("feelslike_c")) + "°"); humidity.setText(current.getInt("humidity") + "%");
                    wind.setText(number(current.getDouble("wind_kph")) + " km/h"); pressure.setText(number(current.getDouble("pressure_mb")) + " mb");
                    art.code = current.getJSONObject("condition").getInt("code"); art.day = current.optInt("is_day", 1) == 1; art.repaint();
                    updated.setText("Atualizado em " + current.getString("last_updated") + " (horário local) · WeatherAPI");
                    feedback.setForeground(MUTED); feedback.setText("Consulta atualizada. Você pode buscar outra cidade.");
                    remember(place.getString("name"));
                } catch (Exception ex) {
                    Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                    feedback.setForeground(new Color(166, 80, 55));
                    feedback.setText(cause instanceof IllegalStateException ? cause.getMessage() : "Não foi possível consultar o clima. Verifique a conexão e tente novamente.");
                } finally { consult.setEnabled(true); search.setEnabled(true); consult.setText("Consultar cidade"); }
            }
        }.execute();
    }

    private void remember(String city) {
        recent.remove(city); recent.add(city); if (recent.size() > 5) recent.remove(recent.iterator().next());
        recentPanel.removeAll(); java.util.List<String> items = new java.util.ArrayList<>(recent); java.util.Collections.reverse(items);
        for (String value : items) {
            JButton button = new JButton(value); button.setAlignmentX(Component.LEFT_ALIGNMENT); button.setMaximumSize(new Dimension(180, 40));
            button.setHorizontalAlignment(SwingConstants.LEFT); button.setFont(new Font("Segoe UI", Font.PLAIN, 13)); button.setForeground(INK);
            button.setBorder(new EmptyBorder(10, 0, 10, 0)); button.setContentAreaFilled(false); button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.addActionListener(e -> { if (consult.isEnabled()) { search.setText(value); query(); } }); recentPanel.add(button);
        }
        recentPanel.revalidate(); recentPanel.repaint();
    }

    private static String number(double value) { return String.format(Locale.forLanguageTag("pt-BR"), value == Math.rint(value) ? "%.0f" : "%.1f", value); }
    private static JPanel column() { JPanel panel = new JPanel(); panel.setOpaque(false); panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); return panel; }
    private static JLabel label(String text, int size, boolean bold, Color color) {
        JLabel label = new JLabel(text); label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size)); label.setForeground(color); label.setAlignmentX(Component.LEFT_ALIGNMENT); return label;
    }
    private static final class RoundedPanel extends JPanel {
        private final Color color;
        RoundedPanel(Color color) { this.color = color; setOpaque(false); }
        protected void paintComponent(Graphics graphics) { super.paintComponent(graphics); Graphics2D g = (Graphics2D) graphics.create(); g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); g.setColor(color); g.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24); g.dispose(); }
    }
    private static final class WeatherArt extends JPanel {
        int code = 1003; boolean day = true;
        WeatherArt() { setOpaque(false); }
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics); Graphics2D g = (Graphics2D) graphics.create(); g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            double scale = Math.min(getWidth() / 245.0, getHeight() / 190.0); g.translate((getWidth()-245*scale)/2, (getHeight()-190*scale)/2); g.scale(scale,scale);
            g.setColor(new Color(255, 255, 255, 85)); g.fillOval(23, 12, 190, 160);
            if (day) { g.setColor(new Color(234, 182, 79)); g.fillOval(74, 35, 76, 76); g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); for(int i=0;i<8;i++){ double a=Math.PI*i/4; g.draw(new Line2D.Double(112+Math.cos(a)*49,73+Math.sin(a)*49,112+Math.cos(a)*58,73+Math.sin(a)*58)); } }
            else { g.setColor(new Color(132,155,186)); g.fillOval(83,34,78,78); g.setColor(new Color(231,241,248)); g.fillOval(106,24,71,71); }
            if (code != 1000) { g.setColor(new Color(186,207,222)); g.fillRoundRect(50, 104, 147, 42, 40, 40); g.fillOval(73,77,66,64); g.fillOval(119,88,57,54); g.setColor(new Color(248,251,253)); g.fillRoundRect(61,100,143,40,38,38); g.fillOval(84,70,66,64); g.fillOval(132,87,55,49); }
            if ((code >= 1150 && code <= 1201) || code == 1063 || (code >= 1240 && code <= 1246)) { g.setColor(new Color(82,144,189)); g.setStroke(new BasicStroke(4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND)); for(int i=0;i<3;i++)g.drawLine(97+i*32,151,91+i*32,164); }
            g.dispose();
        }
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> { ClimaDesktop app = new ClimaDesktop(); app.setVisible(true); }); }
}
