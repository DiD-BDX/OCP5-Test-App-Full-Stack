// Importation des dépendances nécessaires pour les tests
import { TestBed } from '@angular/core/testing'; // Outils de test Angular
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { UserService } from './user.service'; // Service pour la gestion des utilisateurs
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Module pour les tests HTTP

// Définition du groupe de tests pour le service UserService
describe('UserService', () => {
  // Déclaration des variables pour le service et le contrôleur de tests HTTP
  let service: UserService;
  let httpTestingController: HttpTestingController;

  // Configuration initiale avant chaque test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Importation du module de tests HTTP
      providers: [UserService] // Fourniture du service à tester
    });

    service = TestBed.inject(UserService); // Injection du service
    httpTestingController = TestBed.inject(HttpTestingController); // Injection du contrôleur de tests HTTP
  });

  // Vérification qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  afterEach(() => {
    httpTestingController.verify();
  });

  // Test d'intégration pour vérifier que la méthode getById() du service fait une requête GET à l'API avec le bon ID
  test('should retrieve user by id', () => {
    service.getById('1').subscribe(); // Appel de la méthode à tester avec l'ID '1'

    const req = httpTestingController.expectOne('api/user/1'); // Attente d'une requête à l'URL spécifiée
    expect(req.request.method).toEqual('GET'); // Vérification que la méthode de la requête est bien GET
  });

  // Test d'intégration pour vérifier que la méthode delete() du service fait une requête DELETE à l'API avec le bon ID
  test('should delete user', () => {
    service.delete('1').subscribe(); // Appel de la méthode à tester avec l'ID '1'

    const req = httpTestingController.expectOne('api/user/1'); // Attente d'une requête à l'URL spécifiée
    expect(req.request.method).toEqual('DELETE'); // Vérification que la méthode de la requête est bien DELETE
  });
});