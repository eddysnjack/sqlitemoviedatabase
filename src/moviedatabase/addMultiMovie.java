
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviedatabase;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.apache.commons.io.FileUtils;
import org.eclipse.persistence.tools.file.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Eddy
 */
public class addMultiMovie extends javax.swing.JFrame {
    Boolean mouseClickedSwitch=true;
    String PosterPath=null;
    static Statement iamastate;
    static Connection iamaconnection;
    Timer counter;
    ArrayList<String> queListArray=new ArrayList<String>();
    int queueIndex=0;
    String Currentimdbid="";
    
    //==========================================================================
    public addMultiMovie() {
        initComponents();

         MYinitiations();
    }

    
    
    
    //==========================================================================
    public static void CONNECTofadd(){
        try {
            iamaconnection = DriverManager.getConnection("jdbc:sqlite:moviedatabase.db");
            iamastate = iamaconnection.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "bağlantı veya database hatası \nhata: \n" + ex.getMessage());
        }
    }
    /**
     * Creates new form addmovieframe
     */
    
    //==========================================================================
    private void StartQueueJob(){
        if(!queListArray.isEmpty()){
            jTextFieldImdbId.setText(queListArray.get(queueIndex));
//            Currentimdbid=queListArray.get(queueIndex);
            StartFetch();
            
        }
    }
    private void StartFetch(){
        JCheckBox[] checkboxes={jCheckBox2,jCheckBox3,jCheckBox4,jCheckBox5,jCheckBox6,jCheckBox7,jCheckBox8,jCheckBox9,
            jCheckBox10,jCheckBox11,jCheckBox12,jCheckBox13,jCheckBox14,jCheckBox15,jCheckBox16,jCheckBox17,jCheckBox18,
            jCheckBox19,jCheckBox20,jCheckBox21,jCheckBox22,jCheckBox23,jCheckBox24,jCheckBox25,jCheckBox26,jCheckBox27};
        for (int i = 0; i < checkboxes.length; i++) {
            checkboxes[i].setSelected(false);
        }
        jButton2ImdbFetch.doClick();
    }
    private void fetchIsOk(){
        if(jCheckBox1Autosave.isSelected()){
            jButton1SAVE.doClick();
        }
    }
    private void SavingIsDone(){
        if(queueIndex < queListArray.size()-1){
            queueIndex++;
            StartQueueJob();
        }else{
            queueIndex=0;
            JOptionPane.showMessageDialog(null, "Queue is done!");
        }
    }
    //==========================================================================
    private void MYinitiations(){
        counter=new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                jTextFieldOrName.setText("zımbırtı");
//                setEnabled(true);
            }
        }
        );
        counter.setRepeats(false);
        //--------------------------------------------------------
        jComboBox1Search.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if(ke.getKeyCode()==KeyEvent.VK_ENTER){
                    Document doc=null;
                    String query=jComboBox1Search.getSelectedItem().toString();
                    try {
                        doc= Jsoup.connect("http://www.imdb.com/find?s=tt&q="+query).timeout(15000).userAgent("Mozilla").get();
                        int resultsCount=doc.select("td.result_text > a").size();
                        DefaultComboBoxModel dfcmd=new DefaultComboBoxModel();
                        if(resultsCount>0 && resultsCount<5){
                        for (int i = 0; i < resultsCount; i++) {
                                dfcmd.addElement(doc.select("td.result_text").get(i).text()+"#"+doc.select("td.result_text > a").get(i).attr("href"));
                            }
                        }
                        if(resultsCount>5){
                            for (int i = 0; i < 5; i++) {
                                dfcmd.addElement(doc.select("td.result_text").get(i).text()+"#"+doc.select("td.result_text > a").get(i).attr("href"));
                            }
                        }
                    jComboBox1Search.setModel(dfcmd);
                    } catch (IOException ex) {
                        Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldOrName = new javax.swing.JTextField();
        jTextFieldAlName = new javax.swing.JTextField();
        jTextFieldYear = new javax.swing.JTextField();
        jTextFieldImdbId = new javax.swing.JTextField();
        jTextFieldImdbVotes = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1Comment = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jCheckBox1Watched = new javax.swing.JCheckBox();
        jButton1SAVE = new javax.swing.JButton();
        jFormattedTextFieldPerRat = new javax.swing.JFormattedTextField();
        jFormattedTextFieldIMDBRat = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox17 = new javax.swing.JCheckBox();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox26 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        jCheckBox23 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox27 = new javax.swing.JCheckBox();
        jButton2ImdbFetch = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel10Poster = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea3Actorids = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2Actornames = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea4Directorname = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea5Directorid = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2writerName = new javax.swing.JTextArea();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea3WriterID = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea4MusicNAme = new javax.swing.JTextArea();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextArea5MusicID = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTextArea6CamName = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTextArea7CamID = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jTextField1Countrys = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jCheckBox1Autosave = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jComboBox1Search = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jButton1StartQueue = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTextPane1Queue = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 426));

        jLabel1.setText("Original  Name:*");

        jLabel2.setText("Alternative Name:");

        jLabel3.setText("Year:");

        jLabel4.setText("IMDB Id:");

        jLabel5.setText("IMDB Rating:");

        jLabel6.setText("IMDB Votes:");

        jLabel7.setText("Personal Rating:");

        jTextFieldOrName.setText("Required Field");
        jTextFieldOrName.setPreferredSize(new java.awt.Dimension(230, 20));
        jTextFieldOrName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextFieldOrNameMouseClicked(evt);
            }
        });
        jTextFieldOrName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldOrNameKeyTyped(evt);
            }
        });

        jTextFieldAlName.setPreferredSize(new java.awt.Dimension(230, 20));

        jTextFieldYear.setPreferredSize(new java.awt.Dimension(230, 20));
        jTextFieldYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldYearKeyTyped(evt);
            }
        });

        jTextFieldImdbId.setPreferredSize(new java.awt.Dimension(230, 20));
        jTextFieldImdbId.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTextFieldImdbIdMouseReleased(evt);
            }
        });

        jTextFieldImdbVotes.setPreferredSize(new java.awt.Dimension(230, 20));
        jTextFieldImdbVotes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldImdbVotesKeyTyped(evt);
            }
        });

        jTextArea1Comment.setColumns(20);
        jTextArea1Comment.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea1Comment.setRows(5);
        jScrollPane1.setViewportView(jTextArea1Comment);

        jLabel9.setText("Comment:");

        jCheckBox1Watched.setText("Watched");

        jButton1SAVE.setText("SAVE");
        jButton1SAVE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1SAVEActionPerformed(evt);
            }
        });

        jFormattedTextFieldPerRat.setPreferredSize(new java.awt.Dimension(230, 20));
        jFormattedTextFieldPerRat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextFieldPerRatActionPerformed(evt);
            }
        });
        jFormattedTextFieldPerRat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldPerRatKeyTyped(evt);
            }
        });

        jFormattedTextFieldIMDBRat.setPreferredSize(new java.awt.Dimension(230, 20));
        jFormattedTextFieldIMDBRat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldIMDBRatKeyTyped(evt);
            }
        });

        jLabel8.setText("Genres:");

        jPanel2.setLayout(new java.awt.GridLayout(10, 5, 2, 2));

        jCheckBox14.setText("Action");
        jPanel2.add(jCheckBox14);

        jCheckBox21.setText("Adventure");
        jPanel2.add(jCheckBox21);

        jCheckBox11.setText("Animation");
        jPanel2.add(jCheckBox11);

        jCheckBox12.setText("Biography");
        jPanel2.add(jCheckBox12);

        jCheckBox6.setText("Comedy");
        jPanel2.add(jCheckBox6);

        jCheckBox5.setText("Crime");
        jPanel2.add(jCheckBox5);

        jCheckBox13.setText("Documentary");
        jCheckBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox13ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox13);

        jCheckBox17.setText("Drama");
        jPanel2.add(jCheckBox17);

        jCheckBox24.setText("Family");
        jPanel2.add(jCheckBox24);

        jCheckBox8.setText("Fantasy");
        jPanel2.add(jCheckBox8);

        jCheckBox15.setText("Film-Noir");
        jPanel2.add(jCheckBox15);

        jCheckBox18.setText("Game-Show");
        jPanel2.add(jCheckBox18);

        jCheckBox16.setText("History");
        jPanel2.add(jCheckBox16);

        jCheckBox25.setText("Horror");
        jPanel2.add(jCheckBox25);

        jCheckBox19.setText("Music");
        jPanel2.add(jCheckBox19);

        jCheckBox4.setText("Musical");
        jPanel2.add(jCheckBox4);

        jCheckBox26.setText("Mystery");
        jPanel2.add(jCheckBox26);

        jCheckBox7.setText("News");
        jPanel2.add(jCheckBox7);

        jCheckBox9.setText("Reality-TV");
        jPanel2.add(jCheckBox9);

        jCheckBox10.setText("Romance");
        jPanel2.add(jCheckBox10);

        jCheckBox3.setText("Sci-Fi");
        jPanel2.add(jCheckBox3);

        jCheckBox22.setText("Sport");
        jPanel2.add(jCheckBox22);

        jCheckBox23.setText("Talk-Show");
        jPanel2.add(jCheckBox23);

        jCheckBox2.setText("Thriller");
        jPanel2.add(jCheckBox2);

        jCheckBox20.setText("War");
        jPanel2.add(jCheckBox20);

        jCheckBox27.setText("Western");
        jPanel2.add(jCheckBox27);

        jButton2ImdbFetch.setText("Fetch From IMDB");
        jButton2ImdbFetch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ImdbFetchActionPerformed(evt);
            }
        });

        jLabel10Poster.setBackground(new java.awt.Color(0, 0, 0));
        jLabel10Poster.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10Poster.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10Poster.setText("Add a Poster");
        jLabel10Poster.setOpaque(true);
        jLabel10Poster.setPreferredSize(new java.awt.Dimension(150, 150));
        jLabel10Poster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10PosterMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10Poster, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10Poster, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTextArea3Actorids.setColumns(10);
        jTextArea3Actorids.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea3Actorids.setRows(5);
        jScrollPane4.setViewportView(jTextArea3Actorids);

        jLabel16.setText("Actor_id");

        jTextArea2Actornames.setColumns(10);
        jTextArea2Actornames.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea2Actornames.setRows(5);
        jScrollPane3.setViewportView(jTextArea2Actornames);

        jLabel15.setText("Actors:");

        jTextArea4Directorname.setColumns(10);
        jTextArea4Directorname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea4Directorname.setRows(2);
        jScrollPane5.setViewportView(jTextArea4Directorname);

        jTextArea5Directorid.setColumns(10);
        jTextArea5Directorid.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea5Directorid.setRows(2);
        jScrollPane6.setViewportView(jTextArea5Directorid);

        jLabel10.setText("Directors Names:");
        jLabel10.setPreferredSize(new java.awt.Dimension(50, 15));

        jLabel11.setText("Writers:");
        jLabel11.setPreferredSize(new java.awt.Dimension(50, 15));

        jLabel12.setText("Musicians:");
        jLabel12.setPreferredSize(new java.awt.Dimension(50, 15));

        jLabel13.setText("Camera Directors:");
        jLabel13.setPreferredSize(new java.awt.Dimension(50, 15));

        jTextArea2writerName.setColumns(10);
        jTextArea2writerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea2writerName.setRows(2);
        jScrollPane2.setViewportView(jTextArea2writerName);

        jTextArea3WriterID.setColumns(10);
        jTextArea3WriterID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea3WriterID.setRows(2);
        jScrollPane7.setViewportView(jTextArea3WriterID);

        jTextArea4MusicNAme.setColumns(10);
        jTextArea4MusicNAme.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea4MusicNAme.setRows(2);
        jScrollPane8.setViewportView(jTextArea4MusicNAme);

        jTextArea5MusicID.setColumns(10);
        jTextArea5MusicID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea5MusicID.setRows(2);
        jScrollPane9.setViewportView(jTextArea5MusicID);

        jTextArea6CamName.setColumns(10);
        jTextArea6CamName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea6CamName.setRows(2);
        jScrollPane10.setViewportView(jTextArea6CamName);

        jTextArea7CamID.setColumns(10);
        jTextArea7CamID.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea7CamID.setRows(2);
        jScrollPane11.setViewportView(jTextArea7CamID);

        jLabel17.setText("ID's");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane5)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15))
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                            .addComponent(jScrollPane4)))
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTextField1Countrys.setPreferredSize(new java.awt.Dimension(231, 20));

        jLabel14.setText("Countrys:");
        jLabel14.setPreferredSize(new java.awt.Dimension(50, 20));

        jCheckBox1Autosave.setText("AutoSave");
        jCheckBox1Autosave.setVisible(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(48, 48, 48)
                                .addComponent(jScrollPane1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldImdbId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldImdbVotes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jCheckBox1Watched, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextFieldPerRat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                    .addComponent(jFormattedTextFieldIMDBRat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                                    .addComponent(jTextFieldAlName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldYear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldOrName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField1Countrys, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jCheckBox1Autosave)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2ImdbFetch)
                                .addGap(21, 21, 21)
                                .addComponent(jButton1SAVE, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jFormattedTextFieldPerRat, jTextFieldImdbVotes});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jFormattedTextFieldIMDBRat, jTextFieldImdbId, jTextFieldYear});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextFieldOrName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jTextFieldAlName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextFieldYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextFieldImdbId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jFormattedTextFieldIMDBRat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextFieldImdbVotes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jFormattedTextFieldPerRat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7)))
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox1Watched)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField1Countrys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jButton1SAVE)
                                            .addComponent(jButton2ImdbFetch)))
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox1Autosave)))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jCheckBox1Watched, jFormattedTextFieldIMDBRat, jFormattedTextFieldPerRat, jLabel1, jLabel2, jLabel4, jLabel5, jLabel6, jLabel7, jTextFieldAlName, jTextFieldImdbId, jTextFieldImdbVotes, jTextFieldOrName, jTextFieldYear});

        jComboBox1Search.setEditable(true);
        jComboBox1Search.setPreferredSize(new java.awt.Dimension(128, 20));
        jComboBox1Search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1SearchActionPerformed(evt);
            }
        });

        jLabel18.setText("Search Online:");

        jButton1StartQueue.setText("StartQueue");
        jButton1StartQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1StartQueueActionPerformed(evt);
            }
        });

        jTextPane1Queue.setPreferredSize(new java.awt.Dimension(128, 340));
        jScrollPane12.setViewportView(jTextPane1Queue);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBox1Search, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jButton1StartQueue))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1Search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1StartQueue)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1034, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1SAVEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1SAVEActionPerformed
        // SAVE BUTTON
