package com.spring.security.service.oauth2.security;

import com.spring.security.entity.User;
import com.spring.security.exception.BaseException;
import com.spring.security.repository.RoleRepository;
import com.spring.security.repository.UserRepository;
import com.spring.security.service.oauth2.Oauth2UserDetailFactory;
import com.spring.security.service.oauth2.Oauth2UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.ObjectUtils;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Custom0Auth2UserDetailService extends DefaultOAuth2UserService {
private final UserRepository userRepository;

private final RoleRepository roleRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
    try {

        return checkingOAuth2User(userRequest,oAuth2User);
    }catch  (AuthenticationException e) {
    throw e;
        }catch (Exception ex){
        throw new InternalAuthenticationServiceException(ex.getMessage(),ex.getCause());
    }
}
private OAuth2User checkingOAuth2User (OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User){
    Oauth2UserDetails oauth2UserDetails = Oauth2UserDetailFactory.getOauth2UserDetail(oAuth2UserRequest.getClientRegistration().getRegistrationId(),
            oAuth2User.getAttributes());
    if (ObjectUtils.isEmpty(oauth2UserDetails)){
        throw new BaseException("400", "No se encontro las propiedades de oauth2");
    }
    Optional<User> user = userRepository.findByUsernameAndProviderId(oauth2UserDetails.getEmail(), oAuth2UserRequest.getClientRegistration().getRegistrationId());
    User userDetail;
    if(user.isPresent()){
        userDetail = user.get();
        if (!userDetail.getProviderId().equals(oAuth2UserRequest.getClientRegistration().getRegistrationId())){
            throw new BaseException("400","Sitio invalido de login"+ userDetail.getProviderId());
        }
       userDetail = updateOauth2UserDetail(userDetail,oauth2UserDetails);

    }else{
        userDetail =registerNewOauth2UserDetail(oAuth2UserRequest, oauth2UserDetails);
    }
    return new Oauth2UserDetailCustom(userDetail.getId(), userDetail.getUsername(),userDetail.getPassword(), userDetail.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList()));
    }
    public User registerNewOauth2UserDetail(OAuth2UserRequest oAuth2UserRequest, Oauth2UserDetails oauth2UserDetails){
    User user = new User();
    user.setUsername(oauth2UserDetails.getEmail());
    user.setProviderId(oAuth2UserRequest.getClientRegistration().getRegistrationId());
    user.setEnabled(true);
    user.setCredentialsNonExpired(true);
    user.setAccountNonLocked(true);
    user.setRoles(new HashSet<>());
    user.getRoles().add(roleRepository.findByName("USER"));
    return userRepository.save(user);
    }
    public User updateOauth2UserDetail(User user, Oauth2UserDetails oauth2UserDetails){
    user.setUsername(oauth2UserDetails.getEmail());
    return userRepository.save(user);

    }

}

