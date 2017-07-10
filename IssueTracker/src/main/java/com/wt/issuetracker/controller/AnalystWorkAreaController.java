package com.wt.issuetracker.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.joda.time.Period;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;


import com.wt.issuetracker.dao.IssueDAO;
import com.wt.issuetracker.dao.TeamDAO;
import com.wt.issuetracker.dao.UserDAO;
import com.wt.issuetracker.entity.Analyst;
import com.wt.issuetracker.entity.Issue;
import com.wt.issuetracker.entity.Manager;
import com.wt.issuetracker.entity.Message;
import com.wt.issuetracker.entity.Team;
import com.wt.issuetracker.entity.User;
import com.wt.issuetracker.filter.CustomValidator;
import com.wt.issuetracker.security.SecurityCheck;

import javassist.bytecode.Descriptor.Iterator;

@Controller
public class AnalystWorkAreaController implements ServletContextAware {

	private ServletContext servletContext;
	private String path;

	
	@RequestMapping(value = "*/viewIssue.htm", method = RequestMethod.GET)
	public ModelAndView viewIssue(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				ArrayList<String> authList = new ArrayList<String>();
				authList.add("customer");
				authList.add("analyst");
				authList.add("manager");
				boolean x = SecurityCheck.checkAuthorization(request,response,authList);
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		ModelAndView mv = new ModelAndView();
		
		Long userId = (Long)request.getSession(false).getAttribute("sessionUserId");
				
		String authorization = (String)request.getSession(false).getAttribute("authorization");
		if(userId != null){
			
			Long issueId = Long.parseLong(request.getParameter("issueToDisplay"));
			IssueDAO issueDAO = new IssueDAO();
			Issue issue = issueDAO.searchByID(issueId);
			//Pagination
			int first = 1;
			if(request.getParameter("pageNum") == null || request.getParameter("pageNum").equals("")){
				first = 1;
			}
			else{
				first = Integer.parseInt(request.getParameter("pageNum"));
			}
			first = (first -1) *5;
			long tot = issueDAO.numberOfMessages(issueId);
			
			if(tot%5!=0){
				mv.addObject("totalPages",tot/5+1);
			}
			else{
				mv.addObject("totalPages",tot/5);
			}
			mv.addObject("totalMessages",tot);
			//Pagination ends here
			
			//List<Message> messages = issueDAO.getMessagesForIssueId(issueId); 
			mv.addObject("issueId",issueId);
			mv.addObject("issueToBeDisplayed", issue);
			List<Message> messageList = issueDAO.getMessagesForIssueId(issueId,first,5);
			//Collections.reverse(messageList);
			mv.addObject("messages",messageList);
			TeamDAO teamDAO = new TeamDAO();
			Set<Analyst> t = teamDAO.searchById(issue.getTeamId().getTeamId()).getAnalysts();
			
			
			//Logic for transfer to team user
			UserDAO userDAO = new UserDAO();
			User u = userDAO.searchById((Long)request.getSession(false).getAttribute("sessionUserId"));
			Analyst currentPrimaryAssignee = issue.getPrimaryAssigneeId();
			if(issue.getPrimaryAssigneeId()!=null)
			{
				if(!issue.getPrimaryAssigneeId().equals("")){
					if(u instanceof Analyst){
						Analyst a = (Analyst)u;				
						java.util.Iterator<Analyst> i = t.iterator();
						while(i.hasNext()){
							Analyst ia = (Analyst)i.next();
							if(ia.getUserId() == a.getUserId()  || currentPrimaryAssignee.getUserId() == ia.getUserId()){
								a = ia;
								
								break;
							}
						}
						t.remove(a);
					}
					if(u instanceof Manager){
						Analyst a = null;				
						java.util.Iterator<Analyst> i = t.iterator();
						while(i.hasNext()){
							Analyst ia = (Analyst)i.next();
							if(currentPrimaryAssignee.getUserId() == ia.getUserId()){
								a = ia;
								//t.remove(ia);
								break;
							}
						}
						if(a!=null){
							t.remove(a);
						}	
					}
				}
			}
			//System.out.println("Removing analyst : "+a.getFirstName() + " : status : "+t.remove(a));
			mv.addObject("teamList",t);
			//Logic for transfer to team user ends here
			
			//Deadline
			Period period = issueDAO.getDeadline(issueId);
			boolean passedDeadLine = issueDAO.isPastDeadline(issueId);
			
			mv.addObject("passedDeadLine",passedDeadLine);
			mv.addObject("deadline",""+period.getDays() + " days, " + period.getHours() + " hours, " + period.getMinutes()+ " minutes, "+period.getSeconds()+ " seconds"); 
			//Deadline code ends
			mv.setViewName("common/viewIssue");
			mv = setControlDisplayParameters(issueId,mv,u);
						
		}
		
		
		return mv;
		
	}
				
	
	