//        moviedatabasejframe mainpage=new moviedatabasejframe();
//        mainpage.CONNECT();
//        Statement st=mainpage.getstate();

//----------------------------------------------------------------------------------------------------------------
//                                              MOVIE INFO
//----------------------------------------------------------------------------------------------------------------
                CONNECTofadd();
        ResultSet rs;
        String watched;
        if(jCheckBox1Watched.isSelected()){ watched="YES"; }else{watched="NO";}
        JCheckBox[] checkboxes={jCheckBox2,jCheckBox3,jCheckBox4,jCheckBox5,jCheckBox6,jCheckBox7,jCheckBox8,jCheckBox9,
            jCheckBox10,jCheckBox11,jCheckBox12,jCheckBox13,jCheckBox14,jCheckBox15,jCheckBox16,jCheckBox17,jCheckBox18,
            jCheckBox19,jCheckBox20,jCheckBox21,jCheckBox22,jCheckBox23,jCheckBox24,jCheckBox25,jCheckBox26,jCheckBox27};
        
        try {
            if(! jTextFieldImdbId.getText().isEmpty()){
                rs=iamastate.executeQuery("Select * From movies Where imdbid=\""+jTextFieldImdbId.getText()+"\"");
                if(rs.next()){
                    JOptionPane.showMessageDialog(null,"aynı imdb id'li film mevcut. Maalesef ekleyemezsiniz.");
                    SavingIsDone();
                }
            }
            
            PreparedStatement ppst=iamaconnection.prepareStatement("INSERT INTO "
                    + "movies(originalname,alternatename,year,imdbid,imdbrating,imdbvotes,watch,personalrating,comment,country,poster,genre)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            
           if (!jTextFieldOrName.getText().isEmpty()) {//NOT NULL!
                ppst.setObject(1, jTextFieldOrName.getText());
            } else {
                JOptionPane.showMessageDialog(this, "looks like u forget the name ☺");
                return;
            }
           
            if (!jTextFieldAlName.getText().isEmpty()) {
                ppst.setObject(2, jTextFieldAlName.getText());
            }else{
                ppst.setObject(2, null);
            }
            
            if(!jTextFieldYear.getText().isEmpty()) {
                ppst.setObject(3, Integer.parseInt(jTextFieldYear.getText()));
            }else{
                ppst.setObject(3, null);
            }
            
            if(!jTextFieldImdbId.getText().isEmpty()){
               ppst.setObject(4, jTextFieldImdbId.getText());
            }else{
                ppst.setObject(4, null);
            }
            
            if(!jFormattedTextFieldIMDBRat.getText().isEmpty()){
                ppst.setObject(5, jFormattedTextFieldIMDBRat.getText());
            }else{
                ppst.setObject(5, null);
            }
            
            if(!jTextFieldImdbVotes.getText().isEmpty()){
                ppst.setObject(6, jTextFieldImdbVotes.getText());
            }else{
                ppst.setObject(6, null);
            }
            
                                            ppst.setObject(7, watched);//özel
            
            if(!jFormattedTextFieldPerRat.getText().isEmpty()){
                ppst.setObject(8, jFormattedTextFieldPerRat.getText());
            }else{
                ppst.setObject(8, null);
            }
            
            if(!jTextArea1Comment.getText().isEmpty()){
                ppst.setObject(9, jTextArea1Comment.getText());
            }else{
                ppst.setObject(9, null);
            }
            
            if(!jTextField1Countrys.getText().isEmpty()){
                ppst.setObject(10, jTextField1Countrys.getText());
            }else{
                ppst.setObject(10, null);
            }
            
                                            ppst.setObject(11, PosterPath);
                                            
            String genre="";
             for (int i = 0; i < checkboxes.length; i++) {
                if (checkboxes[i].isSelected()) {
                   genre=genre+checkboxes[i].getText()+",";
                     }
            }
            if(!genre.isEmpty()){//for döngüsünde oluşan en sondaki virgülü kesmek için 
                genre=genre.substring(0, genre.length()-1);
            }
                                            ppst.setObject(12, genre);
            
            ppst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "hata"+ex);
            return;
        }
