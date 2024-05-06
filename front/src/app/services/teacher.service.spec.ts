// Importation des dépendances nécessaires pour les tests
import { TestBed } from '@angular/core/testing'; // Outils de test Angular
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { TeacherService } from './teacher.service'; // Service pour la gestion des enseignants
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Module pour les tests HTTP

// Définition du groupe de tests pour le service TeacherService
describe('TeacherService', () => {
  // Déclaration des variables pour le service et le contrôleur de tests HTTP
  let service: TeacherService;
  let httpTestingController: HttpTestingController;

  // Configuration initiale avant chaque test
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // Importation du module de tests HTTP
      providers: [TeacherService] // Fourniture du service à tester
    });

    service = TestBed.inject(TeacherService); // Injection du service
    httpTestingController = TestBed.inject(HttpTestingController); // Injection du contrôleur de tests HTTP
  });

  // Vérification qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  afterEach(() => {
    httpTestingController.verify();
  });

  // Test d'intégration pour vérifier que la méthode all() du service fait une requête GET à l'API
  test('should retrieve all teachers', () => {
    service.all().subscribe(); // Appel de la méthode à tester

    const req = httpTestingController.expectOne('api/teacher'); // Attente d'une requête à l'URL spécifiée
    expect(req.request.method).toEqual('GET'); // Vérification que la méthode de la requête est bien GET
  });

  // Test d'intégration pour vérifier que la méthode detail() du service fait une requête GET à l'API avec le bon ID
  test('should retrieve teacher detail', () => {
    service.detail('1').subscribe(); // Appel de la méthode à tester avec l'ID '1'

    const req = httpTestingController.expectOne('api/teacher/1'); // Attente d'une requête à l'URL spécifiée
    expect(req.request.method).toEqual('GET'); // Vérification que la méthode de la requête est bien GET
  });
});