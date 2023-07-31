package com.prc.sicuro.springbootApp.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.prc.sicuro.springbootApp.model.FileUpload;
import com.prc.sicuro.springbootApp.model.User;
import com.prc.sicuro.springbootApp.repository.FileuploadRepo;
import com.prc.sicuro.springbootApp.repository.UserRepository;
import com.prc.sicuro.springbootApp.service.FileuploadService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

	@Autowired
	private FileuploadService fileUploadService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private FileuploadRepo fileUploadRepo;

	@GetMapping("/login")
	public String login() {
		return "login";		
	}


	@PostMapping("/login")
	public String login(@PathParam("email") String email, @PathParam("password") String password,
			HttpServletRequest request) {

		User user= userRepo.findByEmail(email);

		if (user != null && user.getPassword().equals(password)) {
			HttpSession session = request.getSession();
			session.setAttribute("userEmail", email);
		}
		return "login";
	}



	@GetMapping("/")
	public String getAllImage(Model model) {
		List<FileUpload> list= fileUploadRepo.findAll();
		model.addAttribute("list", list);
		return "/";
	}

	@GetMapping("/FileUploadService")
	public String home() {		
		return "FileUploadService";
	}

	@PostMapping("/imageupload")
	public String imageupload(@RequestParam MultipartFile[] img, HttpSession session ) {

		User user = userRepo.findByEmail((String) session.getAttribute("userEmail"));

		List<FileUpload> uploadedFiles = new ArrayList<>();

		for (MultipartFile img1 : img) {
			FileUpload fileUpload = new FileUpload();
			fileUpload.setImagename(img1.getOriginalFilename());
			fileUpload.setUser(user);

			FileUpload savedFile = fileUploadService.saveFile(fileUpload);

			if (savedFile != null) {
				try {
					File saveFile = new ClassPathResource("static/ImageFiles").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img1.getOriginalFilename());
					Files.copy(img1.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					uploadedFiles.add(savedFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		session.removeAttribute("userEmail");
		return "redirect:/";
	}

	@PostMapping("/fileupdate")
	public String fileUpdate(@RequestParam("fileId") Integer fileId, @RequestParam("img") MultipartFile[] images) throws IOException {
		FileUpload existingFile = fileUploadRepo.findById(fileId).orElse(null);
		if (existingFile == null) {
			return "redirect:/";
		}

		File oldFile = new ClassPathResource("static/ImageFiles").getFile();
		Path oldPath = Paths.get(oldFile.getAbsolutePath() + File.separator + existingFile.getImagename());
		Files.deleteIfExists(oldPath);
		fileUploadRepo.delete(existingFile);

		User user = existingFile.getUser();
		List<FileUpload> uploadedFiles = new ArrayList<>();

		for (MultipartFile img : images) {
			FileUpload fileUpload = new FileUpload();
			fileUpload.setImagename(img.getOriginalFilename());
			fileUpload.setUser(user);

			FileUpload savedFile = fileUploadService.saveFile(fileUpload);

			if (savedFile != null) {
				try {
					File saveFile = new ClassPathResource("static/ImageFiles").getFile();
					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + img.getOriginalFilename());
					Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					uploadedFiles.add(savedFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return "redirect:/";
	}

	@PostMapping("/filedelete")
	public String deleteFile(@RequestParam("fileId") Integer fileId,RedirectAttributes redirectAttributes) {
		FileUpload fileToDelete = fileUploadRepo.findById(fileId).orElse(null);

		if (fileToDelete == null) {
			return "redirect:/filedelete"; 
		}


		try {
			File deleteFile = new ClassPathResource("static/ImageFiles").getFile();
			Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + fileToDelete.getImagename());
			Files.deleteIfExists(deletePath);
			fileUploadRepo.delete(fileToDelete);
			redirectAttributes.addFlashAttribute("success", "File deleted successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "An error occurred while deleting the file.");
		}

		return "redirect:/"; 
	}


	@GetMapping("/update/{fileId}")
	public String showUpdateForm(@PathVariable("fileId") Integer fileId, Model model) {
		// Find the file in the database based on the fileId
		FileUpload fileToUpdate = fileUploadRepo.findById(fileId).orElse(null);

		if (fileToUpdate == null) {
			return "redirect:/";
		}

		model.addAttribute("fileToUpdate", fileToUpdate);
		return "updateform"; // Assuming you have a "updateform.html" template for the update form
	}

	@PostMapping("/update")
	public String updateFile(@ModelAttribute("fileToUpdate") FileUpload updatedFile, 
			RedirectAttributes redirectAttributes) {
		FileUpload existingFile = fileUploadRepo.findById(updatedFile.getId()).orElse(null);

		if (existingFile == null) {

			redirectAttributes.addFlashAttribute("error", "File not found.");
			return "redirect:/";
		}

		existingFile.setImagename(updatedFile.getImagename());

		fileUploadService.saveFile(existingFile);

		redirectAttributes.addFlashAttribute("success", "File updated successfully.");

		return "redirect:/";
	}

	@GetMapping("/delete/{fileId}")
	public String deleteFile1(@PathVariable("fileId") Integer fileId, RedirectAttributes redirectAttributes) {
		FileUpload fileToDelete = fileUploadRepo.findById(fileId).orElse(null);

		if (fileToDelete == null) {
			redirectAttributes.addFlashAttribute("error", "File not found.");
			return "redirect:/";
		}

		try {
			File deleteFile = new ClassPathResource("static/ImageFiles").getFile();
			Path deletePath = Paths.get(deleteFile.getAbsolutePath() + File.separator + fileToDelete.getImagename());
			Files.deleteIfExists(deletePath);
			fileUploadRepo.delete(fileToDelete);

			redirectAttributes.addFlashAttribute("success", "File deleted successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "An error occurred while deleting the file.");
		}

		return "redirect:/";
	}


}
