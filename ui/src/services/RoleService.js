import http from '../utils/http-common';

class RoleService {

    getAll() {
        return http.get("/roles");
    }

    get(id) {
        return http.get(`/roles/${id}`);
    }

    create(data) {
        return http.post("/roles", data);
    }

    update(id, data) {
        return http.patch(`/roles/${id}`, data);
    }

    delete(id) {
        return http.delete(`/roles/${id}`);
    }
}

export default new RoleService()