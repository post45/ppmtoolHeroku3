package com.anabiullin.ppmtool3.web;

import com.anabiullin.ppmtool3.domain.User;
import com.anabiullin.ppmtool3.payload.JWTLoginSucessReponse;
import com.anabiullin.ppmtool3.payload.LoginRequest;
import com.anabiullin.ppmtool3.security.JwtTokenProvider;
import com.anabiullin.ppmtool3.services.MapValidationErrorService;
import com.anabiullin.ppmtool3.services.UserService;
import com.anabiullin.ppmtool3.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.anabiullin.ppmtool3.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?>authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?>errorMap = mapValidationErrorService.MapValidationService(result);
        if (errorMap !=null)return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
    }



    @PostMapping("register")
    public ResponseEntity<?>register(@Valid @RequestBody User user, BindingResult result){

        userValidator.validate(user, result);

      ResponseEntity<?> errorMap  = mapValidationErrorService.MapValidationService(result);
      if (errorMap !=null)return errorMap;

      User newUser = userService.saveUser(user);
      return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
}

//original  web: java $JAVA_OPTS -jar target/ppmtool3-0.0.1-SNAPSHOT.jar -Dserver.port=$PORT $JAR_OPTS
//web: java -Dserver.port=$PORT $JAVA_OPTS -jar target/ppmtool3-0.0.1-SNAPSHOT.jar