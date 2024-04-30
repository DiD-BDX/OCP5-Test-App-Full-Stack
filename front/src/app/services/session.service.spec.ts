import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    service = new SessionService();
  });

  test('should create', () => {
    expect(service).toBeTruthy();
  });

  test('should return logged in status', (done) => {
    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false);
      done();
    });
  });

  test('should log in user', (done) => {
    const user: SessionInformation = { id: 1, username: 'Test User', admin: true, token: 'mock', type: 'mock', firstName: 'Test', lastName: 'User' };
    service.logIn(user);

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(user);
      done();
    });
  });

  test('should log out user', (done) => {
    service.logOut();

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(false);
      expect(service.sessionInformation).toBeUndefined();
      done();
    });
  });
});
