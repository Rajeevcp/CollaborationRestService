package com.niit.controller;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.niit.collaboration.dao.FileUploadDAO;
import com.niit.collaboration.model.UploadFile;
import com.niit.collaboration.model.User;
@RestController
public class FileUploadController {

	@Autowired
	private FileUploadDAO fileUploadDao;
        
	private User user;
	
	
	@ResponseBody
	@RequestMapping(value = "/duUpload", method = RequestMethod.POST)
	public String postFile(@RequestParam(value="file", required=false) MultipartFile file,
	                       @RequestParam(value="data") Object data) throws Exception {
		MultipartFile aFile = file;
         
         System.out.println("Saving file: " + aFile.getName());
	    return "OK!";
	}
	
	/*@RequestMapping(value = "/doUpload", method = RequestMethod.POST)
    public String handleFileUpload(HttpServletRequest request,
    		HttpSession session,
            @RequestParam CommonsMultipartFile fileUpload) throws Exception 
	{
		
		
         User user=(User)session.getAttribute("user");
         if(user==null)
         throw new RuntimeException("Not logged in");
         //System.out.println("user is: " + user.getUsername());
         if (fileUpload != null ) 
         {
             CommonsMultipartFile aFile = fileUpload;
            
                System.out.println("Saving file: " + aFile.getOriginalFilename());
                
                UploadFile uploadFile = new UploadFile();
                uploadFile.setFileName(aFile.getOriginalFilename());
                uploadFile.setData(aFile.getBytes());						//image 
                					//login details
                fileUploadDao.save(uploadFile);								//select * from proj2_profie_pics where username='smith'
                UploadFile getUploadFile=fileUploadDao.getFile(101);
            	String name=getUploadFile.getFileName();
            	System.out.println(getUploadFile.getData());
            	byte[] imagefiles=getUploadFile.getData();  				//image
            	try
            	{														//change the path according to your workspace and the name of your project
            		String path="C:/Users/HP-LAP/workspace/CollaborationFrontEnd/WebContent/images/101";
            		File file=new File(path);								//file.mkdirs();
            		FileOutputStream fos = new FileOutputStream(file);
            		fos.write(imagefiles);									// write the array of bytes in username file.
            		fos.close();
            	}
            	catch(Exception e)
            	{
            		e.printStackTrace();
            	}
         }
                
 
        return "Your Profile Picture has been uploaded successfully...";
    }*/
}

