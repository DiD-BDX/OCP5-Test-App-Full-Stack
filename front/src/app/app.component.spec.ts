import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { of } from 'rxjs';

describe('AppComponent', () => {
  let component: AppComponent;
  let authServiceMock: any;
  let routerMock: any;
  let sessionServiceMock: any;

  beforeEach(() => {
    authServiceMock = {};
    routerMock = {
      navigate: jest.fn()
    };
    sessionServiceMock = {
      $isLogged: jest.fn().mockReturnValue(of(true)),
      logOut: jest.fn()
    };

    component = new AppComponent(authServiceMock, routerMock, sessionServiceMock);
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should return logged in status', (done) => {
    component.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
      done();
    });
  });

  test('should log out user', () => {
    component.logout();

    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['']);
  });
});
