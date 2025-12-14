package gui.page.thongke;

public class ThongKeDoanhThuPage extends javax.swing.JPanel {

	public ThongKeDoanhThuPage() {
		initComponents();
		initLayout();
	}

	private void initLayout() {
		this.add(tabPane);
	}

	private void initComponents() {

		tabPane = new javax.swing.JTabbedPane();

		setBackground(new java.awt.Color(230, 245, 245));
		setMinimumSize(new java.awt.Dimension(1130, 800));
		setPreferredSize(new java.awt.Dimension(1130, 800));
		setLayout(new java.awt.BorderLayout(0, 6));

		tabPane.setPreferredSize(new java.awt.Dimension(100, 30));
		add(tabPane, java.awt.BorderLayout.PAGE_START);
	}

	private javax.swing.JTabbedPane tabPane;
}
