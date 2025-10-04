//package bo.com.edu.diplomado.tuSaliud.Auth.Controller;
//
//import bo.com.edu.diplomado.tuSaliud.Auth.Service.JwtService;
//import bo.com.edu.diplomado.tuSaliud.Auth.Service.UserDetailsServiceImpl;
//import bo.com.edu.diplomado.tuSaliud.Auth.Service.UserRegisterService;
//import bo.com.edu.diplomado.tuSaliud.Auth.models.AuthRequest;
//import bo.com.edu.diplomado.tuSaliud.Auth.models.AuthResponse;
//import bo.com.edu.diplomado.tuSaliud.Controller.ApiController;
//import bo.com.edu.diplomado.tuSaliud.Entity.AccountsEntity;
//import bo.com.edu.diplomado.tuSaliud.Models.Response.ApiResponse;
//import bo.com.edu.diplomado.tuSaliud.Service.AccountsService;
//import bo.com.edu.diplomado.tuSaliud.Service.GendersService;
//import bo.com.edu.diplomado.tuSaliud.Service.PersonsService;
//import bo.com.edu.diplomado.tuSaliud.Service.RolesService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController extends ApiController {
//
//    private final PersonsService personsService;
//    private final UserRegisterService userRegisterService;
//    private final AuthenticationManager authenticationManager;
//    private final UserDetailsServiceImpl userDetailsService;
//    private final JwtService jwtService;
//    private final GendersService gendersService;
//    private final RolesService rolesService;
//
//    @PostMapping("/login")
//    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(
//            @RequestBody AuthRequest request
//    ) {
//        ApiResponse<AuthResponse> response = new ApiResponse<>();
//
//        try {
//            // 1. Autenticar con Spring Security
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getPersonEmail(),
//                            request.getPersonPassword()
//                    )
//            );
//
//            // 2. Verificar que la autenticación fue exitosa
//            if (authentication.isAuthenticated()) {
//                // 3. Obtener UserDetails
//                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//                // 4. Obtener la entidad Person completa
//                AccountsEntity person = ser.findByEmail(request.getPersonEmail())
//                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//                // 5. Generar tokens
//                String jwtToken = jwtService.generateToken(userDetails, person);
//                String refreshToken = jwtService.generateRefreshToken(userDetails);
//
//                AuthResponse authResponse = AuthResponse.builder()
//                        .accessToken(jwtToken)
//                        .refreshToken(refreshToken)
//                        .build();
//
//                response.setData(authResponse);
//                response.setStatus(HttpStatus.OK.value());
//                response.setMessage("Login successful");
//                return ResponseEntity.ok(response);
//            } else {
//                throw new AuthenticationCredentialsNotFoundException("Authentication failed");
//            }
//
//        } catch (BadCredentialsException e) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.setMessage("Invalid email or password");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        } catch (Exception e) {
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.setMessage("Error during authentication: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
//
//
//}
