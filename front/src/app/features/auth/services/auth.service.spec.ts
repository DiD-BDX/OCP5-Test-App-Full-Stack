// Importation des dépendances nécessaires pour les tests
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Module pour les tests HTTP
import { TestBed } from '@angular/core/testing'; // Outils de test Angular
import { AuthService } from './auth.service'; // Service d'authentification à tester
import { LoginRequest } from '../interfaces/loginRequest.interface'; // Interface pour les requêtes de connexion
import { RegisterRequest } from '../interfaces/registerRequest.interface'; // Interface pour les requêtes d'inscription
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest

// Définition du groupe de tests pour le service AuthService
describe('AuthService', () => {
  // Déclaration des variables pour le service et le contrôleur de tests HTTP
  let service: AuthService;
  let httpTestingController: HttpTestingController;

  // Configuration initiale avant chaque test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Importation du module de tests HTTP
      providers: [AuthService] // Fourniture du service à tester
    });

    service = TestBed.inject(AuthService); // Injection du service
    httpTestingController = TestBed.inject(HttpTestingController); // Injection du contrôleur de tests HTTP
  });

  // Vérification qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  afterEach(() => {
    httpTestingController.verify();
  });

  // Test d'intégration pour vérifier que la méthode register() du service fait une requête POST à l'API avec les bonnes données
  test('should register user', () => {
    const registerRequest: RegisterRequest = { email: 'test@test.com', password: 'password', firstName: 'John', lastName: 'Doe'};
    service.register(registerRequest).subscribe(); // Appel de la méthode à tester avec les données d'inscription

    const req = httpTestingController.expectOne('api/auth/register'); // Attente d'une requête à l'URL spécifiée
    expect(req.request.method).toEqual('POST'); // Vérification que la méthode de la requête est bien POST
    expect(req.request.body).toEqual(registerRequest); // Vérification que le corps de la requête correspond aux données d'inscription
  });

  // Test d'intégration pour vérifier que la méthode login() du service fait une requête POST à l'API avec les bonnes données
  test('should log in user', () => {
    const loginRequest: LoginRequest = { email: 'test@test.com', password: 'password' };
    service.login(loginRequest).subscribe(); // Appel de la méthode à tester avec les données de connexion

    const req = httpTestingController.expectOne('api/auth/login'); // Attente d'une requête à l'URL spécifiée
    expect(req.request.method).toEqual('POST'); // Vérification que la méthode de la requête est bien POST
    expect(req.request.body).toEqual(loginRequest); // Vérification que le corps de la requête correspond aux données de connexion
  });
});