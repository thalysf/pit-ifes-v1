import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import { take } from 'rxjs';
import { environment } from 'src/environments/environment';


@Injectable({
    providedIn: 'root'
})
export class ApiService {

    private readonly API = environment.API;

    constructor(private http: HttpClient){
    }

    get(url: string, params = {}) {
        return this.http.get(`${this.API}/${url}`, {
            params: params
        });
    }

    detail(url: string, id: string) {
        return this.http.get(`${this.API}/${url}/${id}`).pipe(take(1));
    }

    delete(url: string, id: string) {
        return this.http.delete(`${this.API}/${url}/${id}`);
    }

    post(url: string, entity: any, params = {}) {
        return this.http.post(`${this.API}/${url}`, entity, {
            params: params
        });
    }

    put(url: string, id: string, entity: any ) {
        return this.http.put(`${this.API}/${url}/${id}`, entity);
    }

    patch(url: string, entity: any ) {
        return this.http.patch(`${this.API}/${url}`, entity);
    }

    getFile(url: string, params = {}){
        return this.http.get(`${this.API}/${url}`, {
            responseType: 'blob',
            params: params
        });
    }

    postFile(url: string, formData: FormData) {
        return this.http.post(`${this.API}/${url}`, formData, {
            responseType: 'text',
        });
    }
}
