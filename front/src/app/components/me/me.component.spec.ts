
import { expect } from '@jest/globals';
import { MeComponent } from './me.component';
import { of } from 'rxjs';


describe('MeComponent', () => {
  let component: MeComponent;
  let userServiceMock: any;
  let sessionServiceMock: any;
  let matSnackBarMock: any;
  let routerMock: any;

  beforeEach(() => {
    userServiceMock = {
      getById: jest.fn().mockReturnValue(of({})),
      delete: jest.fn().mockReturnValue(of({}))
    };

    sessionServiceMock = {
      sessionInformation: { id: '1' },
      logOut: jest.fn()
    };

    matSnackBarMock = {
      open: jest.fn()
    };

    routerMock = {
      navigate: jest.fn()
    };

    component = new MeComponent(routerMock, sessionServiceMock, matSnackBarMock, userServiceMock);
  });

  test('should create', () => {
    expect(component).toBeTruthy();
  });

  test('should get user on init', () => { // test d'integration
    component.ngOnInit();
    expect(userServiceMock.getById).toHaveBeenCalledWith('1');
  });

  test('should go back', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });

  test('should log out user', () => { // test d'integration
    sessionServiceMock.logOut();
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
  });

  test('should delete user', () => { // test d'integration
    component.delete();
    expect(userServiceMock.delete).toHaveBeenCalledWith('1');
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });
});