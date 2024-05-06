// Importation des dépendances nécessaires pour les tests
import { ComponentFixture, TestBed } from '@angular/core/testing'; // Outils de test Angular
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { of } from 'rxjs'; // Utilitaire RxJS pour créer des Observables
import { SessionService } from 'src/app/services/session.service'; // Service pour la gestion des sessions
import { SessionApiService } from '../../services/session-api.service'; // Service pour l'API des sessions
import { ListComponent } from './list.component'; // Le composant à tester
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface'; // Interface pour les informations de session

// Définition du groupe de tests pour le composant ListComponent
describe('ListComponent', () => {
  // Déclaration des variables pour le composant et son environnement de test
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;

  // Configuration initiale avant chaque test
  beforeEach(async () => {
    // Création des mocks pour les services
    const sessionInformation: SessionInformation = {
      id: 1,
      token: 'mock-token',
      type: 'mock-type',
      username: 'mock-username',
      firstName: 'mock-firstName',
      lastName: 'mock-lastName',
      admin: true,
    };

    const sessionServiceMock = {
      sessionInformation: sessionInformation
    };

    const mockSessions = [
      {
        id: 1,
        name: 'mock-name',
        description: 'mock-description',
        date: new Date(),
        teacher_id: 1,
        users: [1],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];

    const sessionApiServiceMock = {
      all: jest.fn().mockReturnValue(of(mockSessions))
    };

    // Configuration du module de test avec le composant à tester et les services mockés
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock }
      ]
    })
      .compileComponents(); // Compilation des composants

    // Création de l'environnement de test pour le composant
    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService); // Injection du service SessionService
    sessionApiService = TestBed.inject(SessionApiService); // Injection du service SessionApiService
    fixture.detectChanges(); // Déclenchement de la détection de changements
  });

  // Test pour vérifier que le composant est bien créé
  test('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier que la méthode all() de SessionApiService est appelée lors de la création du composant
  test('should call all on the SessionApiService when component is created', () => {
    expect(sessionApiService.all).toHaveBeenCalled();
  });

  // Test pour vérifier que les informations de session sont récupérées depuis SessionService
  test('should get sessionInformation from SessionService', () => {
    expect(component.user).toBe(sessionService.sessionInformation);
  });

  // Test pour vérifier que les boutons "create" et "update" sont affichés pour un utilisateur administrateur
  test('should show "create" and "update" buttons for admin user', () => {
    // Configuration du test
    if (sessionService.sessionInformation) {
      sessionService.sessionInformation.admin = true;
    }
    // Exécution du test
    fixture.detectChanges();
    // Vérification du résultat
    const createButton = fixture.debugElement.nativeElement.querySelector('[data-testid="create-button"]');
    const updateButton = fixture.debugElement.nativeElement.querySelector('[data-testid="update-button"]');
    expect(createButton).not.toBeNull();
    expect(updateButton).not.toBeNull();
  });

  // Test pour vérifier que les boutons "create" et "update" ne sont pas affichés pour un utilisateur non administrateur
  test('should not show "create" and "update" buttons for non-admin user', () => {
    // Configuration du test
    if (sessionService.sessionInformation) {
      sessionService.sessionInformation.admin = false;
    }
    // Exécution du test
    fixture.detectChanges();
    // Vérification du résultat
    const createButton = fixture.debugElement.nativeElement.querySelector('[data-testid="create-button"]');
    const updateButton = fixture.debugElement.nativeElement.querySelector('[data-testid="update-button"]');
    expect(createButton).toBeNull();
    expect(updateButton).toBeNull();
  });
});