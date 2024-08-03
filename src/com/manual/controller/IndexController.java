package com.manual.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.manual.model.Configurations;
import com.manual.model.Container;
import com.manual.model.ContainerData;
import com.manual.model.ImageData;
import com.manual.model.Scene;
import com.manual.service.ManualService;
import com.manual.util.ManualFunctions;
import com.manual.util.ManualUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class IndexController 
{
	@Autowired
	ManualService manualService;
	public static Socket session_socket;
	public static Configurations session_Configurations;
	public static PrintWriter print_writer;
	public static ContainerData session_Data;
	String session_selected_sports,session_selected_PreviewIp;
	String Data;
	String Scene;
	boolean is_previous_data = false;
	List<ImageData> imgdata = new ArrayList<ImageData>();
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model) throws JAXBException  
	{
		if(new File(ManualUtil.CONFIGURATION_DIRECTORY + ManualUtil.OUTPUT_XML).exists()) {
			session_Configurations = (Configurations)JAXBContext.newInstance(Configurations.class).createUnmarshaller().unmarshal(
					new File(ManualUtil.CONFIGURATION_DIRECTORY  + ManualUtil.OUTPUT_XML));
		} else {
			session_Configurations = new Configurations();
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
					new File(ManualUtil.CONFIGURATION_DIRECTORY + ManualUtil.OUTPUT_XML));
		}
		model.addAttribute("session_Configurations",session_Configurations);
		
		return "initialise";
	}

	@RequestMapping(value = {"/manual"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String manualPage(ModelMap model, MultipartHttpServletRequest request,
			@RequestParam(value = "select_sports", required = false, defaultValue = "") String select_sports,
			@RequestParam(value = "vizIPAddressEverest", required = false, defaultValue = "") String vizIPAddressEverest,
			@RequestParam(value = "vizIPAddressScenes", required = false, defaultValue = "") String vizIPAddressScenes,
			@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") int vizPortNumber)
			throws UnknownHostException,JAXBException, IOException,IllegalAccessException,InvocationTargetException, URISyntaxException
	{
		session_selected_sports = select_sports;
		session_selected_PreviewIp = vizIPAddressEverest;
		
		if(!vizIPAddressEverest.trim().isEmpty() && vizPortNumber != 0) {
			session_socket = new Socket(vizIPAddressEverest, Integer.valueOf(vizPortNumber));
			print_writer = new PrintWriter(session_socket.getOutputStream(), true);
		}
		
		session_Configurations = new Configurations(vizIPAddressEverest,vizIPAddressScenes, vizPortNumber);
		
		JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
				new File(ManualUtil.CONFIGURATION_DIRECTORY + ManualUtil.OUTPUT_XML));
		
		switch(session_selected_sports) {
		case "BADMINTON":
			model.addAttribute("session_viz_scenes", new File(ManualUtil.BADMINTON_SCENE_DIRECTORY + 
					ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".sum") && pathname.isFile();
			    }
			}));
			
			model.addAttribute("scene_files", new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".xml") && pathname.isFile();
			    }
			}));
			break;
		case "CRICKET":
			if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
				model.addAttribute("session_viz_scenes", new File(ManualUtil.CRICKET_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".sum") && pathname.isFile();
				    }
				}));
				
				model.addAttribute("scene_files", new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".xml") && pathname.isFile();
				    }
				}));
			}else {
				model.addAttribute("session_viz_scenes", new File("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.CRICKET_SCENE_DIRECTORY.replace("C:", "c") + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".sum") && pathname.isFile();
				    }
				}));
				
				model.addAttribute("scene_files", new File("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".xml") && pathname.isFile();
				    }
				}));
			}
			
			break;
		case "FOOTBALL":
			model.addAttribute("session_viz_scenes", new File(ManualUtil.FOOTBALL_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".sum") && pathname.isFile();
			    }
			}));
			
			model.addAttribute("scene_files", new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".xml") && pathname.isFile();
			    }
			}));
			break;	
		}
		
		model.addAttribute("session_selected_sports", session_selected_sports);
		model.addAttribute("session_Data", session_Data);
		return "manual";
	}
	
	@RequestMapping(value = {"/back_to_manual"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String backToManualPage(ModelMap model)
	{
		switch(session_selected_sports) {
		case "BADMINTON":
			model.addAttribute("session_viz_scenes", new File(ManualUtil.BADMINTON_SCENE_DIRECTORY + 
					ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".sum") && pathname.isFile();
			    }
			}));
			
			model.addAttribute("scene_files", new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".xml") && pathname.isFile();
			    }
			}));
			break;
		case "CRICKET":
			if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
				model.addAttribute("session_viz_scenes", new File(ManualUtil.CRICKET_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".sum") && pathname.isFile();
				    }
				}));
				
				model.addAttribute("scene_files", new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".xml") && pathname.isFile();
				    }
				}));
			}else {
				model.addAttribute("session_viz_scenes", new File("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.CRICKET_SCENE_DIRECTORY.replace("C:", "c") + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".sum") && pathname.isFile();
				    }
				}));
				
				model.addAttribute("scene_files", new File("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
					@Override
				    public boolean accept(File pathname) {
				        String name = pathname.getName().toLowerCase();
				        return name.endsWith(".xml") && pathname.isFile();
				    }
				}));
			}
			
			break;
		case "FOOTBALL":
			model.addAttribute("session_viz_scenes", new File(ManualUtil.FOOTBALL_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".sum") && pathname.isFile();
			    }
			}));
			
			model.addAttribute("scene_files", new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".xml") && pathname.isFile();
			    }
			}));
			break;	
		}
		
		model.addAttribute("session_selected_sports", session_selected_sports);
		model.addAttribute("session_Data", session_Data);
		return "manual";
	}
	
	@RequestMapping(value = {"/save_data","/uploadFileToManual","/preview"}, method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody String uploadFormDataToSessionObjects(MultipartHttpServletRequest request)
					throws IllegalAccessException, InvocationTargetException, IOException, JAXBException, NumberFormatException, InterruptedException
	{
		File file;
		MultipartFile mpf;
		String whichFile = "",file_name = "";
		
			if (request.getRequestURI().contains("save_data")||request.getRequestURI().contains("preview")) {
				
				List<Container> containers = new ArrayList<Container>();
				containers.clear();
				for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
					if(entry.getKey().contains("previous_xml_data") || entry.getKey().contains("selectedScene") 
							|| entry.getKey().contains("manual_file_timestamp") 
							|| entry.getKey().contains("select_sport")) {
					}
					else if(entry.getKey().contains("scenePath")) {
						if(!entry.getKey().contains("0_scenePath")) {
							if(is_previous_data == true) {
								
								containers.add(new Container(Integer.valueOf(0), entry.getKey().replaceFirst("", "0_"), session_Data.getContainers().get(0).getContainer_value()));
							}else {
								containers.add(new Container(Integer.valueOf(0), entry.getKey().replaceFirst("", "0_"), entry.getValue()[0]));
							}
						}
					}
					else if(entry.getKey().contains("Logo") || entry.getKey().contains("Image")) {
						if(imgdata.size() > 0) {
							for(int i=0;i<imgdata.size();i++) {
								
								if(imgdata.get(i).getImageId().equalsIgnoreCase(entry.getKey())) {
									containers.add(new Container(Integer.valueOf(entry.getKey().split("_")[0]), entry.getKey(), imgdata.get(i).getImagePath()));
									break;
								}
							}
							
							SetData(containers, is_previous_data, imgdata,entry.getKey(),entry.getValue()[0]);
							
							
						}else {
							if(is_previous_data == true) {
								for(int i = 0;i< session_Data.getContainers().size();i++) {
									if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(entry.getKey())) {
										containers.add(new Container(Integer.valueOf(entry.getKey().split("_")[0]), entry.getKey(), session_Data.getContainers().get(i).getContainer_value()));
										//break;
									}
								}
							}else {
								containers.add(new Container(Integer.valueOf(entry.getKey().split("_")[0]), entry.getKey(), entry.getValue()[0]));
							}
						}
					}
					else if(entry.getKey().contains("file_name")) {
						file_name = entry.getValue()[0];
					}
					else {
						containers.add(new Container(Integer.valueOf(entry.getKey().split("_")[0]), entry.getKey(), entry.getValue()[0]));
					}		
				}
				Collections.sort(containers);
				if(request.getRequestURI().contains("save_data")) {
					switch (session_selected_sports) {
					case "BADMINTON":
						JAXBContext.newInstance(ContainerData.class).createMarshaller().marshal(new ContainerData(containers), 
								new File(ManualUtil.MANUAL_DIRECTORY + 
										ManualUtil.DATA_DIRECTORY + file_name + ManualUtil.XML));
						break;
					case "CRICKET":
//						if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
//							JAXBContext.newInstance(ContainerData.class).createMarshaller().marshal(new ContainerData(containers), 
//									new File(ManualUtil.MANUAL_DIRECTORY + 
//											ManualUtil.DATA_DIRECTORY + file_name + ManualUtil.XML));
//						}else {
//							JAXBContext.newInstance(ContainerData.class).createMarshaller().marshal(new ContainerData(containers), 
//									new File("//" + session_Configurations.getIpAddressScenes() + "//" +
//											ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY + file_name + ManualUtil.XML));
//						}
						String basePath = session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || 
		                  session_Configurations.getIpAddressScenes().equalsIgnoreCase("") 
		                  ? ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY 
		                  : "//" + session_Configurations.getIpAddressScenes() + "//" + 
		                    ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY;

						File Xmlfile = new File(basePath + file_name + ManualUtil.XML);
						File parentDir = Xmlfile.getParentFile();
						if (!parentDir.exists()) parentDir.mkdirs();
				
						JAXBContext.newInstance(ContainerData.class).createMarshaller().marshal(new ContainerData(containers), Xmlfile);
						break;
					case "FOOTBALL":
						JAXBContext.newInstance(ContainerData.class).createMarshaller().marshal(new ContainerData(containers), 
								new File(ManualUtil.MANUAL_DIRECTORY + 
										ManualUtil.DATA_DIRECTORY + file_name + ManualUtil.XML));
						break;	
					}
				}else if (request.getRequestURI().contains("preview")) {
					for(int i = 1; i < containers.size() ; i++) {
						if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + containers.get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
									containers.get(i).getContainer_value() + ";");
						}
						
					}
					TimeUnit.SECONDS.sleep(1);
					Scene = containers.get(0).getContainer_value().split("/")[ containers.get(0).getContainer_value().split("/").length-1].replace(".sum", "");
					ManualFunctions.Preview(session_selected_sports,Scene, print_writer);
				}
				
			}else if (request.getRequestURI().contains("uploadFileToManual")) {
				
				 Iterator<String> fileItr = request.getFileNames();
				 
				 if (fileItr.hasNext()) {
					 
					 whichFile = request.getFileMap().entrySet().iterator().next().getKey();
					 
					 if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
						if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
							imgdata.add(new ImageData(whichFile,ManualUtil.MANUAL_MEDIA_DIRECTORY_PATH + 
									"DOAD" + "_" + request.getFileMap().entrySet().iterator().next().getValue().getOriginalFilename()));
						}
						
					}else {
						imgdata.add(new ImageData(whichFile,"//" + session_Configurations.getIpAddressScenes() + "/" + ManualUtil.MANUAL_MEDIA_DIRECTORY_PATH.replace("C:", "c") + 
								"DOAD" + "_" + request.getFileMap().entrySet().iterator().next().getValue().getOriginalFilename()));
					}
					 
					 while (fileItr.hasNext()) {
						 
						mpf = request.getFile(fileItr.next());
						if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
							if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
								file = new File(ManualUtil.MANUAL_MEDIA_DIRECTORY_PATH + 
										"DOAD" + "_" + request.getFileMap().entrySet().iterator().next().getValue().getOriginalFilename());
								mpf.transferTo(file);
							}
							
						}else {
							file = new File("//" + session_Configurations.getIpAddressScenes() + "/" + ManualUtil.MANUAL_MEDIA_DIRECTORY_PATH.replace("C:", "c") + 
									"DOAD" + "_" + request.getFileMap().entrySet().iterator().next().getValue().getOriginalFilename());
							mpf.transferTo(file);
						}
					 }
				  }
		      }
			
		return JSONObject.fromObject(session_Data).toString();
	}

	@RequestMapping(value = {"/processManualProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processManualProcedures(
			@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
			@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
					throws IOException, IllegalAccessException, InvocationTargetException, JAXBException, InterruptedException
	{	
		switch (whatToProcess.toUpperCase()) {
		
		case "LOAD_SCENE": case "LOAD_DATA": case "LOAD_PREVIOUS_SCENE": case "ANIMATE-OUT": case "ANIMATE-IN": case "CLEAR-ALL": case "BADMINTON-OPTIONS": 
		case "READ-DATA-AND-PREVIEW": case "LOAD_CONTAINER": case "PREVIEW":case "MATCH_PREVIEW":case"PREVIEW_IMAGE_DATA":
			switch (session_selected_sports) {
			
			case "BADMINTON":
				switch(whatToProcess.toUpperCase()) {
				case "LOAD_PREVIOUS_SCENE":
					new Scene(ManualUtil.BADMINTON_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + Scene).
						scene_load(print_writer,ManualUtil.BADMINTON_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + Scene);
					break;
				case "LOAD_SCENE":
					Scene = valueToProcess;
					new Scene(ManualUtil.BADMINTON_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + valueToProcess).
						scene_load(print_writer,ManualUtil.BADMINTON_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + valueToProcess);
					break;
				}
				switch (whatToProcess.toUpperCase()) {
				case "LOAD_PREVIOUS_SCENE":
					session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
							new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY + valueToProcess));
					Collections.sort(session_Data.getContainers());
					
					for(int i = 0; i < session_Data.getContainers().size(); i++) {
					
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_Data.getContainers().get(i).getContainer_key().replaceFirst((i+1)
								+"_", "") + " " + session_Data.getContainers().get(i).getContainer_value() + ";");
						
					}
					ManualFunctions.Preview(session_selected_sports,Scene, print_writer);
					return JSONObject.fromObject(session_Data).toString();
					
				case "LOAD_DATA":
					
					print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE " + 
							ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
					TimeUnit.SECONDS.sleep(2);
					boolean exitLoop = false; int numberOfAttempts = 5;
					List<String> allLines = new ArrayList<String>();
					while (exitLoop == false){
						if(new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE).exists()) {
							allLines = Files.readAllLines(Paths.get(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE));
							break;
						} else {
							TimeUnit.SECONDS.sleep(1);
							numberOfAttempts = numberOfAttempts - 1;
						}
						if(numberOfAttempts <= 0)
						{
							break;
						}
					}
					return JSONArray.fromObject(allLines).toString();
				}
				break;
				
			case "FOOTBALL":
				switch(whatToProcess.toUpperCase()) {
				case "LOAD_PREVIOUS_SCENE":
					new Scene(ManualUtil.FOOTBALL_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + Scene).
						scene_load(print_writer,ManualUtil.FOOTBALL_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + Scene);
					break;
				case "LOAD_SCENE":
					Scene = valueToProcess;
					new Scene(ManualUtil.FOOTBALL_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + valueToProcess).
						scene_load(print_writer,ManualUtil.FOOTBALL_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + valueToProcess);
					break;
				}
				switch (whatToProcess.toUpperCase()) {
				case "LOAD_PREVIOUS_SCENE":
					print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out SHOW 0.0;");
					print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
					session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
							new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY + valueToProcess));
					Collections.sort(session_Data.getContainers());
					
					for(int i = 0; i < session_Data.getContainers().size(); i++) {
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_Data.getContainers().get(i).getContainer_key().replaceFirst((i+1)
								+"_", "") + " " + session_Data.getContainers().get(i).getContainer_value() + ";");
					}
					//Scene = session_Data.getContainers().get(0).getContainer_value().split("Scenes/")[1];
					ManualFunctions.Preview(session_selected_sports,Scene, print_writer);
					return JSONObject.fromObject(session_Data).toString();
					
				case "LOAD_DATA":
					print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE " + 
							ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
					TimeUnit.SECONDS.sleep(2);
					boolean exitLoop = false; int numberOfAttempts = 5;
					List<String> allLines = new ArrayList<String>();
					while (exitLoop == false){
						if(new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE).exists()) {
							allLines = Files.readAllLines(Paths.get(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE));
							break;
						} else {
							TimeUnit.SECONDS.sleep(1);
							numberOfAttempts = numberOfAttempts - 1;
						}
						if(numberOfAttempts <= 0)
						{
							break;
						}
					}
					return JSONArray.fromObject(allLines).toString();
				}
				break;
				
			case "CRICKET":
				switch(whatToProcess.toUpperCase()) {
				case "LOAD_CONTAINER":
					imgdata.clear();
					is_previous_data = true;
					if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
						session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
								new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY + valueToProcess));
					}else {
						session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
								new File("//" + session_Configurations.getIpAddressScenes() + "//" + 
										ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY + valueToProcess));
					}
					
					Collections.sort(session_Data.getContainers());
					
					TimeUnit.SECONDS.sleep(2);
					Scene = session_Data.getContainers().get(0).getContainer_value().split("Scenes/")[1];
					//ManualFunctions.Preview(Scene, print_writer);
					
					return JSONObject.fromObject(session_Data).toString();
			
				case "READ-DATA-AND-PREVIEW": case "PREVIEW":
					imgdata.clear();
					if(valueToProcess.equalsIgnoreCase("BLANK")) {
						return JSONObject.fromObject(session_Data).toString();
					}else {
						if(whatToProcess.toUpperCase().equalsIgnoreCase("READ-DATA-AND-PREVIEW")) {
							is_previous_data = true;
						}
						
						if(whatToProcess.toUpperCase().equalsIgnoreCase("PREVIEW")) {
							valueToProcess = valueToProcess.replace(".sum", ".xml");
						}
						//TimeUnit.SECONDS.sleep(3);
						
						if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
							session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
									new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY + valueToProcess));
						}else {
							session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
									new File("//" + session_Configurations.getIpAddressScenes() + "//" +
											ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY + valueToProcess));
						}
						
						Collections.sort(session_Data.getContainers());
						
						if(whatToProcess.toUpperCase().equalsIgnoreCase("READ-DATA-AND-PREVIEW")) {
							if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
								if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
									new Scene(session_Data.getContainers().get(0).getContainer_value()).
									scene_load(print_writer,session_Data.getContainers().get(0).getContainer_value());
								}
								
							}else {
								new Scene(session_Data.getContainers().get(0).getContainer_value().replace("C:", "c")).
										scene_load(print_writer,session_Data.getContainers().get(0).getContainer_value().replace("C:", "c"));
							}
						}
						
						for(int i = 1; i < session_Data.getContainers().size() ; i++) {
							if(!session_Configurations.getIpAddressEverest().trim().isEmpty() && session_Configurations.getPortNumber() != 0) {
								print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_Data.getContainers().get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
										session_Data.getContainers().get(i).getContainer_value() + ";");
							}
							
						}
						TimeUnit.SECONDS.sleep(2);
						if(whatToProcess.toUpperCase().equalsIgnoreCase("READ-DATA-AND-PREVIEW")) {
							Scene = session_Data.getContainers().get(0).getContainer_value().split("Scenes/")[1];
						}
						
						if(whatToProcess.toUpperCase().equalsIgnoreCase("PREVIEW")) {
							Scene = valueToProcess.replace(".xml", ".sum");
						}
						
						ManualFunctions.Preview(session_selected_sports,Scene, print_writer);
						
						return JSONObject.fromObject(session_Data).toString();
					}
					
				case "LOAD_SCENE":
					is_previous_data = false;
					Scene = valueToProcess;
					if(session_Configurations.getIpAddressEverest().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
						new Scene(ManualUtil.CRICKET_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + valueToProcess).
						scene_load(print_writer,ManualUtil.CRICKET_SCENE_DIRECTORY + ManualUtil.SCENES_DIRECTORY + valueToProcess);
					}else {
						new Scene("//" + session_Configurations.getIpAddressScenes() + "//" + ManualUtil.CRICKET_SCENE_DIRECTORY.replace("C:", "c") + 
								ManualUtil.SCENES_DIRECTORY + valueToProcess).scene_load(print_writer,"//" + session_Configurations.getIpAddressScenes() +
										"//" + ManualUtil.CRICKET_SCENE_DIRECTORY.replace("C:", "c") + ManualUtil.SCENES_DIRECTORY + valueToProcess);
					}
					
					break;
				}
				switch (whatToProcess.toUpperCase()) {
				case "LOAD_PREVIOUS_SCENE":
					
					if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
						session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
								new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.DATA_DIRECTORY + valueToProcess));
					}else {
						session_Data = (ContainerData)JAXBContext.newInstance(ContainerData.class).createUnmarshaller().unmarshal(
								new File("//" + session_Configurations.getIpAddressScenes() + "//" + 
										ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.DATA_DIRECTORY + valueToProcess));
					}
					
					Collections.sort(session_Data.getContainers());
					for(int i = 1; i < session_Data.getContainers().size() ; i++) {
//						if()
						print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET " + session_Data.getContainers().get(i).getContainer_key().replaceFirst((i)+"_", "") + " " + 
												session_Data.getContainers().get(i).getContainer_value() + ";");
					}
					ManualFunctions.Preview(session_selected_sports,Scene, print_writer);
					return JSONObject.fromObject(session_Data).toString();
					
				case "LOAD_DATA":
					imgdata.clear();
					if(session_Configurations.getIpAddressScenes().equalsIgnoreCase("localhost") || session_Configurations.getIpAddressScenes().equalsIgnoreCase("")) {
					//Rows and columns with unwanted tags removed
						if(valueToProcess.contains(",")) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " +valueToProcess.split(",")[1]+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " +valueToProcess.split(",")[2]+ ";");
							print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE_ACTIVE_ONLY " +
									ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE " +
									ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
						}
						
						//GetDataReturn();