	@RequestMapping(value = "*/performAction.htm", method = RequestMethod.POST)
	public ModelAndView performActionOnIssue(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		//System.out.println("Before Security Issue id: " +request.getParameter("issueId"));
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				ArrayList<String> authList = new ArrayList<String>();
				authList.add("customer");
				authList.add("analyst");
				authList.add("manager");
				boolean x = SecurityCheck.checkAuthorization(request,response,authList);
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
			
		Long sessionUserId= (Long)request.getSession(false).getAttribute("sessionUserId");
		System.out.println("Session user id : "+sessionUserId);
		Long issueId= new Long(0);
		Long newUserId = new Long(0);
		String inputMessage = "";
		String action = "";
		///System.out.println("After Security Issue id: " +request.getParameter("issueId"));
		//Long issueId = Long.parseLong(request.getParameter("issueId"));
		ModelAndView mv = new ModelAndView();
		IssueDAO issueDAO = new IssueDAO();
		

			Message message = new Message();
			String newPath = "";
			Date fileDate = new Date();
			//File Upload
			ServletFileUpload uploader = null;
			//String path = request.getRealPath("/") + "Files";
			
			System.out.println(this.path);
			File file = new File(this.path);
			file.mkdirs(); //!wrong  
	        //file.getParentFile().mkdirs();//!correct
	        
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = file;
		fileFactory.setRepository(filesDir);
		uploader = new ServletFileUpload(fileFactory);	
			
		if(!ServletFileUpload.isMultipartContent(request)){
			throw new ServletException("Content type is not multipart/form-data");
		}
		/*
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("<html><head></head><body>");
		*/
		try {
			//List<FileItem> fileItemsList = uploader.parseRequest(request);
			//Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
			//while(fileItemsIterator.hasNext()){
				//FileItem fileItem = fileItemsIterator.next();
				
			List<FileItem> fileItemsList = uploader.parseRequest(request);
			java.util.Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
			while(fileItemsIterator.hasNext()){
				FileItem fileItem = fileItemsIterator.next();
				if (fileItem.isFormField()) {
	                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
	                String fieldname = fileItem.getFieldName();
	                if(fieldname.equals("issueId")){
	                	System.out.println("Issue ID before parsing from FileItem : "+fileItem.getString());
	                	issueId = Long.parseLong(fileItem.getString());
	                	System.out.println("Issue ID Parsed from FileItem : "+issueId);
	                }
	                else if(fieldname.equals("message")){
	                	inputMessage = fileItem.getString();
	                	System.out.println("Messge Parsed from FileItem : "+inputMessage);
	                	inputMessage = CustomValidator.sanitizeInputAlphaNumeric(inputMessage);
	                }
	                else if(fieldname.equals("action")){
	                	action = fileItem.getString();
	                	System.out.println("Action Parsed from FileItem : "+action);

	                }
	                else if (fieldname.equals("newAssignee")){
	                	newUserId = Long.parseLong(fileItem.getString());
	                	System.out.println("New Assignee Parsed from FileItem : "+inputMessage);
	                }
	                
	            } else {
	                // Process form file field (input type="file").
	            	
					if(fileItem.getName()!=null && !fileItem.getName().equals("") && fileItem.getSize()!=0){
						
						System.out.println("FieldName="+fileItem.getFieldName());
						System.out.println("FileName="+fileItem.getName());
						System.out.println("ContentType="+fileItem.getContentType());
						System.out.println("Size in bytes="+fileItem.getSize());
						
						//File file = new File(request.getServletContext().getAttribute("FILES_DIR")+File.separator+fileItem.getName());
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd-hhmmss");
						String appendVal = df.format(fileDate);
						
						newPath = sessionUserId+"-"+appendVal+"-"+fileItem.getName();
						String filePath = this.path+File.separator+sessionUserId+"-"+appendVal+"-"+fileItem.getName();
						
						file = new File(filePath);
						System.out.println("Absolute Path at server="+file.getAbsolutePath());
						if (!file.exists()){
		                    file.createNewFile();
		                    fileItem.write(file);
		                } 
						else{
							fileItem.write(file);
						}
						//fileItem.write(file);
						//out.write("File "+fileItem.getName()+ " uploaded successfully.");
						//out.write("<br>");
						//out.write("<a href=\"UploadDownloadFileServlet?fileName="+fileItem.getName()+"\">Download "+fileItem.getName()+"</a>");
					}
				}

			}
		} catch (FileUploadException e) {
			//out.write("Exception in uploading file.");
			e.printStackTrace();
		} catch (Exception e) {
			//out.write("Exception in uploading file.");
			e.printStackTrace();
			
		}
			 
			//End of file upload
		
		if(action!=null || !action.equals("")){
				
				if(action.equals("Post Comment")){
					//String inputMessage = request.getParameter("message"); 
					
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					message.setActionPerformed(Message.ActionToBePerformed.COMMENT.toString());
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					
					
					
					issueDAO.addMessage(issueId, message);
					
					mv.setViewName("common/viewIssue");
				}
				else if(action.equals("Send For Clarification")){
					//String inputMessage = request.getParameter("message"); 
					
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					message.setActionPerformed(Message.ActionToBePerformed.SENT_FOR_CLARIFICATION.toString());
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					issueDAO.sendForClarification(issueId, message);
					
					mv.setViewName("common/viewIssue");
				}
				
				
				
				else if(action.equals("Approve")){
					//String inputMessage = request.getParameter("message"); 
					
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					
					Issue iss = issueDAO.searchByID(issueId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					if(iss.getStatus().equals(Issue.Status.PENDING_CLOSURE_APPROVAL.toString())){
						message.setActionPerformed(Message.ActionToBePerformed.APPROVED_CLOSURE.toString());
					}
					else if(iss.getStatus().equals(Issue.Status.PENDING_CLARIFICATION_APPROVAL.toString())){
						message.setActionPerformed(Message.ActionToBePerformed.APPROVED_CLARIFICATION.toString());
					}
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					issueDAO.approve(issueId, message);
					
					mv.setViewName("common/viewIssue");
				}
				
				else if(action.equals("Reject")){
					//String inputMessage = request.getParameter("message"); 
					System.out.println("REJECT");
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					Issue iss = issueDAO.searchByID(issueId);
					if(iss.getStatus().equals(Issue.Status.PENDING_CLOSURE_APPROVAL.toString())){
						message.setActionPerformed(Message.ActionToBePerformed.REJECTED_CLOSURE.toString());
					}
					if(iss.getStatus().equals(Issue.Status.PENDING_CLARIFICATION_APPROVAL.toString())){
						message.setActionPerformed(Message.ActionToBePerformed.REJECTED_CLARIFICATION.toString());
					}
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					issueDAO.rejectClarification(issueId, message);
					
					mv.setViewName("common/viewIssue");
				}
				
				else if(action.equals("Provide Clarification")){
					//String inputMessage = request.getParameter("message"); 
					
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					message.setActionPerformed(Message.ActionToBePerformed.PROVIDED_CLARIFICATION.toString());
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					issueDAO.providedClarification(issueId, message);
					
					mv.setViewName("common/viewIssue");
				}
				
				else if(action.equals("Close Issue")){
					//String inputMessage = request.getParameter("message"); 
					
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					message.setActionPerformed(Message.ActionToBePerformed.CLOSED_BY_ANALYST.toString());
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					issueDAO.closeRequest(issueId, message);
					
					mv.setViewName("common/viewIssue");
				}
				
				else if(action.equals("Confirm Closure")){
					//String inputMessage = request.getParameter("message"); 
					
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					message.setActionPerformed(Message.ActionToBePerformed.CLOSED_BY_REQUESTOR.toString());
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					issueDAO.confirmClosure(issueId, message);
					
					mv.setViewName("common/viewIssue");
				}
				
				else if(action.equals("Re-Open")){
					//String inputMessage = request.getParameter("message"); 
					
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					message.setActionPerformed(Message.ActionToBePerformed.REOPEN.toString());
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					issueDAO.reopen(issueId, message);
					
					mv.setViewName("common/viewIssue");
				}
				
				else if(action.equals("Transfer")){
					//String inputMessage = request.getParameter("message"); 
					
					//Long newUserId = Long.parseLong(request.getParameter("newAssignee"));
					
					message.setDescription(inputMessage);
					message.setSenderId(sessionUserId);
					UserDAO userDAO = new UserDAO();
					User u = userDAO.searchById(sessionUserId);
					message.setSenderName(u.getFirstName() + " " + u.getLastName());
					message.setSentDate(fileDate);
					message.setActionPerformed(Message.ActionToBePerformed.TRANSFERRED.toString());
					if(newPath!=null && newPath!=""){
					message.setAttachment(newPath);
					}
					issueDAO.transfer(issueId, message,newUserId);
					
					mv.setViewName("common/viewIssue");
				}
		
			TeamDAO teamDAO = new TeamDAO();
			Issue issue = issueDAO.searchByID(issueId);
			Set<Analyst> t = teamDAO.searchById(issue.getTeamId().getTeamId()).getAnalysts();
			
			//Logic for transfer to team user
			UserDAO userDAO = new UserDAO();
			User u = userDAO.searchById((Long)request.getSession(false).getAttribute("sessionUserId"));
			Analyst currentPrimaryAssignee = issue.getPrimaryAssigneeId();
			if(u instanceof Analyst){
				Analyst a = (Analyst)u;				
				java.util.Iterator<Analyst> i = t.iterator();
				while(i.hasNext()){
					Analyst ia = (Analyst)i.next();
					if(ia.getUserId() == a.getUserId()  || currentPrimaryAssignee.getUserId() == ia.getUserId()){
						a = ia;
						//t.remove(ia);
						break;
					}
				}
				t.remove(a);
			}
			
			if(u instanceof Manager){
				Analyst a = null;				
				java.util.Iterator<Analyst> i = t.iterator();
				while(i.hasNext()){
					Analyst ia = (Analyst)i.next();
					if(currentPrimaryAssignee.getUserId() == ia.getUserId()){
						a = ia;
						//t.remove(ia);
						break;
					}
				}
				if(a!=null){
					t.remove(a);
				}	
			}
			
			//System.out.println("Removing analyst : "+a.getFirstName() + " : status : "+t.remove(a));
			mv.addObject("teamList",t);
			//Logic for transfer to team user ends here
			
			mv.addObject("issueToBeDisplayed",issue);
			
			//Set parameters for control
			mv = setControlDisplayParameters(issueId,mv,u);
			
			//Pagination
			int first = 1;
			if(request.getParameter("pageNum") == null || request.getParameter("pageNum").equals("")){
				first = 1;
			}
			/*
			else{
				first = Integer.parseInt(request.getParameter("pageNum"));
			}
			*/
			first = (first -1) *5;
			long tot = issueDAO.numberOfMessages(issueId);
			if(tot%5!=0){
				mv.addObject("totalPages",tot/5+1);
			}
			else{
				mv.addObject("totalPages",tot/5);
			}
			mv.addObject("totalMessages",tot);
			//Pagination ends here
			
			List<Message> messageList = issueDAO.getMessagesForIssueId(issueId,first,5);
			//Collections.reverse(messageList);
			mv.addObject("messages",messageList);
		
			//Deadline
			Period period = issueDAO.getDeadline(issueId);
			boolean passedDeadLine = issueDAO.isPastDeadline(issueId);
			
			mv.addObject("passedDeadLine",passedDeadLine);
			mv.addObject("deadline",""+period.getDays() + " days, " + period.getHours() + " hours, " + period.getMinutes()+ " minutes, "+period.getSeconds()+ " seconds"); 
			//Deadline code ends
		
		}

		
		mv.addObject("issueId",issueId);
		
		return mv;	
	}


	//Download file
	@RequestMapping(value = "*/downloadFile.htm", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response){
		//Security Check
			if(SecurityCheck.checkAuthentication(request,response)){
				System.out.println("Auth success");
				ArrayList<String> authList = new ArrayList<String>();
				authList.add("customer");
				authList.add("analyst");
				authList.add("manager");
				boolean x = SecurityCheck.checkAuthorization(request,response,authList);
				System.out.println("Authorizzation result : "+x);
			}
		//End Security Check
		try{
		String fileName = request.getParameter("fileName");
		if(fileName == null || fileName.equals("")){
			throw new ServletException("File Name can't be null or empty");
		}
		
		String filePath = this.path + File.separator + fileName;
		System.out.println(filePath);
		File file = new File(filePath);
		if(!file.exists()){
			throw new ServletException("File doesn't exists on server.");
		}
		System.out.println("File location on server::"+file.getAbsolutePath());
		ServletContext ctx = this.servletContext;
		InputStream fis = new FileInputStream(file);
		String mimeType = ctx.getMimeType(file.getAbsolutePath());
		response.setContentType(mimeType != null? mimeType:"application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		
		ServletOutputStream os       = response.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read=0;
		while((read = fis.read(bufferData))!= -1){
			os.write(bufferData, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
		System.out.println("File downloaded at client successfully");
		}
		catch(ServletException e){
			e.printStackTrace();
		}
		catch(FileNotFoundException f){
			f.printStackTrace();
		}
		catch(IOException io){
			io.printStackTrace();
		}
	
	}
	
	

	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
		this.path = this.servletContext.getInitParameter("attachmentDirectory") +File.separator + servletContext.getInitParameter("subFolder");
	}
		
	public ModelAndView setControlDisplayParameters(Long issueId,ModelAndView mv,User u){
		//Passing paramters for displaying correct controls
		IssueDAO isueDAO = new IssueDAO();
		
		Issue issue = isueDAO.searchByID(issueId);
		
		if(issue.getIssueCurrentlyWithId()!=null){
			mv.addObject("roleofassigneduser",issue.getIssueCurrentlyWithId().getRole());
		}
		if(u!=null){
			if(u instanceof Manager){
				Manager uManager = (Manager)u;
				Team issueTeam = issue.getTeamId();
				List<Team> uManTeamList = uManager.getTeamList();
				System.out.println("Issue Team : " + issueTeam.getTeamId());
				for(Team userTeam : uManTeamList){
					System.out.println("userTeam Team : " + userTeam.getTeamId());
					if(userTeam.getTeamId() == issueTeam.getTeamId()){
						mv.addObject("belongsToTeam","yes");
					}
				}
				
			}
		}
		
		return mv;
		//End Passing parameters for displaying correct controls
		
	}


	@RequestMapping(value = "*/downloadPdf.pdf", method = RequestMethod.GET)
    public ModelAndView downloadExcel(HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		System.out.println("PDFHIT");
		//Security Check
		if(SecurityCheck.checkAuthentication(request,response)){
			System.out.println("Auth success");
			ArrayList<String> authList = new ArrayList<String>();
			authList.add("customer");
			authList.add("analyst");
			authList.add("manager");
			boolean x = SecurityCheck.checkAuthorization(request,response,authList);
			System.out.println("Authorizzation result : "+x);
		}
	//End Security Check	
		
		if(request.getParameter("issueId")!=null){
			Long issueId = null;
			try{
				issueId = Long.parseLong(request.getParameter("issueId"));
				IssueDAO issueDAO = new IssueDAO();
				List<Message> messageList = issueDAO.getAllMessagesForIssueId(issueId);
				ModelAndView mv = new ModelAndView();
				mv.addObject("messageList",messageList);
				mv.addObject("pdfIssue",issueDAO.searchByID(issueId));
				mv.addObject("error",false);
				mv.setViewName("issuePdfView");
				return mv;
			}
			catch(NumberFormatException e){
				e.printStackTrace();
				return new ModelAndView("pdfView", "error", true);
			}

		}
		else{
			return new ModelAndView("pdfView", "error", true);
		}
		
	}


}
