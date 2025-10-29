package eu.pmant.app.session;

import eu.pmant.app.dto.SessionData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionDataProvider {

    private final HttpSession session;

    public SessionData getSessionData() {
        return (SessionData) session.getAttribute("sessionData");
    }

    public HttpSession createSession(HttpServletRequest httpRequest) {
        return httpRequest.getSession(true);
    }

    public boolean hasActiveSession() {
        return session != null && session.getAttribute("sessionData") != null;
    }
}