//						print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE " +
//								ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE + ";");
						//print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_GET_ACTIVE_ONLY;");
						TimeUnit.SECONDS.sleep(2);
						boolean exitLoop = false; int numberOfAttempts = 5;
						List<String> allLines = new ArrayList<String>();
						while (exitLoop == false){
							if(new File(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE).exists()) {
								allLines = Files.readAllLines(Paths.get(ManualUtil.MANUAL_DIRECTORY + ManualUtil.CONTAINER_FILE));
								break;
							} else {
								TimeUnit.SECONDS.sleep(1);
								numberOfAttempts = numberOfAttempts - 1;
							}
							if(numberOfAttempts <= 0)
							{
								break;
							}
						}
						return JSONArray.fromObject(allLines).toString();
					}else {
						//Rows and columns with unwanted tags removed
						if(valueToProcess.contains(",")) {
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vRows " +valueToProcess.split(",")[1]+ ";");
							print_writer.println("LAYER1*EVEREST*TREEVIEW*Main*FUNCTION*TAG_CONTROL SET vCoumms " +valueToProcess.split(",")[2]+ ";");
							print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE_ACTIVE_ONLY " + "//" + session_Configurations.getIpAddressScenes() + "//" + 
									ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE + ";");
						}else {
							print_writer.println("LAYER1*EVEREST*GLOBAL TEMPLATE_SAVE " + "//" + session_Configurations.getIpAddressScenes() + "//" + 
									ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE + ";");
						}
						
						TimeUnit.SECONDS.sleep(2);
						boolean exitLoop = false; int numberOfAttempts = 5;
						List<String> allLines = new ArrayList<String>();
						while (exitLoop == false){
							if(new File("//" + session_Configurations.getIpAddressScenes() + "//" +
									ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE).exists()) {
								allLines = Files.readAllLines(Paths.get("//" + session_Configurations.getIpAddressScenes() + "//" + 
									ManualUtil.MANUAL_DIRECTORY.replace("C:", "c") + ManualUtil.CONTAINER_FILE));
								break;
							} else {
								TimeUnit.SECONDS.sleep(1);
								numberOfAttempts = numberOfAttempts - 1;
							}
							if(numberOfAttempts <= 0)
							{
								break;
							}
						}
						return JSONArray.fromObject(allLines).toString();
					}
				case "PREVIEW_IMAGE_DATA":
					
					TimeUnit.MILLISECONDS.sleep(1500);
				    JSONObject json = new JSONObject();
				    Path filePath = session_Configurations.getIpAddressEverest().equalsIgnoreCase("LOCALHOST") 
				        ? Paths.get("C:\\Temp\\Preview.png") 
				        : Paths.get("\\\\" + session_Configurations.getIpAddressEverest() + "\\c\\Temp\\Preview.png");

				    if (Files.exists(filePath)) {
				        json.put("file_data", Files.readAllBytes(filePath));
				        json.put("content_type", "image/PNG");
				        return json.toString();
				    }
				    return "Preview Image does not exist."; 

				}
				break;
			}
			switch (whatToProcess.toUpperCase()) {
			
			case "ANIMATE-OUT":
				//print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In CONTINUE_REVERSE;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*Out START;");
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In CONTINUE;");
				return JSONObject.fromObject(null).toString();
			case "ANIMATE-IN":
				print_writer.println("LAYER1*EVEREST*STAGE*DIRECTOR*In START;");
				return JSONObject.fromObject(session_Data).toString();
			case "CLEAR-ALL":
				print_writer.println("LAYER1*EVEREST*SINGLE_SCENE CLEAR;");
				return JSONObject.fromObject(null).toString();
			}
		
		default:
			return JSONObject.fromObject(null).toString();
		}
	}
	public static String SetData(List<Container> containers,Boolean is_previous_data,List<ImageData> imgdata,String Key,String Value) throws InterruptedException, NumberFormatException
    {
			if(imgdata.size() == 1) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 2) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 3) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 4) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 5) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 6) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 7) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(6).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 8) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(6).getImageId().equalsIgnoreCase(Key) && !imgdata.get(7).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 9) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(6).getImageId().equalsIgnoreCase(Key) && !imgdata.get(7).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(8).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}else if(imgdata.size() == 10) {
				if(!imgdata.get(0).getImageId().equalsIgnoreCase(Key) && !imgdata.get(1).getImageId().equalsIgnoreCase(Key) 
						&& !imgdata.get(2).getImageId().equalsIgnoreCase(Key) && !imgdata.get(3).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(4).getImageId().equalsIgnoreCase(Key) && !imgdata.get(5).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(6).getImageId().equalsIgnoreCase(Key) && !imgdata.get(7).getImageId().equalsIgnoreCase(Key)
						&& !imgdata.get(8).getImageId().equalsIgnoreCase(Key) && !imgdata.get(9).getImageId().equalsIgnoreCase(Key)) {
					if(is_previous_data == true) {
						for(int i = 0;i< session_Data.getContainers().size();i++) {
							if(session_Data.getContainers().get(i).getContainer_key().equalsIgnoreCase(Key)) {
								containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, session_Data.getContainers().get(i).getContainer_value()));
								break;
							}
						}
					}else {
						containers.add(new Container(Integer.valueOf(Key.split("_")[0]), Key, Value));
					}
					//break;
				}
			}
		
		return "";
		
    }
}