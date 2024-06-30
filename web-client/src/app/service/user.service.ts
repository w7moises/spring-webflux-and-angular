import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserLogin } from '../models/user-login';
import { Route, Router } from '@angular/router';
import { CreateUserDto } from '../models/create/create-user-dto';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient, private router: Router) { }

  login(user: UserLogin): Observable<Object> {
    return this.http.post(`${this.baseUrl}/login`, user);
  }

  register(user: CreateUserDto): Observable<Object> {
    return this.http.post(`${this.baseUrl}/ROLE_USER`, user);
  }

  getUserById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  handleToken(token: string, id: number) {
    localStorage.setItem('token', token);
    localStorage.setItem('id', id.toString());
    if (id != null) {
      this.router.navigate(['/order']);
    }
  }

  getToken() {
    return localStorage.getItem('token');
  }

  getUserId() {
    return localStorage.getItem('id');
  }
}