//----------------------------------------------------------------------------------------------------------------
//                                              PERSONS
//----------------------------------------------------------------------------------------------------------------
        try {
            rs=iamastate.executeQuery("SELECT * FROM movies order by datetimeUTC desc limit 1");
            rs.next();
            String CurrentMovieId=rs.getString("id");
            PreparedStatement persons = iamaconnection.prepareStatement("INSERT INTO persons(name,imdbid) VALUES(?,?)");
            PreparedStatement haspersons = iamaconnection.prepareStatement("INSERT INTO movies_has_persons(movies_id,role,persons_id) VALUES(?,?,?)");
//----------------------------------------------DIRECTOR
            if(!jTextArea4Directorname.getText().isEmpty() && !jTextArea5Directorid.getText().isEmpty()){
                String []Dirnamelines=jTextArea4Directorname.getText().split("\n");
                String []DirIDLines=jTextArea5Directorid.getText().split("\n");
            
                for (int i = 0; i < Dirnamelines.length; i++) {
                    if(!Dirnamelines[i].isEmpty() && !DirIDLines[i].isEmpty()){
                     rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+Dirnamelines[i]+"\" AND imdbid=\""+DirIDLines[i]+"\"");
                         if(!rs.next()){
                            persons.setString(1, Dirnamelines[i]);
                            persons.setString(2,DirIDLines[i]);
                            persons.execute();
                         }
                    }
                }
            }
