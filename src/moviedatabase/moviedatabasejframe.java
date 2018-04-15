/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviedatabase;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author Eddy
 */

public class moviedatabasejframe extends javax.swing.JFrame {
    static Connection   iamaconnection;
    static Statement    iamastate;
    String              CurrentPosterPath=null;
    String              CurrentMovieID=null;
    
    public moviedatabasejframe() {
        initComponents();
            if(!new File("moviedatabase.db").exists()){
                int selection = JOptionPane.showConfirmDialog(null, "database Bulunamadı oluşturmak ister misiniz?");
                if(selection==JOptionPane.OK_OPTION){
                    try {
                        new File("moviedatabase.db").createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    System.exit(0);
                }
            }
            
            if(new File("moviedatabase.db").length()==0){
                createNewTables();
            }
        CONNECT();
        jButtonRefresh.doClick(50);
    }
    
    public static  void createNewTables(){
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:moviedatabase.db");
            Statement stmt = conn.createStatement();
//looks one statement only create one table it ignores the others so i try to create them seperetaly... @eddy
        String CreateMovies="CREATE TABLE movies (\n" +
"    id             INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
"    imdbid         VARCHAR UNIQUE,\n" +
"    year           INTEGER,\n" +
"    originalname   VARCHAR NOT NULL,\n" +
"    alternatename  VARCHAR,\n" +
"    imdbrating     DOUBLE,\n" +
"    imdbvotes      INTEGER,\n" +
"    poster         STRING,\n" +
"    watch          STRING,\n" +
"    personalrating DOUBLE,\n" +
"    comment        TEXT,\n" +
"    genre          STRING,\n" +
"    datetimeUTC      TIME    DEFAULT (CURRENT_TIMESTAMP),\n" +
"    country        STRING\n" +
");";
        String Createpersons="CREATE TABLE persons (\n" +
"    id     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
"    name   VARCHAR NOT NULL,\n" +
"    imdbid VARCHAR\n" +
");";
        String Createmovies_has_persons="CREATE TABLE movies_has_persons (\n" +
"    id         INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
"    movies_id  INTEGER REFERENCES movies (id),\n" +
"    role       STRING,\n" +
"    persons_id INTEGER REFERENCES persons (id) \n" +
");";
        String Createmovies_has_files="CREATE TABLE movies_has_files (\n" +
"    movies_id   INTEGER REFERENCES movies (id),\n" +
"    files_name  STRING  PRIMARY KEY,\n" +
"    location    STRING,\n" +
"    rescategory STRING,\n" +
"    version     STRING\n" +
");";
        String Createtvshows="CREATE TABLE tvshows (\n" +
"    id             INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
"    name           VARCHAR NOT NULL,\n" +
"    imdbid         STRING,\n" +
"    yearstart      INTEGER,\n" +
"    yearend        INTEGER,\n" +
"    imdbrating     DOUBLE,\n" +
"    imdbvotes      INTEGER,\n" +
"    poster         STRING,\n" +
"    personalrating DOUBLE,\n" +
"    comment        TEXT,\n" +
"    genre          STRING,\n" +
"    datetimeUTC    TIME    DEFAULT (CURRENT_TIMESTAMP),\n" +
"    country        STRING\n" +
");";
        String Createtvshows_has_persons="CREATE TABLE tvshows_has_persons (\n" +
"    id           INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
"    tvshows_id   INTEGER REFERENCES tvshows (id),\n" +
"    tvshows_name VARCHAR REFERENCES tvshows (name),\n" +
"    role         VARCHAR,\n" +
"    persons_id   INTEGER REFERENCES persons (id) \n" +
");";
        String Createepisodes="CREATE TABLE episodes (\n" +
"    [no]          INTEGER PRIMARY KEY,\n" +
"    name          VARCHAR,\n" +
"    file_name     VARCHAR,\n" +
"    file_location VARCHAR,\n" +
"    season_no     INTEGER,\n" +
"    watch         STRING,\n" +
"    tvshows_id            REFERENCES tvshows (id),\n" +
"    tvshows_name  VARCHAR REFERENCES tvshows (name) \n" +
");";
            // create the tables
            
            stmt.execute(CreateMovies);
            stmt.execute(Createpersons);
            stmt.execute(Createmovies_has_persons);
            stmt.execute(Createmovies_has_files);
            stmt.execute(Createtvshows);
            stmt.execute(Createtvshows_has_persons);
            stmt.execute(Createepisodes);
            } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    
    public static void CONNECT(){
        try {
            iamaconnection = DriverManager.getConnection("jdbc:sqlite:moviedatabase.db");
            iamastate = iamaconnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "bağlantı veya database hatası \nhata: \n" + ex.getMessage());
        }
    }
//    public Statement getstate(){
//        return iamastate;
//    }
//    public void refreshTheTable(){
//        jButtonRefresh.doClick(50); 
//    }
//    public int getthetableselectedrow(){
//        return jTable1.getSelectedRow();
//    }
    public void WriteMovieInfo(){
    try {
//------------------------------information stuff
        CurrentMovieID=jTable1.getValueAt(jTable1.getSelectedRow(),0).toString();
        String query= "SELECT * FROM movies where id="+CurrentMovieID;
        ResultSet rs=iamastate.executeQuery(query);
        rs.next();
        jTextArea1.setText(rs.getString("comment"));
        jLabel16originalname.setText(rs.getString("originalname"));
        jLabel17altenatename.setText(rs.getString("alternatename"));
        jLabel18year.setText(rs.getString("year"));
        jLabel19imdbaddress.setText("http://www.imdb.com/title/"+rs.getString("imdbid"));
        jLabel20imdbrating.setText(rs.getString("imdbrating"));
        jLabel21imdbvotes.setText(NumberFormat.getNumberInstance(Locale.getDefault()).format(rs.getInt("imdbvotes")));
        
        jLabel23genres.setText(rs.getString("genre"));
        jLabel24watched.setText(rs.getString("watch"));
        jLabel28countrys.setText(rs.getString("country"));
//------------------------------cast stuff
        ResultSet PrsnAndMovies;
        ResultSet Persons;
        Statement StPerson=iamaconnection.createStatement();
        Statement StPersonAndMovies=iamaconnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        PrsnAndMovies=StPersonAndMovies.executeQuery("SELECT * FROM movies_has_persons WHERE movies_id="+CurrentMovieID);

        List<String> Directors=new ArrayList<>();
        

        List<String> Writers=new ArrayList<>();
        
        List<String> Musicians=new ArrayList<>();
        
        List<String> CameraMans=new ArrayList<>();
        
        List<String> Actors=new ArrayList<>();
        
        while(PrsnAndMovies.next()){
            String role=PrsnAndMovies.getString("role");
            String personID=PrsnAndMovies.getString("persons_id");
            if(role.equals("Director")){
                Directors.add(personID);
            }
            if(role.equals("Writer")){
                Writers.add(personID);
            }
            if(role.equals("Musician")){
                Musicians.add(personID);
            }
            if(role.equals("Camera")){
                CameraMans.add(personID);
            }
            if(role.equals("Actor")){
                Actors.add(personID);
            }
        }
        String GeneratedHtml="<font color=rgb'(0,102,255)' size='2' face='tahoma'>";
        for (int i = 0; i < Directors.size(); i++) {
            Persons=StPerson.executeQuery("SELECT * FROM persons WHERE id="+Directors.get(i));
            Persons.next();
            String PersonName=Persons.getString("name");
            String PersonIMDBid=Persons.getString("imdbid");
            GeneratedHtml+="<a href='http://www.imdb.com/name/"+PersonIMDBid+"'>"+PersonName+"</a>&nbsp;&nbsp;";
        }
        jTextPane1Director.setText(GeneratedHtml);
        GeneratedHtml="<font color=rgb'(0,102,255)' size='2' face='tahoma'>";
        for (int i = 0; i < Writers.size(); i++) {
            Persons=StPerson.executeQuery("SELECT * FROM persons WHERE id="+Writers.get(i));
            Persons.next();
            String PersonName=Persons.getString("name");
            String PersonIMDBid=Persons.getString("imdbid");
            GeneratedHtml+="<a href='http://www.imdb.com/name/"+PersonIMDBid+"'>"+PersonName+"</a>&nbsp;&nbsp;";
        }
        jTextPane2Writer.setText(GeneratedHtml);
        GeneratedHtml="<font color=rgb'(0,102,255)' size='2' face='tahoma'>";
        for (int i = 0; i < Musicians.size(); i++) {
            Persons=StPerson.executeQuery("SELECT * FROM persons WHERE id="+Musicians.get(i));
            Persons.next();
            String PersonName=Persons.getString("name");
            String PersonIMDBid=Persons.getString("imdbid");
            GeneratedHtml+="<a href='http://www.imdb.com/name/"+PersonIMDBid+"'>"+PersonName+"</a>&nbsp;&nbsp;";
        }
        jTextPane4Music.setText(GeneratedHtml);
        GeneratedHtml="<font color=rgb'(0,102,255)' size='2' face='tahoma'>";
        for (int i = 0; i < CameraMans.size(); i++) {
            Persons=StPerson.executeQuery("SELECT * FROM persons WHERE id="+CameraMans.get(i));
            Persons.next();
            String PersonName=Persons.getString("name");
            String PersonIMDBid=Persons.getString("imdbid");
            GeneratedHtml+="<a href='http://www.imdb.com/name/"+PersonIMDBid+"'>"+PersonName+"</a>&nbsp;&nbsp;";
        }
        jTextPane3Camera.setText(GeneratedHtml);
        GeneratedHtml="<font color=rgb'(0,102,255)' size='2' face='tahoma'>";
        for (int i = 0; i < Actors.size(); i++) {
            Persons=StPerson.executeQuery("SELECT * FROM persons WHERE id="+Actors.get(i));
            Persons.next();
           String PersonName=Persons.getString("name");
            String PersonIMDBid=Persons.getString("imdbid");
            GeneratedHtml+="<a href='http://www.imdb.com/name/"+PersonIMDBid+"'>"+PersonName+"</a>&nbsp;&nbsp;";
        }
        jTextPane1Actors.setText(GeneratedHtml);

//------------------------------rating bar stuff
       if(rs.getString("personalrating")!=null){
            float size=Float.parseFloat(rs.getString("personalrating"))*10;
            if(size<=30){
                jProgressBar1.setForeground(new Color(255,0,0));
            }
            if(30<size && size<=55){
                jProgressBar1.setForeground(new Color(255,75,0));
            }
            if(55<size && size<=75){
                jProgressBar1.setForeground(new Color(255,140,0));
            }
            if(size>75){
                jProgressBar1.setForeground(new Color(0,160,0));
            }
            jProgressBar1.setValue((int)size);
        }else{
            jProgressBar1.setValue(0);
            jProgressBar1.setForeground(new Color(255,155,0));
        }
//------------------------------poster stuff
        if(rs.getObject("poster")!=null){
            jLabel2.setText("");
            String imagesrc=rs.getString("poster");
            BufferedImage newimage = null;
            try {
                File fileitem= new File(imagesrc);
                if(fileitem.exists() && fileitem.length()>0){
                newimage=ImageIO.read(fileitem);
                float imagewidth=newimage.getWidth();
                float imageheight=newimage.getHeight();
                float imageratio=imagewidth/imageheight;
                ImageIcon imcon2 = null;
                 if(imagewidth>imageheight){
                   imcon2=new ImageIcon(newimage.getScaledInstance(jLabel2.getWidth(), (int) (jLabel2.getWidth()/imageratio), Image.SCALE_SMOOTH));  
                 }else{
                     imcon2=new ImageIcon(newimage.getScaledInstance((int) (jLabel2.getHeight()*imageratio), jLabel2.getHeight(), Image.SCALE_SMOOTH));  
                 }
                jLabel2.setIcon(imcon2);
                jLabel2.setIconTextGap(25);
                CurrentPosterPath=imagesrc;
                newimage.flush();
                }else{
                    jLabel2.setText("baba poster yok!\nposteri çalmışlar!");
                    jLabel2.setIcon(null);
                }
            } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);

                }
            }else{
                jLabel2.setText("poster atanmamış");
                jLabel2.setIcon(null);
            }
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Creates new form moviedatabasejframe
     */


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane9 = new javax.swing.JScrollPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButtonADD = new javax.swing.JButton();
        jButtonremove = new javax.swing.JButton();
        jButtonupdate = new javax.swing.JButton();
        jButtonRefresh = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel16originalname = new javax.swing.JLabel();
        jLabel17altenatename = new javax.swing.JLabel();
        jLabel18year = new javax.swing.JLabel();
        jLabel19imdbaddress = new javax.swing.JLabel();
        jLabel20imdbrating = new javax.swing.JLabel();
        jLabel21imdbvotes = new javax.swing.JLabel();
        jLabel23genres = new javax.swing.JLabel();
        jLabel24watched = new javax.swing.JLabel();
        jLabel28countrys = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1Director = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane2Writer = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane3Camera = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextPane4Music = new javax.swing.JTextPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextPane1Actors = new javax.swing.JTextPane();
        jLabel18imdb = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel18DataInfo = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jTextPane1Results = new javax.swing.JTextPane();
        jTextField1search = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jSpinner1yearbig = new javax.swing.JSpinner();
        jSpinner2yearsmall = new javax.swing.JSpinner();
        jSpinner3ratingbig = new javax.swing.JSpinner();
        jLabel22 = new javax.swing.JLabel();
        jSpinner4ratingsmall = new javax.swing.JSpinner();
        jComboBox1genres = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        jSpinner5votes = new javax.swing.JSpinner();
        jLabel25 = new javax.swing.JLabel();
        jTextField1CountrysFind = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jComboBox2sortby = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jButton2GO = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel29 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jTable1.setAutoCreateRowSorter(true);//kişisel kod
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Num", "Original Name", "Alternative Name", "Year", "IMDB id", "Rating", "Votes", "Personal Rating", "Genres", "Watched"
            }
        ) {
            Class[] types = { Integer.class, Integer.class,String.class, String.class,Integer.class,String.class,Integer.class,Integer.class,Integer.class,String.class,String.class };/*kişisel kod*/
            @Override
            public Class getColumnClass(int columnIndex) {
                //return this.types[columnIndex];

                Class returnValue=Object.class;
                if(jTable1.getRowCount()>0){
                    if ((columnIndex >0) && (columnIndex < getColumnCount()) && getValueAt(0, columnIndex)!=null) {
                        returnValue = getValueAt(0, columnIndex).getClass();
                        //    System.out.println("if içi");
                    }// else {
                        //   returnValue = Object.class;
                        //   System.out.println("else içi");
                        // }
                }
                return returnValue;
            }
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        //----------kişisel kod ile netbeans kodunu ayırma bölgesi
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(15);
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1){
                    WriteMovieInfo();
                }

            }
        });

        jButtonADD.setIcon(new javax.swing.ImageIcon("E:\\Books & Codes & Notes\\WorkSpace\\Netbeans\\moviedatabase_SQLite\\icons\\addicon.png")); // NOI18N
        jButtonADD.setText("ADD");
        jButtonADD.setPreferredSize(new java.awt.Dimension(115, 30));
        jButtonADD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonADDActionPerformed(evt);
            }
        });

        jButtonremove.setIcon(new javax.swing.ImageIcon("E:\\Books & Codes & Notes\\WorkSpace\\Netbeans\\moviedatabase_SQLite\\icons\\deleteicon.png")); // NOI18N
        jButtonremove.setText("REMOVE");
        jButtonremove.setPreferredSize(new java.awt.Dimension(115, 30));
        jButtonremove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonremoveActionPerformed(evt);
            }
        });

        jButtonupdate.setIcon(new javax.swing.ImageIcon("E:\\Books & Codes & Notes\\WorkSpace\\Netbeans\\moviedatabase_SQLite\\icons\\editicon.png")); // NOI18N
        jButtonupdate.setText("EDIT");
        jButtonupdate.setPreferredSize(new java.awt.Dimension(115, 30));
        jButtonupdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonupdateActionPerformed(evt);
            }
        });

        jButtonRefresh.setVisible(false);
        jButtonRefresh.setText("REFRESH");
        jButtonRefresh.setPreferredSize(new java.awt.Dimension(105, 30));
        jButtonRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Poster...");
        jLabel2.setOpaque(true);
        jLabel2.setPreferredSize(new java.awt.Dimension(200, 200));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Original Name:");
        jLabel3.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Alternate Name:");
        jLabel4.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Year:");
        jLabel5.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("IMDB Address:");
        jLabel6.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("IMDB Rating:");
        jLabel7.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("IMDB Votes:");
        jLabel8.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Personal Rating:");
        jLabel9.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Genre:");
        jLabel10.setPreferredSize(new java.awt.Dimension(80, 20));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Watched:");
        jLabel11.setPreferredSize(new java.awt.Dimension(80, 20));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Country(s):");
        jLabel15.setPreferredSize(new java.awt.Dimension(85, 20));

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Director:");
        jLabel12.setPreferredSize(new java.awt.Dimension(80, 20));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Writer:");
        jLabel13.setPreferredSize(new java.awt.Dimension(80, 20));

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Music:");
        jLabel14.setPreferredSize(new java.awt.Dimension(80, 20));

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Camera:");
        jLabel16.setPreferredSize(new java.awt.Dimension(50, 14));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(5, 5, 5))
                                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        jLabel16originalname.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel16originalname.setText("Original Name:");
        jLabel16originalname.setPreferredSize(new java.awt.Dimension(200, 20));

        jLabel17altenatename.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17altenatename.setText("Alternate Name:");
        jLabel17altenatename.setPreferredSize(new java.awt.Dimension(200, 20));

        jLabel18year.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18year.setText("Year:");
        jLabel18year.setPreferredSize(new java.awt.Dimension(200, 20));

        jLabel19imdbaddress.setForeground(new java.awt.Color(0, 102, 255));
        jLabel19imdbaddress.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19imdbaddress.setText("http://www.imdb.com/");
        jLabel19imdbaddress.setPreferredSize(new java.awt.Dimension(200, 20));
        jLabel19imdbaddress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel19imdbaddressMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel19imdbaddressMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel19imdbaddressMouseReleased(evt);
            }
        });

        jLabel20imdbrating.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel20imdbrating.setText("IMDB Rating:");
        jLabel20imdbrating.setPreferredSize(new java.awt.Dimension(200, 20));

        jLabel21imdbvotes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21imdbvotes.setText("IMDB Votes:");
        jLabel21imdbvotes.setPreferredSize(new java.awt.Dimension(200, 20));

        jLabel23genres.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel23genres.setText("Genre:");
        jLabel23genres.setPreferredSize(new java.awt.Dimension(80, 20));

        jLabel24watched.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24watched.setText("Watched:");
        jLabel24watched.setPreferredSize(new java.awt.Dimension(80, 20));

        jLabel28countrys.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28countrys.setText("Country(s):");
        jLabel28countrys.setPreferredSize(new java.awt.Dimension(200, 20));

        jProgressBar1.setForeground(new java.awt.Color(255, 180, 0));
        jProgressBar1.setStringPainted(true);

        jTextPane1Director.setEditable(false);
        jTextPane1Director.setBorder(null);
        jTextPane1Director.setContentType("text/html");
        jTextPane1Director.setMinimumSize(new java.awt.Dimension(80, 14));
        jTextPane1Director.setPreferredSize(new java.awt.Dimension(352, 16));
        jTextPane1Director.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jTextPane1DirectorHyperlinkUpdate(evt);
            }
        });
        jScrollPane3.setViewportView(jTextPane1Director);

        jTextPane2Writer.setEditable(false);
        jTextPane2Writer.setBorder(null);
        jTextPane2Writer.setContentType("text/html");
        jTextPane2Writer.setMinimumSize(new java.awt.Dimension(80, 14));
        jTextPane2Writer.setPreferredSize(new java.awt.Dimension(352, 16));
        jTextPane2Writer.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jTextPane2WriterHyperlinkUpdate(evt);
            }
        });
        jScrollPane5.setViewportView(jTextPane2Writer);

        jTextPane3Camera.setEditable(false);
        jTextPane3Camera.setBorder(null);
        jTextPane3Camera.setContentType("text/html");
        jTextPane3Camera.setMinimumSize(new java.awt.Dimension(80, 14));
        jTextPane3Camera.setPreferredSize(new java.awt.Dimension(352, 16));
        jTextPane3Camera.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jTextPane3CameraHyperlinkUpdate(evt);
            }
        });
        jScrollPane6.setViewportView(jTextPane3Camera);

        jTextPane4Music.setEditable(false);
        jTextPane4Music.setBorder(null);
        jTextPane4Music.setContentType("text/html");
        jTextPane4Music.setMinimumSize(new java.awt.Dimension(80, 14));
        jTextPane4Music.setPreferredSize(new java.awt.Dimension(352, 16));
        jTextPane4Music.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jTextPane4MusicHyperlinkUpdate(evt);
            }
        });
        jScrollPane7.setViewportView(jTextPane4Music);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16originalname, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel28countrys, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21imdbvotes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20imdbrating, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19imdbaddress, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18year, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17altenatename, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24watched, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel23genres, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel16originalname, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel17altenatename, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel18year, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel19imdbaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel20imdbrating, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel21imdbvotes, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23genres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel24watched, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28countrys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel17.setText("Cast:");
        jLabel17.setPreferredSize(new java.awt.Dimension(50, 14));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Comment:");
        jLabel1.setPreferredSize(new java.awt.Dimension(50, 14));

        jTextPane1Actors.setEditable(false);
        jTextPane1Actors.setBorder(null);
        jTextPane1Actors.setContentType("text/html");
        jTextPane1Actors.setPreferredSize(new java.awt.Dimension(216, 279));
        jTextPane1Actors.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                jTextPane1ActorsHyperlinkUpdate(evt);
            }
        });
        jScrollPane8.setViewportView(jTextPane1Actors);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jLabel18imdb.setText("jLabel18");
        jLabel18imdb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18imdbMouseClicked(evt);
            }
        });

        jLabel19.setText("jLabel19");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel19MouseReleased(evt);
            }
        });

        jLabel20.setText("Watch trailer");

        jButton2.setIcon(new javax.swing.ImageIcon("E:\\Books & Codes & Notes\\WorkSpace\\Netbeans\\moviedatabase_SQLite\\icons\\addmultiicon.png")); // NOI18N
        jButton2.setText("MULTI-ADD");
        jButton2.setPreferredSize(new java.awt.Dimension(115, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButtonADD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addComponent(jButtonremove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel20)
                                    .addGap(14, 14, 14)
                                    .addComponent(jLabel18imdb)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel19))
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(jButtonupdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonRefresh, jButtonremove});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonADD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonremove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonupdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel18imdb)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        File iconFile= new File("icons/if_imdb_172025.png");
        try {
            BufferedImage bfi=ImageIO.read(iconFile);
            ImageIcon imcon=new ImageIcon( bfi.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            jLabel18imdb.setText("");
            jLabel18imdb.setIcon(imcon);
        } catch (IOException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        iconFile= new File("icons/if_youtube_395445.png");
        try {
            BufferedImage bfi=ImageIO.read(iconFile);
            ImageIcon imcon=new ImageIcon( bfi.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            jLabel19.setText("");
            jLabel19.setIcon(imcon);
        } catch (IOException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }

        jTabbedPane1.addTab("Movies", jPanel1);

        jLabel18DataInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18DataInfo.setText("Databse Currently Contains,");

        jLabel18.setText("SQL Search:");

        jButton1.setText("Export to HTML");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel21.setForeground(new java.awt.Color(0, 102, 255));
        jLabel21.setText("(view the database structure)");
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel21MouseEntered(evt);
            }
        });

        jTextPane1Results.setContentType("text/html");
        jTextPane1Results.setPreferredSize(new java.awt.Dimension(1000, 200));
        jScrollPane12.setViewportView(jTextPane1Results);

        jTextField1search.setText("SELECT * FROM movies");
        jTextField1search.setPreferredSize(new java.awt.Dimension(1000, 20));
        jTextField1search.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1searchKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1search, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 1082, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSpinner1yearbig.setModel(new javax.swing.SpinnerNumberModel(2999, 0, 9999, 1));
        jSpinner1yearbig.setPreferredSize(new java.awt.Dimension(60, 20));

        jSpinner2yearsmall.setModel(new javax.swing.SpinnerNumberModel(1999, 0, 9999, 1));
        jSpinner2yearsmall.setPreferredSize(new java.awt.Dimension(60, 20));

        jSpinner3ratingbig.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(10.0f), Float.valueOf(0.0f), Float.valueOf(10.0f), Float.valueOf(0.1f)));
        jSpinner3ratingbig.setPreferredSize(new java.awt.Dimension(60, 20));

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("<YEAR<");

        jSpinner4ratingsmall.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(5.5f), Float.valueOf(0.0f), Float.valueOf(10.0f), Float.valueOf(0.1f)));
        jSpinner4ratingsmall.setPreferredSize(new java.awt.Dimension(60, 20));

        jComboBox1genres.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "none", "Action", "Adventure", "Animation", "Biography", "Comedy", "Crime", "Documentary", "Drama", "Family", "Fantasy", "Film-Noir", "Game-Show", "History", "Horror", "Music", "Musical", "Mystery", "News", "Reality-TV", "Romance", "Sci-Fi", "Sport", "Talk-Show", "Thriller", "War", "Western" }));

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("<IMDb Rating<");

        jSpinner5votes.setModel(new javax.swing.SpinnerNumberModel(5000, 0, null, 1));

        jLabel25.setText("Votes >");

        jLabel26.setText("Country");

        jLabel23.setText("Genre:");

        jComboBox2sortby.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alphabetically A-Z", "Alphabetically Z-A", "Year A-Z", "Year Z-A", "Votes A-Z", "Votes Z-A", "Ratings A-Z", "Ratings Z-A" }));
        jComboBox2sortby.setPreferredSize(new java.awt.Dimension(145, 20));

        jLabel27.setText("Sort By:");

        jButton2GO.setText("GO!");
        jButton2GO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2GOActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(jList1);

        jLabel29.setText("that Matching Criteria:");

        jLabel28.setText("Find Movies of:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(29, 29, 29)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jSpinner2yearsmall, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSpinner4ratingsmall, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jSpinner1yearbig, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSpinner3ratingbig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox1genres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jSpinner5votes, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel27)
                                .addComponent(jLabel26))
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField1CountrysFind, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel9Layout.createSequentialGroup()
                                    .addGap(26, 26, 26)
                                    .addComponent(jComboBox2sortby, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(jButton2GO, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel28))
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addContainerGap())
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner2yearsmall, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)
                            .addComponent(jSpinner1yearbig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jComboBox1genres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner4ratingsmall, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)
                            .addComponent(jSpinner3ratingbig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jSpinner5votes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1CountrysFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2sortby, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addGap(18, 18, 18)
                        .addComponent(jButton2GO)
                        .addGap(20, 105, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18DataInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel21)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(262, 262, 262))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18DataInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("What's the news?", jPanel2);

        jScrollPane10.setViewportView(jTree1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(849, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 668, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab3", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1122, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab4", jPanel4);

        jScrollPane9.setViewportView(jTabbedPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel19imdbaddressMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19imdbaddressMouseReleased
        try {
            try {
                Desktop.getDesktop().browse(new URI(jLabel19imdbaddress.getText()));
            } catch (URISyntaxException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel19imdbaddressMouseReleased

    private void jLabel19imdbaddressMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19imdbaddressMouseExited
//        Font font = jLabel19imdbaddress.getFont();
//        Map attributes = font.getAttributes();
//        attributes.put(TextAttribute.UNDERLINE, -1);
//        jLabel19imdbaddress.setFont(font.deriveFont(attributes));
    }//GEN-LAST:event_jLabel19imdbaddressMouseExited

    private void jLabel19imdbaddressMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19imdbaddressMouseEntered
//        Font font = jLabel19imdbaddress.getFont();
//        Map attributes = font.getAttributes();
//        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
//        jLabel19imdbaddress.setFont(font.deriveFont(attributes));
        jLabel19imdbaddress.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabel19imdbaddressMouseEntered

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        if(CurrentPosterPath!=null){
            try {
                File poster=new File(CurrentPosterPath);
                Desktop.getDesktop().open(new File(System.getProperty("user.dir")+"\\"+poster.getParent()));
                } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jButtonRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshActionPerformed
        try {
            iamaconnection.close();
            CONNECT();
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel dm = new DefaultTableModel();// bu iki satırı şöyle yazmak da mümkün: DefaultTableModel dm = (DefaultTableModel) getModel();
        dm= (DefaultTableModel) jTable1.getModel();//ben anlamak için böyle bir deneeme yapptım.
        //tablodaki her değer her seferinde yeniden yazılıyordu o yüzden önce içeriği silmeye karar verdim.
        for (int i = dm.getRowCount() - 1; i >= 0; i--) {
            dm.removeRow(i);
        }

        //            for (int i = 0; i < dm.getRowCount(); i++) {
            //               dm.removeRow(i);//saçma sapan bir şey yapmışlar...satır sayısı 1'den başlıyor ama silmek için verilecek index 0'dan başlıyor. kafayı yedim uğraşırken :)
        //            }//yeni gelen ekleme: tabloyu baştan silmeye kalkışınca hata veriyor ama sondan başlayınca sıkıntı yok...vay amk ya :/ yok artık!
        // buralarda denemeelr yaptığım için ortalığı böyle bırakıyorum temizlmiyorum...bilgi olarak kalsın burda...lazım olur
        try {

            ResultSet rs=  iamastate.executeQuery("SELECT * FROM movies");
            int rownum=1;
//            if(!rs.next()){
//                JOptionPane.showMessageDialog(null, "veri tabanı boş gibi sankim...");
//                return;
//            }else{
//                int id=rs.getInt("id");
//                String imdbID=rs.getString("imdbid");
//                int MovieYear=rs.getInt("year");
//                String OrName=rs.getString("originalname");
//                String AltName=rs.getString("alternatename");
//                float imdbrating=rs.getFloat("imdbrating");
//                int imdbvote=rs.getInt("imdbvotes");
//                String watched=rs.getString("watch");
//                float PersRat=rs.getFloat("personalrating");
//                String genre=rs.getString("genre");
//                Object [] row={id,rownum,OrName,AltName,MovieYear,imdbID,imdbrating,imdbvote,PersRat,genre,watched};
//                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
//                model.addRow(row);
//                rownum++;
//            }
            while(rs.next()){
                int id=rs.getInt("id");
                String imdbID=rs.getString("imdbid");
                int MovieYear=rs.getInt("year");
                String OrName=rs.getString("originalname");
                String AltName=rs.getString("alternatename");
                float imdbrating=rs.getFloat("imdbrating");
                int imdbvote=rs.getInt("imdbvotes");
                String watched=rs.getString("watch");
                float PersRat=rs.getFloat("personalrating");
                String genre=rs.getString("genre");
                Object [] row={id,rownum,OrName,AltName,MovieYear,imdbID,imdbrating,imdbvote,PersRat,genre,watched};
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.addRow(row);
                rownum++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "veri tabanı boş gibi sankim...");
            return;
            
        }
            if(jTable1.getRowCount()>0){
                jTable1.setRowSelectionInterval(0, 0);
            }
    }//GEN-LAST:event_jButtonRefreshActionPerformed

    private void jButtonupdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonupdateActionPerformed
        if(jTable1.getSelectedRow()>-1){
            updatemovieframe update= new updatemovieframe(CurrentMovieID,CurrentPosterPath);
            update.setVisible(true);
            this.setVisible(false);
            try {
            iamaconnection.close();
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        }else{
            JOptionPane.showMessageDialog(null,"hangisini değiştireceksin kardeşim!");
        }

    }//GEN-LAST:event_jButtonupdateActionPerformed

    private void jButtonremoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonremoveActionPerformed
        ResultSet rs;
        if(jTable1.getSelectedRow()>-1){
            int results=JOptionPane.showConfirmDialog(null,"","bu öğeyi silmek istediğinize emin misiniz?",JOptionPane.WARNING_MESSAGE);
            if(results==JOptionPane.YES_OPTION){
                try {
                    //delete the connection of persons to this movie
                    PreparedStatement ppst=iamaconnection.prepareStatement("Delete From movies_has_persons WHERE movies_id="+CurrentMovieID);
                    ppst.executeUpdate();
//                    iamastate.execute("Delete From movies_has_persons WHERE movies_id="+CurrentMovieID);
                    //delete the poster
                    rs=iamastate.executeQuery("SELECT * FROM movies WHERE id="+CurrentMovieID);
                    rs.next();
                    if(rs.getObject("poster")!=null){//poster silme işlemi normalde Currentposterpath değişkenine bakılarak yapılıyordu,yan etkileri olduğunu farkettim. değiştirdim. 
                        String path=rs.getString("poster");
                        if(new File(path).exists()){
                        new File(path).delete();
                        }
                    }
                    //Delete the Movie
//                    rs=iamastate.executeQuery("SELECT * FROM movies WHERE id="+CurrentMovieID);
//                    rs.next();//yukarıda çalıştığı için gerek kalmadı buraya...
                    ppst=iamaconnection.prepareStatement("DELETE  FROM movies WHERE id="+CurrentMovieID);
                    ppst.executeUpdate();
//                    iamastate.execute("DELETE  FROM movies WHERE id="+CurrentMovieID);
                    jButtonRefresh.doClick(100);
                    if(jTable1.getRowCount()>0){
                        jTable1.setRowSelectionInterval(0, 0);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"neyi silsem bilemedim şimdi '~'");
        }

    }//GEN-LAST:event_jButtonremoveActionPerformed

    private void jButtonADDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonADDActionPerformed
        // ADD MOVİE BUTTON
        new addmovieframe().setVisible(true);
        setVisible(false);
        try {
            iamaconnection.close();
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButtonADDActionPerformed

    private void jTextPane1ActorsHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_jTextPane1ActorsHyperlinkUpdate
        if(evt.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
            try {
                URI link=evt.getURL().toURI();
                try {
                    Desktop.getDesktop().browse(link);
                } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTextPane1ActorsHyperlinkUpdate

    private void jTextPane1DirectorHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_jTextPane1DirectorHyperlinkUpdate
        if(evt.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
            try {
                URI link=evt.getURL().toURI();
                try {
                    Desktop.getDesktop().browse(link);
                } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTextPane1DirectorHyperlinkUpdate

    private void jTextPane2WriterHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_jTextPane2WriterHyperlinkUpdate
        if(evt.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
            try {
                URI link=evt.getURL().toURI();
                try {
                    Desktop.getDesktop().browse(link);
                } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTextPane2WriterHyperlinkUpdate

    private void jTextPane4MusicHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_jTextPane4MusicHyperlinkUpdate
        if(evt.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
            try {
                URI link=evt.getURL().toURI();
                try {
                    Desktop.getDesktop().browse(link);
                } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTextPane4MusicHyperlinkUpdate

    private void jTextPane3CameraHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_jTextPane3CameraHyperlinkUpdate
        if(evt.getEventType()==HyperlinkEvent.EventType.ACTIVATED){
            try {
                URI link=evt.getURL().toURI();
                try {
                    Desktop.getDesktop().browse(link);
                } catch (IOException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTextPane3CameraHyperlinkUpdate

    private void jLabel18imdbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18imdbMouseClicked
        if(CurrentMovieID!=null && !jLabel19imdbaddress.getText().equals("http://www.imdb.com/title/null")){
            try {
                try {
                    Desktop.getDesktop().browse(new URI(jLabel19imdbaddress.getText()));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jLabel18imdbMouseClicked

    private void jLabel19MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseReleased
        if(CurrentMovieID!=null && !jLabel16originalname.getText().isEmpty()){
            try {
                try {
                    String SearchQuery=jLabel16originalname.getText().replaceAll(" ","%20");
                    Desktop.getDesktop().browse(new URI("https://www.youtube.com/results?search_query="+SearchQuery+"%20trailer"));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jLabel19MouseReleased

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if(jTabbedPane1.getSelectedIndex()==1){
            try {
                ResultSet rs=iamastate.executeQuery("Select * From Persons order by name");
                String Results="";
                while(rs.next()){
                    Results+=rs.getString("name")+"#"+rs.getString("id")+",";
                }
                String PerList[]=Results.split(",");
                DefaultListModel dfl=new DefaultListModel();
                dfl.add(0, "NONE");
                for (int i = 0; i < PerList.length; i++) {
                    dfl.add(i+1, PerList[i]);
                }
                jList1.setModel(dfl);
                int moviecount=0;
                int personcount=0;
                int tvshowcount=0;
                rs=iamastate.executeQuery("SELECT COUNT(*) FROM movies");
                if(rs.next()){
                    moviecount=rs.getInt(1);
                }
                rs=iamastate.executeQuery("SELECT COUNT(*) FROM persons");
                if(rs.next()){
                    personcount=rs.getInt(1);
                }
                rs=iamastate.executeQuery("SELECT COUNT(*) FROM tvshows");
                if(rs.next()){
                    tvshowcount=rs.getInt(1);
                }
                jLabel18DataInfo.setText("<html><body>Databse Currently Contains;<b>"+moviecount+"</b>  Movie, <b>"+tvshowcount+"</b>  TV Show and <b>"+personcount+"</b>  Person</body></html>");
                
            } catch (SQLException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(jTabbedPane1.getSelectedIndex()==2){
            try {
                ResultSet rs=iamastate.executeQuery("Select * From movies order by originalname");
                DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");
                DefaultTreeModel TModel = new DefaultTreeModel(rootNode);
                TModel.setRoot(rootNode);
                while(rs.next()){
                    DefaultMutableTreeNode childnodes = new DefaultMutableTreeNode(rs.getString("originalname"));
                    TModel.insertNodeInto(childnodes, rootNode, rootNode.getChildCount());
                    TModel.get
                }
                jTree1.setModel(TModel);
                
                
                
                
                
            } catch (SQLException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged
//            if(jList1.getSelectedIndex()!=-1){
//                String Selected=jList1.getSelectedValue();
//                String SelectedID=Selected.split("<>")[1];
//                try {
//                    ResultSet rs=iamastate.executeQuery("SELECT * FROM movies_has_persons WHERE persons_id="+SelectedID);
//                    List<String> MovieandRolsList= new ArrayList<>();
//                    while(rs.next()){
//                        MovieandRolsList.add(rs.getString("movies_id")+"-"+rs.getString("role"));
//                    }
//                    DefaultListModel dfl= new DefaultListModel();
//                    for (int i = 0; i < MovieandRolsList.size(); i++) {
//                        String MovieId=MovieandRolsList.get(i).split("-")[0];
//                        rs=iamastate.executeQuery("Select * From movies where id="+MovieId);
//                        rs.next();
//                        String Result=MovieandRolsList.get(i).split("-")[1]+"-->"+rs.getString("originalname");
//                        dfl.add(i, Result);
//                        
//                    }
//                    jList2.setModel(dfl);
//                } catch (SQLException ex) {
//                    Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
    }//GEN-LAST:event_jList1ValueChanged

    private void jTextField1searchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1searchKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            String SqlQuery=jTextField1search.getText();
            String rgb = Integer.toHexString(SystemColor.window.getRGB());
            String backgroundColor = rgb.substring(2, rgb.length());
            try {
                ResultSet rs=iamastate.executeQuery(SqlQuery);
                int columnCount=rs.getMetaData().getColumnCount();
                int count=1;
                String ResultsGonnaWrite="<html>\n"
            + "<head>\n"
            + "<style type=\"text/css\">\n"
            + "table {\n" + "width: 100%;\n" +"font-family:arial;font-size:12px"+ "}\n"
            + "td, th {\n" + "background-color: #" + backgroundColor + "\n"
            + "}\n"
            + "</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "SQL RESULTS:\n"
            + "<div style=\"background-color: black\">\n"
            + "<table border=\"0\" cellpadding=\"2\" cellspacing=\"1\">\n"+"<tr><th>#</th>";
                for (int i = 0; i < columnCount; i++) {
                    ResultsGonnaWrite+="<th>"+rs.getMetaData().getColumnName(i+1)+"</th>";
                }
                ResultsGonnaWrite+="</tr>";
                while(rs.next()){
                    ResultsGonnaWrite+="<td>"+count+"</td>";
                    for (int i = 0; i < columnCount; i++) {
                        ResultsGonnaWrite+="<td>"+rs.getString(i+1)+"</td>";
                    }
                    ResultsGonnaWrite+="</tr>";
                    count++;
                }
                ResultsGonnaWrite+="</table></body></html>";
                jTextPane1Results.setText(ResultsGonnaWrite);
            } catch (SQLException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Laf aramızda ama yanlış bir şeyler mi yazdın acaba?\n\n"+ex);
            }
        }
    }//GEN-LAST:event_jTextField1searchKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser fchoos=new JFileChooser();
        int results=fchoos.showSaveDialog(this);
        if(results==JFileChooser.APPROVE_OPTION){
            try {
                String path=fchoos.getSelectedFile().getAbsolutePath();
                File TheFile=null;
                if(fchoos.getSelectedFile().getName().endsWith(".html")){
                    TheFile=new File(path);
                }else{
                    TheFile=new File(path+".html");
                    
                }
                if(TheFile.exists()){
                    int selection=JOptionPane.showConfirmDialog(null, "file is exist, do you want to overwrite?");
                    if(selection==JOptionPane.OK_OPTION){
                        FileWriter fw=new FileWriter(TheFile);
                        fw.write(jTextPane1Results.getText());
                        fw.flush();
                        fw.close();
                    }
                }else{
                    TheFile.createNewFile();
                    FileWriter fw=new FileWriter(TheFile);
                    fw.write(jTextPane1Results.getText());
                    fw.flush();
                    fw.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        try {
            Desktop.getDesktop().open(new File("icons/Database_Colomns.png"));
        } catch (IOException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseEntered
        jLabel21.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLabel21MouseEntered

    private void jButton2GOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2GOActionPerformed
        String SearchQuery="";
        
        if(jList1.getSelectedIndex()<=0){
            SearchQuery="SELECT * FROM movies WHERE";
        }else if(jList1.getSelectedIndex()>0){
            String PerId=jList1.getSelectedValue().split("#")[1];
            SearchQuery="SELECT * FROM movies INNER JOIN ((SELECT DISTINCT movies_id FROM movies_has_persons WHERE persons_id=(SELECT id FROM persons WHERE id='"+PerId+"'))) as movies_id ON id=movies_id  WHERE";
        }
        String year="(year BETWEEN "+ jSpinner2yearsmall.getValue() +" AND "+ jSpinner1yearbig.getValue()+") ";
        String genre="";
        if(jComboBox1genres.getSelectedIndex()!=0){
            genre="AND (genre LIKE '%"+jComboBox1genres.getSelectedItem().toString()+"%')" ;
        }
        String rating="AND (imdbrating BETWEEN "+jSpinner4ratingsmall.getValue()+" AND "+jSpinner3ratingbig.getValue()+") ";
        String votes="AND (imdbvotes>"+jSpinner5votes.getValue()+" )"; 
        String country="";
        if(!jTextField1CountrysFind.getText().isEmpty()){
            country="AND(country LIKE '%"+jTextField1CountrysFind.getText()+"%')" ;
        }
        String orderby="";
        
        if(jComboBox2sortby.getSelectedIndex()==0){//name
            orderby="ORDER BY originalname ASC;";
        }
        if(jComboBox2sortby.getSelectedIndex()==1){
            orderby="ORDER BY originalname DESC;";
        }
        
        if(jComboBox2sortby.getSelectedIndex()==2){//year
            orderby="ORDER BY year ASC;";
        }
        if(jComboBox2sortby.getSelectedIndex()==3){
            orderby="ORDER BY year DESC;";
        }
        
        if(jComboBox2sortby.getSelectedIndex()==4){//votes
            orderby="ORDER BY imdbvotes ASC;";
        }
        if(jComboBox2sortby.getSelectedIndex()==5){
            orderby="ORDER BY imdbvotes DESC;";
        }
        
        if(jComboBox2sortby.getSelectedIndex()==6){//rating
            orderby="ORDER BY imdbrating ASC;";
        }
        if(jComboBox2sortby.getSelectedIndex()==7){
            orderby="ORDER BY imdbrating DESC;";
        }
        SearchQuery+= year + genre+rating+votes+country+orderby;
        jTextField1search.setText(SearchQuery);
//----------------------------------------------GET THE RESULTS BABY! :) bundan sonrası arama kutusu ile aynı 
        String SqlQuery=SearchQuery;
            String rgb = Integer.toHexString(SystemColor.window.getRGB());
            String backgroundColor = rgb.substring(2, rgb.length());
            try {
                ResultSet rs=iamastate.executeQuery(SqlQuery);
                int columnCount=rs.getMetaData().getColumnCount();
                int count=1;
                String ResultsGonnaWrite="<html>\n"
            + "<head>\n"
            + "<style type=\"text/css\">\n"
            + "table {\n" + "width: 100%;\n" +"font-family:arial;font-size:12px"+ "}\n"
            + "td, th {\n" + "background-color: #" + backgroundColor + "\n"
            + "}\n"
            + "</style>\n"
            + "</head>\n"
            + "<body>\n"
            + "SQL RESULTS:\n"
            + "<div style=\"background-color: black\">\n"
            + "<table border=\"0\" cellpadding=\"2\" cellspacing=\"1\">\n"+"<tr><th>#</th>";
                for (int i = 0; i < columnCount; i++) {
                    ResultsGonnaWrite+="<th>"+rs.getMetaData().getColumnName(i+1)+"</th>";
                }
                ResultsGonnaWrite+="</tr>";
                while(rs.next()){
                    ResultsGonnaWrite+="<td>"+count+"</td>";
                    for (int i = 0; i < columnCount; i++) {
                        ResultsGonnaWrite+="<td>"+rs.getString(i+1)+"</td>";
                    }
                    ResultsGonnaWrite+="</tr>";
                    count++;
                }
                ResultsGonnaWrite+="</table></body></html>";
                jTextPane1Results.setText(ResultsGonnaWrite);
            } catch (SQLException ex) {
                Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Laf aramızda ama yanlış bir şeyler mi yazdın acaba?\n\n"+ex);
            }
    }//GEN-LAST:event_jButton2GOActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new addMultiMovie().setVisible(true);
        setVisible(false);
        try {
            iamaconnection.close();
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        try {
            iamaconnection.close();
        } catch (SQLException ex) {
            Logger.getLogger(moviedatabasejframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
       if(evt.getKeyCode()==KeyEvent.VK_DELETE && jTable1.getSelectedRow()>-1){
           jButtonremove.doClick();
       }
    }//GEN-LAST:event_jTable1KeyReleased

    
    
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
            java.util.logging.Logger.getLogger(moviedatabasejframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(moviedatabasejframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(moviedatabasejframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(moviedatabasejframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new moviedatabasejframe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton2GO;
    private javax.swing.JButton jButtonADD;
    private javax.swing.JButton jButtonRefresh;
    private javax.swing.JButton jButtonremove;
    private javax.swing.JButton jButtonupdate;
    private javax.swing.JComboBox<String> jComboBox1genres;
    private javax.swing.JComboBox<String> jComboBox2sortby;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel16originalname;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel17altenatename;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel18DataInfo;
    private javax.swing.JLabel jLabel18imdb;
    private javax.swing.JLabel jLabel18year;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel19imdbaddress;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel20imdbrating;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel21imdbvotes;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel23genres;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel24watched;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel28countrys;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSpinner jSpinner1yearbig;
    private javax.swing.JSpinner jSpinner2yearsmall;
    private javax.swing.JSpinner jSpinner3ratingbig;
    private javax.swing.JSpinner jSpinner4ratingsmall;
    private javax.swing.JSpinner jSpinner5votes;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1CountrysFind;
    private javax.swing.JTextField jTextField1search;
    private javax.swing.JTextPane jTextPane1Actors;
    private javax.swing.JTextPane jTextPane1Director;
    private javax.swing.JTextPane jTextPane1Results;
    private javax.swing.JTextPane jTextPane2Writer;
    private javax.swing.JTextPane jTextPane3Camera;
    private javax.swing.JTextPane jTextPane4Music;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
}
