/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.leoskvorc.view.admin;

import hr.leoskvorc.dal.RepositoryFactory;
import hr.leoskvorc.model.Movie;
import hr.leoskvorc.parsers.rss.MovieParser;
import hr.leoskvorc.utils.MessageUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import hr.leoskvorc.dal.MovieRepository;
import hr.leoskvorc.view.model.PanelMovieModel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author lskvo
 */
public class DataManagementPanel extends PanelMovieModel {

    private DefaultListModel<Movie> movieModel;
    private static final String currentPath = System.getProperty("user.dir");
    private static final Path DIR = Paths.get(currentPath).getRoot();
    private MovieRepository repository;

    /**
     * Creates new form DataManagement
     */
    public DataManagementPanel() {
        initComponents();
        init();
        super.setRepository(repository);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDelete = new javax.swing.JButton();
        btnLoad = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lsMovies = new javax.swing.JList<>();

        btnDelete.setBackground(new java.awt.Color(91, 0, 0));
        btnDelete.setText("Delete data");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnLoad.setBackground(new java.awt.Color(51, 81, 8));
        btnLoad.setText("Load data");
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(lsMovies);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 500, Short.MAX_VALUE)
                        .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDelete)
                    .addComponent(btnLoad))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed

        try {
            List<Movie> movies = MovieParser.parse();
            repository.createMovies(movies);
            loadModel();
        } catch (Exception ex) {
            Logger.getLogger(DataManagementPanel.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", ex.getMessage());//"Unable to upload movies.");
            System.exit(1);
        }
    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            List<Movie> movies = repository.selectMovies();
            for (Movie movie : movies) {
                Path picture = Paths.get(movie.getPicturePath());
                Files.deleteIfExists(picture);
            }
            repository.deleteMovies();
            loadModel();
        } catch (Exception ex) {
            Logger.getLogger(DataManagementPanel.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Error", "Unable to delete movies!");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLoad;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<Movie> lsMovies;
    // End of variables declaration//GEN-END:variables

    private void init() {
        try {
            repository = RepositoryFactory.getMovieRepository();
            movieModel = new DefaultListModel<>();

            loadModel();
        } catch (Exception ex) {
            Logger.getLogger(DataManagementPanel.class.getName()).log(Level.SEVERE, null, ex);
            MessageUtils.showErrorMessage("Unrecoverable error", "Cannot initiate the form.");
            System.exit(1);
        }
    }

    private void loadModel() throws Exception {
        List<Movie> movies = repository.selectMovies();
        movieModel.clear();
        movies.forEach(movieModel::addElement);
        lsMovies.setModel(movieModel);
    }
}
