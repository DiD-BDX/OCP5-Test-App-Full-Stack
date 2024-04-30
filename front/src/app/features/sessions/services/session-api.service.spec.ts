import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { SessionApiService } from './session-api.service';
import { expect } from '@jest/globals';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });

    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify(); // Vérifiez qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  });

  test('should retrieve all sessions', () => { // test d'integration
    service.all().subscribe();

    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toEqual('GET');
  });

  test('should retrieve session detail', () => { // test d'integration
    service.detail('1').subscribe();

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toEqual('GET');
  });

  test('should delete session', () => { // test d'integration
    service.delete('1').subscribe();

    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toEqual('DELETE');
  });

  test('should create session', () => { // test d'integration
    const session: Session = { 
      id: 1, 
      name: 'Test Session', 
      description: 'Test Description', 
      date: new Date(), 
      teacher_id: 1, 
      users: [] 
    };
    service.create(session).subscribe();
  
    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toEqual('POST');
  });
  
  test('should update session', () => { // test d'integration
    const session: Session = { 
      id: 1, 
      name: 'Test Session', 
      description: 'Test Description', 
      date: new Date(), 
      teacher_id: 1, 
      users: [] 
    };
    service.update('1', session).subscribe();
  
    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toEqual('PUT');
  });

  test('should participate in session', () => { // test d'integration
    service.participate('1', '1').subscribe();

    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toEqual('POST');
  });

  test('should unparticipate in session', () => { // test d'integration
    service.unParticipate('1', '1').subscribe();

    const req = httpTestingController.expectOne('api/session/1/participate/1');
    expect(req.request.method).toEqual('DELETE');
  });
});