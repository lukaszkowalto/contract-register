package controlling.contractRegister.web;

import controlling.contractRegister.model.User;
import controlling.contractRegister.model.VerificationToken;
import controlling.contractRegister.pagination.PageSize;
import controlling.contractRegister.service.UserService;
import controlling.contractRegister.service.VerificationTokenService;
import controlling.contractRegister.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private VerificationTokenService verificationTokenService;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userForm", new UserDTO());
        return "security/registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String register(@ModelAttribute("userForm") @Valid UserDTO userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "security/registration";
        }
        if (userService.getUserByEmail(userForm.getEmail()) != null) {
            model.addAttribute("paramPair", Pair.of(2, messageSource.getMessage("registration.email.already.exist", null, LocaleContextHolder.getLocale())));
            return "security/registration-status";
        }
        userService.save(userForm);
        try {
            verificationTokenService.save(userForm);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: " + e.getMessage());
        }
        model.addAttribute("paramPair", Pair.of(1, userForm.getEmail()));
        return "security/registration-status";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginDTO());
        return "security/login";
    }

    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") UUID confirmationToken) {
        VerificationToken token = verificationTokenService.getVerificationTokenByToken(confirmationToken);

        if (token != null && token.isTokenValid()) {
            User user = userService.getUserByEmail(token.getUser().getEmail());
            user.setEmailVerified(true);
            userService.update(user);
            modelAndView.addObject("paramPair", Pair.of(3, messageSource.getMessage("registration.verification.email.success", null, LocaleContextHolder.getLocale())));
        } else {
            modelAndView.addObject("paramPair", Pair.of(0, messageSource.getMessage("registration.token.invalid", null, LocaleContextHolder.getLocale())));
        }
        modelAndView.setViewName("security/registration-status");
        return modelAndView;
    }

    @GetMapping(value = "/users")
    public String listUsers(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                            @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("users", userService.getPage(pageNumber, pageSize));
        return "select-user";
    }

    @GetMapping(value = "/delete-user")
    public String deleteUser(@RequestParam("id") Integer id) {
        if (userService.existsUserById(id)) {
            userService.deleteUserById(id);
        }
        return "redirect:/users";
    }

    @PostMapping(value = "/users")
    public String filterUsers(@RequestParam(value = "pattern", required = false) String pattern,
                              @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                              @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("users", userService.getPageFiltered(StringUtil.replaceNullWithEmptyString(pattern), pageNumber, pageSize));
        return "select-user";
    }

    @PostMapping(value = "/update-user")
    public String updateUser(
            @ModelAttribute("userForm") UserRoleDTO userRoleDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR: " +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        userService.setRoleAndActivateAccount(userRoleDTO);
        return "redirect:/users";
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }
}