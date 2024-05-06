// Importation des dépendances nécessaires pour les tests
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { AppComponent } from './app.component'; // Composant à tester
import { of } from 'rxjs'; // Utilitaire RxJS pour créer des Observables

// Définition du groupe de tests pour le composant AppComponent
describe('AppComponent', () => {
  // Déclaration des variables pour le composant et les services mockés
  let component: AppComponent;
  let authServiceMock: any;
  let routerMock: any;
  let sessionServiceMock: any;

  // Configuration initiale avant chaque test
  beforeEach(() => {
    // Création des mocks pour les services
    authServiceMock = {};
    routerMock = {
      navigate: jest.fn() // Simule la méthode navigate
    };
    sessionServiceMock = {
      $isLogged: jest.fn().mockReturnValue(of(true)), // Simule la méthode $isLogged
      logOut: jest.fn() // Simule la méthode logOut
    };

    // Création d'une nouvelle instance du composant avec les services mockés
    component = new AppComponent(authServiceMock, routerMock, sessionServiceMock);
  });

  // Test pour vérifier que le composant est bien créé
  test('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test d'intégration pour vérifier que la méthode $isLogged() du composant retourne le bon statut de connexion
  test('should return logged in status', (done) => {
    component.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true); // On s'attend à ce que l'utilisateur soit connecté
      done();
    });
  });

  // Test d'intégration pour vérifier que la méthode logout() du composant déconnecte bien l'utilisateur et navigue vers la page d'accueil
  test('should log out user', () => {
    component.logout(); // Appel de la méthode à tester

    expect(sessionServiceMock.logOut).toHaveBeenCalled(); // Vérification que la méthode logOut a bien été appelée
    expect(routerMock.navigate).toHaveBeenCalledWith(['']); // Vérification que la navigation vers la page d'accueil a bien été effectuée
  });
});