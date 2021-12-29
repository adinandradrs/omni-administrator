package id.codefun.omni.administrator.service.user;

import id.codefun.service.util.CodefunConstants;
import id.codefun.omni.administrator.repository.UserRepository;
import id.codefun.omni.administrator.util.UserPrinciple;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserSecurityService implements UserDetailsService {

	private UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

	@Override
	public UserDetails loadUserByUsername(String email) {
		return userRepository.findByEmail(email).map(data -> UserPrinciple.build(data)).orElseThrow(()->{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, CodefunConstants.ERR_MSG_UNAUTHORIZED);
        });
	}
    
}