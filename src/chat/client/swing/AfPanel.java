package chat.client.swing;



import chat.client.swing.border.AfBorder;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/** AfPanel: 用于快速布局的一个容器
 * 
 * 学习要求：本类会用即可，不要求能自己写出
 */

public class AfPanel extends JPanel
{
	private Color bgColor;

	public AfPanel()
	{
		this.setOpaque(false);// 默认背景透明
	}
		
	///////////// padding /////////////
	public AfPanel padding( int size)
	{
		return padding(size, size, size, size);
	}
	
	public AfPanel padding( int top, int left, int bottom, int right)
	{
		AfBorder.addPadding(this, top,left, bottom,right);
		return this;
	}

	////////////// margin ////////////////
	public AfPanel margin( int size)
	{
		return margin(size, size, size, size);
	}
	
	public AfPanel margin( int top, int left, int bottom, int right)
	{
		AfBorder.addMargin(this, top,left, bottom,right);
		return this;
	}

	// 附加一个外边框
	public void addOuterBorder(JComponent c, Border outerBorder)
	{
		AfBorder.addOuterBorder(this, outerBorder);
	}

	// 附加一个内边框
	public void addInnerBorder(JComponent c, Border innerBorder)
	{
		AfBorder.addInnerBorder(this, innerBorder);
	}
	
	//////////// perferred size ///////////
	public AfPanel preferredSize(int w, int h)
	{
		this.setPreferredSize(new Dimension(w, h));
		return this;
	}
	
	public AfPanel preferredWidth(int w)
	{
		Dimension size = this.getPreferredSize();
		if(size == null)
			size = new Dimension(0,0);
		size.width = w;
		this.setPreferredSize( size);
		return this;
	}
	
	public AfPanel preferredHeight(int h)
	{
		Dimension size = this.getPreferredSize();
		if(size == null)
			size = new Dimension(0,0);
		size.height = h;
		this.setPreferredSize( size);
		return this;
	}
	
	public void setBgColor(Color color)
	{
		this.bgColor = color;
		this.repaint();
	}
	
	// 背景色绘制 ( 参考 Swing入门篇 第9章 )
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		// 绘制背景色
		if( bgColor != null)
		{
			Graphics2D g2d = (Graphics2D)g;	
			g2d.setPaint(bgColor);
			g2d.fillRect(0, 0, getWidth(), getHeight());
		}	
	}
	
}
