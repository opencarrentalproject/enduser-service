import http from '../utils/http-common';

class AuthService {

    login(username, password) {
        const data = {username, password}
        return http.post(`/admin/login`, data);
    }
}

export default new AuthService();