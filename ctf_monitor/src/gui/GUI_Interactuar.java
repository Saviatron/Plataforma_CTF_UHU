/** 
 * Copyright (C) 2019  Javier Martín Moreno
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import config.Config_GUI;

/**
 * JFRAME QUE INTERACTUA CON EL SKETCH
 */
@SuppressWarnings("serial")
public class GUI_Interactuar extends JFrame {

	public DefaultListModel<String> dlm[];
	public JList<String>[] jugadores;
	public JScrollPane[] sPane;
	private int anchoTexto = 100;
	private boolean play = true;
	private boolean atras = false;
	private boolean alante = false;
	private boolean lector = false;
	private String InfoLector = "Información:\n***********************\n\n";
	public JTextArea t_info;
	private int NumEq;
	private int tamx, tamy;
	private String CABECERA = "Equipo ";

	public GUI_Interactuar(String _titulo, String msg, GUI_Mapa sketch, boolean lector) {
		super(_titulo);
		this.lector = lector;
		initComponents(msg, sketch);
	}

	@SuppressWarnings("unchecked")
	private void initComponents(String msg, GUI_Mapa sketch) {
		StringTokenizer linea = new StringTokenizer(msg, "\n");

		StringTokenizer linea1 = new StringTokenizer(linea.nextToken(), ",");
		linea1.nextToken();
		tamx = Integer.parseInt(linea1.nextToken());
		tamy = Integer.parseInt(linea1.nextToken());
		this.NumEq = Integer.parseInt(linea1.nextToken());
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		setDefaultLookAndFeelDecorated(true);
		getContentPane().setBackground(Color.DARK_GRAY);

		dlm = new DefaultListModel[NumEq];
		jugadores = new JList[NumEq];
		sPane = new JScrollPane[NumEq];
		for (int i = 0; i < NumEq; i++) {
			dlm[i] = new DefaultListModel<String>();
			dlm[i].addElement(CABECERA + Config_GUI.Equipos[i]);
			dlm[i].addElement("\n***************\n");

			jugadores[i] = new JList<String>(dlm[i]);
			jugadores[i].setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jugadores[i].setLayoutOrientation(JList.VERTICAL);
			jugadores[i].setPreferredSize(new Dimension(200, 140));

			sPane[i] = new JScrollPane(jugadores[i], JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			sPane[i].setBounds((Config_GUI.ANCHO_CASILLA + 1) * tamx + 1, 1, anchoTexto,
					(Config_GUI.ALTO_CASILLA + 1) * tamy);
		}
		for (int i = 0; i < jugadores.length; i++) {
			JList<String> list = jugadores[i];
			list.addListSelectionListener(new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent arg0) {
					if (!arg0.getValueIsAdjusting()) {
						sketch.setJugadorCamara(list.getSelectedValue());
						// list.clearSelection();
					}
				}
			});

		}

		String[] camaraStrings = { "Camara 1", "Camara 2", "Camara 3", "Camara 4", "Camara 5", "Camara 6", "Camara 7" };

		JComboBox<String> camaraList = new JComboBox<String>(camaraStrings);
		if (Config_GUI.IS3D)
			camaraList.setSelectedIndex(0);
		else
			camaraList.setSelectedIndex(1);

		camaraList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sketch.setCamara(camaraList.getSelectedIndex() + 1);
			}

		});

		JRadioButton button3D = new JRadioButton("3D");
		button3D.setActionCommand("true");
		button3D.setSelected(true);

		JRadioButton button2D = new JRadioButton("2D");
		button2D.setActionCommand("false");

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(button3D);
		group.add(button2D);

		button3D.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sketch.set3D(button3D.isSelected());
				sketch.setCamara(1);
			}

		});

		button2D.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sketch.set3D(!button2D.isSelected());
				sketch.setCamara(2);
			}

		});

		if (!lector)
			for (int i = 0; i < NumEq; i++)
				this.add(sPane[i]);
		this.add(camaraList);
		this.add(button3D);
		this.add(button2D);
		if (lector) {
			t_info = new JTextArea();
			t_info.setPreferredSize(new Dimension(200, 150));
			t_info.setEditable(false);
			t_info.setText(InfoLector);
			t_info.setLineWrap(true);

			JScrollPane s_info = new JScrollPane(t_info, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			s_info.setBounds((Config_GUI.ANCHO_CASILLA + 1) * tamx + 1, 1, anchoTexto,
					(Config_GUI.ALTO_CASILLA + 1) * tamy);

			this.add(s_info);

			JButton Atras = new JButton("Atras");
			Atras.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					atras = true;
				}

			});
			this.add(Atras);
			JButton Alante = new JButton("Alante");
			Alante.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					alante = true;
				}

			});
			this.add(Alante);
			JButton PlayButton = new JButton("Pause");
			PlayButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					play = !play;
					if (play)
						PlayButton.setText("Pause");
					else
						PlayButton.setText("Play");
				}

			});
			this.add(PlayButton);
		}
		pack();

		// TODO
		setLayout(new FlowLayout());
		setSize(220 * (int) Math.ceil((float) NumEq / 4), 740);
		// setIconImage(image);
		setLocation(0, 0);

	}

	public void muestraMensajeGUI(String titulo, String contenido) {

		if (Config_GUI.SONIDO) {
			Sonido.musicaPartida.close();
			Sonido.musicaFin.play();
		}
		JOptionPane.showMessageDialog(this, contenido, titulo, JOptionPane.INFORMATION_MESSAGE);
	}

	public void actInfoLector(String info) {

		t_info.setText(InfoLector + info);
	}

	public void rePintarJugadores(int eq, String jugadores) {
		StringTokenizer st = new StringTokenizer(jugadores, "\n");
		dlm[eq].clear();
		Config_GUI.Equipos[eq] = st.nextToken();
		dlm[eq].addElement(CABECERA + Config_GUI.Equipos[eq]);
		dlm[eq].addElement("\n***************\n");
		while (st.hasMoreTokens()) {
			dlm[eq].addElement(st.nextToken());
		}
	}

	public boolean getPlay() {
		return play;
	}

	public boolean getAtras() {
		return atras;
	}

	public boolean getAlante() {
		return alante;
	}

	public void falseAtras() {
		atras = false;
	}

	public void falseAlante() {
		alante = false;
	}

}
