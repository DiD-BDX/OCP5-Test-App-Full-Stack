import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify(); // Vérifiez qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  });

  test('should register user', () => { // test d'integration
    const registerRequest: RegisterRequest = { email: 'test@test.com', password: 'password', firstName: 'John', lastName: 'Doe'};
    service.register(registerRequest).subscribe();

    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(registerRequest);
  });

  test('should log in user', () => { // test d'integration
    const loginRequest: LoginRequest = { email: 'test@test.com', password: 'password' };
    service.login(loginRequest).subscribe();

    const req = httpTestingController.expectOne('api/auth/login');
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual(loginRequest);
  });
});