import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../service/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  registerForm: FormGroup;
  userLogin = true;
  userRegister = false;

  constructor(private formBuilder: FormBuilder, private userService: UserService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      firstname: ['', Validators.required],
      lastname: ['', Validators.required]
    });
  }
  
  get loginControls() {
    return this.loginForm.controls;
  }

  get registerControls() {
    return this.registerForm.controls;
  }

  user_register() {
    this.loginForm.reset();
    this.userLogin = false;
    this.userRegister = true;
  }
  user_login() {
    this.registerForm.reset();
    this.userLogin = true;
    this.userRegister = false;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }
    this.userService.login(this.loginForm.value).subscribe(data => {
      this.userService.handleToken(data['token'], data['id']);
    }, (error: HttpErrorResponse) => {
      if (error.status === 404) {
        this.snackBar.open('Usuario no existente', 'Close', {
          duration: 2000,
        });
      } else {
        this.snackBar.open('Usuario o Contraseña incorrecta', 'Close', {
          duration: 2000,
        });
      }
    });
  }

  onRegister() {
    if (this.registerForm.invalid) {
      return;
    }
    this.userService.register(this.registerForm.value).subscribe(data => {
      this.user_login();
      this.snackBar.open('Usuario Registrado', 'Close', {
        duration: 2000,
      });
    }, (error: HttpErrorResponse) => {
      if (error.status === 302) {
        const errorMessage = error.error.message;
        this.snackBar.open(errorMessage, 'Close', {
          duration: 2000,
        });

      } else {
        this.snackBar.open("Correo ya registrado", 'Close', {
          duration: 2000,
        });
      }
    });
  }

}
