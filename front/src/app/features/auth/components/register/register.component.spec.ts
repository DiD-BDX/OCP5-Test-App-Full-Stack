
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';

import { expect } from '@jest/globals';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    const authServiceMock = {
      register: jest.fn().mockReturnValue(of({}))
    };

    const routerMock = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      imports: [ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should call register on the AuthService when submit is called', () => { // test d'integration
    // Arrange
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');
    component.form.controls['password'].setValue('password');
    // Act
    component.submit();
    // Assert
    expect(authService.register).toHaveBeenCalled();
  });

  test('should navigate to /login when register is successful', () => { // test d'integration
    // Arrange
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');
    component.form.controls['password'].setValue('password');
    // Act
    component.submit();
    // Assert
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  test('should set onError to true when register fails', () => { // test d'integration
    // Arrange
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('error')));
    component.form.controls['email'].setValue('test@test.com');
    component.form.controls['firstName'].setValue('Test');
    component.form.controls['lastName'].setValue('User');
    component.form.controls['password'].setValue('password');
    // Act
    component.submit();
    // Assert
    expect(component.onError).toBe(true);
  });
});