//----------------------------------------------WRITER
            if(!jTextArea2writerName.getText().isEmpty() && !jTextArea3WriterID.getText().isEmpty()){
                String []WritersName=jTextArea2writerName.getText().split("\n");
                String []WritersID=jTextArea3WriterID.getText().split("\n");
            
                for (int i = 0; i < WritersName.length; i++) {
                    if(!WritersName[i].isEmpty() && !WritersID[i].isEmpty()){
                     rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+WritersName[i]+"\" AND imdbid=\""+WritersID[i]+"\"");
                         if(!rs.next()){
                            persons.setString(1, WritersName[i]);
                            persons.setString(2,WritersID[i]);
                            persons.execute();
                         }
                    }
                }
            }
            
            
//----------------------------------------------MUSICIANS
            if(!jTextArea4MusicNAme.getText().isEmpty() && !jTextArea5MusicID.getText().isEmpty()){
                String []MusicianName=jTextArea4MusicNAme.getText().split("\n");
                String []MusicianID=jTextArea5MusicID.getText().split("\n");
            
                for (int i = 0; i < MusicianName.length; i++) {
                    if(!MusicianName[i].isEmpty() && !MusicianID[i].isEmpty()){
                     rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+MusicianName[i]+"\" AND imdbid=\""+MusicianID[i]+"\"");
                         if(!rs.next()){
                            persons.setString(1, MusicianName[i]);
                            persons.setString(2,MusicianID[i]);
                            persons.execute();
                         }
                    }
                }
            }
