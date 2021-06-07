/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.usermanagement.owner;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.usermanagement.kafka.KafkaMessageProducer;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/*
 * Clase controladora Usuario
 */
@Controller
class UserController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "users/createOrUpdateUserForm";

	private final UserRepository users;

	@Autowired
	KafkaMessageProducer producer;

	public UserController(UserRepository clinicService) {
		this.users = clinicService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/users/new")
	public String initCreationForm(Map<String, Object> model) {
		User user = new User();
		model.put("user", user);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/users/new")
	public String processCreationForm(@Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.users.save(user);
			// Datos para Kafka
			System.out.println("Nuevo usuario");
			producer.sendMessage(user.getAplicacion(), user.getSilcon());
			System.out.println(
					user.getNombre() + user.getCentro() + user.getApellido() + user.getSilcon() + user.getAplicacion());
			return "redirect:/users/" + user.getId();
		}
	}

	@GetMapping("/users/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("user", new User());
		return "users/findUsers";
	}

	@GetMapping("/users")
	public String processFindForm(User user, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /users to return all records
		if (user.getSilcon() == null) {
			user.setSilcon(""); // empty string signifies broadest possible search
		}

		// find users by last name
		Collection<User> results = this.users.findBySilcon(user.getSilcon());
		if (results.isEmpty()) {
			// no users found
			result.rejectValue("silcon", "notFound", "not found");
			return "users/findUsers";
		}
		else if (results.size() == 1) {
			// 1 user found
			user = results.iterator().next();
			return "redirect:/users/" + user.getId();
		}
		else {
			// multiple users found
			model.put("selections", results);
			return "users/usersList";
		}
	}

	@GetMapping("/users/{userId}/edit")
	public String initUpdateUserForm(@PathVariable("userId") int userId, Model model) {
		User user = this.users.findById(userId);
		model.addAttribute(user);
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/users/{userId}/edit")
	public String processUpdateUserForm(@Valid User user, BindingResult result, @PathVariable("userId") int userId) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		else {
			user.setId(userId);
			this.users.save(user);
			// Datos para kafka
			System.out.println("Modificando");
			producer.sendMessage(user.getAplicacion(), user.getSilcon());
			System.out.println(
					user.getNombre() + user.getCentro() + user.getApellido() + user.getSilcon() + user.getAplicacion());

			return "redirect:/users/{userId}";
		}
	}

	/**
	 * Custom handler for displaying an user.
	 * @param userId the ID of the user to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/users/{userId}")
	public ModelAndView showUser(@PathVariable("userId") int userId) {
		ModelAndView mav = new ModelAndView("users/userDetails");
		User user = this.users.findById(userId);
		mav.addObject(user);
		return mav;
	}

	@GetMapping("/users/{userId}/delete")
	public String deleteUser(@PathVariable("userId") int userId, Model model) {
		User user2 = this.users.findById(userId);
		model.addAttribute(user2);

		this.users.deleteById(userId);
		// Datos para Kafka
		System.out.println("Borrando");
		producer.sendMessage(user2.getAplicacion(), user2.getSilcon());
		System.out.println(user2.getNombre() + user2.getCentro() + user2.getApellido() + user2.getSilcon()
				+ user2.getAplicacion());
		User user = new User();
		user.setSilcon("");
		return "redirect:/users/";
	}

}
