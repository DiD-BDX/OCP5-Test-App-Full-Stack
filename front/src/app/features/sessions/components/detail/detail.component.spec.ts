// Importation des dépendances nécessaires pour les tests
import { ComponentFixture, TestBed } from '@angular/core/testing'; // Outils de test Angular
import { MatSnackBar } from '@angular/material/snack-bar'; // Service pour les snack-bars
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { SessionService } from '../../../../services/session.service'; // Service pour la gestion des sessions
import { SessionApiService } from '../../services/session-api.service'; // Service pour l'API des sessions
import { TeacherService } from '../../../../services/teacher.service'; // Service pour la gestion des enseignants
import { ActivatedRoute, Router } from '@angular/router'; // Services pour le routage
import { of } from 'rxjs'; // Utilitaire RxJS pour créer des Observables
import { By } from '@angular/platform-browser'; // Utilitaire pour la sélection d'éléments dans le DOM

import { DetailComponent } from './detail.component'; // Le composant à tester
import { Teacher } from 'src/app/interfaces/teacher.interface'; // Interface pour les enseignants

// Définition du groupe de tests pour le composant DetailComponent
describe('DetailComponent', () => {
  // Déclaration des variables pour le composant et son environnement de test
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionService: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let matSnackBar: MatSnackBar;
  let router: Router;
  let teacherId: number;
  let teacher: Teacher;

  // Configuration initiale avant chaque test
  beforeEach(async () => {
    // Création des mocks pour les services
    const sessionServiceMock = {
      sessionInformation: {
        id: 1,
        token: 'mock-token',
        type: 'mock-type',
        username: 'mock-username',
        firstName: 'mock-firstName',
        lastName: 'mock-lastName',
        admin: true,
      }
    };

    const mockSession = { 
      id: 1,
      name: 'mock-name',
      description: 'mock-description',
      date: new Date(),
      teacher_id: 1,
      users: [1],
      createdAt: new Date(),
      updatedAt: new Date()
    };

    teacher = { id: 1, firstName: 'John', lastName: 'Doe', createdAt: new Date(), updatedAt: new Date()};
    teacherId = 1;
    mockSession.teacher_id = teacherId;

    const sessionApiServiceMock = {
      detail: jest.fn().mockReturnValue(of(mockSession)),
      delete: jest.fn().mockReturnValue(of({})),
      participate: jest.fn().mockReturnValue(of({})),
      unParticipate: jest.fn().mockReturnValue(of({}))
    };

    const teacherServiceMock = {
      detail: jest.fn().mockReturnValue(of(teacher))
    };

    const matSnackBarMock = {
      open: jest.fn()
    };

    const routerMock = {
      navigate: jest.fn()
    };

    const activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1')
        }
      }
    };

    // Configuration du module de test avec le composant à tester et les services mockés
    await TestBed.configureTestingModule({
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: sessionServiceMock },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: TeacherService, useValue: teacherServiceMock },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock },
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    })
      .compileComponents(); // Compilation des composants

    // Création de l'environnement de test pour le composant
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance; // Instance du composant
    sessionService = TestBed.inject(SessionService); // Injection du service SessionService
    sessionApiService = TestBed.inject(SessionApiService); // Injection du service SessionApiService
    teacherService = TestBed.inject(TeacherService); // Injection du service TeacherService
    matSnackBar = TestBed.inject(MatSnackBar); // Injection du service MatSnackBar
    router = TestBed.inject(Router); // Injection du service Router
    fixture.detectChanges(); // Déclenchement de la détection de changements
  });

  // Test pour vérifier que le composant est bien créé
  test('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test pour vérifier que la méthode detail() de SessionApiService est appelée lors de l'initialisation
  test('should fetch session on init', () => {
    expect(sessionApiService.detail).toHaveBeenCalled();
  });

  // Test pour vérifier que la méthode detail() de TeacherService est appelée quand la session est récupérée
  test('should fetch teacher when session is fetched', () => {
    expect(teacherService.detail).toHaveBeenCalledWith(teacherId.toString());
    expect(component.teacher).toEqual(teacher);
  });

  // Test pour vérifier que la session est supprimée et que l'utilisateur est redirigé vers les sessions quand la méthode delete() est appelée
  test('should delete session and navigate to sessions', () => {
    component.delete();
    expect(sessionApiService.delete).toHaveBeenCalled();
    expect(matSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });

  // Test pour vérifier que la méthode participate() de SessionApiService est appelée quand la méthode participate() du composant est appelée
  test('should participate in session', () => {
    component.participate();
    expect(sessionApiService.participate).toHaveBeenCalled();
  });

  // Test pour vérifier que la méthode unParticipate() de SessionApiService est appelée quand la méthode unParticipate() du composant est appelée
  test('should unparticipate in session', () => {
    component.unParticipate();
    expect(sessionApiService.unParticipate).toHaveBeenCalled();
  });

  // Test pour vérifier que le bouton de suppression est affiché quand l'utilisateur est un administrateur
  it('should display delete button when user is admin', () => {
    component.isAdmin = true;
    fixture.detectChanges();
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="delete-button"]'));
    expect(deleteButton).toBeTruthy();
  });

  // Test pour vérifier que le bouton de suppression n'est pas affiché quand l'utilisateur n'est pas un administrateur
  it('should not display delete button when user is not admin', () => {
    component.isAdmin = false;
    fixture.detectChanges();
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="delete-button"]'));
    expect(deleteButton).toBeFalsy();
  });

  // Test pour vérifier que la méthode delete() est appelée quand le bouton de suppression est cliqué
  it('should call delete method when delete button is clicked', () => {
    component.isAdmin = true;
    fixture.detectChanges();
    jest.spyOn(component, 'delete');
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="delete-button"]')).nativeElement;
    deleteButton.click();
    expect(component.delete).toHaveBeenCalled();
  });
});