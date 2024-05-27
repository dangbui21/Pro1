package crawl_data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Crawl_web extends JFrame implements ActionListener {
    private JButton runCrawler1Button;
    private JButton runCrawler2Button;
    private JButton runCrawler3Button;
    private JButton runCrawler4Button;
    private JTextArea logArea;

    public Crawl_web() {
        // Thiết lập cửa sổ chính
        setTitle("Crawler Runner");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Tạo các thành phần giao diện
        runCrawler1Button = new JButton("Run Crawler_1");
        runCrawler2Button = new JButton("Run Crawler_2");
        runCrawler3Button = new JButton("Run Crawler_3");
        runCrawler4Button = new JButton("Run Crawler_4");
        logArea = new JTextArea();
        logArea.setEditable(false);

        // Thêm hành động cho các nút
        runCrawler1Button.addActionListener(this);
        runCrawler2Button.addActionListener(this);
        runCrawler3Button.addActionListener(this);
        runCrawler4Button.addActionListener(this);

        // Sắp xếp các thành phần giao diện
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        panel.add(runCrawler1Button);
        panel.add(runCrawler2Button);
        panel.add(runCrawler3Button);
        panel.add(runCrawler4Button);

        JScrollPane scrollPane = new JScrollPane(logArea);

        // Thêm các thành phần vào cửa sổ chính
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == runCrawler1Button) {
            runCrawler1();
        } else if (e.getSource() == runCrawler2Button) {
            runCrawler2();
        } else if (e.getSource() == runCrawler3Button) {
            runCrawler3();
        } else if (e.getSource() == runCrawler4Button) {
            runCrawler4();
        }
    }

    private void runCrawler1() {
        logArea.append("Running Crawler_1...\n");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Crawler_1 crawler1 = new Crawler_1();
                    crawler1.runCrawler();
                    logArea.append("Crawler_1 completed successfully.\n");
                } catch (Exception ex) {
                    logArea.append("Unexpected error running Crawler_1: " + ex.getMessage() + "\n");
                }
                return null;
            }
        };
        worker.execute();
    }

    private void runCrawler2() {
        logArea.append("Running Crawler_2...\n");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Crawler_2 crawler2 = new Crawler_2();
                    crawler2.runCrawler();
                    logArea.append("Crawler_2 completed successfully.\n");
                } catch (Exception ex) {
                    logArea.append("Unexpected error running Crawler_2: " + ex.getMessage() + "\n");
                }
                return null;
            }
        };
        worker.execute();
    }

    private void runCrawler3() {
        logArea.append("Running Crawler_3...\n");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Crawler_3 crawler3 = new Crawler_3();
                    crawler3.runCrawler();
                    logArea.append("Crawler_3 completed successfully.\n");
                } catch (Exception ex) {
                    logArea.append("Unexpected error running Crawler_3: " + ex.getMessage() + "\n");
                }
                return null;
            }
        };
        worker.execute();
    }

    private void runCrawler4() {
        logArea.append("Running Crawler_4...\n");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Crawler_4 crawler4 = new Crawler_4();
                    crawler4.runCrawler();
                    logArea.append("Crawler_4 completed successfully.\n");
                } catch (Exception ex) {
                    logArea.append("Unexpected error running Crawler_4: " + ex.getMessage() + "\n");
                }
                return null;
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Crawl_web gui = new Crawl_web();
            gui.setVisible(true);
        });
    }
}
