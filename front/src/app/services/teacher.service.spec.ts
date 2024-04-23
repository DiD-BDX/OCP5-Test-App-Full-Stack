import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService]
    });

    service = TestBed.inject(TeacherService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify(); // Vérifiez qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  });

  test('should retrieve all teachers', () => {
    service.all().subscribe();

    const req = httpTestingController.expectOne('api/teacher');
    expect(req.request.method).toEqual('GET');
  });

  test('should retrieve teacher detail', () => {
    service.detail('1').subscribe();

    const req = httpTestingController.expectOne('api/teacher/1');
    expect(req.request.method).toEqual('GET');
  });
});
