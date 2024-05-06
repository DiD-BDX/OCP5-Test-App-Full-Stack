// Importation des dépendances nécessaires pour les tests
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { MeComponent } from './me.component'; // Le composant à tester
import { of } from 'rxjs'; // Utilisé pour simuler les Observables

// Définition du groupe de tests pour le composant MeComponent
describe('MeComponent', () => {
  // Déclaration des variables pour le composant et les services mockés
  let component: MeComponent;
  let userServiceMock: any;
  let sessionServiceMock: any;
  let matSnackBarMock: any;
  let routerMock: any;

  // Configuration initiale avant chaque test
  beforeEach(() => {
    // Création des mocks pour les services
    userServiceMock = {
      getById: jest.fn().mockReturnValue(of({})), // Simule la méthode getById
      delete: jest.fn().mockReturnValue(of({})) // Simule la méthode delete
    };

    sessionServiceMock = {
      sessionInformation: { id: '1' }, // Simule l'information de session
      logOut: jest.fn() // Simule la méthode logOut
    };

    matSnackBarMock = {
      open: jest.fn() // Simule la méthode open
    };

    routerMock = {
      navigate: jest.fn() // Simule la méthode navigate
    };

    // Création de l'instance du composant avec les services mockés
    component = new MeComponent(routerMock, sessionServiceMock, matSnackBarMock, userServiceMock);
  });

  // Test pour vérifier que le composant est bien créé
  test('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier que la méthode ngOnInit() appelle userService.getById avec le bon argument
  test('should get user on init', () => {
    component.ngOnInit();
    expect(userServiceMock.getById).toHaveBeenCalledWith('1');
  });

  // Test pour vérifier que la méthode back() appelle bien window.history.back
  test('should go back', () => {
    const spy = jest.spyOn(window.history, 'back');
    component.back();
    expect(spy).toHaveBeenCalled();
  });

  // Test pour vérifier que la méthode logOut() de sessionService est bien appelée
  test('should log out user', () => {
    sessionServiceMock.logOut();
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
  });

  // Test pour vérifier que la méthode delete() fonctionne correctement
  test('should delete user', () => {
    component.delete();
    expect(userServiceMock.delete).toHaveBeenCalledWith('1');
    expect(matSnackBarMock.open).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });
});