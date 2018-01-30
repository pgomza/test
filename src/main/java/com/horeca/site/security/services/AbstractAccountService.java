package com.horeca.site.security.services;

import com.horeca.site.exceptions.ResourceNotFoundException;
import com.horeca.site.security.models.AbstractAccount;
import com.horeca.utils.PageableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractAccountService<T extends AbstractAccount> {

    final static String PASSWORD_REGEX = "[^\\s]{5,}";

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenStore tokenStore;

    abstract protected PagingAndSortingRepository<T, String> getRepository();

    abstract protected String loginToUsername(String login);

    abstract protected String getOAuthClientId();

    public boolean exists(String login) {
        return loginService.exists(loginToUsername(login));
    }

    public List<T> getAll() {
        List<T> result = new ArrayList<>();
        getRepository().findAll().forEach(result::add);
        return result;
    }

    public Page<T> getAll(Pageable pageable) {
        // TODO make the repository of each subclass implement a method that returns the total count
        return PageableUtils.extractPage(getAll(), pageable);
    }

    public T get(String login) {
        T account = getRepository().findOne(loginToUsername(login));
        if (account == null) {
            throw new ResourceNotFoundException();
        }
        return account;
    }

    public T save(T account) {
        return getRepository().save(account);
    }

    public void delete(String login) {
        if (!exists(login)) {
            throw new ResourceNotFoundException();
        }
        getRepository().delete(loginToUsername(login));
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(
                getOAuthClientId(), loginToUsername(login)
        );
        tokens.forEach(tokenStore::removeAccessToken);
    }

    public Map<String, String> getProfileData(String login) {
        return get(login).getProfileData();
    }

    public Map<String, String> updateProfileData(String login, Map<String, String> updated) {
        T account = get(login);
        account.getProfileData().clear();
        account.getProfileData().putAll(updated);
        return save(account).getProfileData();
    }

    public T getFromAuthentication(Authentication authentication, Class<T> clazz) {
        Object principal = authentication.getPrincipal();
        if (clazz.isAssignableFrom(principal.getClass())) {
            T account = (T) principal;
            // this is necessary because the instance obtained from 'authentication' isn't fully initialized
            return get(account.getLogin());
        }
        else {
            throw new AccessDeniedException("Access denied");
        }
    }

    void disable(T account) {
        account.setEnabled(false);
        save(account);
    }

    void enable(T account) {
        account.setEnabled(true);
        save(account);
    }
}
