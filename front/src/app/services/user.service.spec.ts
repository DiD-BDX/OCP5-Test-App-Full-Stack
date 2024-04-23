import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { UserService } from './user.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });

    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify(); // Vérifiez qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  });

  test('should retrieve user by id', () => { // test d'integration
    service.getById('1').subscribe();

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toEqual('GET');
  });

  test('should delete user', () => { // test d'integration
    service.delete('1').subscribe();

    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toEqual('DELETE');
  });
});
