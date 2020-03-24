package com.neymar.dating.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.aspectj.bridge.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.neymar.dating.exception.ResourceNotFoundException;
import com.neymar.dating.model.User;
import com.neymar.dating.payload.ApiResponse;
import com.neymar.dating.payload.SignUpRequest;
import com.neymar.dating.payload.UserIdentityAvailability;
import com.neymar.dating.payload.UserProfile;
import com.neymar.dating.payload.UserSummary;
import com.neymar.dating.payload.VerificationForm;
import com.neymar.dating.respository.UserRepository;
import com.neymar.dating.security.CurrentUser;
import com.neymar.dating.security.UserPrincipal;
import com.neymar.dating.service.VerificationTokenService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationTokenService verificationTokenService;

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@GetMapping("/user/me")
	@PreAuthorize("hasRole('USER')")
	public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
		UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(),
				currentUser.getName());
		return userSummary;
	}

	@GetMapping("/user/checkUsernameAvailability")
	public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
		Boolean isAvailable = !userRepository.existsByUsername(username);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/user/checkEmailAvailability")
	public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
		Boolean isAvailable = !userRepository.existsByEmail(email);
		return new UserIdentityAvailability(isAvailable);
	}

	@GetMapping("/users/{username}")
	public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

		UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getAvatar(),
				user.getCountry(), user.getProvince(), user.getDescription(), user.getCreatedAt());

		return userProfile;
	}

	@GetMapping("/users/email-verification")
	public String formGet(Model model) {
		model.addAttribute("verificationForm", new VerificationForm());
		return "verification-form";
	}

	@PostMapping("/users/email-verification")
	public String formPost(@Valid VerificationForm verificationForm, BindingResult bindingResult, Model model) {
		if (!bindingResult.hasErrors()) {
			model.addAttribute("noErrors", true);
		}
		model.addAttribute("verificationForm", verificationForm);

		verificationTokenService.createVerification(verificationForm.getEmail());
		return "verification-form";
	}

	@GetMapping("/users/verify-email")
	@ResponseBody
	public String verifyEmail(String code) {
		return verificationTokenService.verifyEmail(code).getBody();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PutMapping("/users/edit")
	public ResponseEntity<User> editUser(@Valid @RequestBody UserProfile userProfile) {
		Optional<User> user = userRepository.findById(userProfile.getId());
		User editUser = new User();
		if (user.isPresent()) {
			editUser = user.get();
			editUser.setAvatar(userProfile.getAvatar());
			editUser.setCountry(userProfile.getCountry());
			editUser.setProvince(userProfile.getProvince());
			editUser.setDescription(userProfile.getDescription());
			userRepository.save(editUser);
		} else {
			return new ResponseEntity(new ApiResponse(false, "UserName is not exist"), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<User>(editUser, HttpStatus.OK);
	}
}