//----------------------------------------------CAMERA
            if(!jTextArea6CamName.getText().isEmpty() && !jTextArea7CamID.getText().isEmpty()){
                String []CamName=jTextArea6CamName.getText().split("\n");
                String []CamID=jTextArea7CamID.getText().split("\n");
            
                for (int i = 0; i < CamName.length; i++) {
                    if(!CamName[i].isEmpty() && !CamID[i].isEmpty()){
                     rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+CamName[i]+"\" AND imdbid=\""+CamID[i]+"\"");
                         if(!rs.next()){
                            persons.setString(1, CamName[i]);
                            persons.setString(2,CamID[i]);
                            persons.execute();
                         }
                    }
                }
            }
//----------------------------------------------ACTORS
            if(!jTextArea2Actornames.getText().isEmpty() && !jTextArea3Actorids.getText().isEmpty()){
                String []Actornamelines=jTextArea2Actornames.getText().split("\n");
                String []ActorIDLines=jTextArea3Actorids.getText().split("\n");
            
                for (int i = 0; i < Actornamelines.length; i++) {
                    if(!Actornamelines[i].isEmpty() && !ActorIDLines[i].isEmpty()){
                     rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+Actornamelines[i]+"\" AND imdbid=\""+ActorIDLines[i]+"\"");
                        if(!rs.next()){
                           persons.setString(1, Actornamelines[i]);
                           persons.setString(2,ActorIDLines[i]);
                           persons.execute();
                        }
                    }
                }
            }
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ if doesnt exist save the persons
//-----------------------------------------------------------------------------------------------------------------------
//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ connect this peoples to this movie

//----------------------------------------------DIRECTOR►◄MOVIE
        if(!jTextArea4Directorname.getText().isEmpty() && !jTextArea5Directorid.getText().isEmpty()){
                String []Dirnamelines=jTextArea4Directorname.getText().split("\n");
                String []DirIDLines=jTextArea5Directorid.getText().split("\n");
            
                for (int i = 0; i < Dirnamelines.length; i++) {
                    rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+Dirnamelines[i]+"\" AND imdbid=\""+DirIDLines[i]+"\"");
                    if(rs.next()){
                        String personid=rs.getString("id");
                        haspersons.setString(1, CurrentMovieId);
                        haspersons.setString(2,"Director");
                        haspersons.setString(3,personid);
                        haspersons.execute();
                    }
                }
            }
//----------------------------------------------WRITERS►◄MOVIE
            if(!jTextArea2writerName.getText().isEmpty() && !jTextArea3WriterID.getText().isEmpty()){
                String []WritersName=jTextArea2writerName.getText().split("\n");
                String []WritersID=jTextArea3WriterID.getText().split("\n");
            
                for (int i = 0; i < WritersName.length; i++) {
                    rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+WritersName[i]+"\" AND imdbid=\""+WritersID[i]+"\"");
                    if(rs.next()){
                        String personid=rs.getString("id");
                        haspersons.setString(1, CurrentMovieId);
                        haspersons.setString(2,"Writer");
                        haspersons.setString(3,personid);
                        haspersons.execute();
                    }
                }
            }
//----------------------------------------------MUSİC►◄MOVIE
            if(!jTextArea4MusicNAme.getText().isEmpty() && !jTextArea5MusicID.getText().isEmpty()){
                String []MusicianName=jTextArea4MusicNAme.getText().split("\n");
                String []MusicianID=jTextArea5MusicID.getText().split("\n");
            
                for (int i = 0; i < MusicianName.length; i++) {
                    rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+MusicianName[i]+"\" AND imdbid=\""+MusicianID[i]+"\"");
                    if(rs.next()){
                        String personid=rs.getString("id");
                        haspersons.setString(1, CurrentMovieId);
                        haspersons.setString(2,"Musician");
                        haspersons.setString(3,personid);
                        haspersons.execute();
                    }
                }
            }
//----------------------------------------------CAMERA►◄MOVIE
            if(!jTextArea6CamName.getText().isEmpty() && !jTextArea7CamID.getText().isEmpty()){
                String []CamName=jTextArea6CamName.getText().split("\n");
                String []CamID=jTextArea7CamID.getText().split("\n");
            
                for (int i = 0; i < CamName.length; i++) {
                    rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+CamName[i]+"\" AND imdbid=\""+CamID[i]+"\"");
                    if(rs.next()){
                        String personid=rs.getString("id");
                        haspersons.setString(1, CurrentMovieId);
                        haspersons.setString(2,"Camera");
                        haspersons.setString(3,personid);
                        haspersons.execute();
                    }
                }
            }

//----------------------------------------------ACTORS►◄MOVIE
        if(!jTextArea2Actornames.getText().isEmpty() && !jTextArea3Actorids.getText().isEmpty()){
                String []Actornamelines=jTextArea2Actornames.getText().split("\n");
                String []ActorIDLines=jTextArea3Actorids.getText().split("\n");
            
                for (int i = 0; i < Actornamelines.length; i++) {
                    if(!Actornamelines[i].isEmpty() && !ActorIDLines[i].isEmpty()){
                    rs=iamastate.executeQuery("SELECT * FROM persons WHERE name=\""+Actornamelines[i]+"\" AND imdbid=\""+ActorIDLines[i]+"\"");
                        if(rs.next()){
                            String personid=rs.getString("id");
                            haspersons.setString(1,CurrentMovieId);
                            haspersons.setString(2,"Actor");
                            haspersons.setString(3,personid);
                            haspersons.execute();
                        }
                    }
                }
            }
            JOptionPane.showMessageDialog(null,"Başarıyla Kaydedildi");
            
            //-----------
            
            iamaconnection.close();
            SavingIsDone();
            
