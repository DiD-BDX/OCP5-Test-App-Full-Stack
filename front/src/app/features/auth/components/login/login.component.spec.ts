import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
//import { MatLegacyCardModule as MatCardModule } from '@angular/material/legacy-card';
import { MatCardModule } from '@angular/material/card';
//import { MatLegacyFormFieldModule as MatFormFieldModule } from '@angular/material/legacy-form-field';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
//import { MatLegacyInputModule as MatInputModule } from '@angular/material/legacy-input';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    const authServiceMock = {
      login: jest.fn().mockReturnValue(of({}))
    };

    const sessionServiceMock = {
      logIn: jest.fn()
    };

    const routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call login on the AuthService when submit is called', () => {
    // Arrange
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['password'].setValue('password');

    // Act
    component.submit();

    // Assert
    expect(authService.login).toHaveBeenCalled();
  });

  it('should call logIn on the SessionService when login is successful', () => {
    // Arrange
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['password'].setValue('password');

    // Act
    component.submit();

    // Assert
    expect(sessionService.logIn).toHaveBeenCalled();
  });

  it('should navigate to /sessions when login is successful', () => {
    // Arrange
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['password'].setValue('password');

    // Act
    component.submit();

    // Assert
    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should set onError to true when login fails', () => {
    // Arrange
    authService.login = jest.fn().mockReturnValue(throwError(() => new Error('error')));
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['password'].setValue('password');

    // Act
    component.submit();

    // Assert
    expect(component.onError).toBe(true);
  });

  it('should set email field as invalid when it is empty', () => {
    // Arrange
    component.form.controls['email'].setValue('');

    // Act
    fixture.detectChanges();

    // Assert
    expect(component.form.controls['email'].invalid).toBe(true);
  });

  it('should set password field as invalid when it is empty', () => {
    // Arrange
    component.form.controls['password'].setValue('');

    // Act
    fixture.detectChanges();

    // Assert
    expect(component.form.controls['password'].invalid).toBe(true);
  });
  
});
