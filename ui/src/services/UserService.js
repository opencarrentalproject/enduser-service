import http from '../utils/http-common';

class UserService {

    getAll() {
        return http.get("/users");
    }

    get(id) {
        return http.get(`/users/${id}`);
    }

    create(data) {
        return http.post("/users", data);
    }

    update(id, data) {
        return http.patch(`/users/${id}`, data);
    }

    delete(id) {
        return http.delete(`/users/${id}`);
    }
}

export default new UserService();