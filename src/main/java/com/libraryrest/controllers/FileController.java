package com.libraryrest.controllers;


import com.libraryrest.DAO.BookDAO;
import com.libraryrest.DAO.UserDao;
import com.libraryrest.models.Book;
import com.libraryrest.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Handles requests for the application file upload requests
 */
@RestController
public class FileController {

    @Autowired
    BookDAO bookDAO;

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public void uploadImageHandler(@RequestParam("image") MultipartFile file,
                                    @RequestParam("bookName") String bookName) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                // find path
                String path = System.getProperty("user.home") + "/images";
                String pathForDatabase = "/images/";
                // Creating the directory to store file
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                Book book = bookDAO.findByName(bookName);
                String fileName = book.hashCode() + ((int) Math.random()*9999) + ".jpeg";
                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + fileName);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                //треба доробити
                book.setImageUrl(pathForDatabase + fileName);
                bookDAO.update(book);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @RequestMapping(value = "/uploadPdf", method = RequestMethod.POST)
    public void uploadPdfHandler(@RequestParam("pdf") MultipartFile file,
                                   @RequestParam("bookName") String bookName) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                // find path
                String path = System.getProperty("user.home") + "/pdf";
                String pathForDatabase = "/pdf/";
                // Creating the directory to store file
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                Book book = bookDAO.findByName(bookName);
                String fileName = book.hashCode() + ((int) Math.random()*9999) + ".pdf";
                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + fileName);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                book.setPdfFileUrl(pathForDatabase + fileName);
                bookDAO.update(book);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/uploadUserAvatar", method = RequestMethod.POST)
    public String uploadUserAvatarHandler(@RequestParam("avatar") MultipartFile file,
                                   @RequestParam("login") String login) {
        String pathForDatabase = "";
        String fileName = "";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                // find path
                String path = System.getProperty("user.home") + "/images";
                pathForDatabase = "/images/";
                // Creating the directory to store file
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                User user = userDao.findByName(login);
                fileName = user.hashCode() + ((int) Math.random()*9999) + ".jpeg";
                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + fileName);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                user.setAvatarUrl(pathForDatabase + fileName);
                userDao.update(user);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return pathForDatabase + fileName;
    }

}