//            dispose();
        } catch (SQLException ex) {
            Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
            
            //for refresing the table↓
//            mainpage.CONNECT(); //olayları biraz değiştirdim. artık kendi connect fonksiyonumuz var. mainpage nesnesi oluşturmaya da gerek kalmadı
           
//            mainpage.setVisible(true);  çıkışta gösterim yapıldığı için buna gerek kalmadı artık
//            mainpage.refreshTheTable();
            
            //for refresing the table↓
//            mainpage.CONNECT(); //olayları biraz değiştirdim. artık kendi connect fonksiyonumuz var. mainpage nesnesi oluşturmaya da gerek kalmadı
           
//            mainpage.setVisible(true);  çıkışta gösterim yapıldığı için buna gerek kalmadı artık
//            mainpage.refreshTheTable();
            
        
    }//GEN-LAST:event_jButton1SAVEActionPerformed

    private void jCheckBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox13ActionPerformed

    private void jTextFieldYearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldYearKeyTyped
          if(evt.getKeyChar()<'0' || evt.getKeyChar()>'9'){
           evt.consume();
       }
    }//GEN-LAST:event_jTextFieldYearKeyTyped

    private void jFormattedTextFieldIMDBRatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldIMDBRatKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) &&  !(evt.getKeyChar()==KeyEvent.VK_PERIOD)){
            evt.consume();
        }
    }//GEN-LAST:event_jFormattedTextFieldIMDBRatKeyTyped

    private void jFormattedTextFieldPerRatKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldPerRatKeyTyped
       if(!Character.isDigit(evt.getKeyChar()) &&  !(evt.getKeyChar()==KeyEvent.VK_PERIOD)){
            evt.consume();
        }            
    }//GEN-LAST:event_jFormattedTextFieldPerRatKeyTyped

    private void jTextFieldImdbVotesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldImdbVotesKeyTyped
        if(!Character.isDigit(evt.getKeyChar()) &&  !(evt.getKeyChar()==KeyEvent.VK_COMMA)){
            evt.consume();
        } 
    }//GEN-LAST:event_jTextFieldImdbVotesKeyTyped

    private void jLabel10PosterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10PosterMouseClicked
        JFileChooser Fcoose = new JFileChooser();
        FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        Fcoose.addChoosableFileFilter(imageFilter);
        Fcoose.setAcceptAllFileFilterUsed(false);
        int result = Fcoose.showOpenDialog(jPanel1);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = Fcoose.getSelectedFile();
            String input = selectedFile.getPath();
            if (!(new File("images").exists())) {
                new File("images").mkdir();
            }
            File outputFile = new File("images\\" + selectedFile.getName());
            if (!outputFile.exists()) {
                try {
                    outputFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                InputStream inpStream = new FileInputStream(selectedFile);
                OutputStream outpStream = new FileOutputStream(outputFile);
                try {
                    FileUtil.copy(inpStream, outpStream);
                    inpStream.close();
                    outpStream.flush();
                    outpStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (outputFile.exists()) {
                PosterPath = outputFile.getPath();
                jLabel10Poster.setText("");
                String imagesrc=outputFile.getPath();
                BufferedImage newimage = null;
                try {
                    File fileitem= new File(imagesrc);
                    newimage=ImageIO.read(fileitem);
                    float imagewidth=newimage.getWidth();
                    float imageheight=newimage.getHeight();
                    float imageratio=imagewidth/imageheight;
                    ImageIcon imcon2 = null;
                    if(imagewidth>imageheight){
                        imcon2=new ImageIcon(newimage.getScaledInstance(jLabel10Poster.getWidth(), (int) (jLabel10Poster.getWidth()/imageratio), Image.SCALE_SMOOTH));
                    }else{
                        imcon2=new ImageIcon(newimage.getScaledInstance((int) (jLabel10Poster.getHeight()*imageratio), jLabel10Poster.getHeight(), Image.SCALE_SMOOTH));
                    }
                    jLabel10Poster.setIcon(imcon2);
                    jLabel10Poster.setIconTextGap(25);
                } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            }        
        }
    }//GEN-LAST:event_jLabel10PosterMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        moviedatabasejframe main=new moviedatabasejframe();
        main.setVisible(true);
        try {
           if(iamaconnection!=null){
                iamaconnection.close();
           }
        } catch (SQLException ex) {
            Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosed

    private void jButton2ImdbFetchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ImdbFetchActionPerformed
        Document doc = null;
         Currentimdbid=jTextFieldImdbId.getText();
        if(!Currentimdbid.isEmpty()){
//------------------------------------------------------------------------------
//                                 GET MOVIE STUFF
//------------------------------------------------------------------------------
                try {
                doc= Jsoup.connect("http://www.imdb.com/title/"+Currentimdbid).timeout(6000).get();
            } catch (IOException ex) {
                Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "FILM sayfasına Bağlantı hatası \n HATA:\n"+ex);
                return;
            }
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%        GET     MOVIE INFO
            String name=doc.select("p:contains(title) strong").text();
            String year=doc.select("a[href*=/year/]").text();
            String votes=doc.select("[itemprop=ratingCount]").text();
            votes=votes.replace(",","");
            String rating=doc.select("span[itemprop=ratingValue]").text();
            int GenreNumbers =doc.select("span[itemprop=genre]").size();
            String genres[]= new String[GenreNumbers];
                for (int i = 0; i < GenreNumbers; i++) {
                    genres[i]=doc.select("span[itemprop=genre]").get(i).text();
                }
            int countryCount=doc.select("a[href*='/search/title?country']").size();
            String Countrys="";
            for (int i = 0; i < countryCount; i++) {
                Countrys=Countrys+doc.select("a[href*='/search/title?country']").get(i).text()+",";
            }
            Countrys=Countrys.substring(0, Countrys.length()-1); 
            

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%        GET     MOVIE POSTER
            String PosterLink=doc.select("link[rel=image_src]").attr("href");
            String[] split = PosterLink.split(",");
            File selectedFile = new File(Currentimdbid+".jpg");
            if (!(new File("images").exists())) {
                new File("images").mkdir();
            }
            File outputFile = new File("images\\" + selectedFile.getName());
            if (!outputFile.exists()) {
                try {
                    outputFile.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(!selectedFile.getAbsoluteFile().equals(outputFile.getAbsoluteFile())){
                try {
                    URL Sourceurl;
                    try {
                        Sourceurl = new URL(split[0]+".jpg");
                        OutputStream outpStream = new FileOutputStream(outputFile);
                        try {
                            FileUtils.copyURLToFile(Sourceurl, outputFile);
                            outpStream.flush();
                            outpStream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%        SET     MOVIE POSTER
            if (outputFile.exists()) {
                PosterPath = outputFile.getPath();
                jLabel10Poster.setText("");
                String imagesrc=outputFile.getPath();
                BufferedImage newimage = null;
                try {
                    File fileitem= new File(imagesrc);
                    newimage=ImageIO.read(fileitem);
                    float imagewidth=newimage.getWidth();
                    float imageheight=newimage.getHeight();
                    float imageratio=imagewidth/imageheight;
                    ImageIcon imcon2 = null;
                    if(imagewidth>imageheight){
                        imcon2=new ImageIcon(newimage.getScaledInstance(jLabel10Poster.getWidth(), (int) (jLabel10Poster.getWidth()/imageratio), Image.SCALE_SMOOTH));
                    }else{
                        imcon2=new ImageIcon(newimage.getScaledInstance((int) (jLabel10Poster.getHeight()*imageratio), jLabel10Poster.getHeight(), Image.SCALE_SMOOTH));
                    }
                    jLabel10Poster.setIcon(imcon2);
                    jLabel10Poster.setIconTextGap(25);
                } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%        SET     MOVIE INFO
            jTextFieldOrName.setText(name);
            jTextFieldImdbVotes.setText(votes);
            jTextFieldYear.setText(year);
            jFormattedTextFieldIMDBRat.setText(rating);
            jTextField1Countrys.setText(Countrys);
            JCheckBox[] checkboxes={jCheckBox2,jCheckBox3,jCheckBox4,jCheckBox5,jCheckBox6,jCheckBox7,jCheckBox8,jCheckBox9,
                jCheckBox10,jCheckBox11,jCheckBox12,jCheckBox13,jCheckBox14,jCheckBox15,jCheckBox16,jCheckBox17,jCheckBox18,
                jCheckBox19,jCheckBox20,jCheckBox21,jCheckBox22,jCheckBox23,jCheckBox24,jCheckBox25,jCheckBox26,jCheckBox27};

            for (int i = 0; i < genres.length; i++) {
                for (int j = 0; j < checkboxes.length; j++) {
                    if(checkboxes[j].getText().equals(genres[i])){
                        checkboxes[j].setSelected(true);
                    }
                }

            }
//------------------------------------------------------------------------------
//                                      GET PERSONS
//------------------------------------------------------------------------------
            try {
                doc= Jsoup.connect("http://www.imdb.com/title/"+Currentimdbid+"/fullcredits").timeout(6000).get();
            } catch (IOException ex) {
                Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "CAST sayfasına Bağlantı hatası \n HATA:\n"+ex);
                return;
            }
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%        GET     MOVIE CAST
        int dirSize=doc.select("h4:contains(directed by)+table td.name").size();
        int writerSize=doc.select("h4:contains(Writing Credits)+table td.name").size();
        int musicianSize=doc.select("h4:contains(Music by)+table td.name").size();
        int camSize=doc.select("h4:contains(Cinematography)+table td.name").size();
        int ActorSize=doc.select("table.cast_list td[itemprop=actor]").size();
        String Directors="";
        String DirectorsIDs="";
        
        String Writers="";
        String WritersIDs="";
        
        String Musicians="";
        String MusiciansIDs="";
        
        String Cameraman="";
        String CameramanIDs="";
        
        String Actors="";
        String ActorsIDs="";
        
            for (int i = 0; i < dirSize; i++) {
               Directors=Directors+doc.select("h4:contains(directed by)+table td.name").get(i).text()+"\n";
               String DirectorsID=doc.select("h4:contains(directed by)+table td.name a").get(i).attr("href");
                DirectorsIDs = DirectorsIDs + DirectorsID.split("/")[2]+"\n";
            }
            for (int i = 0; i < writerSize; i++) {
                Writers=Writers+doc.select("h4:contains(Writing Credits)+table td.name").get(i).text()+"\n";
                String WritersID=doc.select("h4:contains(Writing Credits)+table td.name a").get(i).attr("href");
                WritersIDs=WritersIDs  + WritersID.split("/")[2]+"\n";
            }
            for (int i = 0; i < musicianSize; i++) {
                Musicians=Musicians+doc.select("h4:contains(Music by)+table td.name").get(i).text()+"\n";
                String MusiciansID=doc.select("h4:contains(Music by)+table td.name a").get(i).attr("href");
                MusiciansIDs=MusiciansIDs + MusiciansID.split("/")[2]+"\n";
            }
            for (int i = 0; i < camSize; i++) {
                Cameraman=Cameraman+doc.select("h4:contains(Cinematography)+table td.name").get(i).text()+"\n";
                String CameramanID=doc.select("h4:contains(Cinematography)+table td.name a").get(i).attr("href");
                CameramanIDs=CameramanIDs + CameramanID.split("/")[2]+"\n";
            }
            if(ActorSize>20){
                for (int i = 0; i < 20; i++) {
                    Actors=Actors+doc.select("table.cast_list td[itemprop=actor]").get(i).text()+"\n";
                    String ActorsID=doc.select("table.cast_list td[itemprop=actor] a").get(i).attr("href");
                    ActorsIDs = ActorsIDs + ActorsID.split("/")[2]+"\n";
                }
            }else{
                for (int i = 0; i < ActorSize; i++) {
                    Actors=Actors+doc.select("table.cast_list td[itemprop=actor]").get(i).text()+"\n";
                    String ActorsID=doc.select("table.cast_list td[itemprop=actor] a").get(i).attr("href");
                    ActorsIDs = ActorsIDs + ActorsID.split("/")[2]+"\n";
                }
            }
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%        SET     MOVIE CAST
            jTextArea4Directorname.setText(Directors);
            jTextArea5Directorid.setText(DirectorsIDs);
            jTextArea2writerName.setText(Writers);
            jTextArea3WriterID.setText(WritersIDs);
            jTextArea4MusicNAme.setText(Musicians);
            jTextArea5MusicID.setText(MusiciansIDs);
            jTextArea6CamName.setText(Cameraman);
            jTextArea7CamID.setText(CameramanIDs);
            jTextArea2Actornames.setText(Actors);
            jTextArea3Actorids.setText(ActorsIDs);
            
            fetchIsOk();
        }else{
            JOptionPane.showMessageDialog(this, "İmdbid'si boş gibin");
            return;
        }
        
                
    }//GEN-LAST:event_jButton2ImdbFetchActionPerformed

    private void jFormattedTextFieldPerRatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldPerRatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextFieldPerRatActionPerformed

    private void jTextFieldImdbIdMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldImdbIdMouseReleased
         if(SwingUtilities.isRightMouseButton(evt)){
                JPopupMenu popm=new JPopupMenu();
                JMenuItem item1=new JMenuItem("Paste");
                popm.add(item1);
                popm.show(evt.getComponent(),evt.getX(),evt.getY());
                item1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                            jTextFieldImdbId.setText(data);
                        } catch (UnsupportedFlavorException ex) {
                            Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });                
            }
    }//GEN-LAST:event_jTextFieldImdbIdMouseReleased

    private void jTextFieldOrNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldOrNameKeyTyped
        mouseClickedSwitch=false;
    }//GEN-LAST:event_jTextFieldOrNameKeyTyped

    private void jTextFieldOrNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextFieldOrNameMouseClicked
        if(!mouseClickedSwitch){
            return;
        }
        jTextFieldOrName.setText("");
    }//GEN-LAST:event_jTextFieldOrNameMouseClicked

    private void jComboBox1SearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1SearchActionPerformed
        String selectedresult=jComboBox1Search.getSelectedItem().toString();
        String Splitted[]=selectedresult.split("/");
        if(Splitted.length>2){
            jTextFieldImdbId.setText(Splitted[2]);
            StyledDocument TextPaneDoc = jTextPane1Queue.getStyledDocument();
            try {
                TextPaneDoc.insertString(TextPaneDoc.getLength(), Splitted[2]+"\n",null);
            } catch (BadLocationException ex) {
                Logger.getLogger(addMultiMovie.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_jComboBox1SearchActionPerformed

    private void jButton1StartQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1StartQueueActionPerformed
        if(! jTextPane1Queue.getText().isEmpty()){
            String[] list=jTextPane1Queue.getText().split("\r\n");
            for (int i = 0; i < list.length; i++) {
                queListArray.add(list[i]);
            }
        }
        StartQueueJob();
    }//GEN-LAST:event_jButton1StartQueueActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(addMultiMovie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addMultiMovie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addMultiMovie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addMultiMovie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new addMultiMovie().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1SAVE;
    private javax.swing.JButton jButton1StartQueue;
    private javax.swing.JButton jButton2ImdbFetch;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox1Autosave;
    private javax.swing.JCheckBox jCheckBox1Watched;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox27;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox<String> jComboBox1Search;
    private javax.swing.JFormattedTextField jFormattedTextFieldIMDBRat;
    private javax.swing.JFormattedTextField jFormattedTextFieldPerRat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel10Poster;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextArea jTextArea1Comment;
    private javax.swing.JTextArea jTextArea2Actornames;
    private javax.swing.JTextArea jTextArea2writerName;
    private javax.swing.JTextArea jTextArea3Actorids;
    private javax.swing.JTextArea jTextArea3WriterID;
    private javax.swing.JTextArea jTextArea4Directorname;
    private javax.swing.JTextArea jTextArea4MusicNAme;
    private javax.swing.JTextArea jTextArea5Directorid;
    private javax.swing.JTextArea jTextArea5MusicID;
    private javax.swing.JTextArea jTextArea6CamName;
    private javax.swing.JTextArea jTextArea7CamID;
    private javax.swing.JTextField jTextField1Countrys;
    private javax.swing.JTextField jTextFieldAlName;
    private javax.swing.JTextField jTextFieldImdbId;
    private javax.swing.JTextField jTextFieldImdbVotes;
    private javax.swing.JTextField jTextFieldOrName;
    private javax.swing.JTextField jTextFieldYear;
    private javax.swing.JTextPane jTextPane1Queue;
    // End of variables declaration//GEN-END:variables
}
