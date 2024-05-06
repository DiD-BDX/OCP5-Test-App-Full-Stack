// Importation des dépendances nécessaires pour les tests
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Module pour les tests HTTP
import { ComponentFixture, TestBed } from '@angular/core/testing'; // Outils de test Angular
import { expect } from '@jest/globals'; // Fonction d'assertion de Jest
import { SessionService } from 'src/app/services/session.service'; // Service pour la gestion des sessions
import { SessionApiService } from '../../services/session-api.service'; // Service pour l'API des sessions
import { FormComponent } from './form.component'; // Le composant à tester
import { FormBuilder, Validators } from '@angular/forms'; // Outils pour la création de formulaires
import { MatSnackBar } from '@angular/material/snack-bar'; // Service pour les snack-bars
import { ActivatedRoute, Router } from '@angular/router'; // Services pour le routage
import { of } from 'rxjs'; // Utilitaire RxJS pour créer des Observables
import { TeacherService } from '../../../../services/teacher.service'; // Service pour la gestion des enseignants

// Définition du groupe de tests pour le composant FormComponent
describe('FormComponent', () => {
  // Déclaration des variables pour le composant et son environnement de test
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpTestingController: HttpTestingController;
  let formBuilder: FormBuilder;

  // Création des mocks pour les services
  const sessionApiServiceMock = {
    detail: jest.fn().mockReturnValue(of({})), // Simule la méthode detail
    create: jest.fn().mockReturnValue(of({})), // Simule la méthode create
    update: jest.fn().mockReturnValue(of({})) // Simule la méthode update
  };

  const sessionServiceMock = {
    sessionInformation: { admin: true } // Simule l'information de session
  };

  const routerMock = {
    navigate: jest.fn(), // Simule la méthode navigate
    url: '/update' // Simule l'URL
  };

  // Configuration initiale avant chaque test
  beforeEach(async () => {
    // Configuration du module de test avec le composant à tester et les services mockés
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FormComponent],
      providers: [
        FormBuilder,
        MatSnackBar,
        { provide: ActivatedRoute, useValue: { snapshot: { paramMap: { get: () => '1' } } } },
        { provide: SessionApiService, useValue: sessionApiServiceMock },
        { provide: SessionService, useValue: sessionServiceMock },
        TeacherService,
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents(); // Compilation des composants

    // Injection des services
    httpTestingController = TestBed.inject(HttpTestingController);
    formBuilder = new FormBuilder();
  });

  // Création de l'environnement de test pour le composant avant chaque test
  beforeEach(() => {
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Vérification qu'il n'y a pas de requêtes HTTP en attente à la fin de chaque test
  afterEach(() => {
    httpTestingController.verify();
  });

  // Test pour vérifier que le composant est bien créé
  test('should create', () => {
    expect(component).toBeTruthy();
  });

  // Test d'intégration pour vérifier que le formulaire est initialisé pour la mise à jour
  test('should initialize form for update', () => {
    expect(sessionApiServiceMock.detail).toHaveBeenCalledWith('1');
    expect(component.onUpdate).toBe(true);
  });

  // Test d'intégration pour vérifier que le formulaire est soumis pour la création
  test('should submit form for create', () => {
    component.onUpdate = false;
    component.submit();
    expect(sessionApiServiceMock.create).toHaveBeenCalled();
  });

  // Test d'intégration pour vérifier que le formulaire est soumis pour la mise à jour
  test('should submit form for update', () => {
    component.onUpdate = true;
    component.submit();
    expect(sessionApiServiceMock.update).toHaveBeenCalled();
  });

  // Test pour vérifier que le formulaire est invalide quand un champ requis est manquant
  test('should be invalid when a required field is missing', () => {
    // Rendre le formulaire invalide
    const formBuilder = new FormBuilder();
    component.sessionForm = formBuilder.group({
      name: ['', Validators.required]
    });
    component.submit();
    expect(component.sessionForm?.valid).toBeFalsy();
  });
});