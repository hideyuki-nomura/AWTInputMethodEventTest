/**
 * 
 */
package com.example.inputmethodeventtest;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.text.Annotation;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author nomura
 *
 */
public class App extends JFrame {
	
	/**
	 * 
	 */
	public App() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JTextField fieldInput = new JTextField(20);
		final JTextField fieldReading = new JTextField(20);
		final JButton button = new JButton("clear");
		
		fieldInput.addInputMethodListener(new InputMethodListener() {
			
			public void inputMethodTextChanged(InputMethodEvent event) {
				
				StringBuilder reading = new StringBuilder();
				
				AttributedCharacterIterator it = event.getText();
				if (it == null) return;
				dump(it);
				int runStart = it.getRunStart(Attribute.READING);
				char c = AttributedCharacterIterator.DONE;
				reading.append(readReading(it));
				while ((c = it.next()) != AttributedCharacterIterator.DONE) {
					dump(it);
					
					if (runStart < it.getRunStart(Attribute.READING)) {
						reading.append(readReading(it));
					}
					runStart = it.getRunStart(Attribute.READING);
				}
				
				fieldReading.setText(reading.toString());
			}
			
			public void caretPositionChanged(InputMethodEvent event) {				
			}
		});
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				fieldInput.setText("");
				fieldReading.setText("");
			}
		});
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(createField("Input", fieldInput));
		this.getContentPane().add(createField("Reading", fieldReading));
		this.getContentPane().add(button);
		
		this.pack();
	}

	/**
	 * @param attribute
	 * @return
	 */
	protected String readReading(AttributedCharacterIterator it) {
		Object attr = it.getAttribute(Attribute.READING);
		if (attr != null && attr instanceof Annotation) {
			return ObjectUtils.toString(((Annotation)attr).getValue(), "");
		}
		return "";
	}

	/**
	 * @param it
	 */
	protected void dump(AttributedCharacterIterator it) {
		
		if (it == null) return ;
		
		System.out.println(StringUtils.join(new Object[]{
				it.current(),
				it.getRunStart(Attribute.READING),
				it.getRunLimit(Attribute.READING),
				it.getAttribute(Attribute.READING),
				it.getRunStart(Attribute.LANGUAGE),
				it.getRunLimit(Attribute.LANGUAGE),
				it.getAttribute(Attribute.LANGUAGE),
				it.getRunStart(Attribute.INPUT_METHOD_SEGMENT),
				it.getRunLimit(Attribute.INPUT_METHOD_SEGMENT),
				it.getAttribute(Attribute.INPUT_METHOD_SEGMENT)
				},
				'\t'));
	}

	/**
	 * @param string
	 * @param fieldReading
	 * @return
	 */
	private Component createField(String label, JComponent comp) {
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(new JLabel(label));
		panel.add(comp);
		return panel;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new App().setVisible(true);
	}

}
